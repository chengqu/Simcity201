package simcity201.gui;

import Buildings.Building;
import agents.PassengerAgent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.swing.ImageIcon;

public class walkingAStar
{
      private Map<Integer,mapTile> tileNames= new HashMap<Integer, mapTile>();
      public mapTile[][] map=new mapTile[10][11];
      public Map<String,mapTile> buildingMap=new HashMap<String, mapTile>();
//      public static final int bankX=305;
//      public static final int bankY=40;
//      public static final int marketX=305;
//      public static final int marketY=445;
//      public static final int apartmentX=305;
//      public static final int apartmentY=755;
//      public static final int houseX=730;
//      public static final int houseY=40;
//      public static final int rest1X=730;
//      public static final int rest1Y=405;
//      public static final int rest2X=880;
//      public static final int rest2Y=405;
//      public static final int rest3X=1030;
//      public static final int rest3Y=405;
//      public static final int rest4X=730;
//      public static final int rest4Y=445;
//      public static final int rest5X=880;
//      public static final int rest5Y=445;
//      public static final int rest6X=1030;
//      public static final int rest6Y=445;
      
      
      
      public walkingAStar(){      
         int indexCounter=1;
         for(int i=0;i<10;i++){
               for(int j=0;j<11;j++){
                  int xCoordinate=0, yCoordinate=0;
                  
                  switch (i){
                  case 0:  yCoordinate=70;
                           break;
                  case 1:  yCoordinate=100;
                           break;
                  case 2:  yCoordinate=230;
                           break;
                  case 3: yCoordinate=300;
                           break;
                  case 4:  yCoordinate=375;
                           break;
                  case 5: yCoordinate=420;
                           break;
                  case 6: yCoordinate=475;
                           break;
                  case 7:  yCoordinate=490;
                           break;
                  case 8:  yCoordinate=735;
                           break;
                  case 9:  yCoordinate=825;
                           break;
                           
                  default: break;
                  }
                  
                  switch (j){
                  case 0:  xCoordinate=70;
                           break;
//                  case 1:  xCoordinate=100;
//                           break;
                  case 1:  xCoordinate=335;
                           break;
                  case 2:  xCoordinate=540;
                           break;
                  case 3:  xCoordinate=590;
                           break;
                  case 4:  xCoordinate=635;
                           break;
                  case 5:  xCoordinate=700;
                           break;
                  case 6: xCoordinate=800;
                           break;
                  case 7:  xCoordinate=850;
                           break;
                  case 8: xCoordinate=950;
                           break;
                  case 9:  xCoordinate=1000;
                           break;
                  case 10:  xCoordinate=1095;
                           break;
//                  case 9:  xCoordinate=1165;
//                           break;
                  default: break;
                  }
                  mapTile temp=new mapTile(xCoordinate,yCoordinate, false);
                  map[i][j]=temp;
               }
            }
         for(int i=0;i<10;i++){
            for(int j=0;j<11;j++){
               tileNames.put(indexCounter,map[i][j]);
               indexCounter++;
            }
         }
         
         buildingMap.put("Bank", map[1][0]);
         buildingMap.put("Market", map[2][1]);
//         buildingMap.put("Market2BIG",map[2][2]);
         buildingMap.put("Apart", map[6][1]);
         buildingMap.put("House1", map[1][5]);
         buildingMap.put("House2", map[1][7]);
         buildingMap.put("House3", map[1][9]);
         buildingMap.put("Rest1", map[3][5]);
         buildingMap.put("Rest2", map[3][7]);
         buildingMap.put("Rest3", map[3][9]);
         buildingMap.put("Rest4", map[6][5]);
         buildingMap.put("Rest5", map[6][7]);
         buildingMap.put("Rest6", map[6][9]);
         
         buildingMap.get("Bank").isBuilding=true;
         buildingMap.get("Market").isBuilding=true;
//         buildingMap.get("Market2BIG").isBuilding=true;
         buildingMap.get("Apart").isBuilding=true;
         buildingMap.get("House1").isBuilding=true;
         buildingMap.get("House2").isBuilding=true;
         buildingMap.get("House3").isBuilding=true;
         buildingMap.get("Rest1").isBuilding=true;
         buildingMap.get("Rest2").isBuilding=true;
         buildingMap.get("Rest3").isBuilding=true;
         buildingMap.get("Rest4").isBuilding=true;
         buildingMap.get("Rest5").isBuilding=true;
         buildingMap.get("Rest6").isBuilding=true;
         
         map[0][3].isBuilding=true;
         map[1][3].isBuilding=true;
         map[2][3].isBuilding=true;
         map[3][3].isBuilding=true;
//         map[6][3].isBuilding=true;
         map[7][3].isBuilding=true;
         map[8][3].isBuilding=true;
         map[9][3].isBuilding=true;


         
         map[5][0].isBuilding=true;
         map[5][1].isBuilding=true;
         map[5][5].isBuilding=true;
         map[5][6].isBuilding=true;
         map[5][7].isBuilding=true;
         map[5][8].isBuilding=true;
         map[5][9].isBuilding=true;
         map[5][10].isBuilding=true;
         
         
         
         map[0][3].isTrafficLight=true;
         map[1][3].isTrafficLight=true;
         map[2][3].isTrafficLight=true;
         map[3][3].isTrafficLight=true;
         map[4][3].isTrafficLight=true;
         map[5][3].isTrafficLight=true;
         map[6][3].isTrafficLight=true;
         map[7][3].isTrafficLight=true;
         map[8][3].isTrafficLight=true;
         map[9][3].isTrafficLight=true;
         
         map[5][0].isTrafficLight=false;
         map[5][1].isTrafficLight=false;
         map[5][2].isTrafficLight=false;
         map[5][3].isTrafficLight=false;
         map[5][4].isTrafficLight=false;
         map[5][5].isTrafficLight=false;
         map[5][6].isTrafficLight=false;
         map[5][7].isTrafficLight=false;
         map[5][8].isTrafficLight=false;
         map[5][9].isTrafficLight=false;
         map[5][10].isTrafficLight=false;
         
      }
      
   
   
   
   
