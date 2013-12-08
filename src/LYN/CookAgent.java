package LYN;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;




import java.util.TimerTask;




import java.util.concurrent.Semaphore;

import simcity201.gui.GlobalMap;
import simcity201.interfaces.NewMarketInteraction;
import tracePanelpackage.AlertLog;
import tracePanelpackage.AlertTag;
import LYN.gui.CookGui;
import LYN.gui.WaiterGui;
import LYN.interfaces.Cook;
import LYN.interfaces.Waiter;
import LYN.interfaces.market;
import agent.Agent;
import agents.Grocery;
import agents.Person;
import agents.Worker;

public class CookAgent extends Agent implements Cook, NewMarketInteraction,Worker{	

	public CookGui cookGui = null;
	private Semaphore atTable = new Semaphore(0,true);
	private CashierAgent cashier = null;

	public class Order {
		Waiter w;
		String choice;
		int table;
		State s;


		Order(Waiter w, String choice, int table, State s){
			this.w = w;
			this.table = table;
			this.choice = choice;
			this.s = s;

		}


	}

	private class markets {
		market market;
		Map<String, Double> marketinventory = new HashMap<String,Double>();

		markets(market market){
			this.market = market;
			marketinventory.put("Steak", (double)3);
			marketinventory.put("Chicken", (double)3);
			marketinventory.put("Salad", (double)3);
			marketinventory.put("Pizza", (double)1);	
		}
	}

	public class Food {
		public String choice;
		public int amount;

		Food(String choice, int amount) {
			this.choice = choice;
			this.amount = amount;
		}
	}

	public Map<String, Food> map2 = new HashMap<String, Food>();

	private List<Order> orders
	= new ArrayList<Order>();	
	private List<markets> market1 = new ArrayList<markets>();
	enum State  {pending,cooking,done};	
	public String name;	
	boolean runoutofmarket = false;
	Timer timer = new Timer();
	public float cashiercheck = 0;

	Map<String , Double> map1 = new HashMap<String , Double>();
	Food f = new Food("",0);	
	enum any {withfood, withoutfood};
	Map<String, any> mapstate = new HashMap<String, any>();
	enum state1 {none,cooking};
	public Map<String, state1> mapstate1 = new HashMap<String, state1>();
	public Person p = null;
	public boolean isWorking;
	public CookAgent(String name, CashierAgent cashier) {
		super();
		map1.put("Steak", (double)(5000));
		map1.put("Chicken",(double)7000);
		map1.put("Salad", (double)6000);
		map1.put("Pizza", (double)8000);
		map2.put("Steak", new Food("Steak", 0));
		map2.put("Chicken", new Food("Chicken", 0));
		map2.put("Salad", new Food("Salad", 0));
		map2.put("Pizza", new Food("Pizza", 0));
		if(map2.get("Steak").amount == 0){
			mapstate.put("Steak", any.withoutfood);
		} else {
			mapstate.put("Steak", any.withfood);
		}		
		if(map2.get("Chicken").amount == 0){
			mapstate.put("Chicken", any.withoutfood);
		} else {
			mapstate.put("Chicken", any.withfood);
		}
		if(map2.get("Salad").amount == 0){
			mapstate.put("Salad", any.withoutfood);
		} else {
			mapstate.put("Salad", any.withfood);
		}

		if(map2.get("Pizza").amount == 0){
			mapstate.put("Pizza", any.withoutfood);
		} else {
			mapstate.put("Pizza", any.withfood);
		}

		mapstate1.put("Steak", state1.none);
		mapstate1.put("Chicken", state1.none);
		mapstate1.put("Salad", state1.none);
		mapstate1.put("Pizza", state1.none);
		this.cashier = cashier;
		this.name = name;
		// make some tables

	}


	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	//message
	public void msgAtTable() {//from animation

		atTable.release();
		stateChanged();
	}	


	public void msgaddmarket(market m){
		market1.add(new markets(m));
	}

	public void msgpaybills(float check) {
		cashiercheck = check;
		stateChanged();
	}

	/*
	public void msgfullfilldone(String choice) {
		mapstate.put(choice, any.withfood) ;
		map2.put(choice, new Food(choice,2));
		runoutofmarket = false;
	}
	 */
	public void msgHereisanOrder(Waiter w, String choice, int table){
		AlertLog.getInstance().logMessage(AlertTag.LYNCook, this.name,"Receving message here is an order");
		AlertLog.getInstance().logMessage(AlertTag.LYN, this.name,"Receving message here is an order");

		orders.add(new Order(w,choice,table,State.pending));

		stateChanged();

	}

	public void msgFooddone(Order o){
		o.s = State.done;
		stateChanged();
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		try {
			if(this.p == null){
				return false;
			}

			if(this.isWorking == false) {
				p.msgDone();
				this.p = null;
				return false;
			}


			if(cashiercheck!= 0) {
				paybills();
				return true;
			}
			for (Order o : orders) {
				if (o.s == State.pending) {
					Cookit(o);  
					return true;
				}
			}

			if(map2.get("Steak").amount == 0){
				mapstate.put("Steak", any.withoutfood);
			}
			if(map2.get("Chicken").amount == 0){
				mapstate.put("Chicken", any.withoutfood);
			}
			if(map2.get("Salad").amount == 0){
				mapstate.put("Salad", any.withoutfood);
			}
			if(map2.get("Pizza").amount == 0){
				mapstate.put("Pizza", any.withoutfood);
			}

			for (Order o : orders) {
				if (o.s == State.done) {
					Plateit(o); 
					return true;
				}
			}
		} catch (ConcurrentModificationException errorCCE) {
			return true;
		}
		// TODO Auto-generated method stub
		return false;

	}


