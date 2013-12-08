package simcity201.test.mock;

import agents.Account.AccountType;
import agents.BankDatabase;
import agents.Person;
import agents.Role;
import simcity201.gui.Bank;
import simcity201.gui.BankTellerGui;
import simcity201.interfaces.BankCustomer;
import simcity201.interfaces.BankTeller;

public class MockBankTeller extends Mock implements BankTeller{

	Person self;
	
	public EventLog log = new EventLog();
	
	public BankCustomer customer; 
	
	public MockBankTeller(String name) {
		super(name);
	}

	@Override
	public void youAreAtWork(Person p) {
		log.add(new LoggedEvent("Received youAreAtWork " + p.getName()));
	}

	@Override
	public void howdy(BankCustomer c) {
		log.add(new LoggedEvent("Received howdy " + c.getName()));
	}

	@Override
	public void iNeedAccount(BankCustomer c, String name, String address,
			int ssn, AccountType type) {
		log.add(new LoggedEvent("Received iNeedAccount " + c.getName()));
	}

	@Override
	public void iWantToDeposit(BankCustomer c, float amount, int acc_number) {
		log.add(new LoggedEvent("Received iWantToDeposit " + c.getName()));
	}

	@Override
	public void iWantToWithdraw(BankCustomer c, float amount, int acc_number) {
		log.add(new LoggedEvent("Received iWantToWithdraw " + c.getName()));
	}

	@Override
	public void iWantToLoan(BankCustomer c, float amount, Role role) {
		log.add(new LoggedEvent("Received iWantToLoan " + c.getName()));
	}

	@Override
	public void giveMeTheMoney(BankCustomer c) {
		log.add(new LoggedEvent("Received giveMeTheMoney " + c.getName()));
	}

	@Override
	public void robberyIsDown(BankCustomer c) {
		log.add(new LoggedEvent("Received robberyIsDown " + c.getName()));
	}

	@Override
	public void noThankYou(BankCustomer c) {
		log.add(new LoggedEvent("Received noThankYou " + c.getName()));
	}

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBank(Bank bank) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDB(BankDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(BankTellerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Person getSelf() {
		// TODO Auto-generated method stub
		return self;
	}

	@Override
	public boolean isWorking() {
		// TODO Auto-generated method stub
		return false;
	}

}
