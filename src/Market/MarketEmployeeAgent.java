package Market;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity201.interfaces.MarketCustomer;
import Market.MarketManagerAgent.MyOrder;
import Market.MarketManagerAgent.MyOrderState;
import agent.Agent;
import agents.Person;

public class MarketEmployeeAgent extends Agent {

	String name;
	public Person person;
	
	MarketEmployeeAgent(String name, Person p) {
		this.name = name;
		person = p;
		initPrices(); 
	}
	
	MarketEmployeeGui gui;
	private Semaphore atDestination = new Semaphore(0,true);
	
	/**
	 * DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	 */
	
	MarketManagerAgent manager;

	enum MyOrderState {newOrder, fulfillingOrder, doneOrder, fulfilled, clear, newOutsideOrder};
	
	class MyOrder {
	    int orderID;
	    Order o_; 
	    MyOrderState s_; 
	    MyOrder(Order o, MyOrderState s) {
	    	o_ = o;
	    	s_ = s;
	    }
	}

	enum MyCustomerState {newCustomer, asked, ordered, waitingForOrder, 
		doneCustomer, paid, leaving, charging, clear, paymentNoted, aboutToGetOrder};
	
	class MyCustomer {
	    public MarketCustomer c_;
	    public MyCustomerState s_;
	    public MyOrder o_;
		public float amountPaid; 
	    MyCustomer(MarketCustomer c) {
	    	c_ = c;
	    	s_ = MyCustomerState.newCustomer;
	    	amountPaid = 0;
	    }
	}
	
	enum EmployeeState {working, wantBreak, takeBreak, onBreak, backToWork, holdForBreak}; 

	enum ItemsInMarket {steak, chicken, salad, pizza, sportsCar, suvCar, miniCar};
	
	Map<String, Float> prices = new HashMap<String, Float>();

	final static float steakprice = (float) 15.99; 
	final static float chickenprice = (float) 10.99;
	final static float saladprice = (float) 5.99;
	final static float pizzaprice = (float) 8.99;
	final static float sportscarprice = (float) 150; 
	final static float suvcarprice = (float) 100;
	final static float minicarprice = (float) 90;
	
	EmployeeState state_; 
	
	List<MyCustomer> customers = new ArrayList<MyCustomer>();
	List<MyOrder> orders = new ArrayList<MyOrder>();
	

	
	private void initPrices() {
	
		prices.put("steak", steakprice); 
		prices.put("chicken", chickenprice); 
		prices.put("salad", saladprice);
		prices.put("pizza", pizzaprice);
		prices.put("sportsCar", sportscarprice); 
		prices.put("suvCar", suvcarprice);
		prices.put("miniCar", minicarprice);
	}
	
	public void setManager(MarketManagerAgent m) {
		manager = m;
	}
	
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	/**
	 *  MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	 */
	
	//from employee gui
	public void gui_msgBackAtHomeBase() {
		print("gui_msg Back at home base"); 
		atDestination.release();
	}
	
	//from customer
	public void msgIAmYourCustomer(MarketCustomer c) {
		print("msgIAmYourCustomer called");
		
		customers.add(new MyCustomer(c));
		
		stateChanged(); 
	}

	//from customer 
	public void msgIWantThisStuff(MarketCustomer c, Order o) { //make a copy of the list on the sender side 
		print("msgIWantThisStuff called");
		
		for (MyCustomer mc : customers) {
			if (mc.c_ == c) {
				mc.s_ = MyCustomerState.ordered;
				mc.o_ = new MyOrder(o, MyOrderState.newOrder); 
				break;
			}
		}
		
		stateChanged();
		
		//
		//MyCustomer mc = customers.find(c); 
		//mc.s_ = ordered; 
		//mc.o.orderList_ = orderStuff;
		//mc.o.s_ = MyOrderState.newOrder;
	}

	//from customer 
	public void msgHereIsMyMoney(MarketCustomer c, float cash){
		print("msgHereIsMyMoney called");
		
		for (MyCustomer mc : customers) {
			if (mc.c_ == c) {
				mc.s_ = MyCustomerState.paid;
				
				mc.amountPaid = cash;
				break;
			}
		}
		
		stateChanged();
	}

	//from customer
	public void msgIAmLeaving(MarketCustomer c) {
		print("msgIAmLeaving called"); 
		
		for (MyCustomer mc : customers) {
			if (mc.c_ == c) {
				mc.s_ = MyCustomerState.leaving;
				break;
			}
		}
		
		stateChanged();
		
		//MyCustomer mc = customers.find(c);
		//mc.s_ = MyCustomerState.leaving; 
	}

	//from manager
	public void msgHereIsCustomer(MarketCustomer c) {
		print("msgHereIsCustomer called");
		
		customers.add(new MyCustomer(c)); //MyCustomerState of new Customer is newCustomer
		
		stateChanged();
	}

	//from manager
	public void msgHereIsCallInOrder(Order o) {
		print("msgHereIsCallInOrder called");
		
		orders.add(new MyOrder(o, MyOrderState.newOutsideOrder));
		
		stateChanged();
	}

	//from manager 
	public void msgBreakRequestAnswer(boolean answer) {  
		print("msgBreakRequestAnswer called");
		
	    if (answer == true) {
	        state_ = EmployeeState.takeBreak; 
	        }
	    else if (answer == false) {
	        state_ = EmployeeState.working; 
	    }    
	        
	    stateChanged();
	}
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	/**
	 *  SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER
	 */
	
