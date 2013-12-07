package agents;

import simcity201.test.mock.EventLog;
import simcity201.test.mock.LoggedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simcity201.gui.Bank;
import simcity201.gui.BankCustomerGui;
import simcity201.interfaces.BankATM;
import simcity201.interfaces.BankCustomer;
import simcity201.interfaces.BankTeller;
import agent.Agent;
import agents.Role.roles;

public class BankCustomerAgent extends Agent implements BankCustomer {

	public EventLog log = new EventLog();
	
	
	/*		Data		*/
	
	private String name;
	public Person self;
	private BankCustomerGui gui;
	
	class Task {
		Objective obj;
		TaskState s;
		float amount;
		Account acc;
		int acc_number;
		Account.AccountType type;
		
		Task(Objective obj, TaskState s) {
			this.obj = obj;
			this.s = s;
		}
		Task(Objective obj, Account.AccountType type, TaskState s) {
			this.obj = obj;
			this.type = type;
			this.s = s;
		}
		Task(Objective obj, float amount, int acc_num, TaskState s) {
			this.obj = obj;
			this.amount = amount;
			this.acc_number = acc_num;
			this.s = s;
		}
		Task(Objective obj, float amount, TaskState s) {
			this.obj = obj;
			this.amount = amount;
			this.s = s;
		}
	}
	
	public enum Objective { toWaitOnLine, toApproachTeller, toApproachATM, toDetermineWhatINeed, toMakeAccount, toLoan, toDeposit, toWithdraw, toLeave, toDie }
	public enum TaskState { toDo, pending, needUpdate, rejected } 
	
	public List<Task> tasks = Collections.synchronizedList(new ArrayList<Task>());
	
	public BankTeller teller;
	BankATM atm;
	
	Bank bank;
	boolean isPresentInBank = false;
	
	boolean taskAdded_withdraw = false;
	boolean taskAdded_create = false;
	boolean taskAdded_deposit = false;
	boolean taskAdded_loan = false;
	
	public Semaphore atDest = new Semaphore(0, true);
	
	/*		Messages		*/

	
	public void youAreInside(Person p) { // called by Bank after creation of BankCustomer instance
		log.add(new LoggedEvent("Received youAreInside " + p.getName()));
		tasks.add(new Task(Objective.toWaitOnLine, TaskState.toDo));
		print("you are inside of bank");
		isPresentInBank = true;
		self = p;
		stateChanged();
	}
	
	public void nextOnLine(BankTeller teller) {
		log.add(new LoggedEvent("Received nextOnLine " + teller.getName()));
		this.teller = teller;
		tasks.add(new Task(Objective.toApproachTeller, TaskState.toDo));
		print("done waiting on line");
		stateChanged();
	}
	
	public void nextOnLine(BankATM atm) {
		log.add(new LoggedEvent("Received bankATM " + atm));
		this.atm = atm;
		tasks.add(new Task(Objective.toApproachATM, TaskState.toDo));
		print("done waiting on line");
		stateChanged();
	}
	
	public void howMayIHelpYou() {
		log.add(new LoggedEvent("Received howMayIHelpYou"));
		tasks.add(new Task(Objective.toDetermineWhatINeed, TaskState.toDo));
		stateChanged();
	}
	
	public void hereIsYourAccount(Account account) {
		log.add(new LoggedEvent("Received hereIsYourAccount " + account));
		synchronized( tasks ) {
		for (Task t : tasks) {
			if (t.obj == Objective.toMakeAccount && t.s == TaskState.pending) {
				t.acc = account;
				t.s = TaskState.needUpdate;
				break;
			}
		}//tasks
		}//sync
		stateChanged();
	}
	
	public void unableToMakeAccount(String reason) {
		log.add(new LoggedEvent("Received unableToMakeAccount " + reason));
		//TODO: deal with the reason in v2.2
		synchronized( tasks ) {
		for (Task t : tasks) {
			if (t.obj == Objective.toMakeAccount && t.s == TaskState.pending) {
				t.s = TaskState.rejected;
				break;
			}
		}//tasks
		}//sync
		stateChanged();
	}
	
