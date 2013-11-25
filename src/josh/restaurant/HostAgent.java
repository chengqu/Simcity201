package josh.restaurant;

import agent.Agent;
import agents.Person;

import java.util.*;
import java.util.concurrent.Semaphore;

import josh.restaurant.gui.HostGui;
import josh.restaurant.gui.RestaurantGui;
import josh.restaurant.gui.RestaurantPanel;
import josh.restaurant.gui.CustomerGui.WaitPosition;
import josh.restaurant.interfaces.Host;
import josh.restaurant.interfaces.Waiter;


/**
 * Restaurant Host Agent
 */
public class HostAgent extends Agent implements Host {
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	//variables 
	
	Person person =  null;
	
	static final int NTABLES = 5;
	static final int NSEATS = 5;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	public RestaurantGui restGui = null; 
	public HostGui hostGui = null;  
	public RestaurantPanel restPanel = null; 
	boolean asking;
	
	//variable lists 
	
	public List<CustomerAgent> waitingCustomers = new ArrayList<CustomerAgent>();
	//public List<WaitingPosition> waitingSeats = new ArrayList<WaitingPosition>();
	public Collection<Table> tables;
	public List<MyWaiter> waiters = new ArrayList<MyWaiter>();
	
	//classes --> 
	
	private class MyWaiter {
		Waiter w_;
		private int numberOfCustomers_;
		boolean wantsBreak_; 
		MyWaiter(Waiter w) {
			this.w_ = w;
			this.numberOfCustomers_ = 0; 
			wantsBreak_ = false;
		}
		public void incrementCustomer() {
			numberOfCustomers_ ++;
		}
		public void decrementCustomer() {
			if (numberOfCustomers_ > 0)
				numberOfCustomers_ --;
			else 
				System.out.println("error cant decrement");
		}
	}
	
	public class Table {
		CustomerAgent occupiedBy_;
		int tableNumber_;
		Table(int tableNumber) {
			this.tableNumber_ = tableNumber;
		}
		void setOccupant(CustomerAgent cust) {
			this.occupiedBy_ = cust;
		}
		void setUnoccupied() {
			occupiedBy_ = null;
		}
		CustomerAgent getOccupant() {
			return occupiedBy_;
		}
		boolean isOccupied() {
			return (occupiedBy_ != null);
		}
		public String toString() {
			return "table " + tableNumber_;
		}
		public int getTableNum(){
			return tableNumber_;
		}
	}
	
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 

	//constructor 
	public HostAgent(String name) {
		super();
		this.name = name;
		
		//this is not drawing them...this only makes waiters aware of # of tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));     //how you add to a collections
		}
		
