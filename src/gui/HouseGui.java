package gui;



import java.awt.*;

import javax.swing.ImageIcon;

import agents.HousePerson;

public class HouseGui implements Gui{

	private HousePerson agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	HousePanelGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, MovingToFridge,MovingToCookingArea, MovingToTable, MovingToRestPlace,TV};
	private Command command=Command.noCommand;

	public static final int xFridge = 200;
	public static final int yFridge = 10;
	public static final int xCooking = 10;
	public static final int yCooking= 55;
	public static final int xRest = 350;
	public static final int yRest = 175;
	public static final int xTable = 50;
	public static final int yTable = 90;
	public static final int xBed = 510;
	public static final int yBed = 60;
	

	public HouseGui(HousePerson c, HousePanelGui gui){ //HostAgent m) {
		agent = c;
		xPos = 350;
		yPos = 390;
		xDestination = 350;
		yDestination = 390;
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
			if (command==Command.MovingToFridge || command==Command.MovingToCookingArea || command == Command.MovingToRestPlace || command == Command.MovingToTable) 
				agent.msgAtTable();
			

			command=Command.noCommand;
			
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 350 & yDestination == 275) {
			MoveToFridge2();
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 260 & yDestination == 275) {
			MoveToFridge3();
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 260 & yDestination == 55) {
			MoveToFridge4();
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 50 & yDestination == 55) {
			MoveToRest1();
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 260 & yDestination == 54) {
			MoveToRest2();
		}

		if (xPos == xDestination && yPos == yDestination & xDestination == 260 & yDestination == 274) {
			MoveToRest3();
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 420 & yDestination == 274) {
			MoveToRest4();
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 420 & yDestination == 175) {
			MoveToRest5();
		}
	}

	public void draw(Graphics2D g) {
		
		ImageIcon myIcon = new ImageIcon(this.getClass().getResource("human.jpg"));
		Image img1 = myIcon.getImage();
		g.drawImage(img1, xPos, yPos, 20, 20,  gui);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		
		agent.msgIameatingathome();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void doMoveToFridge(){ //later you will map seatnumber to table coordinates.
		xDestination = 350;
		yDestination = 275;
		
	}
	
	private void MoveToFridge2() {
		xDestination = 260;
		yDestination = 275;
		//command = Command.MovingToFridge;
	}
	
	private void MoveToFridge3() {
		xDestination = 260;
		yDestination = 55;
		//command = Command.MovingToFridge;
	}
	
	private void MoveToFridge4() {
		xDestination = 75;
		yDestination = 55;
		command = Command.MovingToFridge;
	}

	public void doMoveToCookingArea() {
		xDestination = xCooking;
		yDestination = yCooking;
		command = Command.MovingToCookingArea;
	}
	public void doMovetoTable() {
		xDestination = xTable;
		yDestination = yTable;
		command = Command.MovingToTable;
	}
	
	public void doMoveToRestPlace() {
		xDestination = 50;
		yDestination = 55;
		
	}
	
	private void MoveToRest1() {
		xDestination = 260;
		yDestination = 54;
	}
	
	private void MoveToRest2() {
		
		xDestination = 260;
		yDestination = 274;
	}
	
	private void MoveToRest3() {
		
		xDestination = 420;
		yDestination = 274;
	}
	
	private void MoveToRest4() {
		
		xDestination = 420;
		yDestination = 175;
	}
	
	private void MoveToRest5() {
		
		xDestination = xRest;
		yDestination = yRest;
		command = Command.MovingToRestPlace;
	}
	
	public void doMoveToBed() {
		xDestination = xBed;
		yDestination = yBed;
	}
	
	
}
