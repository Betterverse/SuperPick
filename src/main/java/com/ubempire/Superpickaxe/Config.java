package com.ubempire.Superpickaxe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Properties;

public class Config {
	
	public static String getProp(String args) 
	{
	    String mainDirectory = "plugins/iConomySuperPick"; //sets the main directory for easy reference //the file separator is the / sign, this will create a new Zones.dat files in the mainDirectory variable listed above, if no Zones directory exists then it will automatically be made along with the file.
	    Properties prop = new Properties(); //creates a new properties file
		try {
			if (!new File(mainDirectory+"/Config.txt").exists())
			{
				new File(mainDirectory+"").mkdir();
				new File(mainDirectory+"/Config.txt").createNewFile();
				PrintWriter out = new PrintWriter(new FileWriter(mainDirectory+"/Config.txt"));
				out.println("#This is the iConomySuperPick config file.");
				out.println("");
				out.println("#Cost:");
				out.println("Cost=100");
				out.println("#Now you can add the costs per block using the id. Included are commented sample values");
				out.println("#Sets the cost of mining smoothstone ID=1 instantly to 10");
				out.println("#1=10");
				out.println("#Sets the cost of mining cobblestone ID=4 instantly to 5");
				out.println("#4=5");
				out.close();
			}	
			
		    FileInputStream in = new FileInputStream(mainDirectory+"/Config.txt"); //Creates the input stream
		    prop.load(in);
		    
		    String iv = prop.getProperty(args);
		    String ix = prop.getProperty("Cost");
		    if(iv==null) {
			    in.close(); 
		    	return ix;
		    } else {
			    in.close(); 
		    	return iv;
		    }
		    } catch (Exception e) {
		    	System.out.println("[iConomySuperPick] Error while reading config.txt!");
		    	e.printStackTrace();
		    }
		return "[iConomySuperPick] Error while reading config.txt!";
		
	}
	
}