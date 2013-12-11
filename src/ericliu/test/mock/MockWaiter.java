package ericliu.test.mock;

import java.util.Collection;
import java.util.List;

import ericliu.restaurant.CashierAgent;
import ericliu.restaurant.CookAgent;
import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.FoodClass;
import ericliu.restaurant.HostAgent;
import ericliu.restaurant.ReceiptClass;
import ericliu.gui.WaiterGui;
import ericliu.interfaces.Cashier;
import ericliu.interfaces.Customer;
import ericliu.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter
{
   public Cashier cashier;
   public Customer customer;
   
   public EventLog log=new EventLog();
   
   public MockWaiter(String name) {
      super(name);

   }
   
   public void msgHereIsTheReceipt(ReceiptClass receipt){
      log.add(new LoggedEvent("Received receipt."));
      customer.msgHereIsYourReceipt(receipt);
   }

   @Override
   public void msgGoToStart(int startNumber)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgSeatCustomer(CustomerAgent c, int tableNumber)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgYouCanGoOnBreak()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgReadyToOrder(CustomerAgent customerAgent)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void setCook(CookAgent cook)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void setHost(HostAgent host)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void setCashier(CashierAgent cashier)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public int getTableNumber()
   {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public String getMaitreDName()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Collection getTables()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void msgStartWorking()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgResumeWorking()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgWantToGoOnBreak()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgCustomerOrder(CustomerAgent cust, FoodClass Choice)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgOrderSoldOut(int tableNumber, List<FoodClass> soldOutFoods)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgHereIsReplacedOrder(CustomerAgent cust, FoodClass newFoodOrder)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgFoodIsReady(String choice, int tableNumber)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgDoneEating(CustomerAgent cust)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgDoGoToReadySpot()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgAtTable()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgAtStart()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgAtCashier()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgAtReady()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgAtOrder()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void msgAtPickUp()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void setGui(WaiterGui gui)
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public WaiterGui getGui()
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void startThread()
   {
      // TODO Auto-generated method stub
      
   }

   @Override
   public int getNumCustomers()
   {
      // TODO Auto-generated method stub
      return 0;
   }

   
}
