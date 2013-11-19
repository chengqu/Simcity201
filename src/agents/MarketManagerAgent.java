package agents;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;

public class MarketManagerAgent extends Person {

	MarketManagerAgent(String name) {
		this.name = name;
	}
	
	/**
	 * DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	 */
	
	Person self_; 
	
	//my lists of stuff
	List<MyEmployee> employees;
	List<MyCustomer> customers;
	List<MyOrder> orders;
	
	public enum MyCustomerState {pending, assigning, assigned, exited};
	
	private class MyCustomer {
		MarketCustomerAgent c_;
		MyCustomerState state_;
	}
	
	
	private class MyEmployee {
		MarketEmployeeAgent e_;
		int peopleBeingHelped_;  
		boolean wantBreak_;
		float currentMoneyEarned_;  
	}
	
	public enum MyOrderState {received, noted, fulfilling, fulfilled, 
		callingForTruck, calledForTruck, orderSentOut, orderComplete};
	
	private class MyOrder {
		Order o;
		MyOrderState state_;
	}
	
	Transportation truck = null; 
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	/**
	 *  MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	 */
	
	//from employee
	public void IAmHereToWork(Person per, new ) {
		employees.add(new Employee());
	}

	//from employee
	public void HereIsFulfilledOutsideOrder(Order o) {
		MyOrder mo = orders.find(o);
		mo.state_ = MyOrderState.fulfilled;
	}

	//from employess
	public void CustomerLeft(Customer c) {
		customers.remove(c); 
	}

	//from customer
	public void IWantToBuySomething (Customer cust) {
	    customers.add(new MyCustomer());
	}

	//from Restaurant or Other Store 
	public void OrderFromSomeWhere (Order o) {
		orders.add(new MyOrder(MyOrderState.pending, o.whereIsItFrom, o.prepaid?));
	}

	//from Restaurant or Other Store
	public void HereIsOrderPayment(Order o, float payment) {
		Orders.find(mo.o_ == o);
		mo.notePayment();
	}	

	//from employee
	public void CanITakeBreak(EmployeeAgent e) {
	        for (MyEmployee me : employees) {
	                if (me.e_ == e) {
	                        me.wantsBreak_ = true;
	                        break; 
	                }
	        }
	}

	//from Transportation
	public void TruckIsHere(Transportation truck) {
		this.truck  = truck; 
	}
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	/**
	 *  SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER
	 */
	
	public boolean pickAndExecuteAnAction() {
	
		//If there is a mc in customers such that mc.s_ == pending, then
			//GetEmployeeForCustomer(mc);
		//If there is an o in orders such that o.s_ == received, then
			//o.s_ = noted;
			//ProcessOutsideOrder(o); 
		//If there is an o in orders such that o.s_ == fulfilled, then 
			//o.s_ = callingForTruck;
			//CallDeliveryTruck(); //call one truck at a time 
		//If truck != null, then
			//LoadTruckWithOrders();
		//If there is an o in orders such that o.s == orderSentOut && o.notePayment() returns true, then
			//ProcessFulfilledOrder(); 
		//If there is an me in employees that wants to take a break,         
			//ProcessBreakRequest(); 
	
		return false;
	}
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	/**
	 * ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	 */
	
	private void ProcessFulfilledOrder(MyOrder o) {
		Orders.remove(o);
	}

	private void GetEmployeeForCustomer (MyCustomer mc) {
		mc.state_= MyCustomerState.assigning; 
		MyEmployee me = employees.find(employees.leastnumberCust()); 
        me.e.HereIsCustomer(mc);  
        mc.state_ = MyCustomerState.assigned;
	}

	private void ProcessOutsideOrder(MyOrder o) {
		o.state_ = MyOrderState.fulfilling;
		MyEmployee me = employees.find(employees.freeEmployee());
		me.e.HereIsCalInOrder(o);
		o.sender.HereIsYourCharge(o.comouterChareg());
	}

	private void CallDeliveryTruck(MyOrder o) {
		o.state_ = MyOrderState.calledForTruck; 
		this.truck.callDeliverTruckTo(this);	
	
	}

	private void AskCustomerToWait(Customer c) {
                
        Random generate = new Random();
        int i = generate.nextInt(intHowLongToWait);               
        if (c.getName().contains("leaveearly") || i == 0) {
                waitingCustomers.remove(c); 
                c.msgLeaveEarly();                 
        }
        else {
                //nothing to do               
        } 
	}

	private void LoadTruckWithOrders() {
		
		Transportation temptruck = truck;
		this.truck = null;
		
		for (MyOrder mo : orders) {
			if (mo.state_ == MyOrderState.calledForTruck || mo.state_ == MyOrderState.fulfilled) {
				temptruck.add(mo.o);
				mo.state_ = MyOrderState.orderSentOut; 
				break;
			}
		}
	}

	private void ProcessBreakRequest (MyEmployee e) {
                
        if (employees.size() > 1 && customers.size() == 0 && orders.isEmpty()) {
                        
        	employees.remove(e);
            e.e_.msgBreakRequestAnswer(true);
            w.wantsBreak_ = false;                                                 
        }
        else {
        	e.e_.msgBreakRequestAnswer(false);
        	e.wantsBreak_ = false; 
        }                          
	}	
	
	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
}
