package agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import agent.Agent;
import simcity201.gui.Bank;
import simcity201.gui.BankSecurityGui;
import simcity201.interfaces.BankCustomer;
import simcity201.interfaces.BankSecurity;
import simcity201.interfaces.BankTeller;
import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;

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
		helpRequested, secured, normal, chickenedOut
	}
	enum State {
		notAtBank, preparingToWork, onDuty, threatEncounter, leaving
	}
	State state = State.notAtBank;
	List<Threat> threats = 
			Collections.synchronizedList(new ArrayList<Threat>());
	
	Semaphore atDest = new Semaphore(0, true);
	private BankSecurityGui gui;
	
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

	@Override
	public void msgAtDestination() {
		atDest.release();
		//stateChanged();
	}
	/*		Scheduler	*/
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		if (state == State.preparingToWork) {
			reportForDuty();
			return true;
		}else if (state == State.leaving && self != null) {
			leaveWork();
		}else if (state == State.onDuty) {
			//movearound();
		}else if (state == State.threatEncounter) {
			//stop();
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
			if( t.s == ThreatState.chickenedOut ) {
				temp = t;
			}
		}
		} if (temp!=null) {getBackToWork(temp); return true; }
		
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
	
	private void leaveWork() {
		state = State.notAtBank;
		gui.DoLeaveBank();
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "im leavin");
		AlertLog.getInstance().logMessage(AlertTag.BANK_Security, this.name, "im leavin");
		bank.leavingWork(this);
		if(this.isWorking == false) {
			if(self.quitWork)
			{
				bank.quitSecurity();
				self.canGetJob = false;
				self.quitWork = false;
				AlertLog.getInstance().logMessage(AlertTag.BANK, self.getName(),"I QUIT BIaCH");
				AlertLog.getInstance().logMessage(AlertTag.BANK_Security, self.getName(),"I QUIT BIaCH");
			
			for(Role r : self.roles)
			{
				if(r.getRole().equals(Role.roles.WorkerSecurityAtChaseBank))
				{
					self.roles.remove(r);
					break;
				}
			}
			}
			
		}
		
		//self.msgDone();
		//self =null;
		
	}

	private void reportForDuty() {
		state = State.onDuty;
		for (BankTeller bt : bank.tellers) {
			bt.securityOnDuty(this);
		}
		gui.DoGoOnDuty();
		print("I am on duty now");
	}
	private void shotRobber(Threat t) {
		state = State.threatEncounter;
		int reply = JOptionPane.showConfirmDialog(null,
				"Is the robbery gonna succeed to rob the bank!?", "Please choose yes or no", JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
//			print("(chicken out)");
			AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "(chicken out)");
			AlertLog.getInstance().logMessage(AlertTag.BANK_Security, this.name, "(chicken out)");
			t.s = ThreatState.chickenedOut;
			t.teller.giveRobberMoney(t.robber);
		}else { //fails (robber fails to rob bank)
//			print("DIE MOTHER FATHER");
			AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "DIE MOTHER FATHER");
			AlertLog.getInstance().logMessage(AlertTag.BANK_Security, this.name, "DIE MOTHER FATHER");
			t.s = ThreatState.secured;
			t.robber.die();
		}
		
		
	}
	private void removeBody(Threat t) {
//		print("cleaned body");
		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "cleaned body");
		AlertLog.getInstance().logMessage(AlertTag.BANK_Security, this.name, "cleaned body");
		t.s = ThreatState.normal;
		t.robber.disappear();
		
	}
	private void notifyTeller(Threat t) {
//		print("you can get back to work");
		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "you can get back to work");
		AlertLog.getInstance().logMessage(AlertTag.BANK_Security, this.name, "you can get back to work");
		threats.remove(t);
		t.teller.robberyIsDown(t.robber);
		
	}
	private void getBackToWork(Threat t) {
		threats.remove(t);
//		print("getting back to work..");
		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "getting back to work..");
		AlertLog.getInstance().logMessage(AlertTag.BANK_Security, this.name, "getting back to work..");
		state = State.onDuty;
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
		if (isWorking) {
			isWorking = false;
			if (bank.customers.isEmpty()) {
				for (BankTellerAgent teller : bank.tellers) {
					teller.goHome();
				}
				state = State.leaving;
			}
			stateChanged();
		}
	}

	@Override
	public void msgLeave() {
		// TODO Auto-generated method stub
		
	}

	public void setGui(BankSecurityGui g) {
		this.gui = g;
	}

	public void setPerson(Person p) {
		this.self = p;
	}
	
	
}
