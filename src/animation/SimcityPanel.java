package animation;


/* Import your restaurant here */
import guehochoi.gui.AnimationPanel;
import guehochoi.gui.RestaurantGui;
import guehochoi.gui.RestaurantPanel;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
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
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.TimerTask;

import javax.swing.*;

import agents.BusAgent;
import agents.CarAgent;
import agents.PassengerAgent;
import agents.StopAgent;
import agents.TruckAgent;
import Buildings.Building;
import simcity201.gui.Bank;
import simcity201.gui.BusGui;
import simcity201.gui.CarGui;
import simcity201.gui.GlobalMap;
import simcity201.gui.Gui;
import simcity201.gui.PassengerGui;
import simcity201.gui.TrafficLightAgent;
import simcity201.gui.TruckGui;

public class SimcityPanel extends JPanel implements ActionListener,MouseMotionListener, MouseListener{


	/*private BusAgent bus = new BusAgent("Bank","Bus1Crossing1","Market","Bus1Crossing2","Restaurants1","Bus1Crossing3","Restaurants2","Bus1Crossing4","House","Bus1Crossing5","Terminal1",1);

	private BusGui busGui = new BusGui(bus,"Terminal1");
	private BusAgent bus2 = new BusAgent("Rest1","","Rest2","","Bank","","House","Market","Terminal2","","",2);
	private BusGui busGui2 = new BusGui(bus2,"Terminal2");
	private StopAgent stop = new StopAgent(bus,bus2);
	private PassengerAgent p = new PassengerAgent("Passenger", null);
	//	private PassengerGui pGui = new PassengerGui(p);
	private PassengerAgent r = new PassengerAgent("Rich", null);
	private PassengerAgent poor = new PassengerAgent("Poor", null);
	//	private PassengerGui poorGui = new PassengerGui(poor);
	//	private PassengerGui rGui = new PassengerGui(r);
	//	private CarAgent car = new CarAgent("Audi");
	//	private CarGui carGui = new CarGui(car);*/
	public static List<Gui> guis = new ArrayList<Gui>();
	//	private TruckAgent truck = new TruckAgent();
	//	private TruckGui truckGui = new TruckGui(truck);

	//TRAFFIC LIGHT AGENT
	public static TrafficLightAgent trafficLight=new TrafficLightAgent();

	private JFrame inside = new JFrame();
	private JPanel insidePanel = new JPanel();
	private boolean mouseover = false;
	private int x;
	private int y;
	private int LYNrestaurantX = 320;
	private int LYNRestaurantY = 170;
	private int RyanRestaurantX;
	private int RyanRestaurantY;
	private int JoshRestaurantX;
	private int JoshRestaurantY;
	private int DavidRestaurantX;
	private int DavidRestaurantY;
	private int EricRestaurantX;
	private int EricRestaurantY;
	private int BankX;
	private int BankY;
	private int StoreX;
	private int StoreY;
	private int HouseX;
	private int HouseY;
	private int transparency = 50;
	private int SIZEX = 1200;
	private int SIZEY = 850;
	private int RoadLengthlong = 1200;
	private int RoadLengthshort = 850;
	private int RoadWidth = 70;
	private int LeftRoadX = 0;
	private int LeftRoadY = 0;
	private int UpperRoadX = 70;
	private int UpperRoadY = 0;
	private int LowerRoadX = 70;
	private int LowerRoadY = 850-RoadWidth-25;
	private int RightRoadX = 1200-RoadWidth-5;
	private int RightRoadY = 70;
	int count = 0;
	private final double[][] trs = {
			{0.0, 0.15, 0.30, 0.5, 0.65, 0.80, 0.9, 1.0},
			{1.0, 0.0, 0.15, 0.30, 0.5, 0.65, 0.8, 0.9},
			{0.9, 1.0, 0.0, 0.15, 0.3, 0.5, 0.65, 0.8},
			{0.8, 0.9, 1.0, 0.0, 0.15, 0.3, 0.5, 0.65},
			{0.65, 0.8, 0.9, 1.0, 0.0, 0.15, 0.3, 0.5},
			{0.5, 0.65, 0.8, 0.9, 1.0, 0.0, 0.15, 0.3},
			{0.3, 0.5, 0.65, 0.8, 0.9, 1.0, 0.0, 0.15},
			{0.15, 0.3, 0.5, 0.65, 0.8, 0.9, 1.0, 0.0,}
	};





