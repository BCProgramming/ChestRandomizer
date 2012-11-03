package com.BASeCamp.SurvivalChests;

import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class GameTracker implements Runnable {

	private World runningWorld = null;
	private LinkedList<Player> StillAlive = new LinkedList<Player>();
	public  LinkedList<Player> getStillAlive(){return StillAlive;} 
	private HashMap<Integer,Player> FinishPositions = new HashMap<Integer,Player>();
	BCRandomizer _Owner = null;
	public GameTracker(BCRandomizer Owner,World applicableWorld,Player ModeratorPlayer){
		//initialize StillAlive List.
		_Owner = Owner;
		for(Player p:applicableWorld.getPlayers()){
			if(p.isOnline()){
				if(p!=ModeratorPlayer){
				StillAlive.add(p);	
					
					
				}
			}
			
		}
		
		
	}
	//when a player dies, we need to update the list, and possibly even break out if the game has ended as a result.
	public void PlayerDeath(Player deadPlayer,Player assailant){
		//remove player from list.
		StillAlive.remove(deadPlayer);
		Integer theposition = StillAlive.size()+1;
		Bukkit.broadcastMessage(deadPlayer.getDisplayName() + " is " + ChatColor.RED + "OUT!" + ChatColor.YELLOW + " (Place:" + theposition + ")");
		
		FinishPositions.put(theposition, deadPlayer);
		synchronized(StillAlive) { //synch on StillAlive List.
			if(StillAlive.size()==0){
				Bukkit.broadcastMessage("All players participating died. Somehow. No Winner?");
				for(int i=0;i<48;i++)
				{
					
					Arrow result = deadPlayer.getWorld().spawnArrow
					(deadPlayer.getLocation(), 
							new Vector(RandomData.rgen.nextFloat()-0.5f,1,RandomData.rgen.nextFloat()-0.5f).multiply(50), RandomData.rgen.nextFloat()*50,RandomData.rgen.nextFloat());
					
					
					
				}
				_Owner._Tracker=null;
			return;	
			}
		if(StillAlive.size()==1){
			//notify the 'game' is finished, so the run loop can terminate.
			//announce the winner!
			Player winner = StillAlive.getFirst();
			Bukkit.broadcastMessage(winner.getName() + " has WON the event!");
			
			gamecomplete=true;
			//dead player explodes inexplicably.
			
			for(int i=0;i<48;i++)
			{
				
				Arrow result = deadPlayer.getWorld().spawnArrow
				(deadPlayer.getLocation(), 
						new Vector(RandomData.rgen.nextFloat()-0.5f,1,RandomData.rgen.nextFloat()-0.5f).multiply(50), RandomData.rgen.nextFloat()*50,RandomData.rgen.nextFloat());
				
				
				
			}
			gamecomplete=true;
			_Owner._Tracker=null;
			
		}
		else
		{
			
			Bukkit.broadcastMessage(StillAlive.size() + " Players remain!");
			
			
			StringBuffer buildnamelist= new StringBuffer();
			
			for(Player givecompass:StillAlive){
			buildnamelist.append(givecompass.getDisplayName() + ",");	
			
			}
			
			Bukkit.broadcastMessage("Remaining:" + buildnamelist.substring(0,buildnamelist.length()-1));
			
			if(StillAlive.size()==3){
				for(Player givecompass:StillAlive){
					
					givecompass.getInventory().addItem(new ItemStack(Material.COMPASS));
					givecompass.sendMessage(ChatColor.GREEN + "You have been given a Compass.");
					givecompass.sendMessage(ChatColor.GREEN + "it points towards the closest player.");
					
					
				}
				
				
			}
		}
		}
		
	}
	private boolean gamecomplete=false;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	
		
		if(StillAlive.size()<=3){
			for(Player givecompass:StillAlive){
				
				givecompass.getInventory().addItem(new ItemStack(Material.COMPASS));
				givecompass.sendMessage(ChatColor.GREEN + "You have been given a Compass.");
				givecompass.sendMessage(ChatColor.GREEN + "it points towards the closest player.");
				
				
			}	
			
		}
		
		
		while(!gamecomplete)
		{
			
			try {
				synchronized(StillAlive) {
					
				for(Player pa:StillAlive){
					
					//find the nearest player.
					double MinDistance = Float.MAX_VALUE;
					Player currentmin = null;
					for(Player pb:StillAlive){
						if(pa!=pb){
							
							double getdist = pa.getLocation().distance(pb.getLocation());
							
							if(getdist < MinDistance){
								currentmin = pb;
								MinDistance = getdist;
								
							}
							}
							
						}
					//currentmin is closest player to pa. if it's null, then... well, do nothing, I guess.
					if(currentmin!=null) {
					pa.setCompassTarget(currentmin.getLocation());	
						
						
					}
						
						
					}
					
				}
				
				if(gamecomplete) break;
				
				
				Thread.sleep(500); //sleep, so as to prevent being CPU intensive.
				//compasses don't need to be super accurate anyway.
				
			}
			catch(InterruptedException ex)
			{
				
				
			}
			
		}
		
		_Owner._Tracker=null;
		
	}
	//tracks game state, updating the list of still active players when players are killed (GameTracker is notified through PlayerDeathWatcher)
	

}
