package introduction;


import java.util.*;
import java.util.concurrent.*;


/**
 * FutureRenderer
 * <p/>
 * Waiting for image download with \Future
 *
 * @author Brian Goetz and Tim Peierls
 */
/*
 * The Executor framework uses Runnable as its basic task representation. Runnable is a fairly limiting abstraction; run
cannot return a value or throw checked exceptions, although it can have side effects such as writing to a log file or
placing a result in a shared data structure.
Many tasks are effectively deferred computations-executing a database query, fetching a resource over the network,
or computing a complicated function. For these types of tasks, Callable is a better abstraction: it expects that the main
entry point, call, will return a value and anticipates that it might throw an exception.
 */
public abstract class FutureRenderer {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task =
                new Callable<List<ImageData>>() {
                    public List<ImageData> call() {
                        List<ImageData> result = new ArrayList<ImageData>();
                        for (ImageInfo imageInfo : imageInfos)
                            result.add(imageInfo.downloadImage());
                        return result;
                    }
                };

        Future<List<ImageData>> future = executor.submit(task);
        renderText(source);

        try {
            List<ImageData> imageData = future.get();
            for (ImageData data : imageData)
                renderImage(data);
        } catch (InterruptedException e) {
            // Re-assert the thread's interrupted status
            Thread.currentThread().interrupt();
            // We don't need the result, so cancel the task too
            future.cancel(true);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    interface ImageData {
    }

    interface ImageInfo {
        ImageData downloadImage();
    }

    abstract void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    abstract void renderImage(ImageData i);
}
/*
 *As a first step towards making the page renderer more concurrent, let's divide it into two tasks, one that renders the
text and one that downloads all the images. (Because one task is largely CPUͲbound and the other is largely I/OͲbound,
this approach may yield improvements even on singleͲCPU systems.)
Callable and Future can help us express the interaction between these cooperating tasks. In FutureRenderer in
, we create a Callable to download all the images, and submit it to an ExecutorService. This returns a
Future describing the task's execution; when the main task gets to the point where it needs the images, it waits for the
result by calling Future.get. If we're lucky, the results will already be ready by the time we ask; otherwise, at least we
got a head start on downloading the images.
The stateͲdependent nature of get means that the caller need not be aware of the state of the task, and the safe
publication properties of task submission and result retrieval make this approach threadͲsafe. The exception handling
code surrounding Future.get deals with two possible problems: that the task encountered an Exception, or the thread
calling get was interrupted before the results were available. 
FutureRenderer allows the text to be rendered concurrently with downloading the image data. When all the images
are downloaded, they are rendered onto the page. This is an improvement in that the user sees a result quickly and it
exploits some parallelism, but we can do considerably better. There is no need for users to wait for all the images to be
downloaded; they would probably prefer to see individual images drawn as they become available. 
 */
