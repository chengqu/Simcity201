package guehochoi.restaurant;

import agent.Agent;
import agents.Grocery;
import agents.MonitorSubscriber;
import agents.ProducerConsumerMonitor;
import guehochoi.gui.CookGui;
import guehochoi.gui.CustomerGui;
import guehochoi.gui.RestaurantGui;
import guehochoi.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;

import newMarket.MarketRestaurantHandlerAgent;
import simcity201.gui.GlobalMap;
import simcity201.interfaces.NewMarketInteraction;

public class CookAgent extends Agent implements Cook, NewMarketInteraction, MonitorSubscriber{
	
	
	CashierAgent cashier;
	CookGui cookGui;
	
	private List<Order> orders = 
			Collections.synchronizedList(new ArrayList<Order>());
	public enum OrderState {
		pending, cooking, done
	}
	
	private Timer timer;
	public Map<String, Food> foods = new HashMap<String, Food>();
	
	private String name;
	/*
	private List<MyMarket> markets = 
			Collections.synchronizedList(new ArrayList<MyMarket>());
	*/
	private Semaphore atDest = new Semaphore(0, true);
	
	enum DeliveryState { onDelivery, confirmed, delivered };
	
	enum AgentState { sleeping, atWork, openingRestaurant, initStocked, opened } 
	AgentState state = AgentState.sleeping;
	Host host;
	
	public static class Order {
		Waiter w;
		String choice;
		int table;
		OrderState s;
		
		
		Order(Waiter w, String choice, int table, OrderState s) {
			this.w = w; 
			this.choice = choice;
			this.table = table;
			this.s = s;
		}
	}
	public class Food {
		public String type;
		int cookingTime;
		public int amount;
		int low;
		int restockAmount;
		int incomingOrder;
		
		Food(String type, int cookingTime, int amount, int low, int restockAmount) {
			this.type = type; 
			this.cookingTime = cookingTime;
			this.amount = amount;
			this.low = low;
			this.restockAmount = restockAmount;
			this.incomingOrder = 0;
		}
	}
	static int numMarketOrders = 0;
	private class MarketOrder {
		MarketRestaurantHandlerAgent handler;
		List<Grocery> listOfOrders;
		MarketOrderState s;
		int marketOrderID;
		float moneyPaid;
		float priceQuote;
		
		
		MarketOrder(MarketRestaurantHandlerAgent handler, List<Grocery> listOfOrders, MarketOrderState s) {
			this.handler = handler;
			this.listOfOrders = listOfOrders;
			this.s = s;
			this.marketOrderID = numMarketOrders;
			numMarketOrders++;
		}
	}
	private enum MarketOrderState { ordered, toldCashierToPay, paid, delivered, denied, tellCashierToPay }
	private List<MarketOrder> marketOrders =
			Collections.synchronizedList(new ArrayList<MarketOrder>());
	
	private ProducerConsumerMonitor<Order> monitor;
	
	/*
	public class MyMarket {
		Market m;
		List<String> availableList = 
				Collections.synchronizedList(new ArrayList<String>());
		List<MarketOrder> orders = 
				Collections.synchronizedList(new ArrayList<MarketOrder>());
		
		MyMarket(Market m) {
			this.m = m;
			availableList.add("Chicken");availableList.add("Beef");
			availableList.add("Pork");availableList.add("Turkey");
			availableList.add("duck");
		}
	}

	class MarketOrder {
		String choice;
		int quantity;
		DeliveryState s;
		
		MarketOrder(String choice, int quantity, DeliveryState s) {
			this.choice = choice;
			this.quantity = quantity;
			this.s = s;
		}
	}
	*/
	
	
	/* Messages */
	
	@Override
	public void canConsume() {
		stateChanged();
	}
	
	public void hereIsOrder(Waiter w, String choice, int table) {
		orders.add(new Order(w, choice, table, OrderState.pending));
		stateChanged();
	}
	
	public void foodDone(Order o) {
		o.s = OrderState.done;
		stateChanged();
	}
	/*
	public void weAreOutof(String choice, Market m) {
		synchronized ( markets ) {
		for (MyMarket myM : markets) {
			if (myM.m.equals(m)) {
				myM.availableList.remove(choice);
			}
		}//markets
		}//sync
		stateChanged();
	}
	
	public void deliveryScheduled(String choice, int quantity, Market m) {
		synchronized ( markets ) {
		for (MyMarket myM : markets) {
			if (myM.m.equals(m)) {
				myM.orders.add( new MarketOrder (choice, quantity, DeliveryState.onDelivery));
			}
		}//markets
		}//sync
		stateChanged();
	}
	
	public void deliveryFor(String choice, Market m) {
		synchronized ( markets ) {
		for (MyMarket myM : markets) {
			if (myM.m.equals(m)) {
				synchronized (myM.orders) {
				for (MarketOrder mo : myM.orders){
					if (mo.choice.equalsIgnoreCase(choice)) {
						mo.s = DeliveryState.delivered;
					}
				}//myM.orders
				}//sync
			}
		}//markets
		}//sync
		stateChanged();
	}
	*/
	
