package josh.restaurant.gui;


import java.awt.*;

import josh.restaurant.CustomerAgent;
import josh.restaurant.HostAgent;
import josh.restaurant.interfaces.Waiter;

public class WaiterGui implements Gui {
	
	private RestaurantGui gui_; 
    private Waiter waiteragent_ = null; 
    
    //public boolean inMotion; 
    private boolean isPresent;
    private boolean isWorking;
    
    public static int waiterID = 0;

    private int xPos = -10, yPos = -10; //waiter position offscreen initially 
    private int xDestination = 10, yDestination = 10;

    public static final int xTable = 100;
    public static final int yTable = 100;
    
    public static final int xCook = 200;
    public static final int yCook = 300;
    
    public static int waiterSize = 20; 
    public static int allignmentSpace = 20; 
    public static int spaceBtwnTables = 80; 
    public static int walkSpeed = 2; 
    
    public int onScreenHomeX = 0;
    public int onScreenHomeY = 10;
    public static int offScreen = -20; 
    
    private int currentTable =  0; 
    private boolean atDest; 
    private boolean holdFood; 
    private boolean onBreak; 

    public WaiterGui(Waiter agent, RestaurantGui w) {
        this.waiteragent_ = agent;
        this.gui_ = w;
        atDest = true;
        holdFood = false; 
        onBreak = false;
        waiterID += 1;
      
        yDestination = 10;
        onScreenHomeX = (waiterID * 30) ; 
        xDestination = onScreenHomeX;
    }

    public void updatePosition() {
    	
    	if (currentTable < 0 || currentTable > 5 )
    		System.out.println("Something wrong with current table");
    	
        if (xPos < xDestination)
            xPos+=walkSpeed;
        else if (xPos > xDestination)
            xPos-=walkSpeed;

        if (yPos < yDestination)
            yPos+=walkSpeed;
        else if (yPos > yDestination)
            yPos-=walkSpeed;

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
    	if (holdFood == false) {
    		g.setColor(Color.MAGENTA);
    		g.fillRect(xPos, yPos, waiterSize, waiterSize);
    	}
    	else {
    		g.setColor(Color.RED);
            g.fillRect(xPos, yPos, waiterSize, waiterSize);
    	}
    	
    	//change color if the waiter is on break.
    	if (onBreak == false ) {
    		//do nothing 
    	}
    	else {
    		g.setColor(Color.BLACK);
            g.fillRect(xPos, yPos, waiterSize, waiterSize);
    	}
    	
    }
     
    public void gui_msgStartWork() {
    	isWorking = true;
    	isPresent = true;
    }

    public boolean isPresent() {
        return true;
    }
    
    public void holdingFood () {
		
    	if (holdFood == false) {
    		holdFood = true; 
    	}
    	else {
    		holdFood = false;
    	}
		
	}
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~ COORDINATE COMMANDS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public void DoGoToCook() { 
    	
    	currentTable = 0;  
    	atDest = false;
    	
    	xDestination = xCook + allignmentSpace;
    	yDestination = yCook - allignmentSpace;
    	
    }
    
    public void DoGoToThisTable(int tableNum) {
    	
    	currentTable = tableNum; 
    	atDest = false; 
    
    	if (tableNum > 0) {
    		xDestination = xTable + ((tableNum - 1) * spaceBtwnTables) + allignmentSpace;
    		yDestination = yTable - allignmentSpace;
    	}
    	else {
    		xDestination = onScreenHomeX;
    		yDestination = onScreenHomeY;
    		System.out.println("waiter given bad table");
    	}
    	
    }
     
    public void DoGoHome() {
    
    	atDest = false; 
    	currentTable = 0; 
    	
        xDestination = onScreenHomeX; //10
        yDestination = onScreenHomeY; //10
        
    }
    
    public void DoIdleOffScreen () {
    	
    	xDestination = onScreenHomeX; //10
        yDestination = onScreenHomeY; //10 
    }

	public void DoGoPickupCust(int yDest) {
		
		atDest = false; 
		currentTable = 0;
		
		xDestination = 10;
		
		if (yDest < 0) {
			yDestination = 0;
		}	
		else {
			yDestination = yDest;
		}
		
	}
 
}
