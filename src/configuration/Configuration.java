package configuration;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simcity201.gui.GlobalMap;
import simcity201.gui.GlobalMap.BuildingType;
import Buildings.Building;
import agents.Person;



public class Configuration {
	
	private static String typeDeli = ":";
	private static String attributeDeli = "=";
	private static String statementDeli = ";"; 
	
	/*Default Setting for Person*/
	private static String name_ = "John Doe"; 
	private static int hungerLevel_ = 0, age_ = 21; 
	private static float payCheck_ = 0;
	private static double money_ = 100;
	/*Default Setting for Building*/
	
	
	
	/*Current SetUp for Person*/
	
	/*Current SetUp for Building*/
	
	
	
	
	/**
	 * If line begins with //, then it is comment, ignore the line
	 * If line begins with person: then configuring a person
	 * 		example		person: age = 15; money = 1000;
	 * If line begins with building: then configuring a building
	 * 		example		building: type = bank; x = 20; y = 20;
	 * 
	 * @param filename
	 */
	public static void configure(String filename) {

        Scanner s = null;
        List<String> lines = new ArrayList<String>();
        String line = "";
        int line_count = 0;
        String path = "src"+File.separator+"configuration"+File.separator;
        try {
            s = new Scanner(new BufferedReader(
            		new FileReader(path+filename)));

            while (s.hasNextLine()) {
            	line_count ++;
            	line = s.nextLine();
            	if(line.startsWith("//")){
            		//System.out.println("comment--ignored on line " + line_count);
            		continue;
            	}
            	if(line == "") {
            		continue;
            	}else if(line == " ") {
            		continue;
            	}
            	lines.add(line);
            }
        }catch(IOException ioe){
        	//ioe.printStackTrace();
        	System.out.println("Error while attempting to read file: " + path + filename);
        }finally {
            if (s != null) {
                s.close();
            }
        }
        System.out.println("Successfully read " + line_count + " lines.");
        while(!lines.isEmpty()) {
        	line = lines.remove(0);
	        String[] typeSplit = line.split(typeDeli);
	        if (typeSplit.length > 1) {
		        String[] statements = typeSplit[1].split(statementDeli);
		        switch(typeSplit[0].trim().toLowerCase()) {
		            case "person": 	
		            	Person p = new Person(name_, true);
		            	for (int i=0; i<statements.length; i++) {
		            		String[] att = statements[i].split(attributeDeli);
		            		if ( att.length > 1) {
		                		//System.out.print(att[0].trim().toLowerCase() + " ");
		                		//System.out.print(att[1].trim().toLowerCase() + "; ");
		                		switch(att[0].trim().toLowerCase()){
		                		case "name": 
		                			p.setName(att[1]); 
		                			break;
		                		case "is": break; 
		                		case "accounts": 
		                			break;
		                		case "address": 
		                			p.address = att[1]; 
		                			break;
		                		case "age": 
		                			try {
		                				p.age = Integer.parseInt(att[1].trim());
		                			}catch(NumberFormatException ex) {
		                				p.age = age_;
		                			}
		                			break;
		                		case "apartment": break;
		                		case "bills": break;
		                		case "car": break;
		                		case "complex": break;
		                		case "groceries": break;
		                		case "money": 
			                		try {
		                				p.money = (float) Double.parseDouble(att[1].trim());
		                			}catch(NumberFormatException ex) {
		                				p.money = (float) money_;
		                			}
			                		break;
		                		case "hungerLevel": 
		                			try {
		                				p.hungerLevel = Integer.parseInt(att[1].trim());
		                			}catch(NumberFormatException ex) {
		                				p.hungerLevel = hungerLevel_;
		                			}
		                			break;
		                		case "payCheck":
		                			try {
		                				p.payCheck = Float.parseFloat(att[1].trim());
		                			}catch(NumberFormatException ex) {
		                				p.payCheck = payCheck_;
		                			}
		                			break;
		                		case "roles": break;
		                			//p.roles.add(new Role ...);
		                		//case "":
		                			
		                			default: break;
		                			
		                		}
		            		}
		            	}
		            	GlobalMap.getGlobalMap().addPerson(p);
		            	//System.out.println("Configuring person"); 
		            	break;
		            case "building":	
		            /*For building, if any of these are not satisfied then will not create*/	
		            	BuildingType type = null;
		            	int x = -1, y = -1, width = -1, height = -1; 
		            	String name = null;
		            	/**LynRestaurant, RyanRestaurant, JoshRestaurant, 
								DavidRestaurant, EricRestaurant, ChengRestaurant,
									Bank, House, Store, Apartment*/
		            	
		            	for (int i=0; i<statements.length; i++) {
		            		String[] att = statements[i].split(attributeDeli);
		            		if ( att.length > 1) {
		                		//System.out.print(att[0].trim().toLowerCase() + " ");
		                		//System.out.print(att[1].trim().toLowerCase() + "; ");
		            			switch(att[0].trim().toLowerCase()){
		            			case "type":
		            				switch(att[1].trim()) {
		            				case "LynRestaurant": 	type = BuildingType.LynRestaurant; break;
		            				case "RyanRestaurant": 	type = BuildingType.RyanRestaurant; break;
		            				case "JoshRestaurant": 	type = BuildingType.JoshRestaurant; break;
		            				case "DavidRestaurant":	type = BuildingType.DavidRestaurant; break;
		            				case "EricRestaurant": 	type = BuildingType.EricRestaurant; break;
		            				case "ChengRestaurant":	type = BuildingType.ChengRestaurant; break;
		            				case "Bank": 			type = BuildingType.Bank; break;
		            				case "House": 			type = BuildingType.House; break;
		            				case "House1": 			type = BuildingType.House1; break;
		            				case "House2": 			type = BuildingType.House2; break;
		            				case "Store": 			type = BuildingType.Store; break;
		            				case "Apartment": 		type = BuildingType.Apartment; break;
		            				default: break;
		            				} break;
		            			case "x":		
		            				try { x = Integer.parseInt(att[1].trim());
		                			}catch(NumberFormatException ex) {
		                				x = -1;
		                			} break;
		            			case "y":
		            				try { y = Integer.parseInt(att[1].trim());
		                			}catch(NumberFormatException ex) {
		                				y = -1;
		                			} break;
		            			case "width":
		            				try { width = Integer.parseInt(att[1].trim());
		                			}catch(NumberFormatException ex) {
		                				width = -1;
		                			} break;
		            			case "height":	
		            				try { height = Integer.parseInt(att[1].trim());
		                			}catch(NumberFormatException ex) {
		                				height = -1;
		                			} break;
		            			case "name":
		            				name = att[1].trim();
		            			default: break;
		            			}
		            		}
		            	}
		            	if (type == null || name == null ||
		            	x < 0 || y < 0 || width < 0 || height < 0) {
		            		//if any of them is null or <0, don't create
		            		break;
		            	}
		            	GlobalMap.getGlobalMap().addBuilding(type, x, y, width, height, name);
		            	//System.out.println("Configuring building"); 
		            	break;
		        	default:	System.out.println("Invalid expression"); 
		        		break;
		        }
	        }
        }
	}
	
