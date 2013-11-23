package josh.restaurant.test.mock;


import josh.restaurant.CookAgent;
import josh.restaurant.CustomerAgent;
import josh.restaurant.CashierAgent.Bill;
import josh.restaurant.CookAgent.OrderIcon;
import josh.restaurant.interfaces.Cashier;
import josh.restaurant.interfaces.Cook;
import josh.restaurant.interfaces.Market;
import josh.restaurant.interfaces.Waiter;

public class MockMarket extends Mock implements Market {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;	

	public MockMarket(String name, Cashier cashier) {
		super(name);
		this.cashier = cashier;
	}

	@Override
	public void msgHereIsYourPayment(float charge_, String choice) {
		log.add(new LoggedEvent("Payment recieved from cashier for item: " + choice + " charge: " + charge_));
	}

	@Override
	public void msgOrderProduceFromMarket(Cook w, String choice) {
		log.add(new LoggedEvent("Order received from cook for: " + choice));
	}


}
