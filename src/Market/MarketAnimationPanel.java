package Market;

import javax.swing.*;

import simcity201.gui.Gui;
import animation.BaseAnimationPanel;
import Buildings.Building;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MarketAnimationPanel extends BaseAnimationPanel implements ActionListener, MouseListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JPanel animationFrame = new JPanel (); 
	JFrame animationFrame = new JFrame("Market Animation");
	//AnimationPanel animationPanel = new AnimationPanel()
	
	List<Gui> guis = new ArrayList<Gui>();
	
	Object lock = new Object(); 
	
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
		
		synchronized(lock) {
			for (Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
		
		repaint(); 
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		synchronized(lock) {
			for (Gui gui : guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
				}
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
