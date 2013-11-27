package agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import ericliu.restaurant.CashierAgent;
import ericliu.restaurant.CookAgent;
import ericliu.restaurant.FoodClass;
import ericliu.restaurant.HostAgent;
import agent.Agent;

public class WaiterBaseAgent extends Agent
{
private Person person;
   
   //Timer for PayCheck
   Timer payCheckTimer = new Timer();
   private double hoursWorked;
   
   //DATA
   private String name;
   private boolean working=false;
   
   @Override
   protected boolean pickAndExecuteAnAction()
   {
      // TODO Auto-generated method stub
      return false;
   }
}
