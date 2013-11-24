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
public interface  Waiter{
        /**
         * @param total The cost according to the cashier
         *
         * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
         * 
         */
	
	public abstract void added();
	
	public abstract void msgsitAtTable(Customer cust, int table);
	
	public abstract void msgWannaBreak();
	
	public abstract void msgReturnToWork();

	public abstract void msgReadyToOrder(Customer c) ;
	
	public abstract void msgHereismychoice(Customer c, String choice);
	
	public abstract void msgRunoutoffood(String choice, int table);
	
	public abstract void msgOrderisReady(String choice, int table);
	
	public abstract void msgDoneEatingAndLeaving(Customer c);
	
	public abstract void msgAtTable();
	
	public abstract void msgArrivingatTable();
	
	public abstract void msgAtOrigin();
	
	public abstract void msgnotatOrigin();

	public abstract String getName();
	
	
	
    
}