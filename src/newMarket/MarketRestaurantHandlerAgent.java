package newMarket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import newMarket.test.mock.EventLog;
import newMarket.test.mock.LoggedEvent;
import Buildings.Building;
import agent.Agent;
import agents.Grocery;
import agents.TruckAgent;
import animation.SimcityPanel;
import simcity201.gui.GlobalMap;
import simcity201.gui.TruckGui;
import simcity201.interfaces.*;
import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;

public class MarketRestaurantHandlerAgent extends Agent {

	//no person in this dude! 
	
   public EventLog log = new EventLog();

   //list of orders from the various 
   private List<MyOrder> orders 
   	= Collections.synchronizedList(new ArrayList<MyOrder>());
	
   //the delivery truck that goes around the screen
   private TruckAgent truck = new TruckAgent(this);
   private TruckGui truckGui = new TruckGui(truck,GlobalMap.getGlobalMap().getAstar());
   
   private Timer timer = new Timer();
   
   public Object groceryLock = new Object();
   
   public float money;
   
   public Semaphore truckAtDest = new Semaphore(0, true);
	
   public class MyOrder{
	   public List<Grocery> order;
	   public NewMarketInteraction c;
	   public OrderState s;
	   public float price;
	   public boolean restOpen;
		
	   MyOrder(List<Grocery> order, NewMarketInteraction c, OrderState s) {
		   this.order = order;
		   this.c = c;
		   this.s = s;
		   this.restOpen = true;
	   }
   }
   
   public MyOrder orderOut = null;
	
   //constructor sets the truck and truckGui stuff
   //and adds the truck gui to the sim city
	public MarketRestaurantHandlerAgent(){
		truck.setGui(truckGui);
		SimcityPanel.guis.add(truckGui);
		truck.startThread();
	}
	
	public enum OrderState { pending, processing, paid, notEnoughPaid, 
		redoDelivery, sucessDelivery, delivering,};
	
	/*		Messages		*/
	
	public void msgTruckAtDest(boolean deliverStatus) {
		AlertLog.getInstance().logMessage(AlertTag.MarketRestaurantHandler, 
				this.getName(), "msgTruckAtDest called");
		
		if (deliverStatus == true) {
			orderOut.s = OrderState.sucessDelivery;
		} 
		else {
			orderOut.s = OrderState.redoDelivery;
		}
	
		truckAtDest.release();
	}
	
	//TODO msg reception from truck will have a boolean if the 
	//restaurant is open or not.

	/**
	 * from newMarketInteraction 
	 * 
	 * @param c
	 * @param order
	 */
	public void msgDeliveryBad(NewMarketInteraction c, List<Grocery> order) {
		//print("msgDeliberyBad called from: " + c.getName()); 
		AlertLog.getInstance().logMessage(AlertTag.MarketRestaurantHandler, 
				this.getName(), "msgDeliveryBad called");
		
		orders.add(new MyOrder(order, c, OrderState.redoDelivery));
		stateChanged();
		log.add(new LoggedEvent("Recieved an order that could not deliver")); 
	}
	
	/**
	 * from a cook
	 * add new MyOrder consisting of order (a list of groceres)
	 * @param c
	 * @param order
	 */
	public void msgIWantFood(NewMarketInteraction c, List<Grocery> order) {
		//print("msgIWantFood() called from: " + c.getName());
		AlertLog.getInstance().logMessage(AlertTag.MarketRestaurantHandler, 
				this.getName(), "msgIWantFood called");
	
		orders.add(new MyOrder(order, c, OrderState.pending));
		stateChanged();
		log.add(new LoggedEvent("Received msgIWantFood."));
	}
	
