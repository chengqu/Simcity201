package simcity201.test;

import agents.CarAgent;
import agents.Person;
import simcity201.test.mock.MockCar;
import simcity201.test.mock.MockPassenger;
import junit.framework.TestCase;

public class CarTest extends TestCase{
	MockPassenger pa;
	MockCar car;
	Person p;
	CarAgent cara;
	 public void setUp() throws Exception{
	      super.setUp();    
	      
	 pa= new MockPassenger("testp");
	 p = new Person("testp",false); 
	 car = new MockCar("Audi");
	 cara = new CarAgent("Audi");
	   }  
	 
	 public void testOnePassengerDrivingSenario(){
		 pa.msgGoTo(p, "Bank", null, null);
			assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Bank"));
			
			
			assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Bank"));
			
			assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Bank"));
			assertFalse("Scheduler should return true, ",cara.pickAndExecuteAnAction());
			assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Bank"));
			assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Bank"));
			assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Bank"));
			assertFalse("Scheduler should return true, ",cara.pickAndExecuteAnAction());
			
			assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Bank"));
			
			assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Bank"));
			assertFalse("Scheduler should return true, ",cara.pickAndExecuteAnAction());
			assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Bank"));
			assertTrue("Passenger should have received the dest. It didn't.", pa.log.containsString("Received Person's destination Bank"));
	 }
	 
}
