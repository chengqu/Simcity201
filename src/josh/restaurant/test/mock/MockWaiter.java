package josh.restaurant.test.mock;


import josh.restaurant.CustomerAgent;
import josh.restaurant.CashierAgent.Bill;
import josh.restaurant.CookAgent.OrderIcon;
import josh.restaurant.interfaces.Cashier;
import josh.restaurant.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;	

	public MockWaiter(String name, Cashier cashier) {
		super(name);
		this.cashier = cashier;
	}


	@Override
	public void msgSitAtTable(CustomerAgent c, int table) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgHereIsTheBill(Bill b) {
		log.add(new LoggedEvent("Received HereIsTheBill from cashier. Charge = "+ b.charge_));
	}


	@Override
	public void msgLeavingEarly(CustomerAgent c) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgImReadyToOrder(CustomerAgent c) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgHereIsMyChoice(CustomerAgent c, String choice) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgDoneEatingAndLeaving(CustomerAgent c) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgBadOrder(String choice, int table) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgOrderIsReady(String choice, int table, OrderIcon i) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgBreakRequestAnswer(boolean answer) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void gui_msgAtTable() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void gui_msgBackAtHomeBase() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void gui_msgAtCook() {
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

}
