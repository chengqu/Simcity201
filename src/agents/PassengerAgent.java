package agents;

import simcity201.gui.PassengerGui;
import agent.Agent;
import agents.BusAgent.TranEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
	public class PassengerAgent extends Agent {
	private String name;
	private PassengerGui passengerGui;
	private String waitDest = "Market";
	private String dest ;
	private String busDest = "Bank";
	private String stopDest = "Market";
	private String walkDest ="Rest6";
	private String carDest = "Rest1";
	private StopAgent stop = null;
	private CarAgent car = null;
	private BusAgent bus = null;
	private boolean atCar = false;
	Timer timer = new Timer();
	private Person person;
	private Semaphore atDest = new Semaphore(0,true);
	private Semaphore atStop = new Semaphore(0,true);
	public enum AgentState
	{DoingNothing,NeedBus,Walking,WaitingAtStop, OnBus, Arrived, NeedCar, AtCar, OnCar, OffCar, noCar, InBuilding, Pressed};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none,goToStop, GettingOn, GettingOff, GoingToCar, Driving, LeaveCar, Walk, LeaveBus, Enter, LeaveCarEnter, PressStop};
	AgentEvent event = AgentEvent.none;
	
	public PassengerAgent(String name, Person p){
		super();
		this.name = name;
		this.person = p;
		
	}

	public String getPassengerName() {
		return name;
		
	}
	// Messages
	public void msgGoTo(Person p, String dest,String waitDest, CarAgent car, StopAgent stop){
		this.dest = dest;
		this.person = p;
		passengerGui.show();
		if(stop != null){
		if(dest == "Rest1" || dest == "Rest2" || dest == "Rest3"||dest == "Rest4" || dest == "Rest6" )
			this.busDest = "Restaurants1";
		else if(dest == "Rest5" || dest == "House3")
			this.busDest = "Restaurants2";
		else if(dest == "House1"|| dest == "House2")
			this.busDest = "House";
		else this.busDest = dest;
		this.waitDest = waitDest;
		this.state = AgentState.NeedBus;
		this.event = AgentEvent.goToStop;
		}
		else if(car != null){
			this.car = car;
			this.carDest = dest;
			this.state = AgentState.NeedCar;
			this.event = AgentEvent.GoingToCar;
		}
		else {
			this.walkDest = dest;
			state = AgentState.noCar;
			event = AgentEvent.Walk;
		}
		stateChanged();
	}
	
	public void msgGetOn(BusAgent b){
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
	public void	msgAtDest(){
		atDest.release();
		stateChanged();
	}
	
	public void msgAtStop(){
		atStop.release();
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if (state == AgentState.NeedBus && event == AgentEvent.goToStop ){
			state = AgentState.WaitingAtStop;
			goToStop();
			return true;
		}
		
		if (state == AgentState.WaitingAtStop && event == AgentEvent.PressStop ){
			state = AgentState.Pressed;
			PressStop();
			return true;
		}
		if (state == AgentState.Pressed && event == AgentEvent.GettingOn ){
			state = AgentState.OnBus;
			GetOn();
			return true;
		}
		if (state == AgentState.OnBus && event == AgentEvent.GettingOff ){
			state = AgentState.Arrived;
			GetOff();
			return true;
		}
		if (state == AgentState.Arrived && event == AgentEvent.LeaveBus){
			state = AgentState.InBuilding;
			AtDest();
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
		if (state == AgentState.OffCar && event == AgentEvent.LeaveCarEnter){
			state = AgentState.InBuilding;
			AtDest();
			return true;
		}
		
		if(state == AgentState.noCar && event == AgentEvent.Walk){
			state = AgentState.Walking;
			Walk();
			return true;
		}
		if (state == AgentState.Walking && event == AgentEvent.Enter){
			state = AgentState.InBuilding;
			AtDest();
			return true;
		}
		return false;
	}

	private void GetOff() {
		// TODO Auto-generated method stub
		Do("GettingOff");
		passengerGui.getOff(this.busDest);
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = AgentEvent.LeaveBus;
				stateChanged();
			}
		},2000
		);
	}

	private void GetOn() {
		// TODO Auto-generated method stub
		Do("GettingOnBus");
		bus.msgImOn(this);
		passengerGui.hide();
	}

	// Actions
	private void Walk(){
		Do("Walking");
		passengerGui.DoWalkTo(this.dest);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		passengerGui.DoEnter(this.dest);
		event = AgentEvent.Enter;
		
	}
	private void goToStop(){
		Do("GoingToStop");
		passengerGui.DoGoToStop(waitDest);
		try {
			atStop.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = AgentEvent.PressStop;
	}
	private void PressStop(){
		stop.msgINeedBus(this, waitDest, busDest);
	}
	
	private void goToCar(){
		passengerGui.DoGoToCar(car.getX(), car.getY());
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
		passengerGui.hide();
		car.msgINeedARide(this, carDest);
	}
	
	private void GetOffCar(){
		passengerGui.getOff(carDest);
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = AgentEvent.LeaveCarEnter;
				stateChanged();
			}
		},2000
		);
		
	}
	private void AtDest(){
		Do("I'm at destination");
		person.msgAtDest();
		passengerGui.hide();
	}

	public String getName() {
		return name;
	}
	

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(PassengerGui g) {
		passengerGui = g;
	}

	public PassengerGui getGui() {
		return passengerGui;
	}
	
	
	public void setStop(StopAgent stop) {
		// TODO Auto-generated method stub
		this.stop = stop;
		this.state = AgentState.NeedBus;
		this.event = AgentEvent.goToStop;
	}
	public void setCar(CarAgent car) {
		// TODO Auto-generated method stub
		this.car = car;
		this.state = AgentState.NeedCar;
		this.event = AgentEvent.GoingToCar;
	}
}

