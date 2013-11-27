package ericliu.interfaces;
import ericliu.restaurant.ReceiptClass;

public interface Waiter
{
  public abstract void msgHereIsTheReceipt(ReceiptClass receipt);

  public abstract String getName();
}
