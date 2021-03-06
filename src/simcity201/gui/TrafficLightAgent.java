package simcity201.gui;

import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import agent.Agent;
import agents.BusAgent.TranEvent;
import animation.SimcityPanel;

public class TrafficLightAgent extends Agent
{
//   private enum lightState {none, Red, Green};
   private enum lightState {none, changeLights};
//   private enum lightEvent {none, changeToRed, changeToGreen};
   
   private lightState state=lightState.changeLights;
//   private lightEvent event;
   
   //Timer to check when to change lights
   Timer lightTimer = new Timer();
   private boolean vertical = false;
   List<PassengerGui> subscribers=new ArrayList<PassengerGui>();
   List<CarGui> cars = new ArrayList<CarGui>();
   //AStar members
   AstarDriving aStarDriving = GlobalMap.getGlobalMap().getAstar();
   walkingAStar aStarWalking = GlobalMap.getGlobalMap().getWalkAStar();
   
   public void msgSubscribeMe(PassengerGui passenger){
      subscribers.add(passenger);
   }
   public void msgNotifyMe(CarGui car){
	   cars.add(car);
   }
   public TrafficLightAgent(){
      
   }
   protected boolean pickAndExecuteAnAction() {
      if(state==lightState.changeLights){
         changeLights();
         return true;
      }

      
      return false;
   }
   
   private void changeLights(){
      final TrafficLightAgent temp=this;
      state=lightState.none;     
      lightTimer.schedule(new TimerTask() {
         public void run() {
            print("DoneWaiting");
            temp.state = lightState.changeLights;
            temp.stateChanged();
            temp.alertSubscribers();
            temp.alertCars();
            aStarWalking.setTileAccordingToLight();
         }
      },5000
      );
   }
   
   private void alertSubscribers(){
      for(int i=0;i<subscribers.size();i++){
         subscribers.get(i).msgGreenLight();
      }
      subscribers.clear();
   }
   private void alertCars(){
	   if(!cars.isEmpty()){
	   //cars.get(0).msgGreenLight();
	   }
   }
}
