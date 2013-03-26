package com.BASeCamp.SurvivalChests;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryAccessInfo {

	private Inventory _Inventory;
	
	private int _BukkitTask; //TaskID used by the scheduler
	public LinkedList<Player> cachedPlayers = new LinkedList<Player>();
	public Inventory getInventory() { return _Inventory;}
	public void setInventory(Inventory value) { _Inventory=value;}
	
	public int getBukkitTask() { return _BukkitTask;}
	public void setBukkitTask(int value){_BukkitTask=value;}
	
	public InventoryAccessInfo()
	{
		
	}
	public LinkedList<Player> getViewed()
	{
		return cachedPlayers;
		
	}
	
	//called when a player views the inventory of the attached element.
	public void PlayerView(Player p)
	{
		System.out.println("PlayerView:" + p.getName());
		
		if(cachedPlayers.size()==0 || cachedPlayers.getLast()!=p){
			cachedPlayers.addLast(p);
		}
		System.out.println("cachedPlayers size:" + cachedPlayers.size());
	}
	
}
