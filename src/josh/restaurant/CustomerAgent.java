package josh.restaurant;

import agent.Agent;
import agents.Person;

import java.util.*;
import java.util.concurrent.Semaphore;

import josh.restaurant.CookAgent.MyOrder;
import josh.restaurant.CookAgent.orderState;
import josh.restaurant.gui.CustomerGui;
import josh.restaurant.gui.OrderGui;
import josh.restaurant.gui.RestaurantGui;
import josh.restaurant.interfaces.Customer;
import josh.restaurant.interfaces.Waiter;


/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent implements Customer {
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 
	
	Person person = null;
	
	private String name;
	private int hungerLevel = 10;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private OrderGui orderGui; 
	int whereAmISitting; 
	//private Semaphore atDestination = new Semaphore(0,true);
	
	// agent correspondents
	private HostAgent host;
	private Waiter waiter;
	private CashierAgent cashier; 
	
	private float cashOnHand = 0;
	
	final static float steakprice = (float) 15.99; 
	final static float chickenprice = (float) 10.99;
	final static float saladprice = (float) 5.99;
	final static float pizzaprice = (float) 8.99;
	
	// private boolean isHungry = false; //hack for gui
	
	//agent states are changed in messages
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, WaitingForHostToSit, 
		BeingSeated, Sitting, WaitingAtTable, AskedForOrder, 
		WaitingForFood, Eating, DoneEating, ReceivedBill, Paying, 
		Leaving, ReAskedForOrder, GoingToPay, paymentNoted};
	private AgentState state = AgentState.DoingNothing;  //The start state

	//agent events are changed in events
	public enum AgentEvent 
	{none, gotHungry, waitForHost, followHost, seated, decided, doneEating, 
		doneLeaving, atCashier, donePaying, decidedToLeave, paidCashier};
	AgentEvent event = AgentEvent.none;
	
	private String orderChoice; 
	
	CustomerBill bill = new CustomerBill(); 
	public class CustomerBill {
		public float charge_;
		public float inDebt_; 
		public boolean owesMoney;
		CustomerBill() {
			 charge_ = 0;
			 inDebt_ = 0; 
			 owesMoney = false;
		}
	}
	
	
	MyMenu menu = new MyMenu();
	public class MyMenu {
		List <String> foodOnMenu_; 
		Map <String, Float> prices_;
		Random generate_; //= new Random();
		MyMenu() {
			generate_ = new Random();
			foodOnMenu_ = new ArrayList<String>(); 
			prices_ = new HashMap<String, Float>();
			initFoodOnMenu(); 
		}
		//use this when waiter passes me menu
		void addFoodToMyMenu(String item) {
			foodOnMenu_.add(item); 
		}
		void initFoodOnMenu() {
			foodOnMenu_.clear();
			foodOnMenu_.add("Steak"); 
			foodOnMenu_.add("Chicken");
			foodOnMenu_.add("Salad");
			foodOnMenu_.add("Pizza");
			prices_.clear();
			prices_.put("Steak", steakprice); 
			prices_.put("Chicken", chickenprice); 
			prices_.put("Salad", saladprice);
			prices_.put("Pizza", pizzaprice);
		}
		public String getRandomSelection () { 
			if (foodOnMenu_.isEmpty()) { //hack so I dont get null pointer exception if whole market is dead
				throw new IllegalArgumentException("nothing on menu");
			}
			int i = generate_.nextInt(foodOnMenu_.size());
			return foodOnMenu_.get(i); 
		}
		public void removeChoice(String choice) {
			foodOnMenu_.remove(choice);
			print("removed" + choice + "from menu!!!"); 
		}
		public boolean checkIfOnMenu(String string) {
			return foodOnMenu_.contains(string); 
		}
	}
	
	// DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA 

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String Name){
		super();
		name = Name;
		//print("your name is " + name); 
	}
	
	//constructor with second arguement
	public CustomerAgent(String Name, Person p){
		super();
		name = Name;
		person = p;
		//print("your name is " + name); 
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
	
	//function that gives money to customers every time he gets hungry. 
	//called at actnGoToRestaurant 
	public void initCashOnHand() {
		
		Random generate = new Random(); 
		int i = generate.nextInt(4); // generates random int 0-3
		
		if (name.contains("broke")) {
			cashOnHand = 1; 
		}
		else if (name.contains("6")) {
			cashOnHand = 6;
		}
		else {
			switch (i) {
				//random initialization of cash every time customer enters restaurant
				case (0) : cashOnHand = cashOnHand + 4; break; 
				case (1) : cashOnHand = cashOnHand + 6; break;
				case (2) : cashOnHand = cashOnHand + 12; break; 
				case (3) : cashOnHand = cashOnHand + 16; break; 
			}
		}
	}
	
	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	
	public void msgLeaveEarly() {
		
		customerGui.DoExitRestaurant(this);
		
		state = AgentState.DoingNothing;
		
		//person should not pay for anything since left early
		//person should also not have hunger level changed
		//	since restaurant is being left early 
		person.msgDone();
		
		stateChanged();
	}
	
	//from waiter
	public void msgHereIsYourBill(float charge) {
		
		print("I got my bill!");
		print("I already owe $" + bill.inDebt_);
		
		bill.charge_ = charge + bill.inDebt_; 
		bill.inDebt_ = 0;
		
		print("I am charged $" + bill.charge_); 

		state = AgentState.ReceivedBill;
		stateChanged();
	}
	
	//from cashier
	public void msgBillRecieved(float moneyOwed) {
		
		bill.inDebt_ = moneyOwed; 
		
		if (moneyOwed > 0) {
			print("I owe $" + moneyOwed + " next time"); 
		}
		
		state = AgentState.paymentNoted; 
		stateChanged(); 
	}
	
	//from waiter 
	public void msgFollowMeToTable(int table, Waiter w) {
		print("Received msgSitAtTable");
		whereAmISitting =  table; 
		event = AgentEvent.waitForHost;
		setWaiter(w); 
		stateChanged();
	}
	
	//from waiter
	public void msgWhatWouldYouLike() {
		state = AgentState.AskedForOrder;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike(String foodDontHave) {
		state = AgentState.ReAskedForOrder;
		
		//remove the food the cook doesnt have right now...
		menu.removeChoice(foodDontHave);
		//remove foodDontHave
		
		stateChanged(); 
	}
	
	//from waiter
	public void msgHereIsYourFood() {
		state = AgentState.Eating;
		stateChanged(); 
	}
	
	
	// animation messages below....
	
	public void gui_msgAnimationFinishedGoToCashier() {
		event = AgentEvent.atCashier;
		stateChanged();
	}
	
	public void gui_msgAnimationFinishedGoToSeat() {  //from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	public void gui_msgAnimationFinishedLeaveRestaurant() {  //from animation
		event = AgentEvent.doneLeaving;
		
		
		
		
		stateChanged();
	}
	
	public void gui_msgGotHungry() {    //from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		//added this because sometimes customers recieved messages after leaving restaurant 
		//I don't think its dangerous because gui setEnabled controls usages of this behavior anyways
		state = AgentState.DoingNothing; 
		stateChanged();
	}

	// MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	// SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	protected boolean pickAndExecuteAnAction() {
		
		//	********CustomerAgent is a finite state machine*********

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			actnGoToRestaurant();
			return true;
		}
		
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.waitForHost ){
			state = AgentState.WaitingForHostToSit;
			//.....
			return true;
		}
		
		if (state == AgentState.WaitingForHostToSit && event == AgentEvent.waitForHost ){
			state = AgentState.BeingSeated;
			actnSitDown();  
			return true;
		}
		
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Sitting;	
			actnMakeUpMyMind();
			return true;
		}

		if (state == AgentState.Sitting && event == AgentEvent.decided ) {
			state = AgentState.WaitingAtTable;
			actnCallOverWaiter(); 
			return true; 
		}
		
		if (state == AgentState.AskedForOrder && event == AgentEvent.decided) {
			state = AgentState.WaitingForFood; 
			print("call make selection in scheduler"); 
			actnMakeSelection();
			return true; 
		}
		
		if (state == AgentState.Eating && event == AgentEvent.decided) {
			actnEatFood();
			/*no need to run the scheduler right here because we are going
			 * to block the action with the actnEatFood method which includes
			 * inside of it a timer
			*/
			//return true;
		}	
		
		//watch for potential problem...finished eating and bill?????
		if (state == AgentState.ReceivedBill && event == AgentEvent.doneEating){  
			state = AgentState.GoingToPay;
			actnGoToCashier();  
			return true;
		}
		
		if (state == AgentState.GoingToPay && event == AgentEvent.atCashier){
			state = AgentState.Paying;
			actnPayBill();
			return true;
		}
		
		/*
		if (state == AgentState.Paying && event == AgentEvent.donePaying){
			state = AgentState.Leaving;
			
			return true;
		}
		*/
		
		if (state == AgentState.paymentNoted && event == AgentEvent.paidCashier){
			state = AgentState.Leaving;
			actnLeaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		
		//********* NON NORMATIVE **************
		
		if (state == AgentState.ReAskedForOrder && event == AgentEvent.decided)  {
			state = AgentState.WaitingForFood;
			/*
			 * go back to finite state --> 
			 * (state == AgentState.AskedForOrder && event == AgentEvent.decided) 
			 * 		actnMakeSelection()....
			 */
			//waiter agent is already at the table
			actnMakeSelection(); 
			return true;
		}
		
		//scenario if the customer wants to leave early due to the non norm events
		if (state == AgentState.Leaving && event == AgentEvent.decidedToLeave) {
			state = AgentState.DoingNothing;
			print("in scheduler, leaving early"); 
			actnLeaveTable();
			return true;
		}
		
		return false;
	}

	//SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER 
	
	// *********************************************************************************************
	// ** ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS **
	// *********************************************************************************************

	//for when customer wants to leave immediately 
	private void actnLeaveRestaurantNow() {
		
		//sent waiter a special message so as to disrupt workflow so doesn't serve customer anymore 
		waiter.msgLeavingEarly(this);
		
		state = AgentState.Leaving;
		event = AgentEvent.decidedToLeave; 
		
		print("decided to leave restaurant early"); 
		
		//to get rid of order icon
		orderGui.finishedFood();
		
		//this command not needed anymore, initate movement through scheuler now
		//actnLeaveTable();
		
	}
	
	private void actnGoToCashier() {
		
		orderGui.finishedFood();
		
		print("ACTN Going to cashier now"); 
		customerGui.DoGoToCashier();
	}
		
	private void actnPayBill() {
		
		//do not make event donePaying or else customer will go onto next state
		event = AgentEvent.paidCashier;
		
		//already at cashier
	
		float tempMoney;
		print("I have " + cashOnHand + " dollars.");
		
		//debt has already been added to bill charge 
		tempMoney = cashOnHand - (bill.charge_);
		
		if (tempMoney >= 0) {
			cashOnHand = cashOnHand - bill.charge_; 
			bill.owesMoney = false;
			
			//directly access person's money right here
			person.money  -= bill.charge_;
			
			cashier.msgHereIsMyPayment(this, bill.charge_);
		}
		else {
			cashier.msgHereIsMyPayment(this, cashOnHand);
			cashOnHand = 0;
			
			//directly access person's money right here
			person.money -= cashOnHand;
			
		}
		
		print("I now have " + cashOnHand + " dollars.");

	}
	
	private void actnMakeUpMyMind() {
		
		timer.schedule(new TimerTask() {
			public void run() {
				print("I've decided what I should order on this tasty menu");
				event = AgentEvent.decided;
				stateChanged();
			}
		}, 1500);  //time it takes to make up order choice
	}
	
	private void actnCallOverWaiter() {
		Do("try to whistle the waiter down");
		orderGui.setupOrderLoc(whereAmISitting);
		orderGui.setPresent(true);
		waiter.msgImReadyToOrder(this);
	}
	
	private void actnGoToRestaurant() {
		Do("Going to restaurant");
		
		menu.initFoodOnMenu();
		initCashOnHand(); 
		
		host.msgIWantToEat(this);   //send our instance, so he can respond to us, the specific customer
	}

	private void actnSitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(this, whereAmISitting);  
	}
	
	private void actnMakeSelection() {
		Do("deciding what to eat on this tasty menu");
		if (name.contains("pizza")) {
			print("HACK");
			orderChoice = "Pizza"; 
		}
		else if (name.contains("salad")) {
			print("HACK");
			orderChoice = "Salad"; 
		}
		else if (name.contains("steak")) {
			print("HACK");
			orderChoice = "Steak"; 
		}
		else if (name.contains("chicken")) {
			print("HACK");
			orderChoice = "Chicken"; 
		}
		else {
			try {
				orderChoice = menu.getRandomSelection();  // set up stuff for order gui associated with this customer
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
				actnLeaveRestaurantNow();
				return; //so that we don't  order anything from waiter
			}
		}
		
		//initialize random generator for different scenrios
		Random generate = new Random();
		int chance = generate.nextInt(2); //generates random number 0-1
		
		//if random selection is more expensive than cashOnHand
		if (menu.prices_.get(orderChoice) > cashOnHand) {
			//either you can go home or not...
			if (name.contains("cheater")) { //item too expensive but you stay
				print("you are a cheater"); 
				//go on...do nothing
			}
			else if (name.contains("inahurry") || chance == 1) { //item is too expensive so you leave
				print("I am in a hurry and I am going to leave");
				actnLeaveRestaurantNow();
				return; //so that we don't  order anything from waiter
			}
			else {
				print("I dont have that much money so I will just order the cheapest thing"); 
				orderChoice = "Salad"; 
				//if still doesn't have money for cheapest item...
				if (menu.prices_.get(orderChoice) > cashOnHand) {
					if (name.contains("dishonest") || chance == 1 ) { //cheapest item is too expensive, but stay
						print("I cannot pay, but im gonna order anyways"); 
					}
					else { 
						print("I cannot pay for anything on this menu, leaving"); //cheapest item is too expensive, leave
						actnLeaveRestaurantNow();
						return; //so that we don't  order anything from waiter
					}
				}
			}
				
			//this is how we check to see if it was removed from the menu, which means
			//that the customer has previously ordered it an its not available, this is for the cheapest item
			if (!menu.checkIfOnMenu("salad")) {
				print("The cheapest item is no longer available, leaving"); 
				actnLeaveRestaurantNow();
				return; //so that we don't  order anything from waiter
			}
		}	
		
		//if the person is adamant about eating only the first thing that he ordered!!!!
		//this will activate in the hack cases...
		if (!menu.checkIfOnMenu(orderChoice)) {
			print("The item that I ordered is no longer in stock"); 
			actnLeaveRestaurantNow();
			return; //so that we don't  order anything from waiter
		}
		
		//now that all of the hacks an non-norms are out of the way...
		//set up the order icon and let waiter know what this order is
		orderGui.setupOrderIcon(orderChoice);
		waiter.msgHereIsMyChoice(this, orderChoice);
		
		//delete temporary random number generator
		generate = null;
	}

	private void actnEatFood() {
		Do("Eating Food");
		orderGui.gui_msgfoodDelivered();
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			//Object cookie = 1; 
			public void run() {
				print("Done eating: " + orderChoice);
				event = AgentEvent.doneEating;  // AgentEvent is an enum
				//isHungry = false;
				stateChanged();
			}
		},
		5000);   //getHungerLevel() * 1000);//how long to wait before running task
	}

	private void actnLeaveTable() {
		Do("Leaving.");
		
		//orderGui.finishedFood();
	
		waiter.msgDoneEatingAndLeaving(this);
		customerGui.DoExitRestaurant(this);
		//table is not free yet, not until waiter clears it
		
		person.hungerLevel = 0;
		
		person.msgDone();
	}

	// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS 
	
	// Accessors, etc.
	
	public void setWaiter(Waiter w){
		waiter = w; 
	}

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

	public void setOrderGui(OrderGui order) {
		orderGui = order; 
	}

	public void setCashier(CashierAgent cashier) {
		this.cashier = cashier;
	}
	
	public float getCashOnHand () {
		return cashOnHand;
	}
}

