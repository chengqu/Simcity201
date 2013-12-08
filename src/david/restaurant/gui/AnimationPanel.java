package david.restaurant.gui;

import javax.swing.*;

import animation.BaseAnimationPanel;
import animation.GenericListPanel;
import david.restaurant.CookAgent.myFood;
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

	List<JTextField> textFields = new ArrayList<JTextField>();
	List<JButton> buttons = new ArrayList<JButton>();
    private final int WINDOWX = 700;
    private final int WINDOWY = 500;
    public boolean canAdd = false;
    public boolean mouseInComponent = false;
    private JButton addTable = new JButton("Add Table");
    public HostAgent host;
    private List<Table> tables = new ArrayList<Table>();
    Timer timer;
    private RestaurantPanel rp;
    private GenericListPanel cookPanel;
    private GenericListPanel cashierPanel;
    
    int DELAY = 8;
    
    Object lock = new Object();

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	this.setBorder(BorderFactory.createLineBorder(Color.black));
    	
    	this.setLayout(null);
    	
    	this.setPreferredSize(this.getSize());
    	this.setMaximumSize(this.getSize());
    	this.setMinimumSize(this.getSize());
        setVisible(true);
        
        int xTable1 = 250, xTable2 = 250, xTable3 = 250;
        int yTable1 = 100, yTable2 = 180, yTable3 = 260;
        
        this.getSize();
 
    	timer = new Timer(DELAY, this );
    	timer.start();
    	tables.add(new Table(tables.size() + 1, xTable1, yTable1));
    	tables.add(new Table(tables.size() + 1, xTable2, yTable2));
    	tables.add(new Table(tables.size() + 1, xTable3, yTable3));
    	
    	cookPanel = new GenericListPanel();
    	addTable.addActionListener(this);
        addTable.setBounds(500, 20, 150, 20);
        cookPanel.setBounds(500, 60, 150, 250);
        cookPanel.setVisible(true);
        cookPanel.clearPane();
        addMouseListener(this);
        
        cashierPanel = new GenericListPanel();
        cashierPanel.setBounds(500, 320, 150, 100);
        cashierPanel.clearPane();
        cashierPanel.setVisible(true);
    	add(addTable);
        add(cookPanel);
        add(cashierPanel);
    }
    
    public void setRestPanel(RestaurantPanel rp)
    {
    	this.rp = rp;
        for(myFood f : rp.cook.foods.values())
        {
        	JTextField textField;
            JButton button;
            List<JComponent> components;
            
            components = new ArrayList<JComponent>();
            JPanel label = new JPanel();
            label.add(new JLabel("<html><pre>" + f.food.name + "</pre></html>"));
            components.add(label);
            cookPanel.addParams(components);
            
        	System.out.println(f.food.name);
        	textField = new JTextField();
        	textField.setName(f.food.name);
        	textField.setPreferredSize(new Dimension(50, 20));
        	textField.setMaximumSize(new Dimension(50, 20));
        	textField.setMinimumSize(new Dimension(50, 20));
        	textField.addActionListener(this);
        	
        	button = new JButton();
        	button.setName(f.food.name);
        	button.setPreferredSize(new Dimension(70, 20));
        	button.setMaximumSize(new Dimension(70, 20));
        	button.setMinimumSize(new Dimension(70, 20));
        	button.setText(Integer.toString(f.food.amount));
        	
        	components = new ArrayList<JComponent>();
        	buttons.add(button);
        	textFields.add(textField);
        	components.add(textField);
        	components.add(button);
        	cookPanel.addParams(components);
        	cookPanel.setVisible(true);
        }
        
        JTextField textField;
        JButton button;
        List<JComponent> components;
    	
    	components = new ArrayList<JComponent>();
        JPanel label = new JPanel();
        label.add(new JLabel("<html><pre>" + "Money" + "</pre></html>"));
        components.add(label);
        cashierPanel.addParams(components);
        
        textField = new JTextField();
        button = new JButton();
        components = new ArrayList<JComponent>();
        
        textField.setName("Money");
    	textField.setPreferredSize(new Dimension(50, 20));
    	textField.setMaximumSize(new Dimension(50, 20));
    	textField.setMinimumSize(new Dimension(50, 20));
    	textField.addActionListener(this);
    	
    	button.setName("Money");
    	button.setPreferredSize(new Dimension(70, 20));
    	button.setMaximumSize(new Dimension(70, 20));
    	button.setMinimumSize(new Dimension(70, 20));
    	
    	components.add(button);
    	components.add(textField);
    	
    	buttons.add(button);
    	textFields.add(textField);
    	
    	cashierPanel.addParams(components);
    	cashierPanel.setVisible(true);
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
		for(JButton b: buttons)
		{
			for(myFood f: rp.cook.foods.values())
			{
				if(f.food.name == b.getName())
				{
					b.setText(Integer.toString(f.food.amount));
					break;
				}
			}
			if(b.getName().equals("Money"))
			{
				b.setText(Float.toString(rp.cashier.money));
			}
		}
		if(e.getSource().getClass() == JTextField.class)
		{
			JTextField field = (JTextField) e.getSource();
			for(myFood f : rp.cook.foods.values())
			{
				if(field.getName() == f.food.name)
				{
					try
					{
						int amount = Integer.parseInt(field.getText());
						f.food.amount = amount;
						field.setText("");
					}
					catch(NumberFormatException ex)
					{
						field.setText("Invalid type");
					}
					break;
				}
			}
			if(field.getName().equals("Money"))
			{
				for(JButton b: buttons)
				{
					if(b.getName().equals("Money"))
					{
						try
						{
							float amount = Float.parseFloat(field.getText());
							rp.cashier.money = amount;
							field.setText("");
						}
						catch(NumberFormatException ex)
						{
							field.setText("Invalid type");
						}
					}
				}
			}
		}
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

	public void mouseClicked(MouseEvent arg0) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		mouseInComponent = true;
	}

	public void mouseExited(MouseEvent arg0) {
		mouseInComponent = false;
	}

	public void mousePressed(MouseEvent arg0) {
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

	public void mouseReleased(MouseEvent arg0) {
		
	}

	public Dimension getSize() {
		return new Dimension(WINDOWX, WINDOWY);
	}
}
