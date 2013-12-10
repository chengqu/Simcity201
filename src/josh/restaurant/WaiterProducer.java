package josh.restaurant;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import josh.restaurant.CashierAgent.Bill;
import josh.restaurant.CookAgent.OrderIcon;
import josh.restaurant.WaiterAgent.BillState;
import josh.restaurant.WaiterAgent.MyBill;
import josh.restaurant.WaiterAgent.MyCustomer;
import josh.restaurant.WaiterAgent.MyCustomerState;
import josh.restaurant.WaiterAgent.WaiterState;
import josh.restaurant.gui.WaiterGui;
import josh.restaurant.interfaces.Customer;
import josh.restaurant.interfaces.Waiter;
import agent.Agent;
import agents.Person;
import agents.ProducerConsumerMonitor;

public class WaiterProducer extends Agent implements Waiter {	
	
	public WaiterProducer (String name, ProducerConsumerMonitor<CookAgent.MyOrder> monitor) {
		super();
		this.monitor = monitor;
		this.name_ = name;
		state = WaiterState.working; 
	}
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	Person person;
	
	ProducerConsumerMonitor<CookAgent.MyOrder> monitor;
	HostAgent host = null; 
	CookAgent cook = null;
	CashierAgent cashier = null; 
	WaiterGui waitergui = null; 
	private String name_;
	private WaiterState state = null;
	private Semaphore atDestination = new Semaphore(0,true);
	
	public class MyCustomer {
		CustomerAgent c_;
		int tableNum_; 
		MyCustomerState s_;
		String choice_;
		MyCustomer(CustomerAgent c, int table, MyCustomerState waiting) {
			c_ = c;
			tableNum_ = table;
			s_ = waiting;
			choice_ = "";
		}
	}
	
	public class MyBill {
		Bill b_ ; 
		BillState s_;
		Customer c_;
		MyBill(Bill b, Customer c) {
			b_ = b; 
			c_ = c;
			s_ = BillState.billReady; 
		}
	}
	
	public enum BillState {billReady, delivered, paidedInFull, notFullyPaid, delivering}; 
	public enum WaiterState {working, wantBreak, takeBreak, onBreak, backToWork, hold}; 
	public enum TypesOfFood {steak, chicken, salad, pizza};
	public enum OrderState {pending, cooking, complete};
	enum MyCustomerState {waiting, seated, asked, ordered, waitingForFood, 
		finishedFood, foodReady, foodDelivered, waitingToOrder, mustReorder, middleOfAction, leavingEarly, clear}
	List<MyCustomer> customers = new ArrayList<MyCustomer>();
	List<Order> orders = new ArrayList<Order>(); 
	List<MyBill> bills = new ArrayList<MyBill>(); 

	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	// ********************************************************************************************************
	// ** MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES ** 
	// ********************************************************************************************************

	//from waiter gui
	public void gui_msgBackAtHomeBase () {  //from animation, block control
		//print("gui_msgBackAtHomeBase");
		atDestination.release();
	}
	
	public void gui_msgAtTable() {   //from animation, block control
		//print("gui_msgAtTable() called");
		atDestination.release();
	}
	
	public void gui_msgAtCook() { //from animation, block control
		//print("gui_msgAtCook() called");
		atDestination.release();
	}
	
	//from HostAgent
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgSitAtTable(restaurant.CustomerAgent, int)
	 */
	@Override
	public void msgSitAtTable (CustomerAgent c, int table) {
		customers.add(new MyCustomer(c, table, MyCustomerState.waiting));
		stateChanged();
	}
	
