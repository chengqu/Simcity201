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

import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;
import newMarket.gui.MarketCashierGui;
import newMarket.gui.MarketCustomerGui;
import newMarket.gui.MarketDealerGui;
import agents.Grocery;
import agents.Person;
import agents.Role;
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
	
	//map of inventory
	public static Map<String, FoodStock> inventory = new HashMap<String, FoodStock>();
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
	
	final int steakamount = 300; 
	final int chickenamount = 300;
	final int saladamount = 300;
	final int pizzaamount = 300;
	final int sportscaramount = 20; 
	final int suvcaramount = 20;
	final int minicaramount = 20;
	final int beefamount = 300;
	final int turkeyamount =  300;
	final int porkamount =  300;
	final int duckamount = 300;
	
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

	private void initInventory() {
		inventory.put("steak", new FoodStock("steak", steakamount, 30, 50)); 
		inventory.put("chicken", new FoodStock("chicken", chickenamount, 30, 50)); 
		inventory.put("salad", new FoodStock("salad", saladamount, 30, 50));
		inventory.put("pizza", new FoodStock("pizza", pizzaamount, 30, 50));
		inventory.put("beef", new FoodStock("beef", beefamount, 30, 50));
		inventory.put("turkey", new FoodStock("turkey", turkeyamount, 30, 50));
		inventory.put("pork", new FoodStock("pork", porkamount, 30, 50));
		inventory.put("duck", new FoodStock("duck", duckamount, 30, 50));
		inventory.put("sportsCar", new FoodStock("sportsCar", sportscaramount, 4, 10)); 
		inventory.put("suvCar", new FoodStock("suvCar", suvcaramount, 4, 10));
		inventory.put("miniCar", new FoodStock("miniCar", minicaramount, 4, 10));
	}
	
	/**
	 * A class to help manage the inventory of the restaurant.  This will be what is 
	 * returned form the inventory map that is park of newMarket
	 * @author Josh
	 */
	public class FoodStock {
		String name;
		int amount; 
		int lowThresh;
		int reStock;
		public FoodStock (String name, int amount, int lowThresh, int reStock) {
			this.name = name;
			this.amount = amount;
			this.lowThresh = lowThresh;
			this.reStock = reStock;
		}
		public int getAmount() {
			return amount;
		}
		public int getLowThresh() {
			return lowThresh;
		}
		public int getReStock() {
			return reStock;
		}
		public void decreaseStockBy(int amount) {
			if (this.amount - amount >= 0)
				this.amount -= amount;
		}
		public void increaseStockBy(int amount) {
			this.amount += amount;
		}
		public void reStockThis() {
			this.amount += this.reStock;
		}
		public boolean isStockBelowThreshhold() {
			if (this.amount < this.lowThresh) {
				return true;
			}
			else 
				return false;
		}
		public boolean isStockBelowIfRemove(int amount) {
			if (this.amount - amount < this.lowThresh) {
				return true;
			}
			else
				return false;
		}
	}
	
	
	public NewMarket() {
		initPrices();
		initInventory();
		
		//need to set animation panel size to make show up...
		animationPanel.setPreferredSize(animationPanel.getSize());
		animationPanel.setMinimumSize(animationPanel.getSize());
		animationPanel.setMaximumSize(animationPanel.getSize());
		animationPanel.setVisible(true);
		
		MarketCashierAgent cashier = new MarketCashierAgent();
		MarketRestaurantHandlerAgent handler = new MarketRestaurantHandlerAgent();
		MarketDealerAgent dealer = new MarketDealerAgent();
		MarketCashierAgent cashier2 = new MarketCashierAgent();
		
		MarketDealerGui dealerGui = new MarketDealerGui(dealer);
		dealer.setGui(dealerGui);
		animationPanel.addGui(dealerGui);
		
		MarketCashierGui cashierGui2 = new MarketCashierGui(cashier2);
		cashier2.setGui(cashierGui2);
		animationPanel.addGui(cashierGui2);
		
		MarketCashierGui cashierGui = new MarketCashierGui(cashier);
		cashier.setGui(cashierGui);
		animationPanel.addGui(cashierGui);
		
		cashiers.add(cashier);
		cashiers.add(cashier2); 
		handlers.add(handler);
		dealers.add(dealer);
		
		cashier2.startThread();
		cashier.startThread();
		handler.startThread();
		dealer.startThread();
		
		Timer timer = new Timer();
		
		//test task
		timer.schedule(new TimerTask() {
			public void run() {
				Person p = new Person("BLAH 1");
				p.currentTask = new Task(Objective.goTo, "market");
				p.currentTask.sTasks.add(specificTask.buyGroceries);
				p.money = 10000;
				p.homefood.add(new Grocery("Steak", 1));
				p.homefood.add(new Grocery("Steak", 1));
				p.homefood.add(new Grocery("Chicken", 1));
				p.homefood.add(new Grocery("Salad", 1));
				p.homefood.add(new Grocery("Chicken", 1));
				p.homefood.add(new Grocery("Salad", 1));
				p.homefood.add(new Grocery("Chicken", 1));
				p.homefood.add(new Grocery("Duck", 1));
				p.homefood.add(new Grocery("Duck", 300));
				addCustomer(p);
			}
		}, 4000);
		
		//test task
		timer.schedule(new TimerTask() {
			public void run() {
				
				Person p = new Person("BLAH 1");
				p.currentTask = new Task(Objective.goTo, "market");
				p.currentTask.sTasks.add(specificTask.buyGroceries);
				p.money = 100; 
				p.homefood.add(new Grocery("Chicken", 1));
				addCustomer(p);
				
				
				Person g = new Person("BLAH 1");
				g.currentTask = new Task(Objective.goTo, "market");
				g.currentTask.sTasks.add(specificTask.buyCar);
				g.money = 200;
				g.homefood.add(new Grocery("Salad", 1));
				addCustomer(g);
				
				Person h = new Person("BLAH 1");
				h.currentTask = new Task(Objective.goTo, "market");
				h.currentTask.sTasks.add(specificTask.buyCar);
				h.money = 200;
				h.homefood.add(new Grocery("Pizza", 1));
				addCustomer(h);
				
			}
		}, 6550);
		
	}
	
	public void addCustomer(Person p) {
		MarketCustomerAgent customer = new MarketCustomerAgent(p, null, dealers.get(0));
		customer.setMarket(this);
		customers.add(customer);
		
		//set up GUI stuff
		MarketCustomerGui gui = new MarketCustomerGui(customer);
		customer.setGui(gui);
		animationPanel.addGui(gui);
		
		customer.startThread();
		
		AlertLog.getInstance().logMessage(AlertTag.Market, this.name, "add customer called");
		//System.out.println("MARKET: ADD CUSTOMER CALLED");
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
			AlertLog.getInstance().logMessage(AlertTag.Market, 
					this.name, "there are no customers in the market dude, probably going to mess things up"); 
			//System.out.println("there are no customer in the market dude.");
			return null;
		}
		
		//find employee with least number of customers by iterating through list
		int leastCust = cashiers.get(0).gui.howManyCustInLine();
		
		if (leastCust == 0) {
			return cashiers.get(0);
		}
		
		for (MarketCashierAgent a : cashiers){
			if (a.gui.howManyCustInLine() < leastCust) {
				leastCust = a.gui.howManyCustInLine();
			}
		}
		
		//find the first cashier with the least # of customers and pick him
		for (MarketCashierAgent a : cashiers){
			if (a.gui.howManyCustInLine() <= leastCust) {		
				//System.out.println("go to: " + a);
				AlertLog.getInstance().logMessage(AlertTag.Market, 
						this.name, "go to this cashier: " + a.toString());
				return a;
			}
		}	
		AlertLog.getInstance().logMessage(AlertTag.Market, 
				this.name, "error in findLeastBusyCashier"); 
		//System.out.println("ERROR in choose least busy cashier");
		return null;
	}


	@Override
	public Role wantJob(Person p) {
		// TODO Auto-generated method stub
		return null;
	}

}
