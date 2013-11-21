package Buildings;

import java.util.ArrayList;
import java.util.List;

import simcity201.gui.ApartmentPersonGui;
import agents.ApartmentBill;
import agents.ApartmentPerson;
import agents.Person;
import animation.ApartmentAnimationPanel;
import animation.BaseAnimationPanel;

public class ApartmentComplex extends Building{
	
	public List<Apartment> apartments = new ArrayList<Apartment>();
	public ApartmentPerson owner = null;
	private ApartmentAnimationPanel animationPanel;
	String name;
	
	public ApartmentComplex()
	{
		animationPanel = new ApartmentAnimationPanel();
	}
	
	public void addOwner(Person p)
	{
		if(owner != null)
		{
			owner.doThings();
			return;
		}
		Apartment a = new Apartment();
		ApartmentPerson r = new ApartmentPerson(p, this, a);
		a.setPerson(r);
		r.setApartment(a);
		
		ApartmentPersonGui g = new ApartmentPersonGui(r);
		r.setGui(g);
		
		
		
		//add this gui to some sort of animation gui
		
		apartments.add(a);
	}
	
	public void addRenter(Person p)
	{
		for(Apartment a: apartments)
		{
			if(a.person.p != null && a.person.p == p)
			{
				a.person.doThings();
				return;
			}
		}
		Apartment a = new Apartment();
		ApartmentPerson r = new ApartmentPerson(p, this, a);
		a.setPerson(r);
		r.setApartment(a);
		
		ApartmentPersonGui g = new ApartmentPersonGui(r);
		r.setGui(g);
		animationPanel.addGui(g);
		
		//add this gui to some sort of animation gui
		
		apartments.add(a);
	}
	
	public class Apartment{
		public ApartmentPerson person;
		public List<String> Fridge = new ArrayList<String>();
		public List<ApartmentBill> bills = new ArrayList<ApartmentBill>();
		public int strikes = 0;
		String name;
		
		public void setPerson(ApartmentPerson ar)
		{
			if(person == null)
			{
				person = ar;
				ar.setApartment(this);
			}
		}
		
		public void emptyApartment()
		{
			person = null;
			Fridge.clear();
		}
	}

	@Override
	public BaseAnimationPanel getAnimationPanel() {
		return this.animationPanel;
	}
}
