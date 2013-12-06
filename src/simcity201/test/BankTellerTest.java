package simcity201.test;

import simcity201.gui.Bank;
import simcity201.gui.BankMap;
//import simcity201.test.mock.MockBankATM;
import simcity201.gui.BankTellerGui;
import simcity201.test.mock.MockBankCustomer;
import agents.Account;
//import simcity201.test.mock.MockBankSecurity;
import agents.BankTellerAgent;
import agents.Person;
import agents.Role;
import agents.Role.roles;
import junit.framework.TestCase;

public class BankTellerTest extends TestCase {
	
	BankTellerAgent teller;
	MockBankCustomer customer;
	//MockBankATM atm;
	//MockBankSecurity security;
	
	Bank bank;
	BankMap map;
	
	public void setUp() throws Exception {
		super.setUp();
		bank = new Bank(); 
		map = bank.getBankMap();
		
		teller = new BankTellerAgent("Teller");
		BankTellerGui g = new BankTellerGui(teller, map);
		teller.setGui(g);
		teller.setBank(bank);
		teller.setDB(bank.getDatabase());
		
		
		customer = new MockBankCustomer("MockCustomer");
		customer.teller = teller;
	}
	
	public void testNormativeCreatingAccountScenario() {
		assertEquals("Teller should have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should have 0 services. It doesn't.", teller.services.size(), 0);
		
		assertFalse("Scheduler should return false", teller.pickAndExecuteAnAction());
		
		assertEquals("Teller should still have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should still have 0 services. It doesn't.", teller.services.size(), 0);
		
		//receives message from bank
		Person worker = new Person("tellerWorker", true);
		assertEquals("teller's should have person", teller.self, null);
		teller.youAreAtWork(worker);
		assertEquals("teller's should have person", teller.self, worker);
		assertEquals("Teller should now have 1 services. It doesn't.", teller.services.size(), 1);
		teller.services.clear(); // you cannot test bank's shared data, go to next step 
		
		assertFalse("Scheduler should return false, ",teller.pickAndExecuteAnAction());
		
		//receives message from customer
		teller.howdy(customer);
		
		assertEquals("Teller should still have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should now have 1 services. It doesn't.", teller.services.size(), 1);
		
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertTrue("customer should have logged \"Received howMayIHelpYou\"", customer.log.containsString("Received howMayIHelpYou"));
		Person customerReflection = new Person("customer", true);
		teller.iNeedAccount(customer, customerReflection.getName(), customerReflection.address, customerReflection.ssn, Account.AccountType.Checking);
		assertTrue("Teller should have logged \"Received iNeedAccount \"" + customer, teller.log.containsString("Received iNeedAccount " + customer.getName()));
		
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertTrue("customer should have logged \"Received hereIsYourAccount \"", customer.log.containsString("Received hereIsYourAccount "));
		
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertTrue("customer should have logged \"Received anythingElse\"", customer.log.containsString("Received anythingElse"));
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		
		teller.noThankYou(customer);
		
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		
		MockBankCustomer stopper = new MockBankCustomer("stopper");
		bank.iAmOnLine(stopper);
		/* Need stopper to stop the shared data 'line' */
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertEquals("Teller should still have 0 services. It doesn't.", teller.services.size(), 0);
		assertTrue("customer should have logged \"Received anythingElse\"", customer.log.containsString("Received anythingElse"));
		
	}
	
	
	public void testNormativeMakingDepositScenario() {
		assertEquals("Teller should have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should have 0 services. It doesn't.", teller.services.size(), 0);
		
		assertFalse("Scheduler should return false", teller.pickAndExecuteAnAction());
		
		assertEquals("Teller should still have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should still have 0 services. It doesn't.", teller.services.size(), 0);
		
		//receives message from bank
		Person worker = new Person("tellerWorker", true);
		assertEquals("teller's should have person", teller.self, null);
		teller.youAreAtWork(worker);
		assertEquals("teller's should have person", teller.self, worker);
		assertEquals("Teller should now have 1 services. It doesn't.", teller.services.size(), 1);
		teller.services.clear(); // you cannot test bank's shared data, go to next step 
		
		assertFalse("Scheduler should return false, ",teller.pickAndExecuteAnAction());
		
		//receives message from customer
		teller.howdy(customer);
		
		assertEquals("Teller should still have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should now have 1 services. It doesn't.", teller.services.size(), 1);
		
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertTrue("customer should have logged \"Received howMayIHelpYou\"", customer.log.containsString("Received howMayIHelpYou"));
		
		Person customerReflection = new Person(customer.getName(), true);
		Account acc = new Account(customer.getName(), "", 123456789, Account.AccountType.Saving);
		bank.getDatabase().insertAccount(acc);
		customerReflection.accounts.add(acc);
		customer.self = customerReflection;
		
		teller.iWantToDeposit(customer, 5000, acc.getAccountNumber());
		
		assertTrue("Teller should have logged \"Received iWantToDeposit \"" + customer.getName(), teller.log.containsString("Received iWantToDeposit " + customer.getName()));
		
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertTrue("customer should have logged \"Received depositTransaction \"", customer.log.containsString("Received depositTransaction " + true));
		
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertTrue("customer should have logged \"Received anythingElse\"", customer.log.containsString("Received anythingElse"));
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		
		teller.noThankYou(customer);
		
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		
		MockBankCustomer stopper = new MockBankCustomer("stopper");
		bank.iAmOnLine(stopper);
		/* Need stopper to stop the shared data 'line' */
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertEquals("Teller should still have 0 services. It doesn't.", teller.services.size(), 0);
		assertTrue("customer should have logged \"Received anythingElse\"", customer.log.containsString("Received anythingElse"));
		
	}
	public void testNormativeWithdrawingMoneyScenario() {
		assertEquals("Teller should have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should have 0 services. It doesn't.", teller.services.size(), 0);
		
		assertFalse("Scheduler should return false", teller.pickAndExecuteAnAction());
		
		assertEquals("Teller should still have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should still have 0 services. It doesn't.", teller.services.size(), 0);
		
		//receives message from bank
		Person worker = new Person("tellerWorker", true);
		assertEquals("teller's should have person", teller.self, null);
		teller.youAreAtWork(worker);
		assertEquals("teller's should have person", teller.self, worker);
		assertEquals("Teller should now have 1 services. It doesn't.", teller.services.size(), 1);
		teller.services.clear(); // you cannot test bank's shared data, go to next step 
		
		assertFalse("Scheduler should return false, ",teller.pickAndExecuteAnAction());
		
		//receives message from customer
		teller.howdy(customer);
		
		assertEquals("Teller should still have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should now have 1 services. It doesn't.", teller.services.size(), 1);
		
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertTrue("customer should have logged \"Received howMayIHelpYou\"", customer.log.containsString("Received howMayIHelpYou"));
		
		Person customerReflection = new Person(customer.getName(), true);
		Account acc = new Account(customer.getName(), "", 123456789, Account.AccountType.Saving);
		bank.getDatabase().insertAccount(acc);
		customerReflection.accounts.add(acc);
		customer.self = customerReflection;
		acc.deposit(10000);
		
		teller.iWantToWithdraw(customer, 5000, acc.getAccountNumber());
		
		assertTrue("Teller should have logged \"Received iWantToWithdraw \"" + customer.getName(), teller.log.containsString("Received iWantToWithdraw " + customer.getName()));
		
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertTrue("customer should have logged \"Received withdrawTransaction \"" + true, customer.log.containsString("Received withdrawTransaction " + true));
		
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertTrue("customer should have logged \"Received anythingElse\"", customer.log.containsString("Received anythingElse"));
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		
		teller.noThankYou(customer);
		
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		
		MockBankCustomer stopper = new MockBankCustomer("stopper");
		bank.iAmOnLine(stopper);
		/* Need stopper to stop the shared data 'line' */
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertEquals("Teller should still have 0 services. It doesn't.", teller.services.size(), 0);
		assertTrue("customer should have logged \"Received anythingElse\"", customer.log.containsString("Received anythingElse"));
	}
	public void testNormativeMakingLoanScenario() {
		assertEquals("Teller should have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should have 0 services. It doesn't.", teller.services.size(), 0);
		
		assertFalse("Scheduler should return false", teller.pickAndExecuteAnAction());
		
		assertEquals("Teller should still have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should still have 0 services. It doesn't.", teller.services.size(), 0);
		
		//receives message from bank
		Person worker = new Person("tellerWorker", true);
		assertEquals("teller's should have person", teller.self, null);
		teller.youAreAtWork(worker);
		assertEquals("teller's should have person", teller.self, worker);
		assertEquals("Teller should now have 1 services. It doesn't.", teller.services.size(), 1);
		teller.services.clear(); // you cannot test bank's shared data, go to next step 
		
		assertFalse("Scheduler should return false, ",teller.pickAndExecuteAnAction());
		
		//receives message from customer
		teller.howdy(customer);
		
		assertEquals("Teller should still have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should now have 1 services. It doesn't.", teller.services.size(), 1);
		
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertTrue("customer should have logged \"Received howMayIHelpYou\"", customer.log.containsString("Received howMayIHelpYou"));
		
		Person customerReflection = new Person(customer.getName(), true);
		Account accS = new Account(customer.getName(), "", 123456789, Account.AccountType.Saving);
		Account accC = new Account(customer.getName(), "", 123456789, Account.AccountType.Checking);
		bank.getDatabase().insertAccount(accS);
		customerReflection.accounts.add(accS);
		bank.getDatabase().insertAccount(accC);
		customerReflection.accounts.add(accC);
		customerReflection.roles.add(new Role(roles.ApartmentOwner, "Apt1"));
		customer.self = customerReflection;
		accS.deposit(10000);
		
		teller.iWantToLoan(customer, 50000, customer.self.roles.get(0));
		
		assertTrue("Teller should have logged \"Received iWantToLoan \"" + customer.getName(), teller.log.containsString("Received iWantToLoan " + customer.getName()));
		
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertTrue("customer should have logged \"Received loanDecision \"" + true, customer.log.containsString("Received loanDecision " + true));
		
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertTrue("customer should have logged \"Received anythingElse\"", customer.log.containsString("Received anythingElse"));
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		
		teller.noThankYou(customer);
		
		assertEquals("Teller should still have 1 services. It doesn't.", teller.services.size(), 1);
		
		MockBankCustomer stopper = new MockBankCustomer("stopper");
		bank.iAmOnLine(stopper);
		/* Need stopper to stop the shared data 'line' */
		assertTrue("Scheduler should return true, ",teller.pickAndExecuteAnAction());
		assertEquals("Teller should still have 0 services. It doesn't.", teller.services.size(), 0);
		assertTrue("customer should have logged \"Received anythingElse\"", customer.log.containsString("Received anythingElse"));
	}

}
