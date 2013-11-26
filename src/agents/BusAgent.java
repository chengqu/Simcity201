package agents;

import agent.Agent;
import agents.PassengerAgent.AgentEvent;
import simcity201.gui.BusGui;

import java.util.*;
import java.util.concurrent.Semaphore;


public class BusAgent extends Agent {
	List<MyPassenger> MyP = Collections.synchronizedList(new ArrayList<MyPassenger>());
	List<MyStop> MyS = Collections.synchronizedList(new ArrayList<MyStop>());
	public enum TranState{AtTerminal,AtStop1, AtStop2, AtStop3,AtStop4,AtStop5, AtCrossing1, AtCrossing2, AtCrossing3,AtCrossing4, AtCrossing5};
	public enum TranEvent{GoTo1,GoTo2,GoTo3,GoTo4,GoTo5,GoToTerminal,DoingNothing, GoToCrossing1, GoToCrossing2, GoToCrossing3, GoToCrossing4, GoToCrossing5};
	public enum PassengerState{Waiting, Onbus, GotOff};
	TranState state = TranState.AtTerminal;
	TranEvent event = TranEvent.GoTo1;
	private String Dest;
	private String Terminal;
	BusGui busGui = null;
	Timer timer = new Timer();
	private long waitingTime = 1000;
	private Semaphore atDest = new Semaphore(0,true);
	private Semaphore atCrossing = new Semaphore(0,true);
	private int LineNum;
	
	public BusAgent(String dest1,String crossing1, String dest2,String crossing2, String dest3,String crossing3, String dest4,String crossing4, String dest5,String crossing5,String Terminal, int LineNum){
		
		MyS.add(new MyStop(dest1));
		MyS.add(new MyStop(dest2));
		MyS.add(new MyStop(dest3));
		MyS.add(new MyStop(dest4));
		MyS.add(new MyStop(dest5));
		MyS.add(new MyStop(crossing1));
		MyS.add(new MyStop(crossing2));
		MyS.add(new MyStop(crossing3));
		MyS.add(new MyStop(crossing4));
		MyS.add(new MyStop(crossing5));
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
	public void msgAtCrossing(){
		atCrossing.release();
		stateChanged();
	}
	
	protected boolean pickAndExecuteAnAction() {
		
		synchronized(MyS){
			for(MyStop s : MyS){
			if(s.isActive == true && state == TranState.AtTerminal && event == TranEvent.GoTo1){
				GoTo1();
				return true;
			}
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
		
		if(state == TranState.AtStop4 && event == TranEvent.GoToCrossing4){
			GoToCrossing4();
			return true;
		}	
		
		if(state == TranState.AtCrossing4 && event == TranEvent.GoTo5){
				GoTo5();
				return true;
		}
		
		if(state == TranState.AtStop5 && event == TranEvent.GoToCrossing5){
			GoToCrossing5();
			return true;
		}	
			
			if(state == TranState.AtCrossing5 && event == TranEvent.GoToTerminal){
				GoToTerminal();
				return true;
		}
			
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	

	private void GoToCrossing5() {
		// TODO Auto-generated method stub
		Do("GoingToCrossing5");
		busGui.DoGoTo(MyS.get(9).Dest);
		try {
			atCrossing.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = TranEvent.GoToTerminal;
		state = TranState.AtCrossing5;
	}
	private void GoToCrossing4() {
		// TODO Auto-generated method stub
		Do("GoingToCrossing4");
		busGui.DoGoTo(MyS.get(8).Dest);
		try {
			atCrossing.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = TranEvent.GoTo5;
		state = TranState.AtCrossing4;
	}
	private void GoToCrossing3() {
		// TODO Auto-generated method stub
		Do("GoingToCrossing3");
		busGui.DoGoTo(MyS.get(7).Dest);
		try {
			atCrossing.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = TranEvent.GoTo4;
		state = TranState.AtCrossing3;
	}
	private void GoToCrossing2() {
		// TODO Auto-generated method stub
		Do("GoingToCrossing2");
		busGui.DoGoTo(MyS.get(6).Dest);
		try {
			atCrossing.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = TranEvent.GoTo3;
		state = TranState.AtCrossing2;
	}
	private void GoToCrossing1() {
		// TODO Auto-generated method stub
		Do("GoingToCrossing1");
		busGui.DoGoTo(MyS.get(5).Dest);
		try {
			atCrossing.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = TranEvent.GoTo2;
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
		busGui.DoGoWait(Terminal);
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneResting");
				event = TranEvent.GoTo1;
				stateChanged();
			}
		},waitingTime
		);
		state = TranState.AtTerminal;
		if(!MyP.isEmpty()){
			MyS.get(0).isActive = true;
		}
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
		busGui.DoGoWait(MyS.get(4).Dest);
		Stop(MyS.get(4).Dest);
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = TranEvent.GoToCrossing5;
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
		busGui.DoGoWait(MyS.get(3).Dest);
		Stop(MyS.get(3).Dest);
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = TranEvent.GoToCrossing4;
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
		busGui.DoGoWait(MyS.get(2).Dest);
		Stop(MyS.get(2).Dest);
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
		busGui.DoGoWait(MyS.get(1).Dest);
		Stop(MyS.get(1).Dest);
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
		busGui.DoGoWait(MyS.get(0).Dest);
		Stop(MyS.get(0).Dest);
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = TranEvent.GoToCrossing1;
				stateChanged();
			}
		},waitingTime
		);
		state = TranState.AtStop1;
		MyS.get(0).isActive = false;
	}

	public void Stop(String dest){
		try{
			for(MyPassenger mp : MyP ){
			if(mp.WaitDest == dest && mp.PS == PassengerState.Waiting){
				mp.p.msgGetOn(this);
			}
			if(mp.dest == dest && mp.PS == PassengerState.Onbus){
				mp.p.msgGetOff(mp.dest);
				mp.PS = PassengerState.GotOff;
				MyP.remove(mp);
			}
		}
		}catch(ConcurrentModificationException e){
			Do("no one to pick up");
		}
	}
	
	public void setGui(BusGui gui) {
		busGui = gui;
	}

	public BusGui getGui() {
		return busGui;
	}

	
}

