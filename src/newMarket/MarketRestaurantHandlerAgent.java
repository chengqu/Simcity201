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
import simcity201.interfaces.*;

public class MarketRestaurantHandlerAgent extends Agent {

   public EventLog log = new EventLog();

	private List<MyOrder> orders
	= Collections.synchronizedList(new ArrayList<MyOrder>());
	private TruckAgent truck = null;
	
	public float money;
	
	public class MyOrder{
		public List<Grocery> order;
		public NewMarketInteraction c;
		public OrderState s;
		public float price;
		
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
		log.add(new LoggedEvent("Received msgIWantFood."));
	}
	
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
	//	truck.msgDeliverOrder(((Building)o.c).name);
		
	}
	
	private void kickout(MyOrder o) {
		print("kickout");
		orders.remove(o);
		o.c.msgNoFoodForYou();
	}
	public void setTruck(TruckAgent truck){
		this.truck  = truck;
	}

   public List<MyOrder> getOrders()
   {
      // TODO Auto-generated method stub
      return orders;
   }
}
