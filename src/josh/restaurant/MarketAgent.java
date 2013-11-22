package josh.restaurant;

import agent.Agent;
//import restaurant.CookAgent.MyOrder;








import java.util.*;

import josh.restaurant.CookAgent;
import josh.restaurant.interfaces.Cook;
import josh.restaurant.interfaces.Market;


public class MarketAgent extends Agent implements Market {
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	List<MyMarketOrder> orders = Collections.synchronizedList(new ArrayList<MyMarketOrder>());
	String name_;
	
	CashierAgent cashier;
	
	float moneyPot = 0;
	//CookAgent cook_; 
	
	public class MyMarketOrder {
			int partialAmount = 0;
			Cook c_;
			String choice_; 
			MarketOrderState state_;
			public boolean payment_; 
			MyMarketOrder(Cook w, String choice, MarketOrderState state) {
				c_ = w;
				choice_ = choice;
				state_ = state; 
				payment_ = false;
			}
	}
	
	enum MarketOrderState {pending, confirmed, processing, cantProcess, complete, waitingForPayment, finallyPaid}; 
	
	Timer marketTimer = new Timer();
	
	Map<String, MarketFood> foodInventory = new HashMap<String, MarketFood>(); 
	
	private class MarketFood {
		String type_;
		int foodStock_; 
		MarketFood(String choice, int foodStock) {
			type_ = choice; 
			foodStock_ = foodStock; 
		}
	}
	
	final static float steakprice = (float) 15.99; 
	final static float chickenprice = (float) 10.99;
	final static float saladprice = (float) 5.99;
	final static float pizzaprice = (float) 8.99;
	
	//map of the prices of the food
	Map <String, Float > prices = new HashMap<String, Float>();
	
	void initPrices() {
		
		prices.put("steak", steakprice); 
		prices.put("chicken", chickenprice); 
		prices.put("salad", saladprice);
		prices.put("pizza", pizzaprice);
		
	}
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	public MarketAgent(String name) {
		super();
		this.name_ = name;
		initFoodInventory(); 
		initPrices();
	}
	
	private void initFoodInventory() {
		
		//need to init the market differently than the cook....
		
		foodInventory.put("steak", new MarketFood("steak", 30)); 
		foodInventory.put("chicken", new MarketFood("chicken", 30)); 
		foodInventory.put("pizza", new MarketFood("pizza", 30)); 
		foodInventory.put("salad", new MarketFood("salad", 30)); 
	}
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 

