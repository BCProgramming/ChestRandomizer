package com.BASeCamp.SurvivalChests;

import java.security.acl.Owner;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.logging.Level;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import com.BASeCamp.SurvivalChests.Events.GameEndEvent;

public class GameTracker implements Runnable {

	private final class ChestPopulator implements Runnable {
		private final InventoryCloseEvent closured;
		private final InventoryHolder Index;
		private ChestPopulator(InventoryCloseEvent closured,InventoryHolder pIndex) {
			this.closured = closured;
			this.Index=pIndex;
		}

		//repopulate the inventory.	
		public void run()
		{
			InventoryHolder grabh = this.Index;
			if(grabh instanceof Player) return;
			if(closured.getPlayer() instanceof Player)
			{
			Player p = (Player)closured.getPlayer();
			//p.sendMessage("repopulating chest you opened...");
			}
			ChestRandomizer rr = new ChestRandomizer(_Owner,closured.getInventory(),"");
			InventoryHolder ih = grabh;
			BlockState[] bsa=null;
			if(ih instanceof DoubleChest)
			{
			DoubleChest dc = (DoubleChest)ih;
			bsa = new BlockState[]{(BlockState) dc.getRightSide(),(BlockState) dc.getLeftSide()};
			
			}
			else if(ih instanceof Chest ||
					ih instanceof Dispenser )
			{
				bsa = new BlockState[]{(BlockState)ih};
			}
				
			if(bsa==null) return;
			for(BlockState bs:bsa){	
			
			
			if(bs!=null && !_Owner.Randomcommand.hasBlockBeneath(bs.getBlock(),Material.WOOL))
			{
				Location sl = bs.getLocation();
				//special condition:
				//we do NOT repopulate if the last player that accessed us
				//is closer than the given distance.
				System.out.println("Delayed fire, lastinventory:" + lastInventoryAccess.size());
				InventoryAccessInfo iai = lastInventoryAccess.get((InventoryHolder)bs);
				if(iai==null) System.out.println("Alert:iai is null");
				Player closest = iai.getViewed().getLast();
				
				//Player closest = BCRandomizer.getNearestPlayer(bs.getLocation());
				//if distance is less than 16, we want to reschedule...
				int sizelimit = _Owner.Configuration.getRepopPreventionRadius();
				if(bs.getLocation().distance(closest.getLocation()) < sizelimit)
				{
				Bukkit.getScheduler().scheduleSyncDelayedTask(_Owner, new ChestPopulator(closured,(InventoryHolder)bs), _ChestTimeout);
				spawnrepopulationparticles(sl,true);
				return;
				}
				
				rr.setMaxItems(closured.getInventory().getSize());
				rr.Shuffle();
				//get the block location and spawn some particles, too.
				
				
				spawnrepopulationparticles(sl,false);
				
				
				
				
			}
			
			}
			
			
			/*
			 * if (!hasBlockBeneath(iteratestate.getBlock(), Material.WOOL)) {
				allchests.add(casted);
				ChestRandomizer cr = new ChestRandomizer(_Owner,
						casted, sourcefile);
				populatedamount += cr.Shuffle();
			}*/
			
		}
	}
	private Location BorderA,BorderB;
	private World runningWorld = null;
	private LinkedList<Player> StillAlive = new LinkedList<Player>();
	private TeamManager GameTeams = new TeamManager();
	private List<Player> _Spectators = null;
	private LinkedList<Player> _deadPlayers = null;
	private boolean _Accepting = false;
	private PopulatedInventoryData _PopulatedData = null;
	
	public PopulatedInventoryData getPopulatedData() { return _PopulatedData;}
	public void setPopulatedData(PopulatedInventoryData value){_PopulatedData=value;}
	
	public LinkedList<Player> getStillAlive() {
		return StillAlive;
	}
	private HashMap<String,Integer> PlayerLives = new HashMap<String,Integer>(); 
	//^hashmap mapping player names to their current lives. Defaults to each player having 2 lives.
	private HashMap<Integer, String> FinishPositions = new HashMap<Integer, String>();
	BCRandomizer _Owner = null;
	public static CoreEventHandler deathwatcher = null;
	private int _ChestTimeout = 45*20; //45 seconds. gameticks...
	private boolean _MobArenaMode = false; // MobArena Mode is when PvP is
											// disabled completely.
	
	public Location getBorderA(){return BorderA;}
	public void setBorderA(Location value){BorderA=value;}
	
	public Location getBorderB(){return BorderB;}
	public void setBorderB(Location value){BorderB=value;}
	
	
	private boolean AllowHealthRegen = true; 
	
