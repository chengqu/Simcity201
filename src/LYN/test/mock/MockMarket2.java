package LYN.test.mock;


import LYN.Market.orderlist;
import LYN.Menu;
import LYN.WaiterAgent;
import LYN.gui.CustomerGui;
import LYN.interfaces.Cashier;
import LYN.interfaces.Cook;
import LYN.interfaces.Customer;
import LYN.interfaces.Waiter;
import LYN.interfaces.market;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockMarket2 extends Mock implements market {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
        public Cashier cashier;
       public EventLog log = new EventLog();
        public MockMarket2(String name) {
                super(name);
                

        }

       
		@Override
		public void msghereisthebill() {
			 log.add(new LoggedEvent("Received Cashier's bill:)"));
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgfullfillmyorder(Cook c, String Choice, int q) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgfullfilldone(orderlist o) {
			
			 if(o.choice.equals("Steak")){
				 log.add(new LoggedEvent("Fullfill is done, ready to tell cashier to pay" + o.amount*4.99));
			 //cashier.msgpleasepaythebill(this, (double)o.amount*4.99);
			 } else if(o.choice.equals("Chicken")){
				 log.add(new LoggedEvent("Fullfill is done, ready to tell cashier to pay" + o.amount*3.99));
			 //cashier.msgpleasepaythebill(this, (double)o.amount*3.99);
			 } else if(o.choice.equals("Salad")){
				 log.add(new LoggedEvent("Fullfill is done, ready to tell cashier to pay" + o.amount*2.99));
			 //cashier.msgpleasepaythebill(this, (double)o.amount*2.99);
			 } if(o.choice.equals("Pizza")){
				 log.add(new LoggedEvent("Fullfill is done, ready to tell cashier to pay" + o.amount*2.99));
			 //cashier.msgpleasepaythebill(this, (double)o.amount*2.99);
			 }
			// TODO Auto-generated method stub
			
		}


		@Override
		public void msgnotenoughmoney() {
			 log.add(new LoggedEvent("Cashier does not have enough money "));
			// TODO Auto-generated method stub
			
		}

       
        

}