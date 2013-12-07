package simcity201.interfaces;

import agents.Account;
import agents.BankTellerAgent;
import agents.Person;
import simcity201.gui.Bank;
import simcity201.gui.BankCustomerGui;

public interface BankCustomer {

	public void youAreInside(Person p);

	public void nextOnLine(BankTeller teller);

	public void nextOnLine(BankATM atm);

	public void howMayIHelpYou();

	public void hereIsYourAccount(Account account);

	public void unableToMakeAccount(String reason);

	public void depositTransaction(boolean isSuccess, String reason);

	public void withdrawTransaction(boolean isSuccess, String reason);

	public void loanDecision(boolean isApproved);

	public void die();

	public void anythingElse();

	public void msgAtDestination();

	public void setBank(Bank bank);

	public void setGui(BankCustomerGui g);

	public BankCustomerGui getGui();

	public String getName();

	public Person getSelf();

}