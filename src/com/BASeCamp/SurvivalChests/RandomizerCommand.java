package com.BASeCamp.SurvivalChests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;



import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.inventory.*;
import org.bukkit.potion.PotionEffect;
//import org.fusesource.jansi.Ansi.Color;

import BASeCamp.Configuration.INIFile;

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
	private boolean MobArenaMode = false;
	private int ChestTimeout=0;
	
	private int uselives = Integer.MAX_VALUE; //number of lives, defaults to int.MaxValue.
	
	private Location _SpawnSpot = null;
	private int MobTimeout = 0; //0 means no mobtimeout at all. any other value is a number of seconds
	//before Mob spawning will be force-enabled.
	
	public int getChestTimeout() { return ChestTimeout;}
	public void setChestTimeout(int value) { ChestTimeout = value;}
	
	public boolean getaccepting() {
		return accepting;
	}

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

	public static List<String> getPlayerNames(List<Player> source) {
		LinkedList<String> retval = new LinkedList<String>();
		for (Player Playerp : source) {
			retval.add(Playerp.getName());
		}

		return retval;

	}

	private void addSpectator(Player p) {

		spectating.add(p);
		// if there is a game in progress, we need to also give them fly and
		// vanish them.
		if (isGameActive()) {
			p.setAllowFlight(true);
			for (Player pp : joinedplayers) {

				pp.hidePlayer(p);

			}
		}

	}
	private void enableMobSpawns()
	{
		for(GameTracker gt:_Owner.ActiveGames){
			gt.getWorld().setGameRuleValue("doMobSpawns", "true");
			
			
			
		}
		
	}
	private void saveborder(World w)
	{
		String borderfile = BCRandomizer.pluginMainDir + File.separatorChar + w.getName() + ".border";
		File bfile = new File(borderfile);
		try {
		FileWriter fw = new FileWriter(bfile);
		fw.write(useBorderA.getBlockX());
		fw.write(useBorderA.getBlockY());
		fw.write(useBorderA.getBlockZ());
			
		fw.write(useBorderB.getBlockX());
		fw.write(useBorderB.getBlockY());
		fw.write(useBorderB.getBlockZ());
		
		fw.close();
		}
		catch(IOException iox)
		{
		
		}
	}
	private void loadborder(World w)
	{
		
		//look for <worldname.border> in plugin folder.
		String borderfile = BCRandomizer.pluginMainDir + File.separatorChar + w.getName() + ".border";
		File bfile = new File(borderfile);
		if(bfile.exists()){
			try {
			FileReader fr = new FileReader(bfile);
			
			//read a few ints... 6, 3 for each border.
			int ax,ay,az;
			ax = fr.read();
			ay = fr.read();
			az = fr.read();
			int bx,by,bz;
			bx = fr.read();
			by = fr.read();
			bz = fr.read();
			fr.close();
			useBorderA = new Location(w,ax,ay,az);
			useBorderB = new Location(w,bx,by,bz);
			
			
			
			}
			catch(IOException iox){}
			
			
		}
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
			if(usecmd.equalsIgnoreCase("strike"))
			{
				Block b = p.getTargetBlock(null, 200);
				Location l = b.getLocation();
				l.getWorld().strikeLightning(l);
				
				
			
			}
			if (!p.hasPermission(permnode)
					&& !arg2.equalsIgnoreCase("joingame")
					&& !arg2.equalsIgnoreCase("spectategame")) {
				if (!p.isOp()) {
					if (p.getGameMode() == GameMode.CREATIVE)
						p.setGameMode(GameMode.ADVENTURE);
					p.getWorld().strikeLightning(p.getLocation());
					p.getWorld().strikeLightningEffect(p.getLocation());
					p.damage(50);
					Bukkit
							.broadcastMessage(ChatColor.RED
									+ p.getName()
									+ " tried to use a SurvivalChest command, but isn't an op. punishment applied.");
				} else {
					p
							.sendMessage(BCRandomizer.Prefix
									+ "You do not have permission to use that command.");
					p.sendMessage(BCRandomizer.Prefix + ChatColor.GREEN
							+ "NODE:" + permnode);

				}
				return true;
			}
		}
		
		if(arg2.equalsIgnoreCase("fixup")){
			
			doFixUp(p);
			
			
		}
		else if (arg2.equalsIgnoreCase("randomizespawners")) {
			SpawnerRandomizer sr = new SpawnerRandomizer(_Owner);
			sr.RandomizeSpawners(p.getWorld());
		}
		else if(arg2.equalsIgnoreCase("setlives")){
			if(arg3.length==0)
			{
				String usemessage = BCRandomizer.Prefix + "Lives:" + String.valueOf(uselives);
				
			}
			else
			{
				try {
					uselives = Integer.parseInt(arg3[0]);
					if(uselives==0) uselives = Integer.MAX_VALUE;
					String usemessage = BCRandomizer.Prefix + "Lives set to " + String.valueOf(uselives);
					if(p!=null) p.sendMessage(usemessage); else System.out.println(usemessage);
				}
				catch(NumberFormatException nfe)
				{
					
				}
			}
			
			
			
		}
		else if(arg2.equalsIgnoreCase("repoptimeout"))
		{
			////if no arguments given, show current.
			if(arg3.length <1)
			{
				String usemessage = 
						BCRandomizer.Prefix + "repoptimeout is currently set to " + String.valueOf(this.ChestTimeout/20) + " seconds.";
				
				if(p==null) System.out.println(usemessage); else p.sendMessage(usemessage);
				
			}
			else {
				String usemessage = "";
				try {
					ChestTimeout = Integer.parseInt(arg3[0])*20;
					usemessage = BCRandomizer.Prefix + "repoptimeout set to " + String.valueOf(ChestTimeout/20) + " seconds.";
					
				}
				catch(NumberFormatException nfe)
				{
					usemessage = BCRandomizer.Prefix + " Invalid Input. repoptimeout must be a number.";
				}
				if(p==null) System.out.println(usemessage); else p.sendMessage(usemessage);	
			}
			
			
		}
		else if(arg2.equalsIgnoreCase("mobtimeout")){
			
			if(arg3.length < 1)
				if(p==null) System.out.println("insufficient arguments");
				else {p.sendMessage(BCRandomizer.Prefix + "syntax: /mobtimeout <numberofseconds>");
				p.sendMessage(BCRandomizer.Prefix + "current value:" + MobTimeout);
				}
				
			else {
				try {
				MobTimeout= Integer.parseInt(arg3[0]);
				}
				catch(Exception exx){}
				
			}
			
		}
		else if(arg2.equalsIgnoreCase("mobsweeper")){
			
			//force enable mob spawns for active PvP match.
			enableMobSpawns();
		}
		else if(arg2.equalsIgnoreCase("setfly")){
			if(arg3.length < 2) {
				
				p.sendMessage("Insufficient arguments.");
				
			}
			else {
				String playername = arg3[0];
				boolean flyset = Boolean.parseBoolean(arg3[1]);
				
				for(Player pl:Bukkit.getOnlinePlayers()){
					
					
					pl.setFlying(flyset);
					p.sendMessage("Player " + pl.getName() + " flying set to " + flyset);
					
					
				}
				
			
			}
			
		}else if(arg2.equalsIgnoreCase("saveborder"))
		{
			saveborder(p.getWorld());
		}
		else if(arg2.equalsIgnoreCase("loadborder")){
			loadborder(p.getWorld());
		} else if (arg2.equalsIgnoreCase("arenaborder1")) {

			useBorderA = p.getLocation();
			p.sendMessage(BCRandomizer.Prefix + ChatColor.AQUA
					+ "BorderA set to (X,Z)=" + useBorderA.getBlockX() + ","
					+ useBorderA.getBlockZ());

		} 
		
		
		else if (arg2.equalsIgnoreCase("arenaborder2")) {

			useBorderB = p.getLocation();
			p.sendMessage(BCRandomizer.Prefix + ChatColor.AQUA
					+ "BorderB set to (X,Z)=" + useBorderB.getBlockX() + ","
					+ useBorderB.getBlockZ());

		} else if (arg2.equalsIgnoreCase("clearborder")) {

			useBorderA = useBorderB = null;
			p.sendMessage(BCRandomizer.Prefix + ChatColor.AQUA
					+ "Borders cleared.");

		}
		if (arg2.equalsIgnoreCase("gamestate")) {
			// output information about running games.
			int currgame = 1;
			p.sendMessage(BCRandomizer.Prefix + ChatColor.RED
					+ "Currently running games:" + _Owner.ActiveGames.size());
			for (GameTracker gt : _Owner.ActiveGames) {
				p.sendMessage(BCRandomizer.Prefix + ChatColor.RED + "Game:"
						+ currgame);

				p.sendMessage(BCRandomizer.Prefix
						+ ChatColor.RED
						+ "Alive:"
						+ StringUtil.Join(getPlayerNames(gt.getStillAlive()),
								","));
				p.sendMessage(BCRandomizer.Prefix + ChatColor.GRAY + "Dead:"
						+ StringUtil.Join(getPlayerNames(gt.getDead()), ","));
				p.sendMessage(BCRandomizer.Prefix
						+ ChatColor.AQUA
						+ "Spectating:"
						+ StringUtil.Join(getPlayerNames(gt.getSpectating()),
								","));

			}

		}
		if (arg2.equalsIgnoreCase("preparegame")) {
			// prepare game for the issuing players world.
			// unless a world is specified, that is.
			playingWorld = null;
			if (arg3.length >= 1) {
				if((Bukkit.getWorld(arg3[0])!=null))
						{
							playingWorld=Bukkit.getWorld(arg3[0]);
							_SpawnSpot=playingWorld.getSpawnLocation();
						}
						
						


			} else if (p != null) {
				playingWorld = p.getWorld();
				_SpawnSpot = p.getLocation();
				playingWorld.setSpawnLocation((int) _SpawnSpot.getX(),
						(int) _SpawnSpot.getY(), (int) _SpawnSpot.getZ());
			}
			int numseconds=0; //number of seconds will be passed to PrepareGame.
			if(arg3.length>=2){
				//check for number of seconds...
				
				try {
					numseconds = Integer.parseInt(arg3[1]);
				
				}
				catch(NumberFormatException nfe){
					numseconds=0;
				}
				
				
			}
			int preparetime = 0;
			if(arg3.length>=3){
				try {preparetime = Integer.parseInt(arg3[2]);}catch(NumberFormatException nfe){preparetime=0;}}
			
				
			
			
			prepareGame(playingWorld,numseconds,preparetime);
		} else if (arg2.equalsIgnoreCase("joingame")) {
			if (!accepting) {

				p
						.sendMessage(BCRandomizer.Prefix
								+ ChatColor.RED
								+ "You cannot join a game still in progress. use /spectategame if you want to observe.");
				return false;

			}
			if (p == null)
				return false;

			// fire the event.
			ParticipantJoinEvent joinevent = new ParticipantJoinEvent(p);
			Bukkit.getPluginManager().callEvent(joinevent);
			if (joinevent.getCancelled())
				return false;

			if (p.getWorld() != playingWorld) {
				// teleport them to the world the game is in.
				returninfo.put(p, new ReturnData(p));
				Location spawnspot = _SpawnSpot;
				p.teleport(spawnspot);
				p.setBedSpawnLocation(spawnspot);

			}

			if (spectating.contains(p)) {

				spectating.remove(p);

			}

			if (!joinedplayers.contains(p)) {
				joinedplayers.add(p);

			} else {
				p
						.sendMessage(ChatColor.RED
								+ " You are already participating!");

				return false;
			}

			Bukkit.broadcastMessage(p.getDisplayName() + ChatColor.AQUA
					+ " is participating.(" + joinedplayers.size()
					+ " players)");

			Bukkit.broadcastMessage("Current participants:"
					+ StringUtil.Join(getPlayerNames(joinedplayers), ","));
		}
		else if(arg2.equalsIgnoreCase("prepareinfo")){
			if(p==null) return false;
			
			String[] Participants = new String[joinedplayers.size()];
			String[] Spectators = new String[spectating.size()];
			int currparticipant = 0;
			for(Player participant:joinedplayers){
				
				Participants[currparticipant] = participant.getDisplayName();
				
				
			}
			
			int currspectator = 0;
			for(Player spectator:spectating){
				
				Spectators[currspectator] = spectator.getDisplayName();
				
			}
			
			
			String Joined = StringUtil.Join(Participants, ",");
			String spectates = StringUtil.Join(Spectators, ",");
			
			p.sendMessage(BCRandomizer.Prefix +  joinedplayers.size() + " participants:");
			p.sendMessage(BCRandomizer.Prefix + Joined);
			p.sendMessage(BCRandomizer.Prefix + spectating.size() + " Spectating:");
			p.sendMessage(BCRandomizer.Prefix + spectates);
			
			
			
			
		} else if (arg2.equalsIgnoreCase("spectategame")) {

			if (p == null)
				return false;
			if (p.getWorld() == playingWorld) {
				if (joinedplayers.contains(p)) {

					joinedplayers.remove(p); // remove them from the
					// participation list.

				}
				if (!spectating.contains(p)) {
					addSpectator(p);
				} else {
					p.sendMessage(ChatColor.YELLOW
							+ "you are already spectating!");
					return false;
				}
			} else {
				returninfo.put(p, new ReturnData(p));
				Location spawnspot = playingWorld.getSpawnLocation();
				p.teleport(spawnspot);
				p.setBedSpawnLocation(_SpawnSpot);
			}

			Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + p.getDisplayName()
					+ " is spectating.");

			// if a game is in progress, make them invisible and flying.
			if (!accepting) {
				p.setAllowFlight(true);
				GameTracker gotgt = _Owner.getWorldGame(p.getWorld());
				if (gotgt != null)
					gotgt.deathwatcher.hidetoParticipants(p);

			}

		} else if (arg2.equalsIgnoreCase("mobmode")) {

			MobArenaMode = !MobArenaMode;
			p.sendMessage("mob arena mode set to '" + MobArenaMode + "'");

		}
		if (arg2.equalsIgnoreCase("teamsplit")) {
			// get all online Players.
			// unused!
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
			int numseconds = 30;
			if (arg3.length > 0) {
				try {
					numseconds = Integer.parseInt(arg3[0]);

				} catch (Exception exx) {
					numseconds = 30;
				}
			}
			StartGame(p, numseconds, MobArenaMode);
			return false;
		}
		if (arg2.equalsIgnoreCase("friendly")) {
			String friendly = (CoreEventHandler.getFriendlyNameFor(p
					.getItemInHand()));
			p.sendMessage(ChatColor.YELLOW + "Name of item is " + friendly);

		}
		if (arg2.equalsIgnoreCase("stopallgames")) {
			int numgames = 0;
			numgames = stopAllGames();
			_Owner.ActiveGames = new LinkedList<GameTracker>();
			p.sendMessage(numgames + " games stopped.");
		}
		if (arg2.equalsIgnoreCase("repopchests")) {

			String usesource = "";
			if (arg3.length > 0) {
				usesource = arg3[0];
			}
			repopulateChests(usesource, p.getWorld());

		}

		return false;
	}

	private void doFixUp(Player p) {
		//this method has a simple task:
		//It attempts to "fix" all running games.
		
		//this consists of making sure all spectators can fly and participants cannot, and ensuring consistent visibility settings
		//between them.
		//of p is a Player, it will send any relevant information to them.
		//particularly when a fixup needs to be applied.
		
		//first, check fly and not flying...
		if(_Owner.ActiveGames.size() ==0 ){
			if (p!=null) p.sendMessage(BCRandomizer.Prefix + ChatColor.RED + " No Active games to apply fixups. resetting all players.");
			
			if(p!=null){
				
				for(Player fixplayer:p.getWorld().getPlayers()){
					
					if(fixplayer.getAllowFlight()){
						if(p!=null) p.sendMessage("Reverting AllowFlight and Flight for " + fixplayer.getDisplayName());
					
						fixplayer.setAllowFlight(false);
						fixplayer.setFlying(false);
					}
					
				}
				
				
			}
			
			return;
			
			
		}
		if (p!=null) p.sendMessage(BCRandomizer.Prefix + ChatColor.RED + " Attempting fixups on "
				+ ChatColor.AQUA + _Owner.ActiveGames.size() + " Games...");
		for(GameTracker iterate :_Owner.ActiveGames){
			
		//make sure all participants are "grounded"...
			for(Player participant:iterate.getStillAlive()){
				
				
				if(participant.getAllowFlight()){
					if(p!=null) p.sendMessage(BCRandomizer.Prefix + ChatColor.RED + 
							" clearing set Flight for participant " + participant.getDisplayName());
					
					
					participant.setAllowFlight(false);
					participant.setFlying(false);
					
					
				}
				
			}
			//make sure spectators can fly...
			
			for(Player spectator: iterate.getSpectating()){
				
				if(!spectator.getAllowFlight()){
					
					if(p!=null) p.sendMessage(BCRandomizer.Prefix + ChatColor.RED + " setting flight for spectator " + spectator.getDisplayName());
					spectator.setAllowFlight(true);
					
				}
				
				
			}
			
			//now, we first reset ALL visibility statuses...
			for(Player x:iterate.getWorld().getPlayers()){
			    for(Player y:iterate.getWorld().getPlayers()){
			    	
			    	
			    	if(x!=y){
			    		x.showPlayer(y);
			    		
			    	}
			    	
			    	
			    }
				
				
				
				
				
			}
			
			//and now, we make spectators invisible to participants.
			
			for(Player participator:iterate.getStillAlive()){
				
			for(Player hidespectator:iterate.getSpectating()){
				
				
				participator.hidePlayer(hidespectator);
				
				
			}
				
				
				
			}
			
			
			
			
			
		}
		
		
		
		
		
		
		
	}

	private int stopAllGames() {
		int retval = 0;
		for (GameTracker iterate : _Owner.ActiveGames) {

			// inform the players they're game was cancelled.
			for (Player tellem : iterate.getStillAlive()) {

				tellem.sendMessage(ChatColor.RED + "SURVIVAL:"
						+ "The game you are in has been cancelled!");

			}

			iterate.gamecomplete = true;
			retval++;

		}
		return retval;
	}

	private void prepareGame(World inWorld,int delaystart,final int prepatorydelay) {
		
		PrepareGameEvent pge = new PrepareGameEvent();
		Bukkit.getPluginManager().callEvent(pge);
		if (pge.getCancelled()) {
			// preparegame cancelled...
			accepting = false;
			return;

		}

		playingWorld = inWorld;
		playingWorld.setPVP(false);
		accepting = true;
		joinedplayers.clear();
		spectating.clear();

		for (Player px : getAllPlayers()) {

			if (MobArenaMode) {
				px.sendMessage(BCRandomizer.Prefix + ChatColor.YELLOW
						+ " A Mob Arena game is being prepared in "
						+ playingWorld.getName());
				px
						.sendMessage(BCRandomizer.Prefix
								+ ChatColor.YELLOW
								+ " use /joingame to participate before the game starts.");
			}

			else {

				px.sendMessage(BCRandomizer.Prefix + ChatColor.YELLOW
						+ " A Survival game is being prepared in "
						+ playingWorld.getName());
				px
						.sendMessage(BCRandomizer.Prefix
								+ ChatColor.YELLOW
								+ " use /joingame to participate before the game starts.");
			}
			if(delaystart >0){
				
				px.sendMessage(BCRandomizer.Prefix + "ChatColor.YELLOW" + "Game will start in " + delaystart + " seconds.");
				
				
			}
		}
		if(delaystart > 0){
			Bukkit.getScheduler().runTaskLater(_Owner, new Runnable() {
				public void run(){
					StartGame(playingWorld,prepatorydelay,MobArenaMode);
					
				}
				
				
			}, delaystart);
			
			
		}

	}

	public boolean isGameActive() {

		return _Owner.ActiveGames.size() > 0;

	}

	private ResumePvP rp = null;
	Location useBorderA = null;
	Location useBorderB = null;
	private void StartGame(Player p,int numseconds,boolean MobArena){
		StartGame(p,null,numseconds,MobArena);
	}
	private void StartGame(World w,int numseconds,boolean MobArena){
		StartGame(null,w,numseconds,MobArena);
	}
	private void StartGame(Player p,World w, int numseconds, boolean MobArena) {
		accepting = false;
		if (joinedplayers.size() == 0) {
			if (p != null)
				p.sendMessage(BCRandomizer.Prefix
						+ "No players participating! Cannot start game.");
			else
				System.out
						.println("No players participating! Cannot start game.");

			accepting = true;
			return;
		}
		if (_Owner.ActiveGames.size() > 0) {
			// this is for debugging. Right now it will only allow one game at a
			// time.
			if (p != null)
				p
						.sendMessage(BCRandomizer.Prefix
								+ ChatColor.YELLOW
								+ "Game is already in progress! use /stopallgames to stop current games.");
			else
				System.out
						.println("Game in progress. use /stopallgames to stop current games.");

			return;
		}

		String ignoreplayer = null;

		final World grabworld = (w==null?(p!=null?p.getWorld():playingWorld):w);

		if (_Owner.Randomcommand.MobArenaMode)
			numseconds = 0;

		rp = new ResumePvP(_Owner, grabworld, numseconds, joinedplayers,
				spectating,uselives,useBorderA,useBorderB);

		GameStartEvent eventobj = new GameStartEvent(joinedplayers, spectating,
				MobArena);
		Bukkit.getServer().getPluginManager().callEvent(eventobj);

		Bukkit.broadcastMessage(BCRandomizer.Prefix + ChatColor.GOLD
				+ "Survival Event " + ChatColor.GREEN + " has begun in world "
				+ ChatColor.DARK_AQUA + grabworld.getName() + "!");
		Bukkit.broadcastMessage(joinedplayers.size() + " Players.");
		grabworld.setPVP(false);
		
		
		
		
		// iterate through all online players.
		for (Player pl : joinedplayers) {

			if (pl.isOnline()) {

				pl
						.sendMessage(BCRandomizer.Prefix
								+ ChatColor.BLUE
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
				pl.setFlying(false);
				pl.playSound(pl.getLocation(), Sound.ENDERMAN_HIT, 1.0f, 1.0f);
				
				for(Player spectator :spectating){
					spectator.hidePlayer(pl);
					pl.showPlayer(spectator);
				}
				
			}

		}
		for(Player spectator : spectating){
			
			//set flying.
			spectator.setFlying(false);
			spectator.setAllowFlight(true);
			
			
			
			
		}
		
		
		ChestRandomizer.resetStorage();
		repopulateChests("", p.getWorld(), true);
		ShufflePlayers(joinedplayers);
		ResumePvP.BroadcastWorld(grabworld, BCRandomizer.Prefix
				+ ChatColor.LIGHT_PURPLE + " Containers randomized.");

		// GameStartEvent eventobj= new
		// GameStartEvent(joinedplayers,spectating,MobArena);
		rp.getTracker().deathwatcher.onGameStart(eventobj);

		if (!_Owner.Randomcommand.getMobArenaMode()) {
			ResumePvP.BroadcastWorld(grabworld, BCRandomizer.Prefix
					+ ChatColor.GREEN + "PvP will be re-enabled in "
					+ ChatColor.RED + numseconds + ChatColor.GREEN
					+ " Seconds! get ready.");
			ResumePvP.BroadcastWorld(grabworld, BCRandomizer.Prefix + 
					ChatColor.GREEN + "Mob spawns will start in " +ChatColor.RED + + MobTimeout + ChatColor.GREEN + " seconds... try to conclude the match before that, they tend to make a mess.");
		}
		if(MobTimeout >0){
		Bukkit.getScheduler().runTaskLater(_Owner, new Runnable() {
			
			public void run(){
				
				ResumePvP.BroadcastWorld(grabworld, ChatColor.DARK_GREEN + "Yummy contestants for Hungry Mobs!");
				enableMobSpawns();
				
			}
			
			
			
		}, MobTimeout*20);
		}
		Thread thr = new Thread(rp);
		thr.start();
	}

private void ShufflePlayers(List<Player> shufflethese){
		
		//shuffle all players that are "StillAlive" within the arena border.
		//if no border is set, we don't shuffle, and log to the console.
	//_Owner.Randomcommand;
	GameTracker usetracker = null;
	if(shufflethese.size()>0){
		usetracker = _Owner.getGame(shufflethese.get(0));
	}
	
	for(Player p:shufflethese){
		
		GameTracker.deathwatcher.handleGameSpawn(p);
		
		
	}
	
	/*
		if(usetracker.getBorderA()!=null && usetracker.getBorderB()!=null){
			Location ba = usetracker.getBorderA();
			Location bb = usetracker.getBorderB();
			
			double XMinimum = Math.min(ba.getX(), bb.getX());
			double XMaximum = Math.max(ba.getX(), bb.getX());
			double ZMinimum = Math.min(ba.getZ(), bb.getZ());
			double ZMaximum = Math.max(ba.getZ(), bb.getZ());

			//iterate through each Player...
			Random rgen = RandomData.rgen;
			for(Player participant:shufflethese){
				
				double chosenY=0;
				double chosenX=0,chosenZ=0;
				while(chosenY==0){
				//choose a random X and Z...
				chosenX = rgen.nextDouble()*(XMaximum-XMinimum)+XMinimum;
				chosenZ = rgen.nextDouble()*(ZMaximum-ZMinimum)+ZMinimum;
				
				
				
				//now, our task: get the highest block at...
				chosenY = (double)participant.getWorld().getHighestBlockYAt((int)chosenX, (int)chosenZ);
				}
				Location chosenlocation = new Location(participant.getWorld(),chosenX,chosenY,chosenZ);
				participant.teleport(chosenlocation);
				
				System.out.println("Teleported " + participant.getName() + " to " + chosenlocation.toString());
				
				
				
				
				
				
			}
			
			
			
			
		}
		
		
		*/
	}
	
	
	public void repopulateChests(String Source, World w) {

		repopulateChests(Source, w, false);
	}

	public boolean hasBlockBeneath(Block testblock, Material testmaterial) {

		Location spotbelow = new Location(testblock.getWorld(), testblock
				.getX(), testblock.getY() - 1, testblock.getZ());
		return testblock.getWorld().getBlockAt(spotbelow).getType().equals(
				testmaterial);

	}

	public void repopulateChests(final String Source, final World w,
			final boolean silent) {

		int populatedamount = 0;
		LinkedList<Chest> allchests = new LinkedList<Chest>();
		LinkedList<Furnace> allfurnaces = new LinkedList<Furnace>();
		LinkedList<Dispenser> alldispensers = new LinkedList<Dispenser>();
		LinkedList<StorageMinecart> allstoragecarts = new LinkedList<StorageMinecart>();
		LinkedList<BrewingStand> allbrewingstands = new LinkedList<BrewingStand>();
		World gotworld = w;
		if (!silent)
			Bukkit.broadcastMessage(BCRandomizer.Prefix
					+ "BASeCamp Chest Randomizer- Running...");
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
				if(iteratestate instanceof StorageMinecart){
					StorageMinecart casted = (StorageMinecart)iteratestate;
					allstoragecarts.add(casted);
					ChestRandomizer cr = new ChestRandomizer(_Owner,casted.getInventory(),sourcefile);
					populatedamount+=cr.Shuffle();
					
					
				}
				else if(iteratestate instanceof BrewingStand){
					BrewingStand casted = (BrewingStand)iteratestate;
					ChestRandomizer br = new ChestRandomizer(_Owner,casted,sourcefile);
					allbrewingstands.add(casted);
					populatedamount+=br.Shuffle();
					
					
				}
				
				if (iteratestate instanceof Chest) {
					Chest casted = (Chest) iteratestate;
					// randomize!
					if (!hasBlockBeneath(iteratestate.getBlock(), Material.WOOL)) {
						allchests.add(casted);
						ChestRandomizer cr = new ChestRandomizer(_Owner,
								casted, sourcefile);
						populatedamount += cr.Shuffle();
					} else {
						//System.out.println("Storing inventory for a chest");
						ChestRandomizer.StoreInventory(casted);

					}
				} else if (iteratestate instanceof Furnace) {
					Furnace casted = (Furnace) iteratestate;
					allfurnaces.add(casted);
					ChestRandomizer cr = new ChestRandomizer(_Owner, casted
							.getInventory(), sourcefile);
					populatedamount += cr.Shuffle();
					casted.getInventory().setResult(null);
				} else if (iteratestate instanceof Dispenser) {
					Dispenser casted = (Dispenser) iteratestate;
					alldispensers.add(casted);
					ChestRandomizer cr = new ChestRandomizer(_Owner, casted
							.getInventory(), sourcefile);
					populatedamount += cr.Shuffle();
					System.out.println(populatedamount);
				}

			}

		}

		// turn chest LinkedList into an array.
		Chest[] chestchoose = new Chest[allchests.size()];
		allchests.toArray(chestchoose);

		int StaticAdded = 0;
		if (!silent) {
			ResumePvP.BroadcastWorld(w,ChatColor.AQUA.toString()
					+ allchests.size() + ChatColor.YELLOW
					+ " Chests Populated.");
			Bukkit.broadcastMessage(ChatColor.AQUA.toString()
					+ allfurnaces.size() + ChatColor.RED + " Furnaces "
					+ ChatColor.YELLOW + "Populated.");

			ResumePvP.BroadcastWorld(w,ChatColor.AQUA + "" + alldispensers.size()
					+ ChatColor.GREEN + " Dispensers " + ChatColor.YELLOW
					+ " Populated.");

			
			ResumePvP.BroadcastWorld(w,ChatColor.DARK_RED + "Populated "
					+ ChatColor.AQUA.toString() +
					ChatColor.LIGHT_PURPLE + allbrewingstands.size() + ChatColor.RED + " Brewing Stands" +
					ChatColor.YELLOW + " Populated.");
					
					
			
					ResumePvP.BroadcastWorld(w,ChatColor.YELLOW + "Populated "
					+ ChatColor.AQUA.toString() + populatedamount
					+ ChatColor.YELLOW + " slots.");
			
			
			
		}
		for (RandomData iterate : ChestRandomizer.getRandomData(_Owner)) {

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
		if (!silent)
			Bukkit.broadcastMessage(ChatColor.YELLOW + "Added "
					+ ChatColor.AQUA.toString() + StaticAdded
					+ ChatColor.YELLOW + " Static items.");
	}

	public boolean getMobArenaMode() {
		// TODO Auto-generated method stub
		return MobArenaMode;
	}
}