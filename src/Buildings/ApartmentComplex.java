package Buildings;

import java.util.ArrayList;
import java.util.List;

import simcity201.gui.ApartmentPersonGui;
import agents.ApartmentOwner;
import agents.ApartmentPerson;
import agents.Person;
import animation.ApartmentAnimationPanel;
import animation.BaseAnimationPanel;

public class ApartmentComplex extends Building{
	
	public List<Apartment> apartments = new ArrayList<Apartment>();
	public ApartmentOwner owner = null;
	private ApartmentAnimationPanel animationPanel;
	String name;
	
	public ApartmentComplex()
	{
		animationPanel = new ApartmentAnimationPanel();
	}
	
	public void addResidenceOwner(Person p)
	{
		if(owner == null)
		{
			owner = new ApartmentOwner(p);
			owner.startThread();
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
		ApartmentPerson r = new ApartmentPerson(this, a);
		a.setRenter(r);
		r.setApartment(a);
		
		ApartmentPersonGui g = new ApartmentPersonGui(r);
		r.setGui(g);
		
		//add this gui to some sort of animation gui
		
		apartments.add(a);
	}
	
	public class Apartment{
		public ApartmentPerson renter;
		public List<String> Fridge = new ArrayList<String>();
		String name;
		
		public void setRenter(ApartmentPerson ar)
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

	@Override
	public BaseAnimationPanel getAnimationPanel() {
		return this.animationPanel;
	}
}
