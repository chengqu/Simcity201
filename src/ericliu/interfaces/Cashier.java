package ericliu.interfaces;

import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.FoodClass;
import ericliu.restaurant.WaiterAgent;
import ericliu.test.mock.MockCustomer;
import ericliu.test.mock.MockWaiter;
import ericliu.test.mock.EventLog;


public interface Cashier
{
   public abstract void msgCalculateCheck(Waiter waiter, Customer customer, FoodClass customerOrder);

   public abstract void msgHereIsMyPayment(Customer customer, double price);
   
   public abstract void msgNotEnoughMoney(Customer cust);
   
   public abstract void msgThankYouForYourPayment();
  
   public abstract void msgJustPayNextTime();
   
}
