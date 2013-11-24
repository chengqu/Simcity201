package ericliu.test.mock;

import ericliu.restaurant.ReceiptClass;
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

}
