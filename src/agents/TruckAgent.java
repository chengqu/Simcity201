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
	public enum TranState{AtMarket,AtRest1,AtRest2,AtCrossing1, AtCrossing2, AtCrossing3, AtCrossing4};
	public enum TranEvent{Resting,GoToRest1,GoToMarket, GoToCrossing2, GoToRest2, GoToCrossing3, GoToCrossing4, GoToCrossing1};
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
	private long waitingTime = 2000;
	private long loadingTime = 8000;
	private Semaphore atDest = new Semaphore(0,true);
	private Semaphore atCrossing = new Semaphore(0,true);
	public TruckAgent(){
		
		}
	
	class MyOrders{
	       	OrderState os;
	        public String dest;
	        //StopAgent stop;
	        MyOrders(String dest,OrderState os){
	        	this.dest = dest;
	        	this.os = os;
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
	
	public void msgDeliverOrder(String dest){
		MyO.add(new MyOrders(dest,OrderState.Pending));
		event = TranEvent.GoToCrossing1;
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
	public void msgAtCrossing(){
		atCrossing.release();
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
					goToCrossing1(mo);
					return true;
				}
			}
			for(MyOrders mo : MyO){
				if(mo.os == OrderState.Delivering && state == TranState.AtCrossing1 && event == TranEvent.GoToRest1){
				goToRest1(mo);
				return true;
				}
			
			}
			
			if(event == TranEvent.GoToCrossing2 ){
				goToCrossing2();
				return true;
			}
			
			if(state == TranState.AtCrossing2 && event == TranEvent.GoToCrossing3){
				goToCrossing3();
				return true;
			}
			
			for(MyOrders mo : MyO){
				if(mo.os == OrderState.Delivering && state == TranState.AtCrossing3 && event == TranEvent.GoToRest2){
				goToRest2(mo);
				return true;
				}
			}
			
			if(event == TranEvent.GoToCrossing4){
				goToCrossing4();
				return true;
			}
			
			if(state == TranState.AtCrossing4 && event == TranEvent.GoToMarket){
				goToMarket();
				return true;
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

	

	

	private void goToRest1(MyOrders mo) {
		// TODO Auto-generated method stub
		//Do("GoingToRest1");
		if(mo.dest == "Rest2"|| mo.dest == "Rest4"|| mo.dest == "Rest6"){
			truckGui.DoGoToRest(mo.dest);
			timer.schedule(new TimerTask() {
				public void run() {
					//print("DoneWaiting");
					event = TranEvent.GoToCrossing2;
					stateChanged();
				}
			},loadingTime
			);
		}
		else event = TranEvent.GoToCrossing2;
		state = TranState.AtRest1;
	}
	private void goToRest2(MyOrders mo) {
		// TODO Auto-generated method stub
		//Do("GoingToRest2");
		if(mo.dest == "Rest1"|| mo.dest == "Rest3"|| mo.dest == "Rest5"){
			truckGui.DoGoToRest(mo.dest);
			timer.schedule(new TimerTask() {
				public void run() {
					//print("DoneWaiting");
					event = TranEvent.GoToCrossing4;
					stateChanged();
				}
			},loadingTime
			);
		}
		else event = TranEvent.GoToCrossing4;
		mo.os = OrderState.Delivered;
		state = TranState.AtRest2;
	}
	
	private void goToCrossing1(MyOrders mo){
		//Do("GoingToCrossing1");
		truckGui.DoGoToCrossing1();
		try {
			atCrossing.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = TranEvent.GoToRest1;
		state = TranState.AtCrossing1;
		mo.os = OrderState.Delivering;
	}
	
	private void goToCrossing2(){
		//Do("GoingToCrossing2");
		truckGui.DoGoToCrossing2();
		try {
			atCrossing.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state = TranState.AtCrossing2;
		event = TranEvent.GoToCrossing3;
	}
	
	private void goToCrossing3(){
		//Do("GoingToCrossing3");
		truckGui.DoGoToCrossing3();
		try {
			atCrossing.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state = TranState.AtCrossing3;
		event = TranEvent.GoToRest2;
	}
	
	private void goToCrossing4(){
		//Do("GoingToCrossing4");
		truckGui.DoGoToCrossing4();
		try {
			atCrossing.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = TranEvent.GoToMarket;
		state = TranState.AtCrossing4;

	}
	
	private void goToMarket(){
		//Do("GoingToCrossing5");
		truckGui.DoGoToMarket();
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				event = TranEvent.GoToCrossing1;
				stateChanged();
			}
		},waitingTime
		);
		state = TranState.AtMarket;

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

