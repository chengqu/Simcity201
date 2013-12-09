package agents;

import agent.Agent;
import agents.BusAgent.MyPassenger;
import agents.BusAgent.PassengerState;
import agents.BusAgent.TranEvent;
import agents.BusAgent.TranState;
import agents.PassengerAgent.AgentEvent;
import simcity201.gui.BusGui;
import simcity201.gui.CarGui;
import agents.PassengerAgent;
import java.util.*;
import java.util.concurrent.Semaphore;


public class CarAgent extends Agent {
	List<MyPassenger> MyP = new ArrayList<MyPassenger>();
	public enum TranState{Parking,Transit, Something, Parked, GoParking};
	public enum PassengerState{Waiting, Onbus, GotOff};
	TranState state = TranState.Parking;
	TranEvent event = TranEvent.GoTo1;
	CarGui carGui = null;
	String Type;
	String dest;
	Timer timer = new Timer();
	private Semaphore atDest = new Semaphore(0,true);
	public CarAgent(String type){
		this.Type = type;
		}
	
	class MyPassenger{
	       	public PassengerAgent p;
			public TranState TS;
			public PassengerState PS;
	        public String dest;
	        public String waitDest;
	        MyPassenger(PassengerAgent p,String waitDest, String dest, PassengerState ps){
	        	this.dest = dest;
	        	this.p = p;
	        	this.PS = ps;
	        	this.waitDest = waitDest;
	        }
	}
	
	
	public void msgINeedARide(PassengerAgent p,String waitDest, String dest){
		if(waitDest.equals("birth")){
			MyP.add(new MyPassenger(p,"Bank",dest,PassengerState.Waiting));
		}
		else MyP.add(new MyPassenger(p,waitDest,dest,PassengerState.Waiting));
		this.dest = dest;
		stateChanged();
	}
	
	public void	msgAtDest(){
		atDest.release();
		stateChanged();
	}

	public boolean pickAndExecuteAnAction() {
		
		for(MyPassenger mp : MyP){
			if(mp.PS == PassengerState.Waiting && state == TranState.Parking){
				GoTo();
				return true;
			}
		}
		
		if(state == TranState.GoParking){
			GoToPark();
			return true;
		}
		
		
			
		return false;
		
	}


	private void GoTo() {
		
		carGui.DoDriveTo(MyP.get(0).waitDest, MyP.get(0).dest);
		
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Stop((MyP.get(0).dest));
		carGui.DoGoToPark(dest);
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				state = TranState.GoParking;
				stateChanged();
			}
		},1000
		);
		
		
	}
	private void GoToPark(){
		carGui.hide();
		state = TranState.Parking;
	}
	public void Stop(String dest){
		try{
		for(MyPassenger mp : MyP){
			if(mp.dest.equals(dest)){
				mp.p.msgYouAreHere(carGui.getXPos(),carGui.getYPos());
				mp.PS = PassengerState.GotOff;
				MyP.remove(mp);
			}
		}}catch(ConcurrentModificationException e){
			Do("no one to pick up");
		}
	}
	
	
	public void setGui(CarGui gui) {
		carGui = gui;
	}

	public CarGui getGui() {
		return carGui;
	}
	
	public int getX() {
		return carGui.getXPos();
	}
	public int getY() {
		return carGui.getYPos();
	}
	
}

