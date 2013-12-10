package ericliu.restaurant;


import agent.Agent;
import ericliu.restaurant.CustomerAgent.AgentEvent;
import ericliu.gui.HostGui;
import ericliu.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class OrderClass{
   Waiter waiter;
   FoodClass customerChoice;
   int tableNumber;
   OrderState state;
   
   private enum OrderState{none, soldOut, pending, cooking, done};
   private enum OrderEvent{none, done};
   OrderEvent event= OrderEvent.none;
   
   public OrderClass(Waiter waiter, FoodClass choice, int tableNumber, OrderState state){
      this.waiter=waiter;
      customerChoice=choice;
      this.tableNumber=tableNumber;
      this.state=state;
   }
   
   
}