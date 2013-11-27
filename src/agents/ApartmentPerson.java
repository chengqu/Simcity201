package agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import ApartmentGui.ApartmentPersonGui;
import Buildings.ApartmentComplex;
import Buildings.ApartmentComplex.*;
import agent.Agent;

public class ApartmentPerson extends Agent implements ApartPerson{
   
   /**
    * Data
    */
   
   public Person p;
   
   Timer cookTimer = new Timer();
   Timer eatTimer = new Timer();
   Timer fridgeTimer = new Timer();
   Timer t = new Timer();
   
   boolean evicted = false;
   ApartmentComplex apartmentComplex;
   public Apartment apartment;
   
   ApartmentPersonGui gui;
   public boolean justStarted;
   
   Object renterLock = new Object();
   boolean timeToBill = false;
   
   private boolean sleeping = false;
   
   private Semaphore atFridge=new Semaphore(0,true);
   private Semaphore atStove=new Semaphore(0,true);
   private Semaphore atTable=new Semaphore(0,true);
   private Semaphore atLivingRoom=new Semaphore(0,true);
   private Semaphore atBed=new Semaphore(0,true);
   Random rand = new Random();

   //constructor
   public ApartmentPerson(Person agent, ApartmentComplex complex, Apartment a)
   {
      p = agent;
      apartmentComplex = complex;
      apartment = a;
      //groceries.add("Steak");
      this.state=ApartmentPersonState.none;
      justStarted = true;
   }
   
   public ApartmentPerson(Person agent, ApartmentComplex complex, Apartment a, boolean Test)
   {
	   justStarted = true;
	   p = agent;
	   apartmentComplex = complex;
	   apartment = a;
	   this.state = ApartmentPersonState.none;
	   if(Test)
	   {
		   atFridge = new Semaphore(10, true);
		   atStove = new Semaphore(10, true);
		   atTable = new Semaphore(10, true);
		   atLivingRoom = new Semaphore(10, true);
		   atBed = new Semaphore(10, true);
	   }
   }
   
   private enum ApartmentPersonState {none,hasGroceries, hungry, sleeping, busy};
   private ApartmentPersonState state;
   
   public void setGui(ApartmentPersonGui g)
   {
      gui = g;
   }
   
   public void setApartment(Apartment a)
   {
      apartment = a;
   }
   
   public void doThings()
   {
      if(gui != null)
      {
         gui.personArrived();
      }
      stateChanged();
   }
   
   /**
    * Messages
    */
   
   public void msgPleasePayBill(ApartmentBill b)
   {
      //will be changed to p.bill.add(b);
      p.bills.add(b);
   }
   
   public void msgEvicted()
   {
      evicted = true;
   }
   
   /**
    * Messages specific to the owner
    */
   
   public void msgCantPay(ApartmentBill b, ApartPerson a)
   {
      synchronized(renterLock)
      {
         for(Apartment r: apartmentComplex.apartments)
         {
            if(r.person.equals(a))
            {
               r.strikes++;
            }
         }
      }
      stateChanged();
   }
   
   public void msgDoneSleeping()
   {
	   this.sleeping = false;
	   this.stateChanged();
   }
   
   /**
    * Messages from GUI for semaphore releases
    */
   public void msgAtFridge(){
      atFridge.release();
   }
   
   public void msgAtStove(){
      atStove.release();
   }
   
   public void msgAtTable(){
      atTable.release();
   }
   
   public void msgAtLivingRoom(){
      atLivingRoom.release();
   }
   
   public void msgAtBed(){
      atBed.release();
   }
   
   /*
    * TODO: add an action that removes bills, dont put it in 
    * a message. its okay for now, but its better in an
    * actions since it wont look so crazy
    */
   public void msgHereIsMoney(ApartmentBill b, float money, ApartPerson a)
   {
      synchronized(renterLock)
      {
         for(Apartment r: apartmentComplex.apartments)
         {
            if(r.person.equals(a))
            {
               synchronized(p.billLock)
               {
                  for(ApartmentBill bill: r.bills)
                  {
                     if(b == bill && b.getBalance() == money)
                     {
                        r.bills.remove(bill);
                        return;
                     }
                  }
               }
            }
         }
      }
   }
   
   /**
    * Scheduler
    */
   
