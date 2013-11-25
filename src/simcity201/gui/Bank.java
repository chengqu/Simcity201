package simcity201.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.*;

import Buildings.Building;
import agents.BankCustomerAgent;
import agents.BankDatabase;
import agents.BankTellerAgent;
import agents.Person;
import animation.BaseAnimationPanel;

import java.awt.*;
import java.util.List;

public class Bank extends Building{

	private BankAnimationPanel bap = new BankAnimationPanel();
	
	private Vector<BankCustomerAgent> customers = new Vector<BankCustomerAgent>();
	private Vector<BankTellerAgent> tellers = new Vector<BankTellerAgent>();
	
	private BankDatabase db = new BankDatabase();
	
	private BankMap map = new BankMap();
	
	private final static int MAX_LINE = 10;
	private List<BankCustomerAgent> pplOnLine =
			Collections.synchronizedList(new ArrayList<BankCustomerAgent>(MAX_LINE));
	private int line_count = 0;
	
	
	// Temp*****
	
	//private JFrame frame = new JFrame("Bank");
	
	// *********
	
	public Bank() {
		
	}
	
	/**
	 * shared data: line
	 * the iAmOnLine will make you to go to SLEEP, doing something else while waiting
	 * TODO:send out messages from this to the customer gui ..
	 * @param BankCustomer bc
	 */
	synchronized public void iAmOnLine(BankCustomerAgent bca) {
		while (line_count == MAX_LINE) {
			try {
				System.out.println("\tFull, Waiting");
				wait(5000); 	// Full, wait to add
			}catch(InterruptedException ex) {
			}
		}
		
		pplOnLine.add(bca);
		line_count++;
		if (line_count == 1) {
			System.out.println("\tNot Empty, notify");
			notify();		//notify a waiting bank teller
		}
	}
	synchronized public BankCustomerAgent whoIsNextOnLine() {
		while (line_count == 0) {
			try {
				System.out.println("\tEmpty, waiting");
				wait(5000);
			}catch(InterruptedException ex) {};
		}
		
		BankCustomerAgent bca = pplOnLine.remove(0);
		line_count--;
		if (line_count == MAX_LINE-1) {
			System.out.println("\tNot full, notify");
			notify();		//notify customer waiting outside
		}
		System.out.println(bca.getName() +" removed from line");
		return bca;
	}
	
	public void addCustomer(Person person) {
		BankCustomerAgent existingCustomer = null;
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
			customers.add(bca);
			bca.startThread();
			bca.youAreInside(person);
		}
	}
	public void addTeller(Person person) {
		BankTellerAgent existingTeller = null;
		for(BankTellerAgent bta : tellers) {
			if (bta.self.equals(person)){
				existingTeller = bta;
			}
		}
		if(existingTeller != null) {
			existingTeller.youAreAtWork(person);
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
		}
	}
	
	/*
	public static void main(String[] args) {
		Bank bank = new Bank();
		//bank.frame.setVisible(true);
		//bank.frame.setSize(new Dimension(400,400));
		//bank.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Person person = new Person("Customer");
		person.money = 10;
		person.paycheck = 1000;
		person.startThread();
		
		BankCustomerAgent customer = new BankCustomerAgent("Customer");
		BankTellerAgent teller = new BankTellerAgent("Teller");
		customer.setBank(bank);
		teller.setBank(bank);
		teller.setDB(bank.db);
		
		customer.startThread();
		teller.startThread();
		
		teller.youAreAtWork(person);
		customer.youAreInside(person);
		
	}*/

	@Override
	public BaseAnimationPanel getAnimationPanel() {
		return this.bap;
	}
	
}