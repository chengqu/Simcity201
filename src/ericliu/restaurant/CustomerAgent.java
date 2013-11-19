package ericliu.restaurant;

import ericliu.gui.CustomerGui;
import ericliu.gui.RestaurantGui;
import agent.Agent;
import ericliu.interfaces.Customer;
import agents.Person;

import java.util.*;
import java.util.concurrent.Semaphore;

 
/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent implements Customer{
   //PERSON AGENT MEMBER
   private Person person;
   //
   private String name;
   private int hungerLevel = 5;        // determines length of meal
   Timer timer = new Timer();
   private CustomerGui customerGui;

   // agent correspondents
   private WaiterAgent waiter;
   private HostAgent host;
   private CashierAgent cashier;
   private Semaphore atCashier=new Semaphore(0,true);
   private Semaphore atReady=new Semaphore(0,true);
   
   private int tableNumber;
   private int foodOrderNumber;
   //private String order;
   private FoodClass order;
   //private List<String> soldOutFoods;
   private List<FoodClass> soldOutFoods;
   private List<String> STRsoldOutFoods=new ArrayList<String>();
   private List<FoodClass> foodICanBuy=new ArrayList<FoodClass>();
   private boolean AllFoodSoldOut=true;
   //private List<String> restaurantMenu;
   private List<FoodClass> restaurantMenu;
   private boolean buyAffordableFood=false;
   private List<FoodClass> checkedSoldOutFoods=new ArrayList<FoodClass>();
   
   private int seatNumber;
   private double money=22.99;
   private ReceiptClass receipt;
   
   Random randGenerator=new Random();
   
   public int getTableNumber(){
      return tableNumber;
   }
   
//   public void setPaused(){
//      pauseToggle();
//      stateChanged();
//      }
   //    private boolean isHungry = false; //hack for gui
   public enum AgentState
   {DoingNothing, WaitingInRestaurant, dontWantToWait, BeingSeated, Seated, decidingOrder, TooBroke, readyToOrder, Ordered, noMoreFood, GotFood, Eating, GotReceipt, DoneEating, atCashier, TookCareOfReceipt, PaidReceipt, Leaving};
   private AgentState state = AgentState.DoingNothing;//The start state

   public enum AgentEvent 
   {none, drawOrder, drawReceivedOrder, gotHungry, goToStart, goToReady, restaurantFull, followHost, seated, calledWaiter, ordering, ordered, orderSoldOut, replacedOrder, receivedFood, doneEating, doneLeaving};
   AgentEvent event = AgentEvent.none;

   /**
    * Constructor for CustomerAgent class
    *
    * @param name name of the customer
    * @param gui  reference to the customergui so the customer can send it messages
    */
//   public CustomerAgent(String name){
//      super();
//      this.name = name;
//      //checkedSoldOutFoods.add(" ",0,0.0);
//   } 
   
   public CustomerAgent(Person person){
      super();
      this.person=person;
      this.name=person.name;
   }

   /**
    * hack to establish connection to Host agent.
    */
   public void setHost(HostAgent host) {
      this.host = host;
      //tableNumber=this.host.getTableNumber();
   }

   public void setCashier(CashierAgent cashier){
      this.cashier=cashier;
   }
   
   public void setWaiter(WaiterAgent waiter){
      this.waiter=waiter;
   }
   public String getCustomerName() {
      return name;
   }
   // Messages

//   public void msgPaused(){
//      pauseToggle();
//   }
   
   public void msgGotHungry() {//from animation
      print("I'm hungry");
      event = AgentEvent.gotHungry;
      stateChanged();
   }

   public void msgRestaurantIsFull(){
      event=AgentEvent.restaurantFull;
      stateChanged();
   }
    public void msgFollowMe(int tableNumber1,List<FoodClass> menu){
     //state= AgentState.BeingSeated;
     event=AgentEvent.followHost;
     restaurantMenu=menu;
     this.tableNumber=tableNumber1;
     System.out.println("Customer is following waiter.");
     stateChanged();
         
      }
     
   public void msgSitAtTable() {
      //print("Received msgSitAtTable");
      event = AgentEvent.seated;
      stateChanged();
   }
   
   public void msgAreYouReadyToOrder(){
      state=AgentState.readyToOrder;
      stateChanged();
   }
   public void msgWhatDoYouWant(){
      //System.out.println("Customer is ordering food.");
      event=AgentEvent.ordering;
      stateChanged();
   }
   
   public void msgTookOrder(FoodClass customerChoice){
      event=AgentEvent.drawOrder;
      stateChanged();
      
   }
   
   public void msgReceivedOrder(FoodClass customerChoice){
      event=AgentEvent.drawReceivedOrder;
      stateChanged();
   }
   
   public void msgReDoOrder(List<FoodClass> soldOutFoods2){
      soldOutFoods=soldOutFoods2;
      for(FoodClass food:soldOutFoods){
         STRsoldOutFoods.add(food.choice);
      }
      event=AgentEvent.orderSoldOut;
      stateChanged();
   }
   
   public void msgYouMustLeave(){
      state=AgentState.noMoreFood;
      stateChanged();
   }
   public void msgHereIsYourOrder(){
     // System.out.println("Customer Event is: "+ event);
      state=AgentState.GotFood;
      //event=AgentEvent.receivedFood;
      stateChanged();
   }

   public void msgHereIsYourReceipt(ReceiptClass receipt){
      this.receipt=receipt;
    state=AgentState.GotReceipt;
    stateChanged();
   }
   
   public void msgJustPayNextTime(){
      Do("I did not have enough money but he let me off the hook.");
      state=state.TookCareOfReceipt;
      stateChanged();
   }
   
   public void msgThankYouForYourPayment(){
      Do("I paid my bill.");
      state=state.TookCareOfReceipt;
      stateChanged();
   }
   
   public void msgAnimationFinishedGoToSeat() {
      //from animation
      event = AgentEvent.seated;
      stateChanged();
   }
   public void msgAnimationFinishedLeaveRestaurant() {
      //from animation
      event = AgentEvent.doneLeaving;
      stateChanged();
   }

   public void msgAtCashier(){
      atCashier.release();
      //atStart=false;
   }
   
   public void msgAtReady(){
      atReady.release();
   }
   public void msgDoGoToReadySpot(){
      event=AgentEvent.goToReady;
      stateChanged();
   }
   
   public void msgWaitHere(int seatNumber){
      this.seatNumber=seatNumber;
      event=AgentEvent.goToStart;
      stateChanged();
   }
   /**
    * Scheduler.  Determine what action is called for, and do it.
    */
   protected boolean pickAndExecuteAnAction() {
      // CustomerAgent is a finite state machine
//      if(!paused){
         if(event==AgentEvent.goToStart){
            DoGoToWaitingSpot();
            return true;
         }
         if(event==AgentEvent.goToReady){
            DoGoToReady();
            return true;
         }
         if(event==AgentEvent.drawOrder){
            DoDrawOrder();
            return true;
         }
         if(event==AgentEvent.drawReceivedOrder){
            DoDrawReceivedOrder();
            return true;
         }
         if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
            System.out.println("Customer is going to the Restaurant.");
            goToRestaurant();
            state = AgentState.WaitingInRestaurant;
            return true;
         }
         if(state==AgentState.WaitingInRestaurant && event==AgentEvent.restaurantFull){
            //decideIfYouWantToWait();
            return true;
         }
         if(state==AgentState.dontWantToWait && event==AgentEvent.restaurantFull){
            leaveRestaurant();
            return true;
         }
         if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
            
            state=AgentState.decidingOrder;
            SitDown(tableNumber);
            checkFoodICanBuy();
            return true;
         }
         if (state == AgentState.readyToOrder && event == AgentEvent.seated ){
            
            callWaiterToOrder();
            return true;
         }
         
         if (state == AgentState.readyToOrder && event == AgentEvent.ordering){
            if(money<5.99)
               checkMoney();
            else
               orderFood();
            state = AgentState.Ordered;
            return true;
         }
         
         if(state==AgentState.Ordered && event == AgentEvent.orderSoldOut){
            if(buyAffordableFood==true)
               replaceAffordableOrder();
            else
               replaceOrder();
            //event=AgentEvent.replacedOrder;
            return true;
         }
         if (state == AgentState.GotFood /*&& event == AgentEvent.ordered*/){
            
            EatFood();
            state = AgentState.Eating;
            return true;
         }
         if(state==AgentState.noMoreFood && event==AgentEvent.replacedOrder){
            leaveTable();
            return true;
         }
         if (state == AgentState.TooBroke){
            leaveTable();        
            return true;
         }
         
         if (state == AgentState.GotReceipt && event == AgentEvent.doneEating){
            state = AgentState.Leaving;
            payReceipt();
            return true;
         }
         
         if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
            state = AgentState.DoingNothing;
            //no action
            return true;
         }
