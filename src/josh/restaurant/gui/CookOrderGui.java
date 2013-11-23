package josh.restaurant.gui;

import java.awt.*;

import josh.restaurant.CookAgent;
import josh.restaurant.CustomerAgent;
import josh.restaurant.HostAgent;
import josh.restaurant.CookAgent.MyOrder;

public class CookOrderGui implements Gui{

	private boolean isPresent = false;

	RestaurantGui gui;
	
	//private int startingXPos = 100; 
	private int xPos;
	private int yPos;
	
	private int xCook = 260;
	private int yCook = 300;
	
	private String orderStr; 
	private String foodStr;  
	
	private int spaceBtwn = 60;
	private int spaceAbove = 20; 
	boolean cooking;
	
	//MyOrder  displayedOrder;

	public CookOrderGui(RestaurantGui gui){ 
		//displayedOrder = null;
		this.gui = gui;
		setPresent(false);
		orderStr = "";
		cooking = false;
	}
	
	public void displayOrderCooking(MyOrder o, int position_) {
		//displayedOrder = o;
		orderStr = o.getChoice_(); 
		
		yPos = yCook;
		xPos = xCook + (position_ * spaceBtwn);
		
		cooking = true; 
		
		setPresent(true);
	}
	
	public void displayOrderReady(MyOrder o, int position_) {
		
		yPos =  yCook - (position_ * spaceAbove);
		xPos = xCook - 50;
		
		cooking = false;
	}
	
	public void orderPickedUp () {
		setPresent(false); 
		orderStr = ""; 
	}
	
	//draw a different string based on whether food is there or not
	public void draw(Graphics2D g) {
		g.setColor(Color.PINK);
		g.setFont(new Font("default", Font.CENTER_BASELINE, 14));
		g.drawString(orderStr, xPos, yPos);
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

}
