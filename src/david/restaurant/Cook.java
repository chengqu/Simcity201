package david.restaurant;

import david.restaurant.Interfaces.Waiter;

public interface Cook {

	public abstract void msgHereIsAnOrder(Waiter w, Order o);
	public abstract void msgHereIsItems(MarketAgent m, RestockList r);
	public abstract void msgCanPartiallyFill(MarketAgent m, RestockList r);
}
