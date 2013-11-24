package ericliu.gui;


import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.HostAgent;
import ericliu.restaurant.WaiterAgent;
import ericliu.restaurant.CookAgent;
import ericliu.restaurant.CashierAgent;

import java.awt.*;
import java.awt.List;
import java.util.*;
import java.util.concurrent.Semaphore;

public class CookGui implements Gui {

    private CookAgent agent = null;
    
    RestaurantGui gui;
    
    private int xPos = 620, yPos = 400;//default waiter position
    private int xDestination = 620, yDestination = 400;//default start position

    public static final int xTable1 = 200;
    public static final int yTable1 = 250;
    public static final int xTable2 = 200;
    public static final int yTable2 = 350;
    public static final int xTable3 = 300;
    public static final int yTable3 = 250;
    public static final int refrigeratorX=640;
    public static final int refrigeratorY=340;
    public static final int platingX=540;
    public static final int platingY=380;
    public static final int cookingX=620;
    public static final int cookingY=460;
    public static final int startX=620;
    public static final int startY=400;
    

    private boolean flag = false;
    private boolean cookingFlag=false;
    private boolean isPresent=false;
    
    private boolean cookingFood=false;
    private boolean orderPickedUp=false;
    private String customerOrder;
//    private java.util.List<String> cookedOrders=Collections.synchronizedList(new ArrayList<String>());
   // private ArrayList<String> cookedOrders=new ArrayList<String>();
     

    
    

    public CookGui(CookAgent cook, RestaurantGui gui){ //HostAgent m) {
       agent = cook;
       xPos = 620;
       yPos = 400;
       xDestination = 620;
       yDestination = 400;
       //maitreD = m;
       this.gui = gui;
    }
    
    public CookGui(CookAgent agent) {
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
            & (xDestination == platingX) & (yDestination == platingY) ) {
           agent.msgAtPlating();
           
        }
        
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == cookingX) & (yDestination == cookingY) && cookingFlag) {
             agent.msgAtCooking();
             cookingFlag=false;
          }
        
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == refrigeratorX) & (yDestination == refrigeratorY)) {
             agent.msgAtFridge();
          }
        
        if (xPos == xDestination && yPos == yDestination
              & (xDestination == startX) & (yDestination == startY) && flag) {
             agent.msgAtStart();
             flag = false;
          }
        if ((xPos != startX) || (yPos != startY))
           flag = true;
        
        if((xPos!=cookingX) || (yPos!=cookingY))
           cookingFlag=true;
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
        g.drawString(agent.getName(), xPos+20, yPos);
        if(cookingFood){
           g.setColor(Color.BLACK);
           g.drawString(customerOrder, xPos, yPos+30);
        }
//        if(!orderPickedUp && cookedOrder!=null)
//           g.drawString(cookedOrder, platingX, platingY);
//        if(!cookedOrders.isEmpty()){
//           for(String order:cookedOrders){
//              g.drawString(order, platingX, platingY);
//           }
//           
//        }
           
 
    }

    public void drawCustomerOrder(String choice){
       cookingFood=true;
       customerOrder=choice;
    }
    
    public void undrawCustomerOrder(){
       cookingFood=false;
    }
    
//    public void drawOrder(String customerOrder){
//
//       cookedOrders.add(customerOrder);
//
//    }
//    
//    public void undrawOrder(String customerOrder){
//       cookedOrders.remove(customerOrder);
//    }
    
    public void goToStart(){
       xDestination=startX;
       yDestination=startY;
    }
    
    public void goToFridge(){
       xDestination=refrigeratorX;
       yDestination=refrigeratorY;
       
    }

    public void goToCookArea(){
       xDestination=cookingX;
       yDestination=cookingY;
    }
    
    public void goToPlateArea(){
       xDestination=platingX;
       yDestination=platingY;
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
