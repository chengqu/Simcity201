package animation;


/* Import your restaurant here */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

import agents.Person;
import simcity201.gui.GlobalMap;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class PersonListPanel extends JPanel implements ActionListener {

   //PANEL FOR LIST OF ADDED PEOPLE
    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private List<JButton> list = new ArrayList<JButton>();
    
    //PANEL FOR DATA MEMBERS FOR PERSON 
    private JPanel addInformation=new JPanel();
    
    public JButton addPersonB = new JButton("Add");
    
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
    private String[] homeInfo = {"Owner", "Renter"};
    private JComboBox homeInfoList=new JComboBox(homeInfo);
    
    private String[] vehicle={"Bus","Car","Walk"};
    private JComboBox vehicleList=new JComboBox(vehicle);
    
    private static ImageIcon myIcon;
    
    private JCheckBox alive=new JCheckBox();
    
    //ROLES LIST
    private String[] roles= {"Robbery", "TellerAtChaseBank", "AptOwner", "ApartmentOwner", "ApartmentRenter",
          "JonnieWalker", "houseRenter", "houseOwner", "marketManager"};
    //TASKS LIST
    private String[] objectives={"goTo", "patron", "worker", "house"};

    private JLabel picture=new JLabel();
    
    private ControlPanel controlPanel;
    private Simcity simcity;
    private String type;
    private String name;
    
    PersonEditor editor;

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
        c.gridx = 1;
        c.gridy = 7;
        homeInfoList.addActionListener(this);
        addInformation.add(homeInfoList,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 8;
        addInformation.add(new JLabel("<html><pre>  Want Car: </pre></html>"),c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 8;
        wantCarList.addActionListener(this);
        addInformation.add(wantCarList,c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 0;
        c.gridy = 9;
        addInformation.add(new JLabel("<html><pre>  Vehicle Preference: </pre></html>"),c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 9;
        vehicleList.addActionListener(this);
        addInformation.add(vehicleList,c);
        
       c.fill = GridBagConstraints.HORIZONTAL;
       c.weightx=0.5;
       c.gridx = 1;
       c.gridy = 12;
       addPersonB.addActionListener(this);
       addPersonB.addActionListener(controlPanel.editor);
       addInformation.add(addPersonB,c);
       
       add(addInformation,BorderLayout.WEST);
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
        if (e.getSource() == addPersonB) {
           String name=personTextField.getText();

           Float money_=Float.parseFloat(money.getText());
           int hungerLevel=(int)hungerLevels.getSelectedItem();  
           int age_=Integer.parseInt(age.getText());
           Float payCheck_=Float.parseFloat(payCheck.getText());
           String home=(String)homeList.getSelectedItem();
           String homeInfo=(String)homeInfoList.getSelectedItem();
           boolean wantCar;
           String vehicle_=(String)vehicleList.getSelectedItem();
           if(wantCarList.getSelectedItem()=="Yes")
              wantCar=true;
           else
              wantCar=false;
           addPerson(name, money_, hungerLevel, age_, payCheck_, home, homeInfo, wantCar, vehicle_);

        }
        else if(e.getSource()==pauseButton){
           pauseRestaurant();
        }
        else {
         
         for (JButton temp:list){
                if (e.getSource() == temp){
                }

            }
        }
    }

    public void addPerson(String name, float money, int hungerLevel, int age, float payCheck, String home, String homeInfo, boolean wantCar, String vehicle) {

        if (name != null) {
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

            controlPanel.addPerson(name, money, hungerLevel, age, payCheck, home, homeInfo, wantCar, vehicle);//puts customer on list
            controlPanel.showInfo(type, name);
            validate();
        }
    }
    
    public void pauseRestaurant(){
    	
    }
}
