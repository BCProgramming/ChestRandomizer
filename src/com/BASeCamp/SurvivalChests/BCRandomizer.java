package com.BASeCamp.SurvivalChests;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.*;

import BASeCamp.Configuration.INIFile;

public class BCRandomizer extends JavaPlugin {
	// new method:

	// preparegame sets the game to accept players.
	// joingame willl be issued by players that wish to play.
	// startgame begins that game.

	// implementation: startgame will use a pre-existing list of players that
	// used /joingame.
	// the list will be cleared by preparegame.

	// PLUGIN todolist: Sometimes games get "stuck.". stopallgames doesn't
	// appear to fix it. investigate this and also make it actually show a
	// message
	// to the issuer (or the entire server) indicating the game has been
	// cancelled.
	// possibly add tpall command to teleport all players or something.
	public static Configurator Configuration = null;
	public static VictoryTracker Victories = null;
	public static final String Prefix = ChatColor.WHITE + "[" + ChatColor.GOLD
			+ "BCSurv" + ChatColor.WHITE + "]";
	public final static String PluginName = "BCRandomizer";
	public static String pluginMainDir = "./plugins/BCRandomizer";
	public static String SchematicsFolder = "./plugins/BCRandomizer/Schematics";
	// TODO: use plugin.yml to retrieve configuration data- more precisely, to
	// retrieve the file to use for the randomization data.
	// Another idea is to allow that to be changed via other commands, possibly
	// referring to files in the plugin folder.
	public static String pluginConfigLocation = pluginMainDir
			+ "/survivalchests.cfg";
	public static String SpawnerConfigLocation = pluginMainDir
			+ "/spawners.cfg";
	public RandomizerCommand Randomcommand = null;
	public LinkedList<GameTracker> ActiveGames = new LinkedList<GameTracker>();

	public GriefPrevention gp = null;
	
	private void getGriefPrevention(){
		try {
		for(Plugin iterate:Bukkit.getPluginManager().getPlugins()){
			if(iterate.getClass().getName().equalsIgnoreCase("GriefPrevention")){
				gp = (GriefPrevention)iterate;
				return;
			}
		}
		}
		catch(Exception exx){
			return;
		}
		
		
		
		
	}
	
	public GameTracker getGame(Player p) {
		// returns true of the player is a participant or spectator of a game.
		// null otherwise.
		GameTracker participant = isParticipant(p);
		if (participant != null)
			return participant;
		GameTracker spectator = isSpectator(p);
		return spectator;

	}
	public GameTracker getGame(World w) {
		//retrieves the GameTracker for the given world. Or, the first one it finds...
		for(GameTracker gt:ActiveGames){
			if(gt.getRunningWorld()==w) return gt;
		}
		
		return null;
		
	}
	public GameTracker isParticipant(Player testplayer) {
		for (GameTracker gt : ActiveGames) {

			if (gt.getStillAlive().contains(testplayer))
				return gt;

		}
		return null;

	}

	public GameTracker isSpectator(Player testplayer) {
		for (GameTracker gt : ActiveGames) {
			if (gt.getSpectating().contains(testplayer))
				return gt;
		}
		return null;
	}

	// public GameTracker _Tracker = null;
	public static void clearPlayerInventory(Player p) {

		p.getInventory().clear();
		p.getEquipment().clear();

	}

	public void ExtinguishFlames(final World exWorld) {

		Bukkit.getScheduler().callSyncMethod(this, new Callable<Integer>() {

			public Integer call() {

				for (Chunk iteratechunk : exWorld.getLoadedChunks()) {

					for (int x = 0; x < 15; x++) {
						for (int y = 0; y < 255; y++)
							for (int z = 0; z < 15; z++) {
								// get block at this position.
								Block getblock = iteratechunk.getBlock(x, y, z);
								if (getblock.getType().equals(Material.FIRE)) {

									// extinguish

									Block blockbelow = iteratechunk.getBlock(x,
											y - 1, z);
									if (!blockbelow.getType().equals(
											Material.NETHERRACK)) {
										getblock.setType(Material.AIR);

									}

								}

							}
					}

				}
				return 0;
			}
		});

	}

