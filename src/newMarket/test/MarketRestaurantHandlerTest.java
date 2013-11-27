package newMarket.test;

import java.util.ArrayList;
import java.util.List;

import simcity201.interfaces.NewMarketInteraction;
import Buildings.Building;
import agents.Grocery;
import agents.Person;
import agents.TruckAgent;
import newMarket.MarketRestaurantHandlerAgent;
import newMarket.MarketRestaurantHandlerAgent.OrderState;
import newMarket.MarketRestaurantHandlerAgent;
import newMarket.NewMarket;
//import restaurant.WaiterAgent.Bill;
//import newMarket.interfaces.restaurantHandler;
//import newMarket.test.mock.Mockrestaurant;
//import newMarket.test.mock.MockMarket;
//import newMarket.test.mock.MockWaiter;

import newMarket.MarketRestaurantHandlerAgent;
import newMarket.test.mock.MockRestaurant;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the restaurantHandlerAgent's basic interaction
 * with waiters, restaurants, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * 
 */
public class MarketRestaurantHandlerTest extends TestCase
{
   
   //these are instantiated for each test separately via the setUp() method.
   MarketRestaurantHandlerAgent restaurantHandler;
   MockRestaurant restaurant;
   MockRestaurant restaurant2;
   Building temp;
   

   /**
    * This method is run before each test. You can use it to instantiate the class variables
    * for your agent and mocks, etc.
    */
   public void setUp() throws Exception{
      super.setUp();    
      temp=new newMarket.NewMarket();
      restaurantHandler = new MarketRestaurantHandlerAgent();    
      restaurantHandler.money=0;
      restaurant = new MockRestaurant("restaurant");
      
      
   }  
   /**
    * This tests the restaurantHandler under very simple terms: one restaurant is ready to pay the exact bill.
    */
   public void testOneNormalrestaurantScenario()
   {
      //setUp() runs first before this test!
      restaurant.restaurantHandler=restaurantHandler;
      List<Grocery> order=new ArrayList<Grocery>();
      order.add(new Grocery("Steak",5));
      
      //check preconditions
      assertEquals("MarketrestaurantHandler should have 0 orders in it. It doesn't.",restaurantHandler.getOrders().size(), 0);      
      assertEquals("MarketrestaurantHandlerAgent should have an empty event log before the MarketrestaurantHandler's msgIWantFood is called. Instead, the MarketrestaurantHandler's event log reads: "
                  + restaurantHandler.log.toString(), 0, restaurantHandler.log.size());
      
      //step 1 of the test

      restaurantHandler.msgIWantFood(restaurant, order);

      //check postconditions for step 1 and preconditions for step 2
      assertEquals("restaurant should have an empty event log before the MarketrestaurantHandler's scheduler is called. Instead, the restaurant's event log reads: "
                  + restaurant.log.toString(), 0, restaurant.log.size());
      
      assertEquals("MarketrestaurantHandler should have 1 order in it. It doesn't.", restaurantHandler.getOrders().size(), 1);
      
      
      assertEquals(
            "restaurant should have an empty event log after the MarketrestaurantHandler's scheduler is called for the first time. Instead, the restaurant's event log reads: "
                  + restaurant.log.toString(), 0, restaurant.log.size());
      
            
//      assertEquals("MarketrestaurantHandler order should know who the restaurant is. It doesn't.", restaurantHandler.getOrders().get(0).c.name , "restaurant");

      
      assertEquals("MarketrestaurantHandler order should know what the grocery is. It doesn't.", restaurantHandler.getOrders().get(0).order.get(0).getFood(),
            "Steak");
      
      assertEquals("MarketrestaurantHandler order should know what the amount is. It doesn't.", restaurantHandler.getOrders().get(0).order.get(0).getAmount(),
            5);
      
     
      //step 2 of the test
//    restaurantHandler.ReadyToPay(restaurant, receipt);
      assertTrue("MarketrestaurantHandler should contain a order with state == pending. It doesn't.",
            restaurantHandler.getOrders().get(0).s == OrderState.pending);
      
      assertTrue("restaurantHandler's schedule should have returned true. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3
      
      
      assertTrue("restaurant should have received the price. It didn't.", restaurant.log.containsString("Received msgHereIsPrice."));
      
      
      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      restaurantHandler.msgHereIsMoney(restaurant, (float)(15.99*5));
      
      //check postconditions for step 3 / preconditions for step 4
      
      assertEquals("MarketrestaurantHandler's cash should have increased to 5*15.99. It did not", restaurantHandler.money, (float)(5*15.99));
      
      assertTrue("MarketrestaurantHandler should have logged \"Received msgHereIsMoney, and restaurant Paid\" but didn't. His log reads instead: " 
            + restaurantHandler.log.getLastLoggedEvent().toString(), restaurantHandler.log.containsString("Received msgHereIsMoney, and restaurant Paid"));
      
      
      assertEquals("MarketrestaurantHandler order state should be paid", restaurantHandler.getOrders().get(0).s,
            OrderState.paid);
      
      
      assertEquals("restaurant should have 1 logged events. It doesn't.", restaurant.log.size(), 1);
      

      
      
      
      //step 4
      assertTrue("restaurantHandler's scheduler should have returned true. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("restaurant should have received. It didn't.", restaurant.log
            .containsString("Received msgHereIsFood."));
            
      assertTrue("MarketrestaurantHandler should have no more orders.", restaurantHandler.getOrders().isEmpty());
      
      assertEquals("MarketrestaurantHandler should have a total of 5*15.99", restaurantHandler.money, (float)(5*15.99));
      
      assertFalse("MarketrestaurantHandler scheduler should return false. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      
   
   }
   
   public void testRestaurantCantPayScenario()
   {
      //setUp() runs first before this test!
      restaurant.restaurantHandler=restaurantHandler;
      List<Grocery> order=new ArrayList<Grocery>();
      order.add(new Grocery("Steak",5));
      
      //check preconditions
      assertEquals("MarketrestaurantHandler should have 0 orders in it. It doesn't.",restaurantHandler.getOrders().size(), 0);      
      assertEquals("MarketrestaurantHandlerAgent should have an empty event log before the MarketrestaurantHandler's msgIWantFood is called. Instead, the MarketrestaurantHandler's event log reads: "
                  + restaurantHandler.log.toString(), 0, restaurantHandler.log.size());
      
      //step 1 of the test

      restaurantHandler.msgIWantFood(restaurant, order);

      //check postconditions for step 1 and preconditions for step 2
      assertEquals("restaurant should have an empty event log before the MarketrestaurantHandler's scheduler is called. Instead, the restaurant's event log reads: "
                  + restaurant.log.toString(), 0, restaurant.log.size());
      
      assertEquals("MarketrestaurantHandler should have 1 order in it. It doesn't.", restaurantHandler.getOrders().size(), 1);
      
      
      assertEquals(
            "restaurant should have an empty event log after the MarketrestaurantHandler's scheduler is called for the first time. Instead, the restaurant's event log reads: "
                  + restaurant.log.toString(), 0, restaurant.log.size());
      
            
//      assertEquals("MarketrestaurantHandler order should know who the restaurant is. It doesn't.", restaurantHandler.getOrders().get(0).c.name , "restaurant");

      
      assertEquals("MarketrestaurantHandler order should know what the grocery is. It doesn't.", restaurantHandler.getOrders().get(0).order.get(0).getFood(),
            "Steak");
      
      assertEquals("MarketrestaurantHandler order should know what the amount is. It doesn't.", restaurantHandler.getOrders().get(0).order.get(0).getAmount(),
            5);
      
     
      //step 2 of the test
//    restaurantHandler.ReadyToPay(restaurant, receipt);
      assertTrue("MarketrestaurantHandler should contain a order with state == pending. It doesn't.",
            restaurantHandler.getOrders().get(0).s == OrderState.pending);
      
      assertTrue("restaurantHandler's schedule should have returned true. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3
      
      
      assertTrue("restaurant should have received the price. It didn't.", restaurant.log.containsString("Received msgHereIsPrice."));
      
      
      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      restaurantHandler.msgHereIsMoney(restaurant, (float)(15.99));
      
      //check postconditions for step 3 / preconditions for step 4
      
      assertEquals("MarketrestaurantHandler's cash shouldn't have increased. It did not", restaurantHandler.money, (float)(0.00));
      
      assertTrue("MarketrestaurantHandler should have logged \"Received msgHereIsMoney, but restaurant couldn't Pay\" but didn't. His log reads instead: " 
            + restaurantHandler.log.getLastLoggedEvent().toString(), restaurantHandler.log.containsString("Received msgHereIsMoney, but restaurant couldn't Pay"));
      
      
      assertEquals("MarketrestaurantHandler order state should be unpaid", restaurantHandler.getOrders().get(0).s,
            OrderState.notEnoughPaid);
      
      
      assertEquals("restaurant should have 1 logged events. It doesn't.", restaurant.log.size(), 1);
      

      
      
      
      //step 4
      assertTrue("restaurantHandler's scheduler should have returned true. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("restaurant should have been kicked out. It didn't.", restaurant.log
            .containsString("Received msgGetOut."));
            
      assertTrue("MarketrestaurantHandler should have no more orders.", restaurantHandler.getOrders().isEmpty());
      
      assertEquals("MarketrestaurantHandler should have a total of $0", restaurantHandler.money, (float)(0.00));
      
      assertFalse("MarketrestaurantHandler scheduler should return false. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      
   
   }
   
   public void testOneNormalrestaurantScenarioWith2Customers()
   {
      //setUp() runs first before this test!
      restaurant2 = new MockRestaurant("restaurant");      
      restaurant2.restaurantHandler=restaurantHandler;
      List<Grocery> order=new ArrayList<Grocery>();
      order.add(new Grocery("Steak",5));
      List<Grocery> order2=new ArrayList<Grocery>();
      order2.add(new Grocery("Chicken",5));
      
      //check preconditions
      assertEquals("MarketrestaurantHandler should have 0 orders in it. It doesn't.",restaurantHandler.getOrders().size(), 0);      
      assertEquals("MarketrestaurantHandlerAgent should have an empty event log before the MarketrestaurantHandler's msgIWantFood is called. Instead, the MarketrestaurantHandler's event log reads: "
                  + restaurantHandler.log.toString(), 0, restaurantHandler.log.size());
      
      //step 1 of the test

      restaurantHandler.msgIWantFood(restaurant, order);
      restaurantHandler.msgIWantFood(restaurant2, order2);

      //check postconditions for step 1 and preconditions for step 2
      assertEquals("restaurant should have an empty event log before the MarketrestaurantHandler's scheduler is called. Instead, the restaurant's event log reads: "
                  + restaurant.log.toString(), 0, restaurant.log.size());
      
      assertEquals("MarketrestaurantHandler should have 2 orders in it. It doesn't.", restaurantHandler.getOrders().size(), 2);
      
      
      assertEquals(
            "restaurant should have an empty event log after the MarketrestaurantHandler's scheduler is called for the first time. Instead, the restaurant's event log reads: "
                  + restaurant.log.toString(), 0, restaurant.log.size());
      
            
     // assertEquals("MarketrestaurantHandler order should know who the restaurant is. It doesn't.", restaurantHandler.getOrders().get(0).c.getName() , "restaurant");

      
      assertEquals("MarketrestaurantHandler order should know what the grocery is. It doesn't.", restaurantHandler.getOrders().get(0).order.get(0).getFood(),
            "Steak");
      
      assertEquals("MarketrestaurantHandler order should know what the amount is. It doesn't.", restaurantHandler.getOrders().get(0).order.get(0).getAmount(),
            5);
      
      assertEquals("MarketrestaurantHandler order should know what the grocery2 is. It doesn't.", restaurantHandler.getOrders().get(1).order.get(0).getFood(),
            "Chicken");
      
      assertEquals("MarketrestaurantHandler order should know what the amount2 is. It doesn't.", restaurantHandler.getOrders().get(1).order.get(0).getAmount(),
            5);
      
     
      //step 2 of the test
//    restaurantHandler.ReadyToPay(restaurant, receipt);
      assertTrue("MarketrestaurantHandler should contain a order with state == pending. It doesn't.",
            restaurantHandler.getOrders().get(0).s == OrderState.pending);
      
      assertTrue("restaurantHandler's schedule should have returned true. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3
      
      
      assertTrue("restaurant should have received the price. It didn't.", restaurant.log.containsString("Received msgHereIsPrice."));
      
      assertTrue("MarketrestaurantHandler should contain another order with state == pending. It doesn't.",
            restaurantHandler.getOrders().get(1).s == OrderState.pending);
      
      assertTrue("restaurantHandler's schedule should have returned true. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3 for restaurant 2
      
      
      assertTrue("restaurant should have received the price. It didn't.", restaurant2.log.containsString("Received msgHereIsPrice."));
      
      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      restaurantHandler.msgHereIsMoney(restaurant, (float)(15.99*5));
      
      //check postconditions for step 3 / preconditions for step 4
      
      assertEquals("MarketrestaurantHandler's cash should have increased to 5*15.99. It did not", restaurantHandler.money, (float)(5*15.99));
      
      assertTrue("MarketrestaurantHandler should have logged \"Received msgHereIsMoney, and restaurant Paid\" but didn't. His log reads instead: " 
            + restaurantHandler.log.getLastLoggedEvent().toString(), restaurantHandler.log.containsString("Received msgHereIsMoney, and restaurant Paid"));
      
      
      assertEquals("MarketrestaurantHandler order state should be paid", restaurantHandler.getOrders().get(0).s,
            OrderState.paid);
      
      
      assertEquals("restaurant should have 1 logged events. It doesn't.", restaurant.log.size(), 1);
      
      restaurantHandler.msgHereIsMoney(restaurant2, (float)(5*10.99));
      
      //check postconditions for step 3 / preconditions for step 4 for restaurant 2
      
      assertEquals("MarketrestaurantHandler's cash should have increased to 5*15.99+5*10.99. It did not", restaurantHandler.money, ((float)(5*15.99)+(float)(5*10.99)));
      
      assertTrue("MarketrestaurantHandler should have logged \"Received msgHereIsMoney, and restaurant Paid\" but didn't. His log reads instead: " 
            + restaurantHandler.log.getLastLoggedEvent().toString(), restaurantHandler.log.containsString("Received msgHereIsMoney, and restaurant Paid"));
      
      
      assertEquals("MarketrestaurantHandler order state should be paid", restaurantHandler.getOrders().get(1).s,
            OrderState.paid);
      
      
      assertEquals("restaurant should have 1 logged events. It doesn't.", restaurant2.log.size(), 1);
      

      
      
      
      //step 4
      assertTrue("restaurantHandler's scheduler should have returned true. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("restaurant should have received. It didn't.", restaurant.log
            .containsString("Received msgHereIsFood."));
            
      assertTrue("restaurantHandler's scheduler should have returned true. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      
      //check postconditions for step 4 for restaurant 2
      assertTrue("restaurant2 should have received. It didn't.", restaurant2.log
            .containsString("Received msgHereIsFood."));
      
      assertTrue("MarketrestaurantHandler should have no more orders.", restaurantHandler.getOrders().isEmpty());
      
      assertEquals("MarketrestaurantHandler should have a total of 5*15.99+5*10.99", restaurantHandler.money, ((float)(5*15.99)+(float)(5*10.99)));
      
      assertFalse("MarketrestaurantHandler scheduler should return false. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      
   
   }
   
   public void testOneNormalrestaurantScenarioWith2CustomersButOneCantPay()
   {
      //setUp() runs first before this test!
      restaurant2 = new MockRestaurant("restaurant");      
      restaurant2.restaurantHandler=restaurantHandler;
      List<Grocery> order=new ArrayList<Grocery>();
      order.add(new Grocery("Steak",5));
      List<Grocery> order2=new ArrayList<Grocery>();
      order2.add(new Grocery("Chicken",5));
      
      //check preconditions
      assertEquals("MarketrestaurantHandler should have 0 orders in it. It doesn't.",restaurantHandler.getOrders().size(), 0);      
      assertEquals("MarketrestaurantHandlerAgent should have an empty event log before the MarketrestaurantHandler's msgIWantFood is called. Instead, the MarketrestaurantHandler's event log reads: "
                  + restaurantHandler.log.toString(), 0, restaurantHandler.log.size());
      
      //step 1 of the test

      restaurantHandler.msgIWantFood(restaurant, order);
      restaurantHandler.msgIWantFood(restaurant2, order2);

      //check postconditions for step 1 and preconditions for step 2
      assertEquals("restaurant should have an empty event log before the MarketrestaurantHandler's scheduler is called. Instead, the restaurant's event log reads: "
                  + restaurant.log.toString(), 0, restaurant.log.size());
      
      assertEquals("MarketrestaurantHandler should have 2 orders in it. It doesn't.", restaurantHandler.getOrders().size(), 2);
      
      
      assertEquals(
            "restaurant should have an empty event log after the MarketrestaurantHandler's scheduler is called for the first time. Instead, the restaurant's event log reads: "
                  + restaurant.log.toString(), 0, restaurant.log.size());
      
            
     // assertEquals("MarketrestaurantHandler order should know who the restaurant is. It doesn't.", restaurantHandler.getOrders().get(0).c.getName() , "restaurant");

      
      assertEquals("MarketrestaurantHandler order should know what the grocery is. It doesn't.", restaurantHandler.getOrders().get(0).order.get(0).getFood(),
            "Steak");
      
      assertEquals("MarketrestaurantHandler order should know what the amount is. It doesn't.", restaurantHandler.getOrders().get(0).order.get(0).getAmount(),
            5);
      
      assertEquals("MarketrestaurantHandler order should know what the grocery2 is. It doesn't.", restaurantHandler.getOrders().get(1).order.get(0).getFood(),
            "Chicken");
      
      assertEquals("MarketrestaurantHandler order should know what the amount2 is. It doesn't.", restaurantHandler.getOrders().get(1).order.get(0).getAmount(),
            5);
      
     
      //step 2 of the test
//    restaurantHandler.ReadyToPay(restaurant, receipt);
      assertTrue("MarketrestaurantHandler should contain a order with state == pending. It doesn't.",
            restaurantHandler.getOrders().get(0).s == OrderState.pending);
      
      assertTrue("restaurantHandler's schedule should have returned true. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3
      
      
      assertTrue("restaurant should have received the price. It didn't.", restaurant.log.containsString("Received msgHereIsPrice."));
      
      assertTrue("MarketrestaurantHandler should contain another order with state == pending. It doesn't.",
            restaurantHandler.getOrders().get(1).s == OrderState.pending);
      
      assertTrue("restaurantHandler's schedule should have returned true. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      //check postconditions for step 2 / preconditions for step 3 for restaurant 2
      
      
      assertTrue("restaurant should have received the price. It didn't.", restaurant2.log.containsString("Received msgHereIsPrice."));
      
      
      //step 3
      //NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
      
      
      restaurantHandler.msgHereIsMoney(restaurant, (float)(15.99*5));
      
      //check postconditions for step 3 / preconditions for step 4
      
      assertEquals("MarketrestaurantHandler's cash should have increased to 5*15.99. It did not", restaurantHandler.money, (float)(5*15.99));
      
      assertTrue("MarketrestaurantHandler should have logged \"Received msgHereIsMoney, and restaurant Paid\" but didn't. His log reads instead: " 
            + restaurantHandler.log.getLastLoggedEvent().toString(), restaurantHandler.log.containsString("Received msgHereIsMoney, and restaurant Paid"));
      
      
      assertEquals("MarketrestaurantHandler order state should be paid", restaurantHandler.getOrders().get(0).s,
            OrderState.paid);
      
      
      assertEquals("restaurant should have 1 logged events. It doesn't.", restaurant.log.size(), 1);
      
      restaurantHandler.msgHereIsMoney(restaurant2, (float)(10.99));
      
      //check postconditions for step 3 / preconditions for step 4 for restaurant 2
      
      assertEquals("MarketrestaurantHandler's cash should have not increased. It did not", restaurantHandler.money, ((float)(5*15.99)));
      
      assertTrue("MarketrestaurantHandler should have logged \"Received msgHereIsMoney, but restaurant couldn't Pay\" but didn't. His log reads instead: " 
            + restaurantHandler.log.getLastLoggedEvent().toString(), restaurantHandler.log.containsString("Received msgHereIsMoney, but restaurant couldn't Pay"));
      
      
      assertEquals("MarketrestaurantHandler order state should be unpaid", restaurantHandler.getOrders().get(1).s,
            OrderState.notEnoughPaid);
      
      
      assertEquals("restaurant should have 1 logged events. It doesn't.", restaurant2.log.size(), 1);
      

      
      
      
      //step 4
      assertTrue("restaurantHandler's scheduler should have returned true. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      
      //check postconditions for step 4
      assertTrue("restaurant should have received. It didn't.", restaurant.log
            .containsString("Received msgHereIsFood."));
            
      assertTrue("restaurantHandler's scheduler should have returned true. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      
      //check postconditions for step 4 for restaurant 2
      assertTrue("restaurant2 should have been kicked out. It didn't.", restaurant2.log
            .containsString("Received msgGetOut."));
      
      assertTrue("MarketrestaurantHandler should have no more orders.", restaurantHandler.getOrders().isEmpty());
      
      assertEquals("MarketrestaurantHandler should have a total of 5*15.99", restaurantHandler.money, ((float)(5*15.99)));
      
      assertFalse("MarketrestaurantHandler scheduler should return false. It didn't.", restaurantHandler.pickAndExecuteAnAction());
      
   
   }
}