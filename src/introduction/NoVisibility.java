package introduction;

public class NoVisibility 
{
	private static boolean ready;
	private static int number;

	private static class ReaderThread extends Thread {
		public void run() 
		{
			while (!ready)
			{
				Thread.yield();
			}
				
			System.out.println(number);
		}
	}

	public static void main(String[] args) {
		new ReaderThread().start();
		number = 42;
		ready = true;
	}
}

/*
 NOTE:While it may seem obvious that NoVisibility will print 42, it is in fact possible that it will print zero, or never terminate at all!
  Because it does not use adequate synchronization, there is no guarantee that the values of ready and number written by the
  main thread will be visible to the reader thread.
  
  NoVisibility could loop forever because the value of ready might never become
visible to the reader thread. Even more strangely, NoVisibility could print
zero because the write to ready might be made visible to the reader thread before
the write to number, a phenomenon known as reordering. There is no guarantee
that operations in one thread will be performed in the order given by the program,
as long as the reordering is not detectable from within that thread—even
if the reordering is apparent to other threads.1 When the main thread writes first to
number and then to ready without synchronization, the reader thread could see
those writes happen in the opposite order—or not at all.

This may seem like a broken design, but it is meant to allow JVMs to take full advantage of the
performance of modern multiprocessor hardware. For example, in the absence of synchronization,
the Java Memory Model permits the compiler to reorder operations and cache values in registers, and
permits CPUs to reorder operations and cache values in processor-specific caches
 */