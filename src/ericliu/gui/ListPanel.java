package ericliu.gui;

import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.HostAgent;
import ericliu.restaurant.WaiterAgent;
import ericliu.restaurant.CashierAgent;
import ericliu.restaurant.MarketAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

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
    
    private JButton pauseButton=new JButton("PAUSE");
    
    private JTextField customerTextField= new JTextField();
    private JTextField waiterTextField= new JTextField();

    
    private JCheckBox hungry=new JCheckBox();
    

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
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

//        customerTextField.setMaximumSize(getPreferredSize());
//        add(customerTextField);       
//        
//        hungry.setText("Hungry?");
//        //hungry.setEnabled(false);
//        add(hungry);
//        
//        
//        addPersonB.addActionListener(this);
//        add(addPersonB);
//
//        pauseButton.addActionListener(this);
//        add(pauseButton);
//        
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
        
    }
    public void setTextField(String name){
       customerTextField.setText(name);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
     public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
           String name=customerTextField.getText();
           addPerson(name);
        }
//        else if(e.getSource()==pauseButton){
//           pauseRestaurant();
//        }
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
    public void addPerson(String name) {
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
            //restPanel.addPerson(type, name,hungry.isSelected());//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
    
//    public void pauseRestaurant(){
//       /*restPanel.getHost().msgPaused();
//       restPanel.getWaiter().msgPaused();
//       restPanel.getCook().msgPaused();
//       for(CustomerAgent customer: restPanel.getCustomers())
//          {
//             customer.msgPaused();
//          }
//       }*/
//       
//    
//       restPanel.getHost().setPaused();
//       for(WaiterAgent waiter: restPanel.getWaiters())
//       {
//          waiter.setPaused();
//       }
//       //restPanel.getWaiter().setPaused();
//       restPanel.getCook().setPaused();
//       for(CustomerAgent customer: restPanel.getCustomers())
//       {
//          customer.setPaused();
//       }
//       restPanel.getCashier().setPaused();
//       for(MarketAgent market: restPanel.getMarkets()){
//          market.setPaused();
//       }
//    }
}
