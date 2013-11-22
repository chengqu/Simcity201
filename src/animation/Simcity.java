package animation;



import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import agents.Person;
import guehochoi.gui.*;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class Simcity extends JPanel {
	
   

    private JPanel restLabel = new JPanel();
    private SimcityGui gui; //reference to main gui
    private  Date date =  Calendar.getInstance().getTime();
    public boolean sleep;
    public boolean start = true;
 
    Timer timer = new Timer();
    
    Person p = new Person("joe");

    public Simcity(SimcityGui gui) {
        this.gui = gui;
        
        add(restLabel);
    }
    
    public boolean timetosleep(){
    	//return true;
    	return ((Math.abs(Calendar.getInstance().getTime().getMinutes()-date.getMinutes())%1 == 0) && (Calendar.getInstance().getTime().getMinutes()!=date.getMinutes())&& (Calendar.getInstance().getTime().getSeconds()==date.getSeconds() ));
    }
    
    public void setNewTime() {
    	date = Calendar.getInstance().getTime();
    }
    public boolean timetowakeup(){
    	
    	return (!(Math.abs((Calendar.getInstance().getTime().getSeconds()- date.getSeconds()))%10 == 0));
    	
    }
    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
}
