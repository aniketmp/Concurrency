package introduction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ThreadPerTaskWebServer
 * <p/>
 * Web server that starts a new thread for each request
 *
 * @author Brian Goetz and Tim Peierls
 */
/*
 * ThreadPerTaskWebServer is similar in structure to the singleͲthreaded version -the main thread still alternates
between accepting an incoming connection and dispatching the request. The difference is that for each connection, the
main loop creates a new thread to process the request instead of processing it within the main thread. 
 */
public class ThreadPerTaskWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    handleRequest(connection);
                }
            };
            new Thread(task).start();
        }
    }

    private static void handleRequest(Socket connection) {
        // request-handling logic here
    }
}

/* Disadvantages of Unbounded Thread Creation
 * For production use, however, the threadͲperͲtask approach has some practical drawbacks, especially when a large
number of threads may be created:

Thread lifecycle overhead:
 	Thread creation and teardown are not free. The actual overhead varies across platforms, but
thread creation takes time, introducing latency into request processing, and requires some processing activity by the
JVM and OS. If requests are frequent and lightweight, as in most server applications, creating a new thread for each
request can consume significant computing resources.

Resource consumption:
 	Active threads consume system resources, especially memory. When there are more runnable
threads than available processors, threads sit idle. Having many idle threads can tie up a lot of memory, putting
pressure on the garbage collector, and having many threads competing for the CPUs can impose other performance
costs as well. If you have enough threads to keep all the CPUs busy, creating more threads won't help and may even
hurt.

Stability:
 	There is a limit on how many threads can be created. The limit varies by platform and is affected by factors
including JVM invocation parameters, the requested stack size in the Thread constructor, and limits on threads placed
by the underlying operating system. When you hit this limit, the most likely result is an OutOfMemoryError. trying to
recover from such an error is very risky; it is far easier to structure your program to avoid hitting this limit.
Up to a certain point, more threads can improve throughput, but beyond that point creating more threads just slows
down your application, and creating one thread too many can cause your entire application to crash horribly. The way
to stay out of danger is to place some bound on how many threads your application creates, and to test your application
thoroughly to ensure that, even when this bound is reached, it does not run out of resources.

The problem with the thread-per-task approach is that nothing places any limit on the number of threads created
except the rate at which remote users can throw HTTP requests at it. Like other concurrency hazards, unbounded
thread creation may appear to work just fine during prototyping and development, with problems surfacing only when
the application is deployed and under heavy load. So a malicious user, or enough ordinary users, can make your web
server crash if the traffic load ever reaches a certain threshold. For a server application that is supposed to provide high
availability and graceful degradation under load, this is a serious failing.
 */
