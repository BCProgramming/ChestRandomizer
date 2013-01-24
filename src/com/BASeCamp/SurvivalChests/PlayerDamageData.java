package com.BASeCamp.SurvivalChests;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

//stores data on a player and the damage they have received.
public class PlayerDamageData {

	public class PlayerDamageEntry {
		private String _PlayerName;
		private int _DamageAmount;
		
		
		public String getPlayerName() { return _PlayerName;}
		//public void setPlayerName(String value){_PlayerName=value;}
		public int getDamageAmount(){ return _DamageAmount;}
		//public void setDamageAmount(int value){_DamageAmount=value;}
		
		public PlayerDamageEntry(String pPlayerName,int pDamage){
			
			_PlayerName = pPlayerName;
			_DamageAmount = pDamage;
			
			
		}
		
		
		
	}
	private String PlayerName="";
	private String WorldName="";
	
	public Player getPlayer(){
		return Bukkit.getPlayerExact(PlayerName);
		
		
	}
	public World getWorld() { return Bukkit.getWorld(WorldName);}
	private HashMap<String,List<PlayerDamageEntry>> _DamageData = new HashMap<String,List<PlayerDamageEntry>>();
	public PlayerDamageData(Player pTarget)
	{
		PlayerName = pTarget.getName();
		WorldName = pTarget.getWorld().getName();
		
		
	}
	/**
	 * retrieves the total amount of damage done by the given player to the player represented by this
	 * instance.
	 * @param sSource Name of player.
	 * @return
	 */
	public int getTotalDamage(String sSource){
		if(_DamageData.containsKey(sSource)){
			List<PlayerDamageEntry> grablist = _DamageData.get(sSource);
			int accum=0;
			for(PlayerDamageEntry p:grablist){
				accum+=p.getDamageAmount();
			}
			
			return accum;
			
		}
		return 0;
	}
	/**
	 * Adds the given amount of Damage as a new entry in the appropriate list.
	 * @param source name Player doing damage to this player.
	 * @param amount Amount of Damage to add.
	 */
	public void addDamage(String source,int amount){
		//adds the damage entry to the appropriate hashmap.
		//_DamageData is indexed by the player name.
		//if it doesn't have the entry, add it in.
		if(!_DamageData.containsKey(source))
			_DamageData.put(source, new LinkedList<PlayerDamageEntry>());
		
		//grab this list reference.
		List<PlayerDamageEntry> grablist = _DamageData.get(source);
		//add in this damage entry.
		
		
		
	}
	
	
	
	
	
}
