package ericliu.restaurant;
import ericliu.interfaces.Customer;
import ericliu.interfaces.Waiter;
import ericliu.interfaces.Cashier;

public class BillClass
{
   Cashier cashier;
   Customer customer;
   int TableNumber;
   double mealPrice;
   
   public BillClass(Cashier cashier, Customer customer, int TableNumber, double mealPrice){
      this.cashier=cashier;
      this.customer=customer;
      this.TableNumber=TableNumber;
      this.mealPrice=mealPrice;
   }
}
