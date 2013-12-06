package simcity201.test;

import java.util.Timer;
import java.util.TimerTask;

import agents.ApartmentBill;
import agents.ApartmentPerson;
import agents.Grocery;
import agents.Person;
import agents.Role;
import agents.Task;
import Buildings.ApartmentComplex;
import Buildings.ApartmentComplex.Apartment;
import Buildings.Building;
import junit.framework.TestCase;

public class ApartmentPersonTest extends TestCase{
	ApartmentComplex complex;
	Timer t;
	public boolean timeOut;
	
	public ApartmentPersonTest() throws Exception
	{
		super.setUp();
		complex = new ApartmentComplex();
		complex.name = "complex";
		complex.type = Building.Type.Apartment;
		
		Person p = new Person("Joey", true);
		complex.addRenterTest(p);
		p = new Person("Lone Wanderer", true);
		complex.addRenterTest(p);
		p = new Person("Three dog", true);
		complex.addOwnerTest(p);
		t = new Timer();
		timeOut = false;
	}
	
	/* Single sTask tests */
	public void testDepositingGroceries()
	{
		ApartmentPerson a = complex.apartments.get(0).person;
		a.p.roles.add(new Role(Role.roles.ApartmentRenter, complex.name));
		Task t = new Task(Task.Objective.house, complex.name);
		t.sTasks.add(Task.specificTask.depositGroceries);
		a.p.currentTask = t;
		
		/* Pre condition testing */
		a.p.groceries.add(new Grocery("Steak", 1));
		assertEquals("Person should have a grocery in him", a.p.groceries.size(), 1);
		assertEquals("Person should have steak", a.p.groceries.get(0).getFood(), "Steak");
		assertEquals("Person should have 1 steak", a.p.groceries.get(0).getAmount(), 1);
		assertEquals("Person doesnt have the correct amount of sTasks", a.p.currentTask.sTasks.size(), 1);
		assertEquals("Person has wrong sTask", a.p.currentTask.sTasks.get(0).toString(),
				Task.specificTask.depositGroceries.toString());
		/*assertEquals("Just started should be true", a.justStarted, true);
		a.pickAndExecuteAnAction();
		assertEquals("Just started should be false", a.justStarted, false);*/
		
		//testing whether or not he actually deposits the groceries
		final ApartmentPersonTest temp = this;
		int oldSizeFridge = a.apartment.Fridge.size();
		this.t.schedule(new TimerTask()
		{
			public void run() {
				if(temp.timeOut ==false)
				{
					temp.timeOut = true;
				}
			}
			
		}, 5000);
		a.pickAndExecuteAnAction();
		while(!temp.timeOut && a.p.groceries.size() > 0)
		{
			
		}
		assertEquals("The groceries should be empty", a.p.groceries.size(), 0);
		assertNotSame("There should now be some amount of food in the fridge", a.apartment.Fridge.size(), oldSizeFridge);
		assertEquals("There should now be some amount of food in the fridge", a.apartment.Fridge.size(), 1);
		/* Post run testing, make sure everything is cleaned up correctly */
		assertEquals("The person shouldn't really have any sTasks",
				a.p.currentTask.sTasks.size(), 0);
	}
	
