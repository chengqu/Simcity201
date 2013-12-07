package ericliu.restaurant;

import agent.Agent;
import agents.Grocery;
import ericliu.restaurant.CustomerAgent.AgentEvent;
import ericliu.gui.CashierGui;
import ericliu.interfaces.Customer;
import ericliu.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ericliu.interfaces.Cashier;
import ericliu.test.mock.EventLog;
import ericliu.test.mock.LoggedEvent;
/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CashierAgent extends Agent implements Cashier{
   static final int NTABLES = 3;//a global for the number of tables.
   //Notice that we implement waitingCustomers using ArrayList, but type it
   //with List semantics.
   Timer timer = new Timer();
   
   public EventLog log = new EventLog();
   private CookAgent cook;
   public double cash=300.00;
   
   FoodClass Steak= new FoodClass("Steak", 0, 15.99);
   FoodClass Chicken= new FoodClass("Chicken", 0, 10.99);
   FoodClass Salad=new FoodClass("Salad", 0, 5.99);
   FoodClass Pizza=new FoodClass("Pizza", 0, 8.99);
   
   public List<FoodClass> menu= Arrays.asList(Steak, Chicken, Salad, Pizza);
   
   private Map<String,Double> FoodPrice= new HashMap<String, Double>();

   
   public ArrayList<Table> tables;
   //note that tables is typed with Collection semantics.
   //Later we will see how it is implemented
   //public List<String> soldOutFoods=new ArrayList<String>();
   
//   public void setPaused(){
//      pauseToggle();
//      stateChanged();
//   }
   
//   private class Receipt{
//      WaiterAgent waiter;
//      CustomerAgent customer;
//      FoodClass customerOrder;
//      double mealPrice;
//      ReceiptState state;
//      
//      public Receipt(WaiterAgent waiter, CustomerAgent customer, FoodClass customerOrder, double mealPrice, ReceiptState state){
//         this.waiter=waiter;
//         this.customer=customer;
//         this.customerOrder=customerOrder;
//         this.mealPrice=mealPrice;
//         this.state=state;
//      }
//      
//   }
   public List<MarketBill> marketBills=Collections.synchronizedList(new ArrayList<MarketBill>());
   
   public class MarketBill{
      MarketBillClass bill;
      ReceiptState state;
      
      public MarketBill(MarketBillClass bill, ReceiptState state){
         this.bill=bill;
         this.state=state;
      }
      
      public MarketBillClass getBill(){
         return bill;
      }
      
      public ReceiptState getState(){
         return state;
      }
   }
   
   public double getCash(){
      return cash;
   }
   
   public void setCash(double cash){
      this.cash=cash;
   }
   
   public List<Receipt> receipts = Collections.synchronizedList(new ArrayList<Receipt>());
   
   public class Receipt{
      public ReceiptClass r;
      private ReceiptState state;
      
      public Receipt(ReceiptClass receipt, ReceiptState state){
         r=receipt;
         this.state=state;
      }
      
      public ReceiptState getState(){
         return state;
      }
   }
   
   public enum ReceiptState {none, calculated, unpaid, sentPayment, customerApproached, NotEnoughMoney, paid};

   
   private boolean atStart;
   private int currentTableNumber;
   private String name;
   private Semaphore atTable = new Semaphore(0,true);

   public CashierGui cashierGui = null;

   public CashierAgent(String name) {
      super();

      FoodPrice.put("Steak",15.99);
      FoodPrice.put("Chicken",10.99);
      FoodPrice.put("Salad", 5.99);
      FoodPrice.put("Pizza",8.99);
      FoodPrice.put("No More Food", 0.0);
      
      this.name = name;
      // make some tables
      tables = new ArrayList<Table>(NTABLES);
      for (int ix = 1; ix <= NTABLES; ix++) {
         tables.add(new Table(ix));//how you add to a collections
      }
      
      
   }

  
   
   public String getMaitreDName() {
      return name;
   }

   public String getName() {
      return name;
   }

  
   public void setCook(CookAgent cook){
      this.cook=cook;
   }

   
   // Messages

//   public void msgPaused(){
//      pauseToggle();
//   }
   
   public void msgCalculateCheck(Waiter waiter, Customer customer, FoodClass customerOrder){
      Do("Received Request to calculate check.");
      double price;
      //price=FoodPrice.get(customerOrder.getChoice());
      price=customerOrder.cost;
      ReceiptClass customerReceipt=new ReceiptClass(waiter, customer, customerOrder, price);
      receipts.add(new Receipt(customerReceipt,ReceiptState.calculated));
      stateChanged();
   }
 
//   public void msgHereIsTheMarketBill(MarketBillClass bill){
//      marketBills.add(new MarketBill(bill, ReceiptState.calculated));
//      stateChanged();
//   }
   public void msgHereIsPrice(MarketBillClass bill){
      marketBills.add(new MarketBill(bill, ReceiptState.calculated));
      stateChanged();
      Do("Received price from cook from market");
   }
   
   public void msgNotEnoughMoney(Customer cust){
      synchronized(receipts) {
         for(Receipt receipt: receipts){
            if(receipt.r.getCustomer()==cust){
               receipt.state=ReceiptState.NotEnoughMoney;
               stateChanged();
            }
         }
      }
      log.add(new LoggedEvent("Received msgNotEnoughMoney."));
      
   }
 

   public void msgHereIsMyPayment(Customer cust, double price){
      synchronized(receipts){
         for(Receipt receipt: receipts){
            if(receipt.r.getCustomer()==cust){
               //currentReceipt=receipt;
               receipt.state=ReceiptState.paid;
               stateChanged();
               Do("Changed state to Paid");
            }
         }
      cash+=price;
      log.add(new LoggedEvent("Received msgHereIsMyPayment."));
      }
   }
   
   
   
   public void msgThankYouForYourPayment(){
      log.add(new LoggedEvent("Received msgThankYouForYourPayment."));
      Do("\n\nSuccessfully paid Market for order!\n\n");
   }
   
   public void msgJustPayNextTime(){
      log.add(new LoggedEvent("Received msgJustPayNextTime."));
      Do("\n\nI Couldn't pay the Market, but they're letting me pay next time.\n\n");
   }
   /**
    * Scheduler.  Determine what action is called for, and do it.
    */
   public boolean pickAndExecuteAnAction() {
      /* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
       */
      
//      if(!paused){
         synchronized(receipts){
            for (Receipt receipt : receipts) {
               
                  if (receipt.state==ReceiptState.calculated){
                     GiveReceiptToWaiter(receipt);
                     //System.out.println(FoodCount.get(order.choice));
                     return true;
                  }
                  if (receipt.state==ReceiptState.NotEnoughMoney){
                     TellCustomerToPayNextTime(receipt);
                     receipts.remove(receipt);
                     return true;
                  }
                  if (receipt.state==ReceiptState.paid){
                     ThankTheCustomer(receipt);
                     receipts.remove(receipt);
                     return true;
                  }
            
            }
         }
         synchronized(marketBills){
            for(MarketBill marketBill:marketBills){
               if(marketBill.state==ReceiptState.calculated){
                  //GiveReceiptToCook(marketBill);
                  PayTheMarket(marketBill);
                  return true;
               }
   //            if(marketBill.state==ReceiptState.paid){
   //               ThankTheCook(marketBill);
   //               marketBills.remove(marketBill);
   //               return true;
   //            }
   //            if(marketBill.state==ReceiptState.NotEnoughMoney){
   //               TellCookToPayNextTime(marketBill);
   //               marketBills.remove(marketBill);
   //               return true;
   //            }
            }
         }
//      }
      return false;
      //we have tried all our rules and found
      //nothing to do. So return false to main loop of abstract agent
      //and wait.
   }

   // Actions

   public void GiveReceiptToWaiter(Receipt receipt){
      Do("Here is the meal cost for the customer");
//      receipt.waiter.msgHereIsTheMealCost(receipt.customer,receipt.mealPrice);
      receipt.r.getWaiter().msgHereIsTheReceipt(receipt.r);
      receipt.state=ReceiptState.unpaid;
      stateChanged();
   } 

   public void TellCustomerToPayNextTime(Receipt receipt){
      Do("Just pay next time!");
      receipt.r.getCustomer().msgJustPayNextTime();
   }

   public void ThankTheCustomer(Receipt receipt){
      Do("Thanks for paying your bill!");
      receipt.r.getCustomer().msgThankYouForYourPayment();
   }
   
   public void PayTheMarket(MarketBill bill){
      //Do("\n\n\nTRYING TO PAY MARKET\n\n\n");
//      if(cash>=bill.bill.getOrderPrice()){
//         cash-=bill.bill.getOrderPrice();
//         bill.bill.market.msgHereIsYourPayment(bill.bill);
//      }
//      else{
//         bill.bill.market.msgNotEnoughMoneyToPay(bill.bill);
//      }
      Do("Sent money to cook to give back to market");
      if(bill.bill.getOrderPrice()<cash){
         bill.bill.getCook().msgHereIsMoney(bill.bill.getOrderPrice());
         cash-=bill.bill.getOrderPrice();
      }
      else{
         bill.bill.getCook().msgHereIsMoney(0);
      }
      marketBills.remove(bill);
      
   }

   //utilities

   public void setGui(CashierGui gui) {
      cashierGui = gui;
   }

   public CashierGui getGui() {
      return cashierGui;
   }

   private class Table {
      CustomerAgent occupiedBy;
      int tableNumber;

      
      Table(int tableNumber) {
         this.tableNumber = tableNumber;
         
      }

      void setOccupant(CustomerAgent cust) {
         occupiedBy = cust;
      }

      void setUnoccupied() {
         occupiedBy = null;
      }

      CustomerAgent getOccupant() {
         return occupiedBy;
      }

      boolean isOccupied() {
         return occupiedBy != null;
      }

      public String toString() {
         return "table " + tableNumber;
      }
   }

   
}

