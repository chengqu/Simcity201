package Market;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simcity201.interfaces.MarketCustomer;
import agent.Agent;
import agents.Grocery;
import agents.Person;

public class MarketCustomerAgent extends Agent implements MarketCustomer {

	String name;
	public Person person;
	
	public String getName() {
		return name;
	}
	
	public MarketCustomerAgent(String name, Person p) {
		this.name = name;
		person = p;
	}
	
	private Semaphore atDestination = new Semaphore(0,true);
	Timer customerTimer = new Timer(); 
	
	/**
	 * DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	 */
	
	//THIS IS A HACK RIGHT NOW
	String whatIWant = "steak"; 
	
	StoreMenu storemenu = new StoreMenu(); 
	
	MarketEmployeeAgent employee;
	MarketManagerAgent manager;
	
	MarketCustomerGui gui;
	
	enum CustomerState {enteringStore, waiting, ordering, waitingForOrder, orderRecieved,
		payingForOrder, paidForOrder, leavingStore};
		
	CustomerState state = CustomerState.enteringStore;
	CustomerBill bill = new CustomerBill(); 
	
	//filled-in at initiation if there is debt 
	class CustomerBill {
	        public float charge_;
	        public float inDebt_; 
	        public boolean owesMoney;
	        CustomerBill() {
	        	charge_ = -1;
	        	inDebt_ = 0;
	        }
	}
	
	//hack so that customer knows who to tell hungry
	public void setManager(MarketManagerAgent m) {
		manager = m;
	}
	
	public void setStoreMenu(StoreMenu s) {
		storemenu = s;
	}
	 
	public void doThings() {
		stateChanged();
	}
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	/**
	 * MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES  
	 */

	//from customer gui
	public void gui_msgAtEmployee() {
		atDestination.release();
	}
	
	//from Employee
	public void msgAskForCustomerOrder(MarketEmployeeAgent e) {
		print("msgAskForCustomerOrder called");

		employee = e;
		state = CustomerState.ordering;
		//moveToOrderingArea() //animation
		
		stateChanged();
	}

	//from Employee
	public void msgHereIsYourStuff(Order o) {
		print("msgHereIsYourStuff called");
		
		//person.groceries.add(new Grocery(o.)
		/*
		for (int i=0; i < o.travelingOrder.foodList.size(); i++) { //null pointer excepion here...
			person.groceries.add(new Grocery(
					o.travelingOrder.foodList.get(i), o.travelingOrder.foodAmounts.get(i)));
		}
		*/
		
		//if (orderList.get(0) == whatIwant) 
		state = CustomerState.orderRecieved;
		
		stateChanged();
	}

	//from employee
	@Override
	public void msgHereIsYourOrderCharge(float charge) {
		print("msgHereIsYourOrderCharge called");
		
		bill.charge_ = charge;
		
		stateChanged();
	}

	//from Employee
	public void msgPaymentNoted() {
		print("msgPaymentNoted called");
		
		state = CustomerState.paidForOrder;
		
		stateChanged();
	}
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	/* (non-Javadoc)
	 * @see agents.MarketCustomer#pickAndExecuteAnAction()
	 */
	
	@Override
	public boolean pickAndExecuteAnAction() {
		
		//If state == paidForOrder, then 
			//LeaveRestaurant();
		if (state == CustomerState.paidForOrder) {
			actnLeaveMarket();
			return true;
		}

		//If state == orderReceived && bill.charge != null, then 
			//MakePayment();
		if (state == CustomerState.orderRecieved && bill.charge_ != -1) {
			actnMakePayment();
			return true;
		}

		//If state == ordering, then 
			//GiveEmployeeMyOrder();
		if (state == CustomerState.ordering) {
			actnGiveEmployeeMyOrder();
			return true;
		}

		//If state == enteringStore, then
			//State == waiting ;
			//manager.IWantToBuySomething(this);
		if (state == CustomerState.enteringStore) {
			state = CustomerState.waiting;
			actnEnterTheStore();
		}
	
		return false;
	}
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	/**
	 * ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	 */
	
	private void actnEnterTheStore() {
		
		customerTimer.schedule(new TimerTask() {
			public void run() {
				print("walking lesiurely into store");
				actnMakeContactWithManager();
			}
		}, 2000);
		
	}
	
	private void actnMakeContactWithManager() {
		manager.msgIWantToBuySomething(this);
	}
	
	private void actnGiveEmployeeMyOrder() {
		state = CustomerState.waitingForOrder;
		
		//first...move to employee area!!!!!
		gui.DoGoTo(this, employee);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//formulate the order
		//order 1 steak for now...
		//Order o = new Order(this, whatIWant, 1);
		Order o =  new Order(this, "steak", 1, null);
		
		//tell the employee what I want
		employee.msgIWantThisStuff(this, o);	
		
	}

	private void actnMakePayment() {
		state = CustomerState.payingForOrder;
		
		//actnCalculatePayment();
		
		person.money -= storemenu.howMuchIsThat(whatIWant);
		
		employee.msgHereIsMyMoney(this, storemenu.howMuchIsThat(whatIWant));
	}
	
	private void actnCalculatePayment() {	
		
	}

	private void actnLeaveMarket() {
		//DoLeaveRestaurant(); 
		employee.msgIAmLeaving(this); 
		
		person.msgDone();
		
		state = CustomerState.leavingStore;
		
	}

	@Override
	public void msgLeaveEarly() {
		// TODO Auto-generated method stub
		
	}
	
	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	
	public void setGui(MarketCustomerGui g) {
		gui = g;
	}
}
