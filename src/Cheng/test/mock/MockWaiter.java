package Cheng.test.mock;


import java.util.List;

import agents.Person;
import Cheng.CashierAgent;
import Cheng.CookAgent;
import Cheng.CustomerAgent;
import Cheng.HostAgent;
import Cheng.gui.WaiterGui;
import Cheng.interfaces.Cashier;
import Cheng.interfaces.Customer;
import Cheng.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements Waiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockWaiter(String name) {
		super(name);

	}

	@Override
	public void setCashier(CashierAgent c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(HostAgent h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCook(CookAgent cook) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getMyCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgSeatCustomer(CustomerAgent c, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantFood(CustomerAgent c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyOrder(CustomerAgent c, String Choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFoodReady(String Choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantToPay(CustomerAgent c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingTable(CustomerAgent c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIsorigin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCustomer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(String Choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void IWantBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OffBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(WaiterGui gui, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WaiterGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTimeIn(int timeIn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTimeIn() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void goHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Person getPerson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgLeave() {
		// TODO Auto-generated method stub
		
	}


	/*@Override
	public void HereIsYourTotal(double total) {
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

		if(this.name.toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			cashier.IAmShort(this, 0);

		}else if (this.name.toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.HereIsMyPayment(this, Math.ceil(total));

		}else{
			//test the normative scenario
			cashier.HereIsMyPayment(this, total);
		}
	}

	@Override
	public void HereIsYourChange(double total) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
	}

	@Override
	public void YouOweUs(double remaining_cost) {
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
	}*/

}