	public void depositTransaction(boolean isSuccess, String reason) { 
		//TODO: deal with the reason in v2.2
		log.add(new LoggedEvent("Received depositTransaction " + isSuccess));
		synchronized( tasks ) {
		for (Task t : tasks) {
			if (t.obj == Objective.toDeposit && t.s == TaskState.pending) {
				if (isSuccess)
					t.s = TaskState.needUpdate;
				else
					t.s = TaskState.rejected;
			}
		}//tasks
		}//sync
		stateChanged();
	}
	
	public void withdrawTransaction(boolean isSuccess, String reason) { 
		//TODO: deal with the reason in v2.2
		log.add(new LoggedEvent("Received withdrawTransaction " + isSuccess));
		synchronized( tasks ) {
		for (Task t : tasks) {
			if (t.obj == Objective.toWithdraw && t.s == TaskState.pending) {
				if (isSuccess)
					t.s = TaskState.needUpdate;
				else
					t.s = TaskState.rejected;
			}
		}//tasks
		}//sync
		stateChanged();
	}
	
	
	public void loanDecision( boolean isApproved )  {
		log.add(new LoggedEvent("Received loanDecision " + isApproved));
		synchronized( tasks ) {
		for (Task t : tasks) {
			if (t.obj == Objective.toLoan && t.s == TaskState.pending) {
				if (isApproved)
					t.s = TaskState.needUpdate;
				else
					t.s = TaskState.rejected;
			}
		}//tasks
		}//sync
		stateChanged();
	}
	
	public void die() {
		log.add(new LoggedEvent("Received die"));
		tasks.add(new Task(Objective.toDie, TaskState.toDo));
		stateChanged();
	}
	
	public void anythingElse() {
		log.add(new LoggedEvent("Received anythingElse"));
		tasks.add(new Task(Objective.toLeave, TaskState.toDo));
		stateChanged();
	} 
	
	
	public void msgAtDestination() {
		atDest.release();
		stateChanged();
	}
	
	/* 		Scheduler 		*/
	