	public boolean getAllowHealthRegen() {
		// TODO Auto-generated method stub
		return AllowHealthRegen;
	}
	public void setAllowHealthRegen(boolean value){
		AllowHealthRegen = value;
	}
	
	public World getWorld() { return runningWorld;}
	private HashMap<String, Integer> ScoreTally = new HashMap<String, Integer>();
	private ScoreTally PvPScores = null;
	// Note that gameTracker also tracks Mob Arena style games.
	// Mob Arena is similar in principle to the Mob Arena Plugin, but is more an
	// extension
	// of the Player Versus Player aspects of this plugin to extend to a
	// different style.

	public ScoreTally getTally(){ return PvPScores;}
	
	
	public boolean getMobArenaMode() {
		return _MobArenaMode;
	}
	private static final int defaultscoreincrement=1000;
	private HashMap<Player,Integer> nextScoreLevel = new HashMap<Player,Integer>();
	
		
	private HashMap<InventoryHolder,InventoryAccessInfo> lastInventoryAccess = new HashMap<InventoryHolder,InventoryAccessInfo>();
	
	//called to cancel all pending repopulations.
	public void CancelRepopulations()
	{
		for(InventoryAccessInfo iai:lastInventoryAccess.values())
		{
			Bukkit.getScheduler().cancelTask(iai.getBukkitTask());
			
			
		}
		
	}
	
		
	
	
	public void onInventoryOpen(InventoryOpenEvent event)
	{
		
		System.out.println("GameTracker:onInventoryOpen");
		//if an item exists, cancel the task and set it to 0.
		//when the inventory is closed the task will be restarted.
		InventoryAccessInfo iai =null;
		InventoryHolder ih = event.getInventory().getHolder();
		if(ih instanceof Player) { return;}
		if(ih instanceof BrewingStand || ih instanceof Furnace) return;
		InventoryHolder[] useholders = new InventoryHolder[]{event.getInventory().getHolder()};
		//we need to check for chests, and convert to an array and iterate on it.
		if(ih instanceof DoubleChest){
			//System.out.println("it's a doublechest...");
			DoubleChest dc = (DoubleChest)ih;
			useholders = new InventoryHolder[] {	
					dc.getLeftSide(),
					dc.getRightSide()
			};
			
		}
		for(InventoryHolder useholder:useholders){
		if(lastInventoryAccess.containsKey(useholder)){
		iai = lastInventoryAccess.get(useholder);
		}
		else
		{
			iai = new InventoryAccessInfo();
			iai.setInventory(event.getInventory());
			lastInventoryAccess.put(useholder, iai);
		}
		if(iai.getBukkitTask()!=0)
		{
			//System.out.println("Cancelling task:" + iai.getBukkitTask());
			Bukkit.getScheduler().cancelTask(iai.getBukkitTask());
			iai.setBukkitTask(0);
			
		}
		
		//either way, register a view.
		//tell them who looked at it last, too.
		if(event.getPlayer() instanceof Player)
		{
			if(iai.cachedPlayers.size() >0)
			{
				Player lastviewer = iai.cachedPlayers.getLast();
				((Player)event.getPlayer()).sendMessage(BCRandomizer.Prefix + " Last viewed by:" + lastviewer.getDisplayName());
				
				
			}
			
			
			iai.PlayerView((Player)(event.getPlayer()));
		}
		}
		
	}
	private void spawnrepopulationparticles(Location sl,boolean showfailed)
	{
		if(showfailed) return; //change: do nothing for failed respawn.
		Sound usesound = showfailed?Sound.ANVIL_BREAK:Sound.ENDERMAN_TELEPORT;
		
		sl.getWorld().playSound(sl, Sound.ENDERMAN_TELEPORT, 10,1);
		for(int i=0;i<10;i++)
		{
			//choose random location within the block.
			double offsetX = RandomData.rgen.nextDouble();
			double offsetY = RandomData.rgen.nextDouble();
			double offsetZ = RandomData.rgen.nextDouble();
			int randomdir = RandomData.rgen.nextInt(9);
			int useradius = 128;
			Location effectlocation = new Location(sl.getWorld(), sl.getX()+offsetX,sl.getY()+offsetY,sl.getZ()+offsetZ);
			Effect succeedeffect = RandomData.rgen.nextBoolean()?Effect.SMOKE:Effect.MOBSPAWNER_FLAMES;
			Effect faileffect = RandomData.rgen.nextBoolean()?Effect.POTION_BREAK:Effect.BOW_FIRE;
			Effect useeffect = showfailed?faileffect:succeedeffect; 
			sl.getWorld().playEffect(effectlocation,useeffect,randomdir,useradius);
			
			
			
			
			
		}
		
		
	}
	
