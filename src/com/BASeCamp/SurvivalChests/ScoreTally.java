package com.BASeCamp.SurvivalChests;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

import com.BASeCamp.SurvivalChests.PlayerTally;
import com.BASeCamp.SurvivalChests.TallyData;



//class used for managing a score tally.

public class ScoreTally {
	public enum ScoreSystem{
		/**
		 * Score will be simply the number of Kills.
		 */
		Kills,
		/**
		 * Score will be total Kills minus total deaths.
		 */
		KillsMinusDeaths,
		/**
		 * Score will be kills minus total deaths minus double the suicides.
		 */
		KillsMinusDeathsMinus2xsuicides
		
		
	}
	//"master" ScoreTally; this basically manages a set of PlayerTally's.
	//each Player Tally manages the Kill and Death counters of a single player.
	//thus we need a PlayerTally for each Player.
	private HashMap<String,PlayerTally> PlayerScores = new HashMap<String,PlayerTally>();
	private GameTracker _owner = null;
	
	public ScoreTally(GameTracker tracker,List<Player> participants){
		//initialize HashMap with an Empty PlayerTally for each Participant.
		_owner = tracker;
		for(Player p:participants){
			PlayerScores.put(p.getName(), new PlayerTally(p.getName()));
		}
		
		
	}
	//accessor methods that directly access a PlayerTally.
	public PlayerTally getTally(String p)
	{
		//if not found, add it. GameTracker's have an AddParticipant method, so
		//the number of participants can change during a game in "continuous" mode.
		if(!PlayerScores.containsKey(p)){
			PlayerScores.put(p, new PlayerTally(p));
		}
		
		return PlayerScores.get(p);
	}
	public PlayerTally getTally(Player p)
	{
		return getTally(p.getName());
	}
	public void clearTally(String p){
		
		
		//retrieve that item.
		PlayerTally applicable = getTally(p);
		applicable.Clear();
		
		
	}
	public void clearTally(Player p){
		clearTally(p.getName());
	}
	public void PlayerDeath(Player Victim,Player Assailant)
	{
		PlayerDeath(Victim.getName(),Assailant.getName());
	}
	//increments the appropriate data for the Victim and the player.
	public void PlayerDeath(String Victim,String Assailant)
	{
	//increment the death to the victim.
		PlayerTally VictimTally = getTally(Victim);
		VictimTally.DeathFrom(Assailant);
		//increment the kill for the assailant.
		PlayerTally KillerTally = getTally(Assailant);
		KillerTally.KilledPlayer(Victim);
		
		
		
	}
	public int getPlayerKills(Player p){
		return getPlayerKills(p.getName());
	}
	public int getPlayerKills(String PlayerName){
		//get number of kills total by that player.
		PlayerTally KillerTally = getTally(PlayerName);
		return KillerTally.getTotalKills();
		
	}
	public int getPlayerDeaths(Player p){
		return getPlayerDeaths(p.getName());
	}
	public int getPlayerDeaths(String PlayerName){
		PlayerTally deathtally = getTally(PlayerName);
		return deathtally.getTotalDeaths();
	}
	public int getPlayerSuicides(String PlayerName){
		//suicides are naturally just the number of deaths that are attributable to the same player.
		//parto f this has to be handled in the appropriate death event. If the pl
		PlayerTally deathtally = getTally(PlayerName);
		return deathtally.getDeathsFrom(PlayerName);
	}
	public int getPlayerScore(String PlayerName,ScoreSystem ss){
		
		//get total Kills, deaths, and suicides.
		int TotalKills = getPlayerKills(PlayerName);
		int TotalDeaths = getPlayerDeaths(PlayerName);
		int TotalSuicides = getPlayerSuicides(PlayerName);
		switch(ss){
		case Kills:
			return TotalKills;
		case KillsMinusDeaths:
			return TotalKills-TotalDeaths;
		case KillsMinusDeathsMinus2xsuicides:
			return TotalKills-TotalDeaths-TotalSuicides;
		default:
			return TotalKills;
		
		}
		
		
		
		
	}
	
	
	
}
