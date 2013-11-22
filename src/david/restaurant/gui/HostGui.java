package david.restaurant.gui;


import david.restaurant.CustomerAgent;
import david.restaurant.HostAgent;

import java.awt.*;

import javax.swing.ImageIcon;

public class HostGui implements Gui {

    private HostAgent agent = null;

    public int xPos = -20, yPos = -20;//default waiter position
    public int xDestination = -20, yDestination = -20;//default start position

    public int xTable = 200;
    public int yTable = 250;

    public HostGui(HostAgent agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable + 20) & (yDestination == yTable - 20)) {
           //agent.msgAtTable();
        }
    }

    public void draw(Graphics2D g) {
    	g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    	
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(CustomerAgent customer) {
        xDestination = xTable + 20;
        yDestination = yTable - 20;
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
