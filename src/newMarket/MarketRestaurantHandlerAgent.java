package newMarket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

   public EventLog log = new EventLog();

   //list of orders from the various 
   private List<MyOrder> orders 
   	= Collections.synchronizedList(new ArrayList<MyOrder>());
	
   //the delivery truck that goes around the screen
   private TruckAgent truck = new TruckAgent();
   private TruckGui truckGui = new TruckGui(truck);
   
   public float money;
	
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
	
   //constructor sets the truck and truckGui stuff
   //and adds the truck gui to the sim city
	public MarketRestaurantHandlerAgent(){
		truck.setGui(truckGui);
		SimcityPanel.guis.add(truckGui);
		truck.startThread();
	}
	public enum OrderState { pending, processing, paid, notEnoughPaid,  };
	
	/*		Messages		*/
	
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
	
		
		
		return false;
	}
	
	/*		Action		*/
	
	//state changed to processing
	//for groceries add up the price for all of 
	private void givePrice(MyOrder o) {
		o.s = OrderState.processing;
		float price = 0;
		
		for (Grocery g : o.order) {
			price += NewMarket.prices.get(g.getFood()) * g.getAmount();
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
		orders.remove(o);
		log.add(new LoggedEvent("giveFood(), here is the order for the restaurant"));
		
		//need to block action here...
		
		o.c.msgHereIsFood(o.order);
		truck.msgDeliverOrder(o.c.getName());
		
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
      // TODO Auto-generated method stub
      return orders;
   }
}
