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

        map.addBuilding(BuildingType.DavidRestaurant, 695, 265, 80, 80, "Rest1");
        map.addBuilding(BuildingType.RyanRestaurant, 695, 535, 80, 80, "Rest2");
        map.addBuilding(BuildingType.LynRestaurant, 845, 265, 80, 80, "Rest3");
        map.addBuilding(BuildingType.EricRestaurant, 845, 535, 80, 80, "Rest4");
        map.addBuilding(BuildingType.JoshRestaurant, 995, 265, 80, 80, "Rest5");
        map.addBuilding(BuildingType.ChengRestaurant, 995, 535, 80, 80, "Rest6");
        map.addBuilding(BuildingType.House, 695, 130, 80, 80, "House1");
        map.addBuilding(BuildingType.Apartment, 200, 525, 150, 100, "Apartment");
        map.addBuilding(BuildingType.Bank, 200, 120, 150, 80, "Bank");
        
        
        david.restaurant.gui.RestaurantGui rest1 = (david.restaurant.gui.RestaurantGui)map.searchByName("Rest1");
        guehochoi.gui.RestaurantGui rest2 = (guehochoi.gui.RestaurantGui)map.searchByName("Rest2");
        LYN.gui.RestaurantGui rest3 = (LYN.gui.RestaurantGui)map.searchByName("Rest3");
        ericliu.gui.RestaurantGui rest4=(ericliu.gui.RestaurantGui)map.searchByName("Rest4");
       josh.restaurant.gui.RestaurantGui rest5 = (josh.restaurant.gui.RestaurantGui)map.searchByName("Rest5");
       Cheng.gui.RestaurantGui rest6 = (Cheng.gui.RestaurantGui)map.searchByName("Rest6");
        House.gui.HousePanelGui h = (House.gui.HousePanelGui)map.searchByName("House1");
        Buildings.ApartmentComplex a = (Buildings.ApartmentComplex)map.searchByName("Apartment");
        Bank bank = (Bank)map.searchByName("Bank");
        
        
        bank.addCustomer(new Person("Customer"));
        bank.addTeller(new Person("Teller"));
        rest1.restPanel.addPerson("Waiters", "w1");
        //rest1.restPanel.addPerson("Customers", "Chicken");
        rest2.restPanel.addPerson("Waiters", "w2");
        rest2.restPanel.addPerson("Customers", "d");
        rest3.restPanel.addPerson("Customers", "hi", true);
        rest3.restPanel.addWaiter("Waiters", "hello");
        rest4.restPanel.addWaiter("Waiters", "w2",true);
        rest4.restPanel.addPerson("Customers", "d",true);

        rest5.restPanel.AddCustomer(new Person("lkdsfj"));
        rest5.restPanel.addPerson("Waiters", "dsf", false);
        
        rest6.restPanel.addPerson("Customers", "asdf", 1);
        Person renter=new Person("renter");
        renter.setHungerLevel(21);
        a.addRenter(renter);

        h.housePanel.addOwner(p);
        a.addOwner(p);
        //a.addRenter(p);


        map.startAllPeople();
        
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
