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
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		long timediff = System.currentTimeMillis()-starttime;
		System.out.println("timediff:" + timediff);
		while((timediff/1000)<_SecondsDelay){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("timediff:" + timediff);
			timediff = System.currentTimeMillis()-starttime;	
		}
		Bukkit.broadcastMessage(ChatColor.RED + "PvP Re-Enabled! Fight ON!");
		useWorld.setPVP(true);
		
		useWorld.setStorm(true);
		useWorld.setThundering(true);
		useWorld.setThunderDuration(RandomData.rgen.nextInt(20*60));
		for(Player iterate:useWorld.getPlayers())
		{
			if(iterate.isOnline()) {
		       Location currlocation = iterate.getLocation();
		       currlocation = new Location(useWorld,currlocation.getX()+1,currlocation.getY()-3,currlocation.getZ()-1);
		       useWorld.playEffect(currlocation, Effect.GHAST_SHRIEK, 10);
		
		       useWorld.strikeLightning(currlocation);
		     
		       
		       
		       iterate.addPotionEffect(Potion.getBrewer().createEffect(PotionEffectType.BLINDNESS, 500, 1));
			}
		}
		//done! resumePvP...
		
		
	}

	
	
	
}