		asking = false; 
	}
	
	//constructor with person agent as second argument
	public HostAgent(String name, Person p) {
		super();
		this.name = name;
		person = p;
		//this is not drawing them...this only makes waiters aware of # of tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));     //how you add to a collections
		}
		
		asking = false; 
	}

	public String getMaitreDName() {
		return name;
	}
	public String getName() {
		return name;
	}
	public List getWaitingCustomers() {
		return waitingCustomers;
	}
	public Collection getTables() {
		return tables;
	}
	
	//~~
	
	public void addMyWaiter(Waiter w) {
		
		//we copy the waiter that restaurant panel creates and make a MyWaiter
		print("addMyWaiter called");
		waiters.add(new MyWaiter(w));
		stateChanged();
	}
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	public void msgIWantToEat(CustomerAgent cust) {
		print("I want to eat called");
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgTableIsFree(Waiter w, int tablenum) {
		
		//my waiter now lost a customer 
		for (MyWaiter mw : waiters) {
			if (mw.w_ == w) {
				print("mywaiter now has one less cutomer to serve"); 
				mw.decrementCustomer();
				break; 
			}
		}
		
		//this table is now free
		for (Table table : tables) {
			if (table.getTableNum() == tablenum) {
				print(table + " is free now");
				table.setUnoccupied();
				break; 
			}
		}
		stateChanged();
	}

	public void msgCanITakeBreak(Waiter w) {

		for (MyWaiter mw : waiters) {
			if (mw.w_ == w) {
				mw.wantsBreak_ = true;
				break; 
			}
		}
		stateChanged(); 
	}
	
	public void msgAtTable() {   //from animation
		print("msgAtTable() called");
		atTable.release();     // = true;   
		stateChanged();
	}
	
	//where is this from??????????
	public void msgReadyToWork () {
		stateChanged();
	}

	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	public boolean pickAndExecuteAnAction() {
		
		try {
		
		/*
		If there exists a table in table that !occupied && !waitingCustomers.empty && !waiters.empty then, 
		GetWaiter(table, waitingCustomers.top())
		*/
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!waitingCustomers.isEmpty()) { 
					if (!waiters.isEmpty()) { 
						print("starting get waiter");
						actnGetWaiter(waitingCustomers.get(0), table);
						return true;     //return true to the abstract agent to reinvoked the scheduler.
					}
				}
			}
		}
		
		if (!waitingCustomers.isEmpty() && asking == false) { //if there are still waiting customers
			asking = true; 
			CustomerAgent c = waitingCustomers.get(0); //the top 
			actnAskCustomerToWait(c); 
			print("asking customer if wants to wait");
			return false;
		}
		
		
		//If there exists mw in waiters that wants to take a break, ProcessBreakRequest
		for (MyWaiter w : waiters)  {
			if (w.wantsBreak_ == true) {
				actnProcessBreakRequest(w); 
				return true; 
			}
		}
		
		} 
		catch (ConcurrentModificationException e) {
			return false;
		}
		
		//print("sleep");
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER

	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 

	//private void actnTellCustToWaitHere(WaitingPosition w) {
		
		
		
		
		
		
	//}
	
	
	private void actnAskCustomerToWait(CustomerAgent c) {
		
		Random generate = new Random();
		int i = generate.nextInt(10); 
		
		print("asking customer if wants to wait"); 
		
		//for (CustomerAgent c : waitingCustomers) {
		if (c.getName().contains("leaveearly") || i == 0) {
			waitingCustomers.remove(c); 
			c.msgLeaveEarly(); 
			print("I dont want to wait!!!!!"); 
		}
		else {
			//do nothing
			//willing to wait
		}
		
		asking = false; 
		
		//delete temporary random number generator 
		generate = null;
		
		//dont enter state changed or else will go into busy wait
		//stateChanged(); 
	}
	
	private void actnGetWaiter(CustomerAgent currentCust, Table table) {
		
		print("getting waiter"); 
		
		//find waiter with least number of customers by iterating through list
		int leastCust = waiters.get(0).numberOfCustomers_; 
		for (MyWaiter mywaiter : waiters){
			if (mywaiter.numberOfCustomers_ < leastCust)
				leastCust = mywaiter.numberOfCustomers_;
		}
		//find the first waiter with the least # of customers and pick him
		for (MyWaiter mywaiter : waiters){
			if (mywaiter.numberOfCustomers_ <= leastCust) {
				mywaiter.w_.msgSitAtTable(currentCust, table.tableNumber_); //msg to waiter
				mywaiter.incrementCustomer();
				
				//make sure host agent doesn't infinite loop! take care of host info so he knows
				waitingCustomers.remove(currentCust);
				table.setOccupant(currentCust);
				
				//your job as a HOST IS DONE NOW for this occasion!!
				break;
			}
		}
	}
	
	private void actnProcessBreakRequest (MyWaiter w) {
		
		if (waiters.size() > 1 && w.numberOfCustomers_ == 0) {
			//it might be redundant to ask if #ofcustomers is 0 because the waiter
			//only asks for break when he has no more customers.
			
			//going to remove waiter from list of waiters for now... 
			//TO DO...actually we can add this waiter back to waiters...dont need to create
			//a new waiter.  right now I only create a new waiter
			waiters.remove(w);
			w.w_.msgBreakRequestAnswer(true);
			w.wantsBreak_ = false; 
			//restGui.updateInfoPanel(w);
			
			//remove my waiter
			w = null; 
			
		}
		else {
			w.w_.msgBreakRequestAnswer(false);
			w.wantsBreak_ = false; 
		}
			 	
	}

	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	
	//utilities
	
	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public void setGui(RestaurantGui gui) {
		restGui = gui;
	}
	
	public void setGui(RestaurantPanel gui){
		System.out.println("WORKING");
		restPanel = gui;
	}
	
	public HostGui getGui() {
		return hostGui;
	}

}

