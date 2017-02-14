package introduction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Immutable object means it's state cannot be changed once it's created.

public class ImmutablityCheck 
{
	


	private int id;
	private String name; 
	private String isPrivateVariableImmutable; //Still in one scenario this variable value may change. See point 4.
	private final Address addr;  //Sometimes private final also may not work. See point 5.
	private final List<Point> points;
	
	
	class ImmutabilityBreak2
	{
		public void breakPrivateVariableMutability(String change)
		{
			isPrivateVariableImmutable=change;
		}
	}
	

	/*
	  1)To make the class immutable, don't keep default constructor.Let client create object of this using parameterized constructor
	    to ensure all the instance variable values are assigned to it while object creation.
	  2)As far as possible, don't give setter method. So that no object outside this class can change its value. 
	  3)Ensure that no class would break its encapsulation by making instance variable private.
	  4)Still private variable can be changed "IF THERE IS INNER CLASS (ImmutabilityBreak2) WHICH MANIPULATES YOUR PRIVATE INSTANCE VARIABLE (isPrivateVariableImmutable)".
	    The solution for such situation is make those variable final.  
	  5)If instance variables are primitives or String then steps 1-4 solves your problem, but if some instance variables are reference then
	    you have to take extra care to make your object immutable. In case of String reference, we don't have to take care since String is 
	    already immutable.
	    There are two solutions addressing this issue.
	    a)Make address Cloneable and implement clone method for creating deep copy and then return that copy instead of original object. But if you don't have source code of this class then 
	      make the child of it and make it Cloneable.
	    b)If that class is final then simply create new object from your method and copy all the fields from mutable object and then return copy 
	      as you can see it getImmutableAddress method.
	  6)Still steps 1-5 wont work in case if we have a list of immutable objects like List<Point>. Because since mutability of class 
	    depends upon the mutability of instance variables and if ANY OF THE INSTANCE VARIABLE IS COLLECTION THEN ITS MUTABILITY DEPENDS UPON 
	    ALL THE ITEMS IN THE COLLECTIONS AS WELL. Hence only coping the state won't work here in case of collection as you can see in 
	    getImmutableList. 
	  7)In order to solve the problem we faced in step 6 we have to perform deep copy of all the collection as we can see in get
	   getImmutableModifiedList.          
	    
	*/
	public ImmutablityCheck(int id,String name)
	{
		this.id=id;
		this.name=name;
		this.isPrivateVariableImmutable="defalt";//Assigning default value.
		addr=new Address();
		addr.setStreetName("Swami Vivekananda Street");
		addr.setCity("Mumbai");
		
		points=new ArrayList<>();
		Point p=new Point();
		p.setX(10);
		p.setY(20);
		points.add(p);
		
		p=new Point();
		p.setX(100);
		p.setY(200);
		
		points.add(p);
		
		
	}
	
	public int getId() {
		return id; //you can return this directly since PRIMITIVES ARE PASS BY VALUE.
	}

	public String getName() {
		return name; //Since String is immutable, hence we can simply return it's reference from the method
	}
	public ImmutabilityBreak2 getImmutabilityBreak2Instance()
	{
		return new ImmutabilityBreak2();
	}
	public Address getAddress()
	{
		return this.addr;
	}
	public Address getImmutableAddress()
	{
		Address address=new Address();
		address.setStreetName(this.getAddress().getStreetName());
		address.setCity(this.getAddress().getCity());
		return address;
	}
	public List<Point> getImmutableList()
	{
		List<Point> points=new ArrayList<>();
		Iterator<Point> itr=this.points.iterator();
		while(itr.hasNext())
		{
			points.add(itr.next());
		}
		return points;
	}
	public List<Point> getImmutableModifiedList()
	{
		List<Point> points=new ArrayList<>();
		Iterator<Point> itr=this.points.iterator();
		while(itr.hasNext())
		{
			try {
				points.add((Point) itr.next().clone()); //Here we are not adding original point, instead adding clone of object.
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return points;
	}

	public static void main(String[] args) 
	{
		ImmutablityCheck im=new ImmutablityCheck(1, "aniket");
		String name=im.getName();
		System.out.println("name:"+name);
		name="abc";
		System.out.println("name:"+im.getName()); //string is immutable.
		
		System.out.println("before:"+im.isPrivateVariableImmutable);
		ImmutabilityBreak2 ib=im.getImmutabilityBreak2Instance();
		ib.breakPrivateVariableMutability("not immutable!");
		System.out.println("after:"+im.isPrivateVariableImmutable);
		
		
		Address address=im.getAddress();
		System.out.println("addr:"+address);
		System.out.println("Although Address is private and final , it is mutable.");
		address.setStreetName("pqr");
		address.setCity("xyz");
		System.out.println("addr:"+im.getAddress());
		
		address.setStreetName("Swami Vivekananda Street");
		address.setCity("Mumbai");
		System.out.println("---using cloning technique--");
		System.out.println("before addr:"+im.getImmutableAddress());
		address=im.getImmutableAddress();
		address.setStreetName("pqr");
		address.setCity("xyz");
		System.out.println("after addr:"+im.getImmutableAddress());
		
		List<Point> points=im.getImmutableList();
		System.out.println("points:"+points);
		System.out.println("Making point mutable");
		Point p=points.get(0);
		p.setX(40);
		
		System.out.println("points:"+im.getImmutableList());
		p.setX(10); //reverting
		
		System.out.println("----Immutable in all sence----");
		
		List<Point> points2=im.getImmutableModifiedList();
		System.out.println("before: points:"+im.getImmutableModifiedList());
		Point p2=points2.get(0);
		p2.setX(40);
		
		System.out.println("before: points:"+im.getImmutableModifiedList());
		
		
		
		
	}
}
class ImmutabilityBreak extends ImmutablityCheck
{

	public ImmutabilityBreak(int id, String name) 
	{
		super(id, name);		
	}
	public void setName(String name)
	{
		//super.name=name; //you can't do that,Since variable is private hence it is not visible within this object. 
	}
}
class Address 
{
	private String streetName;
	private String city;
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public String toString() {
		return "Address [streetName=" + streetName + ", city=" + city + "]";
	}
	
	
}
class Point implements Cloneable
{
  	private int x;
  	private int y;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
  	@Override
  	protected Object clone() throws CloneNotSupportedException {
  		// TODO Auto-generated method stub
  		return super.clone();
  	}
	
}