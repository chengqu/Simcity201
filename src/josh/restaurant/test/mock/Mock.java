package josh.restaurant.test.mock;

/**
 * This is the base class for all mocks.
 *
 * @author Sean Turner
 *
 */
public class Mock {
	private String name;
	public EventLog log;

	public Mock(String name) {
		this.setName(name);
		this.log = new EventLog();
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return this.getClass().getName() + ": " + getName();
	}

	public void setName(String name) {
		this.name = name;
	}

}
