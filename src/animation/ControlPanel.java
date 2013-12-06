package animation;
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

import simcity201.gui.GlobalMap;
import agents.Person;


import agents.Role;

public class ControlPanel extends JPanel implements ActionListener{

   
    boolean black = false;
    private  float alpha = 0f;
    private Timer timer;
    
    private int SIZEX=500;
    private int SIZEY=500;
    int count = 0;
    //private SimcityGui simcitygui = new SimcityGui();
    private Simcity simcity ;
    
   GlobalMap map;

   private SimcityGui gui; //reference to main gui
   
   public PersonEditor editor = new PersonEditor(this);
   public PersonListPanel personPanel = new PersonListPanel(this);

   public ControlPanel(Simcity simcity, SimcityGui gui) {
       this.gui = gui;
       this.simcity=simcity;
       add(personPanel);
       add(editor);
   }
   
   public void showInfo(String type, String name) {
	   for (int i = 0; i < GlobalMap.getGlobalMap().getListOfPeople().size(); i++) {
	       Person temp = GlobalMap.getGlobalMap().getListOfPeople().get(i);
	       if (temp.getName() == name)
	           gui.updateInfoPanel(temp);
	   }
   }
   
   public void showInfo(Person p)
   {
	   gui.updateInfoPanel(p);
   }

   public void addPerson(String name, float money, int hungerLevel, int age, float payCheck, String home, String homeInfo,boolean wantCar, String vehicle) {
     Person p = new Person(name);   
     p.setMoney(money);
     p.setHungerLevel(hungerLevel);
     p.age = age;
     p.payCheck=payCheck;
     p.wantCar=wantCar;
     GlobalMap.getGlobalMap().addPerson(p);
     simcity.addPerson(p, home, homeInfo, vehicle);
     editor.addPerson(p);
   }
   
	public void actionPerformed(ActionEvent arg0) {
	   
	   if(simcity.timetosleep())
	   {   System.out.println("true");
	       simcity.setNewTime();
	       black = true;
	   }
	   if( black == true) {
	      
	      alpha += 0.005f;
	      
	      if(alpha >=1) {
	         alpha = 1;
	         black = false;
	         alpha = 0;
	      }
	   }
	   count += 1;
	   repaint();
	}
}