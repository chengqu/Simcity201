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

import agents.BankSecurityAgent;
import simcity201.interfaces.BankSecurity;


public class BankSecurityGui implements Gui {

	BankSecurity agent;
	
	private boolean isPresent = false;
	
	private int SIZE_Security_X = 40, SIZE_Security_Y = 40;
	
    private int xPos, yPos;
    private int xDestination, yDestination;
    
    private enum Command {noCommand, goToDest, leaveBank, patrolStart, patrolRight, patrolLeft};
    private Command command = Command.noCommand;
    
    class Destination {
    	Point p;
    	Command c;
    	public Destination(Point p, Command c) {
    		this.p = p;
    		this.c = c;
    	}
    }
    
    private List<Destination> destinations = new ArrayList<Destination>();
    
    private BankMap map;
    
    
    private String imagedir = "/animation/";
    private String imageFileName = "Bank_Security.png";
    BufferedImage icon;
    
    
    public BankSecurityGui(BankSecurityAgent agent, BankMap map) {
    	this.agent = agent;
    	this.map = map;
    	xPos = BankMap.ENTRANCE.x; yPos = BankMap.ENTRANCE.y;//default customer position
    	xDestination = BankMap.ENTRANCE.x+20; yDestination = BankMap.ENTRANCE.y-20;//default start position
    	
    	String imageCaption = "BankSecurity:" +agent.getName();
    	ImageIcon temp = createImageIcon(imagedir + imageFileName, imageCaption);
    	icon = getScaledImage(temp.getImage(), SIZE_Security_X, SIZE_Security_Y);
    	
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
	    	if (command==Command.leaveBank) {
	    		isPresent = false;
	    		command = Command.noCommand;
	    		agent.msgAtDestination();
	    	}
	    	if (command == Command.patrolStart) {
	    		command = Command.patrolRight;
	    		agent.msgAtDestination();
	    	}
	    	if (command == Command.patrolRight) {
	    		Point p = map.getSecurityPatrolPosition();
	    		p.x = p.x - 50;
	    		destinations.add(new Destination(p, Command.patrolLeft));
	    	}
	    	if (command == Command.patrolLeft) {
	    		Point p = map.getSecurityPatrolPosition();
	    		p.x = p.x + 50;
	    		destinations.add(new Destination(p, Command.patrolRight));
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
		if (isPresent) {
			g.setColor(Color.MAGENTA);
	        //g.fillRect(xPos, yPos, SIZE_TELLER_X, SIZE_TELLER_Y);
			g.drawImage(icon, xPos, yPos, null);
		}
        
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	public void setPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}

	public void setAgent(BankSecurity agent) {
		this.agent = agent;
	}
	public void setMap(BankMap map) {
		this.map = map;
	}

	public void DoGoOnDuty() {
		setPresent(true);
		Point p = map.getSecurityPatrolPosition();
		destinations.add(new Destination(p, Command.patrolStart));
	}

}