//      }
     // Do("atReady Available Permits: "+atReady.availablePermits());
      return false;
      
   }

   // Actions

   private void goToRestaurant() {
      Do("Going to restaurant");
      host.msgIWantFood(this);//send our instance, so he can respond to us
      
   }

   private void decideIfYouWantToWait(){
      int shouldIWait=randGenerator.nextInt(2);
      if(shouldIWait==1){
         Do("\nRestaurant is Full but I will wait\n");
         event=AgentEvent.gotHungry;
      }
      else{
         host.msgImNotWaiting(this);
         state=AgentState.dontWantToWait;
      }
   }
   private void SitDown(int tableNumber) {
      //Do("\n\n\n TABLE NUMBER: "+tableNumber+"\n\n\n");
      try {
         atReady.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      customerGui.DoGoToSeat(tableNumber);
      Do("Sitting Down.");
      decideOrder();
      //shouldIBuyAffordableFood();
      
   }

   private void decideOrder(){
      
      timer.schedule(new TimerTask() {
         Object cookie = 1;
         public void run() {
            print("Done deciding, cookie=" + cookie);
            state=AgentState.readyToOrder;
            //isHungry = false;
            stateChanged();
         }
      },
      5000);
      
   } 
   private void callWaiterToOrder(){
      customerGui.drawDecidingOrderMark();
      Do("Calling Waiter");
      waiter.msgReadyToOrder(this);
      event=AgentEvent.calledWaiter;
   }
    
   private void checkMoney(){
      int stayIfNoMoney=0;
      stayIfNoMoney=randGenerator.nextInt(2);
      if(stayIfNoMoney==1){
         Do("I have no money so I'm going to leave.");
         leaveTable();
      }
      else{
         orderFood();
      }
   }
   private void checkFoodICanBuy(){
      for(FoodClass food:restaurantMenu){
         if(food.getCost()<=money){
            foodICanBuy.add(food);
         }
      }
   }
   
   private void shouldIBuyAffordableFood(){
      int INTbuyAffordableFood;
      INTbuyAffordableFood=randGenerator.nextInt(2);
      if(INTbuyAffordableFood==1){
         buyAffordableFood=true;
      }
      
   }
   private void orderFood(){
      
         
         if(name.equals("Steak")){
            foodOrderNumber=0;
         }
         else if(name.equals("Chicken")){
            foodOrderNumber=1;
         }
         else if(name.equals("Salad")){
            foodOrderNumber=2;
         }
         else if(name.equals("Pizza")){
            foodOrderNumber=3;
         }
         else{
            foodOrderNumber=randGenerator.nextInt(restaurantMenu.size());
         }
         Do("Ordered Food");
         if(buyAffordableFood==true)
            if(foodOrderNumber<foodICanBuy.size()){
               order=foodICanBuy.get(foodOrderNumber);
               waiter.msgCustomerOrder(this,order);
            }
            else
               {replaceAffordableOrder();}
         else{
            order=restaurantMenu.get(foodOrderNumber);
            waiter.msgCustomerOrder(this,order);
         }
      
   }
   
   private void replaceAffordableOrder(){
      int tempFoodOrderNumber;
      while(true){
         tempFoodOrderNumber=randGenerator.nextInt(foodICanBuy.size());
//         if(soldOutFoods.size()==4){
//            order=new FoodClass("No More Food",0,0);
//            break;
//         }
//         else if(soldOutFoods.contains(restaurantMenu.get(tempFoodOrderNumber))){
//            continue;
//         }
//         else {
//            foodOrderNumber=tempFoodOrderNumber;
//            order=restaurantMenu.get(foodOrderNumber);
//            break;
//         }
         if(!STRsoldOutFoods.contains(foodICanBuy.get(tempFoodOrderNumber).choice)){
            foodOrderNumber=tempFoodOrderNumber;
            order=foodICanBuy.get(foodOrderNumber);
            break;
         }
         else{
            if(!checkedSoldOutFoods.contains(foodICanBuy.get(tempFoodOrderNumber)))
               checkedSoldOutFoods.add(foodICanBuy.get(tempFoodOrderNumber));
//            for(FoodClass food:checkedSoldOutFoods){
//               Do(food.choice+"\n");
//            }
            if(checkedSoldOutFoods.size()==foodICanBuy.size()){
               //order.choice="No More Food";
               System.out.println("Trying to ask for no more food!");
               //order=new FoodClass("No More Food",0,0);
               state=AgentState.noMoreFood;
               Do("I'm leaving because you have no more food.");
               break;
            }
           continue;
         }
      }
      /*if(order.equals("No More Food")){
         waiter.msgDoneEating(this);
         customerGui.undrawOrder();
         customerGui.DoExitRestaurant();
      }
      else{*/
         //System.out.println("ORDER SUCCESSFULLY REPLACED");
         waiter.msgHereIsReplacedOrder(this, order);
         Do("I replaced my order");
         event=AgentEvent.replacedOrder;
      //}
   }
   private void replaceOrder(){
      int tempFoodOrderNumber;
      while(true){
         tempFoodOrderNumber=randGenerator.nextInt(restaurantMenu.size());
//         if(soldOutFoods.size()==4){
//            order=new FoodClass("No More Food",0,0);
//            break;
//         }
//         else if(soldOutFoods.contains(restaurantMenu.get(tempFoodOrderNumber))){
//            continue;
//         }
//         else {
//            foodOrderNumber=tempFoodOrderNumber;
//            order=restaurantMenu.get(foodOrderNumber);
//            break;
//         }
         if(!STRsoldOutFoods.contains(restaurantMenu.get(tempFoodOrderNumber).choice)){
            foodOrderNumber=tempFoodOrderNumber;
            order=restaurantMenu.get(foodOrderNumber);
            break;
         }
         else{
            if(!checkedSoldOutFoods.contains(restaurantMenu.get(tempFoodOrderNumber)))
               checkedSoldOutFoods.add(restaurantMenu.get(tempFoodOrderNumber));
//            Do("CHECKED SOLD OUT FOODS SIZE: "+checkedSoldOutFoods.size());
            if(checkedSoldOutFoods.size()==restaurantMenu.size()){
               //order.choice="No More Food";
               System.out.println("Trying to ask for no more food!");
               //order=new FoodClass("No More Food",0,0);
               state=AgentState.noMoreFood;
               Do("I'm leaving because you have no more food.");
               break;
            }
           continue;
         }
      }
      waiter.msgHereIsReplacedOrder(this, order);
      Do("I replaced my order");
      event=AgentEvent.replacedOrder;
   }
   private void EatFood() {
      Do("Eating Food");
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
      5000);//getHungerLevel() * 1000);//how long to wait before running task
   }

   private void payReceipt(){
      Do("Leaving Table.");
      customerGui.undrawOrder();
      customerGui.DoGoToCashier();
      waiter.msgDoneEating(this);
      Do("Going to the cashier to pay my bill.");
      System.out.println("MEALPRICE: "+receipt.getMealPrice());
      if(receipt.getMealPrice()>money){
         cashier.msgNotEnoughMoney(this);
      }
      else{
         cashier.msgHereIsMyPayment(this,receipt.getMealPrice());
         money-=receipt.getMealPrice();
      }
      try {
         atCashier.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      timer.schedule(new TimerTask() {
         Object cookie = 1;
         public void run() {
            customerGui.DoExitRestaurant();
         }
      },
      2000);
      state=AgentState.DoingNothing;
   }
   
   private void leaveTable() {
      Do("Leaving.");
      waiter.msgDoneEating(this);
      customerGui.undrawOrder();
      customerGui.DoExitRestaurant();
      state=AgentState.DoingNothing;
   }

   private void leaveRestaurant(){
      Do("\n\nI'm leaving because the restaurant is full\n\n");
      //waiter.msgDoneEating(this);
      state=AgentState.DoingNothing;
   }
   
   //ANIMATIONS
   private void DoGoToWaitingSpot(){
      customerGui.DoGoToWaitingSeat(seatNumber);
      event=AgentEvent.none;
   }
   
   private void DoGoToReady(){
      customerGui.DoGoToReady();
      event=AgentEvent.none;
   }
   
   private void DoDrawOrder(){
      customerGui.drawOrderTaken(order.choice);
      event=AgentEvent.none;
   }
   
   private void DoDrawReceivedOrder(){
      customerGui.drawReceivedOrder(order.choice);
      event=AgentEvent.none;
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

