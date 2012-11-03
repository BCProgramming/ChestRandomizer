package com.BASeCamp.SurvivalChests;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class BCRandomizer extends JavaPlugin {
	public final static String PluginName="BCRandomizer";
	  public static String pluginMainDir = "./plugins/BCRandomizer";
	    public static String pluginConfigLocation = pluginMainDir + "/hungergames.cfg";
	    public static RandomizerCommand Randomcommand = null; 
	    
	    @Override
	    public void onEnable(){
	    	Randomcommand = new RandomizerCommand(this);
	    	  PluginCommand batchcommand = this.getCommand("repopchests");
	    	  
		         batchcommand.setExecutor(Randomcommand);
		         
		         
		         PluginCommand delayPvP = this.getCommand("stoppvp");
		         delayPvP.setExecutor(Randomcommand);
		         

		         
		         PluginCommand teamsplit = this.getCommand("teamsplit");
		         teamsplit.setExecutor(Randomcommand);
		         
	    
	    }
	    public static void main(String[] args){
	    	
	    	String[] choosefrom = new String[] { "StringA","StringB","StringC","StringD"};
	    	float[] probdata = new float[] {100f,100f,100f,100f};
	    	String chosen = RandomData.Choose(choosefrom, probdata);
	    	System.out.println(chosen);
	    	
	    	for(int i=0;i<20;i++){
	    	String generatedLore = NameGenerator.GenerateLore();
	    	System.out.println(generatedLore);
	    	
	    	
	    	}
	    	
	    	
	    }
	    
}
