package Buildings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;





import ApartmentGui.ApartmentPersonGui;
import agents.ApartmentBill;
import agents.ApartmentPerson;
import agents.Person;
import ApartmentGui.ApartmentAnimationPanel;
import animation.BaseAnimationPanel;

public class ApartmentComplex extends Building{
	
	public List<Apartment> apartments = new ArrayList<Apartment>();
	public ApartmentPerson owner = null;
	private ApartmentAnimationPanel animationPanel;
	Random rand = new Random();
	
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
		
		ApartmentPersonGui g= new ApartmentPersonGui(r, animationPanel, 0,0,0,0);
		r.setGui(g);
		
		p.apartment = a;
		r.startThread();
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
		
		ApartmentPersonGui g = new ApartmentPersonGui(r, animationPanel, 200 + rand.nextInt(200), 200 + rand.nextInt(200), 
				250 + rand.nextInt(200), 250 + rand.nextInt(200));
		r.setGui(g);
		
		p.apartment = a;
		r.startThread();
		
		animationPanel.addGui(g);
		
		//add this gui to some sort of animation gui
		
		apartments.add(a);
		System.out.println("\nADDED PERSON: "+a.person.p.getName()+"\n");
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
