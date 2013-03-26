package com.BASeCamp.SurvivalChests;

import java.util.HashMap;


//inner class: represents the tally of a specific player, tracking who they have killed and who they
//have been killed by.
public class PlayerTally
{
	
	private String PlayerName=null;
	//HashMap of Deaths. the key is the Victim.
	//the value HashMap is indexed by the assailant.
	private HashMap<String,TallyData> Deaths = new HashMap<String,TallyData>();
	//hashmap of Kills. the key is the assailant.
	//the value HashMap is indexed by the Victim.
	private HashMap<String,TallyData> Kills = new HashMap<String,TallyData>();
	
	public String getPlayerName() { return PlayerName;}
	public void setPlayerName(String value){PlayerName=value;} 
	
	public PlayerTally(String pPlayer)
	{
		PlayerName=pPlayer;
		
	}
	//accessor methods: these get and set the scores.
	
	//method that gets the current number of Deaths of PlayerName caused by the given player.
	
	public int getDeathsFrom(String Assailant)
	{
		
		//if the Hashmap doesn't have an entry for this assailant, return 0...
		if(!Deaths.containsKey(Assailant))
			return 0;
		
		
		//otherwise, grab the entry.
		return Deaths.get(Assailant).getAttackCount();
		
		
	}
	//retrieves the number of kills by PlayerName of Victim.
	public int getKillsOf(String Victim){
		if(!Deaths.containsKey(Victim)){
			return 0;
		}
		
		return Kills.get(Victim).getAttackCount();
		
	}
	//accessors that get the Total kills and total Deaths.
	
	public int getTotalDeaths()
	{
		int accumulated = 0;
		
		
				for(TallyData td:Deaths.values()){
					accumulated+=td.getAttackCount();
				}
		
		return accumulated;
		
		
	}
	public int getTotalKills()
	{
		int accumulated = 0;
				for(TallyData td:Kills.values()){
					accumulated+=td.getAttackCount();
				}
		
		return accumulated;
	}
	
	/**
	 * Called when this Player is killed by another. (This method does not update any structures
	 * relevant for the killer) 
	 * @param assailant Player that killed this player.
	 * @return current tallied kills of this player by assailant.
	 */
	public int DeathFrom(String assailant){
		//called when the PlayerName player is killed.
		//when a player is killed:
		
		//if the key isn't in the Deaths HashMap, add it. Since this is also the first tallied entry,
		//add it with 1 and return.
		
		if(!Deaths.containsKey(assailant)){
			Deaths.put(assailant, new TallyData(assailant,PlayerName,1));
			return 1;
		}
		
		//key already exists, retrieve the value, and increment the Attack Count.
		TallyData tallyit = Deaths.get(assailant);
		int incremented = tallyit.getAttackCount()+1;
		tallyit.setAttackCount(incremented);
		return incremented;
		
		
	}
	/**
	 * called when this player kills another. (This method does not update any structures relevant for the victim)
	 * @param victim Player killed by this player.
	 * @return current tallied kills of victim by this player.
	 */
	public int KilledPlayer(String victim)
	{
	//we cannot count killing themselves.
		if(PlayerName.equalsIgnoreCase(victim)) return 0;
		//if the item is not in the hashmap, add it with value 1 and return that.
		if(!Kills.containsKey(victim)){
			Kills.put(victim, new TallyData(PlayerName,victim,1));
			return 1;
		}
		
		TallyData tallyit = Kills.get(victim);
		int incremented = tallyit.getAttackCount()+1;
		tallyit.setAttackCount(incremented);
		return incremented;
	}
	public void ClearKills()
	{
		Kills = new HashMap<String,TallyData>();
		
	}
	public void ClearDeaths(){
		Deaths = new HashMap<String,TallyData>();
	}
	public void Clear() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}