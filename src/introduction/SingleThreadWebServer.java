package introduction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * There are a number of possible policies for scheduling tasks within an application, some of which exploit the potential
for concurrency better than others. The simplest is to execute tasks sequentially in a single thread. SingleThreadWebServer
 processes its tasks ͲHTTP requests arriving on port 80 -sequentially. The details of the request
processing aren't important; we're interested in characterizing the concurrency of various scheduling policies.

 */

public class SingleThreadWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            Socket connection = socket.accept();
            handleRequest(connection);
        }
    }

    private static void handleRequest(Socket connection) {
        // request-handling logic here
    }
}
/*

 SingleThreadedWebServer is simple and theoretically correct, but would perform poorly in production because it can
handle only one request at a time. The main thread alternates between accepting connections and processing the
associated request. While the server is handling a request, new connections must wait until it finishes the current
request and calls accept again. This might work if request processing were so fast that handleRequest effectively
returned immediately, but this doesn't describe any web server in the real world.
Processing a web request involves a mix of computation and I/O. The server must perform socket I/O to read the
request and write the response, which can block due to network congestion or connectivity problems. It may also
perform file I/O or make database requests, which can also block. In a singleͲthreaded server, blocking not only delays
completing the current request, but prevents pending requests from being processed at all. If one request blocks for an
unusually long time, users might think the server is unavailable because it appears unresponsive. At the same time,
resource utilization is poor, since the CPU sits idle while the single thread waits for its I/O to complete.
In server applications, sequential processing rarely provides either good throughput or good responsiveness. There are
exceptionsͲsuch as when tasks are few and longͲlived, or when the server serves a single client that makes only a single
request at a timeͲbut most server applications do not work this way.

A more responsive approach is to create a new thread for servicing each request, as shown in
ThreadPerTaskWebServer
 */
