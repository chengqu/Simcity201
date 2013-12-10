package LYN.gui;

import LYN.CashierAgent;
import LYN.CookAgent;
import LYN.CustomerAgent;
import LYN.HostAgent;
import LYN.Market;
import LYN.WaiterAgent;
import LYN.Menu;
import LYN.WaiterProducer;
import LYN.interfaces.Waiter;

import javax.swing.*;

import simcity201.gui.BankTellerGui;
import simcity201.gui.GlobalMap;
import simcity201.test.mock.LoggedEvent;
import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;
import agents.BankTellerAgent;
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
public class RestaurantPanel extends JPanel implements ActionListener {

	//Host, cook, waiters and customers
	private HostAgent host = new HostAgent("Sarah",this);
	//private WaiterAgent waiter = new WaiterAgent("waiter");

	private Market market1 = new Market("Superior"); 
	private Market market2 = new Market("Ralphs");
	private Market market3 = new Market("FreshAndEasy");
	private Menu menu = new Menu("menu");
	public CashierAgent cashier = new CashierAgent("Cashier");
	private Waiter temp;
	private HostGui hostGui = new HostGui(host);
	private ProducerConsumerMonitor<CookAgent.Order> pm = new ProducerConsumerMonitor<CookAgent.Order>(30);
	public CookAgent cook = new CookAgent("Rest3", cashier, this, pm);
	private int i = 0; int j = 0;
	//private WaiterGui waiterGui = new WaiterGui(waiter);

	private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
	private Vector<Waiter> waiters = new Vector<Waiter>();
	private Vector<Worker> workers = new Vector<Worker>();
	private Worker w = host;

	private JPanel restLabel = new JPanel();
	private ListPanel customerPanel = new ListPanel(this, "Customers");
	private WaiterPanel waiterpanel = new WaiterPanel(this, "Waiters");
	private JPanel group = new JPanel();

	private RestaurantGui gui; //reference to main gui
	final int wageHourInMili = 10;
	public int internalClock = 0;
	int wage = 20;// $20/hr
	public boolean isOpen = false;

	int numWaiters = 1;
	boolean haveCook = true;
	boolean haveHost = true;
	boolean haveCashier = true;
   private int worknumber = 0;
	Object lock = new Object();

	int maxWaiters = 3;

	public Timer wageTimer = new Timer(wageHourInMili, this);

