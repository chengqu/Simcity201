package ericliu.test.mock;


import ericliu.interfaces.Cashier;
import ericliu.interfaces.Customer;
import ericliu.interfaces.Waiter;
import ericliu.restaurant.ReceiptClass;

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
	public Waiter waiter;
	
	public EventLog log=new EventLog();
	
	public MockCustomer(String name) {
		super(name);

	}

	public String getCustomerName(){
	   return name;
	}
	@Override
	public void msgHereIsYourReceipt(ReceiptClass receipt) {
	   double total=receipt.getMealPrice();
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

//		if(this.name.toLowerCase().contains("thief")){
//			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
////			cashier.IAmShort(this, 0);
//		   cashier.msgNotEnoughMoney(this);
//
//		}else if (this.name.toLowerCase().contains("rich")){
//			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
//         cashier.msgHereIsMyPayment(this, Math.ceil(total));
//         //cashier.msgHereIsMyPayment(this);
//
//		}else{
//			//test the normative scenario
//		   cashier.msgHereIsMyPayment(this, total);
//         //cashier.msgHereIsMyPayment(this,15.99 );
//		}
	}

//	@Override
//	public void HereIsYourChange(double total) {
//		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
//	}
//
//	@Override
//	public void YouOweUs(double remaining_cost) {
//		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
//	}
	
	public void msgThankYouForYourPayment() {
//      log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
         log.add(new LoggedEvent("Received msgThankYouForYourPayment from cashier."));

	}

   
   public void msgJustPayNextTime() {
//      log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
      log.add(new LoggedEvent("Received msgJustPayNextTime from cashier."));
   }

}
