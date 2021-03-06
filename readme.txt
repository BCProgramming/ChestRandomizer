BASeCamp 'Survival Chests' Chest Randomizer plugin
Version 2.0, Written for Bukkit API 1.5. (Should work on 1.4.7, as well). Requires WorldEdit for Arena Generation features.
BASeCamp Survival Chests is a plugin designed to help in the organization and management of survival-based, Minecraft gameplay. Features include:


-Chest Randomization and respawn/repopulation
-Simple Game Tracking
-Death Message Modifications
-Mob Fight/Mob Arena Mode.
-Arena Generation, which includes inserting schematics into the generated arena, with a bedrock border.


Chest Randomization

The Chest Randomization Feature is one of the more powerful and interesting features provided by the plugin. It can be customized. To customize it, open the plugin .jar file and extract survivalchests.cfg and place it in the \plugins\BCRandomizer file of your bukkit installation. Make the requisite changes in the file as desired.

Rnadomization Customization:

The configuration is relatively simple, consisting mostly of lines of comma-delimited values indicating each item. Here is one such line from the default configuration:

!Diamond %CLEVERSWORDNAME%,2,276,0,0.1,0.9,1,1,,NONE,900,SHARPNESS,100,FIREASPECT,50,KNOCKBACK,100

Name,Weight,ItemID,Data,LowDurability,HighDurability,MinStack,MaxStack,Lore,EnchantmentName, EnchantmentProbability...

The first part indicates the name of this item. The exclamation mark tells the plugin that the generated item should share the name given. %CLEVERSWORDNAME% uses a random name generator to create a name for this sword; some examples of this case could be "Katana of Brisk Sneezing" or "Blade of Extreme Selling". Implemented "clever" names include:

%CLEVERAXENAME%
%CLEVERBOOKNAME%
%CLEVERHOENAME%
%CLEVERSHOVELNAME%
%CLEVERSHEARSNAME%
%CLEVERSIGNNAME%
%CLEVERHATNAME%
%CLEVERCHESTPLATENAME%
%CLEVERLEGGINGSNAME%
%CLEVERBOOTSNAME%
%CLEVERSWORDNAME%
%CLEVERBOWNAME%


The second entry is the probability weight of this item. The plugin adds up the weights of all the items in the file, and the percentage that this value is of that total is the percentage chance that this item will spawn. In this case, the item is very rare (perhaps even too rare) and will not generate very often, because most entries in the default file are 20's, 50's, and 100's. The ItemID is the... well, ItemID, of the item. the Diamond Sword is ItemID 276, so that is what is put here. Next comes the Data value of the item. For items such as Dyes (which you might want to use for some reason, I'm not going to judge you) this item will determine some qualities. Most items don't use it, but if you happen to want to create an item that does, this is available. The next item is the minimum durability, followed by the maximum durability. the generator will select a value between the given percentages of the created items total MaxDurability and give the created item the resulting value. In this case, the item generated will have a durability above 10% but no more than 90% when inserted into a chest or other inventory. The next two items are the minimum and maximum stack sizes for the generated Item Stack. This has 1 for both, meaning you won't get stacks of diamond swords, which is probably for the best (and definitely why me in the past chose that). Next, the blank item is the optional "lore" for the item. This has some special rules; you can indicate a Lore to use here directly. However, if the item name has "of" in the title, (which most of  the built-in "clever" name generators will insert) than there is a chance the plugin will give the item a random lore value itself.

The items after this are Enchantments followed by their probability weighting. Valid Enchantment names the plugin recognizes as of Plugin Version 2.0 are NONE, FLAME, POWER, INFINITY, PUNCH, SHARPNESS,ARTHROPODS,SMITE,EFFICIENCY,UNBREAKING,FIREASPECT,KNOCKBACK,FORTUNE,LOOTING,RESPIRATION,PROTECTION,BLASTPROTECTION,FEATHERFALLING,FIREPROTECTION,PROJECTILEPROTECTION,SILKTOUCH,AQUAAFFINITY, and THORNS. A enchantment name can be prefixed with a exclamation mark; if this is done then the item will be forcibly given that enchantment with the level that will be used in lieu of a probability weighting.

