package simcity201.interfaces;

import java.util.ArrayList;
import java.util.List;

import Market.MarketManagerAgent;

public interface MarketInteraction {
	
	public abstract void msgHereIsMarketBill(Bill b);
	
	public abstract void msgHereIsMarketFood(Order o);

	public class Order {
		public List<String> foodList = new ArrayList<String>();
		public List<Integer> foodAmounts = new ArrayList<Integer>();
	}
	
	public class Bill {
		
		private float moneyOwed;
		private MarketManagerAgent marketManager;
		
		public Bill(float money, MarketManagerAgent m) {
			moneyOwed = money;
			marketManager = m;
		}
		
		public float getCharge() {
			return moneyOwed;
		}
		
		public MarketManagerAgent getManager() {
			return marketManager;
		}
	}
	
	
	
}
