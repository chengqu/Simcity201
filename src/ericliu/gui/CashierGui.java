package ericliu.gui;


import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.HostAgent;
import ericliu.restaurant.WaiterAgent;
import ericliu.restaurant.CookAgent;
import ericliu.restaurant.CashierAgent;

import java.awt.*;

public class CashierGui implements Gui {

    private CashierAgent agent = null;
    
    RestaurantGui gui;
    
    private int xPos = 390, yPos = 40;//default waiter position
    private int xDestination = 200, yDestination = 50;//default start position

    public static final int xTable1 = 200;
    public static final int yTable1 = 250;
    public static final int xTable2 = 200;
    public static final int yTable2 = 350;
    public static final int xTable3 = 300;
    public static final int yTable3 = 250;
    

    private boolean flag = false;
    private boolean isPresent=false;

     

    
    

    public CashierGui(CashierAgent cashier, RestaurantGui gui){ //HostAgent m) {
       agent = cashier;
       xPos = 200;
       yPos = 50;
       xDestination = 200;
       yDestination = 50;
       //maitreD = m;
       this.gui = gui;
    }
    
    public CashierGui(CashierAgent agent) {
        this.agent = agent;
    }

    public void updatePosition() {
//        if (xPos < xDestination)
//            xPos++;
//        else if (xPos > xDestination)
//            xPos--;
//
//        if (yPos < yDestination)
//            yPos++;
//        else if (yPos > yDestination)
//            yPos--;
//
//        if (xPos == xDestination && yPos == yDestination
//            & (xDestination == xTable1 + 20) & (yDestination == yTable1 - 20)) {
//           agent.msgAtTable();
//        }
//        
//        if (xPos == xDestination && yPos == yDestination
//              & (xDestination == xTable2 + 20) & (yDestination == yTable2 - 20)) {
//             agent.msgAtTable();
//          }
//        
//        if (xPos == xDestination && yPos == yDestination
//              & (xDestination == xTable3 + 20) & (yDestination == yTable3 - 20)) {
//             agent.msgAtTable();
//          }
//        
//        if (xPos == xDestination && yPos == yDestination
//              & (xDestination == -20) & (yDestination == -20) && flag) {
//             agent.msgAtStart();
//             flag = false;
//          }
//        if ((xPos != -20) || (yPos != -20))
//           flag = true;
    }

    public void setPresent(boolean p) {
       isPresent = p;
    }
    
    public boolean isPresent() {
       return true;
   }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(xPos, yPos, 20, 20);
        g.drawString("CASHIER", 373, 90);
 
    }




    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public int getXDestination() {
       return xDestination;
   }

   public int getYDestination() {
       return yDestination;
   }
    
    
}
