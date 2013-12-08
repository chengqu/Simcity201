package agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import agent.Agent;
import simcity201.gui.Bank;
import simcity201.interfaces.BankCustomer;
import simcity201.interfaces.BankSecurity;
import simcity201.interfaces.BankTeller;

public class BankSecurityAgent extends Agent implements BankSecurity, Worker {

	
	/*		Data		*/
	int timeIn=0;
	boolean isWorking = true;
	public Person self;
	private String name;
	//BankSecurityGui gui;
	Bank bank;
	
	class Threat {
		BankCustomer robber;
		BankTeller teller;
		ThreatState s;
		Threat(BankCustomer robber, BankTeller teller, ThreatState s) {
			this.robber = robber;
			this.teller = teller;
			this.s = s;
		}
	}
	enum ThreatState {
		helpRequested, secured, normal
	}
	enum State {
		notAtBank, preparingToWork, onDuty
	}
	State state = State.notAtBank;
	List<Threat> threats = 
			Collections.synchronizedList(new ArrayList<Threat>());
	
	/*		Messages	*/

	public void youAreAtWork(Person person) {
		self = person;
		state = State.preparingToWork;
		isWorking=true;
		stateChanged();
	}
	
	public void helpMe(BankCustomer c, BankTeller t) {
		threats.add(new Threat(c, t, ThreatState.helpRequested));
		stateChanged();
	}
	
	/*		Scheduler	*/
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		if (state == State.preparingToWork) {
			reportForDuty();
			return true;
		}else if (state == State.onDuty) {
			
		}
		
		Threat temp = null;
		
		synchronized(threats) {
		for (Threat t : threats) {
			if( t.s == ThreatState.helpRequested ) {
				temp = t;
			}
		}
		} if (temp!=null) {shotRobber(temp); return true; }
		
		synchronized(threats) {
		for (Threat t : threats) {
			if( t.s == ThreatState.secured ) {
				temp = t;
			}
		}
		} if (temp!=null) {removeBody(temp); return true; }
		
		synchronized(threats) {
		for (Threat t : threats) {
			if( t.s == ThreatState.normal ) {
				temp = t;
			}
		}
		} if (temp!=null) {notifyTeller(temp); return true; }
		
		
		
		return false;
	}
	
	/*		Action		*/
	
	private void reportForDuty() {
		state = State.onDuty;
		for (BankTeller bt : bank.tellers) {
			bt.securityOnDuty(this);
		}
		print("I am on duty now");
	}
	private void shotRobber(Threat t) {
		print("DIE MOTHER FATHER");
		t.s = ThreatState.secured;
		int reply = JOptionPane.showConfirmDialog(null,
				"Is the robbery gonna succeed to rob the bank!?", "Please choose yes or no", JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			
		}else { //fails
			t.robber.die();
		}
		
		
	}
	private void removeBody(Threat t) {
		print("cleaned body");
		t.s = ThreatState.normal;
		t.robber.disappear();
		
	}
	private void notifyTeller(Threat t) {
		print("you can get back to work");
		threats.remove(t);
		t.teller.robberyIsDown(t.robber);
		
	}
	
	/*		Utilities	*/
	public BankSecurityAgent(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public boolean isWorking() {
		return isWorking;
	}
	public Person getPerson() {
		return this.self;
	}
	@Override
	public void setTimeIn(int timeIn) {
		this.timeIn = timeIn;
	}
	@Override
	public int getTimeIn() {
		return timeIn;
	}
	@Override
	public void goHome() {
		isWorking = false;
	}

	
	
}
