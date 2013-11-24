package LYN.gui;

import LYN.CookAgent;
import LYN.CustomerAgent;
import LYN.HostAgent;
import LYN.WaiterAgent;
import LYN.gui.RestaurantGui;

import java.awt.*;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CookGui implements Gui{

	private CookAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantGui gui;

	public int xPos, yPos,myY;
	public int xDestination, yDestination;
	

	
	public void callpause() {
		agent.pause();
	}

	 
    public void callresume(){
    	agent.resume();
    }
    public CookGui(CookAgent agent) {
        this.agent = agent;
    }

    

	public CookGui(CookAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = 399;
		yPos = 200;
		xDestination = 399;
		yDestination = 200;
		//maitreD = m;
		this.gui = gui;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		
		 if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == 400) & (yDestination == 100) ) {
	           agent.msgAtTable();
	           
	           
	           }

		 if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == 400) & (yDestination == 200) ) {
	           agent.msgAtTable();
	           xDestination = 399;
	           yDestination = 200;
	           
	           }


		 if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == 400) & (yDestination == 300) ) {
	           agent.msgAtTable();
	           
	           }
	           
	           


		
	}

	public void draw(Graphics2D g) {
		ImageIcon myIcon = new ImageIcon(this.getClass().getResource("cook.jpeg"));
		Image img1 = myIcon.getImage();
		g.drawImage(img1, xPos, yPos, 20, 20, new JPanel());
		if(agent.returnsteak()){
		g.drawString("st", 390 , 100); }
		if(agent.returnsalad()) {
		g.drawString("sa", 403, 100); }
		if(agent.returnpizza()){
		g.drawString("pi", 419 , 100); }
		if(agent.returnchicken()){
		g.drawString("ch", 430 , 100);} 
		
		
		if(agent.returnsteak1()){
			g.drawString("st", xPos+5, yPos+5); }
			if(agent.returnsalad1()) {
			g.drawString("sa",  xPos+5, yPos+5); }
			if(agent.returnpizza1()){
			g.drawString("pi", xPos+5, yPos+5); }
			if(agent.returnchicken1()){
			g.drawString("ch",  xPos+5, yPos+5);} 
		
		   
	}
	
	public void movetorefrigerator() {
		xDestination = 400;
		yDestination = 100;
	}
	
	public void movetoplaceposition() {
		xDestination = 400;
		yDestination = 300;
	}

	public void movetocookposition() {
		xDestination = 400;
		yDestination = 200;
	}
	
	public void rest() {
		xDestination = 399;
		yDestination = 200;
	}
	public boolean isPresent() {
		return isPresent;
	}

	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	
	
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
	
}
