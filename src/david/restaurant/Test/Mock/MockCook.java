package david.restaurant.Test.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import david.restaurant.Cook;
import david.restaurant.MarketAgent;
import david.restaurant.Order;
import david.restaurant.RestockList;
import david.restaurant.Interfaces.Waiter;

public class MockCook extends Mock implements Cook{

	public int numFoodReceived = 0;
	public List<RestockList> fulfilledRequests = Collections.synchronizedList(new ArrayList<RestockList>());
	public List<RestockList> partialRequests = Collections.synchronizedList(new ArrayList<RestockList>());
	
	Object fulfilledLock = new Object();
	Object partialLock = new Object();
	
	public MockCook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsAnOrder(Waiter w, Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsItems(MarketAgent m, RestockList r) {
		// TODO Auto-generated method stub
		synchronized(fulfilledLock)
		{
			fulfilledRequests.add(r);
		}
	}

	@Override
	public void msgCanPartiallyFill(MarketAgent m, RestockList r) {
		// TODO Auto-generated method stub
		synchronized(partialLock)
		{
			partialRequests.add(r);
		}
	}
}
