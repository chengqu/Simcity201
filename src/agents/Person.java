package agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import simcity201.gui.Bank;
import simcity201.gui.GlobalMap;
import simcity201.gui.PassengerGui;
import Buildings.ApartmentComplex;
import Buildings.ApartmentComplex.Apartment;
import Buildings.Building;
import Market.Market;
import House.gui.HousePanelGui;
import House.gui.HousePersonPanel;
import agent.Agent;
import agents.Task.specificTask;
import animation.SimcityPanel;

public class Person extends Agent{

	/**
	 * DATA
	 */
	public double money;
	public float payCheck;
	public int hungerLevel;
	public String job;
	public int age;
	String location = "birth";
	boolean tempBool = true;
	
	Random rand = new Random();
	
	private String name;
	public List<ApartmentBill> bills = new ArrayList<ApartmentBill>();
	public List<Grocery> groceries = new ArrayList<Grocery>();
	public List<Grocery> homefood = new ArrayList<Grocery>();
	
	public Apartment apartment = null;
	public ApartmentComplex complex = null;
	public HousePanelGui house = null;
	public CarAgent car = null;
	public Task currentTask = null; 
	
	PassengerAgent passenger;
	
	Timer t = new Timer();
	
	StopAgent s;
	
	//add a bank name string later, so he doesn't have to look through the map to get it <--- maybe we want to do this

	private int hungerThreshold = 20; 
	public void doThings() {
		stateChanged();
	}
	
	public boolean needToWork = false;
	public int houseBillsToPay = 0;


	/**
	 * @author Ryan (Gueho) Choi
	 *	List of variables added as needed for Bank:  address, ssn
	 *	accounts, paycheckThreshold, cashLoThreshold, enoughMoneyToBuyACar, wantCar
	 *	how to fire functions in bank-
	 *		1. making new account: accounts.isEmpty()
	 *		2. deposit: paycheck >= paycheckThreshold
	 *		3. withdraw: money <= cashLowThreshold
	 *		4. loan: wantCar = true && accounts total + money + paycheck < enoughMoneyToBuyACar
	 */
	
	public List<Account> accounts = new ArrayList<Account>();
	public final float payCheckThreshold = 100; 
	public final float cashLowThreshold = 20;
	public final float enoughMoneyToBuyACar = 20000;
	public boolean wantCar = false;
	public final int ssn = 123456789;
	public String address = "Parking Structure A at USC";
	
	
	/*
	 * Insert car and bus (or bus stop) agents here
	 * add gui here also for walking
	 */
	
	enum PersonState{needBank, needRestaurant, needHome, needStore,
				needWork, waiting, inAction, decide, moving, none};
				
	enum PersonEvent{none, done, atDest};
	
	PersonEvent frontEvent;
	PersonState currentState;
	
	List<PersonEvent> events = new ArrayList<PersonEvent>();
	public List<Role> roles = new ArrayList<Role>();
	public List<Task> tasks = new ArrayList<Task>();
	
	//locks
	public Object eventLock = new Object();
	public Object stateLock = new Object();
	public Object taskLock = new Object();
	public Object billLock = new Object();
	public Object groceryLock = new Object();
	
	private static long hungerIncrementTime = 5000;
	
	//this is a special lock that has a purpose, but i forgot what it currently is.
	//please do not use it
	public Object generalLock = new Object();
	
	public Person(String name) {
		this.name = name;
		money = 0;
		payCheck = 0;
		hungerLevel = 0;
		age = 0;
		currentState = PersonState.none;
		frontEvent = PersonEvent.none;
		this.passenger = new PassengerAgent(name, this);
	      PassengerGui g = new PassengerGui(passenger);
	      passenger.setGui(g);
	      SimcityPanel.guis.add(g);
	      passenger.startThread();
	      
	      s = new StopAgent(GlobalMap.getGlobalMap().buses.get(0), null);
	      s.startThread();
	      
	      final Person p = this;
	}
	
	/**
	 * MESSAGES
	 */
	
	public void msgDone()
	{
		synchronized(eventLock)
		{
			events.add(PersonEvent.done);
			frontEvent = PersonEvent.done;
		}
		hungerLevel += 3 + rand.nextInt(5);
		stateChanged();
	}
	
	public void msgAtDest()
	{
		synchronized(eventLock)
		{
			events.add(PersonEvent.atDest);
			frontEvent = PersonEvent.atDest;
		}
		hungerLevel += 3 + rand.nextInt(5);
		stateChanged();
	}
	
