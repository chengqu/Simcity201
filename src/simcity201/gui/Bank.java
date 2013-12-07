package simcity201.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.*;

import simcity201.interfaces.BankCustomer;
import simcity201.interfaces.BankTeller;
import simcity201.test.mock.EventLog;
import simcity201.test.mock.LoggedEvent;
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
	
	private BankAnimationPanel bap = new BankAnimationPanel();
	
	public Vector<BankCustomerAgent> customers = new Vector<BankCustomerAgent>();
	public Vector<BankTellerAgent> tellers = new Vector<BankTellerAgent>();
	public Vector<BankSecurityAgent> securities = new Vector<BankSecurityAgent>();
	
	private BankDatabase db = new BankDatabase();
	
	private BankMap map = new BankMap();
	
	private final static int MAX_LINE = 15;
	private List<BankCustomer> pplOnLine =
			Collections.synchronizedList(new ArrayList<BankCustomer>(MAX_LINE));
	private int line_count = 0;
	
	final int wageHour = 3000;
	int internalClock = 0;
	int wage = 20;// $20/hr
	
	Timer wageTimer = new Timer(wageHour, this);
	
	public List<Worker> workers =
			Collections.synchronizedList(new ArrayList<Worker>());
	
	
	// Temp*****
	
	//private JFrame frame = new JFrame("Bank");
	
	// *********
	
	public Bank() {
		bap.setMap(map);
		bap.setPreferredSize(new Dimension(BankAnimationPanel.WINDOWX, BankAnimationPanel.WINDOWY));
		bap.setMinimumSize(new Dimension(BankAnimationPanel.WINDOWX, BankAnimationPanel.WINDOWY));
		bap.setMaximumSize(new Dimension(BankAnimationPanel.WINDOWX, BankAnimationPanel.WINDOWY));
		bap.setVisible(true);
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
	synchronized public BankCustomer whoIsNextOnLine() {
		while (line_count == 0) {
			try {
				//System.out.println("\tEmpty, waiting");
				wait(5000);
			}catch(InterruptedException ex) {};
		}
		
		BankCustomer bca = pplOnLine.remove(0);
		line_count--;
		if (line_count == MAX_LINE-1) {
			//System.out.println("\tNot full, notify");
			notify();		//notify customer waiting outside
		}
		System.out.println(bca.getName() +" removed from line");
		return bca;
	}
	
	public void addCustomer(Person person) {
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
			BankCustomerGui g = new BankCustomerGui(bca, map);
			
			bap.addGui(g);
			bca.setBank(this);
			bca.setGui(g);
			customers.add(bca);
			bca.startThread();
			bca.youAreInside(person);
		}
	}
	public void addWorker(Person person) {
		Role role = null;
		
		for (Role r : person.roles) {
			if(r.getRole() == roles.TellerAtChaseBank || 
					r.getRole() == roles.SecurityAtChaseBank) {
				role = r;
				break;
			}
		}
		if (role == null) {
			log.add(new LoggedEvent("should not get here"));
			return;
		}
		
		if (role.getRole() == roles.TellerAtChaseBank) {
			log.add(new LoggedEvent("teller added"));
			
			BankTellerAgent existingTeller = null;
			for(BankTellerAgent bta : tellers) {
				if (bta.self.equals(person)){
					existingTeller = bta;
				}
			}
			if(existingTeller != null) {
				existingTeller.youAreAtWork(person);
				workers.add(existingTeller);
			}else {
				BankTellerAgent bta = new BankTellerAgent(person.getName());
				BankTellerGui g = new BankTellerGui(bta, map);
				
				bap.addGui(g);
				bta.setGui(g);
				bta.setBank(this);
				bta.setDB(this.db);
				tellers.add(bta);
				bta.startThread();
				bta.youAreAtWork(person);
				workers.add(bta);
			}
		}else if(role.getRole() == roles.SecurityAtChaseBank) {
			log.add(new LoggedEvent("security added"));
		}
	}
	
	public void leavingWork(Worker w) {
		w.getPerson().payCheck += (internalClock - w.getTimeIn()) * wage;
		workers.remove(w);
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == wageTimer) {
			internalClock+= 2;
			if (workers.size() > 1) {
				for(Worker w : workers) {
					if (internalClock - w.getTimeIn() > 30) {
						w.goHome();
						break;
					}
				}
			}
		}
	}
	
	

	
}
