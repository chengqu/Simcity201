//package newMarket.test.mock;
//
//import java.util.List;
//
//import newMarket.MarketCashierAgent;
//import newMarket.interfaces.MarketCustomer;
//import agents.Grocery;
//
//public class MockMarketCustomer extends Mock implements MarketCustomer
//{
//   public MarketCashierAgent cashier;
//   public EventLog log = new EventLog();
//   public MockPerson p;
//
//   public MockMarketCustomer(MockPerson p, MarketCashierAgent cashier){
//      super();
//      this.p=p;
//      this.cashier=cashier;
//   }
//   public void msgHereIsPrice(List<Grocery> order, float price){
//      log.add(new LoggedEvent("Received msgHereIsPrice."));
//   }
//   public void msgHereIsFood(List<Grocery> order){
//      log.add(new LoggedEvent("Received msgHereIsFood."));
//   }
//   public void msgGetOut(){
//      log.add(new LoggedEvent("Received msgGetOut."));
//   }
//}
