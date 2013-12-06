package simcity201.gui;

import java.net.URISyntaxException;
import java.util.*;

import agents.Grocery;
import newMarket.MarketRestaurantHandlerAgent;
import newMarket.NewMarket;
import agents.BusAgent;
import agents.Person;
import Buildings.Building;
import agents.Role;

public class GlobalMap {
	/*Singleton -- */
	private static GlobalMap map = new GlobalMap();
	private GlobalMap() {}
	int ssn = 1000000;
	Object ssnLock = new Object();
	public static GlobalMap getGlobalMap() {
		return map;}
	/*-------------*/
	
	/* Data */
	protected Map<String, Building> buildings =
			new HashMap<String, Building>();
	public Collection<Building> getBuildings() {
		return buildings.values();
	}
	Timer timer = new Timer();
	public Timer getTimer() {
		return this.timer;
	}
	public enum BuildingType { LynRestaurant, RyanRestaurant, JoshRestaurant, 
							DavidRestaurant, EricRestaurant, ChengRestaurant,
								Bank, House, Store, Apartment } 
	public List<BusAgent> buses = new ArrayList<BusAgent>();
	
	public MarketRestaurantHandlerAgent marketHandler = null;
	
	public void addBuilding(BuildingType type, int x, int y, int width, int height, String name) {
		Building temp;
		switch(type) {
			case Apartment:
				temp = new Buildings.ApartmentComplex();
				temp.x = x; temp.y = y;
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				break;
			case Bank:
				temp = new Bank();
				temp.x = x; temp.y = y;
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				break;
			case ChengRestaurant:
				try {
					temp = new Cheng.gui.RestaurantGui();
					temp.x = x; temp.y = y;
					temp.width = width; temp.height = height;
					temp.name = name;
					temp.type = Building.Type.Restaurant;
					buildings.put(temp.name, temp);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case DavidRestaurant:
				temp = new david.restaurant.gui.RestaurantGui();
				temp.x = x; temp.y = y;
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				temp.type = Building.Type.Restaurant;
				break;
			case EricRestaurant:
			   temp = new ericliu.gui.RestaurantGui();
	            temp.x = x; temp.y = y;
	            temp.width = width; temp.height = height;
	            temp.name = name;
	            buildings.put(temp.name, temp);
	            temp.type = Building.Type.Restaurant;
				break;
			case House:
				temp = new House.gui.HousePanelGui();
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				break;
			case JoshRestaurant:
				temp = new josh.restaurant.gui.RestaurantGui();
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				temp.type = Building.Type.Restaurant;
				break;
			case LynRestaurant:
				temp = new LYN.gui.RestaurantGui();
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				temp.type = Building.Type.Restaurant;
				break;
			case RyanRestaurant:
				temp = new guehochoi.gui.RestaurantGui();
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				temp.type = Building.Type.Restaurant;
				break;
			case Store:
				//temp = new Market.Market();
				temp = new newMarket.NewMarket();
				marketHandler = ((newMarket.NewMarket)temp).handlers.get(0);
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				break;
			default:
				break;
		}
	}
	
	protected List<Person> people =
			new ArrayList<Person>();
	
	public void addPerson(Person p)
	{
		people.add(p);
	}
	
	public void startAllPeople() {
		for(Person p: people) {
			p.startThread();
			p.doThings();
		}
	}
	
	/**
	 * @param str
	 * @return building if exist, null if not
	 */

	public Building searchByName(String str) {
		return buildings.get(str);
	}
	
	public List<Person> getListOfPeople()
	{
		return people;
	}
	
	public String[] peopleNames()
	{
		List<String> temp = new ArrayList<String>();
		for(Person p: people)
		{
			temp.add(p.getName());
		}
		return (String[]) temp.toArray();
	}

	public Person searchPersonByName(String name)
	{
		for(Person p: people)
		{
			if(p.getName().equals(name))
			{
				return p;
			}
		}
		return null;
	}
	
	public int getNewSSN()
	{
		synchronized(ssnLock)
		{
			ssn++;
			return ssn;
		}
	}
}
