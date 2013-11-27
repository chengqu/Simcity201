package agents;

public class Role {
	/**
	 * @author Ryan (Gueho) Choi
	 * I have added Robbery, TellerAtChaseBank
	 */
	public enum roles{Robbery, TellerAtChaseBank, AptOwner, ApartmentOwner, ApartmentRenter,
<<<<<<< HEAD
				JonnieWalker, houseRenter, houseOwner, marketManager, preferHomeEat, preferBus,
				preferCar};
=======
				JonnieWalker, houseRenter, houseOwner, marketManager, preferHomeEat};
>>>>>>> Transportation
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
