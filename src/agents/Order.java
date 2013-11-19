package agents;

public class Order {
	public enum roles{};
	private roles role;
	private String location;
	
	public Order(roles r, String l)
	{
		role = r;
		location = l;
	}
	
	public roles getRole()
	{
		return role;
	}
	
	public String getLocation()
	{
		return location;
	}
}
