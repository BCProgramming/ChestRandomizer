package com.BASeCamp.SurvivalChests;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

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

	
	
	//standard stuff. Players Killed counts.
	//key is player name. Value is number of times killed.
	
	public HashMap<String,Integer> PlayerKills = new HashMap<String,Integer>();
	//Key is Mob name. Value is number of kills. 
	public HashMap<String,Integer> MobKills = new HashMap<String,Integer>();
	
	public LinkedList<Integer> ArenaScores = new LinkedList<Integer>(); //scores won in the arena. Each one represents a single game played
	//in Mob Arena mode.
	
	
	
	
}