	public boolean pickAndExecuteAnAction() {
		
		//If e.state == takeBreak, then,
		        //TakeBreak();
		if (state_ == EmployeeState.takeBreak) {
			actnTakeBreak();
			return true;
		}
		
		//If e.state == backToWork, then,
		        //BackToWork();
		if (state_ == EmployeeState.backToWork) {
			actnBackToWork();
			return true;
		}
		
		//If there is mc in customers such that mc.s == leaving, then 
			//CustomerLeaving();
		for (MyCustomer mc : customers) {
			if (mc.s_ == MyCustomerState.leaving) {
				mc.s_ = MyCustomerState.clear;
				actnCustomerLeaving(mc);
				return true;
			}
		}
		
		//If there is mc in customers such that mc.s == paid, then 
			//mc.c.PaymenNoted();
		for (MyCustomer mc : customers) {
			if (mc.s_ == MyCustomerState.paid) {
				mc.s_ = MyCustomerState.paymentNoted;
				mc.c_.msgPaymentNoted();
				return true;
			} 
		}
		
		//If there is mc in customers such that mc.o == fulfilled, then
			//GiveOrderAndChargeCust(mc);
		for (MyCustomer mc : customers) {
			if (mc.s_ == MyCustomerState.aboutToGetOrder) { //null pointer exception here no longer
				//mc.o_.s_ = MyOrderState.clear; 
				actnGiveOrderAndChargeCustomer(mc);
				return true;
			} 
		}
		
		//If there is mc in customers such that mc.s == ordered, then
			//FullfillCustomerOrder(mc);
		for (MyCustomer mc : customers) {
			if (mc.s_ == MyCustomerState.ordered) {
				actnFullfillCustomerOrder(mc);
				return true;
			} 
		}
		
		//If there is mc in customers such that mc.s == newCustomer, then 
			//AskForCustomerOrder(mc);
		for (MyCustomer mc : customers) {
			if (mc.s_ == MyCustomerState.newCustomer) {
				actnAskForCustomerOrder(mc);
				return true;
			} 
		}
		
		
		//If there is mo in orders such that mo.s == doneOrder, then 
			//TellManagerOrderDone(mo);
		for (MyOrder o : orders) {
			if (o.s_ == MyOrderState.doneOrder) {
				actnTellManagerOrderDone(o); 
				return true;
			} 
		}
		
		//If there is mo in orders such that mo.s == newOrder, then 
			//FullfillOutsideOrder(mo); //myorder
		for (MyOrder o : orders) {
			if (o.s_ == MyOrderState.newOutsideOrder) {
				actnFullfillOutsideOrder(o); 
				return true;
			} 
		}
		
		//If customers.isEmpty and e.s == wantBreak, then
		        //askForBeak();
		if (customers.isEmpty() && state_ == EmployeeState.wantBreak)
			actnAskForBreak();
		
		return false;
	}
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	/**
	 * ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	 */
	
	private void actnBackToWork() {        
        //manager.msgIAmHereToWork(per, e);(this); 
        state_ = EmployeeState.working;                 
	}
	
	private void actnTakeBreak() {         
        state_ = EmployeeState.onBreak;        
	}
	
	private void actnAskForBreak() {
        state_ = EmployeeState.holdForBreak;
        manager.msgCanITakeBreak(this);
	}
	
	private void actnCustomerLeaving(MyCustomer mc) {
		customers.remove(mc);
		manager.msgCustomerLeft(mc.c_); //null pointer exception here...
	}

	private void actnAskForCustomerOrder(MyCustomer mc) {
		mc.s_ = MyCustomerState.asked;
		mc.c_.msgAskForCustomerOrder(this); 
	}
	
	private void actnFullfillCustomerOrder(MyCustomer mc) {
		mc.s_ = MyCustomerState.waitingForOrder;

		simcity201.interfaces.MarketInteraction.Order customerWant 
			= mc.o_.o_.GiveMeTheWholeOrder();

		//RISKY here... passing in list that can be acessed...
		gui.DoGetThisItem(customerWant.foodList);
		
		//gui.DoGetThisItem("steak");
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mc.s_ = MyCustomerState.aboutToGetOrder;
		mc.o_.s_ = MyOrderState.fulfilled; 
	}
	
	private void actnGiveOrderAndChargeCustomer(MyCustomer mc) {
		mc.s_ = MyCustomerState.charging; 
		
		mc.c_.msgHereIsYourStuff(mc.o_.o_);
		
		float charge =  0;
		
		//get the order from the marketinteraction class
		simcity201.interfaces.MarketInteraction.Order temp = mc.o_.o_.GiveMeTheWholeOrder();
		
		for (int i=0; i < temp.foodList.size(); i++) {
			charge += prices.get(temp.foodList.get(i)) * temp.foodAmounts.get(i);
		}
		
		mc.c_.msgHereIsYourOrderCharge(charge);
	}
	
	private void actnFullfillOutsideOrder(MyOrder mo) {
		mo.s_ = MyOrderState.fulfillingOrder; 
		//fulfillOutsideOrder() //animation
		mo.s_ = MyOrderState.doneOrder;
	}

	private void actnTellManagerOrderDone(MyOrder mo) {
		manager.msgHereIsFulfilledOutsideOrder(mo.o_); 
		orders.remove(mo);
	}
	
	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	
	public void setGui(MarketEmployeeGui g) {
		gui = g;
	}
	
}
