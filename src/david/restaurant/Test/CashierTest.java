package david.restaurant.Test;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import david.restaurant.Bill;
import david.restaurant.CashierAgent;
import david.restaurant.CashierAgent.checkState;
import david.restaurant.Check;
import david.restaurant.MarketAgent;
import david.restaurant.RestockList;
import david.restaurant.Interfaces.Market;
import david.restaurant.Test.Mock.EventLog;
import david.restaurant.Test.Mock.MockCook;
import david.restaurant.Test.Mock.MockCustomer;
import david.restaurant.Test.Mock.MockMarket;
import david.restaurant.Test.Mock.MockWaiter;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class CashierTest extends TestCase
{
        //these are instantiated for each test separately via the setUp() method.
        CashierAgent cashier;
        List<Market> markets;
        MockWaiter waiter;
        List<MockWaiter> waiters;
        MockCustomer customer;
        List<MockCustomer> customers;
        MockCook cook;
        
        public void setUp() throws Exception
        {
        	super.setUp();
        	markets = Collections.synchronizedList(new ArrayList<Market>());
        	waiters = Collections.synchronizedList(new ArrayList<MockWaiter>());
        	customers = Collections.synchronizedList(new ArrayList<MockCustomer>());
        	markets.add(new MockMarket("m1"));
        	markets.add(new MockMarket("m2"));
        	//cashier = new CashierAgent(markets);
        	waiter = new MockWaiter("Joe");
        	customer = new MockCustomer("David");
        	cook = new MockCook("Cook");
        	//cashier.startThread();
        	System.out.println("In Setup");
        	waiters.add(new MockWaiter("w1"));
        	waiters.add(new MockWaiter("w2"));
        	waiters.add(new MockWaiter("w3"));
        	customers.add(new MockCustomer("c1"));
        	customers.add(new MockCustomer("c2"));
        	customers.add(new MockCustomer("c3"));
        }
        
        public void testOne()
        {
        	//test for lab. doesn't really count
        	/*assertEquals("Cashier should have 0 checks, but currently has non-0 checks",cashier.checks.size(), 0); 
        	cashier.msgProcessOrder(waiter, customer, "Steak");
        	while(waiter.log.size() == 0)
        		;
        	System.out.print(waiter.log.getLastLoggedEvent().toString());*/
        }
        
        public void testTwo()
        {
        	//plan
        	/*
        	 * -Create a real market, real cashier and a mock cook (already done in setup)
        	 * -have the mock cook order an amount of food that is fulfillable in one order by the market
        	 * -after the cashier receives a bill, print saying that it actually got it.
        	 * -use asserts later, print for now since to use asserts, i will have to actually
        	 *  modify the agent code to get access to the execute action function.
        	 */
        	System.out.println("Cashier Test Two");
        	RestockList list = new RestockList();
        	list.itemAmounts.add(1);
        	list.items.add("Steak");
        	markets.get(0).msgNeedFood(cook, list);
        	cashier.msgHereIsBill(new Bill(markets.get(0).getName() + ": 1", 15.99f));
        	assertEquals("Cashier should have 1 bills after getting message", 1, cashier.bills.size());
        	cashier.pickAndExecuteAnAction();
        	assertEquals("Cashier should have 0 bills after getting message", 0, cashier.bills.size());
        	assertEquals("Market should have 1 have received bill", 1, ((MockMarket)markets.get(0)).log.size());
        }
        
        public void testThree()
        {
        	//plan
        	/*
        	 * -create a real market, real cashier, and a mock cook. 
        	 * -have the cook order an amount of food that can't be fulfilled by one market, but can be fulfilled by
        	 * 	two
        	 * -after the cashier receives the bills, print saying that it actually got the bill
        	 * -use asserts later, print for now since to use asserts, i will have to actually
        	 *  modify the agent code to get access to the execute action function.
        	 */
        	System.out.println("Cashier Test Three");
        	RestockList list = new RestockList();
        	list.itemAmounts.add(5);
        	list.items.add("Steak");
        	markets.get(0).msgNeedFood(cook, list);
        	cashier.msgHereIsBill(new Bill(markets.get(0).getName() + ": 1", 4 * 15.99f));
        	cashier.msgHereIsBill(new Bill(markets.get(1).getName() + ": 1", 1 * 15.99f));
        	assertEquals("Cashier should have 2 bills", 2, cashier.bills.size());
        	for(int i = 0; i < 2; i++)
        	{
        		cashier.pickAndExecuteAnAction();
        	}
        	assertEquals("Cashier should have 0 bills", cashier.bills.size(), 0);
        	assertEquals("Market 0 should have received 1 payment", 1, ((MockMarket)markets.get(0)).log.getInstancesOfLog("GotMoney"));
        	assertEquals("Market 1 should have received 1 payment", 1, ((MockMarket)markets.get(1)).log.getInstancesOfLog("GotMoney"));
        	System.out.println("done");
        }
        
        public void testFour()
        {
        	//test plan
        	/*
        	 * -Test the normative scenario in which the waiter asks the cashier a check
        	 *  then the customer pays the check
        	 *  	-make sure that the cashier actually makes the check and sends it
        	 *  		-check the waiter for this
        	 *  	-make sure that the cashier gets the check back from the customer
        	 *  	-make sure that the cashier processes, then deletes the check 
        	 *  	 from the customer
        	 */
        	cashier.msgProcessOrder(waiter, customer, "Steak");
        	assertEquals("Cashier must have 1", 1, cashier.checks.size());
        	assertEquals("The check must be in unprocessedstate", checkState.unprocessed, cashier.checks.get(0).state);
        	
        	cashier.pickAndExecuteAnAction();
        	assertEquals("Waiter should have received check", 1, waiter.log.getInstancesOfLog("check"));
        	customer.msgHereIsCheck(waiter.checks.get(0));
        	waiter.checks.remove(0);
        	cashier.msgHereIsMoney(customer.checks.get(0), customer.checks.get(0).balance);
        	assertEquals("Cashier should have a check with balance of 0", 0.0f, cashier.checks.get(0).check.balance);
        	cashier.pickAndExecuteAnAction();
        	assertEquals("Cashier must have 0 checks", 0, cashier.checks.size());
        }
        
        public void testFive()
        {
        	//test plan
        	/*
        	 * -test the non-normative scenario where the customer can't pay
        	 */
        	cashier.msgProcessOrder(waiter, customer, "Steak");
        	assertEquals("Cashier must have 1", 1, cashier.checks.size());
        	assertEquals("The check must be in unprocessedstate", checkState.unprocessed, cashier.checks.get(0).state);
        	
        	cashier.pickAndExecuteAnAction();
        	while(waiter.log.size() == 0)
        		;
        	assertEquals("Waiter should have received check", 1, waiter.log.getInstancesOfLog("check"));
        	customer.msgHereIsCheck(waiter.checks.get(0));
        	waiter.checks.remove(0);
        	cashier.msgCantPay(customer.checks.get(0));
        	System.out.println(Float.toString(cashier.checks.get(0).check.balance));
        	assertEquals("Cashier should have a check with balance of 15.99", 15.99f, cashier.checks.get(0).check.balance);
        	cashier.pickAndExecuteAnAction();
        	assertEquals("Cashier must have 1 check", 1, cashier.checks.size());
        }
        
        public void testSix()
        {
        	//test plan
        	/*
        	 * test a full scenario
        	 * -cashier gets a request from 2 waiters & a bill
        	 * -one customer can pay, one can't
        	 * -the cashier must then pay the bill 
        	 */
        	cashier.msgProcessOrder(waiters.get(0), customers.get(0), "Steak");
        	cashier.msgProcessOrder(waiters.get(1), customers.get(1), "Chicken");
        	
        	assertEquals("Cashier should have 2 checks", 2, cashier.checks.size());
        	
        	customers.get(0).msgHereIsCheck(cashier.checks.get(0).check);
        	customers.get(1).msgHereIsCheck(cashier.checks.get(1).check);
        	
        	for(int i = 0; i < 2; i++)
        	{
        		cashier.pickAndExecuteAnAction();
        	}
        	
        	assertEquals("Cashier should have 2 checks", 2, cashier.checks.size());
        	
        	cashier.msgHereIsBill(new Bill(((MockMarket)markets.get(0)).getName() + ": 1", 15.99f));
        	assertEquals("Cashier should have 1 bill", 1, cashier.bills.size());
        	
        	cashier.pickAndExecuteAnAction();
        	assertEquals("Cashier should have 0 bills", 0, cashier.bills.size());
        	
        	cashier.msgHereIsMoney(customers.get(0).checks.get(0), 15.99f);
        	cashier.msgCantPay(customers.get(1).checks.get(0));
        	
        	for(int i = 0; i < 2; i++)
        	{
        		cashier.pickAndExecuteAnAction();
        	}
        	
        	assertEquals("Cashier should have 1 check", 1, cashier.checks.size());
        }
        
        public void testSeven()
        {
	    	//test plan
	    	/*
	    	 * test a full scenario
	    	 * -similar to testSix, however rather than having one bill
	    	 * -have two and make it so that he can pay one, but not the other
	    	 * -this is to make sure the correct messages are sent to the market
	    	 */
        	
        	cashier.msgProcessOrder(waiters.get(0), customers.get(0), "Steak");
        	cashier.msgProcessOrder(waiters.get(1), customers.get(1), "Chicken");
        	
        	assertEquals("Cashier should have 2 checks", 2, cashier.checks.size());
        	
        	customers.get(0).msgHereIsCheck(cashier.checks.get(0).check);
        	customers.get(1).msgHereIsCheck(cashier.checks.get(1).check);
        	
        	for(int i = 0; i < 2; i++)
        	{
        		cashier.pickAndExecuteAnAction();
        	}
        	
        	assertEquals("Cashier should have 2 checks", 2, cashier.checks.size());
        	
        	cashier.msgHereIsBill(new Bill(markets.get(0).getName() + ": 1", 4 * 15.99f));
        	cashier.msgHereIsBill(new Bill(markets.get(1).getName() + ": 1", 1 * 15.99f));
        	assertEquals("Cashier should have 1 bill", 2, cashier.bills.size());
        	
        	for(int i = 0; i < 2; i++)
        	{
        		cashier.pickAndExecuteAnAction();
        	}
        	assertEquals("Cashier should have 0 bills", 0, cashier.bills.size());
        	
        	cashier.msgHereIsMoney(customers.get(0).checks.get(0), 15.99f);
        	cashier.msgCantPay(customers.get(1).checks.get(0));
        	
        	for(int i = 0; i < 2; i++)
        	{
        		cashier.pickAndExecuteAnAction();
        	}
        	
        	assertEquals("Cashier should have 1 check", 1, cashier.checks.size());
        }
}