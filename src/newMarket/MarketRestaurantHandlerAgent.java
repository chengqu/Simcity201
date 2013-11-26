package newMarket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Agent;
import agents.Grocery;
import simcity201.interfaces.*;

public class MarketRestaurantHandlerAgent extends Agent {

	private List<MyOrder> orders
	= Collections.synchronizedList(new ArrayList<MyOrder>());
	
	private class MyOrder{
		List<Grocery> order;
		NewMarketInteraction c;
		OrderState s;
		float price;
		
		MyOrder(List<Grocery> order, NewMarketInteraction c, OrderState s) {
			this.order = order;
			this.c = c;
			this.s = s;
		}
	}
	public enum OrderState { pending, processing, paid, notEnoughPaid,  };
	
	/*		Messages		*/
	
	public void msgIWantFood(NewMarketInteraction c, List<Grocery> order) {
		print("gotfood!!!");
		orders.add(new MyOrder(order, c, OrderState.pending));
		stateChanged();
	}
	
	public void msgHereIsMoney(NewMarketInteraction c, float money) {
		
		synchronized(orders) {
			for (MyOrder o : orders) {
				if (o.c.equals(c) && o.s==OrderState.processing) {
					print("Money!!!!!!!!!!!!!!!!!1");
					if (o.price > money) {
						o.s = OrderState.notEnoughPaid;
					}else {
						o.s = OrderState.paid;
					}
					break;
				}
			}
		}
		stateChanged();
	}
	
	/*		Scheduler		*/
	
	protected boolean pickAndExecuteAnAction() {
	
	MyOrder temp = null;
	
	synchronized(orders) {
		for (MyOrder o : orders) {
			if(o.s == OrderState.pending ) {
				//givePrice(o);
				//return true;
				temp = o;
				break;
			}
		}
	}	if (temp!=null) { givePrice(temp); return true; }
	

	synchronized(orders) {
		for (MyOrder o : orders) {
			if(o.s == OrderState.paid ) {
				//givePrice(o);
				//return true;
				temp = o;
				break;
			}
		}
	}	if (temp!=null) { giveFood(temp); return true; }
	

	synchronized(orders) {
		for (MyOrder o : orders) {
			if(o.s == OrderState.notEnoughPaid ) {
				//givePrice(o);
				//return true;
				temp = o;
				break;
			}
		}
	}	if (temp!=null) { kickout(temp); return true; }
	
		
		
		return false;
	}
	
	/*		Action		*/
	
	private void givePrice(MyOrder o) {
		o.s = OrderState.processing;
		float price = 0;
		for (Grocery g : o.order) {
			price += NewMarket.prices.get(g.getFood()) * g.getAmount();
		}
		o.price = price;

		if (price > 0) {
			o.c.msgHereIsPrice(o.order, price);
		}else {
			print("price!!!!!!!!!!!!!!!!!!!!!!!!!!1");
			o.c.msgHereIsPrice(o.order, -1);
		}
	}
	
	private void giveFood(MyOrder o) {
		orders.remove(o);
		print("Order!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		o.c.msgHereIsFood(o.order);
		
		
	}
	
	private void kickout(MyOrder o) {
		print("kickout");
		orders.remove(o);
		o.c.msgNoFoodForYou();
	}
}
