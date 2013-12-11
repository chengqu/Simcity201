package simcity201.test;

import simcity201.gui.Bank;
import simcity201.gui.BankCustomerGui;
import simcity201.gui.BankMap;
import simcity201.test.mock.MockBankTeller;
import agents.Account;
import agents.BankCustomerAgent;
import agents.Person;
import agents.Role;
import agents.Task;
import agents.Role.roles;
import junit.framework.TestCase;

public class BankCustomerTest extends TestCase {
	
	BankCustomerAgent customer;
	MockBankTeller teller;
	Person p;
	
	Bank bank;
	BankMap map;
	
	public void setUp() throws Exception {
		super.setUp();
		
		bank = new Bank();
		map = bank.getBankMap();
		p = new Person("customer", true);
		customer = new BankCustomerAgent(p.getName());
		BankCustomerGui g = new BankCustomerGui(customer, map, bank.bap);
		customer.setBank(bank);
		customer.setGui(g);
		
		teller = new MockBankTeller("teller");
		
	}
	
	public void testNormativeScenarioCreatingAccount() {
		assertTrue("BankCustomer should not contain any teller now", customer.teller == null);
		assertEquals("BankCustomer should have 0 any tasks", customer.tasks.size(), 0);
		assertEquals("BankCustomer should have same name as person", customer.getName(), p.getName());
		assertEquals("BankCustomer should have same person", customer.self, null);
		
		assertFalse("scheduler should return false", customer.pickAndExecuteAnAction());
		
		assertEquals("BankCustomer should still have 0 any tasks", customer.tasks.size(), 0);
		assertTrue("BankCustomer should still not contain any teller now", customer.teller == null);
		
		//message the customer
		customer.youAreInside(p);
		assertEquals("BankCustomer should have same person", customer.self, p); // now he has pointer to person
		
		assertTrue("customer should have logged \"Received youAreInside \"" + p.getName(), customer.log.containsString("Received youAreInside " + p.getName()));
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		
		assertEquals("customer should have 0 atDest Semaphore", customer.atDest.availablePermits(), 0);
		customer.atDest.release();// give virtual permit for gui so that deadlock doesnt happen
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("customer should have 0 atDest Semaphore", customer.atDest.availablePermits(), 0);
		
		assertEquals("BankCustomer should have 0 tasks", customer.tasks.size(), 0);
		assertEquals("bank should extract the same customer if teller asks for it", bank.whoIsNextOnLine(teller), customer);
		teller.customer = customer; // assume teller is the one who called the above bank.whoIsNext
		customer.nextOnLine(teller);
		assertTrue("customer should have logged \"Received nextOnLine \"" + teller.getName(), customer.log.containsString("Received nextOnLine " + teller.getName()));
		
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		assertEquals("customer should have 0 atDest Semaphore", customer.atDest.availablePermits(), 0);
		customer.atDest.release();// give virtual permit for gui so that deadlock doesnt happen
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		
		assertTrue("teller should have logged \"Received howdy \"" + customer.getName(), teller.log.containsString("Received howdy " + customer.getName()));
		assertEquals("BankCustomer should have 0 tasks", customer.tasks.size(), 0);
		
//		customer.howMayIHelpYou();
		assertTrue("customer should have logged \"Received howMayIHelpYou\"", customer.log.containsString("Received howMayIHelpYou"));
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		
		//
		assertTrue("customer's pointer to person should not have account",p.accounts.isEmpty());
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		assertTrue("teller should have logged \"Received iNeedAccount \"" + customer.getName(), teller.log.containsString("Received iNeedAccount " + customer.getName()));
		
		Account acc = new Account(customer.getName(), p.address, p.ssn, Account.AccountType.Saving);
		bank.getDatabase().insertAccount(acc);
		customer.hereIsYourAccount(acc);
		assertTrue("the pointed person should have no accounts", p.accounts.isEmpty());
		assertTrue("customer should have logged \"Received hereIsYourAccount \"" + acc, customer.log.containsString("Received hereIsYourAccount " + acc));
		
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		
		assertFalse("the pointed person should have account", p.accounts.isEmpty());
		assertEquals("only one acccount", p.accounts.size(), 1);
		assertSame("and that account should be the same", p.accounts.get(0), acc);
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
	
		p.money = p.cashLowThreshold+1; // he won't decide anything else
		p.wantCar = false;
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		//customer.anythingElse();
		
		assertTrue("customer should have logged \"Received anythingElse\"", customer.log.containsString("Received anythingElse"));
		assertEquals("BankCustomer should have 2 tasks", customer.tasks.size(), 2);
		
		customer.atDest.release();
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 0 tasks", customer.tasks.size(), 0);
		assertTrue("teller should have logged \"Received noThankYou \"" + customer.getName(), teller.log.containsString("Received noThankYou " + customer.getName()));
		
		assertFalse("scheduler should return false", customer.pickAndExecuteAnAction());
		
	}
	public void testNormativeScenarioDeposit() {
		assertTrue("BankCustomer should not contain any teller now", customer.teller == null);
		assertEquals("BankCustomer should have 0 any tasks", customer.tasks.size(), 0);
		assertEquals("BankCustomer should have same name as person", customer.getName(), p.getName());
		assertEquals("BankCustomer should have same person", customer.self, null);
		
		assertFalse("scheduler should return false", customer.pickAndExecuteAnAction());
		
		assertEquals("BankCustomer should still have 0 any tasks", customer.tasks.size(), 0);
		assertTrue("BankCustomer should still not contain any teller now", customer.teller == null);
		
		//message the customer
		customer.youAreInside(p);
		assertEquals("BankCustomer should have same person", customer.self, p); // now he has pointer to person
		
		assertTrue("customer should have logged \"Received youAreInside \"" + p.getName(), customer.log.containsString("Received youAreInside " + p.getName()));
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		
		assertEquals("customer should have 0 atDest Semaphore", customer.atDest.availablePermits(), 0);
		customer.atDest.release();// give virtual permit for gui so that deadlock doesnt happen
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("customer should have 0 atDest Semaphore", customer.atDest.availablePermits(), 0);
		
		assertEquals("BankCustomer should have 0 tasks", customer.tasks.size(), 0);
		assertEquals("bank should extract the same customer if teller asks for it", bank.whoIsNextOnLine(teller), customer);
		teller.customer = customer; // assume teller is the one who called the above bank.whoIsNext
		customer.nextOnLine(teller);
		assertTrue("customer should have logged \"Received nextOnLine \"" + teller.getName(), customer.log.containsString("Received nextOnLine " + teller.getName()));
		
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		assertEquals("customer should have 0 atDest Semaphore", customer.atDest.availablePermits(), 0);
		customer.atDest.release();// give virtual permit for gui so that deadlock doesnt happen
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		
		assertTrue("teller should have logged \"Received howdy \"" + customer.getName(), teller.log.containsString("Received howdy " + customer.getName()));
		assertEquals("BankCustomer should have 0 tasks", customer.tasks.size(), 0);
		
//		customer.howMayIHelpYou();
		assertTrue("customer should have logged \"Received howMayIHelpYou\"", customer.log.containsString("Received howMayIHelpYou"));
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		
		// will only decide to deposit
		Account acc = new Account(p.getName(), p.address, p.ssn, Account.AccountType.Saving);
		bank.getDatabase().insertAccount(acc);
		p.accounts.add(acc);
		p.payCheck = 2* p.payCheckThreshold;
		p.wantCar = false;
		p.money = p.cashLowThreshold * 2;
		
		assertFalse("customer's pointer to person should have account",p.accounts.isEmpty());
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		assertTrue("teller should have logged \"Received iWantToDeposit \"" + customer.getName(), teller.log.containsString("Received iWantToDeposit " + customer.getName()));
		
		assertFalse("scheduler should return false", customer.pickAndExecuteAnAction());
		customer.depositTransaction(true, null);
		assertTrue("customer should have logged \"Received depositTransaction \"", customer.log.containsString("Received depositTransaction " + true));
		
		//will update
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		//customer.anythingElse();
		assertTrue("paycheck should be cleared to 0",p.payCheck == 0);
		assertTrue("customer should have logged \"Received anythingElse\"", customer.log.containsString("Received anythingElse"));
		assertEquals("BankCustomer should have 2 tasks", customer.tasks.size(), 2);
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		customer.atDest.release();
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("teller should have logged \"Received noThankYou \"" + customer.getName(), teller.log.containsString("Received noThankYou " + customer.getName()));
		
		assertFalse("scheduler should return false", customer.pickAndExecuteAnAction());
		
	}
	public void testNormativeScenarioWithdraw() {
		assertTrue("BankCustomer should not contain any teller now", customer.teller == null);
		assertEquals("BankCustomer should have 0 any tasks", customer.tasks.size(), 0);
		assertEquals("BankCustomer should have same name as person", customer.getName(), p.getName());
		assertEquals("BankCustomer should have same person", customer.self, null);
		
		assertFalse("scheduler should return false", customer.pickAndExecuteAnAction());
		
		assertEquals("BankCustomer should still have 0 any tasks", customer.tasks.size(), 0);
		assertTrue("BankCustomer should still not contain any teller now", customer.teller == null);
		
		//message the customer
		customer.youAreInside(p);
		assertEquals("BankCustomer should have same person", customer.self, p); // now he has pointer to person
		
		assertTrue("customer should have logged \"Received youAreInside \"" + p.getName(), customer.log.containsString("Received youAreInside " + p.getName()));
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		
		assertEquals("customer should have 0 atDest Semaphore", customer.atDest.availablePermits(), 0);
		customer.atDest.release();// give virtual permit for gui so that deadlock doesnt happen
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("customer should have 0 atDest Semaphore", customer.atDest.availablePermits(), 0);
		
		assertEquals("BankCustomer should have 0 tasks", customer.tasks.size(), 0);
		assertEquals("bank should extract the same customer if teller asks for it", bank.whoIsNextOnLine(teller), customer);
		teller.customer = customer; // assume teller is the one who called the above bank.whoIsNext
		customer.nextOnLine(teller);
		assertTrue("customer should have logged \"Received nextOnLine \"" + teller.getName(), customer.log.containsString("Received nextOnLine " + teller.getName()));
		
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		assertEquals("customer should have 0 atDest Semaphore", customer.atDest.availablePermits(), 0);
		customer.atDest.release();// give virtual permit for gui so that deadlock doesnt happen
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		
		assertTrue("teller should have logged \"Received howdy \"" + customer.getName(), teller.log.containsString("Received howdy " + customer.getName()));
		assertEquals("BankCustomer should have 0 tasks", customer.tasks.size(), 0);
		
//		customer.howMayIHelpYou();
		assertTrue("customer should have logged \"Received howMayIHelpYou\"", customer.log.containsString("Received howMayIHelpYou"));
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		
		// will only decide to withdraw
		Account acc = new Account(p.getName(), p.address, p.ssn, Account.AccountType.Saving);
		bank.getDatabase().insertAccount(acc);
		acc.deposit(10000);
		p.accounts.add(acc);
		p.payCheck = p.payCheckThreshold-1;
		p.wantCar = false;
		p.money = p.cashLowThreshold-1;
		
		assertFalse("customer's pointer to person should have account",p.accounts.isEmpty());
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		assertTrue("teller should have logged \"Received iWantToWithdraw \"" + customer.getName(), teller.log.containsString("Received iWantToWithdraw " + customer.getName()));
		
		assertFalse("scheduler should return false", customer.pickAndExecuteAnAction());
		customer.withdrawTransaction(true, null);
		assertTrue("customer should have logged \"Received withdrawTransaction \"", customer.log.containsString("Received withdrawTransaction " + true));
		
		//will update
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		
		//customer.anythingElse();
		assertTrue("money should be greater than low threshold", p.money>p.cashLowThreshold);
		assertTrue("customer should have logged \"Received anythingElse\"", customer.log.containsString("Received anythingElse"));
		assertEquals("BankCustomer should have 2 tasks", customer.tasks.size(), 2);
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		customer.atDest.release();
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("teller should have logged \"Received noThankYou \"" + customer.getName(), teller.log.containsString("Received noThankYou " + customer.getName()));
		
		assertFalse("scheduler should return false", customer.pickAndExecuteAnAction());
		
	}
	public void testNormativeScenarioLoan() {
		assertTrue("BankCustomer should not contain any teller now", customer.teller == null);
		assertEquals("BankCustomer should have 0 any tasks", customer.tasks.size(), 0);
		assertEquals("BankCustomer should have same name as person", customer.getName(), p.getName());
		assertEquals("BankCustomer should have same person", customer.self, null);
		
		assertFalse("scheduler should return false", customer.pickAndExecuteAnAction());
		
		assertEquals("BankCustomer should still have 0 any tasks", customer.tasks.size(), 0);
		assertTrue("BankCustomer should still not contain any teller now", customer.teller == null);
		
		//message the customer
		customer.youAreInside(p);
		assertEquals("BankCustomer should have same person", customer.self, p); // now he has pointer to person
		
		assertTrue("customer should have logged \"Received youAreInside \"" + p.getName(), customer.log.containsString("Received youAreInside " + p.getName()));
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		
		assertEquals("customer should have 0 atDest Semaphore", customer.atDest.availablePermits(), 0);
		customer.atDest.release();// give virtual permit for gui so that deadlock doesnt happen
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("customer should have 0 atDest Semaphore", customer.atDest.availablePermits(), 0);
		
		assertEquals("BankCustomer should have 0 tasks", customer.tasks.size(), 0);
		assertEquals("bank should extract the same customer if teller asks for it", bank.whoIsNextOnLine(teller), customer);
		teller.customer = customer; // assume teller is the one who called the above bank.whoIsNext
		customer.nextOnLine(teller);
		assertTrue("customer should have logged \"Received nextOnLine \"" + teller.getName(), customer.log.containsString("Received nextOnLine " + teller.getName()));
		
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		assertEquals("customer should have 0 atDest Semaphore", customer.atDest.availablePermits(), 0);
		customer.atDest.release();// give virtual permit for gui so that deadlock doesnt happen
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		
		assertTrue("teller should have logged \"Received howdy \"" + customer.getName(), teller.log.containsString("Received howdy " + customer.getName()));
		assertEquals("BankCustomer should have 0 tasks", customer.tasks.size(), 0);
		
//		customer.howMayIHelpYou();
		assertTrue("customer should have logged \"Received howMayIHelpYou\"", customer.log.containsString("Received howMayIHelpYou"));
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);

