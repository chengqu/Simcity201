package Cheng;

import agent.Agent;
import agents.Person;
import agents.Role;
import agents.Worker;
import Cheng.gui.HostGui;
import Cheng.interfaces.Host;
import Cheng.interfaces.Waiter;
import Cheng.CashierAgent;
import Cheng.CookAgent;


import Cheng.gui.RestaurantPanel;


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
public class HostAgent extends Agent implements Host,Worker{
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public Collection<Table> tables;
	public List<MyWaiter> Waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public List<CustomerAgent> Customers = new ArrayList<CustomerAgent>();
	//public enum WaiterState{Busy, Available};
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	private boolean origin = false;
	public String name;
	private int seatnum;
	private Semaphore atTable = new Semaphore(0,true);
	public enum WaiterState{Pending,OnBreak, Working};
	public enum CustomerState{Hungry, Waiting, Leaving};
	public enum CashierState{ShortOfMoney,Loaned,NoDebt};
	private CashierState cstate = CashierState.NoDebt;
	//public HostGui hostGui = null;
	private int count = 3;
	private double loan = 0;
	private double money  = 1000000;
	private CashierAgent cashier;

	public Person p = null;
	public int timeIn;
	public boolean isWorking;

	public CookAgent cook = null;
	public RestaurantPanel r = null;
	public HostAgent(String name,RestaurantPanel r) {
		super();
		this.r = r;
		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		//Waiters.add(new WaiterAgent("MikeCai"));
	}

	public String getMaitreDName() {
		return name;
	}
	public void setWaiter(Waiter c){
		this.Waiters.add(new MyWaiter(c,WaiterState.Working));
	}

	public String getName() {
		return name;
	}

	public void setCashier(CashierAgent c){
		this.cashier = c;
	}
	public List getWaitingCustomers() {
		return Customers;
	}

	public Collection getTables() {
		return tables;
	}
	// Messages
	public void msgIWantFood(CustomerAgent cust) {
		Do("addCustomer");
		Customers.add(cust);
		stateChanged();
	}

