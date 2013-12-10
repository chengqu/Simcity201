package simcity201.interfaces;

import java.util.ArrayList;
import java.util.List;

import agents.Grocery;
import newMarket.MarketRestaurantHandlerAgent;

public interface NewMarketInteraction {
	
	public abstract void msgHereIsPrice(List<Grocery> orders, float price);
	
	public abstract void msgHereIsFood(List<Grocery> orders);
	
	public abstract void msgNoFoodForYou();

	public abstract void msgOutOfStock();
	
	public abstract String getName();
	
	public class Bill {
		
		public MarketRestaurantHandlerAgent handler;
		public float moneyOwed;
		
		public Bill(MarketRestaurantHandlerAgent handler, float moneyOwed) {
			this.handler = handler;
			this.moneyOwed = moneyOwed;
		}
	}	
}
