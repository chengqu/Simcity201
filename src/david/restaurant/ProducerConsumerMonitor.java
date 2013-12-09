package david.restaurant;
import java.util.Vector;

import david.restaurant.CookAgent.myOrder;

public class ProducerConsumerMonitor {
	private int Max;
	private int count;
	private Vector<Object> objects;
	private MonitorSubscriber subscriber;
	
	public ProducerConsumerMonitor(int Max)
	{
		count = 0;
		objects = new Vector<Object>();
		subscriber = null;
		this.Max = Max;
	}
	
	public void setSubscriber(MonitorSubscriber subscriber)
	{
		this.subscriber = subscriber;
	}
	
	synchronized public boolean insert(Object o)
	{
		if(count == Max)
		{
			try
			{
				System.out.println("\tFull");
				wait(5000);
			}
			catch(InterruptedException e)
			{
				
			}
			if(count == Max)
			{
				return false;
			}
		}
		insert_item(o);
		count++;
		if(count == 1)
		{
			System.out.println("Not empty\n");
			notify();
		}
		return true;
	}
	
	synchronized public Object remove()
	{
		if(count == 0)
		{
			try
			{
				System.out.println("Empty");
				wait(5000);
			}
			catch(InterruptedException ex)
			{
				
			}
			if(count == 0)
			{
				return null;
			}
		}
		
		Object data = remove_item();
		count--;
		if(count == Max - 1)
		{
			System.out.println("\tNot full");
			notify();
		}
		return data;
	}
	
	private void insert_item(Object o)
	{
		objects.add(o);
	}
	
	private Object remove_item()
	{
		Object object = objects.get(0);
		objects.remove(0);
		return object;
	}
}
