package LYN.test.mock;


import LYN.Menu;
import LYN.WaiterAgent;
import LYN.gui.CustomerGui;
import LYN.interfaces.Cashier;
import LYN.interfaces.Customer;
import LYN.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
        public Cashier cashier;
       public EventLog log = new EventLog();
        public MockCustomer(String name) {
                super(name);
                

        }

        @Override
        public void msghereisyourbill(double check) {
                log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ check));
              /*
                if(this.name.toLowerCase().contains("thief")){
                        //test the non-normative scenario where the customer has no money if their name contains the string "theif"
                        cashier.msgcustomernotenoughmoney(this,0,check);

                }else if (this.name.toLowerCase().contains("rich")){
                        //test the non-normative scenario where the customer overpays if their name contains the string "rich"
                        cashier.msghereismoney(this,check,(float)Math.ceil(check));

                }else{
                        //test the normative scenario
                        cashier.msghereismoney(this,check,(float)check);
                }
                */
        }

        @Override
        public void msghereisyourchange(double change) {
                log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ change));
        }

		@Override
		public void gotHungry() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgcannotwait() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgFollowMe(Waiter w, int a, Menu m) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgAnimationFinishedGoToSeat() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgnotenoughmoney() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgwhatwouldyoulike() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgwhatwouldyouliketoreorder(String choice) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgHereisyourfood() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgAnimationFinishedLeaveRestaurant() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgArriveatcashier() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public CustomerGui getGui() {
			// TODO Auto-generated method stub
			return null;
		}

       
        

}