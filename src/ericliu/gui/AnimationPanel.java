package ericliu.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import animation.BaseAnimationPanel;


public class AnimationPanel extends BaseAnimationPanel implements ActionListener {
   
    public Object lock=new Object();

    private final int WINDOWX = 750;
    private final int WINDOWY = 850;
    private Image bufferImage;
    private Dimension bufferSize;
    private final int Table1fillrect1=200;
    private final int Table1fillrect2=250;
    private final int Table2fillrect1=200;
    private final int Table2fillrect2=350;
    private final int Table3fillrect1=300;
    private final int Table3fillrect2=250;
    private final int CashierTableX=300;
    private final int CashierTableY=60;
    private final int CashierTablefillrectX=200;
    private final int CashierTablefillrectY=50;
    private final int fillrect3=50;
    private final int fillrect4=50;
    private final int CustomerWaitingAreaX=20;
    private final int CustomerWaitingAreaY=40;
    private final int CustomerWaitingAreafillrectX=80;
    private final int CustomerWaitingAreafillrectY=140;
    private final int WaiterWaitingAreaX=20;
    private final int WaiterWaitingAreaY=400;
    private final int WaiterWaitingAreafillrectX=80;
    private final int WaiterWaitingAreafillrectY=80;
    private final int CookAreaX=540;
    private final int CookAreaY=340;
    private final int CookAreafillrectX=140;
    private final int CookAreafillrectY=140;
    private final int kitchenAreaX=590;
    private final int kitchenAreaY=480;
    private final int kitchenAreafillrectX=90;
    private final int kitchenAreafillrectY=50;
    private final int platingAreaX=520;
    private final int platingAreaY=360;
    private final int platingAreafillrectX=20;
    private final int platingAreafillrectY=60;
    private final int refrigeratorX=620;
    private final int refrigeratorY=320;
    private final int refrigeratorfillrectX=60;
    private final int refrigeratorfillrectY=20;
    
    
    class Coordinates{
       int xPos;
       int yPos;
    }
    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
      setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
      Timer timer = new Timer(3, this );
      timer.start();

    }

   public void actionPerformed(ActionEvent e) {
      ///synchronized(lock){
         for(Gui gui : guis) {
            
                gui.updatePosition();
            
        }
      //}
      
      repaint();  //Will have paintComponent called
   }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, this.getWidth(), this.getHeight() );

        
        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(Table1fillrect1, Table1fillrect2, fillrect3, fillrect4);//200 and 250 need to be table params

        g2.setColor(Color.ORANGE);
        g2.fillRect(Table2fillrect1, Table2fillrect2, fillrect3, fillrect4);

        g2.setColor(Color.ORANGE);
        g2.fillRect(Table3fillrect1, Table3fillrect2, fillrect3, fillrect4);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(CashierTableX,CashierTableY, CashierTablefillrectX, CashierTablefillrectY);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(CustomerWaitingAreaX,CustomerWaitingAreaY,CustomerWaitingAreafillrectX,CustomerWaitingAreafillrectY);
        
        g2.setColor(Color.BLACK);
        g2.drawString("Customer Waiting Area", 20, 20);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(WaiterWaitingAreaX, WaiterWaitingAreaY, WaiterWaitingAreafillrectX, WaiterWaitingAreafillrectY);
        
        g2.setColor(Color.BLACK);
        g2.drawString("Waiter Waiting Area", 20, 380);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(CookAreaX,CookAreaY,CookAreafillrectX,CookAreafillrectY);
        
        g2.setColor(Color.BLACK);
        g2.drawString("Cook Area", 540,320);
        
        g2.setColor(Color.BLUE);
        g2.fillRect(kitchenAreaX, kitchenAreaY, kitchenAreafillrectX, kitchenAreafillrectY);
        g2.setColor(Color.BLACK);
        g2.drawString("Cooking Area", kitchenAreaX-90, kitchenAreaY+40);
        
        g2.setColor(Color.GREEN);
        g2.fillRect(platingAreaX, platingAreaY, platingAreafillrectX, platingAreafillrectY);
        g2.setColor(Color.BLACK);
        g2.drawString("Plating Area", platingAreaX-60, platingAreaY-10);
        
        g2.setColor(Color.BLACK);
        g2.fillRect(refrigeratorX,refrigeratorY,refrigeratorfillrectX,refrigeratorfillrectY);
        g2.drawString("Refrigerator", refrigeratorX, refrigeratorY-10);
        
        //synchronized(lock){
           for(Gui gui : guis) {
               if (gui.isPresent()) {
                   gui.draw(g2);
               }
           }
        //}
    }

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(WaiterGui gui) {
       guis.add(gui);
   }
   
   public void addGui(CashierGui gui){
      guis.add(gui);
   
   }
   
   public void addGui(CookGui gui){
      guis.add(gui);
   }
   
   public Dimension getSize(){
      return new Dimension(WINDOWX,WINDOWY+50);
   }
}
