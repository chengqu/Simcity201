package david.restaurant.Test.Mock;

import java.util.ArrayList;
import java.util.List;

import david.restaurant.Check;
import david.restaurant.Menu;
import david.restaurant.Interfaces.Customer;
import david.restaurant.Interfaces.Waiter;

public class MockCustomer extends Mock implements Customer{
	public EventLog log = new EventLog();
	public List<Check> checks = new ArrayList<Check>();
	public MockCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void BecomesHungry() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " is hungry"));
	}

	@Override
	public void msgIsFull() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Restaurant is full"));
	}

	@Override
	public void FollowMeToTable(Waiter w, Menu m) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " can be seated"));
	}

	@Override
	public void msgDecidedOrder() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " has decided their food"));
	}

	@Override
	public void WhatWouldYouLike(Waiter w) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " can make a decision"));
	}

	@Override
	public void HereIsYourFood() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " received food"));
	}

	@Override
	public void msgReorder(Menu m) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " must reorder"));
	}

	@Override
	public void msgNoFood() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " no more food in restaurant"));
	}

	@Override
	public void msgHereIsCheck(Check c) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent(this.getName() + " received their check"));
		checks.add(c);
	}

}
