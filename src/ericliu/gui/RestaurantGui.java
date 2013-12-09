package ericliu.gui;

import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.WaiterAgent;
import agents.Person;
import agents.Role;
import animation.BaseAnimationPanel;
import Buildings.Building;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends Building implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */  
	
    public RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JTextField textField;
    private JCheckBox onBreak;
    
    private JButton pauseButton;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 450;
        int WINDOWY = 750;

//        setLayout(new BorderLayout(5,10));
//        setBounds(20, 50, WINDOWX+700, WINDOWY);
        
       /* animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setLayout(new BorderLayout());
        animationFrame.setBounds(50, 50 , WINDOWX, WINDOWY);
        animationFrame.setVisible(true);
    	  animationFrame.add(animationPanel); */
    	
    	 /*Dimension animationDim=new Dimension(WINDOWX+250, (int) (WINDOWY * .6));
       animationPanel.setPreferredSize(animationDim);
       animationPanel.setMinimumSize(animationDim);
       animationPanel.setMaximumSize(animationDim);*/
       
    	 
    	 //add(animationPanel, BorderLayout.EAST);
    	//animationPanel.setLayout(new GridLayout(1, 2, 30, 0));
    	//add(animationPanel,BorderLayout.EAST);
       // setLayout(new BoxLayout((Container) getContentPane(), 
        	//	BoxLayout.Y_AXIS));
    	   
    	  // setLayout(new BorderLayout(5,10));
    	
    	  //pauseButton=new JButton("PAUSE");
        //pauseButton.addActionListener(this);
        
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        
        
        //add(restPanel,BorderLayout.WEST);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        onBreak = new JCheckBox();
        onBreak.setVisible(false);
        onBreak.addActionListener(this);
        
        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        infoPanel.add(onBreak);
        //add(infoPanel,BorderLayout.SOUTH);
        animationPanel.setRestPanel(restPanel);
    }
    
  
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            CustomerAgent customer = (CustomerAgent) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        if(person instanceof WaiterAgent){
           onBreak.setVisible(true);
           WaiterAgent waiter=(WaiterAgent) person;
           onBreak.setText("Break?");
           onBreak.setSelected(waiter.getGui().onBreak());
           onBreak.setEnabled(!waiter.getGui().onBreak());
           stateCB.setText("Working?");
           stateCB.setSelected(waiter.getGui().isWorking());
           stateCB.setEnabled(!waiter.getGui().isWorking());
           infoLabel.setText(
                 "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        }
        infoPanel.validate();
    } 
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerAgent) {
                CustomerAgent c = (CustomerAgent) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
            else if (currentPerson instanceof WaiterAgent) {
               WaiterAgent w = (WaiterAgent) currentPerson;
               w.getGui().setWorking(true);
               stateCB.setEnabled(false);
               w.getGui().setBreak(false);
           }
        }
        else if(e.getSource() == onBreak){
           if (currentPerson instanceof WaiterAgent) {
              WaiterAgent w = (WaiterAgent) currentPerson;
              w.getGui().setBreak(true);
              onBreak.setEnabled(false);
              w.getGui().setWorking(false);
           }
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) {
        if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    public void setWaiterEnabled(WaiterAgent w) {
       if (currentPerson instanceof WaiterAgent) {
           WaiterAgent wait = (WaiterAgent) currentPerson;
           if (w.equals(wait)) {
               //w.getGui().setWorking(true);
               stateCB.setEnabled(true);
               stateCB.setSelected(false);
           }
       }
   }
    
    /**
     * Main routine to get gui started
     */
//    /*
//    public static void main(String[] args) {
//        RestaurantGui gui = new RestaurantGui();
//        gui.setTitle("csci201 Restaurant");
//        gui.setVisible(true);
//        gui.setResizable(false);
//        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//    */
    
    public BaseAnimationPanel getAnimationPanel(){
       return this.animationPanel;
    }
    
    public RestaurantPanel getRestPanel(){
       return this.restPanel;
    }


	@Override
	public Role wantJob(Person p) {
		// TODO Auto-generated method stub
		return null;
	}
}