   //GETTER/SETTERS
   public Map<Integer,mapTile> getTileNames(){
      return tileNames;
   }
   //END GETTER/SETTERS
   
   //HELPER FUNCTIONS
   public mapTile findMapTile(int x, int y){
      mapTile temp=new mapTile();
      for(int i=0;i<10;i++){
         for(int j=0;j<11;j++){
            if(map[i][j].xCoordinate==x && map[i][j].yCoordinate==y){
               temp=map[i][j];
               break;
            }
         }
      }
      return temp;
   }
   
   public int findIndex(mapTile tile){
      int index=0;
      for(int i=1;i<=110;i++){
         if(tileNames.get(i)==tile){
            index=i;
            break;
         }
      }
      return index;
   }
   
   public int findHScore(mapTile start, mapTile destination){
      int hScore=0;
      int startX=start.xCoordinate;
      int startY=start.yCoordinate;
      int endX=destination.xCoordinate;
      int endY=destination.yCoordinate;
      
      hScore=10*(Math.abs(startX-endX)+Math.abs(startY-endY));
      return hScore;
   
   }
   
   public void findSurroundingTiles(int tileIndex){
      mapTile current=tileNames.get(tileIndex);
      boolean up=true,down=true,left=true,right=true;
      if(tileIndex%6==1){
         left=false;
      }
      if(tileIndex%6==0){
         right=false;
      }
      if(tileIndex<=6){
         up=false;
      }
      if(tileIndex>=31){
         down=false;
      }
      
      if(left!=false){
         current.openList.add(tileNames.get(tileIndex-1));
      }
      if(right!=false){
         current.openList.add(tileNames.get(tileIndex-1));
      }
      if(up!=false){
         current.openList.add(tileNames.get(tileIndex-6));
      }
      if(up!=false){
         current.openList.add(tileNames.get(tileIndex+6));
      }
   }
   
   private mapTile calculateBestTile(mapTile current_, int endIndex){
      mapTile current=current_;
      mapTile destination=tileNames.get(endIndex);
      mapTile bestTile=new mapTile();
      int bestScore=0;
      for(int i=0;i<current.openList.size();i++){
         int currentScore=findHScore(current.openList.get(i),destination);
         if(currentScore>bestScore){
            bestScore=currentScore;
            bestTile=current.openList.get(i);
         }
      }
      bestTile.parent=current;
      return bestTile;
   }
   
