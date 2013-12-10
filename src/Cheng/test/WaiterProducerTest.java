package Cheng.test;

import java.util.concurrent.Semaphore;

import Cheng.CashierAgent;
import Cheng.CookAgent;
import Cheng.CustomerAgent;
import Cheng.WaiterProducer;
import Cheng.gui.CustomerGui;
import Cheng.gui.RestaurantGui;
import Cheng.gui.RestaurantPanel;
import Cheng.gui.WaiterGui;
import Cheng.test.mock.MockMarket;
import LYN.test.mock.MockMarket2;
import agents.Person;
import agents.ProducerConsumerMonitor;
import junit.framework.TestCase;


/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class WaiterProducerTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	WaiterProducer waiter;
	RestaurantPanel rp;
	RestaurantGui gui;
	CustomerAgent customer;
	ProducerConsumerMonitor<CookAgent.Order> pm;
	MockMarket market1;
	MockMarket2 market2;
	CustomerGui gui1;
	CookAgent cook;
	WaiterGui waitergui;
	Person p;

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();    
		gui = new RestaurantGui();
		rp = new RestaurantPanel(gui);
		gui1 = new CustomerGui(customer,gui);
		waitergui = new WaiterGui(waiter, gui);
		pm = new ProducerConsumerMonitor<CookAgent.Order>(30);
		p = new Person("josh",true);
		cashier = new CashierAgent("cashier",rp); 
		cook = new CookAgent("cook",rp,pm);
		waiter = new WaiterProducer(p,"waiter",rp,pm);
		customer = new CustomerAgent(p);                
		customer.setGui(gui1, 0);
		waiter.setGui(waitergui, 0);
		market1 = new MockMarket("mockmarket1");
		market2 = new MockMarket2("mockmarket2");
		waiter.atTable = new Semaphore(100,true);
		cook.monitor = pm;
		waiter.monitor = pm;
	}        
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the  bill of 5.99 and get $1 change back.
	 */
	public void testSendOrdertoCook() {
		//setUp() runs first before this test!

		waiter.setCook(cook);   
		waiter.p = p;
		pm.subscriber = cook;
		waiter.isWorking = true;
		waiter.cashier = cashier;
		waiter.msgAtCustomer();
		waiter.msgAtCook();
		waiter.msgAtTable();
		waiter.msgAtCashier();
		waiter.msgAtCustomer();
		waiter.msgAtCook();
		waiter.msgAtTable();
		waiter.msgAtCashier();
		waiter.msgSeatCustomer(customer, 1);
		assertEquals("Cook should have 0 bills in it. It doesn't.",waiter.Customers.get(0).getChoice(), null);                
		

		//check preconditions

		waiter.msgHereIsMyOrder(customer, "Steak");
		assertEquals("Cook should have 0 bills in it. It doesn't.",waiter.Customers.get(0).getChoice(), "Steak");                
		
		assertTrue("Cook's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				waiter.pickAndExecuteAnAction());
		

		waiter.msgFoodReady("Steak", 1);
		assertEquals("Cook should have 0 bills in it. It doesn't.",waiter.Customers.get(0).getChoice(), "Steak");
		assertEquals("Cook should have 0 bills in it. It doesn't.",waiter.Customers.get(0).table, 1);  
		
		assertTrue("Cook's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				waiter.pickAndExecuteAnAction());
		assertEquals("Cook should have 0 bills in it. It doesn't.",waiter.seatnum, 1);
		



	}
}
/*	
		//step 1 of the test

		cashier.msghereisthebill(customer, "Salad");;//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.customers.size(), 1);

		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());

		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());



		//step 2 of the test

		cashier.msgcustomercheck(customer);

		//check postconditions for step 2 / preconditions for step 3
		assertTrue("CashierBill should contain a bill with state == readytocheck. It doesn't.",
				cashier.customers.get(0).s == State.readytocheck);

		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("ready to pay"));

		assertTrue("CashierBill should contain a bill of price = $5.99. It contains something else instead: $" 
				+ cashier.customers.get(0).check, cashier.customers.get(0).check == 5.99);

		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
				cashier.customers.get(0).c == customer);


		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());

		//check postconditions for step 3 / preconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourTotal"));

		cashier.msghereismoney(customer, 5.99, 6.99);         
		assertTrue("Cashier should have logged \"Received customer's money\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("customer's money"));


		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
				cashier.pickAndExecuteAnAction());

		assertTrue("CashierBill should contain change = 1 It contains something else instead: $" 
				+ (cashier.customers.get(0).money-5.99), cashier.customers.get(0).money-5.99 == 1);





		//check postconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("HereIsYourChange"));


		assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
				cashier.customers.get(0).s == State.checked);

		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());


	} //end one normal customer scenario

	/*
        public void testTwoNormalMarketScenario() {
            //setUp() runs first before this test!

            market1.cashier = cashier;//You can do almost anything in a unit test.    
            market2.cashier = cashier;

            //check preconditions
            cashier.msgaddmarket(market1);
            cashier.msgaddmarket(market2);

            assertEquals("Cashier should have billstate of nothing.",cashier.markets.get(0).bs, billstate.nothing); 
            assertEquals("Cashier should have billstate of nothing.",cashier.markets.get(1).bs, billstate.nothing); 
            assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                                            + cashier.log.toString(), 0, cashier.log.size());


            //step 1 of the test

            cashier.msgpleasepaythebill(market1, 2.99);//send the message from a waiter

            //check postconditions for step 1 and preconditions for step 2
            assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                            + market1.log.toString(), 0, market1.log.size());


            assertEquals("Cashier should have billstate of money in it. It doesn't.", cashier.markets.get(0).bs, billstate.money);


            assertTrue("Cashier should have logged \"received bill\" but didn't. His log reads instead: " 
                    + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("reveived bill"));

            cashier.msgpleasepaythebill(market2, 2.99);
            assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                    + market2.log.toString(), 0, market2.log.size());
            assertEquals("Cashier should have billstate of money in it. It doesn't.", cashier.markets.get(1).bs, billstate.money);
            assertTrue("Cashier should have logged \"received bill\" but didn't. His log reads instead: " 
                    + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("reveived bill"));
            assertTrue("Cashier's scheduler should have returned true , but didn't.", cashier.pickAndExecuteAnAction());


            assertEquals(
                            "Cashier should have  left"
                                            , cashier.money, 4.91);
            assertEquals("Cashier should have billstate of nothing.",cashier.markets.get(0).bs, billstate.nothing);

            assertTrue("MockMarket should have logged \"Received Cashier's bill\" but didn't. His log reads instead: " 
                    + market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received Cashier's bill")); 


            assertTrue("Cashier's scheduler should have returned true , but didn't.", cashier.pickAndExecuteAnAction());
            assertEquals(
                    "Cashier should have  left"
                                    , cashier.money, 1.92);
    assertEquals("Cashier should have billstate of nothing.",cashier.markets.get(1).bs, billstate.nothing);

    assertTrue("MockMarket should have logged \"Received Cashier's bill\" but didn't. His log reads instead: " 
            + market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Received Cashier's bill")); 
    } //end one normal customer scenario

        public void testOneNormalMarketScenario() {
            //setUp() runs first before this test!

            market1.cashier = cashier;//You can do almost anything in a unit test.                        

            //check preconditions
            cashier.msgaddmarket(market1);

                     assertEquals("Cashier should have billstate of nothing.",cashier.markets.get(0).bs, billstate.nothing); 

            assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                                            + cashier.log.toString(), 0, cashier.log.size());


            //step 1 of the test

            cashier.msgpleasepaythebill(market1, 5.98);//send the message from a waiter

            //check postconditions for step 1 and preconditions for step 2
            assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                            + market1.log.toString(), 0, market1.log.size());

            assertEquals("Cashier should have billstate of money in it. It doesn't.", cashier.markets.get(0).bs, billstate.money);


            assertTrue("Cashier should have logged \"received bill\" but didn't. His log reads instead: " 
                    + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("reveived bill"));

            assertTrue("Cashier's scheduler should have returned true , but didn't.", cashier.pickAndExecuteAnAction());

            assertEquals(
                            "Cashier should have 1.92 left"
                                            , cashier.money, 1.92);
            assertEquals("Cashier should have billstate of nothing.",cashier.markets.get(0).bs, billstate.nothing);

            assertTrue("MockMarket should have logged \"Received Cashier's bill\" but didn't. His log reads instead: " 
                    + market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received Cashier's bill"));                                          
    } //end one normal customer scenario


        public void testOneCustomerhavingnoenoughmoneytopayScenario() {
            //setUp() runs first before this test!

            customer.cashier = cashier;//You can do almost anything in a unit test.                        

            //check preconditions
            assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customers.size(), 0);                
            assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                                            + cashier.log.toString(), 0, cashier.log.size());


            //step 1 of the test

            cashier.msghereisthebill(customer, "Steak");;//send the message from a waiter

            //check postconditions for step 1 and preconditions for step 2
            assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                            + waiter.log.toString(), 0, waiter.log.size());

            assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.customers.size(), 1);

            assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());

            assertEquals(
                            "MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
                                            + waiter.log.toString(), 0, waiter.log.size());

            assertEquals(
                            "MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                                            + waiter.log.toString(), 0, waiter.log.size());



            //step 2 of the test

            cashier.msgcustomercheck(customer);

            //check postconditions for step 2 / preconditions for step 3
            assertTrue("CashierBill should contain a bill with state == readytocheck. It doesn't.",
                            cashier.customers.get(0).s == State.readytocheck);

            assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
                            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("ready to pay"));

            assertTrue("CashierBill should contain a bill of price = $15.99. It contains something else instead: $" 
                            + cashier.customers.get(0).check, cashier.customers.get(0).check == 15.99);

            assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
                                    cashier.customers.get(0).c == customer);


            //step 3
            //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
            assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
                                    cashier.pickAndExecuteAnAction());

            //check postconditions for step 3 / preconditions for step 4
            assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
                            + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourTotal"));

            cashier.msgcustomernotenoughmoney(customer, 6.99, 15.99);         
            assertTrue("Cashier should have logged \"Received customer's money\" but didn't. His log reads instead: " 
                            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("customer's money not enough"));


            assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
                    cashier.pickAndExecuteAnAction());

            assertTrue("CashierBill should contain change = -9.0 It contains something else instead: $" 
                            + (cashier.customers.get(0).money-15.99), cashier.customers.get(0).money-15.99 == -9.0);





            //check postconditions for step 4
            assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
                            + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("HereIsYourChange"));


            assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
                            cashier.customers.get(0).s == State.paynexttime);

            assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                            cashier.pickAndExecuteAnAction());


    } //end one normal customer scenario

        public void testOneNormalMarketScenariowithnotenoughmoneytopay() {
            //setUp() runs first before this test!

            market1.cashier = cashier;//You can do almost anything in a unit test.                        

            //check preconditions
            cashier.msgaddmarket(market1);

                     assertEquals("Cashier should have billstate of nothing.",cashier.markets.get(0).bs, billstate.nothing); 

            assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                                            + cashier.log.toString(), 0, cashier.log.size());


            //step 1 of the test

            cashier.msgpleasepaythebill(market1, 9.98 );//send the message from a waiter

            //check postconditions for step 1 and preconditions for step 2
            assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                            + market1.log.toString(), 0, market1.log.size());

            assertEquals("Cashier should have billstate of nomoney in it. It doesn't.", cashier.markets.get(0).bs, billstate.nomoney);


            assertTrue("Cashier should have logged \"received bill\" but didn't. His log reads instead: " 
                    + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("reveived bill"));

            assertTrue("Cashier's scheduler should have returned true , but didn't.", cashier.pickAndExecuteAnAction());

            assertEquals(
                            "Cashier should have his original money left"
                                            , cashier.money, 7.9);
            assertEquals("Cashier should have billstate of paynexttime.",cashier.markets.get(0).bs, billstate.paynexttime);

            assertTrue("MockMarket should have logged \"Cashier pay next time\" but didn't. His log reads instead: " 
                    + market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Cashier pay next time"));                                          
    } //end one normal customer scenario

        public void testonecustomernormalscenariowithInterleavingofmarketwithpreviousbillstopay() {
        	 market1.cashier = cashier;//You can do almost anything in a unit test.                        

             //check preconditions
             cashier.msgaddmarket(market1);

                      assertEquals("Cashier should have billstate of nothing.",cashier.markets.get(0).bs, billstate.nothing); 

             assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                                             + cashier.log.toString(), 0, cashier.log.size());


             //step 1 of the test

             cashier.msgpleasepaythebill(market1, 10 );//send the message from a waiter

             //check postconditions for step 1 and preconditions for step 2
             assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                             + market1.log.toString(), 0, market1.log.size());

             assertEquals("Cashier should have billstate of nomoney in it. It doesn't.", cashier.markets.get(0).bs, billstate.nomoney);


             assertTrue("Cashier should have logged \"received bill\" but didn't. His log reads instead: " 
                     + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("reveived bill"));

             assertTrue("Cashier's scheduler should have returned true , but didn't.", cashier.pickAndExecuteAnAction());

             assertEquals(
                             "Cashier should have his original money left"
                                             , cashier.money, 7.9);
             assertEquals("Cashier should have billstate of paynexttime.",cashier.markets.get(0).bs, billstate.paynexttime);

             assertTrue("MockMarket should have logged \"Cashier pay next time\" but didn't. His log reads instead: " 
                     + market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Cashier pay next time"));    






        	customer.cashier = cashier;//You can do almost anything in a unit test.                        

             //check preconditions
             assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.customers.size(), 0);                
             assertEquals("CashierAgent should have 1 event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                                             + cashier.log.toString(), 1, cashier.log.size());


             //step 1 of the test

             cashier.msghereisthebill(customer, "Salad");;//send the message from a waiter

             //check postconditions for step 1 and preconditions for step 2
             assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                             + waiter.log.toString(), 0, waiter.log.size());

             assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.customers.size(), 1);

             assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());

             assertEquals(
                             "MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
                                             + waiter.log.toString(), 0, waiter.log.size());

             assertEquals(
                             "MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                                             + waiter.log.toString(), 0, waiter.log.size());

             //step 2 of the test

             cashier.msgcustomercheck(customer);

             //check postconditions for step 2 / preconditions for step 3
             assertTrue("CashierBill should contain a bill with state == readytocheck. It doesn't.",
                             cashier.customers.get(0).s == State.readytocheck);

             assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
                             + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("ready to pay"));

             assertTrue("CashierBill should contain a bill of price = $5.99. It contains something else instead: $" 
                             + cashier.customers.get(0).check, cashier.customers.get(0).check == 5.99);

             assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
                                     cashier.customers.get(0).c == customer);

             //step 3 of the test
             market1.cashier = cashier;//You can do almost anything in a unit test.                        

             //check preconditions


                      assertEquals("Cashier should have billstate of paynexttime.",cashier.markets.get(0).bs, billstate.paynexttime); 

             assertEquals("CashierAgent should have 3 event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                                             + cashier.log.toString(), 2, cashier.log.size());


             //step 1 of the test
             cashier.money = (int)(cashier.money + 10);  //increase the cashier's money to pay both the current and previous bills

             cashier.msgpleasepaythebill(market1, 6 );//send the message from a waiter
             assertEquals(cashier.money, 17.0);
             assertEquals(cashier.markets.get(0).bill, 16.0);
             //check postconditions for step 1 and preconditions for step 2
             assertEquals("MockMarket should have 1 event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                                             + market1.log.toString(), 1, market1.log.size());

             assertEquals("Cashier should have billstate of money in it. It doesn't.", cashier.markets.get(0).bs, billstate.money);


             assertTrue("Cashier should have logged \"received bill\" but didn't. His log reads instead: " 
                     + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("reveived bill"));

             assertTrue("Cashier's scheduler should have returned true , but didn't.", cashier.pickAndExecuteAnAction());

             //The Cashier should deal with the interleaving market first
             assertEquals("Cashier should have billstate of nothing.",cashier.markets.get(0).bs, billstate.nothing);

             assertEquals("Cashier should have 1 left", cashier.money, 1.0);


             assertTrue("MockMarket should have logged \"Received Cashier's bill\" but didn't. His log reads instead: " 
            		 + market1.log.getLastLoggedEvent().toString(), market1.log.containsString("Received Cashier's bill"));

           //  assertTrue(cashier.pickAndExecuteAnAction());

             //after that the Cashier will continue deal with customer
             //step 3
             //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
             assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
                                     cashier.pickAndExecuteAnAction());

             //check postconditions for step 3 / preconditions for step 4
            // assertEquals(cashier.customers.get(0).check, 5.99);
            // System.out.println(cashier.customers.get(0).s);
             //System.out.println(cashier.markets.get(0).bs);
             assertEquals(cashier.customers.get(0).s, State.afterreadytocheck);
             assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
                             + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourTotal"));

             cashier.msghereismoney(customer, 5.99, 6.99);         
             assertTrue("Cashier should have logged \"Received customer's money\" but didn't. His log reads instead: " 
                             + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("customer's money"));


             assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
                     cashier.pickAndExecuteAnAction());

             assertTrue("CashierBill should contain change = 1 It contains something else instead: $" 
                             + (cashier.customers.get(0).money-5.99), cashier.customers.get(0).money-5.99 == 1);





             //check postconditions for step 4
             assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
                             + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("HereIsYourChange"));


             assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
                             cashier.customers.get(0).s == State.checked);

             assertEquals("Cashier should now have 2", cashier.money, 6.99);

             assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
                             cashier.pickAndExecuteAnAction());



        }
 */

