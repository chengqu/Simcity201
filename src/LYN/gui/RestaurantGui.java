package LYN.gui;

import Buildings.Building;
import LYN.CustomerAgent;
import LYN.WaiterAgent;

import javax.swing.*;

import animation.BaseAnimationPanel;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends Building implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    public RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel, infoPanel1;
    private JLabel infoLabel, infoLabel1; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private ListPanel listpanel = new ListPanel(restPanel, "");
    private Image my;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 450;
        int WINDOWY = 350;

       /* 
        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(550, 50 , 550, 450);
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel); */
    	
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
        
        currentPerson = person;

        if (person instanceof CustomerAgent) {
        	stateCB.setVisible(true);
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        
        if (person instanceof WaiterAgent) {
            WaiterAgent customer = (WaiterAgent) person;
            
          //Should checkmark be there? 
            
          //Is customer hungry? Hack. Should ask customerGui
            
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>   Waiter  Name: " + customer.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB ) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
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
    
    public BaseAnimationPanel getAnimationPanel() {
		return this.animationPanel;
	}
    /**
     * Main routine to get gui started
     */
    public String toString()
	{
		return name;
	}
}
