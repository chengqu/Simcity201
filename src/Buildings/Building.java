package Buildings;

import agents.Person;
import agents.Role;
import animation.BaseAnimationPanel;

public abstract class Building {
	public String name;
	public int x, y;
	public int width, height;
	
	public abstract BaseAnimationPanel getAnimationPanel();
	
	public abstract Role wantJob(Person p);
	
	public enum Type { Restaurant, Market, Bank, Apartment, House};
	public Type type;
}