	public static void createTemplate() {
		File directory = new File("src"+File.separator+"configuration");
		//if directory not exist, create one
		directory.mkdir();
		/*
		File[] configFiles = directory.listFiles(new FileFilter(){
			public boolean accept(File arg0) {
				String name = arg0.getName();
				int dotIndex = name.lastIndexOf(".");
				String afterDot = name.substring(dotIndex+1, name.length());
				if (afterDot.equalsIgnoreCase("config")) {
					return true;
				}else {
					return false;
				}
			}
		});*/
		int i = 1;
		File newTemplate;
		do {	// if file exist, increment number
			newTemplate = new File(directory.getPath()+File.separator+i+".config");
			i++;
		}while(newTemplate.exists() && i < 100);
		try {
			if(newTemplate.createNewFile()) {
				// TODO: Write the template on the file 
				System.out.println("Configuration Template File Created");
				System.out.println("Please follow the guide of the template or you will encounter disaster");
			}else {
				System.out.println("Configuration Template File Creation FAILED");
				System.out.println("Try to be consistent with other configuration files");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String initInteractiveFilename() {
		
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setVisible(true);
		GridBagConstraints c = new GridBagConstraints();
		
		JButton button = new JButton("Select");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dim = new Dimension((int)(screenSize.getWidth()/3.5), (int)(screenSize.getHeight()/3.5));
		JFrame frame = new JFrame("Initial Configurations");
		frame.setSize(dim);
		frame.setPreferredSize(dim);
		frame.setMaximumSize(dim);
		frame.setMinimumSize(dim);
		frame.setLocation(new Point((int)(screenSize.getWidth()/9*3), (int)(screenSize.getHeight()/9*3)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
		
		/*while(true) {
			if (false) {
				break;
			}
		}*/
		
		return "";
	}
	
	
	/*
	public static void main(String[] args){
		//createTemplate();
		//configure("2.config");
	}
	*/
}
