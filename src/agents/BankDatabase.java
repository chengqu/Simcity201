package agents;

import java.util.*;

import simcity201.gui.GlobalTime;

public class BankDatabase implements GlobalTime {
	
	/* TODO	you need to find out how & when to call updatePending()
	 * */
	
	public Set<Account> accounts =
			Collections.synchronizedSet(new HashSet<Account>());
	public Map<Integer, Account> accNumberMap = new HashMap<Integer, Account>();
	public Map<Integer, ArrayList<Account>> snnMap = new HashMap<Integer, ArrayList<Account>>();
	
	//private static final BankDatabase singleton_db = new BankDatabase();

	public float budget = 0;
	public final static float loanInterestRate = 0.00001f;
	
	public List<Loan> loans = new ArrayList<Loan>();
	public class Loan {
		float amount;
		Person loaner;
		int weekBorrowed;
		Loan(float amount, Person loaner) {
			this.amount = amount;
			this.loaner = loaner;
			this.weekBorrowed = week;
		}
	}
	public int week = 0;
	
	public BankDatabase() {

		budget = 10000000;
	}
	/*
	public static BankDatabase getDB() {
		return singleton_db;
	}*/
	

	public void insertAccount(Account acc) {
		accounts.add(acc);
		accNumberMap.put(acc.getAccountNumber(), acc);
		if (snnMap.containsKey(acc.getCustomerSSN())) {
			snnMap.get(acc.getCustomerSSN()).add(acc);
		}else {
			ArrayList<Account> temp = new ArrayList<Account>();
			temp.add(acc);
			snnMap.put(acc.getCustomerSSN(), temp);
		}
	}
	public Account searchAccount(int accountNumber) {
		return accNumberMap.get(accountNumber);
	}
	public ArrayList<Account> ssnSearch(int ssn) {
		return snnMap.get(ssn);
	}
	
	public synchronized boolean updateBudget(float amount) {
		if (budget + amount < 0) {
			return false;
		}
		budget += amount;
		return true;
	}
	
	public void updateLoan(float amount, Person customer) {
		loans.add(new Loan(amount, customer));
	}

	@Override
	public void dayPassed() {
		// TODO Auto-generated method stub
		synchronized (accounts) {
		for (Account acc : accounts) {
			acc.update();
		}
		}
	}

	@Override
	public void weekPassed() {
		week++;
		
		for (Loan l : loans) {
			if (l.weekBorrowed+4 <= week && week % 4 == 0) {
				// month came and it's not the month that he borrowed loan
				l.amount += l.amount*loanInterestRate;
			}
		}
	}

}

