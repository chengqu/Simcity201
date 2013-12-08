package newMarket.gui;

import java.util.List;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import newMarket.MarketAnimationPanel;
import newMarket.MarketCashierAgent;
import newMarket.MarketCustomerAgent;
import newMarket.MarketDealerAgent;
import simcity201.gui.Gui;

public class MarketDealerGui implements Gui {

    private MarketDealerAgent agent = null; 
    public CashierLine line;
    
    public boolean isPresent = true;
   
    private int xPos, yPos;  
    private int xDestination, yDestination;
    public int onScreenHomeX, onScreenHomeY;
    
    public static int dealerSize = 20; 
    public static int allignmentSpace = 50;  
    public static int walkSpeed = 2; 
    public static int offScreen = -20; 
   
    private boolean atDest; 
    private boolean holdStuff; 
    
    public MarketDealerGui(MarketDealerAgent a) {
        agent = a;
        atDest = true;
        //line = new CashierLine(agent); 
        
        // to determine where this employee lines up
        xDestination = 50;
       
        // for now employees might be on top of each other 
        yDestination = 400;
        
        //we start off making the employee at the employee station
        xPos = xDestination;
        yPos = yDestination; 
        
        //this is their desination
        onScreenHomeX = xDestination;
        onScreenHomeY = yDestination;
    }

    public void updatePosition() {
    	
        if (xPos < xDestination)
            xPos+=walkSpeed;
        else if (xPos > xDestination)
            xPos-=walkSpeed;

        if (yPos < yDestination)
            yPos+=walkSpeed;
        else if (yPos > yDestination)
            yPos-=walkSpeed;
        
        if (xPos == xDestination && yPos == yDestination && atDest == false) {
        	
        	if (xDestination == onScreenHomeX && yDestination == onScreenHomeY
        			&& holdStuff == true) { 
        		holdStuff = false;  
        		//agent.gui_msgBackAtHomeBase();
        		atDest = true; 
        	}
        	else {
        		System.out.println("update position for market employee wacky");
        	}
        	
        }
          
    }
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void draw(Graphics2D g) {
    	
    	//change color is the waiter is holding food. 
    	if (holdStuff == false) {
    		g.setColor(Color.MAGENTA);
    	}
    	
    	g.fillRect(xPos, yPos, dealerSize, dealerSize);
    	
    }
     
    public void gui_msgStartWork() {
    	isPresent = true;
    }

    public boolean isPresent() {
        return isPresent;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~ COORDINATE COMMANDS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public void DoGetThisItem(List<String> foodList) {
    	System.out.println("DoGetThisStuffCalled");
    	
    	atDest = false; 
    	
    	
    }
     
    public void DoGoHome() {
    
    	atDest = false; 	
        xDestination = onScreenHomeX; 
        yDestination = onScreenHomeY; 
        
    }
    
    public void DoIdleOffScreen () {
    	
    	xDestination = onScreenHomeX; 
        yDestination = onScreenHomeY; 
    }

	public final int getXHome() {
		return onScreenHomeX;
	}
 
	public final int getYHome() {
		return onScreenHomeY;
	}

	public int howManyCustInLine() {
		return line.howManyInLine();
	}
}
