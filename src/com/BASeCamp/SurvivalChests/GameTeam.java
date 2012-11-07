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
	public boolean isMember(Player playertest){
		
		return _Players.contains(playertest);
		
	}
	
	public String getName(){
		return _Name;
		
	}
	public boolean Eliminated(){
		return _Players.size()==0;
	}
	public GameTeam(String pTeamName,List<Player> TeamPlayers){
		_Name = pTeamName;
		_Players = new LinkedList<Player>(_Players);
		
		
	}
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(_Name);
		sb.append("(");
		if(_Players.size() > 0){
		for(Player sp:_Players){
			sb.append(sp.getDisplayName());
			sb.append(",");
		}
		
		sb.setLength(sb.length()-1); //chop off last comma.
		}
		return sb.toString();
	}
}
