package david.restaurant;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.Agent;
import agent.StringUtil;
import agents.Person;
import agents.ProducerConsumerMonitor;
import agents.Worker;
import david.restaurant.Interfaces.Waiter;
import david.restaurant.gui.Gui;
import david.restaurant.gui.RestaurantPanel;
import david.restaurant.gui.WaiterGui;

public class WaiterProducer extends Agent implements Waiter, Worker{
	//members
		enum cState {waitingForTable, notReadyToOrder, waitingToOrder,
					 waitingForFood, eating, leaving };
					
		enum OrderState {pending, atCook, atCustomer, done, cooked, notAvailable};			 
		private List<myCustomer> customers = new ArrayList<myCustomer>();
		private List<myOrder> orders = new ArrayList<myOrder>();
		private List<myCheck> checks = new ArrayList<myCheck>();
		
		boolean timeToLeave = false;
		
		public void print_()
		{
			print(Integer.toString(customers.size() + orders.size() + checks.size()));
		}
		
		public WaiterGui gui = null;
		private HostAgent host;
		private CookAgent cook;

		private Object customerLock = new Object();
		private Object orderLock = new Object();
		
		private boolean breakChange = false;
		enum bState {onBreak, offBreak, doneBreak};
		private bState breakState = bState.offBreak;
		
		private CashierAgent cashier;
		
		private Semaphore uninterruptibleMove = new Semaphore(0);
		private Semaphore orderSequenceSem = new Semaphore(0);
		
		RestaurantPanel rp;
		
		String name;
		
		private ProducerConsumerMonitor<CookAgent.myOrder> monitor;
		
		public WaiterProducer(HostAgent h, String n, CashierAgent c, RestaurantPanel rp_,
				ProducerConsumerMonitor<CookAgent.myOrder> monitor)
		{
			this.monitor = monitor;
			host = h;
			if(n == null)
			{
				name = StringUtil.shortName(this);
			}
			else
			{
				name = n;
			}
			cashier = c;
			rp = rp_;
		}
		
		public void setGui(WaiterGui g)
		{
			gui = g;
		}
		
		public void setCook(CookAgent c)
		{
			cook = c;
		}
		
		public void msgDoneShift()
		{
			timeToLeave = true;
		}
		
