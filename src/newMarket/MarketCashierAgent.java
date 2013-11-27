package newMarket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import newMarket.test.mock.EventLog;
import newMarket.test.mock.LoggedEvent;
import agent.Agent;
import agents.Grocery;
import agents.Person;

public class MarketCashierAgent extends Agent {
	
	/*		Data		*/
	
	public Person self;
	
	public float money;
	
	public EventLog log = new EventLog();
	
	private List<MyOrder> orders
		= Collections.synchronizedList(new ArrayList<MyOrder>());
	
	public class MyOrder{
		public List<Grocery> order;
		public MarketCustomerAgent c;
		public OrderState s;
		public float price;
		
		MyOrder(List<Grocery> order, MarketCustomerAgent c, OrderState s) {
			this.order = order;
			this.c = c;
			this.s = s;
		}
	}
	public enum OrderState { pending, processing, paid, notEnoughPaid,  };
	
	public List<MyOrder> getOrders(){
	   return orders;
	}
	/*		Messages		*/
	
	public void msgIWantFood(MarketCustomerAgent c, List<Grocery> order) {
		orders.add(new MyOrder(order, c, OrderState.pending));
		stateChanged();
		log.add(new LoggedEvent("Received msgIWantFood."));
	}
	
	public void msgHereIsMoney(MarketCustomerAgent c, float money_) {
		synchronized(orders) {
			for (MyOrder o : orders) {
				if (o.c.equals(c) && o.s==OrderState.processing) {
					if (o.price > money) {
						o.s = OrderState.notEnoughPaid;
						log.add(new LoggedEvent("Received msgHereIsMoney, but Customer couldn't Pay"));
					}else {
						o.s = OrderState.paid;
						money+=money_;
						log.add(new LoggedEvent("Received msgHereIsMoney, and Customer Paid"));
					}
					break;
				}
			}
		}
		stateChanged();
	}
	
	/*		Scheduler		*/
	
	public boolean pickAndExecuteAnAction() {
		
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
			o.c.msgHereIsPrice(o.order, -1);
		}
	}
	
	private void giveFood(MyOrder o) {
		orders.remove(o);
		o.c.msgHereIsFood(o.order);
		
	}
	
	private void kickout(MyOrder o) {
		orders.remove(o);
		o.c.msgGetOut();
	}
	
}
