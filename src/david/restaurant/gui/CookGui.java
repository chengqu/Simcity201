package david.restaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import david.restaurant.CookAgent;
import david.restaurant.Menu;
import david.restaurant.Order;

public class CookGui implements Gui{

	CookAgent agent;
	
	Menu m = new Menu();
	
	private Timer timer = new Timer();
	
	static int xCooking = 350, yCooking = 95;
	static int xPlating = 350, yPlating = 145;
	
	enum OrderState {needsCooking, cooking, doneCooking, needsToBePlated};
	enum state {none, goingToPlating, goingToCooking};
	
	state currentState = state.none;
	String printString = "a";
	
	int xDestination = xCooking;
	int yDestination = yCooking;
	int xPos = xCooking;
	int yPos = yCooking;
	private List<myOrder> orders = Collections.synchronizedList(new ArrayList<myOrder>());
	myOrder currentOrder = null;
	
	private Object lock = new Object();
	
	public CookGui(CookAgent c)
	{
		agent = c;
	}
	//put a hierarchy that tries to find if a food is done, if it is then we should 
	//put that one in the plating area
	@Override
	public void updatePosition() {
		synchronized(lock)
		{
			boolean foundOrder = false;
			if(currentState == state.none)
			{
				for(myOrder order: orders)
				{
					if(order.state == OrderState.needsToBePlated)
					{
						currentOrder = order;
						currentState = state.goingToPlating;
						xDestination = xPlating;
						yDestination = yPlating;
						foundOrder = true;
						break;
					}
				}
				if(foundOrder == false)
				{
					for(myOrder order: orders)
					{
						if(order.state == OrderState.needsCooking)
						{
							currentOrder = order;
							currentState = state.goingToPlating;
							xDestination = xPlating;
							yDestination = yPlating;
							foundOrder = true;
							break;
						}
					}
				}
			}
			else if(currentState == state.goingToPlating)
			{
				if (xPos == xDestination && yPos == yDestination) {
		           if(currentOrder.state == OrderState.needsToBePlated)
		           {
		        	   orders.remove(currentOrder);
		        	   agent.gMsgOrderReady(currentOrder.order);
		        	   currentOrder = null;
		        	   currentState = state.goingToCooking;
		        	   System.out.println("removed order");
		        	   xDestination = xCooking;
		        	   yDestination = yCooking;
		           }
		           else if(currentOrder.state == OrderState.needsCooking)
		           {
		        	   currentState = state.goingToCooking;
		        	   xDestination = xCooking;
		        	   yDestination = yCooking;
		           }
		        }
				else
				{
					if (xPos < xDestination)
			            xPos++;
			        else if (xPos > xDestination)
			            xPos--;
	
			        if (yPos < yDestination)
			            yPos++;
			        else if (yPos > yDestination)
			            yPos--;
				}
			}
			else if(currentState == state.goingToCooking)
			{
				if (xPos == xDestination && yPos == yDestination) {
					if(currentOrder == null)
					{
						currentState = state.none;
					}
			           else if(currentOrder.state == OrderState.needsCooking)
			           {
			        	   final myOrder o = currentOrder;
			        	   timer.schedule(new TimerTask(){
			        		   public void run()
			        		   {
			        			   synchronized(lock)
			        			   {
			        				   o.state = OrderState.needsToBePlated;
			        				   System.out.println("done cooking");
			        			   }
			        		   }
			        	   }, currentOrder.time);
			        	   currentOrder.state = OrderState.cooking;
			        	   currentOrder = null;
			        	   currentState = state.none;
			           }
			        }
					else
					{
						if (xPos < xDestination)
				            xPos++;
				        else if (xPos > xDestination)
				            xPos--;
		
				        if (yPos < yDestination)
				            yPos++;
				        else if (yPos > yDestination)
				            yPos--;
					}
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		//box that represents the cook
		g.setColor(Color.black);
		g.fillRect(350, 80, 20, 50);
		
		g.setColor(Color.black);
		g.drawString("Cooking", 390, 110);
		
		g.setColor(Color.pink);
		g.fillRect(350, 140, 20, 50);
		
		g.setColor(Color.black);
		g.drawString("Plating", 390, 170);
		
		g.setColor(Color.CYAN);
		g.fillRect(370, yPos, 20, 20);
		
		if(currentOrder != null)
		{
			g.setColor(Color.black);
			g.drawString(Menu.getAbbreviation(currentOrder.order.choice), xPos + 20, yPos);
		}
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	public void msgNewOrder(Order o, int time)
	{
		synchronized(lock)
		{
			orders.add(new myOrder(o, OrderState.needsCooking, time));
		}
	}

	private class myOrder
	{
		public Order order;
		OrderState state;
		int time;
		public myOrder(Order o, OrderState s, int t)
		{
			state = s;
			order = o;
			time = t;
		}
	}
}
