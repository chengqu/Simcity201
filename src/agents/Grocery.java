package agents;

public class Grocery {
	private String food;
	private int amount;
	public Grocery(String f, int a)
	{
		food = f;
		amount = a;
	}
	
	public String getFood()
	{
		return food;
	}
	
	public int getAmount()
	{
		return amount;
	}
}
