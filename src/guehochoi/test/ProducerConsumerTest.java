package guehochoi.test;

import agents.ProducerConsumerMonitor;
import guehochoi.gui.RestaurantGui;
import guehochoi.restaurant.CookAgent;
import guehochoi.restaurant.HostAgent;
import guehochoi.restaurant.WaiterAgent;
import guehochoi.restaurant.WaiterProducer;
import guehochoi.test.mock.MockCustomer;
import guehochoi.test.mock.MockWaiter;
import junit.framework.TestCase;

public class ProducerConsumerTest extends TestCase  {

	WaiterAgent regWaiter;
	WaiterProducer producerWaiter;
	RestaurantGui restGui;
	HostAgent host;
	CookAgent cook;
	MockWaiter waiter1,waiter2,waiter3;
	
	MockCustomer customer1, customer2, customer3, customer4, customer5, customer6;
	
	ProducerConsumerMonitor<CookAgent.Order> monitor;
	public void setUp() throws Exception{
		super.setUp();
		
		restGui = new RestaurantGui(); 
		regWaiter = new WaiterAgent("regular waiter");
		producerWaiter = new WaiterProducer("producer waiter", restGui.restPanel.monitor);
//		
//		host = restGui.restPanel.host;
//		cook = restGui.restPanel.cook;
//
		monitor = new ProducerConsumerMonitor<CookAgent.Order>(30);
		cook = new CookAgent("cook", monitor);
		cook.state = CookAgent.AgentState.opened;
		
		customer1 = new MockCustomer("customer1");
		customer2 = new MockCustomer("customer2");
		customer3 = new MockCustomer("customer3");
		customer4 = new MockCustomer("customer4");
		customer5 = new MockCustomer("customer5");
		customer6 = new MockCustomer("customer6");
		
	}
	
	public void testProducerConsumerWaiter() {
		
	}
	
	public void testPrdocuerConsumerCook() {
		
	}
	
}
