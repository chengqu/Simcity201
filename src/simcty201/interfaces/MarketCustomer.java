package simcty201.interfaces;

import java.util.List;

public interface MarketCustomer {

	/**
	 *  MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES MESSAGES 
	 */

	//from Employee
	public abstract void msgAskForCustomerOrder();

	//from Employee
	public abstract void msgHereIsYourStuff(List<String> orderList);

	//from employee
	public abstract void msgHereIsYourOrderCharge(float charge);

	//from Employee
	public abstract void msgPaymentNoted();

	/**
	 *  SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER SCHEDULER
	 */

	public abstract boolean pickAndExecuteAnAction();

	public abstract String getName();

	public abstract void msgLeaveEarly();


}