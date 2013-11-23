package david.restaurant;

public class Bill {
	public float balance;
	String ID;
	public Bill(String id, float b)
	{
		balance = b;
		ID = id;
	}
	
	public String getID()
	{
		if(ID != null)
			return ID;
		return null;
	}
}
