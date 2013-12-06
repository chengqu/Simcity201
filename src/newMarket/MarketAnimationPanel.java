package newMarket;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import simcity201.gui.Gui;
import animation.BaseAnimationPanel;

public class MarketAnimationPanel extends BaseAnimationPanel implements ActionListener {
	
	private static int WINDOWX = 500, WINDOWY = 500;
	
	private List<Gui> guis = new ArrayList<Gui>();
	
	public Dimension getSize() {
		return new Dimension(WINDOWX, WINDOWY);
	}

    private final int DELAY = 8;
    
    private Image bufferImage;
    private Dimension bufferSize;

	public MarketAnimationPanel() {
		setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
        
    	Timer timer = new Timer(DELAY, this );
    	timer.start();
    	
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		for(Gui gui : guis) {
            gui.updatePosition();
        }
		repaint();  //Will have paintComponent called
	}

	
	public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
  
        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
        
    }
}
