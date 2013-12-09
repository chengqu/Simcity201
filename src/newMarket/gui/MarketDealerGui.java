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
import newMarket.gui.Line.WaitPosition;
import simcity201.gui.Gui;

public class MarketDealerGui implements Gui {

    private MarketDealerAgent agent = null; 
    public Line line;
    
    public boolean isPresent = true;
   
    private int xPos, yPos;  
    private int xDestination, yDestination;
    public int onScreenHomeX, onScreenHomeY;
    private int carDestination = 480;
    
    public static int dealerSize = 30; 
    //public static int allignmentSpace = 50;  
    public static int walkSpeed = 2; 
    public static int offScreen = -20; 
   
    private boolean atDest; 
    //private boolean holdStuff;
    private boolean carCarry = false;
   
    private List<WaitPosition> waitingPositions;
    
    public class WaitPosition {
		MarketCustomerGui occupiedBy_; 
		WaitPosition(MarketCustomerGui g) {
			occupiedBy_ = g;
		}
		void setUnoccupied() {
			occupiedBy_ = null;
		}
		void setOccupant(MarketCustomerGui occupier) {
			this.occupiedBy_ = occupier;
		}
		MarketCustomerGui getOccupant() {
			return occupiedBy_;
		}
		boolean isOccupied() {
			return (occupiedBy_ != null);
		}
	}
    
    public MarketDealerGui(MarketDealerAgent a) {
        this.agent = a;
        atDest = true;
        waitingPositions = new ArrayList<WaitPosition>(); 
        line = new Line(a);
        
        // to determine where this employee lines up
        xDestination = 60; 
        yDestination = 400;
        
        //we start off making the employee at the employee station
        xPos = xDestination;
        yPos = yDestination; 
   
        //this is their home too
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
        	
        	if (xDestination == onScreenHomeX && yDestination == onScreenHomeY) { 
        		carCarry = false;  
        		//agent.gui_msgBackAtHomeBase();
        		atDest = true; 
        	}
        	else if (xDestination == carDestination && yDestination == carDestination) {
        		xDestination = onScreenHomeX;
        		yDestination = onScreenHomeY;
        		carCarry = true;
        	}
        	else {
        		System.out.println("update position for market employee wacky");
        	}
        	
        }
          
    }
    
    public void draw(Graphics2D g) {
    	
    	if (carCarry) {
    		g.fillOval(xPos, (yPos-10), (dealerSize * 2), (dealerSize * 2));
    	}
    	
    	g.setColor(Color.CYAN);
    	g.fillRect(xPos, yPos, dealerSize, dealerSize);
    }
     
    public void gui_msgStartWork() {
    	isPresent = true;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~ COORDINATE COMMANDS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public void DoWaitForDealer(MarketCustomerGui custGui) {
    	
    	if (waitingPositions.size() == 0) {
			waitingPositions.add(new WaitPosition(custGui));
			//custGui.setXDest(agent.gui.getXHome());
			custGui.setXDest(5);
			custGui.setYDest((agent.gui.getYHome() - 20));
		}
    	else {
			int freeCount = 1;
			boolean seated = false;
			for (WaitPosition w : waitingPositions) {
				if (!w.isOccupied()) {
					w.setOccupant(custGui);
					//custGui.setXDest(agent.gui.getXHome());
					custGui.setXDest(5);
					custGui.setYDest((agent.gui.getYHome() - ((freeCount - 1) * 30)));
					seated = true;
					break;
				}
				freeCount += 1;
			}
			if (seated == false) { //if this new position exceed the positions already available 
				waitingPositions.add(new WaitPosition(custGui));
				//custGui.setXDest(agent.gui.getXHome());
				custGui.setXDest(5);
				custGui.setYDest((agent.gui.getYHome() - ((freeCount - 1) * 30)));
			}
			
		}
    	
    } 
    
    public void exitLine(MarketCustomerGui custGui) {
    	for (WaitPosition wp : waitingPositions) {
    		if (wp.getOccupant() == custGui) {
    			wp.setUnoccupied();
    			custGui.DoExitMarket(null);
    			break; 
    		}
    	}
    	//waitingPositions.remove(custGui);
    	System.out.println("leaving the dealer line");
    	//updateLine();
    }

	public void updateLine() {
		for (WaitPosition wp : waitingPositions) {
			if (wp.isOccupied()) {
				wp.occupiedBy_.DoWalkDownLine();
			}
		}
	}
     
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public void DoFetchCar(String typeOfCar) {	
    	
    	//do nothing with the car yet i guess...
    	
    	atDest = false; 
    	xDestination = carDestination;
    	yDestination = carDestination;
    }
    
    public void DoGoHome() {
    	atDest = false; 	
        xDestination = onScreenHomeX; 
        yDestination = onScreenHomeY; 
    }
    
    public boolean isPresent() {
        return isPresent;
    }

	public final int getXHome() {
		return onScreenHomeX;
	}
 
	public final int getYHome() {
		return onScreenHomeY;
	}
	
	public final int getXPos() {
        return xPos;
    }

    public final int getYPos() {
        return yPos;
    }
   
}
