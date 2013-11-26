package newMarket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity201.gui.CarGui;

import agent.Agent;
import agents.CarAgent;
import agents.Grocery;
import agents.Person;
import animation.SimcityPanel;

public class MarketDealerAgent extends Agent {
	
	/*		Data		*/
	
	public Person self;
	
	private List<MyOrder> orders
		= Collections.synchronizedList(new ArrayList<MyOrder>());
	
	private class MyOrder{
		String type;
		MarketCustomerAgent c;
		OrderState s;
		float price;
		
		MyOrder(String type, MarketCustomerAgent c, OrderState s) {
			this.type = type;
			this.c = c;
			this.s = s;
		}
	}
	public enum OrderState { pending, processing, paid, notEnoughPaid,  };
	
	/*		Messages		*/
	
	public void msgIWantCar(MarketCustomerAgent c, String type) {
		orders.add(new MyOrder(type, c, OrderState.pending));
		stateChanged();
	}
	
	public void msgHereIsMoney(MarketCustomerAgent c, float money) {
		synchronized(orders) {
			for (MyOrder o : orders) {
				if (o.c.equals(c) && o.s==OrderState.processing) {
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
		}	if (temp!=null) { giveCar(temp); return true; }
		

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
			price += NewMarket.prices.get(o.type);
		o.price = price;
		if (price > 0) {
			o.c.msgHereIsCarPrice(o.type, price);
		}else {
			o.c.msgHereIsCarPrice(o.type, -1);
		}
	}
	
	private void giveCar(MyOrder o) {
		orders.remove(o);
		CarAgent car = new CarAgent(o.type);
		CarGui carGui = new CarGui(car);
		 SimcityPanel.guis.add(carGui);
		 car.setGui(carGui);
		o.c.msgHereIsCar(car);
		
	}
	
	private void kickout(MyOrder o) {
		orders.remove(o);
		o.c.msgGetOut();
	}
	
}
