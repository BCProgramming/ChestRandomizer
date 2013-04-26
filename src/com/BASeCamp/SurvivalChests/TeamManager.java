package com.BASeCamp.SurvivalChests;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeamManager {

	
	//Stuff we need to change for teams:
	
	//'friendly fire'. This needs to be handled when Players damage each other.
	//if Friendly Fire is disabled and the players are on the same team, cancel it.
	
	//Team elimination:
	//when a player dies, special logic to determine if they are on a team and if so how many players 
	//are left on that team. If the team is eliminated, state that as well.
	
	//Team Scoring
	//particularly for Mob Arena Mode, Keep track of total
	//scores for an entire team.
	//PvP scores need to disregard friendly fire kills, or only give negative points for them.
	
	
	//Un-teamed:
	//the StillAlive list in the gametracker will list all players.
	//if a player is not on a team, then team logic will not take place.
	//Either that, or all non-teamed players can be put on their own Team when the game starts,
	//making sure that all logic can deal with teams.
	
	
	private GameTracker _Tracker = null;
	private HashMap<String,GameTeam> Teams = new HashMap<String,GameTeam>();
	public GameTracker getTracker(){return _Tracker;}
	
	
	public GameTeam getTeam(String TeamName){
		if(Teams.containsKey(TeamName)) return Teams.get(TeamName);
		return null;
	}
	public GameTeam getPlayerTeam(String PlayerName){
		
		Player p = Bukkit.getPlayer(PlayerName);
		for(GameTeam gt:Teams.values()){
			if(gt.isMember(p)) return gt;
		}
		return null;
		
	}
	//puts the given Player onto the specified team. If the team is not currently present, it will be created.
	//The Player will also be removed from any existing teams. If a Team is emptied as a result,
	//that team will be removed.
	public GameTeam SetPlayerTeam(Player p,String TeamName){
		//sets the given Player to the given team.
		//first, let's get the Target team. If the team isn't present, we will create it.
		GameTeam gt;
		
		GameTeam SourceTeam = getPlayerTeam(p.getName());
		if(SourceTeam!=null) SourceTeam.RemovePlayer(p);
		
		if(!Teams.containsKey(TeamName)){
			gt=new GameTeam(TeamName);
			Teams.put(TeamName, gt);
		}
		else {
			gt = Teams.get(TeamName);
		}
		
		return null;
		
	}
	public TeamManager(){
		
	}
	
	
	
	
}