   //
   //MAIN FIND PATH FUNCTION
   synchronized public List<mapTile> findPath(mapTile start, mapTile destination){
      boolean buildingInWay=false;
      boolean searchedOnce=false;
      
      int searchLimit=0;
      
      List<mapTile> path=Collections.synchronizedList(new ArrayList<mapTile>());
      
      List<mapTile> openList=Collections.synchronizedList(new ArrayList<mapTile>());
      List<mapTile> closedList=Collections.synchronizedList(new ArrayList<mapTile>());
      
//      mapTile start=findMapTile(startX,startY);
//      mapTile destination=findMapTile(targetX,targetY);
      mapTile current=new mapTile();
      
      int startIndex=findIndex(start);      
      int endIndex=findIndex(destination);
      int currentIndex=0;
      
      currentIndex=startIndex;
      current=start;
      current.hScore=findHScore(current,destination);
      
      //openList.add(current); //Add starting tile into openList
      
      if(start==destination){
         return path;
      }
      
      //System.out.println("Set current tile to starting tile");
      
      while((!closedList.contains(destination) || openList.isEmpty()) && searchLimit<=50){
         //Now check surrounding tiles and add to openList is accessible  
//         System.out.println("Start Tile: "+findIndex(start)+"; End Tile: "+findIndex(destination));
//         System.out.println("END xCoordinate: "+destination.xCoordinate+"; END yCoordinate:"+destination.yCoordinate);
//
//         System.out.println("Current Tile:"+findIndex(current)+" Score: "+current.hScore);
//         System.out.println("CURRENT xCoordinate: "+current.xCoordinate+"; CURRENT yCoordinate:"+current.yCoordinate);
//         System.out.println("Open List: ");
//         
//         //Case if there is a building in the best path. recalculate
//         for(int i=0;i<openList.size();i++){
//            System.out.println(findIndex(openList.get(i))+", isBuilding: "+openList.get(i).isBuilding + ", hScore:" + openList.get(i).hScore);
//            System.out.println("xCoordinate: "+openList.get(i).xCoordinate+"; yCoordinate:"+openList.get(i).yCoordinate);
//         }
//         
//         System.out.println("\n\n");
         
         boolean movedToNextTile=false;
         
         synchronized(openList){
            for(int i=0;i<openList.size();i++){
               if(openList.get(i).hScore<current.hScore){
                  if(openList.get(i).isBuilding==true && openList.get(i)!=destination){
                     //openList.remove(i);
    //                 buildingInWay=true;
                     continue;
                  }
                  else{
                     current=openList.get(i);
                     currentIndex=findIndex(current);
                     openList.remove(current);
                     //openList.clear();
                     closedList.add(current);
                     movedToNextTile=true;
                  }
   //               current=openList.get(i);
   //               currentIndex=findIndex(current);
   //               openList.remove(current);
   //               //openList.clear();
   //               closedList.add(current);
   //               if(current.isBuilding==true && current!=destination){
   //                  buildingInWay=true;
   //               }
                  //System.out.println(findIndex(openList.get(i)));
                  //System.out.println("changed current tile to the one with best g score on the open list");
               }
            }
         }
         
         if(current==start){
            if(searchedOnce==false){
               movedToNextTile=true;
            }
            else{
               movedToNextTile=false;
            }
         }
         
         
         if(movedToNextTile==false){
            //System.out.println("\n\nBLAHBLAISODIJASLDJ\n\n");
//            mapTile parent=current.parent;
//          openList.add(parent);
          //  openList.add(current);
            current=findNextLowestScore(openList,current);
             currentIndex=findIndex(current);
             openList.remove(current);
             closedList.add(current);
             movedToNextTile=true;
         }
//         if(buildingInWay==true){
////            mapTile parent=current.parent;
////            openList.add(parent);
//            current=findNextLowestScore(openList,current);
//            currentIndex=findIndex(current);
//            openList.remove(current);
//            closedList.add(current);
//            buildingInWay=false;
//         }
         
//         if(tileNames.get(currentIndex-1)!=null){
         if(currentIndex%11!=1){
               mapTile leftTile=tileNames.get(currentIndex-1);
               if(!openList.contains(leftTile) && !closedList.contains(leftTile)){
                  leftTile.parent=current;
                  leftTile.hScore=findHScore(leftTile,destination);
                  openList.add(leftTile);
               }
            
         }
//         if(tileNames.get(currentIndex+1)!=null){ 
         if(currentIndex%11!=0){
               mapTile rightTile=tileNames.get(currentIndex+1);
               if(!openList.contains(rightTile) && !closedList.contains(rightTile)){
                  rightTile.parent=current;
                  rightTile.hScore=findHScore(rightTile,destination);
                  openList.add(rightTile);
               }
            
         }
         if(tileNames.get(currentIndex-11)!=null){
               mapTile upTile=tileNames.get(currentIndex-11);
               if(!openList.contains(upTile) && !closedList.contains(upTile)){
                  upTile.parent=current;
                  upTile.hScore=findHScore(upTile,destination);
                  openList.add(upTile);
               }
            
         }
         if(tileNames.get(currentIndex+11)!=null){
               mapTile downTile=tileNames.get(currentIndex+11);
               if(!openList.contains(downTile) && !closedList.contains(downTile)){
                  downTile.parent=current;
                  downTile.hScore=findHScore(downTile,destination);
                  openList.add(downTile);
               }
            
         }
         
//         if(tileNames.get(currentIndex-1)!=null){
//            if(tileNames.get(currentIndex-1).isBuilding==false ||  (tileNames.get(currentIndex-1).isBuilding==true && tileNames.get(currentIndex-1)==destination)){        
//               mapTile leftTile=tileNames.get(currentIndex-1);
//               if(!openList.contains(leftTile) && !closedList.contains(leftTile)){
//                  leftTile.parent=current;
//                  leftTile.hScore=findHScore(leftTile,destination);
//                  openList.add(leftTile);
//               }
//            }
//            else{
//               buildingInWay=true;
//            }
//         }
//         if(tileNames.get(currentIndex+1)!=null){ 
//            if(tileNames.get(currentIndex+1).isBuilding==false ||  (tileNames.get(currentIndex+1).isBuilding==true && tileNames.get(currentIndex+1)==destination)){
//               mapTile rightTile=tileNames.get(currentIndex+1);
//               if(!openList.contains(rightTile) && !closedList.contains(rightTile)){
//                  rightTile.parent=current;
//                  rightTile.hScore=findHScore(rightTile,destination);
//                  openList.add(rightTile);
//               }
//            }
//            else{
//               buildingInWay=true;
//            }
//         }
//         if(tileNames.get(currentIndex-8)!=null){
//            if(tileNames.get(currentIndex-8).isBuilding==false ||  (tileNames.get(currentIndex-8).isBuilding==true && tileNames.get(currentIndex-8)==destination)){
//               mapTile upTile=tileNames.get(currentIndex-8);
//               if(!openList.contains(upTile) && !closedList.contains(upTile)){
//                  upTile.parent=current;
//                  upTile.hScore=findHScore(upTile,destination);
//                  openList.add(upTile);
//               }
//            }
//            else{
//               buildingInWay=true;
//            }
//         }
//         if(tileNames.get(currentIndex+8)!=null){
//            if(tileNames.get(currentIndex+8).isBuilding==false ||  (tileNames.get(currentIndex+8).isBuilding==true && tileNames.get(currentIndex+8)==destination)){               
//               mapTile downTile=tileNames.get(currentIndex+8);
//               if(!openList.contains(downTile) && !closedList.contains(downTile)){
//                  downTile.parent=current;
//                  downTile.hScore=findHScore(downTile,destination);
//                  openList.add(downTile);
//               }
//            }
//            else{
//               buildingInWay=true;
//            }
//         }
         
//         System.out.println("added all available adjacent tiles to open list with g score");
//         for(int i=0;i<openList.size();i++){
//          System.out.println("Tile: "+findIndex(openList.get(i))+"; hScore: "+openList.get(i).hScore);
//       }
         
         
//         System.out.println("removed current tile from open list and added to closed list");
//         System.out.println("OPEN LIST SIZE: "+openList.size());
//         System.out.println("ClOSED LIST SIZE: "+closedList.size());
         
         searchedOnce=true;
         searchLimit++;
      }
      
      if(searchLimit<=50){
         mapTile temp=destination;
         while(temp!=start){
            path.add(temp);
            temp=temp.parent;
     //       System.out.println("added tile to the path");
         }
      }
      path.add(start);
  //    System.out.println("traversed the path and added it to the path tile list"+"\n");
      Collections.reverse(path);
      return path;
      
      //startIndex=findIndex(start);
      //
      //endIndex=findIndex(destination);
      //
      //current=start;
      //currentIndex=startIndex;
      //
      //while(currentIndex!=endIndex){
      //   findSurroundingTiles(currentIndex);
      //   current.openList.remove(current.parent);
      //   current=calculateBestTile(current,endIndex);
      //   currentIndex=findIndex(current);
      //}
      //
      //while(current.parent!=null){
      //   start.path.add(current.parent);
      //   current=current.parent;
      //}
      //
      //for(int i=0;i<start.path.size();i++){
      //   start.tilePath.add(start.path.remove());
      //}
      //return start.tilePath;
   }
   
