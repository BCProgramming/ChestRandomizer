package com.BASeCamp.SurvivalChests;


import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedList;

import net.minecraft.server.Block;
import net.minecraft.server.EntityItemFrame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.*;
import org.bukkit.util.Vector;
import org.bukkit.*;




public class PlayerDeathWatcher implements Listener{
	private BCRandomizer _owner=null;
	private World watchworld;
	public static LinkedList<GameTracker> _Trackers= new LinkedList<GameTracker>();
	//public GameTracker _Tracker=null;
	public PlayerDeathWatcher(BCRandomizer bcRandomizer,GameTracker gt,World watchworld) {
			_owner=bcRandomizer;
			_Trackers.add(gt);
			
			
	}
	
	public static boolean allCaps(String strtest){
	for(int i=0;i<strtest.length();i++){
		
		char currchar = strtest.charAt(i);
		if(currchar!=Character.toUpperCase(currchar))
			return false;	
		
		}
		return true;
	
	}
	public static String capitalizeString(String string) {
		  char[] chars = string.toLowerCase().toCharArray();
		  boolean found = false;
		  for (int i = 0; i < chars.length; i++) {
		    if (!found && Character.isLetter(chars[i])) {
		      chars[i] = Character.toUpperCase(chars[i]);
		      found = true;
		    } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' ) { // You can add other chars here
		      found = false;
		    }
		  }
		  return String.valueOf(chars);
		}
	private static String FriendlizeName(String source)
	{
		source = source.replace("item_"," ");
		source = source.replace('_', ' ');
		
		
		
		return allCaps(source)?capitalizeString(source):source;
		
	}
	HashMap<ItemFrame,ItemStack> repopulateFrames = new HashMap<ItemFrame,ItemStack>(); //key is the frame, value is what to repopulate with
	
	
	@EventHandler
	public void OnHangingBreakByEntityEvent(HangingBreakByEntityEvent event){
		HangingBreakByEntityEvent edam = event;
		
		if(edam.getEntity() instanceof ItemFrame){
			System.out.println("itemFrame broken");
			if(edam.getRemover() instanceof Player){
				Player theplayer = (Player)edam.getRemover();
				if(_owner.isParticipant(theplayer)!=null){
				
				//if it has a button, give the player a button and remove the button from the itemframe.
				//also add this itemframe to the list of frames to repopulate.
				ItemFrame gotframe = (ItemFrame) edam.getEntity();
				
					 
				ItemStack contents = gotframe.getItem();
				if(contents!=null){
				gotframe.setItem(null);
				repopulateFrames.put(gotframe, contents);
				//give the player the contents.
				theplayer.getInventory().addItem(contents);
				theplayer.sendMessage("You have acquired a " + getFriendlyNameFor(contents));
				}
				else{
					//not a participant.
					theplayer.sendMessage(ChatColor.YELLOW + "You cannot break item frames unless you are participating.");
					
					
				}
				}
				
			}
			
			
			event.setCancelled(true);
		return;	
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		if(event instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent edam = (EntityDamageByEntityEvent)event;
			
		
		if(edam.getEntity() instanceof Player){
			
			
			//entity is the player that is damaged.
			Player damaged = (Player)edam.getEntity();
			
			if(damaged.getWorld()!=watchworld) return;
			
			//
			
			if(edam.getDamager() instanceof Player){
				Player Attacker = (Player)edam.getDamager();
				
				//if the damagee (receiver) is in a game, and the attacker isn't...
				//only allow the damage if both players are participants in the same game.
				if(_owner.Randomcommand.getaccepting()) {
					
					event.setCancelled(true);
					
				}
				GameTracker gameorigin = _owner.isParticipant(damaged);
				if(!gameorigin.getStillAlive().contains(Attacker)){
					
					System.out.println(Attacker.getName() + " tried to attack " + damaged.getName() + "Cancelled as they are not participants in the same game.");
					event.setCancelled(true);
					
				}
				
				//only Player versus Player damage is reported.
				//<Attacker> has struck you with a <Weapon> for <Damage>
				//only report when the distance between the players is more than three blocks.
				if(Attacker!=null){
				if(damaged.getLocation().distance(Attacker.getLocation()) > 3){
					damaged.sendMessage("You have been struck by " + Attacker.getDisplayName() + "!");
				}
				}
			}
			
		}
		}
	}
	public static String getFriendlyNameFor(ItemStack Item){
		
		if(Item==null) return "Nothing";
		String weapon = Item.getType().name();
		
		ItemNamer.load(Item);
		String gotname= ItemNamer.getName();
		if(gotname!=null && gotname!="")
			weapon = gotname;
		else{
			if(RandomData.isHead(Item)){
				weapon = RandomData.getHeadName(Item);
				
			}
			else if(RandomData.isDye(Item)){
				weapon = RandomData.getDyeName(Item);
			}
		
		
		}
		return FriendlizeName(weapon);
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		
		if(_owner.Randomcommand.getaccepting()){
			
			event.getPlayer().sendMessage(ChatColor.YELLOW + "Welcome! A game is being prepared. ");
			event.getPlayer().sendMessage(ChatColor.YELLOW + "use /joingame to join, and /spectategame to watch.");
			
			
		}
		
		
		
		
	}
	 @EventHandler
	 public void onInteract(PlayerInteractEvent event) {
		 
		 if(event.hasBlock()){
		 
			 if (event.getClickedBlock().getType() == Material.CHEST ||
					 event.getClickedBlock().getType()==Material.DISPENSER ||
					 event.getClickedBlock().getType()==Material.FURNACE){
			
				 
				 //only ops may look in chests when they are outside a game.
				 Player p  = event.getPlayer();
				 if(_owner.isParticipant(p)==null){
					 
					 //not a participant... so they had better be an op!
					 if(!p.isOp()){
						 //oh... too bad.
						 event.setCancelled(true);
					 }
					 
				 }
				 
				 
				 
				 
			 }
		 } 
	 }
   @EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		
	   if(_Trackers==null || _Trackers.size() == 0) return;
		//Bukkit.broadcastMessage(event.getEntity().getName() + " has been slain!");
		ChatColor usecolor = ChatColor.YELLOW;
		String usemessage = event.getDeathMessage();
		final Player dyingPlayer = event.getEntity();
		
	
		
		
		String DyingName = dyingPlayer.getName();
		String KillerName="";
		final Player Killer = dyingPlayer.getKiller();
		if(Killer!=null) KillerName = Killer.getName();
		if(dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.FALL))
		{
		
		usemessage=dyingPlayer.getName()+ " Fell " + Math.round(dyingPlayer.getFallDistance()) + " Blocks to their Death." ;
			
		}
		else if(dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.BLOCK_EXPLOSION)){
			String[] explosionstrings = new String[] {
					dyingPlayer + " blew up.",
					dyingPlayer + "'s body parts became shrapnel",
					dyingPlayer + " exploded.",
					dyingPlayer + " imitated a Creeper."
					
			};
			usemessage = RandomData.Choose(explosionstrings);
		
		}
		else if((dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.DROWNING))){
		String[] possiblemessages = new String[] {
				DyingName + " Drowned.",
				DyingName + " Forgot to come up for air",
				DyingName + " Sleeps with the fishes"};
		usemessage = RandomData.Choose(possiblemessages);
		
				
				
		}
		else if((dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.SUICIDE))){
			String[] possiblemessages = new String[]{
					DyingName + " ended it all",
					DyingName + " didn't even leave a suicide note",
					DyingName + " was a casualty of their own genius",
					DyingName + " killed their own dumb self",
					DyingName + " committed suicide.",
					DyingName + " cried for Celestia.",
					DyingName + " failed to survive death"
					
			};
			usemessage = RandomData.Choose(possiblemessages);
			
		}
		else if((dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.POISON))){
		String[] possiblemessages = new String[] {
			DyingName + " threw up and then died.",
			DyingName + " should have seen a doctor.",
			DyingName + " couldn't find the antidote.",
			DyingName + " died from poison."};
			usemessage = RandomData.Choose(possiblemessages);
		
		}
		else if((dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.SUFFOCATION))){
			String[] possiblemessages = new String[] {
			DyingName + " tried to share space with a wall. It didn't work out.",
			DyingName + " suffocated in a wall.",
			DyingName + " choked on their own stupidity."
					
			};
			usemessage = RandomData.Choose(possiblemessages);
		}
		else if((dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.LIGHTNING))){
			
			String[] possiblemessages = new String[] {
			 DyingName + " was struck down by Celestia",
			 DyingName + " was struck down by Luna",
			 DyingName + " was electrocuted.",
			 DyingName + " acted as a lightning rod",
			 DyingName + " was struck by lightning",
			 DyingName + " was consumed by King Sombre"
					
			};
			
			
		}
		else if(Killer!=null){
			//ok, get the item the Killer has.
			if(Killer instanceof Player){
			String weapon = getFriendlyNameFor(Killer.getItemInHand());
			
			
			
			
			
			String[] possiblemessages = new String[] {
					DyingName + " met there end from " + 
					KillerName + "'s " + weapon + ".",
					KillerName + " and their " + weapon + " slaughtered " + DyingName,
					DyingName + " was slain by " + KillerName + " using " + weapon,
					KillerName + " killed " + DyingName + " with a " + weapon,
					KillerName + " gave " + DyingName + " a closer look at their " + weapon
			};
			if(_owner.isParticipant(dyingPlayer)!=null){
			Bukkit.getPluginManager().callEvent(new ParticipantDeathEvent(dyingPlayer,Killer,weapon));
			}
			usemessage = RandomData.Choose(possiblemessages);
			
			}
		}
		usemessage = usemessage.replace(DyingName, ChatColor.RED + DyingName + usecolor);
		if(KillerName.length() > 0)
			usemessage = usemessage.replace(KillerName,ChatColor.BLUE + KillerName + ChatColor.YELLOW);
		usemessage=usemessage + "(" + dyingPlayer.getLastDamage() + " damage)";
				BCRandomizer.emitmessage(usemessage + dyingPlayer.getLastDamageCause().toString());
		event.setDeathMessage(usecolor + usemessage);
		
		ItemStack createdhead = RandomData.getHead(DyingName);
		Enchantment useenchant;
		
		EnchantmentProbability ep = new EnchantmentProbability();
		ep.Apply(createdhead, true);
		
		
		
		//event.getDrops().add(createdhead);
		//we need to return first, so the death message can be issued first, then we will tell the gameTracker about the death. 
		_owner.getServer().getScheduler().scheduleSyncDelayedTask(_owner, new Runnable() {

			   public void run() {
			   
		
		if(_Trackers!=null){
			//if there is a Tracker, notify it of the player death. do this after. The tracker
			//tracks the game itself.
			//_Tracker.PlayerDeath(dyingPlayer,Killer);
			for(GameTracker Tracker : _Trackers){
				Tracker.PlayerDeath(dyingPlayer, Killer);
			}
		}
			   }
		}, 20L);
	
		
	}
   
   @EventHandler 
   public void onPlayerMove(PlayerMoveEvent event) {
	  // System.out.println("Player moved:" + event.getPlayer().getName());
	   //check the borders...
	   Location BorderA = _owner.Randomcommand.BorderA;
	   Location BorderB = _owner.Randomcommand.BorderB;
	   double XMinimum,XMaximum,ZMinimum,ZMaximum;
	   if(BorderA!=null && BorderB !=null){
		   System.out.println("BorderA And BorderB are not null...");
		   XMinimum=Math.min(BorderA.getX(),BorderB.getX());
		   XMaximum = Math.max(BorderA.getX(), BorderB.getX());
		   ZMinimum = Math.min(BorderA.getZ(),BorderB.getZ());
		   ZMaximum = Math.max(BorderA.getZ(), BorderB.getZ());
		   
		   System.out.println("XMin:" + XMinimum + " Xmax:" + XMaximum + " ZMin:" + ZMinimum + "ZMax:" + ZMaximum);
		   
		   Player p = event.getPlayer();
		   if(null!=_owner.isParticipant(p)){
			   System.out.println("moved player is a participant.");
			   Location ploc = p.getLocation();
			   if(ploc.getX() < XMinimum){
				   System.out.println("lower than XMin");
				   
				    p.setVelocity(
				    		new Vector(1+Math.abs(p.getVelocity().getX()+1),
				    				p.getVelocity().getY(),
				    				p.getVelocity().getZ()));
				    
				    p.teleport(new Location(p.getWorld(),XMinimum+1,ploc.getY(),ploc.getZ()));
				   	
				  
			   }
			   if(ploc.getZ() < ZMinimum){
				   System.out.println("lower than ZMin");
				   
				   p.setVelocity(
					new Vector(p.getVelocity().getX(),
							p.getVelocity().getY(),
							1+Math.abs(p.getVelocity().getZ())));
				   
				   
				   
				   
				   
				   p.teleport(new Location(p.getWorld(),ploc.getX(),ploc.getY(),ZMinimum+1));
				   
			   }
			   
			   
			   
			   if(ploc.getX() > XMaximum){
				   System.out.println("higher than XMax");
				   p.setVelocity(
						   new Vector(-Math.abs(p.getVelocity().getX())-1,
								   p.getVelocity().getY(),
								   p.getVelocity().getZ()));
						   
				   
				   p.teleport(new Location(p.getWorld(),XMaximum-1,ploc.getY(),ploc.getZ()));
			   }
			   if(ploc.getZ() > ZMaximum){
				   System.out.println("higher than ZMax");
				   
				   p.setVelocity(
						   new Vector(p.getVelocity().getX(),
						   p.getVelocity().getY(),
						   -Math.abs(p.getVelocity().getZ())-1));
				   
				p.teleport(new Location(p.getWorld(),ploc.getX(),ploc.getY(),ZMaximum-1));   
				   
			   }
			   
			   
			   
			   
			   
			   
			   
		   }   
	   }
	   
	   
	   
	   
   }
   
   
   private Material KeySpotMaterial = Material.GOLD_BLOCK;
   LinkedList<Location> setAirLocations = new LinkedList<Location>();
   @EventHandler
   public void onBlockPlacement(BlockPlaceEvent event){
	   System.out.println("block placed by " + event.getPlayer().getName());
	   
	   ////change: no block changes can be made by non ops.
	   if(event.getBlockAgainst().getType().equals(KeySpotMaterial) &&
			   event.getItemInHand().getType().equals(Material.STONE_BUTTON))
	   {
		   event.getPlayer().sendMessage(ChatColor.GOLD + "Key Placed!");
		   event.getPlayer().playEffect(event.getPlayer().getLocation(),Effect.EXTINGUISH,1);
		   //save this Location to reset it to air.
		   setAirLocations.add(event.getBlock().getLocation());
		   return; //allow placement of buttons on gold blocks.
		   
	   }
	   else if(event.getItemInHand().getType().equals(Material.STONE_BUTTON)){
		   event.getPlayer().sendMessage(ChatColor.WHITE + "This is a Key! Place it on a " + ChatColor.GOLD + FriendlizeName(KeySpotMaterial.name()) + ChatColor.WHITE + " To find treasures!");
		   event.setCancelled(true);
		   return;
	   }
	   
	   if(!event.getPlayer().isOp()){
		   event.getPlayer().sendMessage("You cannot place blocks.");
		   event.setCancelled(true);
		   return;
	   }
	   if(null!=_owner.isParticipant(event.getPlayer())){
		   event.getPlayer().sendMessage("You cannot place blocks when in an event, even as an Op.");
		   event.setCancelled(true);
	   }
	   
   }
   @EventHandler
   public void onBlockBreak(BlockBreakEvent event){
	   //can't break blocks if participating.
	   
	   
	   if(!event.getPlayer().isOp()){
		   event.getPlayer().sendMessage("You cannot break blocks.");
		   event.setCancelled(true);
		   return;
	   }
	   if(null!=_owner.isParticipant(event.getPlayer())){
		   event.getPlayer().sendMessage("You cannot break blocks when in an event, even as an Op.");
		   event.setCancelled(true);
	   }
   }
   
   
   @EventHandler
   public void onPlayerDisconnect(PlayerQuitEvent event) {
	   
	   if(_owner.Randomcommand.getaccepting()){
		   _owner.Randomcommand.getjoinedplayers().remove(event.getPlayer());
		   
	   }
	   
	   else if(_Trackers!=null)
    	   for(GameTracker Tracker : _Trackers){
    	   BCRandomizer.clearPlayerInventory(event.getPlayer());
    	   
    	   Tracker.PlayerDeath(event.getPlayer(), null);
    	   }   
       
   }
   private HashMap<World,LinkedList<CachedFrameData>> CachedData = new HashMap<World,LinkedList<CachedFrameData>>();

   public void onGameStart(GameStartEvent event)
   {
	   
	   System.out.println("Game Started");
	   World Worldgrab = event.getParticipants().get(0).getWorld();
	   //record itemframe locations.
	   if(!CachedData.containsKey(Worldgrab))
		   CachedData.put(Worldgrab, new LinkedList<CachedFrameData>());
	   LinkedList<CachedFrameData> datagrab = CachedData.get(Worldgrab);
	   int framescount = 0;
	   for(Chunk iteratechunk:Worldgrab.getLoadedChunks()){
		   
		   for(Entity iterateentity :iteratechunk.getEntities()){
			   
			   if(iterateentity instanceof ItemFrame){
				   
				   ItemFrame g;
				   ItemFrame foundframe = (ItemFrame)iterateentity;
				   datagrab.add(new CachedFrameData(foundframe));
				   framescount++;
			   }
			   
		   }
		   
	   }
	   System.out.println("Cached " + framescount + " Item frames.");
	   
	 
	   
	   
   }


   
 
   public void onGameEnd(GameEndEvent event){
	   //re-add the item frames.
	   System.out.println("Game End");
	   World worldevent = event.getAllParticipants().get(0).getWorld();
	   
	   ChestRandomizer.resetStoredInventories();
	   
	   
	   if(!CachedData.containsKey(worldevent)) {
		   System.out.println("SurvivalChests:Key not found for world attempting to revive itemframes...");
		   return; //hmm, curious.
	   }
	   LinkedList<CachedFrameData> framedata = CachedData.get(worldevent); 
	   
	   System.out.println("restoring " + repopulateFrames.size() + " Item Frame contents...");
	   for(ItemFrame iterateframe : repopulateFrames.keySet()){
		   
		   
		   iterateframe.setItem(repopulateFrames.get(iterateframe));
		   
		   
	   }
	  
	   
	//find them all!
	   for(Chunk loopchunk:event.getAllParticipants().get(0).getWorld().getLoadedChunks()){
		   
		   for(int x=0;x<15;x++){
			   
			   for(int y=0;y<255;y++){
				   
				   for(int z=0;z<15;z++){
					   
					   org.bukkit.block.Block acquired = loopchunk.getBlock(x,y,z);
					   if(acquired.getType().equals(Material.GOLD_BLOCK)){
						
						   World owner = acquired.getWorld();
						   
						   //check +1 -1 x and +1 -1 Z combinations, look for existing air blocks.
						   
						   LinkedList<Location> CheckLocations = new LinkedList<Location>();
						   CheckLocations.add(new Location(owner,x-1,y,z));
						   CheckLocations.add(new  Location(owner,x,y,z-1));
						   CheckLocations.add(new Location(owner,x,y,z+1));
						   CheckLocations.add(new Location(owner,x+1,y,z));
						   for(Location checloc:CheckLocations){
							   
							   if(checloc.getBlock().getType().equals(Material.AIR)){
								   
								   setAirLocations.add(checloc);
								   
							   }
							   
							   
						   }
						   
						   
						   
					   }
						   
						   
						   
						   
						   
						   
						   
					   }
					   
					   
				   }
				   
			   }
			   
		   }
		   
		   
	   
	   
	   
		
	   for(Location setair : setAirLocations){
		   
		   setair.getBlock().setType(Material.AIR);
		   
		   
		   
	   }
	   setAirLocations = new LinkedList<Location>();
	   
	   
		   
		   
	   }
	   
	   
	   
	   
	   
	   
	   
   }
   
   
   

