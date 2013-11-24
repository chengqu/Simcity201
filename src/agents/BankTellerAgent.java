package agents;

import agent.Agent;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity201.gui.Bank;
import simcity201.gui.BankTellerGui;

public class BankTellerAgent extends Agent {

	/*		Data		*/
	
	public Person self;
	private String name;
	BankTellerGui gui;
	class Service {
		BankCustomerAgent c;
		String customerName;
		String address;
		int ssn;
		Account.AccountType type;
		ServiceState s;
		//Account acc;
		float amount;
		Role role;
		int acc_number;
		
		Service (BankCustomerAgent c, String name, String address, int ssn, Account.AccountType type, ServiceState s) {
			this.c = c;
			this.customerName = name;
			this.address = address;
			this.ssn = ssn;
			this.type = type;
			this.s = s;
		}
		Service (BankCustomerAgent c, float amount, int acc_number, ServiceState s) {
			this.c = c;
			this.amount = amount;
			this.acc_number = acc_number;
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
		Service (ServiceState s) {
			this.s = s;
		}
	}
	public enum ServiceState {
		greetCustomer, prepareToWork,
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
	
	Semaphore atDest = new Semaphore(0, true);
	
	/*		Messages		*/
	public void youAreAtWork(Person p) {
		self = p;
		services.add(new Service(ServiceState.prepareToWork));
		stateChanged();
	}
	public void howdy(BankCustomerAgent c) {
		services.add(new Service(c, ServiceState.greetCustomer));
		stateChanged();
	}
	public void iNeedAccount(BankCustomerAgent c, String name, String address, int ssn, Account.AccountType type) {
		Service existingRecord = null;
		/*
		synchronized (services) {
		for (Service s : services) {
			if (s.c.equals(c)) {
				existingRecord = s;
				s.s = ServiceState.accountCreateRequested;
			}
		}
		}*/
		if ( existingRecord == null) {
			services.add(new Service(c, name, address, ssn, type, ServiceState.accountCreateRequested));
		}
		stateChanged();
	}
	
	public void iWantToDeposit(BankCustomerAgent c, float amount, int acc_number) {
		Service existingRecord = null;
		synchronized (services) {
		for (Service s : services) {
			if (s.c.equals(c)) {
				existingRecord = s;
				s.acc_number = acc_number;
				s.s = ServiceState.depositRequested;
			}
		}
		}
		if ( existingRecord == null) {
			services.add(new Service(c, amount, acc_number, ServiceState.depositRequested));
		}
		stateChanged();
	}
	
	public void iWantToWithdraw(BankCustomerAgent c, float amount, int acc_number) {
		Service existingRecord = null;
		synchronized (services) {
		for (Service s : services) {
			if (s.c.equals(c)) {
				existingRecord = s;
				s.acc_number = acc_number;
				s.s = ServiceState.withdrawRequested;
			}
		}
		}
		if ( existingRecord == null) {
			services.add(new Service(c, amount, acc_number, ServiceState.withdrawRequested));
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
		/*
		if ( existingRecord == null) {
			services.add(new Service(c, ServiceState.done));
		}*/
		stateChanged();
	}
	
	public void msgAtDestination() {
		atDest.release();
		stateChanged();
	}
	
	/*		Scheduler		*/
	
	protected boolean pickAndExecuteAnAction() {
		Service tempService = null;
		RobberyThreat tempThreat = null;
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.prepareToWork) {
				//services.remove(s);
				//callNextOnLine();
				tempService = s;break;
				//return true;
			}
		}
		}	if(tempService != null)	{services.remove(tempService); callNextOnLine(); return true;}
		
		synchronized (threats) {
		for (RobberyThreat t : threats) {
			if (t.s == ThreatState.needHelp) {
				//askForHelp(t);
				//return true;
				tempThreat = t; break;
			}
		}
		}	if(tempThreat != null) {askForHelp(tempThreat); return true;}
		
