package com.BASeCamp.SurvivalChests;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.BASeCamp.SurvivalChests.Events.PlayerSpectateEvent;

//class that manages game preparation.
// each /preparegame <name> will create a new instance of GamePreparer.
public class GamePreparer {

	private String _Name = "Game";
	private LinkedList<String> JoinedPlayers = new LinkedList<String>();
	private LinkedList<String> Spectating = new LinkedList<String>();
	private boolean MobArenaMode = false;
	
	
	public boolean getMobArenaMode() { return MobArenaMode;}
	public void setMobArenaMode(boolean newvalue) { MobArenaMode = newvalue;}
	
	private Location _SpawnSpot = null;
	
	public Location getSpawnSpot(){ return _SpawnSpot;}
	public void setSpawnSpot(Location newlocation){ _SpawnSpot = newlocation;}
	public void AddSpectator(Player addplayer){
	
		Spectating.add(addplayer.getName());
		PlayerSpectateEvent pse = new PlayerSpectateEvent(this,addplayer);
		Bukkit.getPluginManager().callEvent(pse);
		
		
		
	}
	public void AddSpectator(String PlayerName){
		AddSpectator(Bukkit.getPlayerExact(PlayerName));
		
		
	}
	public List<String> getSpectating() { return Spectating;}
	public List<String> getParticipating() { return JoinedPlayers;}
	public boolean isSpectating(String PlayerName){
		
		for(String iterate:Spectating){
			
			if(iterate.equals(PlayerName))
				return true;
			
		}
		return false;
		
	}
	public boolean isParticipant(String PlayerName){
		
		for(String iterate:JoinedPlayers){
			
			if(iterate.equals(PlayerName)) return true;
			
			
		}
		return false;
		
	}
		public void AddParticipant(Player addplayer){
		
		//is it in the Spectating list..
		
		
		
		JoinedPlayers.add(addplayer.getName());
		
	}
	public void AddParticipant(String PlayerName){
		
		
		
	}
	public GamePreparer(Location SpawnSpot){
		//sets the spawn location for this game being prepared. If a player joins or spectates they are sent there.
		_SpawnSpot = SpawnSpot;
		
		
	}
	
	
	
}
