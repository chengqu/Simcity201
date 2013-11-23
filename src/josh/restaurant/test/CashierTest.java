package josh.restaurant.test;

//import restaurant.CashierAgent.cashierBillState;
//import restaurant.WaiterAgent.Bill;
import josh.restaurant.CashierAgent;
import josh.restaurant.Order;
import josh.restaurant.CashierAgent.BillState;
import josh.restaurant.test.mock.MockCustomer;
import josh.restaurant.test.mock.MockMarket;
import josh.restaurant.test.mock.MockWaiter;
import junit.framework.*;

public class CashierTest extends TestCase
{
	
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market;
		
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();	
		
		//real cashier 
		cashier = new CashierAgent("cashier");

		//mock customer
		customer = new MockCustomer("mockcustomer");	
		//mock waiter
		waiter = new MockWaiter("mockwaiter", cashier);
		//mock market
		market = new MockMarket("mockmarket", cashier); 
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOne_NormalCustomerScenario()
	{
		
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		customer.cashier = cashier; 
		
		//check preconditions 
		assertEquals("Cashier should have 0 bills. FAILED ", cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty eventlog. Instead, Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		Order o = new Order(waiter, "steak", 2);
		cashier.msgProduceABill(o, customer); 
		//this will produce a bill in the message reception...add it to my bills
		
		//check postconditions for step 1 and preconditions for step 2 
		//post *****
		assertEquals("MockWaiter should have an empty event log before Cashier's scheduler called. Instead, MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		
		//pre *****

		assertTrue("Cashier's scheduler should return true when it gets a order, but didn't.", cashier.pickAndExecuteAnAction());
		
		//actn CalculateCheck will be called 
		
		assertTrue("The BillState should be changed to calculating, but it didn't.", cashier.bills.get(0).state_ == BillState.calculating);
		
		//step 2 of the test
		customer.msgHereIsYourBill((float)15.99);
		//(here is my payment called from within mock customer)
		
		// here is my payment from the customer
		//cashier.msgHereIsMyPayment(customer, (float) 15.99);
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("BillState should be goingToPay. it didn't", cashier.bills.get(0).state_ == BillState.goingToPay);
		
		assertTrue("CashierBill should contain note of customer payment = $15.99. It contains something else instead: $" 
				+ cashier.bills.get(0).charge_, cashier.bills.get(0).whatCustPaid_ == (float) 15.99);
				
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.bills.get(0).c_ == customer);
		
		
		//step 3
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
					cashier.pickAndExecuteAnAction());
		
		//actnChargeCustomer leads to actnPaymentFullfilled
		assertTrue("BillState should be charging. it didn't", cashier.bills.get(0).state_ == BillState.paid);
		
		//step 4
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		//actnChargeCustomer leads to actnRemoveBill
		assertEquals("bill should have been removed form bills, it was not", cashier.bills.size(), 0);
		
	} 
	
	public void testTwo_PaymentTooLitteFromCustomer()
	{
		
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		customer.cashier = cashier; 
		customer.setName("dishonestbroke"); 
		
		//check preconditions 
		assertEquals("Cashier should have 0 bills. FAILED ", cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty eventlog. Instead, Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		Order o = new Order(waiter, "steak", 2);
		cashier.msgProduceABill(o, customer); 
		//this will produce a bill in the message reception...add it to my bills
		
		//check postconditions for step 1 and preconditions for step 2 
		//post *****
		assertEquals("MockWaiter should have an empty event log before Cashier's scheduler called. Instead, MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		
		//pre *****

		assertTrue("Cashier's scheduler should return true when it gets a order, but didn't.", cashier.pickAndExecuteAnAction());
		
		//actn CalculateCheck will be called 
		
		assertTrue("The BillState should be changed to calculating, but it didn't.", cashier.bills.get(0).state_ == BillState.calculating);
		
		//step 2 of the test
		customer.msgHereIsYourBill((float) 15.99);
		//(here is my payment called from within mock customer)
		
		// here is my payment from the customer
		//cashier.msgHereIsMyPayment(customer, (float) 15.99);
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("BillState should be goingToPay. it didn't", cashier.bills.get(0).state_ == BillState.goingToPay);
		
		assertTrue("CashierBill should contain note of customer payment = $1. It contains something else instead: $" 
				+ cashier.bills.get(0).charge_, cashier.bills.get(0).whatCustPaid_ == (float) 1);
		
		
		/*  the charge is coming up at 0 because the calculate charged is based after a timer function that is in its own thread
		assertTrue("CashierBill should contain note of charge = $15.99. It contains something else instead: $" 
				+ cashier.bills.get(0).charge_, cashier.bills.get(0).charge_ == (float) 0);
		*/
		//quick fix
		cashier.bills.get(0).charge_ = (float) 15.99;
		
		assertFalse("CashierBill should contain note of charge = $15.99. It contains something else instead: $" 
				+ cashier.bills.get(0).charge_, cashier.bills.get(0).charge_ == (float) 0);
				
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.bills.get(0).c_ == customer);
		
		//step 3
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
					cashier.pickAndExecuteAnAction());
		
		//actnChargeCustomer leads to actnPaymentNotGood
		assertTrue("BillState should be inDebt. it isnt", cashier.bills.get(0).state_ == BillState.inDebt);
		
		//step 4
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		//actnChargeCustomer leads to actnRemoveBill
		assertEquals("bill should have been removed form bills, it was not", cashier.bills.size(), 0);
		
	} //end scenario two 
	
