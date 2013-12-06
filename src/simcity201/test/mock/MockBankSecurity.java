package simcity201.test.mock;

import guehochoi.test.mock.EventLog;
import simcity201.interfaces.BankCustomer;
import simcity201.interfaces.BankSecurity;

public class MockBankSecurity extends Mock implements BankSecurity{
	
	public EventLog log = new EventLog();
	
	public MockBankSecurity(String name) {
		super(name);
	}

	@Override
	public void helpMe(BankCustomer c) {
		// TODO Auto-generated method stub
		
	}

}
