package david.restaurant;

public class Food {
	public String name;
	public int amount;
	public int low;
	public int max;
	public String abbreviation;
	public int time;
	public float cost;
	
	public Food(String n)
	{
		name = n;
	}
	
	public Food(String n, String a)
	{
		name = n;
		abbreviation = a;
	}
	
	public Food(String n, String a, float c)
	{
		name = n;
		abbreviation = a;
		cost = c;
	}
	
	public Food(String n, int a)
	{
		name = n;
		amount = a;
	}
	
	public Food(String n, int a, int t)
	{
		name = n;
		amount = a;
		time = t;
	}
	
	public Food(String n, int am, int l, int m, int t)
	{
		name = n;
		amount = am;
		low = l;
		max = m;
		time = t;
	}
	
	public Food(String n, int am, int l, int m, String ab, int t, float c)
	{
		name = n;
		amount = am;
		low = l;
		max = m;
		abbreviation = ab;
		time = t;
		cost = c;
	}
	
	static public Food copy(Food f)
	{
		return new Food(f.name, f.amount, f.low, f.max, f.abbreviation, f.time, f.cost);
	}
}
