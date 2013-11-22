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
    
    public static final int xCrossing1 = 570;
    public static final int yCrossing1 = 40;
    
    public static final int xCrossing2 = 570;
    public static final int yCrossing2 = 405;
    
    public static final int xCrossing3 = 40;
    public static final int yCrossing3 = 405;
    
    public static final int xBank = 300;
    public static final int yBank = 40;
    
    public static final int xMarket = 570;
    public static final int yMarket = 200;
    
    public static final int xHouse = 300;
    public static final int yHouse = 405;
    
    public static final int xRest1 = 40;
    public static final int yRest1 = 300;
    
    public static final int xRest2 = 40;
    public static final int yRest2 = 150;
    
    public static final int xRest3 = 0;
    public static final int yRest3 = 0;
    
    public static final int xRest4 = 0;
    public static final int yRest4 = 0;
    
    public static final int xRest5 = 0;
    public static final int yRest5 = 0;
    
    public static final int xRest6 = 0;
    public static final int yRest6 = 0;
    
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
        		& (xDestination == xRest1) & (yDestination == yRest1)) {
           agent.msgAtDest();
           agent.msgAtRest1();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xRest2) & (yDestination == yRest2)) {
           agent.msgAtDest();
           agent.msgAtRest2();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xRest3) & (yDestination == yRest3)) {
           agent.msgAtDest();
           agent.msgAtRest3();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xRest4) & (yDestination == yRest4)) {
           agent.msgAtDest();
           agent.msgAtRest4();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xRest5) & (yDestination == yRest5)) {
           agent.msgAtDest();
           agent.msgAtRest5();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xRest6) & (yDestination == yRest6)) {
           agent.msgAtDest();
           agent.msgAtRest6();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTerminal1) & (yDestination == yTerminal1)) {
           agent.msgAtDest();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTerminal2) & (yDestination == yTerminal2)) {
           agent.msgAtDest();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xCrossing1) & (yDestination == yCrossing1)) {
           agent.msgAtDest();
        }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xCrossing2) & (yDestination == yCrossing2)) {
           agent.msgAtDest();
        }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xCrossing3) & (yDestination == yCrossing3)) {
           agent.msgAtDest();
        }

    }

    public void draw(Graphics2D g) {
    	g.drawImage(img,xPos,yPos,null);
        //g.setColor(Color.MAGENTA);
        //g.fillRect(xPos, yPos, 20, 20);
    	g.setColor(Color.red);
    	g.fillRect(xBank+15, yBank+30, 10, 10);
    	g.drawString("Bank", xBank, yBank+50);
    	g.fillRect(xMarket-15, yMarket, 10, 10);
    	g.drawString("Market", xMarket-50, yMarket);
    	g.fillRect(xHouse+15, yHouse-20, 10, 10);
    	g.drawString("House", xHouse, yHouse-20);
    	g.fillRect(xRest1+50, yRest1, 10, 10);
    	g.drawString("Rest1", xRest1+50, yRest1);
    	g.fillRect(xRest2+50, yRest2, 10, 10);
    	g.drawString("Rest2", xRest2+50, yRest2);
    	g.fillRect(200, 795, 30, 30);
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
    	if(dest == "Rest1"){
            xDestination = xRest1;
            yDestination = yRest1;}
    	if(dest == "Rest2"){
            xDestination = xRest2;
            yDestination = yRest2;}
    	if(dest == "Rest3"){
            xDestination = xRest3;
            yDestination = yRest3;}
    	if(dest == "Rest4"){
            xDestination = xRest4;
            yDestination = yRest4;}
    	if(dest == "Rest5"){
            xDestination = xRest5;
            yDestination = yRest5;}
    	if(dest == "Rest6"){
            xDestination = xRest6;
            yDestination = yRest6;}
    	if(dest == "Terminal1"){
            xDestination = xTerminal1;
            yDestination = yTerminal1;}
    	if(dest == "Terminal2"){
            xDestination = xTerminal2;
            yDestination = yTerminal2;}
    	if(dest == "Crossing1"){
            xDestination = xCrossing1;
            yDestination = yCrossing1;}
    	if(dest == "Crossing2"){
            xDestination = xCrossing2;
            yDestination = yCrossing2;}
    	if(dest == "Crossing3"){
            xDestination = xCrossing3;
            yDestination = yCrossing3;}
    	
    }
    
   
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
