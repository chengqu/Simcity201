package josh.restaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

import josh.restaurant.CustomerAgent;
import josh.restaurant.HostAgent;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    
    private JButton pauseB = new JButton("pause"); 

    private RestaurantPanel restPanel;
    private String type;
    
    private JTextField nameEnter = new JTextField();
    private JCheckBox initCheckBox;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters and can handle both
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;
        
        pauseB.addActionListener(this);
        add(pauseB); 
        
        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        
        addPersonB.addActionListener(this);
        add(addPersonB);
        
        nameEnter.setMaximumSize(getPreferredSize());
        add(nameEnter);
        
        //hungerCheck.
        if (type == "Customers") {
        	initCheckBox = new JCheckBox("Hungry?"); 
        	add(initCheckBox); 
        }

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
        
    }
    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
        	addPerson(nameEnter.getText());
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        }
        else if (e.getSource() == pauseB) {	
        	
        	if (pauseB.getText() == "pause") {
        		pauseB.setText("resume");
        	}
        	else {
        		pauseB.setText("pause");
        	}
        	restPanel.pauseResume(type);
        }
        else {
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
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
    public void addPerson(String name) {
        
    	//this stuff messes up the dynamics of my stateCB button...
    	/*
    	if (name.equals("") && type.equals("Customers")) {
        	name = "cust dude";
        }
        else if (name.equals("")) {
        	name = "waiter dude"; 
        }
        */
        	
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
            
        if (type == "Customers")
        	restPanel.addPerson(type, name, initCheckBox.isSelected()); //puts customer on list
        else 
            restPanel.addPerson(type, name, false);
            
        restPanel.showInfo(type, name);  
        validate();
            
    }
}
