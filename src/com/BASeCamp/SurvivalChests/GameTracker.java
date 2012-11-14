package com.BASeCamp.SurvivalChests;

import java.security.acl.Owner;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class GameTracker implements Runnable {

	private World runningWorld = null;
	private LinkedList<Player> StillAlive = new LinkedList<Player>();
	private List<Player> _Spectators = null;
	private LinkedList<Player> _deadPlayers = null;
	public  LinkedList<Player> getStillAlive(){return StillAlive;} 
	private HashMap<Integer,Player> FinishPositions = new HashMap<Integer,Player>();
	BCRandomizer _Owner = null;
	public CoreEventHandler deathwatcher = null;
	private boolean _MobArenaMode = false; //MobArena Mode is when PvP is disabled completely.
	
	
	private HashMap<Player,Integer> ScoreTally = new HashMap<Player,Integer>();
	
	//Note that gameTracker also tracks Mob Arena style games.
	//Mob Arena is similar in principle to the Mob Arena Plugin, but is more an extension
	//of the Player Versus Player aspects of this plugin to extend to a different style.
	
	public boolean getMobArenaMode() { return _MobArenaMode;}
	
	
	public HashMap<Player,Integer> getScoreTally() { return ScoreTally;}
	
	public GameTracker(BCRandomizer Owner, World applicableWorld,List<Player> Participants,List<Player> spectators){
		
		this(Owner,applicableWorld,Participants,spectators,false);
		
	}
	public GameTracker(BCRandomizer Owner,World applicableWorld,List<Player> Participants, List<Player> spectators,boolean MobArena){
		//initialize StillAlive List.
	      deathwatcher= new CoreEventHandler(Owner,this,applicableWorld);
	      Owner.getServer().getPluginManager().registerEvents(deathwatcher, Owner);
		_MobArenaMode = MobArena;
		_Owner = Owner;
		_Owner.ActiveGames.add(this);
		_Spectators = spectators;
		ScoreTally = new HashMap<Player,Integer>();
		for(Player p:Participants){
			StillAlive.add(p);
			ScoreTally.put(p, 0);
			
		}
		_deadPlayers = new LinkedList<Player>();
		//raise custom event.
		if(!_MobArenaMode)
		   Bukkit.broadcastMessage(BCRandomizer.Prefix + "Survival game started! " + StillAlive.size() + " participants.");
		else
		{
			
			
			
		   Bukkit.broadcastMessage(BCRandomizer.Prefix + "Mob Arena game started! " + StillAlive.size() + " participants.");
		}
		String[] participantNames = new String[StillAlive.size()];
		
		int i=0;
		for(Player Alive:StillAlive){
			
			participantNames[i] = Alive.getDisplayName();
			i++;
		}
		i=0;
		
		Bukkit.broadcastMessage(BCRandomizer.Prefix  +ChatColor.RED + "participating:" + StringUtil.Join(participantNames,","));
		
		//now, we set everything up: vanish all the spectators.
		
		for(Player spectator : _Spectators){
			for(Player joined:Participants){
				
				joined.hidePlayer(spectator);
				
			}
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
				Bukkit.broadcastMessage("All players participating died.");
				if(_MobArenaMode) broadcastresults();
				addprize(deadPlayer);
				gamecomplete=true;
				GameEndEvent eventobj = new GameEndEvent(deadPlayer,FinishPositions);
				Bukkit.getServer().getPluginManager().callEvent(eventobj);
				deathwatcher.onGameEnd(eventobj);
				deathwatcher._Trackers.remove(this);
			return;	
			}
		if(StillAlive.size()==1 && !_MobArenaMode){
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
				
				pp.sendMessage(BCRandomizer.Prefix +  ChatColor.BLUE + "You will be returned to your before-game position in 5 seconds.");
				
				
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
			
			if(StillAlive.size()==3 && !_MobArenaMode){
				gavecompasses=true;
				for(Player givecompass:StillAlive){
					ItemStack CompassItem = new ItemStack(Material.COMPASS);
					ItemNamer.load(CompassItem);
					ItemNamer.setName("BASeCamp" + ChatColor.ITALIC + "(r)" + ChatColor.RESET + " Player Finder");
					CompassItem = ItemNamer.getItemStack();
					givecompass.getInventory().addItem(CompassItem);
					givecompass.sendMessage(BCRandomizer.Prefix +ChatColor.GREEN + "You have been given a BASeCamp Player Finder!");
					givecompass.sendMessage(BCRandomizer.Prefix +ChatColor.GREEN + "it points towards the closest participant.");
					
					
				}	
				
				
			}
		}
		}
		
	}
	private void broadcastresults() {
		// TODO Auto-generated method stub
		
		//broadcast the game points!
		
		//we need to sort it by the score.
		SortedSet<KeyValuePair<Integer,Player>> sortset = new TreeSet<KeyValuePair<Integer,Player>>();
		
		//iterate through ScoreTally.
		//iterate through the keys.
		for(Player pkey:ScoreTally.keySet()){
			
			sortset.add(new KeyValuePair<Integer,Player>(ScoreTally.get(pkey),pkey));
			
			
			
		}
		//default iterator will go in ascending order.
		//create a stack, iterate, and then pop from the stack until it is empty. This
		//should go through the same iterator backwards.
		Stack<KeyValuePair<Integer,Player>> reversing = new Stack<KeyValuePair<Integer,Player>>();
		for(KeyValuePair<Integer,Player> setitem:sortset){
			
			reversing.push(setitem);
			
			
		}
		int Position = 1;
		Bukkit.broadcastMessage(BCRandomizer.Prefix +   "--Results--");
		while(!reversing.isEmpty()){
			//FINALLY we can output the results.
			KeyValuePair<Integer,Player> element = reversing.pop();
			Bukkit.broadcastMessage(Position + ":" + element.getValue().getDisplayName() + ", Score of " + element.getKey().toString());
			
			
			
			
			
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
							givecompass.sendMessage(BCRandomizer.Prefix +ChatColor.GREEN + "You have been given a BASeCamp Player Finder!");
							givecompass.sendMessage(BCRandomizer.Prefix +ChatColor.GREEN + "it points towards the closest participant.");
							
							
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
	public void TimeoutExpired() {
		
		
	
		if(!_MobArenaMode){
		for(Player iterate:StillAlive)
		{
			int numactive = 0;
			runningWorld.setPVP(true);
			if(iterate.isOnline()) {
				numactive++;
		       Location currlocation = iterate.getLocation();
		       currlocation = new Location(runningWorld,currlocation.getX()+5,currlocation.getY()-15,currlocation.getZ()-5);
		       runningWorld.playEffect(currlocation, Effect.GHAST_SHRIEK, 10);
		
		       //useWorld.strikeLightning(currlocation);
		     
		       
		       
		       
		       
		       iterate.addPotionEffect(Potion.getBrewer().createEffect(PotionEffectType.BLINDNESS, 500, 1));
		       iterate.sendMessage(ChatColor.BOLD.toString() +ChatColor.LIGHT_PURPLE + "You have been temporarily blinded!");
			}
		}
		
		
		
		Bukkit.broadcastMessage(ChatColor.RED + "PvP Enabled in World + " + runningWorld.getName());
		Bukkit.broadcastMessage(ChatColor.GREEN + "Good luck to all contestants! May luck favour you! ;)");
		}
		else
		{
			
			
			
			
		}
		
		
	}
	

}
