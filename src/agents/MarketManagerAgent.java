package agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import simcty201.interfaces.MarketCustomer;
import agent.Agent;

public class MarketManagerAgent extends Person {

	MarketManagerAgent(String name) {
		this.name = name;
	}
	
	private static final int HowLongToWait = 10;
	
	/**
	 * DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	 */
	
	Person self_; 
	
	//my lists of stuff
	List<MyEmployee> employees = new ArrayList<MyEmployee>();
	List<MyCustomer> customers = new ArrayList<MyCustomer>();
	List<MyOrder> orders = new ArrayList<MyOrder>();
	
	public enum MyCustomerState {pending, assigning, assigned, exited};
	
	private class MyCustomer {
		MarketCustomer c_;
		MyCustomerState state_;
		MyCustomer(MarketCustomer c, MyCustomerState state) {
			c_ = c;
			state_ = state;
		}
	}
	
	
	private class MyEmployee {
		MarketEmployeeAgent e_;
		int peopleBeingHelped_;  
		boolean wantBreak_;
		float currentMoneyEarned_; 
		MyEmployee(MarketEmployeeAgent e) {
			e_ = e;
			peopleBeingHelped_ = 0;
			wantBreak_ = false;
			currentMoneyEarned_ = 0; 
		}
	}
	
	public enum MyOrderState {received, noted, fulfilling, fulfilled, 
		callingForTruck, calledForTruck, orderSentOut, orderComplete};
	
	private class MyOrder {
		Order o_;
		MyOrderState state_;
		MyOrder(Order o, MyOrderState s) {
			o_ = o;
			state_ = s;
		}
	}
	
	Transportation truck = null; 
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	/**
	 *  MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	 */
	
	//from employee
	public void msgIAmHereToWork(Person per, MarketEmployeeAgent e) {
		employees.add(new MyEmployee(e));
	}

	//from employee
	public void msgHereIsFulfilledOutsideOrder(Order o) {
		MyOrder mo = orders.find(o);
		mo.state_ = MyOrderState.fulfilled;
	}

	//from employees
	public void msgCustomerLeft(MarketCustomer c) {
		customers.remove(c); 
	}

	//from customer
	public void msgIWantToBuySomething (MarketCustomer c) {
	    customers.add(new MyCustomer(c, MyCustomerState.pending);
	}

	//from Restaurant or Other Store 
	public void msgOrderFromSomeWhere (Order o) {
		orders.add(new MyOrder(o, MyOrderState.received));
		//orders.add(new MyOrder(MyOrderState.received, o.whereIsItFrom, o.prepaid?));
	}

	//from Restaurant or Other Store
	public void msgHereIsOrderPayment(Order o, float payment) {
		orders.find(mo.o_ == o);
		mo.notePayment();
	}	

	//from employee
	public void msgCanITakeBreak(MarketEmployeeAgent e) {
	        for (MyEmployee me : employees) {
	                if (me.e_ == e) {
	                        me.wantBreak_ = true;
	                        break; 
	                }
	        }
	}

	//from Transportation
	public void msgTruckIsHere(Transportation truck) {
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
	
	private void actnProcessFulfilledOrder(MyOrder o) {
		orders.remove(o);
	}

	private void actnGetEmployeeForCustomer (MyCustomer mc) {
		mc.state_= MyCustomerState.assigning; 
		MyEmployee me = employees.find(employees.leastnumberCust()); 
        me.e_.HereIsCustomer(mc);  
        mc.state_ = MyCustomerState.assigned;
	}

	private void actnProcessOutsideOrder(MyOrder o) {
		o.state_ = MyOrderState.fulfilling;
		MyEmployee me = employees.find(employees.freeEmployee());
		me.e_.msgHereIsCalInOrder(o.o_);
		o.sender.msgHereIsYourCharge(o.ComputeCharge());
	}

	private void actnCallDeliveryTruck(MyOrder o) {
		o.state_ = MyOrderState.calledForTruck; 
		this.truck.callDeliverTruckTo(this);	
	
	}

	private void actnAskCustomerToWait(MarketCustomerAgent c) {
                
        Random generate = new Random();
        int i = generate.nextInt(intHowLongToWait); //             
        if (c.getName().contains("leaveearly") || i == 0) {
                waitingCustomers.remove(c); 
                c.msgLeaveEarly();                 
        }
        else {
                //nothing to do               
        } 
	}

	private void actnLoadTruckWithOrders() {
		
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

	private void actnProcessBreakRequest (MyEmployee e) {
                
        if (employees.size() > 1 && customers.size() == 0 && orders.isEmpty()) {
                        
        	employees.remove(e);
            e.e_.msgBreakRequestAnswer(true);
            e.wantBreak_ = false;                                                 
        }
        else {
        	e.e_.msgBreakRequestAnswer(false);
        	e.wantBreak_ = false; 
        }                          
	}
	
	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
}
