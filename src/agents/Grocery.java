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
	
	public boolean equals(Grocery g) {
		if ( g.getAmount() == amount) {
			if ( g.getFood().equalsIgnoreCase(food)) {
				return true;
			}
		}
		return false;
	}
}
