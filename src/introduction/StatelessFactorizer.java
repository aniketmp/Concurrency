package introduction;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
//stage 1:Currently the servlet is Stateless servlet, hence it's thread-safe.
public class StatelessFactorizer extends HttpServlet 
{
	//stage 2:Now let's add 'hit count' that measures the number of request proceed.
	private long count=0;
	
	//stage3:Lets fix the issue occurred in stage 2.
	private final AtomicLong count2 = new AtomicLong(0);
	
	public void service(ServletRequest req, ServletResponse resp) 
	{
		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = factor(i);
		//stage 2
		++count;
		//stage 3
		count2.incrementAndGet();
		
		encodeIntoResponse(resp, factors);
	}

	private BigInteger[] factor(BigInteger i) {
		return null;
	}

	private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		return null;
	}

}
/*
 *Stateless objects are always threadsafe.
 *
 Unfortunately, stage 2 is not thread safe, even though it would work just fine in a single threaded 
environment.  Just  like  UnsafeSequence  on  page  6,  it  is  susceptible  to  lost  updates.  
While  the  increment  operation, ++count, may  look  like a  single  action  because  of  its compact  
syntax,  it  is not  atomic, which means  that  it  does  not execute as a single, indivisible operation. Instead, it is shorthand for a sequence of three discrete operations: fetch the 
current value, add one to it, and write the new value back. This is an example of a read-modify-write 
operation, in which the resulting state is derived from the previous state.

 stage 3:The java.util.concurrent.atomic package contains atomic variable classes for effecting atomic state transitions on 
numbers  and  object  references.  By  replacing  the  long  counter  with  an  AtomicLong, we  ensure  that  all  actions  that 
access the counter state are atomic. [5] Because the state of the servlet is the state of the counter and the counter is 
thread safe, our servlet is once again thread safe.  


 */