	//from cashier
	public void msgHereIsYourPayment(float monies, String choice) {

		synchronized (orders) {
		for (MyMarketOrder o : orders) {
			if (o.payment_ == false && o.choice_.equals(choice)) {
				o.payment_ = true;
				moneyPot += monies;
				o.state_ = MarketOrderState.finallyPaid;
			}
			break;
		}
		stateChanged();
		}
	}
	
	
	public void msgOrderProduceFromMarket (Cook w, String choice) {
		//(CookAgent w, String choice, orderState state)
		orders.add(new MyMarketOrder(w, choice, MarketOrderState.pending));
		print("recieved a market order for " + choice);
		stateChanged();
	}


	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	public boolean pickAndExecuteAnAction() {
		
		//check inventroy despite any orders
		if (orders.isEmpty())
			inventoryCheck(); 
		
		synchronized(orders) {
		for (MyMarketOrder o: orders) {
			if (o.state_ == MarketOrderState.finallyPaid) {
				actnRemoveOrder(o); 
				return true; 
			}
		}
		}
		
		synchronized (orders){
		//If there exists an order in orders such that o.s = pending, then, CheckInventroy(o); // actions
		for (MyMarketOrder o: orders) {
			if (o.state_ == MarketOrderState.pending) {
				actnCheckInventory(o); 
				return true; 
			}
		}
		}
		
		synchronized (orders) {
		//If there exists an order in orders such that o.s = pending then, FulfillOrder(o); //action
		for (MyMarketOrder o : orders) {
			if (o.state_ == MarketOrderState.confirmed) {  // orderState.confirme
				print("gonna get this from my farm suppliers");
				actnFulfillOrder(o); 
				return true;
			}
		}
		}
		
		synchronized (orders) {
		//If there exists an order in orders such that o.s = cantCook, then TellCookCantProcess
		for (MyMarketOrder o : orders) {
			if (o.state_ == MarketOrderState.cantProcess) {  // orderState.confirmed
				print("cant order more of this food... !!!!!!!!!!!!!");
				actnTellCookCantProcess(o);
				return true;
			}
		}
		}
		
		synchronized (orders) {
		//If there exists an order in orders such that o.s = cantCook, then delivermore food!
		for (MyMarketOrder o : orders) {
			if (o.state_ == MarketOrderState.complete) {  
				actnDeliverOrder(o);
				return true;
			}
		}
		}
						
		//*************************************************
		
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER

	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 

	void actnRemoveOrder(MyMarketOrder o) {
		orders.remove(o); 
	}
	
	void actnTellCookCantProcess(MyMarketOrder o) {
		
		o.c_.msgNeedToOrderFromBackup(this, o.choice_);	
		orders.remove(o); 
	}
	
	void inventoryCheck() {
		
		for (String key : foodInventory.keySet()) {
			int currentStock = foodInventory.get(key).foodStock_; 
			print("The market has " + currentStock + " " + key);
		}
	}
	
	
	void actnCheckInventory(MyMarketOrder o) {
		
		//ordering control comes from the cook
		
		int currentStock = foodInventory.get(o.choice_).foodStock_;  
		
		//deliver food 5 units at a time, if less than or equal to 4, cannot process
		if (currentStock <= 0) {
			//can't make the food!!!! 
			o.state_ = MarketOrderState.cantProcess;  
		}
		else if (currentStock <= 4) {
			o.state_ = MarketOrderState.confirmed;
		}
		else {
			o.state_ = MarketOrderState.confirmed; 
		}
	}
	
	void actnFulfillOrder(final MyMarketOrder o) {
		
		if (o.state_ == MarketOrderState.confirmed)
			foodInventory.get(o.choice_).foodStock_-=5; 
		else {
			o.partialAmount = foodInventory.get(o.choice_).foodStock_;
			foodInventory.get(o.choice_).foodStock_ = 0;
		}
			
			//DoOrder(o); //animation 
			o.state_ = MarketOrderState.processing; 
			marketTimer.schedule(new TimerTask() {
				public void run() {
					print("fullfilling order");
					actnOrderDone(o); 
					stateChanged();
				}
			}, 20000) ; //* (foodMap.get(o.choice_)).time_); 
	}	
	
	void actnOrderDone(MyMarketOrder o) {
		o.state_ = MarketOrderState.complete; 
		
		float charge;
		
		if (o.partialAmount > 0) {
			charge = (prices.get(o.choice_) * o.partialAmount);
		}
		else {
			charge = (prices.get(o.choice_) * 5);
		}
		
		
		if(o.partialAmount > 0) 
			cashier.msgBillFromTheMarket(this, o.choice_, charge , false);  // if the order is incomplete...
		else
			cashier.msgBillFromTheMarket(this, o.choice_, charge, true);

		//msgBillFromTheMarket(MarketAgent m, String produce, float chargeAmount, boolean complete)
		
	}
	
	void actnDeliverOrder(MyMarketOrder o) {
		
		print("Going To Deliver Order"); 
		
		o.c_.msgMarketOrderDone(o.choice_);
		
		if (o.payment_ == true)
			orders.remove(o);
		else {
			o.state_ = MarketOrderState.waitingForPayment;
		}
	}
	

	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	
	//utilities
	
	public String getName() {
		return name_;
	}

	public void setCashier(CashierAgent c) {
		cashier = c;
	}
	
}


