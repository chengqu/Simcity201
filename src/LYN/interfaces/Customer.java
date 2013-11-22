package LYN.interfaces;

import LYN.Menu;
import LYN.WaiterAgent;
import LYN.gui.CustomerGui;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {
        /**
         * @param total The cost according to the cashier
         *
         * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
         * 
         */
	
	    public abstract void gotHungry();
	    
	    public abstract void msgcannotwait();
	    
	    public abstract void msgFollowMe(Waiter w, int a, Menu m);
	    
	    public abstract void msgAnimationFinishedGoToSeat();
	    
	    public abstract void msgnotenoughmoney();
	    
	    public abstract void msgwhatwouldyoulike();
	    
	    public abstract void msgwhatwouldyouliketoreorder(String choice) ;
	    
	    public abstract void msgHereisyourfood();
	    
	    
	   
        public abstract void msghereisyourbill(double check);

        /**
         * @param total change (if any) due to the customer
         *
         * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
         */
        public abstract void msghereisyourchange(double change);
        
        public abstract void msgAnimationFinishedLeaveRestaurant();
        
        public abstract void msgArriveatcashier() ;
        
        public abstract String getName();

        public CustomerGui getGui();



}