package LYN.gui;


import LYN.CustomerAgent;
import LYN.HostAgent;
import LYN.WaiterAgent;

import java.awt.*;

public class WaiterGui implements Gui {

    private WaiterAgent agent = null;
    RestaurantGui gui;

    private int yPos = 10;//default waiter position
    private int myhome;
    public int xPos;
    private int xtemp;
    public int xDestination, yDestination = 10;//default start position
    private boolean isPresent = false;
    public int xTable = 200;
    public static final int yTable = 250;

    public WaiterGui(WaiterAgent agent) {
        this.agent = agent;
    }

    public void callpause(){
		agent.pause();
	}
    
    public void callresume(){
    	agent.resume();
    }

    public WaiterGui(WaiterAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = myhome;
		yPos = 10;
		xDestination = myhome;
		yDestination = 10;
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

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable + 20) & (yDestination == yTable - 20) ) {
           agent.msgAtTable();
           
           }
        
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == 400) & (yDestination == 200) ) {
           agent.msgAtTable();
           
           }
        
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == 400) & (yDestination == 300) ) {
           agent.msgAtTable();
           
           }
           
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xtemp) & (yDestination == 50) ) {
        	xtemp = 0;
           agent.msgAtTable();
           
           }
      
       
        if (xPos == myhome && yPos == 10 & (xDestination == myhome) & (yDestination == 10)){        	
        	agent.msgAtOrigin();
        	
        } else {
        	agent.msgnotatOrigin();
        	
        }
        
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
        if (agent.a==1){
        	g.drawString("ST", 390 , 300); 
        }
        if (agent.a==2){
        	g.drawString("Ch", 403 , 300); 
        }
        if (agent.a==3){
        	g.drawString("Sa", 419 , 300); 
        }
        if (agent.a==4){
        	g.drawString("Pi", 430 , 300); 
        }
        
        if (agent.b==1){
        	g.drawString("ST", xPos + 5 , yPos); 
        }
        if (agent.b==2){
        	g.drawString("Ch", xPos + 5 , yPos); 
        }
        if (agent.b==3){
        	g.drawString("Sa", xPos + 5 , yPos); 
        }
        if (agent.b==4){
        	g.drawString("Pi", xPos + 5 , yPos); 
        }
        
    }

    public boolean isPresent() {
        return isPresent;
    }
    
   
    public void setPresent(boolean p) {
		isPresent = p;
	}


    public void DoBringToTable( int a) {
        System.out.print(a);
      
    	if (a == 1) {
    		xTable = 200;
    	xDestination = xTable + 20;
        yDestination = yTable - 20;}
        else if (a == 2){
        	xTable = 100;
        	xDestination = xTable + 20;
            yDestination = yTable - 20;
        } else {
        	xTable = 300;
        	xDestination = xTable + 20;
            yDestination = yTable - 20;
        }
    }
    
    public void DoMovetoCustomer(int y) {
    	xtemp = y;
    	xDestination = y;
    	yDestination = 50;
    }
    public void DogotoCustomer( int a) {
    	
    	if (a == 1) {
    		xTable = 200;
    	xDestination = xTable + 20;
        yDestination = yTable - 20;}
        else if (a == 2){
        	xTable = 100;
        	xDestination = xTable + 20;
            yDestination = yTable - 20;
        } else {
        	xTable = 300;
        	xDestination = xTable + 20;
            yDestination = yTable - 20;
        }
    }
    public void Dosendordertocook() {
       
    		
    	xDestination = 400;
        yDestination = 200;
        
    }
    
    public void Domovetocook() {
    	xDestination = 400;
    	yDestination = 300;
    }
    
    public void DoMoveAway() {
    	xDestination = 500;
    	yDestination = 20;
    }

    public void DoLeaveCustomer() {
        xDestination = myhome;
        yDestination = 10;
    }

    public void setXPos(int p) {
    	xPos = p;
    	myhome = p;
    }
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
