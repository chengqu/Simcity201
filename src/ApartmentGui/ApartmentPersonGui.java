package ApartmentGui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import simcity201.gui.Gui;
import agents.ApartPerson;
import agents.ApartmentPerson;

public class ApartmentPersonGui implements Gui{

	ApartmentPerson agent;
	
	private int xPos, yPos;
   private int xDestination, yDestination;
    
	private boolean present = false;
	private boolean visible = true;
	private boolean personHere = false;
	
	private ApartmentAnimationPanel gui;

	private enum Command {noCommand};
	private Command command = Command.noCommand;
	
//	private int xFridge, yFridge, xEntrance, yEntrance;
	private int entranceX=235;
	private int entranceY=420;
	private int fridgeX=335;
	private int fridgeY=235;
	private int stoveX=325;
	private int stoveY=200;
	private int tableX=95;
	private int tableY=315;
	private int livingRoomX=320;
	private int livingRoomY=380;
	private int bedX=110;
	private int bedY=40;
	
	boolean wantToLeave = false;
	
	Timer t = new Timer();
	
	private boolean flag=false; //BOOLEAN TO CHECK IF GUI IS AT LIVING ROOM POSITION (DEFAULT REST SPOT)
	
	private static int fridgeHeight = 30, fridgeWidth = 50;
	private static int entranceHeight = 20, entranceWidth = 20;
	
	public ApartmentPersonGui(ApartmentPerson a, ApartmentAnimationPanel gui)
	{
		agent = a;
		xPos=entranceX;
		yPos=entranceY;
		xDestination=235;
		yDestination=420;
		this.gui=gui;
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
            & (xDestination == fridgeX) & (yDestination == fridgeY)) {
    	  t.schedule(new TimerTask(){
    		  		public void run() {
    		  			agent.msgDoneTask();
    		  		}
    		  	}, 3000);
           return;
        }
      
      if (xPos == xDestination && yPos == yDestination
              & (xDestination == entranceX) & (yDestination == entranceY) && wantToLeave) {
	      	 agent.msgDoneTask();
	      	 wantToLeave = false;
             return;
          }
      
      if (xPos == xDestination && yPos == yDestination
            & (xDestination == stoveX) & (yDestination == stoveY)) {
    	  t.schedule(new TimerTask(){
		  		public void run() {
		  			agent.msgDoneTask();
		  		}
		  	}, 3000);
    	  return;
        }
      
      if (xPos == xDestination && yPos == yDestination
            & (xDestination == tableX) & (yDestination == tableY)) {
    	  t.schedule(new TimerTask(){
		  		public void run() {
		  			agent.msgDoneTask();
		  		}
		  	}, 3000);
    	  return;
        }
      
      if (xPos == xDestination && yPos == yDestination
            & (xDestination == bedX) & (yDestination == bedY)) {
    	  t.schedule(new TimerTask(){
		  		public void run() {
		  			agent.msgDoneTask();
		  		}
		  	}, 6000);
    	  return;
        }
      
      if (xPos == xDestination && yPos == yDestination
            & (xDestination == livingRoomX) & (yDestination == livingRoomY)) {
    	  agent.msgDoneTask();
           flag=false;
        }
      
      if(xPos!=livingRoomX && yPos!=livingRoomY){
         flag=true;
      }
		
	}
	
	public void draw(Graphics2D g) {
//			g.setColor(Color.green);
//			g.drawRect(xFridge, yFridge, fridgeWidth, fridgeHeight);
//			g.setColor(Color.red);
//			g.drawRect(xEntrance, yEntrance, entranceHeight, entranceWidth);
			if(personHere)
			{
				//draw person 
			  ImageIcon myIcon = new ImageIcon(this.getClass().getResource("person2.png"));
		      Image img1 = myIcon.getImage();
		      g.drawImage(img1, xPos, yPos, 50, 50, gui);
			   
			}
	}
	
	public void goToFridge(){
	   xDestination=fridgeX;
	   yDestination=fridgeY;
	}
	
	public void goToStove(){
      xDestination=stoveX;
      yDestination=stoveY;
   }
	
	public void goToTable(){
      xDestination=tableX;
      yDestination=tableY;
   }
	
	public void goToBed(){
      xDestination=bedX;
      yDestination=bedY;
   }
	
	public void goToLivingRoom(){
      xDestination=livingRoomX;
      yDestination=livingRoomY;
   }
	
	public void goToEntrance()
	{
		wantToLeave = true;
		xDestination = entranceX;
		yDestination = entranceY;
	}
	
	public void personArrived()
	{
		personHere = true;
	}
	
	public void personLeft()
	{
		personHere = false;
	}

	public void setPresent(boolean b)
	{
		present = b;
	}
	
	public void setVisible(boolean b)
	{
		visible = b;
	}
	public boolean isPresent() {
		return true;
	}
	
	public boolean isVisible()
	{
		return true;
	}
}
