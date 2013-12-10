package ericliu.gui;


import ericliu.interfaces.Waiter;
import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.HostAgent;
import ericliu.restaurant.WaiterAgent;
import ericliu.restaurant.CookAgent;

import java.awt.*;

public class WaiterGui implements Gui {

    private Waiter agent;
    
    RestaurantGui gui;
    
    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    public int startNumber;
    
    public static final int xTable1 = 200;
    public static final int yTable1 = 250;
    public static final int xTable2 = 200;
    public static final int yTable2 = 350;
    public static final int xTable3 = 300;
    public static final int yTable3 = 250;
    public static final int CashierX=390;
    public static final int CashierY=110;
    public static final int readyX=110;
    public static final int readyY=190;
    public static final int start1x=30;
    public static final int start1y=410;
    public static final int start2x=60;
    public static final int start2y=410;
    public static final int start3x=30;
    public static final int start3y=440;
    public static final int start4x=60;
    public static final int start4y=440;
    public static final int orderFoodX=520;
    public static final int orderFoodY=460;
    public static final int pickUpFoodX=500;
    public static final int pickUpFoodY=380;
    
    private boolean isPresent = false;
    private boolean isWorking = false;
    private boolean isOnBreak = false;
    private boolean goingToTable=false;

    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean flag3 = false;
    private boolean flag4 = false;
    private boolean orderFlag=false;
    
    private boolean deliveringFood=false;
    private String customerOrder;
     

    
    

    public WaiterGui(Waiter w, RestaurantGui gui){ //HostAgent m) {
       agent = w;
       xPos = -20;
       yPos = 400;
       xDestination = -20;
       yDestination = 400;
       //maitreD = m;
       this.gui = gui;
    }
    
    public WaiterGui(Waiter agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (xPos == xDestination && yPos == yDestination
            & (xDestination == xTable1 + 20) & (yDestination == yTable1 - 20)) {
           agent.msgAtTable();
        }
        
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == xTable2 + 20) & (yDestination == yTable2 - 20)) {
             agent.msgAtTable();
          }
        
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == xTable3 + 20) & (yDestination == yTable3 - 20)) {
             agent.msgAtTable();
          }
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == CashierX) & (yDestination == CashierY)) {
             agent.msgAtCashier();
          }
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == readyX) & (yDestination == readyY) ) {
             agent.msgAtReady();
            
          }
        
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == orderFoodX) & (yDestination == orderFoodY) && orderFlag ) {
             agent.msgAtOrder();
             orderFlag=false;
          }
        
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == pickUpFoodX) & (yDestination == pickUpFoodY)  ) {
             agent.msgAtPickUp();
          }

        if (xPos == xDestination && yPos == yDestination
              & (xDestination == start1x) & (yDestination == start1y) && flag1) {
              agent.msgAtStart();
              flag1 = false;
          }
        
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == start2x) & (yDestination == start2y) && flag2) {
             agent.msgAtStart();
             flag2 = false;
          }
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == start3x) & (yDestination == start3y) && flag3) {
             agent.msgAtStart();
             flag3 = false;
          }
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == start4x) & (yDestination == start4y) && flag4) {
             agent.msgAtStart();
             flag4 = false;
          }
        
        if ((xPos != start1x) || (yPos != start1y))
           flag1 = true;
        if ((xPos != start2x) || (yPos != start2y))
           flag2 = true;
        if ((xPos != start3x) || (yPos != start3y))
           flag3 = true;
        if ((xPos != start4x) || (yPos != start4y))
           flag4 = true;
        
        if((xPos!=orderFoodX) || (yPos!=orderFoodY))
           orderFlag=true;
    }

    public void setPresent(boolean p) {
       isPresent = p;
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString(agent.getName(), xPos+20, yPos);
        if(deliveringFood){
           g.setColor(Color.BLACK);
           g.drawString(customerOrder, xPos, yPos);
        }
    }

    public void drawCustomerOrder(String choice){
       deliveringFood=true;
       customerOrder=choice;
    }
    
    public void undrawCustomerOrder(){
       deliveringFood=false;
    }
    public boolean isPresent() {
        return true;
    }

    public void DoGoToReady(){
       xDestination=readyX;
       yDestination=readyY;

    }
    public void DoGoToTable(CustomerAgent customer, int tableNumber) {
        //goingToTable=true;
       
        if(tableNumber==1)
        {
           
           xDestination = xTable1 + 20;
           yDestination = yTable1 - 20;
           //System.out.println("Waiter going to table.");
        }
        
        else if(tableNumber==2)
        {
           xDestination = xTable2 + 20;
           yDestination = yTable2 - 20;
        }
        
        else if(tableNumber==3)
        {
           xDestination = xTable3 + 20;
           yDestination = yTable3 - 20;
        }

    }

    public void DoGoToStart(int startNumber){
       if(startNumber==1)
       {
          xDestination = start1x;
          yDestination = start1y;
       }
       if(startNumber==2){
          xDestination = start2x;
          yDestination = start2y;
      }
       if(startNumber==3){
          xDestination = start3x;
          yDestination = start3y;
      }
       if(startNumber==4){
          xDestination = start4x;
          yDestination = start4y;
       }
    }
    
    public void DoGoToOrder(){
       xDestination=orderFoodX;
       yDestination=orderFoodY;

    }
    
    public void DoGoToPickUp(){
       xDestination=pickUpFoodX;
       yDestination=pickUpFoodY;

    }
    
    public void DoGoToCashier(){
       xDestination=CashierX;
       yDestination=CashierY;

    }
    public void DoLeaveCustomer() {
       System.out.println("Waiter leaving table.");
       deliveringFood=false;
       if(startNumber==1){
           xDestination = start1x;
           yDestination = start1y;
       }
       if(startNumber==2){
          xDestination = start2x;
          yDestination = start2y;
      }
       if(startNumber==3){
          xDestination = start3x;
          yDestination = start3y;
      }
       if(startNumber==4){
          xDestination = start4x;
          yDestination = start4y;
      }
    }

    public void setWorking(boolean working) {
       if(working){
          isWorking = true;
          agent.msgStartWorking();
          setPresent(true);
       }
       else{
          isWorking=false;
          setPresent(false);
       }
    }
    
    public void setBreak(boolean onBreak) {
       if(onBreak){
          isOnBreak = true;
          agent.msgWantToGoOnBreak();
          setPresent(false);
       }
       else{
          isOnBreak = false;
          
          isWorking=true;
          agent.msgStartWorking();
          setPresent(true);
       }
    }
    
    public void leaveRestaurant(){
       xDestination=20;
       yDestination=600;
       System.out.println("\nLeaving Restaurant \n");
    }
    public boolean onBreak(){
       return isOnBreak;
    }
    public boolean isWorking() {
       return isWorking;
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
    
   public void setStart(int startNumber){
      this.startNumber=startNumber;
   }
}
