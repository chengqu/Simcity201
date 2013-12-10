package agents;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class StopAgent extends Agent{
	Map<PassengerAgent, BusAgent> map = new HashMap<PassengerAgent, BusAgent>();

	List<MyBus> Bus  = Collections.synchronizedList(new ArrayList<MyBus>());
	List<WaitingPeople> WaitingPeople = Collections.synchronizedList(new ArrayList<WaitingPeople>());
	
	private class MyBus{
			BusAgent b;
			int LineNum;
			String route;
			MyBus(BusAgent b, String route){
				this.b = b;
				this.route = route;
			}
			}
	
	private class WaitingPeople{
			public BusAgent b;
			public PassengerAgent p;  
			public String waitingDest;
			public String Dest;
			
			WaitingPeople(PassengerAgent p, BusAgent b, String wd, String d){
				this.b = b;
				this.Dest = d;
				this.p = p;
				this.waitingDest = wd;
			}
			}

	//Bus.add ( new BusAgent ( "BankRest1MarketHouse", 1));
	//Bus.add ( new BusAgent( "Rest2Rest3Rest4Rest5",2));
	
	public StopAgent(BusAgent b1,BusAgent b2) {
		super();
		Bus.add(new MyBus(b1,"BankMarketHouseRestaurants1Restaurants2"));
		Bus.add(new MyBus(b2,"BankMarketHouseRest1Rest2"));
		
	}
	
	public void msgINeedBus(PassengerAgent p,String wd,String dest){
		BusAgent b = null;
		Random rand = new Random();
		int  n = rand.nextInt(2);
		b = Bus.get(n).b;
		WaitingPeople.add(new WaitingPeople(p,b,wd,dest));
		//map.put(p, b);
		stateChanged();
	}
	protected boolean pickAndExecuteAnAction() {
		if(!WaitingPeople.isEmpty()){
			TakePassenger(WaitingPeople.get(0));
			return true;
		}
		
		return false;
	}

	// Actions
private void TakePassenger(WaitingPeople p){
		p.b.msgStopAt(p.p,p.waitingDest,p.Dest);
		WaitingPeople.remove(p);
		
	}
	
	
		
}

