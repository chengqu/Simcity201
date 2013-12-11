package ericliu.test;

import ericliu.restaurant.CashierAgent;
import ericliu.restaurant.CashierAgent.ReceiptState;
//import restaurant.WaiterAgent.Bill;
import ericliu.restaurant.ReceiptClass;
import ericliu.interfaces.Cashier;
import ericliu.test.mock.MockCustomer;
import ericliu.test.mock.MockMarket;
import ericliu.test.mock.MockWaiter;
import ericliu.restaurant.MarketBillClass;
import ericliu.restaurant.FoodClass;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * 
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter;
	MockWaiter waiter1;
	MockWaiter waiter2;
	MockCustomer customer;
	MockCustomer customer1;
	MockCustomer customer2;
	MockMarket market;
	MockMarket market1;
	MockMarket market2;
	
	FoodClass Steak= new FoodClass("Steak", 0, 15.99);
	FoodClass Chicken= new FoodClass("Chicken", 0, 10.99);

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("cashier");		
		cashier.setCash(0.00);
		customer = new MockCustomer("mockcustomer");	
		customer1= new MockCustomer("mockcustomer1");
		customer2= new MockCustomer("mockcustomer2");
		waiter = new MockWaiter("mockwaiter");
		waiter1 = new MockWaiter("mockwaiter1");
		waiter2=new MockWaiter("mockwaiter2");
		market= new MockMarket("mockmarket");
		market1= new MockMarket("mockmarket1");
		market2= new MockMarket("mockmarket2");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!
		
		customer.cashier = cashier;//You can do almost anything in a unit test.		
		customer.waiter=waiter;
		waiter.customer=customer;
		waiter.cashier=cashier;
//		cashier.waiter=waiter;
//		cashier.customer=customer;
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.receipts.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test

		cashier.msgCalculateCheck(waiter, customer, Steak);

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.receipts.size(), 1);
		
		
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ customer.log.toString(), 0, customer.log.size());
		
		assertEquals("Cashier should have 1 receipt in it. It doesn't.", cashier.receipts.size(), 1);
		
      assertEquals("Cashier receipt should know who the customer is. It doesn't.", cashier.receipts.get(0).r.getCustomer()
            .getCustomerName() , "mockcustomer");
      
      assertEquals("Cashier receipt should know who the waiter is. It doesn't.", cashier.receipts.get(0).r.getWaiter().getName(),
            "mockwaiter");
      
      assertEquals("Cashier receipt should know what the order is. It doesn't.", cashier.receipts.get(0).r.getCustomerOrder().getChoice(),
            "Steak");
      
      assertEquals("Cashier receipt should know what the price is. It doesn't.", cashier.receipts.get(0).r.getMealPrice(),
            15.99);
      
     
		//step 2 of the test