	public void onInventoryClose(InventoryCloseEvent event)
	{
		//System.out.println("GameTracker:onInventoryClose");
		//when an inventory is closed:
		final InventoryCloseEvent closured = event;
		InventoryHolder[] ihs;
		//doublechests are stored as both sides of the chest. Both will be
		//repopulated at the same time and are essentially viewed simultaneously.
		//we cannot use a DoubleChest instance, because the other code teases it into separate
		//Chest instances, which won't be in the HashMap.
		if(closured.getInventory().getHolder() instanceof DoubleChest)
		{
		DoubleChest dc = (DoubleChest)closured.getInventory().getHolder();
		
		ihs = new InventoryHolder[]{dc.getRightSide(),dc.getLeftSide()};
		
		}
		else {
		ihs = new InventoryHolder[]{event.getInventory().getHolder()};
		}
		for(InventoryHolder ih:ihs){
		
		if(ih instanceof Player) { return;}
		if(ih instanceof BrewingStand || ih instanceof Furnace) return;
		//if the interval amount is 0, assume we don't repopulate.
		if(_ChestTimeout==0) return;
		//System.out.println("ChestTimeout=" + _ChestTimeout);
		InventoryAccessInfo iai = null;
		if(!lastInventoryAccess.containsKey(ih))
		{
		iai = new InventoryAccessInfo();
		iai.setInventory(event.getInventory());
		lastInventoryAccess.put(ih,iai);	
		//System.out.println("added ItemHolder...");
		}
		else
		{
			iai=lastInventoryAccess.get(ih);
			//System.out.println("retrieved ItemHolder...");
		}
		
			if(iai.getBukkitTask()!=0)
			{
				//cancel.
				//System.out.println("Cancelling existing task ID=" + iai.getBukkitTask());
				Bukkit.getScheduler().cancelTask(iai.getBukkitTask());
				
			}
			Bukkit.getLogger().log(Level.INFO, "scheduling inventory repopulation for " + _ChestTimeout);
			//send a message to the player.
			iai.PlayerView((Player)event.getPlayer());
			//schedule new task.
			iai.setBukkitTask(Bukkit.getScheduler().scheduleSyncDelayedTask(_Owner, new ChestPopulator(closured,ih), _ChestTimeout));
			
			
		
		
		}
		
		
		
	}
	
	
	public int getNextLevel(Player p) {
		
		if(nextScoreLevel.containsKey(p)){
			return nextScoreLevel.get(p).intValue();
		}
		else{
			
			nextScoreLevel.put(p, defaultscoreincrement);
			return defaultscoreincrement;
			
		}
		
		
	}
	public void setNextLevel(Player p,int Value){
		nextScoreLevel.put(p, Value);
		
		
		
	}
	
	public HashMap<String, Integer> getScoreTally() {
		return ScoreTally;
	}
	public void AddTeam(GameTeam gt){
		
	}
	public void AddParticipant(Player p){
		//task: we need to add this player to the Active Game.
		//first, we need to plonk them into all the structures.
		
		//-Add the StillAlive list
		StillAlive.add(p);
		//-hide spectators from them
		//show them to spectators
		for(Player s:this.getSpectating()){
			p.hidePlayer(s);
			s.showPlayer(p);
		}
		
		p.teleport(deathwatcher.handleGameSpawn(p));
		
		
		//things we don't do here are randomize their location or give them items. We just deal with making
		//sure the player is considered a participant.
		
		
		
		
	}
	public Location chooseSpotinBorder(){
		
		
		if(BorderA==null||BorderB==null) return null;
		
		Location ba = getBorderA();
		Location bb = getBorderB();
		
		double XMinimum = Math.min(ba.getX(), bb.getX());
		double XMaximum = Math.max(ba.getX(), bb.getX());
		double ZMinimum = Math.min(ba.getZ(), bb.getZ());
		double ZMaximum = Math.max(ba.getZ(), bb.getZ());
		
		
		Random rgen = RandomData.rgen;
		double chosenY=0;
		double chosenX=0,chosenZ=0;
		while(chosenY==0){
		//choose a random X and Z...
		chosenX = rgen.nextDouble()*(XMaximum-XMinimum)+XMinimum;
		chosenZ = rgen.nextDouble()*(ZMaximum-ZMinimum)+ZMinimum;
		//now, our task: get the highest block at...
		//chosenY = (double)ba.getWorld().getHighestBlockYAt((int)chosenX, (int)chosenZ);
		chosenY = BCRandomizer.getHighestBlockYAt(ba.getWorld(),(int)chosenX,(int)chosenZ);
		}
		Location chosenlocation = new Location(ba.getWorld(),chosenX,chosenY,chosenZ);
		//participant.teleport(chosenlocation);
		
		//System.out.println("Teleported " + participant.getName() + " to " + chosenlocation.toString());
		return chosenlocation;
		
		
		
	}
	private Scoreboard Scoreboard=null;
	