POTIONS:

Potions are another item that can be generated. The Syntax is similar to the standard Item line, but changes the meanings of some of the entries. All Potion lines start with POTION:

POTION:NAME,PROBABILITY,ID,<unused>,Duration,Level,Splash

The first is, self evidently, the name of the potion. Valid Potion names as of Plugin version 2.0 are BLINDNESS,CONFUSION,RESISTANCE,HASTE,FIRERESIST,DAMAGE,HEAL,HUNGER,STRENGTH,INVISIBILITY,JUMP,NIGHTVISION,POISON,REGENERATION,SLOWNESS,SPEED,RESPIRATION, and WITHER.

The second is the standard Probability; this is used the same as the aforementioned probability for other items. Next is the ID. This will always be 373. Current implementations force all Potions to use Item ID 373, but you can never know how the future will hold so it's best to be explicit and specify 373 in your configurations. The next item is unused. Duration is the length of the potion effect, if applicable, in seconds. The next two are the level of the potion effect and whether the potion is a splash potion.

One thing notable with potions is that the plugin will randomly add up to 4 additional effects to any potion, which could be any of the valid effects. This can create negative potions (like poison) that have powerful positive effects (regeneration, speed, jump, etc) as well as positive potions that have negative effects. This feature was added to make the game more hectic and attempt to level the playing field by making it more difficult to quickly decide which items to take and which ones to leave.

Items can have special "prefixes" added to them. Prefixes are when the line starts with a prefix and a colon. valid prefixes include STATIC:, SUPERENCHANT: and SINGLEENCHANT:

STATIC: has a special effect; instead of chest population choosing items when it populates all the chests, static items instead choose a random chest to be placed into after the main population run. This means that items prefixed with STATIC: will always be present in a chest somewhere, but will only ever appear once. SUPERENCHANT cranks up the enchantment counts and probabilities for the enchantment selection logic, which usually results in more enchantments as well as more powerful enchantments. SINGLEENCHANT: will only allow one enchantment to be applied Total.


This is similar to many existing Mob Arena modes, but is designed to be used in a designated world. Usually, maps that work with the Survival-based gameplay work just as well in Mob Arena Mode, as well. Essentially, this mode pits players against each other, not in open combat, but in a competition to get the most points. Points are awarded by killing mobs, which spawn with various potion buffs, Equipment (the same equipment that can spawn in chests, in fact), and mob-specific, randomized variations, such as baby mobs, charged creepers, and wither skeletons. Mob spawn rates are cranked up and the mob cap is as well. These monsters are no pushover; running outside, even with full armour, is still a dangerous proposition.



Permissions nodes:

chestrandomizer.worldname.command

available commands are repopchests, preparegame, startgame,joingame,spectategame,arenaborder1, arenaborder2, clearborder, mobmode,mobsweeper, and mobtimeout
-repopchests repopulates chests. This is also issued automatically when games start.
-preparegame prepares a new game.
-joingame is issued by players after an admin issues /preparegame to prepare the game (or anybody with perms). It will join that game event. The player will be teleported to the appropriate world at the location the player who issued /preparegame was when they issued it.
-spectategame same as above, but the players will be spectators. This is supposed to allow spectators to be invisible and observe the match without interfering with or distracting players, but I have had reports of some minor issues in this regard that I have not yet been able to investigate.
-mobmode toggles whether a game that is prepared will be a mob arena game or a standard PvP arena game.
-mobtimeout is used for PvP matches. provided with an argument, it specifies the number of seconds before the mob spawning algorithm will be re-enabled. The mob spawns will be the same as those you would find in the Mob Arena Mode, but currently the caps and spawnrate are not changed.
-mobsweeper so-named since it is designed to "sweep out" players that are camping. Force-enables the Mob spawning algorithm described above.
-repoptimeout sets the timeout value for opened chests.
-newarena <name> <XSize> <ZSize> <FeatureFactor> generates a new arena and teleports the invoking player to it when it initially generates.
-borders shows you the currently set borders. /newarena will initialize the border values to the size of the created arena.
-arenaborder1 and arenaborder2 set the corners of the border for a rectangular region.
-setlives sets the number of lives each player has for both PvP as well as MobArena Matches. 0 indicates infinite lives which is a flag for a continuous game.



