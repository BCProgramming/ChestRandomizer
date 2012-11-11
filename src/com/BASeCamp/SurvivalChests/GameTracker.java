package com.BASeCamp.SurvivalChests;

import java.security.acl.Owner;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
	private LinkedList<Player> _deadPlayers = null;
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
		_deadPlayers = new LinkedList<Player>();
		//raise custom event.
		
		Bukkit.broadcastMessage(ChatColor.YELLOW + "SURVIVAL:Survival game started! " + StillAlive.size() + " participants.");
		
		
		String[] participantNames = new String[StillAlive.size()];
		
		int i=0;
		for(Player Alive:StillAlive){
			
			participantNames[i] = Alive.getDisplayName();
			i++;
		}
		i=0;
		
		Bukkit.broadcastMessage("SURVIVAL:" +ChatColor.RED + "participating:" + StringUtil.Join(participantNames,","));
		
		//now, we set everything up: vanish all the spectators.
		
		for(Player spectator : _Spectators){
			BCRandomizer.VanishPlayer(spectator);
		}
		
		
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
		deadPlayer.teleport(deadPlayer.getWorld().getSpawnLocation());
		if(gamecomplete) return;
		if(!StillAlive.contains(deadPlayer)){
			return;
			
		}
		StillAlive.remove(deadPlayer);
		_deadPlayers.add(deadPlayer);
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
				GameEndEvent eventobj = new GameEndEvent(deadPlayer,FinishPositions);
				Bukkit.getServer().getPluginManager().callEvent(eventobj);
				deathwatcher.onGameEnd(eventobj);
				deathwatcher._Trackers.remove(this);
			return;	
			}
		if(StillAlive.size()==1){
			//notify the 'game' is finished, so the run loop can terminate.
			//announce the winner!
			Player winner = StillAlive.getFirst();
			Bukkit.broadcastMessage(winner.getName() + " has WON the event!");
			//raise custom event.
			
			
			GameEndEvent eventobj = new GameEndEvent(winner,FinishPositions);
			Bukkit.getServer().getPluginManager().callEvent(eventobj);
			deathwatcher.onGameEnd(eventobj);
			gamecomplete=true;
			//dead player explodes inexplicably.
			
			addprize(deadPlayer);
			gamecomplete=true;
			deathwatcher._Trackers.remove(this);
			//move players back to their original spots where desired.
			for(Player pp:_Owner.Randomcommand.returninfo.keySet()){
				
				pp.sendMessage(ChatColor.BLUE + "You will be returned to your before-game position in 5 seconds.");
				
				
			}
			_Owner.getServer().getScheduler().scheduleSyncDelayedTask(_Owner, new Runnable() {

				   public void run() {
					   for(Player pp:_Owner.Randomcommand.returninfo.keySet()){
							_Owner.Randomcommand.returninfo.get(pp).sendBack();
											
							
							
						}
				   }
				}, 5*20L);
			
			
			
			
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
	private boolean gavecompasses = false;
	@Override
	public void run() {
		// TODO Auto-generated method stub
	
		
		
		
		
		while(!gamecomplete)
		{
			
			try {
				synchronized(StillAlive) {
					
					if(StillAlive.size()<=3 && !gavecompasses){
						gavecompasses=true;
						for(Player givecompass:StillAlive){
							ItemStack CompassItem = new ItemStack(Material.COMPASS);
							ItemNamer.load(CompassItem);
							ItemNamer.setName("BASeCamp" + ChatColor.ITALIC + "(r)" + ChatColor.RESET + " Player Finder");
							CompassItem = ItemNamer.getItemStack();
							givecompass.getInventory().addItem(CompassItem);
							givecompass.sendMessage(ChatColor.GREEN + "You have been given a BASeCamp Player Finder!");
							givecompass.sendMessage(ChatColor.GREEN + "it points towards the closest participant.");
							
							
						}	
						
					}
					
					
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
		
		//make spectators unable to fly, since the game is over.
		for(Player Spectator:_Spectators){
			Location Spectatorlocation = Spectator.getLocation();
			int Yuse = Spectator.getWorld().getHighestBlockYAt(Spectator.getLocation());
			Location setLocation = new Location(Spectator.getWorld(),Spectatorlocation.getX(),Yuse,Spectatorlocation.getZ());
			//teleport them to a safe place before we make them no longer fly.
			Spectator.teleport(setLocation);
			Spectator.setAllowFlight(false);
			Spectator.setFlying(false);
			BCRandomizer.unvanishPlayer(Spectator);
			
		}
		//also, disable PvP again.
		if(runningWorld!=null){
		runningWorld.setPVP(false);}
		Bukkit.broadcastMessage(ChatColor.YELLOW + "Game Over. PvP re-disabled.");
		
		if(_Owner!=null && _Owner.ActiveGames!=null){
		_Owner.ActiveGames.remove(this);
		}
		
		//deathwatcher._Tracker=null;
		//TODO: fix nullpointer exception.
		if(deathwatcher!=null && deathwatcher._Trackers!=null){
		deathwatcher._Trackers.remove(this);}
		
	}
	//tracks game state, updating the list of still active players when players are killed (GameTracker is notified through PlayerDeathWatcher)
	public List<Player> getSpectating() {
		// TODO Auto-generated method stub
		return this._Spectators;
	}
	public List<Player> getDead() {
		// TODO Auto-generated method stub
		return _deadPlayers;
	}
	

}
