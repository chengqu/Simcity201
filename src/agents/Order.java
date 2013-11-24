package agents;
import java.util.*;

public class Order {
	
	private Person sender;
	
	Map<String, Integer> travelingOrder = new HashMap<>();
	List<Item> completeOrder = new ArrayList<>();
	
	public Order(Person s) {
		sender = s;
	}
	
	public Order(Person s, String item, int amount) {
		sender = s;
		AddItemAndAmount(item, amount);
	}
	
	private class Item {
		String item_;
		int count_;
		Item(String i, int c) {
			item_ = i;
			count_ = c;
		}
	}
	
	public void AddItemAndAmount(String item, int amount) {
		
		for (Item i : completeOrder) {
			if (item == i.item_) {
				i.count_ += amount;
				return;
			}
		}
		
		completeOrder.add(new Item(item, amount));
	}
	
	public Map<String, Integer> GiveMeTheWholeOrder() {
		
		for (Item i : completeOrder) {
			travelingOrder.put(i.item_, i.count_);
		}
		
		return travelingOrder;
	}
	
	public Person getSender() {
		return sender;
	}
	
	public boolean isSameOrder(Person p, Map<String, Integer> map) {
		
		if (p == sender) {
			if (map == travelingOrder)
				return true;
		}
		
		return false;
	}
	
}