	public void msgLeavingTable(int seatNum) {
		for (Table table : tables) {
			if (table.tableNumber == seatNum) {
				AlertLog.getInstance().logMessage(AlertTag.Rosshost, p.getName(),"customer leaving");
				table.setUnoccupied();
				Customers.remove(table.occupiedBy);
				stateChanged();
			}
		}

		if(isWorking == false && Customers.size() == 0) {
			AlertLog.getInstance().logMessage(AlertTag.Rosshost, this.name,"Closing the restaurara");
			AlertLog.getInstance().logMessage(AlertTag.Ross, this.name,"Closing the resarafds");

			for(MyWaiter w: Waiters){
				w.w.msgLeave();
			}
			this.Waiters.clear();

			cook.msgLeave();
			cashier.msgLeave();
			r.closeRestaurant();

			if(p.quitWork)
			{
				r.quitHost();
				p.canGetJob = false;
				p.quitWork = false;
				AlertLog.getInstance().logMessage(AlertTag.Ross, p.getName(),"I QUIT");
			}
			for(Role r : p.roles)
			{
				if(r.getRole().equals(Role.roles.WorkerRossHost))
				{
					p.roles.remove(r);
					break;
				}
			}
			p.payCheck += 30;

			this.p.msgDone();
			this.p = null;
		}



	}
	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	public void msgIsorigin(){
		origin = true;
		stateChanged();
	}
	public void msgWaiterOnBreak(WaiterAgent w){
		for(MyWaiter mw :Waiters){
			if(mw.w == w){
				mw.s = WaiterState.Pending;
			}
		}
		stateChanged();
	}
	public void msgWaiterOffBreak(WaiterAgent w){
		for(MyWaiter mw :Waiters){
			if(mw.w == w){
				mw.s = WaiterState.Working;
			}
		}
		stateChanged();
	}
	public void msgINeedMoney(double money){
		AlertLog.getInstance().logMessage(AlertTag.Rosshost, p.getName(),"loan money to cashier");
		cstate = CashierState.ShortOfMoney;
		this.loan = money;
		stateChanged();
	}
	public void msgPayDebt(double debt){
		AlertLog.getInstance().logMessage(AlertTag.Rosshost, p.getName(),"recieved debt from cashier");
		this.money += debt;
		cstate = CashierState.NoDebt;
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
		if(this.p == null) {
			return false;
		}


		int minIndex = 0;
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!Customers.isEmpty()) {
					for(int i = 0; i < Waiters.size()-1; i++){
						System.out.println(i);
						if(Waiters.get(i).CustNum < Waiters.get(i+1).CustNum){
							minIndex = i;
						}
						else{
							minIndex = i+1;
						}
					}
					Do("seatCustomer");
					Waiters.get(minIndex).CustNum++;
					seatCustomer(Waiters.get(minIndex).w,Customers.get(0), table);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.

				}
			}
		}


		synchronized(Waiters){
			for(MyWaiter mw :Waiters){
				if(mw.s == WaiterState.Pending){
					WaiterIsOnBreak(mw);
					return true;
				}
			}
		}
		if(cstate == CashierState.ShortOfMoney){
			LoanToCashier();
			return true;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(Waiter w,CustomerAgent customer, Table table) {
		AlertLog.getInstance().logMessage(AlertTag.Rosshost, p.getName(),"seating customer");
		customer.msgTableAvailable();
		w.msgSeatCustomer(customer, table.tableNumber);
		seatnum = table.tableNumber;
		//DoSeatCustomer(customer, table);
		table.setOccupant(customer);
		Customers.remove(customer);
	}
	private void WaiterIsOnBreak(MyWaiter w){
		Do("WaiterIsOnBreak");
		w.w.msgOnBreak();
		w.s = WaiterState.OnBreak;

	}
	// The animation DoXYZ() routines

	private void RestaurantFull(CustomerAgent c){
		c.msgTableFull();
		Customers.remove(c);
	}
	//utilities
	private void LoanToCashier(){
		this.money -= loan;
		cashier.msgHereIsMoney(loan);
		cstate = CashierState.Loaned;
	}

	private class MyWaiter {
		int CustNum = 0;
		Waiter w;
		WaiterState s;

		MyWaiter(Waiter c, WaiterState s){
			this.w = c;
			this.s = s;
		}
	}

	private class Table {
		CustomerAgent occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(CustomerAgent cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerAgent getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}

	@Override
	public void setTimeIn(int timeIn) {
		// TODO Auto-generated method stub
		this.timeIn = timeIn;
	}

	@Override
	public int getTimeIn() {
		// TODO Auto-generated method stub
		return timeIn;
	}

	@Override
	public void goHome() {
		// TODO Auto-generated method stub
		isWorking = false;
		if(isWorking == false && Customers.size() == 0) {
			AlertLog.getInstance().logMessage(AlertTag.Rosshost, this.name,"Closing the restaurara");
			AlertLog.getInstance().logMessage(AlertTag.Ross, this.name,"Closing the resarafds");

			for(MyWaiter w: Waiters){
				w.w.msgLeave();
			}
			this.Waiters.clear();

			cook.msgLeave();
			cashier.msgLeave();
			r.closeRestaurant();

			if(p.quitWork)
			{
				r.quitHost();
				p.canGetJob = false;
				p.quitWork = false;
				AlertLog.getInstance().logMessage(AlertTag.Ross, p.getName(),"I QUIT");
			}
			for(Role r : p.roles)
			{
				if(r.getRole().equals(Role.roles.WorkerRossHost))
				{
					p.roles.remove(r);
					break;
				}
			}
			p.payCheck += 30;

			this.p.msgDone();
			this.p = null;
		}

	}

	@Override
	public Person getPerson() {
		// TODO Auto-generated method stub
		return this.p;
	}

	@Override
	public void msgLeave() {
		// TODO Auto-generated method stub

	}

	public void setCook(CookAgent cook) {
		this.cook = cook;
		// TODO Auto-generated method stub

	}




}

