package david.restaurant.Test.Mock;

import java.util.ArrayList;
import java.util.List;

import david.restaurant.Check;
import david.restaurant.CookAgent;
import david.restaurant.CustomerAgent;
import david.restaurant.Order;
import david.restaurant.Interfaces.Customer;
import david.restaurant.Interfaces.Waiter;
import david.restaurant.gui.Gui;
import david.restaurant.gui.WaiterGui;

public class MockWaiter extends Mock implements Waiter {

	public EventLog log = new EventLog();
	
	public List<Check> checks = new ArrayList<Check>();
	
	public MockWaiter(String name) {
		super(name);
		
	}

	
	public void msgPleaseSitCustomer(CustomerAgent c, int t) {
		
		log.add(new LoggedEvent(this.getName() + " please seat customer"));
	}

	
	public void msgImReadyToOrder(CustomerAgent c) {
		
		log.add(new LoggedEvent(this.getName() + " can accept order from " + c.getName()));
	}

	
	public void msgHereIsMyChoice(CustomerAgent c, String choice) {
		
		log.add(new LoggedEvent(this.getName() + ", " + c.getName() + " ordered " + choice));
	}

	
	public void msgDoneEatingAndLeaving(CustomerAgent c) {
		
		log.add(new LoggedEvent(this.getName() + ", " + c.getName() + " is leaving"));
	}

	
	public void msgOrderIsReady(Order o) {
		
		log.add(new LoggedEvent(this.getName() + " Received order from cook"));
	}

	
	public void msgGoOnBreak() {
		
		log.add(new LoggedEvent(this.getName() + " ask to go on break"));
	}

	
	public void msgStopBreak() {
		
		log.add(new LoggedEvent(this.getName() + " leave break"));
	}

	
	public void msgOkBreak() {
		
		log.add(new LoggedEvent(this.getName() + " allowed to go on break"));
	}

	
	public void msgNoBreak() {
		
		log.add(new LoggedEvent(this.getName() + " not allowed to go on break"));
	}

	
	public void msgNotAvailable(Order o) {
		
		log.add(new LoggedEvent(this.getName() + " order not available"));
	}

	
	public void msgHereIsCheck(Check c) {
		
		log.add(new LoggedEvent(this.getName() + " received check"));
		checks.add(c);
	}

	
	public void print_() {
		
		
	}

	
	public void setGui(WaiterGui g) {
		
		
	}

	
	public void setCook(CookAgent c) {
		
		
	}

	
	public void msgDoneShift() {
		
		
	}

	
	public void msgDoneSeating() {
		
		
	}

	
	public void msgAtCashier() {
		
		
	}

	
	public void msgCanStartSeating() {
		
		
	}

	
	public void msgCanTakeOrder() {
		
		
	}

	
	public void msgAtCook() {
		
		
	}

	
	public boolean pickAndExecuteAnAction() {
		
		return false;
	}

	
	public Gui getGui() {
		
		return null;
	}


	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
