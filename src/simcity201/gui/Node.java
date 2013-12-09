package simcity201.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {
    	int x;
    	int y;
    	boolean visited = false;
    	boolean occupied  = false;
    	public List<Node> child = Collections.synchronizedList(new ArrayList<Node>());
    	Node lastNode;
    	Node(int x,int y){
    		this.x = x;
    		this.y = y;
    	}
    	
//    	synchronized public void setOccupied(){
//    		occupied = true;
//    	}
//    	synchronized public void setUnOccupied(){
//    		occupied = false;
//    	}
//    	synchronized public boolean isOccupied(){
//    		return occupied;
//    	}
    

}
