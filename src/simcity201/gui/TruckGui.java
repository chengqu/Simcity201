package simcity201.gui;


import agents.BusAgent;
import agents.PassengerAgent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.*;

import javax.swing.ImageIcon;

public class TruckGui implements Gui {
	
    private BusAgent agent = null;

    private int xPos = 20, yPos = 20;//default bus position
    private int xDestination = 20, yDestination = 20;//default bus position
    
    private String buspic = "bus.png";
	private Image img;
	
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
    
    

    public TruckGui(BusAgent agent, String Terminal) {
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

        

    }

    public void draw(Graphics2D g) {
    	g.drawImage(img,xPos,yPos,null);
        //g.setColor(Color.MAGENTA);
        //g.fillRect(xPos, yPos, 20, 20);
    	g.setColor(Color.red);
    	
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoTo(String dest) {
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
}
