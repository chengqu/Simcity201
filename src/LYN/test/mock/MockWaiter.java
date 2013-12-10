package LYN.test.mock;


import LYN.Menu;
import LYN.WaiterAgent;
import LYN.WaiterAgent.MyCustomer;

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
public class MockWaiter extends Mock implements Waiter {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
        public Cashier cashier;
       public EventLog log = new EventLog();
        public MockWaiter(String name) {
                super(name);
                

        }

   
		@Override
		public void added() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgsitAtTable(Customer cust, int table) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgWannaBreak() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgReturnToWork() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgReadyToOrder(Customer c) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgHereismychoice(Customer c, String choice) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgRunoutoffood(String choice, int table) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgOrderisReady(String choice, int table) {
			 
			 log.add(new LoggedEvent("Customer is about to get food, send the check to cashier" + choice +" " + table));// TODO Auto-generated method stub
			 
		}

		@Override
		public void msgDoneEatingAndLeaving(Customer c) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgAtTable() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgArrivingatTable() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgAtOrigin() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgnotatOrigin() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void msgLeave() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pause() {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void resume() {
			// TODO Auto-generated method stub
			
		}

       
        

}