	public RestaurantPanel(RestaurantGui gui) {
		this.gui = gui;
		CookGui cookGui = new CookGui(cook, gui);
		host.setGui(hostGui);
		host.setCook(cook);
		host.setCashier(cashier);
		cook.setGui(cookGui);
		cookGui.setPresent(true);
		gui.animationPanel.addGui(hostGui);
		gui.animationPanel.addGui(cookGui);
		pm.setSubscriber(cook);
		host.startThread();
		cook.startThread();
		market1.startThread();
		market2.startThread();
		market3.startThread();
		cashier.setcook(cook);
		cashier.startThread();

		market1.setCook(cook);
		market2.setCook(cook);
		market3.setCook(cook);
		market1.setCashier(cashier);
		market2.setCashier(cashier);
		market3.setCashier(cashier);
		cook.msgaddmarket(market1);
		cook.msgaddmarket(market2);
		cook.msgaddmarket(market3);

		cashier.msgaddmarket(market1);
		cashier.msgaddmarket(market2);
		cashier.msgaddmarket(market3);



		setLayout(new GridLayout(1, 2, 20, 20));
		group.setLayout(new GridLayout(1, 2, 10, 10));

		group.add(customerPanel);
		group.add(waiterpanel);

		initRestLabel();
		add(restLabel);
		add(group);

		wageTimer.setActionCommand("InternalTick");
		wageTimer.start();
		
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
				CustomerAgent temp1 = customers.get(i);
				if (temp1.getName() == name)
					gui.updateInfoPanel(temp1);
			}
		}
		if (type.equals("Waiters")) {

			for (int i = 0; i < waiters.size(); i++) {
				Waiter temp2 = waiters.get(i);
				if (temp2.getName() == name)
				{
					gui.updateInfoPanel(temp2);
					temp = temp2;
				}
			}
		}
	}

	public Role wantJob(Person p)
	{
		synchronized(lock)
		{
			if(!haveHost)
			{
				haveHost = true;
				return new Role(Role.roles.WorkerLYNHost, gui.name);
			}
			else if(!haveCook)
			{

				AlertLog.getInstance().logMessage(AlertTag.LYN, "LYN","New Cook");
				haveCook = true;
				return new Role(Role.roles.WorkerLYNCook, gui.name);
			}
			else if(!haveCashier)
			{
				haveCashier = true;
				return new Role(Role.roles.WorkerLYNCashier, gui.name);
			}
			else if(numWaiters < maxWaiters)
			{
				numWaiters++;
				return new Role(Role.roles.WorkerLYNWaiter, gui.name);
			}
			else
			{
				return null;
			}
		}
	}

	/**
	 * Adds a customer or waiter to the appropriate list
	 *
	 * @param type indicates whether the person is a customer or waiter (later)
	 * @param name name of person
	 */
	public void breakwaiter(){
		temp.msgWannaBreak();
	}

	public void addPerson(Person p) {

		if(isOpen == false){
			AlertLog.getInstance().logMessage(AlertTag.PERSON, p.getName(),"Cannot add Customer");
			p.msgDone();
		} else {

			CustomerAgent c = new CustomerAgent(p, p.getName());	
			CustomerGui g = new CustomerGui(c, gui);

			gui.animationPanel.addGui(g);// dw
			c.setHost(host);
			//c.setWaiter(waiter);

			c.setGui(g);
			customers.add(c);
			c.setCashier(cashier);
			c.startThread(); 
			g.xPos = 170+j*30;
			g.xDestination = 170 + j*30;
			g.setYpos(170 + j*30);
			j++;


			c.getGui().setHungry();
		}




	}

	public void addWorker(Person p) {

		synchronized(workers){
			for (Role r : p.roles) {
				Role role = null;
				if(r.getRole() == roles.WorkerLYNWaiter) {
					role = null;
					int workernumber = 0;
					//WaiterProducer c = new WaiterProducer(p, p.getName(), pm);
					WaiterAgent c = new WaiterAgent(p, p.getName());	
					WaiterGui g = new WaiterGui(c, gui);

					gui.animationPanel.addGui(g);// dw
					c.setHost(host);
					c.setCook(cook);
					c.setCashier(cashier);
					c.setGui(g);
					c.setMenu(menu);
					waiters.add(c);
					host.addwaiter(c);
					g.setPresent(true);
					g.xPos = 10+i*25;
					g.xDestination = 10 + i*25;
					g.setXPos(10 + i*25);
					c.isWorking = true;
					i++;
					c.startThread(); 
					c.setTimeIn(internalClock);
					workers.add(c);
					//c.getGui().setEnabled();
					AlertLog.getInstance().logMessage(AlertTag.LYN, "LYN","Waiter");
					this.worknumber++;


				}  else if(r.getRole() == roles.WorkerLYNCook) {
					role = null;
					role = r;
					cook.p = p;
					workers.add(cook);
					cook.setTimeIn(internalClock);
					cook.isWorking = true;
					AlertLog.getInstance().logMessage(AlertTag.LYN, "LYN","Cook");
					this.worknumber++;

				} else if(r.getRole() == roles.WorkerLYNHost) {
					role = null;
					role = r;
					host.p = p;
					host.name = p.getName();
					workers.add(host);
					//host.setTimeIn(internalClock);
					host.isWorking = true;
					AlertLog.getInstance().logMessage(AlertTag.LYN, "LYN","Host");
					this.worknumber++;
					
				} else if(r.getRole() == roles.WorkerLYNCashier) {
					role = null;
					role = r;
					cashier.p = p;
					cashier.name = p.getName();
					workers.add(cashier);
					cashier.setTimeIn(internalClock);
					cashier.isWorking = true;
					AlertLog.getInstance().logMessage(AlertTag.LYN, "LYN","Cashier");
					this.worknumber++;

				}

			}
		}
		if(this.worknumber>=4){
			OpenRestaurant();
		}
	}

	public void OpenRestaurant() {
		synchronized(workers){
			{
			
				AlertLog.getInstance().logMessage(AlertTag.LYN, "LYN","Opening!!!");
				isOpen = true;
				host.setTimeIn(internalClock);
			
			}
		}
	}


	synchronized public void actionPerformed(ActionEvent arg0) {

		if(isOpen == true){
			if(arg0.getActionCommand().equals("InternalTick")) {
				internalClock+= 2;
				if(internalClock - host.getTimeIn() > 30){
					host.goHome();
				}
			}
		}
	}

	synchronized public void closeRestaurant() {

		AlertLog.getInstance().logMessage(AlertTag.LYN, "LYN","Closed");
		isOpen = false;
		this.worknumber = 0;
		workers.clear();

		waiters.clear();

	}

	public void quitCook()
	{
		haveCook = false;
		GlobalMap.getGlobalMap().addJob(this.gui);
		AlertLog.getInstance().logMessage(AlertTag.LYN, "LYN","no Cook");
	}

	public void quitWaiter()
	{
		numWaiters--;
		GlobalMap.getGlobalMap().addJob(this.gui);
	}

	public void quitCashier()
	{
		haveCashier = false;
		GlobalMap.getGlobalMap().addJob(this.gui);
	}

	public void quitHost()
	{
		haveHost = false;
		GlobalMap.getGlobalMap().addJob(this.gui);
	}
}
