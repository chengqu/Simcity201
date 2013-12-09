package agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simcity201.test.mock.EventLog;
import simcity201.test.mock.LoggedEvent;
import ApartmentGui.ApartmentPersonGui;
import Buildings.ApartmentComplex;
import Buildings.ApartmentComplex.*;
import agent.Agent;

public class ApartmentPerson extends Agent implements ApartPerson{
   
   /**
    * Data
    */
   
	public EventLog log = new EventLog();
	
   public Person p;
   
   public String name;
   
   Timer cookTimer = new Timer();
   Timer eatTimer = new Timer();
   Timer fridgeTimer = new Timer();
   Timer t = new Timer();
   
   public boolean evicted = false;
   ApartmentComplex apartmentComplex;
   public Apartment apartment;
   
   ApartmentPersonGui gui;
   
   Object renterLock = new Object();
   public boolean timeToBill = false;
   
   public boolean firstTime = true;
   
   public boolean sleeping = false;
   
   private Semaphore taskSemaphore;
   
   Random rand = new Random();

   public String getName()
   {
	   return this.name;
   }
   
   //constructor
   public ApartmentPerson(Person agent, ApartmentComplex complex, Apartment a)
   {
      p = agent;
      apartmentComplex = complex;
      apartment = a;
      //groceries.add("Steak");
      this.name = p.getName();
      firstTime = true;
      taskSemaphore = new Semaphore(0, true);
   }
   
   public ApartmentPerson(Person agent, ApartmentComplex complex, Apartment a, boolean Test)
   {
	   p = agent;
	   apartmentComplex = complex;
	   apartment = a;
	   this.name = p.getName();
	   taskSemaphore = new Semaphore(10, true);
	   firstTime = true;
   }
   
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
	   print("lololol");
	   this.log.add(new LoggedEvent("Got a bill"));
	   p.bills.add(b);
   }
   
   public void msgEvicted()
   {
      evicted = true;
   }
   
   public void msgDoneTask()
   {
	   taskSemaphore.release();
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
                     if(b == bill && b.getBalance() <= money)
                     {
                        r.bills.remove(bill);
                        log.add(new LoggedEvent("Received money from: " + a.getName()));
                        return;
                     }
                     else
                     {
                    	 r.strikes++;
                    	 r.bills.remove(bill);
                    	 log.add(new LoggedEvent(a.getName() + " couldn't pay"));
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
	   if(firstTime)
	   {
		   firstTime = false;
		   return false;
	   }
      if(evicted)
      {
         doClearApartment();
         doLeave();
         return false;
      }
      if(p.currentTask != null)
      {
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
      doLeave();
      return false;
   }

   private void doSleep() {
	   sleeping = true;
	   gui.goToBed();
	   log.add(new LoggedEvent("Sleeping"));
	   try {
		   taskSemaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   sleeping = false;
	   log.add(new LoggedEvent("Done Sleeping"));
   }

private void doLeave() {
	gui.goToEntrance();
	try {
		taskSemaphore.acquire();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
      log.add(new LoggedEvent("Payed bills"));
   }

   private void doCookAndEatFood() {
	   if(apartment.Fridge.size() <= 0)
	   {
		   return;
	   }
      gui.goToFridge();
      try {
    	  taskSemaphore.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      int a = apartment.Fridge.size();
      apartment.Fridge.remove(rand.nextInt(a));
      gui.goToStove();
      try {
         taskSemaphore.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      p.hungerLevel = 0;
      gui.goToTable();
      try {
         taskSemaphore.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      log.add(new LoggedEvent("Ate food"));
   }

   private void doStoreGroceries() {
      //make him move to fridge
      gui.goToFridge();
      try {
		taskSemaphore.acquire();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
      log.add(new LoggedEvent("Stored Groceries"));
   }
   
   private void doBillPeople()
   {
      for(Apartment r: apartmentComplex.apartments)
      {
         r.person.msgPleasePayBill(new ApartmentBill(10.0f, r.person, this));
      }
      log.add(new LoggedEvent("Billed People"));
   }

   private void doClearApartment() {
      for(Role r: p.roles)
      {
         if(r.getRole() == Role.roles.ApartmentRenter)
         {
            p.roles.remove(r);
            apartmentComplex.apartments.remove(this.apartment);
            p.apartment = null;
            apartment = null;
            apartmentComplex = null;
            break;
         }
      }
      log.add(new LoggedEvent("Cleared apartment, evicted"));
   }
}
