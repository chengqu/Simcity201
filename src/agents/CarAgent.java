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
	public enum TranState{Parking,Transit};
	public enum PassengerState{Waiting, Onbus, GotOff};
	TranState state = TranState.Parking;
	TranEvent event = TranEvent.GoTo1;
	private String Dest;
	private String Terminal;
	private int LineNum;
	private boolean AtBank = false;
	private boolean AtMarket = false;
	private boolean AtHouse = false;
	private boolean AtRest1 = false;
	private boolean AtRest2 = false;
	private boolean AtRest3 = false;
	private boolean AtRest4 = false;
	private boolean AtRest5 = false;
	private boolean AtRest6 = false;
	CarGui carGui = null;
	String Type;
	Timer timer = new Timer();
	private long waitingTime = 5000;
	private Semaphore atDest = new Semaphore(0,true);
	
	public CarAgent(String type){
		this.Type = type;
		}
	
	class MyPassenger{
	       	public PassengerAgent p;
			public TranState TS;
			public PassengerState PS;
	        public String dest;
	        //StopAgent stop;
	        MyPassenger(PassengerAgent p, String dest, PassengerState ps){
	        	this.dest = dest;
	        	this.p = p;
	        	this.PS = ps;
	        	
	        }
	}
	
	
	public void msgINeedARide(PassengerAgent p, String dest){
		MyP.add(new MyPassenger(p,dest,PassengerState.Waiting));
		stateChanged();
	}
	
	public void	msgAtDest(){
		atDest.release();
		stateChanged();
	}
	public void msgAtBank(){
		AtBank = true;
		stateChanged();
	}
	public void msgAtMarket(){
		AtMarket = true;
		stateChanged();
	}
	public void msgAtHouse(){
		AtHouse = true;
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
		
		for(MyPassenger mp : MyP){
			if(mp.PS == PassengerState.Waiting && state == TranState.Parking){
				GoTo();
				return true;
			}
		}
		
		for(MyPassenger mp: MyP){
			if(mp.PS == PassengerState.GotOff){
				GoParking();
				return true;
			}
		}
		
		
			
			if(AtBank == true){
				AtBank = false;
				Stop("Bank");
				return true;
			}
			if(AtMarket == true){
				AtMarket = false;
				Stop("Market");
				return true;
			}
			if(AtHouse == true){
				AtHouse = false;
				Stop("House");
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
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}


	private void GoTo() {
		// TODO Auto-generated method stub
		carGui.DoGoTo(MyP.get(0).dest);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				state = TranState.Parking;
				stateChanged();
			}
		},2000
		);
		state = TranState.Transit;
		
	}
	private void GoParking(){
		carGui.DoGoToPark(MyP.get(0).dest);
	}
	public void Stop(String dest){
		for(MyPassenger mp : MyP){
			if(mp.dest == dest){
				mp.p.msgYouAreHere();
				mp.PS = PassengerState.GotOff;
			}
		}
	}
	
	
	public void setGui(CarGui gui) {
		carGui = gui;
	}

	public CarGui getGui() {
		return carGui;
	}
	
	public int getX() {
		return carGui.xPos;
	}
	public int getY() {
		return carGui.yPos;
	}
	
}

