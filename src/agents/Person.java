package agents;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;

public class Person extends Agent{

	/**
	 * DATA
	 */
	public float money;
	public float paycheck;
	public int hungerLevel;
	int age;
	String name;
	
	/*
	 * Insert car and bus (or bus stop) agents here
	 * add gui here also for walking
	 */
	
	enum PersonState{needBank, needRestaurant, needHome, needStore,
				needWork, waiting, inAction, decide, moving, none};
				
	enum PersonEvent{none, done, atDest};
	
	PersonEvent frontEvent = PersonEvent.none;
	PersonState currentState = PersonState.none;
	
	List<PersonEvent> events = new ArrayList<PersonEvent>();
	List<Role> roles = new ArrayList<Role>();
	List<Task> tasks = new ArrayList<Task>();
	
	//locks
	Object eventLock = new Object();
	Object stateLock = new Object();
	Object taskLock = new Object();
	
	/**
	 * MESSAGES
	 */
	
	public void msgDone()
	{
		synchronized(eventLock)
		{
			events.add(PersonEvent.done);
			frontEvent = PersonEvent.done;
		}
		stateChanged();
	}
	
	public void msgAtDest()
	{
		synchronized(eventLock)
		{
			events.add(PersonEvent.atDest);
			frontEvent = PersonEvent.atDest;
		}
		stateChanged();
	}
	
	protected boolean pickAndExecuteAnAction() {
		synchronized(taskLock)
		{
			if(currentState == PersonState.needRestaurant)
			{
				Task task = null;
				for(Task t: tasks)
				{
					if(t.getObjective() == Task.Objective.goTo)
					{
						task = t;
						break;
					}
				}
				if(task != null)
				{
					goToRestaurant(task);
					return true;
				}
			}
			
			if(currentState == PersonState.needBank)
			{
				Task task = null;
				for(Task t: tasks)
				{
					if(t.getObjective() == Task.Objective.goTo)
					{
						task = t;
						break;
					}
				}
				if(task != null)
				{
					goToBank(task);
					return true;
				}
			}
			
			if(currentState == PersonState.needStore)
			{
				Task task = null;
				for(Task t: tasks)
				{
					if(t.getObjective() == Task.Objective.goTo)
					{
						task = t;
						break;
					}
				}
				if(task != null)
				{
					goToStore(task);
					return true;
				}
			}
			
			if(currentState == PersonState.needHome)
			{
				Task task = null;
				for(Task t: tasks)
				{
					if(t.getObjective() == Task.Objective.goTo)
					{
						task = t;
						break;
					}
				}
				if(task != null)
				{
					goToHome(task);
					return true;
				}
			}
			synchronized(eventLock)
			{
				if(currentState == PersonState.moving && frontEvent == PersonEvent.atDest)
				{
					Enter();
					return true;
				}
				if(currentState == PersonState.inAction && frontEvent == PersonEvent.done)
				{
					Decide();
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * ACTIONS
	 */
	
	private void goToRestaurant(Task t)
	{
		currentState = PersonState.moving;
		tasks.remove(t);
		/*
		 * Make call to correct form of transportation
		 * need car, bus, etc for this. pass t.location
		 * to the vehicle (or something like that)
		 */
	}
	
	private void goToBank(Task t)
	{
		currentState = PersonState.moving;
		tasks.remove(t);
		/*
		 * Make call to correct form of transportation
		 * need car, bus, etc for this. pass t.location
		 * to the vehicle (or something like that)
		 */
	}
	
	private void goToStore(Task t)
	{
		currentState = PersonState.moving;
		tasks.remove(t);
		/*
		 * Make call to correct form of transportation
		 * need car, bus, etc for this. pass t.location
		 * to the vehicle (or something like that)
		 */
	}
	
	private void goToHome(Task t)
	{
		currentState = PersonState.moving;
		tasks.remove(t);
		/*
		 * Make call to correct form of transportation
		 * need car, bus, etc for this. pass t.location
		 * to the vehicle (or something like that)
		 */
	}
	
	private void Enter()
	{
		currentState = PersonState.inAction;
		Task t = tasks.get(0);
		if(t.getObjective() == Task.Objective.patron)
		{
			/*
			 * get building from map, call "addCustomer(this)"
			 */
		}
		else if(t.getObjective() == Task.Objective.worker)
		{
			/*
			 * get building from map, call "addWorker(this)"
			 */
		}
		else if(t.getObjective() == Task.Objective.master)
		{
			/*
			 * get building from map, call "addHomeOwner(this)"
			 */
		}
		else
		{
			//something has gone wrong if we get here
		}
	}
	
	private void Decide()
	{
		
	}
}
