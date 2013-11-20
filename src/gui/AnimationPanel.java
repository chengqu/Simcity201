package gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 500;
    private final int WINDOWY = 550;
    private int transparency = 0;
    private Image bufferImage;
    private Dimension bufferSize;
    private boolean setTV = false;;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(10, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		
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
		g.drawImage(img1, 0, 0, WINDOWX+40, WINDOWY-90,  this);
		
          //TV
		Color color = new Color(0, 0, 0, 255 * transparency / 100);
		g.setColor(color);
		g.fillRect(365, 51, 22, 13);
		
       


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

    public void addGui(HouseGui gui) {
        guis.add(gui);
    }
    
}
