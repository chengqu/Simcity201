package agents;

import simcity201.gui.PassengerGui;
import agent.Agent;
import agents.BusAgent.TranEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
	public class PassengerAgent extends Agent {
	private String name;
	private PassengerGui customerGui;
	private String waitDest = "Bank";
	private String dest = "Rest1";
	private StopAgent stop = null;
	private CarAgent car = null;
	private BusAgent bus = null;
	private boolean atCar = false;
	Timer timer = new Timer();
	public enum AgentState
	{DoingNothing,Walking,WaitingAtStop, OnBus, Arrived, NeedCar, AtCar, OnCar, OffCar, noCar};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none,goToStop, GettingOn, GettingOff, GoingToCar, Driving, LeaveCar, Walk};
	AgentEvent event = AgentEvent.none;
	
	public PassengerAgent(String name){
		super();
		this.name = name;
		if(car == null && bus == null){
			state = AgentState.noCar;
			event = AgentEvent.Walk;
		}
	}

	/**
	 * hack to establish connection to Host agent.
	 */

	public String getCustomerName() {
		return name;
		
	}
	// Messages

	public void msgGetOn(BusAgent b){
		//print("Get On The Bus");
		event = AgentEvent.GettingOn;
		this.bus = b;
		stateChanged();
	}
	public void msgGetOff(String dest){
		event = AgentEvent.GettingOff;
		stateChanged();
	}
	
	public void msgYouAreHere(){
		Do("Im here");
		event = AgentEvent.LeaveCar;
		stateChanged();
	}
	
	public void msgAtCar(){
		atCar = true;
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (state == AgentState.DoingNothing && event == AgentEvent.goToStop ){
			state = AgentState.WaitingAtStop;
			goToStop(dest);
			return true;
		}
		if (state == AgentState.WaitingAtStop && event == AgentEvent.GettingOn ){
			state = AgentState.OnBus;
			GetOn();
			return true;
		}
		if (state == AgentState.OnBus && event == AgentEvent.GettingOff ){
			state = AgentState.Arrived;
			GetOff();
			return true;
		}
		
		if (state == AgentState.NeedCar && event == AgentEvent.GoingToCar){
			state = AgentState.AtCar;
			goToCar();
			return true;
		}
		if (state == AgentState.AtCar && event == AgentEvent.Driving && atCar == true){
			atCar = false;
			state = AgentState.OnCar;
			GetOnCar();
			return true;
		}
		if (state == AgentState.OnCar && event == AgentEvent.LeaveCar ){
			state = AgentState.OffCar;
			GetOffCar();
			return true;
		}

		if(state == AgentState.noCar && event == AgentEvent.Walk){
			state = AgentState.Walking;
			Walk();
			return true;
		}
		return false;
	}

	private void GetOff() {
		// TODO Auto-generated method stub
		Do("GettingOff");
		customerGui.DoGoToStop(dest);
		customerGui.show(dest);
	}

	private void GetOn() {
		// TODO Auto-generated method stub
		Do("GettingOnBus");
		bus.msgImOn(this);
		customerGui.hide();
	}

	// Actions
	private void Walk(){
		Do("Walking");
		customerGui.DoGoToCar(500, 500);
		
	}
	private void goToStop(String dest){
		Do("GoingToStop");
		customerGui.DoGoToStop(waitDest);
		stop.msgINeedBus(this, "Bank", "Rest1");
	}
	
	private void goToCar(){
		customerGui.DoGoToCar(car.getX(), car.getY());
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = AgentEvent.Driving;
				stateChanged();
			}
		},2000
		);
	}
	
	private void GetOnCar(){
		customerGui.DoGoToStop("Bank");
		customerGui.hide();
		car.msgINeedARide(this, this.waitDest, "Bank");
	}
	
	private void GetOffCar(){
		customerGui.show("Bank");
		customerGui.DoGoToStop("Market");
		
	}
	

	public String getName() {
		return name;
	}
	

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(PassengerGui g) {
		customerGui = g;
	}

	public PassengerGui getGui() {
		return customerGui;
	}
	
	
	public void setStop(StopAgent stop) {
		// TODO Auto-generated method stub
		this.stop = stop;
		this.state = AgentState.DoingNothing;
		this.event = AgentEvent.goToStop;
	}
	public void setCar(CarAgent car) {
		// TODO Auto-generated method stub
		this.car = car;
		this.state = AgentState.NeedCar;
		this.event = AgentEvent.GoingToCar;
	}
}