	public void goToWork() {
		state = AgentState.atWork;
		stateChanged();
	}
	
	public void msgAtDestination() {
		atDest.release();
		stateChanged();
	}
	
	///////////////////////////////////////////////////////////////////
	@Override
	public void msgHereIsPrice(List<Grocery> orders, float price) {
		int incomingSize = orders.size();
		int numberOfMatch = 0;
		MarketOrder matchedOrder = null;
		synchronized (marketOrders) {
			for (MarketOrder mo : marketOrders) {
				for(Grocery og : mo.listOfOrders) {
					for(Grocery ig : orders) {
						if (og.equals(ig)) {
							numberOfMatch++;
							if (numberOfMatch == incomingSize) {
								matchedOrder = mo;
							}
							break;
						}
					}
				}
			}
		}
		if ( incomingSize == numberOfMatch ) {
			matchedOrder.s = MarketOrderState.tellCashierToPay;
			matchedOrder.priceQuote = price;
		}else {
			//got a wrong order, doesn't do any more interaction
			print("This is not what I orderd!!! trying to rip me off?");
		}
		stateChanged();
	}
	
	public void msgHereIsMoney(float amount, int marketOrderID) {
		synchronized(marketOrders) {
			for (MarketOrder mo: marketOrders) {
				if (mo.marketOrderID == marketOrderID && mo.s == MarketOrderState.toldCashierToPay) {
					mo.s = MarketOrderState.paid;
					mo.moneyPaid = amount;
					mo.handler.msgHereIsMoney(this, amount);
					//print("\t\thandler should have gotton payment");
					break;
				}
			}
		}
	}

	@Override
	public void msgHereIsFood(List<Grocery> orders) {
		int incomingSize = orders.size();
		int numberOfMatch = 0;
		MarketOrder matchedOrder = null;
		synchronized (marketOrders) {
			for (MarketOrder mo : marketOrders) {
				for(Grocery og : mo.listOfOrders) {
					for(Grocery ig : orders) {
						if (og.equals(ig)) {
							numberOfMatch++;
							if (numberOfMatch == incomingSize) {
								matchedOrder = mo;
							}
							break;
						}
					}
				}
			}
		}
		if ( incomingSize == numberOfMatch ) {
			//print("\t\tI got my food");
			matchedOrder.s = MarketOrderState.delivered;
		}else {
			//got a wrong order delivery, does no more action
			print("This is not what I orderd!!! trying to rip me off?");
		}
		stateChanged();
	}

	@Override
	public void msgNoFoodForYou() {
		synchronized (marketOrders) {
			for (MarketOrder mo : marketOrders) {
				if (mo.s == MarketOrderState.paid && mo.moneyPaid < mo.priceQuote) {
					mo.s = MarketOrderState.denied;
					break;
				}
			}
		}
		stateChanged();
		
	}
	////////////////////////////////////////////////////////////////////
	
	
	
