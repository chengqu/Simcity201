package animation;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import simcity201.gui.GlobalMap;
import agents.Person;

public class ControlPanel extends JPanel implements ActionListener{

   
    boolean black = false;
    private  float alpha = 0f;
    private Timer timer;
    
    private int SIZEX=300;
    private int SIZEY=300;
    int count = 0;
    //private SimcityGui simcitygui = new SimcityGui();
    private Simcity simcity ;
    
   GlobalMap map;

   private SimcityGui gui; //reference to main gui
   
   public PersonEditor personEditor = new PersonEditor(this);
   public PersonListPanel personPanel = new PersonListPanel(this);
   public BuildingEditor buildingEditor= new BuildingEditor(this);

   public ControlPanel(Simcity simcity, SimcityGui gui) {
	   this.setMaximumSize(new Dimension(SIZEX, SIZEY));
	   this.setPreferredSize(new Dimension(SIZEX, SIZEY));
	   this.setMinimumSize(new Dimension(SIZEX, SIZEY));
       this.gui = gui;
       this.simcity=simcity;
       this.setLayout(new BorderLayout());
       add(personPanel, BorderLayout.NORTH);
       add(personEditor, BorderLayout.SOUTH);
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
     personEditor.addPerson(p);
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