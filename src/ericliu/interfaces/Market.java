package ericliu.interfaces;

import ericliu.restaurant.MarketBillClass;

public interface Market
{
   public abstract void msgHereIsYourPayment(MarketBillClass bill);
   
   public abstract void msgNotEnoughMoneyToPay(MarketBillClass bill);
   
   public abstract String getName();
}
