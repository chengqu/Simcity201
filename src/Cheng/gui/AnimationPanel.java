package Cheng.gui;

import javax.swing.*;

import david.restaurant.CookAgent.myFood;
import Cheng.CookAgent.Food;
import animation.BaseAnimationPanel;
import animation.GenericListPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;


public class AnimationPanel extends BaseAnimationPanel implements ActionListener {

	List<JTextField> textFields = new ArrayList<JTextField>();
	List<JButton> buttons = new ArrayList<JButton>();
	
    private final int WINDOWX = 700;
    private final int WINDOWY = 800;
    private final int TABLEX = 200;
    private final int TABLEY = 250;
    private final int TABLEW = 50;
    private final int TABLEH = 50;
    private final int t = 10;
    
    private JButton addTable;
    private JButton pause;
    private JButton Break;
    public boolean goBreak = false;
    public int clicked = 0;
    public boolean table2 = false;
    public boolean table3 = false;
    public int tablew2;
    public int tableh2;
    public int tablex2;
    public int tabley2;
    
    public int tablew3;
    public int tableh3;
    public int tablex3;
    public int tabley3;
    
    private Image bufferImage;
    private Dimension bufferSize;
    private List<Gui> guis = new ArrayList<Gui>();
    Object lock = new Object();
    
    private RestaurantPanel rp;
    private GenericListPanel cookPanel;
    private GenericListPanel cashierPanel;
    
    public AnimationPanel() {
    	
    	this.setLayout(null);
    	
       	addTable = new JButton();
    	addTable.setText("addTable");
    	addTable.addActionListener(this);
    	//add(addTable);
    	
    	
    	
    	pause = new JButton();
    	pause.setText("Pause");
    	pause.addActionListener(this);
    	add(pause);
    	
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(t, this );
    	timer.start();
    	
    	cookPanel = new GenericListPanel();
        cookPanel.setBounds(20, 500, 150, 250);
        cookPanel.setVisible(true);
        cookPanel.clearPane();
        
        cashierPanel = new GenericListPanel();
        cashierPanel.setBounds(200, 500, 250, 100);
        cashierPanel.clearPane();
        cashierPanel.setVisible(true);
        add(cookPanel);
        add(cashierPanel);
    }
    
    public void setRestPanel(RestaurantPanel rp)
    {
    	this.rp = rp;
        for(Food f : rp.cook.food)
        {
        	JTextField textField;
            JButton button;
            List<JComponent> components;
            
            components = new ArrayList<JComponent>();
            JPanel label = new JPanel();
            label.add(new JLabel("<html><pre>" + f.Choice + "</pre></html>"));
            components.add(label);
            cookPanel.addParams(components);
            
        	System.out.println(f.Choice);
        	textField = new JTextField();
        	textField.setName(f.Choice);
        	textField.setPreferredSize(new Dimension(50, 20));
        	textField.setMaximumSize(new Dimension(50, 20));
        	textField.setMinimumSize(new Dimension(50, 20));
        	textField.addActionListener(this);
        	
        	button = new JButton();
        	button.setName(f.Choice);
        	button.setPreferredSize(new Dimension(70, 20));
        	button.setMaximumSize(new Dimension(70, 20));
        	button.setMinimumSize(new Dimension(70, 20));
        	button.setText(Integer.toString(f.number));
        	
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
    	textField.setPreferredSize(new Dimension(100, 20));
    	textField.setMaximumSize(new Dimension(100, 20));
    	textField.setMinimumSize(new Dimension(100, 20));
    	textField.addActionListener(this);
    	
    	button.setName("Money");
    	button.setPreferredSize(new Dimension(150, 20));
    	button.setMaximumSize(new Dimension(150, 20));
    	button.setMinimumSize(new Dimension(150, 20));
    	
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
			for(Food f : rp.cook.food)
			{
				if(f.Choice == b.getName())
				{
					b.setText(Integer.toString(f.number));
					break;
				}
			}
			if(b.getName().equals("Money"))
			{
				b.setText(Double.toString(rp.cashier.money));
			}
		}
		if(e.getSource().getClass() == JTextField.class)
		{
			JTextField field = (JTextField) e.getSource();
			for(Food f : rp.cook.food)
			{
				if(field.getName() == f.Choice)
				{
					try
					{
						int amount = Integer.parseInt(field.getText());
						f.number = amount;
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
		
		synchronized(lock) {
			for (Gui gui : guis) {
			gui.updatePosition();
			}
			}
		repaint();  //Will have paintComponent called
		if(e.getSource() == pause){
			if(pause.getText().compareTo("Pause") == 0){
			pause.setText("Resume");
	        for(Gui gui : guis) {
	            if (gui.isPresent()) {
	                gui.pause();
	            }
	        }
		}
			else if(pause.getText().compareTo("Resume")== 0){
				pause.setText("Pause");
				 for(Gui gui : guis) {
			            if (gui.isPresent()) {
			                gui.jixu();
			            }
				 }
			}
		}
		
		
		}
		
	
		
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(180, 250, 100, 100);//200 and 250 need to be table params
        
        
        //if(table2 == true){
        g2.setColor(Color.ORANGE);
        g2.fillRect(320, 250, 100, 100);//200 and 250 need to be table params
        //}
        //if(table3 == true){
        g2.setColor(Color.ORANGE);
        g2.fillRect(30, 250, 100, 100);//200 and 250 need to be table params
        //}
        g2.setColor(Color.lightGray);
        g2.fillRect(550, 50, 100, 400);
        
        g2.setColor(Color.blue);
        g2.fillRect(460, 400, 30, 20);
        g2.drawString("Fridge", 450, 400);
        
        g2.setColor(Color.red);
        g2.fillRect(360, 400, 30, 20);
        g2.drawString("Grill", 360, 400);
        
        g2.setColor(Color.black);
        g2.fillRect(250, 400, 20, 50);
        g2.drawString("Plating", 250, 400);
        
        g2.setColor(Color.lightGray);
        g2.fillRect(550, 50, 100, 400);
        
        

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
    public void addGui(CookGui gui){
    	guis.add(gui);
    }
    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    public void addGui(WaiterGui gui){
    	guis.add(gui);
    }
    public Dimension getSize() {
		return new Dimension(WINDOWX, WINDOWY);
	}
}
