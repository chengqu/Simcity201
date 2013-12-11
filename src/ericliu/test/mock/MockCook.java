package ericliu.test.mock;

import agents.MonitorSubscriber;
import agents.ProducerConsumerMonitor;
import ericliu.interfaces.Cook;
import ericliu.interfaces.Waiter;
import ericliu.restaurant.FoodClass;
import ericliu.restaurant.CookAgent.Order;

public class MockCook extends Mock implements Cook, MonitorSubscriber
{
   private ProducerConsumerMonitor<Order> monitor;
   public EventLog log=new EventLog();
   
   public MockCook(String name) {
      super(name);

   }
   
   public void msgHereIsTheOrder(Waiter waiter, FoodClass customerChoice, int tableNumber){
      log.add(new LoggedEvent("Received Order."));   }
   
   public void msgNotBusy(){
      busy.release();
   }

   @Override
   public void canConsume()
   {
      // TODO Auto-generated method stub
      
   }
}
