package Market;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import simcity201.gui.Gui;

public class MarketEmployeeGui implements Gui {
	
	private MarketAnimationPanel marketAnimationPanel; 
    private MarketEmployeeAgent agent = null; 
    
    private boolean isPresent = true;
    private boolean isWorking;

    private int xPos = -10, yPos = -10; //waiter position offscreen initially 
    private int xDestination = 10, yDestination = 10;
    
    public static int employeeSize = 20; 
    
    public static int allignmentSpace = 50;  
    
    public static int walkSpeed = 2; 
    
    public int onScreenHomeX;
    public int onScreenHomeY;
    public static int offScreen = -20; 
   
    private boolean atDest; 
    private boolean holdStuff; 
    
    private boolean onBreak;
    
    Map<String, Dimension> myStoreMap = new HashMap<String, Dimension>();
    String currentFoodFetch = null;
    
    private void initMyStoreMap() {
    	
    	//these are locations that the employee gui will go to get
    	//the specific products referred to by string 
    	myStoreMap.put("steak", new Dimension(200, 360));
    	myStoreMap.put("chicken", new Dimension(280, 280));
    	myStoreMap.put("pizza", new Dimension(320, 300));
    	myStoreMap.put("salad", new Dimension(440, 360));
    	
    }

    public MarketEmployeeGui(MarketEmployeeAgent a) {
        agent = a;
        atDest = true;
        
        holdStuff = false; 
        onBreak = false;
        
        initMyStoreMap();
      
        yDestination = 80;
        xDestination = 160;
        
        //we start off making the employee at the employee station
        yPos = 80;
        xPos = 160; 
        
        onScreenHomeY = 80;
        onScreenHomeX = 160;
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
        	if (xDestination == (myStoreMap.get(currentFoodFetch)).width &&
        			yDestination == (myStoreMap.get(currentFoodFetch)).height) {
        		//now change colors to show that you are holding something
        		holdingStuff();
        		//now go back to home
        		xDestination = onScreenHomeX;
        		yDestination = onScreenHomeY;
        	}
        	else if (xDestination == onScreenHomeX && yDestination == onScreenHomeY
        			&& holdStuff == true) { 
        		holdStuff = false;  
        		//agent.gui_msgBackAtHomeBase();
        		atDest = true; 
        	}
        	else {
        		System.out.println("update position for market employee wacky");
        	}
        	
        }
          
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
    
    public void DoGetThisItem(String itemString) {
    	
    	atDest = false; 
    	currentFoodFetch = itemString;
    	
    	Dimension temp = myStoreMap.get(itemString); 
    	
    	if (temp == null) {
    		System.out.println("the waiter cannot retreive that unknown food");
    		xDestination = onScreenHomeX;
    		yDestination = onScreenHomeY;
    		currentFoodFetch = null;
    		return;
    	}
    	
    	xDestination = temp.width;
    	yDestination = temp.height;
    
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
 
}
