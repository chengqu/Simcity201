package david.restaurant;

import agent.Agent;
import david.restaurant.gui.HostGui;
import david.restaurant.gui.Table;
import david.restaurant.WaiterAgent;

import java.util.*;
import java.util.concurrent.Semaphore;
//import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

/**
 * Restaurant Host Agent
 */
public class HostAgent extends Agent {
	private List<myCustomer> customers = Collections.synchronizedList(new ArrayList<myCustomer>());
	private List<myWaiter> waiters = new ArrayList<myWaiter>();
	public Collection<Table> tables = new ArrayList<Table>();
	private enum cState{waiting, notified, canBeSeated, needsToMove, moving, goingToBeSeated, leaving, 
		needsToShift, beingServed};

	private Object customerLock = new Object();
	private Object tableLock = new Object();
	private Object waiterLock = new Object();
	private Object lineLock = new Object();
	
	private String name;
	
	int peopleInLine = 0;

	public HostGui hostGui = null;
	
	public void print_()
	{
		print(Integer.toString(customers.size() + waiters.size() + tables.size()));
	}

	public HostAgent(String name) {
		super();

		this.name = name;
		// make some tables
		tables = new ArrayList<Table>();
	}
	
	public void msgAddTable(Table t)
	{
		synchronized(tableLock)
		{
			tables.add(t);
			stateChanged();
		}
	}
	
	public void AddWaiter(WaiterAgent w)
	{
		synchronized(waiterLock)
		{
			waiters.add(new myWaiter(w));
		}
		stateChanged();
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return customers;
	}

	public Collection getTables() {
		return tables;
	}
	
	public void gMsgCustomerReady(CustomerAgent cust)
	{
		synchronized(customerLock)
		{
			for(myCustomer c: customers)
			{
				if(c.customer == cust)
				{
					c.state = cState.canBeSeated;
					break;
				}
			}
		}
		stateChanged();
	}
	
	public void gMsgShiftCustomers()
	{
		print("gMsgShiftCustomers");
		synchronized(customerLock)
		{
			for(myCustomer cust: customers)
			{
				if(cust.state == cState.canBeSeated || cust.state == cState.needsToMove || cust.state == cState.moving)
				{
					cust.placeInLine--;
					cust.state = cState.needsToMove;
				}
			}
			peopleInLine--;
		}
		stateChanged();
	}
	
	// Messages
	public void msgIWantToEat(CustomerAgent cust) {
		synchronized(customerLock)
		{
			print(Integer.toString(tables.size()));
			print("msgIWanttoEat");
			int numCustomers = 0;
			for(myCustomer c: customers)
			{
				if(c.state == cState.canBeSeated || c.state == cState.moving || c.state == cState.needsToMove 
						|| c.state == cState.needsToShift || c.state == cState.beingServed)
				{
					numCustomers++;
				}
			}
			if(numCustomers >= tables.size())
			{
				print("waiting customer");
				customers.add(new myCustomer(cust, cState.waiting, -1));
			}
			else
			{
				customers.add(new myCustomer(cust, cState.needsToMove, peopleInLine++));
			}
			stateChanged();
			return;
		}
	}
	
	public void msgStaying(CustomerAgent c)
	{
		synchronized(customerLock)
		{
			boolean temp = false;
			for(myCustomer mc: customers)
			{
				if(mc.customer == c)
				{
					mc.state = cState.needsToMove;
					mc.placeInLine = peopleInLine++;
					temp = true;
					break;
				}
			}
			if(!temp)
			{
				
				customers.add(new myCustomer(c, cState.needsToMove, peopleInLine++));
				print("problem");
			}
		}
		stateChanged();
	}
	
	void msgLeaving(CustomerAgent c)
	{
		synchronized(customerLock)
		{
			myCustomer temp = null;
			for(myCustomer mc: customers)
			{
				if(mc.customer == c)
				{
					temp = mc;
					break;
				}
			}
			if(temp != null)
			{
				customers.remove(temp);
			}
		}
		stateChanged();
	}
	
	public void msgTableIsFree(int t, WaiterAgent w, CustomerAgent c)
	{
		Table table = null;
		synchronized(tableLock)
		{
			for(Table temp:tables)
			{
				if(temp.tableNumber == t)
				{
					table = temp;
					break;
				}
			}
			if(table != null)
			{
				table.setUnoccupied();
			}
		}
		synchronized(waiterLock)
		{
			for(myWaiter waiter: waiters)
			{
				if(waiter.w == w)
				{
					waiter.numCustomers--;
					break;
				}
			}
		}
		synchronized(customerLock)
		{
			myCustomer customer = null;
			for(myCustomer cust: customers)
			{
				if(cust.customer == c)
				{
					customer = cust;
				}
			}
			if(customer != null)
			{
				customers.remove(customer);
			}
		}
		stateChanged();
	}
	
