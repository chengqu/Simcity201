package Market;
import java.util.*;

import agents.Person;

public class Order {
	
	//person who sent this initial order
	private Person sender;
	
	//the data structure we use to communicate what is in this order
	//with outside agents
	Map<String, Integer> travelingOrder = new HashMap<>();
	
	//internal list to use for managing orders...
	//comprised of internal class <Item>
	List<Item> completeOrder = new ArrayList<>();
	
	//constructor
	public Order(Person s) {
		sender = s;
	}
	
	//constructor with item and amount of that item
	public Order(Person s, String item, int amount) {
		sender = s;
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
		
		for (Item i : completeOrder) {
			if (item == i.item_) {
				i.count_ += amount;
				return;
			}
		}
		
		completeOrder.add(new Item(item, amount));
	}
	
	//this is how a receiver of the order reads the order
	public Map<String, Integer> GiveMeTheWholeOrder() {
		
		for (Item i : completeOrder) {
			travelingOrder.put(i.item_, i.count_);
		}
		
		return travelingOrder;
	}
	
	//get who sent the order
	public Person getSender() {
		return sender;
	}
	
	//check if this is the same order
	public boolean isSameOrder(Person p, Map<String, Integer> map) {
		
		if (p == sender) {
			if (map == travelingOrder)
				return true;
		}
		
		return false;
	}
	
}
