package agents;

import gui.HouseGui;

import java.util.ArrayList;
import java.util.List;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import agent.Agent;

public class HousePerson extends Agent{
        
        /**
         * Data
         */
        
        public Person p;
        List<String> groceries; //this is going to be a part of the person 
        Random run = new Random();
        boolean evicted = false;
        State s;
        enum State {hungry,readytocook,cooking,cooked,eating,full,nothing,rest};
        String choice;
        String name;
        Timer timer = new Timer();
        private Semaphore atTable = new Semaphore(0,true);
        //ApartmentOwner ao;
        //ApartmentComplex apartmentComplex;
        //Apartment apartment;
        
        
        public HouseGui gui = null;
        
        //constructor
        public HousePerson(Person p)//ApartmentComplex complex)
        {
        	this.p = p;
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
        	
        }
        
        private void Sleep() {
        	gui.doMoveToBed();
        	s= State.nothing;
        }
        private void doPayBills() {
                
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
       

}