package newMarket.test;

import java.util.ArrayList;
import java.util.List;

import Buildings.Building;
import Market.MarketManagerAgent.MyOrderState;
import agents.Grocery;
import agents.Person;
import newMarket.MarketCashierAgent;
import newMarket.MarketCashierAgent.OrderState;
import newMarket.MarketCustomerAgent;
import newMarket.MarketDealerAgent;
//import restaurant.WaiterAgent.Bill;
//import newMarket.interfaces.Cashier;
//import newMarket.test.mock.MockCustomer;
//import newMarket.test.mock.MockMarket;
//import newMarket.test.mock.MockWaiter;

import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * 
 */
public class MarketCashierAgentTest extends TestCase
{
   //these are instantiated for each test separately via the setUp() method.
   MarketCashierAgent cashier;
   MarketCustomerAgent customer;
   Building temp;
   MarketDealerAgent dealer;
   

   /**
    * This method is run before each test. You can use it to instantiate the class variables
    * for your agent and mocks, etc.
    */
   public void setUp() throws Exception{
      super.setUp();    
      temp=new newMarket.NewMarket();
      cashier = new MarketCashierAgent();    
      cashier.money=(float)0.00;
     // MockPerson p=new MockPerson("TestCust");
//    MockMarketCustomer=new MockMarketCustomer(p,cashier);
      Person p=new Person("TestCust",true);
      customer = new MarketCustomerAgent(p, cashier,dealer); 

      
      
   }  
   /**
    * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
    */
   public void testOneNormalCustomerScenario()
   {
      //setUp() runs first before this test!
    
      List<Grocery> order=new ArrayList<Grocery>();
      order.add(new Grocery("Steak",5));
      
      //check preconditions
      assertEquals("MarketCashier should have 0 orders in it. It doesn't.",cashier.getOrders().size(), 0);      
      assertEquals("MarketCashierAgent should have an empty event log before the MarketCashier's msgIWantFood is called. Instead, the MarketCashier's event log reads: "
                  + cashier.log.toString(), 0, cashier.log.size());
      
      //step 1 of the test

      cashier.msgIWantFood(customer, order);

      //check postconditions for step 1 and preconditions for step 2
      assertEquals("Customer should have an empty event log before the MarketCashier's scheduler is called. Instead, the Customer's event log reads: "
                  + customer.log.toString(), 0, customer.log.size());
      
      assertEquals("MarketCashier should have 1 order in it. It doesn't.", cashier.getOrders().size(), 1);
      
      
      assertEquals(
            "Customer should have an empty event log after the MarketCashier's scheduler is called for the first time. Instead, the Customer's event log reads: "
                  + customer.log.toString(), 0, customer.log.size());
      
            
      assertEquals("MarketCashier order should know who the customer is. It doesn't.", cashier.getOrders().get(0).c.self.getName() , "TestCust");

      
      assertEquals("MarketCashier order should know what the grocery is. It doesn't.", cashier.getOrders().get(0).order.get(0).getFood(),
            "Steak");
      
      assertEquals("MarketCashier order should know what the amount is. It doesn't.", cashier.getOrders().get(0).order.get(0).getAmount(),
            5);
      
     
      //step 2 of the test
//    cashier.ReadyToPay(customer, receipt);
      assertTrue("MarketCashier should contain a order with state == pending. It doesn't.",
            cashier.getOrders().get(0).s == OrderState.pending);
      
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3
      
      
      assertTrue("Customer should have received the price. It didn't.", customer.log.containsString("Received msgHereIsPrice."));
      
      
      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      cashier.msgHereIsMoney(customer, (float)(5*15.99));
      
      //check postconditions for step 3 / preconditions for step 4
      
      assertEquals("MarketCashier's cash should have increased to 5*15.99. It did not", cashier.money, (float)(5*15.99));
      
      assertTrue("MarketCashier should have logged \"Received msgHereIsMoney, and Customer Paid\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgHereIsMoney, and Customer Paid"));
      
      
      assertEquals("MarketCashier order state should be paid", cashier.getOrders().get(0).s,
            OrderState.paid);
      
      
      assertEquals("Customer should have 1 logged events. It doesn't.", customer.log.size(), 1);
      

      
      
      
      //step 4
      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("Customer should have received. It didn't.", customer.log
            .containsString("Received msgHereIsFood."));
            
      assertTrue("MarketCashier should have no more orders.", cashier.getOrders().isEmpty());
      
      assertEquals("MarketCashier should have a total of 5*15.99", cashier.money, (float)(5*15.99));
      
