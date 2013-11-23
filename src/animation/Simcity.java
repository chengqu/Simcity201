
package animation;



import javax.swing.*;

import Buildings.Building;
import simcity201.gui.GlobalMap;
import simcity201.gui.GlobalMap.BuildingType;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class Simcity extends JPanel {

    private JPanel restLabel = new JPanel();

    private SimcityGui gui; //reference to main gui
    private  Date date =  Calendar.getInstance().getTime();
    public boolean sleep;
    public boolean start = true;
 
    Timer timer = new Timer();
   
    GlobalMap map;
    
    public Simcity(SimcityGui gui) {
        this.gui = gui;
        add(restLabel);
        
        
        /* Add buildings here */
        map = GlobalMap.getGlobalMap();
        map.addBuilding(BuildingType.DavidRestaurant, 320, 170, 65, 65, "rest1");
        map.addBuilding(BuildingType.DavidRestaurant, 150, 100, 100, 100, "rest2");
        david.restaurant.gui.RestaurantGui rest1 = (david.restaurant.gui.RestaurantGui)map.searchByName("rest1");
        david.restaurant.gui.RestaurantGui rest2 = (david.restaurant.gui.RestaurantGui)map.searchByName("rest2");
        rest1.restPanel.addPerson("Waiters", "w1");
        rest1.restPanel.addPerson("Customers", "Chicken");
        rest2.restPanel.addPerson("Waiters", "w2");
        rest2.restPanel.addPerson("Customers", "d");
        
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
