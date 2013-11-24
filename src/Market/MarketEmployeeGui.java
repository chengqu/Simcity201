package Market;

import java.awt.*;
import simcity201.gui.Gui;

public class MarketEmployeeGui implements Gui {
	
	private MarketAnimationPanel marketAnimationPanel; 
    private MarketEmployeeAgent agent = null; 
    
    private boolean isPresent;
    private boolean isWorking;

    private int xPos = -10, yPos = -10; //waiter position offscreen initially 
    private int xDestination = 10, yDestination = 10;
    
    public static int employeeSize = 20; 
    
    public static int allignmentSpace = 20; 
    public static int spaceBtwnTables = 80; 
    
    public static int walkSpeed = 2; 
    
    public int onScreenHomeX = 0;
    public int onScreenHomeY = 10;
    public static int offScreen = -20; 
   
    private boolean atDest; 
    private boolean holdStuff; 
    
    private boolean onBreak; 

    public MarketEmployeeGui(MarketEmployeeAgent a) {
        agent = a;
        atDest = true;
        
        holdStuff = false; 
        onBreak = false;
      
        yDestination = 10;
        onScreenHomeX = 10 ; 
        xDestination = onScreenHomeX;
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
        
        /*

        //if the position is at the destination and the destination has not already been met 
        if (xPos == xDestination && yPos == yDestination && atDest == false) { 
        	if (xDestination == xTable + ((currentTable - 1) * spaceBtwnTables) + allignmentSpace & 
        			yDestination == yTable - allignmentSpace) {
        		waiteragent_.gui_msgAtTable();
        		atDest = true; 
        	}
        	else if (xDestination == onScreenHomeX && yDestination == onScreenHomeY) { 
        		waiteragent_.gui_msgBackAtHomeBase();
        		atDest = true; 
        	}	
        	else if (xDestination == xCook + allignmentSpace && yDestination == yCook - allignmentSpace) { 
        		System.out.println("in waiter gui, im at the coook!");
        		waiteragent_.gui_msgAtCook();
        		atDest = true;
        	}
        	else if (xDestination == 10 && yDestination >= 0) {
        		//so we can go to exact customer location
        		waiteragent_.gui_msgBackAtHomeBase();
        		atDest = true;
        	}
        	else  {
        		System.out.println("update position wacky");
        		System.out.println("X" + xPos);
        		System.out.println("Y" + yPos);
        	}	
        	
        }
        
        */
    }
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setBreak(boolean b) {
    	
    	if (b==true)
    		onBreak = true; 
    	else 
    		onBreak = false;	
    }

    public void draw(Graphics2D g) {
    	
    	//change color is the waiter is holding food. 
    	if (holdStuff == false) {
    		g.setColor(Color.MAGENTA);
    	}
    	else {
    		g.setColor(Color.RED);
    	}
    	
    	//change color if the waiter is on break.
    	if (onBreak == false ) {
    		//do nothing 
    	}
    	else {
    		g.setColor(Color.BLACK);
    	}
    	
    	g.fillRect(xPos, yPos, employeeSize, employeeSize);
    	
    }
     
    public void gui_msgStartWork() {
    	isWorking = true;
    	isPresent = true;
    }

    public boolean isPresent() {
        return true;
    }
    
    public void holdingStuff () {
		
    	if (holdStuff == false) {
    		holdStuff = true; 
    	}
    	else {
    		holdStuff = false;
    	}
		
	}
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~ COORDINATE COMMANDS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public void DoGoToThisTable(int tableNum) {
    	
    	atDest = false; 
    
    	/*
    	
    	if (tableNum > 0) {
    		xDestination = xTable + ((tableNum - 1) * spaceBtwnTables) + allignmentSpace;
    		yDestination = yTable - allignmentSpace;
    	}
    	else {
    		xDestination = onScreenHomeX;
    		yDestination = onScreenHomeY;
    		System.out.println("waiter given bad table");
    	}
    	
    	*/
    	
    }
     
    public void DoGoHome() {
    
    	atDest = false; 	
        xDestination = onScreenHomeX; //10
        yDestination = onScreenHomeY; //10
        
    }
    
    public void DoIdleOffScreen () {
    	
    	xDestination = onScreenHomeX; //10
        yDestination = onScreenHomeY; //10 
    }

	public void DoGoPickupCust(int yDest) {
		
		atDest = false;
		
		xDestination = 10;
		
		if (yDest < 0) {
			yDestination = 0;
		}	
		else {
			yDestination = yDest;
		}
		
	}
 
}