      assertFalse("MarketCashier scheduler should return false. It didn't.", cashier.pickAndExecuteAnAction());
      
   
   }//end one normal customer scenario
   
   
   public void testCustomerCantPayScenario()
   {
    //setUp() runs first before this test!
      
      List<Grocery> order=new ArrayList<Grocery>();
      order.add(new Grocery("Steak",5));
      
      //check preconditions
      assertEquals("MarketCashier should have 0 orders in it. It doesn't.",cashier.getOrders().size(), 0);      
      assertEquals("MarketCashierAgent should have an empty event log before the MarketCashier's msgIWantFood is called. Instead, the MarketCashier's event log reads: "
                  + cashier.log.toString(), 0, cashier.log.size());
      
      //step 1 of the test

      cashier.msgIWantFood(customer, order);

      //check postconditions for step 1 and preconditions for step 2
      assertEquals("Customer should have an empty event log before the MarketCashier's scheduler is called. Instead, the Customer's event log reads: "
                  + customer.log.toString(), 0, customer.log.size());
      
      assertEquals("MarketCashier should have 1 order in it. It doesn't.", cashier.getOrders().size(), 1);
      
      
      assertEquals(
            "Customer should have an empty event log after the MarketCashier's scheduler is called for the first time. Instead, the Customer's event log reads: "
                  + customer.log.toString(), 0, customer.log.size());
      
            
      assertEquals("MarketCashier order should know who the customer is. It doesn't.", cashier.getOrders().get(0).c.self.getName() , "TestCust");

      
      assertEquals("MarketCashier order should know what the grocery is. It doesn't.", cashier.getOrders().get(0).order.get(0).getFood(),
            "Steak");
      
      assertEquals("MarketCashier order should know what the amount is. It doesn't.", cashier.getOrders().get(0).order.get(0).getAmount(),
            5);
      
     
      //step 2 of the test
//    cashier.ReadyToPay(customer, receipt);
      assertTrue("MarketCashier should contain a order with state == pending. It doesn't.",
            cashier.getOrders().get(0).s == OrderState.pending);
      
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3
      
      
      assertTrue("Customer should have received the price. It didn't.", customer.log.containsString("Received msgHereIsPrice."));
      
      
      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      cashier.msgHereIsMoney(customer, (float)(15.99));
      
      //check postconditions for step 3 / preconditions for step 4
      
      assertEquals("MarketCashier's cash should have not increased. It did not", cashier.money, (float)0.0);
      
      assertTrue("MarketCashier should have logged \"Received msgHereIsMoney, but Customer couldn't Pay\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgHereIsMoney, but Customer couldn't Pay"));
      
      
      assertEquals("MarketCashier order state should be unpaid", cashier.getOrders().get(0).s,
            OrderState.notEnoughPaid);
      
      
      assertEquals("Customer should have 1 logged events. It doesn't.", customer.log.size(), 1);
      

      
      
      
      //step 4
      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("Customer should have been kicked out. It didn't.", customer.log
            .containsString("Received msgGetOut."));
            
      assertTrue("MarketCashier should have no more orders.", cashier.getOrders().isEmpty());
      
      assertEquals("MarketCashier should have a total money of 0", cashier.money, (float)0.0);
      
      assertFalse("MarketCashier scheduler should return false. It didn't.", cashier.pickAndExecuteAnAction());
      
   
   }
   
   public void testTwoNormalCustomerScenario()
   {
      //setUp() runs first before this test!
      
      Person p2=new Person("TestCust2",true);
      MarketCustomerAgent customer2=new MarketCustomerAgent(p2, cashier,dealer);
      customer2 = new MarketCustomerAgent(p2, cashier,dealer); 
      
      List<Grocery> order=new ArrayList<Grocery>();
      order.add(new Grocery("Steak",5));
      List<Grocery> order2=new ArrayList<Grocery>();
      order2.add(new Grocery("Chicken",5));
      
      //check preconditions
      assertEquals("MarketCashier should have 0 orders in it. It doesn't.",cashier.getOrders().size(), 0);      
      assertEquals("MarketCashierAgent should have an empty event log before the MarketCashier's msgIWantFood is called. Instead, the MarketCashier's event log reads: "
                  + cashier.log.toString(), 0, cashier.log.size());
      
      //step 1 of the test

      cashier.msgIWantFood(customer, order);
      cashier.msgIWantFood(customer2, order2);

      //check postconditions for step 1 and preconditions for step 2
      assertEquals("Customer should have an empty event log before the MarketCashier's scheduler is called. Instead, the Customer's event log reads: "
                  + customer.log.toString(), 0, customer.log.size());
      
      assertEquals("MarketCashier should have 2 order in it. It doesn't.", cashier.getOrders().size(), 2);
      
      
      assertEquals(
            "Customer should have an empty event log after the MarketCashier's scheduler is called for the first time. Instead, the Customer's event log reads: "
                  + customer.log.toString(), 0, customer.log.size());
      assertEquals(
            "Customer2 should have an empty event log after the MarketCashier's scheduler is called for the first time. Instead, the Customer's event log reads: "
                  + customer2.log.toString(), 0, customer2.log.size());
            
      assertEquals("MarketCashier order should know who the customer is. It doesn't.", cashier.getOrders().get(0).c.self.getName() , "TestCust");

      
      assertEquals("MarketCashier order should know what the grocery is. It doesn't.", cashier.getOrders().get(0).order.get(0).getFood(),
            "Steak");
      
      assertEquals("MarketCashier order should know what the amount is. It doesn't.", cashier.getOrders().get(0).order.get(0).getAmount(),
            5);
      
      assertEquals("MarketCashier order should know who the customer2 is. It doesn't.", cashier.getOrders().get(1).c.self.getName() , "TestCust2");

      
      assertEquals("MarketCashier order should know what the grocery is. It doesn't.", cashier.getOrders().get(1).order.get(0).getFood(),
            "Chicken");
      
      assertEquals("MarketCashier order should know what the amount is. It doesn't.", cashier.getOrders().get(1).order.get(0).getAmount(),
            5);
      
     
      //step 2 of the test
//    cashier.ReadyToPay(customer, receipt);
      assertTrue("MarketCashier should contain a order with state == pending. It doesn't.",
            cashier.getOrders().get(0).s == OrderState.pending);
      
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3
      
      
      assertTrue("Customer should have received the price. It didn't.", customer.log.containsString("Received msgHereIsPrice."));
      
      assertTrue("MarketCashier should contain another order with state == pending. It doesn't.",
            cashier.getOrders().get(1).s == OrderState.pending);
      
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3 for customer2
      
      
      assertTrue("Customer2 should have received the price. It didn't.", customer2.log.containsString("Received msgHereIsPrice."));
      
      
      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      cashier.msgHereIsMoney(customer, (float)(5*15.99));
      
      //check postconditions for step 3 / preconditions for step 4
      
      assertEquals("MarketCashier's cash should have increased to 5*15.99. It did not", cashier.money, (float)(5*15.99));
      
      assertTrue("MarketCashier should have logged \"Received msgHereIsMoney, and Customer Paid\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgHereIsMoney, and Customer Paid"));
      
      
      assertEquals("MarketCashier order state should be paid", cashier.getOrders().get(0).s,
            OrderState.paid);
      
      
      assertEquals("Customer should have 1 logged events. It doesn't.", customer.log.size(), 1);
      
      cashier.msgHereIsMoney(customer2, (float)(5*10.99));
      
      //check postconditions for step 3 / preconditions for step 4 for customer2
      
      assertEquals("MarketCashier's cash should have increased by 5*10.99. It did not", cashier.money, ((float)(5*15.99)+(float)(5*10.99)));
      
      assertTrue("MarketCashier should have logged \"Received msgHereIsMoney, and Customer Paid\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgHereIsMoney, and Customer Paid"));
      
      
      assertEquals("MarketCashier order state should be paid", cashier.getOrders().get(1).s,
            OrderState.paid);
      
      
      assertEquals("Customer should have 1 logged events. It doesn't.", customer2.log.size(), 1);
      
      
      
      
      //step 4
      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("Customer should have received. It didn't.", customer.log
            .containsString("Received msgHereIsFood."));
      
      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("Customer2 should have received. It didn't.", customer2.log
            .containsString("Received msgHereIsFood."));
            
      assertTrue("MarketCashier should have no more orders.", cashier.getOrders().isEmpty());
      
      assertEquals("MarketCashier should have a total of 5*15.99+*10.99", cashier.money, ((float)(5*15.99)+(float)(5*10.99)));
      
      assertFalse("MarketCashier scheduler should return false. It didn't.", cashier.pickAndExecuteAnAction());
      
   
   }
   
   public void testTwoNormalCustomerScenarioButOneCantPay()
   {
      //setUp() runs first before this test!
      
      Person p2=new Person("TestCust2",true);
      MarketCustomerAgent customer2=new MarketCustomerAgent(p2, cashier,dealer);
      customer2 = new MarketCustomerAgent(p2, cashier,dealer); 
      
      List<Grocery> order=new ArrayList<Grocery>();
      order.add(new Grocery("Steak",5));
      List<Grocery> order2=new ArrayList<Grocery>();
      order2.add(new Grocery("Chicken",5));
      
      //check preconditions
      assertEquals("MarketCashier should have 0 orders in it. It doesn't.",cashier.getOrders().size(), 0);      
      assertEquals("MarketCashierAgent should have an empty event log before the MarketCashier's msgIWantFood is called. Instead, the MarketCashier's event log reads: "
                  + cashier.log.toString(), 0, cashier.log.size());
      
      //step 1 of the test

      cashier.msgIWantFood(customer, order);
      cashier.msgIWantFood(customer2, order2);

      //check postconditions for step 1 and preconditions for step 2
      assertEquals("Customer should have an empty event log before the MarketCashier's scheduler is called. Instead, the Customer's event log reads: "
                  + customer.log.toString(), 0, customer.log.size());
      
      assertEquals("MarketCashier should have 2 order in it. It doesn't.", cashier.getOrders().size(), 2);
      
      
      assertEquals(
            "Customer should have an empty event log after the MarketCashier's scheduler is called for the first time. Instead, the Customer's event log reads: "
                  + customer.log.toString(), 0, customer.log.size());
      assertEquals(
            "Customer2 should have an empty event log after the MarketCashier's scheduler is called for the first time. Instead, the Customer's event log reads: "
                  + customer2.log.toString(), 0, customer2.log.size());
            
      assertEquals("MarketCashier order should know who the customer is. It doesn't.", cashier.getOrders().get(0).c.self.getName() , "TestCust");

      
      assertEquals("MarketCashier order should know what the grocery is. It doesn't.", cashier.getOrders().get(0).order.get(0).getFood(),
            "Steak");
      
      assertEquals("MarketCashier order should know what the amount is. It doesn't.", cashier.getOrders().get(0).order.get(0).getAmount(),
            5);
      
      assertEquals("MarketCashier order should know who the customer2 is. It doesn't.", cashier.getOrders().get(1).c.self.getName() , "TestCust2");

      
      assertEquals("MarketCashier order should know what the grocery is. It doesn't.", cashier.getOrders().get(1).order.get(0).getFood(),
            "Chicken");
      
      assertEquals("MarketCashier order should know what the amount is. It doesn't.", cashier.getOrders().get(1).order.get(0).getAmount(),
            5);
      
     
      //step 2 of the test
//    cashier.ReadyToPay(customer, receipt);
      assertTrue("MarketCashier should contain a order with state == pending. It doesn't.",
            cashier.getOrders().get(0).s == OrderState.pending);
      
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3
      
      
      assertTrue("Customer should have received the price. It didn't.", customer.log.containsString("Received msgHereIsPrice."));
      
      assertTrue("MarketCashier should contain another order with state == pending. It doesn't.",
            cashier.getOrders().get(1).s == OrderState.pending);
      
      assertTrue("Cashier's schedule should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      
      //check postconditions for step 2 / preconditions for step 3 for customer2
      
      
      assertTrue("Customer2 should have received the price. It didn't.", customer2.log.containsString("Received msgHereIsPrice."));
      
      
      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      cashier.msgHereIsMoney(customer, (float)(5*15.99));
      
      //check postconditions for step 3 / preconditions for step 4
      
      assertEquals("MarketCashier's cash should have increased to 5*15.99. It did not", cashier.money, (float)(5*15.99));
      
      assertTrue("MarketCashier should have logged \"Received msgHereIsMoney, and Customer Paid\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgHereIsMoney, and Customer Paid"));
      
      
      assertEquals("MarketCashier order state should be paid", cashier.getOrders().get(0).s,
            OrderState.paid);
      
      
      assertEquals("Customer should have 1 logged events. It doesn't.", customer.log.size(), 1);
      
      cashier.msgHereIsMoney(customer2, (float)(10.99));
      
      //check postconditions for step 3 / preconditions for step 4 for customer2
      
      assertEquals("MarketCashier's cash should not have increased. It did not", cashier.money, ((float)(5*15.99)));
      
      assertTrue("MarketCashier should have logged \"Received msgHereIsMoney, but Customer couldn't Pay\" but didn't. His log reads instead: " 
            + cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgHereIsMoney, but Customer couldn't Pay"));
      
      
      assertEquals("MarketCashier order state should be paid", cashier.getOrders().get(1).s,
            OrderState.notEnoughPaid);
      
      
      assertEquals("Customer should have 1 logged events. It doesn't.", customer2.log.size(), 1);
      
      
      
      
      //step 4
      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("Customer should have received. It didn't.", customer.log
            .containsString("Received msgHereIsFood."));
      
      assertTrue("Cashier's scheduler should have returned true. It didn't.", cashier.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("Customer2 should have been kicked out. It didn't.", customer2.log
            .containsString("Received msgGetOut."));
            
      assertTrue("MarketCashier should have no more orders.", cashier.getOrders().isEmpty());
      
      assertEquals("MarketCashier should have a total of 5*15.99", cashier.money, ((float)(5*15.99)));
      
      assertFalse("MarketCashier scheduler should return false. It didn't.", cashier.pickAndExecuteAnAction());
      
   
   }
}