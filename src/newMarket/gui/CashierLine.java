package newMarket.gui;

import java.util.List;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import newMarket.MarketAnimationPanel;
import newMarket.MarketCashierAgent;
import newMarket.MarketCustomerAgent;
import simcity201.gui.Gui;

public class CashierLine {
	
	private MarketCashierAgent agent = null;
	private List<MarketCustomerGui> customersWaiting;
	
	public CashierLine(MarketCashierAgent c) {
		agent = c;
    	customersWaiting = new ArrayList<MarketCustomerGui>();
    }
	
    public boolean isLineEmpty() {
    	return customersWaiting.isEmpty();
    }
    public int howManyInLine() {
    	return customersWaiting.size();
    }
    
    public Dimension waitInLine(MarketCustomerGui custGui) {
    	customersWaiting.add(custGui);
    	Dimension temp = new Dimension();
    	temp.width = agent.gui.onScreenHomeX - 20;
    	int length = customersWaiting.size();
    	temp.height = agent.gui.onScreenHomeY - (length * 30);
    	return temp;
    } 
}
