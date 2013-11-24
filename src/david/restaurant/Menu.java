package david.restaurant;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	public List<Food> items = new ArrayList<Food>();
	
	public Menu()
	{
		items.add(new Food("Steak", "ST", 15.99f));
		items.add(new Food("Chicken", "C", 10.99f));
		items.add(new Food("Salad", "SA", 5.99f));
		items.add(new Food("Pizza", "P", 8.99f));
	}
	
	public static String getAbbreviation(String choice)
	{
		Menu m = new Menu();
		for(Food item: m.items)
		{
			if(item.name == choice)
			{
				return item.abbreviation;
			}
		}
		return null;
	}
}