   public mapTile findClosestTile(int xCoordinate, int yCoordinate){
      double lowestDistance;
   
      int firstXDifference=xCoordinate-tileNames.get(1).xCoordinate;
      int firstYDifference=yCoordinate-tileNames.get(1).yCoordinate;
      double firstDistance=Math.sqrt( Math.pow(firstYDifference, 2)+Math.pow(firstYDifference, 2));
      
      lowestDistance=firstDistance;
      mapTile closestTile=tileNames.get(1);
      
      for(int i=2;i<=tileNames.size();i++){
         
         int xDifference=xCoordinate-tileNames.get(i).xCoordinate;
         int yDifference=yCoordinate-tileNames.get(i).yCoordinate;
         double currentDistance=Math.sqrt( Math.pow(xDifference, 2)+Math.pow(yDifference, 2));
         if(currentDistance<lowestDistance){
            lowestDistance=currentDistance;
            closestTile=tileNames.get(i);
         }
      }
      if(closestTile==buildingMap.get("Market")){
         closestTile=map[1][1];
      }
      if(closestTile==buildingMap.get("House1")){
         closestTile=map[1][4];
      }
      if(closestTile==buildingMap.get("House2")){
         closestTile=map[1][6];
      }
      if(closestTile==buildingMap.get("House3")){
         closestTile=map[1][8];
      }
      return closestTile;
     
   }
   
