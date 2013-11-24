package agents;
import java.util.*;

import agents.MarketEmployeeAgent.EmployeeState;
import agents.MarketEmployeeAgent.MyCustomer;
import agents.MarketEmployeeAgent.MyOrder;

public class StoreMenu {
	
	StoreMenu() {
		initPrices();
	}

	Random generate = new Random();
	
	Map<String, Float> prices = new HashMap<String, Float>();

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
	
	public String whatFoodDoIWant() {
		
		int i = generate.nextInt(3);
		
		if (i == 0) {
			return "steak";
		}
		if (i == 1) {
			return "chicken";
		}
		if (i == 2) {
			return "salad";
		}
		return "pizza";
	}
	
	public String whatCarDoIWant() {
		
		/*
		int i = generate.nextInt(2);
		if (i == 0) {
			return "sportsCar";
		}
		if (i == 1) {
			return "suvCar";
		}
		return "miniCar";
		*/
		
		return "miniCar";
	}
	
	public float howMuchIsThat(String choice) {
	
		return prices.get(choice);
	}
}
