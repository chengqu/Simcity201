package david.restaurant;

public interface Cook {

	public abstract void msgHereIsAnOrder(WaiterAgent w, Order o);
	public abstract void msgHereIsItems(MarketAgent m, RestockList r);
	public abstract void msgCanPartiallyFill(MarketAgent m, RestockList r);
}
