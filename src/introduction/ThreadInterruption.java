package introduction;

public class ThreadInterruption 
{
	public static void main(String[] args) throws InterruptedException 
	{
		MyThread t=new MyThread();
		t.setName("MyThread");
		t.start();
		Thread.currentThread().sleep(1000);
		System.out.println("Interrupting...");
		t.cancel();
	}
	
}
class MyThread extends Thread
{
	@Override
	public void run() {
		int i=0;
		boolean loop=true;
		while(loop)
		{
			try {
				System.out.println("Sleeping...");
				//if(1==2)  			//If the thread is not holding lock then it cannot be interrupted.
					sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Interrupted by "+Thread.currentThread().getName());
				loop=false;
				
			}
		}		
	}
	public void cancel()
	{
		interrupt(); //interrupt method interrupts the target thread, and isInterrupted returns the interrupted status of the target thread.
					 //The poorly named static interrupted method clears the interrupted status of the current thread and returns its
		             //previous value; this is the only way to clear the interrupted status. 
	}
}