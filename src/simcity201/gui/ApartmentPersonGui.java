package simcity201.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import agents.ApartmentPerson;

public class ApartmentPersonGui implements Gui{

	ApartmentPerson agent;
	
	private boolean present = false;
	private boolean visible = true;
	private boolean personHere = false;
	
	private int xDestination, yDestination;
	private enum Command {noCommand};
	private Command command = Command.noCommand;
	
	public ApartmentPersonGui(ApartmentPerson a)
	{
		agent = a;
	}
	
	public void updatePosition() {
		
	}
	
	public void draw(Graphics2D g) {
			g.setColor(Color.green);
			g.drawRect(100, 100, 100, 100);
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
