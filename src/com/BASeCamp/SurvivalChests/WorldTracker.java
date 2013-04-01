package com.BASeCamp.SurvivalChests;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BlockPopulator;
public class WorldTracker implements Listener  {

	private BCRandomizer _owner;
	
	public WorldTracker(BCRandomizer owner){
		_owner = owner;
		_owner.getServer().getPluginManager().registerEvents(this, _owner);
	}
	
	@EventHandler
	public void onWorldInit(WorldInitEvent wie){
		String testname = wie.getWorld().getName();
		System.out.println("onWorldInit:" + testname);
		boolean isarena=false;
		if(RandomizerCommand.ArenaNames!=null){
			for(String iterate:RandomizerCommand.ArenaNames){
				if(iterate.equalsIgnoreCase(testname)){
					isarena=true;
					break;
				}
			}
			
			
		}
		if(isarena){
			System.out.println("Arena Detected...");
			System.out.println("Populators:" + wie.getWorld().getPopulators().size());
			
			for(BlockPopulator bp:wie.getWorld().getPopulators()){
				System.out.println("BP:Class:" + bp.getClass().getName());
			}
			
			
		}
	}
	
	
}
