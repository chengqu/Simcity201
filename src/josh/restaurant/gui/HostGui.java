package josh.restaurant.gui;


import java.awt.*;

import josh.restaurant.CustomerAgent;
import josh.restaurant.HostAgent;
import josh.restaurant.WaiterAgent;


//I kept this agent to refer to so I could more effectively make the waiter gui
public class HostGui implements Gui {

    private HostAgent agent = null;

    private int xPos = -20, yPos = -20; //default waiter position
    private int xDestination = -20, yDestination = -20; //default start position

    public static final int xTable = 100;
    public static final int yTable = 100;
    
    public static int hostSize = 20; 
    public static int walkSpeed = 20; 

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
        		
        		& (xDestination == xTable +  walkSpeed) & (yDestination == yTable - walkSpeed)) {
           agent.msgAtTable(); //to host agent
        }
        
        if (xPos == xDestination && yPos == yDestination) {
        	if (xDestination == -walkSpeed && yDestination == -walkSpeed){
        		agent.msgReadyToWork(); 
        	}	
        }
        	
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, hostSize, hostSize);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(CustomerAgent customer, int tablenum) {
    	
    	 

    	xDestination = xTable + walkSpeed;
    	//xDestination = xTable + walkSpeed; // + (80*tablenum);
        yDestination = yTable - walkSpeed;
    
    
    
    }

    public void DoLeaveCustomer() {
        xDestination = -walkSpeed; //-20
        yDestination = -walkSpeed; //-20 
    }
    

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
