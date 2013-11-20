package josh.restaurant.gui;

import javax.swing.*;

import animation.BaseAnimationPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends BaseAnimationPanel implements ActionListener {

	private static int xTableSize = 50; 
	private static int yTableSize = 50; 
	private int xStartCor = 100; 
	private int yStartCor = 100;
	
    private final int WINDOWX = 700;
    private final int WINDOWY = 400;
    private Image bufferImage;
    private Dimension bufferSize;
    private final int spaceBtwnTables = 80; 
    
    
    //***
    static int NTABLES = 5;  //a global for the number of tables.  THREEEEEE TABLES
    //***
    
    private List<Gui> guis = new ArrayList<Gui>();
    

    public AnimationPanel() {
    	
    	Dimension d = new Dimension(WINDOWX,WINDOWY);
    	this.setPreferredSize(d);
    	this.setMaximumSize(d);
    	this.setMinimumSize(d);
    	
    	//setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(20, this );
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

        //Here is the table
        g2.setColor(Color.ORANGE);
        
        // making some tables here
        
        for(int i=0; i < NTABLES; i++) {
        
        	g2.fillRect(xStartCor + (i * spaceBtwnTables), yStartCor, xTableSize, yTableSize);	
        }
 
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }
    
    public void addGui(CashierGui gui) {
    	guis.add(gui);
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(WaiterGui gui) {
    	guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
    	guis.add(gui); 
    }
    public void addGui(OrderGui gui) {
    	guis.add(gui);
    }

	public void addGui(CookOrderGui gui) {
		guis.add(gui);
		
	}

	@Override
	public Dimension getSize() {
		// TODO Auto-generated method stub
		return (new Dimension(WINDOWX, WINDOWY));
	}
}
