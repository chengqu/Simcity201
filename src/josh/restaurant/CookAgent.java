package josh.restaurant;

import agent.Agent;
import agents.MonitorSubscriber;
import agents.Person;
import agents.ProducerConsumerMonitor;

import java.util.*;
import java.util.concurrent.Semaphore;

import josh.restaurant.CustomerAgent.AgentEvent;
import josh.restaurant.gui.CookOrderGui;
import josh.restaurant.gui.HostGui;
import josh.restaurant.interfaces.Cook;
import josh.restaurant.interfaces.Market;
import josh.restaurant.interfaces.Waiter;
//import newMarket.MarketRestaurantHandlerAgent;


public class CookAgent extends Agent implements Cook , MonitorSubscriber{
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	Person person = null;
	
	List<MyOrder> myOrders = Collections.synchronizedList(new ArrayList<MyOrder>());
	
	public List<OrderIcon> myOrderIcons = new ArrayList<OrderIcon>(); 
	
	String name_;
	
	public List<Market> markets = new ArrayList<Market>();
	
	ProducerConsumerMonitor<MyOrder> monitor;
	
	//original market that cook is loyal too
	Market market_;
	
	public class OrderIcon {
		CookOrderGui gui_;
		int position_;
		String whatIsIt;
		int numberOnGrill;
		boolean cookingNow; 
		public OrderIcon(CookOrderGui gui, int position) {
			gui_ = gui;
			position_ = position; //1 - 5 
			cookingNow = false;
		}	
	}
	
	public static class MyOrder {
			Waiter w_;
			private String choice_; 
			int table_;
			orderState state_; 
			OrderIcon i_;
			public List<Market> invalidMarkets;
			MyOrder(Waiter w, String choice, int table, orderState state) {
				w_ = w;
				choice_ = choice;
				table_ = table;
				state_ = state; 
			}
			MyOrder(String choice, orderState state, Market m) {
				choice_ = choice;
				state_ = state;
				invalidMarkets = new Vector<Market>();
				invalidMarkets.add(m); 
			}

			public String getChoice_() {
				return choice_;
			}

			public void addOrderIcon(OrderIcon i) {
				i_ = i;	
			}
	}
	
	enum orderState {pending, confirmed, cantCook, cooking, complete, orderingFromBackup, contactingBackup}; 
	
	Timer cookTimer = new Timer();
	
	public Map<String, Food> foodMap = new HashMap<String, Food>(); 
	
	public class Food {
		public String type_;
		public int time_; 
		float price_; 
		public int foodStock_; 
		int lowThresh_;
		int reStock_;
		boolean orderedRestock_;
		Food(String choice, int time, int foodStock, int lowThresh, int reStock) {
			type_ = choice;
			time_ = time;
			foodStock_ = foodStock;
			lowThresh_ = lowThresh;
			reStock_ = reStock;
			orderedRestock_ = false;
		}
	}
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	public void setCookOrderGui(CookOrderGui o, int position) {
		myOrderIcons.add(new OrderIcon(o, position));
	}
	
	public CookAgent(String name, ProducerConsumerMonitor<MyOrder> m) {
		super();
		this.name_ = name;
		this.monitor = m;
		initFoodMap(); 
	}
	
	//constructor with second argument 
	public CookAgent(String name, Person p) {
		super();
		this.name_ = name;
		person = p;
		initFoodMap(); 
	}
	
	private void initFoodMap () {
		
	 	//Food(String choice, int time, int foodStock, int lowThresh, int reStock)
		foodMap.put("Steak", new Food("Steak", 5, 10, 3, 7)); 
		foodMap.put("Chicken", new Food("Chicken", 4, 10, 3, 8)); 
		foodMap.put("Salad", new Food("Salad", 1, 12, 4, 7));
		foodMap.put("Pizza", new Food("Pizza", 2, 2, 6, 8));
		
	}
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	//from cook
	public void msgNeedToOrderFromBackup(Market m, String choice) {
		
		synchronized (myOrders) {
		myOrders.add(new MyOrder(choice, orderState.orderingFromBackup, m)); 
		stateChanged();
		}
	}
	
	//from market
	public void msgMarketOrderDone(String food) {
		
		print("ORDER RECIEVED from market"); 
		foodMap.get(food).foodStock_ += 5;
		foodMap.get(food).orderedRestock_ = false; 
		stateChanged(); 
	}
	
