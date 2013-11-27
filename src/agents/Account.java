package agents;

import java.text.DateFormat;
import java.util.*;

public class Account {

	private int accountNumber;
	private int customerSSN;
	private String customerName;
	private String customerAddress;
	private AccountType type;
	
	private float totalAmount;
	private float pendingAmount; // pending amount will be cleared when database is updated
	
	private static int CheckingLimit = 100000;
	private static int SavingLimit = 500000;
	
	private static int num_generator = 10000000;
	public enum AccountType { Checking, Saving }
	
	class LogTransaction {
		Date timestamp;
		float amount;
		float total;
		
		LogTransaction (float amount, float total) {
			timestamp = new Date();
			this.amount = amount;
			this.total = total;
		}
	}
	
	List<LogTransaction> transactions =
			new ArrayList<LogTransaction>();
	
	public Account (String name, String address, int ssn, AccountType type) {
		this.customerName = name;
		this.customerSSN = ssn;
		this.customerAddress = address;
		this.type = type;
		this.totalAmount = 0;
		this.pendingAmount = 0;
		this.accountNumber = num_generator;
		num_generator++;
	}
	
	public void deposit(float amount) {
		pendingAmount += amount;
		transactions.add(new LogTransaction(amount, totalAmount+pendingAmount));
	}
	
	public void withdraw(float amount) {
		pendingAmount -= amount;
		transactions.add(new LogTransaction(amount, totalAmount+pendingAmount));
	}
	
	
	public int getAccountNumber() {
		return accountNumber;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public String getCustomerName() {
		return customerName;
	}
	public int getCustomerSSN() {
		return customerSSN;
	}
	public float getBalance() {
		return totalAmount + pendingAmount;
	}
	public AccountType getType() {
		return type;
	}
	public float getPendingAmount() {
		return pendingAmount;
	}
	public void update() {
		totalAmount += pendingAmount;
	}
	public float getDepositLimit() {
		if (type == AccountType.Checking) {
			return CheckingLimit;
		}else {
			return SavingLimit;
		}
	}
	
	public String displayTransactionHistory() {
		StringBuilder sb = new StringBuilder();
		String welcome = "Transaction History for account: " + accountNumber + "\n";
		sb.append(welcome);
		for(LogTransaction log : transactions) {
			sb.append(DateFormat.getTimeInstance().format(log.timestamp));
			sb.append(": ");
			if (log.amount < 0) {
				sb.append("Withdrawal= ");
			}else {
				sb.append("Deposit= ");
			}
			sb.append(Math.abs(log.amount));
			sb.append(", remaining total= ");
			sb.append(log.total);
			sb.append("\n");
		}
		if (sb.toString().equalsIgnoreCase(welcome)) {
			
			return sb.toString();
		}else {
			return "No transaction history was found for this account";
		}
	}
}
