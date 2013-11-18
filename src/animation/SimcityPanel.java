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
import java.util.TimerTask;

import javax.swing.*;

public class SimcityPanel extends JPanel implements ActionListener,MouseMotionListener, MouseListener{

	


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
    private Timer timer;
    
    //TODO: add your restaurant here
    
    //private SimcityGui simcitygui = new SimcityGui();
    private Simcity simcity ;
	private guehochoi.gui.RestaurantGui restGui = new guehochoi.gui.RestaurantGui();
	guehochoi.gui.AnimationPanel animationPanel = restGui.animationPanel;
	
    


	public SimcityPanel(Simcity simcity) {
		     this.simcity = simcity;
	    	 Dimension PANEL_DIM = new Dimension(SIZEX, SIZEY);
	    	this.setPreferredSize(PANEL_DIM);
	    	this.setMaximumSize(PANEL_DIM);
	    	this.setMinimumSize(PANEL_DIM);
	
		addMouseMotionListener(this);
		addMouseListener(this);
		 timer = new Timer(20,  this);
		timer.start();

		

		//Dimension inside_dim = new Dimension(1000, 850);
		inside.setVisible(false);
    
		//inside.setPreferredSize(inside_dim);
		//inside.setMinimumSize(inside_dim);
		//inside.setMaximumSize(inside_dim);
  
		inside.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		inside.setLocation(1000, 0);
		inside.add(insidePanel);
		//setLayout(new GridLayout(10,10));

		//setBackground(Color.white);
		//List<MyBlock> blocks = new ArrayList<MyBlock>();        
 
	}
	
	



