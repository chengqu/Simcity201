package agents;

import java.util.ArrayList;
import java.util.List;

import Buildings.ApartmentComplex;
import Buildings.ApartmentComplex.*;
import agent.Agent;
import agents.ApartmentOwner.Bill;

public class ApartmentRenter extends Agent{
	
	/**
	 * Data
	 */
	
	Person p;
	List<String> groceries; //this is going to be a part of the person 
	
	boolean evicted = false;
	ApartmentOwner ao;
	ApartmentComplex apartmentComplex;
	Apartment apartment;
	List<Bill> bills = new ArrayList<Bill>();
	
	//constructor
	public ApartmentRenter(ApartmentComplex complex)
	{
		apartmentComplex = complex;
	}
	
	/**
	 * Messages
	 */
	
	public void msgPleasePayBill(Bill b)
	{
		bills.add(b);
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
		if(bills.size() > 0)
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
		
	}

}
