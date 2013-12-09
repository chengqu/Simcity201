package david.restaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.Semaphore;

import david.restaurant.CustomerAgent;
import david.restaurant.HostAgent;
import david.restaurant.Menu;
import david.restaurant.WaiterAgent;
import david.restaurant.Interfaces.Waiter;

public class WaiterGui implements Gui{
	private int xBreakRoom = 370, yBreakRoom = 400;
	private static int xCook = 330, yCook = 160;
	private static int xCustomerInitial = 120, yCustomerInitial = 50;
	public int xPos, yPos;//default waiter position
    public int xDestination = -20, yDestination = -20;//default start position
    private static int xCashier = -20;
    private static int yCashier = 400;
    private Waiter waiter;
    private CustomerGui cGui;
    private int tableNumber;
    private String choice;
    private boolean canDrawChoice = false;
    HostAgent host;
    
    private enum Command {none, seating, breakRoom, takeOrder, getCustomer, goToCook, goToCashier};
    private Command command;
    
    public WaiterGui(Waiter w, int xBreak, int yBreak, HostAgent h)
    {
    	host = h;
    	xPos = xBreak;
    	yPos = yBreak;
    	xBreakRoom = xBreak;
    	yBreakRoom = yBreak;
    	command = Command.none;
    	waiter = w;
    }
    
	@Override
	public void updatePosition() {
		if(command != Command.none)
		{
			if (xPos < xDestination)
		        xPos++;
		    else if (xPos > xDestination)
		        xPos--;
		
		    if (yPos < yDestination)
		        yPos++;
		    else if (yPos > yDestination)
		        yPos--;
		
		    if (xPos == xDestination && yPos == yDestination)
		    {
		    	if(command == Command.seating)
		    	{
		    		command = Command.none;
		    		waiter.msgDoneSeating();
		    	}
		    	else if(command == Command.breakRoom)
		    	{
		    		command = Command.none;
		    	}
		    	else if(command == Command.takeOrder)
		    	{
		    		command = Command.none;
		    		waiter.msgCanTakeOrder();
		    	}
		    	else if(command == Command.getCustomer)
		    	{
		    		command = Command.none;
		    		waiter.msgCanStartSeating();
		    		cGui.DoGoToSeat(tableNumber);
		    	}
		    	else if(command == Command.goToCook)
		    	{
		    		command = Command.none;
		    		waiter.msgAtCook();
		    	}
		    	else if(command == Command.goToCashier)
		    	{
		    		command = Command.none;
		    		waiter.msgAtCashier();
		    	}
		    	canDrawChoice = false;
		    	choice = "";
		    }
		}
	}

	@Override
	public void draw(Graphics2D g) {
		int rectSize = 20;
		int xString = 20;
		int yString = 15;
    	g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, rectSize, rectSize);
        if(canDrawChoice == true)
        {
        	g.setColor(Color.BLACK);
        	g.drawString(choice, xPos + xString, yPos + yString);
        }
    }

	@Override
	public boolean isPresent() {
        return true;
    }
	
	public void DoGetCustomer(CustomerAgent c, int table)
	{
		cGui = c.getGui();
		xDestination = xCustomerInitial;
		yDestination = yCustomerInitial;
		command = Command.getCustomer;
		tableNumber = table;
	}
	
	public void DoDrawFoodChoice(String c)
	{
		choice = c;
		canDrawChoice = true;
	}

    public void DoBringToTable(CustomerAgent customer, int t) {
    	Table table = null;
    	int offset = 20;
		for(Table temp: host.tables)
		{
			if(temp.tableNumber == t)
			{
				table = temp;
				break;
			}
		}
		if(table != null)
		{
			xDestination = table.xPos + offset;
			yDestination = table.yPos - offset;
			command = Command.seating;
		}
    }
    
    public void DoGoToCashier()
    {
    	xDestination = xCashier;
    	yDestination = yCashier;
    	command = Command.goToCashier;
    }
    
    public void DoGoToCook()
    {
    	xDestination = xCook;
    	yDestination = yCook;
    	command = Command.goToCook;
    	Menu m = new Menu();
    }
    
    public void DoGoToBreakRoom()
    {
    	xDestination = xBreakRoom;
        yDestination = yBreakRoom;
        command = Command.breakRoom;
    }
    
    public void DoGoToCustomer(int t)
    {
    	Table table = null;
    	int offset = 20;
		for(Table temp:host.tables)
		{
			if(temp.tableNumber == t)
			{
				table = temp;
				break;
			}
		}
		if(table != null)
		{
			xDestination = table.xPos + offset;
			yDestination = table.yPos - offset;
			command = Command.takeOrder;
		}
    }
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
