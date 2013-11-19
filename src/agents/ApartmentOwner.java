package agents;

import java.util.ArrayList;
import java.util.List;

import simcity201.gui.ApartmentOwnerGui;
import Buildings.ApartmentComplex;
import Buildings.ApartmentComplex.Apartment;
import agent.Agent;

public class ApartmentOwner extends Agent{

	/**
	 * Data
	 */
	
	Person p;
	List<String> groceries; //this is going to be a part of the person 
	
	ApartmentOwner ao;
	ApartmentComplex apartmentComplex;
	Apartment apartment;
	ApartmentOwnerGui gui;
	
	List<myApartmentRenter> renters = new ArrayList<myApartmentRenter>();
	
	boolean timeToBill;
	
	Object renterLock = new Object();
	
	//constructor
	public ApartmentOwner(Person person)
	{
		p = person;
		this.stateChanged();
	}
	
	/**
	 * Messages
	 */
	
	public void msgCantPay(Bill b, ApartmentRenter a)
	{
		synchronized(renterLock)
		{
			for(myApartmentRenter r: renters)
			{
				if(r.equals(a))
				{
					r.strikes++;
				}
			}
		}
		stateChanged();
	}
	
	
	/*
	 * TODO: add an action that removes bills, dont put it in 
	 * a message. its okay for now, but its better in an
	 * actions since it wont look so crazy
	 */
	public void msgHereIsMoney(Bill b, float money, ApartmentRenter a)
	{
		synchronized(renterLock)
		{
			for(myApartmentRenter r: renters)
			{
				if(r.equals(a))
				{
					for(Bill bill: r.bills)
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
	
	/**
	 * Scheduler
	 */
	
	protected boolean pickAndExecuteAnAction() {
		if(groceries.size() > 0)
		{
			doStoreGroceries();
			return true;
		}
		if(p.hungerLevel < 20 /* && haveEnoughFood && haveTime*/)
		{
			doCookAndEatFood();
			return true;
		}
		if(timeToBill)
		{
			doBillPeople();
			return true;
		}
		else
		{
			doLeave();
			return false;
		}
	}

	private void doLeave() {
		
	}

	private void doCookAndEatFood() {
	
	}
	
	private void doBillPeople()
	{
		for(myApartmentRenter r: renters)
		{
			r.renter.msgPleasePayBill(new Bill(10.0f, r.renter, this));
		}
	}

	private void doStoreGroceries() {
		
	}
	
	private class myApartmentRenter{
		public ApartmentRenter renter;
		public List<Bill> bills = new ArrayList<Bill>();
		public int strikes = 0;
		public myApartmentRenter(ApartmentRenter ar)
		{
			renter = ar;
		}
	}
}
