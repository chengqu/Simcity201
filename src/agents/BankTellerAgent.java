package agents;

import agent.Agent;

import java.util.*;

public class BankTellerAgent extends Agent {

	/*		Data		*/
	
	private Person self;
	private String name;
	class Service {
		BankCustomerAgent c;
		String customerName;
		String address;
		String ssn;
		Account.AccountType type;
		ServiceState s;
		Account acc;
		float amount;
		Role role;
		
		Service (BankCustomerAgent c, String name, String address, String ssn, Account.AccountType type, ServiceState s) {
			this.c = c;
			this.customerName = name;
			this.address = address;
			this.ssn = ssn;
			this.type = type;
			this.s = s;
		}
		Service (BankCustomerAgent c, float amount, Account acc, ServiceState s) {
			this.c = c;
			this.amount = amount;
			this.acc = acc;
			this.s = s;
		}
		Service (BankCustomerAgent c, float amount, Role role, ServiceState s) {
			this.c = c;
			this.amount = amount;
			this.role = role;
			this.s = s;
		}
		Service (BankCustomerAgent c, ServiceState s) {
			this.c = c;
			this.s = s;
		}
	}
	public enum ServiceState {
		accountCreateRequested, depositRequested, withdrawRequested,
		loanRequested, processing, doneProcessing, asked, done
	}
	List<Service> services
		= Collections.synchronizedList(new ArrayList<Service>());
	
	Bank bank;
	BankDatabase database;	//from bank
	
	class RobberyThreat {
		BankCustomerAgent c;
		ThreatState s;
		
		RobberyThreat(BankCustomerAgent c, ThreatState s) {
			this.c = c;
			this.s = s;
		}
	}
	public enum ThreatState {
		needHelp, calledHelp, secured
	}
	List<RobberyThreat> threats = 
			Collections.synchronizedList(new ArrayList<RobberyThreat>());
	BankSecurityAgent security;
	
	/*		Messages		*/
	
	public void iNeedAccount(BankCustomerAgent c, String name, String address, String ssn, Account.AccountType type) {
		Service existingRecord = null;
		synchronized (services) {
		for (Service s : services) {
			if (s.c.equals(c)) {
				existingRecord = s;
				s.s = ServiceState.accountCreateRequested;
			}
		}
		}
		if ( existingRecord == null) {
			services.add(new Service(c, name, ssn, address, type, ServiceState.accountCreateRequested));
		}
		stateChanged();
	}
	
	public void iWantToDeposit(BankCustomerAgent c, float amount, Account acc) {
		Service existingRecord = null;
		synchronized (services) {
		for (Service s : services) {
			if (s.c.equals(c)) {
				existingRecord = s;
				s.s = ServiceState.depositRequested;
			}
		}
		}
		if ( existingRecord == null) {
			services.add(new Service(c, amount, acc, ServiceState.depositRequested));
		}
		stateChanged();
	}
	
	public void iWantToWithdraw(BankCustomerAgent c, float amount, Account acc) {
		Service existingRecord = null;
		synchronized (services) {
		for (Service s : services) {
			if (s.c.equals(c)) {
				existingRecord = s;
				s.s = ServiceState.depositRequested;
			}
		}
		}
		if ( existingRecord == null) {
			services.add(new Service(c, amount, acc, ServiceState.withdrawRequested));
		}
		stateChanged();
	}
	
	public void iWantToLoan(BankCustomerAgent c, float amount, Role role) {
		Service existingRecord = null;
		synchronized (services) {
		for (Service s : services) {
			if (s.c.equals(c)) {
				existingRecord = s;
				s.s = ServiceState.loanRequested;
			}
		}
		}
		if ( existingRecord == null) {
			services.add(new Service(c, amount, role, ServiceState.loanRequested));
		}
		stateChanged();
	}
	
	public void giveMeTheMoney(BankCustomerAgent c) {
		
		synchronized (services) {
		for (Service s : services) {
			if (s.c.equals(c)) {
				services.remove(s);
				break;
			}
		}
		}
		
		threats.add(new RobberyThreat(c, ThreatState.needHelp));
		stateChanged();
	}
	
	public void robberyIsDown(BankCustomerAgent c) {
		synchronized (threats) {
		for (RobberyThreat t : threats) {
			if (t.s == ThreatState.calledHelp) {
				if (t.c.equals(c)) {
					t.s = ThreatState.secured;
				}
			}
		}
		}
		stateChanged();
	}
	
