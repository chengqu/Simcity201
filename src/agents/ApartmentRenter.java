package agents;

import java.util.ArrayList;
import java.util.List;

import simcity201.gui.ApartmentRenterGui;
import Buildings.ApartmentComplex;
import Buildings.ApartmentComplex.*;
import agent.Agent;

public class ApartmentRenter extends Agent{
	
	/**
	 * Data
	 */
	
	public Person p;
	List<String> groceries; //this is going to be a part of the person 
	
	boolean evicted = false;
	ApartmentOwner ao;
	ApartmentComplex apartmentComplex;
	Apartment apartment;
	
	ApartmentRenterGui gui;
	
	//constructor
	public ApartmentRenter(ApartmentComplex complex)
	{
		apartmentComplex = complex;
		stateChanged();
	}
	
	public void setGui(ApartmentRenterGui g)
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
		else
		{
			doLeave();
			return false;
		}
	}

	private void doLeave() {
		
	}

	private void doPayBills() {
		
	}

	private void doCookAndEatFood() {
	
	}

	private void doStoreGroceries() {
		
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
		//Apartment.remove(this);
	}

}
