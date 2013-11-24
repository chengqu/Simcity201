package agents;

import java.util.*;

public class Task {
	public enum Objective{goTo, patron, worker, master}; //master refers to home
	private Objective objective;
	private String location;
	public enum specificTask{eat, takeOutMoney, buyGroceries, none};
	private List<specificTask> specificTasks;
	
	public Task(Objective o, String l, List<specificTask> listOfTasks)
	{
		objective = o;
		location = l;
		specificTasks = listOfTasks;
	}
	
	public Objective getObjective()
	{
		return objective;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public List<specificTask> getTaskList()
	{
		return specificTasks;
	}
}
