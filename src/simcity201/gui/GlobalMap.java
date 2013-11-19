package simcity201.gui;

import java.util.*;

import agents.Person;
import Buildings.Building;


public class GlobalMap {
	/*Singleton -- */
	private static GlobalMap map = new GlobalMap();
	private GlobalMap() {}
	public static GlobalMap getGlobalMap() {
		return map;}
	/*-------------*/
	
	
	/* Data */
	protected List<Building> buildings =
			new ArrayList<Building>();
	
	public void addBuilding(Building b) {
		buildings.add(b);
	}
	
	protected List<Person> people = new ArrayList<Person>();
	
	/**
	 * @param str
	 * @return building if exist, null if not
	 */
	public Building searchBuildingsByName(String str) {
		for(Building b : buildings) {
			if (str.equalsIgnoreCase(b.name)) {
				return b;
			}
		}
		return null;
	}
	
	public void addPersonToWorld(String name)
	{
		Person p = new Person(name);
		p.startThread();
		people.add(p);
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
