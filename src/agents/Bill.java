package agents;

public class Bill {
	private float balance;
	private ApartmentOwner owner;
	private ApartmentRenter renter;
	
	public Bill(float b, ApartmentRenter r, ApartmentOwner o)
	{
		balance = b;
		owner = o;
		renter = r;
	}
	
	public float getBalance()
	{
		return balance;
	}
	
	public ApartmentOwner getOwner()
	{
		return owner;
	}
	
	public ApartmentRenter getRenter()
	{
		return renter;
	}
}
