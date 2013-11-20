package gui;

import javax.swing.*;

import agents.HousePerson;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private HousePerson house = new HousePerson("Sarah");
    

   

    private JPanel restLabel = new JPanel();
    
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui
    private HouseGui houseGui = new HouseGui(house,gui);

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        house.setGui(houseGui);

        gui.animationPanel.addGui(houseGui);
        house.startThread();
        //houseGui.setPresent(true);
        houseGui.setHungry();
       

       // initRestLabel();
       // add(restLabel);
    }

}
