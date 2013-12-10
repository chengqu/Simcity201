package Cheng;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;
import Cheng.CookAgent.CookState;
import Cheng.CookAgent.Order;
import Cheng.gui.RestaurantPanel;
import Cheng.gui.WaiterGui;
import Cheng.interfaces.Waiter;
import agent.Agent;
import agents.Person;
import agents.ProducerConsumerMonitor;
import agents.Role;
import agents.Worker;

public class WaiterProducer extends Agent implements Waiter,Worker{
	public ProducerConsumerMonitor<CookAgent.Order> monitor;
	public List<MyCustomer> Customers
	= new ArrayList<MyCustomer>();
	private Menu menu = new Menu();
	private boolean origin = false;
	private boolean atcook = false;
	private boolean served = false;
	private String name;
	public int seatnum;
	public Semaphore atTable = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0,true);
	private Semaphore atCustomer = new Semaphore(0,true);
	private Semaphore atCashier = new Semaphore(0,true);
	private CookAgent Cook;
	public WaiterGui waiterGui = null;
	public HostAgent host;
	public CashierAgent cashier;
	public enum CustomerState{DoingNothing,Waiting, Seated, ReadyToOrder, Asked,Ordered, BeingServed,Served, Leaving, Eating, WaitingForfood, Reorder,ReadyToPay, Paying, ReadyToSeat};
	private boolean isBreak = false;
	public Person p;
	public boolean isWorking;
	public RestaurantPanel rp;
	public WaiterProducer(Person p,String name, RestaurantPanel rp,ProducerConsumerMonitor<CookAgent.Order> monitor) {
		super();
		this.p = p;
		this.rp = rp;
		this.name = name;
		// make some tables
		this.monitor = monitor;

	}
	public void setCashier(CashierAgent c){
		this.cashier = c;
	}
	public void setHost(HostAgent h){
		this.host = h;
	}
	public void setCook(CookAgent cook){
		this.Cook = cook;
	}
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getMyCustomers() {
		return Customers;
	}
	
	
	public void msgSeatCustomer(CustomerAgent c, int table){
		Do("msgSeatCustomer");
		Customers.add(new MyCustomer(c,table,CustomerState.Waiting));
		stateChanged();
	}
	public void msgIWantFood(CustomerAgent c) {
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"I want food");
		for(MyCustomer mycust : Customers){
			if(mycust.getCust() == c)
			{
				MyCustomer mc = mycust;
				mc.s = CustomerState.ReadyToOrder;
			}
		}
		stateChanged();
	}

	public void msgHereIsMyOrder(CustomerAgent c, String Choice){
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"Here is my order");
		for(MyCustomer mycust : Customers){
			if(mycust.getCust() == c)
			{
				MyCustomer mc = mycust;
				mc.Choice = Choice;
				mc.s = CustomerState.Asked;
				seatnum = mc.table;
			}
		}
		stateChanged();
	}

	public void msgFoodReady(String Choice, int table){
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"Foode ready");
		for(MyCustomer mycust : Customers){
			if(mycust.table == table && mycust.Choice == Choice){
				mycust.s = CustomerState.BeingServed;
			}
		}
		stateChanged();
	}
	public void msgIWantToPay(CustomerAgent c){
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"msg I want to pay");
		for(MyCustomer mycust : Customers){
			if(mycust.getCust() == c){
				mycust.s = CustomerState.Paying;
			}
		}
		stateChanged();
	}
	public void msgLeavingTable(CustomerAgent c) {
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"msg leaveing table");
		for(MyCustomer mycust : Customers){
			if(mycust.getCust() == c)
			{
				mycust.s = CustomerState.Leaving;
			}
		}

		stateChanged();
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
	public void msgAtCook(){
		atcook = true;
		atCook.release();
		stateChanged();
	}
	public void msgAtCashier(){
		atCashier.release();
		stateChanged();
	}
	public void msgAtCustomer(){
		atCustomer.release();
		stateChanged();
	}
	public void msgOutOfFood(String Choice, int table){
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"OUt of food");
		for(MyCustomer mycust : Customers){
			if(mycust.table == table && mycust.Choice.equals(Choice)){
				menu.menu.remove(Choice);
				mycust.s = CustomerState.Reorder;
			}
		}
		stateChanged();
	}
	public void msgOnBreak(){
		isBreak = true;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		for(MyCustomer mycust : Customers) {
			if (mycust.s == CustomerState.Waiting && origin == true) {
				origin = false;
				PickCustomer(mycust);//the action
				return true;//return true to the abstract agent to reinvoke the scheduler.
			}
		}
		
		if(isWorking == false) {
			isWorking = true;
			LeaveRestaurant();
			return true;
		}

		try{
			for(MyCustomer mycust : Customers){
				if(mycust.s == CustomerState.Leaving){
					NotifyHost(mycust);
					return true;
				}
			}

			

			for(MyCustomer mycust : Customers) {
				if (mycust.s == CustomerState.ReadyToSeat) {
					seatCustomer(mycust);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}

			for(MyCustomer mycust : Customers){
				if(mycust.s == CustomerState.ReadyToOrder){
					origin = false;
					TakeOrder(mycust);
					return true;
				}
			}
			for(MyCustomer mycust : Customers){
				//System.out.println("!"+mycust.s);
				if(mycust.s == CustomerState.Reorder && atcook == true){
					origin = false;
					atcook = false;
					ReOrder(mycust);
					return true;
				}
			}
			for(MyCustomer mycust : Customers){
				if(mycust.s == CustomerState.Ordered){
					WaitOrder(mycust);
					return true;
				}
			}
			for(MyCustomer mycust : Customers){
				if(mycust.s == CustomerState.Asked){
					origin = false;
					GiveOrder(mycust);
					return true;
				}
			}

			for(MyCustomer mycust : Customers){
				if(mycust.s == CustomerState.WaitingForfood){
					origin = false;
					WaitCook(mycust);
					return true;
				}
			}
			for(MyCustomer mycust : Customers ){
				if(mycust.s == CustomerState.BeingServed && atcook == true){
					origin = false;
					atcook = false;
					ServeFood(mycust);
					return true;
				}
			}
			for(MyCustomer mycust : Customers){
				if(mycust.s == CustomerState.Paying){
					origin = false;
					TakeCheck(mycust);
					return true;
				}
			}
			if(Customers.size() == 0 && origin == true && isBreak == true ){
				isBreak = false;
				origin = false;
				isOnBreak();
				return true;
			}
		}catch(ConcurrentModificationException e){
			return false;
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void PickCustomer(MyCustomer c){
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"Picking up customer");
		waiterGui.DoGoToCustomer();
		try {
			atCustomer.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.c.msgReadyToSeat();
		c.s = CustomerState.ReadyToSeat;
	}
	private void seatCustomer(MyCustomer c) {
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"seat customer");
		waiterGui.DoBringToTable(c.c,c.table); 
		c.c.msgFollowMe(new Menu(),c.table,this);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoLeaveCustomer();
		c.s = CustomerState.Seated;

	}

	// The animation DoXYZ() routines
	private void LeaveRestaurant() {
		waiterGui.DoLeaveCustomer();

		if(p.quitWork)
		{
			rp.quitWaiter();
			p.canGetJob = false;
			p.quitWork = false;
			AlertLog.getInstance().logMessage(AlertTag.Ross, p.getName(),"I QUIT");
		
		for(Role r : p.roles)
		{
			if(r.getRole().equals(Role.roles.WorkerRossWaiter))
			{
				p.roles.remove(r);
				break;
			}
		}
		}

		p.msgDone();
	}


	private void TakeOrder(MyCustomer c){
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"Taking order");
		waiterGui.DoBringToTable(c.c,c.table); 
		c.c.msgWhatDoYouWant();
		c.s = CustomerState.Ordered;
	}

	private void WaitOrder(MyCustomer c){
		waiterGui.DoBringToTable(c.c,c.table); 
	}
	private void GiveOrder(MyCustomer c){
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"Giving Order");

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.s = CustomerState.WaitingForfood;
		//Cook.msgCookOrder(this, c.Choice, c.table);
		monitor.insert(new Order(this,c.Choice,c.table,CookState.ProducerFancy));
	}
	private void WaitCook(MyCustomer c){
		waiterGui.DoLeaveCustomer();

	}
	private void ReOrder(MyCustomer c){
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"Let customer reorder");
		waiterGui.DoBringToTable(c.c, c.table);
		c.c.msgReorder(menu);
		c.s = CustomerState.Ordered;
	}

	private void ServeFood(MyCustomer c){
		waiterGui.DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"serving food");
		waiterGui.msgshowOrder(c.Choice);
		waiterGui.DoBringToTable(c.c, c.table);
		System.out.println(c.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.c.msgHereIsTheFood();
		c.s = CustomerState.Eating;
		waiterGui.DoLeaveCustomer();
		waiterGui.msghideOrder();
	}
	private void TakeCheck(MyCustomer c){
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"Taking the check");
		waiterGui.DoBringToTable(c.c, c.table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoGoToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cashier.msgComputeCheck(this, c.c, c.Choice);
		c.s = CustomerState.Leaving;
	}
	private void NotifyHost(MyCustomer c){
		AlertLog.getInstance().logMessage(AlertTag.RossWaiter, p.getName(),"Notify Host");
		waiterGui.DoLeaveCustomer();
		c.c.msgGoToPay();
		host.msgLeavingTable(c.table);
		c.s = CustomerState.DoingNothing;
		Customers.remove(c);
	}
	
	private void isOnBreak(){
		waiterGui.setWaiterEnabled();
	}
	//utilities

	public void setGui(WaiterGui gui,int count) {
		waiterGui = gui;
		waiterGui.setNumber(count);
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	public void setFood(){
		Cook.setRunOutOfFood();
	}
	public class MyCustomer {
		CustomerAgent c;
		public int table;
		String Choice;
		CustomerState s;
		double Cash;

		MyCustomer(CustomerAgent c, int table, CustomerState s){
			this.c = c;
			this.table = table;
			this.s = s;
		}
		public CustomerAgent getCust(){
			return this.c;
		}
		public String getChoice(){
			return Choice;
		}

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
		return null;
	}
	@Override
	public void msgLeave() {
		// TODO Auto-generated method stub
		isWorking = false;
		stateChanged();
	}
	@Override
	public void IWantBreak() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void OffBreak() {
		// TODO Auto-generated method stub
		
	}

}


