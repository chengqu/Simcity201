package simcity201.test.mock;

import guehochoi.test.mock.EventLog;
import guehochoi.test.mock.LoggedEvent;
import simcity201.gui.CarGui;
import simcity201.interfaces.Car;
import simcity201.interfaces.Passenger;



	public class MockCar extends Mock implements Car{
		
		public EventLog log = new EventLog();
		
		public MockCar(String name) {
			super(name);
			
		}

		@Override
		public void msgINeedARide(MockPassenger p, String dest) {
			// TODO Auto-generated method stub
			log.add(new LoggedEvent("Received Person's destination " + dest));
		}

		@Override
		public void msgAtDest() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void Stop(String dest) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setGui(CarGui gui) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public CarGui getGui() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getX() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getY() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
	


