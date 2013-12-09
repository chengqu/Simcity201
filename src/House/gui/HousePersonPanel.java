package House.gui;

import javax.swing.*;

import House.agents.HousePerson;
import agents.Grocery;
import agents.Person;
import agents.Task;
import animation.GenericListPanel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class HousePersonPanel extends JPanel {

	//Host, cook, waiters and customers
	public Person p;
	public HousePerson r= new HousePerson(p,this);
	public List<Grocery> groceries = new ArrayList<Grocery>();
	public boolean moretask;


	private HousePanelGui gui; //reference to main gui
	private HouseGui houseGui = new HouseGui(r,gui);

	public HousePersonPanel(HousePanelGui gui) {
		this.gui = gui;
		map2.put("Steak", (double)0);
		map2.put("Chicken", (double)0);
		map2.put("Salad", (double)0);
		map2.put("Pizza", (double)0);
		//house.setGui(houseGui);

		// gui.animationPanel.addGui(houseGui);
		// house.startThread();
		// houseGui.setPresent(true);
		//houseGui.setHungry();
		// house.msgPayBills();
		// house.msgRestathome();


		// initRestLabel();
		// add(restLabel);
	}

	public Map<String, Double> map2 = new HashMap<String, Double>();
	private class Fridge {
		public String choice;
		public int amount;

		Fridge(String choice, int amount) {
			this.choice = choice;
			this.amount = amount;
		}
	}

	public void addOwner(Person p)
	{


		moretask = false;
		this.p = p;

		r = new HousePerson(p,this);

		HouseGui houseGui = new HouseGui(r,gui);
		r.setGui(houseGui);
		gui.animationPanel.addGui(houseGui);


		r.startThread();
		houseGui.setPresent(true);

		Task.specificTask temp = null;
		for(Task.specificTask s:p.currentTask.sTasks) {
			if(s.equals(Task.specificTask.eatAtHome)){

				temp = s;
				break;
			}
		}
		if(temp!=null) {
			System.out.print("eatathome");
			moretask = true;
			p.currentTask.sTasks.remove(temp);
			r.msgIameatingathome();
		}

		for(Task.specificTask s:p.currentTask.sTasks) {
			if(s.equals(Task.specificTask.sleepAtHome)){

				temp = s;
				break;
			}
		}
		if(temp!=null && moretask == false) {
			System.out.print("sleep");
			moretask = true;
			p.currentTask.sTasks.remove(temp);
			r.msgRestathome();
		}

		for(Task.specificTask s:p.currentTask.sTasks) {
			if(s.equals(Task.specificTask.depositGroceries)){

				temp = s;
				break;
			}
		}
		if(temp!=null && moretask == false) {
			System.out.print("depositathome");
			moretask = true;
			p.currentTask.sTasks.remove(temp);
			r.msgstoreGroceries();
		}
		//house.msgIameatingathome();


		//add this gui to some sort of animation gui


	}

	public void addRenter(Person p)
	{

		moretask = false;
		this.p = p;
		r = new HousePerson(p,this);
		r.startThread();
		HouseGui houseGui = new HouseGui(r,gui);
		r.setGui(houseGui);
		gui.animationPanel.addGui(houseGui);
		houseGui.setPresent(true);
		Task.specificTask temp = null;
		for(Task.specificTask s:p.currentTask.sTasks) {
			if(s.equals(Task.specificTask.eatAtHome)){

				temp = s;
				break;
			}
		}
		if(temp!=null) {
			moretask = true;
			p.currentTask.sTasks.remove(temp);
			r.msgIameatingathome();
		}

		for(Task.specificTask s:p.currentTask.sTasks) {
			if(s.equals(Task.specificTask.sleepAtHome)){

				temp = s;
				break;
			}
		}
		if(temp!=null && moretask == false) {
			moretask = true;
			p.currentTask.sTasks.remove(temp);
			r.msgRestathome();
		}

		for(Task.specificTask s:p.currentTask.sTasks) {
			if(s.equals(Task.specificTask.depositGroceries)){

				temp = s;
				break;
			}
		}
		if(temp!=null && moretask == false) {
			moretask = true;
			p.currentTask.sTasks.remove(temp);
			r.msgstoreGroceries();
		}

		for(Task.specificTask s:p.currentTask.sTasks) {
			if(s.equals(Task.specificTask.payBills)){

				temp = s;
				break;
			}
		}
		if(temp!=null && moretask == false) {
			moretask = true;
			p.currentTask.sTasks.remove(temp);
			r.msgPayBills();
		}
		//house.msgIameatingathome();


	}

	public void deleteperson(HousePerson p) {
		//house.stopThread();
		p.getGui().setPresent(false);
	}

	public void updatemap() {
		map2.put("Steak", (double)2);
		map2.put("Chicken", (double)2);
		map2.put("Salad", (double)2);
		map2.put("Pizza", (double)2);
		groceries.clear();

	}

	public List<Grocery> returngroceries() {
		for (String key : map2.keySet()) {
			if(map2.get(key) == (double)0) {
				groceries.add(new Grocery(key,2));
			}
		}
		return groceries;
	}
}