	public void noThankYou(BankCustomerAgent c) {
		Service existingRecord = null;
		synchronized (services) {
		for (Service s : services) {
			if (s.c.equals(c)) {
				existingRecord = s;
				s.s = ServiceState.done;
			}
		}
		}
		if ( existingRecord == null) {
			services.add(new Service(c, ServiceState.done));
		}
		stateChanged();
	}
	
	/*		Scheduler		*/
	
	protected boolean pickAndExecuteAnAction() {
		
		synchronized (threats) {
		for (RobberyThreat t : threats) {
			if (t.s == ThreatState.needHelp) {
				askForHelp(t);
				return true;
			}
		}
		}
		
		synchronized (threats) {
		for (RobberyThreat t : threats) {
			if (t.s == ThreatState.secured) {
				clearThreat(t);
				return true;
			}
		}
		}
		
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.accountCreateRequested) {
				createAccount(s);
				return true;
			}
		}
		}
		
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.depositRequested) {
				processDeposit(s);
				return true;
			}
		}
		}
		
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.withdrawRequested) {
				processWithdraw(s);
				return true;
			}
		}
		}
		
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.loanRequested) {
				processLoan(s);
				return true;
			}
		}
		}
		
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.doneProcessing) {
				askForAnythingElse(s);
			}
		}
		}
		
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.done) {
				serviceDone(s);
			}
		}
		}
		
		
		callNextOnLine();
		
		return false;
	}
	
	/*		Action		*/
	
	private void askForHelp(RobberyThreat t) {
		t.s = ThreatState.calledHelp;
		security.helpMe(t.c);
		print("KILL THIS FUCKING ROBBERY!!!");
	}
	private void clearThreat(RobberyThreat t) {
		threats.remove(t);
		print("My bank never gets robbed :)");
	}
	private void createAccount(Service s) {
		s.s = ServiceState.processing;
		List<Account> accounts = database.ssnSearch(s.ssn);
		boolean found = false;
		synchronized (accounts) {
		for (Account acc : accounts) {
			if (s.type == acc.getType()) {
				s.c.unableToMakeAccount("The same type account exists");
				found =true;
				break;
			}
		}
		}
		if (!found) {
			Account acc = new Account(s.customerName, s.address, s.ssn, s.type);
			database.insertAccount(acc);
			s.c.hereIsYourAccount(acc);
		}
		s.s = ServiceState.doneProcessing;
		print("create account");
	}
	private void processDeposit(Service s) {
		s.s = ServiceState.processing;
		Account customerAccount = database.searchAccount(s.acc.getAccountNumber());
		if (customerAccount.getBalance() + s.amount > customerAccount.getDepositLimit()) {
			s.c.transaction(false, "Your account reached a limit");
		}else {
			customerAccount.deposit(s.amount);
			s.c.transaction(true, null);
		}
		s.s = ServiceState.doneProcessing;
		print("deposit");
	}
	private void processWithdraw(Service s) {
		s.s = ServiceState.processing;
		Account customerAccount = database.searchAccount(s.acc.getAccountNumber());
		if (customerAccount.getBalance() < s.amount) {
			s.c.transaction(false, "You do not have enough money in your account");
		}else {
			customerAccount.deposit(s.amount);
			s.c.transaction(true, null);
		}
		s.s = ServiceState.doneProcessing;
		print("withdraw");
	}
	private void processLoan(Service s) {
		s.s = ServiceState.processing;
		if (s.role != null) { // s.role is guaranteed to be a job role
			// if role is not a owner and s.amount > 50000, then you can't loan
			// else you will get a loan
		}else {
			s.c.loanDecision(false);
		}
		s.s = ServiceState.doneProcessing;
		print("loan");
	}
	private void askForAnythingElse(Service s) {
		s.s = ServiceState.asked;
		s.c.anythingElse();
		print("anything else?");
	}
	private void callNextOnLine() {
		BankCustomerAgent c = bank.whoIsNextOnLine();
		c.nextOnLine(this);
		print("next on line?");
	}
	private void serviceDone(Service s) {
		services.remove(s);
		print("service done");
	}
	
	
	
	/*		Utilities		*/
	public BankTellerAgent(String name) {
		this.name = name;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
}
 