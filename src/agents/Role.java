package agents;

public class Role {
	public enum roles{Robbery, aptOwner, ApartmentOwner, ApartmentRenter};
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
