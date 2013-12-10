package House.test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import agents.Person;








import agents.Task;
import House.agents.HousePerson;
import House.agents.HousePerson.StateHouse;
import House.gui.HouseGui;
import House.gui.HousePanelGui;
import House.gui.HousePersonPanel;
import House.test.mock.MockPerson;
import junit.framework.TestCase;



/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class Housetest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	HousePerson person;
	HouseGui gui;
	HousePersonPanel panel;
	HousePanelGui panelgui;
	Timer timer;
	boolean timeOut;
	//MockPerson p;
	Person p;
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		p = new Person("joe", true);
		person = new HousePerson("joe",100);
		panelgui = new HousePanelGui();
		gui = new HouseGui(person,panelgui);
		panel = new HousePersonPanel(panelgui);
		person.panel = panel;
		person.p = p;


		person.gui = gui;
		timeOut = false;
		timer = new Timer();
		//     person = new HousePerson("joe");
		//p = new Person("jos");
	}        
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the  bill of 5.99 and get $1 change back.
	 */
	public void teststorehome(){

		Task t = new Task(Task.Objective.house, panelgui.name);

		p.currentTask = t;
		p.currentTask.sTasks.clear();
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.nothing);                
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
				+ person.log.toString(), 0, person.log.size());		

		person.msgstoreGroceries();
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.store); 
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("store"));

		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.nothing); 
		assertEquals("House should have 0 bills in it. It doesn't.",person.panel.map2.get("Steak"), 2.0);
		assertEquals("House should have 0 bills in it. It doesn't.",p.groceries.size(), 0);
		assertEquals("House should have 0 bills in it. It doesn't.",person.gui.isPresent(), false);
		assertEquals("House should have 0 bills in it. It doesn't.",panel.groceries.size(), 0);
	}

	public void testPaybills(){

		Task t = new Task(Task.Objective.house, panelgui.name);

		p.currentTask = t;
		p.money = 30;
		p.currentTask.sTasks.clear();
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.nothing);                
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
				+ person.log.toString(), 0, person.log.size());		

		person.msgPayBills();
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.ReadytoPaybill); 
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("bills"));

		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.openlaptop);
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.nothing); 
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}

		},4000);

		while(true)
		{
			if(p.money == 10 && p.houseBillsToPay == 0){
				break;
			}

		}

		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.nothing); 
		assertEquals("House should have 0 bills in it. It doesn't.",gui.isPresent(), false);



	}


	public void testeathome(){
		Task t = new Task(Task.Objective.house, panelgui.name);

		p.currentTask = t;
		p.currentTask.sTasks.clear();
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.nothing);                
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
				+ person.log.toString(), 0, person.log.size());


		person.msgIameatingathome();
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.hungry); 
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("eat"));

		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.readytocook);

		/*
    	timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}

    	},4000);
		 */
		while(true)
		{
			if(person.s.equals(StateHouse.cooked) == true){
				break;
			}

		}
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.cooked);
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.eating);

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}

		},4000);

		while(true)
		{
			if(person.s.equals(StateHouse.full) == true){
				break;
			}

		}
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.nothing);
		assertEquals("House should have 0 bills in it. It doesn't.",0, p.hungerLevel);
		assertEquals("House should have 0 bills in it. It doesn't.",gui.isPresent(), false);
		assertFalse("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		//assertFalse("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
		//        person.pickAndExecuteAnAction());*/
		//assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
		//        person.pickAndExecuteAnAction());
		//assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.eating);

		//assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.cooking); 

	}

	public void testsleeptathome(){
		Task t = new Task(Task.Objective.house, panelgui.name);

		p.currentTask = t;
		p.currentTask.sTasks.clear();
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.nothing);                
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
				+ person.log.toString(), 0, person.log.size());


		person.msgRestathome();
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.rest); 
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("sleep"));

		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		//assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.nothing);


		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}

		},4000);

		while(true)
		{
			if(person.s.equals(StateHouse.nothing) == true){
				break;
			}

		}
		assertEquals("House should have 0 bills in it. It doesn't.",gui.isPresent(), false);
		assertFalse("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		//assertFalse("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
		//        person.pickAndExecuteAnAction());*/
		//assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
		//        person.pickAndExecuteAnAction());
		//assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.eating);

		//assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.cooking); 

	}

	public void teststorehometheneatathome(){

		Task t = new Task(Task.Objective.house, panelgui.name);
		t.sTasks.add(Task.specificTask.eatAtHome);

		p.currentTask = t;

		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.nothing);                
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
				+ person.log.toString(), 0, person.log.size());		

		person.msgstoreGroceries();
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.store); 
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("store"));

		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		//assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.nothing); 
		assertEquals("House should have 0 bills in it. It doesn't.",person.panel.map2.get("Steak"), 2.0);
		assertEquals("House should have 0 bills in it. It doesn't.",p.groceries.size(), 0);
		assertEquals("House should have 0 bills in it. It doesn't.",panel.groceries.size(), 0);
		assertEquals("House should have 0 bills in it. It doesn't.",p.currentTask.sTasks.size(), 0);





		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.hungry); 
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("eat"));

		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.readytocook);

		/*
    	timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}

    	},4000);
		 */
		while(true)
		{
			if(person.s.equals(StateHouse.cooked) == true){
				break;
			}

		}
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.cooked);
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.eating);

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}

		},4000);

		while(true)
		{
			if(person.s.equals(StateHouse.full) == true){
				break;
			}

		}
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());
		assertEquals("House should have 0 bills in it. It doesn't.",person.s, StateHouse.nothing);
		assertEquals("House should have 0 bills in it. It doesn't.",0, p.hungerLevel);
		assertEquals("House should have 0 bills in it. It doesn't.",gui.isPresent(), false);
		assertFalse("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				person.pickAndExecuteAnAction());

	}


}