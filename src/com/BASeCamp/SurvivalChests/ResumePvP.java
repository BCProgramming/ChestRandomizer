package com.BASeCamp.SurvivalChests;

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

	private int _SecondsDelay;
	private long starttime;
	World useWorld = null;
	public ResumePvP(World target,int numseconds){
		_SecondsDelay=numseconds;
		starttime= System.currentTimeMillis();
		useWorld=target;
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
		
		int numactive = 0;
		useWorld.setPVP(true);
		
		
		
		
		for(Player iterate:useWorld.getPlayers())
		{
			if(iterate.isOnline()) {
				numactive++;
		       Location currlocation = iterate.getLocation();
		       currlocation = new Location(useWorld,currlocation.getX()+5,currlocation.getY()-15,currlocation.getZ()-5);
		       useWorld.playEffect(currlocation, Effect.GHAST_SHRIEK, 10);
		
		       //useWorld.strikeLightning(currlocation);
		     
		       
		       
		       iterate.addPotionEffect(Potion.getBrewer().createEffect(PotionEffectType.BLINDNESS, 500, 1));
		       iterate.sendMessage(ChatColor.BOLD.toString() +ChatColor.LIGHT_PURPLE + "You have been temporarily blinded!");
			}
		}
		Bukkit.broadcastMessage(ChatColor.RED + "PvP Re-Enabled in World + " + useWorld.getName());
		Bukkit.broadcastMessage(ChatColor.GREEN + "Good luck to all contestants! May luck favour you! ;)");
		
		
	}

	
	
	
}
