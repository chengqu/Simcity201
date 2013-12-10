package josh.restaurant;

import agent.Agent;
//import restaurant.CookAgent.Food;




import agents.Person;
import guehochoi.test.mock.EventLog;

import java.util.*;
import java.util.concurrent.Semaphore;

import newMarket.MarketRestaurantHandlerAgent;
import josh.restaurant.CashierAgent.BillState;
import josh.restaurant.CookAgent.MyOrder;
import josh.restaurant.CookAgent.orderState;
import josh.restaurant.CustomerAgent.AgentEvent;
import josh.restaurant.gui.HostGui;
import josh.restaurant.interfaces.Cashier;
import josh.restaurant.interfaces.Customer;
import josh.restaurant.interfaces.Market;
import josh.restaurant.interfaces.Waiter;
import josh.restaurant.test.mock.MockCustomer;
import josh.restaurant.test.mock.MockWaiter;



public class CashierAgent extends Agent implements Cashier {
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	Person person = null;
	
	CookAgent cook;
	
	public EventLog log = new EventLog();
	
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	String name_;
	
	final static float steakprice = (float) 15.99; 
	final static float chickenprice = (float) 10.99;
	final static float saladprice = (float) 5.99;
	final static float pizzaprice = (float) 8.99;
	
	//map of the prices of the food
	Map <String, Float > prices = new HashMap<String, Float>();
	
	public class Bill {
			Waiter w_;
			String choice_; 
			int table_;
			public BillState state_; 
			public Customer c_;
			public float whatCustPaid_;
			public float charge_;
			Market m_;
			MarketRestaurantHandlerAgent handler_  = null;
			Bill(Waiter w, String choice, int table, BillState state, Customer c) {
				w_ = w;
				choice_ = choice;
				table_ = table;
				state_ = state;
				c_ = c;
				charge_ = 0;
			}
			
			Bill(Market m, String choice, BillState state, float charged) {
				choice_ = choice;
				state_ = state;
				charge_ = charged;
				m_ = m;
			}
			
			Bill(MarketRestaurantHandlerAgent handler, String choice, BillState state, float charged) {
				handler_ = handler;
				choice_ = choice;
				state_ = state;
				charge_ = charged;
				m_ = null;
			}

			
	}
	
	public enum BillState {pending, calculating, calculated, delivered, paid, inDebt, goingToPay, charging,
					marketPending, marketPaid, marketIncomplete, marketPaying}; 
	
	Timer cashierTimer = new Timer();
	
	public float totalMoney;
	
	void initPrices() {
		
		prices.put("steak", steakprice); 
		prices.put("chicken", chickenprice); 
		prices.put("salad", saladprice);
		prices.put("pizza", pizzaprice);
		
	}
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	public CashierAgent(String name) {
		super();
		this.name_ = name;
		initPrices(); 
		//init the cashier with 100 dollars 
		totalMoney = 100;
	}
	
	//constructor with second argument 
	public CashierAgent(String name, Person p) {
		super();
		this.name_ = name;
		person = p;
		initPrices(); 
		//init the cashier with 100 dollars 
		totalMoney = 100;
	}
	
	public void setCook(CookAgent cook) {
		this.cook = cook;
	}
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 

	//from market
	public void msgBillFromTheMarket(Market m, String produce, float chargeAmount, boolean complete) {
		
		if (complete)
			bills.add(new Bill(m, produce, BillState.marketPending, chargeAmount));
		else 
			bills.add(new Bill(m, produce, BillState.marketIncomplete, chargeAmount));
		
		stateChanged();
	}
	
	public void msgBillFromTheMarket(MarketRestaurantHandlerAgent h, String produce, float charge) {
		
		bills.add(new Bill(h, produce, BillState.marketPending, charge));
		
		stateChanged();
	}
	
	//from waiter
	public void msgProduceABill(Order o, Customer c) {

		print("PRODUCING A BILL"); 
		
		//make a bill based on the info from the order, add it to list of bills 
		bills.add(new Bill(o.w_, o.choice_, o.tableNum_, BillState.pending, c)); 
		stateChanged();
	}
	
