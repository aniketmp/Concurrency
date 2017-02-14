package introduction;

public class ThisEscape 
{
	int a=12;
	int b=14;
	ThisEscape()
	{
		new Another().doSomething(this);
		a=35;
		b=37;
	}
	
	public static void main(String[] args) {
		new ThisEscape();
	}
}
class Another
{
	public void doSomething(ThisEscape e)
	{
		System.out.println("a:"+e.a+" b:"+e.b);
	}
}
/*
 * object is in a predictable, consistent state only after its constructor returns, so publishing an object
from within its constructor can publish an incompletely constructed object. This is
true even if the publication is the last statement in the constructor. If the this reference
escapes during construction, the object is considered not properly constructed.

The internal synchronization in thread-safe collections means that placing an
object in a thread-safe collection, such as a Vector or synchronizedList, fulfills
the last of these requirements. If thread A places object X in a thread-safe collection
and thread B subsequently retrieves it, B is guaranteed to see the state of X
as A left it, even though the application code that hands X off in this manner has
no explicit synchronization.
 */
 