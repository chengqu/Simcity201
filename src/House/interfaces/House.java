package House.interfaces;

import House.agents.HousePerson;



/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface House {
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 * 
	 */

	public abstract void msgPayBills();

	public abstract void msgPayingbill();



	public abstract void msgRestathome();


	public abstract void msgIameatingathome();

	public abstract void msgdoneCooking();


	public abstract void msgstoreGroceries();


	public abstract void msgEvicted();



	public abstract void msgAtTable() ;




}