	public Scoreboard getScoreboard(){return Scoreboard;}
	private int InitialPlayerLives = 0;
	public int getInitialPlayerLives() { return InitialPlayerLives;}
	
	public GameTracker(BCRandomizer Owner, World applicableWorld,
			List<Player> Participants, List<Player> spectators, boolean MobArena,int pPlayerLives, Location pBorderA, Location pBorderB, Scoreboard ss) {
		// initialize StillAlive List.
		Scoreboard = ss;
		InitialPlayerLives = pPlayerLives;
		BorderA=pBorderA;
		BorderB=pBorderB;
		if(!MobArena) PvPScores = new ScoreTally(this,Participants);
		_ChestTimeout = Owner.Randomcommand.getChestTimeout();
		
		//initialize Lives.
		
		PlayerLives = new  HashMap<String,Integer>();
		for(Player p:Participants)
		{
			//add lives to the hashmap.
			PlayerLives.put(p.getName(), new Integer(pPlayerLives));
			
		}
		
		
		
		if (deathwatcher == null) {
			deathwatcher = new CoreEventHandler(Owner, this, applicableWorld);
			Owner.getServer().getPluginManager().registerEvents(deathwatcher,
					Owner);
		} else
			deathwatcher.getTrackers().add(this);

		_MobArenaMode = MobArena;
		_Owner = Owner;
		_Owner.ActiveGames.add(this);
		_Spectators = spectators;
		ScoreTally = new HashMap<String, Integer>();
		for (Player p : Participants) {
			StillAlive.add(p);
			ScoreTally.put(p.getName(), 0);

		}
		_deadPlayers = new LinkedList<Player>();
		// raise custom event.
		if (!_MobArenaMode)
			Bukkit.broadcastMessage(BCRandomizer.Prefix
					+ "Survival game started! " + StillAlive.size()
					+ " participants.");
		else {

			Bukkit.broadcastMessage(BCRandomizer.Prefix
					+ "Mob Arena game started! " + StillAlive.size()
					+ " participants.");
		}
		String[] participantNames = new String[StillAlive.size()];

		int i = 0;
		for (Player Alive : StillAlive) {

			participantNames[i] = Alive.getDisplayName();
			i++;
		}
		i = 0;

		Bukkit.broadcastMessage(BCRandomizer.Prefix + ChatColor.RED
				+ "participating:" + StringUtil.Join(participantNames, ","));

		// now, we set everything up: vanish all the spectators.

		for (Player spectator : _Spectators) {
			for (Player joined : Participants) {

				joined.hidePlayer(spectator);

			}
		}

	}

	private void addprize(Player deathplayer) {

		for (int i = 0; i < 64; i++) {
			// add prize gold.
			ItemStack prizegold = new ItemStack(Material.GOLD_NUGGET);

			// prizegold = ItemNamer.renameItem(prizegold,"Mulreay Gold",
			// "Awarded t)
			// ItemNamer.setName("Mulreay Gold");
			// prizegold = ItemNamer.getItemStack();
			deathplayer.getWorld().dropItemNaturally(deathplayer.getLocation(),
					prizegold);

		}

	}
	private boolean GameConcluding = false;
	
	public void setGameConcluding(boolean value){GameConcluding = value;}
	public boolean getGameConcluding() { return GameConcluding;}
	private int currentTopScore = 0;
	public void CheckTopScores(Player p){
		int grabscore = ScoreTally.get(p.getName());
		if(grabscore > currentTopScore){
			String usemessage = BCRandomizer.Prefix + p.getDisplayName() + " has the score to beat-" + String.valueOf(grabscore) + ">" + String.valueOf(currentTopScore);
			for(Player iterate:runningWorld.getPlayers()){
				
				
				iterate.sendMessage(usemessage);
				
				
				
			}
			
			
		}
	}
	
