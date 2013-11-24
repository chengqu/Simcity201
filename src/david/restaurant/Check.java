package david.restaurant;

import david.restaurant.Interfaces.Customer;

public class Check {
	public Customer customer;
	public float balance;
	String choice;
	public Check(Customer c, String ch)
	{
		customer = c;
		balance = 0;
		choice = ch;
	}
}
