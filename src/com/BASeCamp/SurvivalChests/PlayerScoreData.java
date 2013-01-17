package com.BASeCamp.SurvivalChests;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PlayerScoreData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4828667024840867065L;
//Score Data, stores/restores data about a single player's overall statistics with the plugin.
//stuff we could track: Mobs killed. (For Mob Arena)
//Players Killed
//Timed placed in positions in each mode.
	
//MobArena Game scores. store game score acquired by player over all the games they have played.

	
	public String PlayerName;
	//standard stuff. Players Killed counts.
	//key is player name. Value is number of times killed.
	
	public HashMap<String,Integer> PlayerKills = new HashMap<String,Integer>();
	//Key is Mob name. Value is number of kills. 
	public HashMap<String,Integer> MobKills = new HashMap<String,Integer>();
	
	public LinkedList<Integer> ArenaScores = new LinkedList<Integer>(); //scores won in the arena. Each one represents a single game played
	//in Mob Arena mode.
	
	//key is placement position. value is times placed there.
	public HashMap<Integer,Integer> ArenaPlacements = new HashMap<Integer,Integer>(); 
	
	public HashMap<Integer,Integer> PVPPlacements = new HashMap<Integer,Integer>();
	
	
	public PlayerScoreData(){
		
		
	}
	
	public int getArenaPlacementCount(int Position){
		
		if(!ArenaPlacements.containsKey(Position)) return  0;
		return ArenaPlacements.get(Position);
		
		
	}
	public int getPVPPlacementCount(int Position) {
		
		if(!PVPPlacements.containsKey(Position)) return 0;
		return PVPPlacements.get(Position);
		
	}
	//called to increment data for the given placement.
	public int PVPPlaced(int placement){
		PVPPlacements.put(placement,getPVPPlacementCount(placement)+1);
		
		return getPVPPlacementCount(placement);
	}
	public int ArenaPlaced(int placement){
		
		ArenaPlacements.put(placement, getArenaPlacementCount(placement)+1);
		return getArenaPlacementCount(placement);
		
		
		
	}
	
	public int getPlayerKillCount(String PlayerName){
		
		
		if(!PlayerKills.containsKey(PlayerName)) return 0;
		return PlayerKills.get(PlayerName);
		
		
	}
	public void AddArenaScore(int Score){
		
		ArenaScores.add(Score);
		Collections.sort(ArenaScores);
		
	}
	public LinkedList<Integer> getArenaScores(){
		
		
		return (LinkedList<Integer>) ArenaScores.clone();
		
	}
	public List<String> GetPlayersKilled(){
		
		LinkedList<String> PK = new LinkedList<String>();
		for(String killedname : PlayerKills.keySet()) {
			
			
			PK.add(killedname);
			
		}
		return PK;
		
		
		
		
	}
	
	
	
	
	
}
