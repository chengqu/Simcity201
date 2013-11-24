package simcty201.interfaces;

import java.util.List;

import Market.MarketEmployeeAgent;
import Market.Order;

public interface MarketCustomer {

	//from Employee
	//public abstract void msgHereIsYourStuff(List<String> orderList);

	//from employee
	public abstract void msgHereIsYourOrderCharge(float charge);

	//from Employee
	public abstract void msgPaymentNoted();

	public abstract boolean pickAndExecuteAnAction();

	public abstract void msgLeaveEarly();

	public abstract void msgHereIsYourStuff(Order o);

	public abstract void msgAskForCustomerOrder(MarketEmployeeAgent e);

	public abstract String getName();


}