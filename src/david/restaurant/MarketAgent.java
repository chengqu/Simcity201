package david.restaurant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

<<<<<<< HEAD
import simcty201.interfaces.MarketStoreInterface;
=======
>>>>>>> Transportation
import david.restaurant.Interfaces.Cashier;
import david.restaurant.Interfaces.Market;
import agent.Agent;


<<<<<<< HEAD
public class MarketAgent extends Agent implements Market, MarketStoreInterface{
=======
public class MarketAgent extends Agent implements Market{
>>>>>>> Transportation
	//data
	List<RestockList> requests = Collections.synchronizedList(new ArrayList<RestockList>());
	Map<String, Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());
	List<myBill> bills = Collections.synchronizedList(new ArrayList<myBill>());
	Cook cook;
	Timer timer = new Timer();
	Cashier cashier;
	String name;
	int billId = 0;
	
	Object requestLock = new Object();
	Object foodLock = new Object();
	Object billLock = new Object();
	
	enum billState {sent, payed, cantBePayed};
	
	int STEAKAMOUNT = 4;
	int CHICKENAMOUNT = 6;
	int SALADAMOUNT = 5;
	int PIZZAAMOUNT = 4;
	
	public MarketAgent(String n)
	{
		name = n;
		foods.put("Steak", new Food("Steak", STEAKAMOUNT, 0, 0, null, 0, 15.99f));
		foods.put("Chicken", new Food("Chicken", CHICKENAMOUNT, 0, 0, null, 0, 10.99f));
		foods.put("Salad", new Food("Salad", SALADAMOUNT, 0, 0, null, 0, 5.99f));
		foods.put("Pizza", new Food("Pizza", PIZZAAMOUNT, 0, 0, null, 0, 8.99f));
	}
	
	public void setCashier(Cashier c)
	{
		cashier = c;
	}
	
	//messages
	public void msgHereIsMoney(float money, String id)
	{
		//turn this into an action that can be done by the scheduler
		print("msgHereIsMoney");
		synchronized(billLock)
		{
			myBill temp = null;
			for(myBill bill: bills)
			{
				if(bill.bill.ID.equalsIgnoreCase(id))
				{
					temp = bill;
					break;
				}
			}
			if(temp != null)
			{
				temp.state = billState.payed;
				temp.money = money;
			}
		}
		stateChanged();
	}
	
	public void msgNeedFood(Cook c, RestockList r)
	{
		synchronized(requestLock)
		{
			print("Need Food");
			cook = c;
			requests.add(r);
		}
		stateChanged();
	}
	
	public void msgCantPay(String id)
	{
		synchronized(billLock)
		{
			myBill temp = null;
			for(myBill bill: bills)
			{
				if(bill.bill.ID.equalsIgnoreCase(id))
				{
					temp = bill;
					break;
				}
			}
			if(temp != null)
			{
				temp.state = billState.cantBePayed;
				temp.money = 0;
			}
		}
		stateChanged();
	}
	
	public void drain()
	{
		print("Food drained");
		for(Food f: foods.values())
		{
			f.amount = 0;
		}
	}
	
	@Override
	public boolean pickAndExecuteAnAction() {
		synchronized(billLock)
		{
			myBill temp = null;
			for(myBill bill: bills)
			{
				if(bill.state == billState.payed)
				{
					temp = bill;
					break;
				}
			}
			if(temp != null)
			{
				DoProcessPayedBill(temp);
				return true;
			}
		}
		
		synchronized(billLock)
		{
			myBill temp = null;
			for(myBill bill: bills)
			{
				if(bill.state == billState.cantBePayed)
				{
					temp = bill;
					break;
				}
			}
			if(temp != null)
			{
				DoProcessBillCantPay(temp);
				return true;
			}
		}
		
		synchronized(requestLock)
		{
			if(requests.isEmpty() == false)
			{
				DoFulfillRequest(requests.get(0));
				return true;
			}
		}
		return false;
	}
	
	//actions
	private void DoProcessPayedBill(myBill b)
	{
		print("bill payed, now removed");
		bills.remove(b);
	}
	
	private void DoProcessBillCantPay(myBill b)
	{
		print("bill removed, couldn't be payed");
		bills.remove(b);
	}

	private void DoFulfillRequest(RestockList r) 
	{
		final RestockList temp = new RestockList();
		RestockList temp2 = new RestockList();
		boolean sendWarning = false;
		for(int i = 0; i < r.items.size(); i++)
		{
			if(foods.get(r.items.get(i)).amount >= r.itemAmounts.get(i))
			{
				temp.items.add(r.items.get(i));
				temp.itemAmounts.add(r.itemAmounts.get(i));
				foods.get(r.items.get(i)).amount -= r.itemAmounts.get(i);
			}
			else
			{
				temp.items.add(r.items.get(i));
				temp.itemAmounts.add(foods.get(r.items.get(i)).amount);
				
				temp2.items.add(r.items.get(i));
				temp2.itemAmounts.add(foods.get(r.items.get(i)).amount);
				
				foods.get(r.items.get(i)).amount = 0;
				sendWarning = true;
			}
		}
		requests.remove(r);
		final MarketAgent mTemp = this;
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				synchronized(mTemp)
				{
					float balance = 0;
					for(int i = 0; i < temp.itemAmounts.size(); i++)
					{
						print("1");
						balance += temp.itemAmounts.get(i) * foods.get(temp.items.get(i)).cost;
	 				}
					if(balance > 0)
					{
						synchronized(billLock)
						{
							Bill bill = new Bill(mTemp.getName() + Integer.toString(billId), balance);
							cashier.msgHereIsBill(bill);
							bill = new Bill(mTemp.getName() + Integer.toString(billId), balance);
							bills.add(new myBill(bill, billState.sent, 0));
							billId++;
						}
					}
				}
				cook.msgHereIsItems(mTemp, temp);
			}
			
		}, 15000);
		
		if(sendWarning)
		{
			cook.msgCanPartiallyFill(this, temp2);
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	private class myBill
	{
		Bill bill;
		billState state;
		float money;
		public myBill(Bill b, billState s, float m)
		{
			money = m;
			bill = b;
			state = s;
		}
	}
}
