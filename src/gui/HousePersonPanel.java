package gui;

import javax.swing.*;

import agents.HousePerson;
import agents.Person;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class HousePersonPanel extends JPanel {

    //Host, cook, waiters and customers
	Person p;
    private HousePerson house = new HousePerson(p);
    

   

    private JPanel restLabel = new JPanel();
    
    private JPanel group = new JPanel();

    private HousePanelGui gui; //reference to main gui
    private HouseGui houseGui = new HouseGui(house,gui);

    public HousePersonPanel(HousePanelGui gui) {
        this.gui = gui;
        house.setGui(houseGui);

        gui.animationPanel.addGui(houseGui);
        house.startThread();
        houseGui.setPresent(true);
       // houseGui.setHungry();
        house.msgRestathome();
       

       // initRestLabel();
       // add(restLabel);
    }
    
    public void addOwner(Person p)
    {
            
            
            HousePerson r = new HousePerson(p);
            r.startThread();
            HouseGui houseGui = new HouseGui(r,gui);
            r.setGui(houseGui);
            gui.animationPanel.addGui(houseGui);
            houseGui.setPresent(true);
            
            
            //add this gui to some sort of animation gui
            
          
    }
    
    public void addRenter(Person p)
    {
    	 HousePerson r = new HousePerson(p);
    	 r.startThread();
         HouseGui houseGui = new HouseGui(r,gui);
         r.setGui(houseGui);
         gui.animationPanel.addGui(houseGui);
         houseGui.setPresent(true);
         
    }
   
}
