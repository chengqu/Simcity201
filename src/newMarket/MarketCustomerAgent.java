package newMarket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import newMarket.gui.MarketCustomerGui;
import newMarket.test.mock.EventLog;
import newMarket.test.mock.LoggedEvent;
import agent.Agent;
import agents.CarAgent;
import agents.Grocery;
import agents.Person;
import agents.Role;
import agents.Task;

public class MarketCustomerAgent extends Agent {

	/*		Data		*/
	
	public Person self;
	
	MarketCashierAgent cashier;
	MarketDealerAgent dealer;
	NewMarket market;
	
	//unit testing stuff
	public EventLog log = new EventLog();
	
	//gui stuff
	private MarketCustomerGui gui;
	private Semaphore atDestination = new Semaphore(0,true);
	public void setGui (MarketCustomerGui gui) {
		this.gui = gui;
	}
	
	public enum AgentState { none, waitingForPrice, needToPayGroceries, leaving, 
		waitingForGroceries, gotGrocery, gotKickedOut, needToPayCar, waitingForCar, gotCar };

	AgentState state;
	
	//how much the order will be
	float orderPriceQuote = -1;
	
	//Grocery is defined among sim city agents 
	List<Grocery> order;
	
	CarAgent car = null;
	public MarketCustomerAgent(Person p, MarketCashierAgent cashier, MarketDealerAgent dealer) {
		this.self = p;
		this.state = AgentState.none;
		//this.cashier = cashier; //can be null because we will just assign a cashier.
		this.dealer = dealer;
	}

	/*		Messages		*/
	
	/**
	 * from cashier
	 * if the state is 'waitingForPrice', changes state to 'needToPayGroceries'
	 * @param order
	 * @param price
	 */
	public void msgHereIsPrice(List<Grocery> order, float price) {
		print("msgHereIsPrice called");
		
		if (state == AgentState.waitingForPrice && price <= 0) {
			//if these conditions are met...there is a problem
			//with the order and it needs to be removed 
			System.out.println("Received a weird price...weird.");
			//state will change because the kickOut message is coming imminently
		}
		else if (state == AgentState.waitingForPrice) {
			// maybe check order?
			orderPriceQuote = price;
			state = AgentState.needToPayGroceries;
		}
		stateChanged();
		log.add(new LoggedEvent("Received msgHereIsPrice."));
	}
	
	/**
	 * from cashier
	 * if the state is 'waitForGroceries', changes state to 'gotGrocery'
	 * @param order (which should be a list of groceries)
	 */
	public void msgHereIsFood(List<Grocery> order) {
		print("msgHereIsFood called");
		if (state == AgentState.waitingForGroceries) {
			state = AgentState.gotGrocery;
		}
		stateChanged();
		log.add(new LoggedEvent("Received msgHereIsFood."));
	}
	
	/**
	 * from dealer
	 * if state is 'waitingForPrice', changes to 'needToPayCar'
	 * @param type
	 * @param price
	 */
	public void msgHereIsCarPrice(String type, float price) {
		print("msgHereIsCarPrice called");
		if (state == AgentState.waitingForPrice) {
			// maybe check order?
			orderPriceQuote = price;
			print("the car is going to cost: " + orderPriceQuote);
			state = AgentState.needToPayCar;
		}
		stateChanged();
	}

	/**
	 * from
	 * if state is 'waitingForCar', changes to gotCar
	 * @param car
	 */
	public void msgHereIsCar(CarAgent car) {
		print("msgHereIsCar called");
		if (state == AgentState.waitingForCar) {
			this.car = car;
			state = AgentState.gotCar;
		}
		stateChanged();
	}
	
	/**
	 * from cashier
	 * if state 'waitingForGroceries', changes to gotKickedOut
	 */
	public void msgGetOut() {
		print("msgGetOut called");
		if (state == AgentState.waitingForGroceries) {
			state = AgentState.gotKickedOut;
		}
		else if (state == AgentState.waitingForPrice) {
			state = AgentState.gotKickedOut;
		}
		stateChanged();
		log.add(new LoggedEvent("Received msgGetOut."));
	}
	
	/*		Scheduler		*/
	
