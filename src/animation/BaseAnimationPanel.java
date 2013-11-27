package animation;

import java.awt.Dimension;

import javax.swing.*;

/**
 * Everyone's AnimationPanel must extends this instead of JPanel
 * Must implement getSize that returns Dimension
 * @author GChoi
 *
 */
public abstract class BaseAnimationPanel extends JPanel {
	
	
	/**
	 * Override this function
	 */
	public abstract Dimension getSize();
	
}
