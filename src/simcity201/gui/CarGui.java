package simcity201.gui;


import agents.BusAgent;
import agents.PassengerAgent;
import agents.CarAgent;
import animation.SimcityPanel;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.*;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;

public class CarGui implements Gui {

    private CarAgent agent = null;

    public int xPos = 100, yPos = 100;//default bus position
    private int xDestination = 100, yDestination = 100;//default bus position

    private Semaphore greenLight= new Semaphore(0,true);
    private String buspic = "car.png";
	private Image img;
	private AstarDriving astar;
    private boolean hide = false;
    private boolean driving = false;
    private boolean onRoad = false;
    public static final int xBank = 200;
    public static final int yBank = 120;
    
    public static final int xMarket = 400;
    public static final int yMarket = 160;
    
    public static final int xApart = 200;
    public static final int yApart = 525;
    
    
    public static final int xRest1 = 705;
    public static final int yRest1 = 325;
    
    public static final int xRest2 = 705;
    public static final int yRest2 = 475;
    
    public static final int xRest3 = 855;
    public static final int yRest3 = 325;
    
    public static final int xRest4 = 855;
    public static final int yRest4 = 475;
    
    public static final int xRest5 = 1005;
    public static final int yRest5 = 325;
    
    public static final int xRest6 = 1005;
    public static final int yRest6 = 475;
	    
    public static final int xHouse1 = 695;
    public static final int yHouse1 = 130;
    
    public static final int xHouse2 = 845;
    public static final int yHouse2 = 130;
    
    public static final int xHouse3 = 995;
    public static final int yHouse3 = 130;
    
    private List<Node> nodes = new ArrayList<Node>();
 
    public CarGui(CarAgent agent, AstarDriving astar) {
        this.agent = agent;
        ImageIcon customer = new ImageIcon(this.getClass().getResource(buspic));
		img = customer.getImage();
		this.astar = astar;
		this.nodes = astar.node();
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
//        for(Node n: nodes){
//        	if (xPos == xDestination && yPos == yDestination
//        			& (xDestination == n.x) & (yDestination == n.y)) {
//        				astar.setReleased(n);
//        }
//        }
    }
    
    //public void 

    public void draw(Graphics2D g) {
    	if(hide == false){
    	g.drawImage(img,xPos,yPos,30,30,null);
    	}
    }

    public boolean isPresent() {
        return true;
    }
    public void hide(){
    	hide = true;
    }
    public void msgGreenLight(){
 	   greenLight.release();
 	}
    
    public void DoDriveTo(String startDest,String dest){
    	hide  = false;
    	List<Node> path;
    	List<Node> visited = new ArrayList<Node>();
    	driving = false;
    	path = astar.astar(astar.map.get(startDest), astar.map.get(dest));
    	while(!path.isEmpty()){
    		if(path.get(0).isTrafficLight == true){
    			SimcityPanel.trafficLight.msgNotifyMe(this);
    			try {
					greenLight.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		if((xPos != path.get(0).x || yPos != path.get(0).y) && astar.isOccupied(path.get(0)) == false){
    			try {
    				path.get(0).atNode.acquire();
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			xDestination = path.get(0).x;
    			yDestination = path.get(0).y;
    			astar.setOccupied(path.get(0));
    			driving = true;
    			if(!visited.isEmpty()){
    				onRoad = true;
    			}
    			else onRoad = false;
    		}
    		else if(xPos == path.get(0).x && yPos == path.get(0).y){
    			astar.setUnOccupied(path.get(0));
    			visited.add(path.get(0));
    			astar.setReleased(path.get(0));
    			path.remove(0);
    			driving = false;
    			
    		}
    		else if(astar.isOccupied(path.get(0)) == true && driving == false){
    			if(visited.isEmpty()){//astar.map.get(path.get(0)) != null || path.get(0).child.size() == 1||visited.size() == 0||visited.get(visited.size()-1).child.size() == 1){
    				xDestination = xPos;
    				yDestination = yPos;
    			}
    			else {
    				List<Node> temp = astar.astar(visited.get(visited.size()-1), astar.map.get(dest));
    				path.clear();
    				visited.clear();
    				for(Node n : temp){
    					path.add(n);
    	           	}
    			}
    		}
    }

    	
    	agent.msgAtDest();
    }
    public void DoGoToPark(String dest){
    	onRoad = false;
    	if(dest.equals("Bank")){
            xDestination = xBank +60;
            yDestination = yBank +60;}
        	if(dest.equals("Market")){
                xDestination = xMarket +60;
                yDestination = yMarket +60;}
    	if(dest.equals("Rest1")){
            xDestination = xRest1 +60;
            yDestination = yRest1 +60;}
    	if(dest.equals("Rest2")){
            xDestination = xRest2 +60;
            yDestination = yRest2 +60;}
    	if(dest.equals("Rest3")){
            xDestination = xRest3 +60;
            yDestination = yRest3 +60;}
    	if(dest.equals("Rest4")){
            xDestination = xRest4 +60;
            yDestination = yRest4 +60;}
    	if(dest.equals("Rest5")){
            xDestination = xRest5 +60;
            yDestination = yRest5 +60;}
    	if(dest.equals("Rest6")){
            xDestination = xRest6 +60;
            yDestination = yRest6 +60;}
    	if(dest.equals("House1")){
            xDestination = xHouse1 +60;
            yDestination = yHouse1 +60;
    	}
    	if(dest.equals("House2")){
            xDestination = xHouse2 +60;
            yDestination = yHouse2 +60;
    	}
    	if(dest.equals("House3")){
            xDestination = xHouse3 +60;
            yDestination = yHouse3 +60;}
    	if(dest.equals("Apart")){
            xDestination = xApart +60;
            yDestination = yApart +60;}
    			
    }
    public void DoGoTo(String dest) {
    	if(dest.equals("Bank")){
            xDestination = xBank;
            yDestination = yBank;}
        	if(dest.equals("Market")){
                xDestination = xMarket;
                yDestination = yMarket;}
        	
    	if(dest.equals("Rest1")){
            xDestination = xRest1;
            yDestination = yRest1;}
    	if(dest.equals("Rest2")){
            xDestination = xRest2;
            yDestination = yRest2;}
    	if(dest.equals("Rest3")){
            xDestination = xRest3;
            yDestination = yRest3;}
    	if(dest.equals("Rest4")){
            xDestination = xRest4;
            yDestination = yRest4;}
    	if(dest.equals("Rest5")){
            xDestination = xRest5;
            yDestination = yRest5;}
    	if(dest.equals("Rest6")){
            xDestination = xRest6;
            yDestination = yRest6;}
    	if(dest.equals("House1")){
            xDestination = xHouse1;
            yDestination = yHouse1;
    	}
    	if(dest.equals("House2")){
            xDestination = xHouse2;
            yDestination = yHouse2;
    	}
    	if(dest.equals("House3")){
            xDestination = xHouse3;
            yDestination = yHouse3;}
    	if(dest.equals("Apart")){
            xDestination = xApart;
            yDestination = yApart;}
    	
    	
    }
    
   
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    public boolean isDriving(){
    	return onRoad;
    }
}
