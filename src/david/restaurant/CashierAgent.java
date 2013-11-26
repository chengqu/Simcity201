package david.restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import david.restaurant.Interfaces.Cashier;
import david.restaurant.Interfaces.Customer;
import david.restaurant.Interfaces.Market;
import david.restaurant.Interfaces.Waiter;
import david.restaurant.Test.Mock.EventLog;
import david.restaurant.Test.Mock.LoggedEvent;
import agent.Agent;
import agents.Grocery;

public class CashierAgent extends Agent implements Cashier{

	public List<myCheck> checks = Collections.synchronizedList(new ArrayList<myCheck>());
	Map<String, Float> prices = Collections.synchronizedMap(new HashMap<String, Float>());
	public enum checkState {unprocessed, processed, nextTime};
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	private List<Market> markets;
	private List<cookOrder> cookOrders = new ArrayList<cookOrder>();
	public float money = 300;
	
	CookAgent cook;
	
	Object checkLock = new Object();
	Object billLock = new Object();
	
	public EventLog log = new EventLog();
	
	public void print_()
	{
		print(Integer.toString(checks.size() + prices.size()));
	}
	
	//messages
	public CashierAgent(List<Market> m)
	{
		markets = Collections.synchronizedList(m);
		prices.put("Steak", 15.99f);
		prices.put("Chicken", 10.99f);
		prices.put("Salad", 5.99f);
		prices.put("Pizza", 8.99f);
	}
	
	public void msgHereIsBill(Bill b)
	{
		print("msgHereIsBill");
		log.add(new LoggedEvent("HereIsBill"));
		synchronized(billLock)
		{
			bills.add(b);
		}
		stateChanged();
	}
	
	public void msgProcessOrder(Waiter w, Customer c, String choice)
	{
		synchronized(checkLock)
		{
			print("processOrder");
			for(myCheck ch: checks)
			{
				if(ch.check.customer == c)
				{
					ch.state = checkState.unprocessed;
					ch.check.choice = choice;
					stateChanged();
					return;
				}
			}
			checks.add(new myCheck(w, checkState.unprocessed, new Check(c, choice)));
		}
		stateChanged();
	}
	
	public void msgHereIsMoney(Check ch, float balance)
	{
		synchronized(checkLock)
		{
			print("HereIsMoney: " + ch.balance);
			for(myCheck c: checks)
			{
				if(c.check == ch)
				{
					c.check.balance -= balance;
					break;
				}
			}
		}
		stateChanged();
	}
	public void msgCantPay(Check ch)
	{
		synchronized(checkLock)
		{
			for(myCheck c: checks)
			{
				if(c.check == ch)
				{
					print("cantPayMsg");
					c.state = checkState.nextTime;
				}
			}
		}
	}
	
	//msg from cook
	public void msgHereIsPrice(List<Grocery> orders, float price) {
		synchronized(cookOrders)
		{
			cookOrders.add(new cookOrder(orders, price));
		}
		stateChanged();
	}
	
	public boolean pickAndExecuteAnAction() {
		synchronized(cookOrders)
		{
			if(cookOrders.isEmpty() == false)
			{
				DoProcessCookOrder(cookOrders.get(0));
				return true;
			}
		}
		
		synchronized(billLock)
		{
			if(bills.isEmpty() == false)
			{
				DoProcessBill(bills.get(0));
				return true;
			}
		}
		
		synchronized(checkLock)
		{
			for(myCheck ch: checks)
			{
				if(ch.state == checkState.unprocessed)
				{
					DoProcessCheck(ch);
					return true;
				}
			}
		}
		
		synchronized(checkLock)
		{
			for(myCheck ch: checks)
			{
				if(ch.check.balance == 0)
				{
					DoRemoveCheck(ch);
					return true;
				}
			}
		}
		return false;
	}
	
	private void DoProcessCookOrder(cookOrder order) {
		cookOrders.remove(order);
		if(order.price > this.money)
		{
			cook.msgHereIsMoney(this.money);
		}
		else
		{
			this.money -= order.price;
			cook.msgHereIsMoney(order.price);
		}
	}

	//actions
	void DoProcessBill(Bill b)
	{
		if(money > b.balance)
		{
			money -= b.balance;
			bills.remove(b);
			for(Market m: markets)
			{
				if(b.ID.contains(m.getName()))
				{
					m.msgHereIsMoney(b.balance, b.ID);
					break;
				}
			}
		}
		else
		{
			bills.remove(b);
			for(Market m: markets)
			{
				if(b.ID.contains(m.getName()))
				{
					m.msgCantPay(b.ID);
					break;
				}
			}
		}
	}
	
	void DoProcessCheck(myCheck ch)
	{
		ch.check.balance += prices.get(ch.check.choice);
		ch.state = checkState.processed;
		ch.waiter.msgHereIsCheck(ch.check);
	}
	
	void DoRemoveCheck(myCheck ch)
	{
		print("Balance:" + ch.check.balance);
		checks.remove(ch);
	}
	
	private class cookOrder{
		List<Grocery> order;
		float price;
		cookOrder(List<Grocery> o, float price)
		{
			this.price = price;
			order = o;
		}
	}
	
	public class myCheck
	{
		public Check check;
		public Waiter waiter;
		public checkState state;
		public myCheck(Waiter w, checkState cs, Check c)
		{
			check = c;
			waiter = w;
			state = cs;
		}
	}

	public void setCook(CookAgent cook) {
		this.cook = cook;
	}
}
