package josh.restaurant.gui;

import javax.swing.*;

import animation.BaseAnimationPanel;
import Buildings.Building;

import java.awt.*;
import java.awt.event.*;

import josh.restaurant.CustomerAgent;
import josh.restaurant.WaiterAgent;
import josh.restaurant.interfaces.Waiter;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends Building implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JPanel animationFrame = new JPanel (); 
	JFrame animationFrame = new JFrame("Restaurant Animation");
	
	AnimationPanel animationPanel = new AnimationPanel();
	  
    public RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JButton clearMarket;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	
        int WINDOWX = 500;
        int WINDOWY = 380;
        animationPanel.setRestPanel(restPanel);
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Customer Name: " + customer.getName() + " </pre></html>");
        }
        if (person instanceof WaiterAgent) {
        	WaiterAgent waiter = (WaiterAgent) person; 
        	//make sure its selected / enabled based on break request or not 
        	if (waiter.isWorking()) {
        		stateCB.setText("Request Break");
        		stateCB.setEnabled(true);
        		stateCB.setSelected(false);
        	}
        	else if (waiter.isOnBreak()) {
        		stateCB.setText("Go Back to Work"); 
        		stateCB.setEnabled(true);
        		stateCB.setSelected(false);
        	}
        	else if (waiter.isWaitingForBreak()) {
        		stateCB.setText("Break Requested");
        		stateCB.setEnabled(false);
        		stateCB.setSelected(true);
        	}
        	else  {
        		System.out.println("problem in update info panel");
        	}
        	infoLabel.setText(
                    "<html><pre>     Waiter Name: " + waiter.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v2, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            if (currentPerson instanceof WaiterAgent) {
            	WaiterAgent w = (WaiterAgent) currentPerson; 
            	if (w.isOnBreak()) {
            		w.gui_msgBackToWork(); 
            		stateCB.setSelected(false); 
            	} 
            	else {
            		w.gui_msgWantToGoOnBreak();
            		stateCB.setEnabled(false);
            		stateCB.setText("Break Requested");
            		//stateCB.setSelected(false);
            	}
            }
        }
        else if (e.getSource() == clearMarket) {
        	System.out.println("clearing cook inventory");
        	restPanel.clearCookInventroy();
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    public void setWaiterOnBreak(Waiter w) {
    	if (currentPerson instanceof WaiterAgent) {
    		Waiter wait = (Waiter) currentPerson; 
    		if (w.equals(wait)) {
    			stateCB.setEnabled(true); 
    			stateCB.setSelected(false);
    			stateCB.setText("Go Back to Work");
    		}
    	}
    }
    
    public void setWaiterBackToWork(Waiter w) {
    	if (currentPerson instanceof WaiterAgent) {
    		Waiter wait = (Waiter) currentPerson; 
    		if (w.equals(wait)) {
    			stateCB.setEnabled(true); 
    			stateCB.setSelected(false);
    			stateCB.setText("Request Break");
    		}
    	}
    }
    
    /**
     * Main routine to get gui started
     */
    /*public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    */
	@Override
	public BaseAnimationPanel getAnimationPanel() {
		return animationPanel;
	}
}
