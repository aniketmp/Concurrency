package introduction;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

/*
 *step 1:In this section we develop an efficient and scalable result cache for a computationally expensive function.
	Let's start with the obvious approach ?a simple HashMapand then look at some of its concurrency disadvantages and
	how to fix them. The Computable<A,V> interface in describes a function with input of type A and result of type V.
	ExpensiveFunction, which implements Computable, takes a long time to compute its result; we'd like to create a
	Computable wrapper that remembers the results of previous computations and encapsulates the caching process. (This
	technique is known as Memorization.)
	
	Memorizer1 in Listing 5.16 shows a first attempt: using a HashMap to store the results of previous computations. The
	compute method first checks whether the desired result is already cached, and returns the preͲcomputed value if it is.
	Otherwise, the result is computed and cached in the HashMap before returning.
	
  problem of stage 1:
  	HashMap is not threadͲsafe, so to ensure that two threads do not access the HashMap at the same time, Memorizer1
	takes the conservative approach of synchronizing the entire compute method. This ensures thread safety but has an
	obvious scalability problem: only one thread at a time can execute compute at all. If another thread is busy computing a
	result, other threads calling compute may be blocked for a long time. If multiple threads are queued up waiting to
	compute values not already computed, compute may actually take longer than it would have without Memorization.
	
 stage 2:stage 2 improves on the awful concurrent behavior of stage 1 by replacing the HashMap with a
	ConcurrentHashMap. Since ConcurrentHashMap is threadͲsafe, there is no need to synchronize when accessing the
	backing Map, thus eliminating the serialization induced by synchronizing compute in stage 1.
	
	stage 2 certainly has better concurrent behavior than stage 1: multiple threads can actually use it
	concurrently. But it still has some defects as a cache -there is a window of vulnerability in which two threads calling
	compute at the same time could end up computing the same value. In the case of memorization, this is merely
	inefficient -the purpose of a cache is to prevent the same data from being calculated multiple times. For a more
	general-purpose caching mechanism, it is far worse; for an object cache that is supposed to provide once-and-only-once
	initialization, this vulnerability would also pose a safety risk.
	
	Problem of stage 2:
	The problem with stage 2 is that if one thread starts an expensive computation, other threads are not aware that
	the computation is in progress and so may start the same computation. We'd like to
	somehow represent the notion that "thread X is currently computing f (27)", so that if another thread arrives looking for
	f (27), it knows that the most efficient way to find it is to head over to Thread X's house, hang out there until X is
	finished, and then ask "Hey, what did you get for f (27)?"

	idea behind stage 3:
	We've already seen a class that does almost exactly this: FutureTask. FutureTask represents a computational process
	that may or may not already have completed. FutureTask.get returns the result of the computation immediately if it is
	available; otherwise it blocks until the result has been computed and then returns it.
	
	stage 3: Stage 3 is present in AdvancedMemorizer class.
	to be continued..
 */
public class Memorizer <A, V> implements Computable<A, V> {
	
    //private final Map<A, V> cache = new HashMap<A, V>(); //stage 1
	private final Map<A, V> cache = new ConcurrentHashMap<A, V>(); //stage 2
	
    private final Computable<A, V> c;

    public Memorizer(Computable<A, V> c) 
    {
        this.c = c;
    }

    //public synchronized V compute(A arg) throws InterruptedException // stage 1
    public V compute(A arg) throws InterruptedException
    {
        V result = cache.get(arg);
        if (result == null)
        {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}


interface Computable <A, V> {
    V compute(A arg) throws InterruptedException;
}

class ExpensiveFunction implements Computable<String, BigInteger> 
{
    public BigInteger compute(String arg) 
    {
        // after deep thought...
        return new BigInteger(arg);
    }
}