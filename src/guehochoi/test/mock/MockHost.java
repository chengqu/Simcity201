package guehochoi.test.mock;

import java.util.Collection;
import java.util.List;

import guehochoi.gui.RestaurantGui;
import guehochoi.interfaces.Cashier;
import guehochoi.interfaces.Customer;
import guehochoi.interfaces.Host;
import guehochoi.interfaces.Waiter;

public class MockHost extends Mock implements Host {

	public EventLog log = new EventLog();
	public Cashier cashier;
	
	public MockHost(String name) {
		super(name);
	}

	@Override
	public void msgIWantFood(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tableIsFree(int t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readyToWork(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wantToGoOnBreak(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void customerClear(Customer c, boolean clear) {
		log.add(new LoggedEvent("Receive customerClear"));
		
	}

	@Override
	public void iAm(boolean staying, Customer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void takeCustomers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCashier(Cashier cashier) {
		this.cashier = cashier;
	}

	@Override
	public List getWaitingCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection getTables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRestGui(RestaurantGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTable() {
		// TODO Auto-generated method stub
		
	}
	

}
