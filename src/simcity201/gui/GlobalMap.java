package simcity201.gui;

import java.net.URISyntaxException;
import java.util.*;

import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;
import agents.Grocery;
import newMarket.MarketRestaurantHandlerAgent;
import newMarket.NewMarket;
import agents.BusAgent;
import agents.Person;
import Buildings.ApartmentComplex;
import Buildings.Building;
import agents.Role;
import agents.Role.roles;
import animation.SimcityGui;
import animation.SimcityPanel;

public class GlobalMap {
	/*Singleton -- */
	private static GlobalMap map = new GlobalMap();
	private GlobalMap() {}
	int ssn = 1000000;
	Object ssnLock = new Object();
	SimcityGui gui;
	
	protected List<job> jobs = Collections.synchronizedList(new ArrayList<job>());
	protected AstarDriving astar ;
	protected walkingAStar aStarMap;
	
	public TrafficLightAgent trafficLight;
	
	public static GlobalMap getGlobalMap() {
		return map;}
	/*-------------*/
	
	/* Data */
	protected static Map<String, Building> buildings =
			new HashMap<String, Building>();
	public Collection<Building> getBuildings() {
		return buildings.values();
	}
	Timer timer = new Timer();
	public Timer getTimer() {
		return this.timer;
	}
	public enum BuildingType { LynRestaurant, RyanRestaurant, JoshRestaurant, 
							DavidRestaurant, EricRestaurant, ChengRestaurant,
								Bank, House, House1,House2, Store, Apartment } 
	public List<BusAgent> buses = new ArrayList<BusAgent>();
	
	public MarketRestaurantHandlerAgent marketHandler = null;
	
	public void startTrafficStuff()
	{
	   astar = new AstarDriving();
	   aStarMap=new walkingAStar();
//	   trafficLight=new TrafficLightAgent();
	}
	
