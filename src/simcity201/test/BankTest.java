package simcity201.test;

import java.awt.event.ActionEvent;
import java.util.concurrent.Semaphore;

import agents.Account;
import agents.Person;
import agents.Role;
import agents.Role.roles;
import simcity201.gui.Bank;
import junit.framework.TestCase;

public class BankTest extends TestCase {

	Person customer;
	Person teller;
	
	Bank bank;
	public void setUp() throws Exception {
		super.setUp();
		customer = new Person("customer", true);
		teller = new Person("teller", true);
		
		bank = new Bank();
	}
	
	public void testAddingByRole() {
		/*This test is before implementing verification of work location */
		assertTrue("customers should be empty", bank.customers.isEmpty());
		assertTrue("tellers should be empty", bank.tellers.isEmpty());
		
		/* because teller has no appropriate role, shouldn't be added */
		bank.addWorker(teller);
		assertTrue("bank should have logged \"should not get here\", but it didn't. Instead, "
				+ bank.log.getLastLoggedEvent().toString(), bank.log.containsString("should not get here"));
		assertFalse("bank should not have logged \"teller added\", but it did. Instead, "
				+ bank.log.getLastLoggedEvent().toString(), bank.log.containsString("teller added"));
		assertEquals("tellers should be now containing one", bank.tellers.size(), 0);
		
		assertEquals("bank should have 0 workers", bank.workers.size(), 0);
		teller.addRole(roles.TellerAtChaseBank, "Bank");
		bank.addWorker(teller);
		assertEquals("tellers should be now containing one", bank.tellers.size(), 1);
		assertTrue("bank should have logged \"teller added\", but it didn't. Instead, "
				+ bank.log.getLastLoggedEvent().toString(), bank.log.containsString("teller added"));
		assertEquals("bank should have 1 workers", bank.workers.size(), 1);
		
		assertEquals("securities should be now containing 0", bank.securities.size(), 0);
		Person security = new Person("sec", true);
		security.addRole(roles.SecurityAtChaseBank, "Bank");
		bank.addWorker(security);
		assertTrue("bank should have logged \"security added\", but it didn't. Instead, "
				+ bank.log.getLastLoggedEvent().toString(), bank.log.containsString("security added"));
		//assertEquals("securities should be now containing one", bank.securities.size(), 1);
		//assertEquals("bank should have 2 workers", bank.workers.size(), 2);
	}
	
	public void testTellerWorking() {
		/*Scenario detail: teller ONLY leaves after waiting for customer on line */
		assertTrue("customers should be empty", bank.customers.isEmpty());
		assertTrue("tellers should be empty", bank.tellers.isEmpty());
		
		teller.addRole(roles.TellerAtChaseBank, "Bank");
		teller.payCheck = 0;
		bank.addWorker(teller);
        assertEquals("tellers should be now containing one", bank.tellers.size(), 1);
		assertTrue("bank should have logged \"teller added\", but it didn't. Instead, "
				+ bank.log.getLastLoggedEvent().toString(), bank.log.containsString("teller added"));
		assertEquals("bank should have 1 workers", bank.workers.size(), 1);
		Person bankTeller2 = new Person("Teller2", true);
		bankTeller2.payCheck = 0;
        bankTeller2.addRole(roles.TellerAtChaseBank, "Bank");
        bank.addWorker(bankTeller2);
        assertEquals("bank should have 2 workers", bank.workers.size(), 2);
        
        assertEquals("internalClock should be 0", bank.internalClock, 0);
        assertEquals("Bank should have 2 workers", bank.workers.size(), 2);
        for(int i=0; i < 16; i++) { // fire internal tick 16 times so that the worker can go home
	        bank.actionPerformed(new ActionEvent (bank.wageTimer, ActionEvent.ACTION_PERFORMED , "InternalTick"));
        }
        assertEquals("internalClock should be 2", bank.internalClock, 32);
        
        /* below test is not very appropriate because shared data makes hard to predict timing */
        java.util.Timer t = new java.util.Timer();
        final Semaphore sem = new Semaphore(0, true);
        t.schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				sem.release();
			}
        }, 13000);
        try {
			sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        assertEquals("Bank should have 1 workers", bank.workers.size(), 1);
        assertTrue("bank should have logged \"leaving work\", but it didn't. Instead, "
				+ bank.log.getLastLoggedEvent().toString(), bank.log.containsString("leaving work"));
        System.out.println("who is still working :" + bank.workers.get(0).getPerson().getName());
        assertTrue("teller should have more than 0 paycheck now ", teller.payCheck >0 );
        
	}
	
	
}
	
