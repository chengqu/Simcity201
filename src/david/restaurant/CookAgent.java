package david.restaurant;

import david.restaurant.WaiterAgent;
import david.restaurant.Order;
import david.restaurant.Interfaces.Market;
import david.restaurant.gui.CookGui;
import david.restaurant.gui.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import simcity201.gui.GlobalMap;
import simcity201.interfaces.NewMarketInteraction;
import agent.Agent;
import agents.Grocery;

public class CookAgent extends Agent implements Cook, NewMarketInteraction{
        //Data
	private Timer timer = new Timer();
	
	private List<myOrder> orders = new ArrayList<myOrder>();
	private Map<String, myFood> foods = Collections.synchronizedMap(new HashMap<String, myFood>());
	private List<Market> markets = new ArrayList<Market>();
	private List<myNewRestockList> list = new ArrayList<myNewRestockList>();
	CookGui gui;
	CashierAgent cashier;
	
	Object foodLock = new Object();
	Object orderLock = new Object();
	Object requestLock = new Object();
	
	private int marketIndex = 0;
	
	private boolean orderedFood = false;
	
	enum OrderState {pending, cooked, cooking };
	
	private static int steakTime = 3000;
	private static int chickenTime = 2000;
	private static int saladTime = 1000;
	private static int pizzaTime = 4000;
	
	private static int steakLow = 3;
	private static int chickenLow = 4;
	private static int saladLow = 5;
	private static int pizzaLow = 4;
	
	private static int STEAKAMOUNT = 2;
	private static int CHICKENAMOUNT = 3;
	private static int SALADAMOUNT = 3;
	private static int PIZZAAMOUNT = 3;
	
	private static int steakMax = 5;
	private static int chickenMax = 6;
	private static int saladMax = 8;
	private static int pizzaMax = 9;
	
	public void print_()
	{
	      	for(myFood f: foods.values())
	      	{
	      		print(f.food.name + ": " + f.food.amount);
	      	}
	}
	
	public void setGui(CookGui g)
	{
		gui = g;
	}
	
	//Messages
	public CookAgent(List<Market> m, CashierAgent c)
	{
		cashier = c;
		for(Market ma: m)
        {
                markets.add(ma);
        }
	    foods.put("Steak", new myFood(new Food("Steak", STEAKAMOUNT, steakLow, steakMax, steakTime)));
        foods.put("Chicken", new myFood(new Food("Chicken", CHICKENAMOUNT, chickenLow, chickenMax, chickenTime)));
        foods.put("Salad", new myFood(new Food("Salad", SALADAMOUNT, saladLow, saladMax, saladTime)));
        foods.put("Pizza", new myFood(new Food("Pizza", PIZZAAMOUNT, pizzaLow, pizzaMax, pizzaTime)));
        DoOrderFood();
	}
	
	public void setCashier(CashierAgent c)
	{
		cashier = c;
	}
	
	public void addMarket(MarketAgent m)
	{
	        markets.add(m);
	        stateChanged();
	}
	
	public void msgHereIsAnOrder(WaiterAgent w, Order o)
	{
	        print("inmsgHereisAnOrder");
	        orders.add(new myOrder(w, o, OrderState.pending));
	        stateChanged();
	}
	
	public void msgHereIsItems(MarketAgent m, RestockList r)
	{
	        print("Got items");
	        synchronized(requestLock)
	        {
		        for(int i = 0; i < r.items.size(); i++)
		        {
		                print(r.items.get(i) + ": " + r.itemAmounts.get(i));
		        }
		        //requests.add(new myRestockList(r, false));
	        }
	        stateChanged();
	}
	
	public void gMsgOrderReady(Order o)
	{
		synchronized(orderLock)
		{
			for(int i = 0; i < orders.size(); i++)
			{
				if(o == orders.get(0).order)
				{
					orders.get(i).orderState = OrderState.cooked;
					break;
				}
			}
		}
		stateChanged();
	}
	
	public void msgCanPartiallyFill(MarketAgent m, RestockList r)
	{
	        print("Got partial message");
	        synchronized(foodLock)
	        {
		        for(myFood food: foods.values())
	            {
	                if(r.items.contains(food.food.name) == true)
	                {
	                    for(int i = 0; i < markets.size(); i++)
	                    {
                            if(m == markets.get(i))
                            {
                                    food.canFulfill.set(i, false);
                                    break;
                            }
	                    }
	                }
	            }
		        synchronized(requestLock)
		        {
		        	//requests.add(new myRestockList(r, true));
		        }
	        }
	        stateChanged();
	}
	
