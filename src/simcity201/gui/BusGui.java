package simcity201.gui;


import agents.BusAgent;
import agents.PassengerAgent;

import java.awt.*;

import javax.swing.ImageIcon;

public class BusGui implements Gui {

    private BusAgent agent = null;

    private int xPos = 20, yPos = 20;//default bus position
    private int xDestination = 20, yDestination = 20;//default bus position
    
    private String buspic = "bus.png";
	private Image img;
    
	//route 1
    public static final int xBus1Crossing1 = 570;
    public static final int yBus1Crossing1 = 40;
    
    public static final int xBus1Crossing2 = 570;
    public static final int yBus1Crossing2 = 445;
    
    public static final int xBus1Crossing3 = 1165;
    public static final int yBus1Crossing3 = 445;
    
    public static final int xBus1Crossing4 = 1165;
    public static final int yBus1Crossing4 = 0;
    
    public static final int xBus1Crossing5 = 40;
    public static final int yBus1Crossing5 = 0;
    
    public static final int xBank = 300;
    public static final int yBank = 40;
    
    public static final int xMarket = 570;
    public static final int yMarket = 200;
    
    public static final int xHouse = 850;
    public static final int yHouse = 0;
    
    public static final int xRestaurants1 = 855;
    public static final int yRestaurants1 = 445;
    
    public static final int xRestaurants2 = 1165;
    public static final int yRestaurants2 = 250;
    
    public static final int xTerminal1 = 40;
    public static final int yTerminal1 = 40;
        
    //route 2
    public static final int xApart = 250;
    public static final int yApart = 445;
    
    public static final int xRestaurants3 = 570;
    public static final int yRestaurants3 = 605;
    
    public static final int xUnknow = 850;
    public static final int yUnknow = 795;
    
    public static final int xRestaurants4 = 850;
    public static final int yRestaurants4 = 405;
    
    public static final int xRestaurants5 = 250;
    public static final int yRestaurants5 = 445;
    
    public static final int xBus2Crossing1 = 570;
    public static final int yBus2Crossing1 = 445;
    
    public static final int xBus2Crossing2 = 570;
    public static final int yBus2Crossing2 = 795;
    
    public static final int xBus2Crossing3 = 1165;
    public static final int yBus2Crossing3 = 795;
    
    public static final int xBus2Crossing4 = 1165;
    public static final int yBus2Crossing4 = 405;
    
    public static final int xBus2Crossing5 = 40;
    public static final int yBus2Crossing5 = 405;
    
    public static final int xTerminal2 = 40;
    public static final int yTerminal2 = 445;
    

