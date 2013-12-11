package guehochoi.test;

import java.util.concurrent.Semaphore;

import agents.ProducerConsumerMonitor;
import guehochoi.gui.CookGui;
import guehochoi.gui.KitchenGui;
import guehochoi.gui.RestaurantGui;
import guehochoi.restaurant.CookAgent;
import guehochoi.restaurant.HostAgent;
import guehochoi.restaurant.WaiterAgent;
import guehochoi.restaurant.WaiterProducer;
import guehochoi.restaurant.CookAgent.OrderState;
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
		
		KitchenGui kitchenGui = new KitchenGui();
		CookGui cookGui = new CookGui(cook, kitchenGui);
        cook.setGui(cookGui);
		
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
		assertTrue("cook shouldn't have any order", cook.orders.isEmpty()) ;
		monitor.setSubscriber(cook);
		monitor.insert(new CookAgent.Order(waiter1, "Chicken", 1, OrderState.pending));
		
		cook.pickAndExecuteAnAction();
		
		
		assertTrue("cook should have one order", cook.orders.size()==1);
		
		
		
		
//		java.util.Timer t = new java.util.Timer();
//        final Semaphore sem = new Semaphore(0, true);
//        t.schedule(new java.util.TimerTask() {
//			@Override
//			public void run() {
//				sem.release();
//			}
//        }, 6000);
//        try {
//			sem.acquire();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
        
	}
	
}
