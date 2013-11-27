package House.interfaces;

import agents.Person;



/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Persontest {
        /**
         * @param total The cost according to the cashier
         *
         * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
         * 
         */
	public abstract void msgDone();
	
	public abstract void msgAtDest();
	
 	
	
}