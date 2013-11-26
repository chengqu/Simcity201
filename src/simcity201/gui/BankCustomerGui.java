package simcity201.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import simcity201.interfaces.BankCustomer;
import agents.BankCustomerAgent;
import agents.BankTellerAgent;

public class BankCustomerGui implements Gui {

	private int SIZE_CUSTOMER_X =40, SIZE_CUSTOMER_Y = 40;
	
    private int xPos = BankMap.ENTRANCE.x-30, yPos = BankMap.ENTRANCE.y+30;//default customer position
    private int xDestination = BankMap.ENTRANCE.x, yDestination = BankMap.ENTRANCE.y;//default start position

    private enum Command {noCommand, goToDest, backToCounter, nothingToDo};
    private Command command = Command.noCommand;
    
    private class Destination {
    	Point p;
    	Command c;
    	Destination(Point p, Command c) {
    		this.p = p;
    		this.c = c;
    	}
    }
    
    private List<Destination> destinations = new ArrayList<Destination>();
    
    BankCustomer agent;
    
    private BankMap map;
    
    
    private String imagedir = "/animation/";
    private String imageFileName = "Bank_Customer.png";
    BufferedImage icon;
    
    
    public  BankCustomerGui(BankCustomerAgent agent, BankMap map) {
    	this.agent = agent;
    	this.map = map;
    	
    	String imageCaption = "BankCustomer:" +agent.getName();
    	ImageIcon temp = createImageIcon(imagedir + imageFileName, imageCaption);
    	icon = getScaledImage(temp.getImage(), SIZE_CUSTOMER_X, SIZE_CUSTOMER_Y);
    }
    
    protected ImageIcon createImageIcon(String path, String description) {
    	java.net.URL imgURL = getClass().getResource(path);
    	//System.out.println(getClass().getResource(path));
    	if(imgURL != null) {
    		return new ImageIcon(imgURL, description);
    	}else {
    		// could not find file
    		//System.out.println("\n\n\nCANNOT FIND THE IMAGE\n\n\n");
    		return null;
    	}
    }
    private BufferedImage getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    
    
	@Override
	public void updatePosition() {
		
		if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination) {
        	// Animation has arrived at the destination
        	
        	if (command==Command.goToDest) {
        		agent.msgAtDestination();
        		command = Command.noCommand;
        	}
        	
           if (!destinations.isEmpty()) {
        	   Destination dest = destinations.remove(0);
        	   xDestination = (int)dest.p.getX();
        	   yDestination = (int)dest.p.getY();
        	   command = dest.c;
           }
        }
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
        //g.fillRect(xPos, yPos, SIZE_CUSTOMER_X, SIZE_CUSTOMER_Y);
		
		g.drawImage(icon, xPos, yPos, null);
	}

	@Override
	public boolean isPresent() {
		
		return true;
	}

	public void setAgent(BankCustomer agent) {
		this.agent = agent;
	}
	public void setMap(BankMap map) {
		this.map = map;
	}


	public void DoGoToLine() {
		Point p = map.getCustomerPosition(this);
		destinations.add(new Destination(p, Command.goToDest));
	}


	public void DoApproachTeller(BankTellerAgent teller) {
		Point p = map.getTellerWindow(teller.getGui());
		destinations.add(new Destination(p, Command.goToDest));
		map.positionAvailable(this);
	}
	
	public void DoLeaveBank() {
		destinations.add(new Destination(new Point(BankMap.ENTRANCE.x, BankMap.ENTRANCE.y), Command.goToDest));
	}
}
