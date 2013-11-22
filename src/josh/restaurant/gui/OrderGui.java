package josh.restaurant.gui;

import java.awt.*;

import josh.restaurant.CookAgent;
import josh.restaurant.CustomerAgent;
import josh.restaurant.HostAgent;

public class OrderGui implements Gui{

	private boolean isPresent = false;

	RestaurantGui gui;
	
	private int startingXPos = 100; 
	private int xPos = 100;
	private static int yPos = 100;
	
	private static int xCook = 260;
	private static int yCook = 300;
	
	private String orderStr; 
	private String foodStr;  
	
	private int spaceBtwnTables = 80; 

	private boolean foodDelivered; 

	public OrderGui(RestaurantGui gui){ 
		this.gui = gui;
		foodDelivered = false; 
		setPresent(false);
		orderStr = "?"; 
	}
	
	public void setupOrderLoc (int tableNum) {
		
		if (tableNum > 0)
			xPos = startingXPos + ((tableNum -1) * spaceBtwnTables); 
	}
	
	public void setupOrderIcon (String str) {
		
		foodStr = str; 
		
		StringBuffer sb = new StringBuffer();
		sb.append(foodStr); 
		sb.append("?"); 
		
		orderStr = sb.toString(); 
	}
	
	public void gui_msgfoodDelivered() {
		foodDelivered = true; 
	}
	
	//draw a different string based on whether food is there or not
	public void draw(Graphics2D g) {
		g.setColor(Color.PINK);
		g.setFont(new Font("default", Font.CENTER_BASELINE, 14));
		if (foodDelivered == false) {
			g.drawString(orderStr, xPos, yPos);
		}
		else 
			g.drawString(foodStr, xPos, yPos);
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public void finishedFood () {
		setPresent(false); 
		orderStr = "?"; 
		foodDelivered = false; 
	}
	
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
}
