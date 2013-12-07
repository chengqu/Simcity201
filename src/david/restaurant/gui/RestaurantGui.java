package david.restaurant.gui;

import david.restaurant.CustomerAgent;
import david.restaurant.gui.RestaurantPanel.myCustomer;
import david.restaurant.WaiterAgent;
import david.restaurant.CookAgent;

import javax.swing.*;

import animation.BaseAnimationPanel;
import Buildings.Building;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends Building implements ActionListener{
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	public david.restaurant.gui.AnimationPanel animationPanel = new david.restaurant.gui.AnimationPanel();

    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    public RestaurantPanel restPanel = new RestaurantPanel(this);

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	animationPanel.setPreferredSize(animationPanel.getSize());
    	animationPanel.setMaximumSize(animationPanel.getSize());
    	animationPanel.setMinimumSize(animationPanel.getSize());
    }
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource().getClass() == JCheckBox.class && ((JCheckBox)e.getSource()).isSelected() == true)
    	{
    		JCheckBox hungryCheck = (JCheckBox)e.getSource();
    		CustomerAgent c = null;
    		if(hungryCheck.getName().length() > 9 && hungryCheck.getName().substring(0, 9).equalsIgnoreCase("customers"))
    		{
	    		for(myCustomer temp: restPanel.customers)
	    		{
	    			if(hungryCheck.getName() == temp.c.getName())
	    			{
	    				c = temp.c;
	    				break;
	    			}
	    		}
	    		if(c != null)
	    		{
	    			c.BecomesHungry();
	    			hungryCheck.setEnabled(false);
	    		}
    		}
    	}
    }
    public void setCustomerEnabled(CustomerAgent c) {
    	for(myCustomer temp: restPanel.customers)
    	{
    		if(temp.c == c)
    		{
    			temp.hungryCheck.setEnabled(true);
    			temp.hungryCheck.setSelected(false);
    		}
    	}
    }
	public BaseAnimationPanel getAnimationPanel() {
		return animationPanel;
	}
	public String toString()
	{
		return name;
	}
}