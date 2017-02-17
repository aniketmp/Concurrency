package introduction;

import java.util.*;

/*
    The concept of put if absent is straight forward enough-check to see if an element is in the collection before adding it, 
	and do not add it if it is already there.
	
    The requirement that  the  class  be  thread safe  implicitly  adds  another  requirement that operations like  put if absent  be  atomic .Any 
    reasonable interpretation suggests that, if you take a List that does not contain object X, and add X twice with put if 
    absent, the resulting collection contains only one copy of X .But,  if put if absent were not atomic, with some unlucky 
    timing two threads could both see that X was not present and both add X, resulting in two copies of X .
    
stage 1: Non thread safe Attempt to Implement Put if absent
    
 */
public class PutIfAbsent <E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<E>());

    //public synchronized boolean putIfAbsent(E x) {//stage 1
    public boolean putIfAbsent(E x) {//stage 2
        boolean absent = !list.contains(x);
        synchronized (list) { //stage 2
        	if (absent)
        		list.add(x);
        	return absent;
        }//stage 2
    }
}

/* consequences of stage 1:
Why  wouldn't  this  work?  After  all,  putIfAbsent  is  synchronized,  right?  The  problem  is  that  it  synchronizes  on  the 
wrong  lock . Whatever  lock  the  List  uses  to  guard  its  state,  it  sure  isn't  the  lock  on  the  PutIfAbsent Helper . PutIfAbsent Helper 
provides  only  the  illusion  of  synchronization;  the  various  list operations,  while  all synchronized,  use  different  locks, 
which means that putIfAbsent  is not atomic relative to other operations on the List .So there  is no guarantee that 
another thread won't modify the list while putIfAbsent is executing .

stage 2:To make this approach work, we have to use the same lock that the List uses by using client side locking or external 
locking .Client side  locking  entails guarding  client code that uses some object X with the  lock X uses to guard  its own 
state .In order to use client side locking, you must know what lock X uses .
 */
