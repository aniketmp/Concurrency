package introduction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/*
 * The  standard  way  to iterate a Collection is with an Iterator, either explicitly or through the for each loop syntax introduced 
in Java 5.0,  but using  iterators does not obviate the need to lock the  collection during  iteration  if other threads can concurrently 
modify it .The iterators returned by the synchronized collections are not designed to deal with concurrent modification, 
and they are fail fast   meaning that if they detect that the collection has changed since iteration began, they throw the 
unchecked ConcurrentModificationException .
 * 
 * These fail fast iterators are not designed to be foolproof   they are designed to catch concurrency errors on a "good 
faith effort"  basis and  thus  act  only  as  early warning  indicators  for  concurrency  problems . They  are implemented  by 
associating a modification count with the collection: if the modification count changes during iteration, hasNext or next 
throws ConcurrentModificationException .However, this check is done without synchronization, so there is a risk of 
seeing a stale value of the modification count and therefore that the iterator does not realize a modification has been 
made .This was a deliberate design tradeoff to reduce the performance impact of the concurrent modification detection 
code.[
 */

class MyThread1 extends Thread
{
	List list;
	public MyThread1(List list) {
		this.list=list;
	}
	@Override
	public  void run() {
		/* stage 1:
		 * Although this list is synchronized ,it throws concurrent modification exception.Because each methods in ArrayList is not threadsafe. 
		   Thread 1 can call delete and thread 2 can iterate over it.And since it is not happening concurrently, Its throwing exception. 
		   Here although it looks like we are holding lock for entire the iteration period, this is not true.Lock is on list not 
		   an set of code.Hence once you call any method in the list for that period non other thread can modify it but as 
		   soon as method gets finished other thread can modify same list.
		   *Solution for this is Vector.Which holds a lock for entire duration while iteration.
		 */
		 
		synchronized(list)  //no effect with without vector
		{
			Iterator itr=list.iterator();
		
			while(itr.hasNext())
			{			
				Integer i=(Integer) itr.next();
				System.out.println(getName()+" i:"+i);			
				
			}
		}
		
	}
}
class MyThread2 extends Thread
{
	List list;
	public MyThread2(List list) {
		this.list=list;
	}
	@Override
	public void run() {
		Iterator itr=list.iterator();
		while(itr.hasNext())
		{		
			Integer i=(Integer) itr.next();
			System.out.println(getName()+" i:"+i);
			if(i.equals(new Integer(1)))
			{				
				System.out.println(getName()+" removing");
				itr.remove();
			}
				
			
		}
		
	}
}
public class ConcurrentModificationExceptionDemo {

	
	public static void main(String[] args) {
		ArrayList<Integer> list2=new ArrayList<>(); //stage 1:
		List list=Collections.synchronizedList(list2); //stage 1 coninued..This also can't solve problem.
//		Vector<Integer> list=new Vector<>(); //stage 2:
		
		
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		list.add(9);
		list.add(10);
		list.add(11);
		
		Thread t1=new MyThread1(list);
		t1.setName("T1");
		Thread t2=new MyThread2(list);
		t2.setName("       T2");
		t1.start();
		t2.start();
	}

}
