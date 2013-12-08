package simcity201.gui;

import Buildings.Building;
import agents.PassengerAgent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;


public class PassengerGui implements Gui{

   //AStar
   walkingAStar aStarMap=new walkingAStar();
   
	private PassengerAgent agent = null;
	private boolean isPresent = true;
	private boolean isHungry = false;
	private boolean hide = false;
	//private HostAgent host

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, walkToDest, walking};
	private Command command=Command.noCommand;
	
    class Destination {
    	Point p;
    	Command c;
    	public Destination(Point p, Command c) {
    		this.p = p;
    		this.c = c;
    	}
    }
    
    private List<Destination> destinations = new ArrayList<Destination>();
	
	
	private String passengerpic = "passenger.png";
	private Image img;
	
	public int xCar=1;
	public int yCar=1;
	
//	public static final int xBank = 300;
//	public static final int yBank = 40;
//	    
//	public static final int xMarket = 570;
//	public static final int yMarket = 200;
//	    
//	public static final int xHouse = 850;
//	public static final int yHouse = 0;
//	    
//	public static final int xRestaurants1 = 855;
//	public static final int yRestaurants1 = 445;
//	    
//	public static final int xRestaurants2 = 1165;
//	public static final int yRestaurants2 = 250;
//	
//	public static final int xBankfoot = 200;
//	public static final int yBankfoot = 120;
//	    
//	public static final int xMarketfoot = 400;
//	public static final int yMarketfoot = 160;
//	    
//	public static final int xApartfoot = 200;
//	public static final int yApartfoot= 525;
//    
//	public static final int xRest1 = 705;
//    public static final int yRest1 = 325;
//    
//    public static final int xRest2 = 705;
//    public static final int yRest2 = 475;
//    
//    public static final int xRest3 = 855;
//    public static final int yRest3 = 325;
//    
//    public static final int xRest4 = 855;
//    public static final int yRest4 = 475;
//    
//    public static final int xRest5 = 1005;
//    public static final int yRest5 = 325;
//    
//    public static final int xRest6 = 1005;
//    public static final int yRest6 = 475;
//	    
//    public static final int xHouse1 = 695;
//    public static final int yHouse1 = 130;
//    
//    public static final int xHouse2 = 845;
//    public static final int yHouse2 = 130;
//    
//    public static final int xHouse3 = 995;
//    public static final int yHouse3 = 130;
//    
//    public static final int xApart = 0;
//    public static final int yApart = 0;
	
	public static final int xBank = 300;
   public static final int yBank = 40;
       
   public static final int xMarket = 570;
   public static final int yMarket = 200;
       
   public static final int xHouse = 850;
   public static final int yHouse = 0;
       
   public static final int xRestaurants1 = 855;
   public static final int yRestaurants1 = 445;
       
   public static final int xRestaurants2 = 1165;
   public static final int yRestaurants2 = 250;
   
   public static final int xBankfoot = 200;
   public static final int yBankfoot = 120;
       
   public static final int xMarketfoot = 400;
   public static final int yMarketfoot = 160;
       
   public static final int xApartfoot = 200;
   public static final int yApartfoot= 525;
    
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
    
    public static final int xApart = 0;
    public static final int yApart = 0;

	public PassengerGui(PassengerAgent c){ //HostAgent m) {
		agent = c;
		xPos = 40;
		yPos = 40;
		xDestination = 40;
		yDestination = 40;
		ImageIcon customer = new ImageIcon(this.getClass().getResource(passengerpic));
		img = customer.getImage();
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

		if (xPos == xDestination && yPos == yDestination) {
			
			if (command == Command.walkToDest) {
				command = Command.noCommand;
				agent.msgAtDest();
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}
			if (command == Command.walking) {
			   command = Command.noCommand;
			}
			
			if (!destinations.isEmpty()) {
	    	   Destination dest = destinations.remove(0);
	    	   xDestination = (int)dest.p.getX();
	    	   yDestination = (int)dest.p.getY();
	    	   command = dest.c;
	       }
			
		}
		
		 if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == xCar) & (yDestination == yCar)) {
	          agent.msgAtCar();
	          System.out.println("Release the car semephore!!!!!!!!!!!!!");
	        }

	        if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == xBank) & (yDestination == yBank)) {
	         agent.msgAtStop();
	        }
	        if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == xMarket) & (yDestination == yMarket)) {
	         agent.msgAtStop();
	        }
	        if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == xHouse) & (yDestination == yHouse)) {
	         agent.msgAtStop();
	        }
	        
	        if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == xRestaurants1) & (yDestination == yRestaurants1)) {
	         agent.msgAtStop();
	        }
	        if (xPos == xDestination && yPos == yDestination
	        		& (xDestination == xRestaurants2) & (yDestination == yRestaurants2)) {
	         agent.msgAtStop();
	        }

	}

	public void draw(Graphics2D g) {
		if(hide == false){
			g.drawImage(img,xPos,yPos,null);}
		//g.setColor(Color.GREEN);
		//g.fillRect(xPos, yPos, 20, 20);}
	}

	public boolean isPresent() {
		return isPresent;
	}
	
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToCar(int x, int y){
		xDestination = x;
		yDestination = y;
		this.xCar = x;
		this.yCar = y;
	}
	public void DoEnterCar(){
		xDestination = xCar+2;
		yDestination = yCar+2;
	}
	public void DoGoToStop(String dest){
		if(dest == "Bank"){
	        xDestination = xBank;
	        yDestination = yBank;}
	    	if(dest == "Market"){
	            xDestination = xMarket;
	            yDestination = yMarket;}
	    	if(dest == "House"){
	            xDestination = xHouse;
	            yDestination = yHouse;}
	    	if(dest == "Restaurants1"){
	            xDestination = xRestaurants1;
	            yDestination = yRestaurants1;}
	    	if(dest == "Restaurants2"){
	            xDestination = xRestaurants2;
	            yDestination = yRestaurants2;}
	    	
	}
	public void DoWalkTo( String dest){
//		Building b = GlobalMap.getGlobalMap().searchByName(dest);
//		Point p = new Point(b.x, b.y);
//		destinations.add(
//				new Destination(p, Command.walkToDest));
	   
	   //mapTile start=aStarMap.buildingMap.get(startDest);
	   mapTile start=aStarMap.findMapTile(xPos,yPos);
	   mapTile destination=aStarMap.buildingMap.get(dest);
	   List<mapTile> path=aStarMap.findPath(start, destination);
	   
	   while(!path.isEmpty()) {
	      
	      int x = path.get(0).xCoordinate;
	      int y = path.get(0).yCoordinate;
	      
	      if (path.size() == 1) {
	         destinations.add(
	               new Destination(new Point(x,y), Command.walkToDest));
	      }
	      destinations.add(
	            new Destination(new Point(x,y), Command.walking));
	      path.remove(0);
	   }
	   
//      while(!path.isEmpty()){
//         if(xPos != path.get(0).xCoordinate || yPos != path.get(0).yCoordinate){
////            xDestination = path.get(0).x;
////            yDestination = path.get(0).y;
//            Point p = new Point(path.get(0).xCoordinate, path.get(0).yCoordinate);
//            destinations.add(
//                new Destination(p, Command.walkToDest));
//         }
//         else if(xPos==path.get(0).xCoordinate && yPos == path.get(0).yCoordinate)
//            path.remove(0);
//      }
//      
//      agent.msgAtDest();

	}
	public void hide() {
		// TODO Auto-generated method stub
		hide = true;
	}
	public void show(){
		hide = false;
	}
	
	public void getOff(String dest) {
		// TODO Auto-generated method stub
		hide = false;
		
		for(Building b: GlobalMap.getGlobalMap().getBuildings()) {
			if( dest.equalsIgnoreCase(b.name) ) {
				xPos =b.x; yPos = b.y;
				destinations.add(new Destination(new Point(b.x, b.y), Command.walkToDest));
				break;
			}
		}
	}
	public void DoWait(String dest){
		if(dest == "Bank"){
	        xDestination = xBank+1;
	        yDestination = yBank+1;
			}
	    	if(dest == "Market"){
	            xDestination = xMarket+1;
	            yDestination = yMarket+1;
	            }
	    	if(dest == "House"){
	            xDestination = xHouse+1;
	            yDestination = yHouse+1;
	            }
	    	if(dest == "Restaurants1"){
	            xDestination = xRestaurants1+1;
	            yDestination = yRestaurants1+1;
	            }
	    	if(dest == "Restaurants2"){
	            xDestination = xRestaurants2+1;
	            yDestination = yRestaurants2+1;
	            }
	}
	public void showCar(int x, int y){
		hide = false;
		 xDestination = x+1;
	     yDestination = y+1;
	     xPos = x+1;
	     yPos = y+1;
	}
	public void shodCar(String dest){
		hide = false;
		if(dest == "Bank"){
        xDestination = xBankfoot;
        yDestination = yBankfoot;
        xPos = xBankfoot;
        yPos = yBankfoot;
		}
    	if(dest == "Market"){
            xDestination = xMarketfoot;
            yDestination = yMarketfoot;
            xPos = xMarketfoot;
            yPos = yMarketfoot;
            }
    	if(dest == "House"){
            xDestination = xHouse;
            yDestination = yHouse;
            xPos = xHouse;
            yPos = yHouse;
            }
    	if(dest == "Restaurants1"){
            xDestination = xRestaurants1;
            yDestination = yRestaurants1;
            xPos = xRestaurants1;
            yPos = yRestaurants1;
            }
    	if(dest == "Restaurants2"){
            xDestination = xRestaurants2;
            yDestination = yRestaurants2;
            xPos = xRestaurants2;
            yPos = yRestaurants2;
            }
    	if(dest == "Rest1"){
            xDestination = xRest1;
            yDestination = yRest1;
            xPos = xRest1;
            yPos = yRest1;
            }
    	if(dest == "Rest2"){
            xDestination = xRest2;
            yDestination = yRest2;
            xPos = xRest2;
            yPos = yRest2;
            }
    	if(dest == "Rest3"){
            xDestination = xRest3;
            yDestination = yRest3;
            xPos = xRest3;
            yPos = yRest3;
            }
    	if(dest == "Rest4"){
            xDestination = xRest4;
            yDestination = yRest4;
            xPos = xRest4;
            yPos = yRest4;
            }
    	if(dest == "Rest5"){
            xDestination = xRest5;
            yDestination = yRest5;
            xPos = xRest5;
            yPos = yRest5;
            }
    	if(dest == "Rest6"){
            xDestination = xRest6;
            yDestination = yRest6;
            xPos = xRest6;
            yPos = yRest6;
    	}
    	if(dest == "House1"){
            xDestination = xHouse1;
            yDestination = yHouse1;
            xPos = xHouse1;
            yPos = yHouse1;
    	}
    	if(dest == "House2"){
            xDestination = xHouse2;
            yDestination = yHouse2;
            xPos = xHouse2;
            yPos = yHouse2;
    	}
    	if(dest == "House3"){
            xDestination = xHouse3;
            yDestination = yHouse3;
            xPos = xHouse3;
            yPos = yHouse3;
    	}
    	if(dest == "Apart"){
            xDestination = xApart;
            yDestination = yApart;
            xPos = xApart;
            yPos = yApart;
            
    	}
	}
	public void showBus(String dest){
		hide = false;
		if(dest == "Bank"){
        xDestination = xBank;
        yDestination = yBank;
        xPos = xBank;
        yPos = yBank;
		}
    	if(dest == "Market"){
            xDestination = xMarket;
            yDestination = yMarket;
            xPos = xMarket;
            yPos = yMarket;
            }
    	if(dest == "House"){
            xDestination = xHouse;
            yDestination = yHouse;
            xPos = xHouse;
            yPos = yHouse;
            }
    	if(dest == "Restaurants1"){
            xDestination = xRestaurants1;
            yDestination = yRestaurants1;
            xPos = xRestaurants1;
            yPos = yRestaurants1;
            }
    	if(dest == "Restaurants2"){
            xDestination = xRestaurants2;
            yDestination = yRestaurants2;
            xPos = xRestaurants2;
            yPos = yRestaurants2;
            }
    	if(dest == "Rest1"){
            xDestination = xRest1;
            yDestination = yRest1;
            xPos = xRest1;
            yPos = yRest1;
            }
    	if(dest == "Rest2"){
            xDestination = xRest2;
            yDestination = yRest2;
            xPos = xRest2;
            yPos = yRest2;
            }
    	if(dest == "Rest3"){
            xDestination = xRest3;
            yDestination = yRest3;
            xPos = xRest3;
            yPos = yRest3;
            }
    	if(dest == "Rest4"){
            xDestination = xRest4;
            yDestination = yRest4;
            xPos = xRest4;
            yPos = yRest4;
            }
    	if(dest == "Rest5"){
            xDestination = xRest5;
            yDestination = yRest5;
            xPos = xRest5;
            yPos = yRest5;
            }
    	if(dest == "Rest6"){
            xDestination = xRest6;
            yDestination = yRest6;
            xPos = xRest6;
            yPos = yRest6;
    	}
    	if(dest == "House1"){
            xDestination = xHouse1;
            yDestination = yHouse1;
            xPos = xHouse1;
            yPos = yHouse1;
    	}
    	if(dest == "House2"){
            xDestination = xHouse2;
            yDestination = yHouse2;
            xPos = xHouse2;
            yPos = yHouse2;
    	}
    	if(dest == "House3"){
            xDestination = xHouse3;
            yDestination = yHouse3;
            xPos = xHouse3;
            yPos = yHouse3;
    	}
    	if(dest == "Apart"){
            xDestination = xApart;
            yDestination = yApart;
            xPos = xApart;
            yPos = yApart;
            
    	}
	}
}
