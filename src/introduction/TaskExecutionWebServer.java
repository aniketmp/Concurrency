package introduction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * TaskExecutionWebServer
 * <p/>
 * Web server using a thread pool
 *
 * @author Brian Goetz and Tim Peierls
 */
/*
 * and java.util.concurrent provides a flexible thread
pool implementation as part of the Executor framework. The primary abstraction for task execution in the Java class
libraries is not Thread, but Executor..
public interface Executor {
 void execute(Runnable command);
} 

Executor may be a simple interface, but it forms the basis for a flexible and powerful framework for asynchronous task
execution that supports a wide variety of task execution policies. It provides a standard means of decoupling task
submission from task execution, describing tasks with Runnable. The Executor implementations also provide lifecycle
support and hooks for adding statistics gathering, application management, and monitoring.
Executor is based on the producerͲconsumer pattern, where activities that submit tasks are the producers (producing
units of work to be done) and the threads that execute tasks are the consumers (consuming those units of work). Using
an Executor is usually the easiest path to implementing a producer-consumer design in your application.
 */
public class TaskExecutionWebServer {
    private static final int NTHREADS = 100;
    private static final Executor exec
            = Executors.newFixedThreadPool(NTHREADS);
    		//step 2:
    		//=new ThreadPerTaskExecutor(); //We can easily modify TaskExecutionWebServer to behave like ThreadPer-TaskWebServer
    		//=new WithinThreadExecutor(); //Similarly, it is also easy to write an Executor that would make TaskExecutionWebServer behave like the single-threaded version
    
    //The value of decoupling submission from execution is that it lets you easily specify, and subsequently change without great difficulty, the execution policy for a given class of tasks
    
    /*
     . An execution policy specifies:
     
     * In what thread will tasks be executed?
	 * In what order should tasks be executed (FIFO, LIFO, priority order)?
     * How many tasks may execute concurrently?
     * How many tasks may be queued pending execution?
     * If a task has to be rejected because the system is overloaded, which task should be selected as the victim, and
		how should the application be notified?
	 * What actions should be taken before or after executing a task?
	 
	 	Execution policies are a resource management tool, and the optimal policy depends on the available computing
	resources and your quality-of-service requirements
     */
    
    
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    handleRequest(connection);
                }
            };
            exec.execute(task);
        }
    }

    private static void handleRequest(Socket connection) {
        // request-handling logic here
    }
}

/*	step 1:
 * Building a web server with an Executor is easy. TaskExecutionWebServer replaces the hardͲcoded thread
creation with an Executor. In this case, we use one of the standard Executor implementations, a fixedͲsize thread pool
with 100 threads.
In TaskExecutionWebServer, submission of the request-handling task is decoupled from its execution using an
Executor, and its behavior can be changed merely by substituting a different Executor implementation. Changing
Executor implementations or configuration is far less invasive than changing the way tasks are submitted; Executor
configuration is generally a oneͲtime event and can easily be exposed for deployment-time configuration, whereas task
submission code tends to be strewn throughout the program and harder to expose.

step 2:
We can easily modify TaskExecutionWebServer to behave like ThreadPer-TaskWebServer by substituting an Executor
that creates a new thread for each request. Writing such an Executor is trivial, as shown in ThreadPerTaskExecutor

Similarly, it is also easy to write an Executor that would make TaskExecutionWebServer behave like the single-
threaded version, executing each task synchronously before returning from execute, as shown in
WithinThreadExecutor
 */
class ThreadPerTaskExecutor implements Executor {
	 public void execute(Runnable r) {
	 new Thread(r).start();
	 };
	} 

class WithinThreadExecutor implements Executor {
	 public void execute(Runnable r) {
	 r.run();
	 };
	}