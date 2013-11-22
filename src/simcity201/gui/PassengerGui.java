package simcity201.gui;

import agents.PassengerAgent;

import java.awt.*;

import javax.swing.ImageIcon;

public class PassengerGui implements Gui{

	private PassengerAgent agent = null;
	private boolean isPresent = true;
	private boolean isHungry = false;
	private boolean isOnbus = false;
	//private HostAgent host

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	
	private String passengerpic = "passenger.png";
	private Image img;
	
	public int xCar;
	public int yCar;
	
	public static final int xBank = 300;
    public static final int yBank = 45;
    
    public static final int xMarket = 575;
    public static final int yMarket = 200;
    
    public static final int xHouse = 300;
    public static final int yHouse = 410;
    
    public static final int xRest1 = 45;
    public static final int yRest1 = 300;
    
    public static final int xRest2 = 45;
    public static final int yRest2 = 150;
	    
	    public static final int xRest3 = 0;
	    public static final int yRest3 = 0;
	    
	    public static final int xRest4 = 0;
	    public static final int yRest4 = 0;
	    
	    public static final int xRest5 = 0;
	    public static final int yRest5 = 0;
	    
	    public static final int xRest6 = 0;
	    public static final int yRest6 = 0;
	    
	    public static final int xTerminal1 = 20;
	    public static final int yTerminal1 = 20;
	    
	    public static final int xTerminal2 = 450;
	    public static final int yTerminal2 = 350;
	    
	    public static final int xTable = 200;
	    public static final int yTable = 250;

	public PassengerGui(PassengerAgent c){ //HostAgent m) {
		agent = c;
		xPos = xBank;
		yPos = yBank;
		xDestination = xBank;
		yDestination = yBank;
		ImageIcon customer = new ImageIcon(this.getClass().getResource(passengerpic));
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

		if (xPos == xDestination && yPos == yDestination) {
			
			command=Command.noCommand;
		}
		 if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == xCar) & (yDestination == yCar)) {
	          agent.msgAtCar();
	        }
	}

	public void draw(Graphics2D g) {
		if(isOnbus == false){
			g.drawImage(img,xPos,yPos,null);}
		//g.setColor(Color.GREEN);
		//g.fillRect(xPos, yPos, 20, 20);}
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		xDestination = xTable;
		yDestination = yTable;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	public void DoGoToCar(int x, int y){
		xDestination = x;
		yDestination = y;
		this.xCar = x;
		this.yCar = y;
	}
	public void DoGoToStop(String dest){
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
	}

	public void hide() {
		// TODO Auto-generated method stub
		isOnbus = true;
	}

	public void show(String dest) {
		// TODO Auto-generated method stub
		isOnbus = false;
		if(dest == "Bank"){
	        xDestination = xBank;
	        yDestination = yBank;
	        xPos = xBank;
            yPos = yBank;
			}
	    	if(dest == "Market"){
	            xDestination = xMarket;
	            yDestination = yMarket;
	            xPos = xMarket;
	            yPos = yMarket;
	            }
	    	if(dest == "House"){
	            xDestination = xHouse;
	            yDestination = yHouse;
	            xPos = xHouse;
	            yPos = yHouse;
	            }
	    	if(dest == "Rest1"){
	            xDestination = xRest1;
	            yDestination = yRest1;
	            xPos = xRest1;
	            yPos = yRest1;
	            }
	    	if(dest == "Rest2"){
	            xDestination = xRest2;
	            yDestination = yRest2;
	            xPos = xRest2;
	            yPos = yRest2;
	            }
	    	if(dest == "Rest3"){
	            xDestination = xRest3;
	            yDestination = yRest3;
	            xPos = xRest3;
	            yPos = yRest3;
	            }
	    	if(dest == "Rest4"){
	            xDestination = xRest4;
	            yDestination = yRest4;
	            xPos = xRest4;
	            yPos = yRest4;
	            }
	    	if(dest == "Rest5"){
	            xDestination = xRest5;
	            yDestination = yRest5;
	            xPos = xRest5;
	            yPos = yRest5;
	            }
	    	if(dest == "Rest6"){
	            xDestination = xRest6;
	            yDestination = yRest6;
	            xPos = xRest6;
	            yPos = yRest6;
	    	}
	}
}
