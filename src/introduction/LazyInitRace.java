package introduction;

public class LazyInitRace {
    private ExpensiveObject instance = null;

    public ExpensiveObject getInstance() {
        if (instance == null)
            instance = new ExpensiveObject();
        return instance;
    }
}

class ExpensiveObject { }
/*
 LazyInitRace has race conditions that can undermine its correctness. Say that threads A and B 
 execute getInstance at the same time. A  sees  that instance  is null, and  instantiates 
 a new ExpensiveObject. B  also  checks  if instance  is null. Whether instance is null at this 
 point depends unpredictably on timing, including the vagaries of scheduling and how long A takes 
 to instantiate the ExpensiveObject and set the instance field. If instance is null when B examines 
 it, the two callers to getInstance may receive two different results, even though getInstance is always supposed to 
return the same instance. 

 Like most concurrency errors, race conditions don't always result in failure: some unlucky timing is also required. But 
race conditions can cause serious problems. If LazyInitRace is used to instantiate an application wide registry, having it 
return  different  instances from multiple  invocations could cause  registrations  to be  lost or  multiple  activities to have 
inconsistent  views  of  the  set  of  registered  objects.  If  UnsafeSequence  is  used  to  generate  entity  identifiers  in  a 
persistence framework, two distinct objects could end up with the same ID, violating identity integrity constraints. 

*/