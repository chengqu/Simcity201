package david.restaurant.Test.Mock;

import java.util.ArrayList;
import java.util.List;

import david.restaurant.Check;
import david.restaurant.CustomerAgent;
import david.restaurant.Order;
import david.restaurant.Interfaces.Customer;
import david.restaurant.Interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {

	public EventLog log = new EventLog();
	
	public List<Check> checks = new ArrayList<Check>();
	
	public MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub3
	}

	@Override
	public void msgPleaseSitCustomer(CustomerAgent c, int t) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " please seat customer"));
	}

	@Override
	public void msgImReadyToOrder(CustomerAgent c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " can accept order from " + c.getName()));
	}

	@Override
	public void msgHereIsMyChoice(CustomerAgent c, String choice) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + ", " + c.getName() + " ordered " + choice));
	}

	@Override
	public void msgDoneEatingAndLeaving(CustomerAgent c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + ", " + c.getName() + " is leaving"));
	}

	@Override
	public void msgOrderIsReady(Order o) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " Received order from cook"));
	}

	@Override
	public void msgGoOnBreak() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " ask to go on break"));
	}

	@Override
	public void msgStopBreak() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " leave break"));
	}

	@Override
	public void msgOkBreak() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " allowed to go on break"));
	}

	@Override
	public void msgNoBreak() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " not allowed to go on break"));
	}

	@Override
	public void msgNotAvailable(Order o) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " order not available"));
	}

	@Override
	public void msgHereIsCheck(Check c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " received check"));
		checks.add(c);
	}

}
