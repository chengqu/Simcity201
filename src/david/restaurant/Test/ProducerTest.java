package david.restaurant.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import david.restaurant.CashierAgent;
import david.restaurant.CookAgent;
import david.restaurant.Order;
import david.restaurant.WaiterAgent;
import david.restaurant.WaiterProducer;
import david.restaurant.Interfaces.Market;
import david.restaurant.Test.Mock.MockCook;
import david.restaurant.Test.Mock.MockCustomer;
import david.restaurant.Test.Mock.MockMarket;
import david.restaurant.Test.Mock.MockWaiter;
import david.restaurant.gui.CookGui;
import junit.framework.TestCase;
import agents.MonitorSubscriber;
import agents.ProducerConsumerMonitor;

public class ProducerTest extends TestCase{
	CookAgent cook;
	CashierAgent cashier;
    List<Market> markets;
    MockWaiter waiter;
    List<MockWaiter> waiters;
    WaiterProducer producer;
    WaiterAgent wAgent;
    MockCustomer customer;
    List<MockCustomer> customers;
    ProducerConsumerMonitor<CookAgent.myOrder> monitor;
	MockWaiter mockWaiter;
    CookGui g;
	public void setUp() throws Exception
    {
    	super.setUp();
    	mockWaiter = new MockWaiter("mockWaiter");
    	monitor = 
        		new ProducerConsumerMonitor<CookAgent.myOrder>(30);
    	markets = Collections.synchronizedList(new ArrayList<Market>());
    	waiters = Collections.synchronizedList(new ArrayList<MockWaiter>());
    	customers = Collections.synchronizedList(new ArrayList<MockCustomer>());
    	markets.add(new MockMarket("m1"));
    	markets.add(new MockMarket("m2"));
    	//cashier = new CashierAgent(markets);
    	waiter = new MockWaiter("Joe");
    	customer = new MockCustomer("David");
    	cook = new CookAgent(markets, cashier, monitor, null);
    	//cashier.startThread();
    	System.out.println("In Setup");
    	customers.add(new MockCustomer("c1"));
    	customers.add(new MockCustomer("c2"));
    	customers.add(new MockCustomer("c3"));
    	wAgent = new WaiterAgent(null, "Joe", cashier, null, null);
    	producer = new WaiterProducer(null, "Joep", cashier, null, monitor, null, true);
    	cook = new CookAgent(markets, cashier, monitor, null);
    	g = new CookGui(cook);
    	cook.setGui(g);
    }
	
	public void testInsert()
	{
		//initialization tests make sure everything is correct. not much to do
		assertEquals("Empty orders list", 0, producer.orders.size());
		assertEquals("Monitor shouldnt have anything", 0, monitor.list.size());
		
		
		WaiterProducer.myOrder o = new WaiterProducer.myOrder(new Order(0, "Steak"), WaiterProducer.OrderState.pending);
		producer.orders.add(o);
		System.out.println("DBefore pick");

		producer.uninterruptibleMove.release();
		producer.orderSequenceSem.release();
		producer.pickAndExecuteAnAction();
		assertEquals("Monitor should have 1 thing", 1, monitor.list.size());
		
		//post test checks
		assertEquals("Should still have 1 order", 1, producer.orders.size());
		assertEquals("state should be at cook", WaiterProducer.OrderState.atCook, producer.orders.get(0).orderState);
	}
	
	public void testExtract()
	{
		//iniliaziation tests
		assertEquals("Cook should have 0 order", 0, cook.orders.size());
		assertEquals("Monitor shouldnt have anything", 0, monitor.list.size());
		monitor.insert(new CookAgent.myOrder(mockWaiter, new Order(1, "Steak"), CookAgent.OrderState.pending));
		assertEquals("Monitor should have 1 order", 1, monitor.list.size());
		
		cook.pickAndExecuteAnAction();
		
		
		assertEquals("MOnitor shouldnt have anything", 0, monitor.list.size());
		assertEquals("Should be cooking", cook.orders.get(0).orderState, CookAgent.OrderState.cooking);
	}
}
