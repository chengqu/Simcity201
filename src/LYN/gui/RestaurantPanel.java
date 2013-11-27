package LYN.gui;

import LYN.CashierAgent;
import LYN.CookAgent;
import LYN.CustomerAgent;
import LYN.HostAgent;
import LYN.Market;
import LYN.WaiterAgent;
import LYN.Menu;

import javax.swing.*;

import agents.Person;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Sarah");
    //private WaiterAgent waiter = new WaiterAgent("waiter");
   
    private Market market1 = new Market("Superior"); 
    private Market market2 = new Market("Ralphs");
    private Market market3 = new Market("FreshAndEasy");
    private Menu menu = new Menu("menu");
    private CashierAgent cashier = new CashierAgent("Cashier");
    private WaiterAgent temp;
    private HostGui hostGui = new HostGui(host);
<<<<<<< HEAD
    private CookAgent cook = new CookAgent("cook", cashier);
=======
    private CookAgent cook = new CookAgent("Rest3", cashier);
>>>>>>> Transportation
    private int i = 0; int j = 0;
    //private WaiterGui waiterGui = new WaiterGui(waiter);

    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private WaiterPanel waiterpanel = new WaiterPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        CookGui cookGui = new CookGui(cook, gui);
        host.setGui(hostGui);
        cook.setGui(cookGui);
        cookGui.setPresent(true);
        gui.animationPanel.addGui(hostGui);
        gui.animationPanel.addGui(cookGui);
    
        host.startThread();
        cook.startThread();
        market1.startThread();
        market2.startThread();
        market3.startThread();
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
        cashier.setcook(cook);
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
                WaiterAgent temp2 = waiters.get(i);
                if (temp2.getName() == name)
                {
                    gui.updateInfoPanel(temp2);
                    temp = temp2;
                }
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
    
    public void addWaiter(String type, String name) {

    	if (type.equals("Waiters")) {
    		WaiterAgent c = new WaiterAgent(name);	
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
    		
    		i++;
    		c.startThread();   
    		//c.getGui().setEnabled();
    		
    		 
    		
    	}    	
    	
    }
    
    

}
