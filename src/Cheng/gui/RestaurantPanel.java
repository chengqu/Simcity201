package Cheng.gui;

import Cheng.*;
import Cheng.CookAgent.Order;
import Cheng.gui.WaiterGui;
import Cheng.interfaces.Waiter;

import javax.swing.*;

import simcity201.gui.GlobalMap;
import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;
import agents.Person;
import agents.ProducerConsumerMonitor;
import agents.Role;
import agents.Worker;
import agents.Role.roles;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel implements ActionListener{
	private RestaurantGui gui; //reference to main gui
	//Host, cook, waiters and customers
	private HostAgent host = new HostAgent("Sarah",this);
	private HostGui hostGui = new HostGui(host);
	
	// private WaiterAgent waiter = new WaiterAgent("MikeCai");
	//private WaiterGui waiterGui = new WaiterGui(waiter,gui);

	public CashierAgent cashier = new CashierAgent("Cashier",this);
	private CashierGui cashierGui = new CashierGui(cashier);
	private ProducerConsumerMonitor<Order> monitor = new ProducerConsumerMonitor<Order>(30);
	public CookAgent cook = new CookAgent("Rest6",this,monitor);
	private CookGui cookGui = new CookGui(cook);

	private MarketAgent Qmarket = new MarketAgent("Quincy Market",0,0,0,0);
	private MarketAgent Market1 = new MarketAgent("Market1",10,10,10,10);
	private MarketAgent Market2 = new MarketAgent("Market2",10,10,10,10);

	private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
	private Vector<Waiter> waiters = new Vector<Waiter>();
	private Vector<Worker> workers = new Vector<Worker>();

	private JPanel restLabel = new JPanel();
	private ListPanel customerPanel = new ListPanel(this, "Customers");
	private WaiterPanel waiterPanel = new WaiterPanel(this,"Waiters");
	private JPanel group = new JPanel();
	public JTextField namefield = new JTextField(10);

	final int wageHourInMili = 1000000;
	public int internalClock = 0;
	int wage = 20;// $20/hr
	public boolean isOpen = false;

	int numWaiters = 1;
	boolean haveCook = true;
	boolean haveHost = true;
	boolean haveCashier = true;
	boolean isclosing = false;
	private int worknumber = 0;
	Object lock = new Object();

	int maxWaiters = 3;

	public Timer wageTimer = new Timer(wageHourInMili, this);
	private int workCooknumber = 0;

	private int workHostnumber = 0;

	private int workCashiernumber = 0;



	public RestaurantPanel(RestaurantGui gui) {
		namefield.setSize(10, 10);
		this.gui = gui;
		monitor.setSubscriber(cook);
		cook.setMarket(Qmarket);
		cook.setMarket1(Market1);
		cook.setMarket2(Market2);
		Qmarket.setCook(cook);
		Qmarket.setCashier(cashier);
		Qmarket.startThread();

		Market1.setCook(cook);
		Market1.setCashier(cashier);
		Market1.startThread();

		Market2.setCook(cook);
		Market2.setCashier(cashier);
		Market2.startThread();

		//host.setWaiter(waiter);
		host.setCashier(cashier);
		host.setCook(cook);
		gui.animationPanel.addGui(hostGui);
		host.startThread();
		System.out.println("host start");

		//waiter.setHost(host);
		//waiter.setGui(waiterGui,0);
		cook.setGui(cookGui);
		gui.animationPanel.addGui(cookGui);
		// gui.animationPanel.addGui(waiterGui);
		//waiter.startThread();

		cashier.setCook(cook);
		cook.setCashier(cashier);
		//waiter.setCook(cook);
		//waiter.setCashier(cashier);
		cashier.setHost(host);
		cashier.startThread();
		cook.startThread();

		setLayout(new GridLayout(1, 2, 20, 20));
		group.setLayout(new GridLayout(1, 2, 10, 10));

		group.add(customerPanel);
		group.add(waiterPanel);
		group.add(namefield);
		initRestLabel();
		add(restLabel);
		add(group);

		wageTimer.setActionCommand("InternalTick");
		wageTimer.start();

	}

	public Role wantJob(Person p)
	{
		synchronized(lock)
		{
			if(!haveHost)
			{
				haveHost = true;
				return new Role(Role.roles.WorkerRossHost, gui.name);
			}
			else if(!haveCook)
			{

				AlertLog.getInstance().logMessage(AlertTag.LYN, "LYN","New Cook");
				haveCook = true;
				return new Role(Role.roles.WorkerRossCook, gui.name);
			}
			else if(!haveCashier)
			{
				haveCashier = true;
				return new Role(Role.roles.WorkerRossCashier, gui.name);
			}
			else if(numWaiters < maxWaiters)
			{
				numWaiters++;
				return new Role(Role.roles.WorkerRossWaiter, gui.name);
			}
			else
			{
				return null;
			}
		}
	}


	/**
	 * Sets up the restaurant label that includes the menu,
	 * and host and cook information
	 */
	private void initRestLabel() {
		JLabel label = new JLabel();
		//restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
		restLabel.setLayout(new BorderLayout());
		label.setText(
				"<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

		restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		restLabel.add(label, BorderLayout.CENTER);
		restLabel.add(new JLabel("               "), BorderLayout.EAST);
		restLabel.add(new JLabel("               "), BorderLayout.WEST);
	}

	/**
	 * When a customer or waiter is clicked, this function calls
	 * updatedInfoPanel() from the main gui so that person's information
	 * will be shown
	 *
	 * @param type indicates whether the person is a customer or waiter
	 * @param name name of person
	 */
	public void showInfo(String type, String name) {

		if (type.equals("Customers")) {

			for (int i = 0; i < customers.size(); i++) {
				CustomerAgent temp = customers.get(i);
				if (temp.getName() == name)
					gui.updateInfoPanel(temp);
			}
		}
		if (type.equals("Waiters")) {
			for (int i = 0; i < waiters.size(); i++) {
				WaiterAgent temp = (WaiterAgent) waiters.get(i);
				if (temp.getName() == name)
					gui.updateWaiterPanel(temp);
			}
		}
	}

	/**
	 * Adds a customer or waiter to the appropriate list
	 *
	 * @param type indicates whether the person is a customer or waiter (later)
	 * @param name name of person
	 */
	public void addPerson(Person p) {

		//if (type.equals("Customers")) {
		CustomerAgent c = new CustomerAgent(p);	
		CustomerGui g = new CustomerGui(c, gui);
		gui.animationPanel.addGui(g);// dw
		c.setHost(host);
		c.setCashier(cashier);
		c.setGui(g,1);
		customers.add(c);
		c.startThread();
		c.getGui().setHungry();
		//}
	}

	public void addWorker(Person p) {

		synchronized(workers){
			for (Role r : p.roles) {
				Role role = null;
				if(r.getRole() == roles.WorkerRossWaiter) {
					role = null;

					//WaiterProducer c = new WaiterProducer(p, p.getName(), pm);
					//WaiterAgent c = new WaiterAgent(p, p.getName(),this);	
					WaiterProducer c = new WaiterProducer(p,p.getName(),this,monitor);
					WaiterGui g = new WaiterGui(c,gui);
					waiters.add(c);
					c.setGui(g,0);
					gui.animationPanel.addGui(g);
					c.setHost(host);
					c.setCook(cook);
					c.setCashier(cashier);
					host.setWaiter(c);
					c.isWorking = true;
					c.setTimeIn(internalClock);
					workers.add(c);
					//c.getGui().setEnabled();
					AlertLog.getInstance().logMessage(AlertTag.Ross, "Ross","Waiter");
					this.worknumber++;
					c.startThread();




				}  else if(r.getRole() == roles.WorkerRossCook) {
					role = null;
					role = r;
					cook.p = p;
					workers.add(cook);
					cook.setTimeIn(internalClock);
					cook.isWorking = true;
					AlertLog.getInstance().logMessage(AlertTag.Ross, "Ross","Cook");
					this.workCooknumber++;

				} else if(r.getRole() == roles.WorkerRossHost) {
					role = null;
					role = r;
					host.p = p;
					host.name = p.getName();
					workers.add(host);
					//host.setTimeIn(internalClock);
					host.isWorking = true;
					AlertLog.getInstance().logMessage(AlertTag.Ross, "Ross","Host");
					this.workHostnumber++;

				} else if(r.getRole() == roles.WorkerRossCashier) {
					role = null;
					role = r;
					cashier.p = p;
					cashier.name = p.getName();
					workers.add(cashier);
					cashier.setTimeIn(internalClock);
					cashier.isWorking = true;
					AlertLog.getInstance().logMessage(AlertTag.Ross, "Ross","Cashier");
					this.workCashiernumber++;

				}

			}
		}
		if(worknumber>0 && workHostnumber>0 && workCashiernumber>0 && workCooknumber>0){
			OpenRestaurant();
		}
	}

	public void OpenRestaurant() {
		synchronized(workers){
			{

				AlertLog.getInstance().logMessage(AlertTag.Ross, "Ross","Opening!!!");
				isOpen = true;
				host.setTimeIn(internalClock);
				isclosing = false;

			}
		}
	}


	synchronized public void actionPerformed(ActionEvent arg0) {

		if(isOpen == true){
			if(arg0.getActionCommand().equals("InternalTick")) {
				internalClock+= 2;
				if(internalClock - host.getTimeIn() > 30){
					host.goHome();
					isclosing = true;
				}
			}
		}
	}

	synchronized public void closeRestaurant() {

		AlertLog.getInstance().logMessage(AlertTag.Ross, "Ross","Closed");
		isOpen = false;
		this.worknumber = 0;
		internalClock = 0;
		host.timeIn = 0;
		workers.clear();

		waiters.clear();

	}

	public void quitCook()
	{
		haveCook = false;
		GlobalMap.getGlobalMap().addJob(this.gui);
		AlertLog.getInstance().logMessage(AlertTag.Ross, "Ross","no Cook");
	}

	public void quitWaiter()
	{
		numWaiters--;
		GlobalMap.getGlobalMap().addJob(this.gui);
		AlertLog.getInstance().logMessage(AlertTag.Ross, "Ross","no Waiter");
	}

	public void quitCashier()
	{
		haveCashier = false;
		GlobalMap.getGlobalMap().addJob(this.gui);
		AlertLog.getInstance().logMessage(AlertTag.Ross, "Ross","no Cashier");
	}

	public void quitHost()
	{
		haveHost = false;
		GlobalMap.getGlobalMap().addJob(this.gui);
		AlertLog.getInstance().logMessage(AlertTag.Ross, "Ross","no Host");
	}

	public void getJobs() {
		for(int i = numWaiters; i<maxWaiters; i++){
			GlobalMap.getGlobalMap().addJob(this.gui);
		}
		if(!haveCashier) {
			GlobalMap.getGlobalMap().addJob(this.gui);
		} 

		if(!haveHost) {
			GlobalMap.getGlobalMap().addJob(this.gui);
		}
		if(!haveCook) {
			GlobalMap.getGlobalMap().addJob(this.gui);
		}

	}

	/*
    public void addWaiter(String type, String name, int number){
    	if (type.equals("Waiters")){
    		WaiterAgent w = new WaiterAgent(name);
    		WaiterGui g = new WaiterGui(w,gui);
    		waiters.add(w);
    		w.setGui(g,number);
    		gui.animationPanel.addGui(g);
    		w.setHost(host);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		host.setWaiter(w);
    		w.startThread();
    	}
    }

	 */

}
