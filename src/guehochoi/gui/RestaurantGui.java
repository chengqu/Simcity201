package guehochoi.gui;

import guehochoi.restaurant.CustomerAgent;
import guehochoi.restaurant.WaiterAgent;

import javax.swing.*;

import Buildings.Building;
import agent.Agent;
import animation.BaseAnimationPanel;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends Building implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui) to be removed
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	public AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    public RestaurantPanel restPanel = new RestaurantPanel(this);
    private JPanel optionPanel = new JPanel();
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	
        int WINDOWX = 600;
        int WINDOWY = 550;
        
    	Dimension animationDim = new Dimension(WINDOWX, WINDOWY);
    	animationPanel.setPreferredSize(animationDim);
    	animationPanel.setMinimumSize(animationDim);
    	animationPanel.setMaximumSize(animationDim);
    	
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
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }else if (person instanceof WaiterAgent) {
        	/*This is added for lab 6*/
        	WaiterAgent waiter = (WaiterAgent) person;
        	if (waiter.getGui().isWaiterOnBreak()) {
        		stateCB.setText("BackToWork");
            	stateCB.setSelected(!waiter.getGui().isWaiterOnBreak());
            	stateCB.setEnabled(waiter.getGui().isWaiterOnBreak());
        	}else {
	        	stateCB.setText("WantABreak?");
	        	stateCB.setSelected(waiter.getGui().isWaiterOnBreak());
	        	stateCB.setEnabled(!waiter.getGui().isWaiterOnBreak());
        	}
        	infoLabel.setText(
        			"<html><pre>	Name: " + waiter.getName() + "</pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }else if ( currentPerson instanceof WaiterAgent) {
            	WaiterAgent w = (WaiterAgent) currentPerson;
            	if (stateCB.getText().equals("BackToWork")) {
            		w.getGui().setOffBreak();
            		stateCB.setEnabled(false);
            	}else {
	            	w.getGui().setOnBreak();
	            	stateCB.setEnabled(false);
            	}
            }
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
    public void setWaiterBreakEnabled(WaiterAgent w) {
    	if (currentPerson instanceof WaiterAgent) {
    		WaiterAgent waiter = (WaiterAgent) currentPerson;
    		if (w.equals(waiter)) {
    			stateCB.setEnabled(true);
    			stateCB.setSelected(false);
    			w.waiterGui.setOffBreak();
    		}
    	}
    }
    public void waiterWentOnBreak(WaiterAgent w) {
    	if (currentPerson instanceof WaiterAgent) {
    		WaiterAgent waiter = (WaiterAgent) currentPerson;
    		
    	}
    }
    
    /**
     * Main routine to get gui started
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        //gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    */
	@Override
	public BaseAnimationPanel getAnimationPanel() {
		return this.animationPanel;
	}
}
