package simcity201.test.mock;

import guehochoi.test.mock.EventLog;
import simcity201.interfaces.BankATM;

public class MockBankATM extends Mock implements BankATM {

	public EventLog log = new EventLog();
	
	public MockBankATM(String name) {
		super(name);
	}

}
