package agents;

public class Role {
	/**
	 * @author Ryan (Gueho) Choi
	 * I have added Robbery, TellerAtChaseBank
	 */
	public enum roles{Robbery, WorkerTellerAtChaseBank, WorkerSecurityAtChaseBank,
				AptOwner, ApartmentOwner, ApartmentRenter,
				JonnieWalker, houseRenter, houseOwner, WorkermarketManager, preferHomeEat, preferBus,
				preferCar, WorkerLYNWaiter,WorkerLYNCook, WorkerLYNHost, WorkerLYNCashier, WorkerRyanWaiter,WorkerRyanCook, WorkerRyanHost, WorkerRyanCashier,
				WorkerDavidWaiter,WorkerDavidCook, WorkerDaividhost, WorkerDavidCashier,WorkerEricWaiter,WorkerEricCook, WorkerEricHost, WorkerEricCashier,
				WorkerJoshWaiter,WorkerJoshCook, WorkerJoshHost, WorkerJoshCashier,WorkerRossWaiter,WorkerRossCook, WorkerRossHost, WorkerRossCashier};
	private roles role;
	private String location;
	
	public Role(roles r, String l)
	{
		role = r;
		location = l;
	}
	
	public roles getRole()
	{
		return role;
	}
	
	public String getLocation()
	{
		return location;
	}
}
