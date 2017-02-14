package introduction;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;

//stage 1:This servlet caches most recently computed results.Here We used  AtomicReference to manage the last number and its factors
public class CachingFactorizer extends HttpServlet{
	private final AtomicReference<BigInteger> lastNumber = new AtomicReference<BigInteger>();
	private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<BigInteger[]>();
	//Here we are storing the request in lastNumber-->lastFactors (key-value) type form.Hence we are checking first whether 
	//lastNumber is same as current ,if yes then retrieving lastFactors.
	
	//Stage 3:Here AtomicReference is not needed, since we are already using synchronized blocks to construct atomic operations, 
	//using two different synchronization mechanisms would be confusing and would offer no performance or safety benefit. 
	private BigInteger lastNumber2;
	private BigInteger[] lastFactors2;
	
	synchronized public void service(ServletRequest req, ServletResponse resp) {//stage 2
		BigInteger i = extractFromRequest(req);
		 
		if (i.equals(lastNumber.get()))
		{
			encodeIntoResponse(resp, lastFactors.get());
		}
		else 
		{
			BigInteger[] factors = factor(i);//Here we may get confuse because still the two threads can override the factors value. But its not possible, since factors is local variable, every thread has a own copy of it. 
			lastNumber.set(i);
			lastFactors.set(factors);			
			encodeIntoResponse(resp, factors);
		}
	}
	//stage 3
	public void service2(ServletRequest req, ServletResponse resp) 
	{
		BigInteger i = extractFromRequest(req);
		 
		if (i.equals(lastNumber2))
		{
			encodeIntoResponse(resp, lastFactors2);
		}
		else 
		{
			BigInteger[] factors = factor(i);
			synchronized(this)//We are using synchrozied block only when it is necessary
			{
				lastNumber2=i;
				lastFactors2=factors;
			}						
			encodeIntoResponse(resp, factors);
		}
	}

	
	private BigInteger[] factor(BigInteger i) {
		// TODO Auto-generated method stub
		return null;
	}

	private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
		// TODO Auto-generated method stub
		
	}

	private BigInteger extractFromRequest(ServletRequest req) {
		// TODO Auto-generated method stub
		return null;
	}
}
/*
 *Consequences of stage 1:
 
Unfortunately, this approach does not work. Even though the atomic references
 are individually thread-safe, UnsafeCachingFactorizer has race conditions
 that could make it produce the wrong answer.
 
 
 stage 2:Making the whole method synchronized.
 
 CachingFactorizer is now thread-safe; however, this approach is fairly extreme, since it inhibits multiple clients from using the 
 factoring servlet simultaneously at all—resulting in unacceptably poor responsiveness. This problem—which is a performance problem,
 not a thread safety problem—is addressed in next Step.
	Because service is synchronized, only one thread may execute it at once. This subverts the intended use of the servlet 
framework—that servlets be able to handle multiple requests simultaneously—and can result in frustrated users if the
 load is high enough. If the servlet is busy factoring a large number, other clientshave to wait until the current request is complete 
 before the servlet can start on the new number. If the system has multiple CPUs, processors may remain idle
 even if the load is high. In any case, even short-running requests, such as those for which the value is cached, 
 may take an unexpectedly long time because they must wait for previous long-running requests to complete.
 *
 stage 3:Use synchronized block only when necessary.
 */