	//from customer 
	public void msgHereIsMyPayment(Customer c, float customerCash) {

		synchronized(bills) {
			for (Bill b : bills) {
				if (b.c_ == c) {
					b.whatCustPaid_ = customerCash;
					b.state_ = BillState.goingToPay;
					break;
				}
			}
		stateChanged();
		}
	}
	
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	public boolean pickAndExecuteAnAction() {
		//print("banksy run"); 
		
		synchronized(bills) {
			for (Bill b : bills) {
				if (b.state_ == BillState.marketPaid) {
					actnNoteMarketPayment(b);
					return true;
				}
			}
		}
		
		synchronized(bills) {
			for (Bill b : bills) {
				if (b.state_ == BillState.marketIncomplete) {
					actnCheckForFulfilledOrder(b);
					return true;
				}
			}
		}
		
		synchronized(bills) {
			for (Bill b : bills) {
				if (b.state_ == BillState.marketPending) {
					actnPayMarket(b);
					return true;
				}
			}
		}
		
		synchronized(bills) {
			//If there exists b in bills such that b.s == goingToPay, then ChargeCustomer(b)
			for (Bill b : bills) {
				if (b.state_ ==  BillState.goingToPay) {
					print("in scheduler paying now");
					actnChargeCustomer(b);
					return true;
				}
			}
		}
		
		synchronized(bills) {
			//If there exists b in bills such that b.s == calculated, then SendtheBill(b)
			for (Bill b : bills) {
				if (b.state_ == BillState.calculated) {
					print("in scheduler calculated"); 
					actnSendTheBill(b); 
					return true;
				}	
			}
		}
		
		synchronized(bills) {
			//If there exists an b in bills such that b.s = pending, then, CalculateCheck(o); // actions
			for (Bill b: bills) {
				if (b.state_ == BillState.pending) {
					print("in scheuler calculating");
					actnCalculateCheck(b); 
					return true;
				}
			}
		}
		
		synchronized(bills) {
			//If there exists a b in bills such that b.s == inDebt or paid. RemoveBill(b)
			for (Bill b : bills) {
				if (b.state_ == BillState.inDebt || b.state_ == BillState.paid) {
					print("in scheduler removing bill");
					actnRemoveBill(b); 
					return true;
				}
			}
		}
	
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER

	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	
	void actnNoteMarketPayment(Bill b) {
		
		//opportunity to keep record of payments here...
		
		bills.remove(b);
		
	}
	
	void actnPayMarket(Bill b) {
		
		b.state_ = BillState.marketPaying;
		
		if (b.handler_ != null) {
			if (totalMoney - b.charge_ >= 0) {
				//paid in full
				totalMoney -= b.charge_;
				//b.m_.msgHereIsYourPayment(b.charge_, b.choice_);
				cook.msgPassOnThisOrder(b.charge_, b.choice_);
			}
			else {
				//paid whatever cashier has got left
				totalMoney = 0;
				//b.m_.msgHereIsYourPayment(totalMoney, b.choice_);
				cook.msgPassOnThisOrder(b.charge_, b.choice_);
				print("sorry, but we are not able to pay in full");
			}
			b.state_ = BillState.marketPaid;
			return;
		}
	
		if (totalMoney - b.charge_ >= 0) {
			//paid in full
			totalMoney -= b.charge_;
			b.m_.msgHereIsYourPayment(b.charge_, b.choice_);
		}
		else {
			//paid whatever cashier has got left
			totalMoney = 0;
			b.m_.msgHereIsYourPayment(totalMoney, b.choice_);
			print("sorry, but we are not able to pay in full");
		}
		
		b.state_ = BillState.marketPaid;	
	}
	
	void actnCheckForFulfilledOrder(Bill b) {
		
		synchronized (bills) {
		for (Bill bill : bills) {
			//if (bill.state_ == BillState.marketIncomplete) { dont need this because a full order can compensate for partial order
				if (bill != b) {
					if (bill.choice_.equals(b.choice_)) {
						//if we get two incomplete orders for same produce
						//because this was a special half order we will best price for both 
						if (bill.charge_ >= b.charge_)
							b.charge_ = bill.charge_;
						else 
							bill.charge_ = b.charge_;
						
						bill.state_ = BillState.marketPending;
						b.state_ = BillState.marketPending;
					}
				}
			//}
		}
		}
	}
	
	
	void actnRemoveBill (Bill b) {
		
		bills.remove(b);
		
		//opporunity here in future implementations to keep database of payments
	}
	
	void actnSendTheBill (Bill b) {
		print("bill sent"); 
		b.state_ = BillState.delivered;
		b.w_.msgHereIsTheBill(b); 
	}
	
	protected void actnCalculateCheck (final Bill b) { 
	 
		//need to change state or else you go into a busy wait
		b.state_ = BillState.calculating; 
		
		cashierTimer.schedule(new TimerTask() {
			public void run() {
				print("calculating bill for customer");	
				actnCalculationDone(b); 
				stateChanged();
			}
		}, 750); 
	}
	
	void actnCalculationDone(Bill b) {
		
		print("calculation done"); 
		
		int quantity = 1; //hack, since we only have one food iteam for now
		b.charge_ = (prices.get(b.choice_)) * quantity;
	
		b.state_ = BillState.calculated;
		
	}
	
	void actnChargeCustomer(Bill b) {
		
		b.state_ = BillState.charging;
		
		//adding to the money that the restaurant makes
		totalMoney += b.whatCustPaid_;
		
		float tempMoney;
		tempMoney = b.whatCustPaid_ - b.charge_;
		
		if (tempMoney >= 0) {
			actnPaymentFullfilled(b); 
		}
		else {
			print("IIIIIIIIIIIIIINNNNNNNNN DDDDDEBBBBBBBBT");  
			tempMoney = (tempMoney) * -1; 
			actnPaymentNotGood(b, tempMoney);
		}
		
	}
	
	void actnPaymentFullfilled(Bill b) {
		
		b.c_.msgBillRecieved((float) 0); //0 because no money is owed
		b.state_ = BillState.paid;
		
	}
	
	void actnPaymentNotGood(Bill b, float moneyOwed) {
		
		b.c_.msgBillRecieved(moneyOwed);
		b.state_ =  BillState.inDebt;
		
	}
	
	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	
	//utilities
	
	public String getName() {
		return name_;
	}

}



