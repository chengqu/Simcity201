package newMarket.gui;

import java.util.List;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import newMarket.MarketAnimationPanel;
import newMarket.MarketCashierAgent;
import newMarket.MarketCustomerAgent;
import simcity201.gui.Gui;

public class MarketCashierGui implements Gui {
	
	private MarketAnimationPanel marketAnimationPanel; 
    private MarketCashierAgent agent = null; 
    public CashierLine line;
    
    public boolean isPresent = true;
   
    private int xPos, yPos;  
    private int xDestination, yDestination;
    public int onScreenHomeX, onScreenHomeY;
    
    public static int employeeSize = 20; 
    public static int allignmentSpace = 50;  
    public static int walkSpeed = 2; 
    public static int offScreen = -20; 
   
    private boolean atDest; 
    private boolean holdStuff; 
    
    private boolean onBreak;
    private static int employeeID = 0;  
    
    //getting food in the gui stuff
    Map<String, Dimension> foodLocMap = new HashMap<String, Dimension>();
    String currentFoodFetch = null;
    List<Dimension> fetchList = new ArrayList<Dimension>();
    private int fetchListInt;
      
    private void initMyStoreMap() {
    	
    	//these are locations that the employee gui will go to get
    	//the specific products referred to by string 
    	foodLocMap.put("steak", new Dimension(200, 340));
    	foodLocMap.put("chicken", new Dimension(280, 280));
    	foodLocMap.put("pizza", new Dimension(320, 300));
    	foodLocMap.put("salad", new Dimension(440, 340));
    	
    }

    public MarketCashierGui(MarketCashierAgent a) {
        agent = a;
        atDest = true;
        holdStuff = false; 
        onBreak = false;
        line = new CashierLine(agent); 
        
        initMyStoreMap();
        
        // to determine where this employee lines up
        xDestination = 160 + ((employeeID % 3) * 120);
       
        // for now employees might be on top of each other 
        yDestination = 80;
        
        //we start off making the employee at the employee station
        xPos = xDestination;
        yPos = yDestination; 
        
        //this is their desination
        onScreenHomeX = xDestination;
        onScreenHomeY = yDestination;
        
        employeeID += 1;
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
        	if (xDestination == fetchList.get(fetchListInt).width && 
        			yDestination == fetchList.get(fetchListInt).height) {
        		//now change colors to show that you are holding something
        		holdStuff = true;
        		if ((fetchListInt + 1) == fetchList.size() ) {
        			//now go back to home
            		xDestination = onScreenHomeX;
            		yDestination = onScreenHomeY;
            		fetchListInt = 0;
        		}
        		else {
        			//iterate to next item on the fetch list
        			fetchListInt += 1;
        			xDestination = fetchList.get(fetchListInt).width;
        			yDestination = fetchList.get(fetchListInt).height;
        		}
        	}
        	else if (xDestination == onScreenHomeX && yDestination == onScreenHomeY
        			&& holdStuff == true) { 
        		holdStuff = false;  
        		agent.gui_msgBackAtHomeBase();
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
    		if (fetchListInt % 3 == 0)
    			g.setColor(Color.RED);
    		else if (fetchListInt % 3 == 1)
    			g.setColor(Color.CYAN);
    		else
    			g.setColor(Color.YELLOW);
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
    	isPresent = true;
    }

    public boolean isPresent() {
        return isPresent;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~ COORDINATE COMMANDS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public void DoGetThisItem(List<String> foodList) {
    	System.out.println("DoGetThisStuffCalled");
    	
    	atDest = false; 
    	fetchList.clear();
    	fetchListInt = 0;
    	
    	//copy items from passed in food list to make dimension destinations 
    	for (String food : foodList) {
    		Dimension temp = foodLocMap.get(food); 
    		if (temp != null) {
    			fetchList.add(temp);
    		}
    		else {
    			System.out.println("the waiter cannot retreive that unknown food");
    		}
    	}
    	
    	if (fetchList.isEmpty() == true) {
    		System.out.println("the waiter has nothing to retreive");
    		xDestination = onScreenHomeX;
    		yDestination = onScreenHomeY;
    		//agent.gui_msgBackAtHomeBase();
    		atDest = true;
    		return;
    	}
    	
    	xDestination = fetchList.get(0).width;
    	yDestination = fetchList.get(0).height;
    	fetchListInt = 0;
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

	public int getXHome() {
		return onScreenHomeX;
	}
 
	public int getYHome() {
		return onScreenHomeY;
	}

	public int howManyCustInLine() {
		return line.howManyInLine();
	}
}
