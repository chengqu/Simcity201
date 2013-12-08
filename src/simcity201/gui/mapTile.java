package simcity201.gui;

import Buildings.Building;
import agents.PassengerAgent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.ImageIcon;

public class mapTile
{
   public  int xCoordinate;
   public  int yCoordinate;
   public boolean isBuilding;
//   public int fScore;
//   public int gScore;
   public int hScore;
   //List of open tiles around currrent tile
   List<mapTile> openList=new ArrayList<mapTile>();
   //Path for gui to take
//   public List<mapTile> tilePath=new ArrayList<mapTile>();
//   PriorityQueue<mapTile> path = new PriorityQueue<mapTile>();
   //PARENT TILE (NODE)
   public mapTile parent;
   
   public mapTile(int xCoordinate, int yCoordinate, boolean isBuilding){
      this.xCoordinate=xCoordinate;
      this.yCoordinate=yCoordinate;
      this.isBuilding=isBuilding;
      parent=new mapTile();
   }
   public mapTile(){

   }
   
}

