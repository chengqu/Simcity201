package simcity201.test.mock;

import guehochoi.test.mock.EventLog;
import guehochoi.test.mock.LoggedEvent;
import agents.Account;
import agents.BankTellerAgent;
import agents.Person;
import simcity201.gui.Bank;
import simcity201.gui.BankCustomerGui;
import simcity201.interfaces.BankATM;
import simcity201.interfaces.BankCustomer;
import simcity201.interfaces.BankTeller;

public class MockBankCustomer extends Mock implements BankCustomer{

	public Person self;
	
	public EventLog log = new EventLog();
	
	public BankTeller teller;
	
	public MockBankCustomer(String name) {
		super(name);
	}

	@Override
	public void youAreInside(Person p) {
		log.add(new LoggedEvent("Received youAreInside " + p));
	}

	@Override
	public void nextOnLine(BankTellerAgent teller) {
		log.add(new LoggedEvent("Received nextOnLine " + teller));
	}

	@Override
	public void nextOnLine(BankATM atm) {
		log.add(new LoggedEvent("Received bankATM " + atm));
	}

	@Override
	public void howMayIHelpYou() {
		log.add(new LoggedEvent("Received howMayIHelpYou"));
	}

	@Override
	public void hereIsYourAccount(Account account) {
		log.add(new LoggedEvent("Received hereIsYourAccount "));
	}

	@Override
	public void unableToMakeAccount(String reason) {
		log.add(new LoggedEvent("Received unableToMakeAccount " + reason));
	}

	@Override
	public void depositTransaction(boolean isSuccess, String reason) {
		log.add(new LoggedEvent("Received depositTransaction " + isSuccess));
	}

	@Override
	public void withdrawTransaction(boolean isSuccess, String reason) {
		log.add(new LoggedEvent("Received withdrawTransaction " + isSuccess));
	}

	@Override
	public void loanDecision(boolean isApproved) {
		log.add(new LoggedEvent("Received loanDecision " + isApproved));
	}

	@Override
	public void die() {
		log.add(new LoggedEvent("Received die"));
	}

	@Override
	public void anythingElse() {
		log.add(new LoggedEvent("Received anythingElse"));
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
	public void setGui(BankCustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BankCustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

}
