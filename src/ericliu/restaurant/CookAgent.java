package ericliu.restaurant;

import agent.Agent;
import agents.Grocery;
import ericliu.restaurant.CustomerAgent.AgentEvent;
import ericliu.gui.HostGui;
import ericliu.gui.CookGui;
import ericliu.gui.WaiterGui;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import newMarket.MarketRestaurantHandlerAgent;
import newMarket.NewMarket;
import simcity201.gui.GlobalMap;
import simcity201.interfaces.NewMarketInteraction;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CookAgent extends Agent implements NewMarketInteraction{
   static final int NTABLES = 3;//a global for the number of tables.
   //Notice that we implement waitingCustomers using ArrayList, but type it
   //with List semantics.
   Timer timer = new Timer();
   public Map<String,Integer> FoodCount= new HashMap<String, Integer>();
   
   private CookGui cookGui=null;
   
   private CashierAgent cashier;
   
   FoodClass Steak= new FoodClass("Steak", 0, 15.99);
   FoodClass Chicken= new FoodClass("Chicken", 0, 10.99);
   FoodClass Salad=new FoodClass("Salad", 0, 5.99);
   FoodClass Pizza=new FoodClass("Pizza", 0, 8.99);
   
   //public List<String> menu= Arrays.asList("Steak","Chicken","Salad","Pizza");
   public List<FoodClass> menu= Arrays.asList(Steak, Chicken, Salad, Pizza);
   
   public List<Order> orders = Collections.synchronizedList (new ArrayList<Order>());
   public ArrayList<Table> tables;
   
   // NEWMARKET AGENT
   public MarketRestaurantHandlerAgent market= GlobalMap.getGlobalMap().marketHandler;
   
   //MARKET LISTS FOR MULTIPLE MARKETS
//   public ArrayList<MarketAgent> markets=new ArrayList<MarketAgent>();
//   public ArrayList<MarketAgent> soldOutMarkets=new ArrayList<MarketAgent>();
//   public ArrayList<MarketAgent> freeMarkets=new ArrayList<MarketAgent>();
   
   
   
   //note that tables is typed with Collection semantics.
   //Later we will see how it is implemented
   //public List<String> soldOutFoods=new ArrayList<String>();
   public List<FoodClass> soldOutFoods=new ArrayList<FoodClass>();
   private List<String> STRsoldOutFoods=new ArrayList<String>();
   public List<FoodClass> lowStockFoods=new ArrayList<FoodClass>();
   
   public List<MarketOrder> marketOrders=Collections.synchronizedList(new ArrayList<MarketOrder>());
      
   private class MyMarketBill{
      MarketBillClass bill;
      MarketBillState state;
      
      public MyMarketBill(MarketBillClass bill, MarketBillState state){
         this.bill=bill;
         this.state=state;
      }
   }
   
   private enum MarketBillState{none, receivedBill};
   //SEMAPHORES FOR ANIMATIONS
   private Semaphore atStart=new Semaphore(1,true);
   private Semaphore atPlating=new Semaphore(0,true);
   private Semaphore atCooking=new Semaphore(0,true);
   private Semaphore atFridge=new Semaphore(0,true);
   public Semaphore busy=new Semaphore(1,true);
   
   private double cash=20.00;
//   public void setPaused(){
//      pauseToggle();
//      stateChanged();
//   }
   
   
   private class MyWaiter{
      WaiterAgent waiter;
      public List<Order> orders;
      
   }
   
   private class Order{
      WaiterAgent waiter;
      FoodClass customerChoice;
      int tableNumber;
      OrderState state;
      
      public Order(WaiterAgent waiter, FoodClass choice, int tableNumber, OrderState state){
         this.waiter=waiter;
         customerChoice=choice;
         this.tableNumber=tableNumber;
         this.state=state;
      }
   }
   
   private class MarketOrder{
      //MarketAgent market;
      Map<String, Integer> finishedCookOrder;
      List<FoodClass> cookOrder;
      //MarketBillClass bill;
      MarketOrderState state;
      
      public MarketOrder(/*MarketAgent market,*/ Map<String, Integer> finishedCookOrder, List<FoodClass> cookOrder, MarketOrderState state){
         //this.market=market;
         this.finishedCookOrder=finishedCookOrder;
         this.cookOrder=cookOrder;
         //this.bill=bill;
         this.state=state;
      }
   }
   private enum CookState{none, checkingFood}
   CookState state= CookState.none;
   
   private enum OrderState{none, soldOut, pending, cooking,receivedFoodFromMarketAndBill, done};
   public enum OrderEvent{none, marketSoldOut, done};
   OrderEvent event= OrderEvent.none;
   
   private enum MarketOrderState{none, receivedFoodFromMarketAndBill};
  
   //private boolean atStart;
   private int currentTableNumber;
   private String name;
   private Semaphore atTable = new Semaphore(0,true);

   
   public CookAgent(String name) {
      super();

      //market=global
      
      FoodCount.put("Steak",1);
      FoodCount.put("Chicken",0);
      FoodCount.put("Salad", 1);
      FoodCount.put("Pizza",1);
      FoodCount.put("No More Food", 100);
      
      this.name = name;
      // make some tables
      tables = new ArrayList<Table>(NTABLES);
      for (int ix = 1; ix <= NTABLES; ix++) {
         tables.add(new Table(ix));//how you add to a collections
      }
      
      for(FoodClass food: menu){
         if(FoodCount.get(food.choice)<=2){
            Do("ADDING " +food.choice+" INTO LOW STOCK FOODS LIST");
            lowStockFoods.add(food);
            //STRsoldOutFoods.add(food.choice);
         }
         if(FoodCount.get(food.choice)<=0){
            Do("ADDING " +food.choice+" INTO SOLD OUT FOODS LIST");
            soldOutFoods.add(food);
            STRsoldOutFoods.add(food.choice);
         }
      }
      
   }

   public void setCashier(CashierAgent cashier){
      this.cashier=cashier;
   }
  
  // public void addMarket(MarketAgent market){
//      markets.add(market);
//      freeMarkets.add(market);
  // }
   
   public int getTableNumber(){
      return currentTableNumber;
   }
   
   public String getMaitreDName() {
      return name;
   }

   public String getName() {
      return name;
   }

   public List getOrders() {
      return orders;
   }

   public Collection getTables() {
      return tables;
   }
   
   
   // Messages

//   public void msgPaused(){
//      pauseToggle();
//   }
   
   public void msgWhatFoodsAreSoldOut(WaiterAgent waiter){
      state=CookState.checkingFood;
         
   }
  
//   public void msgOrderSoldOut(MarketAgent market, List<FoodClass> soldOutFoods){
//      //soldOutMarkets.add(market);
//      //freeMarkets.remove(market);
//      event=OrderEvent.marketSoldOut;
//      stateChanged();
//   } 
   
   public void msgHereIsTheOrder(WaiterAgent waiter, FoodClass customerChoice, int tableNumber){
      //System.out.println("Adding order to the orders list.");
      //System.out.println(ReturnFoodCount(choice));
     //if(ReturnFoodCount(choice)<=0 /*&& ReturnFoodCount(choice)!=null*/){
      try {
         busy.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      if(FoodCount.get(customerChoice.choice)<=0 && FoodCount.get(customerChoice.choice)!=100){
         System.out.println(customerChoice.choice+" is Sold Out");
         orders.add(new Order(waiter, customerChoice, tableNumber, OrderState.soldOut));
         stateChanged();
      }
      else{
         orders.add(new Order(waiter, customerChoice, tableNumber, OrderState.pending));
         System.out.println("Added order to the orders list.");
         stateChanged();
      }
   }
   
//   public void msgHereIsYourOrderFromTheMarket(MarketAgent market, Map <String, Integer> finishedCookOrder, List<FoodClass> cookOrder, boolean fullOrder){
//      for(FoodClass food:cookOrder){
//         int amountFromWaiter=finishedCookOrder.get(food.choice);
//         FoodCount.put(food.choice, FoodCount.get(food.choice)+amountFromWaiter);
//         if(FoodCount.get(food.choice)>0 && !soldOutFoods.isEmpty()){
//            soldOutFoods.remove(food.choice);
//         }
//         if(FoodCount.get(food.choice)>2){
//            lowStockFoods.remove(food.choice);
//         }
//      }
//      if(fullOrder==false){
//         //soldOutMarkets.add(market);
//         event=OrderEvent.marketSoldOut;
//         stateChanged();
//      }
//      Do("Received the order from the Market and RESTOCKED my inventory.");
//      //marketOrders.add(new MarketOrder(market, finishedCookOrder, cookOrder, MarketOrderState.receivedFoodFromMarketAndBill));    
//      event=OrderEvent.none;
//      stateChanged();
//   }
   
 
   
   

   
   public void msgFoodIsDone(Order order){
      order.state=OrderState.done;
      stateChanged();
   }

   
   
   public void msgAtStart(){
      atStart.release();
   }
   public void msgAtFridge(){
      atFridge.release();
   }
   public void msgAtPlating(){
      atPlating.release();
   }
   public void msgAtCooking(){
      atCooking.release();
   }
   
   /**
    * Scheduler.  Determine what action is called for, and do it.
    */
   protected boolean pickAndExecuteAnAction() {
      /* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
       */
      //System.out.println(orders.isEmpty());
      //if(!orders.isEmpty()){
 //     if(!paused){
         if(event==OrderEvent.marketSoldOut){
            askMarketForFood();
         }
         synchronized(orders){
            for (Order order : orders) {
               
                  if(order.state==OrderState.soldOut){
                     orderSoldOut(order);
                     return true;
                  }
                  if (order.state==OrderState.pending){
                    // Do("\n\n CALLING COOK IT FUNCTION \n\n");
                     CookIt(order);
                     //System.out.println(FoodCount.get(order.choice));
                     return true;
                  }
                  if(order.state==OrderState.done && event==OrderEvent.done){
                     System.out.println("Cook is plating the order.");
                     PlateIt(order);
                     orders.remove(order);
                     return true;
                  }

            }
 //        }

      }
      
      //Do("Cook atFridge Permits: "+atFridge.availablePermits());
      return false;
      //we have tried all our rules and found
      //nothing to do. So return false to main loop of abstract agent
      //and wait.
   }

   // Actions

   private void orderSoldOut(Order order){
//      if(!soldOutFoods.contains(order.customerChoice)){
//         soldOutFoods.add(order.customerChoice);
//      }
      if(FoodCount.get(order.customerChoice.choice)<=0){
         if(!STRsoldOutFoods.contains(order.customerChoice.choice)){
            Do("ADDING " +order.customerChoice.choice+" INTO SOLD OUT FOODS LIST");
            soldOutFoods.add(order.customerChoice);
            STRsoldOutFoods.add(order.customerChoice.choice);
         }
      }

      order.waiter.msgOrderSoldOut(order.tableNumber, soldOutFoods);
      orders.remove(order);
      busy.release();
   }

   private void askMarketForFood(){
      Do("Giving The Market My Order!");
//      if(!freeMarkets.isEmpty()){
//         freeMarkets.get(0).msgHereIsTheCookOrder(this, lowStockFoods);
//         freeMarkets.remove(freeMarkets.get(0));
//      }
//      else{
//         Do("No More Markets have food in stock.");
//         event=OrderEvent.none;
//      }
      List<Grocery> order=new ArrayList<Grocery>();
      Do("Low Stock Food List: ");
      for(FoodClass food: lowStockFoods){
         order.add(new Grocery(food.choice, 5));
         Do(food.choice);
      }
      Do("Grocery List: ");
      for(Grocery food:order){
         Do(food.getFood()+": "+food.getAmount());
      }
      market.msgIWantFood(this, order);
      event=OrderEvent.none;
   }
   private void CookIt(Order order){

         cookGui.goToFridge();
         try {
            atFridge.acquire();
         } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         cookGui.drawCustomerOrder(order.customerChoice.choice);
         cookGui.goToCookArea();
         if(FoodCount.get(order.customerChoice.choice)>0){
            Do("Decrementing food count");
            FoodCount.put(order.customerChoice.choice,FoodCount.get(order.customerChoice.choice)-1);
         }
         if(FoodCount.get(order.customerChoice.choice)<=2){
//            if(!lowStockFoods.contains(order.customerChoice)){
//               lowStockFoods.add(order.customerChoice);
//            }
            boolean present=false;
            for(FoodClass food:lowStockFoods){
               if(food.choice.equals(order.customerChoice.choice)){
                  present=true;
                  break;
               }
            }
            if(!present){
               lowStockFoods.add(order.customerChoice);
            }
         }
//         if(FoodCount.get(order.customerChoice.choice)<=0){
//            if(!soldOutFoods.contains(order.customerChoice)){
//               soldOutFoods.add(order.customerChoice);
//            }
//         }
         System.out.println("Cook is cooking "+order.customerChoice.choice);
         try {
            atCooking.acquire();
         } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         timer.schedule(new TimerTask() {
            Object Order = 1;
            public void run() {
               print("Done cooking, order=" + Order);
               event=OrderEvent.done;
//               try {
//                  atCooking.acquire();
//               } catch (InterruptedException e) {
//                  // TODO Auto-generated catch block
//                  e.printStackTrace();
//               }
               cookGui.goToPlateArea();
               cookGui.undrawCustomerOrder();
               stateChanged();
               
            }
         },
         3000);//getHungerLevel() * 1000);//how long to wait before running task
         //msgFoodIsDone(order);
         
         
         order.state=OrderState.done;
         
         //stateChanged();
         
         //}
         /*else 
            soldOutFoods.add(order.choice);
            order.state=OrderState.none;*/
         //}
         
         
     // }
   }

   private void PlateIt(Order order){
      Do("Plating Order: "+order.customerChoice.choice);
//      if(soldOutFoods.size()==4){
//         askMarketForFood(order);
//      }
      if(!lowStockFoods.isEmpty()){
         askMarketForFood();
      }
      try {
         atPlating.acquire();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      //cookGui.drawOrder(order.customerChoice.choice);
      order.waiter.msgFoodIsReady(order.customerChoice.choice, order.tableNumber);
      cookGui.goToStart();
     // order.state=OrderState.none;

      //System.out.println(order.choice+" Count: "+ ReturnFoodCount(order.choice));
      //orders.remove(order);
      //order.state=OrderState.none;
      busy.release();
   }

  // private void PayMarket(MyMarketBill marketBill){
//      Do("Paying "+marketBill.bill.market);
//      if(cash>=marketBill.bill.orderPrice){
//         cash-=marketBill.bill.orderPrice;
//         //marketBill.market.msgHereIsTheCookOrderPayment(marketOrder.bill, this);
//         cashier.msgHereIsTheMarketBillPayment(marketBill.bill);
//      }
//      else{
//        // marketOrder.market.msgIDoNotHaveEnoughMoney(marketOrder.bill, this);
//         cashier.msgIDoNotHaveEnoughMoney(marketBill.bill);
//      }
//      marketBills.remove(marketBill);
      //market.msgHereIsMoney(c, money);
   //}

   //utilities

   public void setGui(CookGui gui) {
      cookGui = gui;
   }

   public CookGui getGui() {
      return cookGui;
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

   /**
    * Added messages for newMarket interaction
    * 
    */
   public void msgHereIsMoney(float money){
      Do("Received payment from cashier to give to market");
      market.msgHereIsMoney(this, money);
      
   }
   
   @Override
   public void msgHereIsPrice(List<Grocery> orders, float price)
   {
      // TODO Auto-generated method stub
      MarketBillClass bill=new MarketBillClass(orders,price, this);
      Do("Received price from market to give to cashier");
      cashier.msgHereIsPrice(bill);
      
   }

   public void msgHereIsFood(List<Grocery> orders){
      for(Grocery food:orders){
         int amountFromMarket=food.getAmount();
         FoodCount.put(food.getFood(), FoodCount.get(food.getFood())+amountFromMarket);
         if(FoodCount.get(food.getFood())>0 && !soldOutFoods.isEmpty()){
            soldOutFoods.remove(food.getFood());
         }
         if(FoodCount.get(food.getFood())>2){
            lowStockFoods.remove(food.getFood());
         }
      }
//      if(fullOrder==false){
//         //soldOutMarkets.add(market);
//         event=OrderEvent.marketSoldOut;
//         stateChanged();
//      }
      Do("Received the order from the Market and RESTOCKED my inventory.");
      //marketOrders.add(new MarketOrder(market, finishedCookOrder, cookOrder, MarketOrderState.receivedFoodFromMarketAndBill));    
      for(Grocery food:orders){
         Do(food.getFood()+": "+FoodCount.get(food.getFood()));
      }
      event=OrderEvent.none;
      stateChanged();
   }

   @Override
   public void msgNoFoodForYou()
   {
      Do("Did not have enough money for food so did not receive my order!");
      event=OrderEvent.none;
      stateChanged();
      
   }

@Override
public void msgOutOfStock() {
	// TODO Auto-generated method stub
	
}
}
