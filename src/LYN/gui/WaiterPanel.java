package LYN.gui;

import LYN.CustomerAgent;
import LYN.HostAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class WaiterPanel extends JPanel implements ActionListener {

   
    public JScrollPane pane1 =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
 
    private JButton addWaiter = new JButton("Add");
    private JButton breakbutton = new JButton("Break");
   
    private JTextField text1 = new JTextField();
    private RestaurantPanel restPanel;
 
    
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public WaiterPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

        
        addWaiter.addActionListener(this);
        add(addWaiter);
        breakbutton.addActionListener(this);
        add(breakbutton);
        text1.setMaximumSize(new Dimension(120,40));
        add(text1);
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        
        pane1.setViewportView(view);
        add(pane1);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == breakbutton ) {
        	restPanel.breakwaiter();
        }
        else
        if (e.getSource() == addWaiter) {
        	addwaiter(text1.getText());
        }
        else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
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
  
   
    
    public void addwaiter(String name) {
    	if (name != null) {
    	
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane1.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            
          // restPanel.addWaiter(type, name);//puts customer on list
           restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    	
    }
}