	public void msgHereIsPrice(List<Grocery> orders, float price) {
		cashier.msgHereIsPrice(orders, price);
	}

	
	public void msgHereIsFood(List<Grocery> orders) {
		synchronized(foodLock)
		{
			for(int i = 0; i < orders.size(); i++)
			{
				foods.get(orders.get(i).getFood()).amountLeft += orders.get(i).getAmount();
				foods.get(orders.get(i).getFood()).requested = false;
			}
		}
	}

	public void msgNoFoodForYou() {
		synchronized(foodLock)
		{
			for(myFood f: foods.values())
			{
				if(f.requested == true)
				{
					f.requested = false;
				}
			}
		}
	}
	
	public void drain()
	{
	        print("Food drained");
	        for(myFood f: foods.values())
	        {
	                f.food.amount = -1000;
	        }
	}
	
	public void drainAndOrder()
	{
	        print("Food drained & ordering");
	        for(myFood f: foods.values())
	        {
	                f.food.amount = -1000;
	        }
	        DoOrderFood();
	}
	
	//Scheduler
	public boolean pickAndExecuteAnAction() {
	        myOrder orderTemp;
	        
	        if((orderTemp = doesExistOrder(OrderState.pending)) != null)
	        {
	                DoCookOrder(orderTemp);
	                return true;
	        }
	        if((orderTemp = doesExistOrder(OrderState.cooked)) != null)
	        {
	                DoTellWaiterOrderDone(orderTemp);
	                return true;
	        }
	        return false;
	}
	
	private myOrder doesExistOrder(OrderState o)
	{
		synchronized(orderLock)
		{
	        for(myOrder temp: orders)
	        {
	                if(temp.orderState == o)
	                        return temp;
	        }
	        return null;
		}
	}
	
	//Actions
	
	void DoCookOrder(final myOrder o)
	{
	        //restock first 
	        RestockList a = new RestockList();
	        boolean increment = false;
	        print("in do cook order");
	        synchronized(foodLock)
	        {     
		        if(timer != null)
		        {
		                if(foods.get(o.order.choice).food.amount > 0)
		                {
		                        o.orderState = OrderState.cooking;
		                        foods.get(o.order.choice).food.amount--;
		                        gui.msgNewOrder(o.order, foods.get(o.order.choice).food.time);
		                }
		                else
		                {
		                        orders.remove(o);
		                        o.waiter.msgNotAvailable(o.order);
		                }
		        }
		        DoOrderFood();
	        }
	}
	
	void DoTellWaiterOrderDone(myOrder o)
	{
		orders.remove(o);
		o.waiter.msgOrderIsReady(o.order);
	}
	
	void DoOrderFood()
	{
		List<Grocery> g = new ArrayList<Grocery>();
		
		print("ordering");
		for(myFood food: foods.values())
		{
			if(food.requested == false && food.food.amount <= food.food.low)
			{
				food.requested = true;
				g.add(new Grocery(food.food.name, food.food.max - food.food.amount));
			}
			print(food.food.name + ": " + Integer.toString(food.food.max - food.food.amount));
		}
		if(g.size() > 0)
		{
			GlobalMap.getGlobalMap().marketHandler.msgIWantFood(this, g);
		}
	}
	
	private class myNewRestockList
	{
		public List<Grocery> order;
		public myNewRestockList()
		{
			order = new ArrayList<Grocery>();
		}
	}
	
	private class myOrder
	{
		public WaiterAgent waiter;
		public Order order;
		public OrderState orderState;
		public myOrder(WaiterAgent w, Order o, OrderState os)
		{
			waiter = w;
			order = o;
			orderState = os;
		}
	}
	
	private class myFood
	{
		public Food food;
		public boolean requested;
		public int amountLeft;
		public List<Boolean> canFulfill;
		public myFood(Food f)
		{
			amountLeft = 0;
			food = f;
			requested = false;
			canFulfill = new ArrayList<Boolean>();
			for(Market m: markets)
			{
				canFulfill.add(true);
			}
		}
	}

	public void msgHereIsMoney(float money) {
		GlobalMap.getGlobalMap().marketHandler.msgHereIsMoney(this, money);
	}
}