//		cashier.ReadyToPay(customer, receipt);
      assertTrue("CashierBill should contain a receipt with state == calculated. It doesn't.",
            cashier.receipts.get(0).getState() == ReceiptState.calculated);
      
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
		//check postconditions for step 2 / preconditions for step 3
		
		
		assertTrue("Mockwaiter should have gotten a receipt. It didn't.", waiter.log.containsString("Received receipt."));
		
		
		
		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		
		
		cashier.msgHereIsMyPayment(customer, 15.99);
		
		//check postconditions for step 3 / preconditions for step 4
		
		assertEquals("Cash should have increased to 15.99. It did not", cashier.getCash(), 15.99);
		
		assertTrue("Cashier should have logged \"Received msgHereIsMyPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgHereIsMyPayment."));
		
		
		assertEquals("Cashier receipt state should be paid", cashier.receipts.get(0).getState(),
            ReceiptState.paid);
		
      assertEquals("Mockwaiter should have only 1 logged event. It doesn't.", waiter.log.size(), 1);
      
      assertEquals("Mockcustomer should have 1 logged events. It doesn't.", customer.log.size(), 1);
      

		
		
		
		//step 4
      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 4
      assertTrue("Mockcustomer should have gotten a thankyou message. It didn't.", customer.log
            .containsString("Received msgThankYouForYourPayment from cashier."));
      
      assertEquals("Mockwaiter should have only 1 logged event. It doesn't.", waiter.log.size(), 1);
      
      assertTrue("Cashier should have no more bills.", cashier.receipts.isEmpty());
      
      assertEquals("Cashier should have a total of 15.99", cashier.getCash(), 15.99);
      
      assertFalse("Cashier scheduler should return false. It didn't.", cashier.pickAndExecuteAnAction());
		
	
	}//end one normal customer scenario
	
	
	public void testCustomerCantPayScenario()
   {
      //setUp() runs first before this test!
      
      customer.cashier = cashier;//You can do almost anything in a unit test.    
      customer.waiter=waiter;
      waiter.customer=customer;
      waiter.cashier=cashier;
//    cashier.waiter=waiter;
//    cashier.customer=customer;
      
      //check preconditions
      assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.receipts.size(), 0);      
      assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                  + cashier.log.toString(), 0, cashier.log.size());
      
      //step 1 of the test

      cashier.msgCalculateCheck(waiter, customer, Steak);

      //check postconditions for step 1 and preconditions for step 2
      assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                  + waiter.log.toString(), 0, waiter.log.size());
      
      assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.receipts.size(), 1);
      
      
      assertEquals(
            "MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
                  + waiter.log.toString(), 0, waiter.log.size());
      
      assertEquals(
            "MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                  + customer.log.toString(), 0, customer.log.size());
      
      assertEquals("Cashier should have 1 receipt in it. It doesn't.", cashier.receipts.size(), 1);
      
      assertEquals("Cashier receipt should know who the customer is. It doesn't.", cashier.receipts.get(0).r.getCustomer()
            .getCustomerName() , "mockcustomer");
      
      assertEquals("Cashier receipt should know who the waiter is. It doesn't.", cashier.receipts.get(0).r.getWaiter().getName(),
            "mockwaiter");
      
      assertEquals("Cashier receipt should know what the order is. It doesn't.", cashier.receipts.get(0).r.getCustomerOrder().getChoice(),
            "Steak");
      
      assertEquals("Cashier receipt should know what the price is. It doesn't.", cashier.receipts.get(0).r.getMealPrice(),
            15.99);
      
     
      //step 2 of the test
//    cashier.ReadyToPay(customer, receipt);
      assertTrue("CashierBill should contain a receipt with state == calculated. It doesn't.",
            cashier.receipts.get(0).getState() == ReceiptState.calculated);
      
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3
      
      
      assertTrue("Mockwaiter should have gotten a receipt. It didn't.", waiter.log.containsString("Received receipt."));
      
      
      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      cashier.msgNotEnoughMoney(customer);
      
      //check postconditions for step 3 / preconditions for step 4
      
      assertEquals("Cash should have not increased. It did not", cashier.getCash(), 0.00);
      
      assertTrue("Cashier should have logged \"Received msgNotEnoughMoney\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgNotEnoughMoney."));
      
      
      assertEquals("Cashier receipt state should be NotEnoughMoney", cashier.receipts.get(0).getState(),
            ReceiptState.NotEnoughMoney);
      
      assertEquals("Mockwaiter should have only 1 logged event. It doesn't.", waiter.log.size(), 1);
      
      assertEquals("Mockcustomer should have 1 logged events. It doesn't.", customer.log.size(), 1);
      

      
      
      
      //step 4
      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("Mockcustomer should have gotten a justPayNextTime message. It didn't.", customer.log
            .containsString("Received msgJustPayNextTime from cashier."));
      
      assertEquals("Mockwaiter should have only 1 logged event. It doesn't.", waiter.log.size(), 1);
      
      assertTrue("Cashier should have no more bills.", cashier.receipts.isEmpty());
      
      assertEquals("Cashier should have a total of $0.00", cashier.getCash(), 0.00);
      
      assertFalse("Cashier scheduler should return false. It didn't.", cashier.pickAndExecuteAnAction());
      
   
   }
	
	public void testOneCustomer2Bills()
   {
      //setUp() runs first before this test!
      
      customer.cashier = cashier;//You can do almost anything in a unit test.    
      customer.waiter=waiter;
      waiter.customer=customer;
      waiter.cashier=cashier;
//    cashier.waiter=waiter;
//    cashier.customer=customer;
      
      //check preconditions
      assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.receipts.size(), 0);      
      assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                  + cashier.log.toString(), 0, cashier.log.size());
      
      //step 1 of the test

      cashier.msgCalculateCheck(waiter, customer, Steak);

      //check postconditions for step 1 and preconditions for step 2

      
      assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.receipts.size(), 1);
      
      
      assertEquals(
            "MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
                  + waiter.log.toString(), 0, waiter.log.size());
      
      assertEquals(
            "MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                  + customer.log.toString(), 0, customer.log.size());
      
      assertEquals("Cashier should have 1 receipt in it. It doesn't.", cashier.receipts.size(), 1);
      
      assertEquals("Cashier receipt should know who the customer is. It doesn't.", cashier.receipts.get(0).r.getCustomer()
            .getCustomerName() , "mockcustomer");
      
      assertEquals("Cashier receipt should know who the waiter is. It doesn't.", cashier.receipts.get(0).r.getWaiter().getName(),
            "mockwaiter");
      
      assertEquals("Cashier receipt should know what the order is. It doesn't.", cashier.receipts.get(0).r.getCustomerOrder().getChoice(),
            "Steak");
      
      assertEquals("Cashier receipt should know what the price is. It doesn't.", cashier.receipts.get(0).r.getMealPrice(),
            15.99);
      
     
      //step 2 of the test
//    cashier.ReadyToPay(customer, receipt);
      assertTrue("CashierBill should contain a receipt with state == calculated. It doesn't.",
            cashier.receipts.get(0).getState() == ReceiptState.calculated);
      
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3
      
      
      assertTrue("Mockwaiter should have gotten a receipt. It didn't.", waiter.log.containsString("Received receipt."));
      
      
      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      cashier.msgHereIsMyPayment(customer, 15.99);
      
      //check postconditions for step 3 / preconditions for step 4
      
      assertEquals("Cash should have increased to 15.99. It did not", cashier.getCash(), 15.99);
      
      assertTrue("Cashier should have logged \"Received msgHereIsMyPayment\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgHereIsMyPayment."));
      
      
      assertEquals("Cashier receipt state should be paid", cashier.receipts.get(0).getState(),
            ReceiptState.paid);
      
      assertEquals("Mockwaiter should have only 1 logged event. It doesn't.", waiter.log.size(), 1);
      
      assertEquals("Mockcustomer should have 1 logged events. It doesn't.", customer.log.size(), 1);
      

      
      
      
      //step 4
      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("Mockcustomer should have gotten a thankyou message. It didn't.", customer.log
            .containsString("Received msgThankYouForYourPayment from cashier."));
      
      assertEquals("Mockwaiter should have only 1 logged event. It doesn't.", waiter.log.size(), 1);
      
      assertTrue("Cashier should have no more bills.", cashier.receipts.isEmpty());
      
      assertEquals("Cashier should have a total of 15.99", cashier.getCash(), 15.99);
      
      assertFalse("Cashier scheduler should return false. It didn't.", cashier.pickAndExecuteAnAction());
         
      assertEquals("Mockcustomer should have 2 logged events. It doesn't.", customer.log.size(), 2);

      
      //CUSTOMER COMES BACK FOR DIFFERENT ORDER
      assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.receipts.size(), 0);      
      assertEquals("CashierAgent should have an 1 entry in its event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                  + cashier.log.toString(), 1, cashier.log.size());
      
      //step 1 of the test

      cashier.msgCalculateCheck(waiter, customer, Chicken);

      //check postconditions for step 1 and preconditions for step 2
  
      
      assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.receipts.size(), 1);
      


      assertEquals(
            "MockCustomer should have 2 entry events log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                  + customer.log.toString(), 2, customer.log.size());
      
      assertEquals("Cashier should have 1 receipt in it. It doesn't.", cashier.receipts.size(), 1);
      
      assertEquals("Cashier receipt should know who the customer is. It doesn't.", cashier.receipts.get(0).r.getCustomer()
            .getCustomerName() , "mockcustomer");
      
      assertEquals("Cashier receipt should know who the waiter is. It doesn't.", cashier.receipts.get(0).r.getWaiter().getName(),
            "mockwaiter");
      
      assertEquals("Cashier receipt should know what the order is. It doesn't.", cashier.receipts.get(0).r.getCustomerOrder().getChoice(),
            "Chicken");
      
      assertEquals("Cashier receipt should know what the price is. It doesn't.", cashier.receipts.get(0).r.getMealPrice(),
            10.99);
      
     
      //step 2 of the test
//    cashier.ReadyToPay(customer, receipt);
      assertTrue("CashierBill should contain a receipt with state == calculated. It doesn't.",
            cashier.receipts.get(0).getState() == ReceiptState.calculated);
      
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3
      
      
      assertTrue("Mockwaiter should have gotten a receipt. It didn't.", waiter.log.containsString("Received receipt."));
      
      
      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      cashier.msgHereIsMyPayment(customer, 10.99);
      
      //check postconditions for step 3 / preconditions for step 4
      
      assertEquals("Cash should have increased to 15.99+10.99. It did not", cashier.getCash(), 26.98);
      
      assertTrue("Cashier should have logged \"Received msgHereIsMyPayment\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgHereIsMyPayment."));
      
      
      assertEquals("Cashier receipt state should be paid", cashier.receipts.get(0).getState(),
            ReceiptState.paid);
      
      assertEquals("Mockwaiter should have only 2 logged evenst. It doesn't.", waiter.log.size(), 2);
      
      assertEquals("Mockcustomer should have 3 logged events. It doesn't.", customer.log.size(), 3);
      

      
      
      
      //step 4
      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("Mockcustomer should have gotten a thankyou message. It didn't.", customer.log
            .containsString("Received msgThankYouForYourPayment from cashier."));
      
      
      assertTrue("Cashier should have no more bills.", cashier.receipts.isEmpty());
      
      assertEquals("Cashier should have a total of 26.98", cashier.getCash(), 26.98);
      
      assertFalse("Cashier scheduler should return false. It didn't.", cashier.pickAndExecuteAnAction());
      
   
   }
	
	public void test2Customers2Bills()
   {
      //setUp() runs first before this test!
      
      customer1.cashier = cashier;//You can do almost anything in a unit test.    
      customer1.waiter=waiter;
      customer2.cashier = cashier;   
      customer2.waiter=waiter;
      waiter1.customer=customer1;
      waiter1.cashier=cashier;
      waiter2.customer=customer2;
      waiter2.cashier=cashier;
//    cashier.waiter=waiter;
//    cashier.customer=customer;
      
      //check preconditions
      assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.receipts.size(), 0);      
      assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                  + cashier.log.toString(), 0, cashier.log.size());
      
      //step 1 of the test

      cashier.msgCalculateCheck(waiter1, customer1, Steak);
      cashier.msgCalculateCheck(waiter2, customer2, Steak);

      //check postconditions for step 1 and preconditions for step 2
      assertEquals("MockWaiter1 should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
                  + waiter1.log.toString(), 0, waiter1.log.size());
      assertEquals("MockWaiter2 should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
            + waiter2.log.toString(), 0, waiter2.log.size());
      
      assertEquals("Cashier should have 2 bills in it. It doesn't.", cashier.receipts.size(), 2);
      
      
      assertEquals(
            "MockCustomer1 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                  + customer1.log.toString(), 0, customer1.log.size());
      
      assertEquals(
            "MockCustomer2 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
                  + customer2.log.toString(), 0, customer2.log.size());
      
      
      assertEquals("Cashier receipt should know who the customer is. It doesn't.", cashier.receipts.get(0).r.getCustomer()
            .getCustomerName() , "mockcustomer1");
      
      assertEquals("Cashier receipt should know who the customer is. It doesn't.", cashier.receipts.get(1).r.getCustomer()
            .getCustomerName() , "mockcustomer2");
      
      assertEquals("Cashier receipt should know who the waiter is. It doesn't.", cashier.receipts.get(0).r.getWaiter().getName(),
            "mockwaiter1");
      
      assertEquals("Cashier receipt should know who the waiter is. It doesn't.", cashier.receipts.get(1).r.getWaiter().getName(),
            "mockwaiter2");
      
      assertEquals("Cashier receipt should know what the first order is. It doesn't.", cashier.receipts.get(0).r.getCustomerOrder().getChoice(),
            "Steak");
      
      assertEquals("Cashier receipt should know what the second order is. It doesn't.", cashier.receipts.get(1).r.getCustomerOrder().getChoice(),
            "Steak");
      
      assertEquals("Cashier receipt should know what the first price is. It doesn't.", cashier.receipts.get(0).r.getMealPrice(),
            15.99);
      
      assertEquals("Cashier receipt should know what the second price is. It doesn't.", cashier.receipts.get(1).r.getMealPrice(),
            15.99);
      
     
      //step 2 of the test
//    cashier.ReadyToPay(customer, receipt);
      assertTrue("CashierBill should contain a receipt with state == calculated. It doesn't.",
            cashier.receipts.get(0).getState() == ReceiptState.calculated);
      
      assertTrue("CashierBill should contain a receipt with state == calculated. It doesn't.",
            cashier.receipts.get(1).getState() == ReceiptState.calculated);
      
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
     
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());

      //check postconditions for step 2 / preconditions for step 3
      
      
      assertTrue("Mockwaiter1 should have gotten a receipt. It didn't.", waiter1.log.containsString("Received receipt."));
      
      assertTrue("Mockwaiter2 should have gotten a receipt. It didn't.", waiter2.log.containsString("Received receipt."));

      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      cashier.msgNotEnoughMoney(customer1);
      cashier.msgHereIsMyPayment(customer2, 15.99);
      //check postconditions for step 3 / preconditions for step 4
      
      assertEquals("Cash should be 15.99. It did not", cashier.getCash(), 15.99);
      
      assertTrue("Cashier should have logged \"Received msgNotEnoughMoney\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgNotEnoughMoney."));
      
      assertTrue("Cashier should have logged \"Received msgHereIsMyPayment\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgHereIsMyPayment."));
      
      assertEquals("Cashier receipt1 state should be NotEnoughMoney", cashier.receipts.get(0).getState(),
            ReceiptState.NotEnoughMoney);
      
      assertEquals("Cashier receipt2 state should be paid", cashier.receipts.get(1).getState(),
            ReceiptState.paid);
      
      assertEquals("Mockwaiter1 should have only 1 logged event. It doesn't.", waiter1.log.size(), 1);
      
      assertEquals("Mockwaiter2 should have only 1 logged event. It doesn't.", waiter2.log.size(), 1);

      assertEquals("Mockcustomer1 should have 1 logged events. It doesn't.", customer1.log.size(), 1);
      
      assertEquals("Mockcustomer2 should have 1 logged events. It doesn't.", customer2.log.size(), 1);

      
      
      
      //step 4
      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());

      //check postconditions for step 4
      assertTrue("Mockcustomer1 should have gotten a justPayNextTime message. It didn't.", customer1.log
            .containsString("Received msgJustPayNextTime from cashier."));
      
      assertTrue("Mockcustomer2 should have gotten a thank you message. It didn't.", customer2.log
            .containsString("Received msgThankYouForYourPayment from cashier."));
            
      assertTrue("Cashier should have no more bills.", cashier.receipts.isEmpty());
      
      assertEquals("Cashier should have a total of $15.99", cashier.getCash(), 15.99);
      
      assertFalse("Cashier scheduler should return false. It didn't.", cashier.pickAndExecuteAnAction());
      
   
   }
	
	public void testNormativeMarket()
   {
      //setUp() runs first before this test!
      

      market.cashier=cashier;
      cashier.setCash(30.00);
//    cashier.waiter=waiter;
//    cashier.customer=customer;
      //MarketBillClass bill=new MarketBillClass(30.00, market);
      
      //check preconditions
      assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.receipts.size(), 0);      
      assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                  + cashier.log.toString(), 0, cashier.log.size());
      
      //step 1 of the test

      //cashier.msgHereIsTheMarketBill(bill);
      
      //check postconditions for step 1 and preconditions for step 2
      assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
                  + market.log.toString(), 0, market.log.size());
      
   //   assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.marketBills.size(), 1);
      
      
      assertEquals(
            "MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
                  + market.log.toString(), 0, market.log.size());
      
            
