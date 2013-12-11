package guehochoi.test.mock;

import guehochoi.restaurant.Check;
import guehochoi.gui.WaiterGui;
import guehochoi.interfaces.Cashier;
import guehochoi.interfaces.Cook;
import guehochoi.interfaces.Customer;
import guehochoi.interfaces.Host;
import guehochoi.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {

	public EventLog log = new EventLog();
	
	public MockWaiter(String name) {
		super(name);
	}

	@Override
	public void sitAtTable(Customer c, int table) {
		log.add(new LoggedEvent("sitAtTable"));
		
	}

	@Override
	public void readyToOrder(Customer c) {
		log.add(new LoggedEvent("readyToOrder"));
		
	}

	@Override
	public void hereIsMyChoice(Customer c, String choice) {
		log.add(new LoggedEvent("hereIsMyChoice"));
		
	}

	@Override
	public void orderIsReady(String choice, int table) {
		log.add(new LoggedEvent("orderIsReady"));
		
	}

	@Override
	public void doneEating(Customer c) {
		log.add(new LoggedEvent("doneEating"));
		
	}

	@Override
	public void leaving(Customer c) {
		log.add(new LoggedEvent("leaving"));
	}

	@Override
	public void outOf(String choice, int table) {
		log.add(new LoggedEvent("outOf"));
	}

	@Override
	public void hereIsCheck(Check check, Customer c) {
		log.add(new LoggedEvent("hereIsCheck"));
	}

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWantBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBackToWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBreakEnabled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(WaiterGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WaiterGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHost(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCook(Cook cook) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WaiterGui getWaiterGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startThread() {
		// TODO Auto-generated method stub
		
	}

	
}
