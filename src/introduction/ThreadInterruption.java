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
		interrupt();
	}
}