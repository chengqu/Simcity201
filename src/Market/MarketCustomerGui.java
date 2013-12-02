package Market;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import simcity201.gui.Gui;

public class MarketCustomerGui implements Gui {

	private MarketCustomerAgent agent = null;
	private boolean isPresent = true;

	MarketAnimationPanel animationPanel = null;

	private int xPos, yPos;
	private int xDestination, yDestination;
	
	private enum Command {noCommand, GoToEmployee, LeaveMarket};
	private Command command=Command.noCommand;

	public static int customerSize = 20; 
	public static int offScreen = -40; 
	
	public static int startCor = 0; 
	
	public static final int onScreenHomeX = 10;
	public static final int onScreenHomeY = 10;
	public static final int spacebtwn = 30;
	
	public static int walkSpeed = 2;
	
	public static List<WaitPosition> waitingPos = new ArrayList<WaitPosition>();
	
	public void setAnimationPanel(MarketAnimationPanel m) {
		animationPanel = m;
	}
	
	public class WaitPosition {
		MarketCustomerAgent occupiedBy_;
		public int xPos = 10;
		public int yPos = 10; 
		WaitPosition(MarketCustomerAgent c) {
			occupiedBy_ = c;
		}
		void setUnoccupied() {
			occupiedBy_ = null;
		}
		void setOccupant(MarketCustomerAgent cust) {
			this.occupiedBy_ = cust;
		}
		MarketCustomerAgent getOccupant() {
			return occupiedBy_;
		}
		boolean isOccupied() {
			return (occupiedBy_ != null);
		}
	}

	public MarketCustomerGui(MarketCustomerAgent c){
		agent = c;
		
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
	
	}
	
	public int getYPos () {
		return (yPos);
	}
	
	public int getXPos () {
		return (xPos);
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

		//the state changes are important here as they inform customer gui what msg to send back to customer
		if (xPos == xDestination && yPos == yDestination) {
			
			if(command == Command.GoToEmployee) {
				agent.gui_msgAtEmployee();
			}
			else if (command == Command.LeaveMarket) {
				//agent.gui_msgLeftMarket()
			}
			command = Command.noCommand;
			
			/*
			if (command==Command.GoToSeat) // initial approach to seat
				//agent.gui_msgAnimationFinishedGoToSeat();
			
			else if (command == Command.GoToCashier) 
				//agent.gui_msgAnimationFinishedGoToCashier();
			
			else if (command==Command.LeaveRestaurant) { // leaving restaurant 
				//agent.gui_msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				//reset customer so no longer hungry and so cust in list is no longer hungry
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
			
			*/
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, customerSize, customerSize);
	}

	//seat customer based on what # employee assigned to
	public void DoGoTo(MarketCustomerAgent c, MarketEmployeeAgent a) {
		
		for (WaitPosition w : waitingPos) {
			if (w.getOccupant() == c) {
				w.setUnoccupied();
				break;
			}
		}
		
		xDestination = (a.gui.onScreenHomeX) - 20 ;
		yDestination = 80;
		
		command = Command.GoToEmployee;	
	}
	
	public void DoExitMarket(MarketCustomerAgent c) {
		
		for (WaitPosition w : waitingPos) {
			if (w.getOccupant() == c) {
				w.setUnoccupied();
				break;
			}
		}
		
		xDestination = offScreen;
		yDestination = offScreen;
		command = Command.LeaveMarket;
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}
}
