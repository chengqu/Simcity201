package ericliu.restaurant;

import agent.Agent;
import agents.Person;
import agents.WaiterBaseAgent;
import ericliu.restaurant.CookAgent.OrderEvent;
import ericliu.restaurant.CustomerAgent.AgentEvent;
import ericliu.test.mock.EventLog;
import ericliu.test.mock.LoggedEvent;
//import restaurant.HostAgent.Table;
import ericliu.gui.HostGui;
import ericliu.gui.WaiterGui;
import ericliu.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class WaiterAgent extends WaiterBaseAgent implements Waiter{
   //Person Class
   public Person person;
   
   public EventLog log = new EventLog();
   
   //Timer for PayCheck
   Timer payCheckTimer = new Timer();
   private double hoursWorked;
   
   
   static final int NTABLES = 3;//a global for the number of tables.
   //Notice that we implement waitingCustomers using ArrayList, but type it
   //with List semantics.
   public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
   public ArrayList<Table> tables;
   
   FoodClass Steak= new FoodClass("Steak", 0, 15.99);
   FoodClass Chicken= new FoodClass("Chicken", 0, 10.99);
   FoodClass Salad=new FoodClass("Salad", 0, 5.99);
   FoodClass Pizza=new FoodClass("Pizza", 0, 8.99);
   
   //public List<String> menu= Arrays.asList("Steak","Chicken","Salad","Pizza");
   public List<FoodClass> menu= Arrays.asList(Steak, Chicken, Salad, Pizza);
   //public List<String> soldOutFoods;
   public List<FoodClass> soldOutFoods;
   
   private CookAgent cook;
   private HostAgent host;
   private CashierAgent cashier;
   
   private boolean givingFood=false;
   private boolean customerOrderIsSoldOut=false;
   private boolean alreadyPresent=false;
   
   private boolean breakAvailable=false;
   private boolean wantToGoOnBreak=false;
   
   public boolean working=false;

//   public void setPaused(){
//      pauseToggle();
//      stateChanged();
//   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#setCook(ericliu.restaurant.CookAgent)
    */
   @Override
   public void setCook(CookAgent cook){
      this.cook=cook;
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#setHost(ericliu.restaurant.HostAgent)
    */
   @Override
   public void setHost(HostAgent host){
      this.host=host;
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#setCashier(ericliu.restaurant.CashierAgent)
    */
   @Override
   public void setCashier(CashierAgent cashier){
      this.cashier=cashier;
   }
   
   //note that tables is typed with Collection semantics.
   //Later we will see how it is implemented
   public static class MyCustomer{
      public CustomerAgent C;
      public int tableNumber;
      //String choice;
      public FoodClass customerChoice;
      public CustomerState state;
      boolean ordered=false;
      ReceiptClass receipt;
      
      public MyCustomer(CustomerAgent Cust,int TableNumber, CustomerState State){
         C=Cust;
         tableNumber=TableNumber;
         state=State;
        //customerChoice=null;
      }
      
      CustomerAgent getCustomer(){
         return C;
      }
      
      CustomerState getState(){
         return state;
      }
      
//      FoodClass Steak= new FoodClass("Steak", 0, 15.99);
//      FoodClass Chicken= new FoodClass("Chicken", 0, 10.99);
//      FoodClass Salad=new FoodClass("Salad", 0, 5.99);
//      FoodClass Pizza=new FoodClass("Pizza", 0, 8.99);
      
   }
   public enum AgentEvent 
   {none, goToStart, goToReady, startedWork, resumedWork, mustCheckOrder, wantToGoOnBreak};
   AgentEvent event = AgentEvent.none;
   
   public enum CustomerState{none, waiting, seatingCustomer, seated, readyToOrder, askingForOrder, ordering, checkingOrder, needNewOrder, ordered, noMoreFood, gaveOrderToCook, receivedFood, gaveFoodToCustomer, receivedReceipt, gaveReceiptToCustomer, doneEating};
   private boolean atStart;
   private int currentTableNumber;
   private String name;
   private Semaphore atTable = new Semaphore(0,true);
   private Semaphore atStartSemaphore= new Semaphore(0,true);
   private Semaphore atCashier=new Semaphore(0,true);
   private Semaphore atReady=new Semaphore(0,true);
   private Semaphore atOrder=new Semaphore(0,true);
   private Semaphore atPickUp=new Semaphore(0,true);
   
   private int startNumber;
   
   public WaiterGui waiterGui = null;

   public WaiterAgent(Person person, List<FoodClass> soldOutFoods) {
      super();
      
      this.soldOutFoods=soldOutFoods;
      this.person=person;
      this.name = person.getName();
      hoursWorked=0;
      // make some tables
      tables = new ArrayList<Table>(NTABLES);
      for (int ix = 1; ix <= NTABLES; ix++) {
         tables.add(new Table(ix));//how you add to a collections
      }
      
      
   }
   
//   public WaiterAgent(String name, List<FoodClass> soldOutFoods) {
//      super();
//      
//      this.soldOutFoods=soldOutFoods;
//     
//      this.name = name;
//      hoursWorked=0;
//      // make some tables
//      tables = new ArrayList<Table>(NTABLES);
//      for (int ix = 1; ix <= NTABLES; ix++) {
//         tables.add(new Table(ix));//how you add to a collections
//      }
//      
//      
//   }

   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#getTableNumber()
    */
   @Override
   public int getTableNumber(){
      return currentTableNumber;
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#getMaitreDName()
    */
   @Override
   public String getMaitreDName() {
      return name;
   }

   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#getName()
    */
   @Override
   public String getName() {
      return name;
   }

   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#getCustomers()
    */
   
   public List<MyCustomer> getCustomers(){
      return customers;
   }
   

   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#getTables()
    */
   @Override
   public Collection getTables() {
      return tables;
   }
  
   // Messages

//   public void msgPaused(){
//      pauseToggle();
//   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgStartWorking()
    */
   @Override
   public void msgStartWorking(){
      working=true;
      wantToGoOnBreak=false;
      event=AgentEvent.startedWork;
      //host.msgWaiterStartedWork(this);
      stateChanged();
      
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgResumeWorking()
    */
   @Override
   public void msgResumeWorking(){
      working=true;
      wantToGoOnBreak=false;
      event=AgentEvent.resumedWork;
      //host.msgWaiterStartedWork(this);
      stateChanged();
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgYouCanGoOnBreak()
    */
   @Override
   public void msgYouCanGoOnBreak(){
      breakAvailable=true;
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgWantToGoOnBreak()
    */
   @Override
   public void msgWantToGoOnBreak(){
      wantToGoOnBreak=true;
      event=AgentEvent.wantToGoOnBreak;
      stateChanged();
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgSeatCustomer(ericliu.restaurant.CustomerAgent, int)
    */
   @Override
   public void msgSeatCustomer(CustomerAgent C, int tableNumber){
      if(name.equals("wantBreak")){
         event=AgentEvent.wantToGoOnBreak;
      }
     customers.add(new MyCustomer(C,tableNumber, CustomerState.waiting));
     stateChanged();
   }
   
  
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgReadyToOrder(ericliu.restaurant.CustomerAgent)
    */
   @Override
   public void msgReadyToOrder(CustomerAgent cust) {
      MyCustomer customer = null;
      synchronized(customers){
         for (MyCustomer c : customers) {
            if (c.getCustomer() == cust) {
               customer=c;
            }
         }
      }
      customer.state=CustomerState.readyToOrder;   
      stateChanged();
   }

   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgCustomerOrder(ericliu.restaurant.CustomerAgent, ericliu.restaurant.FoodClass)
    */
   @Override
   public void msgCustomerOrder(CustomerAgent cust, FoodClass Choice) {
      MyCustomer customer = null;
      synchronized(customers){
         for (MyCustomer c : customers) {
            if (c.getCustomer() == cust) {
               customer=c;
            }
         }
      }
      customer.customerChoice=Choice;
      customer.state=CustomerState.ordered;  
      customer.ordered=true;
      stateChanged();
      log.add(new LoggedEvent("Received msgCustomerOrder."));

   }

   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgOrderSoldOut(int, java.util.List)
    */
   @Override
   public void msgOrderSoldOut(int tableNumber, List<FoodClass> soldOutFoods){
      MyCustomer customer = null;
      synchronized(customers){
         for (MyCustomer c : customers) {
            if (c.C.getTableNumber() == tableNumber) {
               //System.out.println("Customer is:"+c.C);
               customer=c;
            }
         }
      }
      this.soldOutFoods=soldOutFoods;
      customer.state=CustomerState.needNewOrder;
      stateChanged();
   }

   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgHereIsReplacedOrder(ericliu.restaurant.CustomerAgent, ericliu.restaurant.FoodClass)
    */
   @Override
   public void msgHereIsReplacedOrder(CustomerAgent cust, FoodClass newFoodOrder){
      MyCustomer customer = null;
      synchronized(customers){
         for (MyCustomer c : customers) {
            if (c.getCustomer() == cust) {
               customer=c;
            }
         }
      }
      customer.customerChoice=newFoodOrder;
      //customer.state=CustomerState.ordered;   
      //event=AgentEvent.none;
      /*if(customer.choice.equals("No More Food")){
         customer.state=CustomerState.noMoreFood;
      }
      else{*/
         customer.state=CustomerState.ordered;   
         stateChanged();
      //}
   }
   
//   public void msgGotOrder(){
//      try {
//         atOrder.acquire();
//      } catch (InterruptedException e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      }
//      waiterGui.DoLeaveCustomer();
//   }
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgFoodIsReady(java.lang.String, int)
    */
   @Override
   public void msgFoodIsReady(String choice, int tableNumber){
      MyCustomer customer = null;
      synchronized(customers){
         for (MyCustomer c : customers) {
            if (c.C.getTableNumber() == tableNumber) {
               //System.out.println("Customer is:"+c.C);
               customer=c;
            }
         }
      }
      //customer.C.msgHereIsYourOrder(choice);
      customer.state=CustomerState.receivedFood;
      stateChanged();
      log.add(new LoggedEvent("Received cooked food from cook."));

   } 
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgHereIsTheReceipt(ericliu.restaurant.ReceiptClass)
    */
   @Override
   public void msgHereIsTheReceipt(ReceiptClass receipt){
      MyCustomer customer = null;
      synchronized(customers){
         for (MyCustomer c : customers) {
            if (c.getCustomer() == receipt.getCustomer()) {
               customer=c;
            }
         }
      }
      customer.receipt=receipt;
      customer.state=CustomerState.receivedReceipt;
      stateChanged();
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgDoneEating(ericliu.restaurant.CustomerAgent)
    */
   @Override
   public void msgDoneEating(CustomerAgent cust){
      MyCustomer customer = null;
      synchronized(customers){
         for (MyCustomer c : customers) {
            if (c.getCustomer() == cust) {
               customer=c;
            }
         }
      }
      customer.state=CustomerState.doneEating;
      stateChanged();
      
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgDoGoToReadySpot()
    */
   @Override
   public void msgDoGoToReadySpot(){
      event=AgentEvent.goToReady;
      stateChanged();
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgGoToStart(int)
    */
   @Override
   public void msgGoToStart(int startNumber){
      this.startNumber=startNumber;
      event=AgentEvent.goToStart;
      stateChanged();
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgAtTable()
    */
   @Override
   public void msgAtTable() {//from animation
      //print("msgAtTable() called");
      atTable.release();// = true;
      //stateChanged();
      atStart=false;
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgAtStart()
    */
   @Override
   public void msgAtStart(){
      atStartSemaphore.release();
      //stateChanged();
      atStart=true;
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgAtCashier()
    */
   @Override
   public void msgAtCashier(){
      atCashier.release();
      atStart=false;
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgAtReady()
    */
   @Override
   public void msgAtReady(){
      atReady.release();
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgAtOrder()
    */
   @Override
   public void msgAtOrder(){
      atOrder.release();
   }
   
   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#msgAtPickUp()
    */
   @Override
   public void msgAtPickUp(){
      atPickUp.release();
   }
   /**
    * Scheduler.  Determine what action is called for, and do it.
    */
   public boolean pickAndExecuteAnAction() {
      /* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
       */
      if(working){
          if(event==AgentEvent.startedWork)
          {
             tellHostWorking();
             return true;
          }
          if(event==AgentEvent.goToStart){
             DoGoToStart();
             return true;
          }
          if(event==AgentEvent.goToReady){
             DoGoToReady();
             return true;
          }
//          if(event==AgentEvent.resumedWork)
//          {
//             tellHostResumedWorking();
//             return true;
//          }
          if(event==AgentEvent.wantToGoOnBreak){
             tellHostBreakTime();
             return true;
          }
          if(!customers.isEmpty()){
            // synchronized(customers){
                 for (MyCustomer customer : customers) {
                  if (customer.state==CustomerState.waiting) {              
                     AlertLog.getInstance().logMessage(AlertTag.EricWaiter, this.name, "Waiter is trying to seat customer");

                     seatCustomer(customer,customer.tableNumber);
                     return true;
                  }
                 }
            // }
            // synchronized(customers){
                 for (MyCustomer customer : customers) {
                  if(customer.state==CustomerState.readyToOrder){
                        AlertLog.getInstance().logMessage(AlertTag.EricWaiter, this.name, "Waiter is taking Customer's Order.");
                        takeOrder(customer,customer.tableNumber);
                        return true;
                     
                  }
                 }
            // }
   
            // synchronized(customers){
                 for (MyCustomer customer : customers) {
                    if(customer.state==CustomerState.noMoreFood){
                       tellCustomerToLeave(customer);
                       return true;
                    }
                   }
             //}
             
             //synchronized(customers){
                 for (MyCustomer customer : customers) {
                  if(customer.state==CustomerState.ordered){
                     giveOrder(customer, customer.customerChoice, customer.tableNumber);
                     return true;
                  }
                 }
           //  }
             
           //  synchronized(customers){
                 for (MyCustomer customer : customers) {
                    if(customer.state==CustomerState.needNewOrder){
                       takeNewOrder(customer,customer.tableNumber);
                       return true;
                    }
                 }
            // }
             
         //    synchronized(customers){
                 for (MyCustomer customer : customers) {
                  if(customer.state==CustomerState.receivedFood){
                     giveFood(customer);
                     return true;
                  }
                }
        //     }
              
          //   synchronized(customers){
                 for (MyCustomer customer : customers) {
                    if(customer.state==CustomerState.receivedReceipt){
                       giveReceipt(customer);
                       return true;
                    }
                  }
          //   }
              
          //   synchronized(customers){
                 for (MyCustomer customer : customers) {
                  if(customer.state==CustomerState.doneEating){
                     cleanTable(customer);
                     return true;
                  }
                 }
          //   }
           
          }
          //return false;
       //}
      }
      //Do("\n\nCUSTOMERS SIZE: "+customers.size()+"\n\n");
      //Do("pickUp permits" + atPickUp.availablePermits());
      return false;
      //we have tried all our rules and found
      //nothing to do. So return false to main loop of abstract agent
      //and wait.
   }

   // Actions

   private void tellHostWorking(){
      event=AgentEvent.none;
     // stateChanged();
      
      host.msgWaiterStartedWork(this);
      AlertLog.getInstance().logMessage(AlertTag.EricWaiter, this.name, "TOLD HOST IM WORKING!!!");

     // stateChanged();
      //cook.msgWhatFoodsAreSoldOut();
   }
//   private void tellHostResumedWorking(){
//      event=AgentEvent.none;
//     // stateChanged();
//      host.msgWaiterResumedWork(this);
//      Do("\n\nTELL HOST RESUMED WORKING\n\n");
//     // stateChanged();
//      //cook.msgWhatFoodsAreSoldOut();
//   }
   
   private void tellHostBreakTime(){
      event=AgentEvent.none;
      host.msgWaiterNeedsBreak(this);
   }
   private void seatCustomer(MyCustomer customer, int tableNumber) {
      try {
         atStartSemaphore.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      waiterGui.DoGoToReady();
      try {
         atReady.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      //host.msgTookCustomer();
      customer.C.msgFollowMe(tableNumber, menu);
      DoSeatCustomer(customer,tableNumber);
      
      try {
         atTable.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      Do("Seating Customer.");
      customer.state=CustomerState.seated;
     waiterGui.DoLeaveCustomer();
   }
   

   
   private void takeOrder(MyCustomer customer, int tableNumber){
     
      DoGoToTable(customer.C, tableNumber, customer.customerChoice);
      AlertLog.getInstance().logMessage(AlertTag.EricWaiter, this.name, "Taking Order.");

      customer.C.msgWhatDoYouWant();
      customer.state=CustomerState.ordering; 
      try {
         atTable.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      if(customer.ordered==true)
         customer.C.msgTookOrder(customer.customerChoice);
      //waiterGui.DoLeaveCustomer();
     // waiterGui.DoGoToOrder();

   }
   
   private void takeNewOrder(MyCustomer customer, int tableNumber){
      
      DoGoToTable(customer.C, tableNumber, customer.customerChoice);
      AlertLog.getInstance().logMessage(AlertTag.EricWaiter, this.name, "Taking NEW Order.");

      customer.C.msgReDoOrder(soldOutFoods);
     // host.msgTheseFoodsAreSoldOut(soldOutFoods);
      //Do("Send the customer menu of sold out foods");
      customer.state=CustomerState.ordering; 
      try {
         atTable.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      customer.C.msgTookOrder(customer.customerChoice);
     // waiterGui.DoLeaveCustomer();
     // waiterGui.DoGoToOrder();

   }
   
   private void tellCustomerToLeave(MyCustomer customer){
      DoGoToTable(customer.C, customer.tableNumber, customer.customerChoice);
      customer.C.msgYouMustLeave();
      waiterGui.DoLeaveCustomer();
      customer.state=CustomerState.doneEating;
      host.msgCleanedTable(this, customer.C);

   }
   private void giveOrder(MyCustomer customer, FoodClass choice, int tableNumber){
      waiterGui.drawCustomerOrder(choice.choice+"?");
      waiterGui.DoGoToOrder();
      AlertLog.getInstance().logMessage(AlertTag.EricWaiter, this.name, "Giving Order to Cook");

      try {
         atOrder.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      waiterGui.DoLeaveCustomer();
      cook.msgHereIsTheOrder(this, choice, tableNumber);
      customer.state=CustomerState.gaveOrderToCook;
      
      log.add(new LoggedEvent("Gave order to cook."));

      //Do("\n\nGAVE COOK ORDER \n\n");
     // stateChanged();
   }
   
   private void giveFood(MyCustomer customer){
      waiterGui.DoGoToPickUp();
      try {
         atPickUp.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      //cook.msgWaiterReceivedFood(customer.customerChoice.choice);
      givingFood=true;
      DoGoToTable(customer.C, customer.tableNumber, customer.customerChoice);
      //System.out.println("Sold out foods: "+ soldOutFoods.size());
      AlertLog.getInstance().logMessage(AlertTag.EricWaiter, this.name, "Giving food to customer.");

      customer.C.msgHereIsYourOrder();
      customer.state=CustomerState.gaveFoodToCustomer;
      try {
         atTable.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      waiterGui.undrawCustomerOrder();
      customer.C.msgReceivedOrder(customer.customerChoice);
      AskCashierForCheck(customer);
      try {
         atCashier.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      waiterGui.DoGoToTable(customer.C, customer.tableNumber);
      try {
         atTable.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      waiterGui.DoLeaveCustomer();
      givingFood=false;
   }
   
   private void AskCashierForCheck(MyCustomer customer){
      waiterGui.DoGoToCashier();
      AlertLog.getInstance().logMessage(AlertTag.EricWaiter, this.name, "Asking Cashier for Check.");

      cashier.msgCalculateCheck( this, customer.C, customer.customerChoice);
   }
   private void giveReceipt(MyCustomer customer){
      AlertLog.getInstance().logMessage(AlertTag.EricWaiter, this.name, "Giving receipt to customer.");

      customer.C.msgHereIsYourReceipt(customer.receipt);
      customer.state=CustomerState.gaveReceiptToCustomer;

      
   }
   
   private void cleanTable(MyCustomer customer){
      AlertLog.getInstance().logMessage(AlertTag.EricWaiter, this.name, "Clearing the table because Customer left.");

      //DoGoToTable(customer.C,customer.tableNumber,customer.choice);
//      customer.state=CustomerState.none;
      customers.remove(customer);
      //waiterGui.DoLeaveCustomer();
      if(breakAvailable==true && wantToGoOnBreak==true){
         AlertLog.getInstance().logMessage(AlertTag.EricWaiter, this.name, "\n\n\n I'M GOING ON BREAK \n\n\n");

         host.msgImOnBreak(this);
         working=false;
         waiterGui.leaveRestaurant();
         waiterGui.setWorking(false);
      }
      host.msgCleanedTable(this, customer.C);
      waiterGui.DoLeaveCustomer();
   }
   
   private void goOnBreak(){
      AlertLog.getInstance().logMessage(AlertTag.EricWaiter, this.name, "\n\n\n I'M GOING ON BREAK \n\n\n");
      host.msgImOnBreak(this);
      working=false;
      waiterGui.leaveRestaurant();
      waiterGui.setWorking(working);
   }
   // The animation DoXYZ() routines
   private void DoGoToStart(){
      waiterGui.setStart(startNumber);
      waiterGui.DoGoToStart(startNumber);
      event=AgentEvent.none;
   }
   
   private void DoGoToReady(){
      waiterGui.DoGoToReady();
      event=AgentEvent.none;
   }
   private void DoSeatCustomer(MyCustomer customer, int tableNumber) {
      //Notice how we print "customer" directly. It's toString method will do it.
      //Same with "table"
      
      print("Seating " + customer + " at table number: " + tableNumber);
      waiterGui.DoGoToTable(customer.C, tableNumber); 
      //waiterGui.DoLeaveCustomer();
      
   }
   
   private void DoGoToTable(CustomerAgent C, int tableNumber, FoodClass customerChoice){
      
//      try {
//         atStartSemaphore.acquire();
//      } catch (InterruptedException e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      }
     

      if(givingFood)
         waiterGui.drawCustomerOrder(customerChoice.choice);
      waiterGui.DoGoToTable(C, tableNumber);
      //Do("/n ERROR HERE /n");

      //waiterGui.updatePosition();
   }
   private void DoLeaveTable() {
      
      print("Leaving Customer's Table");
      waiterGui.DoLeaveCustomer(); 

   }

   //utilities

   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#setGui(ericliu.gui.WaiterGui)
    */
   @Override
   public void setGui(WaiterGui gui) {
      waiterGui = gui;
   }

   /* (non-Javadoc)
    * @see ericliu.restaurant.Waiter_#getGui()
    */
   @Override
   public WaiterGui getGui() {
      return waiterGui;
   }

   private class Table {
      CustomerAgent occupiedBy;
      int tableNumber;

      
      Table(int tableNumber) {
         this.tableNumber = tableNumber;
         
      }

      void setOccupant(CustomerAgent cust) {
         occupiedBy = cust;
      }

      void setUnoccupied() {
         occupiedBy = null;
      }

      CustomerAgent getOccupant() {
         return occupiedBy;
      }

      boolean isOccupied() {
         return occupiedBy != null;
      }

      public String toString() {
         return "table " + tableNumber;
      }
   }
   
   public int getNumCustomers(){
      return customers.size();
   }
}
