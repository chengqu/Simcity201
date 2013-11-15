package Buildings;

import java.util.ArrayList;
import java.util.List;

import agents.ApartmentRenter;

public class ApartmentComplex extends Building{
	
	
	public class Apartment{
		public ApartmentRenter renter;
		public List<String> Fridge = new ArrayList<String>();
		
		public Apartment(ApartmentRenter ar)
		{
			renter = ar;
		}
		
		public void emptyApartment()
		{
			renter = null;
			Fridge.clear();
		}
	}
}
