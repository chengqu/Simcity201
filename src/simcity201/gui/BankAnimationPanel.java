package simcity201.gui;

import javax.swing.*;

import agents.BankTellerAgent;
import animation.BaseAnimationPanel;
import simcity201.gui.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankAnimationPanel extends BaseAnimationPanel implements ActionListener {
	public final static int WINDOWX = 600;
    public final static int WINDOWY = 550;
    
    JButton button = new JButton();
    JTextField text = new JTextField();
    
    //Object lock = new Object();
    
    private final int DELAY = 8;
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
    private BankMap map;
    Bank b;
    
    public BankAnimationPanel(Bank b) {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        this.b = b;
        
        bufferSize = this.getSize();
        
    	Timer timer = new Timer(DELAY, this );
    	timer.start();
    	
    	text.setPreferredSize(new Dimension(140, 20));
    	text.setMaximumSize(new Dimension(140, 20));
    	text.setMinimumSize(new Dimension(140, 20));
    	button.setPreferredSize(new Dimension(150, 20));
    	button.setMaximumSize(new Dimension(150, 20));
    	button.setMinimumSize(new Dimension(150, 20));
    	
    	add(text);
    	add(button);
    	text.addActionListener(this);
    }

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == text)
		{
			try
			{
				float m = Float.parseFloat(text.getText());
				b.db.budget = m;
				button.setText(Float.toString(m));
				text.setText("");
			}
			catch(NumberFormatException ex)
			{
				text.setText("Incorrect data format, please use float");
			}
		}
		else
		{
			button.setText(Float.toString(b.db.budget));
			//synchronized(lock) {
			for(Gui gui : guis) {
	            gui.updatePosition();
				}
			//}
			repaint();  //Will have paintComponent called
		}
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        map.draw(g2);
       
        //synchronized(lock){
        for(Gui gui : guis) {
            //if (gui.isPresent()) {
                gui.draw(g2);
            //}
        }
        //}
    }
    
    public void addGui(BankCustomerGui gui) {
    	guis.add(gui);
    }
    public void addGui(BankTellerGui gui) {
    	guis.add(gui);
    }
    public void addGui(BankSecurityGui g) {
		guis.add(g);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(WINDOWX+30, WINDOWY+30);
	}
	public void setMap(BankMap map) {
		this.map = map;
	}

	
    
}
