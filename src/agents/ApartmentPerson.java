package agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import ApartmentGui.ApartmentPersonGui;
import Buildings.ApartmentComplex;
import Buildings.ApartmentComplex.*;
import agent.Agent;

public class ApartmentPerson extends Agent{
	
	/**
	 * Data
	 */
	
	public Person p;
	
	private boolean busy=false;
	
	Timer cookTimer = new Timer();
	Timer eatTimer = new Timer();
	Timer fridgeTimer = new Timer();
	
	List<String> groceries=new ArrayList<String>(); //this is going to be a part of the person 
	
	boolean evicted = false;
	ApartmentComplex apartmentComplex;
	Apartment apartment;
	
	ApartmentPersonGui gui;
	
	Object renterLock = new Object();
	boolean timeToBill = false;
	
	private Semaphore atFridge=new Semaphore(0,true);
   private Semaphore atStove=new Semaphore(0,true);
   private Semaphore atTable=new Semaphore(0,true);
   private Semaphore atLivingRoom=new Semaphore(0,true);
   private Semaphore atBed=new Semaphore(0,true);

	//constructor
	public ApartmentPerson(Person agent, ApartmentComplex complex, Apartment a)
	{
		p = agent;
		apartmentComplex = complex;
		apartment = a;
		groceries.add("Steak");
		this.state=ApartmentPersonState.none;
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
		stateChanged();
	}
	
	public void msgEvicted()
	{
		evicted = true;
		stateChanged();
	}
	
	/**
	 * Messages specific to the owner
	 */
	
	public void msgCantPay(ApartmentBill b, ApartmentPerson a)
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
	public void msgHereIsMoney(ApartmentBill b, float money, ApartmentPerson a)
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
	
	protected boolean pickAndExecuteAnAction() {
		if(evicted)
		{
			doClearApartment();
			return true;
		}
	   
		if(groceries.size()>0 )
		{
		   //state=ApartmentPersonState.busy;
			doStoreGroceries();
			return true;
		}
		if(p.hungerLevel>20 )
		{
		   //state=ApartmentPersonState.busy;
			doCookAndEatFood();
			return true;
		}
		if(p.bills.size() > 0)
		{
			doPayBills();
			return true;
		}
		if(timeToBill)
		{
			for(Role r: p.roles)
			{
				if(r.getRole() == Role.roles.ApartmentOwner)
				{
					doBillPeople();
					return true;
				}
			}
		}
		//doLeave();
		//doGoToDefault();
		return false;
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
	}

	private void doCookAndEatFood() {
		//make him move to stove and say what he's cooking.
		//then make him go to table to eat
		//then brings the food to sink
		//then set hunger level to zero
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
      }
	   gui.goToStove();
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
      2000);
	   p.hungerLevel = 0;
	  // gui.goToTable();
	   try {
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
      3000);
	   //state=ApartmentPersonState.none;
	}

	private void doStoreGroceries() {
		//make him move to fridge
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
	   groceries.clear();
	   
		
		//state=ApartmentPersonState.none;
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
