package House.gui;

import javax.swing.*;

import animation.BaseAnimationPanel;
import animation.GenericListPanel;
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
    private GenericListPanel foodEditor;
    private JButton editorButton;
    boolean editorOpen = false;

    private List<Gui> guis = new ArrayList<Gui>();
    
    private HousePersonPanel hpp;

    public AnimationPanel() {
    	this.setLayout(null);
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
//        foodEditor = new GenericListPanel();
//    	foodEditor.setBounds(100, 210, 200, 200);
//    	foodEditor.setVisible(true);
//    	add(foodEditor);
        
        bufferSize = this.getSize();
        editorButton = new JButton();
        editorButton.setBounds(0, 0, 100, 30);
        editorButton.setVisible(true);
        editorButton.setText("Open Editor");
        this.add(editorButton);
        editorButton.addActionListener(this);
 
    	Timer timer = new Timer(10, this );
    	timer.start();
    }
    
    public void setPanel(HousePersonPanel hpp)
    {
    	this.hpp = hpp;
    }

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == editorButton)
		{
			/*if(editorOpen == false)
			{
				JDialog dialog = new JDialog();
				this.add(dialog);
				editorOpen = true;
			}*/
		}
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
