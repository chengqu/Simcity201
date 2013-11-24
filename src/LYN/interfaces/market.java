package LYN.interfaces;

import LYN.CookAgent;
import LYN.CustomerAgent;
import LYN.Market;
import LYN.Market.orderlist;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface market {
        /**
         * @param total The cost according to the cashier
         *
         * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
         * 
         */
	
	public abstract void msgnotenoughmoney();
	
	public abstract void msghereisthebill();
	
	public abstract void msgfullfillmyorder(Cook c, String Choice, int q);
	
	public abstract void msgfullfilldone(orderlist o);

	
}