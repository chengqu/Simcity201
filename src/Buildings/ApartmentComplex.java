package Buildings;

import java.util.ArrayList;
import java.util.List;

import simcity201.gui.ApartmentRenterGui;
import agents.ApartmentOwner;
import agents.ApartmentRenter;
import agents.Person;

public class ApartmentComplex extends Building{
	
	public List<Apartment> apartments = new ArrayList<Apartment>();
	public ApartmentOwner owner = new ApartmentOwner();
	
	public void addResidenceOwner(Person p)
	{
		if(owner == null)
		{
			owner = new ApartmentOwner();
		}
		else
		{
			System.out.println("Apartment complex already has an owner.");
		}
	}
	
	public void addResidenceRenter(Person p)
	{
		for(Apartment a: apartments)
		{
			if(a.renter.p != null && a.renter.p == p)
			{
				a.renter.doThings();
				return;
			}
		}
		Apartment a = new Apartment();
		ApartmentRenter r = new ApartmentRenter(this);
		a.setRenter(r);
		r.setApartment(a);
		
		ApartmentRenterGui g = new ApartmentRenterGui(r);
		r.setGui(g);
		
		r.startThread();
		
		//add this gui to some sort of animation gui
		
		apartments.add(a);
	}
	
	public class Apartment{
		public ApartmentRenter renter;
		public List<String> Fridge = new ArrayList<String>();
		String name;
		
		public void setRenter(ApartmentRenter ar)
		{
			if(renter == null)
			{
				renter = ar;
				ar.setApartment(this);
			}
		}
		
		public void emptyApartment()
		{
			renter = null;
			Fridge.clear();
		}
	}
}
