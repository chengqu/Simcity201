package simcity201.gui;

import java.awt.Graphics2D;

import agents.ApartmentOwner;

public class ApartmentOwnerGui implements Gui{

	ApartmentOwner agent;
	
	public ApartmentOwnerGui(ApartmentOwner owner)
	{
		agent = owner;
	}
	
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

}
