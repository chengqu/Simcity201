package newMarket;

import guehochoi.restaurant.Menu.Item;
import guehochoi.restaurant.Menu.Type;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import newMarket.gui.MarketCashierGui;
import newMarket.gui.MarketCustomerGui;
import agents.Person;
import agents.Task;
import agents.Task.Objective;
import agents.Task.specificTask;
import animation.BaseAnimationPanel;
import Buildings.Building;

public class NewMarket extends Building {
	
	//the main animation panel
	MarketAnimationPanel animationPanel = new MarketAnimationPanel();
	
	List<MarketCustomerAgent> customers = new ArrayList<MarketCustomerAgent>();
	List<MarketCashierAgent> cashiers = new ArrayList<MarketCashierAgent>();
	List<MarketDealerAgent> dealers = new ArrayList<MarketDealerAgent>();
	public List<MarketRestaurantHandlerAgent> handlers = new ArrayList<MarketRestaurantHandlerAgent>();
	
	//map of prices, has steak, chicken, salad, pizza, sportscar, minicar, beef, turkey, pork, and duck
	public static Map<String, Float> prices = new HashMap<String, Float>();

	final static float steakprice = (float) 15.99; 
	final static float chickenprice = (float) 10.99;
	final static float saladprice = (float) 5.99;
	final static float pizzaprice = (float) 8.99;
	final static float sportscarprice = (float) 150; 
	final static float suvcarprice = (float) 100;
	final static float minicarprice = (float) 90;
	final static float beefprice = 12.99f;
	final static float turkeyprice =  11.99f;
	final static float porkprice =  10.99f;
	final static float duckprice = 16.99f;
	
	//basic init function, called at beginning of contructor 
	private void initPrices() {
		prices.put("Steak", steakprice); 
		prices.put("Chicken", chickenprice); 
		prices.put("Salad", saladprice);
		prices.put("Pizza", pizzaprice);
		prices.put("Beef", beefprice);
		prices.put("Turkey", turkeyprice);
		prices.put("Pork", porkprice);
		prices.put("Duck", duckprice);
		prices.put("SportsCar", sportscarprice); 
		prices.put("SuvCar", suvcarprice);
		prices.put("MiniCar", minicarprice);
	}

	
	public NewMarket() {
		initPrices();
		
		//need to set animation panel size to make show up...
		animationPanel.setPreferredSize(animationPanel.getSize());
		animationPanel.setMinimumSize(animationPanel.getSize());
		animationPanel.setMaximumSize(animationPanel.getSize());
		animationPanel.setVisible(true);
		
		MarketCashierAgent cashier = new MarketCashierAgent();
		MarketRestaurantHandlerAgent handler = new MarketRestaurantHandlerAgent();
		MarketDealerAgent dealer = new MarketDealerAgent();
		
		MarketCashierGui cashierGui = new MarketCashierGui(cashier);
		cashier.setGui(cashierGui);
		animationPanel.addGui(cashierGui);
		
		cashiers.add(cashier);
		handlers.add(handler);
		dealers.add(dealer);
		
		cashier.startThread();
		handler.startThread();
		dealer.startThread();
		
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {
			public void run() {
				Person p = new Person("BLAH 1");
				p.currentTask = new Task(Objective.goTo, "market");
				p.currentTask.sTasks.add(specificTask.buyGroceries);
				addCustomer(p);
				
				Person g = new Person("BLAH 1");
				g.currentTask = new Task(Objective.goTo, "market");
				g.currentTask.sTasks.add(specificTask.buyGroceries);
				addCustomer(g);
				
				Person h = new Person("BLAH 1");
				h.currentTask = new Task(Objective.goTo, "market");
				h.currentTask.sTasks.add(specificTask.buyGroceries);
				addCustomer(h);
			}
		}, 4000);
		
		timer.schedule(new TimerTask() {
			public void run() {
				Person p = new Person("BLAH 1");
				p.currentTask = new Task(Objective.goTo, "market");
				p.currentTask.sTasks.add(specificTask.buyGroceries);
				addCustomer(p);
				
				Person g = new Person("BLAH 1");
				g.currentTask = new Task(Objective.goTo, "market");
				g.currentTask.sTasks.add(specificTask.buyGroceries);
				addCustomer(g);
				
				Person h = new Person("BLAH 1");
				h.currentTask = new Task(Objective.goTo, "market");
				h.currentTask.sTasks.add(specificTask.buyGroceries);
				addCustomer(h);
			}
		}, 4750);
	}
	
	public void addCustomer(Person p) {
		MarketCustomerAgent customer = new MarketCustomerAgent(p, cashiers.get(0), dealers.get(0));
		customer.setMarket(this);
		customers.add(customer);
		
		//set up gui stuff
		MarketCustomerGui gui = new MarketCustomerGui(customer);
		customer.setGui(gui);
		animationPanel.addGui(gui);
		
		customer.startThread();
	}
	
	public void removeCustomer(MarketCustomerAgent c) {
		c.stopThread();
		customers.remove(c);
	}
	
	@Override
	public BaseAnimationPanel getAnimationPanel() {
		return this.animationPanel;
	}


	public MarketCashierAgent findLeastBusyCashier() {
		
		if (cashiers.isEmpty()) {
			System.out.println("there are no customer in the market dude.");
			return null;
		}
		
		//find employee with least number of customers by iterating through list
		int leastCust = cashiers.get(0).gui.howManyCustInLine();
		if (leastCust == 0) {
			return cashiers.get(0);
		}
		
		for (MarketCashierAgent a : cashiers){
			if (a.gui.howManyCustInLine() < leastCust)
				leastCust = a.gui.howManyCustInLine();
		}
		//find the first cashier with the least # of customers and pick him
		for (MarketCashierAgent a : cashiers){
			if (a.gui.howManyCustInLine() <= leastCust) {		
				return a;
			}
		}		
		return null;
	}

}
