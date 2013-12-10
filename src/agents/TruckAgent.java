package agents;

import agent.Agent;
import agents.PassengerAgent.AgentEvent;
import simcity201.gui.BusGui;
import simcity201.gui.TruckGui;
import simcity201.interfaces.NewMarketInteraction;

import java.util.*;
import java.util.concurrent.Semaphore;

import newMarket.MarketRestaurantHandlerAgent;


public class TruckAgent extends Agent {
	private String rest;
	List<MyOrders> MyO = new ArrayList<MyOrders>();
	List<MyRestaurants> MyR = new ArrayList<MyRestaurants>();
	public enum TranState{AtMarket,AtRest};
	public enum TranEvent{Resting,GoToMarket};
	public enum OrderState{Pending,Delivering,Delivered};
	TranState state = TranState.AtMarket;
	TranEvent event = TranEvent.Resting;
	private boolean AtMarket = false;
	TruckGui truckGui = null;
	MarketRestaurantHandlerAgent handler = null;
	Timer timer = new Timer();
	private long loadingTime = 2000;
	private Semaphore atDest = new Semaphore(0,true);
	public TruckAgent(MarketRestaurantHandlerAgent handler){
			this.handler = handler;
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
	
	public void msgDeliverOrder(NewMarketInteraction c ){
		MyO.add(new MyOrders(c.getName(),OrderState.Pending));
		stateChanged();
	}
	
	public void	msgAtDest(String rest){
		atDest.release();
		this.rest = rest;
		stateChanged();
	}
	
	public void msgAtMarket(){
		AtMarket = true;
		stateChanged();
	}
	
	
	protected boolean pickAndExecuteAnAction() {
		
			while(!MyO.isEmpty() && AtMarket == true){
				AtMarket = false;
				goToRest(MyO.get(0));
				return true;
			}
			if(state == TranState.AtRest && event == TranEvent.GoToMarket){
					goToMarket();
					return true;
				}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	

	private void goToRest(MyOrders mo){
		truckGui.DoGoTo("Market",mo.dest);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				//print("DoneWaiting");
				event = TranEvent.GoToMarket;
				stateChanged();
			}
		},loadingTime
		);
		mo.os = OrderState.Delivered;
		state = TranState.AtRest;
	}
	
	private void goToMarket(){
		handler.msgTruckAtDest(false);
		truckGui.DoGoTo(rest, "Market");
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state = TranState.AtMarket;
		event = TranEvent.Resting;
		truckGui.DoGoToMarket();
		MyO.remove(0);
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

