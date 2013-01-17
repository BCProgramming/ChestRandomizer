package com.BASeCamp.SurvivalChests;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ResumePvP implements Runnable{

	private BCRandomizer _bcr = null;
	private int _SecondsDelay;
	private long starttime;
	private List<Player> _Players = null;
	private List<Player> _Spectators = null;
	public Thread TrackerThread = null;
	World useWorld = null;
	private GameTracker usetracker;
	public GameTracker getTracker() { return usetracker;}
	public ResumePvP(BCRandomizer bcr,World target,int numseconds, List<Player> ActivePlayers,List<Player> Spectators){
		_bcr = bcr;
		_SecondsDelay=numseconds;
		starttime= System.currentTimeMillis();
		_Players=ActivePlayers;
		_Spectators = Spectators;
		useWorld=target;
		usetracker = new GameTracker(_bcr,useWorld,_Players,_Spectators,bcr.Randomcommand.getMobArenaMode());
	}
	public static void BroadcastWorld(World toWorld,String Message){
		
		for(Player p:toWorld.getPlayers()){
			if(p.isOnline()){
				p.sendMessage(Message);
				
				
			}
			
			
		}
		
		
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		int delay = 0;
		
		long timediff = System.currentTimeMillis()-starttime;
		long prevdiff = timediff;
		System.out.println("timediff:" + timediff);
		while((float)(timediff/1000)<(float)_SecondsDelay){
			try {
				
				
				delay++;
				if(delay==4){
				delay=0;	
					//Bukkit.broadcastMessage(ChatColor.RED + String.valueOf((_SecondsDelay - timediff)) + " Seconds...");
				BroadcastWorld(useWorld,ChatColor.RED + String.valueOf(_SecondsDelay - Math.floor((Math.abs(timediff))/1000)) + " Seconds...");
				
				}
				Thread.sleep(500);
				prevdiff = timediff;
				timediff = System.currentTimeMillis()-starttime;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("timediff:" + timediff);
			timediff = System.currentTimeMillis()-starttime;	
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(_bcr, new Runnable() {
		public void run() {
			usetracker.TimeoutExpired();
			
		}
			
			
		
		},1);
		
		
		//format is /startgame <seconds> <moderator> <spectators>
		
		
		
		
		
		
		
		TrackerThread = new Thread(usetracker);
		TrackerThread.start();
		
	}

	
	
	
}
