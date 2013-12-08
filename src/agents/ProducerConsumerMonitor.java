package agents;
import java.util.Vector;

import david.restaurant.CookAgent.myOrder;

public class ProducerConsumerMonitor<Type>{
	private int Max;
	private int count;
	private Vector<Type> list;
	private MonitorSubscriber subscriber;
	int waitTime;
	
	public ProducerConsumerMonitor(int Max)
	{
		waitTime = 1000;
		count = 0;
		list = new Vector<Type>();
		subscriber = null;
		this.Max = Max;
	}
	
	public void setSubscriber(MonitorSubscriber subscriber)
	{
		this.subscriber = subscriber;
	}
	
	synchronized public boolean insert(Type t)
	{
		if(count == Max)
		{
			try
			{
				System.out.println("\tFull");
				wait(waitTime);
			}
			catch(InterruptedException e)
			{
				
			}
			if(count == Max)
			{
				return false;
			}
		}
		insert_item(t);
		count++;
		if(count == 1)
		{
			System.out.println("Not empty\n");
			notify();
		}
		return true;
	}
	
	synchronized public Type remove()
	{
		if(count == 0)
		{
			try
			{
				System.out.println("Empty");
				wait(waitTime);
			}
			catch(InterruptedException ex)
			{
				
			}
			if(count == 0)
			{
				return null;
			}
		}
		
		Type data = remove_item();
		count--;
		if(count == Max - 1)
		{
			System.out.println("\tNot full");
			notify();
		}
		return data;
	}
	
	private void insert_item(Type t)
	{
		list.add(t);
	}
	
	private Type remove_item()
	{
		Type t = list.get(0);
		list.remove(0);
		return t;
	}
}