	protected boolean pickAndExecuteAnAction() {
		synchronized(taskLock)
		{
			if(currentState == PersonState.needRestaurant)
			{
				Task task = null;
				for(Task t: tasks)
				{
					if(t.getObjective() == Task.Objective.goTo)
					{
						task = t;
						break;
					}
				}
				if(task != null)
				{
					goToRestaurant(task);
					return true;
				}
			}
			
			if(currentState == PersonState.needBank)
			{
				Task task = null;
				for(Task t: tasks)
				{
					if(t.getObjective() == Task.Objective.goTo)
					{
						task = t;
						break;
					}
				}
				if(task != null)
				{
					goToBank(task);
					return true;
				}
			}
			
			if(currentState == PersonState.needStore)
			{
				Task task = null;
				for(Task t: tasks)
				{
					if(t.getObjective() == Task.Objective.goTo)
					{
						task = t;
						break;
					}
				}
				if(task != null)
				{
					goToStore(task);
					return true;
				}
			}
			
			if(currentState == PersonState.needHome)
			{
				Task task = null;
				for(Task t: tasks)
				{
					if(t.getObjective() == Task.Objective.goTo)
					{
						task = t;
						break;
					}
				}
				if(task != null)
				{
					goToHome(task);
					return true;
				}
			}
			synchronized(eventLock)
			{
				if(currentState == PersonState.moving)
				{
					if(frontEvent == PersonEvent.atDest)
					{
						Enter();
						return true;
					}
				}
				if((currentState == PersonState.inAction && frontEvent == PersonEvent.done) || currentState == PersonState.none)
				{
					Decide();
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * ACTIONS
	 */
	
	private void goToRestaurant(Task t)
	{
		currentState = PersonState.moving;
		tasks.remove(t);

		//passenger.msgGoTo(this, "Rest1", null, null);
		passenger.msgGoTo(this,t.getLocation(), null, null);
	}
	
	private void goToBank(Task t)
	{
		currentState = PersonState.moving;
		tasks.remove(t);
		/*
		 * Make call to correct form of transportation
		 * need car, bus, etc for this. pass t.location
		 * to the vehicle (or something like that)
		 */
		
		passenger.msgGoTo(this, t.getLocation(),null, null);
	}
	
	private void goToStore(Task t)
	{
		currentState = PersonState.moving;
		tasks.remove(t);
		/*
		 * Make call to correct form of transportation
		 * need car, bus, etc for this. pass t.location
		 * to the vehicle (or something like that)
		 */
		passenger.msgGoTo(this, t.getLocation(), null, null);
	}
	
	private void goToHome(Task t)
	{
		currentState = PersonState.moving;
		tasks.remove(t);
		/*
		 * Make call to correct form of transportation
		 * need car, bus, etc for this. pass t.location
		 * to the vehicle (or something like that)
		 */
		passenger.msgGoTo(this, t.getLocation(),null, null);
	}
	
	private void Enter()
	{
		currentState = PersonState.inAction;
		if(tasks.size() == 0)
		{
			print("0 task size");
			return;
		}
		Task t = tasks.get(0);
		currentTask = t;
		tasks.remove(t);
		if(t.getObjective() == Task.Objective.patron)
		{
			/*
			 * get building from map, call "addCustomer(this)"
			 */
			if(GlobalMap.getGlobalMap().searchByName(t.getLocation()).getClass() == david.restaurant.gui.RestaurantGui.class)
			{
				david.restaurant.gui.RestaurantGui temp = (david.restaurant.gui.RestaurantGui)GlobalMap.getGlobalMap().searchByName(t.getLocation());
				temp.restPanel.addCustomer(this);
				return;
			}
			else if(GlobalMap.getGlobalMap().searchByName(t.getLocation()).getClass() == guehochoi.gui.RestaurantGui.class)
			{
				guehochoi.gui.RestaurantGui temp = (guehochoi.gui.RestaurantGui)GlobalMap.getGlobalMap().searchByName(t.getLocation());
				/*Need to add addCustomer to this ryan's restaurant panel or gui*/
				temp.restPanel.addCustomer(this);
				return;
			}
			else if(GlobalMap.getGlobalMap().searchByName(t.getLocation()).getClass() == LYN.gui.RestaurantGui.class)
			{
				LYN.gui.RestaurantGui temp = (LYN.gui.RestaurantGui)GlobalMap.getGlobalMap().searchByName(t.getLocation());
				/*Need to add addCustomer to this Lyn's restaurant panel or gui*/
				temp.restPanel.addPerson(this);
				return;
			}
			else if(GlobalMap.getGlobalMap().searchByName(t.getLocation()).getClass() == ericliu.gui.RestaurantGui.class)
			{
				ericliu.gui.RestaurantGui temp = (ericliu.gui.RestaurantGui)GlobalMap.getGlobalMap().searchByName(t.getLocation());
				temp.restPanel.msgAddCustomer(this);
				return;
			}
			else if(GlobalMap.getGlobalMap().searchByName(t.getLocation()).getClass() == josh.restaurant.gui.RestaurantGui.class)
			{
				josh.restaurant.gui.RestaurantGui temp = (josh.restaurant.gui.RestaurantGui)GlobalMap.getGlobalMap().searchByName(t.getLocation());
				temp.restPanel.AddCustomer(this);
				return;
			}
			else if(GlobalMap.getGlobalMap().searchByName(t.getLocation()).getClass() == Cheng.gui.RestaurantGui.class)
			{
				Cheng.gui.RestaurantGui temp = (Cheng.gui.RestaurantGui)GlobalMap.getGlobalMap().searchByName(t.getLocation());
				temp.restPanel.addPerson(this);
				return;
			}
			else if(GlobalMap.getGlobalMap().searchByName(t.getLocation()).getClass() == Bank.class)
			{
				Bank temp = (Bank)GlobalMap.getGlobalMap().searchByName(t.getLocation());
				temp.addCustomer(this);
				return;
			}
			else if(GlobalMap.getGlobalMap().searchByName(t.getLocation()).getClass() == newMarket.NewMarket.class)
			{
				//Market temp = (Market)GlobalMap.getGlobalMap().searchByName(t.getLocation());
				newMarket.NewMarket temp = (newMarket.NewMarket)GlobalMap.getGlobalMap().searchByName(t.getLocation());
				temp.addCustomer(this);
				return;
			}
			
		}
		else if(t.getObjective() == Task.Objective.worker)
		{
			//nobody really goes to work yet, so leave this unfinished. However, it needs to be done
			//by v2
			/*
			 * get building from map, call "addWorker(this)"
			 */
		}
		else if(t.getObjective() == Task.Objective.house)
		{
			/*
			 * get building from map, call "addHomeOwner(this)" or apartmentRenter(this), etc...
			 */
			for(Role r: roles)
			{
				if(r.getRole() == Role.roles.ApartmentOwner)
				{
					ApartmentComplex temp = (ApartmentComplex)GlobalMap.getGlobalMap().searchByName(t.getLocation());
					temp.addOwner(this);
					return;
				}
				if(r.getRole() == Role.roles.ApartmentRenter)
				{
					ApartmentComplex temp = (ApartmentComplex)GlobalMap.getGlobalMap().searchByName(t.getLocation());
					temp.addRenter(this);
					return;
				}
				if(r.getRole() == Role.roles.houseRenter)
				{
					House.gui.HousePanelGui temp = (House.gui.HousePanelGui)GlobalMap.getGlobalMap().searchByName(t.getLocation());
					temp.housePanel.addRenter(this);
					return;
				}
				if(r.getRole() == Role.roles.houseOwner)
				{
					House.gui.HousePanelGui temp = (House.gui.HousePanelGui)GlobalMap.getGlobalMap().searchByName(t.getLocation());
					temp.housePanel.addOwner(this);
					return;
				}
			}
		}
		else
		{
			//something has gone wrong if we get here
		}
	}
	
	private void Decide()
	{
		//beginning
		tasks.clear();	//we are currently clearing the tasks, but in the future we wont
		
		float totalMoney_ = (float)this.money + payCheck;
		for (Account acc : accounts) {
			totalMoney_ += acc.getBalance();
		}
		
		if(needToWork)
		{
			//... need to add work 
			return;
		}
		else if(this.groceries.size() > 0)
		{
			if(apartment != null)
			{
				tasks.add(new Task(Task.Objective.goTo, this.complex.name));
				Task t = new Task(Task.Objective.house, this.complex.name);
				tasks.add(t);
				currentTask = t;
				currentTask.sTasks.add(Task.specificTask.depositGroceries);					
				currentState = PersonState.needHome;
				return;
			}
			else if(house != null)
			{
				System.out.println(groceries.size());
				tasks.add(new Task(Task.Objective.goTo, this.house.name));
				Task t = new Task(Task.Objective.house, this.house.name);
				tasks.add(t);
				currentTask = t;
				currentTask.sTasks.add(Task.specificTask.depositGroceries);					
				currentState = PersonState.needHome;
				return;
			}
			else
			{
				//shouldnt happen. save spot for homeless dude
				this.groceries.clear();
			}
			return;
		}
		else if(accounts.isEmpty())
		{
			//make an account at the bank.
			Bank b = (Bank)GlobalMap.getGlobalMap().searchByName("Bank");
			tasks.add(new Task(Task.Objective.goTo, b.name));
			tasks.add(new Task(Task.Objective.patron, b.name));
			currentState = PersonState.needBank;
			return;
		}
		else if(payCheck >= payCheckThreshold)
		{
			//deposit money
			Bank b = (Bank)GlobalMap.getGlobalMap().searchByName("Bank");
			tasks.add(new Task(Task.Objective.goTo, b.name));
			tasks.add(new Task(Task.Objective.patron, b.name));
			currentState = PersonState.needBank;
			return;
		}
		else if(wantCar)
		{
			float totalMoney = (float)money + payCheck;
			for (Account acc : accounts) {
				totalMoney += acc.getBalance();
			}
			
			//TODO 1: IF REJECTED FOR LOAN SET WANTCAR TO FALSE & maybe reset wantcar at a later moment in time
			if(totalMoney < enoughMoneyToBuyACar)
			{
				//... fill out tasks
				//if doesn't work, replace b.name with "Bank"
				Bank b = (Bank)GlobalMap.getGlobalMap().searchByName("Bank");
				tasks.add(new Task(Task.Objective.goTo, b.name));
				tasks.add(new Task(Task.Objective.patron, b.name));
				currentState = PersonState.needBank;
				return;
			}
			else
			{
				//... buy a car
				//if doesn't work, replace b.name with "Market"
				newMarket.NewMarket m = (newMarket.NewMarket)GlobalMap.getGlobalMap().searchByName("Market");
				tasks.add(new Task(Task.Objective.goTo, m.name));
				tasks.add(new Task(Task.Objective.patron, m.name));
				currentState = PersonState.needStore;
				wantCar = false;
				return;
			}
		}
		else if(this.money < this.cashLowThreshold)
		{
			float totalMoney = payCheck;
			for (Account acc : accounts) {
				totalMoney += acc.getBalance();
			}
			if(totalMoney < 2 * this.cashLowThreshold)
			{
				//TODO: USE SPECIFIC TASKS TO MAKE HIM WALK HOME & SLEEP
				for(Role r: roles)
				{
					if(r.getRole() == Role.roles.ApartmentRenter || r.getRole() == Role.roles.ApartmentOwner)
					{
						tasks.add(new Task(Task.Objective.goTo, this.complex.name));
						Task t = new Task(Task.Objective.patron, this.complex.name);
						tasks.add(t);
						currentTask = t;
						currentTask.sTasks.add(Task.specificTask.sleepAtApartment);					
						currentState = PersonState.needHome;
						return;
					}
				}
				for(Role r: roles)
				{
					if(r.getRole() == Role.roles.houseRenter || r.getRole() == Role.roles.houseOwner)
					{
						tasks.add(new Task(Task.Objective.goTo, house.name));
						Task t = new Task(Task.Objective.patron, this.house.name);
						tasks.add(t);
						currentTask = t;
						currentTask.sTasks.add(Task.specificTask.sleepAtHome);					
						currentState = PersonState.needHome;
						return;
					}
				}
			}
			else
			{
				Bank b = (Bank)GlobalMap.getGlobalMap().searchByName("Bank");
				tasks.add(new Task(Task.Objective.goTo, b.name));
				tasks.add(new Task(Task.Objective.patron, b.name));
				currentState = PersonState.needBank;
				return;
			}
		}
		else if(apartment != null && apartment.Fridge.size() == 0)
		{

			
				tasks.add(new Task(Task.Objective.goTo, "Market"));
				Task t = new Task(Task.Objective.patron, "Market");
				tasks.add(t);
				currentTask = t;
				currentTask.sTasks.add(Task.specificTask.buyGroceries);					
				currentState = PersonState.needStore;
				return;
			
		}
		else if(house != null && house.housePanel.returngroceries().size()!=0)		//TODO: add groceries to house
		{
			
				System.out.println(house.housePanel.returngroceries().size());
				homefood = house.housePanel.returngroceries();
				tasks.add(new Task(Task.Objective.goTo, "Market"));
				Task t = new Task(Task.Objective.patron, "Market");
				tasks.add(t);
				currentTask = t;
				currentTask.sTasks.add(Task.specificTask.buyGroceries);					
				currentState = PersonState.needStore;
				return;
			

		}
		else if(hungerLevel > this.hungerThreshold)
		{
			for(Role r : roles)
			{
				if(r.getRole().equals(Role.roles.preferHomeEat))
				{
					for(Role r_: roles)
					{
						if(r_.getRole().equals(Role.roles.ApartmentOwner) || r_.getRole().equals(Role.roles.ApartmentRenter))
						{
							//use apartment to fill out task
							tasks.add(new Task(Task.Objective.goTo, this.complex.name));
							Task t = new Task(Task.Objective.patron, this.complex.name);
							tasks.add(t);
							currentTask = t;
							currentTask.sTasks.add(Task.specificTask.eatAtApartment);					
							currentState = PersonState.needHome;
							return;
						}
						if(r_.getRole().equals(Role.roles.houseOwner) || r_.getRole().equals(Role.roles.houseRenter))
						{
							//use house to fill out task
							tasks.add(new Task(Task.Objective.goTo, house.name));
							Task t = new Task(Task.Objective.patron, this.house.name);
							tasks.add(t);
							currentTask = t;
							currentTask.sTasks.add(Task.specificTask.eatAtHome);					
							currentState = PersonState.needHome;
							return;
						}
					}
				}
			}
			tasks.add(new Task(Task.Objective.goTo, "Rest1"));
			Task t = new Task(Task.Objective.patron, "Rest1");
			tasks.add(t);
			//currentTask = t;
			//currentTask.sTasks.add(Task.specificTask.eatAtHome);					
			currentState = PersonState.needRestaurant;
			
			//choose between restaurants to eat at if he has money above a threshold
			List<Building> buildings = new ArrayList<Building>();
			for(Building b: GlobalMap.getGlobalMap().getBuildings())
			{
				if(b.type == Building.Type.Restaurant)
				{
					buildings.add(b);
				}
			}
			Building b = buildings.get(rand.nextInt(buildings.size()));
			tasks.add(new Task(Task.Objective.goTo, b.name));
			tasks.add(new Task(Task.Objective.patron, b.name));
			currentState = PersonState.needRestaurant;
			return;
		}
		else if(bills.size() > 0 || houseBillsToPay > 0)
		{
			//TODO: NEED TO ADD BILLS TO REPAY LOANS!!!
			//TODO: USE SPECIFIC TASKS LATER
			for(Role r: roles)
			{
				if(r.getRole() == Role.roles.ApartmentRenter)
				{
					tasks.add(new Task(Task.Objective.goTo, this.complex.name));
					Task t = new Task(Task.Objective.patron, this.complex.name);
					tasks.add(t);
					currentTask = t;
					currentTask.sTasks.add(Task.specificTask.payBills);					
					currentState = PersonState.needHome;
					return;
				}
			}
			for(Role r: roles)
			{
				if(r.getRole() == Role.roles.houseRenter)
				{
					tasks.add(new Task(Task.Objective.goTo, house.name));
					Task t = new Task(Task.Objective.patron, this.house.name);
					tasks.add(t);
					currentTask = t;
					currentTask.sTasks.add(Task.specificTask.payBills);					
					currentState = PersonState.needHome;
					return;
				}
			}
		}
		else
		{
			//TODO: USE SPECIFIC TASKS TO SLEEP
			currentState = PersonState.needHome;
			for(Role r: roles)
			{
				if(r.getRole() == Role.roles.ApartmentRenter || r.getRole() == Role.roles.ApartmentOwner)
				{
					tasks.add(new Task(Task.Objective.goTo, this.complex.name));
					Task t = new Task(Task.Objective.house, this.complex.name);
					tasks.add(t);
					currentTask = t;
					currentTask.sTasks.add(Task.specificTask.sleepAtApartment);					
					currentState = PersonState.needHome;
					return;
				}
			}
			for(Role r: roles)
			{
				if(r.getRole() == Role.roles.houseRenter || r.getRole() == Role.roles.houseOwner)
				{
					tasks.add(new Task(Task.Objective.goTo, house.name));
					Task t = new Task(Task.Objective.house, this.house.name);
					tasks.add(t);
					currentTask = t;
					currentTask.sTasks.add(Task.specificTask.sleepAtHome);					
					currentState = PersonState.needHome;
					return;
				}
			}
			//TODO: IF WE GET HERE, THE PERSON IS HOMELESS. MAKE HIM GO TO SLEEP IN SOME RANDOM SPOT
			return;
		}
	}


	public String getName() {
		return this.name;

	}

	
	//Accessors and Getters
	public void addRole(Role.roles r, String location) {
		roles.add(new Role(r, location));
	}
	
	
	public int getHungerLevel(){
	   return hungerLevel;
	}
	
	public void setHungerLevel(int hungerLevel){
	   this.hungerLevel=hungerLevel;
	}
	
	public double getMoney(){
	   return money;
	}
	
	public void setMoney(double money){
	   this.money=money;
	}
	

}
