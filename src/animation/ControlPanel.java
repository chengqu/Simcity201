package animation;


/* Import your restaurant here */
import guehochoi.gui.AnimationPanel;
import guehochoi.gui.RestaurantGui;
import guehochoi.gui.RestaurantPanel;






import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.*;

import agents.Person;
import ericliu.restaurant.CashierAgent;
import ericliu.restaurant.CookAgent;
import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.HostAgent;
import ericliu.restaurant.MarketAgent;
import ericliu.restaurant.WaiterAgent;
import ericliu.gui.CashierGui;
import ericliu.gui.CookGui;
import ericliu.gui.CustomerGui;
import ericliu.gui.HostGui;
import ericliu.gui.ListPanel;
import ericliu.gui.WaiterGui;
import ericliu.gui.WaiterListPanel;

public class ControlPanel extends JPanel implements ActionListener,MouseMotionListener, MouseListener{

   
    boolean black = false;
    private  float alpha = 0f;
    private Timer timer;
    
    private int SIZEX=500;
    private int SIZEY=500;
    int count = 0;
    //private SimcityGui simcitygui = new SimcityGui();
    private Simcity simcity ;
// private guehochoi.gui.RestaurantGui restGui = new guehochoi.gui.RestaurantGui();
// BaseAnimationPanel animationPanel = restGui.getAnimationPanel();
   
   private ericliu.gui.RestaurantGui restGui = new ericliu.gui.RestaurantGui();
   BaseAnimationPanel animationPanel = restGui.getAnimationPanel();
   private ericliu.gui.RestaurantPanel restPanel=restGui.getRestPanel();
//   
// private ericliu.gui.RestaurantGui ericRestGui=new ericliu.gui.RestaurantGui();
// BaseAnimationPanel ericAnimationPanel=restGui.getAnimationPanel();
    


 //Host, cook, waiters and customers
   private HostAgent host = new HostAgent("Host");
   private HostGui hostGui = new HostGui(host);

   /*private WaiterAgent waiter=new WaiterAgent("Waiter");
   private WaiterGui waiterGui=new WaiterGui(waiter);*/
   private SimcityGui gui; //reference to main gui

   
   //private WaiterAgent waiter=new WaiterAgent("Waiter");
   private Vector <Person> persons=new Vector<Person>();
  
  
   private JPanel restLabel = new JPanel();
   private PersonListPanel personPanel = new PersonListPanel(this);

   private JPanel group = new JPanel();


//   public Vector<MarketAgent> getMarkets(){
//      return markets;
//   }
   
 
   public Vector<Person> getPersons(){
      return persons;
   }
   public ControlPanel(Simcity simcity) {
       this.gui = gui;
       this.simcity=simcity;

          //setLayout(new GridLayout(1, 2, 10, 10));
//
         // group.setLayout(new BorderLayout());
          
          group.add(personPanel);
//
//       group.add(waiterPanel);
//       
//       initRestLabel();
//       add(restLabel);
         add(group);
   }

   /**
    * Sets up the restaurant label that includes the menu,
    * and host and cook information
    */
   private void initRestLabel() {
       JLabel label = new JLabel();
       //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
       restLabel.setLayout(new BorderLayout());
       label.setText(
               "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

       restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
       restLabel.add(label, BorderLayout.CENTER);
       restLabel.add(new JLabel("               "), BorderLayout.EAST);
       restLabel.add(new JLabel("               "), BorderLayout.WEST);
       
   }

   /**
    * When a customer or waiter is clicked, this function calls
    * updatedInfoPanel() from the main gui so that person's information
    * will be shown
    *
    * @param type indicates whether the person is a customer or waiter
    * @param name name of person
    */
   public void showInfo(String type, String name) {
//
//       if (type.equals("Persons")) {
//
//           for (int i = 0; i < persons.size(); i++) {
//               Person temp = persons.get(i);
//               if (temp.name == name)
//                   gui.updateInfoPanel(temp);
//                   //gui.updateTextField(temp);
//           }
//       }

   }

   /**
    * Adds a customer or waiter to the appropriate list
    *
    * @param type indicates whether the person is a customer or waiter (later)
    * @param name name of person
    */
   public void addPerson(String type, String name, boolean isHungry) {

//     if (type.equals("Persons")) {
//        Person p = new Person(name);   
//        //CustomerGui g = new CustomerGui(c, gui);
//
//     if(isHungry==true)
//     {
//       // g.setHungry();
//     }
//       // gui.animationPanel.addGui(g);// dw
//        c.setHost(host);
//        c.setCashier(cashier);
//        if(!waiters.isEmpty()){
//          c.setWaiter(waiters.get(0));
//        }
//        //c.setWaiter(waiter);
//        //c.setGui(g);
//        customers.add(c);
//        c.startThread();
//     }
   }
   
   public void addWaiter(String type, String name, boolean isWorking) {

//      if (type.equals("Waiters")) {
//         WaiterAgent w = new WaiterAgent(name, cook.soldOutFoods);   
//         WaiterGui g = new WaiterGui(w, gui);
//
//      if(isWorking==true)
//      {
//         g.setWorking(true);
//      }
//         gui.animationPanel.addGui(g);// dw
//         w.setHost(host);
//         w.setCook(cook);
//         w.setCashier(cashier);
//         w.setGui(g);
//         
//         //host.addWaiter(w);
//         waiters.add(w);
//         w.startThread();
//      }
    }


 


@Override
public void actionPerformed(ActionEvent arg0) {
   // TODO Auto-generated method stub
   
   if(simcity.timetosleep())
   {   System.out.println("true");
       simcity.setNewTime();
       
      black = true;
   }
   if( black == true) {
      
      alpha += 0.005f;
      
      if(alpha >=1) {
         alpha = 1;
         //if(!simcity.timetowakeup()){
         black = false;
         alpha = 0;
         //}
      }
   }
   count += 1;
    
    
    
    //System.out.println(simcity.getCurrentSimTime());

   repaint();
   
}

@Override
public void mouseDragged(MouseEvent arg0) {
   // TODO Auto-generated method stub
   //mouseMoved(arg0);

   
}

@Override
public void mouseMoved(MouseEvent arg0) {
//   x = (int)arg0.getPoint().getX();
//   y = (int)arg0.getPoint().getY();
    
   //repaint();
   // TODO Auto-generated method stub
   
}



@Override
public void mouseClicked(MouseEvent e) {
   
   //repaint();
   // TODO Auto-generated method stub
   
}



@Override
public void mouseEntered(MouseEvent e) {
   // TODO Auto-generated method stub
   //repaint();
}



@Override
public void mouseExited(MouseEvent e) {
   // TODO Auto-generated method stub
   //repaint();
}



@Override
public void mousePressed(MouseEvent e) {
   //repaint();
   // TODO Auto-generated method stub
   
}



@Override
public void mouseReleased(MouseEvent e) {
   // TODO Auto-generated method stub
   //repaint();
}





}