	/* Scheduler */
	protected boolean pickAndExecuteAnAction() {
		
		
		System.out.println("\n\n\n\n\n\nSche\n\n\n\n\n");
		
		
		if (state == AgentState.atWork) {
			openRestaurant();
			return true;
		}
		if (state == AgentState.initStocked) {
			tellHostToTakeCustomers();
			return true;
		}
		
		Order temp = null;
		
		synchronized ( orders ) {
		for( Order o : orders ) {
			if (o.s == OrderState.done) {
				//plateIt(o);
				//return true;
				temp = o;
				break;
			}
		}//orders
		}//sync
		if (temp != null) { plateIt(temp); return true; }

		
		
		if ((temp=monitor.remove()) != null) {
			if (temp.s == OrderState.pending) {
				orders.add(temp);
				cookIt(temp);
				return true;
			}
		}
		
		synchronized ( orders ) {
		for( Order o : orders ) {
			if (o.s == OrderState.pending) {
				//cookIt(o);
				//return true;
				temp = o;
				break;
			}
		}//orders
		}//sync
		if (temp != null) { cookIt(temp); return true; }
		
		MarketOrder tempMO = null;
		
		synchronized ( marketOrders) {
		for ( MarketOrder mo : marketOrders) {
			if ( mo.s == MarketOrderState.tellCashierToPay ) {
				tempMO = mo;
				break;
			}
		}
		}
		if (tempMO != null) { tellCashierToPay(tempMO); return true; }
		
		synchronized ( marketOrders) {
		for ( MarketOrder mo : marketOrders) {
			if ( mo.s == MarketOrderState.delivered ) {
				tempMO = mo;
				break;
			}
		}
		}
		if (tempMO != null) { restockAndUpdate(tempMO); return true; }
		
		synchronized ( marketOrders) {
		for ( MarketOrder mo : marketOrders) {
			if ( mo.s == MarketOrderState.denied ) {
				tempMO = mo;
				break;
			}
		}
		}
		if (tempMO != null) { marketOrderDenied(tempMO); return true; }
		
		/*
		synchronized ( markets ) {
		for (MyMarket m : markets) {
			for (MarketOrder o : m.orders) {
				if (o.s == DeliveryState.onDelivery) {
					confirmOrder(m);
					return true;
				}
			}
		}//markets
		}//sync
		
		synchronized ( markets ) {
		for (MyMarket m : markets) {
			for (MarketOrder o : m.orders) {
				if (o.s == DeliveryState.delivered) {
					restock(m);
					return true;
				}
			}
		}//markets
		}//sync
		*/
		cookGui.moveToHome();
		
		return false;
	}
	
	
	
	

	/* Actions */
	private void cookIt(Order o) {
		Food f = foods.get(o.choice);
		/*
		if (f.amount + f.incomingOrder <= f.low) {
			synchronized ( markets ) {
			for (MyMarket m : markets) {
				if (m.availableList.contains(f.type)) {
					print(m.m.getName() + ", i need " + f.type + " asap");
					m.m.orderFor(f.type, f.restockAmount);
				}
			}//markets
			}//sync
			f.setIncomingOrder(0);
			//f.incomingOrder = 0;// this is bug-prone, think about "Late Delivery"
			f.isOrdered = true; // These two lines need better way
			
		}*/
		if (f.amount <= 0) {
			print(o.w + " " + o.choice + " is out of stock");
			o.w.outOf(o.choice, o.table);
			orders.remove(o);
			return;
		}
		f.amount--;
		
		List<Grocery> groceriesToOrder = new ArrayList<Grocery>();
		synchronized (foods) {
			for (Food fd : foods.values()) {
				if (fd.incomingOrder + fd.amount <= fd.low) {
					groceriesToOrder.add(new Grocery(fd.type, fd.restockAmount));
					fd.incomingOrder += fd.restockAmount;
				}
			}
		}
		if (!groceriesToOrder.isEmpty()) {
			MarketRestaurantHandlerAgent myHandler = GlobalMap.getGlobalMap().marketHandler;
			marketOrders.add(new MarketOrder(myHandler, groceriesToOrder, MarketOrderState.ordered));
			for ( Grocery g : groceriesToOrder) {
				print("Ordering: " + g.getFood() + ", " + g.getAmount());
			}
			myHandler.msgIWantFood(this, groceriesToOrder);
		}
		
		final Order fo = o;	
		fo.s = OrderState.cooking;
		print("I am cooking " + o.choice + " for " + o.table);
		cookGui.DoCooking(o.choice);
		try{
			atDest.acquire();
		}catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		
		timer.schedule(new TimerTask() {
			public void run() {
				foodDone(fo);
				//o.s = OrderState.done;
			}
		}, f.cookingTime);

	}
	private void plateIt(Order o) {
		cookGui.DoPlating(o.choice); // Animation
		try{
			atDest.acquire();
		}catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		
		print("done cooking, " + o.choice);
		o.w.orderIsReady(o.choice, o.table);
		orders.remove(o);
	}
	/*
	private void confirmOrder(MyMarket m ) {
		
		synchronized ( m.orders ) {
		for (MarketOrder o : m.orders) {
			if (o.s == DeliveryState.onDelivery) {
				Food f = foods.get(o.choice);
				if (o.quantity <= f.restockAmount - f.incomingOrder) {
					//f.setIncomingOrder(f.incomingOrder + o.quantity);
					f.incomingOrder += o.quantity;
					o.s = DeliveryState.confirmed;
					print(m.m + ", I confirm the order of "+o.choice +" amount "+ o.quantity +", send me asap");
					m.m.confirmation(true, o.choice);
				}else {
					print("I do not confirm the order. I already filled my " + o.choice);
					m.orders.remove(o);
					m.m.confirmation(false, o.choice);
				}
				return;
			}
		}//m.orders
		}//sync
		
	}
	private void restock(MyMarket m){
		if (state == AgentState.openingRestaurant)
			state = AgentState.initStocked;
		synchronized ( m.orders ) {
		for (MarketOrder o : m.orders) {
			if (o.s == DeliveryState.delivered) {
				Food f = foods.get(o.choice);
				f.amount = f.amount + o.quantity;
				f.isOrdered = false;
				print(f.amount + " of " +o.choice + " have been restocked.");
				m.orders.remove(o);
				return;
			}
		}//m.orders
		}//sync
		
	}*/
	private void openRestaurant() {
		state = AgentState.openingRestaurant;
		print("Initial restocking foods");
		

		List<Grocery> groceriesToOrder = new ArrayList<Grocery>();
		synchronized (foods) {
			for (Food fd : foods.values()) {
				if (fd.incomingOrder + fd.amount <= fd.low) {
					groceriesToOrder.add(new Grocery(fd.type, fd.restockAmount));
					fd.incomingOrder += fd.restockAmount;
				}
			}
		}
		if (!groceriesToOrder.isEmpty()) {
			//print("\t\t I am ordering food");
			MarketRestaurantHandlerAgent myHandler = GlobalMap.getGlobalMap().marketHandler;
			marketOrders.add(new MarketOrder(myHandler, groceriesToOrder, MarketOrderState.ordered));
			for ( Grocery g : groceriesToOrder) {
				print("Ordering: " + g.getFood() + ", " + g.getAmount());
			}
			myHandler.msgIWantFood(this, groceriesToOrder);
		}else {
			print("there is nothing to restock");
			tellHostToTakeCustomers();
		}
		
		
		/*
		boolean nothingToRestock = true;
		Food f;
		for (Menu.Type t : Menu.Type.values()) {
			f = foods.get(t.toString());
			if (f.amount <= f.low && !f.isOrdered) {
				nothingToRestock = false;
				
				for (MyMarket m : markets) {
					print(m.m.getName() + ", i need " + f.type + " to open the restaurant");
					m.m.orderFor(f.type, f.restockAmount);
				
				}//sync
				f.setIncomingOrder(0);
				//f.incomingOrder = 0;// this is bug-prone, think about "Late Delivery"
				f.isOrdered = true; // These two lines need better way
				
			}
		}
		if (nothingToRestock) {
			print("there is nothing to restock");
			tellHostToTakeCustomers();
		}*/
	}
	private void tellHostToTakeCustomers() {
		print(host + ", let's start working ");
		state = AgentState.opened;
		host.takeCustomers();
	}
	
