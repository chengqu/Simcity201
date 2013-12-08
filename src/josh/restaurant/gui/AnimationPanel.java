package josh.restaurant.gui;

import javax.swing.*;

import david.restaurant.CookAgent.myFood;
import animation.BaseAnimationPanel;
import animation.GenericListPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

import josh.restaurant.CookAgent.Food;

public class AnimationPanel extends BaseAnimationPanel implements ActionListener {

	private static int xTableSize = 50; 
	private static int yTableSize = 50; 
	private int xStartCor = 100; 
	private int yStartCor = 100;
	
    private final int WINDOWX = 900;
    private final int WINDOWY = 700;
    private Image bufferImage;
    private Dimension bufferSize;
    private final int spaceBtwnTables = 80; 
    
    
    //***
    static int NTABLES = 5;  //a global for the number of tables.  THREEEEEE TABLES
    //***
    
    private List<Gui> guis = new ArrayList<Gui>();
    
    private RestaurantPanel rp;
    private GenericListPanel cookPanel;
    private GenericListPanel cashierPanel;
    List<JTextField> textFields = new ArrayList<JTextField>();
	List<JButton> buttons = new ArrayList<JButton>();

    public AnimationPanel() {
    	this.setLayout(null);
    	Dimension d = new Dimension(WINDOWX,WINDOWY);
    	this.setPreferredSize(d);
    	this.setMaximumSize(d);
    	this.setMinimumSize(d);
    	
    	//setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(8, this );
    	timer.start();
    	
    	cookPanel = new GenericListPanel();
        cookPanel.setBounds(700, 60, 150, 250);
        cookPanel.setVisible(true);
        cookPanel.clearPane();
        
        cashierPanel = new GenericListPanel();
        cashierPanel.setBounds(700, 320, 150, 100);
        cashierPanel.clearPane();
        cashierPanel.setVisible(true);
        add(cookPanel);
        add(cashierPanel);
    }
    
    public void setRestPanel(RestaurantPanel rp)
    {
    	this.rp = rp;
        for(Food f : rp.cook.foodMap.values())
        {
        	JTextField textField;
            JButton button;
            List<JComponent> components;
            
            components = new ArrayList<JComponent>();
            JPanel label = new JPanel();
            label.add(new JLabel("<html><pre>" + f.type_+ "</pre></html>"));
            components.add(label);
            cookPanel.addParams(components);
            
        	System.out.println(f.type_);
        	textField = new JTextField();
        	textField.setName(f.type_);
        	textField.setPreferredSize(new Dimension(50, 20));
        	textField.setMaximumSize(new Dimension(50, 20));
        	textField.setMinimumSize(new Dimension(50, 20));
        	textField.addActionListener(this);
        	
        	button = new JButton();
        	button.setName(f.type_);
        	button.setPreferredSize(new Dimension(70, 20));
        	button.setMaximumSize(new Dimension(70, 20));
        	button.setMinimumSize(new Dimension(70, 20));
        	button.setText(Integer.toString(f.foodStock_));
        	
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
			for(Food f : rp.cook.foodMap.values())
			{
				if(f.type_ == b.getName())
				{
					b.setText(Integer.toString(f.foodStock_));
					break;
				}
			}
			if(b.getName().equals("Money"))
			{
				b.setText(Float.toString(rp.cashier.totalMoney));
			}
		}
		if(e.getSource().getClass() == JTextField.class)
		{
			JTextField field = (JTextField) e.getSource();
			for(Food f : rp.cook.foodMap.values())
			{
				if(field.getName() == f.type_)
				{
					try
					{
						int amount = Integer.parseInt(field.getText());
						f.foodStock_ = amount;
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
							rp.cashier.totalMoney = amount;
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
		for(Gui gui : guis) {
            gui.updatePosition();
        }
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
