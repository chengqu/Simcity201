package ericliu.test.mock;

import ericliu.restaurant.MarketBillClass;
import ericliu.restaurant.ReceiptClass;
import ericliu.interfaces.Cashier;
import ericliu.interfaces.Customer;
import ericliu.interfaces.Market;
import ericliu.interfaces.Waiter;

public class MockMarket extends Mock implements Market
{
   public Cashier cashier;

   
   public EventLog log=new EventLog();
   
   public MockMarket(String name) {
      super(name);

   }
   
   public void msgHereIsYourPayment(MarketBillClass bill){
      log.add(new LoggedEvent("Received cashier's payment."));
      cashier.msgThankYouForYourPayment();
   }

   public void msgNotEnoughMoneyToPay(MarketBillClass bill){
      log.add(new LoggedEvent("Cashier does not have enough money."));
      cashier.msgJustPayNextTime();
   }
}