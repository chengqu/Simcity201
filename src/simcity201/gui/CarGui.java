package simcity201.gui;


import agents.BusAgent;
import agents.PassengerAgent;
import agents.CarAgent;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.*;

import javax.swing.ImageIcon;

public class CarGui implements Gui {

    private CarAgent agent = null;

    public int xPos = 100, yPos = 100;//default bus position
    private int xDestination = 100, yDestination = 100;//default bus position
    
    private String buspic = "car.png";
	private Image img;
    
    
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
    
    class Node{
    	int x;
    	int y;
    	boolean visited = false;
    	public List<Node> child = new ArrayList<Node>();
    	Node lastNode;
    	Node(int x,int y){
    		this.x = x;
    		this.y = y;
    	}
    }
  
    
    
    private List<Node> nodes = new ArrayList<Node>();
    private List<Node> path  = new ArrayList<Node>();
    Map<String , Node> map = new HashMap<String, Node>();
        
   
    
    public CarGui(CarAgent agent) {
        this.agent = agent;
        ImageIcon customer = new ImageIcon(this.getClass().getResource(buspic));
		img = customer.getImage();
		//construct road map
    	nodes.add(new Node(0,0));		//0
    	nodes.add(new Node(40,0));		//1
    	nodes.add(new Node(570,0));		//2
    	nodes.add(new Node(605,0));		//3
    	nodes.add(new Node(1125,0));	//4
    	nodes.add(new Node(1165,0));	//5
    	nodes.add(new Node(0,40));		//6
    	nodes.add(new Node(40,40));		//7
    	nodes.add(new Node(570,40));	//8
    	nodes.add(new Node(605,40));	//9
    	nodes.add(new Node(1125,40));	//10
    	nodes.add(new Node(1165,40));	//11
    	nodes.add(new Node(0,405));		//12
    	nodes.add(new Node(40,405));	//13
    	nodes.add(new Node(570,405));	//14
    	nodes.add(new Node(605,405));	//15
    	nodes.add(new Node(1125,405));	//16
    	nodes.add(new Node(1165,405));	//17
    	nodes.add(new Node(0,445));		//18
    	nodes.add(new Node(40,445));	//19
    	nodes.add(new Node(570,445));	//20
    	nodes.add(new Node(605,445));	//21
    	nodes.add(new Node(1125,445));	//22
    	nodes.add(new Node(1165,445));	//23
    	nodes.add(new Node(0,755));		//24
    	nodes.add(new Node(40,755));	//25
    	nodes.add(new Node(570,755));	//26
    	nodes.add(new Node(605,755));	//27
    	nodes.add(new Node(1125,755));	//28
    	nodes.add(new Node(1165,755));	//29
    	nodes.add(new Node(0,795));		//30
    	nodes.add(new Node(40,795));	//31
    	nodes.add(new Node(570,795));	//32
    	nodes.add(new Node(605,795));	//33
    	nodes.add(new Node(1125,795));	//34
    	nodes.add(new Node(1165,795));	//35
    	nodes.add(new Node(300,40));	map.put("Bank", nodes.get(36));//36 Bank
    	nodes.add(new Node(570,200));  	map.put("Market", nodes.get(37));//37 Market
    	nodes.add(new Node(730,405));   map.put("Rest1", nodes.get(38));//38 Rest1
    	nodes.add(new Node(880,405));	map.put("Rest3", nodes.get(39));//39 Rest3
    	nodes.add(new Node(1030,405));	map.put("Rest5", nodes.get(40));//40 Rest5
    	nodes.add(new Node(730,445));	map.put("Rest2", nodes.get(41));//41 Rest2
    	nodes.add(new Node(880,445));	map.put("Rest4", nodes.get(42));//42 Rest4
    	nodes.add(new Node(1030,445));	map.put("Rest6", nodes.get(43));//43 Rest6
    	nodes.add(new Node(250,445));	map.put("Apart", nodes.get(44));//44 Apart
    	nodes.add(new Node(730,40));	map.put("House1", nodes.get(45));//45 House1
    	//end of adding nodes
    	
    	//set up road map
    	nodes.get(0).child.add(nodes.get(6));
    	nodes.get(1).child.add(nodes.get(0));
    	nodes.get(2).child.add(nodes.get(1)); nodes.get(2).child.add(nodes.get(8));
    	nodes.get(3).child.add(nodes.get(2));
    	nodes.get(4).child.add(nodes.get(3)); nodes.get(4).child.add(nodes.get(10));
    	nodes.get(5).child.add(nodes.get(4));
    	nodes.get(6).child.add(nodes.get(7)); nodes.get(6).child.add(nodes.get(12));
    	nodes.get(7).child.add(nodes.get(36)); nodes.get(7).child.add(nodes.get(1));
    	nodes.get(8).child.add(nodes.get(9)); nodes.get(8).child.add(nodes.get(37));
    	nodes.get(9).child.add(nodes.get(45));nodes.get(9).child.add(nodes.get(3));
    	nodes.get(10).child.add(nodes.get(11));nodes.get(10).child.add(nodes.get(16));
    	nodes.get(11).child.add(nodes.get(5));
    	nodes.get(12).child.add(nodes.get(18));
    	nodes.get(13).child.add(nodes.get(12));nodes.get(13).child.add(nodes.get(7));
    	nodes.get(14).child.add(nodes.get(13));nodes.get(14).child.add(nodes.get(20));
    	nodes.get(15).child.add(nodes.get(14));nodes.get(15).child.add(nodes.get(9));
    	nodes.get(16).child.add(nodes.get(40));nodes.get(16).child.add(nodes.get(22));
    	nodes.get(17).child.add(nodes.get(16));nodes.get(17).child.add(nodes.get(11));
    	nodes.get(18).child.add(nodes.get(19));nodes.get(18).child.add(nodes.get(24));
    	nodes.get(19).child.add(nodes.get(44));nodes.get(19).child.add(nodes.get(13));
    	nodes.get(20).child.add(nodes.get(21));nodes.get(20).child.add(nodes.get(26));
    	nodes.get(21).child.add(nodes.get(41));nodes.get(21).child.add(nodes.get(15));
    	nodes.get(22).child.add(nodes.get(23));nodes.get(22).child.add(nodes.get(28));
    	nodes.get(23).child.add(nodes.get(17));
    	nodes.get(24).child.add(nodes.get(30));
    	nodes.get(25).child.add(nodes.get(24));nodes.get(25).child.add(nodes.get(19));
    	nodes.get(26).child.add(nodes.get(25));nodes.get(26).child.add(nodes.get(32));
    	nodes.get(27).child.add(nodes.get(26));nodes.get(27).child.add(nodes.get(21));
    	nodes.get(28).child.add(nodes.get(27));nodes.get(28).child.add(nodes.get(34));
    	nodes.get(29).child.add(nodes.get(28));nodes.get(29).child.add(nodes.get(23));
    	nodes.get(30).child.add(nodes.get(31));
    	nodes.get(31).child.add(nodes.get(32));nodes.get(31).child.add(nodes.get(25));
    	nodes.get(32).child.add(nodes.get(33));
    	nodes.get(33).child.add(nodes.get(34));nodes.get(33).child.add(nodes.get(27));
    	nodes.get(34).child.add(nodes.get(35));
    	nodes.get(35).child.add(nodes.get(29));
    	nodes.get(36).child.add(nodes.get(8));
    	nodes.get(37).child.add(nodes.get(14));
    	nodes.get(38).child.add(nodes.get(15));
    	nodes.get(39).child.add(nodes.get(38));
    	nodes.get(40).child.add(nodes.get(39));
    	nodes.get(41).child.add(nodes.get(42));
    	nodes.get(42).child.add(nodes.get(43));
    	nodes.get(43).child.add(nodes.get(22));
    	nodes.get(44).child.add(nodes.get(20));
    	nodes.get(45).child.add(nodes.get(10));
    	
    	//end of contructing road map
    }
    
