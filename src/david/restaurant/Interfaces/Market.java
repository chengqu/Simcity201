package david.restaurant.Interfaces;

import david.restaurant.Cook;
import david.restaurant.RestockList;
import david.restaurant.Test.Mock.EventLog;

public interface Market {
	public abstract void msgHereIsMoney(float money, String id);
	public abstract void msgNeedFood(Cook c, RestockList r);
	public abstract void msgCantPay(String id);
	public abstract String getName();
}
