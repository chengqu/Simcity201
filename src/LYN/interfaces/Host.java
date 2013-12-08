package LYN.interfaces;

import LYN.CustomerAgent;
import LYN.Menu;
import LYN.WaiterAgent;
import LYN.gui.CustomerGui;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Host {
        /**
         * @param total The cost according to the cashier
         *
         * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
         * 
         */
	
	    public abstract void msgBreak(Waiter w);
	    
	    public abstract void msgReturntoWork(Waiter w);
	    
	    public abstract void addwaiter(Waiter w);
	    
	    public abstract void msgIWantFood(Customer cust);
	    
	    public abstract void  msgTableisfree(int t, Customer c);
	    
	   


}