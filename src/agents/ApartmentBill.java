package agents;

public class ApartmentBill {
	private float balance;
	private ApartPerson owner;
	private ApartPerson renter;
	
	public ApartmentBill(float b, ApartPerson r, ApartPerson o)
	{
		balance = b;
		owner = o;
		renter = r;
	}
	
	public float getBalance()
	{
		return balance;
	}
	
	public ApartPerson getOwner()
	{
		return owner;
	}
	
	public ApartPerson getRenter()
	{
		return renter;
	}
}