		//messages from customer, host, and cook. can call stateChanged
		public void msgPleaseSitCustomer(CustomerAgent c, int t)
		{
			/*try {
				CustomerMutex.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			synchronized(customerLock)
			{
				customers.add(new myCustomer(c, t, cState.waitingForTable, new Menu()));
			}
			//CustomerMutex.release();
			//print("msgPleaseSitcustomer");
			stateChanged();
		}
		
		public void msgImReadyToOrder(CustomerAgent c)
		{
			myCustomer mc = null;
			for(myCustomer temp:customers)
			{
				if(temp.customer == c)
				{
					mc = temp;
					break;
				}
			}
			if(mc != null)
			{
				mc.state = cState.waitingToOrder;
			}
			//print("msgImReadyToOrder");
			stateChanged();
		}
		
		public void msgHereIsMyChoice(CustomerAgent c, String choice)
		{
			//release a semaphore in this function so that the
			//action can continue
			myCustomer mc = null;
			for(myCustomer temp:customers)
			{
				if(temp.customer == c)
				{
					mc = temp;
					break;
				}
			}
			if(mc != null)
			{
				mc.state = cState.waitingForFood;
				mc.choice = choice;
				orders.add(new myOrder(new Order(mc.table, choice), OrderState.pending));
			}
			orderSequenceSem.release();
			stateChanged();
		}
		
		public void msgDoneEatingAndLeaving(CustomerAgent c)
		{
			myCustomer mc = null;
			for(myCustomer temp:customers)
			{
				if(temp.customer == c)
				{
					mc = temp;
					break;
				}
			}
			if(mc != null)
			{
				mc.state = cState.leaving;
			}
			//print("msgDoneEatingAndLeaving");
			stateChanged();
		}
		
		public void msgOrderIsReady(Order o)
		{
			//System.out.println("inOrderIsReadyMsg");
			/*try {
				OrderMutex.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			synchronized(orderLock)
			{
				for(myOrder order: orders)
				{
					if(order.order.table == o.table || order.order == o)
					{
						//print("DoneinOrderIsReadyMsg");
						order.orderState = OrderState.cooked;
						break;
					}
				}
			}
			//OrderMutex.release();
			stateChanged();
		}
		
		//gui messages. can't call stateChanged 
		public void msgDoneSeating()
		{
			//print("msgDoneSeating");
			uninterruptibleMove.release();
		}
		
		public void msgAtCashier()
		{
			uninterruptibleMove.release();
		}
		
		public void msgCanStartSeating()
		{
			//print("msgCanStartSeating");
			uninterruptibleMove.release();
		}
		
		public void msgCanTakeOrder()
		{
			//print("msgCanTakeOrder");
			orderSequenceSem.release();
		}
		
		public void msgAtCook()
		{
			//print("msgAtCook");
			uninterruptibleMove.release();
		}
		
		public void msgGoOnBreak()
		{
			breakState = bState.offBreak;
			breakChange = true;
			DoWantBreakMessage();
			System.out.println("msgGoOnBreak");
			stateChanged();
		}
		
		public void msgStopBreak()
		{
			System.out.println("msgStopBreak");
			if(breakState == bState.onBreak)
			{
				breakChange = true;
				breakState = bState.doneBreak;
				DoOffBreakMessage();
			}
			stateChanged();
		}
		
		public void msgOkBreak()
		{
			System.out.println("msgOkBreak");
			breakState = bState.onBreak;
			stateChanged();
		}
		
		public void msgNoBreak()
		{
			rp.noBreak(this);
			System.out.println("msgNoBreak");
			breakState = bState.offBreak;
			stateChanged();
		}
		
		public void msgNotAvailable(Order o)
		{
			for(myOrder order: orders)
			{
				if(order.order.table == o.table)
				{
					order.orderState = OrderState.notAvailable;
					break;
				}
			}
			stateChanged();
		}
		
		public void msgHereIsCheck(Check c)
		{
			synchronized(checks)
			{
				if(c != null)
					print("hereIsCheck");
				checks.add(new myCheck(c));
				stateChanged();
			}
		}
		
		//scheduler
		public boolean pickAndExecuteAnAction() {
			
			try
			{
				myCustomer tempCustomer;
				myOrder tempOrder;
				//print("sched: running " + customers.size() + "  ");
				if((tempCustomer = doesExistCust(cState.leaving)) != null)
				{
					DoFreeTable(tempCustomer);
					return true;
				}
				
				else if((tempOrder = doesExistOrder(OrderState.cooked)) != null)
				{
					DoTakeOrderToCustomer(tempOrder);
					return true;
				}
				
				else if(checks.isEmpty() == false)
				{
					DoProcessCheck(checks.get(0));
					return true;
				}
				
				else if((tempCustomer = doesExistCust(cState.waitingForTable)) != null)
				{
					DoSeatTable(tempCustomer);
					return true;
				}
				
				else if((tempCustomer = doesExistCust(cState.waitingToOrder)) != null)
				{
					DoTakeOrder(tempCustomer);
					return true;
				}
				
				else if((tempOrder = doesExistOrder(OrderState.notAvailable)) != null)
				{
					DoReorderMessage(tempOrder);
					return true;
				}
				
				else if((tempOrder = doesExistOrder(OrderState.pending)) != null)
				{
					DoSendToCook(tempOrder);
					return true;
				}
				gui.DoGoToBreakRoom();
				return false;
			}
			catch(ConcurrentModificationException e)
			{
				print("ConcurrentModificationException caught");
				return false;
			}
		}
		
		private myCustomer doesExistCust(cState s)
		{
			myCustomer mc = null;
			
			/*try {
				CustomerMutex.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			synchronized(customerLock)
			{
				if(customers.isEmpty() == false)
				{
					for(myCustomer temp: customers)
					{
						if(temp.state == s)
						{
							mc = temp;
							break;
						}
					}
				}
			}
			//CustomerMutex.release();
			return mc;
		}
		
		private myOrder doesExistOrder(OrderState o)
		{
			myOrder order = null;
			
			/*try {
				OrderMutex.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			synchronized(orderLock)
			{
				if(orders.isEmpty() == false)
				{
					for(myOrder temp: orders)
					{
						if(temp.orderState == o)
						{
							order = temp;
							break;
						}
					}
				}
			}
			//OrderMutex.release();
			return order;
		}
		
		//actions
		void DoSendToCook(myOrder tempOrder)
		{	
			gui.DoGoToCook();
			try {
				uninterruptibleMove.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			synchronized(orderLock)
			{
				tempOrder.orderState = OrderState.atCook;
			}
			//cook.msgHereIsAnOrder(this, tempOrder.order);
			synchronized(orderLock)
			{
				CookAgent.myOrder o = new CookAgent.myOrder(this, tempOrder.order, CookAgent.OrderState.pending);
				if(monitor.insert(o))
				{
					tempOrder.orderState = OrderState.atCook;
				}
			}
		}
		
		void DoTakeOrderToCustomer(myOrder tempOrder)
		{
			gui.DoGoToCook();
			try {
				uninterruptibleMove.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			gui.DoGoToCustomer(tempOrder.order.table);
			
			for(myCustomer customer: customers)
			{
				if(customer.table == tempOrder.order.table)
				{
					gui.DoDrawFoodChoice(Menu.getAbbreviation(tempOrder.order.choice));
					break;
				}
			}
			try {
				orderSequenceSem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			myCustomer mc = null;
			for(myCustomer temp:customers)
			{
				if(temp.table == tempOrder.order.table)
				{
					System.out.println("CTable: " + temp.table + "  OTable: " + tempOrder.order.table);
					mc = temp;
					break;
				}
			}
			if(mc != null)
			{
				/*try {
					OrderMutex.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
				synchronized(orderLock)
				{
					orders.remove(tempOrder);
				}
				//OrderMutex.release();
				mc.customer.HereIsYourFood();
			}
			//do animation
			cashier.msgProcessOrder(this, mc.customer, tempOrder.order.choice);
		}
		
		void DoTakeOrder(myCustomer c)
		{
			//print("inDoTakeOrder helper");
			gui.DoGoToCustomer(c.table);
			try {
				orderSequenceSem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			c.customer.WhatWouldYouLike(this);
			try {
				orderSequenceSem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			c.state = cState.waitingForFood;
		}
		
		void DoSeatTable(myCustomer c)
		{
			gui.DoGetCustomer(c.customer, c.table);
			try{
				uninterruptibleMove.acquire();
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			Menu temp = new Menu();
			temp.items.clear();
			for(Food f: c.menu.items)
			{
				temp.items.add(Food.copy(f));
			}
			c.menu = temp;
			c.customer.FollowMeToTable(this, new Menu());
			gui.DoBringToTable(c.customer, c.table);
			c.state = cState.notReadyToOrder;
			//System.out.println("inDoSeatTable");
			try {
				uninterruptibleMove.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		void DoFreeTable(myCustomer c)
		{
			host.msgTableIsFree(c.table, this, c.customer);
			synchronized(customerLock)
			{
				customers.remove(c);
			}
		}
		
		private void DoWantBreakMessage()
		{
			breakChange = false;
			breakState = bState.offBreak;
			host.msgWantToGoOnBreak(this);
		}
		
		private void DoOffBreakMessage()
		{
			breakChange = false;
			breakState = bState.offBreak;
			host.msgOffBreak(this, customers.size());
		}
		
		private void DoReorderMessage(myOrder o)
		{
			myCustomer c = null;
			for(myCustomer temp: customers)
			{
				if(temp.table == o.order.table)
				{
					c = temp;
					break;
				}
			}
			if(c != null)
			{
				orders.remove(o);
				Menu m = new Menu();
				m.items.clear();
				
				for(Food f: c.menu.items)
				{
					m.items.add(Food.copy(f));
				}
				
				Food temp = null;
				for(Food item: m.items)
				{
					if(o.order.choice.equalsIgnoreCase(item.name))
					{
						temp = item;
					}
				}
				if(temp != null)
				{
					m.items.remove(temp);
				}
				
				print(Integer.toString(m.items.size()));
				
				//do go to customer animation
				gui.DoGoToCustomer(c.table);
				try {
					orderSequenceSem.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(m.items.size() > 0)
				{
					Menu m_ = new Menu();
					m_.items.clear();
					for(Food f: m.items)
					{
						m_.items.add(Food.copy(f));
					}
					c.menu = m_;
					c.customer.msgReorder(m);
				}
				else
				{
					Menu m_ = new Menu();
					m_.items.clear();
					for(Food f: m.items)
					{
						m_.items.add(Food.copy(f));
					}
					c.menu = m_;
					c.customer.msgNoFood();
				}
			}
		}
		
		void DoProcessCheck(myCheck ch)
		{
			synchronized(checks)
			{
				if(ch.received == false)
				{
					gui.DoGoToCashier();
					try {
						uninterruptibleMove.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ch.received = true;
				}
				else 
				{
					for(myCustomer c: customers)
					{
						if(c.customer == ch.check.customer)
						{
							gui.DoGoToCustomer(c.table);
							try {
								orderSequenceSem.acquire();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					ch.check.customer.msgHereIsCheck(ch.check);
					checks.remove(ch);
				}
			}
		}
		
		//helpers
		public Gui getGui()
		{
			return gui;
		}
		
		public String getName()
		{
			return name;
		}
		
		private class myCustomer{
			public CustomerAgent customer;
			public int table;
			public cState state;
			public String choice;
			public Menu menu;
			
			public myCustomer(CustomerAgent c, int t, cState s, Menu m)
			{
				customer = c;
				table = t;
				state = s;
				menu = m;
			}
		}
		
		private class myOrder
		{
			Order order;
			OrderState orderState;
			public myOrder(Order o, OrderState os)
			{
				order = o;
				orderState = os;
			}
		}
		
		private class myCheck
		{
			public Check check;
			public boolean received;
			public myCheck(Check c)
			{
				check = c;
				received = false;
			}
		}

		int timeIn = 0;
		Person self =null;
		public boolean isWorking;
		public Person p;
		
		@Override
		public void setTimeIn(int timeIn) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getTimeIn() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void goHome() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Person getPerson() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void msgLeave() {
			// TODO Auto-generated method stub
			
		}
}
