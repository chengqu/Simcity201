package simcity201.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

import simcity201.interfaces.BankTeller;
import agents.BankTellerAgent;

public class BankTellerGui implements Gui {

	BankTeller agent;
	
	private boolean isPresent = false;
	
	private int SIZE_TELLER_X = 40, SIZE_TELLER_Y = 40;
	
    private int xPos, yPos;
    private int xDestination, yDestination;
    
    private enum Command {noCommand, goToDest, leaveBank};
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
    private String imageFileName = "Bank Teller.png";
    BufferedImage icon;
    
    
    public BankTellerGui(BankTellerAgent agent, BankMap map) {
    	this.agent = agent;
    	this.map = map;
    	xPos = BankMap.ENTRANCE.x; yPos = BankMap.ENTRANCE.y;//default customer position
    	xDestination = BankMap.ENTRANCE.x+20; yDestination = BankMap.ENTRANCE.y-20;//default start position
    	
    	String imageCaption = "BankTeller:" +agent.getName();
    	ImageIcon temp = createImageIcon(imagedir + imageFileName, imageCaption);
    	icon = getScaledImage(temp.getImage(), SIZE_TELLER_X, SIZE_TELLER_Y);
    	
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
	    		map.positionAvailable(this);
	    		command = Command.noCommand;
	    		agent.msgAtDestination();
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

	public void setAgent(BankTeller agent) {
		this.agent = agent;
	}
	public void setMap(BankMap map) {
		this.map = map;
	}

	public void DoGoToTellerWindow() {
		Point p = map.getTellerPosition(this);
		setPresent(true);
		destinations.add(new Destination(p, Command.goToDest));
	}
	public void DoLeaveBank() {
		Point p = BankMap.ENTRANCE;
		destinations.add(new Destination(p, Command.leaveBank));
	}
}
