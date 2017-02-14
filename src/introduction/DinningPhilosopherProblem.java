package introduction;
//uncompleted.... 
class Philosopher implements Runnable
{
	boolean isHungry=true;
	String name;	
	Fork left;
	Fork right;	
	public Philosopher(String name) 
	{
		this.name=name;
	}
	@Override
	public void run() 
	{				
		while(true)
		{
			eat();
		}
	}
	public synchronized void eat()
	{
		System.out.println("Philosopher "+name+" is eating with fork "+left.no+" & fork "+right.no);
		if(isHungry && left.isAvailable && right.isAvailable)
		{			
			left.isAvailable=false;
			right.isAvailable=false;			
			try {
				Thread.sleep((long)(3000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			left.isAvailable=true;
			right.isAvailable=true;
			isHungry=false;
		}		
		else if(isHungry==false)
		{
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isHungry=true;
		}
		
	}
}

class Fork 
{
	boolean isAvailable=true;
	int no;
	public Fork(int no) 
	{
		this.no=no;
	}
}
public class DinningPhilosopherProblem 
{
	Fork[] forks=new Fork[4];
	Philosopher[] philosophers=new Philosopher[4];
	public DinningPhilosopherProblem() {
		philosophers[0]=new Philosopher("A");
		philosophers[1]=new Philosopher("B");
		philosophers[2]=new Philosopher("C");
		philosophers[3]=new Philosopher("D");
		
		forks[0]=new Fork(1);
		forks[1]=new Fork(2);
		forks[2]=new Fork(3);
		forks[3]=new Fork(4);
		
		philosophers[0].left=forks[0];
		philosophers[1].right=forks[0];
		
		philosophers[1].left=forks[1];
		philosophers[2].right=forks[1];
		
		philosophers[2].left=forks[2];
		philosophers[3].right=forks[2];
		
		philosophers[3].left=forks[3];
		philosophers[0].right=forks[3];
		philosophers[1].right=forks[0];
	}
	
	public static void main(String[] args) 
	{
		DinningPhilosopherProblem d=new DinningPhilosopherProblem();
		System.out.println("starting...");
		for(int i=0;i<d.philosophers.length;i++)
		{
			new Thread(d.philosophers[i]).start();
		}
		

		
	}
}
