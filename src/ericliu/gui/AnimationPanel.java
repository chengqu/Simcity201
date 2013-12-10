package ericliu.gui;

import javax.swing.*;

import david.restaurant.CookAgent.myFood;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import ericliu.restaurant.FoodClass;
import animation.BaseAnimationPanel;
import animation.GenericListPanel;


public class AnimationPanel extends BaseAnimationPanel implements ActionListener {
   
    public Object lock=new Object();

    private final int WINDOWX = 750;
    private final int WINDOWY = 750;
    private Image bufferImage;
    private Dimension bufferSize;
    private final int Table1fillrect1=200;
    private final int Table1fillrect2=250;
    private final int Table2fillrect1=200;
    private final int Table2fillrect2=350;
    private final int Table3fillrect1=300;
    private final int Table3fillrect2=250;
    private final int CashierTableX=300;
    private final int CashierTableY=60;
    private final int CashierTablefillrectX=200;
    private final int CashierTablefillrectY=50;
    private final int fillrect3=50;
    private final int fillrect4=50;
    private final int CustomerWaitingAreaX=20;
    private final int CustomerWaitingAreaY=40;
    private final int CustomerWaitingAreafillrectX=80;
    private final int CustomerWaitingAreafillrectY=140;
    private final int WaiterWaitingAreaX=20;
    private final int WaiterWaitingAreaY=400;
    private final int WaiterWaitingAreafillrectX=80;
    private final int WaiterWaitingAreafillrectY=80;
    private final int CookAreaX=540;
    private final int CookAreaY=340;
    private final int CookAreafillrectX=140;
    private final int CookAreafillrectY=140;
    private final int kitchenAreaX=590;
    private final int kitchenAreaY=480;
    private final int kitchenAreafillrectX=90;
    private final int kitchenAreafillrectY=50;
    private final int platingAreaX=520;
    private final int platingAreaY=360;
    private final int platingAreafillrectX=20;
    private final int platingAreafillrectY=60;
    private final int refrigeratorX=620;
    private final int refrigeratorY=320;
    private final int refrigeratorfillrectX=60;
    private final int refrigeratorfillrectY=20;
    
    private RestaurantPanel rp;
    private GenericListPanel cookPanel;
    private GenericListPanel cashierPanel;
    List<JTextField> textFields = new ArrayList<JTextField>();
	List<JButton> buttons = new ArrayList<JButton>();
    
    class Coordinates{
       int xPos;
       int yPos;
    }
    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	
      this.setLayout(null);
      setSize(WINDOWX, WINDOWY);
      this.setPreferredSize(new Dimension(WINDOWX, WINDOWY));
      this.setMaximumSize(new Dimension(WINDOWX, WINDOWY));
      this.setMinimumSize(new Dimension(WINDOWX, WINDOWY));
      setVisible(true);
        
      bufferSize = this.getSize();
      Timer timer = new Timer(3, this );
      timer.start();

      cookPanel = new GenericListPanel();
      cookPanel.setBounds(10, 500, 150, 250);
      cookPanel.setVisible(true);
      cookPanel.clearPane();
      