	public Scanner acquireStream(String SourceName) {

		try {
			if (SourceName != "") {

				try {
					return new Scanner(new URL(SourceName).openStream());

				} catch (Exception urlexception) {

					// String pathgrab =
					// SourceName.substring(0,SourceName.lastIndexOf(File.separatorChar));
					// System.out.println("pathgrab=" + pathgrab);
					try {
						return new Scanner(new File(SourceName));

					} catch (Exception directexception) {
						//didn't even work directly. Let's look in the PluginFolder.
						String uselocation = BCRandomizer.pluginMainDir;
						if(!uselocation.endsWith("\\")){
							uselocation=uselocation+"\\";
							
						}
						return new Scanner(new File(uselocation + SourceName));
						
					}

				}

			}
		} catch (Exception exx) {
			// attempt to read it from our jar.
			// first we only want the filename.

			Bukkit.getLogger().log(Level.INFO,
					"BCSURV:" + SourceName + " Not found. Reading from JAR...");
			// Bukkit.getLogger().log(Level.WARNING,
			// "Warning: Config/List file not found at " +
			// BCRandomizer.pluginConfigLocation + " using built in.");
			// sc = new
			// Scanner(getClass().getClassLoader().getResourceAsStream("survivalchests.cfg"));
			String onlyfname = new File(SourceName).getName();
			InputStream makeresult = null;

			makeresult = getClass().getClassLoader().getResourceAsStream(
					onlyfname);

			if (makeresult == null)
				makeresult = getClass().getClassLoader().getResourceAsStream(
						SourceName);

			return makeresult == null ? null : new Scanner(makeresult);

		}

		String onlyfile = SourceName.substring(SourceName
				.lastIndexOf(File.separatorChar) + 1);
		// System.out.println("Onlyfile=" + onlyfile);

		return new Scanner(this.getClass().getClassLoader()
				.getResourceAsStream(onlyfile));
	}

	public static String getItemMaterial(ItemStack item) {

		Material mat = item.getType();
		if (mat.equals(Material.LEATHER_HELMET)
				|| mat.equals(Material.LEATHER_CHESTPLATE)
				|| mat.equals(Material.LEATHER_LEGGINGS)
				|| mat.equals(Material.LEATHER_BOOTS))
			return "Leather";
		else if (mat.equals(Material.WOOD_SPADE)
				|| mat.equals(Material.WOOD_AXE)
				|| mat.equals(Material.WOOD_SWORD)
				|| mat.equals(Material.WOOD_PICKAXE))
			return "Wood";
		else if (mat.equals(Material.CHAINMAIL_HELMET)
				|| mat.equals(Material.CHAINMAIL_CHESTPLATE)
				|| mat.equals(Material.CHAINMAIL_LEGGINGS)
				|| mat.equals(Material.CHAINMAIL_BOOTS))
			return "Chainmail";

		else if (mat.equals(Material.IRON_HELMET)
				|| mat.equals(Material.IRON_CHESTPLATE)
				|| mat.equals(Material.IRON_LEGGINGS)
				|| mat.equals(Material.IRON_BOOTS)
				|| mat.equals(Material.IRON_SPADE)
				|| mat.equals(Material.IRON_AXE)
				|| mat.equals(Material.IRON_SWORD)
				|| mat.equals(Material.IRON_PICKAXE))
			return "Iron";
		else if (mat.equals(Material.GOLD_HELMET)
				|| mat.equals(Material.GOLD_CHESTPLATE)
				|| mat.equals(Material.GOLD_LEGGINGS)
				|| mat.equals(Material.GOLD_BOOTS)
				|| mat.equals(Material.GOLD_SPADE)
				|| mat.equals(Material.GOLD_AXE)
				|| mat.equals(Material.GOLD_SWORD)
				|| mat.equals(Material.GOLD_PICKAXE))
			return "Gold";
		else if (mat.equals(Material.DIAMOND_HELMET)
				|| mat.equals(Material.DIAMOND_CHESTPLATE)
				|| mat.equals(Material.DIAMOND_LEGGINGS)
				|| mat.equals(Material.DIAMOND_BOOTS)
				|| mat.equals(Material.DIAMOND_SPADE)
				|| mat.equals(Material.DIAMOND_AXE)
				|| mat.equals(Material.DIAMOND_SWORD)
				|| mat.equals(Material.DIAMOND_PICKAXE))
			return "Diamond";
		else
			return "";

	}

	public static void VanishPlayer(Player invisify) {

		for (Player p : Bukkit.getOnlinePlayers()) {

			p.hidePlayer(invisify);

		}

	}

	public static void unvanishPlayer(Player visify) {

		for (Player p : Bukkit.getOnlinePlayers()) {
			p.showPlayer(visify);
		}

	}

	public BCRandomizer() {

		Victories = new VictoryTracker(this);

	}

