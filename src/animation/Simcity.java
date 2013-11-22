package animation;



import javax.swing.*;

import simcity201.gui.BusGui;
import simcity201.gui.CarGui;
import simcity201.gui.PassengerGui;
import agents.BusAgent;
import agents.CarAgent;
import agents.PassengerAgent;
import agents.StopAgent;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class Simcity extends JPanel {
	
	

    private JPanel restLabel = new JPanel();
    /*
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private WaiterPanel waiterpanel = new WaiterPanel(this, "Waiters");
    private JPanel group = new JPanel();
    */

    private SimcityGui gui; //reference to main gui
    private  Date date =  Calendar.getInstance().getTime();
    public boolean sleep;
    public boolean start = true;
 
    Timer timer = new Timer();

   

    public Simcity(SimcityGui gui) {
        this.gui = gui;
			
        add(restLabel);
        
    }
    
    public boolean timetosleep(){
    	
    	return ((Math.abs(Calendar.getInstance().getTime().getMinutes()-date.getMinutes())%1 == 0) && (Calendar.getInstance().getTime().getMinutes()!=date.getMinutes())&& (Calendar.getInstance().getTime().getSeconds()==date.getSeconds() ));
    }
    
    public void setNewTime() {
    	date = Calendar.getInstance().getTime();
    }
    public boolean timetowakeup(){
    	sleep = true;
    	
    		
			timer.schedule(new TimerTask() {
				
				public void run() {
					sleep = false;
					
				}
				
			},
			1000);
			
    	
    	return sleep;
    	//return ((Calendar.getInstance().getTime().getSeconds()-sleepdate.getSeconds())%10 == 0);
    	
    }
    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    
    /*
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


*/
    
    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    /*
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
    
    /*
    public void breakwaiter(){
    	temp.msgWannaBreak();
    }
    public void addPerson(String type, String name, boolean hungry1) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
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
    		if ( hungry1 == true) {
    				
    		 c.getGui().setHungry();
    		 
    		}
    	}    	
    	
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
    */
    

}