//      assertEquals("Cashier marketBill should know who the market is. It doesn't.", cashier.marketBills.get(0).getBill().getMarket()
//            .getName() , "mockmarket");
      
      
      
//      assertEquals("Cashier marketBill should know what the price is. It doesn't.", cashier.marketBills.get(0).getBill().getOrderPrice(),
//            30.00);
      
     
      //step 2 of the test
//    cashier.ReadyToPay(customer, receipt);
//      assertTrue("CashierBill should contain a bill with state == calculated. It doesn't.",
//            cashier.marketBills.get(0).getState() == ReceiptState.calculated);
      
    //  assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3
      
      
//      assertTrue("Mockwaiter should have gotten a receipt. It didn't.", waiter.log.containsString("Received receipt."));
      
   //   assertEquals("Cash should have decreased. It did not", cashier.getCash(), 0.00);

      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      cashier.msgThankYouForYourPayment();
      
      //check postconditions for step 3 / preconditions for step 4
      
      
      assertTrue("Cashier should have logged \"Received msgThankYouForYourPayment.\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgThankYouForYourPayment."));
      
      
      
    //  assertEquals("MockMarket should have only 1 logged event. It doesn't.", market.log.size(), 1);
      
  
      

      
      
      
      //step 4
//      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
//      
//      //check postconditions for step 4
//      assertTrue("Mockcustomer should have gotten a justPayNextTime message. It didn't.", customer.log
//            .containsString("Received msgJustPayNextTime from cashier."));
//      
//      assertEquals("Mockwaiter should have only 1 logged event. It doesn't.", waiter.log.size(), 1);
      
      assertTrue("Cashier should have no more bills.", cashier.marketBills.isEmpty());
      
  //    assertEquals("Cashier should have a total of $0.00", cashier.getCash(), 0.00);
      
      assertFalse("Cashier scheduler should return false. It didn't.", cashier.pickAndExecuteAnAction());
      
   
   }
	
	public void testOneOrderTwoMarkets()
   {
      //setUp() runs first before this test!
      

      market1.cashier=cashier;
      market2.cashier=cashier;
      cashier.setCash(30.00);
//    cashier.waiter=waiter;
//    cashier.customer=customer;
//      MarketBillClass bill1=new MarketBillClass(15.00, market1);
//      MarketBillClass bill2=new MarketBillClass(15.00, market2);
      
      //check preconditions
      assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.receipts.size(), 0);      
      assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
                  + cashier.log.toString(), 0, cashier.log.size());
      
      //step 1 of the test