	public void paintComponent(Graphics g) {
		
	   
		Graphics2D g2 = (Graphics2D)g;
     
       
        	 
		//Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
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
        
        //crosswalk
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect (RoadWidth+15, RoadWidth, (RoadLengthlong-RoadWidth*4)/2, 15);
        g2.fillRect(RoadWidth, RoadWidth, 15, (RoadLengthshort-RoadWidth*2)/2);
        
        
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                6 * 0.1f));
        g2.setColor(Color.red);
        g2.fill3DRect(RoadWidth+2, RoadWidth, (RoadLengthlong-RoadWidth*3)/2-20, 2,true);
        
        
        
        
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
        
        
		//draw buildings
		ImageIcon myIcon = new ImageIcon(this.getClass().getResource("restaurant.jpg"));
		Image img1 = myIcon.getImage();
		g.drawImage(img1, LYNrestaurantX, LYNRestaurantY, 65, 65,  this);
	    
		
		if(x <= LYNrestaurantX+65 && x>=LYNrestaurantX && y <= LYNRestaurantY+65 && y>=LYNRestaurantY ){ 
			mouseover = true;
		
			Color color = new Color(255, 255, 0, 255 * transparency / 100);
			g.setColor(color);
			g.fillRect(LYNrestaurantX, LYNRestaurantY, 65, 65);
			g.setColor(Color.BLUE);
			Font font = new Font("Lucida Handwriting", Font.BOLD+Font.ITALIC, 15);
			g.setFont(font);
		    g.drawString("LYN's Restaurant",LYNrestaurantX, LYNRestaurantY-2);
	    	
		} else if(x <= RyanRestaurantX+65 && x>=RyanRestaurantX && y <= RyanRestaurantY+65 && y>=RyanRestaurantY ){ 
			mouseover = true;
		
			Color color = new Color(255, 255, 0, 255 * transparency / 100);
			g.setColor(color);
			g.fillRect(RyanRestaurantX, RyanRestaurantY, 65, 65);
			g.setColor(Color.BLUE);
			Font font = new Font("Lucida Handwriting", Font.BOLD+Font.ITALIC, 15);
			g.setFont(font);
		    g.drawString("Ryan's Restaurant",RyanRestaurantX, RyanRestaurantY-2);
	    	
		} else if(x <= JoshRestaurantX+65 && x>=JoshRestaurantX && y <= JoshRestaurantY+65 && y>=JoshRestaurantY ){ 
			mouseover = true;
		
			Color color = new Color(255, 255, 0, 255 * transparency / 100);
			g.setColor(color);
			g.fillRect(JoshRestaurantX, JoshRestaurantY, 65, 65);
			g.setColor(Color.BLUE);
			Font font = new Font("Lucida Handwriting", Font.BOLD+Font.ITALIC, 15);
			g.setFont(font);
		    g.drawString("Josh's Restaurant",JoshRestaurantX, JoshRestaurantY-2);
	    	
		} else if(x <= DavidRestaurantX+65 && x>=DavidRestaurantX && y <= LYNRestaurantY+65 && y>=LYNRestaurantY ){ 
			mouseover = true;
		
			Color color = new Color(255, 255, 0, 255 * transparency / 100);
			g.setColor(color);
			g.fillRect(DavidRestaurantX, DavidRestaurantY, 65, 65);
			g.setColor(Color.BLUE);
			Font font = new Font("Lucida Handwriting", Font.BOLD+Font.ITALIC, 15);
			g.setFont(font);
		    g.drawString("David's Restaurant",DavidRestaurantX, DavidRestaurantY-2);
	    	
		} else if(x <= EricRestaurantX+65 && x>=EricRestaurantX && y <= EricRestaurantY+65 && y>=EricRestaurantY ){ 
			mouseover = true;
		
			Color color = new Color(255, 255, 0, 255 * transparency / 100);
			g.setColor(color);
			g.fillRect(EricRestaurantX, EricRestaurantY, 65, 65);
			g.setColor(Color.BLUE);
			Font font = new Font("Lucida Handwriting", Font.BOLD+Font.ITALIC, 15);
			g.setFont(font);
		    g.drawString("Eric's Restaurant",EricRestaurantX, EricRestaurantY-2);
	    	
		} else if(x <= BankX+65 && x>=BankX && y <= BankY+65 && y>=BankY ){ 
			mouseover = true;
		
			Color color = new Color(255, 255, 0, 255 * transparency / 100);
			g.setColor(color);
			g.fillRect(BankX, BankY, 65, 65);
			g.setColor(Color.BLUE);
			Font font = new Font("Lucida Handwriting", Font.BOLD+Font.ITALIC, 15);
			g.setFont(font);
		    g.drawString("Bank",BankX, BankY-2);
	    	
		}else if(x <= StoreX+65 && x>=StoreX && y <= StoreY+65 && y>=StoreY ){ 
			mouseover = true;
		
			Color color = new Color(255, 255, 0, 255 * transparency / 100);
			g.setColor(color);
			g.fillRect(StoreX, StoreY, 65, 65);
			g.setColor(Color.BLUE);
			Font font = new Font("Lucida Handwriting", Font.BOLD+Font.ITALIC, 15);
			g.setFont(font);
		    g.drawString("Store",StoreX, StoreY-2);
	    	
		}else {
			mouseover = false;
		}
	
	
		
		
		
		/*
		g2.setColor(Color.YELLOW);
		 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                RenderingHints.VALUE_ANTIALIAS_ON);
	        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
	                RenderingHints.VALUE_RENDER_QUALITY);
	        
	        

	        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND,
	                BasicStroke.JOIN_ROUND));
	        g2.translate(SIZEX/2,SIZEY/2);

	        for (int i = 0; i < 8; i++) {
	            
	            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
	                    (float) trs[count % 8][i]));

	            g2.rotate(Math.PI / 4f);
	            g2.drawLine(0, -30, 0, -40);
	        }
        */
        
	      //Fade out
	        g2.setColor(Color.BLACK);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2.fillRect(0, 0, 1200, 850);
        
		
    
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
		//insidePanel.repaint();
		insidePanel.validate();
    //inside.removeAll();
    //inside.add(holding);
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
	x = (int)arg0.getPoint().getX();
	y = (int)arg0.getPoint().getY();
	 
	//repaint();
	// TODO Auto-generated method stub
	
}



@Override
public void mouseClicked(MouseEvent e) {
	System.out.println(""+x+y);
	if(x <= LYNrestaurantX+65 && x>=LYNrestaurantX && y <= LYNRestaurantY+65 && y>=LYNRestaurantY ){ 
		activateThisPanel(animationPanel);
	}
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