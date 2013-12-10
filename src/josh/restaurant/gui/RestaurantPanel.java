package josh.restaurant.gui;

import javax.swing.*;

import josh.restaurant.*;
import Market.MarketCustomerAgent;
import Market.MarketCustomerGui;
import Market.MarketEmployeeAgent;
import Market.MarketEmployeeGui;
import Market.MarketManagerAgent;
import agents.Person;
import agents.Role;
import josh.restaurant.interfaces.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import agents.ProducerConsumerMonitor;


/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

	private ProducerConsumerMonitor<CookAgent.MyOrder> monitor = 
			new ProducerConsumerMonitor<CookAgent.MyOrder>(30);
	
    //Host, cook, waiters and customers
	
    private HostAgent host;  // = new HostAgent("Sarah"); lets make host in constructor
    //private HostGui hostGui = new HostGui(host); dont need anymore, 
    public CookAgent cook; 
    private CookGui cookGui;
    //private MarketAgent market; 
    public CashierAgent cashier; 
    private CashierGui cashierGui;
   
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<Waiter> waiters = new Vector<Waiter>();
    private Vector<MarketAgent> markets = new Vector<MarketAgent>();
 
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters"); 
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui
    boolean pauseCustomers;
    boolean pauseWaiters;

    public RestaurantPanel(RestaurantGui gui) {
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

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
    	
    	//initialize the one cashier,
    	addPerson("Cashier", "Banksy", false);
    	
    	//initialize with one market, must be init before cook
    	addPerson("Market", "Ralphs", false); 
    	
    	//initialize the one cook ...
    	addPerson("Cook", "Louie", false); 
    
    	//add another market
    	addPerson("Market", "Vons", true); 
    	
        JLabel label = new JLabel();
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + 
                "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }

    public void pauseResume(String type) {
    	
    	System.out.println("PAUSE RESUME CALLED");
    	
    	if (type == "Waiters") {
    		if (pauseCustomers == false) {
    			for (Waiter w : waiters) {
    				w.pause();
    			}
    			pauseCustomers = true; 
    		}
    		else {
    			for (Waiter w : waiters) {
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
        		Waiter temp = waiters.get(i); 
        		if (temp.getName() == name)
        			gui.updateInfoPanel(temp);
        	}
        }
    }

    public void AddCustomer(Person p) {
		
		if (host == null) {
			System.out.println("Cannot add a person without having a manager");
			return;
		}
		
		CustomerAgent c = new CustomerAgent(p.getName(), p);
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
		
		c.getGui().setHungry();
	}
	
	
	public void AddWorker(Person p) {
		/*
		
		for (Role r : p.roles) {
			if (r.getRole() == Role.roles.) {
				if (host.person == p) {
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
		
		*/
		
		WaiterAgent w = new WaiterAgent(p.getName());	
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
    
    
    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, Boolean modifier) {
    	
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
    		
    		//WaiterAgent w = new WaiterAgent(name);
    		WaiterProducer w = new WaiterProducer(name, monitor);
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
    		
    		cook = new CookAgent(name, monitor);
    		monitor.setSubscriber(cook);
    		cookGui = new CookGui(cook, gui); 
    		
    		gui.animationPanel.addGui(cookGui);
    		
    		cook.setMarket(markets.get(0));
    		
    		//when we make the cook, tell the cashier bout him.
    		cashier.setCook(cook);
    		cook.setCashier(cashier);
    		
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
    }
    
    public void clearCookInventroy() {
    	cook.clearInventory();
    }

}
