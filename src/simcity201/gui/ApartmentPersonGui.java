package simcity201.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import agents.ApartPerson;

public class ApartmentPersonGui implements Gui{

	ApartPerson agent;
	
	private boolean present = false;
	private boolean visible = true;
	private boolean personHere = false;
	
	private int xDestination, yDestination;
	private int xPos, yPos;
	private enum Command {noCommand};
	private Command command = Command.noCommand;
	
	private int xFridge, yFridge, xEntrance, yEntrance;
	private static int fridgeHeight = 30, fridgeWidth = 50;
	private static int entranceHeight = 20, entranceWidth = 20;
	
	public ApartmentPersonGui(ApartPerson a, int xfridge, int yfridge, int xentrance, int yentrance)
	{
		agent = a;
		xFridge = xfridge;
		yFridge = yfridge;
		xEntrance = xentrance;
		yEntrance = yentrance;
		xPos = xEntrance;
		yPos = yEntrance;
	}
	
	public void updatePosition() {
		
	}
	
	public void draw(Graphics2D g) {
			g.setColor(Color.green);
			g.fillRect(xFridge, yFridge, fridgeWidth, fridgeHeight);
			g.setColor(Color.red);
			g.fillRect(xEntrance, yEntrance, entranceHeight, entranceWidth);
			if(personHere)
			{
				g.setColor(Color.black);
				g.fillRect(xPos, yPos, 20, 20);
			}
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
