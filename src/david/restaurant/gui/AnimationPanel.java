package david.restaurant.gui;

import javax.swing.*;

import animation.BaseAnimationPanel;
import david.restaurant.HostAgent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


public class AnimationPanel extends BaseAnimationPanel implements ActionListener, MouseListener{

    private final int WINDOWX = 450;
    private final int WINDOWY = 350;
    public boolean canAdd = false;
    public boolean mouseInComponent = false;
    private JButton addTable = new JButton("Add Table");
    public HostAgent host;
    private List<Table> tables = new ArrayList<Table>();
    Timer timer;
    
    int DELAY = 8;
    
    Object lock = new Object();

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	this.setBorder(BorderFactory.createLineBorder(Color.black));
    	
    	this.setPreferredSize(this.getSize());
    	this.setMaximumSize(this.getSize());
    	this.setMinimumSize(this.getSize());
        setVisible(true);
        add(addTable);
        addTable.addActionListener(this);
        addMouseListener(this);
        
        int xTable1 = 250, xTable2 = 250, xTable3 = 250;
        int yTable1 = 100, yTable2 = 180, yTable3 = 260;
        
        this.getSize();
 
    	timer = new Timer(DELAY, this );
    	timer.start();
    	tables.add(new Table(tables.size() + 1, xTable1, yTable1));
    	tables.add(new Table(tables.size() + 1, xTable2, yTable2));
    	tables.add(new Table(tables.size() + 1, xTable3, yTable3));
    }
    
    public void pause()
    {
    	timer.stop();
    }
    
    public void unpause()
    {
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().getClass() == JButton.class)
		{
			JButton button = (JButton)e.getSource();
			if(button.getText() == "Add Table")
			{
				button.setText("Can add Table");
				canAdd = true;
				button.setEnabled(false);
			}
		}
		synchronized(lock)
		{
			for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.updatePosition();
	            }
	        }
		}
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        for(Table temp: tables)
        {
        	g2.setColor(Color.ORANGE);
        	g2.fillRect(temp.xPos, temp.yPos, 50, 50);//200 and 250 need to be table params
        }
        synchronized(lock)
        {
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.draw(g2);
	            }
	        }
        }
    }
    
    public void addGui(Gui gui)
    {
    	guis.add(gui);
    }
    
    public void addHost(HostAgent h)
    {
    	host = h;
    	for(Table temp: tables)
    	{
    		h.msgAddTable(temp);
    	}
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mouseInComponent = true;
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		mouseInComponent = false;
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(mouseInComponent == true && canAdd == true)
		{
			int x = arg0.getX();
			int y = arg0.getY();
			tables.add(new Table(tables.size() + 1, x, y));
			if(host != null)
				host.msgAddTable(tables.get(tables.size() - 1));
			canAdd = false;
			addTable.setText("Add Table");
			addTable.setEnabled(true);
			System.out.println(host.tables.size());
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public Dimension getSize() {
		return new Dimension(WINDOWX, WINDOWY);
	}
}