	private void tellCashierToPay(MarketOrder mo) {
		print("Cashier, pay to the market");
		mo.s = MarketOrderState.toldCashierToPay;
		cashier.payToMarket(mo.priceQuote, mo.marketOrderID);
	}
	
	private void restockAndUpdate(MarketOrder mo) {
		if (state == AgentState.openingRestaurant)
			state = AgentState.initStocked;
		print("restocked, cashier, update");
		for (Grocery g : mo.listOfOrders) {
			Food f = foods.get(g.getFood());
			f.amount += g.getAmount();
			f.incomingOrder = 0;
		}
		marketOrders.remove(mo);
		cashier.updateMoney(mo.moneyPaid);
	}
	
	private void marketOrderDenied(MarketOrder mo) {
		print("market order denied");
		marketOrders.remove(mo);
	}
	
	
	/* utilities */
	
	public CookAgent(String name, ProducerConsumerMonitor<Order> monitor) {
		super();
		this.name = name;
		timer = new Timer();
		// foods cooking time, amount, low preset, restockAmount
		foods.put("Chicken", new Food("Chicken", 3000, 10, 4, 10));
		foods.put("Beef", new Food("Beef", 2000, 0, 4, 5));
		foods.put("Turkey", new Food("Turkey", 4000, 10, 4, 10));
		foods.put("Pork", new Food("Pork", 5000, 10, 4, 10));
		foods.put("Duck", new Food("Duck", 4000, 10, 4, 10));
		this.monitor = monitor;
	}
	
	public String getName() {
		return this.name;
	}
	public String toString() {
		return this.name;
	}
	/*
	public void addMarket(Market m) {
		this.markets.add(new MyMarket(m));
	}*/
	public void setHost(Host host) {
		this.host = host;
	}
	public void setGui(CookGui cookGui) {
		this.cookGui = cookGui;
	}
	public void setCashier(CashierAgent cashier) {
		this.cashier = cashier;
	}

	/* HACKS */
	public void gotNoChicken() {
		foods.get("Chicken").amount = 0;
	}

	
	
	
	
}