	//from cashier 
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgHereIsTheBill(restaurant.CashierAgent.Bill)
	 */
	@Override
	public void msgHereIsTheBill(Bill b) {
		print("msgHereIsThebill");
		bills.add(new MyBill(b, b.c_)); //constructor makes s_ = billReady
		stateChanged(); 
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgLeavingEarly(restaurant.CustomerAgent)
	 */
	@Override
	public void msgLeavingEarly(CustomerAgent c) {
		
		for (MyCustomer mycustomer : customers) {
			if (mycustomer.c_ == c) {
				mycustomer.s_ = MyCustomerState.leavingEarly;
				print(mycustomer.c_.getName() + " leaving " + mycustomer.tableNum_);
				break;
			}
		}
		stateChanged();
	}
	
	//from CustomerAgent
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgImReadyToOrder(restaurant.CustomerAgent)
	 */
	@Override
	public void msgImReadyToOrder (CustomerAgent c) {
		for (MyCustomer mycustomer : customers) {
			if (mycustomer.c_ == c) {
				mycustomer.s_ = MyCustomerState.waitingToOrder;  //change from seated to waitingToOrder
				break;
			}
		}
		//print("SC from msgI'mReadytoOrder"); 
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgHereIsMyChoice(restaurant.CustomerAgent, java.lang.String)
	 */
	@Override
	public void msgHereIsMyChoice (CustomerAgent c, String choice) {
	//MyCustomerState initially equals waiting to order 
		
		for (MyCustomer mycustomer : customers) {
			if (mycustomer.c_ == c) {
				mycustomer.s_ = MyCustomerState.ordered;
				mycustomer.choice_ = choice;
				print("the customer ordered a " + choice);
				break;
			}
		}
		
		//print("SC from Hereismychoice"); 
		stateChanged();
	    //MyCustomerState now equals ordered
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgDoneEatingAndLeaving(restaurant.CustomerAgent)
	 */
	@Override
	public void msgDoneEatingAndLeaving (CustomerAgent c) {
		
		for (MyCustomer mycustomer : customers) {
			if (mycustomer.c_ == c) {
				mycustomer.s_ = MyCustomerState.finishedFood;
				print(mycustomer.c_.getName() + " leaving " + mycustomer.tableNum_);
				//don't forget to now clear the table
				break;
			}
		}
		//print("SC from DoneandLeaving");
		stateChanged(); 
	}
	
	//from CookAgent
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgBadOrder(java.lang.String, int)
	 */
	@Override
	public void msgBadOrder(String choice, int table ) {
		
		Order failedOrder = null; 
		
		for (Order order : orders) {
			if (order.choice_ == choice && order.tableNum_ == table) {
				failedOrder = order;
				break; 
			}
		}
		
		//find out who the order belongs too
		for (MyCustomer mc : customers) {
			if (mc.choice_ == failedOrder.choice_ && mc.tableNum_ == failedOrder.tableNum_) {
				mc.s_ = MyCustomerState.mustReorder;
				//mc.c_.msgHereIsYourFood(); nope
				break;
			}
		}
		stateChanged(); 
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgOrderIsReady(java.lang.String, int, restaurant.CookAgent.OrderIcon)
	 */
	@Override
	public void msgOrderIsReady (String choice, int table, OrderIcon i) {
		
		//we got a message from the cook that an order is ready, but we have to find out which order and mark it
		for (Order order : orders) {
			if (order.choice_ == choice && order.tableNum_ == table) {
				order.finished_ = true; 
				order.i_ = i;
				break; 
			}
		}
		
		/* alternative implementation changing the state of the customer agent
		for (MyCustomer mycustomer : customers) {
			if (mycustomer.choice_ == choice && mycustomer.tableNum_ == table) {
				mycustomer.s_ = MyCustomerState.foodReady; 
				break;
			}
		}
		*/
		//print("SC frokm orderisready");
		stateChanged();
	}
	
	public void gui_msgBackToWork() {
		state = WaiterState.backToWork; 
		stateChanged(); 
	}

	//ideally from a gui check box...
	public void gui_msgWantToGoOnBreak() {
		state = WaiterState.wantBreak;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgBreakRequestAnswer(boolean)
	 */
	@Override
	public void msgBreakRequestAnswer(boolean answer) {
		
		if (answer == true) {
			//take break, but when finished with all your work!!!
			state = WaiterState.takeBreak; 
			print("My break request is confirmed!!!!! YAAAAA. "); 
		}
		else if (answer == false) {
			state = WaiterState.working; 
			print("My break was denied. Aw shucks."); 
			
			//********** concurrency issues right here 
			//host.restGui.updateInfoPanel(this);
		}
		stateChanged(); 
	}
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	//*********************************************************************************************************
	//** SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER **
	//*********************************************************************************************************
	
	public boolean pickAndExecuteAnAction() {
		//print ("scheduler run");
		
		try {
		
		// ********* break scenario **********
		if ( /*customers.isEmpty() && */ state == WaiterState.takeBreak) {
			
			print("GONNA TAKE A BREAK"); 
			actnTakeBreak(); 
			return true; 	
		}
		
		if (state == WaiterState.backToWork) {
			print("BACK TO WORK"); 
			actnBackToWork();
			return true; 
		}
		
		//goes at top of scheduler so we can process non-norm quickly
		//if there is a customer in customers such that c.s is leaving early, action clear table
		for (MyCustomer mc : customers) {
			if (mc.s_ == MyCustomerState.leavingEarly) { 
				mc.s_ = MyCustomerState.clear; //to make sure we dont do anything more with this customer
				actnClearTable(mc);
				return true;
			}
		}
		
		//if there is a bill in bills such that b.s is billReady, action deliver
		for (MyBill b : bills) {
			if (b.s_ == BillState.billReady) {
				print("In scheduler: I have a bill ready to deliver"); 
				actnDeliverBill(b);
				return true; 
			}
		}
		
		//if there is an order ready in the list of orders
		for (Order o : orders) {
			if (o.finished_) {
				print("the cook wants me to pick up an order");
				actnServeFood(o);
				print("finished serving food");
				return true;
			}
		}
		
		/* alternative implementation with the customer having a statechange when food is ready, 
		 * decided that this was not the preferred way becasue its more realistic to know that
		 * all you have ready is an order, not necessarily whose
		for (MyCustomer mc : customers) {
			if (mc.s_ == MyCustomerState.foodReady) { 
				print("bringing food to table" + mc.tableNum_);
				actnServeFood(mc); 
				return true;
			}
		}
		*/
		
		//If there exists c in customers such that c.s = mustReOrder then, AskForOrderSecondTime(c); 
		for (MyCustomer mc : customers) {
			if (mc.s_ == MyCustomerState.mustReorder) { 
				actnAskForOrderSecondTime(mc, mc.choice_);
				return true;
			}
		}
		
		//If there exists c in customers such that c.s = waiting then, SeatCustomer(c); 
		for (MyCustomer mc : customers) {
			if (mc.s_ == MyCustomerState.waiting) { 
				print("actnSeatCustomer called");
				actnSeatCustomer(mc);
				return true;
			}
		}
		
		//If there exists c in customers such that c.s = ordered then, OrderToCook(c);
		for (MyCustomer mc : customers) {
			if (mc.s_ == MyCustomerState.ordered) { 
				print("order in scheduler noted");
				actnOrderToCook(mc);
				return true;
			}
		}
		
		//If there exists c in customers such that c.s = waitingToOrder then, takeOrder(c);
		for (MyCustomer mc : customers) {
			if (mc.s_ == MyCustomerState.waitingToOrder) {
				print("size: " + customers.size()); 
				print("going to ask for order");
				actnAskForOrder(mc);
				return true;
			}
		}		
				
		//If there exists c in customers such that c.s = finishedFood then, clearTable(c);
		for (MyCustomer mc : customers) {
			if (mc.s_ ==  MyCustomerState.finishedFood) {
				actnClearTable(mc); 
				return true;
			}
		}
		
		// ************* break scenario ***************
		
		if ( customers.isEmpty() &&  state == WaiterState.wantBreak) {
			//print("IN SCHEDULLER, WANT TO TAKE BREAK!!!"); 
			actnAskForBreak(); 
			return true; 
		}
		
		}
		catch (ConcurrentModificationException e) {
			return false; 
		}
		
		//go off screen if waiter does not have anything to do
		//print("IDLE"); 
		waitergui.DoIdleOffScreen();
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER

	// *****************************************************************************************************
	// ** ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS **
	// *****************************************************************************************************
	
	void actnDeliverBill(MyBill b) {
		
		b.s_ = BillState.delivering;
		
		//move to cook
		waitergui.DoGoToThisTable(b.c_.whereAmISitting);
				
		//control block, pause waiter agent while gui moves to cook
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		b.c_.msgHereIsYourBill(b.b_.charge_); 
		print ("delivered bill to csutomer"); 	

	}
	
	void actnBackToWork() {
		
		//back to work, send myself to waiter to remind him
		host.addMyWaiter(this); 
		waitergui.setBreak(false);
		host.restGui.setWaiterBackToWork(this);
		state = WaiterState.working; 
		
	}
	
	void actnTakeBreak() {
		
		//take the break; 
		state = WaiterState.onBreak;
		waitergui.setBreak(true);
		host.restGui.setWaiterOnBreak(this);	
		
	}
	
	void actnAskForBreak() {
		state = WaiterState.hold;
		host.msgCanITakeBreak(this); 
	}
	
	void actnOrderToCook(MyCustomer mc) {
		
		//found problem here, customer scheduler was clobbering, needed state change
		mc.s_ = MyCustomerState.waitingForFood;
		
		//formulate order
		Order o = new Order(this, mc.choice_, mc.tableNum_);
		orders.add(o);
		
		//move to cook
		waitergui.DoGoToCook();
		
		//control block, pause waiter agent while gui moves to cook
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//cook.msgHereIsAnOrder(this, o);
		monitor.insert(new CookAgent.MyOrder(this, o.choice_, o.tableNum_, CookAgent.orderState.pending));
		
		print ("delivered order to cook!!!"); 
		
	}
	
	void actnAskForOrderSecondTime (MyCustomer mc, String foodDontHave){
		
		print("askforordersecondtime called");
		
		mc.s_ = MyCustomerState.waitingToOrder; 
		
		//first move over their via a gui!!!!
		waitergui.DoGoToThisTable(mc.tableNum_);
	
		//control block, pause waiter agent while gui moves to table
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		print("WhatWouldYouLike called"); 
		mc.c_.msgWhatWouldYouLike(foodDontHave);
		//needs to happen instantly because waiter shouldnt leave table.
	}
	
	void actnAskForOrder(MyCustomer mc){
		//MyCustomerState is Waiting to Order
		
		//change state here because scheduler was outpacing my other actions
		mc.s_ = MyCustomerState.middleOfAction; 
	
		//first move over their via a gui!!!!
		waitergui.DoGoToThisTable(mc.tableNum_);
	
		print("aquire"); 
		//control block, pause waiter agent while gui moves to table
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		print("release"); 
		
		print("WhatWouldYouLike called"); 
		mc.c_.msgWhatWouldYouLike();
		//needs to happen instantly because waiter shouldnt leave table.
	}
	
	void actnSeatCustomer (MyCustomer mc) {
		
		print("bleh" + mc.c_.getGui().getYPos());
		
		/*will pick up customer at 0 position because this will be called before movement 
		unless we want to block movement to waiting positions...
		but i dont want to do this, because customers should be flexible, the action of going to the wait position
		shouldnt be blocked if there is a table available
		*/
		waitergui.DoGoPickupCust(mc.c_.getGui().getYPos()); //animation
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mc.c_.msgFollowMeToTable(mc.tableNum_, this);
		
		//block here to wait until customer away from waiting area? 
		//
		//
		//
		
		waitergui.DoGoToThisTable(mc.tableNum_); //animation
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mc.s_ = MyCustomerState.seated;
	}
	
	
	void actnServeFood (Order o) {

		waitergui.DoGoToCook();
			
		//control block, pause waiter agent while gui moves to cook
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		print ("picking up food now");
		//pick up food here --->
		//changes color to indicate he is holding food
		o.i_.gui_.orderPickedUp();
		waitergui.holdingFood();
		//.......
		
		waitergui.DoGoToThisTable(o.tableNum_);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//remove order from orders
		orders.remove(o);
		
		//waiter has served food at table, waiter now can confirm that customer has been served
		//and change customer state.  but no need to act on new state
		for (MyCustomer mc : customers) {
			if (mc.choice_ == o.choice_ && mc.tableNum_ == o.tableNum_) {
				mc.s_ = MyCustomerState.foodDelivered;
				mc.c_.msgHereIsYourFood();
				cashier.msgProduceABill(o, mc.c_);
				print("sent message produced bill"); 
				break;
			}
		}
		
		//changed color of waiter to indicated that he is no longer holding food
		waitergui.holdingFood();
		
	}
	
	void actnClearTable(MyCustomer mc) {
		
		mc.s_ = MyCustomerState.middleOfAction;
		
		//remove this customer so we don't keep serving them
		customers.remove(mc);
		
		print("CLEARING OUT NOW");
		host.msgTableIsFree(this, mc.tableNum_);
		
		//check for break here? or at the top of the scheduler
		
	}

	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	
	public boolean isWaitingForBreak() {
		if (state == WaiterState.wantBreak) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isOnBreak() {
		if (state == WaiterState.onBreak) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isWorking() {
		if (state == WaiterState.working) {
				return true;
			}
			else {
				return false;
			}
	}
	
	public String getName() {
		return name_;
	}

	public void setCook(CookAgent cook) {
		this.cook = cook; 
	}
	
	public void setHost(HostAgent host) {
		this.host = host;
	}
	
	public void setCashier(CashierAgent cashier) {
		this.cashier = cashier; 
	}
	
	public void setGui (WaiterGui gui) {
		waitergui = gui;
	}
	
	public WaiterGui getGui() {
		return waitergui;
	}
	
}