	public void testEatingFood()
	{
		ApartmentPerson a = complex.apartments.get(0).person;
		a.p.roles.add(new Role(Role.roles.ApartmentRenter, complex.name));
		Task t = new Task(Task.Objective.house, complex.name);
		t.sTasks.add(Task.specificTask.eatAtApartment);
		a.p.currentTask = t;
		a.p.hungerLevel = 50;
		a.apartment.Fridge.add(new Grocery("Steak" , 1));
		
		/* Pre condition testing */
		assertEquals("There should now be some amount of food in the fridge", a.apartment.Fridge.size(), 1);
		assertEquals("Person doesnt have the correct amount of sTasks", a.p.currentTask.sTasks.size(), 1);
		assertEquals("Person doesnt have the correct amount of sTasks", a.p.hungerLevel, 50);
		assertEquals("Person has wrong sTask", a.p.currentTask.sTasks.get(0).toString(),
				Task.specificTask.eatAtApartment.toString());
		/*assertEquals("Just started should be true", a.justStarted, true);
		a.pickAndExecuteAnAction();
		assertEquals("Just started should be false", a.justStarted, false);*/
		assertTrue("HungerLevel should be higher than threshold", a.p.hungerLevel > a.p.hungerThreshold);
		
		//testing whether or not he actually deposits the groceries
		final ApartmentPersonTest temp = this;
		this.t.schedule(new TimerTask()
		{
			public void run() {
				System.out.println("TIMER RAN");
				temp.timeOut = true;
			}
			
		}, 8000);
		a.pickAndExecuteAnAction();
		while(!timeOut && a.p.hungerLevel > 0)
		{
			
		}
		assertEquals("The groceries should be empty", a.p.hungerLevel, 0);
		assertEquals("There should be no food in the fridge", a.apartment.Fridge.size(), 0);
		/* Post run testing, make sure everything is cleaned up correctly */
		assertEquals("The person shouldn't really have any sTasks",
				a.p.currentTask.sTasks.size(), 0);
	}
	
	public void testPayBills()
	{
		ApartmentPerson a = complex.apartments.get(0).person;
		a.p.roles.add(new Role(Role.roles.ApartmentRenter, complex.name));
		Task t = new Task(Task.Objective.house, complex.name);
		t.sTasks.add(Task.specificTask.payBills);
		a.p.currentTask = t;
		a.p.money = 50;
		double previousMoney = a.p.money;
		float billCharge = 10;
		
		a.p.bills.add(new ApartmentBill(billCharge, a, complex.apartments.get(2).person));
		/* precondition */
		assertEquals("Apartment person has a bill to pay", a.p.bills.size(), 1);
		assertEquals("Person has wrong sTask", a.p.currentTask.sTasks.get(0).toString(),
				Task.specificTask.payBills.toString());
		/*assertEquals("Just started should be true", a.justStarted, true);
		a.pickAndExecuteAnAction();
		assertEquals("Just started should be false", a.justStarted, false);*/
		
		//actual test part
		final ApartmentPersonTest temp = this;
		this.t.schedule(new TimerTask()
		{
			public void run() {
				System.out.println("TIMER RAN");
				temp.timeOut = true;
			}
			
		}, 8000);
		a.pickAndExecuteAnAction();
		while(!timeOut && a.p.bills.size() > 0)
		{
			
		}
		assertEquals("The bills should be empty", a.p.bills.size(), 0);
		assertEquals("The person should have less money equal to how much he had before, minus the bill charge",
				a.p.money, previousMoney - billCharge);
		
		/* Post run testing, make sure everything is cleaned up correctly */
		assertEquals("The person shouldn't really have any sTasks",
				a.p.currentTask.sTasks.size(), 0);
	}
	
	public void testSleeping()
	{
		ApartmentPerson a = complex.apartments.get(0).person;
		a.p.roles.add(new Role(Role.roles.ApartmentRenter, complex.name));
		Task t = new Task(Task.Objective.house, complex.name);
		t.sTasks.add(Task.specificTask.sleepAtApartment);
		a.p.currentTask = t;
		/* precondition */
		assertEquals("Person has wrong sTask", a.p.currentTask.sTasks.get(0).toString(),
				Task.specificTask.sleepAtApartment.toString());
		/*assertEquals("Just started should be true", a.justStarted, true);
		a.pickAndExecuteAnAction();
		assertEquals("Just started should be false", a.justStarted, false);*/
		
		final ApartmentPersonTest temp = this;
		this.t.schedule(new TimerTask()
		{
			public void run() {
				System.out.println("TIMER RAN");
				temp.timeOut = true;
			}
			
		}, 8000);
		a.pickAndExecuteAnAction();
		while(!timeOut && !a.log.containsString("Done Sleeping"))
		{
			
		}
		
		//post pick&execute testing
		assertEquals("Should have two logged events in log", a.log.size(), 2);
		assertEquals("Should have a logged event 'Done Sleeping'", a.log.containsString("Done Sleeping"), true);
		assertEquals("Should have a logged event 'Sleeping'", a.log.containsString("Sleeping"), true);
		assertEquals("Should not be sleeping", a.sleeping, false);
		
		/* Post run testing, make sure everything is cleaned up correctly */
		assertEquals("The person shouldn't really have any sTasks",
				a.p.currentTask.sTasks.size(), 0);
	}
	
