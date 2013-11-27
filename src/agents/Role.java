package agents;

public class Role {
	/**
	 * @author Ryan (Gueho) Choi
	 * I have added Robbery, TellerAtChaseBank
	 */
	public enum roles{Robbery, TellerAtChaseBank, AptOwner, ApartmentOwner, ApartmentRenter,
				JonnieWalker, houseRenter, houseOwner, marketManager, preferHomeEat};
	private roles role;
	private String location;
	
	public Role(roles r, String l)
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
