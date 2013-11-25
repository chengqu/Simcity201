package Market;
import java.util.*;

import agents.Person;

public class Order {
	
	//person who sent this initial order
	private MarketCustomerAgent customer;
	simcity201.interfaces.MarketInteraction market;
	
	//the data structure we use to communicate what is in this order
	//with outside agents
	//Map<String, Integer> travelingOrder = new HashMap<>();
	simcity201.interfaces.MarketInteraction.Order travelingOrder = 
			new simcity201.interfaces.MarketInteraction.Order();
	
	//internal list to use for managing orders...
	//comprised of internal class <Item>
	List<Item> completeOrder = new ArrayList<>();
	
	//constructor
	public Order(MarketCustomerAgent a, simcity201.interfaces.MarketInteraction m) {
		market = m;
		customer = a;
	}
	
	//constructor with item and amount of that item
	public Order(MarketCustomerAgent a, String item, int amount, simcity201.interfaces.MarketInteraction m) {
		market = m;
		customer = a;
		AddItemAndAmount(item, amount);
	}
	
	//private class
	private class Item {
		String item_;
		int count_;
		Item(String i, int c) {
			item_ = i;
			count_ = c;
		}
	}
	
	//add and item and amount of that item
	public void AddItemAndAmount(String item, int amount) {
		System.out.println("AddItemAndAmount in Order called");
		
		
		for (Item i : completeOrder) {
			if (item == i.item_) {
				i.count_ += amount;
				return;
			}
		}
		
		completeOrder.add(new Item(item, amount));
	}
	
	//this is how a receiver of the order reads the order
	public simcity201.interfaces.MarketInteraction.Order GiveMeTheWholeOrder() {
		
		for (Item i : completeOrder) {
			travelingOrder.foodList.add(i.item_);
			travelingOrder.foodAmounts.add(i.count_);
		}
		
		return travelingOrder;
	}
	
	//get who sent the order
	public MarketCustomerAgent getCustomer() {
		return customer;
	}
	
	//check if this is the same order
	public boolean isSameOrder(MarketCustomerAgent a, simcity201.interfaces.MarketInteraction.Order o) {
		
		if (a == customer) {
			if (o == travelingOrder)
				return true;
		}
		
		return false;
	}
	
}