		synchronized (threats) {
		for (RobberyThreat t : threats) {
			if (t.s == ThreatState.secured) {
				//clearThreat(t);
				//return true;
				tempThreat = t; break;
			}
		}
		}	if(tempThreat != null) {clearThreat(tempThreat); return true;}
		

		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.greetCustomer) {
				//greetings(s);
				//return true;
				tempService = s; break;
			}
		}
		}	if(tempService != null) {greetings(tempService); return true;}
		
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.accountCreateRequested) {
				//createAccount(s);
				//return true;
				tempService = s; break;
			}
		}
		}	if(tempService != null) {createAccount(tempService); return true;}
		
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.depositRequested) {
				//processDeposit(s);
				//return true;
				tempService = s; break;
			}
		}
		}	if(tempService != null) {processDeposit(tempService); return true;}
		
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.withdrawRequested) {
				//processWithdraw(s);
				//return true;
				tempService = s; break;
			}
		}
		}	if(tempService != null) {processWithdraw(tempService); return true;}
		
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.loanRequested) {
				//processLoan(s);
				//return true;
				tempService = s; break;
			}
		}
		}	if(tempService != null) {processLoan(tempService); return true;}
		
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.doneProcessing) {
				//askForAnythingElse(s);
				//return true;
				tempService = s; break;
			}
		}
		}	if(tempService != null) {askForAnythingElse(tempService); return true;}
		
		synchronized (services) {
		for (Service s : services) {
			if (s.s == ServiceState.done) {
				//serviceDone(s);
				//return true;
				tempService = s; break;
			}
		}
		}	if(tempService != null) {serviceDone(tempService); return true;}
		/*
		if (services.isEmpty())
			callNextOnLine();
		*/
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
	private void greetings(Service s) {
		services.remove(s);
		s.c.howMayIHelpYou();
		print("How may I help you?");
	}
	private void createAccount(Service s) {
		//print("Creating account");
		s.s = ServiceState.processing;
		List<Account> accounts = database.ssnSearch(s.ssn);
		boolean found = false;
		if (accounts != null) {
		for (Account acc : accounts) {
			if (s.type == acc.getType()) {
				print("the same type account already exist, unable to make account");
				s.c.unableToMakeAccount("The same type account exists");
				found =true;
				break;
			}
		}
		}
		if (!found) {
			Account acc = new Account(s.customerName, s.address, s.ssn, s.type);
			database.insertAccount(acc);
			print("here is your account: "+acc.getType().toString() + ", " + acc.getAccountNumber());
			s.c.hereIsYourAccount(acc);
		}
		s.s = ServiceState.doneProcessing;
		
	}
	private void processDeposit(Service s) {
		s.s = ServiceState.processing;
		print("\t\t " + s.acc_number);
		Account customerAccount = database.searchAccount(s.acc_number);
		if (customerAccount.getBalance() + s.amount > customerAccount.getDepositLimit()) {
			s.c.depositTransaction(false, "Your account reached a limit");
		}else {
			customerAccount.deposit(s.amount);
			s.c.depositTransaction(true, null);
		}
		s.s = ServiceState.doneProcessing;
		print("deposit");
	}
	private void processWithdraw(Service s) {
		s.s = ServiceState.processing;
		Account customerAccount = database.searchAccount(s.acc_number);
		if (customerAccount.getBalance() < s.amount) {
			s.c.withdrawTransaction(false, "You do not have enough money in your account");
		}else {
			customerAccount.deposit(s.amount);
			s.c.withdrawTransaction(true, null);
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
		print("next on line?");
		BankCustomerAgent c = bank.whoIsNextOnLine();
		print("next is " + c.getName());
		c.nextOnLine(this);
	}
	private void serviceDone(Service s) {
		services.remove(s);
		print("service done");
		callNextOnLine();
	}
	
	
	
	/*		Utilities		*/
	public BankTellerAgent(String name) {
		this.name = name;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public String getName(){
		return this.name;
	}
	public void setDB(BankDatabase db) {
		this.database = db;
	}
	public void setGui(BankTellerGui g) {
		this.gui = g;
	}
	
}
 