	@Override
	public boolean pickAndExecuteAnAction() {
		
		Task tempTask = null;
		
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.toDo) {
				if (t.obj == Objective.toDie) {
					//goDead();
					//return false;
					tempTask = t; break;
				}
			}
		}
		}	if (tempTask != null) {goDead(); return false;}
		
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.toDo) {
				if (t.obj == Objective.toWaitOnLine) {
					//goToLine(t);
					//return true;
					tempTask = t; break;
				}
			}
		}
		}	if (tempTask != null) {goToLine(tempTask); return true;}
	
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.toDo) {
				if (t.obj == Objective.toApproachTeller) {
					//approachTeller(t);
					//return true;
					tempTask = t; break;
				}
			}
		}
		}	if (tempTask != null) {approachTeller(tempTask); return true;}
		
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.toDo) {
				if (t.obj == Objective.toApproachATM) {
					//approachATM(t);
					//return true;
					tempTask = t; break;
				}
			}
		}
		}	if (tempTask != null) {approachATM(tempTask); return true;}
		
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.toDo) {
				if (t.obj == Objective.toDetermineWhatINeed) {
					//determineWhatINeed(t);
					//return true;
					tempTask = t; break;
				}
			}
		}
		}	if (tempTask != null) {determineWhatINeed(tempTask); return true;}
		
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.pending) {
				return false;	// I don't want to rush ATM or teller
			}
		}
		}
		
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.needUpdate) {
				//update(t);		// will remove task as well
				//return true;
				tempTask = t; break;
			}
		}
		}	if (tempTask != null) {update(tempTask); return true;}
		
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.rejected) {
				//dealWithRejection(t);
				//return true;
				tempTask = t; break;
			}
		}
		}	if (tempTask != null) {dealWithRejection(tempTask); return true;}
		
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.toDo) {
				if (t.obj == Objective.toMakeAccount) {
					//makeAccount(t);
					//return true;
					tempTask = t; break;
				}
			}
		}
		}	if (tempTask != null) {makeAccount(tempTask); return true;}
		
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.toDo) {
				if (t.obj == Objective.toDeposit) {
					//depositMoney(t);
					//return true;
					tempTask = t; break;
				}
			}
		}
		}	if (tempTask != null) {depositMoney(tempTask); return true;}
		
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.toDo) {
				if (t.obj == Objective.toWithdraw) {
					//withdrawMoney(t);
					//return true;
					tempTask = t; break;
				}
			}
		}
		}	if (tempTask != null) {withdrawMoney(tempTask); return true;}
		
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.toDo) {
				if (t.obj == Objective.toLoan) {
					//loanMoney(t);
					//return true;
					tempTask = t; break;
				}
			}
		}
		}	if (tempTask != null) {loanMoney(tempTask); return true;}
		
		synchronized (tasks) {
		for (Task t : tasks) {
			if (t.s == TaskState.toDo) {
				if (t.obj == Objective.toLeave) {
					//leaveBank(t);
					//return true;
					tempTask = t; break;
				}
			}
		}
		}	if (tempTask != null) {leaveBank(tempTask); return true;}
		
		return false;
	}

	private void goDead() {
		tasks.clear();
		print("I just wanted to rob 5 bucks . . . dead");
	}
	private void goToLine(Task t) {
		//DoGoOnLine();
		tasks.remove(t);
		gui.DoGoToLine();
		try{
			atDest.acquire();
		}catch(InterruptedException ie) {
			ie.printStackTrace();
		}
		print("I am on line now");
		bank.iAmOnLine(this);
	}
	private void approachTeller(Task t) {
		if ( teller instanceof BankTellerAgent)
			gui.DoApproachTeller((BankTellerAgent)teller);
		
		try{
			atDest.acquire();
		}catch(InterruptedException ie) {
			ie.printStackTrace();
		}
		tasks.remove(t);
		teller.howdy(this);
		print("howdy teller");
	}
	private void approachATM(Task t) {
		tasks.remove(t);
		//atm.howdy();
		print("howdy atm");
	}
	private void determineWhatINeed(Task t) {
		tasks.remove(t);
		boolean isRobbery = false;
		for(Role r : self.roles) {
			if (r.getRole() == Role.roles.Robbery) {
				isRobbery = true;
				break;
			}
		}
		
		if (!isRobbery) {
		if (self.accounts.isEmpty() && !taskAdded_create) {
			tasks.add(new Task(Objective.toMakeAccount, Account.AccountType.Saving, TaskState.toDo));
			print("task: create new account");
			// I want to create new account
			taskAdded_create = true;
		}
		float totalMoney = (float)self.money + self.payCheck;
		Account checking = null;
		Account saving = null;
		for (Account acc : self.accounts) {
			totalMoney += acc.getBalance();
			if (acc.getBalance() >= 2*self.cashLowThreshold &&
					self.money < self.cashLowThreshold &&
					!taskAdded_withdraw) {
				// I want to withdraw
				print("task: withdrawal");
				tasks.add(new Task(Objective.toWithdraw, 2*self.cashLowThreshold, acc.getAccountNumber(), TaskState.toDo));
				taskAdded_withdraw = true;
			}
			if (acc.getType() == Account.AccountType.Checking) {
				checking = acc;
			}else if (acc.getType() == Account.AccountType.Saving) {
				saving = acc;
			}
		}
		
		if (self.payCheck >= self.payCheckThreshold && saving!=null && !taskAdded_deposit) {
			// I want to deposit
			tasks.add(new Task(Objective.toDeposit, self.payCheck, saving.getAccountNumber(), TaskState.toDo));
			print("task: depoist");
			taskAdded_deposit = true;
		}
		
		if (totalMoney < self.enoughMoneyToBuyACar && self.wantCar && !taskAdded_loan) {
			// I want loan
			/*IDEA: when loan is approved, you make checking, and checking will have the borrowed money*/
			tasks.add(new Task(Objective.toLoan, self.enoughMoneyToBuyACar, TaskState.toDo));
			print("task: loan");
			taskAdded_loan = true;
		}
		
		}else {
			// I want to rob
			// tasks.add( new Task ...
		}
		
		print("determined what to do here in bank");
		
		if (tasks.isEmpty()) {
			tasks.add(new Task(Objective.toLeave, TaskState.toDo));
		}
	}
	private void makeAccount(Task t) {
		t.s = TaskState.pending;		
		teller.iNeedAccount(this, self.getName(), self.address, self.ssn, t.type);
		print("I need to create an account");
	}
	private void depositMoney(Task t) {
		t.s = TaskState.pending;
		if (teller != null) {
			//print("acc_number: " + t.acc_number);
			teller.iWantToDeposit(this, t.amount, t.acc_number);
		}else {
			//atm.deposit(this, t.amount, t.acc);
		}
		print("I want to deposit my money");
	}
	private void withdrawMoney(Task t) {
		t.s = TaskState.pending;
		if (teller != null) {
			teller.iWantToWithdraw(this, t.amount, t.acc_number);
		}else {
			//atm.withdraw(this, t.amount, t.acc);
		}
		print("I'd like to withdraw my money");
	}
	private void loanMoney(Task t) {
		t.s = TaskState.pending;
		Role myJob = null;
		for (Role r : self.roles) {
			if (r.getRole() == roles.ApartmentOwner|| r.getRole() == roles.AptOwner
					|| r.getRole() == roles.houseOwner|| r.getRole() == roles.TellerAtChaseBank) {
				myJob = r;
			}
		}
		teller.iWantToLoan(this, t.amount, myJob);
		print("Let me loan some money");
	}
	private void update(Task t) {
		
		if (t.obj == Objective.toMakeAccount) {
			self.accounts.add(t.acc);
		}else if (t.obj == Objective.toDeposit) {
			self.payCheck -= t.amount;
			//t.acc.setTotal(t.acc.getBalance() +t.amount); same pointer, not needed
			//self.accounts.updateAccount(t.acc); // stub
		}else if (t.obj == Objective.toWithdraw) {
			self.money += t.amount;
			//t.acc.setTotal(t.acc.getTotal() - t.amount);
			//self.accounts.updateAccount(t.acc); // stub
		}else if (t.obj == Objective.toLoan) {
			self.money += t.amount;
			for (Account ac : self.accounts) {
				float temp = ac.getBalance();
				self.money += temp;
				ac.withdraw(temp);
			}
			System.out.println("\t\t\t"+self.money);
			//		self.cash += t.amount;
		}
		tasks.remove(t);
		print(t.obj.toString() + " updated ");
		tasks.add(new Task(Objective.toDetermineWhatINeed, TaskState.toDo));
		
	}
	private void dealWithRejection(Task t) {
		tasks.remove(t);
		if (t.obj == Objective.toMakeAccount) {
			//self.accounts.add(t.acc);
		}else if (t.obj == Objective.toDeposit) {
			//self.payCheck -= t.amount;
			//t.acc.setTotal(t.acc.getBalance() +t.amount); same pointer, not needed
			//self.accounts.updateAccount(t.acc); // stub
		}else if (t.obj == Objective.toWithdraw) {
			//self.money += t.amount;
			//t.acc.setTotal(t.acc.getTotal() - t.amount);
			//self.accounts.updateAccount(t.acc); // stub
		}else if (t.obj == Objective.toLoan) {
			self.wantCar = false;
			//self.money += t.amount;
			//		self.cash += t.amount;
		}
		print("u rejected me? hell");
	}
	private void leaveBank(Task t) {
		tasks.remove(t);
		//if (tasks.isEmpty()) {
		tasks.clear();
			if (teller != null){
				teller.noThankYou(this);			
			}else {
				//atm.noThankYou(this);
			}
		//}
		if (isPresentInBank) {
			isPresentInBank = false;
			taskAdded_deposit = false; taskAdded_withdraw = false;
			taskAdded_create = false; taskAdded_loan = false;
			print("no thank you, im leavin");
			gui.DoLeaveBank();
			try{
				atDest.acquire();
			}catch(InterruptedException ie) {
				ie.printStackTrace();
			}
			self.msgDone();
		}
	}
	
	/*		Utilities 		*/
	public BankCustomerAgent(String name) {
		this.name = name;
	}
	
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public String getName(){
		return this.name;
	}
	
	public void setGui(BankCustomerGui g) {
		this.gui = g;
	}
	
	public BankCustomerGui getGui() {
		return this.gui;
	}

	@Override
	public Person getSelf() {
		return self;
	}

}
