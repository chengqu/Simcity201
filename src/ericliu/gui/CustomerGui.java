package ericliu.gui;

import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.HostAgent;

import java.awt.*;

public class CustomerGui implements Gui{

   private CustomerAgent agent = null;
   private boolean isPresent = false;
   private boolean isHungry = false;

   //private HostAgent host;
   RestaurantGui gui;

   private int xPos, yPos;
   private int xDestination, yDestination;
   private enum Command {noCommand, GoToSeat, LeaveRestaurant};
   private Command command=Command.noCommand;

   private boolean flag=false; 
   private boolean atReadyFlag=false;
   
   public static final int xTable = 200;
   public static final int yTable = 250;
   public static final int CashierX=390;
   public static final int CashierY=110;
   public static final int readyX=90;
   public static final int readyY=170;
   public static final int seat1x=200;
   public static final int seat1y=250;
   public static final int seat2x=200;
   public static final int seat2y=350;
   public static final int seat3x=300;
   public static final int seat3y=250;

   public static final int waitingSeat1x=20;
   public static final int waitingSeat1y=40;
   public static final int waitingSeat2x=60;
   public static final int waitingSeat2y=40;
   public static final int waitingSeat3x=20;
   public static final int waitingSeat3y=70;
   public static final int waitingSeat4x=60;
   public static final int waitingSeat4y=70;
   public static final int waitingSeat5x=20;
   public static final int waitingSeat5y=100;
   public static final int waitingSeat6x=60;
   public static final int waitingSeat6y=100;
   public static final int waitingSeat7x=20;
   public static final int waitingSeat7y=130;
   public static final int waitingSeat8x=60;
   public static final int waitingSeat8y=130;
   
   private boolean decidingOrder=false;
   private boolean Ordered=false;
   private String order;
   
   
   public CustomerGui(CustomerAgent c, RestaurantGui gui){ //HostAgent m) {
      agent = c;
      xPos = -20;
      yPos = -20;
      xDestination = -20;
      yDestination = -20;
      //maitreD = m;
      this.gui = gui;
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

      if (xPos == xDestination && yPos == yDestination) {
         if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
         else if (command==Command.LeaveRestaurant) {
            agent.msgAnimationFinishedLeaveRestaurant();
            System.out.println("about to call gui.setCustomerEnabled(agent);");
            isHungry = false;
            gui.setCustomerEnabled(agent);
         }
         command=Command.noCommand;
      }
      
      if (xPos == xDestination && yPos == yDestination
            & (xDestination == CashierX) & (yDestination == CashierY)) {
           agent.msgAtCashier();
        }
      
      if (xPos == xDestination && yPos == yDestination
            & (xDestination == readyX) & (yDestination == readyY) && flag) {
           agent.msgAtReady();
           flag=false;
           
      }
      
      if(xPos!=90 && yPos!=170){
         flag=true;
      }
      
   }

   public void draw(Graphics2D g) {
      g.setColor(Color.GREEN);
      g.fillRect(xPos, yPos, 20, 20);
      if(decidingOrder|| Ordered){
         g.setColor((Color.BLACK));
         g.drawString(order, xPos, yPos);
      }
   }

   public boolean isPresent() {
      return isPresent;
   }
    
   public void drawDecidingOrderMark(){
      decidingOrder=true;
      order="?";
   }

   public void drawOrderTaken(String choice){
      decidingOrder=false;
      Ordered=true;
      order=choice+"?";
   }
   
   public void drawReceivedOrder(String choice){
      order=choice;
   }
   
   public void undrawOrder(){
      decidingOrder=false;
      Ordered=false;
   }
   public void setPresent(boolean p) {
      isPresent = p;
   }

   public boolean isHungry(){
      return isHungry;
   }
   public void setHungry() {
      isHungry = true;
      agent.msgGotHungry();
      setPresent(true);
   }
   
   public void DoGoToReady(){
      xDestination=readyX;
      yDestination=readyY;
   }
   
   public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
      if(seatnumber==1)
      {
         xDestination = seat1x;
         yDestination = seat1y;
      }
      
      if(seatnumber==2)
      {
         xDestination = seat2x;
         yDestination = seat2y;
      }
      
      if(seatnumber==3)
      {
         xDestination = seat3x;
         yDestination = seat3y;
      }
      command = Command.GoToSeat;
   }
   
   public void DoGoToWaitingSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
      if(seatnumber==1)
      {
         xDestination = waitingSeat1x;
         yDestination = waitingSeat1y;
      }
      
      if(seatnumber==2)
      {
         xDestination = waitingSeat2x;
         yDestination = waitingSeat2y;
      }
      
      if(seatnumber==3)
      {
         xDestination = waitingSeat3x;
         yDestination = waitingSeat3y;
      }
      
      if(seatnumber==4)
      {
         xDestination = waitingSeat4x;
         yDestination = waitingSeat4y;
      }
      
      if(seatnumber==5)
      {
         xDestination = waitingSeat5x;
         yDestination = waitingSeat5y;
      }
      
      if(seatnumber==6)
      {
         xDestination = waitingSeat6x;
         yDestination = waitingSeat6y;
      }
      
      if(seatnumber==7)
      {
         xDestination = waitingSeat7x;
         yDestination = waitingSeat7y;
      }

      if(seatnumber==8)
      {
         xDestination = waitingSeat8x;
         yDestination = waitingSeat8y;
      }
//      command = Command.GoToSeat;
   }

   public void DoGoToCashier(){
      xDestination=CashierX;
      yDestination=CashierY;
   }
   
   public void DoExitRestaurant() {
      xDestination = -40;
      yDestination = -40;
      command = Command.LeaveRestaurant;
   }
}
