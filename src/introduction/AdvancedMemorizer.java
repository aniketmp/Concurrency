package introduction;

/*
 * continued from stage 2 i.e Memorizer.java
 * stage 3 (AdvancedMemorizer) redefines the backing Map for the value cache as a ConcurrentHashMap<A,Future<V>> instead of a 
	ConcurrentHashMap<A,V>. stage 3 first checks to see if the appropriate calculation has been started
 	(as opposed to finished, as in stage 2). If not, it creates a FutureTask, registers it in the Map, and starts the
	computation; otherwise it waits for the result of the existing computation. The result might be available immediately or
	might be in the process of being computedͲbut this is transparent to the caller of Future.get.
	The stage 3 implementation is almost perfect: it exhibits very good concurrency (mostly derived from the excellent
	concurrency of ConcurrentHashMap), the result is returned efficiently if it is already known, and if the computation is in
	progress by another thread, newly arriving threads wait patiently for the result. 
	
	Problem in stage 3:
	There is still a small window of vulnerability in which two threads might compute the same value. This window is far smaller than in
	stage 2, but because the if block in compute is still a non-atomic check-then-act sequence, it is possible for two
	threads to call compute with the same value at roughly the same time, both see that the cache does not contain the
	desired value, and both start the computation.
	
	stage 3 is vulnerable to this problem because a compound action (put-if-absent) is performed on the backing map
	that cannot be made atomic using locking. 
	
	Stage 4:Final memorizer
	Stage 4 takes advantage of the atomic putIfAbsent
	method of ConcurrentMap, closing the window of vulnerability in stage 3.
	Caching a Future instead of a value creates the possibility of cache pollution: if a computation is cancelled or fails,
	future attempts to compute the result will also indicate cancellation or failure. To avoid this, Memorizer removes the
	Future from the cache if it detects that the computation was cancelled; it might also be desirable to remove the Future
	upon detecting a RuntimeException if the computation might succeed on a future attempt. Memorizer also does not
	address cache expiration, but this could be accomplished by using a subclass of FutureTask that associates an
	expiration time with each result and periodically scanning the cache for expired entries. (Similarly, it does not address
	cache eviction, where old entries are removed to make room for new ones so that the cache does not consume too
	much memory.)
 */
import java.util.*;
import java.util.concurrent.*;

/**
 * Memoizer2
 * <p/>
 * Replacing HashMap with ConcurrentHashMap
 *
 * @author Brian Goetz and Tim Peierls
 */
public class AdvancedMemorizer <A, V> implements Computable<A, V> 
{
    private final ConcurrentMap<A, Future<V>> cache
            = new ConcurrentHashMap<A, Future<V>>();
    private final Computable<A, V> c;

    public AdvancedMemorizer(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(final A arg) throws InterruptedException 
    {
        while (true) {
            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable<V> eval = new Callable<V>() {
                    public V call() throws InterruptedException {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<V>(eval);
                //f = cache.put(arg, ft); //stage 3
                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                cache.remove(arg, f); //stage 4
            } catch (ExecutionException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }
}