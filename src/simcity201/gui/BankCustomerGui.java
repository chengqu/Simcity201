package simcity201.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import agents.BankCustomerAgent;
import agents.BankTellerAgent;

public class BankCustomerGui implements Gui {

	private int SIZE_CUSTOMER_X =20, SIZE_CUSTOMER_Y = 20;
	
    private int xPos = BankMap.ENTRANCE.x-30, yPos = BankMap.ENTRANCE.y+30;//default customer position
    private int xDestination = BankMap.ENTRANCE.x, yDestination = BankMap.ENTRANCE.y;//default start position

    private enum Command {noCommand, goToDest, backToCounter, nothingToDo};
    private Command command = Command.noCommand;
    
    private class Destination {
    	Point p;
    	Command c;
    	Destination(Point p, Command c) {
    		this.p = p;
    		this.c = c;
    	}
    }
    
    private List<Destination> destinations = new ArrayList<Destination>();
    
    BankCustomerAgent agent;
    
    private BankMap map;
    
    public  BankCustomerGui(BankCustomerAgent agent, BankMap map) {
    	this.agent = agent;
    	this.map = map;
    }
    
    
	@Override
	public void updatePosition() {
		
		if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination) {
        	// Animation has arrived at the destination
        	
        	if (command==Command.goToDest) {
        		agent.msgAtDestination();
        		command = Command.noCommand;
        	}
        	
           if (!destinations.isEmpty()) {
        	   Destination dest = destinations.remove(0);
        	   xDestination = (int)dest.p.getX();
        	   yDestination = (int)dest.p.getY();
        	   command = dest.c;
           }
        }
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
        g.fillRect(xPos, yPos, SIZE_CUSTOMER_X, SIZE_CUSTOMER_Y);
	}

	@Override
	public boolean isPresent() {
		
		return true;
	}

	public void setAgent(BankCustomerAgent agent) {
		this.agent = agent;
	}
	public void setMap(BankMap map) {
		this.map = map;
	}


	public void DoGoToLine() {
		Point p = map.getCustomerPosition(this);
		destinations.add(new Destination(p, Command.goToDest));
	}


	public void DoApproachTeller(BankTellerAgent teller) {
		Point p = map.getTellerWindow(teller.getGui());
		destinations.add(new Destination(p, Command.goToDest));
		map.positionAvailable(this);
	}
	
	public void DoLeaveBank() {
		destinations.add(new Destination(new Point(BankMap.ENTRANCE.x, BankMap.ENTRANCE.y), Command.goToDest));
	}
}
