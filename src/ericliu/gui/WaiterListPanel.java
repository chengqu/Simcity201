package ericliu.gui;

import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.HostAgent;
import ericliu.restaurant.WaiterAgent;

import javax.swing.*;

import agents.Person;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class WaiterListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addWaiterB= new JButton("Add");
    private JButton goOnBreak= new JButton("Break");
    private JButton goOFFBreak= new JButton("Resume Work");

    

    private JTextField waiterTextField= new JTextField();

    

    private JCheckBox working=new JCheckBox();

    //private JTextField EnterName=new JTextField("Enter Name Here",30);
    
    //private TextHandler nameFieldHandler=new TextHandler();
    
    private RestaurantPanel restPanel;
    private String type;
    private String name;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public WaiterListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

        
//        waiterTextField.setMaximumSize(getPreferredSize());
//        add(waiterTextField);
//        
//        working.setText("Working?");
//        //hungry.setEnabled(false);
//        add(working);
//        
//        addWaiterB.addActionListener(this);
//        add(addWaiterB);
//
//        goOnBreak.addActionListener(this);
//        add(goOnBreak);
//        
//        goOFFBreak.addActionListener(this);
//        add(goOFFBreak);
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
        
    }
 
    public void setTextField(String name){
       waiterTextField.setText(name);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
     public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addWaiterB) {
         // Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
           String name=waiterTextField.getText();
           //addWaiter(name);
        }
        else if(e.getSource()==goOnBreak){
           String name=waiterTextField.getText();
           goOnBreak(name);
           restPanel.showInfo(type, name);
           setTextField(name);

        }
        else if(e.getSource()==goOFFBreak){
           String name=waiterTextField.getText();
           goOFFBreak(name);
           restPanel.showInfo(type, name);
           setTextField(name);
        }
        else {
         
         for (JButton temp:list){
                if (e.getSource() == temp){
                    restPanel.showInfo(type, temp.getText());
                    setTextField(temp.getText());
                }
            }
        }
    }

     
    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addWaiter(Person person) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addWaiter(type, person,working.isSelected());//puts customer on list
            restPanel.showInfo(type, name);//puts working button on panel
            validate();
        }
    }
    
    public void goOnBreak(String name){
       //WaiterAgent tempWaiter= null;
       for(WaiterAgent waiter: restPanel.getWaiters()){
          if(waiter.getName().equals(name)){
             //tempWaiter=waiter;
             waiter.msgWantToGoOnBreak();
             waiter.getGui().setWorking(false);
             waiter.getGui().setBreak(true);
          }
          
       }
//       tempWaiter.msgWantToGoOnBreak();
//       tempWaiter.getGui().setWorking(false);
//       tempWaiter.getGui().setBreak(true);
       restPanel.showInfo(type, name);
    }
    
    public void goOFFBreak(String name){
       //WaiterAgent tempWaiter= null;
       for(WaiterAgent waiter: restPanel.getWaiters()){
          if(waiter.getName().equals(name)){
             //tempWaiter=waiter;
             waiter.msgStartWorking();
             waiter.getGui().setWorking(true);
             waiter.getGui().setBreak(false);
          }
          
       }
//       tempWaiter.msgStartWorking();
//       tempWaiter.getGui().setWorking(true);
//       tempWaiter.getGui().setBreak(false);
       restPanel.showInfo(type, name);
    }
    
   
}
