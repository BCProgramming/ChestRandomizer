package com.BASeCamp.SurvivalChests;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ReturnData {
	
	//initialized to cache their
	//current information.
	
	private Location _Location=null;
	private Player _Player = null;
	private World _World = null;
	
	public ReturnData(Player p){
		
		_Player = p;
		_Location = p.getLocation();
		_World = p.getWorld();
		
		
	}
	public void sendBack(){
		
		_Player.sendMessage(ChatColor.AQUA + " You have returned.");
		_Player.teleport(_Location);
		
		
		
	}

}
