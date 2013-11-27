package simcity201.gui;


import agents.BusAgent;
import agents.PassengerAgent;
import agents.TruckAgent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.*;

import javax.swing.ImageIcon;

public class TruckGui implements Gui {
	
    private TruckAgent agent = null;

    private int xPos = 570, yPos = 200;//default bus position
    private int xDestination = 570, yDestination = 200;//default bus position
    
    private String buspic = "truck.png";
	private Image img;
	
	public static final int xRest1 = 705;
    public static final int yRest1 = 405;
    
    public static final int xRest2 = 705;
    public static final int yRest2 = 445;
    
    public static final int xRest3 = 855;
    public static final int yRest3 = 405;
    
    public static final int xRest4 = 855;
    public static final int yRest4 = 445;
    
    public static final int xRest5 = 1005;
    public static final int yRest5 = 405;
    
    public static final int xRest6 = 1005;
    public static final int yRest6 = 445;
    
    public static final int xTruckCrossing1 = 570;
    public static final int yTruckCrossing1 = 445;
    
    public static final int xTruckCrossing2 = 1125;
    public static final int yTruckCrossing2 = 445;
    
    public static final int xTruckCrossing3 = 1125;
    public static final int yTruckCrossing3 = 405;
    
    public static final int xTruckCrossing4 = 605;
    public static final int yTruckCrossing4 = 405;
    
    public static final int xMarket = 570;
    public static final int yMarket = 200;
    
    

    public TruckGui(TruckAgent agent) {
        this.agent = agent;
        ImageIcon customer = new ImageIcon(this.getClass().getResource(buspic));
		img = customer.getImage();
       
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
        		& (xDestination == xMarket) & (yDestination == yMarket)) {
           //agent.msgAtDest();
           agent.msgAtMarket();
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
        		& (xDestination == xTruckCrossing1) & (yDestination == yTruckCrossing1)) {
           agent.msgAtCrossing();
        }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTruckCrossing2) & (yDestination == yTruckCrossing2)) {
           agent.msgAtCrossing();
        }

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTruckCrossing3) & (yDestination == yTruckCrossing3)) {
           agent.msgAtCrossing();
        }
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTruckCrossing4) & (yDestination == yTruckCrossing4)) {
           agent.msgAtCrossing();
        }

    }

    public void draw(Graphics2D g) {
    	g.drawImage(img,xPos,yPos,90,120,null);
        //g.setColor(Color.MAGENTA);
        //g.fillRect(xPos, yPos, 20, 20);
    	g.setColor(Color.red);
    	
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToRest(String dest) {
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
    	
    }
    
   
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void DoGoToCrossing1() {
		// TODO Auto-generated method stub
		xDestination = xTruckCrossing1;
		yDestination = yTruckCrossing1;
	}

	public void DoGoToCrossing2() {
		// TODO Auto-generated method stub
		xDestination = xTruckCrossing2;
		yDestination = yTruckCrossing2;
	}

	public void DoGoToCrossing3() {
		// TODO Auto-generated method stub
		xDestination = xTruckCrossing3;
		yDestination = yTruckCrossing3;
	}

	public void DoGoToCrossing4() {
		// TODO Auto-generated method stub
		xDestination = xTruckCrossing4;
		yDestination = yTruckCrossing4;
	}
	public void DoGoToMarket() {
		// TODO Auto-generated method stub
		xDestination = xMarket;
		yDestination = yMarket;
	}
}
