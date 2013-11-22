package josh.restaurant.interfaces;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cook {

	public abstract void msgMarketOrderDone(String choice_);

	public abstract void msgNeedToOrderFromBackup(Market marketAgent,
			String choice_);
	
	

}