package agents;

public class Task {
	public enum Objective{goTo, patron, worker, master}; //master refers to home
	private Objective objective;
	private String location;
	
	public Task(Objective o, String l)
	{
		objective = o;
		location = l;
	}
	
	public Objective getObjective()
	{
		return objective;
	}
	
	public String getLocation()
	{
		return location;
	}
}
