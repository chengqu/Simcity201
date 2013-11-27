package simcity201.interfaces;

import simcity201.gui.BusGui;
import agents.CarAgent;
import agents.StopAgent;
import agents.Person;
import agents.BusAgent;


import simcity201.gui.PassengerGui;

public interface Passenger {

	public String getPassengerName();

	// Messages
	public void msgGoTo(Person p, String dest, CarAgent car, StopAgent stop);

	public void msgGetOn(BusAgent b);

	public void msgGetOff(String dest);

	public void msgYouAreHere();

	public void msgAtCar();

	public void msgAtDest();

	public void msgAtStop();

	public String getName();

	public String toString();

	public void setGui(PassengerGui g);

	public PassengerGui getGui();

	public void setStop(StopAgent stop);

	public void setCar(CarAgent car);

}