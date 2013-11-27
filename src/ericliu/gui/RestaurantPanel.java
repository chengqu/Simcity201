package ericliu.gui;

import ericliu.restaurant.CustomerAgent;
import ericliu.restaurant.HostAgent;
import ericliu.restaurant.WaiterAgent;
import ericliu.restaurant.CookAgent;
import ericliu.restaurant.CashierAgent;
//import ericliu.restaurant.MarketAgent;
import agents.Person;

import javax.swing.*;

import newMarket.MarketRestaurantHandlerAgent;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Host");
    private HostGui hostGui = new HostGui(host);

    /*private WaiterAgent waiter=new WaiterAgent("Waiter");
    private WaiterGui waiterGui=new WaiterGui(waiter);*/
    private RestaurantGui gui; //reference to main gui

    private CashierAgent cashier=new CashierAgent("Cashier");
    private CashierGui cashierGui=new CashierGui(cashier);
<<<<<<< HEAD
    private CookAgent cook=new CookAgent("Cook");
=======
    private CookAgent cook=new CookAgent("Rest4");
>>>>>>> Transportation
    private CookGui cookGui=new CookGui(cook, gui);
    
    //private WaiterAgent waiter=new WaiterAgent("Waiter");
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
//    private Vector<MarketAgent> markets= new Vector<MarketAgent>();
//    private MarketAgent market1=new MarketAgent("Market1", 1, 1, 0, 0);
//    private MarketAgent market2=new MarketAgent("Market2", 0, 0, 1, 1);
//    private MarketAgent market3=new MarketAgent("Market3", 0, 0 ,0 ,0);
   
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private WaiterListPanel waiterPanel= new WaiterListPanel(this, "Waiters");
    private JPanel group = new JPanel();


    public HostAgent getHost(){
       return host;
    }
    public Vector<WaiterAgent> getWaiters(){
       return waiters;
    }
    
//    public Vector<MarketAgent> getMarkets(){
//          Vector<MarketAgent> markets= new Vector<MarketAgent>();
//          markets.add(market1);
//          markets.add(market2);
//          markets.add(market3);
//          return markets;
//    }
//    public Vector<MarketAgent> getMarkets(){
//       return markets;
//    }
    
    public CookAgent getCook(){
       return cook;
    }
    
    public CashierAgent getCashier(){
       return cashier;
    }
    public Vector<CustomerAgent> getCustomers(){
       return customers;
    }
    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        host.setGui(hostGui);
        
        gui.animationPanel.addGui(hostGui);
        host.startThread();
        
        
        
        cashier.startThread();
        cashier.setGui(cashierGui);
        cashier.setCook(cook);
        gui.animationPanel.addGui(cashierGui);
        
//        market1.startThread();
//        market1.setCashier(cashier);
//        market2.startThread();
//        market2.setCashier(cashier);
//        market3.startThread();
//        market3.setCashier(cashier);
        
        cook.startThread();
        cook.setGui(cookGui);
        cook.setCashier(cashier);
        gui.animationPanel.addGui(cookGui);
//        cook.addMarket(market1);
//        cook.addMarket(market2);
//        cook.addMarket(market3);

        //addWaiter("Waiters", "Waiter", true);
        
        setLayout(new GridLayout(1, 2, 10, 10));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);

        group.add(waiterPanel);
        
        initRestLabel();
        add(restLabel);
        add(group);
    }

    /**
     * Messaage from Person to RestaurantPanel that he is entering the restaurant
     * so the Restaurant Panel should add a new customer with this instance of person
     */
    public void msgAddCustomer(Person person){
       addPerson("Customers", person, true);
    }
    /**
     * Message from customer back to RestaurantPanel alerting when its functions in 
     * the Restaurant are complete. 
     */
    public void msgCustomerDone(CustomerAgent customer){
       //customer.person.msgDoneAtEricRestaurant();
       System.out.println(customer.getName()+" IS DONE AT RESTAURANT");
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

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
                    //gui.updateTextField(temp);
            }
        }
        else if (type.equals("Waiters")) {

           for (int i = 0; i < waiters.size(); i++) {
               WaiterAgent temp = waiters.get(i);
               if (temp.getName() == name)
                   gui.updateInfoPanel(temp);
           }
       }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, Person person, boolean isHungry) {

      if (type.equals("Customers")) {
         CustomerAgent c = new CustomerAgent(person);   
         CustomerGui g = new CustomerGui(c, gui);

      if(isHungry==true)
      {
         g.setHungry();
      }
         gui.animationPanel.addGui(g);// dw
         c.setRestPanel(this);
         c.setHost(host);
         c.setCashier(cashier);
         if(!waiters.isEmpty()){
           c.setWaiter(waiters.get(0));
         }
         //c.setWaiter(waiter);
         c.setGui(g);
         customers.add(c);
         c.startThread();
         //c.msgGotHungry();
      }
    }
    
//    public void addPerson(String type, String name, boolean isHungry) {
//
//       if (type.equals("Customers")) {
//          CustomerAgent c = new CustomerAgent(name);   
//          CustomerGui g = new CustomerGui(c, gui);
//
//       if(isHungry==true)
//       {
//          g.setHungry();
//       }
//          gui.animationPanel.addGui(g);// dw
//          c.setRestPanel(this);
//          c.setHost(host);
//          c.setCashier(cashier);
//          if(!waiters.isEmpty()){
//            c.setWaiter(waiters.get(0));
//          }
//          //c.setWaiter(waiter);
//          c.setGui(g);
//          customers.add(c);
//          c.startThread();
//       }
//     }
    
//    public void addWaiter(String type, Person person, boolean isWorking) {
//
//       if (type.equals("Waiters")) {
//          WaiterAgent w = new WaiterAgent(person, cook.soldOutFoods);   
//          WaiterGui g = new WaiterGui(w, gui);
//
//       if(isWorking==true)
//       {
//          g.setWorking(true);
//       }
//          gui.animationPanel.addGui(g);// dw
//          w.setHost(host);
//          w.setCook(cook);
//          w.setCashier(cashier);
//          w.setGui(g);
//          
//          //host.addWaiter(w);
//          waiters.add(w);
//          w.startThread();
//       }
//     }
    
    public void addWaiter(String type, String name, boolean isWorking) {

       if (type.equals("Waiters")) {
          WaiterAgent w = new WaiterAgent(name, cook.soldOutFoods);   
          WaiterGui g = new WaiterGui(w, gui);

       if(isWorking==true)
       {
          g.setWorking(true);
       }
          gui.animationPanel.addGui(g);// dw
          w.setHost(host);
          w.setCook(cook);
          w.setCashier(cashier);
          w.setGui(g);
          
          //host.addWaiter(w);
          waiters.add(w);
          w.startThread();
       }
     }

}