	private boolean black = false;
	private  float alpha = 0f;
	private  int trans1 = 0;
	private  int trans2 = 0;
	private  int trans3 = 0;
	private Timer timer;

	//TODO: add your restaurant here

	//private SimcityGui simcitygui = new SimcityGui();
	private Simcity simcity ;
	//private guehochoi.gui.RestaurantGui restGui = new guehochoi.gui.RestaurantGui();
	//BaseAnimationPanel animationPanel = restGui.getAnimationPanel();

	//private josh.restaurant.gui.RestaurantGui restGui = new josh.restaurant.gui.RestaurantGui();
	//BaseAnimationPanel animationPanel = restGui.getAnimationPanel(); 


	public SimcityPanel(Simcity simcity) {
		this.simcity = simcity;
		Dimension PANEL_DIM = new Dimension(SIZEX, SIZEY);
		this.setPreferredSize(PANEL_DIM);
		this.setMaximumSize(PANEL_DIM);
		this.setMinimumSize(PANEL_DIM);

		addMouseMotionListener(this);
		addMouseListener(this);
		timer = new Timer(10,  this);
		timer.start();

		
		trafficLight.startThread();
//		GlobalMap.getGlobalMap().trafficLight=trafficLight;
//		GlobalMap.getGlobalMap().startLight();

		//Dimension inside_dim = new Dimension(1000, 850);
		inside.setVisible(true);

		//inside.setPreferredSize(inside_dim);
		//inside.setMinimumSize(inside_dim);
		//inside.setMaximumSize(inside_dim);

		inside.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		inside.setLocation(1000, 0);
		inside.add(insidePanel);

	}



	boolean start = false;
	public void Start()
	{
		start = true;
	}

	public void paintComponent(Graphics g) {

		if(start)
		{
			Graphics2D g2 = (Graphics2D)g;

			

			//Clear the screen by painting a rectangle the size of the frame
			//		g2.setColor(getBackground());
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRect(0, 0, SIZEX, SIZEY );



			//Draw the roads


			g2.setColor(Color.GRAY);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					9 * 0.1f));

			g2.fillRect(UpperRoadX, UpperRoadY, RoadLengthlong, RoadWidth);
			g2.fillRect(LeftRoadX, LeftRoadY, RoadWidth, RoadLengthshort);
			g2.fillRect(LowerRoadX, LowerRoadY, RoadLengthlong-RoadWidth*2-5, RoadWidth);
			g2.fillRect(RightRoadX, RightRoadY, RoadWidth, RoadLengthshort);
			g2.fillRect(RoadWidth, SIZEY/2-20, RoadLengthlong-2*RoadWidth-5, RoadWidth);
			g2.fillRect(SIZEX/2-32, RoadWidth, RoadWidth, SIZEY-2*RoadWidth-25);
			
//			for(int i=0; i<GlobalMap.getGlobalMap().getWalkAStar().getTileNames().size(); i++){
//            int xCoordinate,yCoordinate;
//            xCoordinate=GlobalMap.getGlobalMap().getWalkAStar().getTileNames().get(i+1).xCoordinate;
//            yCoordinate=GlobalMap.getGlobalMap().getWalkAStar().getTileNames().get(i+1).yCoordinate;
//            if(GlobalMap.getGlobalMap().getWalkAStar().getTileNames().get(i+1).isTrafficLight==true){
//               g2.setColor(Color.red);
//            }
//            else{
//               g2.setColor(Color.green);
//            }
//            g2.fillRect(xCoordinate, yCoordinate, 10, 10);
//         }
			
