package simcity201.gui;


import agents.BusAgent;
import agents.PassengerAgent;
import agents.TruckAgent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.*;

import javax.swing.ImageIcon;

public class TruckGui implements Gui {
	
    private TruckAgent agent = null;

    private int xPos = 520, yPos = 200;//default bus position
    private int xDestination =520 , yDestination = 200;//default bus position
    
    private String buspic = "truck.png";
	private Image img;
	
	public static final int xRest1 = 705;
    public static final int yRest1 = 405;
    
    public static final int xRest2 = 705;
    public static final int yRest2 = 445;
    
    public static final int xRest3 = 855;
    public static final int yRest3 = 405;
    
    public static final int xRest4 = 855;
    public static final int yRest4 = 445;
    
    public static final int xRest5 = 1005;
    public static final int yRest5 = 405;
    
    public static final int xRest6 = 1005;
    public static final int yRest6 = 445;
    
    public static final int xTruckCrossing1 = 570;
    public static final int yTruckCrossing1 = 445;
    
    public static final int xTruckCrossing2 = 1125;
    public static final int yTruckCrossing2 = 445;
    
    public static final int xTruckCrossing3 = 1125;
    public static final int yTruckCrossing3 = 405;
    
    public static final int xTruckCrossing4 = 605;
    public static final int yTruckCrossing4 = 405;
    
    public static final int xMarket = 520;
    public static final int yMarket = 200;
    
    private AstarDriving astar;

    public TruckGui(TruckAgent agent,AstarDriving astar) {
        this.agent = agent;
        ImageIcon customer = new ImageIcon(this.getClass().getResource(buspic));
		img = customer.getImage();
        this.astar = astar;
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

        	
        if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xMarket) & (yDestination == yMarket)) {
           //agent.msgAtDest();
           agent.msgAtMarket();
        }

    }

    public void draw(Graphics2D g) {
    	g.drawImage(img,xPos,yPos,30,30,null);
        //g.setColor(Color.MAGENTA);
        //g.fillRect(xPos, yPos, 20, 20);
    	g.setColor(Color.red);
    	
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoTo(String start, String dest) {
    	List<Node> path;
    	List<Node> visited = new ArrayList<Node>();
    	boolean driving = false;
    	
    	path = astar.astar(astar.map.get(start), astar.map.get(dest));
    	while(!path.isEmpty()){
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
    				//astar.setVisited(path.get(1));
    				List<Node> temp = astar.astar(visited.get(visited.size()-1), astar.map.get(dest));
    				path.clear();
    				visited.clear();
    				for(Node n : temp){
    					path.add(n);
    	           	}
    			}
    		}
    }

    	agent.msgAtDest(dest);
    }
    
   
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void DoGoToCrossing1() {
		// TODO Auto-generated method stub
		xDestination = xTruckCrossing1;
		yDestination = yTruckCrossing1;
	}

	public void DoGoToCrossing2() {
		// TODO Auto-generated method stub
		xDestination = xTruckCrossing2;
		yDestination = yTruckCrossing2;
	}

	public void DoGoToCrossing3() {
		// TODO Auto-generated method stub
		xDestination = xTruckCrossing3;
		yDestination = yTruckCrossing3;
	}

	public void DoGoToCrossing4() {
		// TODO Auto-generated method stub
		xDestination = xTruckCrossing4;
		yDestination = yTruckCrossing4;
	}
	public void DoGoToMarket(){
		xDestination = xMarket;
		yDestination = yMarket;
	}
}
