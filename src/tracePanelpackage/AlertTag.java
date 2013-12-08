package tracePanelpackage;


/**
 * These enums represent tags that group alerts together.  <br><br>
 * 
 * This is a separate idea from the {@link AlertLevel}.
 * A tag would group all messages from a similar source.  Examples could be: BANK_TELLER, RESTAURANT_ONE_WAITER,
 * or PERSON.  This way, the trace panel can sort through and identify all of the alerts generated in a specific group.
 * The trace panel then uses this information to decide what to display, which can be toggled.  You could have all of
 * the bank tellers be tagged as a "BANK_TELLER" group so you could turn messages from tellers on and off.
 * 
 * @author Keith DeRuiter
 *
 */
public enum AlertTag {
	PERSON,
	BANK_TELLER,
	BANK_CUSTOMER,
	BANK_Security,
	BANK,
	ApartmentPerson,
	HousePerson,
	Trans,
	StopAgent,
	TruckAgent,
	BusAgent,
	CarAgent,
	BankATM,
	PassengerAgent,
	Market,
	MarketCashier,
	MarketCustomer,
	MarketDealer,
	MarketRestaurantHandler,
	LYN,
	LYNCustomer,
	LYNCook,
	LYNCashier,
	LYNWaiter,
	LYNhost,
	Josh,
	JoshCustomer,
	JoshCook,
	JoshCashier,
	JoshWaiter,
	Joshhost,
	Ryan,
	RyanCustomer,
	RyanCook,
	RyanCashier,
	RyanWaiter,
	Ryanhost,
	David,
	DavidCustomer,
	DavidCook,
	DavidWaiter,
	Davidhost,
	DavidCashier,
	Eric,
	EricCook,
	EricCustomer,
	EricWaiter,
	EricCashier,
	Erichost,
	Ross,
	RossCustomer,
	RossCook,
	RossWaiter,
	RossCashier,
	Rosshost
	
}
