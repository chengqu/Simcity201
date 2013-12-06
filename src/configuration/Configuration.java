package configuration;

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



public class Configuration {
	
	private static String typeDeli = ":";
	private static String attributeDeli = "=";
	private static String statementDeli = ";"; 
	
	/*Default Setting for Person*/
	
	/*Default Setting for Building*/
	
	
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
        try {
            s = new Scanner(new BufferedReader(
            		new FileReader("src"+File.separator+"configuration"+File.separator+filename)));

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
        	ioe.printStackTrace();
        }finally {
            if (s != null) {
                s.close();
            }
        }
        System.out.println("Successfully read " + line_count + " lines.");
        while(!lines.isEmpty()) {
        	line = lines.remove(0);
	        String[] typeSplit = line.split(typeDeli);
	        String[] statements = typeSplit[1].split(statementDeli);
	        switch(typeSplit[0].trim().toLowerCase()) {
	            case "person": 	
	            	for (int i=0; i<statements.length; i++) {
	            		String[] att = statements[i].split(attributeDeli);
	            		if ( att.length > 1) {
	                		System.out.print(att[0].trim().toLowerCase() + " ");
	                		System.out.print(att[1].trim().toLowerCase() + "; ");
	                		
	            		}
	            	}
	            	//System.out.println("Configuring person"); 
	            	break;
	            case "building":	
	            	for (int i=0; i<statements.length; i++) {
	            		String[] att = statements[i].split(attributeDeli);
	            		if ( att.length > 1) {
	                		System.out.print(att[0].trim().toLowerCase() + " ");
	                		System.out.print(att[1].trim().toLowerCase() + "; ");
	            		}
	            	}
	            	//System.out.println("Configuring building"); 
	            	break;
	        	default:	System.out.println("Invalid expression"); 
	        		break;
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
	
	public static void main(String[] args){
		createTemplate();
		//configure("1.config");
	}
}
