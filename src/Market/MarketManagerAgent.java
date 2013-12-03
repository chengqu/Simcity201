package Market;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import simcity201.interfaces.MarketCustomer;
import simcity201.interfaces.MarketInteraction.Bill;
import agent.Agent;
import agents.Person;

public class MarketManagerAgent extends Agent {
	
	String name;
	public Person person;
	
	StoreMenu prices = new StoreMenu(); 
	
	public MarketManagerAgent(String name, Person p) {
		this.name = name;
		person = p;
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
	
	public enum MyOrderState {received, noted, fulfilled, 
		callingForTruck, calledForTruck, orderSentOut, orderComplete, 
		prepareOrderToSend, initialProcessing, packaging, deliveringElectronically};
	
	public class MyOrder {
		Order o_;
		MyOrderState state_;
		boolean payment_;
		simcity201.interfaces.MarketInteraction.Bill bill;
		MyOrder(Order o, MyOrderState s) {
			o_ = o;
			state_ = s;
			payment_ = false;
			bill = null;
		}
		boolean notePayment() {
			return payment_;
		}
		public void addBillToOrder(Bill b) {
			bill = b;
		}
	}
	
	//Transportation truck = null; 
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	/**
	 *  MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	 */
	
	//from employee
	public void msgIAmHereToWork(Person per, MarketEmployeeAgent e) {
		print("msgIAmHereToWork called");
		
		employees.add(new MyEmployee(e));
		
		stateChanged();
	}

	//from employee
	public void msgHereIsFulfilledOutsideOrder(Order o) {
		print("msgHereIsFulfilledOutsideOrder called");
		
		for (MyOrder mo : orders) {
			if (mo.o_ == o) {
				mo.state_ = MyOrderState.fulfilled;
				break;
			}
		}
		
		stateChanged();
	}

	//from employees
	public void msgCustomerLeft(MarketEmployeeAgent ea, MarketCustomer c) {
		print("msgCustomerLeft called");
		
		for (MyCustomer mc :customers) {
			if (mc.c_ == c) {
				customers.remove(mc);
				break;
			}
		} 
		
		for (MyEmployee me : employees) {
			if (me.e_ == ea) {
				me.peopleBeingHelped_--;
				break;
			}
		}
		
		stateChanged();
	}

	//from customer
	public void msgIWantToBuySomething (MarketCustomer c) {
	    customers.add(new MyCustomer(c, MyCustomerState.pending));
	    stateChanged();
	}

	//from Restaurant or Other Store 
	public void msgOrderFromSomeWhere (Order o) {
		print("msgOrderFromSomeWhere called"); 
		
		orders.add(new MyOrder(o, MyOrderState.received));
		//orders.add(new MyOrder(MyOrderState.received, o.whereIsItFrom, o.prepaid?));
		stateChanged(); 
	}

	/*
	//from Restaurant or Other Store
	public void msgHereIsOrderPayment(Order o, float payment) {
		
		for (MyOrder mo : orders) {
			if (mo.o_ == o) {
				mo.state_ = MyOrderState.orderComplete;
				mo.payment_ = true;
				//mo.notePayment();  need to implement????
				break;
			}
		}
		stateChanged();
	}	
	*/
	
	//from Restauraunt or Other Store
	public void msgHereIsOrderPayment(Bill b, float payment) { 
		print("msgHereIsOrderPayment called");
		
		for (MyOrder mo : orders) {
			if (mo.bill == b) {
				mo.state_ = MyOrderState.prepareOrderToSend;
				break;
			}
		}
		
		stateChanged(); 
	}

	//from employee
	public void msgCanITakeBreak(MarketEmployeeAgent e) {
		print("msgCanITakeBreak called");
		
	    for (MyEmployee me : employees) {
	            if (me.e_ == e) {
	                    me.wantBreak_ = true;
	                    break; 
	            }
	    }
	        
	    stateChanged();
	}

	//from Transportation
	/*
	public void msgTruckIsHere(Transportation truck) {
		this.truck  = truck; 
	}	
	*/
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	/**
	 *  SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER
	 */
	
	public boolean pickAndExecuteAnAction() {
	
		//If there is a mc in customers such that mc.s_ == pending, then
			//GetEmployeeForCustomer(mc);
		for (MyCustomer mc : customers) {
			if (mc.state_ == MyCustomerState.pending) {
				actnGetEmployeeForCustomer(mc);
				return true;
			}
		}
		
		//If there is an o in orders such that o.s_ == prepareOrderToSend, the 
			//package order for sending 
		for (MyOrder mo : orders) {
			if (mo.state_ == MyOrderState.prepareOrderToSend) {
				actnPackOrderForSending(mo);
				return true;
			}
		}
		
		
		//If there is an o in orders such that o.s_ == received, then
			//o.s_ = noted;
			//ProcessOutsideOrder(o); 
		for (MyOrder mo : orders) {
			if (mo.state_ == MyOrderState.received) {
				mo.state_ = MyOrderState.noted;
				actnProcessOutsideOrder(mo);
				return true;
			}
		}
		
		//If there is an o in orders such that o.s_ == fulfilled, then 
			//o.s_ = callingForTruck;
			//CallDeliveryTruck(); //call one truck at a time 
		for (MyOrder mo : orders) {
			if (mo.state_ == MyOrderState.fulfilled) {
				mo.state_ = MyOrderState.deliveringElectronically;
				actnDeliverElectronically(mo);
				return true;
			}
		}
		
		/*
		for (MyOrder mo : orders) {
			if (mo.state_ == MyOrderState.callingForTruck) {
				//actnCallDeliveryTruck();
				return true;
			}
		}
		*/
		
		//If truck != null, then
			//LoadTruckWithOrders();
		/*
		if (truck != null) {
			actnLoadTruckWithOrders(); 
		}
		*/
		
		//If there is an o in orders such that o.s == orderSentOut && o.notePayment() returns true, then
			//ProcessFulfilledOrder(); 
		/*
		for (MyOrder mo : orders) {
			if (mo.state_ == MyOrderState.orderSentOut && mo.notePayment()) {
				mo.state_ = MyOrderState.noted;
				actnProcessOutsideOrder(mo);
				return true;
			}
		}
		*/
		
		
		//If there is an me in employees that wants to take a break,         
			//ProcessBreakRequest(); 
		
		for (MyEmployee e : employees) {
			if (e.wantBreak_) {
				actnProcessBreakRequest(e);
				return true;
			}
		}
	
		//before end of scheduler
		return false;
	}
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	private void actnDeliverElectronically(MyOrder mo) {
		
		mo.o_.market.msgHereIsMarketFood(mo.o_.travelingOrder);
		
		//DELETING ORDER NOW!!!!!
		//no need to go back to the scheudler for now...
		System.out.println("ORDER DELETED AFTER TELLING RESTAURANT");
		orders.remove(mo);
	}

	private void actnPackOrderForSending(MyOrder mo) {
		mo.state_ = MyOrderState.packaging; 
		
		//get least busy employee in this actn
		MyEmployee me = actnGetEmployee();
		
		me.e_.msgHereIsCallInOrder(mo.o_);
	}

	/**
	 * ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	 */
	
	private void actnProcessFulfilledOrder(MyOrder mo) {
		orders.remove(mo);
	}

	private void actnGetEmployeeForCustomer (MyCustomer mc) {
		mc.state_= MyCustomerState.assigning; 
		
		MyEmployee me = actnGetEmployee(); 
		
        me.e_.msgHereIsCustomer(mc.c_);  
        mc.state_ = MyCustomerState.assigned;
	}
	
	private MyEmployee actnGetEmployee() {
		
		print("getting employee"); 
		
		//find employee with least number of customers by iterating through list
		int leastCust = employees.get(0).peopleBeingHelped_; 
		for (MyEmployee me : employees){
			if (me.peopleBeingHelped_ < leastCust)
				leastCust = me.peopleBeingHelped_;
		}
		//find the first waiter with the least # of customers and pick him
		for (MyEmployee me : employees){
			if (me.peopleBeingHelped_ <= leastCust) {
				
				me.peopleBeingHelped_ ++;
				
				/*
				.w_.msgSitAtTable(currentCust, table.tableNumber_); //msg to waiter
				mywaiter.incrementCustomer();
				
				//make sure host agent doesn't infinite loop! take care of host info so he knows
				waitingCustomers.remove(currentCust);
				table.setOccupant(currentCust);
				*/
				return me;
			}
		}
		
		print("error in getting employee");
		return employees.get(0); 
	}
	
	private void actnProcessOutsideOrder(MyOrder mo) {
		mo.state_ = MyOrderState.initialProcessing;
		
		float charge = 0; 
		simcity201.interfaces.MarketInteraction.Order temp = mo.o_.GiveMeTheWholeOrder();
		for (int i=0; i < temp.foodList.size(); i++) {
			charge += prices.howMuchIsThat(temp.foodList.get(i)) * temp.foodAmounts.get(i);
		}
		
		//this call is now in actnPackOrderForSending
		//me.e_.msgHereIsCallInOrder(o.o_);
		
		simcity201.interfaces.MarketInteraction.Bill bill = new 
				simcity201.interfaces.MarketInteraction.Bill(charge, this);
		
		mo.addBillToOrder(bill);
				
		if (mo.o_.market != null) {
			mo.o_.market.msgHereIsMarketBill(bill);
		}
		else 
			System.out.println("ERROR null market");
	}

	/*
	private void actnCallDeliveryTruck(MyOrder o) {
		o.state_ = MyOrderState.calledForTruck; 
		this.truck.callDeliverTruckTo(this);	
	
	}
	*/

	private void actnAskCustomerToWait(MyCustomer mc) {
            
		int HowLongToWait = 10;
		
        Random generate = new Random();
        int i = generate.nextInt(HowLongToWait);            
        if (mc.c_.getName().contains("leaveearly") || i == 0) {
        	
        	mc.c_.msgLeaveEarly();               
        }
        else {
                //nothing to do               
        } 
	}

	/*
	
	private void actnLoadTruckWithOrders() {
		
		//Transportation temptruck = truck;
		//this.truck = null;
		
		for (MyOrder mo : orders) {
			if (mo.state_ == MyOrderState.calledForTruck || mo.state_ == MyOrderState.fulfilled) {
				temptruck.add(mo.o);
				mo.state_ = MyOrderState.orderSentOut; 
				break;
			}
		}
	}

	*/
	
	private void actnProcessBreakRequest (MyEmployee me) {
                
        if (employees.size() > 1 && customers.size() == 0 && orders.isEmpty()) {
                        
        	employees.remove(me);
            me.e_.msgBreakRequestAnswer(true);
            me.wantBreak_ = false;                                                 
        }
        else {
        	me.e_.msgBreakRequestAnswer(false);
        	me.wantBreak_ = false; 
        }                          
	}
	
	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
}
