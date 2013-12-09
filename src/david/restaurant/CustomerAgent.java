package david.restaurant;

import david.restaurant.Interfaces.Customer;
import david.restaurant.Interfaces.Waiter;
import david.restaurant.gui.AnimationPanel;
import david.restaurant.gui.CustomerGui;
import david.restaurant.gui.RestaurantGui;
import david.restaurant.gui.RestaurantPanel;
import david.restaurant.gui.Table;
import agent.Agent;
import agents.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent implements Customer{
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	public CustomerGui customerGui;
	Random random = new Random();
	Semaphore cashierSem = new Semaphore(0);
	
	public Person person;
	
	float money;
	
	private RestaurantPanel panel;

	// agent correspondents
	private HostAgent host;
	private Waiter waiter = null;
	private Menu menu = null;
	private CashierAgent cashier;
	
	private boolean useName = false;
	
	enum fullState {none, stay, leave};
	fullState fState = fullState.none;
	
	enum payState {none, dont, pay};
	payState pState = payState.none;
	
	public enum event {nothing, goToRestaurant, goToTable, seated, decidedOrder, canGiveOrder, canEatFood, canLeave, reorder, noFood, checkReceived
					, fullRestaurant};
	private List<event> events = new ArrayList<event>();
	
	public enum state {none, waitingInLine, beingSeated, decidingOrder, waitingToGiveOrder, waitingToEat, eating, doneAndLeaving };
	//List<InternalState> stateNew = new ArrayList<InternalState>();
	private List<state> states = new ArrayList<state>();
	
	private List<Check> checks = new ArrayList<Check>();
	
	Object eventLock = new Object();
	Object stateLock = new Object();
	Object checkLock = new Object();
 	
	private AnimationPanel animationPanel;
	
	public void print_()
	{
		print(Integer.toString(states.size() + checks.size()));
	}
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(Person p_, String n, HostAgent h, RestaurantPanel p, CashierAgent c){
		super();
		person = p_;
		this.name = p_.getName();
		String[] stringList = n.split(",", -1);
		for(int i = 0; i < stringList.length; i++)
		{
			try
			{
				int a;
				if(i == 0)
				{
					a = Integer.parseInt(n.substring(9));
				}
				else
				{
					a = Integer.parseInt(stringList[i]);
				}
				money = a;
				break;
			}
			catch(NumberFormatException e)
			{
				money = 6 + random.nextInt(15);
			}
		}
		if(n.contains(",l") || n.contains(",L"))
		{
			fState = fullState.leave;
		}
		if(n.contains(",s") || n.contains(",S"))
		{
			if(fState == fullState.leave)
			{
				fState = fullState.none;
			}
			else
			{
				fState = fullState.stay;
			}
		}
		if(n.contains(",d") || n.contains(",D"))
		{
			pState = payState.dont;
		}
		if(n.contains(",p") || n.contains(",P"))
		{
			if(pState == payState.dont)
			{
				pState = payState.none;
			}
			else
			{
				pState = payState.pay;
			}
		}
		states.add(state.none);
		host = h;
		panel = p;
		cashier = c;
		if(pState == payState.none)
		{
			if(random.nextBoolean())
			{
				pState = payState.pay;
			}
			else
			{
				pState = payState.dont;
			}
		}
		print(pState.toString());
		print(Float.toString(money));
		print(fState.toString());
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostAgent host) {
		this.host = host;
	}

	public String getCustomerName() {
		return name;
	}
	
	/**
	 * New Messages for V2
	 */
	
	public void BecomesHungry()
	{
		synchronized(eventLock)
		{

			states.clear();
			events.clear();
			states.add(state.none);
			waiter = null;
			menu = null;
			//print("BecomesHungry");
			events.add(event.goToRestaurant);
			stateChanged();
		}
	}
	
	public void msgIsFull()
	{
		synchronized(eventLock)
		{
			print("msgIsFull");
			events.add(event.fullRestaurant);
			stateChanged();
		}
	}
	
	//add a menu class
	public void FollowMeToTable(Waiter w, Menu m)
	{
		//print("msgFollowMeToTable");
		synchronized(eventLock)
		{
			waiter = w;
			menu = m;
			for(Food item: m.items)
			{
				if(name.contains(item.name))
				{
					useName = true;
					break;
				}
			}
			events.add(event.goToTable);
			stateChanged();
		}
	}
	
	public void msgDecidedOrder()
	{
		synchronized(eventLock)
		{
			events.add(event.decidedOrder);
			stateChanged();
		}
	}
	
	public void WhatWouldYouLike(Waiter w)
	{
		//print("cust: whatWouldYouLikeMsg");
		synchronized(eventLock)
		{
			events.add(event.canGiveOrder);
			stateChanged();
		}
	}
	
	public void HereIsYourFood()
	{
		//print("msgHereIsYouFood");
		synchronized(eventLock)
		{
			events.add(event.canEatFood);
			stateChanged();
		}
	}
	
	public void msgReorder(Menu m)
	{
		print("msgReorder");
		synchronized(eventLock)
		{
			menu = m;
			for(Food item: menu.items)
			{
				//print(item.name);
			}
			useName = false;
			events.add(event.reorder);
			stateChanged();
		}
	}
	
	public void msgNoFood()
	{
		//print("msgNoFood");
		synchronized(eventLock)
		{
			events.add(event.noFood);
			stateChanged();
		}
	}
	
	public void msgHereIsCheck(Check c)
	{
		//print("msgHereIsCheck");
		synchronized(eventLock)
		{
			synchronized(checkLock)
			{
				events.add(event.checkReceived);
				checks.add(c);
			}
		}
		stateChanged();
	}
	
	//gui messages
	public void msgNowSeated()
	{
		//print("msgNowSeated");
		synchronized(eventLock)
		{
			events.add(event.seated);
			stateChanged();
		}
	}
	
	public void msgDoneEating()
	{
		//print("msgDoneEating");
		synchronized(eventLock)
		{
			events.add(event.canLeave);
			stateChanged();
		}
		
	}
	
	public void setPerson(Person person_)
	{
		person = person_;
	}
	
	public void msgAtCashier()
	{
		cashierSem.release();
	}
	
	//don't call stateChanged() in the messages below this comment
	public void msgResetState()
	{
		print("msgResetState");
		synchronized(eventLock)
		{
			synchronized(stateLock)
			{
				if(money < 0)
				{
					money = 50;
				}
				else
				{
					money += (20 + random.nextInt(10));
				}
				panel.customerDone(this);
				if(person != null)
				{
					person.msgDone();
					person.hungerLevel = 0;
				}
			}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/**
		 * New Scheduler
		 */
		if(findEvent(event.goToRestaurant) && findState(state.none))
		{
			DoGoToRestaurant();
			return true;
		}
		
		else if(findEvent(event.fullRestaurant) && findState(state.waitingInLine))
		{
			DoDecideStay();
			return true;
		}
		
		else if(findEvent(event.goToTable) && findState(state.waitingInLine))
		{
			DoGoToTable();
			return true;
		}
		
		else if(findEvent(event.seated) && findState(state.beingSeated))
		{
			//print("sched: DecideOrders");
			DoDecideOrder();
			return true;
		}
	
		else if(findEvent(event.decidedOrder) && findState(state.decidingOrder))
		{
			DoWaitingToGiveOrder();
			return true;
		}
		
		else if(findEvent(event.canGiveOrder) && findState(state.waitingToGiveOrder))
		{
			DoGiveOrder();
			return true;
		}
		
		else if(findEvent(event.canEatFood) && findState(state.waitingToEat))
		{
			DoEatFood();
			return true;
		}
		
		else if(findEvent(event.reorder) && findState(state.waitingToEat))
		{
			DoDecideOrder();
			return true;
		}
		
		else if(findEvent(event.noFood) && findState(state.waitingToEat))
		{
			DoLeaveRestaurant();
			return true;
		}
		
		else if(findEvent(event.canLeave) && findEvent(event.checkReceived) 
				&& findState(state.eating))
		{
			DoPay();
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	private boolean findEvent(event e)
	{
		synchronized(eventLock)
		{
			for(event temp:events)
			{
				if(temp == e)
					return true;
			}
			return false;
		}
	}
	
	private boolean findState(state s)
	{
		synchronized(stateLock)
		{
			for(state temp: states)
			{
				if(temp == s)
				{
					return true;
				}
			}
			return false;
		}
	}

	// Actions
	
	
	/**
	 * New Actions
	 */
	
	void DoGoToRestaurant()
	{
		//state_.remove(states.none);
		//print("doGoToRestaurant");
		synchronized(eventLock)
		{
			synchronized(stateLock)
			{
				print("doGoToRestaurant");
				events.remove(event.nothing);
				host.msgIWantToEat(this);
				events.remove(event.goToRestaurant);
				states.add(state.waitingInLine);
			}
		}
	}
	
	void DoGoToTable()
	{
		//print("DoGoToTable");
		//state_.remove(states.waitingInLine);
		states.add(state.beingSeated);
		customerGui.setPresent(true);
		customerGui.msgCanGoToTable();
		events.remove(event.goToTable);
	}
	
	void DoDecideOrder()
	{
		//print("DoDecideOrder");
		//state_.remove(states.beingSeated);
		int randLimit = 6;
		int time = 5000;
		states.add(state.decidingOrder);
		final CustomerAgent temp = this;
		int rand = random.nextInt(randLimit);
		events.remove(events.size() - 1);
		if(rand == 0)
		{
			rand += 1;
		}
		timer.schedule(new TimerTask(){
			public void run() {
				temp.msgDecidedOrder();
			}
			
		}, (time)/(rand));
	}
	
	void DoWaitingToGiveOrder()
	{
		//print("DoWaitingToGiveOrder");
		states.add(state.waitingToGiveOrder);
		events.remove(event.decidedOrder);
		for(Food food: menu.items)
		{
			if(food.cost < person.money || pState == payState.dont)
			{
				waiter.msgImReadyToOrder(this);
				return;
			}
		}
		print("insufficient funds");
		DoLeaveRestaurant();
	}
	
	void DoGiveOrder()
	{
		//print("DoGiveORder");
		//state_.remove(states.waitingToGiveOrder);
		states.add(state.waitingToEat);
		events.remove(event.canGiveOrder);
		
		if(useName == false)
		{
			//System.out.println(menu.items.size());
			int numChoice;
			while(true)
			{
				numChoice = random.nextInt(menu.items.size());
				if(menu.items.get(numChoice).cost > person.money)
				{
					if(menu.items.size() > 1)
					{
						menu.items.remove(numChoice);
					}
					else if(menu.items.size() == 1 && pState == payState.dont)
					{
						break;
					}
				}
				else
				{
					break;
				}
			}
			customerGui.waitingForFood(Menu.getAbbreviation(menu.items.get(numChoice).name));
			waiter.msgHereIsMyChoice(this, menu.items.get(numChoice).name);
		}
		else
		{
			for(int i = 0; i < menu.items.size(); i++)
			{
				if(name.contains(menu.items.get(i).name))
				{
					int choice = i;
					while(true)
					{
						if(menu.items.get(choice).cost > person.money)
						{
							if(menu.items.size() > 1)
							{
								menu.items.remove(choice);
								choice = random.nextInt(menu.items.size());
							}
							else if(menu.items.size() == 1 && pState == payState.dont)
							{
								break;
							}
						}
						else
						{
							break;
						}
					}
					customerGui.waitingForFood(Menu.getAbbreviation(menu.items.get(choice).name));
					waiter.msgHereIsMyChoice(this, menu.items.get(choice).name);
					break;
				}
			}
		}
	}

	void DoEatFood()
	{
		//print("DoEatFood");
		//System.out.println("InDoEatFood");
		states.add(state.eating);
		customerGui.DoEatingAnimation();
		events.remove(event.canEatFood);
	}

	void DoLeaveRestaurant()
	{
		//print("DoLeaveREstaurant");
		states.add(state.none);
		//state_.remove(states.Eating);
		if(events.contains(event.canLeave))
		{
			events.remove(event.canLeave);
		}
		if(events.contains(event.noFood))
		{
			events.remove(event.noFood);
		}
		events.add(event.nothing);
		customerGui.DoLeaveRestaurant();
		waiter.msgDoneEatingAndLeaving(this);
	}
	
	void DoPay()
	{
		synchronized(checkLock)
		{
			print("DoPay");
			events.remove(event.checkReceived);
			customerGui.DoGoToCashier();
			try {
				cashierSem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(person.money > checks.get(0).balance)
			{
				person.money -= checks.get(0).balance;
				Check c = checks.get(0);
				checks.remove(c);
				cashier.msgHereIsMoney(c, c.balance);
			}
			else
			{
				Check c = checks.get(0);
				checks.remove(c);
				cashier.msgCantPay(c);
			}
		}
		DoLeaveRestaurant();
	}
	
	void DoDecideStay()
	{
		print("DoDecideStay");
		final CustomerAgent c = this;
		events.remove(event.fullRestaurant);
		if(fState == fullState.none)
		{
			if(random.nextBoolean())
			{
				print("Leaving");
				host.msgLeaving(this);
				timer.schedule(new TimerTask()
				{
					public void run() {
						c.msgResetState();
					}
					
				}, 1000);
			}
			else
			{
				print("Staying");
				host.msgStaying(this);
			}
		}
		else if(fState == fullState.stay)
		{
			host.msgStaying(this);
		}
		else
		{
			host.msgLeaving(this);
			timer.schedule(new TimerTask()
			{
				public void run() {
					c.msgResetState();
				}
				
			}, 1000);
		}
	}
	
	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
	
	public void setName(String n)
	{
		name = n;
	}
}

