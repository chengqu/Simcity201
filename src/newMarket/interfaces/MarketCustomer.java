package newMarket.interfaces;

import java.util.List;

import agents.Grocery;

public interface MarketCustomer  
{
   public void msgHereIsPrice(List<Grocery> order, float price);
   public void msgHereIsFood(List<Grocery> order);
   public void msgGetOut();
}
