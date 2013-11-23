package simcity201.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import agents.BankCustomerAgent;

public class BankCustomerGui implements Gui {

	private int SIZE_CUSTOMER_X, SIZE_CUSTOMER_Y = 20;
	
    private int xPos = BankMap.ENTRANCE.x, yPos = BankMap.ENTRANCE.y;//default customer position
    private int xDestination = BankMap.ENTRANCE.x, yDestination = BankMap.ENTRANCE.y;//default start position

    private enum Command {noCommand, goToDest, backToCounter, nothingToDo};
    private Command command = Command.noCommand;
    
    private class Destination {
    	Point p;
    	Command c;
    	public Destination(Point p, Command c) {
    		this.p = p;
    		this.c = c;
    	}
    }
    
    private List<Destination> destinations = new ArrayList<Destination>();
    
    BankCustomerAgent agent;
    
    
    
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
		g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, SIZE_CUSTOMER_X, SIZE_CUSTOMER_Y);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAgent(BankCustomerAgent agent) {
		this.agent = agent;
	}
}
