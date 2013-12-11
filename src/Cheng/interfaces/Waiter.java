package Cheng.interfaces;

import java.util.List;

import Cheng.CashierAgent;
import Cheng.CookAgent;
import Cheng.CustomerAgent;
import Cheng.HostAgent;
import Cheng.gui.WaiterGui;
import agents.Person;


public interface Waiter {
	public void setCashier(CashierAgent c);

	public void setHost(HostAgent h);

	public void setCook(CookAgent cook);

	public String getMaitreDName();

	public String getName();

	public List getMyCustomers();

	public void msgSeatCustomer(CustomerAgent c, int table);

	public void msgIWantFood(CustomerAgent c);

	public void msgHereIsMyOrder(CustomerAgent c, String Choice);

	public void msgFoodReady(String Choice, int table);

	public void msgIWantToPay(CustomerAgent c);

	public void msgLeavingTable(CustomerAgent c);

	public void msgAtTable();

	public void msgIsorigin();

	public void msgAtCook();

	public void msgAtCashier();

	public void msgAtCustomer();

	public void msgOutOfFood(String Choice, int table);

	public void msgOnBreak();

	public void IWantBreak();

	public void OffBreak();

	public void setGui(WaiterGui gui, int count);

	public WaiterGui getGui();

	public void setFood();

	public void setTimeIn(int timeIn);

	public int getTimeIn();

	public void goHome();

	public Person getPerson();

	public void msgLeave();


}