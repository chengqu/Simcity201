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
    
    public static final int xBus1Crossing1 = 570;
    public static final int yBus1Crossing1 = 40;
    
    public static final int xBus1Crossing2 = 570;
    public static final int yBus1Crossing2 = 445;
    
    public static final int xBus1Crossing3 = 1165;
    public static final int yBus1Crossing3 = 445;
    
    public static final int xBus1Crossing4 = 1165;
    public static final int yBus1Crossing4 = 0;
    
    public static final int xBus1Crossing5 = 0;
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
    
    public static final int xTerminal2 = 450;
    public static final int yTerminal2 = 350;
    

    public BusGui(BusAgent agent, String Terminal) {
        this.agent = agent;
        ImageIcon customer = new ImageIcon(this.getClass().getResource(buspic));
		img = customer.getImage();
        if(Terminal == "Terminal1"){
        	xPos = xTerminal1;
        	yPos = yTerminal1;
            xDestination = xTerminal1;
            yDestination = yTerminal1;}
    	if(Terminal == "Terminal2"){
    		xPos = xTerminal2;
    		yPos = yTerminal2;
            xDestination = xTerminal2;
            yDestination = yTerminal2;}
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
           agent.msgAtBank();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xMarket) & (yDestination == yMarket)) {
           agent.msgAtDest();
           agent.msgAtMarket();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xHouse) & (yDestination == yHouse)) {
           agent.msgAtDest();
           agent.msgAtHouse();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xRestaurants1) & (yDestination == yRestaurants1)) {
           agent.msgAtDest();
           agent.msgAtRestaurants1();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xRestaurants2) & (yDestination == yRestaurants2)) {
           agent.msgAtDest();
           agent.msgAtRestaurants2();
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
        		& (xDestination == xTerminal2) & (yDestination == yTerminal2)) {
           agent.msgAtDest();
        }
       

    }

    public void draw(Graphics2D g) {
    	g.drawImage(img,xPos,yPos,null);
    	//g.fillRect(695, 265, 80, 80);
    	//g.fillRect(695, 535, 80, 80);
    	//g.fillRect(845, 265, 80, 80);
    	//g.fillRect(845, 535, 80, 80);
    	//g.fillRect(995, 265, 80, 80);
    	//g.fillRect(995, 535, 80, 80);
    	//g.fillRect(695, 130, 80, 80);
    	//g.fillRect(845, 130, 80, 80);
    	//g.fillRect(995, 130, 80, 80);
    	//g.fillRect(200, 120, 150, 80);
    	//g.fillRect(200, 525, 150, 100);
    	//g.fillRect(400, 160, 100, 150);
    	
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoTo(String dest) {
    	if(dest == "Bank"){
        xDestination = xBank;
        yDestination = yBank;}
    	if(dest == "Market"){
            xDestination = xMarket;
            yDestination = yMarket;}
    	if(dest == "House"){
            xDestination = xHouse;
            yDestination = yHouse;}
    	if(dest == "Restaurants1"){
            xDestination = xRestaurants1;
            yDestination = yRestaurants1;}
    	if(dest == "Restaurants2"){
            xDestination = xRestaurants2;
            yDestination = yRestaurants2;}
    	if(dest == "Terminal1"){
            xDestination = xTerminal1;
            yDestination = yTerminal1;}
    	if(dest == "Terminal2"){
            xDestination = xTerminal2;
            yDestination = yTerminal2;}
    	if(dest == "Bus1Crossing1"){
            xDestination = xBus1Crossing1;
            yDestination = yBus1Crossing1;}
    	if(dest == "Bus1Crossing2"){
            xDestination = xBus1Crossing2;
            yDestination = yBus1Crossing2;}
    	if(dest == "Bus1Crossing3"){
            xDestination = xBus1Crossing3;
            yDestination = yBus1Crossing3;}
    	if(dest == "Bus1Crossing4"){
            xDestination = xBus1Crossing4;
            yDestination = yBus1Crossing4;}
    	if(dest == "Bus1Crossing5"){
            xDestination = xBus1Crossing5;
            yDestination = yBus1Crossing5;}
    }
    
   
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