Game Tracking Specifics:

Although originally outside the original scope of the plugin, I added the capability to track games to the plugin. This is done by somebody with permission to use the preparegame command. the preparegame command prepares a game to start; the command accepts a name of the world in which to start the event or if not provided it will use the world of the issuing player. The location of the player issuing the Preparegame command will be recorded as well.

At this point,it is now announced and players that wish to participate (on the entire server) to use /joingame. Those who wish to spectate use /spectategame.

If the player is outside the world in which the event occurs, in both cases they will be teleported to the location stored when the game was prepared.

when /startgame is issued in PvP mode, (again, by somebody with the proper permissions), it can either be provided a time interval to delay (default is 30 seconds) in which all PvP combat will be disabled. All players will be put into adventure mode and their health and hunger bars will be refilled. After the time has expired, players will be incurred with 5 seconds of blindness (this is to help prevent kills immediately when PvP is re-enabled).

When a game starts, Chests are randomized. Chests/Furnaces/Dispensers with Wool beneath are "static". These will not have randomized contents. However, their contents at the start of the game will be stored and reset when the game is over. Item frames are dealt with similarly, but have special logic described below.


Item Frames

Item Frames are often used for decorative purposes. By default in Minecraft, hitting a Item Frame would destroy the frame and drop both the item frame and the contents of the item frame on the ground. The plugin prevents this. When a game starts, it saves the current contents of all Item Frames in the world. When a player hits an Item frame, they are given the contents of that item frame. For decorative items often used in item frames, such as trapdoors and anvils, this is of dubious purpose. However this can be useful for Swords or other items, and is a good way to "hide" good weapons in plain sight, by making them belong; such as a powerful sword in a mansion parlour as a mantlepiece. The plugin also takes special care for buttons.

As it is currently written, Buttons are the only item that can be placed. Buttons can only be placed on Gold Blocks. This requires the map to either be modified or designed with this plugin's constraints in mind, if it includes this sort of interaction. When a game is reset, the contents of static chests are restored and all item frames have their original contents restored as well. Buttons that were placed while the game was in progress are removed as well.

Since preparegame sets the spawn, it's worth noting that if you are in creative mode you ought to make sure you are a reasonable height, otherwise players will fall and die the moment they try to join or spectate the game.



The Head Randomizing line is nice and simple:

HEAD:BC_Programming,1200

Format is: 

HEAD:PlayerName,Weight

Which is self-explanatory.

The actual "game" play has two modes. The standard Player Versus Player combat mode, which pits players against one another in a pre-designed arena (arena not included). This is designed to have support for player spectating- players that are spectating a game are made invisible to participants, and cannot damage players. An important consideration however is that they currently still have a few capabilities that could be exploited, such as being able to pick up items off the ground or serve as an "invisible shield" and block arrows.  

When a game starts, players are given a long lasting "HUNGER" Effect, which lasts 18 minutes. This is something that needs to be closely monitored. If arena borders are set, Players are randomly distributed throughout the Arena, placing them on the highest solid block at the given location, so (ideally) tall structures should have some way down. This can also be used for "bonus" chests that lucky players might find, involving platforms above the main arena containing treasure. (placed on Wool blocks, of course). etc.

