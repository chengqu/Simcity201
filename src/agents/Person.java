package agents;

import java.util.ArrayList;
import java.util.List;

import simcity201.gui.Bank;
import simcity201.gui.GlobalMap;
import simcity201.gui.PassengerGui;
import Buildings.ApartmentComplex;
import Buildings.ApartmentComplex.Apartment;
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
	int age;
	String location = "birth";
	boolean tempBool = true;
	
	private String name;
	public List<ApartmentBill> bills = new ArrayList<ApartmentBill>();
	public List<Grocery> groceries = new ArrayList<Grocery>();
	
	public Apartment apartment = null;
	public ApartmentComplex complex = null;
	public HousePanelGui house = null;
	
	public Task currentTask = null; 
	
	PassengerAgent passenger;
	
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
		stateChanged();
	}
	
	public void msgAtDest()
	{
		synchronized(eventLock)
		{
			events.add(PersonEvent.atDest);
			frontEvent = PersonEvent.atDest;
		}
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
		passenger.msgGoTo(this, t.getLocation(), null, null);
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
				/*Need to add addCustomer to this cheng's restaurant panel or gui*/
				//temp.restPanel.addCustomer(this);
				return;
			}else if(GlobalMap.getGlobalMap().searchByName(t.getLocation()).getClass() == Bank.class)
			{
				Bank temp = (Bank)GlobalMap.getGlobalMap().searchByName(t.getLocation());
				temp.addCustomer(this);
				/*Need to add addCustomer to this cheng's restaurant panel or gui*/
				//temp.restPanel.addCustomer(this);
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
		
		
		if (tempBool){
			tasks.add(new Task(Task.Objective.goTo, "Market"));
			tasks.add(new Task(Task.Objective.patron, "Market"));
			
			currentState = PersonState.needStore;
			tempBool = false;
			return;
		}
		
		if(groceries.size() > 0)
		{
			//... deposit groceries at home || apartment
			for(Role r: roles)
			{
				if(r.getRole() == Role.roles.ApartmentOwner || r.getRole() == Role.roles.ApartmentRenter)
				{
					tasks.add(new Task(Task.Objective.goTo, complex.name));
					tasks.add(new Task(Task.Objective.house, complex.name));
					currentState = PersonState.needHome;
					return;
				}
				if(r.getRole() == Role.roles.houseOwner || r.getRole() == Role.roles.houseRenter)
				{
					tasks.add(new Task(Task.Objective.goTo, house.name));
					tasks.add(new Task(Task.Objective.house, house.name));
					currentState = PersonState.needHome;
					return;
				}
			}
		}
		else if(false /*needToWork*/)
		{
			
			return;
		}
		
		else if(hungerLevel > hungerThreshold)
		{
			tasks.add(new Task(Task.Objective.goTo, "Rest2"));
			tasks.add(new Task(Task.Objective.patron, "Rest2"));
			currentState = PersonState.needRestaurant;
			return;
		}
		else if(tempBool)
		{
			//... go home or go to apartment
			currentState = PersonState.needHome;
			tempBool = false;
			for(Role r: roles)
			{
				if(r.getRole() == Role.roles.ApartmentRenter || r.getRole() == Role.roles.ApartmentOwner)
				{
					tasks.add(new Task(Task.Objective.goTo, complex.name));
					tasks.add(new Task(Task.Objective.house, complex.name));
					return;
				}
			}
			for(Role r: roles)
			{
				if(r.getRole() == Role.roles.houseRenter || r.getRole() == Role.roles.houseOwner)
				{
					houseBillsToPay = 0;
					tasks.add(new Task(Task.Objective.goTo, house.name));
					tasks.add(new Task(Task.Objective.house, house.name));
					return;
				}
			}

			return;
		}
		else if(bills.size() > 0 || houseBillsToPay > 0)
		{
			for(Role r: roles)
			{
				if(r.getRole() == Role.roles.ApartmentRenter)
				{
					tasks.add(new Task(Task.Objective.goTo, complex.name));
					tasks.add(new Task(Task.Objective.house, complex.name));
					currentState = PersonState.needHome;
					return;
				}
			}
			for(Role r: roles)
			{
				if(r.getRole() == Role.roles.houseRenter)
				{
					houseBillsToPay = 0;
					tasks.add(new Task(Task.Objective.goTo, house.name));
					tasks.add(new Task(Task.Objective.house, house.name));
					currentState = PersonState.needHome;
					return;
				}
			}
		}
		 if(house != null)		//TODO: add groceries to house
			{
				if(house.housePanel.house.returngroceries().size()!=0){
					groceries = house.housePanel.house.returngroceries();
					tasks.add(new Task(Task.Objective.goTo, "Market"));
					tasks.add(new Task(Task.Objective.patron, "Market"));
					currentTask.sTasks.add(Task.specificTask.buyGroceries);
					currentState = PersonState.needStore;
				}
				return;
			}
		else if(apartment != null)
		{
			if(apartment.Fridge.size() == 0)
			{
				tasks.add(new Task(Task.Objective.goTo, "Market"));
				tasks.add(new Task(Task.Objective.patron, "Market"));
				currentState = PersonState.needStore;
				return;
			}
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
		else if(wantCar == true)
		{
			float totalMoney = (float)money + payCheck;
			for (Account acc : accounts) {
				totalMoney += acc.getBalance();
				if (acc.getBalance() >= 2*cashLowThreshold &&
						money < cashLowThreshold) {
				}
			}
			
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
				Market m = (Market)GlobalMap.getGlobalMap().searchByName("Market");
				tasks.add(new Task(Task.Objective.goTo, m.name));
				tasks.add(new Task(Task.Objective.patron, m.name));
				currentState = PersonState.needStore;
				return;
			}
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
		else if(money <= cashLowThreshold)
		{
			//get money from bank
			Bank b = (Bank)GlobalMap.getGlobalMap().searchByName("Bank");
			tasks.add(new Task(Task.Objective.goTo, b.name));
			tasks.add(new Task(Task.Objective.patron, b.name));
			currentState = PersonState.needBank;
			return;
		}
		
		else
		{
			//... go home or go to apartment
			currentState = PersonState.needHome;
			for(Role r: roles)
			{
				if(r.getRole() == Role.roles.ApartmentRenter || r.getRole() == Role.roles.ApartmentOwner)
				{
					tasks.add(new Task(Task.Objective.goTo, complex.name));
					tasks.add(new Task(Task.Objective.house, complex.name));
					return;
				}
			}
			for(Role r: roles)
			{
				if(r.getRole() == Role.roles.houseRenter || r.getRole() == Role.roles.houseOwner)
				{
					houseBillsToPay = 0;
					tasks.add(new Task(Task.Objective.goTo, house.name));
					tasks.add(new Task(Task.Objective.house, house.name));
					return;
				}
			}

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
