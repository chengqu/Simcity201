package agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Agent;

public class BankCustomerAgent extends Agent {

	/*		Data		*/
	
	String name;
	Person self;
	public enum AgentState{ entering, waitingOnLine, determining, goingToATM, leaving, atATM, atTeller, waitingForResponse, dead }
	public enum AgentEvent{ none, doneEntering, doneWaitingOnLine, gotShot, ATMResponded, tellerResponded, asked }
	
	AgentState state = AgentState.entering;
	List<AgentEvent> events = new ArrayList<AgentEvent>(); // no synchronization required
	AgentEvent event = AgentEvent.none;
	
	class Task {
		Objective obj;
		TaskState s;
		float amount;
		Account acc;
		Account.AccountType type;
	}
	
	public enum Objective { toMakeAccount, toLoan, toDeposit, toWithdraw }
	public enum TaskState { toDo, pending, done, rejected } 
	
	List<Task> tasks = Collections.synchronizedList(new ArrayList<Task>());
	
	BankTellerAgent teller;
	BankATMAgent atm;
	
	Bank bank;
	
	
	/*		Messages		*/

	public void youAreInside() { // called by Bank after creation of BankCustomer instance
		events.add (AgentEvent.doneEntering);
		stateChanged();
	}
	
	public void nextOnLine(BankTellerAgent teller) {
		events.add (AgentEvent.doneWaitingOnLine);
		this.teller = teller;
		stateChanged();
	}
	
	public void anythingElse() {
		events.add (AgentEvent.asked);
		stateChanged();
	} 
	
	public void hereIsYourAccount(Account account) {
		synchronized( tasks ) {
		for (Task t : tasks) {
			if (t.obj == Objective.toMakeAccount) {
				t.acc = account;
				t.s = TaskState.done;
			}
		}//tasks
		}//sync
		stateChanged();
	}
	public void unableToMakeAccount(String reason) {
		//TODO: deal with the reason in v2.2
		synchronized( tasks ) {
		for (Task t : tasks) {
			if (t.obj == Objective.toMakeAccount) {
				t.s = TaskState.rejected;
			}
		}//tasks
		}//sync
		stateChanged();
	}
	public void transaction(boolean isSuccess, String reason) { 
		//TODO: deal with the reason in v2.2
		events.add(AgentEvent.tellerResponded);
		
		synchronized( tasks ) {
		for (Task t : tasks) {
			if (t.s == TaskState.pending) {
				if (isSuccess)
					t.s = TaskState.done;
				else
					t.s = TaskState.rejected;
			}
		}//tasks
		}//sync
		stateChanged();
	}
		
	public void ATMtransaction(boolean isSuccess, String reason) { 
		events.add(AgentEvent.ATMResponded);
		synchronized( tasks ) {
		for (Task t : tasks) {
			if (t.s == TaskState.pending) {
				if (isSuccess)
					t.s = TaskState.done;
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
			if (t.s == TaskState.pending) {
				if (isApproved)
					t.s = TaskState.done;
				else
					t.s = TaskState.rejected;
			}
		}//tasks
		}//sync
		stateChanged();
	}
	public void die() {
		events.add(AgentEvent.gotShot);
		stateChanged();
	}
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		if (!events.isEmpty()) {
			event = events.remove(0);
			// Do I care about any messages that can be 'not read' ? 
			// I have states that I expect messages, I ignore any random incoming messages
		}
		
		if (event == AgentEvent.gotShot) {
			goDead();
			return false;	//false because I am dead
		}
		
		if (event == AgentEvent.ATMResponded) {
			state = AgentState.atATM;	//I do not return true here, that will cause overwritting
		}
		
		if (event == AgentEvent.tellerResponded) {
			state = AgentState.atTeller;//I do not return true here, that will cause overwritting
		}
		
		if (state == AgentState.entering && event == AgentEvent.doneEntering) {
			determineWhatINeed();
		}
		
		if (state == AgentState.goingToATM && !tasks.isEmpty()) {
			goToATM();
		}

		synchronized( tasks ) {
		for (Task t : tasks) {
			if (t.s == TaskState.done) {
				update();
				return true;
			}
		}//tasks
		}//sync
		
		if (state == AgentState.atATM) {
			synchronized( tasks ) {
			for (Task t : tasks) {
				if (t.s == TaskState.toDo) {
					if (t.obj == Objective.toDeposit) {
						makeDeposit(t);
						return true;
					}
					if (t.obj == Objective.toWithdraw) {
						withdrawMoney(t);
						return true;
					}
				}
			}//tasks
			}//sync
		}
		

		if (state == AgentState.waitingOnLine && event == AgentEvent.doneWaitingOnLine) {
			if(!tasks.isEmpty()) {
				talkToTeller();
			}
		}
		
		if (state == AgentState.atTeller) {
			synchronized( tasks ) {
			for (Task t : tasks) {
				if (t.s == TaskState.toDo) {
					if (t.obj == Objective.toMakeAccount) {
						makeAccount(t);
					}
					if (t.obj == Objective.toDeposit) {
						makeDeposit(t);
						return true;
					}
					if (t.obj == Objective.toWithdraw) {
						withdrawMoney(t);
						return true;
					}
					if (t.obj == Objective.toLoan) {
						loanMoney(t);
					}
				}
			}//tasks
			}//sync
		}
		
		synchronized( tasks ) {
		for (Task t : tasks) {
			if (t.s == TaskState.toDo || t.s == TaskState.pending) {
				return false; // he has something to do or waiting for response
			}
		}//tasks
		}//sync
		
		if (tasks.isEmpty() && state != AgentState.leaving) {
			leaveBank();
		}
		
		return false;
	}

	private void goDead() {
		print("I just wanted to get 5 bucks . . . dead");
	}
	private void determineWhatINeed() {
		state = AgentState.determining;
		boolean isRobbery = false;
		//if role is robbery, make isRobbery true
		
		if (!isRobbery) {
			// if self.accounts is empty
			// 		tasks.add (new Task(toMakeAccount, both));
			// else
			// 		check each accounts and if amount >= low && cash < low,
			//			then tasks.add( new Task(toWithdraw, 2*low, acc);
			//				 return
			
			// if paycheck
		}
		
		//DoGoOnLine();
		bank.iAmOnLine(this);
		state = AgentState.waitingOnLine;
		print("I am going on the line");
	}
	private void goToATM() {
		state = AgentState.atATM;
		//DoGoToATM();
		print("I'm just gonna go to ATM");
	}
	private void update() {
		print("updating...");
	}
	private void makeDeposit(Task t) {
		print("I'd like to deposit");
	}
	private void withdrawMoney(Task t) {
		print("I'd like to withdraw my money");
	}
	private void talkToTeller() {
		state = AgentState.atTeller;
		print("What's up teller");
		//DoGoToTeller();
	}
	private void makeAccount(Task t) {
		print("I'd like to make new account");
	}
	private void loanMoney(Task t) {
		print("Let me loan some money");
	}
	private void leaveBank() {
		//self.done();
		state = AgentState.leaving;
		print("leavin");
	}
	
	
	
	/*		Utilities 		*/
	public BankCustomerAgent(String name) {
		this.name = name;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}

}
