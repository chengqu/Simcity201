package david.restaurant.gui;

import david.restaurant.CustomerAgent;
import david.restaurant.HostAgent;
import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CustomerGui implements Gui{

	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	private Timer timer;
	private Random rand;
	private boolean allowedToMove = false;
	private String foodString = "";
	private HostAgent host;
	
	boolean sendMessage = false;
	
	Object lock = new Object();

	//private HostAgent host;
	RestaurantGui gui;
	
	private static int xCashier = -20;
    private static int yCashier = 400;
	private static int xEntrance = -40;
	private static int yEntrance = -40;

	private int xPos = xEntrance, yPos = yEntrance;
	private int xDestination = xEntrance, yDestination = yEntrance;
	private enum Command {noCommand, goToWaitingArea, goToSeat, LeaveRestaurant, waitToEat, Eat, goToCashier};
	private Command command = Command.noCommand;

	public CustomerGui(CustomerAgent c, RestaurantGui gui, HostAgent h){
		timer = new Timer();
		rand = new Random();
		agent = c;
		this.gui = gui;
		host = h;
		xPos = xEntrance;
		yPos = yEntrance;
	}

	public void updatePosition() {
		if(command != Command.noCommand || (command == Command.goToSeat && allowedToMove == true) ||
				command == Command.goToWaitingArea)
		{
			if(sendMessage == true)
			{
				host.gMsgShiftCustomers();
				sendMessage = false;
			}
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;
		
			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
		
			if (xPos == xDestination && yPos == yDestination) {
				if(command == Command.goToSeat)
				{
					agent.msgNowSeated();
					allowedToMove = false;
					command = Command.noCommand;
				}
				else if(command == Command.LeaveRestaurant)
				{
					agent.msgResetState();
					command = Command.noCommand;
				}
				else if(command == Command.goToCashier)
				{
					agent.msgAtCashier();
					command = Command.noCommand;
				}
				else if(command == Command.goToWaitingArea)
				{
					command = Command.noCommand;
					host.gMsgCustomerReady(agent);
				}
			}
		}
	}
	
	public void msgCanGoToTable()
	{
		synchronized(lock)
		{
			allowedToMove = true;
			if(command == Command.goToSeat)
			{
				sendMessage = true;
			}
		}
	}
	
	public void msgGoToWaitingArea(int x, int y)
	{
		xDestination = x;
		yDestination = y;
		command = Command.goToWaitingArea;
	}

	public void draw(Graphics2D g) {
		int customerRectSize = 20;
		int xWhite = 20;
		int xString = 20;
		int xWhiteSize = 25;
		int yWhiteSize = 20;
		int yString = 15;
		String question = "?";
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, customerRectSize, customerRectSize);
		g.setColor(Color.WHITE);
		if(command == Command.Eat && foodString != "")
		{
			g.fillRect(xPos + xWhite, yPos, xWhiteSize, yWhiteSize);
			g.setColor(Color.BLACK);
			g.drawString(foodString, xPos + xString, yPos + yString);
		}
		else if(command == Command.waitToEat && foodString != "")
		{
			g.fillRect(xPos + xWhite, yPos, xWhiteSize, yWhiteSize);
			g.setColor(Color.BLACK);
			g.drawString(foodString + question, xPos + xString, yPos + yString);
		}
	}

	public boolean isPresent() {
		return true;
	}
	public void setHungry() {
		isHungry = true;
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToCashier()
	{
		xDestination = xCashier;
		yDestination = yCashier;
		command = Command.goToCashier;
	}

	public void DoGoToSeat(int seatnumber) {
		synchronized(lock)
		{
			Table table = null;
			for(Table temp:HostAgent.tables)
			{
				if(temp.tableNumber == seatnumber)
				{
					table = temp;
					break;
				}
			}
			if(table != null)
			{
				xDestination = table.xPos;
				yDestination = table.yPos;
				command = Command.goToSeat;
			}
			if(allowedToMove == true)
			{
				sendMessage = true;
			}
		}
	}
	
	public void DoEatingAnimation()
	{
		command = Command.Eat;
		int timeMax = 6;
		int time = rand.nextInt(timeMax);
		int timeMultiplier = 1000;
		if(time == 0)
		{
			time+=1;
		}
		command = Command.Eat;
		timer.schedule(new TimerTask(){
			public void run() {
				agent.msgDoneEating();
			}
			
		}, time * timeMultiplier);
	}
	
	public void DoLeaveRestaurant()
	{
		xDestination = xEntrance;
		yDestination = yEntrance;
		command = Command.LeaveRestaurant;
	}

	public void DoExitRestaurant() {
		xDestination = xEntrance;
		yDestination = yEntrance;
		command = Command.LeaveRestaurant;
	}
	
	public void waitingForFood(String s)
	{
		foodString = s;
		command = Command.waitToEat;
	}
	
	public void eatingFood()
	{
		command = Command.Eat;
	}
}
