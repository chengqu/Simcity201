package david.restaurant.Test.Mock;

import david.restaurant.Cook;
import david.restaurant.RestockList;
import david.restaurant.Interfaces.Market;

public class MockMarket extends Mock implements Market{
	
	String name;
	public EventLog log = new EventLog();
	
	public MockMarket(String n) {
		super(n);
		name = n;
		// TODO Auto-generated constructor stub
	}
	
	public String getName()
	{
		return name;
	}

	@Override
	public void msgHereIsMoney(float money, String id) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("GotMoney"));
	}

	@Override
	public void msgNeedFood(Cook c, RestockList r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCantPay(String id) {
		// TODO Auto-generated method stub
		
	}
}
