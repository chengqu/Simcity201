package animation;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class SimcityGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Simcity Animation");
	
	
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private Simcity restPanel;
    public SimcityPanel animationPanel;
    
    //private ListPanel listpanel = new ListPanel(restPanel, "");
    private Image my;


    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public SimcityGui() {
        int WINDOWX = 1200;
        int WINDOWY = 850;
        
        restPanel = new Simcity(this);
        animationPanel = new SimcityPanel(restPanel);
       
    	animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(50, 0 , WINDOWX, WINDOWY);
        Dimension PANEL_DIM = new Dimension(WINDOWX, WINDOWY);
    	animationFrame.setPreferredSize(PANEL_DIM);
    	animationFrame.setMaximumSize(PANEL_DIM);
    	animationFrame.setMinimumSize(PANEL_DIM);
        animationFrame.setVisible(true);
        animationFrame.setResizable(false);
        animationFrame.add(animationPanel);
        
        
    }

    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        
    }
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        SimcityGui gui = new SimcityGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
