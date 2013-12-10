package simcity201.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import agents.CarAgent;

public class AstarDriving {

	 
	  
	    
	    
	    private List<Node> nodes = Collections.synchronizedList(new ArrayList<Node>());
	    //public  List<Node> path  = Collections.synchronizedList(new ArrayList<Node>());
	    Map<String , Node> map = Collections.synchronizedMap(new HashMap<String, Node>());
	        
	   
	    
	    public AstarDriving() {
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
	    	nodes.add(new Node(880,40));	map.put("House2", nodes.get(46));//46 House2
	    	nodes.add(new Node(1030,40));	map.put("House3", nodes.get(47));//47 House3
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
	    	nodes.get(45).child.add(nodes.get(46));
	    	nodes.get(46).child.add(nodes.get(47));
	    	nodes.get(47).child.add(nodes.get(10));
	    	
	    	//end of contructing road map
	    }
	    synchronized public List<Node> node(){
	    	return nodes;
	    }
	    
	    synchronized public List<Node> astar(Node root, Node end){
	    	List<Node> Queue = new ArrayList<Node>();
	    	List<Node> path = new ArrayList<Node>();
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
	    	for(Node n : nodes){
	    		n.visited = false;
	    	}
	    	path.add(root);
	    	Collections.reverse(path);
	    	return path;
	    }
	   synchronized public void setOccupied(Node node){
		   for(Node n : nodes){
			   n.occupied = true;
		   }
	   }
	   synchronized public void setUnOccupied(Node node){
		   for(Node n : nodes){
			  n.occupied = false;
		   }
	   }
	synchronized public boolean isOccupied(Node node){
		  boolean x = false;
		   for(Node n : nodes){
			   x = n.occupied;
		   }
		   return x;
	   }
//	synchronized public void setAquired(Node node){
//		for(Node n : nodes){
//			  try {
//				n.atNode.acquire();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		   }
//	}
	synchronized public void setReleased(Node node){
		for(Node n : nodes){
			  n.atNode.release();
		   }
	}
}
