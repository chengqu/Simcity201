package ericliu.restaurant;

import agent.Agent;
import ericliu.restaurant.WaiterAgent.CustomerState;
import ericliu.gui.HostGui;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent {
   static final int NTABLES = 3;//a global for the number of tables.
   static final int NWAITINGSEATS=8;
   static final int NWAITERSEATS=4;
   //Notice that we implement waitingCustomers using ArrayList, but type it
   //with List semantics.
   public List<CustomerAgent> waitingCustomers= Collections.synchronizedList(new ArrayList<CustomerAgent>());
   public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
   public List<MyWaiter> waiters  = Collections.synchronizedList(new ArrayList<MyWaiter>());
   public ArrayList<Table> tables;
   public ArrayList<waitingSeat>waitingSeats;
   public ArrayList<waiterSeat>waiterSeats;
//   public List<WaiterAgent> waitersWaitingForBreak=new ArrayList<WaiterAgent>();
//   public List<WaiterAgent> waiters=new ArrayList<WaiterAgent>();
//   public List<WaiterAgent> freeWaiters=new ArrayList<WaiterAgent>();
//   public List<WaiterAgent> breakWaiters=new ArrayList<WaiterAgent>();

   private boolean breakAvailable=false;
   
//   public void setPaused(){
//      /*if(paused==false){
//         paused=true;
//      }
//      else{
//         paused=false;
//      }*/
//      pauseToggle();
//      stateChanged();
//   }
   
   //note that tables is typed with Collection semantics.
   //Later we will see how it is implemented
   private boolean readySpotTaken=false;
   private boolean atStart;
   private int currentTableNumber;
   private String name;
   private List<FoodClass> soldOutFoods;
   private Semaphore atTable = new Semaphore(0,true);

   public HostGui hostGui = null;

   public HostAgent(String name) {
      super();

      this.name = name;
      // make some tables
      tables = new ArrayList<Table>(NTABLES);
      for (int ix = 1; ix <= NTABLES; ix++) {
         tables.add(new Table(ix));//how you add to a collections
      }
      waitingSeats=new ArrayList<waitingSeat>(NWAITINGSEATS);
      for (int ix = 1; ix <= NWAITINGSEATS; ix++) {
         waitingSeats.add(new waitingSeat(ix));//how you add to a collections
      }
      
      waiterSeats=new ArrayList<waiterSeat>(NWAITERSEATS);
      for (int ix = 1; ix <= NWAITERSEATS; ix++) {
         waiterSeats.add(new waiterSeat(ix));//how you add to a collections
      }
      
      
   }
   
   private class MyCustomer{
      CustomerAgent C;
      int tableNumber;
      int seatNumber;
      CustomerState state;

      MyCustomer(CustomerAgent Cust, CustomerState State){
         C=Cust;
         state=State;
        //customerChoice=null;
      }
      
      CustomerAgent getCustomer(){
         return C;
      }
      
      CustomerState getState(){
         return state;
      }
      
   }
   
   private class MyWaiter{
      WaiterAgent W;
      int startNumber;
      WaiterState state;
      boolean needBreak=false;
      MyWaiter(WaiterAgent waiter, WaiterState State){
         W=waiter;
         state=State;
        //customerChoice=null;
      }
      
      WaiterAgent getWaiter(){
         return W;
      }
      
      WaiterState getState(){
         return state;
      }
      
   }
   private enum WaiterState{none, startedWork, free, busy, onBreak};
   private enum CustomerState{none, waiting, seatedWaiting};
   private enum WaiterEvent{none, waitingForBreak}
   WaiterEvent event= WaiterEvent.none;
   
   public int getTableNumber(){
      return currentTableNumber;
   }
   
   public String getMaitreDName() {
      return name;
   }

   public String getName() {
      return name;
   }

   public List getWaitingCustomers() {
      return waitingCustomers;
   }

   public Collection getTables() {
      return tables;
   }
   // Messages

//   public void msgPaused(){
//      pauseToggle();
//   }
   
   public void msgImNotWaiting(CustomerAgent customer){
     if(!waitingCustomers.isEmpty()){
        waitingCustomers.remove(customer);
     }
   }
   public void msgWaiterStartedWork(WaiterAgent waiter){

      MyWaiter wait=null;
      synchronized(waiters){
         for(MyWaiter w:waiters){
            if(w.getWaiter()==waiter){
               wait=w;
            }
         }
      }
      if(wait!=null){ 
         wait.state=WaiterState.startedWork;
      }
      else{
         waiters.add(new MyWaiter(waiter, WaiterState.startedWork));
      }
      stateChanged();
   }
   

   
   public void msgWaiterNeedsBreak(WaiterAgent waiter){
      MyWaiter wait=null;
      synchronized(waiters){
         for(MyWaiter w:waiters){
            if(w.getWaiter()==waiter){
               wait=w;
            }
         }
      }
      wait.needBreak=true;
      //event=WaiterEvent.waitingForBreak;
      if(waiters.size()>1){
//         waitersWaitingForBreak.add(waiter);
         breakAvailable=true;
         Do("You can go on break. There is someone else working, but finish serving your customers first.");
         stateChanged();
      }
      else{
         Do("You can't go on break. You're the only waiter working.");
         breakAvailable=false;
      }
   }
   public void msgTookCustomer(){
      readySpotTaken=false;
      stateChanged();
   }
   public void msgImOnBreak(WaiterAgent waiter){
     // freeWaiters.remove(waiter);
//      breakWaiters.add(waiter);
        MyWaiter wait=null;
        synchronized(waiters){
           for(MyWaiter w:waiters){
              if(w.getWaiter()==waiter){
                 wait=w;
              }
           }
        }
        wait.state=WaiterState.onBreak;
        wait.needBreak=false;
        stateChanged();
   }
   
   public void msgIWantFood(CustomerAgent cust) {
      //customers.add(cust);
      //waitingCustomers.add(cust);
      customers.add(new MyCustomer(cust, CustomerState.waiting));
      stateChanged();
      //System.out.println("Added customer to waiting customer list");
   }

   public void msgTheseFoodsAreSoldOut(List<FoodClass> soldOutFoods){
      this.soldOutFoods=soldOutFoods;
   }
   public void msgCleanedTable(WaiterAgent waiter, CustomerAgent cust) {
      for (Table table : tables) {
         if (table.getOccupant() == cust) {
            print(cust + " leaving " + table);
            table.setUnoccupied();
            stateChanged();
         }
      }
      
//      if(!breakWaiters.contains(waiter))
//         freeWaiters.add(waiter);
      MyWaiter wait=null;
      synchronized(waiters){
         for(MyWaiter w:waiters){
            if(w.getWaiter()==waiter){
               wait=w;
            }
         }
      }
      wait.state=WaiterState.free;
      customers.remove(cust);
      stateChanged();
   }
   
   public void msgTableEmpty(){
      
   }
   public void msgAtTable() {//from animation
      //print("msgAtTable() called");
      atTable.release();// = true;
      stateChanged();
      atStart=false;
   }

   public void msgAtStart(){
      atStart=true;
   }
   /**
    * Scheduler.  Determine what action is called for, and do it.
    */
   protected boolean pickAndExecuteAnAction() {
      /* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
       */
//      if(!paused){
         //isBreakAvailable();
//       if(breakAvailable==true){
//          System.out.println("\n Break is available \n");
//          if(!waitersWaitingForBreak.isEmpty())
//             //System.out.println("\n Waiters waiting for break list is not empty \n");
//             giveWaiterBreak(waitersWaitingForBreak.get(0));
//       } 
         //else{
         synchronized(waiters){
            for(MyWaiter waiter:waiters){
               if(waiter.state==WaiterState.startedWork){
                  for(waiterSeat seat:waiterSeats){
                    if(!seat.isOccupied()){
                        tellWaiterToGoToStart(waiter, seat, seat.startNumber);
                        return true;
                    }
                  }
               }
            }
         }
         synchronized(customers){
            for(MyCustomer customer: customers){
               if(customer.state==CustomerState.waiting){
                     for(waitingSeat seat:waitingSeats){
                        if(!seat.isOccupied()){
                           tellCustomerToSitInWaitingArea(customer, seat, seat.seatNumber);
                           return true;
                        } 
                     }
                  }
               }
         }
         synchronized(waiters){
            for(MyWaiter waiter : waiters){
               if(waiter.needBreak==true){
                  if(breakAvailable==true){
                     giveWaiterBreak(waiter);
                  }
               }
            }
         }
//         if(!readySpotTaken){
         checkFreeWaiters();
         //checkIfWaitersNeedABreak();
         synchronized(waiters){
            for(MyWaiter waiter:waiters){
               if(waiter.state==WaiterState.free){   
                  for (Table table : tables) {
                     if (!table.isOccupied()) {
   //                     checkIfWaitersNeedABreak();
   //                     if(!freeWaiters.isEmpty()){
                        synchronized(customers){
                           for(MyCustomer customer:customers){
                              if(customer.state==CustomerState.seatedWaiting || customer.state==CustomerState.waiting){   
      //                        if (!waitingCustomers.isEmpty()) {
                                 //System.out.println("Trying to assign customer to waiter");
      //                           assignCustomerToWaiter(waitingCustomers.get(0), table, table.tableNumber, freeWaiters.get(0));//the action
                                 //displayFreeWaiters();                           
                                 assignCustomerToWaiter(customer, table, table.tableNumber, waiter);//the action
                                 return true;//return true to the abstract agent to reinvoke the scheduler.
                                   
      //                        }
                              }
        
                           }
                        }
                     }
               //  }
               }
               }
            }
//         }
//         }
      }
      //System.out.println("\n\n"+waiters.size()+"\n\n");
      
      return false;
      //we have tried all our rules and found
      //nothing to do. So return false to main loop of abstract agent
      //and wait. 
   }

   // Actions

   private void tellCustomerRestaurantIsFull(CustomerAgent customer){
      customer.msgRestaurantIsFull();
   }
   
   private void tellWaiterToGoToStart(MyWaiter waiter, waiterSeat waiterStart, int startNumber){
      waiter.W.msgGoToStart(startNumber);
      waiterStart.setOccupant(waiter.W);
      waiter.state=WaiterState.free;
      
   }
   private void tellCustomerToSitInWaitingArea(MyCustomer customer, waitingSeat seat, int seatNumber){
      customer.C.msgWaitHere(seatNumber);
      seat.setOccupant(customer.C);
      customer.state=CustomerState.seatedWaiting;
   }

   private void checkFreeWaiters(){
      synchronized(waiters){
         for(MyWaiter waiter:waiters){
            if(waiter.W.customers.size()>=1){
               waiter.state=WaiterState.busy;
            }
         }
      }
   }
   
   private void assignCustomerToWaiter(MyCustomer customer, Table table, int tableNumber, MyWaiter waiter) {
     // Do("\n\nRUNNING ASSIGNCUSTOMERTOWAITER\n\n");
//      if(waiter.W.getCustomers().size()>=1){
//         waiter.state=WaiterState.busy;
//         System.out.println("Removed "+waiter.W.getName()+" from Free Waiter List");
//      }
      customer.C.msgDoGoToReadySpot();
      for (waitingSeat seat : waitingSeats) {
         if (seat.getOccupant() == customer.C) {
            print(customer + " leaving " + seat);
            seat.setUnoccupied();
            stateChanged();
         }
      }
      Do("Assigning Customer to Waiter.");
      customer.C.setWaiter(waiter.W);
     
      waiter.W.msgSeatCustomer(customer.C,tableNumber);
      table.setOccupant(customer.C);
//      if(freeWaiters.get(0).getCustomers().size()>=1){
//         //busyWaiters.add(freeWaiters.get(0));
//         freeWaiters.remove(w);
//         System.out.println("Removed "+w.getName()+" from Free Waiter List");
//      }
      
     /*else{
         
         freeWaiters.add(freeWaiters.get(0));
      }*/
//      waitingCustomers.remove(customer);
      customer.state=CustomerState.none;
    }
   
      
    private void giveWaiterBreak(MyWaiter waiter){
       Do("Giving "+waiter.W.getName()+" a break");
//       freeWaiters.remove(waiter);
//       waitersWaitingForBreak.remove(waiter);
       waiter.state=WaiterState.onBreak;
       waiter.W.msgYouCanGoOnBreak();
       for (waiterSeat seat : waiterSeats) {
          if (seat.getOccupant() == waiter.W) {
             print(waiter + " leaving " + seat + " to go on break");
             seat.setUnoccupied();
             stateChanged();
          }
       }
//       waiter.state=WaiterState.onBreak;
      // waiters.remove(waiter);
       //freeWaiters.remove(waiter);
    }
    
    private void displayFreeWaiters(){
       System.out.println("\n\n FREE WAITERS: \n");
//       for(WaiterAgent waiter: freeWaiters){
//          System.out.println(waiter.getName()+", ");
//       }
       synchronized(waiters){
          for(MyWaiter waiter:waiters){
             if(waiter.state==WaiterState.free){
                System.out.println(waiter.W.getName()+", ");
             }
          }
       }
    }
    private void checkIfWaitersNeedABreak(){
       if(breakAvailable==true){
          /*if(!waitersWaitingForBreak.isEmpty()){
             giveWaiterBreak(waitersWaitingForBreak.get(0));
          }*/
          synchronized(waiters){
             for(MyWaiter waiter:waiters){
                if(waiter.needBreak==true){
                   giveWaiterBreak(waiter);
                   
                }
             }
          }
       } 
    }
    private void isBreakAvailable(){
       int freeWaiters=0;
       synchronized(waiters){
          for(MyWaiter waiter:waiters){
             if(waiter.state==WaiterState.free){
                freeWaiters+=1;
             }
          }
       }
       if(freeWaiters>1)
          breakAvailable=true;
       else
          breakAvailable=false;
    }
   //}

   // The animation DoXYZ() routines
   
   private void DoGiveCustomerToWaiter() {
     

   }

   //utilities

   public void setGui(HostGui gui) {
      hostGui = gui;
   }

   public HostGui getGui() {
      return hostGui;
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
   
   private class waitingSeat{
      CustomerAgent occupiedBy;
      int seatNumber;
      
      waitingSeat(int seatNumber){
         this.seatNumber=seatNumber;
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
         return "table " + seatNumber;
      }
   }
   
   private class waiterSeat{
      WaiterAgent occupiedBy;
      int startNumber;
      
      waiterSeat(int startNumber){
         this.startNumber=startNumber;
      }
      void setOccupant(WaiterAgent waiter) {
         occupiedBy = waiter;
      }

      void setUnoccupied() {
         occupiedBy = null;
      }

      WaiterAgent getOccupant() {
         return occupiedBy;
      }

      boolean isOccupied() {
         return occupiedBy != null;
      }

      public String toString() {
         return "table " + startNumber;
      }
   }
}
