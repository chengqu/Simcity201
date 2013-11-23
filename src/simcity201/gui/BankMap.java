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
		
		MyTellerPos(Point point, PointState s, BankTellerGui g) {
			this.point = point;
			this.s = s;
			this.g= g;
		}
	}
	List<MyTellerPos> tellerPositions = new ArrayList<MyTellerPos>();
	
	private class MyCustomerPos {
		Point point;
		PointState s;
		BankTellerGui g;
		
		MyCustomerPos(Point point, PointState s, BankTellerGui g) {
			this.point = point;
			this.s = s;
			this.g = g;
		}
	}
	List<MyCustomerPos> customerPositions = new ArrayList<MyCustomerPos>();
	
	public BankMap() {
		
	}
	
}
