package ericliu.test;

import java.util.ArrayList;
import java.util.List;

import newMarket.MarketCashierAgent.OrderState;
import agents.Person;
import agents.ProducerConsumerMonitor;
import junit.framework.*;
import ericliu.restaurant.CashierAgent;
import ericliu.restaurant.CashierAgent.ReceiptState;
import ericliu.restaurant.CookAgent;
import ericliu.restaurant.CustomerAgent;
//import restaurant.WaiterAgent.Bill;
import ericliu.restaurant.ReceiptClass;
import ericliu.restaurant.WaiterAgent;
import ericliu.restaurant.WaiterProducer;
import ericliu.restaurant.WaiterAgent.CustomerState;
import ericliu.restaurant.WaiterAgent.MyCustomer;
import ericliu.gui.CookGui;
import ericliu.gui.WaiterGui;
import ericliu.interfaces.Cashier;
import ericliu.interfaces.Cook;
import ericliu.test.mock.MockCook;
import ericliu.test.mock.MockCustomer;
import ericliu.test.mock.MockMarket;
import ericliu.test.mock.MockWaiter;
import ericliu.restaurant.MarketBillClass;
import ericliu.restaurant.FoodClass;

public class WaiterAgentTest extends TestCase
{
   
   Person c;
   WaiterAgent waiter;
   CookAgent cook;
   ProducerConsumerMonitor<CookAgent.Order> monitor;
   FoodClass choice;
   List<FoodClass> soldOutFoods;
   CustomerAgent cust;
   MyCustomer customer;
   WaiterGui gui;
   CookGui cGui;
   
   public void setUp() throws Exception{
      super.setUp();    
      c=new Person("TestCustomer",true);
      soldOutFoods=new ArrayList<FoodClass>();
      monitor=new ProducerConsumerMonitor<CookAgent.Order>(30);
      cook=new CookAgent("Cook",monitor);
      cGui=new CookGui(cook);
      cook.setGui(cGui);
//      monitor.setSubscriber(cook);
      waiter= new WaiterAgent("Waiter",soldOutFoods);
      choice=new FoodClass("Steak", 0, 15.99);
      cust=new CustomerAgent(c);
      cust.tableNumber=1;
      customer=new MyCustomer(cust,1,CustomerState.none);
      gui=new WaiterGui(waiter);
      waiter.setGui(gui);
      waiter.setCook(cook);
   }
   
   public void testWaiterGiveSuccessfulOrder(){
      //Tell waiter to start working
      waiter.working=true;
     
      waiter.customers.add(customer);
      
      //Check pre-conditions
      assertEquals("Waiter should have an empty event log before the. Instead, the Waiter's event log reads: "
            + waiter.log.toString(), 0, waiter.log.size());
      
      //step 1 of the test
      waiter.msgCustomerOrder(cust, choice);
      
      
      
      assertTrue("Waiter should have logged \"Received msgCustomerOrder\" but didn't. His log reads instead: " 
            + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgCustomerOrder."));
      
      //step 2
      //Check if waiter has the correct customer and order
      
      assertEquals("Waiter should know who the customer is. It doesn't.", waiter.getCustomers().get(0).C.getName(),"TestCustomer");
      
      assertEquals("Waiter receipt should know what the order is. It doesn't.", waiter.getCustomers().get(0).customerChoice.getChoice(),"Steak");
           
      //Run the scheduler
      
      assertTrue("Waiter should contain a customerder with state == ordered. It doesn't.",
            waiter.getCustomers().get(0).state == CustomerState.ordered);      
      
      assertTrue("Waiter should be working.",
            waiter.working == true);
      
      //step 3
      waiter.msgAtOrder();
      cook.msgNotBusy();
      
      assertTrue("Waiter scheduler should have returned true. It didn't.", waiter.pickAndExecuteAnAction());
      
    
      assertTrue("Waiter should have logged \"Gave order to cook.\" but didn't. His log reads instead: " 
            + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Gave order to cook."));
      
      assertTrue("Waiter should contain a customer with state == ordered. It doesn't.",
            waiter.getCustomers().get(0).state == CustomerState.gaveOrderToCook); 
      
      //Release gui semaphores for cook so that it can run its functions
      cook.msgAtFridge();
      cook.msgAtCooking();
      cook.msgAtPlating();
      
      //Run cook's pickAndExecuteAnAction  to ensure he receives and uses the monitor list   
      cook.pickAndExecuteAnAction();
      
      assertTrue("Cook should have logged \"Received order.\" but didn't. His log reads instead: " 
            + cook.log.getLastLoggedEvent().toString(), cook.log.containsString("Received order."));
      
//      assertTrue("Cook should have removed an order into his monitor list " 
//            + cook.log.getLastLoggedEvent().toString(), cook.monitor.list.size()==0);

      //step 4
      //Send message back to waiter and check if waiter receives food
     waiter.msgFoodIsReady("Steak", 1);
     
      assertTrue("Waiter should have logged \"Received cooked food from cook.\" but didn't. His log reads instead: " 
            + waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received cooked food from cook."));
      
      //Waiter producer/consumer interaction complete;     
   }
}
