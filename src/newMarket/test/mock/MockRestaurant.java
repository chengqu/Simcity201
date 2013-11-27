package newMarket.test.mock;

import java.util.List;

import newMarket.test.mock.LoggedEvent;
import newMarket.MarketRestaurantHandlerAgent;
import newMarket.test.mock.EventLog;
import agents.Grocery;
import simcity201.interfaces.NewMarketInteraction;

public class MockRestaurant extends Mock implements NewMarketInteraction
{
   public MarketRestaurantHandlerAgent restaurantHandler;
   public EventLog log = new EventLog();
   
   public MockRestaurant(String name) {
      super(name);

   }
   public void msgHereIsPrice(List<Grocery> orders, float price){
      log.add(new LoggedEvent("Received msgHereIsPrice."));
   }
   
   public void msgHereIsFood(List<Grocery> orders){
      log.add(new LoggedEvent("Received msgHereIsFood."));
   }
   
   public void msgNoFoodForYou(){
      log.add(new LoggedEvent("Received msgGetOut."));
   }
   
//   public class Bill {
//      
//      public MarketRestaurantHandlerAgent handler;
//      public float moneyOwed;
//      
//      public Bill(MarketRestaurantHandlerAgent handler, float moneyOwed) {
//         this.handler = handler;
//         this.moneyOwed = moneyOwed;
//      }
//   }
   
}
