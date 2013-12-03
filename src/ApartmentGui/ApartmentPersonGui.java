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
	
	enum Command{goToBed, goToFridge, goToStove, goToTable, goToEntrance, none, gotoLivingRoom};
	Command command = Command.none;
	
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
		if(command == Command.none)
			return;
		
	      if (xPos < xDestination)
	         xPos++;
	      else if (xPos > xDestination)
	         xPos--;
	
	      if (yPos < yDestination)
	         yPos++;
	      else if (yPos > yDestination)
	         yPos--;
      
	      if(xPos == xDestination && yPos == yDestination)
	      {
	    	  if(command == Command.goToBed)
	    	  {
	    		  command = Command.none;
	    		  t.schedule(new TimerTask(){
	  		  		public void run() {
	  		  			agent.msgDoneTask();
	  		  		}
	    		  }, 6000);
	    		  return;
	    	  }
	    	  else if(command == Command.goToEntrance)
	    	  {
	    		  command = Command.none;
	    		  agent.msgDoneTask();
	    	  }
	    	  else if(command == Command.goToFridge)
	    	  {
	    		  command = Command.none;
	    		  agent.msgDoneTask();
	    	  }
	    	  else if(command == Command.goToStove)
	    	  {
	    		  command = Command.none;
	    		  t.schedule(new TimerTask()
	    		  {
	    			  public void run()
	    			  {
	    				  agent.msgDoneTask();
	    			  }
	    		  }, 6000);
	    	  }
	    	  else if(command == Command.gotoLivingRoom)
	    	  {
	    		  command = Command.none;
	    		  agent.msgDoneTask();
	    	  }
	    	  else if(command == Command.goToTable)
	    	  {
	    		  command = Command.none;
	    		  t.schedule(new TimerTask()
	    		  {
	    			  public void run()
	    			  {
	    				  agent.msgDoneTask();
	    			  }
	    		  }, 7000);
	    	  }
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
	   command = Command.goToFridge;
	}
	
	public void goToStove(){
      xDestination=stoveX;
      yDestination=stoveY;
      command = Command.goToStove;
   }
	
	public void goToTable(){
      xDestination=tableX;
      yDestination=tableY;
      command = Command.goToEntrance;
   }
	
	public void goToBed(){
      xDestination=bedX;
      yDestination=bedY;
      command = Command.goToBed;
   }
	
	public void goToLivingRoom(){
      xDestination=livingRoomX;
      yDestination=livingRoomY;
      command = Command.gotoLivingRoom;
   }
	
	public void goToEntrance()
	{
		wantToLeave = true;
		xDestination = entranceX;
		yDestination = entranceY;
		command = Command.goToEntrance;
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