      cashierPanel = new GenericListPanel();
      cashierPanel.setBounds(300, 500, 150, 100);
      cashierPanel.clearPane();
      cashierPanel.setVisible(true);
      add(cookPanel);
      add(cashierPanel);
    }
    
    public void setRestPanel(RestaurantPanel rp)
    {
    	this.rp = rp;
        for(String f : rp.cook.FoodCount.keySet())
        {
        	JTextField textField;
            JButton button;
            List<JComponent> components;
            
            components = new ArrayList<JComponent>();
            JPanel label = new JPanel();
            label.add(new JLabel("<html><pre>" + f + "</pre></html>"));
            components.add(label);
            cookPanel.addParams(components);
            
        	System.out.println(f);
        	textField = new JTextField();
        	textField.setName(f);
        	textField.setPreferredSize(new Dimension(50, 20));
        	textField.setMaximumSize(new Dimension(50, 20));
        	textField.setMinimumSize(new Dimension(50, 20));
        	textField.addActionListener(this);
        	
        	button = new JButton();
        	button.setName(f);
        	button.setPreferredSize(new Dimension(70, 20));
        	button.setMaximumSize(new Dimension(70, 20));
        	button.setMinimumSize(new Dimension(70, 20));
        	button.setText(Integer.toString(rp.cook.FoodCount.get(f)));
        	
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
      ///synchronized(lock){
	   try
	   {
		   for(JButton b: buttons)
			{
				for(String f : rp.cook.FoodCount.keySet())
				{
					if(f == b.getName())
					{
						b.setText(Integer.toString(rp.cook.FoodCount.get(f)));
						break;
					}
				}
				if(b.getName().equals("Money"))
				{
					b.setText(Double.toString(rp.cashier.cash));
				}
			}
			if(e.getSource().getClass() == JTextField.class)
			{
				JTextField field = (JTextField) e.getSource();
				for(String f : rp.cook.FoodCount.keySet())
				{
					if(field.getName() == f)
					{
						try
						{
							int amount = Integer.parseInt(field.getText());
							rp.cook.FoodCount.put(f, amount);
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
								rp.cashier.cash = amount;
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
	   }
	   catch(ConcurrentModificationException er)
	   {
		   
	   }
         for(Gui gui : guis) {
            
                gui.updatePosition();
            
        }
      //}
      
      repaint();  //Will have paintComponent called
   }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, this.getWidth(), this.getHeight() );

        
        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(Table1fillrect1, Table1fillrect2, fillrect3, fillrect4);//200 and 250 need to be table params

        g2.setColor(Color.ORANGE);
        g2.fillRect(Table2fillrect1, Table2fillrect2, fillrect3, fillrect4);

        g2.setColor(Color.ORANGE);
        g2.fillRect(Table3fillrect1, Table3fillrect2, fillrect3, fillrect4);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(CashierTableX,CashierTableY, CashierTablefillrectX, CashierTablefillrectY);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(CustomerWaitingAreaX,CustomerWaitingAreaY,CustomerWaitingAreafillrectX,CustomerWaitingAreafillrectY);
        
        g2.setColor(Color.BLACK);
        g2.drawString("Customer Waiting Area", 20, 20);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(WaiterWaitingAreaX, WaiterWaitingAreaY, WaiterWaitingAreafillrectX, WaiterWaitingAreafillrectY);
        
        g2.setColor(Color.BLACK);
        g2.drawString("Waiter Waiting Area", 20, 380);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(CookAreaX,CookAreaY,CookAreafillrectX,CookAreafillrectY);
        
        g2.setColor(Color.BLACK);
        g2.drawString("Cook Area", 540,320);
        
        g2.setColor(Color.BLUE);
        g2.fillRect(kitchenAreaX, kitchenAreaY, kitchenAreafillrectX, kitchenAreafillrectY);
        g2.setColor(Color.BLACK);
        g2.drawString("Cooking Area", kitchenAreaX-90, kitchenAreaY+40);
        
        g2.setColor(Color.GREEN);
        g2.fillRect(platingAreaX, platingAreaY, platingAreafillrectX, platingAreafillrectY);
        g2.setColor(Color.BLACK);
        g2.drawString("Plating Area", platingAreaX-60, platingAreaY-10);
        
        g2.setColor(Color.BLACK);
        g2.fillRect(refrigeratorX,refrigeratorY,refrigeratorfillrectX,refrigeratorfillrectY);
        g2.drawString("Refrigerator", refrigeratorX, refrigeratorY-10);
        
        //synchronized(lock){
           for(Gui gui : guis) {
               if (gui.isPresent()) {
                   gui.draw(g2);
               }
           }
        //}
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
   
   public void addGui(CashierGui gui){
      guis.add(gui);
   
   }
   
   public void addGui(CookGui gui){
      guis.add(gui);
   }
   
   public Dimension getSize(){
      return new Dimension(WINDOWX,WINDOWY+50);
   }
}
