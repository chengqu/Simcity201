package animation;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import simcity201.gui.GlobalMap;
import tracePanelpackage.TracePanel;
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
   
   public PersonEditor editor = new PersonEditor(this);
   public PersonListPanel personPanel = new PersonListPanel(this);
   public TracePanel tracepanel = new TracePanel();

   public ControlPanel(Simcity simcity, SimcityGui gui) {
	   this.setMaximumSize(new Dimension(SIZEX, SIZEY));
	   this.setPreferredSize(new Dimension(SIZEX, SIZEY));
	   this.setMinimumSize(new Dimension(SIZEX, SIZEY));
       this.gui = gui;
       this.simcity=simcity;
       this.setLayout(new BorderLayout());
       add(personPanel, BorderLayout.CENTER);
       add(editor, BorderLayout.SOUTH);
       Dimension anidim = new Dimension(190, 300);
       tracepanel.setPreferredSize(anidim);
       tracepanel.setMinimumSize(anidim);
       tracepanel.setMaximumSize(anidim);
       add(tracepanel, BorderLayout.EAST);
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
	   
	}
}