package com.BASeCamp.SurvivalChests;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import net.minecraft.server.TileEntityMobSpawner;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.potion.PotionEffect;
import org.fusesource.jansi.Ansi.Color;

import com.BASeCamp.SurvivalChests.*;

public class RandomizerCommand implements CommandExecutor {

	private BCRandomizer _Owner = null;

	private LinkedList<Player> joinedplayers = new LinkedList<Player>(); // list
																			// of
																			// players
																			// that
																			// accepted
																			// to
																			// join
																			// a
																			// game.
	private LinkedList<Player> spectating = new LinkedList<Player>(); // list of
																		// players
																		// ready
																		// to
																		// spectate
																		// a
																		// preparing
																		// game.
	private boolean accepting = false;
	private Location _SpawnSpot = null;
	public boolean getaccepting() { return accepting;}
	public LinkedList<Player> getjoinedplayers() {
		return joinedplayers;
	}

	World playingWorld = null;
	public HashMap<Player, ReturnData> returninfo = new HashMap<Player, ReturnData>();

	public BCRandomizer getOwner() {
		return _Owner;
	}

	public void setOwner(BCRandomizer value) {
		_Owner = value;
	}

	public RandomizerCommand(BCRandomizer Owner) {
		_Owner = Owner;

	}

	private String TeamList(Player[] listing) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < listing.length; i++) {

			sb.append(listing[i]);
			if (i < listing.length - 1)
				sb.append(",");

		}
		return sb.toString();

	}

	public List<Player> getAllPlayers() {

		LinkedList<Player> createlist = new LinkedList<Player>();
		for (World w : Bukkit.getWorlds()) {
			for (Player p : w.getPlayers()) {
				createlist.add(p);
			}

		}
		return createlist;

	}
	public static List<String> getPlayerNames(List<Player> source){
		LinkedList<String> retval = new LinkedList<String>();
		for(Player Playerp:source){
			retval.add(Playerp.getName());
		}
		
		return retval;
		
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
		BCRandomizer.emitmessage("onCommand:" + arg2);
		Player p = null;
		if (sender instanceof Player) {

			// make sure they have permission.

			p = (Player) sender;

			String usecmd = arg2.toLowerCase();
			String WorldName = p.getWorld().getName().toLowerCase();
			String permnode = "chestrandomizer." + WorldName + "." + usecmd;

			if (!p.hasPermission(permnode)
					&& !arg2.equalsIgnoreCase("joingame") && !arg2.equalsIgnoreCase("spectategame")) {
				if (!p.isOp()) {
					if (p.getGameMode() == GameMode.CREATIVE)
						p.setGameMode(GameMode.ADVENTURE);
					p.getWorld().strikeLightning(p.getLocation());
					p.getWorld().strikeLightningEffect(p.getLocation());
					p.damage(50);
					Bukkit
							.broadcastMessage(ChatColor.RED
									+ p.getName()
									+ " tried to use Chest Randomizer, but isn't an op. punishment applied.");
				} else {
					p
							.sendMessage("You do not have permission to use that command.");
					p.sendMessage(ChatColor.GREEN + "NODE:" + permnode);

				}
				return true;
			}
		}
		if(arg2.equalsIgnoreCase("arenaborder1")){
			
			BorderA = p.getLocation();
			p.sendMessage(ChatColor.AQUA + "BorderA set to (X,Z)=" + BorderA.getBlockX()+ "," + BorderA.getBlockZ());
			
		}
		else if(arg2.equalsIgnoreCase("arenaborder2")){
			
			BorderB = p.getLocation();
			p.sendMessage(ChatColor.AQUA + "BorderB set to (X,Z)=" + BorderB.getBlockX()+ "," + BorderB.getBlockZ());
			
		}
		else if(arg2.equalsIgnoreCase("clearborder")){
			
			BorderA=BorderB=null;
			p.sendMessage(ChatColor.AQUA + "Borders cleared.");
			
		}
		if (arg2.equalsIgnoreCase("gamestate")){
			//output information about running games.
			int currgame=1;
			p.sendMessage(ChatColor.RED + "Currently running games:" + _Owner.ActiveGames.size());
			for(GameTracker gt: _Owner.ActiveGames){
				p.sendMessage(ChatColor.RED + "Game:" + currgame);
				
				p.sendMessage(ChatColor.RED + "Alive:" + StringUtil.Join(getPlayerNames(gt.getStillAlive()),","));
				p.sendMessage(ChatColor.GRAY + "Dead:" + StringUtil.Join(getPlayerNames(gt.getDead()), ","));
				p.sendMessage(ChatColor.AQUA + "Spectating:" + StringUtil.Join(getPlayerNames(gt.getSpectating()), ","));
				
			}
			
			
			
			
		}
		if (arg2.equalsIgnoreCase("preparegame")) {
			// prepare game for the issuing players world.
			// unless a world is specified, that is.
			playingWorld = null;
			if (arg3.length == 1) {
				for (World w : Bukkit.getWorlds()) {

					if (w.getName().equalsIgnoreCase(arg3[0])) {
						// world found.
						playingWorld = w;
						_SpawnSpot = playingWorld.getSpawnLocation();
						break;
					}

				}
				
				
				
			} else if (p != null) {
				playingWorld = p.getWorld();
				_SpawnSpot = p.getLocation();
				playingWorld.setSpawnLocation((int)_SpawnSpot.getX(),(int)_SpawnSpot.getY(),(int)_SpawnSpot.getZ());
			}
			
			prepareGame(playingWorld);
		} else if (arg2.equalsIgnoreCase("joingame")) {
			if(!accepting)
			{
				
				p.sendMessage( ChatColor.RED + "You cannot join a game still in progress. use /spectategame if you want to observe.");
				return false;
				
			}
			if (p == null)
				return false;
			
			
			//fire the event.
			ParticipantJoinEvent joinevent = new ParticipantJoinEvent(p);
			Bukkit.getPluginManager().callEvent(joinevent);
			if(joinevent.getCancelled()) return false;
			
			if (p.getWorld() != playingWorld) {
				//teleport them to the world the game is in.
				returninfo.put(p, new ReturnData(p));
				Location spawnspot =_SpawnSpot;
				p.teleport(spawnspot);
				p.setBedSpawnLocation(spawnspot);
				
			}
				
				if(spectating.contains(p)){
					
					spectating.remove(p);
					
					
				}
				
				if (!joinedplayers.contains(p)) {
					joinedplayers.add(p);

				}
				else {
					p.sendMessage(ChatColor.RED + " You are already participating!");
					
					return false;
				}
						
			
			Bukkit.broadcastMessage(p.getDisplayName() + ChatColor.AQUA
					+ " is participating.(" + joinedplayers.size()
					+ " players)");
			
			Bukkit.broadcastMessage("Current participants:" + StringUtil.Join(getPlayerNames(joinedplayers), ","));
			
			
		} else if (arg2.equalsIgnoreCase("spectategame")) {
			
			if (p == null)
				return false;
			if (p.getWorld() == playingWorld) {
				if(joinedplayers.contains(p)){
					
				     joinedplayers.remove(p); //remove them from the participation list.	
				    
					
				}
				if (!spectating.contains(p)) {
					spectating.add(p);

				}
				else {
					p.sendMessage(ChatColor.YELLOW + "you are already spectating!");
					return false;
				}
			} else {
				returninfo.put(p,new ReturnData(p));
				Location spawnspot = playingWorld.getSpawnLocation();
				p.teleport(spawnspot);
				p.setBedSpawnLocation(_SpawnSpot);
			}

			Bukkit.broadcastMessage(Color.MAGENTA + p.getDisplayName() + " is spectating.");
			
			//if a game is in progress, make them invisible and flying.
			if(!accepting){
				p.setAllowFlight(true);
				BCRandomizer.VanishPlayer(p);
				
				
			}
			

		}
		if (arg2.equalsIgnoreCase("teamsplit")) {
			// get all online Players.
				//unused!
			LinkedList<Player> onlineplayers = new LinkedList<Player>();
			for (Player checkonline : p.getWorld().getPlayers()) {
				if (checkonline.isOnline()) {
					onlineplayers.add(checkonline);
				}

			}

			// now split contents of onlineplayers in half.
			LinkedList<Player> TeamA = new LinkedList<Player>();
			LinkedList<Player> TeamB = new LinkedList<Player>();
			for (int i = 0; i < onlineplayers.size() / 2; i++) {

				Player[] theplayers = new Player[onlineplayers.size()];
				onlineplayers.toArray(theplayers);
				Player addteamA = RandomData.Choose(theplayers);
				TeamA.add(addteamA);
				onlineplayers.remove(addteamA);

			}
			TeamB = onlineplayers;

			Player[] TeamAArr = new Player[TeamA.size()];
			Player[] TeamBArr = new Player[TeamB.size()];
			TeamA.toArray(TeamAArr);
			TeamB.toArray(TeamBArr);

			Bukkit.broadcastMessage("Team A is " + TeamList(TeamAArr));
			Bukkit.broadcastMessage("Team B is " + TeamList(TeamBArr));

			for (Player APlayer : TeamAArr) {
				APlayer.setDisplayName("[TEAM A]" + APlayer.getDisplayName());
				APlayer.sendMessage(ChatColor.YELLOW + "You are on Team A!");

			}

			for (Player BPlayer : TeamBArr) {
				BPlayer.setDisplayName("[TEAM B]" + BPlayer.getDisplayName());
				BPlayer.sendMessage(ChatColor.YELLOW + "You are on Team B!");

			}

		}

		if (arg2.equalsIgnoreCase("startgame")) {
			int numseconds=30;
			if(arg3.length > 0){
			try {
				numseconds = Integer.parseInt(arg3[0]);
				
				
			}
			catch(Exception exx){ numseconds=30;}
			}
			StartGame(p,numseconds);
			return false;
		}
		if (arg2.equalsIgnoreCase("friendly")) {
			String friendly = (PlayerDeathWatcher.getFriendlyNameFor(p
					.getItemInHand()));
			p.sendMessage(ChatColor.YELLOW + "Name of item is " + friendly);

		}
		if (arg2.equalsIgnoreCase("stopallgames")) {
			int numgames=0;
			numgames = stopAllGames();
			_Owner.ActiveGames = new LinkedList<GameTracker>();
			p.sendMessage(numgames + " games stopped.");
		}
		if (arg2.equalsIgnoreCase("repopchests")) {

			String usesource = "";
			if(arg3.length > 0){ usesource=arg3[0];}
			repopulateChests(usesource, p.getWorld());

		}

		return false;
	}
	private int stopAllGames() {
		int retval = 0;
		for (GameTracker iterate : _Owner.ActiveGames) {
			
			//inform the players they're game was cancelled.
			for(Player tellem:iterate.getStillAlive()){
				
				tellem.sendMessage(ChatColor.RED + "SURVIVAL:" + "The game you are in has been cancelled!");
				
			}
			
			iterate.gamecomplete = true;
			retval++;

		}
		return retval;
	}
	private void prepareGame(World inWorld) {
		
		
		PrepareGameEvent pge = new PrepareGameEvent();
		Bukkit.getPluginManager().callEvent(pge);
		if(pge.getCancelled()){
			//preparegame cancelled...
			accepting=false;
			return;
			
			
		}
		
		playingWorld = inWorld;
		playingWorld.setPVP(false);
		accepting = true;
		joinedplayers.clear();
		spectating.clear();
		
		
		
		
		for (Player px : getAllPlayers()) {

			px.sendMessage(ChatColor.RED + "SURVIVAL:" + ChatColor.YELLOW
					+ " A Survival game has started in "
					+ playingWorld.getName());
			px
					.sendMessage(ChatColor.RED + "SURVIVAL:" + ChatColor.YELLOW
							+ " use /joingame to participate before the game starts.");

		}
		
		
		
		
	
	}
	Location BorderA = null;
	Location BorderB = null;
	private void StartGame(Player p,int numseconds) {
		accepting = false;
		if (joinedplayers.size() == 0) {
			if(p!=null)
				p.sendMessage("No players participating! Cannot start game.");
			else
				System.out.println("No players participating! Cannot start game.");
			return;
		}
		if (_Owner.ActiveGames.size() > 0) {
			//this is for debugging. Right now it will only allow one game at a time.
			if(p!=null)
			p
					.sendMessage(ChatColor.YELLOW
							+ "Game is already in progress! use /stopallgames to stop current games.");
			else
				System.out.println("Game in progress. use /stopallgames to stop current games.");
		}

		
		String ignoreplayer = null;
	
		World grabworld = p.getWorld();
	
		
		
		ResumePvP rp = new ResumePvP(_Owner, p.getWorld(), numseconds,
				joinedplayers, spectating);
		
		GameStartEvent eventobj =new GameStartEvent(joinedplayers,spectating);
		Bukkit.getServer().getPluginManager().callEvent(eventobj);
		
		
		Bukkit.broadcastMessage(ChatColor.GOLD + "Survival Event "
				+ ChatColor.GREEN + " has begun in world "
				+ ChatColor.DARK_AQUA + grabworld.getName() + "!");
		Bukkit.broadcastMessage(joinedplayers.size() + " Players.");
		grabworld.setPVP(false);

		// iterate through all online players.
		for (Player pl : joinedplayers) {

			if (pl.isOnline()) {

				pl
						.sendMessage(ChatColor.BLUE
								+ "Your Inventory has been cleared. No outside food, please.");
				BCRandomizer.clearPlayerInventory(pl);
				for (PotionEffect iterate : pl.getActivePotionEffects())
					pl.removePotionEffect(iterate.getType());
				
				pl.setExp(0);
				pl.setLevel(0);
				pl.setHealth(20);
				pl.setExhaustion(20);
				pl.setSaturation(20);
				pl.setGameMode(GameMode.ADVENTURE);

				pl.playSound(pl.getLocation(), Sound.ENDERMAN_HIT, 1.0f,
						1.0f);
			}

		}
		ChestRandomizer.resetStorage();
		repopulateChests("", p.getWorld(),true);
		ResumePvP.BroadcastWorld(grabworld, ChatColor.LIGHT_PURPLE + " Containers randomized.");
		
		GameStartEvent evento= new GameStartEvent(joinedplayers,spectating);
		rp.getTracker().deathwatcher.onGameStart(eventobj);
		
		
		
		ResumePvP.BroadcastWorld(grabworld, ChatColor.GREEN
				+ "PvP will be re-enabled in " + ChatColor.RED
				+ numseconds + ChatColor.GREEN + " Seconds! get ready.");
		
		Thread thr = new Thread(rp);
		thr.start();
	}
	private void repopulateChests(String Source,World w){
		
		
		repopulateChests(Source,w,false);
	}
	private boolean hasBlockBeneath(Block testblock,Material testmaterial){
		
		
		Location spotbelow = new Location(testblock.getWorld(),testblock.getX(),testblock.getY()-1,testblock.getZ());
		return testblock.getWorld().getBlockAt(spotbelow).getType().equals(testmaterial);
		
		
	}
	private void repopulateChests(String Source, World w,boolean silent) {
		int populatedamount = 0;
		LinkedList<Chest> allchests = new LinkedList<Chest>();
		LinkedList<Furnace> allfurnaces = new LinkedList<Furnace>();
		LinkedList<Dispenser> alldispensers = new LinkedList<Dispenser>();
		World gotworld = w;
		if(!silent) Bukkit.broadcastMessage("BASeCamp Chest Randomizer- Running...");
		String sourcefile = Source;
		
		// randomize the enderchest contents, too :D
		for (Player popplayer : gotworld.getPlayers()) {
			Inventory grabinv = popplayer.getEnderChest();
			ChestRandomizer cr = new ChestRandomizer(_Owner, grabinv,
					sourcefile);
			cr.setMinItems(grabinv.getSize());
			cr.setMaxItems(grabinv.getSize());
			cr.Shuffle();

		}

		Chunk[] iteratechunks = gotworld.getLoadedChunks();
		// iterate through all the chunks...
		for (Chunk iteratechunk : iteratechunks) {
			// go through all tileentities and look for Chests.
			BlockState[] entities = iteratechunk.getTileEntities();
			for (BlockState iteratestate : entities) {
				if (iteratestate instanceof Chest) {
					Chest casted = (Chest) iteratestate;
					// randomize!
					if(!hasBlockBeneath(iteratestate.getBlock(),Material.WOOL)){
						allchests.add(casted);
						ChestRandomizer cr = new ChestRandomizer(_Owner,
								casted, sourcefile);
						populatedamount += cr.Shuffle();
					}	else{
						System.out.println("Storing inventory for a chest");
						ChestRandomizer.StoreInventory(casted);
						
					}
				}
				else if(iteratestate instanceof Furnace){
					Furnace casted =(Furnace)iteratestate;
					allfurnaces.add(casted);
					ChestRandomizer cr = new ChestRandomizer(_Owner,casted.getInventory(),sourcefile);
					populatedamount+= cr.Shuffle();
				}
				else if(iteratestate instanceof Dispenser){
					Dispenser casted = (Dispenser)iteratestate;
					alldispensers.add(casted);
					ChestRandomizer cr = new ChestRandomizer(_Owner,casted.getInventory(),sourcefile);
					populatedamount += cr.Shuffle();
					
						
						
				}
				else if(iteratestate instanceof TileEntityMobSpawner){
				TileEntityMobSpawner mspawner = (TileEntityMobSpawner)iteratestate;
				//would be fun to randomize mob spawners, but that should be later and a different class and whatnot...
				}
					
				

			    }

		}

		// turn chest LinkedList into an array.
		Chest[] chestchoose = new Chest[allchests.size()];
		allchests.toArray(chestchoose);

		int StaticAdded = 0;
		if(!silent) {
		Bukkit.broadcastMessage(ChatColor.AQUA.toString()
				+ allchests.size() + ChatColor.YELLOW
				+ " Chests Populated.");
		Bukkit.broadcastMessage(ChatColor.AQUA.toString() + 
		allfurnaces.size() + ChatColor.RED + "Furnaces " + ChatColor.YELLOW + "Populated.");
		
		Bukkit.broadcastMessage(ChatColor.AQUA + "" + alldispensers.size() + ChatColor.GREEN + " Dispensers " + ChatColor.YELLOW + " Populated.");
		
		Bukkit.broadcastMessage(ChatColor.YELLOW + "Populated "
				+ ChatColor.AQUA.toString() + populatedamount
				+ ChatColor.YELLOW + " slots.");
		}
		for (RandomData iterate : ChestRandomizer.addall) {

			ItemStack result = iterate.Generate();
			if (result != null) {
				// choose a random chest.
				Chest chosen = RandomData.Choose(chestchoose);
				Inventory iv = chosen.getBlockInventory();
				// BCRandomizer.emitmessage("Added Static Item:" +
				// result.toString());
				iv.addItem(result);
				StaticAdded++;
			}
		}
		if(!silent) Bukkit.broadcastMessage(ChatColor.YELLOW + "Added "
				+ ChatColor.AQUA.toString() + StaticAdded
				+ ChatColor.YELLOW + " Static items.");
	}
}