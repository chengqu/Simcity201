package simcity201.gui;

import java.net.URISyntaxException;
import java.util.*;

import agents.Person;
import Buildings.Building;
import agents.Role;

public class GlobalMap {
	/*Singleton -- */
	private static GlobalMap map = new GlobalMap();
	private GlobalMap() {}
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
				break;
			case EricRestaurant:
			   temp = new ericliu.gui.RestaurantGui();
            temp.x = x; temp.y = y;
            temp.width = width; temp.height = height;
            temp.name = name;
            buildings.put(temp.name, temp);
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
				break;
			case LynRestaurant:
				temp = new LYN.gui.RestaurantGui();
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				break;
			case RyanRestaurant:
				temp = new guehochoi.gui.RestaurantGui();
				temp.x = x; temp.y = y; 
				temp.width = width; temp.height = height;
				temp.name = name;
				buildings.put(temp.name, temp);
				break;
			case Store:
				temp = new Market.Market();
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
	public enum whoIs { HungryPerson, Robbery }
	public void addPerson(whoIs w, String name) {
		Person p = new Person(name);
		/*
		switch(w) {
			case Robbery: break;
			case HungryPerson:
				p.hungerLevel = 70;
				p.money = 1000;
				p.roles.add(new Role(Role.roles.ApartmentRenter, "Apt1"));
			break;
			default:	
				break;
		}*/
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
}
