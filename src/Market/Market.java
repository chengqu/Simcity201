package Market;

import javax.swing.*;

import Buildings.Building;
import agents.Person;
import agents.Role;
import animation.BaseAnimationPanel;

import java.awt.Dimension;
import java.awt.event.*;
import java.util.*;


public class Market extends Building {
	
	MarketAnimationPanel marketAnimationPanel;
	List<Person> people = new ArrayList<Person>();
	
	List<MarketCustomerAgent> customers = new ArrayList<MarketCustomerAgent>();
	List<MarketEmployeeAgent> employees = new ArrayList<MarketEmployeeAgent>(); 
	
	MarketManagerAgent manager = null;
	
	public Market() {
		
		marketAnimationPanel = new MarketAnimationPanel();
	
		Dimension d = marketAnimationPanel.getSize();
    	marketAnimationPanel.setPreferredSize(d);
    	marketAnimationPanel.setMaximumSize(d);
    	marketAnimationPanel.setMinimumSize(d);
        
    	//HACK, eventually we want to add our manager 
        //manager = new MarketManagerAgent();
    	
    	Person p = new Person("Manager Man");
		manager = new MarketManagerAgent(p.getName(), p);
		manager.startThread();
    	
    	initPeople();
    	initPeople();
    	initPeople(); 
    }
	
	private void initPeople() {
	
		Person p1 = new Person("Worker Man"); 
		MarketEmployeeAgent m = new MarketEmployeeAgent(p1.getName(), p1);
		employees.add(m);
		
		MarketEmployeeGui g = new MarketEmployeeGui(m);
		m.setGui(g);
		marketAnimationPanel.addGui(g);
		
		m.startThread();
		
		manager.msgIAmHereToWork(p1, m);
		m.setManager(manager);
		
		Person p2 = new Person("second cust"); 
		addCustomer(p2);
	}
	
	public void addCustomer(Person p) {
		
		if (manager == null) {
			System.out.println("Cannot add a person without having a manager");
			return;
		}
		
		MarketCustomerAgent a = new MarketCustomerAgent(p.getName(),p);
		customers.add(a);
		
		//add customer gui
		MarketCustomerGui g = new MarketCustomerGui(a); 
		g.setAnimationPanel(marketAnimationPanel);
		//get customer gui stuffs
		a.setGui(g);
		marketAnimationPanel.addGui(g);

		a.setManager(manager);
		
		customers.add(a);
		
		a.startThread();
		
		a.doThings();
	}
	
	
	public void AddWorker(Person p) {
		
		for (Role r : p.roles) {
			if (r.getRole() == Role.roles.marketManager) {
				if (manager.person == p) {
					//don't do anything
				}
				else {
					manager = new MarketManagerAgent(p.getName(), p);
					
					//add manager gui and
					//set appropriastee gui stuff
				
					people.add(p);
					
					p.startThread();
				}
				return;
			} //if the person is a manager 
		} //for loop
		
		MarketEmployeeAgent a = new MarketEmployeeAgent(p.getName(), p); 
		
		//add employee gui and
		MarketEmployeeGui g = new MarketEmployeeGui(a);
		//set appropriate gui stuff
		
		people.add(p);
		
		employees.add(a);
		
		a.startThread();
		//p.startThread();
	}
	
	
	/*
	
    //Host, cook, waiters and customers
	
    private HostAgent host;  // = new HostAgent("Sarah"); lets make host in constructor
    //private HostGui hostGui = new HostGui(host); dont need anymore, 
    private CookAgent cook; 
    private CookGui cookGui;
    //private MarketAgent market; 
    private CashierAgent cashier; 
    private CashierGui cashierGui;
   

    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    private Vector<MarketAgent> markets = new Vector<MarketAgent>();
 
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters"); 
    private JPanel group = new JPanel();

    private MarketGui gui; //reference to main gui
    boolean pauseCustomers;
    boolean pauseWaiters;

    public Market(MarketGui gui) {
        this.gui = gui;
        
    	pauseCustomers = false;
    	pauseWaiters = false; 
        
        host = new HostAgent("Bob");
        host.setGui(this);
        host.setGui(gui);
        host.startThread();

        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);
        group.add(waiterPanel); 

        initRestLabel();
        
        add(restLabel);
        add(group);
        
        
    }
    */

