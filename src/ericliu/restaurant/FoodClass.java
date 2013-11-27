package ericliu.restaurant;

import agent.Agent;
import ericliu.restaurant.CustomerAgent.AgentEvent;
import ericliu.gui.HostGui;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class FoodClass
{
   String choice;
   int cookingTime;
   double cost;
   


   public FoodClass(String choice2, int cookingTime2, double cost2)
   {
      choice=choice2;
      cookingTime=cookingTime2;
      cost=cost2;
   }
   
   //GETTERs
   public String getChoice(){
      return choice;
   }
   
   public int getCookingTime(){
      return cookingTime;
   }
   
   public double getCost(){
      return cost;
   }
   
}

