package agents;
import java.util.*;

public class Order {
	
	Person sender;
	
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
	
	public List<Item> GiveMeTheWholeOrder() {
		return completeOrder;
	}
	
}
