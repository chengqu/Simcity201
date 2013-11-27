package simcity201.test;

import agents.BusAgent;
import agents.CarAgent;
import agents.Person;
import agents.StopAgent;
import simcity201.gui.BusGui;
import simcity201.test.mock.MockCar;
import simcity201.test.mock.MockPassenger;
import newMarket.MarketCashierAgent;
import newMarket.MarketCustomerAgent;
import junit.framework.TestCase;

	public class BusTest extends TestCase{
		
		MockPassenger pa;
		Person p;
		CarAgent car;
		private BusAgent bus = new BusAgent("Bank","Bus1Crossing1","Market","Bus1Crossing2","Restaurants1","Bus1Crossing3","Restaurants2","Bus1Crossing4","House","Bus1Crossing5","Terminal1",1);
	    private BusGui busGui = new BusGui(bus,"Terminal1");
	    private BusAgent bus2 = new BusAgent("Rest1","","Rest2","","Bank","","House","Market","Terminal2","","",2);
	    private BusGui busGui2 = new BusGui(bus2,"Terminal2");
	    private StopAgent stop = new StopAgent(bus,bus2);
		
		 public void setUp() throws Exception{
		      super.setUp();    
		      
		 pa= new MockPassenger("testp");
		 p = new Person("testp",false); 
		 car = new CarAgent("Audi");
		      
		   }  
		 
		 public void testOnePassengerBusSenario(){
			 pa.msgGoTo(p, "Market", null,stop );
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Market"));
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's stop "));
			 
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Market"));
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's stop "));
			 
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Market"));
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's stop "));
			 assertFalse("Scheduler should return true, ",car.pickAndExecuteAnAction());
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Market"));
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's stop "));
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Market"));
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's stop "));
			 
			 
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Market"));
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's stop "));
			 
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Market"));
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's stop "));
			 assertFalse("Scheduler should return true, ",car.pickAndExecuteAnAction());
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Market"));
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's stop "));
			 
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Market"));
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's stop "));
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Market"));
			 assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's stop "));
		 }
}
