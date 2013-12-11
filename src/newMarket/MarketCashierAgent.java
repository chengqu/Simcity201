package newMarket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;
import newMarket.gui.Line;
import newMarket.gui.MarketCashierGui;
import newMarket.test.mock.EventLog;
import newMarket.test.mock.LoggedEvent;
import agent.Agent;
import agents.Grocery;
import agents.Person;

public class MarketCashierAgent extends Agent {
	
	public MarketCashierAgent() {	
	}
	
	public MarketCashierAgent(Person p) {
		this.self = p;
	}
	
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
		//print("msgIWantFood called");
		AlertLog.getInstance().logMessage(AlertTag.MarketCashier, 
				this.getName(), "msgHereIWantFood called" );
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
		//print("msgHereIsMoney called");
		AlertLog.getInstance().logMessage(AlertTag.MarketCashier, 
				this.getName(), "msgHereIsMoney called" );
		synchronized(orders) {
			for (MyOrder o : orders) {
				if (o.c.equals(c) && o.s==OrderState.processing) {
					if (o.price > money_) {
						o.s = OrderState.notEnoughPaid;
						print("customer couldn't pay");
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
		
		try {
		
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
		
		} catch (ConcurrentModificationException e) {
			return false;
		}
		
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
			if (NewMarket.prices.get(g.getFood()) == null) {
				print("in give price, I don't recognize that food: " + g.getFood());
				continue;
			}
			print(g.getFood());
			print(NewMarket.prices.get(g.getFood()).toString() + " * " +  g.getAmount());
			
			price += NewMarket.prices.get(g.getFood()) * g.getAmount();
		}
		
		if (price > 150)
			o.price = 150;
		else
			o.price = price;
		
		print("The price in GivePrice is " + price); 
		print("The price I am giving is " + o.price);
		
		if (price > 0) {
			o.c.msgHereIsPrice(o.order, price);
		}else {
			//this is the message we send for bad order.
			//we won't be able to go through with the transaction
			o.c.msgHereIsPrice(o.order, -1);
			kickout(o);
			print("Hello " + o.c + ", unfortunately due to price misunderstanding");
			print("this is that weird price: " + price);
		}
	}
	
	//remove order o from orders
	//send message HereIsFood to customer
	synchronized private void giveFood(MyOrder o) {
		
		List<String> foodStrings = new ArrayList<String>();
		
		//cashier GUI will use lower case strings 
		for (Grocery g : o.order) {
			//check if we can even recognize the food or not...
			if ((g.getFood()) == null) {
				continue;
			}
			foodStrings.add((g.getFood()).toLowerCase());
		}
		
		gui.DoGetThisItem(foodStrings);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//rework the stocks
		for (Grocery g : o.order) {
			//if i remove this will my inventory be bad?
			if (NewMarket.inventory.get(g.getFood()).
					isStockBelowIfRemove(g.getAmount())) {
				//System.out.println("Sorry but the market is low on this! not able to fulfill");
				AlertLog.getInstance().logMessage(AlertTag.MarketCashier, 
						this.getName(), "the market is low on this and will not fulfill order" );
				kickout(o);
				return;
			}
			else {
				NewMarket.inventory.get(g.getFood()).
					decreaseStockBy(g.getAmount());
			}
		}
		
		
		//remove this order
		orders.remove(o);	
		
		//only give food if we had it in the inventory!!!
		o.c.msgHereIsFood(o.order);
		
		gui.line.exitLine(o.c.getGui());
	}
	
	//remove order o from orders 
	//send message to GetOut to customer
	private void kickout(MyOrder o) {
		orders.remove(o);
		o.c.msgGetOut();
		gui.line.exitLine(o.c.getGui());
	}

	public Line getLine() {
		return gui.line;
	}

	public void setGui(MarketCashierGui gui) {
		this.gui = gui;
	}
	
	//utilities
	public List<MyOrder> getOrders(){
		return orders;
	}
		
	public boolean hasOrders () {
		return (!orders.isEmpty());
	}

	public MarketCashierGui getGui() {
		return gui;
	}
}
