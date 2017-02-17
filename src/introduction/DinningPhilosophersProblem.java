package introduction;



class Status
{
	public static final int EATING=0;
	public static final int THINKING=1;
	public static final int OCCUPIED=0;
	public static final int FREE=1;
}
class Philosopher implements Runnable
{
	int status=1;
	Fork left;
	Fork right;
	String name;
	
	public Philosopher(String name) {
		super();
		this.name = name;
	}
	public void eat()
	{
		while(true)
		{
			if(left.status==Status.FREE && right.status==Status.FREE)
			{				
				synchronized(left)
				{					
					synchronized(right)
					{
						left.status=Status.OCCUPIED;
						right.status=Status.OCCUPIED;
						System.out.println(name+" is Eating with "+left.name+" & "+right.name+"..");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
				left.status=Status.FREE;
				right.status=Status.FREE;				
			}
			think();
		}
			
		
	}
	public void think()
	{
		System.out.println(name+" is Thinking");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() 
	{
		eat();
		think();
		
	}
}
class Fork 
{
	int status=1;
	String name;
	public Fork(String name) {
		this.name=name;
	}
}

public class DinningPhilosophersProblem {

	public static void main(String[] args) {
		Fork fork1=new Fork("Fork1");
		Philosopher ph1=new Philosopher("Philosopher 1");
		Philosopher ph2=new Philosopher("Philosopher 2");
		ph1.right=ph2.left=fork1;
		
		Fork fork2=new Fork("Fork2");
		Philosopher ph3=new Philosopher("Philosopher 3");
		fork2=ph2.right=ph3.left=fork2;
		
		Fork fork3=new Fork("Fork3");
		Philosopher ph4=new Philosopher("Philosopher 4");
		fork3=ph3.right=ph4.left=fork3;
		
		Fork fork4=new Fork("Fork4");
		Philosopher ph5=new Philosopher("Philosopher 5");
		fork4=ph4.right=ph5.left=fork4;
		
		Fork fork5=new Fork("Fork5");
		ph5.right=ph1.left=fork5;
		
		new Thread(ph1).start();
		new Thread(ph2).start();
		new Thread(ph3).start();
		new Thread(ph4).start();
		new Thread(ph5).start();
		
	}
	
	
}