	public void testSendBills()
	{
		ApartmentPerson a = complex.apartments.get(2).person;
		a.p.roles.add(new Role(Role.roles.ApartmentOwner, complex.name));
		Task t = new Task(Task.Objective.house, complex.name);
		a.p.currentTask = t;
		a.timeToBill = true;
		
		/* pre condition test */
		/* precondition */
		assertEquals("Person shouldnt have any sTasks", a.p.currentTask.sTasks.size(), 0);
		/*assertEquals("Just started should be true", a.justStarted, true);
		a.pickAndExecuteAnAction();
		assertEquals("Just started should be false", a.justStarted, false);*/
		
		final ApartmentPersonTest temp = this;
		this.t.schedule(new TimerTask()
		{
			public void run() {
				System.out.println("TIMER RAN");
				temp.timeOut = true;
			}
			
		}, 8000);
		a.pickAndExecuteAnAction();
		while(!timeOut && !a.log.containsString("Billed People"))
		{
			
		}
		assertEquals("Time to bill should be false", a.timeToBill, false);
		assertEquals("Person didnt get a bill", complex.apartments.get(0).person.p.bills.size(), 1);
		assertEquals("Owner isnt correct", complex.apartments.get(0).person.p.bills.get(0).getOwner(), a);
		assertEquals("Didnt log event", complex.apartments.get(0).person.log.containsString("Got a bill"), true);
		assertEquals("Person didn't get a bill", complex.apartments.get(1).person.p.bills.size(), 1);
		assertEquals("Owner isnt correct", complex.apartments.get(1).person.p.bills.get(0).getOwner(), a);
		assertEquals("Didnt log event", complex.apartments.get(1).person.log.containsString("Got a bill"), true);
	}
	
	public void testGettingEvicted()
	{
		ApartmentPerson a = complex.apartments.get(0).person;
		a.p.roles.add(new Role(Role.roles.ApartmentRenter, complex.name));
		Task t = new Task(Task.Objective.house, complex.name);
		a.p.currentTask = t;
		a.evicted = true;
		
		
		/* pre condition test */
		assertEquals("Person shouldnt have any sTasks", a.p.currentTask.sTasks.size(), 0);
		/*assertEquals("Just started should be true", a.justStarted, true);
		a.pickAndExecuteAnAction();
		assertEquals("Just started should be false", a.justStarted, false);*/
		
		final ApartmentPersonTest temp = this;
		this.t.schedule(new TimerTask()
		{
			public void run() {
				System.out.println("TIMER RAN");
				temp.timeOut = true;
			}
			
		}, 8000);
		a.pickAndExecuteAnAction();
		Apartment apartment = a.apartment;
		while(!timeOut && complex.apartments.contains(apartment))
		{
			
		}
		for(Role r : a.p.roles)
		{
			assertNotSame("He shouldnt have this role", Role.roles.ApartmentRenter, r.getRole());
		}
		assertEquals("Complex shouldn't have the apartment", false, complex.apartments.contains(apartment));
		
		assertEquals("Apartment should be null", null, a.p.apartment);
		assertEquals("Apartment should be null", null, a.apartment);
	}
}