	// when a player dies, we need to update the list, and possibly even break
	// out if the game has ended as a result.
	public void PlayerDeath(Player deadPlayer, Player assailant) {
		// remove player from list.
		//added for 1.5 release: lives.
		//check the number of lives the deadPlayer has left.
		
		//tally the score.
		if(this.InitialPlayerLives==Integer.MAX_VALUE || this.InitialPlayerLives==0){
			//if set to continuous, clear score when players die.
			CheckTopScores(deadPlayer);
			ScoreTally.put(deadPlayer.getName(), 0);
		}
		else {
 
			
		}
		
		
		//if livesremaining is less than int.MAXVALUE...
		if(!GameConcluding) {
			//only check lives if the game is <not> concluding. if it is concluding, all players
			//are basically set to have one life left.
			int livesremaining = PlayerLives.get(deadPlayer.getName()).intValue();
			//if livesremaining is not int.MAXVALUE...
			if(livesremaining == Integer.MAX_VALUE || livesremaining-->0){ //if it's set to MAX_VALUE, it's basically infinite lives.
				
				//possibly got too fancy...
				//if livesremaining is MAX_VALUE, the second part of the or is ignored.
				//if it isn't, then it will be decremented and this code will execute if it's
				//larger than 0.
				//reapply the new value of livesremaining to the player,
				//and then also tell them.
				PlayerLives.put(deadPlayer.getName(), livesremaining);
				deadPlayer.sendMessage(BCRandomizer.Prefix + " You have " + ChatColor.RED + String.valueOf(livesremaining) + ChatColor.YELLOW + " Lives left.");
				//we don't actually need to do anything here, as far as I'm currently aware,
				//because the respawn event handles moving the player and all that guff.
				//we need to return though, in order to prevent the code below from stopping the game.
				
				return;
				
			}

			
		
			
		}
		
		
		deadPlayer.teleport(deadPlayer.getWorld().getSpawnLocation());
		if (gamecomplete)
			return;
		if (!StillAlive.contains(deadPlayer)) {
			return;

		}
		StillAlive.remove(deadPlayer);
		_deadPlayers.add(deadPlayer);
		_Spectators.add(deadPlayer);
		Integer theposition = StillAlive.size() + 1;
		Bukkit.broadcastMessage(deadPlayer.getDisplayName() + " is "
				+ ChatColor.RED + "OUT!" + ChatColor.YELLOW + " (Place:"
				+ theposition + ")");
		
	
		
		
		
		
		Player WinningPlayer = deadPlayer;
		if (_MobArenaMode) {

			Stack<KeyValuePair<Integer, String>> sortedScores = SortScoreTally();
			// FinishPositions = new HashMap<Integer,Player>();
			int currpos = 1;
			while (!sortedScores.isEmpty()) {
				KeyValuePair<Integer, String> popped = sortedScores.pop();
				FinishPositions.put(currpos, popped.getValue());

				currpos++;

			}

		} else {
			BCRandomizer.Victories.madePlace(deadPlayer, theposition);
			if (deathwatcher == null)
				return;
			FinishPositions.put(theposition, deadPlayer.getName());
		}

		synchronized (StillAlive) { // synch on StillAlive List.
			if (StillAlive.size() == 0) {
				CancelRepopulations();
				// Bukkit.broadcastMessage("All players participating died.");
				if (_MobArenaMode) {

					broadcastresults();
					// Bukkit.broadcastMessage(ChatColor.AQUA +
					// "This games winner is " + ChatColor.GREEN +
					// FinishPositions.keySet()

				}
				// addprize(deadPlayer);
				gamecomplete = true;
				GameEndEvent eventobj = new GameEndEvent(deadPlayer,
						FinishPositions, this);
				Bukkit.getServer().getPluginManager().callEvent(eventobj);
				deathwatcher.onGameEnd(eventobj);
				deathwatcher._Trackers.remove(this);
				return;
			}
			if (StillAlive.size() == 1 && !_MobArenaMode) {
				// notify the 'game' is finished, so the run loop can terminate.
				// announce the winner!
				Player winner = StillAlive.getFirst();
				Bukkit.broadcastMessage(winner.getName()
						+ " has WON the event!");
				// raise custom event.

				GameEndEvent eventobj = new GameEndEvent(winner,
						FinishPositions, this);
				Bukkit.getServer().getPluginManager().callEvent(eventobj);
				deathwatcher.onGameEnd(eventobj);
				gamecomplete = true;
				// dead player explodes inexplicably.

				//addprize(deadPlayer);
				gamecomplete = true;
				deathwatcher._Trackers.remove(this);
				// move players back to their original spots where desired.
				for (Player pp : _Owner.Randomcommand.returninfo.keySet()) {

					pp
							.sendMessage(BCRandomizer.Prefix
									+ ChatColor.BLUE
									+ "You will be returned to your before-game position in 5 seconds.");

				}
				_Owner.getServer().getScheduler().scheduleSyncDelayedTask(
						_Owner, new Runnable() {

							public void run() {
								for (Player pp : _Owner.Randomcommand.returninfo
										.keySet()) {
									_Owner.Randomcommand.returninfo.get(pp)
											.sendBack();

								}
							}
						}, 5 * 20L);

			} else {

				Bukkit.broadcastMessage(StillAlive.size() + " Players remain!");

				StringBuffer buildnamelist = new StringBuffer();

				for (Player givecompass : StillAlive) {
					buildnamelist.append(givecompass.getDisplayName() + ",");

				}

				Bukkit.broadcastMessage("Remaining:"
						+ buildnamelist
								.substring(0, buildnamelist.length() - 1));

				if (StillAlive.size() < 4 && !_MobArenaMode && !gavecompasses) {
					gavecompasses = true;
					for (Player givecompass : StillAlive) {
						ItemStack CompassItem = new ItemStack(Material.COMPASS);
						
						CompassItem = ItemNamer.renameItem(CompassItem,
								"BASeCamp" + ChatColor.ITALIC + "(r)"
										+ ChatColor.RESET + " Player Finder");

						givecompass.getInventory().addItem(CompassItem);
						givecompass
								.sendMessage(BCRandomizer.Prefix
										+ ChatColor.GREEN
										+ "You have been given a BASeCamp Player Finder!");
						givecompass.sendMessage(BCRandomizer.Prefix
								+ ChatColor.GREEN
								+ "it points towards the closest participant.");

					}

				}
			}
		}

	}

	
	
	
	private Stack<KeyValuePair<Integer, String>> SortScoreTally() {
		SortedSet<KeyValuePair<Integer, String>> sortset = new TreeSet<KeyValuePair<Integer, String>>();

		// iterate through ScoreTally.
		// iterate through the keys.
		for (String pkey : ScoreTally.keySet()) {

			sortset.add(new KeyValuePair<Integer, String>(ScoreTally.get(pkey),
					pkey));

		}
		// default iterator will go in ascending order.
		// create a stack, iterate, and then pop from the stack until it is
		// empty. This
		// should go through the same iterator backwards.
		Stack<KeyValuePair<Integer, String>> reversing = new Stack<KeyValuePair<Integer, String>>();
		for (KeyValuePair<Integer, String> setitem : sortset) {

			reversing.push(setitem);

		}
		return reversing;
	}