    public BusGui(BusAgent agent, String Terminal) {
        this.agent = agent;
        ImageIcon customer = new ImageIcon(this.getClass().getResource(buspic));
		img = customer.getImage();
        if(Terminal.equals("Terminal1")){
        	xPos = xTerminal1-1;
        	yPos = yTerminal1-1;
            xDestination = xTerminal1-1;
            yDestination = yTerminal1-1;}
    	if(Terminal.equals("Terminal2")){
    		xPos = xTerminal2-1;
    		yPos = yTerminal2-1;
            xDestination = xTerminal2-1;
            yDestination = yTerminal2-1;}
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
        		& (xDestination == xBank) & (yDestination == yBank)) {
           agent.msgAtDest();
        }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xMarket) & (yDestination == yMarket)) {
           agent.msgAtDest();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xHouse) & (yDestination == yHouse)) {
           agent.msgAtDest();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xRestaurants1) & (yDestination == yRestaurants1)) {
           agent.msgAtDest();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xRestaurants2) & (yDestination == yRestaurants2)) {
           agent.msgAtDest();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xBus1Crossing1) & (yDestination == yBus1Crossing1)) {
           agent.msgAtCrossing();
        }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xBus1Crossing2) & (yDestination == yBus1Crossing2)) {
           agent.msgAtCrossing();
        }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xBus1Crossing3) & (yDestination == yBus1Crossing3)) {
           agent.msgAtCrossing();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xBus1Crossing4) & (yDestination == yBus1Crossing4)) {
           agent.msgAtCrossing();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xBus1Crossing5) & (yDestination == yBus1Crossing5)) {
           agent.msgAtCrossing();
        }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTerminal1) & (yDestination == yTerminal1)) {
           agent.msgAtDest();
        }
       
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xBus2Crossing2) & (yDestination == yBus2Crossing2)) {
           agent.msgAtCrossing();
        }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xBus2Crossing3) & (yDestination == yBus2Crossing3)) {
           agent.msgAtCrossing();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xBus2Crossing4) & (yDestination == yBus2Crossing4)) {
           agent.msgAtCrossing();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xBus2Crossing5) & (yDestination == yBus2Crossing5)) {
           agent.msgAtCrossing();
        }
        
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xApart) & (yDestination == yApart)) {
           agent.msgAtDest();
        }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xRestaurants3) & (yDestination == yRestaurants3)) {
           agent.msgAtDest();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xUnknow) & (yDestination == yUnknow)) {
           agent.msgAtDest();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xRestaurants4) & (yDestination == yRestaurants4)) {
           agent.msgAtDest();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xRestaurants5) & (yDestination == yRestaurants5)) {
           agent.msgAtDest();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTerminal2) & (yDestination == yTerminal2)) {
           agent.msgAtDest();
        }

    }

    public void draw(Graphics2D g) {
    	g.drawImage(img,xPos,yPos,30,30,null);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoTo(String dest) {
    	if(dest.equals("Bank")){
        xDestination = xBank;
        yDestination = yBank;}
    	if(dest.equals("Market")){
            xDestination = xMarket;
            yDestination = yMarket;}
    	if(dest.equals("House")){
            xDestination = xHouse;
            yDestination = yHouse;}
    	if(dest.equals("Restaurants1")){
            xDestination = xRestaurants1;
            yDestination = yRestaurants1;}
    	if(dest.equals("Restaurants2")){
            xDestination = xRestaurants2;
            yDestination = yRestaurants2;}
    	if(dest.equals("Terminal1")){
            xDestination = xTerminal1;
            yDestination = yTerminal1;}
    	if(dest.equals("Bus1Crossing1")){
            xDestination = xBus1Crossing1;
            yDestination = yBus1Crossing1;}
    	if(dest.equals("Bus1Crossing2")){
            xDestination = xBus1Crossing2;
            yDestination = yBus1Crossing2;}
    	if(dest.equals("Bus1Crossing3")){
            xDestination = xBus1Crossing3;
            yDestination = yBus1Crossing3;}
    	if(dest.equals("Bus1Crossing4")){
            xDestination = xBus1Crossing4;
            yDestination = yBus1Crossing4;}
    	if(dest.equals("Bus1Crossing5")){
            xDestination = xBus1Crossing5;
            yDestination = yBus1Crossing5;}
    	if(dest.equals("Apart")){
            xDestination = xApart;
            yDestination = yApart;}
    	if(dest.equals("Restaurants3")){
            xDestination = xRestaurants3;
            yDestination = yRestaurants3;}
    	if(dest.equals("Unknow")){
            xDestination = xUnknow;
            yDestination = yUnknow;}
    	if(dest.equals("Restaurants4")){
            xDestination = xRestaurants4;
            yDestination = yRestaurants4;}
    	if(dest.equals("Restaurants5")){
            xDestination = xRestaurants5;
            yDestination = yRestaurants5;}
    	if(dest.equals("Bus2Crossing1")){
            xDestination = xBus1Crossing1;
            yDestination = yBus1Crossing1;}
    	if(dest.equals("Bus2Crossing2")){
            xDestination = xBus1Crossing2;
            yDestination = yBus1Crossing2;}
    	if(dest.equals("Bus2Crossing3")){
            xDestination = xBus1Crossing3;
            yDestination = yBus1Crossing3;}
    	if(dest.equals("Bus2Crossing4")){
            xDestination = xBus1Crossing4;
            yDestination = yBus1Crossing4;}
    	if(dest.equals("Bus2Crossing5")){
            xDestination = xBus1Crossing5;
            yDestination = yBus1Crossing5;}
    	if(dest.equals("Terminal2")){
            xDestination = xTerminal2;
            yDestination = yTerminal2;}
    }
    public void DoGoWait(String dest) {
    	if(dest.equals("Bank")){
        xDestination = xBank+1;
        yDestination = yBank+1;}
    	if(dest.equals("Market")){
            xDestination = xMarket+1;
            yDestination = yMarket+1;}
    	if(dest.equals("House")){
            xDestination = xHouse+1;
            yDestination = yHouse+1;}
    	if(dest.equals("Restaurants1")){
            xDestination = xRestaurants1+1;
            yDestination = yRestaurants1+1;}
    	if(dest.equals("Restaurants2")){
            xDestination = xRestaurants2+1;
            yDestination = yRestaurants2+1;}
    	if(dest.equals("Terminal1")){
            xDestination = xTerminal1+1;
            yDestination = yTerminal1+1;}
    	if(dest.equals("Terminal2")){
            xDestination = xTerminal2+1;
            yDestination = yTerminal2+1;}
    	if(dest.equals("Bus1Crossing1")){
            xDestination = xBus1Crossing1+1;
            yDestination = yBus1Crossing1+1;}
    	if(dest.equals("Bus1Crossing2")){
            xDestination = xBus1Crossing2+1;
            yDestination = yBus1Crossing2+1;}
    	if(dest.equals("Bus1Crossing3")){
            xDestination = xBus1Crossing3+1;
            yDestination = yBus1Crossing3+1;}
    	if(dest.equals("Bus1Crossing4")){
            xDestination = xBus1Crossing4+1;
            yDestination = yBus1Crossing4+1;}
    	if(dest.equals("Bus1Crossing5")){
            xDestination = xBus1Crossing5+1;
            yDestination = yBus1Crossing5+1;}
    	if(dest.equals("Bus2Crossing1")){
            xDestination = xBus1Crossing1+1;
            yDestination = yBus1Crossing1+1;}
    	if(dest.equals("Bus2Crossing2")){
            xDestination = xBus1Crossing2+1;
            yDestination = yBus1Crossing2+1;}
    	if(dest.equals("Bus2Crossing3")){
            xDestination = xBus1Crossing3+1;
            yDestination = yBus1Crossing3+1;}
    	if(dest.equals("Bus2Crossing4")){
            xDestination = xBus1Crossing4+1;
            yDestination = yBus1Crossing4+1;}
    	if(dest.equals("Bus2Crossing5")){
            xDestination = xBus1Crossing5+1;
            yDestination = yBus1Crossing5+1;}
    	if(dest.equals("Apart")){
            xDestination = xApart+1;
            yDestination = yApart+1;}
    	if(dest.equals("Restaurants3")){
            xDestination = xRestaurants3+1;
            yDestination = yRestaurants3+1;}
    	if(dest.equals("Unknow")){
            xDestination = xUnknow+1;
            yDestination = yUnknow+1;}
    	if(dest.equals("Restaurants4")){
            xDestination = xRestaurants4+1;
            yDestination = yRestaurants4+1;}
    	if(dest.equals("Restaurants5")){
            xDestination = xRestaurants5+1;
            yDestination = yRestaurants5+1;}
    }

    
   
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
