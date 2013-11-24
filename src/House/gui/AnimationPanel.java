package House.gui;

import javax.swing.*;

import animation.BaseAnimationPanel;
import House.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends BaseAnimationPanel implements ActionListener {

    private final int WINDOWX = 600;
    private final int WINDOWY = 510;
    private int transparency = 0;
    private Image bufferImage;
    private Dimension bufferSize;
    private boolean setTV = false;
    Object lock = new Object();

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(10, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		
		synchronized(lock) {
			 for(Gui gui : guis) {
		            if (gui.isPresent()) {
		                gui.updatePosition();
		            }
		        }
		}
		transparency +=4;
		if(transparency == 100){
			transparency = 0;
		}

		repaint();  //Will have paintComponent called
	}

	public void setTV(boolean p) {
		setTV = p;
	}
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        //Background
        ImageIcon myIcon = new ImageIcon(this.getClass().getResource("pokerman.jpg"));
		Image img1 = myIcon.getImage();
		g.drawImage(img1, 0, 0, WINDOWX, WINDOWY,  this);
		
          //TV
		Color color = new Color(0, 0, 0, 255 * transparency / 100);
		g.setColor(color);
		g.fillRect(406, 58, 22, 13);
		
       


       
		synchronized(lock) {
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
		}
    }

    public void addGui(HouseGui gui) {
        guis.add(gui);
    }
    
    public Dimension getSize() {
		return new Dimension(WINDOWX, WINDOWY);
	}
    
    
}
