package newMarket.gui;

import java.util.List;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Market.MarketCustomerGui.WaitPosition;
import newMarket.MarketAnimationPanel;
import newMarket.MarketCashierAgent;
import newMarket.MarketCustomerAgent;
import simcity201.gui.Gui;

public class CashierLine {
	
	public static List<WaitPosition> waitingPositions; 
	private MarketCashierAgent agent = null; 
	
	public class WaitPosition {
		MarketCustomerGui occupiedBy_;
		public int xPos = 10;
		public int yPos = 10; 
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
	
	/*
	if (waitingPos.size() == 0) {
			waitingPos.add(new WaitPosition(c));
			xDestination = onScreenHomeX;
			yDestination = onScreenHomeY;
		}
		else {
			int freeCount = 1;
			boolean seated = false;
			for (WaitPosition w : waitingPos) {
				if (!w.isOccupied()) {
					w.setOccupant(c);
					xDestination = onScreenHomeX;
					yDestination = onScreenHomeY + (freeCount * spacebtwn);
					seated = true;
					break;
				}
				freeCount += 1;
			}
			if (seated == false) { //if this new position exceed the positions already available 
				waitingPos.add(new WaitPosition(c));
				xDestination = onScreenHomeX;
				yDestination = (onScreenHomeY) + (freeCount * spacebtwn);
			}
			
		}
		
		xPos = startCor;
		yPos = startCor;
		
	*/

	//private List<MarketCustomerGui> customersWaiting;
	
	public CashierLine(MarketCashierAgent c) {
		agent = c;
		waitingPositions = new ArrayList<WaitPosition>();
    }
	
	/*
    public boolean isLineEmpty() {
    	//return customersWaiting.isEmpty();
    }
    */
	
    public int howManyInLine() {
    	return waitingPositions.size();
    }
	
    public void waitInLine(MarketCustomerGui custGui) {
    	
    	if (waitingPositions.size() == 0) {
			waitingPositions.add(new WaitPosition(custGui));
			custGui.setXDest(agent.gui.onScreenHomeX - 20);
			custGui.setYDest(agent.gui.onScreenHomeY);
		}
    	else {
			int freeCount = 1;
			boolean seated = false;
			for (WaitPosition w : waitingPositions) {
				if (!w.isOccupied()) {
					w.setOccupant(custGui);
					custGui.setXDest((agent.gui.onScreenHomeX - 20));
					//does it need to be free count - 1?????
					custGui.setYDest((agent.gui.onScreenHomeY - ((freeCount - 1) * 30)));
					seated = true;
					break;
				}
				freeCount += 1;
			}
			if (seated == false) { //if this new position exceed the positions already available 
				waitingPositions.add(new WaitPosition(custGui));
				custGui.setXDest((agent.gui.onScreenHomeX - 20));
				//does it need to be free count - 1?????
				custGui.setYDest((agent.gui.onScreenHomeY - ((freeCount - 1) * 30)));
			}
			
		}
    	
    	/*
    	if(!agent.isOccupied) {
    		Dimension temp = new Dimension();
    		temp.width = agent.gui.onScreenHomeX - 20;
        	temp.height = agent.gui.onScreenHomeY; 
        	return temp;
    	}
    	else {
	    	customersWaiting.add(custGui);
	    	Dimension temp = new Dimension();
	    	temp.width = agent.gui.onScreenHomeX - 20;
	    	int length = customersWaiting.size();
	    	temp.height = agent.gui.onScreenHomeY - (length * 30);
	    	//System.out.println(customersWaiting.size());
	    	//System.out.println("YPPPPPPPPP" + temp.width + " " + temp.height);
	    	return temp;
    	}
    	*/
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
    	System.out.println("leaving the newMarket line");
    	updateLine();
    }

	public void updateLine() {
		for (WaitPosition wp : waitingPositions) {
			if (wp.isOccupied()) {
				wp.occupiedBy_.DoWalkDownLine();
			}
		}
	}
}
