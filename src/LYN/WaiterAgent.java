package LYN;

import agent.Agent;
import agents.Person;
import agents.Worker;
import LYN.CustomerAgent.AgentEvent;
import LYN.gui.HostGui;
import LYN.gui.WaiterGui;
import LYN.interfaces.Cashier;
import LYN.interfaces.Cook;
import LYN.interfaces.Customer;
import LYN.interfaces.Host;
import LYN.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class WaiterAgent extends Agent implements Waiter,Worker{

	public List<MyCustomer> customers
	= new ArrayList<MyCustomer>();

	public class MyCustomer{
		Customer c;
		int table;
		String choice;
		State s;
		int ypos;

		MyCustomer(Customer c, int table, String choice, State s, int ypos){
			this.c = c;
			this.table = table;
			this.choice = choice;
			this.s = s;
			this.ypos = ypos;
		}


	}

	enum State { waiting,seated,readytoorder,asked,ordered,alreadyordered,reorder,reordered,orderisready,gotfood,leaving,doneleaving};

	private String name;
	private Host h;
	private Cook cook;
	private Cashier cashier;
	private Menu menu;
	boolean added = false;
	boolean Break = false;
	boolean OnBreaking = false;
	boolean Breakingdown = false;
	private Semaphore atTable = new Semaphore(0,true);
	boolean origin = false;
	public WaiterGui waiterGui = null;
	Timer timer = new Timer();
	public int a = 0;
	public int b = 0;
	public Person p;
	public boolean isWorking;
	public WaiterAgent(Person p, String name) {
		super();
		this.p = p;
		this.name = name;
		// make some tables

	}


	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setCook(CookAgent cook){
		this.cook = cook;
	}

	public void setHost(HostAgent h){
		this.h = h;
	}

	public void setCashier(CashierAgent cashier){
		this.cashier = cashier;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	// Messages
	public void added(){
		added = true;
	}

	public void msgsitAtTable(Customer cust, int table) {
		AlertLog.getInstance().logMessage(AlertTag.LYNWaiter, this.name,"Receiving msgstiattable");
		AlertLog.getInstance().logMessage(AlertTag.LYN, this.name,"Receiving msgstiattable");


		customers.add(new MyCustomer(cust,table," ",State.waiting, cust.getGui().xPos));
		stateChanged();
	}

	public void msgWannaBreak(){
		Break = true;
		stateChanged();
	}

	public void msgReturnToWork() {
		Breakingdown = true;
	}

	public void msgReadyToOrder(Customer c) {
		AlertLog.getInstance().logMessage(AlertTag.LYNWaiter, this.name,"Receving customer message ready to order");
		AlertLog.getInstance().logMessage(AlertTag.LYN, this.name,"Receving customer message ready to order");


		for (MyCustomer mc: customers){
			if (mc.c == c) {
				mc.s = State.readytoorder;
				stateChanged();
			}
		}
	}

	public void msgHereismychoice(Customer c, String choice){
		AlertLog.getInstance().logMessage(AlertTag.LYNWaiter, this.name,"Receiving Customer choice" + choice + c);
		AlertLog.getInstance().logMessage(AlertTag.LYN, this.name,"Receiving Customer choice" + choice + c);


		for (MyCustomer mc: customers){
			if (mc.c == c) {

				mc.s = State.ordered;
				mc.choice = choice;
				stateChanged();
			}
		}
	}

	public void msgRunoutoffood(String choice, int table){

		for (MyCustomer mc: customers){
			if (mc.table == table) {
				AlertLog.getInstance().logMessage(AlertTag.LYNWaiter, this.name,"Running out of food, ready to ask customer " + mc.c +" to reorder");
				AlertLog.getInstance().logMessage(AlertTag.LYN, this.name,"Running out of food, ready to ask customer " + mc.c +" to reorder");


				mc.s = State.reorder;
				stateChanged();
			}
		}
	}

	public void msgOrderisReady(String choice, int table){


		for (MyCustomer mc: customers){
			if (mc.table == table) {
				mc.s = State.orderisready;
				stateChanged();
			}
		}
	}

	public void msgDoneEatingAndLeaving(Customer c){

		for (MyCustomer mc: customers){
			if (mc.c == c) {
				mc.s = State.leaving;
				stateChanged();
			}
		}
	}

	public void msgAtTable() {//from animation

		atTable.release();
		stateChanged();
	}	


	public void msgArrivingatTable() {

		stateChanged();
	}

	public void msgAtOrigin() {		
		origin = true;
		stateChanged();
	}

	public void msgnotatOrigin(){
		origin = false;
		stateChanged();
	}	


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		try {
			if(isWorking == false) {
				isWorking = true;
				LeaveRestaurant();
				return true;
			}

			if(added == true) {
				hostadded();
			}

			if(Break == true) {
				Callhostbreak();
			}

			if(Breakingdown == true) {
				callhostbreakingdown();
			}
			for (MyCustomer c: customers){
				if (c.s == State.waiting && origin == true){

					WaiterSeatCustomer(c);
					return true;
				}
			}
			for (MyCustomer c: customers ){
				if (c.s == State.readytoorder ){
					GoToCustomer(c);
					return true;
				} 
			}
			for (MyCustomer c: customers ){

				if (c.s == State.reorder ){
					print("hello");
					GoToCustomerforreorder(c);
					return true;
				} 
			}
			for (MyCustomer c: customers){
				if (c.s == State.ordered){
					Sendordertocook(c);
					return true;
				} 
			}
			for (MyCustomer c: customers){
				if (c.s == State.orderisready){
					deliverfoodtocustomer(c);
					return true;
				} 
			}
			for (MyCustomer c: customers){
				if (c.s == State.leaving) {
					Tellhost(c);
					return true;
				}

			}
		} catch (ConcurrentModificationException errorCCE) {
			return true;
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	public void hostadded(){
		h.addwaiter(this);
		added = false;
	}

	public void WaiterSeatCustomer(MyCustomer c){


		waiterGui.DoMovetoCustomer(c.ypos);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.c.msgFollowMe(this, c.table, menu);
		waiterGui.DoBringToTable(c.table);

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		waiterGui.DoLeaveCustomer();
		c.s = State.seated;
	}

	// The animation DoXYZ() routines
	private void Callhostbreak(){

		h.msgBreak(this);
		Break = false;
		OnBreaking = true;
	}

	public void GoToCustomer(MyCustomer c) {
		waiterGui.DoBringToTable(c.table);



		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c.c.msgwhatwouldyoulike();

		c.s = State.asked;
	}

	public void GoToCustomerforreorder(MyCustomer c) {
		waiterGui.DoBringToTable(c.table);

		AlertLog.getInstance().logMessage(AlertTag.LYNWaiter, this.name,"Sending msg to customer whatdoyouwant to reorder");
		AlertLog.getInstance().logMessage(AlertTag.LYN, this.name,"Sending msg to customer whatdoyouwant to reorder");


		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c.c.msgwhatwouldyouliketoreorder(c.choice);
		c.s = State.reordered;
	}
	public void Sendordertocook(MyCustomer c) {
		waiterGui.Dosendordertocook();


		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.s = State.alreadyordered;
		waiterGui.DoMoveAway();
		cook.msgHereisanOrder(this, c.choice, c.table);




	}

	public void deliverfoodtocustomer(MyCustomer c) {


		waiterGui.Domovetocook();
		if(c.choice.equals("Steak")){
			a = 1;
		} else if(c.choice.equals("Chicken")){
			a = 2;
		} else if(c.choice.equals("Salad")){
			a = 3;
		} else if(c.choice.equals("Pizza")){
			a = 4;
		} 


		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		a=0;

		if(c.choice.equals("Steak")){
			b = 1;
		} else if(c.choice.equals("Chicken")){
			b = 2;
		} else if(c.choice.equals("Salad")){
			b = 3;
		} else if(c.choice.equals("Pizza")){
			b = 4;
		} 
		waiterGui.DoBringToTable(c.table);

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		b=0;
		c.c.msgHereisyourfood();


		c.s = State.gotfood;
		waiterGui.DoLeaveCustomer();
		cashier.msghereisthebill(c.c, c.choice);

	}

	private void LeaveRestaurant() {
		waiterGui.DoLeaveCustomer();

		p.msgDone();
	}

	public void Tellhost(MyCustomer c) {

		h.msgTableisfree(c.table, c.c);
		c.s = State.doneleaving;
		c.table = 0;
		customers.remove(c);
		waiterGui.DoLeaveCustomer();
		if (customers.size() == 0 && OnBreaking == true){


			timer.schedule(new TimerTask() {
				Object cookie = 1;
				public void run() {

					OnBreaking = false;
					msgReturnToWork();

				}
			},
			10000);
		}

	}

	public void callhostbreakingdown() {
		h.msgReturntoWork(this);
		Breakingdown = false;
	}
	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}


	@Override
	public void setTimeIn(int timeIn) {
		// TODO Auto-generated method stub

	}


	@Override
	public int getTimeIn() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void goHome() {
		// TODO Auto-generated method stub

	}


	@Override
	public Person getPerson() {
		// TODO Auto-generated method stub
		return this.p;
	}


	@Override
	public void msgLeave() {
		// TODO Auto-generated method stub
		isWorking = false;
		stateChanged();
	}



}