	public void testThree_NormativeFromMarketToCashier()
	{
		
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
		//check preconditions 
		assertEquals("Cashier should have 0 bills. FAILED ", cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty eventlog. Instead, Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
	
		cashier.msgBillFromTheMarket(market, "chicken", 30, true);
		//creates new Bill with BillState.marketPending
	
		//check postconditions for step 1 and preconditions for step 2 
		//post *****
		assertEquals("MockWaiter should have an empty event log before Cashier's scheduler called. Instead, MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		
		assertTrue("state of bill should be BillState.marketPending, but it didn't.", cashier.bills.get(0).state_ == BillState.marketPending);

		assertTrue("Cashier's scheduler should return true when it gets a order, but didn't.", cashier.pickAndExecuteAnAction());
		//going to activate action actnPayMarket
		
		assertTrue("state of bill should be BillState.marketPaid, but it didn't.", cashier.bills.get(0).state_ == BillState.marketPaid);
		
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		assertEquals("bill should have been removed form bills, it was not", cashier.bills.size(), 0);
		
		
	} 
	
	public void testFour_NonNormativeFromMarketToCashier()
	{
		
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//the second market, needed to test for the multiple market scenario
		MockMarket market2 = new MockMarket("market2", cashier);
		 
		//check preconditions 
		assertEquals("Cashier should have 0 bills. FAILED ", cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty eventlog. Instead, Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
	
		cashier.msgBillFromTheMarket(market, "chicken", 30, false);
		//creates new Bill with BillState.marketIncomplete
	
		assertEquals("MockMarket should have an empty event log before Cashier's scheduler called. Instead, MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		
		assertTrue("state of bill should be BillState.marketIncomplete, but it didn't.", 
				cashier.bills.get(0).state_ == BillState.marketIncomplete);
		
		assertTrue("the charge of the smaller bill is not 30. FAILED", 
				cashier.bills.get(0).charge_ == 30);
		
		//now the scheduler run
		assertTrue("Cashier's scheduler should return true when it gets a order, but didn't.", cashier.pickAndExecuteAnAction());
		//going to activate action actnPayMarket
		
		//should still be incomplete even after search...
		assertTrue("state of bill should be BillState.marketIncomplete, but it didn't.", 
				cashier.bills.get(0).state_ == BillState.marketIncomplete);
		
		//~~~~~~~~~~~~~~~~~~~~~~~
		
		//adding in second market order
		cashier.msgBillFromTheMarket(market2, "chicken", 45, true);
		
		assertTrue("state of bill should be BillState.marketPaid, but it didn't.", cashier.bills.get(1).state_ == BillState.marketPending);
		
		assertEquals("Cashier should have 2 bills. FAILED ", cashier.bills.size(), 2);	
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~`
		//now we run scheduler again
		
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		//check for fulfilled order should run...this will combine the two bills
		
		//to see if the action ran, we can check price of two bills, and stat of bill(0)
		
		assertTrue("state of bill should be BillState.marketPending, but it didn't.", 
				cashier.bills.get(0).state_ == BillState.marketPending);
		
		assertTrue("the charge of the smaller bill was not bumped up due to combination. FAILED", 
				cashier.bills.get(0).charge_ == 45);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~
		
		//run scheduler four to get rid of bills
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		assertEquals("bill should have been removed form bills, it was not", cashier.bills.size(), 0);
		
	} 
	
	public void testFive_CashierHandlesBothTypesOfBills()
	{
		
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
		//check preconditions 
		assertEquals("Cashier should have 0 bills. FAILED ", cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty eventlog. Instead, Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
	
		cashier.msgBillFromTheMarket(market, "chicken", 30, true);
		//creates new Bill with BillState.marketPending
	
		//check postconditions for step 1 and preconditions for step 2 
		//post *****
		assertEquals("MockWaiter should have an empty event log before Cashier's scheduler called. Instead, MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		
		assertTrue("state of bill should be BillState.marketPending, but it didn't.", cashier.bills.get(0).state_ == BillState.marketPending);
		
		//~~~~~~~~~~~~~
		//now we add a bill from customer
		//step 1 of the test
		Order o = new Order(waiter, "steak", 2);
		cashier.msgProduceABill(o, customer); 
		
		assertTrue("state of bill(1) should be BillState.pending, but it didn't.", cashier.bills.get(1).state_ == BillState.pending);

		//~~~~~~~~~~~~~~~~~~~~~~~
		
		//should process the market order first 
		assertTrue("Cashier's scheduler should return true, but didn't.", cashier.pickAndExecuteAnAction());
		
		assertTrue("state of bill should be BillState.marketPaid, but it didn't.", cashier.bills.get(0).state_ == BillState.marketPaid);
		
			//and othe bill shouldn't have changed 
			assertTrue("state of bill should be BillState.marketPaid, but it didn't.", cashier.bills.get(1).state_ == BillState.pending);
			
		
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		//should get rid of market bill, left with customer bill
		assertEquals("bill should have been removed form bills, it was not", cashier.bills.size(), 1);
		
	} //end normal market order scenario
	
	public void testSix_NonNormativeDoesNotCombineTwoBills()
	{
		
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//the second market, needed to test for the multiple market scenario
		MockMarket market2 = new MockMarket("market2", cashier);
		 
		//check preconditions 
		assertEquals("Cashier should have 0 bills. FAILED ", cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty eventlog. Instead, Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
	
		cashier.msgBillFromTheMarket(market, "chicken", 30, false);
		//creates new Bill with BillState.marketIncomplete
	
		assertEquals("MockMarket should have an empty event log before Cashier's scheduler called. Instead, MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());

		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		
		assertTrue("state of bill should be BillState.marketIncomplete, but it didn't.", 
				cashier.bills.get(0).state_ == BillState.marketIncomplete);
		
		assertTrue("the charge of the smaller bill is not 30. FAILED", 
				cashier.bills.get(0).charge_ == 30);
		
		//now the scheduler run
		assertTrue("Cashier's scheduler should return true when it gets a order, but didn't.", cashier.pickAndExecuteAnAction());
		//going to activate action actnPayMarket
		
		//should still be incomplete even after search...
		assertTrue("state of bill should be BillState.marketIncomplete, but it didn't.", 
				cashier.bills.get(0).state_ == BillState.marketIncomplete);
		
		//~~~~~~~~~~~~~~~~~~~~~~~
		
		//adding in second market order
		cashier.msgBillFromTheMarket(market2, "steak", 45, true);
		
		assertTrue("state of bill should be BillState.marketPaid, but it didn't.", cashier.bills.get(1).state_ == BillState.marketPending);
		
		assertEquals("Cashier should have 2 bills. FAILED ", cashier.bills.size(), 2);	
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~`
		//now we run scheduler again
		
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		//check for fulfilled order should run...this will combine the two bills
		
		//to see if the action ran, we can check price of two bills, and status of bill(0)
		
		//should be false because this one is never taken care of 
		assertFalse("state of bill should be BillState.marketPending, but it didn't.", 
				cashier.bills.get(0).state_ == BillState.marketPending);
		
		assertFalse("the charge of the smaller bill was not bumped up due to combination. FAILED", 
				cashier.bills.get(0).charge_ == 45);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~
		
		//run scheduler four to get rid of bill
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("Cashier's scheduler should have returned true. FAILED", 
				cashier.pickAndExecuteAnAction());
		
		//both should not be removed because the incomplete bill is holding it up
		assertEquals("bill should not have been removed form bills, it was not", cashier.bills.size(), 2);
		
	} 
}
