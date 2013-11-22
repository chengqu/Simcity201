package david.restaurant.Interfaces;

import david.restaurant.Check;
import david.restaurant.CustomerAgent;
import david.restaurant.Order;

public interface Waiter {
	public abstract void msgPleaseSitCustomer(CustomerAgent c, int t);
	public abstract void msgImReadyToOrder(CustomerAgent c);
	public abstract void msgHereIsMyChoice(CustomerAgent c, String choice);
	public abstract void msgDoneEatingAndLeaving(CustomerAgent c);
	public abstract void msgOrderIsReady(Order o);
	public abstract void msgGoOnBreak();
	public abstract void msgStopBreak();
	public abstract void msgOkBreak();
	public abstract void msgNoBreak();
	public abstract void msgNotAvailable(Order o);
	public abstract void msgHereIsCheck(Check c);
}
