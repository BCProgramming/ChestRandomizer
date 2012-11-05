package com.BASeCamp.SurvivalChests;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.*;
//represents a Team of Players
public class GameTeam {
	
	
	private String _Name; //Name of the team.
	private LinkedList<Player> _Players = null;
	
	
	public List<Player> getMembers(){
		
		return _Players;
		
	}
	
	public GameTeam(String pTeamName,List<Player> TeamPlayers){
		_Name = pTeamName;
		_Players = new LinkedList<Player>(_Players);
		
		
	}

}
