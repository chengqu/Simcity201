package animation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.Timer;

import simcity201.gui.ApartmentOwnerGui;
import simcity201.gui.ApartmentRenterGui;

public class ApartmentAnimationPanel extends BaseAnimationPanel implements ActionListener, MouseListener{

	private int windowWidth = 500;
	private int windowHeight = 500;
	List<ApartmentRenterGui> renterGuis = new ArrayList<ApartmentRenterGui>();
	List<ApartmentOwnerGui> ownerGuis = new ArrayList<ApartmentOwnerGui>();
	
	private final int DELAY = 8;
	
	public ApartmentAnimationPanel()
	{
		this.setSize(new Dimension(windowWidth, windowHeight));
		this.setVisible(true);
		
		Timer timer = new Timer(DELAY, this );
    	timer.start();
	}
	
	public void paintComponent(Graphics g)
	{
		for(ApartmentRenterGui gui: renterGuis)
		{
			if(gui.isPresent())
			{
				gui.updatePosition();
			}
		}
		for(ApartmentOwnerGui gui: ownerGuis)
		{
			if(gui.isPresent())
			{
				gui.updatePosition();
			}
		}
		
		for(ApartmentRenterGui gui: renterGuis)
		{
			if(gui.isPresent())
			{
				gui.draw((Graphics2D)g);
			}
		}
		for(ApartmentOwnerGui gui: ownerGuis)
		{
			if(gui.isPresent())
			{
				gui.draw((Graphics2D)g);
			}
		}
	}
	
	public void addGui(ApartmentRenterGui g)
	{
		renterGuis.add(g);
	}
	
	public void addGui(ApartmentOwnerGui g)
	{
		ownerGuis.add(g);
	}
	
	public void removeGui(ApartmentRenterGui g)
	{
		renterGuis.remove(g);
	}
	
	public void removeGui(ApartmentOwnerGui g)
	{
		ownerGuis.remove(g);
	}
	
	public Dimension getSize() {
		return new Dimension(windowWidth, windowHeight);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