    public void astar(Node root, Node end){
    	List<Node> Queue = new ArrayList<Node>();
    	Node temp = end;
    	Queue.add(root);
    	while(!Queue.isEmpty()){
    		for(int i = 0; i<Queue.get(0).child.size();i++){
    			Node a = Queue.get(0).child.get(i);
    			if(a.visited == false){
    				Queue.add(a);
	    			a.lastNode = Queue.get(0);
    			}
    		}
    		Queue.get(0).visited = true;
    		Queue.remove(0);
    	}
    	while(temp != root){
    		path.add(temp);
    		temp = temp.lastNode;
    	}
    	path.add(root);
    	Collections.reverse(path);
    	for(Node n : nodes){
    		n.visited = false;
    	}
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

//        if (xPos == xDestination && yPos == yDestination
//        		& (xDestination == xBank) & (yDestination == yBank)) {
//           agent.msgAtDest();
//        }
//        if (xPos == xDestination && yPos == yDestination
//        		& (xDestination == xMarket) & (yDestination == yMarket)) {
//           agent.msgAtDest();
//        }
//        if (xPos == xDestination && yPos == yDestination
//        		& (xDestination == xApart) & (yDestination == yApart)) {
//           agent.msgAtDest();
//        }
//        if (xPos == xDestination && yPos == yDestination
//        		& (xDestination == xHouse1) & (yDestination == yHouse1)) {
//           agent.msgAtDest();
//        }
//        if (xPos == xDestination && yPos == yDestination
//        		& (xDestination == xHouse2) & (yDestination == yHouse2)) {
//           agent.msgAtDest();
//        }
//        if (xPos == xDestination && yPos == yDestination
//        		& (xDestination == xHouse3) & (yDestination == yHouse3)) {
//           agent.msgAtDest();
//        }
//        if (xPos == xDestination && yPos == yDestination
//        		& (xDestination == xRest1) & (yDestination == yRest1)) {
//           agent.msgAtDest();
//        }
//        if (xPos == xDestination && yPos == yDestination
//        		& (xDestination == xRest2) & (yDestination == yRest2)) {
//           agent.msgAtDest();
//        }
//        if (xPos == xDestination && yPos == yDestination
//        		& (xDestination == xRest3) & (yDestination == yRest3)) {
//           agent.msgAtDest();
//        }
//        if (xPos == xDestination && yPos == yDestination
//        		& (xDestination == xRest4) & (yDestination == yRest4)) {
//           agent.msgAtDest();
//        }
//        if (xPos == xDestination && yPos == yDestination
//        		& (xDestination == xRest5) & (yDestination == yRest5)) {
//           agent.msgAtDest();
//        }
//        if (xPos == xDestination && yPos == yDestination
//        		& (xDestination == xRest6) & (yDestination == yRest6)) {
//           agent.msgAtDest();
//        }
       

    }
    