			// will only decide to get loan 
		Account acc = new Account(p.getName(), p.address, p.ssn, Account.AccountType.Saving);
		bank.getDatabase().insertAccount(acc);
		acc.deposit(1000);
		p.roles.add(new Role(roles.ApartmentOwner, "Apt2"));
		p.accounts.add(acc);
		p.payCheck = p.payCheckThreshold-1;
		p.wantCar = true;
		p.money = p.cashLowThreshold+1;
		
		assertFalse("customer's pointer to person should have account",p.accounts.isEmpty());
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		assertTrue("teller should have logged \"Received iWantToLoan \"" + customer.getName(), teller.log.containsString("Received iWantToLoan " + customer.getName()));
		
		assertFalse("scheduler should return false", customer.pickAndExecuteAnAction());
		customer.loanDecision(true);
		assertTrue("customer should have logged \"Received loanDecision \"" + true, customer.log.containsString("Received loanDecision " + true));
		
		//will update
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		System.out.println(p.money);
		assertTrue("money should be greater than enough to buy a car", p.money>=p.enoughMoneyToBuyACar);
		
		//customer.anythingElse();
		
		assertTrue("customer should have logged \"Received anythingElse\"", customer.log.containsString("Received anythingElse"));
		assertEquals("BankCustomer should have 2 tasks", customer.tasks.size(), 2);
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertEquals("BankCustomer should have 1 tasks", customer.tasks.size(), 1);
		customer.atDest.release();
		assertTrue("scheduler should return true", customer.pickAndExecuteAnAction());
		assertTrue("teller should have logged \"Received noThankYou \"" + customer.getName(), teller.log.containsString("Received noThankYou " + customer.getName()));
		
		assertFalse("scheduler should return false", customer.pickAndExecuteAnAction());
	}
	
	
	
}
