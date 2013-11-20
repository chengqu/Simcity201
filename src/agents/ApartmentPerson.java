package agents;

import java.util.ArrayList;
import java.util.List;

import simcity201.gui.ApartmentPersonGui;
import Buildings.ApartmentComplex;
import Buildings.ApartmentComplex.*;
import agent.Agent;

public class ApartmentPerson extends Agent{
	
	/**
	 * Data
	 */
	
	public Person p;
	List<String> groceries; //this is going to be a part of the person 
	
	boolean evicted = false;
	ApartmentPerson owner = null;
	ApartmentComplex apartmentComplex;
	Apartment apartment;
	
	ApartmentPersonGui gui;
	
	Object renterLock = new Object();
	List<myApartmentRenter> renters = new ArrayList<myApartmentRenter>();
	boolean timeToBill = false;

	//constructor
	public ApartmentPerson(Person agent, ApartmentComplex complex, Apartment a)
	{
		p = agent;
		apartmentComplex = complex;
		apartment = a;
	}
	
	public void setOwner(ApartmentPerson p)
	{
		owner = p;
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
		stateChanged();
	}
	
	/**
	 * Messages
	 */
	
	public void msgPleasePayBill(Bill b)
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
	
	public void msgCantPay(Bill b, ApartmentPerson a)
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
	public void msgHereIsMoney(Bill b, float money, ApartmentPerson a)
	{
		synchronized(renterLock)
		{
			for(myApartmentRenter r: renters)
			{
				if(r.equals(a))
				{
					synchronized(p.billLock)
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
		doLeave();
		return false;
	}

	private void doLeave() {
		p.msgDone();
	}

	private void doPayBills() {
		for(Bill b: p.bills)
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
	
	}

	private void doStoreGroceries() {
		
	}
	
	private void doBillPeople()
	{
		for(myApartmentRenter r: renters)
		{
			r.renter.msgPleasePayBill(new Bill(10.0f, r.renter, this));
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

	private class myApartmentRenter{
		public ApartmentPerson renter;
		public List<Bill> bills = new ArrayList<Bill>();
		public int strikes = 0;
		public myApartmentRenter(ApartmentPerson ar)
		{
			renter = ar;
		}
	}
}