	private void broadcastresults() {
		// TODO Auto-generated method stub

		// broadcast the game points!

		// we need to sort it by the score.
		Stack<KeyValuePair<Integer, String>> reversing = SortScoreTally();
		int Position = 1;
		Bukkit.broadcastMessage(BCRandomizer.Prefix + "--Results--");
		while (!reversing.isEmpty()) {
			// FINALLY we can output the results.
			KeyValuePair<Integer, String> element = reversing.pop();
			Bukkit.broadcastMessage(Position + ":"
					+ Bukkit.getPlayer(element.getValue()).getDisplayName() + ", Score of "
					+ element.getKey().toString());

		}

	}

	private int delayspawnnearby = 0;
	private int BossSpawnDelay = 0;
	public boolean gamecomplete = false;
	private boolean gavecompasses = false;
	//private int ChestTimeout = 0; //number of seconds to delay.
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		gavecompasses=false;
		int delayresetmidnight = 0;
		
		
		
		while (!gamecomplete) {

			try {
				synchronized (StillAlive) {

					if (StillAlive.size() <= 3 && !gavecompasses
							&& !_Owner.Randomcommand.getMobArenaMode()) {
						gavecompasses = true;
						for (Player givecompass : StillAlive) {
							ItemStack CompassItem = new ItemStack(
									Material.COMPASS);

							CompassItem = ItemNamer.renameItem(CompassItem,
									"BASeCamp" + ChatColor.ITALIC + "(r)"
											+ ChatColor.RESET
											+ " Player Finder");

							givecompass.getInventory().addItem(CompassItem);
							givecompass
									.sendMessage(BCRandomizer.Prefix
											+ ChatColor.GREEN
											+ "You have been given a BASeCamp Player Finder!");
							givecompass
									.sendMessage(BCRandomizer.Prefix
											+ ChatColor.GREEN
											+ "it points towards the closest participant.");

						}

					}

					for (Player pa : StillAlive) {

						// if in MobArena Mode, force a mob to spawn near the
						// player!
						delayspawnnearby++;
						if (delayspawnnearby == 60) {
							BossSpawnDelay++;
							delayspawnnearby = 0;
							if (_Owner.Randomcommand.getMobArenaMode()) {
								
									for (int i = 0; i < 15; i++)
										ForceNearbySpawn(pa);
								
							}
						}

						// find the nearest player.
						double MinDistance = Float.MAX_VALUE;
						Player currentmin = null;
						for (Player pb : StillAlive) {
							if (pa != pb) {

								double getdist = pa.getLocation().distance(
										pb.getLocation());

								if (getdist < MinDistance) {
									currentmin = pb;
									MinDistance = getdist;

								}
							}

						}
						// currentmin is closest player to pa. if it's null,
						// then... well, do nothing, I guess.
						if (currentmin != null) {
							pa.setCompassTarget(currentmin.getLocation());

						}

					}

				}

				if (gamecomplete)
					break;

				Thread.sleep(500); // sleep, so as to prevent being CPU
									// intensive.
				// compasses don't need to be super accurate anyway.
				
			} catch (InterruptedException ex) {

			}

		}

