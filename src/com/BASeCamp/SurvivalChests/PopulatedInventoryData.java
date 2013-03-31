package com.BASeCamp.SurvivalChests;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.World;
import org.bukkit.inventory.InventoryHolder;

public class PopulatedInventoryData {

	private String _useWorld=null;
	
	private HashSet<InventoryHolder> _Populated;
	public String getWorld(){ return _useWorld;}
	
	public PopulatedInventoryData(World worlduse){
		_useWorld = worlduse.getName();
		_Populated = new HashSet<InventoryHolder>();
	}
	public void setPopulated(InventoryHolder item){
		
		_Populated.add(item);
				
	}
	public void unsetPopulated(InventoryHolder item){
		_Populated.remove(item);
	}
	
	public boolean isPopulated(InventoryHolder item){
		return _Populated.contains(item);
	}
	
	
	
	
}
