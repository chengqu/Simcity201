package guehochoi.gui;

import javax.swing.*;

import david.restaurant.CookAgent.myFood;
import simcity201.gui.GlobalMap;
import animation.BaseAnimationPanel;
import animation.GenericListPanel;
import guehochoi.restaurant.CookAgent.Food;
import guehochoi.restaurant.HostAgent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimerTask;

public class AnimationPanel extends BaseAnimationPanel implements ActionListener  {

	Object lock = new Object();
	
    public final static int WINDOWX = 600;
    public final static int WINDOWY = 450;
    
    // Added constants list
    public static final int SIZE_TABLE_X = 50;
    public static final int SIZE_TABLE_Y = 50;
    public static final int TABLE_GAP = 50;
    public static Map<Integer, Point> TableMap
    				= new HashMap<Integer, Point>();
    
    private final int DELAY = 1;
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
    
    private RestaurantPanel rp;
    private GenericListPanel cookPanel;
    private GenericListPanel cashierPanel;
    List<JTextField> textFields = new ArrayList<JTextField>();
	List<JButton> buttons = new ArrayList<JButton>();
    

    public AnimationPanel() {
    	this.setLayout(null);
    	setSize(WINDOWX, WINDOWY);
    	this.setPreferredSize(new Dimension(WINDOWX, WINDOWY));
    	this.setMaximumSize(new Dimension(WINDOWX, WINDOWY));
    	this.setMinimumSize(new Dimension(WINDOWX, WINDOWY));
    	
        setVisible(true);
        
        /* TableMap */
        for (int i = 0; i < HostAgent.MAXNTABLES; i++) {
        	TableMap.put(i+1, 
        			new Point((int)((i%3)*(SIZE_TABLE_X+ TABLE_GAP))+TABLE_GAP , 
        					(int)((i/3)*(SIZE_TABLE_Y+TABLE_GAP))+TABLE_GAP));
        }
        
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(DELAY, this );
    	timer.start();
    	
    	cookPanel = new GenericListPanel();
        cookPanel.setBounds(600, 60, 200, 250);
        cookPanel.setVisible(true);
        cookPanel.clearPane();
        
        cashierPanel = new GenericListPanel();
        cashierPanel.setBounds(600, 320, 200, 100);
        cashierPanel.clearPane();
        cashierPanel.setVisible(true);
        add(cookPanel);
        add(cashierPanel);
    }
    
    public void setRestPanel(RestaurantPanel rp)
    {
    	this.rp = rp;
        for(Food f : rp.cook.foods.values())
        {
        	JTextField textField;
            JButton button;
            List<JComponent> components;
            
            components = new ArrayList<JComponent>();
            JPanel label = new JPanel();
            label.add(new JLabel("<html><pre>" + f.type + "</pre></html>"));
            components.add(label);
            cookPanel.addParams(components);
            
        	System.out.println(f.type);
        	textField = new JTextField();
        	textField.setName(f.type);
        	textField.setPreferredSize(new Dimension(50, 20));
        	textField.setMaximumSize(new Dimension(50, 20));
        	textField.setMinimumSize(new Dimension(50, 20));
        	textField.addActionListener(this);
        	
        	button = new JButton();
        	button.setName(f.type);
        	button.setPreferredSize(new Dimension(70, 20));
        	button.setMaximumSize(new Dimension(70, 20));
        	button.setMinimumSize(new Dimension(70, 20));
        	button.setText(Integer.toString(f.amount));
        	
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
    	button.setPreferredSize(new Dimension(120, 20));
    	button.setMaximumSize(new Dimension(120, 20));
    	button.setMinimumSize(new Dimension(120, 20));
    	
    	components.add(button);
    	components.add(textField);
    	
    	buttons.add(button);
    	textFields.add(textField);
    	
    	cashierPanel.addParams(components);
    	cashierPanel.setVisible(true);
    }

	public void actionPerformed(ActionEvent e) {
		for(JButton b: buttons)
		{
			for(Food f : rp.cook.foods.values())
			{
				if(f.type == b.getName())
				{
					b.setText(Integer.toString(f.amount));
					break;
				}
			}
			if(b.getName().equals("Money"))
			{
				b.setText(Double.toString(rp.cashier.restaurantBudget));
			}
		}
		if(e.getSource().getClass() == JTextField.class)
		{
			JTextField field = (JTextField) e.getSource();
			for(Food f : rp.cook.foods.values())
			{
				if(field.getName() == f.type)
				{
					try
					{
						int amount = Integer.parseInt(field.getText());
						f.amount = amount;
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
							rp.cashier.restaurantBudget = amount;
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
		synchronized(lock) {
			for (Gui gui : guis) {
				gui.updatePosition();
			}
		}
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        
        for (int i = 0; i < HostAgent.NTABLES; i++) {
	        //Here is the table
	        g2.setColor(Color.ORANGE);
	        Point position = TableMap.get(i+1);
	        g2.fillRect( (int)position.getX(), (int)position.getY(),
	        		SIZE_TABLE_X, SIZE_TABLE_Y);
	        //200 and 250 need to be table params --> now table's location is set by HostGui
	        // it would make much more sense that the host knows where the table is
	        // may be changed later if more tables come in
	        // I could possibly change it using tables per row with windowsize, which is better
        }
        

        
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

    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    public void addGui(CookGui gui) {
    	guis.add(gui);
    }
    public void addGui(KitchenGui gui) {
    	guis.add(gui);
    }

	@Override
	public Dimension getSize() {
		return new Dimension(WINDOWX+30, WINDOWY+45);
	}
    
}