	public static Player getNearestPlayer(Location testlocation) {

		World applyworld = testlocation.getWorld();

		double MinimumDistance = Double.MAX_VALUE;
		Player minimumPlayer = null;
		// iterate through all the players in this world.
		for (Player p : applyworld.getPlayers()) {
			double gotdistance = p.getLocation().distance(testlocation);
			if (gotdistance < MinimumDistance) {
				MinimumDistance = gotdistance;
				minimumPlayer = p;
			}
		}

		return minimumPlayer;

	}
	public static String getMCVersion()
	{
	String versionstring = Bukkit.getServer().getVersion();
	//find (MC: 1.5)
	String searchfor = "(MC: ";
	int foundpos = versionstring.indexOf(searchfor)+searchfor.length();
	int endparen = versionstring.indexOf(')',foundpos);
	
	return versionstring.substring(foundpos,endparen);
	}
	WorldTracker wt = null;
	@Override
	public void onEnable() {
		
		
		System.out.println("BCSURV running on Server version:" + getMCVersion());
		
		
		try {
			Configuration = new Configurator(this);
			if(NameGen!=null){
				NameGen.setOwner(this);
			}
			else
			{
				NameGen = new NameGenerator(this);
			}
			//System.out.println(Configuration.getContainerStatic().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		getGriefPrevention();
		
		Randomcommand = new RandomizerCommand(this);
		PluginCommand batchcommand = this.getCommand("repopchests");

		batchcommand.setExecutor(Randomcommand);

		PluginCommand delayPvP = this.getCommand("startgame");
		delayPvP.setExecutor(Randomcommand);

		// PluginCommand teamsplit = this.getCommand("teamsplit");
		// teamsplit.setExecutor(Randomcommand);

		PluginCommand friendly = this.getCommand("friendly");
		friendly.setExecutor(Randomcommand);

		this.getCommand("preparegame").setExecutor(Randomcommand);
		this.getCommand("joingame").setExecutor(Randomcommand);
		this.getCommand("spectategame").setExecutor(Randomcommand);
		this.getCommand("gamestate").setExecutor(Randomcommand);
		this.getCommand("arenaborder1").setExecutor(Randomcommand);
		this.getCommand("arenaborder2").setExecutor(Randomcommand);
		this.getCommand("clearborders").setExecutor(Randomcommand);
		//this.getCommand("randomizespawners").setExecutor(Randomcommand);
		this.getCommand("mobmode").setExecutor(Randomcommand);
		this.getCommand("mobtimeout").setExecutor(Randomcommand);
		this.getCommand("mobsweeper").setExecutor(Randomcommand);
		this.getCommand("repoptimeout").setExecutor(Randomcommand);
		this.getCommand("setlives").setExecutor(Randomcommand);
		this.getCommand("concludegame").setExecutor(Randomcommand);
		this.getCommand("borders").setExecutor(Randomcommand);
		this.getCommand("saveborder").setExecutor(Randomcommand);
		this.getCommand("loadborder").setExecutor(Randomcommand);
		this.getCommand("newarena").setExecutor(Randomcommand);
		this.getCommand("listworlds").setExecutor(Randomcommand);
		this.getCommand("mwtp").setExecutor(Randomcommand);
		this.getCommand("mwdel").setExecutor(Randomcommand);
		// this.getCommand("setfly").setExecutor(Randomcommand);
		// this.getCommand("prepareinfo").setExecutor(Randomcommand);
		// this.getCommand("fixup").setExecutor(Randomcommand);
		// this.getCommand("strike").setExecutor(Randomcommand);
		ChestRandomizer.reload(this, null);
		SchematicImporter.Init(this);
		wt = new WorldTracker(this);
		
	}

	@Override
	public void onDisable() {
		try {
		Configuration.save();
		NameGen=null;
		}
		catch(IOException iox){
			
			iox.printStackTrace();
			
		}
		for (GameTracker t : ActiveGames) {
			t.gamecomplete = true;
			t.deathwatcher._Trackers = null;

		}

	}

	private static final boolean _debug = false;

	public static void emitmessage(String msg) {

		if (_debug)
			System.out.println(msg);

	}
	public static NameGenerator NameGen=null;
	public static void main(String[] args) {

		/*
		SpawnRandomizerConfig src = new SpawnRandomizerConfig(new File(
				"D:\\testspawndata.xml"));
		System.out.println(src.SpawnData.size());

		// ChestRandomizer.reloadXML(null);
		System.out.println(ChestRandomizer.getRandomData().size()
				+ " RandomData items...");
		System.out.println(ChestRandomizer.getStaticData().size()
				+ " Static Data items...");
*/
		ItemBucket<Integer> bucket = new ItemBucket<Integer>(ItemBucket.RepeatArray(new Integer[]{1, 2, 3,4,5,6,7,8,9,10},3,Integer.class));
		
		//choose 90 items.
		for(int i=0;i<90;i++){
			int gotvalue = bucket.Dispense().intValue();
			System.out.print(i + ":" + gotvalue + ",");
			
			
			
		}
		
	}
	public static int getAverageY(Location BorderA,Location BorderB){
		
		int MinX = Math.min(BorderA.getBlockX(), BorderB.getBlockX());
		int MinY = Math.min(BorderA.getBlockY(), BorderB.getBlockY());
		int MinZ = Math.min(BorderA.getBlockZ(), BorderB.getBlockZ());
		int MaxX = Math.max(BorderA.getBlockX(), BorderB.getBlockX());
		int MaxY = Math.max(BorderA.getBlockY(), BorderB.getBlockY());
		int MaxZ = Math.max(BorderA.getBlockZ(), BorderB.getBlockZ());
		
		Location MinBorder = new Location(BorderA.getWorld(),MinX,MinY,MinZ);
		Location MaxBorder = new Location(BorderA.getWorld(),MaxX,MaxY,MaxZ);
		int AccumSum=0;
		int countentries=0;
		for(int x=MinBorder.getBlockX();x<MaxBorder.getBlockX();x++){
			for(int z=MinBorder.getBlockZ();z<MaxBorder.getBlockZ();z++){
				
				Location testlocation = new Location(BorderA.getWorld(),x,MaxY,z);
				Location maxY = HighestBlockAt(testlocation);
				
				AccumSum+=maxY!=null?maxY.getBlockY():0;
				countentries++;
				
			}
		}
		
		return AccumSum/countentries;
		
		
	}
	
	public static Location HighestBlockAt(Location loctest){
		return HighestBlockAt(loctest,new Material[]{Material.AIR,Material.BEDROCK,Material.BEACON,Material.LEAVES,Material.WOOD});
	}
	public static int getHighestBlockYAt(World w,int xpos,int zpos){
		
		Location l = HighestBlockAt(new Location(w,xpos,0,zpos));
		return l!=null?l.getBlockY():0;
	}
	public static Location HighestBlockAt(Location loctest,Material[] AirBlocks){
		World w = loctest.getWorld();
		//start at 256.
		int xtest = loctest.getBlockX();
		int ztest = loctest.getBlockZ();
		for(int y=256;y>0;y--){
			Material gettype = w.getBlockAt(xtest,y,ztest).getType();
			boolean isair=false;
			for(Material testtype:AirBlocks){
				if(testtype.equals(gettype)) {isair=true;break;}
				
			}
			if(!isair) return new Location(w,xtest,y,ztest);
			
			
		}
		
		return null;
		
	}
	public GameTracker getWorldGame(World testworld) {

		for (GameTracker gt : ActiveGames) {
			if (gt.getStillAlive().size() > 0) {
				if (gt.getStillAlive().getFirst().getWorld().equals(testworld)) {

					return gt;

				}
			}
		}
		return null;

	}

	public GameTracker getPlayerGame(Player useplayer) {
		// TODO Auto-generated method stub

		for (GameTracker gt : ActiveGames) {

			if (gt.getStillAlive().contains(useplayer)
					|| gt.getSpectating().contains(useplayer)
					|| gt.getDead().contains(useplayer)) {

				return gt;

			}

		}
		return null;

	}

	public static String getHoliday(Date date) {
		// returns null if it isn't a Holiday
		Calendar caluse = Calendar.getInstance();
		caluse.setTime(date);
		return getHoliday(caluse);
	}

	private static HashMap<String, String> HolidayLookup = null; // we use
																	// locale-inspecific
																	// method,
																	// MM/DD

	public static String getHoliday(Calendar cal) {

		if (HolidayLookup == null) {
			// initialize holiday hashmap.

			HolidayLookup = new HashMap<String, String>();
			HolidayLookup.put("0/1", "New Years");
			HolidayLookup.put("5/1", "Canada Day(Canada)");
			HolidayLookup.put("5/4", "Independence Day(US)");

		}
		// create String representation of calendar...
		String uselookup = cal.get(Calendar.MONTH) + "/"
				+ cal.get(Calendar.DAY_OF_MONTH);

		return HolidayLookup.get(uselookup);

	}

}
