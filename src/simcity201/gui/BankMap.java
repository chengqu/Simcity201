package simcity201.gui;

import java.awt.Point;
import java.util.*;


/**
 * @author Gueho (Ryan) Choi
 *	BankMap.java:
 * It contains all the constants needed for the animation
 */
public class BankMap {
	/* Entrance: E, Teller: T, Line: L, ATM: A, Security: S, Customer: C 
	 --------------------------------------------------------------------
	 |											S						|
	 |													C	T			|
	 |	A																|
	 |														T			|
	 |	A								L								|
	 |									L				C	T			|
	 |	A								L								|
	 |									L					T			|
	 |									L								|
	 |									L					T			|
	 |									L								|
	 |									L					T			|
	 |									L								|
	 |		->	L	L	L	L	L	L	L					T			|
	 |																	|
	 |E																	|
	 --------------------------------------------------------------------
	 */
	
	private final static int WINDOWX = BankAnimationPanel.WINDOWX;
	private final static int WINDOWY = BankAnimationPanel.WINDOWY;
	
	public final static Point ENTRANCE = new Point(0, WINDOWY);
	
	// number of rows and columns
	private final static int ROW_ATM = 3;
	private final static int ROW_LINE = 10;
	private final static int COL_LINE = 5;
	private final static int ROW_TELLER = 7;
	
	private enum PointState { available, taken }
	private class MyTellerPos{
		Point point;
		PointState s;
		BankTellerGui g;
		
		MyTellerPos(Point point, PointState s) {
			this.point = point;
			this.s = s;
		}
	}
	private List<MyTellerPos> tellerPositions = 
			Collections.synchronizedList(new ArrayList<MyTellerPos>());
	
	private class MyCustomerPos {
		Point point;
		PointState s;
		BankCustomerGui g;
		
		MyCustomerPos(Point point, PointState s) {
			this.point = point;
			this.s = s;
		}
	}
	private List<MyCustomerPos> customerPositions = 
			Collections.synchronizedList(new ArrayList<MyCustomerPos>());
	
	synchronized public Point getTellerPosition(BankTellerGui g) {
		for(MyTellerPos p : tellerPositions) {
			if(p.s == PointState.available) {
				p.s = PointState.taken;
				p.g = g;
				return p.point;
			}
		}
		// there is no more available position
		return new Point(WINDOWX-30, 25);
	}
	synchronized public void positionAvailable(BankTellerGui g) {
		for(MyTellerPos p : tellerPositions) {
			if (p.g.equals(g) && p.s == PointState.taken) {
				p.s = PointState.available;
				p.g = null;
				break;
			}
		}
	}
	synchronized public Point getCustomerPosition(BankCustomerGui g) {
		for(MyCustomerPos p : customerPositions) {
			if(p.s == PointState.available) {
				p.s = PointState.taken;
				p.g = g;
				return p.point;
			}
		}
		// there is no more available position
		return new Point(WINDOWX-30, 25);
	}
	synchronized public void positionAvailable(BankCustomerGui g) {
		for(MyCustomerPos p : customerPositions) {
			if (p.g.equals(g) && p.s == PointState.taken) {
				p.s = PointState.available;
				p.g = null;
				break;
			}
		}
	}
	synchronized public Point getTellerWindow(BankTellerGui g) {
		for(MyTellerPos p : tellerPositions) {
			if (p.g.equals(g) && p.s == PointState.taken) {
				p.point.x -= 50;
				return p.point;
			}
		}
		
		// should not get here
		return new Point(WINDOWX-30, 25);
	}
	
	
	public BankMap() {
		int xStartLine = (WINDOWX / 2) + 30; 
		int yStartLine = (WINDOWY / 2) - 100;
		int rowOffset = 45;
		int colOffset = 45;
		for(int i=0; i<ROW_LINE; i++) {	// Create the rows of the line
			customerPositions.add(new MyCustomerPos(new Point(xStartLine, yStartLine + (i*rowOffset)), PointState.available));
		}
		for(int i=0; i<COL_LINE; i++) {
			customerPositions.add(new MyCustomerPos(new Point(xStartLine - (i+1)*colOffset, yStartLine + (ROW_LINE-1)*rowOffset), PointState.available));
		}
		
		int tellerX = WINDOWX - 50;
		int tellerY = WINDOWY / (ROW_TELLER + 1);
		int tellerRowOffset = 60;
		for(int i=0; i<ROW_TELLER; i++) {
			tellerPositions.add(new MyTellerPos(new Point(tellerX, tellerY + (i*tellerRowOffset)), PointState.available));
		}
	}
	
}