    //public void 

    public void draw(Graphics2D g) {
    	g.drawImage(img,xPos,yPos,30,30,null);
    }

    public boolean isPresent() {
        return true;
    }
    
    public void DoDriveTo(String startDest,String dest){
    	astar(map.get(startDest), map.get(dest));
    	while(!path.isEmpty()){
    		if(xPos != path.get(0).x || yPos != path.get(0).y){
    			xDestination = path.get(0).x;
    			yDestination = path.get(0).y;
    		}
    		else if(xPos == path.get(0).x && yPos == path.get(0).y)
    			path.remove(0);
    	}
    	
    	agent.msgAtDest();
    }
    public void DoGoToPark(String dest){
    	if(dest == "Bank"){
            xDestination = xBank +30;
            yDestination = yBank +30;}
        	if(dest == "Market"){
                xDestination = xMarket +30;
                yDestination = yMarket +30;}
    	if(dest == "Rest1"){
            xDestination = xRest1 +30;
            yDestination = yRest1 +30;}
    	if(dest == "Rest2"){
            xDestination = xRest2 +30;
            yDestination = yRest2 +30;}
    	if(dest == "Rest3"){
            xDestination = xRest3 +30;
            yDestination = yRest3 +30;}
    	if(dest == "Rest4"){
            xDestination = xRest4 +30;
            yDestination = yRest4 +30;}
    	if(dest == "Rest5"){
            xDestination = xRest5 +30;
            yDestination = yRest5 +30;}
    	if(dest == "Rest6"){
            xDestination = xRest6 +30;
            yDestination = yRest6 +30;}
    	if(dest == "House1"){
            xDestination = xHouse1 +30;
            yDestination = yHouse1 +30;
    	}
    	if(dest == "House2"){
            xDestination = xHouse2 +30;
            yDestination = yHouse2 +30;
    	}
    	if(dest == "House3"){
            xDestination = xHouse3 +30;
            yDestination = yHouse3 +30;}
    	if(dest == "Apart"){
            xDestination = xApart +30;
            yDestination = yApart +30;}
    			
    }
    public void DoGoTo(String dest) {
    	if(dest == "Bank"){
            xDestination = xBank;
            yDestination = yBank;}
        	if(dest == "Market"){
                xDestination = xMarket;
                yDestination = yMarket;}
        	
    	if(dest == "Rest1"){
            xDestination = xRest1;
            yDestination = yRest1;}
    	if(dest == "Rest2"){
            xDestination = xRest2;
            yDestination = yRest2;}
    	if(dest == "Rest3"){
            xDestination = xRest3;
            yDestination = yRest3;}
    	if(dest == "Rest4"){
            xDestination = xRest4;
            yDestination = yRest4;}
    	if(dest == "Rest5"){
            xDestination = xRest5;
            yDestination = yRest5;}
    	if(dest == "Rest6"){
            xDestination = xRest6;
            yDestination = yRest6;}
    	if(dest == "House1"){
            xDestination = xHouse1;
            yDestination = yHouse1;
    	}
    	if(dest == "House2"){
            xDestination = xHouse2;
            yDestination = yHouse2;
    	}
    	if(dest == "House3"){
            xDestination = xHouse3;
            yDestination = yHouse3;}
    	if(dest == "Apart"){
            xDestination = xApart;
            yDestination = yApart;}
    	
    	
    }
    
   
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
