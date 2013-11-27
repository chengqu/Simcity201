package simcity201.interfaces;


import simcity201.gui.CarGui;
import simcity201.test.mock.MockPassenger;

public interface BusStop {

	public void msgINeedARide(MockPassenger p, String dest);

	public void msgAtDest();

	public void Stop(String dest);

	public void setGui(CarGui gui);

	public CarGui getGui();

	public int getX();

	public int getY();

}