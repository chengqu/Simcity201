package agents;

import java.util.*;


public class BankDatabase {
	
	/* TODO	you need to find out how & when to call updatePending()
	 * */
	
	Set<Account> accounts =
			Collections.synchronizedSet(new HashSet<Account>());
	Map<Integer, Account> accNumberMap = new HashMap<Integer, Account>();
	Map<Integer, ArrayList<Account>> snnMap = new HashMap<Integer, ArrayList<Account>>();
	
	//private static final BankDatabase singleton_db = new BankDatabase();
	
	public BankDatabase() {
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
	
	
}