		// make spectators unable to fly, since the game is over.
		for (final Player Spectator : _Spectators) {
			Bukkit.getScheduler().runTask(_Owner, new Runnable(){
				
			public void run(){
				Location Spectatorlocation = Spectator.getLocation();
			
			int Yuse = Spectator.getWorld().getHighestBlockYAt(
					Spectator.getLocation());
			Location setLocation = new Location(Spectator.getWorld(),
					Spectatorlocation.getX(), Yuse, Spectatorlocation.getZ());
			// teleport them to a safe place before we make them no longer fly.
			Spectator.teleport(setLocation);
			Spectator.setAllowFlight(false);
			Spectator.setFlying(false);
			BCRandomizer.unvanishPlayer(Spectator);
			}
			});

		}
		// also, disable PvP again.
		if (runningWorld != null) {
			runningWorld.setPVP(false);
		}
		
		Bukkit.broadcastMessage(ChatColor.YELLOW
				+ "Game Over. PvP disabled.");

		if (_Owner != null && _Owner.ActiveGames != null) {
			_Owner.ActiveGames.remove(this);
		}

		// deathwatcher._Tracker=null;
		// TODO: fix nullpointer exception.
		if (deathwatcher != null && deathwatcher._Trackers != null) {
			deathwatcher._Trackers.remove(this);
		}

	}
	
	
	
	
	
	
	private void ForceNearbySpawn(Player pa) {
		// TODO Auto-generated method stub
		Location CenterPoint = pa.getLocation();
		// Random radius away.
		if(false){
		float useRadius = RandomData.rgen.nextFloat() * 8 + 5;

		// random angle...
		float Angle = (float) (RandomData.rgen.nextDouble() * Math.PI * 2);

		float useX = (float) Math.cos(Angle) * useRadius;
		float useZ = (float) Math.sin(Angle) * useRadius;
		float useY = (float) CenterPoint.getY();
		Location uselocation = new Location(pa.getWorld(), useX, useY, useZ);
		useY = pa.getWorld().getHighestBlockYAt(uselocation);
		// recreate it. The effect here is that over time, a player hiding in a
		// building will find the area just outside it teeming with mobs :P
		uselocation = new Location(pa.getWorld(), useX, useY, useZ);

		// now, we spawn a mob.

		EntityType[] choosetype = new EntityType[] { EntityType.CREEPER,
				EntityType.ZOMBIE, EntityType.SKELETON, EntityType.PIG_ZOMBIE,EntityType.BLAZE,EntityType.WITCH };
		EntityType selected = RandomData.Choose(choosetype);
		// spawn this feller...
		
	//	LivingEntity result = pa.getWorld()
//				.spawnCreature(uselocation, selected); // the hook we already
														// have will randomize
														// them...
		
		LivingEntity result = (LivingEntity) pa.getWorld().spawnEntity(uselocation,selected);
		

		System.out.println("Spawned a " + selected.getName() + " near "
				+ pa.getName());
		}

	}

	// tracks game state, updating the list of still active players when players
	// are killed (GameTracker is notified through PlayerDeathWatcher)
	public List<Player> getSpectating() {
		// TODO Auto-generated method stub
		return this._Spectators;
	}

	public List<Player> getDead() {
		// TODO Auto-generated method stub
		return _deadPlayers;
	}

	public void TimeoutExpired() {

		
		if (runningWorld == null)
			runningWorld = StillAlive.getFirst().getWorld();
		if (!_MobArenaMode) {

			
			
			
			Bukkit.broadcastMessage(ChatColor.RED + "PvP Enabled in World + "
					+ runningWorld.getName());
			
			for (Player iterate : StillAlive) {
				int numactive = 0;
				runningWorld.setPVP(true);

				if (iterate.isOnline()) {
					numactive++;
					Location currlocation = iterate.getLocation();
					currlocation = new Location(runningWorld, currlocation
							.getX() + 5, currlocation.getY() - 15, currlocation
							.getZ() - 5);
					runningWorld.playEffect(currlocation, Effect.GHAST_SHRIEK,
							10);

					// useWorld.strikeLightning(currlocation);
					
					iterate.addPotionEffect(Potion.getBrewer().createEffect(
							PotionEffectType.BLINDNESS, 500, 1));
					iterate.addPotionEffect(Potion.getBrewer().createEffect(PotionEffectType.HUNGER,32767,1));
					
					if(_ChestTimeout>0)
					{
					iterate.sendMessage(BCRandomizer.Prefix + ChatColor.AQUA + "Chests will repopulate " + String.valueOf((this._ChestTimeout/20)) + " seconds after last use.");
					}
					else
					{
						iterate.sendMessage(BCRandomizer.Prefix + ChatColor.AQUA + "Chests will NOT repopulate for this match.");
					}
					iterate.sendMessage(BCRandomizer.Prefix +  ChatColor.BOLD.toString() + ChatColor.GRAY + "You feel very hungry...");
					iterate.sendMessage(BCRandomizer.Prefix + ChatColor.BOLD.toString()
							+ ChatColor.LIGHT_PURPLE
							+ "You have been temporarily blinded!");
					
					
					
					
				}
			}
			runningWorld.setAnimalSpawnLimit(0);
			runningWorld.setAmbientSpawnLimit(0);
			runningWorld.setGameRuleValue("doMobSpawning", "false");
			runningWorld.setGameRuleValue("mobGriefing", "false");
			
			runningWorld.setDifficulty(Difficulty.NORMAL);
			for(Chunk cl: runningWorld.getLoadedChunks())
			{
				for(Entity le:cl.getEntities()){
					if(!(le instanceof Player)){
					le.remove();
					}
					
				}
				
			}
			
			Bukkit.broadcastMessage(ChatColor.GREEN
					+ "Good luck to all contestants! May luck favour you!");
		} else {
			String[] possiblemessages = new String[] {
					"A Cold chill runs down your spine",
					"It is a good night to " + ChatColor.RED + " die.",
					"The Black wind howls." };
			try {
				runningWorld.setDifficulty(Difficulty.HARD);
			runningWorld.setGameRuleValue("doMobSpawning", "true");
			runningWorld.setTime(18000); // make it night.
			runningWorld.setMonsterSpawnLimit(400); // 80 hostile mobs? That's
														// no fun. Let's crank
														// it up...
			runningWorld.setGameRuleValue("mobGriefing", "false");
			runningWorld.setTicksPerMonsterSpawns(40);
			}
			catch(Exception exx) { }
			String chosenmessage = RandomData.Choose(possiblemessages);
			ResumePvP.BroadcastWorld(runningWorld, BCRandomizer.Prefix
					+ ChatColor.RED + chosenmessage);
			
			for(Chunk cl: runningWorld.getLoadedChunks())
			{
				for(Entity le:cl.getEntities()){
					if(!(le instanceof Player)){
					le.remove();
					}
					
				}
				
			}
			

			// extinguish all flames.
			_Owner.ExtinguishFlames(runningWorld);
			
			for (Player iterate : StillAlive) {
				if(_ChestTimeout>0)
				{
				iterate.sendMessage(BCRandomizer.Prefix + ChatColor.AQUA + "Chests will repopulate " + String.valueOf((this._ChestTimeout/20)) + " seconds after last use.");
				}
				else
				{
					iterate.sendMessage(BCRandomizer.Prefix + ChatColor.AQUA + "Chests will NOT repopulate for this match.");
				}
			//iterate.addPotionEffect(Potion.getBrewer().createEffect(PotionEffectType.HUNGER,32767,1));
			//iterate.addPotionEffect(Potion.getBrewer().createEffect(PotionEffectType.SPEED,600,10));
			//iterate.sendMessage(ChatColor.BOLD.toString() + ChatColor.GRAY + "You feel very hungry. Find some milk!");
			}
			// kill all animals in the world, too. Because, why not.

		}



	}


	public World getRunningWorld() {
		// TODO Auto-generated method stub
		return runningWorld;
	}

}
