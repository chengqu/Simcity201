package agents;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;

public class MarketCustomerAgent extends Person {

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
	}
	
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	/**
	 *  MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	 */
	
	//from Employee
	private void AskForCustomerOrder() {
		state = CustomerState.ordering;
		moveToOrderingArea() //animation 

	}

	//from Employee 

	private void HereIsYourStuff(List<String> orderList) {
		if (orderList.get(0) == whatIwant) 
			state = CustomerState.orderRecieved;
	}

	//from employee
	private void HereIsYourOrderCharge(float charge) {
		bill.charge_ = charge;
	}

	//from Employee
	private void PaymentNoted() {
		state = CustomerState.paidForOrder;
	}
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	/**
	 *  SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER
	 */
	
	public boolean pickAndExecuteAnAction() {
		
		//If state == paidForOrder, then 
			//LeaveRestaurant();

		//If state == orderReceived && bill.charge != null, then 
			//MakePayment();

		//If state == ordering, then 
			//GiveEmployeeMyOrder();

		//If state == enteringStore, then
			//State == waiting ;
			//Employee.IamYouCustomer(this);
		
		return false;
	}
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	/**
	 * ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	 */
	
	private void GiveEmployeeMyOrder() {
		state = CustomerState.waitingForOrder;
		Employee.IWantThisStuff(deepCopyThisIWant());
	}

	private void MakePayment() {
		state = CustomerState.payingForOrder;
		bill.calculatePayment();
		employee.HereIsMyMoney(bill.whatIWillPay)
	}

	private void LeaveRestaurant() {
		DoLeaveRestaurant(); 
		employee.IAmLeaving(); 
	}
	
	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
}
