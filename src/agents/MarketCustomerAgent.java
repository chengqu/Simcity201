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
	
	List<String> whatIWant; 
	
	MarketEmployeeAgent employee;
	
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
	
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	/* (non-Javadoc)
	 * @see agents.MarketCustomer#msgAskForCustomerOrder()
	 */
	
	//from Employee
	@Override
	public void msgAskForCustomerOrder() {
		state = CustomerState.ordering;
		//moveToOrderingArea() //animation 

	}

	//from Employee
	/* (non-Javadoc)
	 * @see agents.MarketCustomer#msgHereIsYourStuff(java.util.List)
	 */
	@Override
	public void msgHereIsYourStuff(Order o) {
		//if (orderList.get(0) == whatIwant) 
			state = CustomerState.orderRecieved;
	}

	//from employee
	/* (non-Javadoc)
	 * @see agents.MarketCustomer#msgHereIsYourOrderCharge(float)
	 */
	@Override
	public void msgHereIsYourOrderCharge(float charge) {
		bill.charge_ = charge;
	}

	//from Employee
	/* (non-Javadoc)
	 * @see agents.MarketCustomer#msgPaymentNoted()
	 */
	@Override
	public void msgPaymentNoted() {
		state = CustomerState.paidForOrder;
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

		//If state == enteringStore, then
			//State == waiting ;
			//Employee.IamYouCustomer(this);
		if (state == CustomerState.enteringStore) {
			state = CustomerState.waiting;
			employee.msgIAmYourCustomer(this);
		}
		
		return false;
	}
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	/**
	 * ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	 */
	
	private void actnGiveEmployeeMyOrder() {
		state = CustomerState.waitingForOrder;
		//Employee.IWantThisStuff(deepCopyThisIWant());
	}

	private void actnMakePayment() {
		state = CustomerState.payingForOrder;
		//bill.calculatePayment();
		//employee.HereIsMyMoney(bill.whatIWillPay)
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