//      cashier.msgHereIsTheMarketBill(bill1);
//      cashier.msgHereIsTheMarketBill(bill2);
      
      //check postconditions for step 1 and preconditions for step 2
      assertEquals("MockMarket1 should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
                  + market1.log.toString(), 0, market1.log.size());
      
      assertEquals("MockMarket2 should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: "
            + market2.log.toString(), 0, market2.log.size());
      
      //assertEquals("Cashier should have 2 bills in it. It doesn't.", cashier.marketBills.size(), 2);
      
      
            
//      assertEquals("Cashier marketBill should know who the first market is. It doesn't.", cashier.marketBills.get(0).getBill().getMarket()
//            .getName() , "mockmarket1");
//      
//      assertEquals("Cashier marketBill should know who the second market is. It doesn't.", cashier.marketBills.get(1).getBill().getMarket()
//            .getName() , "mockmarket2");
      
      
//      assertEquals("Cashier marketBill should know what the price is. It doesn't.", cashier.marketBills.get(0).getBill().getOrderPrice(),
//            15.00);
      
//      assertEquals("Cashier marketBill should know what the price is. It doesn't.", cashier.marketBills.get(1).getBill().getOrderPrice(),
//            15.00);
      
     
      //step 2 of the test
//    cashier.ReadyToPay(customer, receipt);
//      assertTrue("CashierBill should contain a bill with state == calculated. It doesn't.",
//            cashier.marketBills.get(0).getState() == ReceiptState.calculated);
      
