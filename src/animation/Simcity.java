package animation;



import javax.swing.*;

import agents.Person;

import Buildings.Building;
import simcity201.gui.Bank;
import simcity201.gui.GlobalMap;
import simcity201.gui.GlobalMap.BuildingType;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class Simcity extends JPanel {


    private JPanel restLabel = new JPanel();

	
   private ArrayList<Person> persons= new ArrayList<Person>();


    private SimcityGui gui; //reference to main gui
    private  Date date =  Calendar.getInstance().getTime();
    public boolean sleep;
    public boolean start = true;
 
    Timer timer = new Timer();
    Person p = new Person("jj");
    GlobalMap map;
    
    public Simcity(SimcityGui gui) {
        this.gui = gui;
        add(restLabel);
        
        

        /* Add buildings here */
        map = GlobalMap.getGlobalMap();

        map.addBuilding(BuildingType.RyanRestaurant, 320, 170, 65, 65, "rest1");
        map.addBuilding(BuildingType.RyanRestaurant, 150, 100, 100, 100, "rest2");
        map.addBuilding(BuildingType.LynRestaurant, 500, 500, 100, 100, "rest3");
        map.addBuilding(BuildingType.House, 400, 400, 50, 50, "h");
        map.addBuilding(BuildingType.Apartment, 600, 400, 25, 25, "apartment");
        map.addBuilding(BuildingType.Bank, 700, 200, 50, 50, "bank");
        
        guehochoi.gui.RestaurantGui rest1 = (guehochoi.gui.RestaurantGui)map.searchByName("rest1");
        guehochoi.gui.RestaurantGui rest2 = (guehochoi.gui.RestaurantGui)map.searchByName("rest2");
        LYN.gui.RestaurantGui rest3 = (LYN.gui.RestaurantGui)map.searchByName("rest3");
        House.gui.HousePanelGui h = (House.gui.HousePanelGui)map.searchByName("h");
        Buildings.ApartmentComplex a = (Buildings.ApartmentComplex)map.searchByName("apartment");
        Bank bank = (Bank)map.searchByName("bank");
        
        bank.addCustomer(new Person("Customer"));
        bank.addTeller(new Person("Teller"));
        rest1.restPanel.addPerson("Waiters", "w1");
        rest1.restPanel.addPerson("Customers", "Chicken");
        rest2.restPanel.addPerson("Waiters", "w2");
        rest2.restPanel.addPerson("Customers", "d");
        rest3.restPanel.addPerson("Customers", "hi", true);
        rest3.restPanel.addWaiter("Waiters", "hello");
        h.restPanel.addOwner(p);
        a.addOwner(p);
        //a.addRenter(p);



        
    }
    
    public boolean timetosleep(){
    	//return true;
    	return ((Math.abs(Calendar.getInstance().getTime().getMinutes()-date.getMinutes())%1 == 0) && (Calendar.getInstance().getTime().getMinutes()!=date.getMinutes())&& (Calendar.getInstance().getTime().getSeconds()==date.getSeconds() ));
    }
    
    public void setNewTime() {
    	date = Calendar.getInstance().getTime();
    }
    public boolean timetowakeup(){
    	
    	return (!(Math.abs((Calendar.getInstance().getTime().getSeconds()- date.getSeconds()))%10 == 0));
    	
    }

 
}
