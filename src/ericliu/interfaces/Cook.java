package ericliu.interfaces;

import java.util.concurrent.Semaphore;

import ericliu.restaurant.*;

public interface Cook
{
   public Semaphore busy=new Semaphore(1,true);
   
   public void msgHereIsTheOrder(Waiter waiter, FoodClass customerChoice, int tableNumber);
   public void msgNotBusy();
}
