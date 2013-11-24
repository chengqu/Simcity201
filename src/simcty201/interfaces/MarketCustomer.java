package simcty201.interfaces;

import java.util.List;

import agents.Order;

public interface MarketCustomer {

	//from Employee
	public abstract void msgAskForCustomerOrder();

	//from Employee
	//public abstract void msgHereIsYourStuff(List<String> orderList);

	//from employee
	public abstract void msgHereIsYourOrderCharge(float charge);

	//from Employee
	public abstract void msgPaymentNoted();

	public abstract boolean pickAndExecuteAnAction();

	public abstract String getName();

	public abstract void msgLeaveEarly();

	public abstract void msgHereIsYourStuff(Order o);


}