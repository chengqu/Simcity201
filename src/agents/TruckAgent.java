package agents;

import agent.Agent;
import agents.PassengerAgent.AgentEvent;
import simcity201.gui.BusGui;
import simcity201.gui.TruckGui;

import java.util.*;
import java.util.concurrent.Semaphore;


public class TruckAgent extends Agent {
	List<MyOrders> MyO = new ArrayList<MyOrders>();
	List<MyRestaurants> MyR = new ArrayList<MyRestaurants>();
	public enum TranState{AtMarket,AtRest,AtCrossing};
	public enum TranEvent{Resting,GoToRest,GoToMarket};
	public enum OrderState{Pending,Delivering,Delivered};
	TranState state = TranState.AtMarket;
	TranEvent event = TranEvent.Resting;
	
	private boolean AtMarket = false;
	private boolean AtRest1 = false;
	private boolean AtRest2 = false;
	private boolean AtRest3 = false;
	private boolean AtRest4 = false;
	private boolean AtRest5 = false;
	private boolean AtRest6 = false;
	TruckGui truckGui = null;
	Timer timer = new Timer();
	private long waitingTime = 5000;
	private Semaphore atDest = new Semaphore(0,true);
	
	public TruckAgent(){
		
		}
	
	class MyOrders{
	       	OrderState os;
	        public String dest;
	        //StopAgent stop;
	        MyOrders(){
	        
	        	
	        }
	}
	
	class MyRestaurants{
			//StopAgent stop;
			public String Dest;
			public boolean isActive = false;
			MyRestaurants(String dest){
				this.Dest = dest;
			}
	}
	
	public void msgDeliverOrder(){
		MyO.add(new MyOrders());
		stateChanged();
	}
	
	public void msgOrderRecieved(){
		event = TranEvent.GoToMarket;
		stateChanged();
	}
	public void	msgAtDest(){
		atDest.release();
		stateChanged();
	}
	public void msgAtMarket(){
		AtMarket = true;
		stateChanged();
	}
	
	public void msgAtRest1(){
		AtRest1 = true;
		stateChanged();
	}
	public void msgAtRest2(){
		AtRest2 = true;
		stateChanged();
	}
	public void msgAtRest3(){
		AtRest3 = true;
		stateChanged();
	}
	public void msgAtRest4(){
		AtRest4 = true;
		stateChanged();
	}
	public void msgAtRest5(){
		AtRest5 = true;
		stateChanged();
	}
	public void msgAtRest6(){
		AtRest6 = true;
		stateChanged();
	}
	
	protected boolean pickAndExecuteAnAction() {
		
			for(MyOrders mo : MyO){
				if(mo.os == OrderState.Pending && AtMarket == true){
					AtMarket = false;
					goToCrossing(mo);
					return true;
				}
			}
			for(MyOrders mo : MyO){
				if(mo.os == OrderState.Delivering && state == TranState.AtCrossing){
				goToRest(mo);
				return true;
				}
			}
			
			for(MyOrders mo:MyO){
				
			}
			
			if(AtRest1 == true){
				AtRest1 = false;
				Stop("Rest1");
				return true;
			}
			if(AtRest2 == true){
				AtRest2 = false;
				Stop("Rest2");
				return true;
			}
			if(AtRest3 == true){
				AtRest2 = false;
				Stop("Rest3");
				return true;
			}
			if(AtRest4 == true){
				AtRest4 = false;
				Stop("Rest4");
				return true;
			}
			if(AtRest5 == true){
				AtRest5 = false;
				Stop("Rest5");
				return true;
			}
			if(AtRest6 == true){
				AtRest6 = false;
				Stop("Rest6");
				return true;
			}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	

	

	private void goToRest(MyOrders mo) {
		// TODO Auto-generated method stub
		Do("GoingToStop1");
		truckGui.DoGoTo(mo.dest);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = TranEvent.GoToMarket;
				stateChanged();
			}
		},2000
		);
		state = TranState.AtRest;
		
	}
	
	private void goToCrossing(MyOrders mo){
		Do("GoingToCrossing");
		if(mo.dest == "Rest1" || mo.dest == "Rest2" || mo.dest == "Rest3"){
			truckGui.DoGoTo("Crossing1");
		}
		if(mo.dest == "Rest4" || mo.dest == "Rest5" || mo.dest == "Rest6"){
			truckGui.DoGoTo("Crossing2");
		}
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = TranEvent.GoToRest;
				stateChanged();
			}
		},2000
		);
		state = TranState.AtCrossing;

	}
		
	public void Stop(String dest){
		for(MyOrders mo : MyO){
			if(mo.dest == dest){
				//mo.cook.msgHereIsDelivery();
			}
		}
	}
	
	public void setGui(TruckGui gui) {
		truckGui = gui;
	}

	public TruckGui getGui() {
		return truckGui;
	}

	
}