	public void msgWantToGoOnBreak(WaiterAgent w)
	{
		myWaiter temp = null;
		synchronized(waiterLock)
		{
			for(myWaiter waiter: waiters)
			{
				if(waiter.w == w)
				{
					waiter.wantBreak = true;
					break;
				}
			}
		}
		stateChanged();
	}
	
	public void msgOffBreak(WaiterAgent w, int numCustomers)
	{
		synchronized(waiterLock)
		{
			for(myWaiter waiter: waiters)
			{
				if(waiter.w == w)
					return;
			}
			waiters.add(new myWaiter(w, numCustomers));
		}
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(waiterLock)
		{
			for(myWaiter waiter: waiters)
			{
				if(waiter.wantBreak == true)
				{
					DoGiveBreak(waiter);
					return true;
				}
			}
		}
		
		synchronized(customerLock)
		{
			boolean temp = false;
			for(myCustomer mc: customers)
			{
				if(mc.state == cState.waiting || mc.state == cState.leaving)
				{
					temp = true;
				}
			}
			if(temp)
			{
				DoNotifyCustomers();
				return true;
			}
		}
		
		synchronized(customerLock)
		{
			for(myCustomer cust: customers)
			{
				if(cust.state == cState.needsToMove)
				{
					cust.state = cState.moving;
					cust.customer.getGui().msgGoToWaitingArea(100 - cust.placeInLine * 30, 50);
					return true;
				}
			}
		}
		
	  	if(customers.isEmpty() == false)
	  	{
	  		synchronized(tableLock)
	  		{
	  			synchronized(customerLock)
	  			{
	  				boolean canSeat = false;
	  				for(myCustomer cust: customers)
	  				{
	  					if(cust.state == cState.canBeSeated)
	  					{
	  						canSeat = true;
	  						break;
	  					}
	  				}
	  				if(canSeat)
	  				{
				  		for(Table temp: tables)
				  		{
				  			if(temp.isOccupied() == false)
				  			{
				  				if(waiters.size() > 0)
				  				{
				  					DoMessageWaiterNewCust(temp);
				  					return true;
				  				}
				  			}
				  		}
	  				}
	  			}
	  		}
	  	}
	  	return false;
	}

	// Actions
	//utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}
	
	private class myWaiter
	{
		public WaiterAgent w;
		public int numCustomers;
		public boolean wantBreak;
		public myWaiter(WaiterAgent waiter)
		{
			w = waiter;
			numCustomers = 0;
			wantBreak = false;
		}
		public myWaiter(WaiterAgent waiter, int n) {
			// TODO Auto-generated constructor stub
			w = waiter;
			numCustomers = n;
			wantBreak = false;
		}
	}
	//actions

	private void DoGiveBreak(myWaiter w)
	{
		if(waiters.size() > 1)
		{
			w.wantBreak = false;
			waiters.remove(w);
			w.w.msgOkBreak();
		}
		else
		{
			w.wantBreak = false;
			w.w.msgNoBreak();
		}
	}
	
	void DoNotifyCustomers()
	{
		synchronized(customerLock)
		{
			print("notifying customers");
			List<myCustomer> list = new ArrayList<myCustomer>();
			for(myCustomer c: customers)
			{
				if(c.state == cState.waiting)
				{
					c.state = cState.notified;
					c.customer.msgIsFull();
				}
				else if(c.state == cState.leaving)
				{
					list.add(c);
				}
			}
			for(myCustomer mc:list)
			{
				customers.remove(mc);
			}
		}
	}
	
	void DoMessageWaiterNewCust(Table t)
	{
		myCustomer c = null;
		synchronized(customerLock)
		{
			for(myCustomer mc: customers)
			{
				if(mc.state == cState.canBeSeated && mc.placeInLine == 0)
				{
					c = mc;
					mc.state = cState.goingToBeSeated;
				}
			}
			if(c == null)
			{
				return ;
			}
		}
		synchronized(waiterLock)
		{
			if(waiters.size() > 0)
			{
				myWaiter w = waiters.get(0);
				int min = w.numCustomers;
				for(myWaiter temp: waiters)
				{
					if(temp.numCustomers < min)
					{
						min = temp.numCustomers;
						w = temp;
					}
				}
				c.state = cState.beingServed;
				t.setOccupant(c.customer);
				w.numCustomers++;
				w.w.msgPleaseSitCustomer(c.customer, t.tableNumber);
			}
		}
	}
	
	private class myCustomer
	{
		public myCustomer(CustomerAgent cust, cState c, int p) 
		{
			customer = cust;
			state = c;
			placeInLine = p;
		}
		public int placeInLine;
		CustomerAgent customer;
		cState state;
	}
}

