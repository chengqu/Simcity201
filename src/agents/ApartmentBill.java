package agents;

public class ApartmentBill {
	private float balance;
	private ApartmentPerson owner;
	private ApartmentPerson renter;
	
	public ApartmentBill(float b, ApartmentPerson r, ApartmentPerson o)
	{
		balance = b;
		owner = o;
		renter = r;
	}
	
	public float getBalance()
	{
		return balance;
	}
	
	public ApartmentPerson getOwner()
	{
		return owner;
	}
	
	public ApartmentPerson getRenter()
	{
		return renter;
	}
}
