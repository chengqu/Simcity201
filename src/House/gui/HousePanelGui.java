package House.gui;

import javax.swing.*;

import agents.Person;
import agents.Role;
import animation.BaseAnimationPanel;
import Buildings.Building;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class HousePanelGui extends Building implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("House");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    public HousePersonPanel housePanel = new HousePersonPanel(this);
    
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
    public HousePanelGui() {
        int WINDOWX = 600;
        int WINDOWY = 510;

        Dimension animationDim = new Dimension(WINDOWX, WINDOWY);
    	animationPanel.setPreferredSize(animationDim);
    	animationPanel.setMinimumSize(animationDim);
    	animationPanel.setMaximumSize(animationDim);
    	animationPanel.setPanel(housePanel);
    }
    
    public void actionPerformed(ActionEvent e) {
       
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
  
    
    
    public BaseAnimationPanel getAnimationPanel() {
		return this.animationPanel;
	}

	@Override
	public Role wantJob(Person p) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
