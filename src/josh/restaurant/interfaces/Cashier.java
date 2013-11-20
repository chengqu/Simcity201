package josh.restaurant.interfaces;

import josh.restaurant.CustomerAgent;
import josh.restaurant.MarketAgent;
import josh.restaurant.Order;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cashier {
	
	/**
	 * 
	 * @param o the order from the waiter
	 * @param c the customer for who the order is for
	 * 
	 * Sent by waiter to the cashier to make a new bill that will be added to cashier's bills
	 */
	
	public abstract void msgProduceABill(Order o, Customer c);

	/**
	 * 
	 * @param charge the amount of money that the customer is going to pay
	 */
	
	public abstract void msgHereIsMyPayment(Customer c, float charge); 
	
	
	/**
	 * 
	 * message sent from market to cashier 
	 * 
	 * @param m
	 * @param produce
	 * @param chargeAmount
	 * @param complete
	 */
	
	public abstract void msgBillFromTheMarket(Market m, String produce, float chargeAmount, boolean complete);

	

	
	// **************************** EXMAPLE ************************************
	
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	//public abstract void HereIsYourTotal(double total);

	/**
	 * @param total change (if any) due to the customer
	 *
	 * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
	 */
	//public abstract void HereIsYourChange(double total);


	/**
	 * @param remaining_cost how much money is owed
	 * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
	 */
	//public abstract void YouOweUs(double remaining_cost);

}