package Cheng.test.mock;


import Cheng.interfaces.Cashier;
import Cheng.interfaces.Customer;
import Cheng.interfaces.Market;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockMarket extends Mock implements Market {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log = new EventLog();
	public MockMarket(String name) {
		super(name);

	}

	@Override
	public void msgPayMarket(double total) {
		log.add(new LoggedEvent("Received PayMarket from cashier. Total = "+ total));
		
	}

	
		
	

}
