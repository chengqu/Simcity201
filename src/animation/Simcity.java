package animation;



import javax.swing.*;

import configuration.Configuration;
import agents.BusAgent;
import agents.CarAgent;
import agents.Person;
import agents.Role;
import agents.Role.roles;
import Buildings.Building;
import simcity201.gui.Bank;
import simcity201.gui.BusGui;
import simcity201.gui.CarGui;
import simcity201.gui.GlobalMap;
import simcity201.gui.GlobalMap.BuildingType;
import simcity201.gui.GlobalTime;







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

    public int day = 0;

    private SimcityGui gui; //reference to main gui
    private  Date date =  Calendar.getInstance().getTime();
    private Date newDay = Calendar.getInstance().getTime();
    public boolean sleep;
    public boolean start = true;
 
    Timer timer = new Timer();
    GlobalMap map;
    
    Person p;
    
    
    public Simcity(SimcityGui gui) {
//    	
//        this.gui = gui;
//        add(restLabel);
//        
//        
//
//        /* Add buildings here */
//        map = GlobalMap.getGlobalMap();
//
//
////        map.addBuilding(BuildingType.Store, 400, 160, 100, 200, "Market");
////        map.addBuilding(BuildingType.DavidRestaurant, 695, 265, 100, 100, "Rest1");
////        map.addBuilding(BuildingType.RyanRestaurant, 695, 535, 100, 100, "Rest2");
////        map.addBuilding(BuildingType.LynRestaurant, 845, 265, 100, 100, "Rest3");
////        map.addBuilding(BuildingType.EricRestaurant, 845, 535, 100, 100, "Rest4");
////        map.addBuilding(BuildingType.JoshRestaurant, 995, 265, 100, 100, "Rest5");
////        map.addBuilding(BuildingType.ChengRestaurant, 995, 535, 100, 100, "Rest6");
////        map.addBuilding(BuildingType.House, 695, 130, 100, 100, "House1");
////
////        map.addBuilding(BuildingType.Apartment, 200, 525, 150, 100, "Apart");
////        map.addBuilding(BuildingType.Bank, 200, 120, 150, 80, "Bank");
//        Configuration.configure("2.config");
//        
//	      david.restaurant.gui.RestaurantGui rest1 = (david.restaurant.gui.RestaurantGui)map.searchByName("Rest1");
//        guehochoi.gui.RestaurantGui rest2 = (guehochoi.gui.RestaurantGui)map.searchByName("Rest2");
//        LYN.gui.RestaurantGui rest3 = (LYN.gui.RestaurantGui)map.searchByName("Rest3");
//        ericliu.gui.RestaurantGui rest4=(ericliu.gui.RestaurantGui)map.searchByName("Rest4");
//        josh.restaurant.gui.RestaurantGui rest5 = (josh.restaurant.gui.RestaurantGui)map.searchByName("Rest5");
//        Cheng.gui.RestaurantGui rest6 = (Cheng.gui.RestaurantGui)map.searchByName("Rest6");
//        House.gui.HousePanelGui h = (House.gui.HousePanelGui)map.searchByName("House1");
//        Buildings.ApartmentComplex a = (Buildings.ApartmentComplex)map.searchByName("Apart");
//        Bank bank = (Bank)map.searchByName("Bank");
//        
//        BusAgent bus = new BusAgent("Bank","Bus1Crossing1","Market","Bus1Crossing2","Restaurants1","Bus1Crossing3","Restaurants2","Bus1Crossing4","House","Bus1Crossing5","Terminal1",1);
//        BusGui busGui = new BusGui(bus,"Terminal1");
//        
//        bus.setGui(busGui);
//
//        bus.startThread();
//        
//        map.buses.add(bus);
//        SimcityPanel.guis.add(busGui);
//        
//        
//        
//        
//        //bank.addCustomer(new Person("Customer"));
//        Person bankTeller = new Person("Teller");
//        bankTeller.roles.add(new Role(roles.TellerAtChaseBank, "Bank"));
//        bank.addWorker(bankTeller);
//        
//        Person bankTeller2 = new Person("Teller2");
//        bankTeller2.roles.add(new Role(roles.TellerAtChaseBank, "Bank"));
//        bank.addWorker(bankTeller2);
//        
//        map.addBuilding(BuildingType.Apartment, 200, 525, 150, 100, "Apart");
//        map.addBuilding(BuildingType.Bank, 200, 120, 150, 80, "Bank");
//        /*
//        Person person = new Person("gulu");
//        person.roles.add(new Role(Role.roles.LYNHost, "Rest3"));
//        rest3.restPanel.addWorker(person);
//        Person person1 = new Person("gala");
//        person1.roles.add(new Role(Role.roles.LYNCook, "Rest3"));
//        rest3.restPanel.addWorker(person1);
//        Person person2 = new Person("galaaaaa");
//        person2.roles.add(new Role(Role.roles.LYNCashier, "Rest3"));
//        rest3.restPanel.addWorker(person2);
//        */
//        Person person3 = new Person("galawaiter");
//        person3.roles.add(new Role(Role.roles.LYNWaiter, "Rest3"));
//        rest3.restPanel.addWorker(person3);
//        /*
//        Person person4 = new Person("galawaiter");
//        person4.roles.add(new Role(Role.roles.houseRenter, "Rest3"));
//        rest3.restPanel.addPerson(person4);*/
//        
//        
//        //bank.addCustomer(new Person("Customer"));
//        rest1.restPanel.addPerson("Waiters", "w1");
//        //rest1.restPanel.addPerson("Customers", "Chicken");
//        rest2.restPanel.addPerson("Waiters", "w2");
//        //rest2.restPanel.addPerson("Customers", "d");
//        
//        //rest3.restPanel.addPerson("Customers", "hi", true);
//       // rest3.restPanel.addWaiter("Waiters", "hello");
//
//        rest4.restPanel.addWaiter("Waiters", "Waiter", true);
//
//        rest5.restPanel.addPerson("Waiters", "dsf", false);
//        //rest5.restPanel.AddCustomer(new Person("lkdsfj"));
//        

    }
    
    java.util.List<GlobalTime> objects = new ArrayList<GlobalTime> ();
    public void registerTimeRequired(GlobalTime me) {
    	objects.add(me);
    }
    
    public void start(SimcityGui gui) {
    	 this.gui = gui;
         add(restLabel);
         
         GlobalMap.busInit();
         Configuration.configure(Configuration.chosenFilename_config);
         GlobalMap.init();
         /* Add buildings here */
         map = GlobalMap.getGlobalMap();
         


//         map.addBuilding(BuildingType.Store, 400, 160, 100, 200, "Market");
//         map.addBuilding(BuildingType.DavidRestaurant, 695, 265, 100, 100, "Rest1");
//         map.addBuilding(BuildingType.RyanRestaurant, 695, 535, 100, 100, "Rest2");
//         map.addBuilding(BuildingType.LynRestaurant, 845, 265, 100, 100, "Rest3");
//         map.addBuilding(BuildingType.EricRestaurant, 845, 535, 100, 100, "Rest4");
//         map.addBuilding(BuildingType.JoshRestaurant, 995, 265, 100, 100, "Rest5");
//         map.addBuilding(BuildingType.ChengRestaurant, 995, 535, 100, 100, "Rest6");
//         map.addBuilding(BuildingType.House, 695, 130, 100, 100, "House1");
 //
//         map.addBuilding(BuildingType.Apartment, 200, 525, 150, 100, "Apart");
//         map.addBuilding(BuildingType.Bank, 200, 120, 150, 80, "Bank");
         
//         
//         david.restaurant.gui.RestaurantGui rest1 = (david.restaurant.gui.RestaurantGui)map.searchByName("Rest1");
//         guehochoi.gui.RestaurantGui rest2 = (guehochoi.gui.RestaurantGui)map.searchByName("Rest2");
//         LYN.gui.RestaurantGui rest3 = (LYN.gui.RestaurantGui)map.searchByName("Rest3");
//         ericliu.gui.RestaurantGui rest4=(ericliu.gui.RestaurantGui)map.searchByName("Rest4");
//         josh.restaurant.gui.RestaurantGui rest5 = (josh.restaurant.gui.RestaurantGui)map.searchByName("Rest5");
//         Cheng.gui.RestaurantGui rest6 = (Cheng.gui.RestaurantGui)map.searchByName("Rest6");
//         House.gui.HousePanelGui h = (House.gui.HousePanelGui)map.searchByName("House1");
//         Buildings.ApartmentComplex a = (Buildings.ApartmentComplex)map.searchByName("Apart");
//         Bank bank = (Bank)map.searchByName("Bank");
//         
         
        
//         Person bankTeller = new Person("Teller");
//         bankTeller.roles.add(new Role(roles.WorkerTellerAtChaseBank, "Bank"));
//         bank.addWorker(bankTeller);
//         
//         Person bankTeller2 = new Person("Teller2");
//         bankTeller2.roles.add(new Role(roles.WorkerTellerAtChaseBank, "Bank"));
//         bank.addWorker(bankTeller2);
//        
//         
//         
//<<<<<<< HEAD
//=======
//         Person security = new Person("Security");
//         security.roles.add(new Role(roles.WorkerSecurityAtChaseBank, "Bank"));
//         security.roles.add(new Role(Role.roles.JonnieWalker,null));
//         security.roles.add(new Role(Role.roles.houseRenter,null));
//         security.house = h;
//         security.needToWork = true;
//         security.quitWork = true;
//         GlobalMap.getGlobalMap().getListOfPeople().add(security);
//         security.startThread();
//         
//         Person robber = new Person("Robber");
//         robber.roles.add(new Role(roles.Robbery, "Bank"));
//         robber.roles.add(new Role(Role.roles.JonnieWalker,null));
//         GlobalMap.getGlobalMap().addPerson(robber);
//         addPerson(robber, "apart", "Owner", "Walk");
//         
//         rest1.restPanel.addPerson("Waiters", "joe");
//         rest1.restPanel.addPerson("Waiters", "jo1e");
//         rest1.restPanel.addPerson("Waiters", "joe2");
//>>>>>>> bf4fadb031d555f2d30a7a71f912cd0137252282
////         
//         Person robber = new Person("Robber");
//         robber.roles.add(new Role(roles.Robbery, "Bank"));
//         robber.roles.add(new Role(Role.roles.JonnieWalker,null));
//         GlobalMap.getGlobalMap().addPerson(robber);
//         addPerson(robber, "apart", "Owner", "Walk");
////         
//         Person bankTeller3 = new Person("Teller3");
//         bankTeller3.roles.add(new Role(roles.TellerAtChaseBank, "Bank"));
//         bank.addWorker(bankTeller3);
//         Person bankTeller4 = new Person("Teller4");
//         bankTeller4.roles.add(new Role(roles.TellerAtChaseBank, "Bank"));
//         bank.addWorker(bankTeller4);
//         Person bankTeller5 = new Person("Teller5");
//         bankTeller5.roles.add(new Role(roles.TellerAtChaseBank, "Bank"));
//         bank.addWorker(bankTeller5);
         /*
         Person person = new Person("gulu");
         person.roles.add(new Role(Role.roles.LYNHost, "Rest3"));
         rest3.restPanel.addWorker(person);
         Person person1 = new Person("gala");
         person1.roles.add(new Role(Role.roles.LYNCook, "Rest3"));
         rest3.restPanel.addWorker(person1);
         Person person2 = new Person("galaaaaa");
         person2.roles.add(new Role(Role.roles.LYNCashier, "Rest3"));
         rest3.restPanel.addWorker(person2);
         */

         //rest3.restPanel.addPerson("Customers", "hi", true);
        // rest3.restPanel.addWaiter("Waiters", "hello");

//         rest4.restPanel.addWaiter("Waiters", "Waiter", true);

//         rest5.restPanel.addPerson("Waiters", "dsf", false);
         //rest5.restPanel.AddCustomer(new Person("lkdsfj"));
         
    }
    
    public boolean timetosleep(){
    	//return true;
    	boolean a = ((Math.abs(Calendar.getInstance().getTime().getMinutes()-newDay.getMinutes())%2 == 0) &&
    			(Calendar.getInstance().getTime().getMinutes()!=newDay.getMinutes())&& 
    			(Calendar.getInstance().getTime().getSeconds()==newDay.getSeconds() ));
    	
    	
    	return a;
    }
   
    
    public void setNewTime() {
    	date = Calendar.getInstance().getTime();
    }
    
    public void setNewDay() {
    	newDay = Calendar.getInstance().getTime();
    }
    public boolean timetowakeup(){
    	
    	boolean a = !(((Math.abs((Calendar.getInstance().getTime().getSeconds()- date.getSeconds())) == 10) && Calendar.getInstance().getTime().getMinutes()==date.getMinutes())
    				||((Math.abs((Calendar.getInstance().getTime().getSeconds()- date.getSeconds()))% 10 == 0) && (Calendar.getInstance().getTime().getMinutes()-date.getMinutes() == 1)));
    	if(!a)
    	{
    		for(Person p:GlobalMap.getGlobalMap().getListOfPeople()){
    			for(Role r: p.roles){
    				if(r.getRole().toString().contains("Worker")){
    					p.needToWork = true;
    				}
    				else
    				{
    					p.daysWithoutJob++;
    				}
    				
    			}
    			p.numdays++;
    			p.numdays%=7;
    		}
    		day++;
    		
    		if(day == 7)
    		{
    			//for all objects o, o.weekPassed();
    			for(Person p: GlobalMap.getGlobalMap().getListOfPeople())
    			{
    				p.houseBillsToPay++;
    				
    			}
    			/*
    			for(GlobalTime t : objects) {
    				t.weekPassed();
    			}*/
    			day = 0;
    		}
    		
    		if(day == 5 || day == 6){
    			System.out.println("weekend");
    		}
    	}
    	return a;
    	
    }
    
    public void addPerson(Person p, String home, String homeInfo, String vehicle){
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
       GlobalMap.getGlobalMap().getListOfPeople().add(p);
       p.startThread();
    }
}
