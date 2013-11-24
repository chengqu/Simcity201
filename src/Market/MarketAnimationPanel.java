package Market;

import javax.swing.*;

import animation.BaseAnimationPanel;
import Buildings.Building;

import java.awt.*;
import java.awt.event.*;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MarketAnimationPanel extends BaseAnimationPanel implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JPanel animationFrame = new JPanel (); 
	JFrame animationFrame = new JFrame("Market Animation");
	//AnimationPanel animationPanel = new AnimationPanel()
	
	private int windowHeight = 500;  
	private int windowLength = 500; 
    
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public MarketAnimationPanel() {
    	
    	Dimension d = new Dimension(windowLength, windowHeight);
    	this.setPreferredSize(d);
    	this.setMaximumSize(d);
    	this.setMinimumSize(d);
  
    }
	
	@Override
	public Dimension getSize() {
		// TODO Auto-generated method stuff
		
		return new Dimension(windowLength, windowHeight);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
