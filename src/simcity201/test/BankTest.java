package simcity201.test;

import agents.Person;
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
		
		/* because teller has no appropriate role*/
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
}
	
