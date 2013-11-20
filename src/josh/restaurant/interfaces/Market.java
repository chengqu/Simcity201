package josh.restaurant.interfaces;

import josh.restaurant.CookAgent;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Market {
	
	/**
	 * 
	 * @param charge_
	 * @param choice
	 */
	
	public abstract void msgHereIsYourPayment(float charge_, String choice);
	
	/**
	 * 
	 * @param w
	 * @param choice
	 */
	public abstract void msgOrderProduceFromMarket (Cook w, String choice);
	

}