	/**
	 * from a cook
	 * 
	 * @param c
	 * @param money_
	 */
	public void msgHereIsMoney(NewMarketInteraction c, float money_) {
		
		synchronized(orders) {
			for (MyOrder o : orders) {
				if (o.c.equals(c) && o.s==OrderState.processing) {
					if (o.price > money_) {
						o.s = OrderState.notEnoughPaid;
						log.add(new LoggedEvent("Received msgHereIsMoney, but restaurant couldn't Pay"));
					}else {
						o.s = OrderState.paid;
						money+=money_;
						log.add(new LoggedEvent("Received msgHereIsMoney, and restaurant Paid"));
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
			
		if (orderOut != null) {
			print("CHAHHAHHAHAHA");
			print(orderOut.s.toString()); 
			if (orderOut.s == OrderState.sucessDelivery)  {
				print("BLAH BLAH BLAH");
				orders.remove(orderOut);
				orderOut.c.msgHereIsFood(orderOut.order);
				orderOut = null;
				return true;
			}
			else {
				print("bleeed");
				orderOut.s = OrderState.redoDelivery; 
				orderOut = null;
				return true;
			}
		}
			
		MyOrder temp = null;
		
		//if there is a myorder o such that o.s == pending, then givePrice(o)
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
		
		//if there is a myorder o such that o.s == paid, then giveFood()
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
		
		//if there is a myorder o such that o.s == notEnoughPaid, then kickout(o)
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
		
		//if there is a myorder o such that o.s == redoDelivery, then giveFoodAgain
		synchronized(orders) {
			for (MyOrder o : orders) {
				if (o.s == OrderState.redoDelivery)
					//giveFoodAgain(o)
					//return true
					temp = o;
					break;
			}
		}	if (temp!=null) { giveFoodAgain(temp); return true; }
		
			
		} catch (ConcurrentModificationException e) {
			return false;
		}
		
			return false;
		
	}
	
	/*		Action		*/
	
	//state changed to processing
	//for groceries add up the price for all of 
	private void givePrice(MyOrder o) {
		o.s = OrderState.processing;
		float price = 0;
		
		//sometimes there was a concurrent mod here...
		synchronized(groceryLock) {
		
			//rest 6 causes concurrent modification here...
			for (Grocery g : o.order) {
				price += NewMarket.prices.get(g.getFood()) * g.getAmount();
			}
		}
		
		o.price= price;
		
		//check the inventory
		boolean goThrough = checkInventory(o); 

		if (goThrough) {
			if (price > 0) {
				log.add(new LoggedEvent("givePrice(), Give price to customer"));
				o.c.msgHereIsPrice(o.order, price);
			}else {
				print("no price to be given");
				log.add(new LoggedEvent("givePrice(), Could not give any price to customer"));
				o.c.msgHereIsPrice(o.order, -1);
			}
		}
		else {
			print("unable to fulfill order for: " + o.c.getName());
			actnOutOfStock(o);
		}
	}
	
	private boolean checkInventory(MyOrder o) {
		//rework the stocks
		for (Grocery g : o.order) {
			//if i remove this will my inventory be bad?
			if (NewMarket.inventory.get(g.getFood()).
					isStockBelowIfRemove(g.getAmount())) {
				print("Sorry but the market is low on this! not able to fulfill");
				return false;
			}
			else {
				NewMarket.inventory.get(g.getFood()).
					decreaseStockBy(g.getAmount());
			}
		}	
		//made it to the end, there is nothing to complain about here.
		return true;
	}
	
	private void giveFood(MyOrder o) {
		//don't remove yet because we have 
		//yet to actually confirm that the restaurant is open
		//orders.remove(o);

		o.s = OrderState.delivering;
		
		log.add(new LoggedEvent("giveFood(), here is the order for the restaurant"));
		
		//truck.msgDeliverOrder(o.c.getName());
		truck.msgDeliverOrder(o.c);

		this.orderOut = o;
		
		try {
			truckAtDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		this.orderOut = o;

		//just to make it run
		//o.s = OrderState.sucessDelivery;
		
		/*
		
		orderOut = null;

		if (o.s == OrderState.sucessDelivery) {
			orders.remove(o);
			o.c.msgHereIsFood(o.order);
		}
		else if (o.s == OrderState.redoDelivery) {
			return;
		}
		else {
			//System.out.println("delivery to the restaurant weird");
			AlertLog.getInstance().logMessage(AlertTag.MarketRestaurantHandler, 
					this.getName(), "delivery to the restaurant weird dude");
			//...............
			orders.remove(o);
		}
		
		//o.c.msgHereIsFood(o.order);
		 
		*/
	}
	
	private void giveFoodAgain(final MyOrder o) {
		
		//wait some specified amount of time
		//or wait until when the next day passes. 
		timer.schedule(new TimerTask() {
			public void run() {
				o.s = OrderState.paid;
				stateChanged();
				//giveFood(o);
				//o.c.msgHereIsFood(o.order);
				//truck.msgDeliverOrder(o.c.getName());
			}
		}, 10000);
	}
	
	//remove order, message OutOfStock
	private void actnOutOfStock(MyOrder o) {
		//print("unable to fulfill order due to out of stock");
		AlertLog.getInstance().logMessage(AlertTag.MarketRestaurantHandler, 
				this.getName(), "kicking out: " + o.c.toString());
		orders.remove(o);
		o.c.msgOutOfStock();
	}
	
	//remove order, message NoFoodForYou
	private void kickout(MyOrder o) {
		//print("kickout");
		AlertLog.getInstance().logMessage(AlertTag.MarketRestaurantHandler, 
				this.getName(), "kicking out: " + o.c.toString());
		orders.remove(o);
		o.c.msgNoFoodForYou();
	}
	
	//utility
	public void setTruck(TruckAgent truck){
		this.truck  = truck;
	}

	//utility 
   public List<MyOrder> getOrders()
   {
      return orders;
   }
}
