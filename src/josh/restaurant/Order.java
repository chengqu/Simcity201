package josh.restaurant;

import josh.restaurant.CookAgent.OrderIcon;
import josh.restaurant.interfaces.Waiter;

public class Order {
	Waiter w_; 
	String choice_;
	int tableNum_;
	boolean finished_;
	OrderIcon i_; 
	public Order(Waiter w, String choice, int tableNum) {
		w_ = w;
		choice_ = choice;
		tableNum_ = tableNum;
		finished_ = false; 
	}
}