//      assertTrue("CashierBill should contain another bill with state == calculated. It doesn't.",
//            cashier.marketBills.get(1).getState() == ReceiptState.calculated);
//      
//      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
//      
//      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());

      //check postconditions for step 2 / preconditions for step 3
      
      
//      assertTrue("Mockwaiter should have gotten a receipt. It didn't.", waiter.log.containsString("Received receipt."));
      
//      assertEquals("Cash should have decreased. It did not", cashier.getCash(), 0.00);

      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      cashier.msgThankYouForYourPayment();
      cashier.msgThankYouForYourPayment();
      //check postconditions for step 3 / preconditions for step 4
      
      
      assertTrue("Cashier should have logged \"Received msgThankYouForYourPayment.\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgThankYouForYourPayment."));
      
      assertTrue("Cashier should have logged \"Received msgThankYouForYourPayment.\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgThankYouForYourPayment."));
      
    //  assertEquals("MockMarket1 should have only 1 logged event. It doesn't.", market1.log.size(), 1);
      
  //    assertEquals("MockMarket2 should have only 1 logged event. It doesn't.", market2.log.size(), 1);

      

      
      
      
      //step 4
//      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
//      
//      //check postconditions for step 4
//      assertTrue("Mockcustomer should have gotten a justPayNextTime message. It didn't.", customer.log
//            .containsString("Received msgJustPayNextTime from cashier."));
//      
//      assertEquals("Mockwaiter should have only 1 logged event. It doesn't.", waiter.log.size(), 1);
      
      assertTrue("Cashier should have no more bills.", cashier.marketBills.isEmpty());
      
   //   assertEquals("Cashier should have a total of $0.00", cashier.getCash(), 0.00);
      
      assertFalse("Cashier scheduler should return false. It didn't.", cashier.pickAndExecuteAnAction());
      
   
   }
}
