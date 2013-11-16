package agents;

import java.util.*;


public class BankDatabase {
	
	
	
	private static final BankDatabase singleton_db = new BankDatabase();
	
	private BankDatabase() {
	}
	
	public static BankDatabase getDB() {
		return singleton_db;
	}
	

	public void insertAccount(Account acc) {
		
	}
	public Account searchAccount(int accountNumber) {
		Account acc = null;
		return acc;
	}
	public List<Account> ssnSearch(String ssn) {
		
		return new ArrayList<Account>();
	}
	
}