Another mode of Play pits players against each other, but with PvP off. "How does that work?" you ask? Well, the players fight not each other, but hordes of heavily armoured monsters, with powerful enchanted equipment. Each mob they kill gives them points based on many factors such as the type of mob as well as the equipment and any active potion effects on the mob and even the player. The game starts in a similar way to the standard PvP game. However the game will also always remain at or near midnight, allowing mobs to spawn. Mobs that spawn are "randomized", with equipment as well as various other things, such as skeletons becoming wither skeletons, and Creepers being powered creepers. The Spawning cap is increased, and the number of mob spawns per tick is increased as well, and mobs will spawn even closer to the player, (I've had mobs spawn 0 blocks in front of me). Obvious focus for players in this mode should be to first armour up and find useful equipment to defend themselves; the number of mobs is quite astounding- and mobs do much more damage than before (Damage is also boosted heavily), so getting a weapon and/or armour should be a top priority. Food is important as well, since you are hit with the same hunger effect in this mode as you are in PvP combat. Note that even with Full armour, groups of mobs can be overwhelming because they are usually heavily armoured and do MUCH more damage than before even without a weapon. Just remember that mobs spawn very quickly, and the equipment mobs can spawn with include all the same weapons and armour defined as being possible to appear in chests. So basically, players and mobs are evenly matched; however, Mobs will also be given random, long-lasting effects, such as speed, regeneration, and even invisibility. Recent updates (1.4.6?) to the Mob AI have made things even more interesting; I've seen mobs jump off 8-story buildings to get to me, apparently aware that either their potion effects or their feather falling boots will protect them on the fall. adding a new challenge. GamePlay in this mode feels like a "zombie apocalypse"; I've been cornered in open areas at a dead end, with monsters pouring in the open side for several minutes at a time. Since much of my testing is solo, this mode is the most well-tested, particularly for a single player, which is still quite an interesting game nonetheless.


Chest repopulation:

one of the features I have added with the release for Bukkit 1.5 include a feature to repopulate chests:

- when the game starts, all chests are repopulated as usual.
-when a player closes a chest or other inventory and a configurable delay elapses, that chest will be repopulated.
- if another player opens the chest, the timeout will be cancelled. When the player closes it again a new one is created.

the desired effect is that only chests accessed by players will timeout, but not if somebody else accesses it in the meantime.


Arena Generation:

2.0 adds a new feature: creating new Arenas. the /newarena command is used for this:

/newarena <name> <XSize> <ZSize> <FeatureFactor>

name will be the name of the new world that is created.
XSize and ZSize are the size of the arena. The plugin will encompass this border with bedrock walls.
FeatureFactor is a feature generation factor to use. The plugin will use Schematics from the ./plugins/BCRandomizer/Schematics folder. If this folder is not found, it will be populated from the jar file.

the Plugin uses the WorldEdit API to import schematics into the generated arena. an options.ini file, like that provided by default from the jar, can be used to change a few things about how a given schematic will be inserted. the ini is a standard INI file. Each section corresponds to the base name of a schematic. This is the default options.ini at the time of this writing (this will certainly change as I make new ones):

[small_bunker]
heightoffset=-4
[desert_tomb]
heightoffset=-10
includebiomes=desert
[pillbox]
heightoffset=-3
[forest_sniper]
heightoffset=-1
forceair=false

each section is named for a .schematic file; for example, the default setup contains a forest_sniper.schematic file. The options are within the [forest_sniper] section. heightoffset changes the Y offset the schematic from ground level. currently ground level is chosen from one corner, but this will likely be changed to provide a smoother insertion appearance. Underground items such as the desert tomb have larger, negative height offsets, to make them under the ground. forceair is used to copy air blocks. forest_sniper uses this because it has a bottom layer with two stone slabs, but is intended to not replace the ground it is placed on with air. 

When imported into a arena the schematics are rotated at random and inserted. Currently they will avoid intersecting the X/Z placement of any other schematic. This feature is still under heavy development to try to get the best 'default' arenas, as well as providing options.

Once an arena is generated (this will take some time and appears to "hang" the server while the world is initially generated), the player that issued the newarena command is teleported there. The plugin will also set the current border to the borders of the created arena. One caveat of this is that that player will not be teleported back to their previous position when the game concludes. They can use either multiverse commands (if the plugin is installed) or they can use the mwtp command provided by this plugin, which will teleport the player to the spawn point of a world that is specified. As with all SurvivalChests commands, the issuing player must have permissions to use the command.

Note: Features that I'm still deciding how to implement and how they should work:

Multiple Lives has some unimplemented features and some oddness that makes it difficult to use for appropriate gameplay in most cases. It will still work fine for PvP matches, but Mob Fight matches will have weird scoring issues.

GITHUB: (It's messy, I apologize in advance for anybody that looks at it...)

https://github.com/BCProgramming/ChestRandomizer
