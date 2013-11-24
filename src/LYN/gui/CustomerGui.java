package LYN.gui;

import LYN.CustomerAgent;
import LYN.HostAgent;

import java.awt.*;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CustomerGui implements Gui{

	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantGui gui;

	public int xPos, yPos,myY;
	public int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, gotoCashier,LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int xTable = 200;
	public static final int yTable = 250;
	
	public void callpause() {
		agent.pause();
	}

	 
    public void callresume(){
    	agent.resume();
    }
    
   public void setYpos(int p) {
	   myY = p;
   }

	public CustomerGui(CustomerAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = myY;
		yPos = 30;
		xDestination = myY;
		yDestination = 30;
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command == Command.gotoCashier) {
				agent.msgArriveatcashier();
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
		
		if(xPos == xDestination && yPos == yDestination 
				&xDestination == myY & yDestination == 30) {
				agent.gotHungry();
				xDestination = myY-1;
				yDestination = 29;
		}
	}

	public void draw(Graphics2D g) {
		ImageIcon myIcon = new ImageIcon(this.getClass().getResource("DSC_0870.JPG"));
		Image img1 = myIcon.getImage();
		g.drawImage(img1, xPos, yPos, 20, 20, new JPanel());
	     
		if( agent.getstate()== 1) {
		g.drawString(agent.getchoice() + "?", xPos +30 , yPos + 25);}
		
		if( agent.getstate() == 2){
	 
	    
			g.drawString(agent.getchoice(), xPos +30 , yPos + 25);
		}
		   
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		xDestination = myY;
		yDestination = 30;
		
		//agent.gotHungry();
		
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		if (seatnumber == 1) {
		xDestination = xTable;
		yDestination = yTable;
		command = Command.GoToSeat;}
		else if (seatnumber == 2) {
			xDestination = 100;
			yDestination = yTable;
			command = Command.GoToSeat;
		} else {
			xDestination = 300;
			yDestination = yTable;
			command = Command.GoToSeat;
		}
	}

	public void DoExitRestaurant() {
		xDestination = -20;
		yDestination = -20;
		command = Command.LeaveRestaurant;
	}
	
	public void gotocashier() {
		xDestination = 50;
		yDestination = 50;
		command = Command.gotoCashier;
	}
	
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
	
}
