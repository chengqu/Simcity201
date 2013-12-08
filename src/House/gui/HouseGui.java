package House.gui;



import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import House.gui.Gui;
import House.agents.HousePerson;

public class HouseGui implements Gui{

	private HousePerson agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	HousePanelGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, MovingToFridge,MovingToCookingArea, MovingToStore,MovingToTable, MovingToRestPlace,TV,MovingToLapTop, MovingTobed};
	private Command command=Command.noCommand;

	public static final int xFridge = 210;
	public static final int yFridge = 10;
	public static final int xCooking = 15;
	public static final int yCooking= 60;
	public static final int xRest = 400;
	public static final int yRest = 190;
	public static final int xTable = 60;
	public static final int yTable = 105;
	public static final int xBed = 560;
	public static final int yBed = 65;
	public static final int xStore = 520;
	public static final int yStore = 60;
	public static final int xLapTop = 500;
	public static final int yLapTop = 390;
	private boolean drawLapTop = false;
	

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
			if (command==Command.MovingToFridge || command==Command.MovingToCookingArea || command == Command.MovingToRestPlace || 
					command == Command.MovingToTable||command == Command.MovingToLapTop || command == Command.MovingTobed || command == Command.MovingToStore) 
				agent.msgAtTable();
			

			command=Command.noCommand;
			
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 350 & yDestination == 300) {
			MoveToFridge2();
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 280 & yDestination == 300) {
			MoveToFridge3();
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 280 & yDestination == 60) {
			MoveToFridge4();
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 60 & yDestination == 60) {
			MoveToRest1();
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 280 & yDestination == 59) {
			MoveToRest2();
		}

		if (xPos == xDestination && yPos == yDestination & xDestination == 280 & yDestination == 299) {
			MoveToRest3();
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 470 & yDestination == 299) {
			MoveToRest4();
		}
		
		if (xPos == xDestination && yPos == yDestination & xDestination == 470 & yDestination == 190) {
			MoveToRest5();
		}
	}

	public void draw(Graphics2D g) {
		
		ImageIcon myIcon = new ImageIcon(this.getClass().getResource("human.jpg"));
		Image img1 = myIcon.getImage();
		g.drawImage(img1, xPos, yPos, 20, 20,  new JPanel());
		
		if(drawLapTop == true) {
			ImageIcon myIcon1 = new ImageIcon(this.getClass().getResource("laptop_256.png"));
			Image img2 = myIcon1.getImage();
			g.drawImage(img2, xLapTop+30, yPos, 20, 20,  new JPanel());
		}
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
		yDestination = 300;
		
	}
	
	public void doMoveToStore() {
		xDestination = xStore;
		yDestination = yStore;
		command = Command.MovingToStore;
	}
	
	private void MoveToFridge2() {
		xDestination = 280;
		yDestination = 300;
		//command = Command.MovingToFridge;
	}
	
	private void MoveToFridge3() {
		xDestination = 280;
		yDestination = 60;
		//command = Command.MovingToFridge;
	}
	
	private void MoveToFridge4() {
		xDestination = 80;
		yDestination = 60;
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
		xDestination = 60;
		yDestination = 60;
		
	}
	
	private void MoveToRest1() {
		xDestination = 280;
		yDestination = 59;
	}
	
	private void MoveToRest2() {
		
		xDestination = 280;
		yDestination = 299;
	}
	
	private void MoveToRest3() {
		
		xDestination = 470;
		yDestination = 299;
	}
	
	private void MoveToRest4() {
		
		xDestination = 470;
		yDestination = 190;
	}
	
	private void MoveToRest5() {
		
		xDestination = xRest;
		yDestination = yRest;
		command = Command.MovingToRestPlace;
	}
	
	public void doMoveToBed() {
		xDestination = xBed;
		yDestination = yBed;
		command = Command.MovingTobed;
	}
	
	public void doMovetoLapTop() {
		xDestination = xLapTop;
		yDestination = yLapTop;
		command = Command.MovingToLapTop;
	}
	
	public void drawLapTop() {
		drawLapTop = true;
	}
	
	public void stopdrawLapTop() {
		drawLapTop = false;
	}

	@Override
	public void callpause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callresume() {
		// TODO Auto-generated method stub
		
	}
}
