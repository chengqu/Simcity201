package simcity201.gui;

import java.util.*;
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
	
	/**
	 * @param str
	 * @return building if exist, null if not
	 */
	public Building searchByName(String str) {
		for(Building b : buildings) {
			if (str.equalsIgnoreCase(b.name)) {
				return b;
			}
		}
		return null;
	}
	
}
