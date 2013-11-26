package ericliu.restaurant;

import java.util.List;

import ericliu.interfaces.Customer;
import ericliu.interfaces.Waiter;
import ericliu.interfaces.Cashier;
import ericliu.interfaces.Market;
import agents.Grocery;

public class MarketBillClass
{
   private float orderPrice;
   //Market market;
   private List<Grocery> order;
   private CookAgent cook;
   
//  public MarketBillClass(double orderPrice, Market market){
//     this.orderPrice=orderPrice;
//     this.market=market;
//     
//  }
   public MarketBillClass(List<Grocery> order, float orderPrice, CookAgent cook){
      this.order=order;
      this.orderPrice=orderPrice;
      this.cook=cook;
      
   }
  
  public List<Grocery> getOrder(){
     return order;
  }
  public float getOrderPrice(){
     return orderPrice;
  }
  
  public CookAgent getCook(){
     return cook;
  }
//  public Market getMarket(){
//     return market;
//  }
}
