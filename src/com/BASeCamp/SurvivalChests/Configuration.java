package com.BASeCamp.SurvivalChests;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Configuration {

	
	
	Properties propdata=null;
	public Configuration(InputStream configsource) throws IOException{
		
		propdata = new Properties();
		propdata.load(configsource);
	}
	public Configuration(File configsource) {
		
		try {
		propdata = new Properties();
		if(!configsource.exists()){
			//empty config.
			
			
			
		}
		else{
			
			propdata.load(new FileReader(configsource));
			
			
			
			
			
			
		}
		
		}
		catch(IOException ex){
			
			
			propdata = new Properties();
			
		}
		
	}
	
	
	
	
	
	
	
	
}
