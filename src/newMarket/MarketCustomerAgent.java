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
	MarketCustomerGui gui;
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
		this.cashier = cashier;
	}

	/*		Messages		*/
	
	/**
	 * from cashier
	 * if the state is 'waitingForPrice', changes state to 'needToPayGroceries'
	 * @param order
	 * @param price
	 */
	public void msgHereIsPrice(List<Grocery> order, float price) {
		
		if (state == AgentState.waitingForPrice) {
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
		if (state == AgentState.waitingForPrice) {
			// maybe check order?
			orderPriceQuote = price;
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
		if (state == AgentState.waitingForGroceries) {
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
			if(self.currentTask.sTasks.size() == 0) {
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
		state = AgentState.waitingForCar;
		self.currentTask.sTasks.remove(Task.specificTask.buyCar);
		if (self.money < orderPriceQuote) {
			dealer.msgHereIsMoney(this, (float)self.money);
		}else {
			dealer.msgHereIsMoney(this, orderPriceQuote);
		}
		
	}
	
	//changes agent state to none,
	//subtracts price quote from money,
	//activates the car within the person,
	//start car thread.
	private void doUpdateCar() {	
		state = AgentState.none;
		self.money -= orderPriceQuote;
		self.car = car;
		self.car.startThread();
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
		
		print("Got to the cashier");  
		
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
	}
	
	//changes state to none
	//homeFood clear the list of Groceries
	//then add the groceries from order to person's groceries
	private void doUpdateGroceries() {
		state = AgentState.none;
		self.homefood.clear();
		self.money -= orderPriceQuote;
		//now the groceries will be the new stuff he purchases 
		for(Grocery g: order) {
			self.groceries.add(g);
		}
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
	
}

