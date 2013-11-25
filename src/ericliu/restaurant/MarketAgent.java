//package ericliu.restaurant;
//
//import agent.Agent;
//import ericliu.restaurant.CustomerAgent.AgentEvent;
//import ericliu.gui.HostGui;
//import ericliu.interfaces.Market;
//
//import simcity201.interfaces.NewMarketInteraction;
//
//import java.util.*;
//import java.util.concurrent.Semaphore;
//import java.util.HashMap;
//import java.util.Timer;
//import java.util.TimerTask;
//
///**
// * Restaurant Host Agent
// */
////We only have 2 types of agents in this prototype. A customer and an agent that
////does all the rest. Rather than calling the other agent a waiter, we called him
////the HostAgent. A Host is the manager of a restaurant who sees that all
////is proceeded as he wishes.
//public class MarketAgent extends Agent implements Market{
//   static final int NTABLES = 3;//a global for the number of tables.
//   //Notice that we implement waitingCustomers using ArrayList, but type it
//   //with List semantics.
//   Timer timer = new Timer();
//   
//   private double cash=0.00;
//   
//   public List<CookOrder> cookOrders = Collections.synchronizedList(new ArrayList<CookOrder>());
//   
//   public List<MarketReceipt> marketReceipts=Collections.synchronizedList(new ArrayList<MarketReceipt>());
//   private CashierAgent cashier;
//   public ArrayList<Table> tables;
//   //note that tables is typed with Collection semantics.
//   //Later we will see how it is implemented
//   //public List<String> soldOutFoods=new ArrayList<String>();
//   public List<FoodClass> soldOutFoods=new ArrayList<FoodClass>();
////   public void setPaused(){
////      pauseToggle();
////      stateChanged();
////   }
//   
//   
//   
//   
//   //private Map<String,Integer> FoodCount= new HashMap<String, Integer>();
//   private Map<String,Integer> FoodCount= new HashMap<String, Integer>();
//   
//   private class CookOrder{
//      CookAgent cook;
//      List<FoodClass> cookOrder;
//      cookOrderState state;
//      Map<String,Integer> finishedCookOrder= new HashMap<String, Integer>();
//      MarketBillClass bill;
//      boolean fullOrder;
//      
//      public CookOrder(CookAgent cook, List<FoodClass> cookOrder, cookOrderState state, MarketBillClass bill, boolean fullOrder){
//         this.cook=cook;
//         this.cookOrder=cookOrder;
//         this.state=state;
//         this.bill=bill;
//         this.fullOrder=fullOrder;
//      }
//   }
//   
//   private class MarketReceipt{
//      MarketBillClass bill;
//      MarketReceiptState state;
//      CookAgent cook; 
//      
//      public MarketReceipt(MarketBillClass bill, MarketReceiptState state, CookAgent cook){
//         this.bill=bill;
//         this.state=state;
//         this.cook=cook;
//      }
//   }
//   
//   public void setCashier(CashierAgent cashier){
//      this.cashier=cashier;
//   }
//   private enum MarketReceiptState{none, paid, unpaid};
//   
//   private enum MarketState{none, checkingFood}
//   MarketState state= MarketState.none;
//   
//   private enum cookOrderState{none, soldOut, pending, pendingButNotCannotCompletelyFulfill, packing, done, waitingForPayment, paid, unpaid};
//   private enum cookOrderEvent{none, done};
//   cookOrderEvent event= cookOrderEvent.none;
//   
//   private boolean atStart;
//   private int currentTableNumber;
//   private String name;
//   private Semaphore atTable = new Semaphore(0,true);
//
//   //public HostGui hostGui = null;
//
//   public MarketAgent(String name, int steak, int chicken, int salad, int pizza) {
//      super();
//
//      FoodCount.put("Steak",steak);
//      FoodCount.put("Chicken",chicken);
//      FoodCount.put("Salad", salad);
//      FoodCount.put("Pizza",pizza);
//      
//      this.name = name;
//      // make some tables
//      tables = new ArrayList<Table>(NTABLES);
//      for (int ix = 1; ix <= NTABLES; ix++) {
//         tables.add(new Table(ix));//how you add to a collections
//      }
//      
//      
//   }
//
// 
//   
//   public int getTableNumber(){
//      return currentTableNumber;
//   }
//   
//   public String getMaitreDName() {
//      return name;
//   }
//
//   public String getName() {
//      return name;
//   }
//
//   public List getCookOrders() {
//      return cookOrders;
//   }
//
//   public Collection getTables() {
//      return tables;
//   }
//   
//   // Messages
//
////   public void msgPaused(){
////      pauseToggle();
////   }
////    
//   public void msgWhatFoodsAreSoldOut(WaiterAgent waiter){
//      state=MarketState.checkingFood;
//         
//   }
//   public void msgHereIsTheCookOrder(CookAgent cook, List<FoodClass> cookOrder){
//      
//     //if(ReturnFoodCount(choice)<=0 /*&& ReturnFoodCount(choice)!=null*/){
//         MarketBillClass bill= new MarketBillClass(0.00, this);
//         List<FoodClass> soldOutOrders=new ArrayList<FoodClass>();
//         Do("Received the Cook's Order");
//         for(FoodClass order: cookOrder){
//            if(FoodCount.get(order.choice)<=0){
//               soldOutOrders.add(order);
//            }
//         }
//         if(soldOutOrders.size()<cookOrder.size()){
//            if(soldOutOrders.size()==0)
//               cookOrders.add(new CookOrder(cook, cookOrder, cookOrderState.pending,bill, true));
//            else
//               cookOrders.add(new CookOrder(cook, cookOrder, cookOrderState.pending,bill, false));
//            Do(" Fulfilling this order ");
//       }
//         else{
//            Do(" Can't fulfill this order ");
//            cookOrders.add(new CookOrder(cook, cookOrder, cookOrderState.soldOut, bill , false));
//         }
//         stateChanged();
//      
//   }
//   
//
//   public void msgCookOrderIsDone(CookOrder order){
//      order.state=cookOrderState.done;
//      stateChanged();
//   }
//
//   public void msgHereIsYourPayment(MarketBillClass bill){
//      cash+=bill.orderPrice;
//      synchronized(cookOrders){
//         for(CookOrder order:cookOrders){
//            if(order.bill==bill){
//               order.state=cookOrderState.paid;
//               stateChanged();
//            }
//         }
//      }
//   }
//   
//   public void msgNotEnoughMoneyToPay(MarketBillClass bill){
//      synchronized(cookOrders){
//         for(CookOrder order:cookOrders){
//            if(order.bill==bill){
//               order.state=cookOrderState.unpaid;
//               stateChanged();
//            }
//         }
//      }
//   }
//   
//   /**
//    * Scheduler.  Determine what action is called for, and do it.
//    */
//   protected boolean pickAndExecuteAnAction() {
//      /* Think of this next rule as:
//            Does there exist a table and customer,
//            so that table is unoccupied and customer is waiting.
//            If so seat him at the table.
//       */
//      //System.out.println(orders.isEmpty());
//      //if(!orders.isEmpty()){
////      if(!paused){
//         synchronized(cookOrders){
//            for (CookOrder cookOrder : cookOrders) {
//              
//                  if(cookOrder.state==cookOrderState.soldOut){
//                     orderSoldOut(cookOrder);
//                     return true;
//                  }
//                  if (cookOrder.state==cookOrderState.pending){
//                     PackageIt(cookOrder);
//                     //System.out.println(FoodCount.get(order.choice));
//                     return true;
//                  }
//   //               if(cookOrder.state==cookOrderState.pendingButNotCannotCompletelyFulfill){
//   //                  PackagePartialOrder(cookOrder);
//   //               }
//                  if(cookOrder.state==cookOrderState.done && event==cookOrderEvent.done){
//                     SendIt(cookOrder);
//                     return true;
//                  }
//                  
//                  if(cookOrder.state==cookOrderState.paid){
//                     thankCashier(cookOrder);
//                     cookOrders.remove(cookOrder);
//                     return true;
//                  }
//                  
//                  if(cookOrder.state==cookOrderState.unpaid){
//                     tellCashierToJustPayNextTime(cookOrder);
//                     cookOrders.remove(cookOrder);
//                     return true;
//                  }
//               //}
//            }
//         }
//         
//
// //     }
//      return false;
//      //we have tried all our rules and found
//      //nothing to do. So return false to main loop of abstract agent
//      //and wait.
//   }
//
//   // Actions
//
//   private void orderSoldOut(CookOrder order){
////      if(!soldOutFoods.contains(order.customerChoice)){
////         soldOutFoods.add(order.customerChoice);
////      }
//      Do("\nCANNOT FULFILL COOKS ORDER BECAUSE ALL FOOD IS SOLD OUT.\n");
//      order.cook.msgOrderSoldOut(this, order.cookOrder);
//      cookOrders.remove(order);
//    }
//   
//   
//   private void PackageIt(CookOrder order){
//      synchronized(cookOrders){
//         for(FoodClass food:order.cookOrder){
//            if(FoodCount.get(food.choice)>0){
//               FoodCount.put(food.choice,FoodCount.get(food.choice)-1);
//               order.finishedCookOrder.put(food.choice, 1);
//               order.bill.orderPrice+=food.cost;
//            }
//            else
//               order.finishedCookOrder.put(food.choice, 0);
//         }
//      }
//         Do("Market is packaging cook's order.");
//         timer.schedule(new TimerTask() {
//            Object Order = 1;
//            public void run() {
//               print("Done packaging, cook order=" + Order);
//               event=cookOrderEvent.done;
//               
//               stateChanged();
//            }
//         },
//         3000);//getHungerLevel() * 1000);//how long to wait before running task
//         //msgFoodIsDone(order);
//         order.state=cookOrderState.done;
//
//   }
//
//   private void SendIt(CookOrder order){
//      order.cook.msgHereIsYourOrderFromTheMarket(this, order.finishedCookOrder, order.cookOrder, order.fullOrder);
//      cashier.msgHereIsTheMarketBill(order.bill);
//      Do("Sending Cook's Order to the cook.");
//      order.state=cookOrderState.waitingForPayment;
//   }
//
//   private void thankCashier(CookOrder order){
//      Do("Thanks for the payment cashier!");
//      cashier.msgThankYouForYourPayment();
//   }
//   
//   private void tellCashierToJustPayNextTime(CookOrder order){
//      Do("Just Pay Next Time Cashier!");
//      cashier.msgJustPayNextTime();
//   }
//
//
//   private class Table {
//      CustomerAgent occupiedBy;
//      int tableNumber;
//
//      
//      Table(int tableNumber) {
//         this.tableNumber = tableNumber;
//         
//      }
//
//      void setOccupant(CustomerAgent cust) {
//         occupiedBy = cust;
//      }
//
//      void setUnoccupied() {
//         occupiedBy = null;
//      }
//
//      CustomerAgent getOccupant() {
//         return occupiedBy;
//      }
//
//      boolean isOccupied() {
//         return occupiedBy != null;
//      }
//
//      public String toString() {
//         return "table " + tableNumber;
//      }
//   }
//}
//
