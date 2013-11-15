package agents;

public class Bill {
	private float balance;
	private ApartmentRenter renter;
	
	public Bill(float b, ApartmentRenter r)
	{
		balance = b;
		renter = r;
	}
	
	public float getBalance()
	{
		return balance;
	}
	
	public ApartmentRenter getRenter()
	{
		return renter;
	}
}
