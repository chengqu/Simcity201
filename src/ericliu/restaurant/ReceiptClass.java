package ericliu.restaurant;
import ericliu.test.mock.MockWaiter;
import ericliu.test.mock.MockCustomer;
import ericliu.interfaces.Waiter;
import ericliu.interfaces.Customer;

public class ReceiptClass
{
//   WaiterAgent waiter;
// CustomerAgent customer;
// FoodClass customerOrder;
// double mealPrice;
// 
// public ReceiptClass(WaiterAgent waiter, CustomerAgent customer, FoodClass customerOrder, double mealPrice){
//    this.waiter=waiter;
//    this.customer=customer;
//    this.customerOrder=customerOrder;
//    this.mealPrice=mealPrice;
// }
   
   private Waiter waiter;
   private Customer customer;
   private FoodClass customerOrder;
   private double mealPrice;
   private double changeDue;
   
 public ReceiptClass(Waiter waiter, Customer customer, FoodClass customerOrder, double mealPrice){
    this.waiter=waiter;
    this.customer=customer;
    this.customerOrder=customerOrder;
    this.mealPrice=mealPrice;
 }
 
 public double getMealPrice(){
    return mealPrice;
 }
 
 public Waiter getWaiter(){
    return waiter;
 }
 
 public Customer getCustomer(){
    return customer;
 }
 
 public FoodClass getCustomerOrder(){
    return customerOrder;
 }
 
 
 public double getChange(){
    return changeDue;
 }
}
