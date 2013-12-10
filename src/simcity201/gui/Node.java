package simcity201.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Node {
    	int x;
    	int y;
    	boolean visited = false;
    	boolean occupied  = false;
    	boolean isTrafficLight = false;
    	Semaphore atNode = new Semaphore(1,true);
    	public List<Node> child = Collections.synchronizedList(new ArrayList<Node>());
    	Node lastNode;
    	Node(int x,int y){
    		this.x = x;
    		this.y = y;
    	}
    	
}
