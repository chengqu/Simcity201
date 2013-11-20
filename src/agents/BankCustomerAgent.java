package agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Agent;

public class BankCustomerAgent extends Agent {

	/*		Data		*/
	
	String name;
	Person self;
	
	class Task {
		Objective obj;
		TaskState s;
		float amount;
		Account acc;
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
		Task(Objective obj, float amount, Account acc, TaskState s) {
			this.obj = obj;
			this.amount = amount;
			this.acc = acc;
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
	
	List<Task> tasks = Collections.synchronizedList(new ArrayList<Task>());
	
	BankTellerAgent teller;
	BankATMAgent atm;
	
	Bank bank;
	boolean isPresentInBank = false;
	
	/*		Messages		*/

	public void youAreInside(Person p) { // called by Bank after creation of BankCustomer instance
		tasks.add(new Task(Objective.toWaitOnLine, TaskState.toDo));
		print("you are inside of bank");
		isPresentInBank = true;
		self = p;
		stateChanged();
	}
	public void nextOnLine(BankTellerAgent teller) {
		this.teller = teller;
		tasks.add(new Task(Objective.toApproachTeller, TaskState.toDo));
		print("done waiting on line");
		stateChanged();
	}
	public void nextOnLine(BankATMAgent atm) {
		this.atm = atm;
		tasks.add(new Task(Objective.toApproachATM, TaskState.toDo));
		print("done waiting on line");
		stateChanged();
	}
	public void howMayIHelpYou() {
		tasks.add(new Task(Objective.toDetermineWhatINeed, TaskState.toDo));
		stateChanged();
	}
	public void hereIsYourAccount(Account account) {
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
		tasks.add(new Task(Objective.toDie, TaskState.toDo));
		stateChanged();
	}
	public void anythingElse() {
		tasks.add(new Task(Objective.toLeave, TaskState.toDo));
		stateChanged();
	} 
	
	/* 		Scheduler 		*/
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		
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
		print("I am on line now");
		bank.iAmOnLine(this);
	}
	private void approachTeller(Task t) {
		//DoApproachTeller();
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
		if (self.accounts.isEmpty()) {
			tasks.add(new Task(Objective.toMakeAccount, Account.AccountType.Saving, TaskState.toDo));
			print("task: create new account");
			// I want to create new account
		}
		float totalMoney = self.money + self.paycheck;
		boolean wFlag = false;
		Account checking = null;
		Account saving = null;
		for (Account acc : self.accounts) {
			totalMoney += acc.getBalance();
			if (acc.getBalance() >= 2*self.cashLowThreshold &&
					self.money < self.cashLowThreshold &&
					!wFlag) {
				// I want to withdraw
				print("task: withdrawal");
				tasks.add(new Task(Objective.toWithdraw, 2*self.cashLowThreshold, acc, TaskState.toDo));
				wFlag = true;
			}
			if (acc.getType() == Account.AccountType.Checking) {
				checking = acc;
			}else if (acc.getType() == Account.AccountType.Saving) {
				saving = acc;
			}
		}
		
		if (self.paycheck >= self.paycheckThreshold && saving!=null) {
			// I want to deposit
			tasks.add(new Task(Objective.toDeposit, self.paycheck, saving, TaskState.toDo));
			print("task: depoist");
		}
		
		if (totalMoney < self.enoughMoneyToBuyACar && self.wantCar) {
			// I want loan
			/*IDEA: when loan is approved, you make checking, and checking will have the borrowed money*/
			tasks.add(new Task(Objective.toLoan, self.enoughMoneyToBuyACar-totalMoney, TaskState.toDo));
			print("task: loan");
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
		teller.iNeedAccount(this, name, "535 S", "123456789", t.type);
		print("I need to create an account");
	}
	private void depositMoney(Task t) {
		t.s = TaskState.pending;
		if (teller != null) {
			teller.iWantToDeposit(this, t.amount, t.acc);
		}else {
			//atm.deposit(this, t.amount, t.acc);
		}
		print("I want to deposit my money");
	}
	private void withdrawMoney(Task t) {
		t.s = TaskState.pending;
		if (teller != null) {
			teller.iWantToWithdraw(this, t.amount, t.acc);
		}else {
			//atm.withdraw(this, t.amount, t.acc);
		}
		print("I'd like to withdraw my money");
	}
	private void loanMoney(Task t) {
		t.s = TaskState.pending;
		//teller.iWantToLoan(this, t.amount, self.roles.getJob());
		print("Let me loan some money");
	}
	private void update(Task t) {
		
		if (t.obj == Objective.toMakeAccount) {
			self.accounts.add(t.acc);
		}else if (t.obj == Objective.toDeposit) {
			self.paycheck -= t.amount;
			//t.acc.setTotal(t.acc.getBalance() +t.amount); same pointer, not needed
			//self.accounts.updateAccount(t.acc); // stub
		}else if (t.obj == Objective.toWithdraw) {
			self.money += t.amount;
			//t.acc.setTotal(t.acc.getTotal() - t.amount);
			//self.accounts.updateAccount(t.acc); // stub
		}else if (t.obj == Objective.toLoan) {
			//		self.cash += t.amount;
		}
		tasks.remove(t);
		print("updated");
		
	}
	private void dealWithRejection(Task t) {
		tasks.remove(t);
		print("u rejected me? hell");
	}
	private void leaveBank(Task t) {
		tasks.remove(t);
		if (tasks.isEmpty()) {
			if (teller != null){
				teller.noThankYou(this);			
			}else {
				//atm.noThankYou(this);
			}
		}
		if (isPresentInBank) {
			isPresentInBank = false;
			print("no thank you, im leavin");
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

}
