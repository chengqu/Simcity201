package LYN;

import agent.Agent;











import agents.Grocery;

import java.util.*;
import java.util.concurrent.Semaphore;

import simcity201.gui.GlobalMap;
import simcity201.interfaces.NewMarketInteraction;
import LYN.interfaces.Cashier;
import LYN.interfaces.Customer;
import LYN.interfaces.market;
import LYN.test.mock.EventLog;
import LYN.test.mock.LoggedEvent;



/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CashierAgent extends Agent  implements Cashier, NewMarketInteraction{
	
	public List<MyCustomer> customers
	=  Collections.synchronizedList(new ArrayList<MyCustomer>());
	
	/*
	public List<MyMarket> markets
	= Collections.synchronizedList(new ArrayList<MyMarket>());
	*/
    
	public class MyCustomer{
    	public Customer c;
    	public String choice;
    	public double check;
    	public double money;
    	public State s;
    	MyCustomer(Customer c, String choice, double check,State s, double money){
    		this.c = c;
    		this.s = s;
    		this.choice = choice;
    		this.check = check;
    		this.money = money;
    
    	}
    	
		
    }
	
	/*
	
	public class MyMarket{
		public market m;
		public double bill;
		public billstate bs;
		MyMarket(market m, double bill, billstate bs){
		   this.m = m;
		   this.bill = bill;
		   this.bs = bs;
		}
	}
	*/
	public enum State {eating,readytocheck,afterreadytocheck,notenoughmoney,paynexttime,checking,checked};
	public enum billstate {money, nomoney, nothing,paynexttime};
	billstate s = billstate.nothing;

	Map<String , Double> map1 = new HashMap<String , Double>();
	String name;
	public EventLog log = new EventLog();
	public double money = 100;
	public float check = 0;
	private CookAgent cook;
	
	public CashierAgent(String name) {
		super();
		map1.put("Steak", (double)(15.99));
		map1.put("Chicken",(double)10.99);
		map1.put("Salad", (double)5.99);
		map1.put("Pizza", (double)8.99);
		
		
		this.name = name;
		// make some tables
		
	}
	
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void setcook(CookAgent cook) {
		this.cook = cook;
	}
	
	//message
	/*
	public void msgaddmarket(market m){
		markets.add(new MyMarket(m,0,billstate.nothing));
	}
	*/
	public void msghereisthebill(Customer c, String choice){
		boolean previous = false;
		for (MyCustomer c1: customers){
			if (c1.c == c && c1.s == State.paynexttime){
				print("You need to pay your previous bill(s)" + c.getName());
				c1.s = State.eating;
				c1.choice = choice;
				c1.check = c1.check + map1.get(choice);
				c1.money = 0;
				previous = true;
			}
		}
		if(previous == false) {
		customers.add(new MyCustomer(c,choice,map1.get(choice),State.eating,0));
		}
		stateChanged();
		
	}
	
	public void msgpleasepaythebill(market m, double bill){
		/*
		log.add(new LoggedEvent("reveived bill"));
		print("Here is my bill for the order: " + bill);
		for(MyMarket m1:markets){
			if(m1.m == m){
				if(m1.bs == billstate.paynexttime){
					print("Cashier needs to pay previous bill(s): "+ m1.bill);
				      if(money>=m1.bill + bill){
					
						
						m1.bs = billstate.money;
						m1.bill += bill;
					} else {
					 m1.bs= billstate.nomoney;
					 m1.bill = m1.bill + bill;
					}
				} else {
					if(money< bill){
						m1.bs = billstate.nomoney;
						m1.bill = bill;
					} else{
					m1.bs = billstate.money;
					m1.bill = bill;
					}
				}
			}
			
		}
		
		stateChanged();
		*/
	}
	
	public void msgcustomercheck(Customer c){
		log.add(new LoggedEvent("Received customer ready to pay"));
		for (MyCustomer c1: customers){
			if (c1.c == c){
				print("Checking customer" + c1.c);
				c1.s = State.readytocheck;
				stateChanged();
			}
		}
	}
	
	public void msgcustomernotenoughmoney(Customer c, double money, double Check) {
		log.add(new LoggedEvent("Received customer's money not enough"));
		for (MyCustomer c1: customers){
			if (c1.c == c){
				print("OK, you can pay next time" + c1.c + money);
				c1.s = State.notenoughmoney;
				c1.money = money;
				stateChanged();
			}
		}
	}
	public void msghereismoney(Customer c, double check, double money){
		log.add(new LoggedEvent("Received customer's money"));
		for (MyCustomer c1: customers){
			if (c1.c == c){
				print("Receiving check" + c);
				c1.s = State.checking;
				c1.money = money;
				stateChanged();
			}
		}
	}
	
	//
	
	public boolean pickAndExecuteAnAction() {
	
	if(s == billstate.money) {
		paythebill();
	}
		/*
		synchronized(markets) {
			for(MyMarket m1: markets){
				if(m1.bs == billstate.nomoney) {
					paynexttime(m1);
					return true;
				}
			}
		
			for(MyMarket m1:markets){
				if(m1.bs == billstate.money) {
					paythebill(m1);
					return true;
				}
			}
		}
		*/
		
		synchronized(customers) {
			for (MyCustomer c: customers){
				if (c.s == State.readytocheck){
					checkcustomer(c);
				
					return true;
				}
			}
			for (MyCustomer c: customers){
				if (c.s == State.notenoughmoney){
					paynexttime(c);
				
					return true;
				}
			}
		
			for (MyCustomer c: customers){
				if (c.s == State.checking){
					givechangetoCustomer(c);
				
					return true;
				}
			}
		}
		return false;
		
	}
	
	//action
	/*
	private void paynexttime(MyMarket m) {
		print("Cashier doesn't have enough money");
		m.m.msgnotenoughmoney();
		m.bs = billstate.paynexttime;
	}
	*/
	
	private void paythebill() {
		print("Cashier is paying the bill, after that cashier will have: " + (money-check) + " left");
		cook.msgpaybills(check);
		
		s = billstate.nothing;
		money -= check;
		/*
		m.m.msghereisthebill();
		m.bs = billstate.nothing;
		money = money - m.bill;
		*/
	}
	private void checkcustomer(MyCustomer c) {
		print("giving check to customer"+c.check);
		c.c.msghereisyourbill(c.check);
		c.s = State.afterreadytocheck;
	}
	
	private void paynexttime(MyCustomer c) {
	    c.s = State.paynexttime;
	    print(" " + (c.money - c.check));
	    c.c.msghereisyourchange(c.money - c.check);
	}
	private void givechangetoCustomer(MyCustomer c) {
		print("giving change to custoemr");
		c.c.msghereisyourchange(c.money - c.check);
		money += c.check;
		print("Cashier now have: " + money);
		c.s = State.checked;
	}


	@Override
	public void msgHereIsPrice(List<Grocery> orders, float price) {
		log.add(new LoggedEvent("reveived bill"));
		print("Here is my bill for the order: " + price);
		s = billstate.money;
		check = price;
		stateChanged();
		/*
		for(MyMarket m1:markets){
			if(m1.m == m){
				if(m1.bs == billstate.paynexttime){
					print("Cashier needs to pay previous bill(s): "+ m1.bill);
				      if(money>=m1.bill + bill){
					
						
						m1.bs = billstate.money;
						m1.bill += bill;
					} else {
					 m1.bs= billstate.nomoney;
					 m1.bill = m1.bill + bill;
					}
				} else {
					if(money< bill){
						m1.bs = billstate.nomoney;
						m1.bill = bill;
					} else{
					m1.bs = billstate.money;
					m1.bill = bill;
					}
				}
			}
		
		*/
	}


	@Override
	public void msgHereIsFood(List<Grocery> orders) {
		// TODO Auto-generated method stub
		//cook.msgHereIsFood(orders);
		
	}


	@Override
	public void msgNoFoodForYou() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgaddmarket(market m) {
		// TODO Auto-generated method stub
		
	}


	
}