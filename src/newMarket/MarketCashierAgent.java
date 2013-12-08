package newMarket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import newMarket.gui.CashierLine;
import newMarket.gui.MarketCashierGui;
import newMarket.test.mock.EventLog;
import newMarket.test.mock.LoggedEvent;
import agent.Agent;
import agents.Grocery;
import agents.Person;

public class MarketCashierAgent extends Agent {
	
	/*		Data		*/
	
	public Person self;
	
	public MarketCashierGui gui;
	
	private Semaphore atDestination = new Semaphore(0,true);
	
	//money for the cashier 
	public float money=(float)0.0;
	
	//log for unit testing
	public EventLog log = new EventLog();

	//synchronized list of orders is an array list 
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
	
	
	/*		Messages		*/
	
	/**
	 * from gui
	 */
	public void gui_msgBackAtHomeBase() {
		atDestination.release();
	}
	
	/**
	 * from customer
	 * adds to cashier's orders a new MyOrder with @param order
	 * @param c
	 * @param order
	 */
	public void msgIWantFood(MarketCustomerAgent c, List<Grocery> order) {
		orders.add(new MyOrder(order, c, OrderState.pending));
		stateChanged();
		log.add(new LoggedEvent("Received msgIWantFood."));
	}
	
	/**
	 * from customer 
	 * checks the money given against the order 
	 * @param c
	 * @param money_
	 */
	public void msgHereIsMoney(MarketCustomerAgent c, float money_) {
		synchronized(orders) {
			for (MyOrder o : orders) {
				if (o.c.equals(c) && o.s==OrderState.processing) {
					if (o.price > money_) {
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
		
		//if there is a myorder o in orders such that o.s == pending, then givePice(o)
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
		
		//if there is a myorder o in orders such that o.s == paid, the giveFood(o)
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
		
		//if there is an order o in orders such that o.s == notEnoughPaid, then kickout(o)
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
		
		/*
		synchronized(orders) {
			if (orders.isEmpty()) {
				
			}
		}
		*/
		
		return false;
	}

	/*		Action		*/
	
	//order state = processing
	//read through grocery list and add to price to make full price
	//end message to customer about the charge 
	private void givePrice(MyOrder o) {
		o.s = OrderState.processing;
		float price = 0;
		for (Grocery g : o.order) {
			price += NewMarket.prices.get(g.getFood()) * g.getAmount();
		}
		o.price = price;
		if (price >= 0) {
			o.c.msgHereIsPrice(o.order, price);
		}else {
			o.c.msgHereIsPrice(o.order, -1);
		}
	}
	
	//remove order o from orders
	//send message HereIsFood to customer
	private void giveFood(MyOrder o) {
		
		List<String> foodStrings = new ArrayList<String>();
		
		//TODO implement so he actually orders stuff
		/*
		for (Grocery g : o.order) {
			foodStrings.add(g.getFood());
		}
		*/
		
		foodStrings.add("steak");
		
		gui.DoGetThisItem(foodStrings);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		orders.remove(o);	
		o.c.msgHereIsFood(o.order);
		gui.line.exitLine(o.c.gui);
	}
	
	//remove order o from orders 
	//send message to GetOut to customer
	private void kickout(MyOrder o) {
		orders.remove(o);
		o.c.msgGetOut();
		gui.line.exitLine(o.c.gui);
	}

	public CashierLine getLine() {
		return gui.line;
	}

	public void setGui(MarketCashierGui gui) {
		this.gui = gui;
	}
	
	//utility
	public List<MyOrder> getOrders(){
		return orders;
	}
		
	public boolean hasOrders () {
		return !orders.isEmpty();
	}
}