   public boolean pickAndExecuteAnAction() {
	   if(justStarted == true)
	   {
		   justStarted = false;
		   return false;
	   }
		   
      if(evicted)
      {
         doClearApartment();
         doLeave();
         return false;
      }
      for(Task.specificTask s : p.currentTask.sTasks)
      {
    	  if(s.equals(Task.specificTask.depositGroceries))
    	  {
    		  doStoreGroceries();
    		  p.currentTask.sTasks.remove(Task.specificTask.depositGroceries);
    		  return true;
    	  }
      }
      for(Task.specificTask s : p.currentTask.sTasks)
      {
    	  if(s.equals(Task.specificTask.eatAtApartment))
    	  {
    		  doCookAndEatFood();
    		  p.currentTask.sTasks.remove(Task.specificTask.eatAtApartment);
    		  return true;
    	  }
      }
      for(Task.specificTask s : p.currentTask.sTasks)
      {
    	  if(s.equals(Task.specificTask.payBills))
    	  {
    		  doPayBills();
    		  p.currentTask.sTasks.remove(Task.specificTask.payBills);
    		  return true;
    	  }
      }
      for(Task.specificTask s : p.currentTask.sTasks)
      {
    	  if(s.equals(Task.specificTask.sleepAtApartment))
    	  {
    		  doSleep();
    		  p.currentTask.sTasks.remove(Task.specificTask.sleepAtApartment);
    		  return true;
    	  }
      }
      
      if(timeToBill)
      {
         for(Role r: p.roles)
         {
            if(r.getRole() == Role.roles.ApartmentOwner)
            {
            	timeToBill = false;
                doBillPeople();
                return true;
            }
         }
      }
      if(!sleeping)
      {
    	  doLeave();
      }
      //doGoToDefault();
      return false;
   }

   private void doSleep() {
	   final ApartPerson p = this;
	   print("Sleepin at apartment");
	   t.schedule(new TimerTask()
	   {
			public void run() {
				p.msgDoneSleeping();
			}
	   }, 6000);
   }

private void doLeave() {
      gui.personLeft();
      p.msgDone();
   }

   private void doGoToDefault(){
      gui.goToLivingRoom();
   }
   
   private void doPayBills() {
      for(ApartmentBill b: p.bills)
      {
         if(p.money >= b.getBalance())
         {
            p.money -= b.getBalance();
            b.getOwner().msgHereIsMoney(b, b.getBalance(), this);
         }
         else
         {
            b.getOwner().msgCantPay(b, this);
         }
      }
      p.bills.clear();
   }

   private void doCookAndEatFood() {
      //make him move to stove and say what he's cooking.
      //then make him go to table to eat
      //then brings the food to sink
      //then set hunger level to zero
	   /*if(apartment.Fridge.size() <= 0)
	   {
		   return;
	   }
      try {
         atLivingRoom.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      gui.goToFridge();
      try {
         atFridge.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }*/
      //TODO: DECREMENT A RANDOM PIECE OF FOOD FROM THE PERSON'S FRIDGE (for now)
      
      int a = apartment.Fridge.size();
      apartment.Fridge.remove(rand.nextInt(a));
      /*gui.goToStove();
      try {
         atStove.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      cookTimer.schedule(new TimerTask() {
         //Object Order = 1;
         public void run() {
            print("Done cooking");
//            event=OrderEvent.done;

            gui.goToTable();           
         }
      },
      2000);*/
      p.hungerLevel = 0;
     // gui.goToTable();
      /*try {
         atTable.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      eatTimer.schedule(new TimerTask() {
         public void run() {
            print("Done eating");
            gui.goToLivingRoom();
            state=ApartmentPersonState.none;
            stateChanged();
            
            
         }
      },
      3000);*/
      //state=ApartmentPersonState.none;
   }

   private void doStoreGroceries() {
      //make him move to fridge
      gui.goToFridge();
      try {
         atFridge.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      fridgeTimer.schedule(new TimerTask() {
         public void run() {
            print("Done storing groceries");
            gui.goToLivingRoom();
            state=ApartmentPersonState.none;
            stateChanged();
         }
      },
      3000);
      List<Grocery> addGroceries = new ArrayList<Grocery>();
      List<Grocery> removeGroceries = new ArrayList<Grocery>();
      for(Grocery g : p.groceries)
      {
    	  for(Grocery g_ : apartment.Fridge)
    	  {
    		  if(g_.getFood().equalsIgnoreCase(g.getFood()))
    		  {
    			  addGroceries.add(new Grocery(g_.getFood(), g.getAmount() + g_.getAmount()));
    			  removeGroceries.add(g_);
    		  }
    	  }
      }
      for(Grocery g: p.groceries)
      {
    	  apartment.Fridge.add(g);
      }
      p.groceries.clear();
   }
   
   private void doBillPeople()
   {
      for(Apartment r: apartmentComplex.apartments)
      {
         r.person.msgPleasePayBill(new ApartmentBill(10.0f, r.person, this));
      }
   }

   private void doClearApartment() {
      for(Role r: p.roles)
      {
         if(r.getRole() == Role.roles.ApartmentRenter)
         {
            p.roles.remove(r);
            break;
         }
      }
   }
}
