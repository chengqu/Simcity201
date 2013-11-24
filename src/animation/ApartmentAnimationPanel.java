package animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

import javax.swing.JButton;
import javax.swing.Timer;

import simcity201.gui.ApartmentPersonGui;

public class ApartmentAnimationPanel extends BaseAnimationPanel implements ActionListener, MouseListener{

	private int windowWidth = 500;
	private int windowHeight = 500;
	List<myApartmentGui> apartmentGuis = new ArrayList<myApartmentGui>();
	
	JButton nextApartment = new JButton();
	JButton previousApartment = new JButton();
	
	int selectedApartment = 0;
	
	private final int DELAY = 8;
	
	public ApartmentAnimationPanel()
	{
		Dimension d = new Dimension(windowWidth, windowHeight);
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);
		
		this.getSize();
		
		Timer timer = new Timer(DELAY, this );
    	timer.start();
    	
    	nextApartment.setName("Next");
    	nextApartment.setText("Next");
    	previousApartment.setName("Previous");
    	previousApartment.setText("Previous");
    	
    	this.setLayout(new FlowLayout(FlowLayout.CENTER));
    	this.add(nextApartment);
    	this.add(previousApartment);
    	
    	nextApartment.addActionListener(this);
    	previousApartment.addActionListener(this);
    	
    	nextApartment.setEnabled(true);
    	nextApartment.setVisible(true);
    	previousApartment.setEnabled(true);
    	previousApartment.setVisible(true);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		if(apartmentGuis.size() == 0)
		{
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, windowWidth, windowHeight);
			g2.setColor(Color.white);
			g2.drawString("Empty Apartment Complex", 100, 100);
			return;
		}
		g2.setColor(Color.BLACK);
		g2.drawString("Apartment: " + Integer.toString(selectedApartment + 1), 10, 10);
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
		apartmentGuis.add(new myApartmentGui(g, apartmentGuis.size()));
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

	
	public void actionPerformed(ActionEvent arg0) {
		JButton button = null;
		if(arg0.getSource().getClass().equals(JButton.class))
		{
			button = (JButton)arg0.getSource();
		}
		if(button != null)
		{
			if(button.getName().equals("Next"))
			{
				selectedApartment++;
				if(selectedApartment >= apartmentGuis.size())
				{
					selectedApartment = 0;
				}
			}
			else if(button.getName().equals("Previous"))
			{
				selectedApartment--;
				if(selectedApartment < 0)
				{
					selectedApartment = apartmentGuis.size() - 1;
				}
			}
		}
		this.repaint();
	}

	
	public void mouseClicked(MouseEvent arg0) {
		
	}

	
	public void mouseEntered(MouseEvent arg0) {
		
	}

	
	public void mouseExited(MouseEvent arg0) {
		
	}

	
	public void mousePressed(MouseEvent arg0) {
		
	}

	
	public void mouseReleased(MouseEvent arg0) {
		
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
