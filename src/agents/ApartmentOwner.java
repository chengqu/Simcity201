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
	
	boolean evicted = false;
	ApartmentOwner ao;
	ApartmentComplex apartmentComplex;
	Apartment apartment;
	ApartmentOwnerGui gui;
	
	//constructor
	
	/**
	 * Messages
	 */
	
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
