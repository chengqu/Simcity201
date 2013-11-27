package LYN;


import LYN.gui.CustomerGui;
import LYN.gui.RestaurantGui;
import LYN.interfaces.Cashier;
import LYN.interfaces.Customer;
import LYN.interfaces.Host;
import LYN.interfaces.Waiter;
import agent.Agent;
import agents.Person;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent implements Customer {
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private int TABLENUM;
	private double Check;
	private String choice = null;
	private int a = 3;
	Random randnumber = new Random();
	private double money;
	
	

	// agent correspondents
	private Host host;
	private Waiter waiter;
	//private Menu menu;
	private Cashier cashier;
	Random run = new Random();
	private Person p;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, lookingatfood,callwaitertocome,ordering,reordered,Eating, DoneEating, Leaving, waittocheck,checking,leaverestaurant};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followHost, seated, wanttoorder,waittoorder,reorder,gotfood,doneEating, gotcheck, gotchange,doneLeaving,nofoodandleave, paying};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(Person p, String name){
		super();
		this.p = p;
		this.name = name;
	}

	
	public List<String> menus
	= new ArrayList<String>();
	
	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostAgent host) {
		this.host = host;
	}
	
	public void setWaiter(WaiterAgent waiter){
		this.waiter = waiter;
	}
	

	public void setCashier(CashierAgent cashier){
		this.cashier = cashier;
	}
	
	// Messages

	public void gotHungry() {//from animation
		print("I'm hungry");
		/*
		if(this.name.equals("salad")){
			money = 6;
		}
		money = money + 2;
		*/
		print("My money is: "+ money);
		
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgcannotwait(){
		print("I can not wait, I am leaving " + name);
		customerGui.DoExitRestaurant();
	}

	public void msgFollowMe(Waiter w, int a, Menu m) {
		print("Received msgSitAtTable");
		event = AgentEvent.followHost;
		if(p.money>=5.99 && p.money<8.99){
			menus.add(m.Salad());
		} else {
		menus.add(m.Chicken());
		menus.add(m.Salad());
		menus.add(m.Pizza());
		menus.add(m.Steak());
		}
        this.waiter = w;
		TABLENUM = a;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgnotenoughmoney() {
		event = AgentEvent.nofoodandleave;
		stateChanged();
	}
	public void msgwhatwouldyoulike() {
		Do("Customer receiving message what would you like");
		event = AgentEvent.waittoorder;
		stateChanged();
	}
	
	public void msgwhatwouldyouliketoreorder(String choice) {
		Do("Customer receiving message reorder");
		for (int i = 0; i<menus.size(); i++) {
			if(menus.get(i) == choice){
				menus.remove(i);
				break;
			}
		}
		if(menus.size() == 0) {
			event = AgentEvent.nofoodandleave;
			a = 3;
		} else {
		event = AgentEvent.reorder;
		}
		stateChanged();
	}
	
	public void msgHereisyourfood() {
		Do("Here is my food, customer" + getName());
		event = AgentEvent.gotfood;
		stateChanged();
	}
	
   public void msghereisyourbill(double check) {
		Do("Here is my check");
		event = AgentEvent.gotcheck;
		Check = check;
		stateChanged();
	}
   
   public void msghereisyourchange(double change) {
	   Do("Here is my change" + change);
	   if(change>=0){
	   p.money = change;
	   }
	   event = AgentEvent.gotchange;
	   stateChanged();
   }
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	public void msgArriveatcashier() {
		event = AgentEvent.paying;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
        try{
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.lookingatfood;
			lookatfood();
			return true;
		}
		if (state == AgentState.lookingatfood && event == AgentEvent.wanttoorder){
			state = AgentState.callwaitertocome;
			callwaiter();
			return true;
		}
		if (state == AgentState.callwaitertocome && event == AgentEvent.waittoorder){
			state = AgentState.ordering;
			tellwaiterthechoice();
			return true;
		}
		
		if ((state == AgentState.ordering) && event == AgentEvent.reorder){
			
			event = AgentEvent.none;
			tellwaiterthechoice();
			return true;
		}
		
		if ((state == AgentState.ordering) && event == AgentEvent.gotfood){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.Leaving;
			p.hungerLevel = 0;
			leaveTable();
			return true;
		}
		if( state == AgentState.Leaving && event == AgentEvent.paying) {
			state = AgentState.waittocheck;
			iwannacheck();
			return true;
		}
		if (state == AgentState.waittocheck && event == AgentEvent.gotcheck){
			state = AgentState.checking;
			givemoneytoCashier();
			return true;
		}
		
		if ((state == AgentState.checking && event == AgentEvent.gotchange)){
			state = AgentState.leaverestaurant;
			p.hungerLevel = 0;
			getcheckandleave();
			return true;
		}
		
		if( (event == AgentEvent.nofoodandleave && state == AgentState.ordering)) {
			state = AgentState.leaverestaurant;
			leave();
			return true;
		}
		if( (event == AgentEvent.nofoodandleave && state == AgentState.lookingatfood)) {
			state = AgentState.leaverestaurant;
			leave();
			return true;
		}
		
		if (state == AgentState.leaverestaurant && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
        }catch (ConcurrentModificationException errorCCE) {
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(TABLENUM);//hack; only one table
	}

	private void lookatfood(){
		Do("now look at menu");
		boolean leave = false;
		Random rand1 = new Random();
		if( p.money < 5.99) {
			if (rand1.nextInt(2) == 0) {
			       leave = true;
				timer.schedule(new TimerTask() {
					Object cookie = 1;
					public void run() {
						print("Done looking at menu, now leaving" + cookie);
					
				       msgnotenoughmoney();
			
			}
				},
				1000);
		}
		}
		
		if(leave == false) {
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done thinking about what to eat" + cookie);
				event = AgentEvent.wanttoorder;
				//isHungry = false;
				stateChanged();
			}
		},
		4000);
		}
	}
	
	private void callwaiter(){
		Do("calling waiter to come to take order");
		waiter.msgReadyToOrder(this);
	}
	
	private void tellwaiterthechoice(){
		Do("Sending message to waiter what do I want");
		if(p.money>=5.99 && p.money < 8.99){
			print("I can only afford salad, and I will do that");
			choice = "Salad";
		} else if(this.name.equals("pizza")){
			choice = "Pizza";
		}
		else {
		String[] t = menus.toArray(new String[0]);
		choice = t[run.nextInt(t.length)];
		}
		
	    a = 1;
		waiter.msgHereismychoice(this,choice);
		
	    
	}
	private void EatFood() {
		Do("Eating Food");
	    a = 2;
		
		    
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		10000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveTable() {
		Do("Leaving and sending message to waiter.");
        a = 3;
		waiter.msgDoneEatingAndLeaving(this);
		
		customerGui.gotocashier();
	}

	private void iwannacheck() {
		Do("I wanna check" );
		cashier.msgcustomercheck(this);
	}
	private void givemoneytoCashier(){
		if(p.money < Check){
			cashier.msgcustomernotenoughmoney(this,p.money,Check);
		}
		else {
		cashier.msghereismoney(this,Check,p.money);
		p.money-=Check;
		}
	}
	
	private void getcheckandleave() {
		customerGui.DoExitRestaurant();
		p.hungerLevel = 0;
		p.msgDone();
	}
	
	private void leave() {
		customerGui.DoExitRestaurant();
		waiter.msgDoneEatingAndLeaving(this);
		p.msgDone();
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
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}
	
	public String getchoice(){
		return choice;
	}

	public int getstate() {
		return a;
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
	
	
}

