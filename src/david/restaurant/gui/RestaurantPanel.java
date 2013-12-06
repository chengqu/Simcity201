package david.restaurant.gui;

import david.restaurant.CookAgent;
import david.restaurant.CustomerAgent;
import david.restaurant.HostAgent;
import david.restaurant.MarketAgent;
import david.restaurant.WaiterAgent;
import david.restaurant.CashierAgent;
import david.restaurant.Interfaces.Market;

import javax.swing.*;

import agents.Person;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel implements ActionListener{
	
    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Sarah");
    private HostGui hostGui = new HostGui(host);
    private CookAgent cook;
    private CashierAgent cashier;
    
    public List<myCustomer> customers = new ArrayList<myCustomer>();
    public List<myWaiterAgent> waiters = new ArrayList<myWaiterAgent>();
    public List<MarketAgent> markets = new ArrayList<MarketAgent>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();
    private JButton pause = new JButton("Pause");
    private JPanel bottomGroup = new JPanel();
    private JPanel topGroup = new JPanel();
    
    Action action;
    Action action2;
    Action action3;
    
    int xLowBound = 50;
    int xHighBound = 200;
    int step = 30;
    
    static int yInitial = 250;
    
    int xCurrent = 50;
    int yCurrent = yInitial;
    
    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
    	
        this.gui = gui;
        gui.animationPanel.addHost(host);
        host.startThread();
        markets.add(new MarketAgent("m1"));
        markets.add(new MarketAgent("m2"));
        markets.add(new MarketAgent("m3"));
        
        List<Market> m = new ArrayList<Market>();
        for(MarketAgent temp: markets)
        {
        	m.add(temp);
        }
        
        cashier = new CashierAgent(m);
        
        for(MarketAgent temp: markets)
        {
        	temp.setCashier(cashier);
        	temp.startThread();
        }
        
        cook = new CookAgent(m, cashier);
        cashier.setCook(cook);
        cook.setName("Rest1");
        CookGui cGui = new CookGui(cook);
        cook.setGui(cGui);
        gui.animationPanel.addGui(cGui);
        
        m = new ArrayList<Market>();
        for(MarketAgent temp: markets)
        {
        	m.add(temp);
        }
        
        for(MarketAgent temp: markets)
        {
        	temp.setCashier(cashier);
        	temp.startThread();
        }
        cashier.startThread();
        cook.startThread();
        
        for(MarketAgent temp: markets)
        {
        	cook.addMarket(temp);
        }

        setLayout(new FlowLayout());
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);

        initRestLabel();
        initPausePanel();
        topGroup.add(restLabel, BorderLayout.WEST);
        topGroup.add(group, BorderLayout.EAST);
        add(topGroup);
        add(bottomGroup);
        
        action = new F1Action();
        action.setEnabled(true);
        
        action2 = new F2Action();
        action2.setEnabled(true);
        
        action3 = new F3Action();
        action3.setEnabled(true);
        
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "F1Pressed");
       	this.getActionMap().put("F1Pressed", action);
       	
       	this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), "F2Pressed");
       	this.getActionMap().put("F2Pressed", action2);
       	
       	this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F3"), "F3Pressed");
       	this.getActionMap().put("F3Pressed", action3);
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
    
    private void initPausePanel()
    {
    	pause.setName("Pause");
        pause.addActionListener(this);
        JPanel temp = new JPanel();
        temp.setLayout(new GridLayout(1, 2, 10, 10));
        temp.add(waiterPanel);
        bottomGroup.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomGroup.add(pause);
        bottomGroup.add(temp);
    }
    
    public void addCustomer(Person p)
    {
    	for(myCustomer c: customers)
    	{
    		if(c.c.person == p)
    		{
    			c.c.BecomesHungry();
    			return;
    		}
    	}
    	myCustomer c = new myCustomer(new CustomerAgent(p, "Customers" + p.getName(), host, this, cashier), null);
		CustomerGui g = new CustomerGui(c.c, gui, host);
		
		gui.animationPanel.addGui(g);
		
		c.c.setHost(host);
		c.c.setGui(g);
		customers.add(c);
		c.c.startThread();
		c.c.BecomesHungry();
    }
    
    public void addPersonToList(String type, String name, JCheckBox checkbox)
    {
    	if (type.equals("Customers")) 
    	{
    		myCustomer c = new myCustomer(new CustomerAgent(null ,type + name, host, this, cashier), checkbox);
    		CustomerGui g = new CustomerGui(c.c, gui, host);
    		
    		checkbox.addActionListener(this);
    		checkbox.addActionListener(gui);
    		checkbox.setName(c.c.getName());
    		
    		gui.animationPanel.addGui(g);
    		
    		c.c.setHost(host);
    		c.c.setGui(g);
    		customers.add(c);
    		c.c.startThread();
    	}
    	else if(type.equals("Waiters"))
    	{
    		WaiterAgent waiter = new WaiterAgent(host, type + name, cashier, this);
    		WaiterGui tempGui = new WaiterGui(waiter, xCurrent, yCurrent, host);
    		waiter.setGui(tempGui);
    		
    		xCurrent += step;
    		if(xCurrent > xHighBound)
    		{
    			xCurrent = xLowBound;
    			yCurrent += step;
    		}
    		
    		gui.animationPanel.addGui(tempGui);
    		
    		checkbox.setEnabled(true);
    		checkbox.setVisible(true);
    		checkbox.setName(waiter.getName());
    		
    		checkbox.addActionListener(this);
    		checkbox.addActionListener(gui);
    		
    		waiter.setCook(cook);
            waiters.add(new myWaiterAgent(waiter, checkbox));
            waiter.startThread();
            
            host.AddWaiter(waiter);
    	}
    }
    
    public void addPerson(String type, String name)
    {
    	if (type.equals("Customers")) 
    	{
    		myCustomer c = new myCustomer(new CustomerAgent(null, type + name, host, this, cashier), null);
    		CustomerGui g = new CustomerGui(c.c, gui, host);
    		
    		gui.animationPanel.addGui(g);
    		
    		c.c.setHost(host);
    		c.c.setGui(g);
    		customers.add(c);
    		c.c.startThread();
    		c.c.BecomesHungry();
    	}
    	else if(type.equals("Waiters"))
    	{
    		WaiterAgent waiter = new WaiterAgent(host, type + name, cashier, null);
    		WaiterGui tempGui = new WaiterGui(waiter, xCurrent, yCurrent, host);
    		waiter.setGui(tempGui);
    		
    		xCurrent += step;
    		if(xCurrent > xHighBound)
    		{
    			xCurrent = xLowBound;
    			yCurrent += step;
    		}
    		
    		gui.animationPanel.addGui(tempGui);
    		
    		waiter.setCook(cook);
            waiters.add(new myWaiterAgent(waiter, null));
            waiter.startThread();
            
            host.AddWaiter(waiter);
    	}
    }
    
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource().getClass() == JCheckBox.class)
    	{
    		JCheckBox hungryCheck = (JCheckBox)e.getSource();
    		if(hungryCheck.getName().length() > 9 )
    		{
	    		if(hungryCheck.getName().substring(0, 9).equals("Customers"))
	    		{
		    		for(myCustomer temp: customers)
		    		{
		    			if(hungryCheck == temp.hungryCheck)
		    			{
		    				if(hungryCheck.isSelected() == true)
		    				{
		    					temp.hungryCheck = hungryCheck;
		    				}
		    				else
		    				{
		    					customers.remove(temp);
		    					customers.add(temp);
		    				}
		    				return;
		    			}
		    		}
	    		}
    		}
    		if(hungryCheck.getName().substring(0, 7).equalsIgnoreCase("Waiters"))
    		{
	    		for(myWaiterAgent temp: waiters)
	    		{
	    			if(hungryCheck == temp.checkBox)
	    			{
	    				System.out.println("PressBreak");
	    				if(hungryCheck.isSelected() == true)
	    				{
	    					temp.w.msgGoOnBreak();
	    				}
	    				else
	    				{
	    					temp.w.msgStopBreak();
	    				}
	    				return;
	    			}
	    		}
    		}
    	}
    	if(e.getSource().getClass() == JButton.class)
    	{
    		JButton temp = (JButton)e.getSource();
    		if(temp.getName() == "Pause")
    		{
    			temp.setName("Restart");
    			temp.setText("Restart");
    			host.pause();
    			cook.pause();
    			for(myCustomer c: customers)
    			{
    				c.c.pause();
    			}
    			for(myWaiterAgent a: waiters)
    			{
    				a.w.pause();
    			}
    			gui.animationPanel.pause();
    		}
    		else if(temp.getName() == "Restart")
    		{
    			temp.setName("Pause");
    			temp.setText("Pause");
    			host.resume();
    			cook.resume();
    			for(myCustomer c: customers)
    			{
    				c.c.resume();
    			}
    			for(myWaiterAgent a: waiters)
    			{
    				a.w.resume();
    			}
    			gui.animationPanel.unpause();
    		}
    	}
    }
    
    public void customerDone(CustomerAgent c)
    {
    	myCustomer mc = null;
    	for(myCustomer temp: customers)
    	{
    		if(temp.c == c)
    		{
    			mc = temp;
    			break;
    		}
    	}
    	if(mc != null && mc.hungryCheck != null)
    	{
			mc.hungryCheck.setEnabled(true);
			mc.hungryCheck.setSelected(false);
    	}
    }
    
    public void noBreak(WaiterAgent w)
    {
    	myWaiterAgent mw = null;
    	for(myWaiterAgent temp: waiters)
    	{
    		if(temp.w == w)
    		{
    			mw = temp;
    			break;
    		}
    	}
    	if(mw != null)
    	{
    		mw.checkBox.setSelected(false);
    	}
    }
    
    public void CookArrived(Person p)
    {
    	//calculate pay later
    	cook.setPerson(p);
    	cook.doThings();
    }
    
    public class myCustomer
    {
    	public CustomerAgent c;
    	public JCheckBox hungryCheck;
    	
    	myCustomer(CustomerAgent agent, JCheckBox check)
    	{
    		c = agent;
    		hungryCheck = check;
    	}
    }

    public class myWaiterAgent
    {
    	public WaiterAgent w;
    	public JCheckBox checkBox;
    	public myWaiterAgent(WaiterAgent agent, JCheckBox check)
    	{
    		w = agent;
    		checkBox = check;
    	}
    }
    
    private class F1Action extends AbstractAction
    {
		public void actionPerformed(ActionEvent arg0) {
			cook.drain();
		}
    }
    
    private class F2Action extends AbstractAction
    {
		public void actionPerformed(ActionEvent arg0) 
		{
			for(MarketAgent m: markets)
			{
				m.drain();
			}
		}
    }
    
    private class F3Action extends AbstractAction
    {
    	public void actionPerformed(ActionEvent arg0) 
		{
			cook.print_();
		}
    }
}