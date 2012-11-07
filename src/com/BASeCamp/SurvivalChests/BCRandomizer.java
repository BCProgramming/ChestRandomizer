package com.BASeCamp.SurvivalChests;

import java.util.LinkedList;

import net.minecraft.server.NBTTagList;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.*;
public class BCRandomizer extends JavaPlugin {
	public final static String PluginName="BCRandomizer";
	  public static String pluginMainDir = "./plugins/BCRandomizer";
	    public static String pluginConfigLocation = pluginMainDir + "/hungergames.cfg";
	    public RandomizerCommand Randomcommand = null; 
	    public LinkedList<GameTracker> ActiveGames = new LinkedList<GameTracker>();
	   // public GameTracker _Tracker = null;
	    public static void clearPlayerInventory(Player p){
	    	
	    	((CraftPlayer)p).getHandle().inventory.b(new NBTTagList());
	    	
	    }
	    @Override
	    public void onEnable(){
	    	Randomcommand = new RandomizerCommand(this);
	    	  PluginCommand batchcommand = this.getCommand("repopchests");
	    	  
		         batchcommand.setExecutor(Randomcommand);
		         
		         
		         PluginCommand delayPvP = this.getCommand("startgame");
		         delayPvP.setExecutor(Randomcommand);
		         

		         
		         PluginCommand teamsplit = this.getCommand("teamsplit");
		         teamsplit.setExecutor(Randomcommand);
		   
		         PluginCommand friendly = this.getCommand("friendly");
		         friendly.setExecutor(Randomcommand);
	
		         
		         
	    
	    }
	    @Override
	    public void onDisable()
	    {
	    	
	    	for(GameTracker t:ActiveGames){
	    		t.gamecomplete =true;
	    		t.deathwatcher._Trackers=null;
	    		
	    	}
	    	
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
