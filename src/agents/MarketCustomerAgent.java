package agents;

import java.util.ArrayList;
import java.util.List;

import simcty201.interfaces.MarketCustomer;
import agent.Agent;

public class MarketCustomerAgent extends Person implements MarketCustomer {

	MarketCustomerAgent(String name) {
		this.name = name;
	}
	
	/**
	 * DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	 */
	
	String whatIWant; 
	
	StoreMenu storemenu; 
	
	MarketEmployeeAgent employee;
	MarketManagerAgent manager;
	
	enum CustomerState {enteringStore, waiting, ordering, waitingForOrder, orderRecieved,
		payingForOrder, paidForOrder};
		
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
	 
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	/**
	 * MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES  
	 */

	//from Employee
	public void msgAskForCustomerOrder(MarketEmployeeAgent e) {
		employee = e;
		state = CustomerState.ordering;
		//moveToOrderingArea() //animation
		stateChanged();
	}

	//from Employee
	public void msgHereIsYourStuff(Order o) {
		//if (orderList.get(0) == whatIwant) 
		state = CustomerState.orderRecieved;
		stateChanged();
	}

	//from employee
	@Override
	public void msgHereIsYourOrderCharge(float charge) {
		bill.charge_ = charge;
		stateChanged();
	}

	//from Employee
	public void msgPaymentNoted() {
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
			actnLeaveRestaurant();
		}

		//If state == orderReceived && bill.charge != null, then 
			//MakePayment();
		if (state == CustomerState.orderRecieved && bill.charge_ != -1) {
			actnMakePayment();
		}

		//If state == ordering, then 
			//GiveEmployeeMyOrder();
		if (state == CustomerState.ordering) {
			actnGiveEmployeeMyOrder();
		}

		/*  I DONT THINK I NEED THIS
		//If state == enteringStore, then
			//State == waiting ;
			//Employee.IamYouCustomer(this);
		if (state == CustomerState.enteringStore) {
			state = CustomerState.waiting;
			employee.msgIAmYourCustomer(this);
		}
		*/
		
		return false;
	}
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	/**
	 * ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	 */
	
	private void actnGiveEmployeeMyOrder() {
		state = CustomerState.waitingForOrder;
		
		//formulate the order
		Order o = new Order(this, whatIWant, 1);
		
		//tell the employee what I want
		employee.msgIWantThisStuff(this, o);	
		
	}

	private void actnMakePayment() {
		state = CustomerState.payingForOrder;
		
		//actnCalculatePayment();
		
		employee.msgHereIsMyMoney(this, storemenu.howMuchIsThat(whatIWant));
	}
	
	private void actnCalculatePayment() {	
		
	}

	private void actnLeaveRestaurant() {
		//DoLeaveRestaurant(); 
		//employee.IAmLeaving(); 
	}

	

	@Override
	public void msgLeaveEarly() {
		// TODO Auto-generated method stub
		
	}
	
	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
}
