package agents;

import agent.Agent;
import agents.PassengerAgent.AgentEvent;
import simcity201.gui.BusGui;

import java.util.*;
import java.util.concurrent.Semaphore;


public class BusAgent extends Agent {
	List<MyPassenger> MyP = new ArrayList<MyPassenger>();
	List<MyStop> MyS = new ArrayList<MyStop>();
	public enum TranState{AtTerminal,AtStop1, AtStop2, AtStop3,AtStop4,AtStop5, AtCrossing1, AtCrossing2, AtCrossing3};
	public enum TranEvent{GoTo1,GoTo2,GoTo3,GoTo4,GoTo5,GoToTerminal,DoingNothing, GoToCrossing1, GoToCrossing2, GoToCrossing3};
	public enum PassengerState{Waiting, Onbus, GotOff};
	TranState state = TranState.AtTerminal;
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
	BusGui busGui = null;
	Timer timer = new Timer();
	private long waitingTime = 5000;
	private Semaphore atDest = new Semaphore(0,true);
	
	public BusAgent(String dest1,String crossing1, String dest2,String crossing2, String dest3,String crossing3, String dest4, String dest5,String Terminal, int LineNum){
		
		MyS.add(new MyStop(dest1));
		MyS.add(new MyStop(dest2));
		MyS.add(new MyStop(dest3));
		MyS.add(new MyStop(dest4));
		MyS.add(new MyStop(dest5));
		MyS.add(new MyStop(crossing1));
		MyS.add(new MyStop(crossing2));
		MyS.add(new MyStop(crossing3));
		this.Terminal = Terminal;
		this.LineNum = LineNum;	
		}
	
	class MyPassenger{
	       	public PassengerAgent p;
			public TranState TS;
			public PassengerState PS;
			public double cash;
			public String WaitDest;
	        public String dest;
	        //StopAgent stop;
	        MyPassenger(PassengerAgent p, String waitingDest, String dest, PassengerState ps){
	        	this.dest = dest;
	        	this.p = p;
	        	this.PS = ps;
	        	this.WaitDest = waitingDest;
	        	
	        }
	}
	
	class MyStop{
			//StopAgent stop;
			public String Dest;
			public boolean isActive = false;
			MyStop(String dest){
				this.Dest = dest;
			}
	}
	

	
	public void msgStopAt(PassengerAgent p,String wd, String dest){
		MyP.add(new MyPassenger(p,wd,dest,PassengerState.Waiting));
		for(MyStop ms:MyS){
			if(ms.Dest == wd){
				ms.isActive = true;
			}
		}
		stateChanged();
	}
	public void msgImOn(PassengerAgent p){
		for (MyPassenger mp: MyP){
			if(mp.p == p){
				mp.PS = PassengerState.Onbus;
			}
		}
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
		
		for(MyStop s : MyS){
			if(s.isActive == true && state == TranState.AtTerminal && event == TranEvent.GoTo1){
				GoTo1();
				return true;
			}
		}
		

		if(state == TranState.AtStop1 && event == TranEvent.GoToCrossing1){
				GoToCrossing1();
				return true;
		}
		
		if(state == TranState.AtCrossing1 && event == TranEvent.GoTo2){
				GoTo2();
				return true;
		}
			

		if(state == TranState.AtStop2 && event == TranEvent.GoToCrossing2){
				GoToCrossing2();
				return true;
		}
			if(state == TranState.AtCrossing2 && event == TranEvent.GoTo3){
				GoTo3();
				return true;
		}

		if(state == TranState.AtStop3 && event == TranEvent.GoToCrossing3){
					GoToCrossing3();
					return true;
			}	
			if(state == TranState.AtCrossing3 && event == TranEvent.GoTo4 ){
				GoTo4();
				return true;
		}
		
			if(state == TranState.AtStop4 && event == TranEvent.GoTo5){
				GoTo5();
				return true;
		}
			
			if(state == TranState.AtStop5 && event == TranEvent.GoToTerminal){
				GoToTerminal();
				return true;
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

	

	private void GoToCrossing3() {
		// TODO Auto-generated method stub
		Do("GoingToCrossing3");
		busGui.DoGoTo(MyS.get(7).Dest);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneResting");
				event = TranEvent.GoTo4;
				stateChanged();
			}
		},waitingTime
		);
		state = TranState.AtCrossing3;
	}
	private void GoToCrossing2() {
		// TODO Auto-generated method stub
		Do("GoingToCrossing2");
		busGui.DoGoTo(MyS.get(6).Dest);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneResting");
				event = TranEvent.GoTo3;
				stateChanged();
			}
		},waitingTime
		);
		state = TranState.AtCrossing2;
	}
	private void GoToCrossing1() {
		// TODO Auto-generated method stub
		Do("GoingToCrossing1");
		busGui.DoGoTo(MyS.get(5).Dest);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneResting");
				event = TranEvent.GoTo2;
				stateChanged();
			}
		},waitingTime
		);
		state = TranState.AtCrossing1;
	}
	private void GoToTerminal() {
		// TODO Auto-generated method stub
		Do("GoingToTerminal");
		busGui.DoGoTo(Terminal);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneResting");
				event = TranEvent.GoTo1;
				stateChanged();
			}
		},waitingTime
		);
		state = TranState.AtTerminal;
		
	}

	private void GoTo5() {
		// TODO Auto-generated method stub
		Do("GoingToStop5");
		busGui.DoGoTo(MyS.get(4).Dest);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = TranEvent.GoToTerminal;
				stateChanged();
			}
		},waitingTime
		);
		state = TranState.AtStop5;
		MyS.get(4).isActive = false;
		
	}

	private void GoTo4() {
		// TODO Auto-generated method stub
		Do("GoingToStop4");
		busGui.DoGoTo(MyS.get(3).Dest);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = TranEvent.GoTo5;
				stateChanged();
			}
		},waitingTime
		);
		state = TranState.AtStop4;
		MyS.get(3).isActive = false;
		
	}

	private void GoTo3() {
		// TODO Auto-generated method stub
		Do("GoingToStop3");
		busGui.DoGoTo(MyS.get(2).Dest);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = TranEvent.GoToCrossing3;
				stateChanged();
			}
		},waitingTime
		);
		state = TranState.AtStop3;
		MyS.get(2).isActive = false;
		
	}

	private void GoTo2() {
		// TODO Auto-generated method stub
		Do("GoingToStop2");
		busGui.DoGoTo(MyS.get(1).Dest);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = TranEvent.GoToCrossing2;
				stateChanged();
			}
		},waitingTime
		);
		state = TranState.AtStop2;
		MyS.get(1).isActive = false;
		
	}

	private void GoTo1() {
		// TODO Auto-generated method stub
		Do("GoingToStop1");
		busGui.DoGoTo(MyS.get(0).Dest);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = TranEvent.GoToCrossing1;
				stateChanged();
			}
		},2000
		);
		state = TranState.AtStop1;
		MyS.get(0).isActive = false;
		
	}

	public void Stop(String dest){
		for(MyPassenger mp : MyP){
			if(mp.WaitDest == dest){
				mp.p.msgGetOn(this);
			}
			if(mp.dest == dest){
				mp.p.msgGetOff(mp.dest);
				mp.PS = PassengerState.GotOff;
			}
		}
	}
	
	public void setGui(BusGui gui) {
		busGui = gui;
	}

	public BusGui getGui() {
		return busGui;
	}

	
}

