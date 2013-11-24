package ericliu.restaurant;

import ericliu.interfaces.Customer;
import ericliu.interfaces.Waiter;
import ericliu.interfaces.Cashier;
import ericliu.interfaces.Market;

public class MarketBillClass
{
   double orderPrice;
   Market market;
   
   
  public MarketBillClass(double orderPrice, Market market){
     this.orderPrice=orderPrice;
     this.market=market;
     
  }
  
  public double getOrderPrice(){
     return orderPrice;
  }
  
  public Market getMarket(){
     return market;
  }
}
