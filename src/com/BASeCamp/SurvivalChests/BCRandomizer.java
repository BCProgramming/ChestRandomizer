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
	//new method:
	
	//preparegame sets the game to accept players.
	//joingame willl be issued by players that wish to play.
	//startgame begins that game.
	
	//implementation: startgame will use a pre-existing list of players that used /joingame.
	//the list will be cleared by preparegame.
	public static VictoryTracker Victories = null;
	
	public final static String PluginName="BCRandomizer";
	  public static String pluginMainDir = "./plugins/BCRandomizer";
	  //TODO: use plugin.yml to retrieve configuration data- more precisely, to retrieve the file to use for the randomization data.
	  //Another idea is to allow that to be changed via other commands, possibly referring to files in the plugin folder.
	    public static String pluginConfigLocation = pluginMainDir + "/survivalchests.cfg";
	    public RandomizerCommand Randomcommand = null; 
	    public LinkedList<GameTracker> ActiveGames = new LinkedList<GameTracker>();
	    
	   // public GameTracker _Tracker = null;
	    public static void clearPlayerInventory(Player p){
	    	
	    	((CraftPlayer)p).getHandle().inventory.b(new NBTTagList());
	    	
	    }
	    public BCRandomizer()
	    {
	    	
	    	Victories = new VictoryTracker(this);
	    	
	    	
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
	
		        
		         this.getCommand("preparegame").setExecutor(Randomcommand);
		         this.getCommand("joingame").setExecutor(Randomcommand);
		         this.getCommand("spectategame").setExecutor(Randomcommand);
	    
	    }
	    @Override
	    public void onDisable()
	    {
	    	
	    	for(GameTracker t:ActiveGames){
	    		t.gamecomplete =true;
	    		t.deathwatcher._Trackers=null;
	    		
	    	}
	    	
	    }
	    private static final boolean _debug=false;
	    public static void emitmessage(String msg){
	    	
	    	if(_debug) System.out.println(msg);
	    	
	    }
	    public static void main(String[] args){
	    	
	    	String[] choosefrom = new String[] { "StringA","StringB","StringC","StringD"};
	    	float[] probdata = new float[] {100f,100f,100f,100f};
	    	String chosen = RandomData.Choose(choosefrom, probdata);
	    	
	    	BCRandomizer.emitmessage(chosen);
	    	
	    	for(int i=0;i<20;i++){
	    	String generatedLore = NameGenerator.GenerateLore();
	    	BCRandomizer.emitmessage(generatedLore);
	    	
	    	
	    	}
	    	
	    	
	    }
	    
}
