package LYN.interfaces;

import LYN.CustomerAgent;
import LYN.Market;
import LYN.WaiterAgent;
import LYN.CookAgent.Order;



/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface  Cook{
        /**
         * @param total The cost according to the cashier
         *
         * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
         * 
         */
	
	public abstract void msgAtTable();
	
	public abstract void msgaddmarket(market m);
	
	public abstract void msgfullfilldone(String choice);
	
	public abstract void msgHereisanOrder(Waiter w, String choice, int table);

	public abstract void msgFooddone(Order o);
	
	
    
}