package com.BASeCamp.SurvivalChests;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.inventory.InventoryHolder;

public class PopulationManager {

	private HashMap<String,PopulatedInventoryData> _PopData = new HashMap<String,PopulatedInventoryData>();
	
	
	public PopulationManager(){}
	public void setPopulated(World w,InventoryHolder ih){
		
		String useworld = w.getName();
		if(!_PopData.containsKey(useworld)){
			_PopData.put(useworld, new PopulatedInventoryData(w));
		}
		
		PopulatedInventoryData getpid = _PopData.get(useworld);
		getpid.setPopulated(ih);
		
		
		
	}
	public void clearPopulationData(World w){
		
			if(_PopData.containsKey(w.getName())) _PopData.remove(w.getName());
		
		
	}
	public boolean getPopulated(World w,InventoryHolder ih){
		
		return _PopData.containsKey(w.getName()) && _PopData.get(w.getName()).isPopulated(ih);
		
	}
	
}
