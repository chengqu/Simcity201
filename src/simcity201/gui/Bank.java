package simcity201.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.*;

import simcity201.interfaces.BankCustomer;
import simcity201.interfaces.BankSecurity;
import simcity201.interfaces.BankTeller;
import simcity201.test.mock.EventLog;
import simcity201.test.mock.LoggedEvent;
import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;
import Buildings.Building;
import agents.BankCustomerAgent;
import agents.BankDatabase;
import agents.BankSecurityAgent;
import agents.BankTellerAgent;
import agents.Person;
import agents.Role;
import agents.Role.roles;
import agents.Worker;
import animation.BaseAnimationPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Bank extends Building implements ActionListener {

	public EventLog log = new EventLog();
	
	
	public Vector<BankCustomerAgent> customers = new Vector<BankCustomerAgent>();
	public Vector<BankTellerAgent> tellers = new Vector<BankTellerAgent>();
	public Vector<BankSecurityAgent> securities = new Vector<BankSecurityAgent>();
	
	public BankDatabase db = new BankDatabase();
	public BankAnimationPanel bap = new BankAnimationPanel(this);
	
	private BankMap map = new BankMap();
	
	private final static int MAX_LINE = 15;
	private List<BankCustomer> pplOnLine =
			Collections.synchronizedList(new ArrayList<BankCustomer>(MAX_LINE));
	private int line_count = 0;
	
	final int wageHourInMili = 3000;
	public int internalClock = 0;
	int wage = 20;// $20/hr
	
	public Timer wageTimer = new Timer(wageHourInMili, this);
	
	public List<Worker> workers =
			Collections.synchronizedList(new ArrayList<Worker>());
	
	public int workerNumber = 0;
	public boolean isOpen = false;
	public boolean isClosing = false;
	
	public boolean haveSecurity = true;
	public int num_tellers = 2;
	public int MAX_TELLERS = 7;
	
	
	// Temp*****
	
	//private JFrame frame = new JFrame("Bank");
	
	// *********
	
	public Bank() {
		bap.setMap(map);
		bap.setPreferredSize(new Dimension(BankAnimationPanel.WINDOWX, BankAnimationPanel.WINDOWY));
		bap.setMinimumSize(new Dimension(BankAnimationPanel.WINDOWX, BankAnimationPanel.WINDOWY));
		bap.setMaximumSize(new Dimension(BankAnimationPanel.WINDOWX, BankAnimationPanel.WINDOWY));
		bap.setVisible(true);
		wageTimer.setActionCommand("InternalTick");
		wageTimer.start();
		
	}
	
	/**
	 * shared data: line
	 * the iAmOnLine will make you to go to SLEEP, doing something else while waiting
	 * TODO:send out messages from this to the customer gui ..
	 * @param BankCustomer bc
	 */
	synchronized public void iAmOnLine(BankCustomer bca) {
		while (line_count == MAX_LINE) {
			try {
				//System.out.println("\tFull, Waiting");
				wait(5000); 	// Full, wait to add
			}catch(InterruptedException ex) {
			}
		}
		
		pplOnLine.add(bca);
		line_count++;
		if (line_count == 1) {
			//System.out.println("\tNot Empty, notify");
			notify();		//notify a waiting bank teller
		}
	}
	synchronized public BankCustomer whoIsNextOnLine(BankTeller teller) {
		while (line_count == 0) {
			try {
				//System.out.println("\tEmpty, waiting");
				if (teller.isWorking()) {
					wait(5000);
				}else {
					// if not working, return null
					return null;
				}
			}catch(InterruptedException ex) {};
		}
		if (teller.isWorking()) {
			BankCustomer bca = pplOnLine.remove(0);
			line_count--;
			if (line_count == MAX_LINE-1) {
				//System.out.println("\tNot full, notify");
				notify();		//notify customer waiting outside
			}
			AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, bca.getName() + " is now being served");
			return bca;
		}else {
			return null;
		}
	}
	
	public void addCustomer(Person person) {
		
		if (isClosing) {
			AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "We are closing, sorry");
			person.msgDone();
			//reject
			return;
		}
		
		if(!isOpen) {
			AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "We are closed, sorry");
			person.msgDone();
			//reject
			return;
		}
		
		BankCustomer existingCustomer = null;
		for(BankCustomerAgent bca : customers) {
			if (bca.self.equals(person)) {
				existingCustomer = bca;
			}
		}
		if (existingCustomer != null) {
			existingCustomer.youAreInside(person);
		}else {
			BankCustomerAgent bca = new BankCustomerAgent(person.getName());
			bca.self = person;
			BankCustomerGui g = new BankCustomerGui(bca, map, bap);
			
			bap.addGui(g);
			bca.setBank(this);
			bca.setGui(g);
			customers.add(bca);
			bca.startThread();
			bca.youAreInside(person);
		}
	}
	public void addWorker(Person person) {
		
		if(isClosing) {
			AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "You are too late, we are already closing");
			person.msgDone();
			return;
		}
		
		Role role = null;
		
		for (Role r : person.roles) {
			if(r.getRole() == roles.WorkerTellerAtChaseBank || 
					r.getRole() == roles.WorkerSecurityAtChaseBank) {
				role = r;
				break;
			}
		}
		if (role == null) {
			log.add(new LoggedEvent("should not get here"));
			AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Should not get here");
			return;
		}
		
		if (role.getRole() == roles.WorkerTellerAtChaseBank) {
			log.add(new LoggedEvent("teller added"));
			AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Teller added");
			
//			BankTellerAgent existingTeller = null;
//			for(BankTellerAgent bta : tellers) {
//				if (bta.self.equals(person)){
//					existingTeller = bta;
//				}
//			}
//			if(existingTeller != null) {
//				existingTeller.youAreAtWork(person);
//				workers.add(existingTeller);
//			}else {
				BankTellerAgent bta = new BankTellerAgent(person.getName());
				BankTellerGui g = new BankTellerGui(bta, map, bap);
				
				bap.addGui(g);
				bta.setGui(g);
				bta.setBank(this);
				bta.setDB(this.db);
				tellers.add(bta);
				bta.startThread();
				if (isOpen) {
					bta.youAreAtWork(person);
				}else {
					bta.setPerson(person);
				}
				if (isOpen) {
					bta.setTimeIn(internalClock);
				}
				//bta.setTimeIn(internalClock);
				workerNumber++;
				synchronized(workers) {
				workers.add(bta);
				for (Worker w : workers) {
					if (w instanceof BankSecurity) {
						bta.securityOnDuty((BankSecurity)w);
					}
				}
				}
//			}
		}else if(role.getRole() == roles.WorkerSecurityAtChaseBank) {
			log.add(new LoggedEvent("security added"));
			AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Security added");
			
//			BankSecurityAgent existingSecurity = null;
//			for(BankSecurityAgent bsa : securities) {
//				if(bsa.self.equals(person)) {
//					existingSecurity = bsa;
//				}
//			}
//			if (existingSecurity != null) {
//				existingSecurity.youAreAtWork(person);
//				workers.add(existingSecurity);
//			}else {
				BankSecurityAgent bsa = new BankSecurityAgent(person.getName());
				BankSecurityGui g = new BankSecurityGui(bsa, map, bap);
				
				bap.addGui(g);
				bsa.setGui(g);
				bsa.setBank(this);
				securities.add(bsa);
				bsa.startThread();
				if (isOpen) {
					bsa.youAreAtWork(person);
				}else {
					bsa.setPerson(person);
				}
				synchronized(workers) {
				workers.add(bsa);
				workerNumber++;
				for (Worker w : workers) {
					if (w instanceof BankTeller) {
						((BankTeller) w).securityOnDuty(bsa);
					}
				}
				}
//			}
		}
		int num_teller = 0;
		int num_security = 0;
		synchronized (workers) {
		for (Worker w : workers) {
			if (w instanceof BankTeller) {
				num_teller ++;
			}else if (w instanceof BankSecurity) {
				num_security ++;
			}
		}
		}
		if (num_teller >= 1 && num_security >=1) {
			openBank();
		}
		
		
	}
	
	public void openBank() {
		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "We are open");
		isOpen = true;
		isClosing = false;
		//isClosing = false;
		synchronized(workers) {
		for(Worker w : workers) {
			w.setTimeIn(internalClock);
			if(w instanceof BankSecurity) {
				((BankSecurityAgent)w).youAreAtWork(w.getPerson());
				w.setTimeIn(internalClock);
			}else if(w instanceof BankTeller){
				((BankTellerAgent)w).youAreAtWork(w.getPerson());
				w.setTimeIn(internalClock);
			}
		}
		}
	}
	
	public void leavingWork(Worker w) {
		log.add(new LoggedEvent("leaving work"));
		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, w.getPerson().getName() + " is leaving work");
		w.getPerson().payCheck += (internalClock - w.getTimeIn()) * wage;
		synchronized(workers) {
			workers.remove(w);
		}
		if(w instanceof BankTeller) {
			tellers.remove((BankTeller)w);
		}else if (w instanceof BankSecurity ){
			securities.remove((BankSecurity)w);
		}
		
		if (workers.isEmpty()) {
			isOpen = false;
			isClosing = false;
			workerNumber = 0;
			
		}
		//if there exist worker in waiting workers such that worker instanceof ...
	}
	
	public void customerLeaving(BankCustomer c) {
		customers.remove(c);
	}
	
	/*
	public static void main(String[] args) {
		Bank bank = new Bank();
		bank.frame.setVisible(true);
		bank.frame.setSize(bank.bap.getSize());
		bank.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bank.bap.setVisible(true);
		bank.frame.add(bank.bap);
		
		Person person = new Person("Customer");
		Person person2 = new Person("Teller");
		person.money = 10;
		//person.paycheck = 1000;
		//person.startThread();
		
		//BankCustomerAgent customer = new BankCustomerAgent("Customer");
		//BankTellerAgent teller = new BankTellerAgent("Teller");
		//customer.setBank(bank);
		//teller.setBank(bank);
		//teller.setDB(bank.db);
		
		//customer.startThread();
		//teller.startThread();
		
		//teller.youAreAtWork(person);
		//customer.youAreInside(person);
		bank.addTeller(person2);
		bank.addCustomer(person);
	}
*/
	@Override
	public BaseAnimationPanel getAnimationPanel() {
		return this.bap;
	}
	
	public BankDatabase getDatabase() {
		return this.db;
	}
	public BankMap getBankMap() {
		return this.map;
	}

	
	int closingCount = 0;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//if (arg0.getSource() == wageTimer) {
		boolean tempIsClosing = false;
		
		if(arg0.getActionCommand().equals("InternalTick")) {
			internalClock+= 1;
			//if (workers.size() > 1) {
			
			if (isClosing && !workers.isEmpty()) {
				synchronized(workers) {
				for(Worker w : workers) {
					w.goHome();
				}
				}
			}
			
			if (!securities.isEmpty() && isOpen) {
				BankSecurityAgent security = securities.get(0);
				if (internalClock - security.getTimeIn() > 30 && !isClosing) {
					AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Let's close!");
					tempIsClosing = true;
					if (closingCount%3 == 0) {
						closingCount = 0;
						flushCustomers();
					}
				}
			
			
			//}
				if(tempIsClosing && customers.isEmpty() && isOpen) {
					tempIsClosing = false;
					isClosing = true;
					//isOpen = false;
					AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Go home guys, send all customers out");
					synchronized(workers) {
					for(Worker w : workers) {
						w.goHome();
					}
					}
				}
			}
		}
	}
	
	public void flushCustomers() {
		for(BankCustomer bc : customers) {
			bc.leave();
		}
		customers.clear();
	}

	@Override
	synchronized public Role wantJob(Person p) {
		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "I want a JOB " + p.getName());
		if(!haveSecurity)
		{			
			haveSecurity = true;
			AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Youre security now!!");
			return new Role(Role.roles.WorkerSecurityAtChaseBank, this.name);
		}
		else if(num_tellers < MAX_TELLERS)
		{
			num_tellers++;
			AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Youre teller now!!");
			return new Role(Role.roles.WorkerTellerAtChaseBank, this.name);
		}
		else
		{
			AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "No jobs for ya");
			return null;
		}
	}

	public void quitSecurity() {
		haveSecurity = false;
		GlobalMap.getGlobalMap().addJob(this);
		AlertLog.getInstance().logMessage(AlertTag.BANK, this.name, "Got no security, anyone with gun?");
	}
	
	public void getJobs() {
		for(int i = num_tellers; i < MAX_TELLERS; i++) {
			GlobalMap.getGlobalMap().addJob(this);
		}
		if (!haveSecurity) {
			GlobalMap.getGlobalMap().addJob(this);
		}
	}
	

	
}