   public mapTile findNextLowestScore(List<mapTile> openList, mapTile current){
//      for(int i=0;i<openList.size();i++){
//         System.out.println(findIndex(openList.get(i)));
//      }
      int nextLowestScore=1000000000;
      mapTile nextLowestScoreMapTile=current;
      for(int i=0;i<openList.size();i++){
         int hScore=openList.get(i).hScore; 
         mapTile temp=openList.get(i);
         if(openList.get(i).isBuilding==false){
            nextLowestScore=hScore;
            nextLowestScoreMapTile=temp;
         }
      }
      
      for(int i=1;i<openList.size();i++){
         if(openList.get(i)!=current && (openList.get(i).hScore<nextLowestScore)){
            if(openList.get(i).isBuilding==false){
               nextLowestScore=openList.get(i).hScore;
               nextLowestScoreMapTile=openList.get(i);
            }
         }
      }
      return nextLowestScoreMapTile;
   }
   
   synchronized public void setTileAccordingToLight(){
//     if(greenStreet=="verticalStreet"){
//        map[3][3].isBuilding=true;
//        map[5][3].isBuilding=true;
//        map[4][2].isBuilding=false;
//        map[4][4].isBuilding=false;
//     }
//     else if(greenStreet=="horizontalStreet"){
//        map[4][2].isBuilding=true;
//        map[4][4].isBuilding=true;
//        map[3][3].isBuilding=false;
//        map[5][3].isBuilding=false;
//     }
      
      
      
      
      if(map[3][3].isTrafficLight==false ){
        
         map[0][3].isTrafficLight=true;
         map[1][3].isTrafficLight=true;
         map[2][3].isTrafficLight=true;
         map[3][3].isTrafficLight=true;
         map[4][3].isTrafficLight=true;
         map[5][3].isTrafficLight=true;
         map[6][3].isTrafficLight=true;
         map[7][3].isTrafficLight=true;
         map[8][3].isTrafficLight=true;
         map[9][3].isTrafficLight=true;
      }
      else{
         map[0][3].isTrafficLight=false;
         map[1][3].isTrafficLight=false;
         map[2][3].isTrafficLight=false;
         map[3][3].isTrafficLight=false;
         map[4][3].isTrafficLight=false;
         map[5][3].isTrafficLight=false;
         map[6][3].isTrafficLight=false;
         map[7][3].isTrafficLight=false;
         map[8][3].isTrafficLight=false;
         map[9][3].isTrafficLight=false;
      }
      
      
      if(map[5][2].isTrafficLight==false ){
         map[5][0].isTrafficLight=true;
         map[5][1].isTrafficLight=true;
         map[5][2].isTrafficLight=true;
         map[5][3].isTrafficLight=true;
         map[5][4].isTrafficLight=true;
         map[5][5].isTrafficLight=true;
         map[5][6].isTrafficLight=true;
         map[5][7].isTrafficLight=true;
         map[5][8].isTrafficLight=true;
         map[5][9].isTrafficLight=true;
         map[5][10].isTrafficLight=true;
      }
      else{
         map[5][0].isTrafficLight=false;
         map[5][1].isTrafficLight=false;
         map[5][2].isTrafficLight=false;
         map[5][3].isTrafficLight=false;
         map[5][4].isTrafficLight=false;
         map[5][5].isTrafficLight=false;
         map[5][6].isTrafficLight=false;
         map[5][7].isTrafficLight=false;
         map[5][8].isTrafficLight=false;
         map[5][9].isTrafficLight=false;
         map[5][10].isTrafficLight=false;
      }
   }
//   public List<mapTile> testSearch(mapTile tile, mapTile destination){
//      List<mapTile> openList=new ArrayList<mapTile>();
//      List<mapTile> closedList=new ArrayList<mapTile>();
//            
//      int currentIndex=findIndex(tile);
//      mapTile current=tile;
//      
//      
//      if(tileNames.get(currentIndex-1)!=null){
//         mapTile leftTile=tileNames.get(currentIndex-1);
//         openList.add(leftTile);
//         leftTile.parent=current;
//         leftTile.hScore=findHScore(leftTile,destination);
//      }
//      if(tileNames.get(currentIndex+1)!=null){
//         mapTile rightTile=tileNames.get(currentIndex+1);
//         openList.add(rightTile);
//         rightTile.parent=current;
//         rightTile.hScore=findHScore(rightTile,destination);
//      }
//      if(tileNames.get(currentIndex-9)!=null){
//         mapTile upTile=tileNames.get(currentIndex-9);
//         openList.add(upTile);
//         upTile.parent=current;
//         upTile.hScore=findHScore(upTile,destination);
//      }
//      if(tileNames.get(currentIndex+9)!=null){
//         mapTile downTile=tileNames.get(currentIndex+9);
//         openList.add(downTile);
//         downTile.parent=current;
//         downTile.hScore=findHScore(downTile,destination);
//      }
//      
//      return openList;
//   }
   //TILE MAP 
   public static void main (String[] args){
      walkingAStar simCity=new walkingAStar();
      int indexCounter=0;
      List<mapTile> path=new ArrayList<mapTile>();
//      for(int i=0;i<6;i++){     
//         for(int j=0;j<6;j++){
////            System.out.println("Row: "+i+"; Column: "+j+"\n"+"xCoordinate: "+simCity.map[i][j].xCoordinate+" ; yCoordinate: "+simCity.map[i][j].yCoordinate);
//              System.out.println("Row: "+i+"; Column: "+j+"\n"+"Tile: "+simCity.getTileNames().get(indexCounter));
//              indexCounter++;
//         }
//      }
      
//      for(int i=0;i<6;i++){
//         for(int j=0;j<10;j++){
//            System.out.println("Tile Index: "+ simCity.findIndex(simCity.map[i][j]));
//         }
//      }
      
//      System.out.println(simCity.findMapTile(40,40).);
//      System.out.println(simCity.findMapTile(570,0));
//      simCity.findSurroundingTiles(3);
//      List<mapTile> openList_=simCity.getTileNames().get(3).openList;
//      for(int i=0;i<openList_.size();i++){
//         int index=simCity.findIndex(openList_.get(i));
//         System.out.println(index);
//      }
      
      
      int xPos=40,yPos=40;
//      System.out.println("Building: "+simCity.buildingMap.get("Bank")+"; Index: "+simCity.findIndex(simCity.buildingMap.get("Bank")));
//      System.out.println("Building: "+simCity.buildingMap.get("Market")+"; Index: "+simCity.findIndex(simCity.buildingMap.get("Market")));
//      System.out.println("Building: "+simCity.buildingMap.get("Apart")+"; Index: "+simCity.findIndex(simCity.buildingMap.get("Apart")));
//      System.out.println("Building: "+simCity.buildingMap.get("House1")+"; Index: "+simCity.findIndex(simCity.buildingMap.get("House1")));
//      System.out.println("Building: "+simCity.buildingMap.get("House2")+"; Index: "+simCity.findIndex(simCity.buildingMap.get("House2")));
//      System.out.println("Building: "+simCity.buildingMap.get("House3")+"; Index: "+simCity.findIndex(simCity.buildingMap.get("House3")));
//      System.out.println("Building: "+simCity.buildingMap.get("Rest1")+"; Index: "+simCity.findIndex(simCity.buildingMap.get("Rest4")));
//      System.out.println("Building: "+simCity.buildingMap.get("Rest2")+"; Index: "+simCity.findIndex(simCity.buildingMap.get("Rest5")));
//      System.out.println("Building: "+simCity.buildingMap.get("Rest3")+"; Index: "+simCity.findIndex(simCity.buildingMap.get("Rest6")));
      
      mapTile start=simCity.buildingMap.get("House1");
      mapTile destination=simCity.buildingMap.get("Market");
      System.out.println("PATH 1:\n");
   //   path=simCity.findPath(start, destination);
      
//    for(int i=0;i<path.size();i++){
//       System.out.println("Tile: "+simCity.findIndex(path.get(i))+"; hScore: "+path.get(i).hScore);
//    }
      
 //     simCity.findClosestTile(40, 40);
//    System.out.println("PATH 2:\n");
    
    path=simCity.findPath(simCity.buildingMap.get("Bank"), simCity.buildingMap.get("House1"));
  //  path=simCity.findPath(simCity.getTileNames().get(13), simCity.getTileNames().get(74));
  //    path=simCity.findPath(start, destination);
//  
    for(int i=0;i<path.size();i++){
       System.out.println("Tile: "+simCity.findIndex(path.get(i))+"; hScore: "+path.get(i).hScore);
    }
    
    for(int i=1;i<=simCity.getTileNames().size();i++){
       if(simCity.getTileNames().get(i).isBuilding==true){
          System.out.println(i);
       }
    }
    
//    System.out.println("PATH 3:\n");
//    path=simCity.findPath(simCity.buildingMap.get("Apart"), simCity.buildingMap.get("Rest5"));
//    
//    for(int i=0;i<path.size();i++){
//       System.out.println("Tile: "+simCity.findIndex(path.get(i))+"; hScore: "+path.get(i).hScore);
//    }
      
//      int xPos=1325,yPos=215;
//      mapTile closestTile=simCity.findClosestTile(xPos,yPos);
//      System.out.println("Closest Tile: "+simCity.findIndex(closestTile));
    
//      for(int i=0;i<path.size();i++){
//         System.out.println("Tile: "+simCity.findIndex(path.get(i)));
//      }
      
      
//        System.out.println(simCity.buildingMap.get("Bank"));
//        System.out.println(simCity.findMapTile(40,40));
      
//      path=simCity.testSearch(simCity.map[4][1],simCity.map[3][0]);
//      for(int i=0;i<path.size();i++){
//         System.out.println("Tile: "+simCity.findIndex(path.get(i))+"; hScore: "+path.get(i).hScore);
//      }
   }
}