	public void addBuilding(BuildingType type, int x, int y, int width, int height, String name) {
		Building temp;
		switch(type) {
			case Apartment:
				temp = new Buildings.ApartmentComplex();
				temp.x = x; temp.y = y;
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				//initApartment(temp);
				break;
			case Bank:
				temp = new Bank();
				temp.x = x; temp.y = y;
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				initBank(temp);
				jobs.add(new job(temp));
				((Bank)temp).getJobs();
				break;
			case ChengRestaurant:
				try {
					temp = new Cheng.gui.RestaurantGui();
					temp.x = x; temp.y = y;
					temp.width = width; temp.height = height;
					temp.name = name;
					temp.type = Building.Type.Restaurant;
					buildings.put(temp.name, temp);
					initCheng(temp);
					jobs.add(new job(temp));
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case DavidRestaurant:
				temp = new david.restaurant.gui.RestaurantGui();
				temp.x = x; temp.y = y;
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				initDavid(temp);
				temp.type = Building.Type.Restaurant;
				jobs.add(new job(temp));
				break;
			case EricRestaurant:
			   temp = new ericliu.gui.RestaurantGui();
	            temp.x = x; temp.y = y;
	            temp.width = width; temp.height = height;
	            temp.name = name;
	            buildings.put(temp.name, temp);
	            initEric(temp);
	            temp.type = Building.Type.Restaurant;
	            jobs.add(new job(temp));
				break;
			case House:
				temp = new House.gui.HousePanelGui();
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				//initHouse(temp);
				break;
			case House1:
				temp = new House.gui.HousePanelGui();
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				//initHouse(temp);
				break;
			case House2:
				temp = new House.gui.HousePanelGui();
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				//initHouse(temp);
				break;
			case JoshRestaurant:
				temp = new josh.restaurant.gui.RestaurantGui();
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				initJosh(temp);
				temp.type = Building.Type.Restaurant;
				jobs.add(new job(temp));
				break;
			case LynRestaurant:
				temp = new LYN.gui.RestaurantGui();
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				initLyn(temp);
				temp.type = Building.Type.Restaurant;
				jobs.add(new job(temp));
				break;
			case RyanRestaurant:
				temp = new guehochoi.gui.RestaurantGui();
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				initRyan(temp);
				temp.type = Building.Type.Restaurant;
				jobs.add(new job(temp));
				break;
			case Store:
				//temp = new Market.Market();
				temp = new newMarket.NewMarket();
				marketHandler = ((newMarket.NewMarket)temp).handlers.get(0);
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				//initStore(temp);
				jobs.add(new job(temp));
				break;
			default:
				break;
		}
	}
	
	
	public void initBank(Building bank) {
		Person teller1 = new Person("BankTeller");
        teller1.roles.add(new Role(Role.roles.WorkerTellerAtChaseBank, bank.name));
        teller1.roles.add(new Role(Role.roles.JonnieWalker,null));
        teller1.roles.add(new Role(Role.roles.houseRenter,null));
        teller1.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(teller1);
       // teller1.startThread();
        
        Person teller2 = new Person("BankTeller 2");
        teller2.roles.add(new Role(Role.roles.WorkerTellerAtChaseBank, bank.name));
        teller2.roles.add(new Role(Role.roles.JonnieWalker,null));
        teller2.roles.add(new Role(Role.roles.houseRenter,null));
        teller2.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(teller2);
       //teller2.startThread();
        
        Person teller3 = new Person("BankTeller 3");
        teller3.roles.add(new Role(Role.roles.WorkerTellerAtChaseBank, bank.name));
        teller3.roles.add(new Role(Role.roles.JonnieWalker,null));
        teller3.roles.add(new Role(Role.roles.houseRenter,null));
        teller3.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(teller3);
        //teller3.startThread();
        
        Person teller4 = new Person("BankTeller 4");
        teller4.roles.add(new Role(Role.roles.WorkerTellerAtChaseBank, bank.name));
        teller4.roles.add(new Role(Role.roles.JonnieWalker,null));
        teller4.roles.add(new Role(Role.roles.houseRenter,null));
        teller4.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(teller4);
        //teller4.startThread();
        
        Person teller5 = new Person("BankTeller 5");
        teller5.roles.add(new Role(Role.roles.WorkerTellerAtChaseBank, bank.name));
        teller5.roles.add(new Role(Role.roles.JonnieWalker,null));
        teller5.roles.add(new Role(Role.roles.houseRenter,null));
        teller5.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(teller5);
        //teller5.startThread();
        
        Person teller6 = new Person("BankTeller 6");
        teller6.roles.add(new Role(Role.roles.WorkerTellerAtChaseBank, bank.name));
        teller6.roles.add(new Role(Role.roles.JonnieWalker,null));
        teller6.roles.add(new Role(Role.roles.houseRenter,null));
        teller6.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(teller6);
        //teller5.startThread();
        
        Person security = new Person("BankSecurity");
        security.roles.add(new Role(roles.WorkerSecurityAtChaseBank, bank.name));
        security.roles.add(new Role(Role.roles.JonnieWalker,null));
        security.roles.add(new Role(Role.roles.houseRenter,null));
        security.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(security);
        //security.startThread();
	}
	
	public void initCheng(Building b) {
		Person person3 = new Person("waiterCheng");
        person3.roles.add(new Role(Role.roles.WorkerRossWaiter, b.name));
        person3.roles.add(new Role(Role.roles.JonnieWalker,null));
        person3.roles.add(new Role(Role.roles.houseRenter,null));
        person3.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(person3);
        
        Person person4 = new Person("hostCheng");
        person4.roles.add(new Role(Role.roles.WorkerRossHost, b.name));
        person4.roles.add(new Role(Role.roles.JonnieWalker,null));
        person4.roles.add(new Role(Role.roles.houseRenter,null));
        person4.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(person4);

        
        Person person5 = new Person("cashierCheng");
        person5.roles.add(new Role(Role.roles.WorkerRossCashier, b.name));
        person5.roles.add(new Role(Role.roles.JonnieWalker,null));
        person5.roles.add(new Role(Role.roles.houseRenter,null));
        person5.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(person5);
        
        Person person6 = new Person("cookCheng");
        person6.roles.add(new Role(Role.roles.WorkerRossCook, b.name));
        person6.roles.add(new Role(Role.roles.JonnieWalker,null));
        person6.roles.add(new Role(Role.roles.houseRenter,null));
        person6.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(person6);
	}
	
	public void initLyn(Building b) {
		Person person3 = new Person("waiterLyn");
        person3.roles.add(new Role(Role.roles.WorkerLYNWaiter, b.name));
        person3.roles.add(new Role(Role.roles.JonnieWalker,null));
        person3.roles.add(new Role(Role.roles.houseRenter,null));
        person3.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(person3);
        
        Person person4 = new Person("hostLyn");
        person4.roles.add(new Role(Role.roles.WorkerLYNHost, b.name));
        person4.roles.add(new Role(Role.roles.JonnieWalker,null));
        person4.roles.add(new Role(Role.roles.houseRenter,null));
        person4.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(person4);
        
        Person person5 = new Person("cashierLyn");
        person5.roles.add(new Role(Role.roles.WorkerLYNCashier, b.name));
        person5.roles.add(new Role(Role.roles.JonnieWalker,null));
        person5.roles.add(new Role(Role.roles.houseRenter,null));
        person5.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(person5);
        
        Person person6 = new Person("cookLyn");
        person6.roles.add(new Role(Role.roles.WorkerLYNCook, b.name));
        person6.roles.add(new Role(Role.roles.JonnieWalker,null));
        person6.roles.add(new Role(Role.roles.houseRenter,null));
        person6.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(person6);

	}
	
	public void initRyan(Building b) {
		((guehochoi.gui.RestaurantGui)b).restPanel.addPerson("Waiters", "Ryan's Waiter");
		((guehochoi.gui.RestaurantGui)b).restPanel.addPerson("Waiters", "Drug Dealer");
	}
	
	public void initEric(Building b) {
		((ericliu.gui.RestaurantGui)b).restPanel.addWaiter("Waiters", "Waiter", true);
	}
	
	public void initJosh(Building b) {
		//((josh.restaurant.gui.RestaurantGui)b).restPanel.initRestLabel();
		((josh.restaurant.gui.RestaurantGui)b).restPanel.addPerson("Waiters", "dsf", false);
		
	}

	public void initDavid(Building b) {
		Person person3 = new Person("waiterDavid");
        person3.roles.add(new Role(Role.roles.WorkerDavidWaiter, b.name));
        person3.roles.add(new Role(Role.roles.JonnieWalker,null));
        person3.roles.add(new Role(Role.roles.houseRenter,null));
        person3.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(person3);
        
        Person person4 = new Person("hostDavid");
        person4.roles.add(new Role(Role.roles.WorkerDavidhost, b.name));
        person4.roles.add(new Role(Role.roles.JonnieWalker,null));
        person4.roles.add(new Role(Role.roles.houseRenter,null));
        person4.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(person4);
        
        Person person5 = new Person("cashierDavid");
        person5.roles.add(new Role(Role.roles.WorkerDavidCashier, b.name));
        person5.roles.add(new Role(Role.roles.JonnieWalker,null));
        person5.roles.add(new Role(Role.roles.houseRenter,null));
        person5.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(person5);
        
        Person person6 = new Person("cookDavid");
        person6.roles.add(new Role(Role.roles.WorkerDavidCook, b.name));
        person6.roles.add(new Role(Role.roles.JonnieWalker,null));
        person6.roles.add(new Role(Role.roles.houseRenter,null));
        person6.needToWork = true;
        GlobalMap.getGlobalMap().getListOfPeople().add(person6);
	}
	
	static BusAgent bus;
	static BusAgent bus2;
	
	public static void busInit() {
		//Bus initialization
		bus = new BusAgent("Bank","Bus1Crossing1","Market","Bus1Crossing2","Restaurants1","Bus1Crossing3","Restaurants2","Bus1Crossing4","House","Bus1Crossing5","Terminal1",1);
		BusGui busGui = new BusGui(bus,"Terminal1");
		bus2 = new BusAgent("Bank","Bus1Crossing1","Market","Bus1Crossing2","Restaurants1","Bus1Crossing3","Restaurants2","Bus1Crossing4","House","Bus1Crossing5","Terminal1",1);
		BusGui busGui2 = new BusGui(bus2,"Terminal1");
		bus.setGui(busGui);
		bus2.setGui(busGui2);
		bus.startThread();
		bus2.startThread();
		map.buses.add(bus);
		map.buses.add(bus2);
		SimcityPanel.guis.add(busGui);
		SimcityPanel.guis.add(busGui2);
	}
	
	
	static int numOfHouse1 = 0;
	static int numOfHouse2 = 0;
	static int numOfHouse3 = 0;
	static int numOfApart = 0;
	
	public static void init() {
	
        //assign    
        for(Person p: people) {
        	if (numOfHouse1 < 3) {
        		if (numOfHouse1 ==0) {
        			//((House.gui.HousePanelGui)buildings.get("House1")).housePanel.addOwner(p);
        			p.roles.add(new Role(Role.roles.houseOwner, "House1"));
        			p.house = (House.gui.HousePanelGui)buildings.get("House1");
        		}else {
        			//((House.gui.HousePanelGui)buildings.get("House1")).housePanel.addRenter(p);
        			p.roles.add(new Role(Role.roles.houseRenter, "House1"));
        			p.house = (House.gui.HousePanelGui)buildings.get("House1");
        		}
        		numOfHouse1++;
        		continue;
        	}
        	if (numOfHouse2 < 3) {
        		if (numOfHouse2 ==0) {
        			//((House.gui.HousePanelGui)buildings.get("House2")).housePanel.addOwner(p);
        			p.roles.add(new Role(Role.roles.houseOwner, "House2"));
        			p.house = ((House.gui.HousePanelGui)buildings.get("House2"));
        		}else {
        			//((House.gui.HousePanelGui)buildings.get("House2")).housePanel.addRenter(p);
        			p.roles.add(new Role(Role.roles.houseRenter, "House2"));
        			p.house = ((House.gui.HousePanelGui)buildings.get("House2"));
        		}
        		numOfHouse2++;
        		continue;
        	}
        	if (numOfHouse3 < 3) {
        		if (numOfHouse3 ==0) {
        			//((House.gui.HousePanelGui)buildings.get("House3")).housePanel.addOwner(p);
        			p.roles.add(new Role(Role.roles.houseOwner, "House2"));
        			p.house = ((House.gui.HousePanelGui)buildings.get("House3"));
        		}else {
        			//((House.gui.HousePanelGui)buildings.get("House3")).housePanel.addRenter(p);
        			p.roles.add(new Role(Role.roles.houseRenter, "House2"));
        			p.house = ((House.gui.HousePanelGui)buildings.get("House3"));
        		}
        		numOfHouse3++;
        		continue;
        	}        	
        	if (numOfApart == 0) {
        		p.roles.add(new Role(Role.roles.ApartmentOwner, "Apart"));
        		((ApartmentComplex)buildings.get("Apart")).addOwner(p);
        		p.complex = ((ApartmentComplex)buildings.get("Apart"));
        	}else {
        		p.roles.add(new Role(Role.roles.ApartmentRenter, "Apart"));
        		((ApartmentComplex)buildings.get("Apart")).addRenter(p);
        		p.complex = ((ApartmentComplex)buildings.get("Apart"));
        	}
        	numOfApart++;
        }
        
        //start everyone
        for(Person p: people) {
        	
        	p.startThread();
			p.doThings();
		}
	}
	
	
	public void addJob(Building b)
	{
		for(job j : jobs)
		{
			if(b.equals(j.b))
			{
				j.jobs++;
				//AlertLog.getInstance().logMessage(AlertTag.LYN, "LYN",b.name+j.jobs);
				return;
			}
		}
	}
	
	public void removeJob(Building b)
	{
		for(job j : jobs)
		{
			if(b.equals(j.b))
			{
				j.jobs--;
				return;
			}
		}
	}
	
	public List<job> getJobs()
	{
		return jobs;
	}
	
	protected static List<Person> people =
			new ArrayList<Person>();
	
	public void addPerson(Person p)
	{
		people.add(p);
	}
	
	public void startAllPeople() {
		for(Person p: people) {
			p.startThread();
			p.doThings();
		}
	}
	
	/**
	 * @param str
	 * @return building if exist, null if not
	 */

	public Building searchByName(String str) {
		return buildings.get(str);
	}
	
	public List<Person> getListOfPeople()
	{
		return people;
	}
	
	public void setGui(SimcityGui gui)
	{
		this.gui = gui;
	}
	
	public SimcityGui getGui()
	{
		return gui;
	}
	
	public String[] peopleNames()
	{
		List<String> temp = new ArrayList<String>();
		for(Person p: people)
		{
			temp.add(p.getName());
		}
		return (String[]) temp.toArray();
	}

	public Person searchPersonByName(String name)
	{
		for(Person p: people)
		{
			if(p.getName().equals(name))
			{
				return p;
			}
		}
		return null;
	}
	
	public int getNewSSN()
	{
		synchronized(ssnLock)
		{
			ssn++;
			return ssn;
		}
	}
	
	static public class job{
		public Building b;
		public int jobs;
		public job(Building b){
			this.b = b;
			jobs= 0;
		}
	}

	public AstarDriving getAstar(){
		return astar;
	}
	
	public walkingAStar getWalkAStar(){
      return aStarMap;
   }
	public void startLight(){
	   trafficLight.startThread();
	}
}
