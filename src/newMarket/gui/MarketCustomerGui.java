package newMarket.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import simcity201.gui.Gui;
import newMarket.*;

public class MarketCustomerGui implements Gui {

	private MarketCustomerAgent agent = null;
	MarketAnimationPanel animationPanel = null;
	
	public void setAnimationPanel(MarketAnimationPanel m) {
		animationPanel = m;
	}
	
	private boolean isPresent = true;
	private int xPos, yPos;
	private int xDest, yDest, xFinalDest, yFinalDest;
	
	private enum Command {noCommand, GoToEmployee, LeaveMarket, waitInLine};
	private Command command=Command.noCommand;

	public static int customerSize = 20; 
	public static int offScreen = -40; 
	public static int startCor = 0; 
	public static int walkSpeed = 2;
	
	public static final int onScreenHomeX = 10;
	public static final int onScreenHomeY = 10;
	public static final int spacebtwn = 30;
	
	//wait position list is static and shared with all market customer guis
	public static List<WaitPosition> waitingPos = new ArrayList<WaitPosition>();
	public static List<WaitPosition> cashierPos = new ArrayList<WaitPosition>(2);
	
	public class WaitPosition {
		MarketCustomerAgent occupiedBy_;
		public int xPos;
		public int yPos; 
		WaitPosition(MarketCustomerAgent c, int xPos, int yPos) {
			occupiedBy_ = c;
			this.xPos = xPos;
			this.yPos = yPos; 
		}
		public void setUnoccupied() {
			occupiedBy_ = null;
		}
		public void setOccupant(MarketCustomerAgent cust) {
			this.occupiedBy_ = cust;
		}
		public MarketCustomerAgent getOccupant() {
			return occupiedBy_;
		}
		public boolean isOccupied() {
			return (occupiedBy_ != null);
		}
	}

	public MarketCustomerGui(MarketCustomerAgent c){
		agent = c;
		
		//below block to for determining where people wait.
		if (waitingPos.size() == 0) {
			waitingPos.add(new WaitPosition(c, 10, 10));
			xDest = onScreenHomeX;
			yDest = onScreenHomeY;
		}
		else {
			int freeCount = 1;
			boolean seated = false;
			for (WaitPosition w : waitingPos) {
				if (!w.isOccupied()) {
					w.setOccupant(c);
					xDest = onScreenHomeX;
					yDest = onScreenHomeY + (freeCount * spacebtwn);
					seated = true;
					break;
				}
				freeCount += 1;
			}
			if (seated == false) { //if this new position exceed the positions already available 
				waitingPos.add(new WaitPosition(c, 10, 10));
				xDest = onScreenHomeX;
				yDest = (onScreenHomeY) + (freeCount * spacebtwn);
			}
		}
		//**************************************
		
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
		
		//move X direction
		if (xPos < xDest)
			xPos+=walkSpeed;
		else if (xPos > xDest)
			xPos-=walkSpeed;

		//move Y direction
		if (yPos < yDest)
			yPos+=walkSpeed;
		else if (yPos > yDest)
			yPos-=walkSpeed;

		//the state changes are important here as they inform customer gui what msg to send back to customer
		if (xPos == xDest && yPos == yDest) {
			
			if(command == Command.GoToEmployee) {
				//System.out.println("at employeeeeeeee");
				agent.gui_msgAtEmployee();
			}
			else if (command == Command.LeaveMarket) {
				agent.gui_msgOffScreen();
				setPresent(false);
			}
			else if (command == Command.waitInLine) {
				//if this spot in line the last spot?
				
				
				
				
			}
			command = Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, customerSize, customerSize);
	}

	//customer goes to a cashier if one of them 
	
	//public void DoGoTo()
	
	
	
	/*
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
	*/
	
	public void DoExitMarket(MarketCustomerAgent c) {
		
		for (WaitPosition w : waitingPos) {
			if (w.getOccupant() == c) {
				w.setUnoccupied();
				break;
			}
		}
		
		xDest = offScreen;
		yDest = offScreen;
		command = Command.LeaveMarket;
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoTo(MarketCashierAgent temp) {
		
		command = Command.waitInLine;
		
		xFinalDest = temp.gui.getXHome();
		yFinalDest = temp.gui.getYHome();
		
		Dimension dim = temp.gui.line.waitInLine(agent);
		
		xDest = dim.width; 
		yDest = dim.height; 
	}
}
