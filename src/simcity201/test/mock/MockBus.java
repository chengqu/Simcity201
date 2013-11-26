package simcity201.test.mock;

import guehochoi.test.mock.EventLog;
import guehochoi.test.mock.LoggedEvent;
import agents.BusAgent;
import agents.CarAgent;
import agents.Person;
import agents.StopAgent;
import simcity201.gui.PassengerGui;
import simcity201.interfaces.Passenger;

public class MockBus extends Mock implements Passenger{

	public EventLog log = new EventLog();
	
	
	public MockBus(String name) {
		super(name);
		
	}

	@Override
	public void msgGoTo(Person p, String dest, CarAgent car, StopAgent stop) {
		log.add(new LoggedEvent("Received Person's destination " + dest));
		if( car!= null){
			log.add(new LoggedEvent("Received Person's car " + car));
		}
		if( stop != null){
			log.add(new LoggedEvent("Received Person's stop " + stop));
		}
	}
	@Override
	public String getPassengerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgGetOn(BusAgent b) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Get on bus " + b));
	}

	@Override
	public void msgGetOff(String dest) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Get off bus " + dest));
	}

	@Override
	public void msgYouAreHere() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Get off car "));
	}

	@Override
	public void msgAtCar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(PassengerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PassengerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStop(StopAgent stop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCar(CarAgent car) {
		// TODO Auto-generated method stub
		
	}

}
