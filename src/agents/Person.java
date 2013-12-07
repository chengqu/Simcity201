package agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import simcity201.gui.Bank;
import simcity201.gui.CarGui;
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
	public float money;
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
	
	/**
	 * FLAGS
	 */
	
	public boolean depositGroceries = false;
	public boolean createAccount = false;
	public boolean getMoneyFromBank = false;
	public boolean depositMoney = false;
	public boolean buyGroceries = false;
	public boolean eatFood = false;
	public boolean payBills = false;
	public boolean goToSleep = false;
	
	public Object commandLock = new Object();
	
	
	
	//add a bank name string later, so he doesn't have to look through the map to get it <--- maybe we want to do this

	public int hungerThreshold = 20; 
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
	public final int ssn;
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
		ssn=GlobalMap.getGlobalMap().getNewSSN();
		money = 0;
		payCheck = 0;
		hungerLevel = 0;
		age = 0;
		currentState = PersonState.none;
		frontEvent = PersonEvent.none;
		car = new CarAgent("audi");
		  CarGui carGui = new CarGui(car);
		  car.setGui(carGui);
		   car.startThread();
		   SimcityPanel.guis.add(carGui);
		   
		this.passenger = new PassengerAgent(name, this);
	      PassengerGui g = new PassengerGui(passenger);
	      passenger.setGui(g);
	      SimcityPanel.guis.add(g);
	      passenger.startThread();
	      
	      
		   
	      s = new StopAgent(GlobalMap.getGlobalMap().buses.get(0), null);
	      s.startThread();
	      
	      final Person p = this;
	}
	
	public Person(String name, boolean mockpersonDontUseExceptMakingMockPerson) {
		this.name=name;
		ssn=GlobalMap.getGlobalMap().getNewSSN();
		money = 0;
		payCheck = 0;
		hungerLevel = 0;
		age = 0;
		currentState = PersonState.none;
		frontEvent = PersonEvent.none;
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
		GlobalMap.getGlobalMap().getGui().controlPanel.personEditor.updatePerson(this);
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
		for(Role r : roles)
		{
			if(r.getRole().equals(Role.roles.JonnieWalker))
			{
				passenger.msgGoTo(this,t.getLocation(), null, null);
				return;
			}
			if(r.getRole().equals(Role.roles.preferCar))
			{
				passenger.msgGoTo(this,t.getLocation(), car, null);
				return;
			}
		   if(r.getRole().equals(Role.roles.preferBus))
			{
				passenger.msgGoTo(this,t.getLocation(), null, this.s);
				return;
			}

		}
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
		
		for(Role r : roles)
		{
			if(r.getRole().equals(Role.roles.JonnieWalker))
			{
				passenger.msgGoTo(this,t.getLocation(), null, null);
				return;
			}
			if(r.getRole().equals(Role.roles.preferCar))
			{
				passenger.msgGoTo(this,t.getLocation(), car, null);
				return;
			}
			if(r.getRole().equals(Role.roles.preferBus))
			{
				passenger.msgGoTo(this,t.getLocation(), null, this.s);
				return;
			}

		}
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
		for(Role r : roles)
		{
			if(r.getRole().equals(Role.roles.JonnieWalker))
			{
				passenger.msgGoTo(this,t.getLocation(), null, null);
				return;
			}
			if(r.getRole().equals(Role.roles.preferCar))
			{
				passenger.msgGoTo(this,t.getLocation(), car, null);
				return;
			}
			if(r.getRole().equals(Role.roles.preferBus))
			{
				passenger.msgGoTo(this,t.getLocation(), null, this.s);
				return;
			}

		}
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
		for(Role r : roles)
		{
			if(r.getRole().equals(Role.roles.JonnieWalker))
			{
				passenger.msgGoTo(this,t.getLocation(), null, null);
				return;
			}
			if(r.getRole().equals(Role.roles.preferCar))
			{
				passenger.msgGoTo(this,t.getLocation(), car, null);
				return;
			}
			if(r.getRole().equals(Role.roles.preferBus))
			{
				passenger.msgGoTo(this,t.getLocation(), null, this.s);
				return;
			}
		  
		}
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
		
		synchronized(commandLock)
		{
			float totalMoney_ = (float)this.money + payCheck;
			for (Account acc : accounts) {
				totalMoney_ += acc.getBalance();
			}
	
			if(!(depositGroceries || createAccount || getMoneyFromBank || buyGroceries ||
					eatFood || payBills || goToSleep))
			{
				if(this.groceries.size() > 0)
				{
					depositGroceries = true;
				}
				if(accounts.isEmpty())
				{
					//make an account at the bank.
					createAccount = true;
				}
				if(payCheck >= payCheckThreshold)
				{
					//deposit money
					depositMoney = true;
				}
				if(this.money < this.cashLowThreshold)
				{
					getMoneyFromBank = true;
				}
				if(apartment != null && apartment.Fridge.size() == 0)
				{
					buyGroceries = true;
				}
				if(house != null && house.housePanel.returngroceries().size()!=0)		//TODO: add groceries to house
				{
					buyGroceries = true;
				}
				if(hungerLevel > this.hungerThreshold)
				{
					eatFood = true;
				}
				if(bills.size() > 0 || houseBillsToPay > 0)
				{
					payBills = true;
				}
				else
				{
					goToSleep = true;
				}
			}
			
			if(needToWork)
			{
				//... need to add work 
				needToWork = false;
				GlobalMap.getGlobalMap().getGui().controlPanel.personEditor.updatePerson(this);
				return;
			}
			if(depositGroceries)
			{
				depositGroceries = false;
				GlobalMap.getGlobalMap().getGui().controlPanel.personEditor.updatePerson(this);
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
				if(house != null)
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
			if(createAccount)
			{
				createAccount = false;
				GlobalMap.getGlobalMap().getGui().controlPanel.personEditor.updatePerson(this);
				//make an account at the bank.
				Bank b = (Bank)GlobalMap.getGlobalMap().searchByName("Bank");
				tasks.add(new Task(Task.Objective.goTo, b.name));
				tasks.add(new Task(Task.Objective.patron, b.name));
				currentState = PersonState.needBank;
				return;
			}
			if(depositMoney)
			{
				depositMoney = false;
				GlobalMap.getGlobalMap().getGui().controlPanel.personEditor.updatePerson(this);
				//deposit money
				Bank b = (Bank)GlobalMap.getGlobalMap().searchByName("Bank");
				tasks.add(new Task(Task.Objective.goTo, b.name));
				tasks.add(new Task(Task.Objective.patron, b.name));
				currentState = PersonState.needBank;
				return;
			}
			if(wantCar)
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
					GlobalMap.getGlobalMap().getGui().controlPanel.personEditor.updatePerson(this);
					return;
				}
			}
			if(getMoneyFromBank)
			{
				getMoneyFromBank = false;
				GlobalMap.getGlobalMap().getGui().controlPanel.personEditor.updatePerson(this);
				float totalMoney = payCheck;
				for (Account acc : accounts) {
					totalMoney += acc.getBalance();
				}
				if(totalMoney < 2 * this.cashLowThreshold)
				{
					
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
			if(buyGroceries && apartment != null)
			{
					buyGroceries = false;
					GlobalMap.getGlobalMap().getGui().controlPanel.personEditor.updatePerson(this);
					homefood = apartment.foodNeeded();
					tasks.add(new Task(Task.Objective.goTo, "Market"));
					Task t = new Task(Task.Objective.patron, "Market");
					tasks.add(t);
					currentTask = t;
					currentTask.sTasks.add(Task.specificTask.buyGroceries);					
					currentState = PersonState.needStore;
					return;
				
			}
			if(buyGroceries && house != null)		//TODO: add groceries to house
			{
				
				buyGroceries = false;
				GlobalMap.getGlobalMap().getGui().controlPanel.personEditor.updatePerson(this);
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
			if(eatFood)
			{
				eatFood = false;
				GlobalMap.getGlobalMap().getGui().controlPanel.personEditor.updatePerson(this);
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
			if(payBills)
			{
				payBills = false;
				GlobalMap.getGlobalMap().getGui().controlPanel.personEditor.updatePerson(this);
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
			if(goToSleep)
			{
				goToSleep = false;
				GlobalMap.getGlobalMap().getGui().controlPanel.personEditor.updatePerson(this);
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
				return;
			}
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
	   this.money=(float)money;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public String toString()
	{
		return name;
	}
}