    public void pauseResume(String type) {
    	
    	/*
    	
    	System.out.println("PAUSE RESUME CALLED");
    	
    	if (type == "Waiters") {
    		if (pauseCustomers == false) {
    			for (WaiterAgent w : waiters) {
    				w.pause();
    			}
    			pauseCustomers = true; 
    		}
    		else {
    			for (WaiterAgent w : waiters) {
    				w.resume(); 
    			}
    			pauseCustomers = false;
    		}	
    	}
    	if (type == "Customers") {
    		if (pauseWaiters == false) {
    			for (CustomerAgent c : customers) {
    				c.pause();
    			}
    			pauseWaiters = true;
    			}
    		else {
    			for (CustomerAgent c : customers) {
    				c.resume();
    			}
    			pauseWaiters = false; 
    		}
    	}
    	
    	*/
    }
     
    public void showInfo(String type, String name) {

    	/*
    	
        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if (type.equals("Waiters")) {
        	for (int i = 0; i < waiters.size(); i++) {
        		WaiterAgent temp = waiters.get(i); 
        		if (temp.getName() == name)
        			gui.updateInfoPanel(temp);
        	}
        }
        
        */
    }

   
    public void addPerson(String type, String name, Boolean modifier) {
    	
    	/*
    	
    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);
    		OrderGui o = new OrderGui(gui); 

    		gui.animationPanel.addGui(g);
    		gui.animationPanel.addGui(o);
    		c.setHost(host);  // host doesnt serve customer anymore , have to hack customer so they know who to go to
    		c.setCashier(cashier);
    		c.setGui(g);
    		c.setOrderGui(o);
    	
    		
    		customers.add(c);
    		c.startThread();
    		
    		if (modifier) {
    			c.getGui().setHungry();  // SET Hungry is original message!!! sets off events that put customer on waiting customer list
    		}
    	}	
    	else if (type.equals("Waiters")) {
    		
    		WaiterAgent w = new WaiterAgent(name);	
    		WaiterGui g = new WaiterGui(w, gui);

    		gui.animationPanel.addGui(g);
    		w.setCook(cook);
    		w.setHost(host);
    		w.setCashier(cashier);
    		w.setGui(g);
    		
    		waiters.add(w);
    		w.startThread();
    		
    		//so that the host may keep a list of all the waiters the panel creates
    		host.addMyWaiter(w);	
    	}	
    	else if (type.equals("Cook")) {
    		
    		cook = new CookAgent(name);
    		cookGui = new CookGui(cook, gui); 
    		
    		gui.animationPanel.addGui(cookGui);
    		
    		cook.setMarket(markets.get(0));
    		cook.startThread();
    		
    		for (int i=1; i <= 5; i++)  {
    			CookOrderGui o = new CookOrderGui(gui);
    			//OrderIcon oi = new OrderIcon();
    			
    			cook.setCookOrderGui(o,i); 
    			gui.animationPanel.addGui(o);
    			
    		}
    			
    	}
    	else if (type.equals("Market")) {
    		
            MarketAgent m = new MarketAgent(name); 
            m.setCashier(cashier);
            markets.add(m); 
            m.startThread();  
            
            if (modifier) {
            	cook.addMarket(m);
            }
    	}
    	else if (type.equals("Cashier")) {
    		
    		cashier = new CashierAgent(name);
    		cashierGui = new CashierGui(cashier, gui); 
    		
    		gui.animationPanel.addGui(cashierGui);
    		
    		cashier.startThread(); 
    	}
    	else 
    		System.out.println("something wrong in RestrauntPanel adding a person");
    		
    	*/
    }

	@Override
	public BaseAnimationPanel getAnimationPanel() {
		// TODO Auto-generated method stub
		return marketAnimationPanel;
	}

    
}
