package newMarket;

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
	List<MarketRestaurantHandlerAgent> handlers = new ArrayList<MarketRestaurantHandlerAgent>();
	
	
	public static Map<String, Float> prices = new HashMap<String, Float>();

	final static float steakprice = (float) 15.99; 
	final static float chickenprice = (float) 10.99;
	final static float saladprice = (float) 5.99;
	final static float pizzaprice = (float) 8.99;
	final static float sportscarprice = (float) 150; 
	final static float suvcarprice = (float) 100;
	final static float minicarprice = (float) 90;
	
	private void initPrices() {
		prices.put("steak", steakprice); 
		prices.put("chicken", chickenprice); 
		prices.put("salad", saladprice);
		prices.put("pizza", pizzaprice);
		prices.put("sportsCar", sportscarprice); 
		prices.put("suvCar", suvcarprice);
		prices.put("miniCar", minicarprice);
	}

	
	public NewMarket() {
		initPrices();
		
		animationPanel.setPreferredSize(animationPanel.getSize());
		animationPanel.setMinimumSize(animationPanel.getSize());
		animationPanel.setMaximumSize(animationPanel.getSize());
		animationPanel.setVisible(true);
		
		MarketCashierAgent cashier = new MarketCashierAgent();
		MarketRestaurantHandlerAgent handler = new MarketRestaurantHandlerAgent();
		cashiers.add(cashier);
		handlers.add(handler);
		cashier.startThread();
		handler.startThread();
		
		
		
	}
	
	public void addCustomer(Person p) {
		MarketCustomerAgent customer = new MarketCustomerAgent(p, cashiers.get(0));
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
