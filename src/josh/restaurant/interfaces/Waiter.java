package josh.restaurant.interfaces;

import josh.restaurant.CashierAgent;
import josh.restaurant.CookAgent;
import josh.restaurant.CustomerAgent;
import josh.restaurant.CashierAgent.Bill;
import josh.restaurant.CookAgent.OrderIcon;

public interface Waiter {

	//from HostAgent
	public abstract void msgSitAtTable(CustomerAgent c, int table);

	//from cashier 
	public abstract void msgHereIsTheBill(Bill b);

	public abstract void msgLeavingEarly(CustomerAgent c);

	//from CustomerAgent
	public abstract void msgImReadyToOrder(CustomerAgent c);

	public abstract void msgHereIsMyChoice(CustomerAgent c, String choice);

	public abstract void msgDoneEatingAndLeaving(CustomerAgent c);

	//from CookAgent
	public abstract void msgBadOrder(String choice, int table);

	public abstract void msgOrderIsReady(String choice, int table, OrderIcon i);

	public abstract void msgBreakRequestAnswer(boolean answer);

	public abstract void gui_msgAtTable();

	public abstract void gui_msgBackAtHomeBase();

	public abstract void gui_msgAtCook();

	public abstract void pause();

	public abstract void resume();

	public abstract String getName();

}