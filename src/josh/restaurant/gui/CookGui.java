package josh.restaurant.gui;

import java.awt.*;

import josh.restaurant.CookAgent;
import josh.restaurant.CustomerAgent;
import josh.restaurant.HostAgent;

public class CookGui implements Gui{

	private CookAgent agent = null;
	private boolean isPresent = false;

	RestaurantGui gui;

	private int xPos, yPos;

	public static final int xTable = 100;
	public static final int yTable = 100;
	
	public static int cookSize = 70; 

	/*the cook has an initial position so we don't need to implement and update position code
	 * the cook will be onscreen and will display string "COOK" because I think that is better
	 */
	public CookGui(CookAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = 200;
		yPos = 300;
		this.gui = gui;
		setPresent(true);
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.setFont(new Font("default", Font.BOLD, 24));
		g.drawString("COOK", xPos, yPos);
		g.fillRect(xPos, yPos, cookSize, cookSize);
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
}
