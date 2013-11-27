package animation;



import javax.swing.*;

import agents.BusAgent;
import agents.CarAgent;
import agents.Person;
import agents.Role;
import Buildings.Building;
import simcity201.gui.Bank;
import simcity201.gui.BusGui;
import simcity201.gui.CarGui;
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

	
//   private ArrayList<Person> persons= new ArrayList<Person>();
    private Vector<Person> people = new Vector<Person>();

    public int day = 0;

    private SimcityGui gui; //reference to main gui
    private  Date date =  Calendar.getInstance().getTime();
    public boolean sleep;
    public boolean start = true;
 
    Timer timer = new Timer();
    GlobalMap map;
    
    Person p;
    
    
    public Simcity(SimcityGui gui) {
        this.gui = gui;
        add(restLabel);
        
        

        /* Add buildings here */
        map = GlobalMap.getGlobalMap();


        map.addBuilding(BuildingType.Store, 400, 160, 100, 200, "Market");
        map.addBuilding(BuildingType.DavidRestaurant, 695, 265, 100, 100, "Rest1");
        map.addBuilding(BuildingType.RyanRestaurant, 695, 535, 100, 100, "Rest2");
        map.addBuilding(BuildingType.LynRestaurant, 845, 265, 100, 100, "Rest3");
        map.addBuilding(BuildingType.EricRestaurant, 845, 535, 100, 100, "Rest4");
        //map.addBuilding(BuildingType.JoshRestaurant, 995, 265, 100, 100, "Rest5");
        map.addBuilding(BuildingType.ChengRestaurant, 995, 535, 100, 100, "Rest6");
        map.addBuilding(BuildingType.House, 695, 130, 100, 100, "House1");

        map.addBuilding(BuildingType.Apartment, 200, 525, 150, 100, "Apart");
        map.addBuilding(BuildingType.Bank, 200, 120, 150, 80, "Bank");
        
        david.restaurant.gui.RestaurantGui rest1 = (david.restaurant.gui.RestaurantGui)map.searchByName("Rest1");
        guehochoi.gui.RestaurantGui rest2 = (guehochoi.gui.RestaurantGui)map.searchByName("Rest2");
        LYN.gui.RestaurantGui rest3 = (LYN.gui.RestaurantGui)map.searchByName("Rest3");
        ericliu.gui.RestaurantGui rest4=(ericliu.gui.RestaurantGui)map.searchByName("Rest4");
        //josh.restaurant.gui.RestaurantGui rest5 = (josh.restaurant.gui.RestaurantGui)map.searchByName("Rest5");
        Cheng.gui.RestaurantGui rest6 = (Cheng.gui.RestaurantGui)map.searchByName("Rest6");
        House.gui.HousePanelGui h = (House.gui.HousePanelGui)map.searchByName("House1");
        Buildings.ApartmentComplex a = (Buildings.ApartmentComplex)map.searchByName("Apart");
        Bank bank = (Bank)map.searchByName("Bank");
        
        BusAgent bus = new BusAgent("Bank","Bus1Crossing1","Market","Bus1Crossing2","Restaurants1","Bus1Crossing3","Restaurants2","Bus1Crossing4","House","Bus1Crossing5","Terminal1",1);
        BusGui busGui = new BusGui(bus,"Terminal1");
        
        bus.setGui(busGui);

        bus.startThread();
        
        map.buses.add(bus);
        SimcityPanel.guis.add(busGui);
        
        
        
        
        //bank.addCustomer(new Person("Customer"));
        bank.addTeller(new Person("Teller"));
        //bank.addCustomer(new Person("Customer"));
        rest1.restPanel.addPerson("Waiters", "w1");
        //rest1.restPanel.addPerson("Customers", "Chicken");
        rest2.restPanel.addPerson("Waiters", "w2");
        //rest2.restPanel.addPerson("Customers", "d");


        //rest3.restPanel.addPerson("Customers", "hi", true);
        rest3.restPanel.addWaiter("Waiters", "hello");

        rest4.restPanel.addWaiter("Waiters", "Waiter", true);


        //rest5.restPanel.AddCustomer(new Person("lkdsfj"));
        //rest5.restPanel.addPerson("Waiters", "dsf", false);
        
        //rest6.restPanel.addPerson("Customers", "asdf", 1);
     
        

        //map.addPerson(null, "joey");

        
        p = new Person("joey");

//      
        a.addOwner(p);
        p.house = h;
        p.roles.add(new Role(Role.roles.preferBus, null));

        //a.addRenter(p);

        p.hungerLevel = 30;
        p.money = 400;
        p.wantCar = false;
        p.payCheck = 300;

        p.roles.add(new Role(Role.roles.ApartmentOwner, a.name));
//        

        //p.roles.add(new Role(Role.roles.ApartmentRenter, p.complex.name));
        

        p.startThread();

        //map.startAllPeople();
        
    }
    
    public boolean timetosleep(){
    	//return true;
    	boolean a = ((Math.abs(Calendar.getInstance().getTime().getMinutes()-date.getMinutes())%3 == 0) &&
    			(Calendar.getInstance().getTime().getMinutes()!=date.getMinutes())&& (Calendar.getInstance().getTime().getSeconds()==date.getSeconds() ));
    	
    	if(a)
    	{
    		day++;
    		if(day == 7)
    		{
    			for(Person p: GlobalMap.getGlobalMap().getListOfPeople())
    			{
    				p.houseBillsToPay++;
    				
    			}
    			day = 0;
    		}
    	}
    	return a;
    }
   
    
    public void setNewTime() {
    	date = Calendar.getInstance().getTime();
    }
    public boolean timetowakeup(){
    	
    	return (!(Math.abs((Calendar.getInstance().getTime().getSeconds()- date.getSeconds()))%10 == 0));
    	
    }
    
    public void addPerson(Person p, String home, String homeInfo, String vehicle){
//       if(home=="apart"){
//          Buildings.ApartmentComplex a = (Buildings.ApartmentComplex)map.searchByName("Apart");
//          if(homeInfo=="Renter"){
//             p.roles.add(new Role(Role.roles.ApartmentRenter, "Apart"));
//             a.addRenter(p);
//          }
//          else if(homeInfo=="Owner"){
//             p.roles.add(new Role(Role.roles.ApartmentOwner, "Apart"));
//             a.addOwner(p);
//          }
//       }
//       else if(home=="House1"){
//          House.gui.HousePanelGui h = (House.gui.HousePanelGui)map.searchByName("House1");
////          if(homeInfo=="Renter"){
////             p.roles.add(new Role(Role.roles.houseRenter, "Apart"));
////             h.addRenter(p);
////          }
////          else if(homeInfo=="Owner"){
////             p.roles.add(new Role(Role.roles.houseOwner, "Apart"));
////             h.addOwner(p);
////          }
//          h.addOwner(p);
  //     }
       Buildings.ApartmentComplex a = (Buildings.ApartmentComplex)map.searchByName("Apart");
       a.addRenter(p);
       p.roles.add(new Role(Role.roles.ApartmentRenter, "Apart"));
       
       if(vehicle.equalsIgnoreCase("Bus")){
          p.roles.add(new Role(Role.roles.preferBus,null));
       }
       else if(vehicle.equalsIgnoreCase("Car")){
          p.roles.add(new Role(Role.roles.preferCar,null));
       }
       else{
          p.roles.add(new Role(Role.roles.JonnieWalker,null));
       }
       people.add(p);
       p.startThread();
       
    }

 
}
