package House.test.mock;

import House.gui.HousePersonPanel;
import House.interfaces.Persontest;




/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockPerson extends Mock implements Persontest {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
        public HousePersonPanel house;
       public EventLog log = new EventLog();
        public MockPerson(String name) {
                super(name);
                

        }
		@Override
		public void msgDone() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void msgAtDest() {
			// TODO Auto-generated method stub
			
		}

       
        

}