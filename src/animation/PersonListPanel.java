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
import java.awt.FlowLayout;
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

   //PANEL FOR LIST OF ADDED PEOPLE
    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    
    //PANEL FOR DATA MEMBERS FOR PERSON 
    private JPanel addInformation=new JPanel();
    
    private JButton addPersonB = new JButton("Add");
    
    private JButton pauseButton=new JButton("PAUSE");
    
    private JTextField personTextField= new JTextField();

    private JTextField waiterTextField= new JTextField();
    
    
    private JTextField money= new JTextField();
    
    private Integer[] hungers={10,20,30,40,50};

    private JComboBox hungerLevels=new JComboBox(hungers);
   
    private JTextField payCheck = new JTextField();
    private JTextField age = new JTextField();
    
    private String[] wantCar = {"Yes", "No"};
    private JComboBox wantCarList=new JComboBox(wantCar);

    private String[] home = {"apart", "House1"};
    private JComboBox homeList=new JComboBox(home);
    
    private static ImageIcon myIcon;
    
    private JCheckBox alive=new JCheckBox();
    
    //ROLES LIST
    private String[] roles= {"Robbery", "TellerAtChaseBank", "AptOwner", "ApartmentOwner", "ApartmentRenter",
          "JonnieWalker", "houseRenter", "houseOwner", "marketManager"};
    //TASKS LIST
    private String[] objectives={"goTo", "patron", "worker", "house"};
    private JComboBox objectiveList=new JComboBox(objectives);
    private JTextField location=new JTextField();
    private String[] sTasks={"none", "eatAtHome", "eatAtApartment", "buyGroceries", 
          "payBills", "sleepAtHome", "buyCar", "takeBus", "takeCar", "walk", "sleepAtApartment",
          "takeOutLoan", "depositMoney", "openBankAccount"};
    private JComboBox sTasksList=new JComboBox(sTasks);
    
    private JComboBox rolesList = new JComboBox(roles);

    private JLabel picture=new JLabel();
    
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
        setLayout(new BorderLayout());
        
        Dimension addDim = new Dimension(300, 400);
        addInformation.setPreferredSize(addDim);
        addInformation.setMinimumSize(addDim);
        addInformation.setMaximumSize(addDim);
        addInformation.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 0;
        addInformation.add(new JLabel("<html><pre> <u> Persons </u><br></pre></html>"),c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 1;
        addInformation.add(new JLabel("<html><pre>  Name: </pre></html>"),c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 1;
        personTextField.setMaximumSize(getPreferredSize());
        addInformation.add(personTextField,c);       
        
        //alive.setText("Alive?");
        //hungry.setEnabled(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 1;
        //add(alive,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=1;
        c.gridx = 3;
        c.gridy = 4;
        //add(picture, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 2;
        addInformation.add(new JLabel("<html><pre>  Money: </pre></html>"),c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 2;
        money.addActionListener(this);
        addInformation.add(money,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 3;
        addInformation.add(new JLabel("<html><pre>  Age: </pre></html>"),c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 3;
        age.addActionListener(this);
        addInformation.add(age,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 4;
        addInformation.add(new JLabel("<html><pre>  PayCheck: </pre></html>"),c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 4;
        payCheck.addActionListener(this);
        addInformation.add(payCheck,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 5;
        addInformation.add(new JLabel("<html><pre>  Hunger Level: </pre></html>"),c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 5;
        hungerLevels.addActionListener(this);
        addInformation.add(hungerLevels,c);
        
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx=0.5;
//        c.gridx = 0;
//        c.gridy = 5;
//        add(new JLabel("<html><pre>  Role: </pre></html>"),c);
//        
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx=0.5;
//        c.gridx = 1;
//        c.gridy = 5;
//        rolesList.addActionListener(this);
//        add(rolesList,c);
    
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 6;
        addInformation.add(new JLabel("<html><pre>  Home: </pre></html>"),c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 6;
        homeList.addActionListener(this);
        addInformation.add(homeList,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 7;
        addInformation.add(new JLabel("<html><pre>  Want Car: </pre></html>"),c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 7;
        wantCarList.addActionListener(this);
        addInformation.add(wantCarList,c);
        
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx=0.5;
//        c.gridx = 0;
//        c.gridy = 8;
//        add(new JLabel("<html><pre>  Task: </pre></html>"),c);
//        
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx=0.5;
//        c.gridx = 1;
//        c.gridy = 8;
//        objectiveList.addActionListener(this);
//        add(objectiveList,c);
//        
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx=0.5;
//        c.gridx = 1;
//        c.gridy = 9;
//        location.addActionListener(this);
//        add(location,c);
        
//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx=0.5;
//        c.gridx = 1;
//        c.gridy = 10;
//        sTasksList.addActionListener(this);
//        add(sTasksList,c);
        
           c.fill = GridBagConstraints.HORIZONTAL;
           c.weightx=0.5;
           c.gridx = 1;
           c.gridy = 8;
           addPersonB.addActionListener(this);
           addInformation.add(addPersonB,c);
           
           add(addInformation,BorderLayout.WEST);
           
     

//        c.fill = GridBagConstraints.HORIZONTAL;
//        c.weightx=0.5;
//        c.gridx = 1;
//        c.gridy = 4;
//        pauseButton.addActionListener(this);
//        add(pauseButton,c);
//        
        

           
           Dimension paneDim = new Dimension(200, 550);
           pane.setPreferredSize(paneDim);
           pane.setMinimumSize(paneDim);
           pane.setMaximumSize(paneDim);
           view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
           pane.setViewportView(view);
           add(pane,BorderLayout.EAST);
    
        
    }
    public void setTextField(String name){
       personTextField.setText(name);
    }
    
    protected void updateLabel(String name) {
       ImageIcon icon = createImageIcon(name + ".png");
       picture.setIcon(icon);
       picture.setToolTipText("A drawing of a " + name.toLowerCase());
       if (icon != null) {
           picture.setText(null);
       } else {
           picture.setText("Image not found");
       }
    }

    protected static ImageIcon createImageIcon(String path) {
//       myIcon = new ImageIcon(PersonListPanel.class.getResource(path));
//       Image img1 = myIcon.getImage();
       java.net.URL imgURL = PersonListPanel.class.getResource(path);
       if (imgURL != null) {
           return new ImageIcon(imgURL);
       } else {
           System.err.println("Couldn't find file: " + path);
           return null;
       }
   }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
     public void actionPerformed(ActionEvent e) {
        if(e.getSource()==rolesList){
           JComboBox cb = (JComboBox)e.getSource();
           JLabel picture=new JLabel();
           String role = (String)cb.getSelectedItem();
//           if(occupation=="Waiter"){
//              picture=waiterPic;
//           }
//           else if(occupation=="Cook"){
//              picture=cookPic;
//           }
//           else if(occupation=="Bank Teller"){
//              picture=bankTellerPic;
//           }
           updateLabel(role);
        }
        if (e.getSource() == addPersonB) {
         // Chapter 2.19 describes showInputDialog()
            //addPerson(JOptionPane.showInputDialog("Please enter a name:"));
           String name=personTextField.getText();

           //String role = (String)rolesList.getSelectedItem();
           Float money_=Float.parseFloat(money.getText());
           int hungerLevel=(int)hungerLevels.getSelectedItem();  
           int age_=Integer.parseInt(age.getText());
           Float payCheck_=Float.parseFloat(payCheck.getText());
           String home=(String)homeList.getSelectedItem();
           boolean wantCar;
           if(wantCarList.getSelectedItem()=="Yes")
              wantCar=true;
           else
              wantCar=false;
           addPerson(name, money_, hungerLevel, age_, payCheck_, home, wantCar);

        }
        else if(e.getSource()==pauseButton){
           pauseRestaurant();
        }
        else {
         
         for (JButton temp:list){
                if (e.getSource() == temp){
//                    controlPanel.showInfo(type, temp.getText());
//                    setTextField(temp.getText());
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

    public void addPerson(String name, float money, int hungerLevel, int age, float payCheck, String home, boolean wantCar) {

        if (name != null) {
//            JButton button = new JButton("Name: "+name+" ; Job: "+occupation);
           JButton button = new JButton("Name: "+name);
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

            //controlPanel.addPerson(type, name,alive.isSelected());//puts customer on list
            controlPanel.addPerson(name, money, hungerLevel, age, payCheck, home, wantCar);//puts customer on list
            controlPanel.showInfo(type, name);
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
