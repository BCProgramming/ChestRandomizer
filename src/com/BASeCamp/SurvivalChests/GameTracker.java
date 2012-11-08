package com.BASeCamp.SurvivalChests;

import java.security.acl.Owner;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
	private List<Player> _Spectators = null;
	public  LinkedList<Player> getStillAlive(){return StillAlive;} 
	private HashMap<Integer,Player> FinishPositions = new HashMap<Integer,Player>();
	BCRandomizer _Owner = null;
	public PlayerDeathWatcher deathwatcher = null;
	public GameTracker(BCRandomizer Owner,World applicableWorld,List<Player> Participants, List<Player> spectators){
		//initialize StillAlive List.
	      deathwatcher= new PlayerDeathWatcher(Owner,this,applicableWorld);
	      Owner.getServer().getPluginManager().registerEvents(deathwatcher, Owner);
		
		_Owner = Owner;
		_Owner.ActiveGames.add(this);
		_Spectators = spectators;
		for(Player p:Participants){
			StillAlive.add(p);
		}
		//raise custom event.
		Bukkit.getServer().getPluginManager().callEvent(new GameStartEvent(Participants,spectators));
		Bukkit.broadcastMessage("survival game started! " + StillAlive.size() + " participants.");
		
		
		String[] participantNames = new String[StillAlive.size()];
		
		int i=0;
		for(Player Alive:StillAlive){
			
			participantNames[i] = Alive.getDisplayName();
			i++;
		}
		i=0;
		
		Bukkit.broadcastMessage(ChatColor.RED + "participating:" + StringUtil.Join(participantNames,","));
		
	}
	private void addprize(Player deathplayer){
		
		for(int i=0;i<64;i++)
		{
			//add prize gold.
			ItemStack prizegold = new ItemStack(Material.GOLD_NUGGET);
			ItemNamer.load(prizegold);
			ItemNamer.setName("Mulreay Gold");
			prizegold = ItemNamer.getItemStack();
			deathplayer.getWorld().dropItemNaturally(deathplayer.getLocation(), prizegold);
			
		}
		
	}
	//when a player dies, we need to update the list, and possibly even break out if the game has ended as a result.
	public void PlayerDeath(Player deadPlayer,Player assailant){
		//remove player from list.
		if(gamecomplete) return;
		if(!StillAlive.contains(deadPlayer)){
			return;
			
		}
		StillAlive.remove(deadPlayer);
		Integer theposition = StillAlive.size()+1;
		Bukkit.broadcastMessage(deadPlayer.getDisplayName() + " is " + ChatColor.RED + "OUT!" + ChatColor.YELLOW + " (Place:" + theposition + ")");
		BCRandomizer.Victories.madePlace(deadPlayer, theposition);
		if(deathwatcher==null) return;
		FinishPositions.put(theposition, deadPlayer);
		synchronized(StillAlive) { //synch on StillAlive List.
			if(StillAlive.size()==0){
				Bukkit.broadcastMessage("All players participating died. Somehow. No Winner?");
				addprize(deadPlayer);
				gamecomplete=true;
				
				
				deathwatcher._Trackers.remove(this);
			return;	
			}
		if(StillAlive.size()==1){
			//notify the 'game' is finished, so the run loop can terminate.
			//announce the winner!
			Player winner = StillAlive.getFirst();
			Bukkit.broadcastMessage(winner.getName() + " has WON the event!");
			//raise custom event.
			Bukkit.getServer().getPluginManager().callEvent(new GameEndEvent(winner));
			gamecomplete=true;
			//dead player explodes inexplicably.
			
			addprize(deadPlayer);
			gamecomplete=true;
			deathwatcher._Trackers.remove(this);
			//move players back to their original spots where desired.
			for(Player pp:_Owner.Randomcommand.returninfo.keySet()){
				_Owner.Randomcommand.returninfo.get(pp).sendBack();
								
				
				
			}
			
			
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
	public boolean gamecomplete=false;
	
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
		_Owner.ActiveGames.remove(this);
		//deathwatcher._Tracker=null;
		deathwatcher._Trackers.remove(this);
		
	}
	//tracks game state, updating the list of still active players when players are killed (GameTracker is notified through PlayerDeathWatcher)
	

}
