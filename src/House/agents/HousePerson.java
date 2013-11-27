	package House.agents;
	
	import House.gui.HouseGui;
	
	
	
import House.gui.HousePanelGui;
import House.gui.HousePersonPanel;
import House.interfaces.House;
import House.interfaces.Persontest;
import House.test.mock.LoggedEvent;
import House.test.mock.EventLog;

	import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
	
	
	import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
	








	import agent.Agent;
import agents.Grocery;
import agents.Person;
import agents.Task;
	
	public class HousePerson extends Agent implements House{
	        
	        /**
	         * Data
	         */
	        
	        public Person p;
	        
	        Random run = new Random();
	        boolean evicted = false;
	        
	        public enum StateHouse {hungry,readytocook,cooking,cooked,eating,full,nothing,rest,ReadytoPaybill,openlaptop,movingtotable,paying, store};
	        public StateHouse s = StateHouse.nothing;
	        public String choice;
	        public String name;
	        Timer timer = new Timer();
	        private Semaphore atTable = new Semaphore(0,true);
	        public HousePersonPanel panel;
	        //ApartmentOwner ao;
	        //ApartmentComplex apartmentComplex;
	        //Apartment apartment;
	        
	        
	        public HouseGui gui = null;
	        
	        

	        public EventLog log = new EventLog();
	        
	       
	       
	        
	        //constructor
	        public HousePerson(Person p, HousePersonPanel panel)//ApartmentComplex complex)
	        {
	        	this.p = p;
	        	this.panel = panel;
	        	
	        	/*
	                apartmentComplex = complex;
	                stateChanged();
	                */
	        }
	        
	        public HousePerson(String name, int sure)//ApartmentComplex complex)
	        {
	        	this.name = name;
	        	atTable = new Semaphore(sure,true);
	        	
	        	/*
	                apartmentComplex = complex;
	                stateChanged();
	                */
	        }
	        
	       
	        /*
	        public void setGui(HouseGui g)
	        {
	                gui = g;
	        }
	        
	        public void setApartment(House a)
	        {
	                House = a;
	        }
	        */
	        
	      


			public void doThings()
	        {
	                stateChanged();
	        }
	        
	        /**
	         * Messages
	         */
	        public void msgPayBills() {
	        	s = StateHouse.ReadytoPaybill;
	        	stateChanged();
	        }
	        
	        public void msgPayingbill() {
	        	s = StateHouse.openlaptop;
	        	stateChanged();
	        }
	        
	        
	        public void msgRestathome() {
	        	log.add(new LoggedEvent("sleep at home"));
	        	s = StateHouse.rest;
	        	stateChanged();
	        }
	        
	        public void msgIameatingathome()
	        { 		log.add(new LoggedEvent("eating at home"));
	                s = StateHouse.hungry;
	                stateChanged();
	        }
	        
	        public void msgdoneCooking()
	        {  
	        	s = StateHouse.cooked;
	        	stateChanged();
	        }
	        
	        public void msgstoreGroceries()
	        {
	        	log.add(new LoggedEvent("reveived store at home"));
	        	s = StateHouse.store;
	        	stateChanged();
	        }
	        
	        public void msgEvicted()
	        {
	                evicted = true;
	                stateChanged();
	        }
	        
	        
	        public void msgAtTable() {//from animation
	    		
	    		atTable.release();
	    		stateChanged();
	    	}	
	    	
	        /**
	         * Scheduler
	         */
	        
	        public boolean pickAndExecuteAnAction() {
	        	
	                if(s == StateHouse.hungry)
	                {
	                        Choosewhattoeat();
	                        return true;
	                }
	                
	                if(s == StateHouse.cooked) {
	                	Eat();
	                	return true;
	                }
	                
	                if(s == StateHouse.full) {
	                	MoveToRestPlace();
	                	return true;
	                }
	                
	                if(s == StateHouse.rest) {
	                	Sleep();
	                	return true;
	                }
	                
	                if(s == StateHouse.ReadytoPaybill) {
	                	doPayBills();
	                	return true;
	                }
	                
	                if(s == StateHouse.openlaptop) {
	                	openlaptop();
	                	return true;
	                }
	                
	                if(s == StateHouse.store) {
	                	doStore();
	                	return true;
	                }
	                /*
	                if(groceries.size() > 0)
	                {
	                        doStoreGroceries();
	                        return true;
	                }
	                if(p.hungerLevel < 20 /* && haveEnoughFood && haveTime)
	                {
	                        doCookAndEatFood();
	                        return true;
	                }
	                if(bills.size() > 0)
	                {
	                        doPayBills();
	                        return true;
	                }
	                */
	               
	                return false;
	        }
	
	        
	        //actions
	        private void Choosewhattoeat() {
	        	choice = "Steak";
	        	gui.doMoveToFridge();
	        	s = StateHouse.readytocook;
	        	try {
	    			atTable.acquire();
	    		} catch (InterruptedException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	        	timer.schedule(new TimerTask() {
	    			Object cookie = 1;
	    			public void run() {
	    				gui.doMoveToCookingArea();
	    	        	
	    	        	try {
	    	    			atTable.acquire();
	    	    		} catch (InterruptedException e) {
	    	    			// TODO Auto-generated catch block
	    	    			e.printStackTrace();
	    	    		}
	    	        	timer.schedule(new TimerTask() {
	    	    			Object cookie = 1;
	    	    			public void run() {
	    	    				print("Done cooking");
	    	    				msgdoneCooking();
	    	    				//isHungry = false;
	    	    				stateChanged();
	    	    			}
	    	    		},
	    	    		4000);
	    			}
	    		},
	    		1000);
	        	
	    		}
	      
	        private void Eat() {
	        	gui.doMovetoTable();
	        	s = StateHouse.eating;
	        	try {
	    			atTable.acquire();
	    		} catch (InterruptedException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	        	timer.schedule(new TimerTask() {
	    			Object cookie = 1;
	    			public void run() {
	    				print("Done eating");
	    				s = StateHouse.full;
	    				//isHungry = false;
	    				stateChanged();
	    			}
	    		},
	    		3000);
	        }
	
	        private void MoveToRestPlace() {
	        	gui.doMoveToRestPlace();
	        	try {
	    			atTable.acquire();
	    		} catch (InterruptedException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	        	s = StateHouse.nothing;
	        	p.msgDone();
	        	p.hungerLevel = 0;
	        	panel.deleteperson(this);
	        	
	        }
	        
	        private void Sleep() {
	        	final HousePerson p1 = this;
	        	gui.doMoveToBed();
	        
	        	timer.schedule(new TimerTask() {
	    			Object cookie = 1;
	    			public void run() {
	    				print("Done Sleeping");
	    				s= StateHouse.nothing;
	    				p.msgDone();
	    				panel.deleteperson(p1);
	    				
	    			}
	    		},
	    		4000);
	        }
	        private void doPayBills() {
	             gui.doMovetoLapTop();
	             s = StateHouse.movingtotable;
	             try {
	     			atTable.acquire();
	     		} catch (InterruptedException e) {
	     			// TODO Auto-generated catch block
	     			e.printStackTrace();
	     		}
	            msgPayingbill();
	             
	        }
	       
	        private void openlaptop() {
	        	s = StateHouse.paying;
	        	gui.drawLapTop();
	        	final HousePerson p1 = this;
	        	timer.schedule(new TimerTask() {
	    			Object cookie = 1;
	    			public void run() {
	    				s = StateHouse.nothing;
	    				gui.stopdrawLapTop();
	    				p.money-=20;
	    				p.msgDone();
	    				p.houseBillsToPay = 0;
	    				panel.deleteperson(p1);
	    			}
	    		},
	    		4000);
	        	
	        	
	        }
	
	       
	        private void doStore() {
	        	print("Finish store");
	        	s= StateHouse.nothing;
	        	print("Statechanged");
	        	panel.updatemap();
	        	print(""+panel.map2.get("Steak"));
	        	p.groceries.clear();
	        	print("" +p.groceries.size());
	        	p.msgDone();
	        	
	        	
	        	panel.deleteperson(this);      	

	        }
	
	      //
	        public void setGui(HouseGui gui) {
	    		this.gui = gui;
	    	}
	
	    	public HouseGui getGui() {
	    		return gui;
	    	}
	    	
	    	
	
	    	
	}