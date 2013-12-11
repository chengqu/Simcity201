package david.restaurant.Interfaces;

import agents.Person;
import david.restaurant.Check;
import david.restaurant.CookAgent;
import david.restaurant.CustomerAgent;
import david.restaurant.Order;
import david.restaurant.gui.Gui;
import david.restaurant.gui.WaiterGui;

public interface Waiter {

	public void print_();

	public void setGui(WaiterGui g);

	public void setCook(CookAgent c);

	public void msgDoneShift();

	//messages from customer, host, and cook. can call stateChanged
	public void msgPleaseSitCustomer(CustomerAgent c, int t);

	public void msgImReadyToOrder(CustomerAgent c);

	public void msgHereIsMyChoice(CustomerAgent c, String choice);

	public void msgDoneEatingAndLeaving(CustomerAgent c);

	public void msgOrderIsReady(Order o);

	//gui messages. can't call stateChanged 
	public void msgDoneSeating();

	public void msgAtCashier();

	public void msgCanStartSeating();

	public void msgCanTakeOrder();

	public void msgAtCook();

	public void msgGoOnBreak();

	public void msgStopBreak();

	public void msgOkBreak();

	public void msgNoBreak();

	public void msgNotAvailable(Order o);

	public void msgHereIsCheck(Check c);

	//scheduler
	public boolean pickAndExecuteAnAction();

	//helpers
	public Gui getGui();

	public String getName();

	public void pause();

	public void resume();

	public void goHome();
}
