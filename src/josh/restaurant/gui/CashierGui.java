package josh.restaurant.gui;

import java.awt.*;

import josh.restaurant.CashierAgent;
import josh.restaurant.CustomerAgent;
import josh.restaurant.HostAgent;

public class CashierGui implements Gui{

	private CashierAgent agent = null;
	private boolean isPresent = false;

	RestaurantGui gui;

	private int xPos, yPos;
	
	public static int cookSize = 30; 

	/*the cook has an initial position so we don't need to implement and update position code
	 * the cook will be onscreen and will display string "COOK" because I think that is better
	 */
	public CashierGui(CashierAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = 10;
		yPos = 280;
		this.gui = gui;
		setPresent(true);
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("default", Font.BOLD, 27));
		g.drawString("$$", xPos, yPos);
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
