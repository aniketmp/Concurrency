package introduction;
/*
 Small Note on Atomic Package
 Its a small toolkit of classes that support lock-free thread-safe programming on single variables. In essence, the classes in this
 package extend the notion of volatile values, fields, and array elements to those that also provide an atomic conditional 
 update operation of the form:
 boolean compareAndSet(expectedValue, updateValue);
 
*/
public class SequenceGenerator 
{
    private int value;

    /**
     * Returns a unique value.
     */
    //Stage 1:Unsafe sequence
    public int getNext() {
        return value++;
    }
  //Stage 2:Thread Safe sequence
    synchronized public int getNext2() {
        return value++;
    }
}
/*
Consequences of Stage 1:
 The problem with UnsafeSequence  is that with some unlucky  timing, two threads could  call getNext and receive the 
same value. The increment notation, nextValue++, may appear to be a single  operation, but  is  in  
fact three separate operations: 
1) Read the value
2) Add one to  it, and 
3) Write out the new  value.  
Since operations in multiple threads may be arbitrarily interleaved by the runtime, it is possible 
for two threads to read the value at the same  time, both see the same value, and then both 
add one to  it. The result  is that the same sequence  number is returned from multiple 
calls in different threads. 

In the absence of synchronization, the compiler, hardware, and runtime are allowed to take substantial  
liberties with the  timing  and  ordering  of  actions,  such  as  caching  variables  in  registers  
or  processor local  caches  where  they  are  temporarily  (or  even  permanently)  invisible  
to  other  threads.  These  tricks  are  in  aid  of  better  performance  and  are generally  desirable,
but  they  place  a  burden  on  the  developer  to  clearly  identify  where  data  is  being  
shared  across threads  so  that  these  optimizations  do  not  undermine  safety.

If  multiple  threads  access  the  same  mutable  state  variable  without  appropriate  synchronization,  your  program  is 
broken. There are three ways to fix it: 
1) Don't share the state variable across threads; 
2) Make the state variable immutable; or 
3) Use synchronization whenever accessing the state variable

A class is 'THREAD SAFE' when it continues to behave correctly when accessed from 
multiple threads. 

 */