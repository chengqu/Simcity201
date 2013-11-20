package animation;


/* Import your restaurant here */
import guehochoi.gui.AnimationPanel;
import guehochoi.gui.RestaurantGui;
import guehochoi.gui.RestaurantPanel;










import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.swing.*;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class PersonListPanel extends JPanel implements ActionListener {

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
    
    private ControlPanel controlPanel;
    private Simcity simcity;
    private String type;
    private String name;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public PersonListPanel(ControlPanel cp) {
        controlPanel = cp;
        //this.simcity=simcity;
        this.type = type;

//        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("<html><pre> <u> Persons </u><br></pre></html>"),c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 1;
        customerTextField.setMaximumSize(getPreferredSize());
        add(customerTextField,c);       
        
        hungry.setText("Alive?");
        //hungry.setEnabled(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 1;
        add(hungry,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 2;
        addPersonB.addActionListener(this);
        add(addPersonB,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 2;
        pauseButton.addActionListener(this);
        add(pauseButton,c);
        
        
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 300;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 3;  
        add(pane, c);
        
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
        else if(e.getSource()==pauseButton){
           pauseRestaurant();
        }
        else {
         
         for (JButton temp:list){
                if (e.getSource() == temp){
                    //controlPanel.showInfo(type, temp.getText());
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
            controlPanel.addPerson(type, name,hungry.isSelected());//puts customer on list
            controlPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
    
    public void pauseRestaurant(){
       /*restPanel.getHost().msgPaused();
       restPanel.getWaiter().msgPaused();
       restPanel.getCook().msgPaused();
       for(CustomerAgent customer: restPanel.getCustomers())
          {
             customer.msgPaused();
          }
       }*/
       
    
//       controlPanel.getHost().setPaused();
//       for(WaiterAgent waiter: controlPanel.getWaiters())
//       {
//          waiter.setPaused();
//       }
//       //restPanel.getWaiter().setPaused();
//       controlPanel.getCook().setPaused();
//       for(CustomerAgent customer: controlPanel.getCustomers())
//       {
//          customer.setPaused();
//       }
//       controlPanel.getCashier().setPaused();
//       for(MarketAgent market: controlPanel.getMarkets()){
//          market.setPaused();
//       }
    }
}
