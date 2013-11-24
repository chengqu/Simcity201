package LYN.interfaces;

import LYN.CustomerAgent;
import LYN.Market;


/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cashier {
        /**
         * @param total The cost according to the cashier
         *
         * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
         * 
         */
	
	public abstract void msghereisthebill(Customer c, String choice);
	
	public abstract void msgcustomercheck(Customer c);
	
	public abstract void msgcustomernotenoughmoney(Customer c, double money, double Check);
	
	public abstract void msghereismoney(Customer c, double check, double money);

	public abstract void msgpleasepaythebill(market m, double bill);
	
	public abstract void msgaddmarket(market m);
    
}