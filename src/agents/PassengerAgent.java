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
	private String waitDest;
	private String dest ;
	private String busDest;
	private String carDest;
	private StopAgent stop = null;
	private CarAgent car = null;
	private BusAgent bus = null;
	Timer timer = new Timer();
	private Person person;
	private Semaphore atDest = new Semaphore(0,true);
	private Semaphore atStop = new Semaphore(0,true);
	private Semaphore atCar = new Semaphore(0,true);
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
	public void msgGoTo(Person p, String dest,CarAgent car, StopAgent stop){
		this.dest = dest;
		this.person = p;
		this.stop = stop;
		passengerGui.show();
		if(stop != null && dest != "Apart"){
			if(dest == "Rest1" || dest == "Rest2" || dest == "Rest3"||dest == "Rest4" || dest == "Rest6" )
					this.busDest = "Restaurants1";
				else if(dest == "Rest5" || dest == "House3")
					this.busDest = "Restaurants2";
				else if(dest == "House1"|| dest == "House2")
					this.busDest = "House";
				else this.busDest = dest;
		//computing waitDest
			if(p.location == "Rest1" || p.location == "Rest2" || p.location == "Rest3"||p.location == "Rest4" || p.location == "Rest6" )
					this.waitDest = "Restaurants1";
				else if(p.location == "Rest5" || p.location == "House3")
					this.waitDest = "Restaurants2";
				else if(p.location == "House1"|| p.location == "House2")
					this.waitDest = "House";
				else if(p.location == "birth")
					this.waitDest = "Bank";
				else if(p.location == "Apart")
					this.waitDest = "Restaurants1";
				else this.waitDest = p.location;
		
		if(this.waitDest == this.busDest || (this.waitDest == "Market" && this.dest == "House1")){
			state = AgentState.noCar;
			event = AgentEvent.Walk;
		}
		else{
		this.state = AgentState.NeedBus;
		this.event = AgentEvent.goToStop;
		}
		}
		else if(car != null){
			this.car = car;
			this.carDest = dest;
			this.state = AgentState.NeedCar;
			this.event = AgentEvent.GoingToCar;
		}
		else {
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
		atCar.release();
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
			state = AgentState.OnCar;
			goToCar();
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
		passengerGui.showBus(this.busDest);
		try {
			atStop.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Walk();
		event = AgentEvent.LeaveBus;
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
		//passengerGui.DoEnter(this.dest);
		event = AgentEvent.Enter;
		
	}
	private void goToStop(){
		Do("GoingToStop");
		passengerGui.DoGoToStop(this.waitDest);
		try {
			atStop.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		passengerGui.DoWait(this.waitDest);
		event = AgentEvent.PressStop;
	}
	private void PressStop(){
		stop.msgINeedBus(this, this.waitDest, this.busDest);
	}
	
	private void goToCar(){
		passengerGui.DoGoToCar(car.getX(), car.getY());
		try {
			atCar.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		passengerGui.hide();
		car.msgINeedARide(this,person.location, this.carDest);
	}
	
	
	private void GetOffCar(){
		passengerGui.showCar(this.carDest);
		timer.schedule(new TimerTask() {
			public void run() {
				print("DoneWaiting");
				event = AgentEvent.LeaveCarEnter;
				stateChanged();
			}
		},1000
		);
		
		
	}
	private void AtDest(){
		Do("I'm at destination");
		person.msgAtDest();
		passengerGui.hide();
		person.location = dest;
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
	}
	public void setCar(CarAgent car) {
		// TODO Auto-generated method stub
		this.car = car;
	}
}

