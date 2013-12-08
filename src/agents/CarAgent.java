package agents;

import agent.Agent;
import agents.BusAgent.MyPassenger;
import agents.BusAgent.PassengerState;
import agents.BusAgent.TranEvent;
import agents.BusAgent.TranState;
import simcity201.gui.BusGui;
import simcity201.gui.CarGui;
import agents.PassengerAgent;
import java.util.*;
import java.util.concurrent.Semaphore;


public class CarAgent extends Agent {
	List<MyPassenger> MyP = new ArrayList<MyPassenger>();
	public enum TranState{Parking,Transit, Something};
	public enum PassengerState{Waiting, Onbus, GotOff};
	TranState state = TranState.Parking;
	TranEvent event = TranEvent.GoTo1;
	CarGui carGui = null;
	String Type;
	String dest;
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
	        //StopAgent stop;
	        MyPassenger(PassengerAgent p,String waitDest, String dest, PassengerState ps){
	        	this.dest = dest;
	        	this.p = p;
	        	this.PS = ps;
	        	this.waitDest = waitDest;
	        }
	}
	
	
	public void msgINeedARide(PassengerAgent p,String waitDest, String dest){
		if(waitDest == "birth"){
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
		
		
		
			
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}


	private void GoTo() {
		// TODO Auto-generated method stub
		Do("shit!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+ MyP.get(0).waitDest+MyP.get(0).dest);
		carGui.DoDriveTo(MyP.get(0).waitDest, MyP.get(0).dest);
		
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Stop((MyP.get(0).dest));
		carGui.DoGoToPark(dest);
		state = TranState.Parking;
		
	}
	public void Stop(String dest){
		try{
		for(MyPassenger mp : MyP){
			if(mp.dest == dest){
				mp.p.msgYouAreHere();
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

