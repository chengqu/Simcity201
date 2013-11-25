package configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;



public class Configuration {
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
        String line = "";
        int line_count = 0;
        try {
            s = new Scanner(new BufferedReader(new FileReader("src/configuration/"+filename)));

            while (s.hasNextLine()) {
            	line_count ++;
            	line = s.nextLine();
            	if(line.startsWith("//")){
            		System.out.println("comment--ignored on line " + line_count);
            		continue;
            	}
                String[] typeSplit = line.split(":");
                switch(typeSplit[0].toLowerCase()) {
	                case "person": 	System.out.println("Configuring person"); break;
	                case "building":	System.out.println("Configuring building"); break;
                	default:	System.out.println("asdfasdf"); 
                		break;
                }
            }
        }catch(IOException ioe){
        	ioe.printStackTrace();
        }finally {
            if (s != null) {
                s.close();
            }
        }
	}
	public static void main(String[] args){
		configure("1.config");
	}
}
