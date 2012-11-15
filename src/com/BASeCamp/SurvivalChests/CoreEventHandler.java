package com.BASeCamp.SurvivalChests;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.server.Block;
import net.minecraft.server.EntityItemFrame;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.entity.CraftMonster;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.bukkit.*;
import org.bukkit.entity.Monster;
public class CoreEventHandler implements Listener {
	private BCRandomizer _owner = null;
	private World watchworld;
	public static LinkedList<GameTracker> _Trackers = new LinkedList<GameTracker>();

	// public GameTracker _Tracker=null;
	
	
	
	
	public CoreEventHandler(BCRandomizer bcRandomizer, GameTracker gt,
			World watchworld) {
		_owner = bcRandomizer;
		_Trackers.add(gt);
		this.watchworld = watchworld;

	}

	public static boolean allCaps(String strtest) {
		for (int i = 0; i < strtest.length(); i++) {

			char currchar = strtest.charAt(i);
			if (currchar != Character.toUpperCase(currchar))
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
			} else if (Character.isWhitespace(chars[i]) || chars[i] == '.') { // You
																				// can
																				// add
																				// other
																				// chars
																				// here
				found = false;
			}
		}
		return String.valueOf(chars);
	}

	private static String FriendlizeName(String source) {
		source = source.replace("item_", " ");
		source = source.replace('_', ' ');

		return allCaps(source) ? capitalizeString(source) : source;

	}

	@EventHandler
	public void OnPlayerInteractEntity(PlayerInteractEntityEvent event) {
		GameTracker applicablegame = _owner.getWorldGame(event.getPlayer().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		if (event.getRightClicked() instanceof ItemFrame) {

			if(!event.getPlayer().isOp())
				event.setCancelled(true);
			else if(_owner.isParticipant(event.getPlayer())!=null){
				
				event.setCancelled(true);
			}

		}

	}

	HashMap<ItemFrame, ItemStack> repopulateFrames = new HashMap<ItemFrame, ItemStack>(); // key
																							// is
																							// the
																							// frame,
																							// value
																							// is
																							// what
																							// to
																							// repopulate
																							// with
	HashMap<ItemFrame, Player> Frametakers = new HashMap<ItemFrame, Player>();

	
	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent event){
		GameTracker applicablegame = _owner.getWorldGame(event.getPlayer().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		if(_owner.isParticipant(event.getPlayer())!=null || _owner.isSpectator(event.getPlayer())!=null){
			
			event.getPlayer().sendMessage(BCRandomizer.Prefix + "You cannot sleep!");
			event.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void OnHangingBreakByEntityEvent(HangingBreakByEntityEvent event) {
		GameTracker applicablegame = _owner.getWorldGame(event.getEntity().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		HangingBreakByEntityEvent edam = event;

		if (edam.getEntity() instanceof ItemFrame) {
			System.out.println("itemFrame broken");
			if (edam.getRemover() instanceof Player) {
				Player theplayer = (Player) edam.getRemover();
				if (_owner.isParticipant(theplayer) != null) {
					event.setCancelled(true);
					// if it has a button, give the player a button and remove
					// the button from the itemframe.
					// also add this itemframe to the list of frames to
					// repopulate.
					ItemFrame gotframe = (ItemFrame) edam.getEntity();
					// check if it is already in the HashMap.
					if (repopulateFrames.containsKey(gotframe)) {
						// somebody already took this item.
						//tell them who it was.
						if(theplayer.equals(Frametakers.get(gotframe))){
							
							theplayer.sendMessage(BCRandomizer.Prefix + " You have already taken this item.");
							return;
						}
						else {
							theplayer.sendMessage(BCRandomizer.Prefix + ChatColor.YELLOW + Frametakers.get(gotframe).getDisplayName() + " has taken this item.");
							return;
						}

					} else {
						ItemStack contents = gotframe.getItem();
						if (contents != null) {
							gotframe.setItem(null);
							repopulateFrames.put(gotframe, contents);
							Frametakers.put(gotframe, theplayer);
							// give the player the contents.
							theplayer.getInventory().addItem(contents);
							theplayer.sendMessage(BCRandomizer.Prefix + "You have acquired a "
									+ getFriendlyNameFor(contents));
						}

						else {
							//item frame contents are null.
							

						}
						
						}
					
				
				}
				else { //(_owner.isParticipant(theplayer) != null) 
					
					
						theplayer
									.sendMessage(BCRandomizer.Prefix + ChatColor.YELLOW
											
											+ "You cannot take from item frames unless you are participating.");
				}
			}

			
			return;
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event){
		
		//only applicable if the world has a game going on and that game is a MobArena game.
		GameTracker applicablegame = _owner.getWorldGame(event.getEntity().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		if(!applicablegame.getMobArenaMode()) return;
		
		//so far we know it is in a game world and it is applicable to a Game that is a Mob Arena
		//Game. 
		
			
			
			
		
		
		//obviously it's only applicable for Monsters.
		if(event.getEntity() instanceof LivingEntity){
			
			//and also only applicable if the Killer is a Player.
			if(event.getEntity().getKiller()!=null){
				
				Player awardplayer = (Player)event.getEntity().getKiller();
				String entityname = FriendlizeName(event.getEntity().getType().getName());
				//also, we can only award a player that is actually participating.
				if(applicablegame.getStillAlive().contains(awardplayer)){
				
				HashMap<Player,Integer> tally = applicablegame.getScoreTally();
				
				//retrieve current score.
				int currscore = tally.get(awardplayer);
				int addedvalue = getMonsterValue((LivingEntity)(event.getEntity()));
				addedvalue += getMonsterValue(awardplayer);
				currscore+= addedvalue;
				tally.put(awardplayer, currscore);
				//inform the player.
				awardplayer.sendMessage(BCRandomizer.Prefix + " You have been awarded " + addedvalue + " Points for killing a " + entityname + " Total(" + currscore + ")");
				}
			}
			
			
		}
		
		
	}
	
	private int getMonsterValue(LivingEntity monster) {
		// TODO Auto-generated method stub
		int basescore = 1;
		basescore = Math.min(1,monster.getMaxHealth()/5);
		//first, calculate base values.
		if(monster instanceof Zombie){
			
			basescore*=1.5f;
			
		}
		else if(monster instanceof Skeleton){
			basescore *=2;
		}
else if(monster instanceof CaveSpider) {
			
			basescore*=4;
		}
		else if(monster instanceof Spider) {
			
			basescore *=1.2;
			
		}
		
		else if(monster instanceof Blaze){
			basescore*=5;
		}
		else if(monster instanceof MagmaCube) {
			basescore*=2;
		}
		else if(monster instanceof Slime){
			basescore*=1;
		}
		else if(monster instanceof Creeper) {
			
			
			Creeper cr = (Creeper)monster;
			
			basescore *= cr.isPowered()?6:2; //three times the score for powered creepers.
		}
		else if(monster instanceof Ghast){
			
			basescore*=10; //Ghasts are worth a lot of points :P
			
			
		}
		for(PotionEffect pe:monster.getActivePotionEffects()){
			
			int potionscore = 0;
			if(pe.getType().equals(PotionEffectType.REGENERATION))
				potionscore = 5;
			else if(pe.getType().equals(PotionEffectType.INCREASE_DAMAGE))
				potionscore+=5;
			else if(pe.getType().equals(PotionEffectType.INVISIBILITY))
				potionscore+=10;
			else if(pe.getType().equals(PotionEffectType.SPEED))
				potionscore+=3;
			
			potionscore*=(pe.getAmplifier()+1);
			
			basescore+=potionscore;
		}
		
		
		if(monster instanceof Player){
			
			//kind of a "secret" salt added to scores. This will increase scores for players with better equipment,
			//making it worthwhile to have better equipment. I was going to do it backwards (worse equipment and better mobs would give you more points) but
			//it seems silly to handicap people that found good equipment. This will also make scores seem more "random" since people
			//are likely to use different items and wear different armour over time.
			
			Player grabplayer = (Player)monster;
			ItemStack[] testitems = new ItemStack[] {
					
					grabplayer.getInventory().getHelmet(),
					grabplayer.getInventory().getChestplate(),
					grabplayer.getInventory().getLeggings(),
					grabplayer.getInventory().getBoots(),
					grabplayer.getInventory().getItemInHand()
					
					
			};
			
			for(ItemStack getvalue:testitems){
				
				if(getvalue!=null){
					
					basescore+=getItemValue(new CraftItemStack(getvalue).getHandle());
					
					
				}
				
			}
			
			
			
			
			
		}
		
		if(monster instanceof Zombie || monster instanceof Skeleton ){
			
			
			CraftMonster cf = (CraftMonster)monster;
			net.minecraft.server.ItemStack[] equipment = cf.getHandle().getEquipment();
			
			for(net.minecraft.server.ItemStack getvalue : equipment) {
				
				
				
				basescore+= getItemValue(getvalue);
				
				
			}
			
			
		}
		
		
		return basescore;
	}
	private HashMap<Enchantment,Integer> enchantmentvalues = null;
	public HashMap<Enchantment,Integer> getEnchantmentValues() {
		
		
		if(enchantmentvalues==null){
			enchantmentvalues = new HashMap<Enchantment,Integer>();
			
			enchantmentvalues.put(Enchantment.ARROW_DAMAGE,1); //Power
			enchantmentvalues.put(Enchantment.ARROW_FIRE,2); //Flame
			enchantmentvalues.put(Enchantment.ARROW_INFINITE,3); //Infinity
			enchantmentvalues.put(Enchantment.ARROW_KNOCKBACK,2); //punch
			
			enchantmentvalues.put(Enchantment.DAMAGE_ALL, 2); //Sharpness
			enchantmentvalues.put(Enchantment.DAMAGE_ARTHROPODS,1); //arthropods
			enchantmentvalues.put(Enchantment.DAMAGE_UNDEAD, 1); //Smite
			enchantmentvalues.put(Enchantment.DIG_SPEED, 1); //efficiency
			enchantmentvalues.put(Enchantment.DURABILITY,1); //unbreaking
			enchantmentvalues.put(Enchantment.FIRE_ASPECT, 2); //Fire Aspect
			enchantmentvalues.put(Enchantment.KNOCKBACK, 2); //knockback
			enchantmentvalues.put(Enchantment.LOOT_BONUS_BLOCKS, 2); //fortune...
			enchantmentvalues.put(Enchantment.LOOT_BONUS_MOBS, 2);
			enchantmentvalues.put(Enchantment.PROTECTION_EXPLOSIONS, 1); //blast protection
			enchantmentvalues.put(Enchantment.PROTECTION_PROJECTILE, 1); //projectile protection
			enchantmentvalues.put(Enchantment.PROTECTION_FIRE, 1); //fire protection
			enchantmentvalues.put(Enchantment.PROTECTION_ENVIRONMENTAL, 2); //protection
			enchantmentvalues.put(Enchantment.SILK_TOUCH, 5); //silk touch
			enchantmentvalues.put(Enchantment.PROTECTION_FALL, 2); //feather falling
			enchantmentvalues.put(Enchantment.OXYGEN, 2); //respiration
			enchantmentvalues.put(Enchantment.WATER_WORKER, 2); //aqua Affinity
			
			
			
			
		}
		
		return enchantmentvalues;
		
		
		
	}
	
	private int getItemValue(net.minecraft.server.ItemStack getvalue){
		
		if(getvalue==null) return 0;
		int basescore = 1;
		
		//material values: 
		//wood/leather: 1
		//Iron: 2
		//Chain:3
		//Gold:4
		//Diamond:5
		
		if(MaterialHelper.isLeather(Material.getMaterial(getvalue.id)) || MaterialHelper.isWooden(Material.getMaterial(getvalue.id)))
			basescore+=1;
		else if(MaterialHelper.isIron(Material.getMaterial(getvalue.id)))
			basescore+=2;
		else if(MaterialHelper.isChainmail(Material.getMaterial(getvalue.id)))
			basescore+=3;
		else if(MaterialHelper.isGold(Material.getMaterial(getvalue.id)))
			basescore+=4;
		else if(MaterialHelper.isDiamond(Material.getMaterial(getvalue.id)))
			basescore+=5;
		
		
			System.out.println("Basescore:" + basescore);
		
			HashMap<Enchantment,Integer> enchants = new HashMap<Enchantment,Integer>();
			
			NBTTagList enchantments = getvalue.getEnchantments();
			if(enchantments!=null) {
			for (int i=0;i<enchantments.size();i++){
				
				NBTTagCompound casted = (NBTTagCompound)enchantments.get(i);
				int enchantmentID = casted.getInt("ID");
				int enchantmentLevel = casted.getInt("Level");
				enchants.put(Enchantment.getById(enchantmentID),enchantmentLevel);
				
				
				
			}
			}
		int enchantmentvalues = 0;
		for(Enchantment iterateenchant:enchants.keySet()) {
			
			enchantmentvalues += getEnchantmentValues().get(iterateenchant).intValue()
			* enchants.get(iterateenchant);
			
			
			
			
		}
		
		basescore+=enchantmentvalues;
		
		System.out.println("final Basescore:" + basescore);
		return basescore;
		
		
	}
	
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		
		
		GameTracker applicablegame = _owner.getWorldGame(event.getEntity().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		
if(event.getEntityType().equals(EntityType.ITEM_FRAME)){
			
			event.setCancelled(true);
			return; //item Frames are INVINCIBLE! MWAHAHA...
			
		}
		
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent edam = (EntityDamageByEntityEvent) event;

			if (edam.getEntity() instanceof Player) {

				// entity is the player that is damaged.
				Player damaged = (Player) edam.getEntity();

				if (damaged.getWorld() != watchworld)
					return;

				//

				if (edam.getDamager() instanceof Player) {
					Player Attacker = (Player) edam.getDamager();

					// if the damagee (receiver) is in a game, and the attacker
					// isn't...
					// only allow the damage if both players are participants in
					// the same game.
					if (_owner.Randomcommand.getaccepting()) {

						event.setCancelled(true);

					}
					GameTracker gameorigin = _owner.isParticipant(damaged);
					if (!gameorigin.getStillAlive().contains(Attacker)) {

						System.out
								.println(Attacker.getName()
										+ " tried to attack "
										+ damaged.getName()
										+ "Cancelled as they are not participants in the same game.");
						event.setCancelled(true);

					}

					// only Player versus Player damage is reported.
					// <Attacker> has struck you with a <Weapon> for <Damage>
					// only report when the distance between the players is more
					// than three blocks.
					if (Attacker != null) {
						if (damaged.getLocation().distance(
								Attacker.getLocation()) > 10) {
							damaged.sendMessage(BCRandomizer.Prefix + "You have been struck by "
									+ Attacker.getDisplayName() + "!");
						}
					}
				}

			}
		}
	}
	public void hidetoParticipants(Player useplayer){
		
		GameTracker gt = _owner.getPlayerGame(useplayer);
		for(Player hideto:gt.getStillAlive()){
			
			hideto.hidePlayer(useplayer);
		}
		
	}
	public static String getFriendlyNameFor(ItemStack Item) {

		if (Item == null)
			return "Nothing";
		String weapon = Item.getType().name();

		ItemNamer.load(Item);
		String gotname = ItemNamer.getName();
		if (gotname != null && gotname != "")
			weapon = gotname;
		else {
			if (RandomData.isHead(Item)) {
				weapon = RandomData.getHeadName(Item);

			} else if (RandomData.isDye(Item)) {
				weapon = RandomData.getDyeName(Item);
			}

		}
		return FriendlizeName(weapon);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		if(null!=_owner.isSpectator(event.getPlayer())){
		
			hidetoParticipants(event.getPlayer());
			
		}
		else if (_owner.Randomcommand.getaccepting()) {

			event.getPlayer().sendMessage(BCRandomizer.Prefix +
					ChatColor.YELLOW + "Welcome! A game is being prepared. ");
			event
					.getPlayer()
					.sendMessage(BCRandomizer.Prefix +
							ChatColor.YELLOW
									+ "use /joingame to join, and /spectategame to watch.");

		}

	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		GameTracker applicablegame = _owner.getWorldGame(event.getPlayer().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		if(!event.getPlayer().getWorld().equals(watchworld)) return;
		if (event.hasBlock()) {

			if (event.getClickedBlock().getType() == Material.CHEST
					|| event.getClickedBlock().getType() == Material.DISPENSER
					|| event.getClickedBlock().getType() == Material.FURNACE) {

				// only ops may look in chests when they are outside a game.
				Player p = event.getPlayer();
				if (_owner.isParticipant(p) == null) {

					// not a participant... so they had better be an op!
					if (!p.isOp()) {
						// oh... too bad.
						p.sendMessage(BCRandomizer.Prefix + ChatColor.YELLOW + "You cannot look in containers if you are not participating.");
						event.setCancelled(true);
					}

				}

			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		GameTracker applicablegame = _owner.getWorldGame(event.getEntity().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		System.out.println("Player died " + event.getEntity().getName());
		if(!event.getEntity().getWorld().equals(watchworld)) return;
		if (_Trackers == null || _Trackers.size() == 0)
			return;
		// Bukkit.broadcastMessage(event.getEntity().getName() +
		// " has been slain!");
		ChatColor usecolor = ChatColor.YELLOW;
		String usemessage = event.getDeathMessage();
		final Player dyingPlayer = event.getEntity();

		String DyingName = dyingPlayer.getName();
		String KillerName = "";
		final Player Killer = dyingPlayer.getKiller();
		if (Killer != null)
			KillerName = Killer.getName();
		if (dyingPlayer.getLastDamageCause().getCause()
				.equals(DamageCause.FALL)) {

			usemessage = dyingPlayer.getName() + " Fell "
					+ Math.round(dyingPlayer.getFallDistance())
					+ " Blocks to their Death.";

		} else if (dyingPlayer.getLastDamageCause().getCause().equals(
				DamageCause.BLOCK_EXPLOSION)) {
			String[] explosionstrings = new String[] {
					dyingPlayer + " blew up.",
					dyingPlayer + "'s body parts became shrapnel",
					dyingPlayer + " exploded.",
					dyingPlayer + " imitated a Creeper."

			};
			usemessage = RandomData.Choose(explosionstrings);

		} else if ((dyingPlayer.getLastDamageCause().getCause()
				.equals(DamageCause.DROWNING))) {
			String[] possiblemessages = new String[] { DyingName + " Drowned.",
					DyingName + " Forgot to come up for air",
					DyingName + " Sleeps with the fishes" };
			usemessage = RandomData.Choose(possiblemessages);

		} else if ((dyingPlayer.getLastDamageCause().getCause()
				.equals(DamageCause.SUICIDE))) {
			String[] possiblemessages = new String[] {
					DyingName + " ended it all",
					DyingName + " didn't even leave a suicide note",
					DyingName + " was a casualty of their own genius",
					DyingName + " killed their own dumb self",
					DyingName + " committed suicide.",
					DyingName + " cried for Celestia.",
					DyingName + " failed to survive death"

			};
			usemessage = RandomData.Choose(possiblemessages);

		} else if ((dyingPlayer.getLastDamageCause().getCause()
				.equals(DamageCause.POISON))) {
			String[] possiblemessages = new String[] {
					DyingName + " threw up and then died.",
					DyingName + " should have seen a doctor.",
					DyingName + " couldn't find the antidote.",
					DyingName + " died from poison." };
			usemessage = RandomData.Choose(possiblemessages);

		} else if ((dyingPlayer.getLastDamageCause().getCause()
				.equals(DamageCause.SUFFOCATION))) {
			String[] possiblemessages = new String[] {
					DyingName
							+ " tried to share space with a wall. It didn't work out.",
					DyingName + " suffocated in a wall.",
					DyingName + " choked on their own stupidity."

			};
			usemessage = RandomData.Choose(possiblemessages);
		} else if ((dyingPlayer.getLastDamageCause().getCause()
				.equals(DamageCause.LIGHTNING))) {

			String[] possiblemessages = new String[] {
					DyingName + " was struck down by Celestia",
					DyingName + " was struck down by Luna",
					DyingName + " was electrocuted.",
					DyingName + " acted as a lightning rod",
					DyingName + " was struck by lightning",
					DyingName + " was consumed by King Sombre"

			};

		} else if (Killer != null) {
			
			
			
			// ok, get the item the Killer has.
			if (Killer instanceof Player) {
				String weapon = getFriendlyNameFor(Killer.getItemInHand());

				String[] possiblemessages = new String[] {
						DyingName + " met there end from " + KillerName + "'s "
								+ weapon + ".",
						KillerName + " and their " + weapon + " slaughtered "
								+ DyingName,
						DyingName + " was slain by " + KillerName + " using "
								+ weapon,
						KillerName + " killed " + DyingName + " with a "
								+ weapon,
						KillerName + " gave " + DyingName
								+ " a closer look at their " + weapon };
				if (_owner.isParticipant(dyingPlayer) != null) {
					Bukkit.getPluginManager().callEvent(
							new ParticipantDeathEvent(dyingPlayer, Killer,
									weapon));
				}
				usemessage = RandomData.Choose(possiblemessages);

			}
			
			
		}
		if (_owner.isParticipant(dyingPlayer) != null) {
			Bukkit.getPluginManager().callEvent(
					new ParticipantDeathEvent(dyingPlayer, null,
							""));
		}
		usemessage = usemessage.replace(DyingName, ChatColor.RED + DyingName
				+ usecolor);
		if (KillerName.length() > 0)
			usemessage = usemessage.replace(KillerName, ChatColor.BLUE
					+ KillerName + ChatColor.YELLOW);
		usemessage = usemessage + "(" + dyingPlayer.getLastDamage()
				+ " damage)";
		BCRandomizer.emitmessage(usemessage
				+ dyingPlayer.getLastDamageCause().toString());
		event.setDeathMessage(usecolor + usemessage);

		ItemStack createdhead = RandomData.getHead(DyingName);
		Enchantment useenchant;

		EnchantmentProbability ep = new EnchantmentProbability();
		ep.Apply(createdhead, true);

		// event.getDrops().add(createdhead);
		// we need to return first, so the death message can be issued first,
		// then we will tell the gameTracker about the death.
		_owner.getServer().getScheduler().scheduleSyncDelayedTask(_owner,
				new Runnable() {

					public void run() {

						if (_Trackers != null) {
							// if there is a Tracker, notify it of the player
							// death. do this after. The tracker
							// tracks the game itself.
							// _Tracker.PlayerDeath(dyingPlayer,Killer);
							for (GameTracker Tracker : _Trackers) {
								Tracker.PlayerDeath(dyingPlayer, Killer);
							}
						}
					}
				}, 20L);

	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(!event.getPlayer().getWorld().equals(watchworld)) return;
		// System.out.println("Player moved:" + event.getPlayer().getName());
		// check the borders...
		GameTracker applicablegame = _owner.getWorldGame(event.getPlayer().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		
		
		Location BorderA = _owner.Randomcommand.BorderA;
		Location BorderB = _owner.Randomcommand.BorderB;
		double XMinimum, XMaximum, ZMinimum, ZMaximum;
		if (BorderA != null && BorderB != null) {
			//System.out.println("BorderA And BorderB are not null...");
			XMinimum = Math.min(BorderA.getX(), BorderB.getX());
			XMaximum = Math.max(BorderA.getX(), BorderB.getX());
			ZMinimum = Math.min(BorderA.getZ(), BorderB.getZ());
			ZMaximum = Math.max(BorderA.getZ(), BorderB.getZ());

		//	System.out.println("XMin:" + XMinimum + " Xmax:" + XMaximum
		//			+ " ZMin:" + ZMinimum + "ZMax:" + ZMaximum);
			boolean boinged = false;
			Player p = event.getPlayer();
			if (null != _owner.isParticipant(p)) {
			//	System.out.println("moved player is a participant.");
				Location ploc = p.getLocation();
				if (ploc.getX() < XMinimum) {
			//		System.out.println("lower than XMin");

					p.setVelocity(new Vector(1 + Math.abs(p.getVelocity()
							.getX() + 1), p.getVelocity().getY(), p
							.getVelocity().getZ()));

					p.teleport(new Location(p.getWorld(), XMinimum + 1, ploc
							.getY(), ploc.getZ()));
					boinged=true;
				}
				if (ploc.getZ() < ZMinimum) {
				//	System.out.println("lower than ZMin");

					p.setVelocity(new Vector(p.getVelocity().getX(), p
							.getVelocity().getY(), 1 + Math.abs(p.getVelocity()
							.getZ())));

					p.teleport(new Location(p.getWorld(), ploc.getX(), ploc
							.getY(), ZMinimum + 1));
					boinged=true;
				}

				if (ploc.getX() > XMaximum) {
			//		System.out.println("higher than XMax");
					p.setVelocity(new Vector(
							-Math.abs(p.getVelocity().getX()) - 1, p
									.getVelocity().getY(), p.getVelocity()
									.getZ()));

					p.teleport(new Location(p.getWorld(), XMaximum - 1, ploc
							.getY(), ploc.getZ()));
					boinged=true;
				}
				if (ploc.getZ() > ZMaximum) {
			//		System.out.println("higher than ZMax");

					p.setVelocity(new Vector(p.getVelocity().getX(), p
							.getVelocity().getY(), -Math.abs(p.getVelocity()
							.getZ()) - 1));

					p.teleport(new Location(p.getWorld(), ploc.getX(), ploc
							.getY(), ZMaximum - 1));
					boinged=true;
				}
				if(boinged) p.sendMessage(BCRandomizer.Prefix + ChatColor.RED + "BOING!" + ChatColor.YELLOW + " You hit the arena border!");
			}
		}

	}

	private Material KeySpotMaterial = Material.GOLD_BLOCK;
	LinkedList<Location> setAirLocations = new LinkedList<Location>();

	@EventHandler
	public void onBlockPlacement(BlockPlaceEvent event) {
		
		GameTracker applicablegame = _owner.getWorldGame(event.getPlayer().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		
		if(!event.getPlayer().getWorld().equals(watchworld)) return;
		System.out.println("block placed by " + event.getPlayer().getName());

		// //change: no block changes can be made by non ops.
		if (event.getBlockAgainst().getType().equals(KeySpotMaterial)
				&& event.getItemInHand().getType()
						.equals(Material.STONE_BUTTON)) {
			event.getPlayer().sendMessage(BCRandomizer.Prefix + ChatColor.GOLD + "Key Placed!");
			event.getPlayer().playEffect(event.getPlayer().getLocation(),
					Effect.EXTINGUISH, 1);
			// save this Location to reset it to air.
			setAirLocations.add(event.getBlock().getLocation());
			return; // allow placement of buttons on gold blocks.

		} else if (event.getItemInHand().getType()
				.equals(Material.STONE_BUTTON)) {
			
			if(null!=_owner.isParticipant(event.getPlayer())){
			
			event.getPlayer().sendMessage(BCRandomizer.Prefix +
					ChatColor.WHITE + "This is a Key! Place it on a "
							+ ChatColor.GOLD
							+ FriendlizeName(KeySpotMaterial.name())
							+ ChatColor.WHITE + " To find treasures!");
			event.setCancelled(true);
			return;
			}
			
		}

		if (!event.getPlayer().isOp()) {
			event.getPlayer().sendMessage(BCRandomizer.Prefix + "You cannot place blocks.");
			event.setCancelled(true);
			return;
		}
		if (null != _owner.isParticipant(event.getPlayer())) {
			event.getPlayer().sendMessage(BCRandomizer.Prefix +
					"You cannot place blocks when in an event, even as an Op.");
			event.setCancelled(true);
		}

	}
	@EventHandler
	public void onSignChange(SignChangeEvent event){
		GameTracker applicablegame = _owner.getWorldGame(event.getPlayer().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		
		if(null!=_owner.getGame(event.getPlayer())){
			event.setCancelled(false);
			event.getPlayer().sendMessage("You cannot place signs.");
			
			
			
		}
		
		
	}
	@EventHandler
	public void onBlockIgnore(BlockIgniteEvent event) {
		if(event.getPlayer()==null) {
			
			event.setCancelled(true);
			
		}
		else if(null!=_owner.getGame(event.getPlayer())){
		event.setCancelled(true);
		event.getPlayer().sendMessage("You cannot ignite blocks");
		}
		
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		// can't break blocks if participating.
		GameTracker applicablegame = _owner.getWorldGame(event.getPlayer().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		if(!event.getPlayer().getWorld().equals(watchworld)) return;
		if (!event.getPlayer().isOp()) {
			event.getPlayer().sendMessage(BCRandomizer.Prefix + "You cannot break blocks.");
			event.setCancelled(true);
			return;
		}
		if (null != _owner.isParticipant(event.getPlayer())) {
			event.getPlayer().sendMessage(BCRandomizer.Prefix +
					"You cannot break blocks when in an event, even as an Op.");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event){
		
		GameTracker applicablegame = _owner.getWorldGame(event.getEntity().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		Location BorderA = _owner.Randomcommand.BorderA;
		Location BorderB = _owner.Randomcommand.BorderB;
		if(BorderA!=null && BorderB!=null){
		//if there are borders set...
			float XMinimum = (float) Math.min(BorderA.getX(), BorderB.getX());
			float XMaximum = (float) Math.max(BorderA.getX(), BorderB.getX());
			float ZMinimum = (float) Math.min(BorderA.getZ(), BorderB.getZ());
			float ZMaximum = (float) Math.max(BorderA.getZ(), BorderB.getZ());
			Location uselocation = event.getEntity().getLocation();
			//check X Coordinate.
			if(uselocation.getX() < XMinimum || uselocation.getX() > XMaximum){
				
				event.setCancelled(true);
				
			}
			else if(uselocation.getZ() < ZMinimum || uselocation.getZ() > ZMaximum){
				
				event.setCancelled(true);
				
			}
			
			
			
			
			
		}
		
		
		
		boolean validworld=event.getEntity().getWorld().equals(watchworld);
		//System.out.println("onCreateSpawn: EntityWorld=" + event.getEntity().getWorld().getName() + " Valid=" + validworld);
		
		if(!validworld) return;
		//if we are in mob battle/fight mode, randomize it. otherwise, Cancel it entirely.
		
		
		
		//System.out.println("Randomizing creature " + event.getEntity().getClass().getName());
		SpawnerRandomizer sr = new SpawnerRandomizer(_owner);
		if(RandomData.rgen.nextFloat() < 0.6f)
			event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 32767, RandomData.rgen.nextInt(3)));
		if(RandomData.rgen.nextFloat() < 0.4f)
			event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 32767, RandomData.rgen.nextInt(3)));
		if(RandomData.rgen.nextFloat() < 0.3f)
		{
			event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,32767,RandomData.rgen.nextInt(3)));
		if(RandomData.rgen.nextFloat() < 0.33f)
			event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,32767,RandomData.rgen.nextInt(1)));
		}
		sr.RandomizeEntity(event.getEntity());
		
		
	}
	
	
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		GameTracker applicablegame = _owner.getWorldGame(event.getPlayer().getWorld());
		if(!_Trackers.contains(applicablegame)|| applicablegame==null) return;
		if (_owner.Randomcommand.getaccepting()) {
			_owner.Randomcommand.getjoinedplayers().remove(event.getPlayer());

		}

		else if (_Trackers != null)
			for (GameTracker Tracker : _Trackers) {
				BCRandomizer.clearPlayerInventory(event.getPlayer());

				Tracker.PlayerDeath(event.getPlayer(), null);
			}

	}

	private HashMap<World, LinkedList<CachedFrameData>> CachedData = new HashMap<World, LinkedList<CachedFrameData>>();

	private int MidnighttaskID = 0;
	public void onGameStart(final GameStartEvent event) {

		if(_owner.Randomcommand.getMobArenaMode()){
		MidnighttaskID = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(_owner, new Runnable() {
			
			public void run() {
				
				event.getParticipants().get(0).getWorld().setTime(18000);
				
			}
			
			
			
			
		}, 60,60);
		}
		System.out.println("Game Started");
		World Worldgrab = event.getParticipants().get(0).getWorld();
		// record itemframe locations.
		if (!CachedData.containsKey(Worldgrab))
			CachedData.put(Worldgrab, new LinkedList<CachedFrameData>());
		LinkedList<CachedFrameData> datagrab = CachedData.get(Worldgrab);
		int framescount = 0;
		for (Chunk iteratechunk : Worldgrab.getLoadedChunks()) {

			for (Entity iterateentity : iteratechunk.getEntities()) {

				if (iterateentity instanceof ItemFrame) {

					ItemFrame g;
					ItemFrame foundframe = (ItemFrame) iterateentity;
					datagrab.add(new CachedFrameData(foundframe));
					framescount++;
				}

			}

		}
		System.out.println("Cached " + framescount + " Item frames.");

	}

	public void onGameEnd(GameEndEvent event) {
		// re-add the item frames.
		if(_owner.Randomcommand.getMobArenaMode()){
			Bukkit.getServer().getScheduler().cancelTask(MidnighttaskID);
			MidnighttaskID=0;
		}
		System.out.println("Game End");
		World worldevent = event.getAllParticipants().get(0).getWorld();
		_Trackers.remove(event.getTracker());
		_owner.ActiveGames.remove(event.getTracker());
		ChestRandomizer.resetStoredInventories();

		if (!CachedData.containsKey(worldevent)) {
			System.out
					.println("SurvivalChests:Key not found for world attempting to revive itemframes...");
			return; // hmm, curious.
		}
		LinkedList<CachedFrameData> framedata = CachedData.get(worldevent);

		System.out.println("restoring " + repopulateFrames.size()
				+ " Item Frame contents...");
		for (ItemFrame iterateframe : repopulateFrames.keySet()) {

			iterateframe.setItem(repopulateFrames.get(iterateframe));

		}

		// find them all!
		for (Chunk loopchunk : event.getAllParticipants().get(0).getWorld()
				.getLoadedChunks()) {

			for (int x = 0; x < 15; x++) {

				for (int y = 0; y < 255; y++) {

					for (int z = 0; z < 15; z++) {

						org.bukkit.block.Block acquired = loopchunk.getBlock(x,
								y, z);
						if (acquired.getType().equals(Material.GOLD_BLOCK)) {

							World owner = acquired.getWorld();

							// check +1 -1 x and +1 -1 Z combinations, look for
							// existing air blocks.

							LinkedList<Location> CheckLocations = new LinkedList<Location>();
							CheckLocations
									.add(new Location(owner, x - 1, y, z));
							CheckLocations
									.add(new Location(owner, x, y, z - 1));
							CheckLocations
									.add(new Location(owner, x, y, z + 1));
							CheckLocations
									.add(new Location(owner, x + 1, y, z));
							for (Location checloc : CheckLocations) {

								if (checloc.getBlock().getType().equals(
										Material.AIR)) {

									setAirLocations.add(checloc);

								}

							}

						}

					}

				}

			}

		}

		for (Location setair : setAirLocations) {

			setair.getBlock().setType(Material.AIR);

		}
		setAirLocations = new LinkedList<Location>();
		
	}

	public LinkedList<GameTracker> getTrackers() {
		// TODO Auto-generated method stub
		return _Trackers;
	}

}