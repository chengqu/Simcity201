package animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.Timer;

import simcity201.gui.ApartmentPersonGui;

public class ApartmentAnimationPanel extends BaseAnimationPanel implements ActionListener, MouseListener{

	private int windowWidth = 500;
	private int windowHeight = 500;
	List<myApartmentGui> apartmentGuis = new ArrayList<myApartmentGui>();
	
	int selectedApartment = 0;
	
	private final int DELAY = 8;
	
	public ApartmentAnimationPanel()
	{
		Dimension d = new Dimension(windowWidth, windowHeight);
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);
		this.setVisible(true);
		
		this.getSize();
		
		Timer timer = new Timer(DELAY, this );
    	timer.start();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		if(apartmentGuis.size() == 0)
		{
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, windowWidth, windowHeight);
			return;
		}
		for(myApartmentGui gui: apartmentGuis)
		{
			gui.gui.updatePosition();
		}
		
		for(myApartmentGui gui: apartmentGuis)
		{
			if(gui.apartmentNumber == selectedApartment)
			{
				if(gui.gui.isPresent())
				{
					gui.gui.draw(g2);
				}
			}
		}
	}
	
	public void addGui(ApartmentPersonGui g)
	{
		apartmentGuis.add(new myApartmentGui(g, apartmentGuis.size() + 1));
	}
	
	public void removeGui(ApartmentPersonGui g)
	{
		for(myApartmentGui gui: apartmentGuis)
		{
			if(gui.gui.equals(g))
			{
				apartmentGuis.remove(gui);
				return ;
			}
		}
	}
	
	public Dimension getSize() {
		return new Dimension(windowWidth, windowHeight);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		this.repaint();
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
	
	private class myApartmentGui{
		public ApartmentPersonGui gui = null;
		public int apartmentNumber;
		public myApartmentGui(ApartmentPersonGui g, int number)
		{
			gui = g;
			apartmentNumber = number;
		}
	}
}
