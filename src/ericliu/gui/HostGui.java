package ericliu.gui;


import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.HostAgent;

import java.awt.*;

public class HostGui implements Gui {

    private HostAgent agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    public static final int xTable1 = 200;
    public static final int yTable1 = 250;
    public static final int xTable2 = 200;
    public static final int yTable2 = 350;
    public static final int xTable3 = 300;
    public static final int yTable3 = 250;
    


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
        		& (xDestination == xTable1 + 20) & (yDestination == yTable1 - 20)) {
           agent.msgAtTable();
        }
        
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == xTable2 + 20) & (yDestination == yTable2 - 20)) {
             agent.msgAtTable();
          }
        
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == xTable3 + 20) & (yDestination == yTable3 - 20)) {
             agent.msgAtTable();
          }
        
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == -20) & (yDestination == -20)) {
             agent.msgAtStart();
          }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(CustomerAgent customer, int tableNumber) {
        if(tableNumber==1)
        {
           xDestination = 200 + 20;
           yDestination = 250 - 20;
        }
        
        else if(tableNumber==2)
        {
           xDestination = 200 + 20;
           yDestination = 350 - 20;
        }
        
        else if(tableNumber==3)
        {
           xDestination = 300 + 20;
           yDestination = 250 - 20;
        }
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
