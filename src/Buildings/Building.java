
package Buildings;

import animation.BaseAnimationPanel;

public abstract class Building {
	public String name;
	public int x, y;
	public int width, height;
	
	public abstract BaseAnimationPanel getAnimationPanel();
}

