package LYN.gui;

import LYN.CookAgent.Food;
import LYN.gui.Gui;

import javax.swing.*;

import david.restaurant.CookAgent.myFood;
import animation.BaseAnimationPanel;
import animation.GenericListPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends BaseAnimationPanel implements ActionListener {

    private final int WINDOWX = 850;
    private final int WINDOWY = 350;
    private Image bufferImage;
    private Dimension bufferSize;
    int x = 200;
    int y = 250;
    static final int width = 50;
    static final int length = 50;
    private List<Gui> guis = new ArrayList<Gui>();
    private JButton stop = new JButton("pause");
    private JButton resume = new JButton("resume");
    private JButton addtable = new JButton("addtable");
    Object lock = new Object();
    
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
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(10, this );
    	timer.start();
    	
    	cookPanel = new GenericListPanel();
        cookPanel.setBounds(500, 20, 150, 250);
        cookPanel.setVisible(true);
        cookPanel.clearPane();
        
        cashierPanel = new GenericListPanel();
        cashierPanel.setBounds(670, 20, 150, 100);
        cashierPanel.clearPane();
        cashierPanel.setVisible(true);
        add(cookPanel);
        add(cashierPanel);
    }
    
    public void setRestPanel(RestaurantPanel rp)
    {
    	this.rp = rp;
        for(Food f : rp.cook.map2.values())
        {
        	JTextField textField;
            JButton button;
            List<JComponent> components;
            
            components = new ArrayList<JComponent>();
            JPanel label = new JPanel();
            label.add(new JLabel("<html><pre>" + f.choice + "</pre></html>"));
            components.add(label);
            cookPanel.addParams(components);
            
        	System.out.println(f.choice);
        	textField = new JTextField();
        	textField.setName(f.choice);
        	textField.setPreferredSize(new Dimension(50, 20));
        	textField.setMaximumSize(new Dimension(50, 20));
        	textField.setMinimumSize(new Dimension(50, 20));
        	textField.addActionListener(this);
        	
        	button = new JButton();
        	button.setName(f.choice);
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

	public void actionPerformed(ActionEvent e) {
		for(JButton b: buttons)
		{
			for(Food f: rp.cook.map2.values())
			{
				if(f.choice == b.getName())
				{
					b.setText(Integer.toString(f.amount));
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
			for(Food f: rp.cook.map2.values())
			{
				if(field.getName() == f.choice)
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
		try
		{
			synchronized(lock) {
				for(Gui gui: guis) {
					gui.updatePosition();
				}
			}
			repaint();
		}
		catch(ConcurrentModificationException e_)
		{
			//System.out.println(e_.getCause().toString());
			repaint();
		}
		//Will have paintComponent called
	}


    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
      

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        
        g2.setColor(Color.ORANGE);
        g2.fillRect(x, y, width, length);//200 and 250 need to be table params
        g2.fillRect(x-100, y, width, length);
        g2.fillRect(x+100, y, width, length);
        
        g2.setColor(Color.black);
       
        g2.fillRect(170,30,25,20);
        g2.fillRect(200,30,25,20);
        g2.fillRect(230,30,25,20);
        g2.fillRect(260,30,25,20);
        g2.fillRect(290,30,25,20);
        g2.fillRect(320,30,25,20);
        g2.fillRect(350,30,25,20);
        g2.fillRect(380,30,25,20);
        g2.fillRect(410,30,25,20);
        
        ImageIcon myIcon = new ImageIcon(this.getClass().getResource("kitchen.jpg"));
		Image img1 = myIcon.getImage();
		g.drawImage(img1, 400, 200, 30, 30,  this);
		
		myIcon = new ImageIcon(this.getClass().getResource("pickup.jpeg"));
		img1 = myIcon.getImage();
		g.drawImage(img1, 400, 300, 30, 30,  this);
		
		myIcon = new ImageIcon(this.getClass().getResource("refrigerator.jpg"));
		img1 = myIcon.getImage();
		g.drawImage(img1, 400, 100, 30, 30,  this);
		
        
       
        /*stop.setPreferredSize(new Dimension(70, 20));
        stop.setLocation(280,5);
        resume.setPreferredSize(new Dimension(80, 20));
        resume.setLocation(350,5);
        
        stop.addActionListener(this);
        add(stop);
        resume.addActionListener(this);
        add(resume);*/
       
        
      
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

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(WaiterGui gui){
    	guis.add(gui);
    }
    public void addGui(CookGui gui){
    	guis.add(gui);
    }
    
    public Dimension getSize() {
		return new Dimension(WINDOWX, WINDOWY+50);
	}
    
}
