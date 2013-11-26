package simcity201.test;

//import simcity201.gui.Bank;
//import simcity201.gui.BankMap;
//import simcity201.test.mock.MockBankATM;
import simcity201.test.mock.MockBankCustomer;
//import simcity201.test.mock.MockBankSecurity;
import agents.BankTellerAgent;
//import agents.Person;
import junit.framework.TestCase;

public class BankTellerTest extends TestCase {
	
	BankTellerAgent teller;
	MockBankCustomer customer;
	//MockBankATM atm;
	//MockBankSecurity security;
	
	
	public void setUp() throws Exception {
		super.setUp();
		teller = new BankTellerAgent("Teller");
		
		customer = new MockBankCustomer("MockCustomer");
		customer.teller = teller;
	}
	
	public void testNormativeCreatingAccountScenario() {
		assertEquals("Teller should have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should have 0 services. It doesn't.", teller.services.size(), 0);
		
		assertFalse("Scheduler should return false", teller.pickAndExecuteAnAction());
		
		assertEquals("Teller should still have 0 threats. It doesn't.",teller.threats.size(), 0);
		assertEquals("Teller should still have 0 services. It doesn't.", teller.services.size(), 0);
		
		//receives message from customer
		teller.howdy(customer);
		
		
	}
	public void testNormativeMakingDepositScenario() {
		
	}
	public void testNormativeWithdrawingMoneyScenario() {
		
	}
	public void testNormativeMakingLoanScenario() {
		
	}

}