			for(int i=0; i<10; i++){
			   for(int j=0;j<11;j++){
			      if(j==3 || i==5){
			   
                  int xCoordinate,yCoordinate;
                  xCoordinate=GlobalMap.getGlobalMap().getWalkAStar().map[i][j].xCoordinate;
                  yCoordinate=GlobalMap.getGlobalMap().getWalkAStar().map[i][j].yCoordinate;
                  
                  if(GlobalMap.getGlobalMap().getWalkAStar().map[i][j].isTrafficLight==true){
                     g2.setColor(Color.red);
                     g2.fillRect(xCoordinate, yCoordinate, 15, 15);
                  }
                  else{
                     g2.setColor(Color.green);
                     g2.fillRect(xCoordinate, yCoordinate, 15, 15);
                  }
			      }
              }
         }

			//crosswalk
			/*
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect (RoadWidth+15, RoadWidth, (RoadLengthlong-RoadWidth*4)/2, 15);
        g2.fillRect(RoadWidth, RoadWidth, 15, (RoadLengthshort-RoadWidth*2)/2);


        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                6 * 0.1f));
        g2.setColor(Color.red);
        g2.fill3DRect(RoadWidth+2, RoadWidth, (RoadLengthlong-RoadWidth*3)/2-20, 2,true);
			 */




			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					9 * 0.1f));
			//Draw double yellow white bars
			g2.setColor(Color.YELLOW);
			g2.fillRect(RoadWidth+30, UpperRoadY+RoadWidth/2-5, RoadLengthlong-2*RoadWidth-70, 3);
			g2.fillRect(RoadWidth+30, UpperRoadY+RoadWidth/2, RoadLengthlong-2*RoadWidth-70, 3);
			g2.fillRect(28, RoadWidth+30, 3, RoadLengthshort-2*RoadWidth-85);
			g2.fillRect(33, RoadWidth+30, 3, RoadLengthshort-2*RoadWidth-85);
			g2.fillRect(RoadWidth + 30, SIZEY-25-RoadWidth/2-5, RoadLengthlong-2*RoadWidth-70, 3);
			g2.fillRect(RoadWidth + 30, SIZEY-25-RoadWidth/2, RoadLengthlong-2*RoadWidth-70, 3);
			g2.fillRect(SIZEX-5-RoadWidth/2-3, RoadWidth+30, 3, RoadLengthshort-2*RoadWidth-85);
			g2.fillRect(SIZEX-5-RoadWidth/2+2, RoadWidth+30, 3, RoadLengthshort-2*RoadWidth-85);
			//mid horizontal and vertical
			for(int i = 0; i<18; i++) {
				if(i<=5) {
					g2.fillRect(SIZEX/2+2, RoadWidth +10 + i*48, 3, 25 );
				} else if(i>=9 && i<=13) {
					g2.fillRect(SIZEX/2+2, RoadWidth +20 + i*48, 3, 25 );
				}
				if(i >= 9) {
					g2.fillRect(RoadWidth*3 + 30+ i*50, SIZEY/2+12, 25 , 3);

				} else {            
					g2.fillRect(RoadWidth+15+i*50, SIZEY/2+12, 25,3);        	
				}
			}



			//White bars
			g2.setColor(Color.WHITE);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					8 * 0.1f));
			for(int i = 1; i <= 24; i++) {
				g2.fillRect(RoadWidth+20+i*40, UpperRoadY+RoadWidth/2-20, 20, 3);
				g2.fillRect(RoadWidth+20+i*40, UpperRoadY+RoadWidth/2+18, 20, 3);
				g2.fillRect(RoadWidth+20+i*40, SIZEY-25-RoadWidth/2-21, 20, 3);
				g2.fillRect(RoadWidth+20+i*40, SIZEY-25-RoadWidth/2+17, 20, 3);
				if(i<=15){
					g2.fillRect(13, RoadWidth+15+i*40, 3, 20);
					g2.fillRect(50, RoadWidth+15+i*40, 3, 20);
					g2.fillRect(SIZEX-5-RoadWidth/2-18, RoadWidth+15+i*40, 3, 20);
					g2.fillRect(SIZEX-5-RoadWidth/2+17, RoadWidth+15+i*40, 3, 20);
				}
			}
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					10 * 0.1f));
			//Draw CrossWalks
			//Upper two
			g2.fillRect(RoadWidth, 0, 3, RoadWidth);
			g2.fillRect(RoadWidth+27, 0, 3, RoadWidth);
			g2.fillRect(SIZEX-RoadWidth-8, 0, 3, RoadWidth);
			g2.fillRect(SIZEX-RoadWidth-40, 0, 3, RoadWidth);
			//Left two
			g2.fillRect(0, RoadWidth, RoadWidth, 3);
			g2.fillRect(0, RoadWidth+28, RoadWidth, 3);
			g2.fillRect(0, SIZEY-RoadWidth-28, RoadWidth, 3);
			g2.fillRect(0, SIZEY-RoadWidth-57, RoadWidth, 3);
			//Bottom two
			g2.fillRect(RoadWidth, SIZEY-RoadWidth-25, 3, RoadWidth);
			g2.fillRect(RoadWidth+30, SIZEY-RoadWidth-25, 3, RoadWidth);
			g2.fillRect(SIZEX-RoadWidth-8, SIZEY-RoadWidth-25, 3, RoadWidth);
			g2.fillRect(SIZEX-RoadWidth-40, SIZEY-RoadWidth-25, 3, RoadWidth);
			//Right two
			g2.fillRect(SIZEX-RoadWidth-5, RoadWidth, RoadWidth, 3);
			g2.fillRect(SIZEX-RoadWidth-5, RoadWidth+28, RoadWidth, 3);
			g2.fillRect(SIZEX-RoadWidth-5, SIZEY-RoadWidth-27, RoadWidth, 3);
			g2.fillRect(SIZEX-RoadWidth-5, SIZEY-RoadWidth-57, RoadWidth, 3);

			//        ImageIcon grass = new ImageIcon(this.getClass().getResource("ground.jpeg"));
			//        Image img_grass = grass.getImage();
			//        g.drawImage(img_grass, 80, 80, 490, 325, this);

			//draw buildings
			for (Building b : GlobalMap.getGlobalMap().getBuildings()) {
				ImageIcon myIcon=null;
				switch(b.name){
				case "Bank":
					myIcon = new ImageIcon(this.getClass().getResource("bank.png"));
					break;
				case "Market":
					myIcon = new ImageIcon(this.getClass().getResource("market.png"));
					break;
				case "Rest1":
					myIcon = new ImageIcon(this.getClass().getResource("rest1.png"));
					break;
				case "Rest2":
					myIcon = new ImageIcon(this.getClass().getResource("rest2.png"));
					break;
				case "Rest3":
					myIcon = new ImageIcon(this.getClass().getResource("rest3.png"));

					break;
				case "Rest4":
					myIcon = new ImageIcon(this.getClass().getResource("rest4.png"));
					break;
				case "Rest5":
					myIcon = new ImageIcon(this.getClass().getResource("rest5.png"));
					break;
				case "Rest6":
					myIcon = new ImageIcon(this.getClass().getResource("rest6.png"));
					break;
				case "House1":
					myIcon = new ImageIcon(this.getClass().getResource("house.png"));
					break;
				case "House2":
					myIcon = new ImageIcon(this.getClass().getResource("house.png"));
					break;
				case "House3":
					myIcon = new ImageIcon(this.getClass().getResource("house.png"));
					break;
				case "Apart":
					myIcon = new ImageIcon(this.getClass().getResource("apart.png"));
					break;
				default: 
					myIcon = new ImageIcon(this.getClass().getResource("restaurant.jpg"));
					break;
				}

				Image img1 = myIcon.getImage();
				g.drawImage(img1, b.x, b.y, b.width, b.height, this);
			}

			/* mouseover -> highlighting */
			mouseover = false;
			for (Building b : GlobalMap.getGlobalMap().getBuildings()) {
				if(x <= b.x+b.width && x>= b.x && y <= b.y+b.height && y>=b.y ){ 
					mouseover = true;

					Color color = new Color(255, 255, 0, 255 * transparency / 100);
					g.setColor(color);
					g.fillRect(b.x, b.y, b.width, b.height);
					g.setColor(Color.BLUE);
					Font font = new Font("Lucida Handwriting", Font.BOLD+Font.ITALIC, 15);
					g.setFont(font);
					g.drawString(b.name,b.x, b.y-2);
					break;
				}
			}

			LYN.gui.RestaurantGui temp = (LYN.gui.RestaurantGui)GlobalMap.getGlobalMap().searchByName("Rest3");
			if(temp.restPanel.isOpen == false) {

				for (Building b : GlobalMap.getGlobalMap().getBuildings()) {
					if(b.name .equals("Rest3") ){

						g.setColor(Color.ORANGE);
						Font font = new Font("Lucida Handwriting", Font.BOLD+Font.ITALIC, 25);
						g.setFont(font);
						g.drawString("Closed",b.x, b.y);
					}
				}

			} 
			guehochoi.gui.RestaurantGui temp1 = (guehochoi.gui.RestaurantGui)GlobalMap.getGlobalMap().searchByName("Rest2");
			if(temp1.restPanel.isOpen == false) {

				for (Building b : GlobalMap.getGlobalMap().getBuildings()) {
					if(b.name .equals("Rest2") ){

						g.setColor(Color.ORANGE);
						Font font = new Font("Lucida Handwriting", Font.BOLD+Font.ITALIC, 25);
						g.setFont(font);
						g.drawString("Closed",b.x, b.y);
					}
				}

			} 

			Bank temp2 = (Bank)GlobalMap.getGlobalMap().searchByName("Bank");
			if(temp2.isOpen == false) {

				for (Building b : GlobalMap.getGlobalMap().getBuildings()) {
					if(b.name .equals("Bank") ){

						g.setColor(Color.ORANGE);
						Font font = new Font("Lucida Handwriting", Font.BOLD+Font.ITALIC, 25);
						g.setFont(font);
						g.drawString("Closed",b.x, b.y);
					}
				}

			} 
			

			if(simcity.day<5) {
				g.drawString("Day:"+String.valueOf(simcity.day), 550, 450);
			}

			if(simcity.day == 5 || simcity.day == 6) {
				g.drawString("Weekend", 550, 450);

			}




			try
			{
			List<PassengerGui> p_ = new ArrayList<PassengerGui>();
				List<CarGui> g_ = new ArrayList<CarGui>();
				for(Gui gui : guis) {
					if (gui.isPresent()) {
						gui.updatePosition();
						if(gui instanceof PassengerGui){
							p_.add((PassengerGui)gui);
						}
						else if(gui instanceof CarGui)
						{
							g_.add((CarGui)gui);
						}
					}
				}
				
				List<Gui> collidedVehicles = new ArrayList<Gui>();
				
				for(PassengerGui p : p_)
				{
					for(CarGui g1 : g_)
					{
						if((Math.abs(g1.getXPos() - p.getXpos()) <= 30) && (Math.abs(g1.getYPos() - p.getYpos())<= 30) ){
							if(!collidedVehicles.contains(p)){
								collidedVehicles.add(p);
							}
						}
					}
				}
				
				for(Gui g1 : collidedVehicles){
					PassengerGui p = (PassengerGui)g1;
					p.hide();
				}
				
				
				collidedVehicles.clear();
				
				for(CarGui p : g_)
				{
					for(CarGui g1 : g_)
					{
						if((Math.abs(g1.getXPos() - p.getXPos()) <= 30) && (Math.abs(g1.getYPos() - p.getYPos())<= 30) ){
							if(!collidedVehicles.contains(g1)){
								collidedVehicles.add(g1);
							}
							if(!collidedVehicles.contains(p)){
								collidedVehicles.add(p);
							}
						}
					}
				}

				for(Gui gui : guis) {
					if (gui.isPresent()) {
						gui.draw(g2);
					}
				}
			}
			catch(ConcurrentModificationException e)
			{

			}
			

			//Fade out
			g2.setColor(Color.BLACK);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2.fillRect(0, 0, 1200, 850);

			//draw ZZZ
			if(black == true){
				Color color = new Color(255, 255, 0, 255 * trans1 / 100);
				g.setColor(color);
				Font font = new Font("Lucida Handwriting", Font.BOLD, 25);
				g.setFont(font);

				g.drawString("Z",SIZEX/2, SIZEY/2);
				color = new Color(255, 255, 0, 255 * trans2 / 100);
				g.setColor(color);
				font = new Font("Lucida Handwriting", Font.BOLD, 30);
				g.setFont(font);
				g.drawString("Z",SIZEX/2+40, SIZEY/2-40);
				color = new Color(255, 255, 0, 255 * trans3 / 100);
				g.setColor(color);
				font = new Font("Lucida Handwriting", Font.BOLD, 35);
				g.setFont(font);
				g.drawString("Z",SIZEX/2+80, SIZEY/2-80);

			}

		}    

	}


	public void activateThisPanel(BaseAnimationPanel holding) {
		//restpanel.addPerson("Customers","hi",true);
		//restpanel.addWaiter("Waiters","hello");
		//holding.setSize(new Dimension(500, 450));
		Dimension relSize = holding.getSize();
		inside.setPreferredSize(relSize);
		inside.setMinimumSize(relSize);
		inside.setMaximumSize(relSize);
		inside.setSize(relSize);
		inside.setVisible(true);

		insidePanel.removeAll();
		insidePanel.setPreferredSize(relSize);
		insidePanel.setMinimumSize(relSize);
		insidePanel.setMaximumSize(relSize);
		insidePanel.setSize(relSize);
		insidePanel.add(holding);
		insidePanel.repaint();
		insidePanel.validate();
		inside.pack();
	}



	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub



		if(simcity.timetosleep())
		{   
			System.out.println("sleep");
			simcity.setNewTime();
			simcity.setNewDay();

			black = true;
		}
		if( black == true) {

			alpha += 0.005f;
			if(alpha >= 1){

				trans1 += 4;
				if(trans1>=100){
					trans2 += 4;
				}
				if(trans2>=100) {
					trans3+=4;
				}
				if(trans1>=100){
					trans1 = 100;

				}
				if(trans2>=100){
					trans2 = 100;
				}

				if(trans3>=100){
					trans3 = 0;
					trans2 = 0;
					trans1 = 0;
				}




			}
			if(alpha >=1) {
				alpha = 1;
				if(!simcity.timetowakeup()){
					black = false;
					alpha = 0;
					simcity.setNewDay();

				}
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
		x = (int)arg0.getPoint().getX();
		y = (int)arg0.getPoint().getY();

		//repaint();
		// TODO Auto-generated method stub

	}



	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println(""+x+y);

		for ( Building b : GlobalMap.getGlobalMap().getBuildings()) {
			if ( x <= (b.x+b.width) && x >= b.x && y <= (b.y+b.height) && y >= b.y) {
				activateThisPanel(b.getAnimationPanel());
			}
		}

		/*
	if(x <= LYNrestaurantX+65 && x>=LYNrestaurantX && y <= LYNRestaurantY+65 && y>=LYNRestaurantY ){ 
		activateThisPanel(animationPanel);
	}*/
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

	public void addGui(PassengerGui gui) {
		guis.add(gui);
	}

	public void addGui(BusGui gui) {
		guis.add(gui);
	}
	public void addGui(CarGui gui) {
		guis.add(gui);
	}

	public void addGui(TruckGui gui) {
		guis.add(gui);
	}




}
