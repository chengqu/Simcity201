package simcity201.gui;

import java.awt.Graphics2D;

import agents.ApartmentPerson;

public class ApartmentPersonGui implements Gui{

	ApartmentPerson agent;
	
	private boolean present = false;
	private boolean visible = false;
	
	public ApartmentPersonGui(ApartmentPerson a)
	{
		agent = a;
	}
	
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
	
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
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
		return present;
	}
	
	public boolean isVisible()
	{
		return visible;
	}
}
