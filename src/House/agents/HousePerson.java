package House.agents;

import House.gui.HouseGui;



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

public class HousePerson extends Agent{
        
        /**
         * Data
         */
        
        public Person p;
        
        Random run = new Random();
        boolean evicted = false;
        State s;
        enum State {hungry,readytocook,cooking,cooked,eating,full,nothing,rest,ReadytoPaybill,openlaptop,movingtotable,paying};
        String choice;
        String name;
        Timer timer = new Timer();
        private Semaphore atTable = new Semaphore(0,true);
        //ApartmentOwner ao;
        //ApartmentComplex apartmentComplex;
        //Apartment apartment;
        
        
        public HouseGui gui = null;
        public List<Grocery> groceries = new ArrayList<Grocery>();
        
        //constructor
        public HousePerson(Person p)//ApartmentComplex complex)
        {
        	this.p = p;
        	map2.put("Steak", new Fridge("Steak", 0));
    		map2.put("Chicken", new Fridge("Chicken", 0));
    		map2.put("Salad", new Fridge("Salad", 0));
    		map2.put("Pizza", new Fridge("Pizza", 0));
        	/*
                apartmentComplex = complex;
                stateChanged();
                */
        }
        
        Map<String, Fridge> map2 = new HashMap<String, Fridge>();
        private class Fridge {
    		public String choice;
    		public int amount;
    		
    		Fridge(String choice, int amount) {
    			this.choice = choice;
    			this.amount = amount;
    		}
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
        	s = State.ReadytoPaybill;
        	stateChanged();
        }
        
        public void msgPayingbill() {
        	s = State.openlaptop;
        	stateChanged();
        }
        
        
        public void msgRestathome() {
        	s = State.rest;
        	stateChanged();
        }
        
        public void msgIameatingathome()
        {
                s = State.hungry;
                stateChanged();
        }
        
        public void msgdoneCooking()
        {  
        	s = State.cooked;
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
        
        protected boolean pickAndExecuteAnAction() {
                if(s == State.hungry)
                {
                        Choosewhattoeat();
                        return true;
                }
                
                if(s == State.cooked) {
                	Eat();
                	return true;
                }
                
                if(s == State.full) {
                	MoveToRestPlace();
                	return true;
                }
                
                if(s == State.rest) {
                	Sleep();
                	return true;
                }
                
                if(s == State.ReadytoPaybill) {
                	doPayBills();
                	return true;
                }
                
                if(s == State.openlaptop) {
                	openlaptop();
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
        	s = State.readytocook;
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
    	        	s = State.cooking;
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
        	s = State.eating;
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
    				s = State.full;
    				//isHungry = false;
    				stateChanged();
    			}
    		},
    		4000);
        }

        private void MoveToRestPlace() {
        	gui.doMoveToRestPlace();
        	try {
    			atTable.acquire();
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	s = State.nothing;
        	p.msgDone();
        	
        }
        
        private void Sleep() {
        	gui.doMoveToBed();
        	s= State.nothing;
        	timer.schedule(new TimerTask() {
    			Object cookie = 1;
    			public void run() {
    				print("Done Sleeping");
    				p.msgDone();
    				
    			}
    		},
    		4000);
        }
        private void doPayBills() {
             gui.doMovetoLapTop();
             s = State.movingtotable;
             try {
     			atTable.acquire();
     		} catch (InterruptedException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}
            msgPayingbill();
             
        }
        
        private void openlaptop() {
        	s = State.paying;
        	gui.drawLapTop();
        	timer.schedule(new TimerTask() {
    			Object cookie = 1;
    			public void run() {
    				s = State.nothing;
    				gui.stopdrawLapTop();
    				p.msgDone();
    			}
    		},
    		4000);
        	
        	
        }

       
        private void doStoreGroceries() {
                
        }

      //
        public void setGui(HouseGui gui) {
    		this.gui = gui;
    	}

    	public HouseGui getGui() {
    		return gui;
    	}
    	
    	public List<Grocery> returngroceries() {
    		for (String key : map2.keySet()) {
    			if(map2.get(key).amount == 0) {
    				groceries.add(new Grocery(key,2));
    			}
    		}
    		return groceries;
    	}

    	
}