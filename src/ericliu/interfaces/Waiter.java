package ericliu.interfaces;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ericliu.gui.WaiterGui;
import ericliu.restaurant.CashierAgent;
import ericliu.restaurant.CookAgent;
import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.FoodClass;
import ericliu.restaurant.HostAgent;
import ericliu.restaurant.ReceiptClass;
import ericliu.restaurant.WaiterProducer.MyCustomer;

public interface Waiter
{
//  List<CustomerAgent> customers = new ArrayList<CustomerAgent>();
 //List<MyCustomer> customers= new ArrayList<MyCustomer>();
//   List<MyCustomer> customers=null;
public abstract void msgHereIsTheReceipt(ReceiptClass receipt);

  public abstract String getName();

public abstract void msgGoToStart(int startNumber);

public abstract void msgSeatCustomer(CustomerAgent c, int tableNumber);

public abstract void msgYouCanGoOnBreak();

public abstract void msgReadyToOrder(CustomerAgent customerAgent);

public abstract void setCook(CookAgent cook);

public abstract void setHost(HostAgent host);

public abstract void setCashier(CashierAgent cashier);

public abstract int getTableNumber();

public abstract String getMaitreDName();



public abstract Collection getTables();

public abstract void msgStartWorking();

public abstract void msgResumeWorking();


public abstract void msgWantToGoOnBreak();



public abstract void msgCustomerOrder(CustomerAgent cust, FoodClass Choice);

public abstract void msgOrderSoldOut(int tableNumber,
      List<FoodClass> soldOutFoods);

public abstract void msgHereIsReplacedOrder(CustomerAgent cust,
      FoodClass newFoodOrder);

//   public void msgGotOrder(){
//      try {
//         atOrder.acquire();
//      } catch (InterruptedException e) {
//         // TODO Auto-generated catch block
//         e.printStackTrace();
//      }
//      waiterGui.DoLeaveCustomer();
//   }
public abstract void msgFoodIsReady(String choice, int tableNumber);


public abstract void msgDoneEating(CustomerAgent cust);

public abstract void msgDoGoToReadySpot();


public abstract void msgAtTable();

public abstract void msgAtStart();

public abstract void msgAtCashier();

public abstract void msgAtReady();

public abstract void msgAtOrder();

public abstract void msgAtPickUp();

public abstract void setGui(WaiterGui gui);

public abstract WaiterGui getGui();

public abstract void startThread();

//public abstract List<MyCustomer> getCustomers();
public abstract int getNumCustomers();

}
