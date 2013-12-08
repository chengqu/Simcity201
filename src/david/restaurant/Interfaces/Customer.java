package david.restaurant.Interfaces;

import david.restaurant.Check;
import david.restaurant.Menu;

public interface Customer {
	public abstract void BecomesHungry();
	public abstract void msgIsFull();
	public abstract void FollowMeToTable(Waiter w, Menu m);
	public abstract void msgDecidedOrder();
	public abstract void WhatWouldYouLike(Waiter w);
	public abstract void HereIsYourFood();
	public abstract void msgReorder(Menu m);
	public abstract void msgNoFood();
	public abstract void msgHereIsCheck(Check c);
}
