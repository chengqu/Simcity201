package LYN;

import agent.Agent;
import agents.Person;
import agents.Worker;
import LYN.WaiterAgent.State;
import LYN.gui.HostGui;
import LYN.gui.RestaurantPanel;
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
public class HostAgent extends Agent implements Host, Worker{
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> customers
	= new ArrayList<MyCustomer>();
	public List<MyWaiter> waiters
	= new ArrayList<MyWaiter>();
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	public String name;
	private boolean full = true;
	public Person p = null;
	public int timeIn;
	public boolean isWorking;
	public CashierAgent cashier = null;
	public CookAgent cook = null;
	public RestaurantPanel r = null;


	public class MyWaiter {
		Waiter w;
		int custnum;
		int table;
		boolean rest;
		MyWaiter(Waiter w, int custnum,int table, boolean rest) {
			this.w = w;
			this.custnum = custnum;
			this.table = table;
			this.rest = rest;
		}


	}
	public class MyCustomer {
		Customer c;
		State s;

		MyCustomer(Customer c,  State s){
			this.c = c;    		
			this.s = s;
		}

	}
	enum State {waiting,seated};

	public HostGui hostGui = null;

	public HostAgent(String name, RestaurantPanel r) {
		super();
		this.r = r;
		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}


	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}


	public Collection getTables() {
		return tables;
	}



	// Messages
	public void msgBreak(Waiter w){
		int restnum = 0;
		for(MyWaiter waiter:waiters){
			if(waiter.rest == true){
				restnum ++;
			}

		}

		if(restnum == waiters.size()-1) {
			AlertLog.getInstance().logMessage(AlertTag.LYNhost, this.name,"waiter rest rejected, cuz I only have one waiter" + w.getName());
			AlertLog.getInstance().logMessage(AlertTag.LYN, this.name,"waiter rest rejected, cuz I only have one waiter" + w.getName());

		}
		for(MyWaiter waiter:waiters){
			if(waiter.w == w && restnum != waiters.size() - 1){
				waiter.rest = true;	

				print("Your are going to break  " + waiter.w.getName());
			}
		}
	}

	public void msgReturntoWork(Waiter w) {
		for (MyWaiter waiter:waiters) {
			if(waiter.w == w) {
				print("Waiter "+ w.getName() + " is returning to work");
				waiter.rest = false;
			}
		}
	}

	public void addwaiter(Waiter w){
		waiters.add(new MyWaiter(w,0,0,false));
		stateChanged();
	}

	public void msgIWantFood(Customer cust) {
		int i = 0;
		for (Table table : tables) {

			if (table.isOccupied()) {
				i++;
			}
		}

		if(i == 3) {
			full = true;
		} else {
			full = false;
		}
		customers.add(new MyCustomer(cust,State.waiting));
		stateChanged();
	}

	public void msgTableisfree(int t, Customer c) {
		for (Table table : tables) {
			if (table.tableNumber == t){
				AlertLog.getInstance().logMessage(AlertTag.LYNhost, this.name,table.getOccupant() + " leaving " + table);
				AlertLog.getInstance().logMessage(AlertTag.LYN, this.name,table.getOccupant() + " leaving " + table);


				table.setUnoccupied();

				for(MyWaiter waiter:waiters) {
					if(waiter.table == table.tableNumber){
						waiter.custnum--;
					}
				}
				stateChanged();
			}
		}

		for(MyCustomer c1:customers){
			if(c1.c == c){
				customers.remove(c1);
			}
		}



		if(isWorking == false && customers.size() == 0) {
			AlertLog.getInstance().logMessage(AlertTag.LYNhost, this.name,"Closing the restaurara");
			AlertLog.getInstance().logMessage(AlertTag.LYN, this.name,"Closing the resarafds");

			for(MyWaiter w: waiters){
				w.w.msgLeave();
			}
			this.waiters.clear();

			cook.msgLeave();
			cashier.msgLeave();
			r.closeRestaurant();
			this.p.msgDone();
			this.p = null;
		}

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
			if(this.p == null) {
				return false;
			}


			for (MyCustomer customer: customers) {
				if(full == true && customer.c.getName().equals("nowait")){
					Customerleave(customer);
					return true;
				}
			}

			for (Table table : tables) {
				if (!table.isOccupied()) {
					for (MyCustomer customer: customers){
						if(customer.s == State.waiting && waiters.size()!=0){
							int p = 0;
							for(int i=0; i<waiters.size(); i++) {	

								if ((p==0 && waiters.get(p).rest == true) || (waiters.get(i).custnum <= waiters.get(p).custnum && waiters.get(i).rest == false)){
									p = i;
								}
							}  

							seatCustomer(waiters.get(p), customer, table);//the action
							waiters.get(p).custnum++;
							waiters.get(p).table = table.tableNumber;

							return true;//return true to the abstract agent to reinvoke the scheduler.


						}
					}
				}
			}
		}catch (ConcurrentModificationException errorCCE) {
			return true;
		}



		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void Customerleave(MyCustomer customer){
		customer.c.msgcannotwait();
		customers.remove(customer);

		if(isWorking == false && customers.size() == 0) {
			for(MyWaiter w: waiters){
				w.w.msgLeave();
			}
			this.waiters.clear();

			cook.msgLeave();
			cashier.msgLeave();
			r.closeRestaurant();
			this.p.msgDone();
			this.p = null;
		}

	}


	private void seatCustomer(MyWaiter w, MyCustomer customer, Table table) {

		w.w.msgsitAtTable(customer.c, table.tableNumber);

		table.setOccupant(customer.c);
		customer.s = State.seated;
	}


	//utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}


	private class Table {
		Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}

	public void setTimeIn(int timeIn) {
		this.timeIn = timeIn;
	}

	@Override
	public int getTimeIn() {
		return timeIn;
	}

	@Override
	public void goHome() {
		isWorking = false;
		if(isWorking == false && customers.size() == 0) {
			AlertLog.getInstance().logMessage(AlertTag.LYNhost, this.name,"Closing the restaurara");
			AlertLog.getInstance().logMessage(AlertTag.LYN, this.name,"Closing the resarafds");

			for(MyWaiter w: waiters){
				w.w.msgLeave();
			}
			this.waiters.clear();

			cook.msgLeave();
			cashier.msgLeave();
			r.closeRestaurant();
			this.p.msgDone();
			this.p = null;
		}


	}
	public boolean isWorking() {
		return isWorking;
	}
	public Person getPerson() {
		return this.p;
	}


	public void setCook(CookAgent cook) {
		this.cook = cook;
		// TODO Auto-generated method stub

	}


	public void setCashier(CashierAgent cashier) {
		this.cashier = cashier;
		// TODO Auto-generated method stub

	}


	@Override
	public void msgLeave() {
		// TODO Auto-generated method stub

	}
}

