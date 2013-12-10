package newMarket;

import java.util.List;
import java.util.concurrent.Semaphore;

import newMarket.gui.MarketCustomerGui;
import newMarket.test.mock.EventLog;
import newMarket.test.mock.LoggedEvent;
import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;
import agent.Agent;
import agents.CarAgent;
import agents.Grocery;
import agents.Person;
import agents.Role;
import agents.Task;

public class MarketCustomerAgent extends Agent {

	/*		Data		*/
	
	public Person self;
	
	//various employees that the customer can interact with...
	MarketCashierAgent cashier;
	MarketDealerAgent dealer;
	NewMarket market;
	
	//unit testing stuff
	public EventLog log = new EventLog();
	
	//GUI stuff
	private MarketCustomerGui gui;
	private Semaphore atDestination = new Semaphore(0,true);
	public void setGui (MarketCustomerGui gui) {
		this.gui = gui;
	}
	
	public enum AgentState { none, waitingForPrice, needToPayGroceries, leaving, 
		waitingForGroceries, gotGrocery, gotKickedOut, needToPayCar, waitingForCar, gotCar };

	//state of the agent that will determine actions 
	AgentState state;
	
	//how much the order will be
	float orderPriceQuote = -1;
	
	//Grocery is defined among sim city agents 
	List<Grocery> order;
	
	//the car is set to null because if he gets a car it will be instantiated
	CarAgent car = null;
	
	/**
	 * THIS IS THE CONSTRUCTOR 
	 * @param p
	 * @param cashier
	 * @param dealer
	 */
	public MarketCustomerAgent(Person p, MarketCashierAgent cashier, MarketDealerAgent dealer) {
		this.self = p;
		this.state = AgentState.none;
		this.cashier = null; //can be null because we will just assigned a cashier.
		this.dealer = dealer;
	}

	/*********************************************************************************************************
			MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 		
	*********************************************************************************************************/
	
	/**
	 * from cashier
	 * if the state is 'waitingForPrice', changes state to 'needToPayGroceries'
	 * @param order
	 * @param price
	 */
	public void msgHereIsPrice(List<Grocery> order, float price) {
		//print("msgHereIsPrice called");
		AlertLog.getInstance().logMessage(AlertTag.MarketCustomer, this.getName(), "msgHereIsPrice called" );
		
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
		//print("msgHereIsFood called");
		AlertLog.getInstance().logMessage(AlertTag.MarketCustomer, this.getName(), "msgHereIsFood called" );
		
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
		//print("msgHereIsCarPrice called");
		AlertLog.getInstance().logMessage(AlertTag.MarketCustomer, this.getName(), "msgHereIsPrice called");
		
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
		//print("msgHereIsCar called");
		AlertLog.getInstance().logMessage(AlertTag.MarketCustomer, this.getName(), "msgHereIsCar called" );
		
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
		//print("msgGetOut called");
		AlertLog.getInstance().logMessage(AlertTag.MarketCustomer, this.getName(), "msgGetOut called" );
		
		if (state == AgentState.waitingForGroceries) {
			state = AgentState.gotKickedOut;
		}
		else if (state == AgentState.waitingForPrice) {
			state = AgentState.gotKickedOut;
		}
		stateChanged();
		log.add(new LoggedEvent("Received msgGetOut."));
	}
	
	/************************************************************************************************
			SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 		
	************************************************************************************************/
	
	//the scheduler dude
	protected boolean pickAndExecuteAnAction() {
		
		//if state equals none, if person agent current task list is empty, doLeave
			//for specificTask in sTasks (specific tasks)
				//if specificTask equals 'buyGroceries', then doOrder()
				//else if specificTask equals 'buyCar', then testDrive()
		if (state==AgentState.none ) {
			
			for(Task.specificTask st : self.currentTask.sTasks) {
				AlertLog.getInstance().logMessage(AlertTag.MarketCustomer, this.getName(), "task: " + st.toString());
				//print(st.toString());
			}
			
			if(self.currentTask.sTasks.size() == 0) {
				//print("the state was none and there are no sTasks");
				AlertLog.getInstance().logMessage(AlertTag.MarketCustomer, this.getName(), 
						"the state was none and there are no sTasks" );
				doLeave();
				return false;
			}
			for (Task.specificTask st : self.currentTask.sTasks) {
				if (st.equals(Task.specificTask.buyGroceries)) {
					// ******************************************************
					// *** FIRST TASK WHICH STARTS THE NORMATIVE ORDERING ***
					// ******************************************************
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

	/************************************************************************************************
	       ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 		
	************************************************************************************************/
	
	//changes state to 'waitingForPrice' and sends dealer IWantCar message 
	private void testDrive() {
		AlertLog.getInstance().logMessage(AlertTag.MarketCustomer, this.getName(), "testDrive called" );
		
		state = AgentState.waitingForPrice;
		//going to get a sports car baby!!
		dealer.msgIWantCar(this, "SportsCar");
	}
	
	//changes state to 'waitForCar' and removes 'buyCar' from specificTasks list
	//if not enough money, send all I got else send the approprite amount 
	private void doPayCar() {
		AlertLog.getInstance().logMessage(AlertTag.MarketCustomer, this.getName(), "doPayCar called" );
		
		state = AgentState.waitingForCar;
		self.currentTask.sTasks.remove(Task.specificTask.buyCar);
		
		AlertLog.getInstance().logMessage(AlertTag.MarketCustomer, this.getName(), "going to the dealer" ); 
		gui.DoWaitForDealer(dealer);
		
		//block before getting to the dealer so gui can move there
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		AlertLog.getInstance().logMessage(AlertTag.MarketCustomer, this.getName(), "got to the dealer location" );
		
		//give money to the dealer
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
		
		//GUI will handle process until agent makes it to the cashier. 
		gui.DoWaitInLine(cashier); 
		
		//block to get in line to go to cashier agent location / cashier line
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//print("Got to the cashier: " + cashier.toString());  
		AlertLog.getInstance().logMessage(AlertTag.MarketCustomer, this.getName(), 
				"Got to the cashier: " + cashier.toString() );
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
		self.money -= orderPriceQuote;
		//self.money = 0; //hack to make sure he doesn't order another car.
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
	/*      Random gui messages     */
	
	public void setMarket(NewMarket newMarket) {
		this.market = newMarket;
	}

	public void gui_msgAtEmployee() {
		print("gui_msgAtEmployee called");
		atDestination.release();
	}

	public void gui_msgOffScreen() {
		print("gui_msgOffScreen called");
		//atDestination.release(); //don't need this semaphore anymore
	}
	public MarketCustomerGui getGui() {
		return gui;
	}
	
}

