package newMarket;

import java.util.ArrayList;
import java.util.Collections;
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
import simcity201.gui.TruckGui;
import simcity201.interfaces.*;

public class MarketRestaurantHandlerAgent extends Agent {

	//no person in this dude! 
	
   public EventLog log = new EventLog();

   //list of orders from the various 
   private List<MyOrder> orders 
   	= Collections.synchronizedList(new ArrayList<MyOrder>());
	
   //the delivery truck that goes around the screen
   private TruckAgent truck = new TruckAgent();
   private TruckGui truckGui = new TruckGui(truck);
   
   private Timer timer = new Timer();
   
   public Object groceryLock = new Object();
   
   public float money;
   
   private Semaphore truckAtDest = new Semaphore(0, true);
	
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
   
   MyOrder orderOut = null;
	
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
		
		if (deliverStatus == true) {
			orderOut.s = OrderState.sucessDelivery;
		} 
		else {
			orderOut.s = OrderState.redoDelivery;
		}
		
		/*
		if(deliverStatus == true) {
			synchronized(orders) {
				for (MyOrder mo : orders) {
					if (mo.equals(o)) {
						mo.s = OrderState.sucessDelivery;
						break;
					}
				}
			}
		}
		else {
			synchronized(orders) {
				for (MyOrder mo : orders) {
					if (mo.equals(o)) {
						mo.s = OrderState.redoDelivery;
						break;
					}
				}
			}
		}
		*/
	
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
		print("msgDeliberyBad called from: " + c.getName()); 
		
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
		print("msgIWantFood() called from: " + c.getName());
	
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
					print("Money!!!!!!!!!!!!!!!!!1");
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
	
		
		return false;
	}
	
	/*		Action		*/
	
	//state changed to processing
	//for groceries add up the price for all of 
	private void givePrice(MyOrder o) {
		o.s = OrderState.processing;
		float price = 0;
		
		synchronized(groceryLock) {
		
			//rest 6 causes concurrent modification here...
			for (Grocery g : o.order) {
				price += NewMarket.prices.get(g.getFood()) * g.getAmount();
			}
		}
		
		o.price= price;

		if (price > 0) {
			log.add(new LoggedEvent("givePrice(), Give price to customer"));
			o.c.msgHereIsPrice(o.order, price);
		}else {
			print("no price to be given");
			log.add(new LoggedEvent("givePrice(), Could not give any price to customer"));
			o.c.msgHereIsPrice(o.order, -1);
		}
	}
	
	private void giveFood(MyOrder o) {
		//dont remove yet
		//orders.remove(o);
		o.s = OrderState.delivering;
		
		log.add(new LoggedEvent("giveFood(), here is the order for the restaurant"));
		
		truck.msgDeliverOrder(o.c.getName());
	
		orderOut = o;
		
		/*
		try {
			truckAtDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		*/
		
		orderOut = null;

		if (o.s == OrderState.sucessDelivery) {
			orders.remove(o);
			o.c.msgHereIsFood(o.order);
		}
		else if (o.s == OrderState.redoDelivery) {
			return;
		}
		else {
			System.out.println("delivery to the restaurant weird");
			//...............
			orders.remove(o);
		}
		
		//o.c.msgHereIsFood(o.order);
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
	
	//remove order, message NoFoodForYou
	private void kickout(MyOrder o) {
		print("kickout");
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
