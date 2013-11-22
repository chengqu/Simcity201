package LYN;


import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;


import java.util.TimerTask;







import LYN.CustomerAgent.AgentEvent;
import LYN.interfaces.Cashier;
import LYN.interfaces.Cook;
import LYN.interfaces.market;
import agent.Agent;

public class Market extends Agent implements market {

	private String name;	
	Timer timer = new Timer();
	private Cook cook;
	private Cashier cashier;
	
	
	
	
	public class Food {
		String choice;
		int amount;
		
		Food(String choice, int amount) {
			this.choice = choice;
			this.amount = amount;
		}
	}
	
	public class orderlist {
		public String choice;
		public status s;
		public int amount;
		
		orderlist(String choice, status s, int amount){
			this.choice = choice;
			this.s = s;
			this.amount = amount;
		}
	}
	enum status {get,doing,done,removed};
	public List<orderlist> orders
	= new ArrayList<orderlist>();	
	Map<String, Food> map2 = new HashMap<String, Food>();
	public Map<String, Double> map3 = new HashMap<String, Double>();
	Food f = new Food("",0);
	public Market(String name) {
		super();
		
		map2.put("Steak", new Food("Steak", 3));
		map2.put("Chicken", new Food("Chicken", 3));
		map2.put("Salad", new Food("Salad", 3));
		map2.put("Pizza", new Food("Pizza", 1));
		map3.put("Steak", 4.99);
		map3.put("Chicken", 3.99);
		map3.put("Salad", 2.99);
		map3.put("Pizza", 2.99);
		
		this.name = name;
		// make some tables
		
	}
	
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void setCook(CookAgent cook) {
		this.cook = cook;
	}
	
	public void setCashier(CashierAgent cashier) {
		this.cashier = cashier;
	}
	//message
	public void msgnotenoughmoney(){
		print("OK, cashier you can pay the bills together in your next order");
	}
	
	public void msghereisthebill(){
		print("Thank you for your payment, wish to serve you next time");
	}
	public void msgfullfillmyorder(Cook c, String Choice, int q) {
		orders.add(new orderlist(Choice,status.get,q));
		print("I will fullfill "+ Choice + q + " and after that I will have " + (map2.get(Choice).amount-q) + "left");
		map2.put(Choice,new Food(Choice,map2.get(Choice).amount-q));
	    if(map2.get(Choice).amount == 0) {
	    	print("I won't be able to supply " + Choice + " anymore");
	    }
		stateChanged();
	}
	
	public void msgfullfilldone(orderlist o){
		
		o.s = status.done;
		stateChanged();
		
	}
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		try {
		// TODO Auto-generated method stub
			for(orderlist o: orders) {
				if(o.s == status.get){
					fullfillit(o);
					return true;
				}
			}
			
			for(orderlist o: orders) {
				if(o.s == status.done){
					callcookfullfillready(o,map3.get(o.choice)*o.amount);
					return true;
				}
			}
		
		}catch (ConcurrentModificationException errorCCE) {
			return true;
		}
		return false;
	}
	
	//action
	public void fullfillit(final orderlist o){
		o.s = status.doing;
		
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			
			public void run() {
				print("Done fullfilling, cookie="  + cookie);
				
				msgfullfilldone(o);
				
				//isHungry = false;
				stateChanged();
			}
		},
		1000);
	}
	
	public void callcookfullfillready(orderlist o,double bill){
		o.s = status.removed;
	    cashier.msgpleasepaythebill(this,bill);
		cook.msgfullfilldone(o.choice);
	}
	
}