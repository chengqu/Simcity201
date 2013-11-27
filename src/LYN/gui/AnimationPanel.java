package LYN.gui;

import LYN.gui.Gui;

import javax.swing.*;

import animation.BaseAnimationPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends BaseAnimationPanel implements ActionListener {

    private final int WINDOWX = 450;
    private final int WINDOWY = 350;
    private Image bufferImage;
    private Dimension bufferSize;
    int x = 200;
    int y = 250;
    static final int width = 50;
    static final int length = 50;
    private List<Gui> guis = new ArrayList<Gui>();
    private JButton stop = new JButton("pause");
    private JButton resume = new JButton("resume");
    private JButton addtable = new JButton("addtable");
    Object lock = new Object();
    
    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(10, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		try
		{
			synchronized(lock) {
				for(Gui gui: guis) {
					gui.updatePosition();
				}
			}
			repaint();
		}
		catch(ConcurrentModificationException e_)
		{
			//System.out.println(e_.getCause().toString());
			repaint();
		}
		//Will have paintComponent called
	}


    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
      

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        
        g2.setColor(Color.ORANGE);
        g2.fillRect(x, y, width, length);//200 and 250 need to be table params
        g2.fillRect(x-100, y, width, length);
        g2.fillRect(x+100, y, width, length);
        
        g2.setColor(Color.black);
       
        g2.fillRect(170,30,25,20);
        g2.fillRect(200,30,25,20);
        g2.fillRect(230,30,25,20);
        g2.fillRect(260,30,25,20);
        g2.fillRect(290,30,25,20);
        g2.fillRect(320,30,25,20);
        g2.fillRect(350,30,25,20);
        g2.fillRect(380,30,25,20);
        g2.fillRect(410,30,25,20);
        
        ImageIcon myIcon = new ImageIcon(this.getClass().getResource("kitchen.jpg"));
		Image img1 = myIcon.getImage();
		g.drawImage(img1, 400, 200, 30, 30,  this);
		
		myIcon = new ImageIcon(this.getClass().getResource("pickup.jpeg"));
		img1 = myIcon.getImage();
		g.drawImage(img1, 400, 300, 30, 30,  this);
		
		myIcon = new ImageIcon(this.getClass().getResource("refrigerator.jpg"));
		img1 = myIcon.getImage();
		g.drawImage(img1, 400, 100, 30, 30,  this);
		
        
       
        /*stop.setPreferredSize(new Dimension(70, 20));
        stop.setLocation(280,5);
        resume.setPreferredSize(new Dimension(80, 20));
        resume.setLocation(350,5);
        
        stop.addActionListener(this);
        add(stop);
        resume.addActionListener(this);
        add(resume);*/
       
        
      
        synchronized(lock) {
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
        }
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(WaiterGui gui){
    	guis.add(gui);
    }
    public void addGui(CookGui gui){
    	guis.add(gui);
    }
    
    public Dimension getSize() {
		return new Dimension(WINDOWX, WINDOWY+50);
	}
    
}