	protected boolean pickAndExecuteAnAction() {
		
		//if state equals none, if person agent current task list is empty, doLeave
			//for specificTask in sTasks (specific tasks)
				//if specificTask equals 'buyGroceries', then doOrder()
				//else if specificTask equals 'buyCar', then testDrive()
		if (state==AgentState.none ) {
			print("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWw"); 
			for(Task.specificTask st : self.currentTask.sTasks)
				print(st.toString());
			
			if(self.currentTask.sTasks.size() == 0) {
				print("the state was none and there are no sTasks");
				doLeave();
				return false;
			}
			for (Task.specificTask st : self.currentTask.sTasks) {
				if (st.equals(Task.specificTask.buyGroceries)) {
					// *** FIRST TASK WHICH STARTS THE NORMATIVE ORDERING ***
					doOrder();
					return true;
				}
				else if(st.equals(Task.specificTask.buyCar)){
					testDrive();
					return true;
				}
			}
		}
		
		//if state equals 'needToPayGroceries', then doPayGroceries()
		if (state==AgentState.needToPayGroceries) {
			doPayGroceries();
			return true;
		}
		
		//if state equals 'needTpPayCar', then doPayCar()
		if(state == AgentState.needToPayCar){
			doPayCar();
			return true;
		}
		
		//if state equals 'gotGrocery', then doUpdateGroceries()
		if (state == AgentState.gotGrocery) {
			doUpdateGroceries();
			return true;
		}
		
		//if state equals 'gotCar', then doUpdateCar()
		if (state == AgentState.gotCar) {
			doUpdateCar();
			return true;
		}
		
		//if state equals 'gotKickedOut', then doLeave()
		if (state == AgentState.gotKickedOut) {
			doLeave();
			return false;
		}
		
		return false;
	}

	/*		Action		*/
	
	//changes state to 'waitingForPrice' and sends dealer IWantCar message 
	private void testDrive() {
		state = AgentState.waitingForPrice;
		
		//hard coded sportsCar right now
		dealer.msgIWantCar(this, "SportsCar");
	}
	
	//changes state to 'waitForCar' and removes 'buyCar' from specificTasks list
	//if not enough money, send all I got else send the approprite amount 
	private void doPayCar() {
		print("doPayCar called");
		
		state = AgentState.waitingForCar;
		self.currentTask.sTasks.remove(Task.specificTask.buyCar);
		
		gui.DoWaitForDealer(dealer);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		print("Got to the dealer");
		
		if (self.money < orderPriceQuote) {
			print("please give me car even though I dont have enough!!");
			dealer.msgHereIsMoney(this, (float)self.money);
		}else {
			print("giving proper money for car"); 
			dealer.msgHereIsMoney(this, orderPriceQuote);
		}
		
	}

	//remove the specificTask 'buyGroceries' from the specific task list
	//state = 'waitingForPrice'
	//order is a list of Groceries from home
	//send IWantFood message 
	private void doOrder() {
		self.currentTask.sTasks.remove(Task.specificTask.buyGroceries);
		state = AgentState.waitingForPrice;
		
		order = self.homefood;
		
		//assign the cashier that you want to go to
		cashier = market.findLeastBusyCashier(); 
		
		//find least busy cashier and go there!!!!!!!!!
		
		//gui will handle process until agent makes it to the cashier. 
		gui.DoWaitInLine(cashier); 
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		print("Got to the cashier: " + cashier.toString());  
		cashier.msgIWantFood(this, order);
	}
	
	//changes state to 'waitingForGroceries
	//pay money for groceries appropriately
	private void doPayGroceries() {
		state = AgentState.waitingForGroceries;
		if (self.money < orderPriceQuote) {
			cashier.msgHereIsMoney(this, (float)self.money);
		}else {
			cashier.msgHereIsMoney(this, orderPriceQuote);
		}
	}
	
	//changes state to leaving
	//send person Done() message. !IMPORTANT!
	//remove this customer from the market panel thing
	private void doLeave() {
		state = AgentState.leaving;
		self.msgDone();
		for(Grocery g: self.groceries) {
			//print(Integer.toString(g.getAmount()));
		}
		market.removeCustomer(this);
		if (gui.isPresent()){
			gui.DoExitMarket(this);
		}
	}
	
	//changes state to none
	//homeFood clear the list of Groceries
	//then add the groceries from order to person's groceries
	private void doUpdateGroceries() {
		print("doUpdateGroceries called");
		state = AgentState.none;
		self.homefood.clear();
		self.money -= orderPriceQuote;
		//now the groceries will be the new stuff he purchases 
		for(Grocery g: order) {
			self.groceries.add(g);
		}
	}
	
	//changes agent state to none,
	//subtracts price quote from money,
	//activates the car within the person,
	//start car thread.
	private void doUpdateCar() {	
		print("doUpdateCar called");
		state = AgentState.none;
		self.currentTask.sTasks.clear();
		//self.money -= orderPriceQuote;
		self.money = 0;
		self.car = this.car;
		self.car.startThread();
		
		Role changerole = null; 
		
		for(Role r : self.roles)
		{
			if(r.getRole().equals(Role.roles.JonnieWalker) || r.getRole().equals(Role.roles.preferBus))
			{
				changerole = r;
			}
		}
		if (changerole != null) {
			self.roles.remove(changerole);
		}
		self.roles.add(new Role(Role.roles.preferCar, null));
	}

	/*		Utilities		*/
	
	public void setMarket(NewMarket newMarket) {
		this.market = newMarket;
	}

	public void gui_msgAtEmployee() {
		print("gui_msgAtEmployee called");
		atDestination.release();
	}

	public void gui_msgOffScreen() {
		print("gui_msgOffScreen called");
		//atDestination.release();
	}
	public MarketCustomerGui getGui() {
		return gui;
	}
	
}

