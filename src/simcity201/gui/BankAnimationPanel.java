package simcity201.gui;

import javax.swing.*;

import simcity201.gui.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankAnimationPanel extends JPanel implements ActionListener {
	public final static int WINDOWX = 600;
    public final static int WINDOWY = 550;
    
    private final int DELAY = 8;
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
    

    public BankAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
        
    	Timer timer = new Timer(DELAY, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );


        for(Gui gui : guis) {
            //if (gui.isPresent()) {
                gui.updatePosition();
            //}
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }
    /*
    public void addGui(BankCustomerGui gui) {
        guis.add(gui);
    }
    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
    public void addGui(KitchenGui gui) {
    	guis.add(gui);
    }*/
}
