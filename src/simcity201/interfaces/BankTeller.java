package simcity201.interfaces;

import agents.Account;
import agents.BankDatabase;
import agents.Person;
import agents.Role;
import agents.Account.AccountType;
import simcity201.gui.Bank;
import simcity201.gui.BankTellerGui;

public interface BankTeller {

	/*		Messages		*/
	public void youAreAtWork(Person p);

	public void securityOnDuty(BankSecurity sec);
	
	public void howdy(BankCustomer c);

	public void iNeedAccount(BankCustomer c, String name, String address,
			int ssn, Account.AccountType type);

	public void iWantToDeposit(BankCustomer c, float amount, int acc_number);

	public void iWantToWithdraw(BankCustomer c, float amount,
			int acc_number);

	public void iWantToLoan(BankCustomer c, float amount, Role role);

	public void giveMeTheMoney(BankCustomer c);

	public void robberyIsDown(BankCustomer c);

	//public void noThankYou(BankCustomer c);
	public void doneNoThankYou(BankCustomer c);

	public void msgAtDestination();

	public void setBank(Bank bank);

	public void setDB(BankDatabase db);

	public void setGui(BankTellerGui g);
	
	public String getName();
	
	public Person getSelf();
	
	public boolean isWorking();

	public void giveRobberMoney(BankCustomer robber);

	public void dontCallCop(BankCustomer customer);

}