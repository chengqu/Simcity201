package josh.restaurant.test.mock;


import josh.restaurant.CustomerAgent.AgentState;
import josh.restaurant.interfaces.Cashier;
import josh.restaurant.interfaces.Customer;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	
	public EventLog log = new EventLog();

	public MockCustomer(String name) {
		super(name);

	}

	
	
	//from cashier
	@Override
	public void msgBillRecieved(float moneyOwed) {
		log.add(new LoggedEvent("BillRecieved from cashier. Total = "+ moneyOwed));
	
	}

	//from waiter
	@Override
	public void msgHereIsYourBill(float charge) {
		// TODO Auto-generated method stub
		if (this.getName().toLowerCase().contains("dishonestbroke")) {
			//test the non-normative scenario where the customer has 1 dolla and orders anything
			cashier.msgHereIsMyPayment(this, (float) 1.00);
		}
		else if (this.getName().toLowerCase().contains("cheater6")){
			//test the non-normative scenario where the customer has 6 but orders anything
			cashier.msgHereIsMyPayment(this, (float) 6);
		}	
		else {
			//test the normative scenario
			cashier.msgHereIsMyPayment(this, (float) charge);
		}
	}
	
	
	// ********************* EXAMPLE *************************************************** 
	
	/*
	
	@Override
	public void HereIsYourTotal(double total) {
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

		if(this.name.toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			cashier.IAmShort(this, 0);

		}else if (this.name.toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.HereIsMyPayment(this, Math.ceil(total));

		}else{
			//test the normative scenario
			cashier.HereIsMyPayment(this, total);
		}
	}

	@Override
	public void HereIsYourChange(double total) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
	}

	@Override
	public void YouOweUs(double remaining_cost) {
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
	}

	*/
}
