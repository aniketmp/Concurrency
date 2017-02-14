package introduction;

public class MutableInteger 
{
	private int value;
	public synchronized int get()//stage 2 
	{ 
		return value; 
	}
	public synchronized void set(int value)//stage 2 
	{ 
		this.value = value; 
	}
}
/*
 stage 1:MutableInteger  is not thread-safe because the value field is
accessed from both get and set without synchronization. Among other hazards,
it is susceptible to stale values: if one thread calls set, other threads calling get
may or may not see that update. We can make MutableInteger thread safe by synchronizing the getter and
setter as shown in SynchronizedInteger in step 2. Synchronizing only the
setter would not be sufficient: threads calling get would still be able to see stale values.

Note:Reading data without synchronization is analogous to using the READ_UNCOMMITTED isolation level
in a database, where you are willing to trade accuracy for performance. However, in the case of
unsynchronized reads, you are trading away a greater degree of accuracy, since the visible value for a
shared variable can be arbitrarily stale.

When a thread reads a variable without synchronization, it may see a stale value,
but at least it sees a value that was actually placed there by some thread rather
than some random value. This safety guarantee is called out-of-thin-air safety.
Out-of-thin-air safety applies to all variables, with one exception: 64-bit numeric
variables (double and long) that are not declared volatile. The Java Memory Model requires fetch and store operations to be atomic,
but for nonvolatile long and double variables, the JVM is permitted to treat a
64-bit read or write as two separate 32-bit operations. If the reads and writes
occur in different threads, it is therefore possible to read a nonvolatile long and
get back the high 32 bits of one value and the low 32 bits of another(When the Java Virtual Machine Specification was written, many widely used processor architectures
could not efficiently provide atomic 64-bit arithmetic operations.). Thus, even
if you don’t care about stale values, it is not safe to use shared mutable long and
double variables in multithreaded programs unless they are declared volatile
or guarded by a lock.
*/