	public void paybills() {
		GlobalMap.getGlobalMap().marketHandler.msgHereIsMoney(this, cashiercheck);
		cashiercheck = 0;
	}
	public void Cookit(final Order o){
		//docooking(o);
		o.s = State.cooking;
		f = map2.get(o.choice);
		if(f.amount == 0) {
			List<Grocery> g = new ArrayList<Grocery>();
			int amount = 2;
			mapstate.put(o.choice, any.withoutfood) ;
			AlertLog.getInstance().logMessage(AlertTag.LYNCook, this.name,"Running out of food, we need 2 more "+ o.choice);
			AlertLog.getInstance().logMessage(AlertTag.LYN, this.name,"Running out of food, we need 2 more "+ o.choice);

			o.w.msgRunoutoffood(o.choice, o.table);
			g.add(new Grocery(o.choice,amount));
			GlobalMap.getGlobalMap().marketHandler.msgIWantFood(this, g);
			/*
		    for (int i = 0; i<market1.size(); i++){
		    	if(market1.get(i).marketinventory.get(o.choice) >= amount){
		    		  market1.get(i).market.msgfullfillmyorder(this,o.choice,amount);
		    		  market1.get(i).marketinventory.put(o.choice, market1.get(i).marketinventory.get(o.choice)-amount);
		    		  amount = 0;
		    		  runoutofmarket = true;
		              break;
		    	} else if(market1.get(i).marketinventory.get(o.choice) < amount && market1.get(i).marketinventory.get(o.choice) > 0) {
		    		amount = amount - market1.get(i).marketinventory.get(o.choice).intValue();

		    		 market1.get(i).market.msgfullfillmyorder(this,o.choice,market1.get(i).marketinventory.get(o.choice).intValue());
		    		 market1.get(i).marketinventory.put(o.choice,(double)0);
		    		 runoutofmarket = true;
		    	}

		    }
		  if(runoutofmarket == false){
			  print("no more market for "+o.choice);
		  }
			 */
			orders.remove(o);
		}
		else {
			mapstate.put(o.choice, any.withfood) ;
			long l = (map1.get(o.choice)).longValue();

			cookGui.movetorefrigerator();
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mapstate1.put(o.choice, state1.cooking);
			cookGui.movetocookposition();

			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			timer.schedule(new TimerTask() {
				Object cookie = 1;
				public void run() {

					cookGui.movetoplaceposition();
					try {
						atTable.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msgFooddone(o);
				}

			},
			l);
			f.amount--;}
	}


	public void Plateit(Order o) {
		//doPlate(o);
		//o.setWaiter(w);

		o.w.msgOrderisReady(o.choice,o.table);
		mapstate1.put(o.choice, state1.none);
		orders.remove(o);
		cookGui.rest();

	}

	//

	public void setGui(CookGui gui) {
		cookGui = gui;
	}

	public CookGui getGui() {
		return cookGui;
	}

	public boolean returnsteak() {
		if(mapstate.get("Steak").equals(any.withoutfood)){
			return false;
		} else {
			return true;
		}
	}


	public boolean returnchicken() {
		if(mapstate.get("Chicken").equals(any.withoutfood)){
			return false;
		} else {
			return true;

		}
	}


	public boolean returnpizza() {
		if(mapstate.get("Pizza").equals(any.withoutfood)){
			return false;
		} else {
			return true;
		}
	}


	public boolean returnsalad() {
		if(mapstate.get("Salad").equals(any.withoutfood)){
			return false;
		} else {
			return true;
		}
	}

	public boolean returnsteak1() {
		if(mapstate1.get("Steak").equals(state1.none)){
			return false;
		} else {
			return true;
		}
	}


	public boolean returnchicken1() {
		if(mapstate1.get("Chicken").equals(state1.none)){
			return false;
		} else {
			return true;
		}
	}


	public boolean returnpizza1() {
		if(mapstate1.get("Pizza").equals(state1.none)){
			return false;
		} else {
			return true;
		}
	}


	public boolean returnsalad1() {
		if(mapstate1.get("Salad").equals(state1.none)){
			return false;
		} else {
			return true;
		}
	}


	@Override
	public void msgHereIsPrice(List<Grocery> orders, float price) {
		cashier.msgHereIsPrice(orders, price);

	}


	@Override
	public void msgHereIsFood(List<Grocery> orders) {
		// TODO Auto-generated method stub
		mapstate.put(orders.get(0).getFood(), any.withfood) ;
		map2.put(orders.get(0).getFood(), new Food(orders.get(0).getFood(),orders.get(0).getAmount()));

	}


	@Override
	public void msgNoFoodForYou() {
		// TODO Auto-generated method stub

	}


	@Override
	public void msgfullfilldone(String choice) {
		// TODO Auto-generated method stub

	}


	@Override
	public void setTimeIn(int timeIn) {
		// TODO Auto-generated method stub

	}


	@Override
	public int getTimeIn() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void goHome() {
		// TODO Auto-generated method stub

	}


	@Override
	public Person getPerson() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void msgLeave() {
		// TODO Auto-generated method stub
		isWorking = false;
		stateChanged();
	}





}