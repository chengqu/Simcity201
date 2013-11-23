package david.restaurant.Interfaces;

import david.restaurant.Bill;
import david.restaurant.Check;
import david.restaurant.WaiterAgent;

public interface Cashier {
	public abstract void msgProcessOrder(Waiter w, Customer c, String choice);
	public abstract void msgHereIsMoney(Check ch, float balance);
	public abstract void msgCantPay(Check ch);
	public abstract void msgHereIsBill(Bill b);
}