	//from waiter
	public void msgHereIsAnOrder (Waiter w, Order o) {
		
		synchronized (myOrders) {
		myOrders.add(new MyOrder(w, o.choice_, o.tableNum_, orderState.pending));
		print("recieved an order for " + o.choice_);
		stateChanged();
		}
	}
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	public boolean pickAndExecuteAnAction() {
		
		if (myOrders.isEmpty())
			actnRoutineCheckInventory();
		
		synchronized (myOrders) {
		for (MyOrder o: myOrders) {
			if (o.state_ == orderState.orderingFromBackup) {
				actnOrderFromBackup(o); 
				return true; 
			}
		}
		}
		
		MyOrder temp;
		if((temp = monitor.remove()) != null)
		{
			if(temp.state_ == orderState.pending)
			{
				myOrders.add(temp);
				actnCheckInventory(temp);
				return true;
			}
		}
		
		synchronized (myOrders) {
		//If there exists an order in orders such that o.s = pending, then, CheckInventroy(o); // actions
		for (MyOrder o: myOrders) {
			if (o.state_ == orderState.pending) {
				actnCheckInventory(o); 
				return true; 
			}
		}
		}
		
		synchronized (myOrders) {
		//If there exists an order in orders such that o.s = done, then, PlateIt(o); //action 
		for (MyOrder o: myOrders) {
			if (o.state_ == orderState.complete) {
				actnPlateIt(o);
				return true;
			}
		}
		}
		
		synchronized (myOrders) {
		//If there exists an order in orders such that o.s = pending then, CookIt(o); //action
		for (MyOrder o : myOrders) {
			if (o.state_ == orderState.confirmed) {  // orderState.confirmed
				print("gonna cook it");
				actnCookIt(o); 
				return true;
			}
		}
		}
		
		synchronized (myOrders) {
		//If there exists an order in orders such that o.s = cantCook, then TellWaiterBadOrder
		for (MyOrder o : myOrders) {
			if (o.state_ == orderState.cantCook) {  // orderState.confirmed
				print("cant cook it");
				actnTellWaiterBadOrder(o); 
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

	void actnOrderFromBackup(MyOrder o) {
		
		o.state_ = orderState.contactingBackup;
		for (Market m : markets) {
			//if the list of invalid markets does not contain m
			if (!o.invalidMarkets.contains(m)) {
				m.msgOrderProduceFromMarket(this, o.choice_);
				return;
			}
		}
		print("problem with ordering more from backup"); 
	}
	
	void actnTellWaiterBadOrder(MyOrder o) {
		print("I cant cook this" + o.getChoice_() + " because im out of food"); 
	
		o.w_.msgBadOrder(o.getChoice_(), o.table_); 
		
		
		myOrders.remove(o);

	}
	
	void actnRoutineCheckInventory() {
		
		for (String key : foodMap.keySet()) {
			int currentStock = foodMap.get(key).foodStock_; 
			if (currentStock <= foodMap.get(key).lowThresh_ && 
					foodMap.get(key).orderedRestock_ == false) {
				// go order some food homie!!!!
				//mimic the methods from actnOrderMoreFood
				foodMap.get(key).orderedRestock_ = true;
				market_.msgOrderProduceFromMarket(this, key);
			}
		}
	}
	
	void actnCheckInventory(MyOrder o) {
		
		int currentStock = foodMap.get(o.getChoice_()).foodStock_;  
		
		if (currentStock <= 0) {
			//can't make the food!!!! 
			o.state_ = orderState.cantCook;  
		}
		else if (currentStock <= foodMap.get(o.getChoice_()).lowThresh_) {
			// go order some food homie!!!!
			actnOrderMoreFood(o); 
		
			o.state_ = orderState.confirmed;
		}
		else {
			o.state_ = orderState.confirmed; 
		}
	}
	
	void actnOrderMoreFood(MyOrder o) {
		
		if (foodMap.get(o.getChoice_()).orderedRestock_ == false) {
			foodMap.get(o.getChoice_()).orderedRestock_ = true;
			market_.msgOrderProduceFromMarket(this, o.getChoice_());
		}
		else 
			print("I'm already ordering this food: " + o.getChoice_()); 
	}
	
	//need to add this because cannot alter variables when the MyOrder o is a static variable...
	void addOrderIcon(MyOrder o, OrderIcon i) {
		o.addOrderIcon(i);
	}
	
	void actnCookIt (final MyOrder o) {
	
		//decrement our inventory 
		foodMap.get(o.getChoice_()).foodStock_--;  
		
		//display the order cooking 
		for (OrderIcon i : myOrderIcons) {
			if (i.gui_.isPresent() == false) { 
				i.gui_.displayOrderCooking(o, i.position_);
				//see short method above...seems like an eclipse thingy...
				addOrderIcon(o, i);
				break; 
			}
		}
		
		//DoCooking(o); //animation 
		o.state_ = orderState.cooking; 
		cookTimer.schedule(new TimerTask() {
			public void run() {
				print("cooking stuff");
				foodDone(o); 
				stateChanged();
			}
		}, 2000 * (foodMap.get(o.getChoice_())).time_); 
	}
	
	void actnPlateIt(MyOrder o) {
		print ("yo waiter, pick up food for table: " + o.table_);
		o.w_.msgOrderIsReady(o.getChoice_(), o.table_, o.i_);
		myOrders.remove(o); 
	}
	
	private void foodDone(MyOrder o) {
		//sorry for this super weird call...
		o.i_.gui_.displayOrderReady(o, o.i_.position_);
		print("ring ring ring food done"); 
		o.state_ = orderState.complete;
	}

	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	
	//utilities
	
	public String getName() {
		return name_;
	}

	public void setMarket(Market market) {
		market_ = market; 
	}

	public void clearInventory() {
		
		print("rotten stuff, clearing my inventory of salad");
		foodMap.get("salad").foodStock_ = 0;
		
	}

	public void addMarket(Market m) {
		markets.add(m); 
	}

	@Override
	public void canConsume() {
		stateChanged();
	}

	

}

