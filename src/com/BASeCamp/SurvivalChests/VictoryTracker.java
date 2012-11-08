package com.BASeCamp.SurvivalChests;

import java.util.HashMap;

import org.bukkit.entity.Player;

//class used to track victories and player placements.

public class VictoryTracker {

	private BCRandomizer _Owner = null;
	
	private HashMap<Player,HashMap<Integer,Integer>> _PlayerPlacements = new HashMap<Player,HashMap<Integer,Integer>>();
	
	/**
	 * retrieves a hashMap containing Placement data for the given player. Keys are positions, starting from 1. 
	 * The number of times the player has made that place is the value associated with that key. For example:
	 * <code>getPlayerPlaceData(MrWiggles).get(1)</code>
	 * will return a boxed Integer that is the number of times the Player within the variable MrWiggles has come in first.
	 * If the Player has not placed anything or is not recorded, null is returned.
	 * @param p Player 
	 * @return
	 */
	public HashMap<Integer,Integer> getPlayerPlaceData(Player p){
		if(p==null) return null;
		return _PlayerPlacements.get(p);
		
		
	}
	
	/**
	 * 
	 * @param Owner Owning plugin class.
	 */
	public VictoryTracker(BCRandomizer Owner){
		_Owner = Owner;
		
		
	}
	public int madePlace(Player p,int Place){
		//called when a player makes a place.
		//retrieve the int array from the hashmap.
		
		if(!_PlayerPlacements.containsKey(p))
			_PlayerPlacements.put(p, new HashMap<Integer,Integer>());
		
		HashMap<Integer,Integer> retrieve = _PlayerPlacements.get(p);
		
		if(retrieve.get(Place)==null) {
			retrieve.put(Place, 1);
			return 1;}
			else
			{
				int newvalue = retrieve.get(Place)+1;
				retrieve.put(Place,newvalue);
				
				System.out.println(newvalue + " inserted into Place " + Place + " hashmap for player " + p.getName());
						return newvalue;
						
				
			}
			
		
	}
	
	
	
	
	
}
