package agents;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;

public class MarketEmployeeAgent extends Person {

	MarketEmployeeAgent(String name) {
		this.name = name;
	}
	
	/**
	 * DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	 */
	
	MarketManagerAgent manager;

	enum MyOrderState {newOrder, fulfillingOrder, doneOrder, fulfilled};
	
	class MyOrder {
	        int orderID;
	        Order o_; 
	        List<String> Order.orderList; 
	        MyOrderState s_; 
	}

	enum MyCustomerState {newCustomer, asked, ordered, waitingForOrder, doneCustomer, paid, leaving, charging};
	
	class MyCustomer {
	        Customer c_;
	        MyCustomerState s_;
	        MyOrder o; 
	}
	
	enum EmployeeState {working, wantBreak, takeBreak, onBreak, backToWork, holdForBreak}; 

	enum ItemsInMarket {steak, chicken, salad, pizza, sportsCar, suvCar, miniCar};

	EmployeeState state_; 
	
	List<MyCustomer> customers = new ArrayList<MyCustomer>();
	List<MyOrder> orders = new ArrayList<MyOrder>();
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	/**
	 *  MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	 */
	
	//from customer
	private void IAmYourCustomer(Customer c) {
		Customers.add(new MyCustomer(c, newCustomer);
	}

	//from customer 
	private void IWantThisStuff(orderStuff) { //make a copy of the list on the sender side 
		MyCustomer mc = customers.find(c); 
		mc.s_ = ordered; 
		mc.o.orderList_ = orderStuff;
		mc.o.s_ = newOrder
	}

	//from customer 
	private void HereIsMyMoney(Customer c, float Cash){
		MyCustomer mc = customers.find(c);
		mc.s_ = MyCustomerState.paid;
		
	}

	//from customer
	private void IAmLeaving(Customer c) {
		MyCustomer mc = customers.find(c);
		mc.s_ = MyCustomerState.leaving; 
	}

	//from manager
	private void HereIsCustomer(Customer c) {
		Customers.add (new MyCustomer(c, newCustomer));
	}

	//from manager
	private void HereIsCallInOrder(Order o) {
		orders.add(new MyOrder(o,newOrder));
	}

	//from manager 
	private void msgBreakRequestAnswer(boolean answer) {        
	        if (answer == true) {
	                state_ = EmployeeState.takeBreak; 
	                }
	                else if (answer == false) {
	                        state_ = Employee.working; 
	        }        
	}
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	/**
	 *  SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER
	 */
	
	public boolean pickAndExecuteAnAction() {
		
		//If e.state == takeBreak, then,
		        //TakeBreak();
		//If e.state == backToWork, then,
		        //BackToWork();
		//If there is mc in customers such that mc.s == leaving, then 
			//CustomerLeaving();
		//If there is mc in customers such that mc.s == paid, then 
			//mc.c.PaymenNoted();
		//If there is mc in customers such that mc.o == fulfilled, then
			//GiveOrderAndChargeCust(mc);
		//If there is mc in customers such that mc.s == ordered, then
			//FullfillCustomerOrder(mc);
		//If there is mc in customers such that mc.s == newCustomer, then 
			//AskForCustomerOrder(mc);
		//If there is mo in orders such that mo.s == doneOrder, then 
			//TellManagerOrderDone(mo);
		//If there is mo in orders such that mo.s == newOrder, then 
			//FullfillOutsideOrder(mo); //myorder
		//If customers.isEmpty and w.s == wantBreak, then
		        //askForBeak();
		
		return false;
	}
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	/**
	 * ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	 */
	
	private void BackToWork() {        
        manager.addMyEmployee(this); 
        state_ = EmployeeState.working;                 
	}
	
	private void TakeBreak() {         
        state_ = EmployeeState.onBreak;        
	}
	
	private void AskForBreak() {
        state_ = EmployeeState.holdForBreak;
        manager.msgCanITakeBreak(this);
	}
	
	private void CustomerLeaving(MyCustomer mc) {
		manager.customerLeft();
		customers.remove(mc);
	}

	private void AskForCustomerOrder(MyCustomer mc) {
		mc.s_ = MyCustomerState.asked;
		mc.c.AskForCustomerOrder(); 
	}
	
	private void FullfillCustomerOrder(MyCustomer mc) {
		mc.s_ = MyCustomerState.waitingForOrder;
		getCustomerOrder() //animation
		mc.o_ = MyOrderState.fulfilled; 
	}
	
	private void GiveOrderAndChargeCustomer(MyCustomer mc) {
		mc.s_ = MyCustomerState.charging; 
		mc.c.HereIsYourStuff(mc.o.orderList);
		mc.c.HereIsOrderCharge(mc.order.computeCharge())
	}
	
	private void FullfillOutsideOrder(MyOrder mo) {
		mo.s_ = MyOrderState.fulfillingOrder; 
		fulfillOutsideOrder() //animation
		mo.s_ = MyOrderState.doneOrder;
	}

	TellManagerOrderDone(MyOrder mo) {
		manager. HereIsFulfilledOutsideOrder(mo.o_); 
		orders.remove(mo);
	}
	
	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
}
