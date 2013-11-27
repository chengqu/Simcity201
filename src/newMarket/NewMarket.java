package newMarket;

import guehochoi.restaurant.Menu.Item;
import guehochoi.restaurant.Menu.Type;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agents.Person;
import animation.BaseAnimationPanel;
import Buildings.Building;

public class NewMarket extends Building {
	
	MarketAnimationPanel animationPanel = new MarketAnimationPanel();
	
	List<MarketCustomerAgent> customers = new ArrayList<MarketCustomerAgent>();
	List<MarketCashierAgent> cashiers = new ArrayList<MarketCashierAgent>();
	List<MarketDealerAgent> dealers = new ArrayList<MarketDealerAgent>();
	public List<MarketRestaurantHandlerAgent> handlers = new ArrayList<MarketRestaurantHandlerAgent>();
	
	
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
		
		animationPanel.setPreferredSize(animationPanel.getSize());
		animationPanel.setMinimumSize(animationPanel.getSize());
		animationPanel.setMaximumSize(animationPanel.getSize());
		animationPanel.setVisible(true);
		
		MarketCashierAgent cashier = new MarketCashierAgent();
		MarketRestaurantHandlerAgent handler = new MarketRestaurantHandlerAgent();
		MarketDealerAgent dealer = new MarketDealerAgent();
		cashiers.add(cashier);
		handlers.add(handler);
		dealers.add(dealer);
		cashier.startThread();
		handler.startThread();
		dealer.startThread();
		
		
		
	}
	
	public void addCustomer(Person p) {
		MarketCustomerAgent customer = new MarketCustomerAgent(p, cashiers.get(0), dealers.get(0));
		customer.setMarket(this);
		customers.add(customer);
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
	

}
