package configuration;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import simcity201.gui.GlobalMap;
import simcity201.gui.GlobalMap.BuildingType;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
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
	
	static String[] strs = {" ", "one", "two", "three"};
	static JComboBox<String> filenameList_config = new JComboBox<String>(strs);
	
	static JComboBox<String> filenameList_music = new JComboBox<String>(strs);
	
	public static String chosenFilename_config;
	public static String chosenFilename_music;
	static Semaphore stopper = new Semaphore(0, true);
	
	public static void initInteractiveFilename() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dim = new Dimension((int)(screenSize.getWidth()/3.5), (int)(screenSize.getHeight()/3.5));
		JFrame frame = new JFrame("Initial Configurations");
		frame.setSize(dim);
		frame.setPreferredSize(dim);
		frame.setMaximumSize(dim);
		frame.setMinimumSize(dim);
		frame.setLocation(new Point((int)(screenSize.getWidth()/9*3), (int)(screenSize.getHeight()/9*3)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setVisible(true);
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel welcomeLabel = new JLabel("<html><head>"
				+ "body { display:block; margin-left:auto; margin-right:auto;k padding-left:100px; padding-right:100px; text-align:right;} </head>"+
				"<body><center><div><b>WELCOME to Team10 SimCity!</b>"
		+ "<br/>Please choose configuration file"
		+ "<br/>and choose music file</div></center></body></html>", SwingConstants.CENTER);
		welcomeLabel.setSize(new Dimension(dim.width, dim.height/3));
		welcomeLabel.setMaximumSize(new Dimension(dim.width, dim.height/3));
		welcomeLabel.setMinimumSize(new Dimension(dim.width, dim.height/3));
		welcomeLabel.setPreferredSize(new Dimension(dim.width, dim.height/3));
		frame.add(welcomeLabel, BorderLayout.NORTH);

		/*Combobox selection for config filename*/
		filenameList_config.setSelectedIndex(0);
		chosenFilename_config = (String) filenameList_config.getSelectedItem(); 
		filenameList_config.setActionCommand("fListCB");
		filenameList_config.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("fListCB")) {
					JComboBox<String> cb = (JComboBox<String>)e.getSource();
			        String selectedFile = (String)cb.getSelectedItem();
			        chosenFilename_config = selectedFile;
			        //System.out.println(selectedFile + ", " + chosenFilename_config);
				}
		       
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.gridwidth = 2;
		panel.add(filenameList_config, c);
		
		/*File Selection button (actually does nothing but checking what was selected*/
		JButton fileButton = new JButton("Confirm");
		fileButton.setActionCommand("fileConfirmButton");
		fileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("fileConfirmButton"))
				System.out.println(chosenFilename_config);
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = .01;
		c.gridwidth = 1;
		refreshConfig();
		panel.add(fileButton, c);
		
		/*Combobox selection for music filename*/
		filenameList_music.setSelectedIndex(0);
		chosenFilename_music = (String) filenameList_music.getSelectedItem();
		filenameList_music.setActionCommand("mListCB");
		filenameList_music.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("mListCB")) {
					JComboBox<String> cb = (JComboBox<String>)e.getSource();
			        String selectedFile = (String)cb.getSelectedItem();
			        chosenFilename_music = selectedFile;
		        //System.out.println(selectedFile + ", " + chosenFilename_music);
				}
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.gridwidth = 2;
		panel.add(filenameList_music, c);
		
		/*File Selection button (actually does nothing but checking what was selected*/
		final JButton fileButton_music = new JButton("Confirm");
		fileButton_music.setActionCommand("musicConfirmButton");
		fileButton_music.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("musicConfirmButton"))
				System.out.println(chosenFilename_music);
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = .01;
		c.gridwidth = 1;
		refreshMusic();
		panel.add(fileButton_music, c);
		
		/*Refresh button*/
		JButton refreshButton = new JButton("Refresh");
		refreshButton.setActionCommand("refreshButton");
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("refreshButton")) {
					refreshConfig();
					refreshMusic();
					System.out.println("Refreshed");
				}
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0.0;
		c.gridwidth = 1;
		panel.add(refreshButton, c);
		
		JButton createButton = new JButton("Create Config Template");
		createButton.setActionCommand("createButton");
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("createButton")) {
					createTemplate();
				}
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 0.0;
		c.gridwidth = 1;
		panel.add(createButton, c);
		
		JButton doneButton = new JButton("Done");
		doneButton.setActionCommand("doneButton");
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Starting....");
				if(e.getActionCommand().equals("doneButton")) {
					stopper.release();
					//System.out.println("Starting....");
				}
			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 2;
		c.weightx = 0.0;
		c.gridwidth = 1;
		panel.add(doneButton, c);
		
		

		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		
		/*Stop*/
		try {
			stopper.acquire();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		
		frame.removeAll();
		frame.setVisible(false);
		
		MusicPlayer musicPlayer = new MusicPlayer();
		musicPlayer.start();
		
	}
	
	public static void refreshConfig() {
		filenameList_config.removeAllItems();
		
		File directory = new File("src"+File.separator+"configuration");
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
		});
		for (int i = 0; i < configFiles.length; i++) {
			filenameList_config.addItem(configFiles[i].getName());
		}
		
	}
	
	public static void refreshMusic() {
		filenameList_music.removeAllItems();
		
		File directory = new File("src"+File.separator+"configuration");
		File[] configFiles = directory.listFiles(new FileFilter(){
			public boolean accept(File arg0) {
				String name = arg0.getName();
				int dotIndex = name.lastIndexOf(".");
				String afterDot = name.substring(dotIndex+1, name.length());
				if (afterDot.equalsIgnoreCase("wav")||afterDot.equalsIgnoreCase("mp3")) {
					return true;
				}else {
					return false;
				}
			}
		});
		for (int i = 0; i < configFiles.length; i++) {
			filenameList_music.addItem(configFiles[i].getName());
		}
		
	}
	
	static class MusicPlayer extends Thread {
		private AudioStream as;
		private AudioPlayer p;
		private boolean playback;
		
		public void run() {
		    startPlayback();
		}
		private void setRandom() {
		    File[] files = getTracks();
		    try {
		        String f = files[(int) (Math.random() * (files.length - 1))].getAbsolutePath();
		        System.out.println("Now Playing: " + f);
		        as = new AudioStream(new FileInputStream(f));
		    } catch (IOException ex) {
		    }
		}
		private void setMusic() {
			 try {
		        String f = "src"+File.separator+"configuration"+File.separator +chosenFilename_music;
		        System.out.println("Now Playing: " + f);
		        as = new AudioStream(new FileInputStream(f));
		    } catch (IOException ex) {
		    }
		}
		public void startPlayback() {
		    playback = true;
		    setMusic();
		    p.player.start(as);
		    try {
		        do {
		        } while (as.available() > 0 && playback);
		        if (playback) {
		            startPlayback();
		        }
		    } catch (IOException ex) {
		    }

		}
		
		public void stopPlayback() {
		    playback = false;
		    p.player.stop(as);
		}

		private File[] getTracks() {
		    File dir = new File(System.getProperty("user.dir") + "\\music");
		    File[] a = dir.listFiles();
		    ArrayList<File> list = new ArrayList<File>();
		    for (File f : a) {
		        if (f.getName().substring(f.getName().length() - 3, f.getName().length()).equals("wav")) {
		            list.add(f);
		        }
		    }
		    File[] ret = new File[list.size()];
		    for (int i = 0; i < list.size(); i++) {
		        ret[i] = list.get(i);
		    }
		    return ret;
		}
	}
	
	
	/*
	public static void main(String[] args){
		//createTemplate();
		//configure("2.config");
	}
	*/
}
