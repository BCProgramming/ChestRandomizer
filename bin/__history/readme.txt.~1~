BASeCamp 'Survival Chests' Chest Randomizer plugin
Version 1.0 (initial release)
BASeCamp Survival Chests is a plugin designed to help in the organization and management of survival-based, Minecraft gameplay. Features include:

-Chest Randomization. Chests will be randomized Either using a custom-made or edited configuration file in \plugins\BCRandomizer, or by using a built-in setup. Features of this randomizer include weighted probabilities on items, support for enchantments, names, and Lore, including the generation of randomized names for tools and equipment as well as a few other items. It also supports the definition of "static" items, which instead of being a weighted probability on each chest will instead choose a random chest for each and put it there (meaning you can have only one item placed somewhere, but always have it exist). It also supports potions as well as player heads.
-Game Tracking Players will join a game that is being prepared. After the game starts, the remaining players will be tracked, and when there is only one player left standing, that player is declared the winner.
-Death Message Modification Death Messages are changed to include more information. This includes the weapon the attacker was using (the specific name) as well as the amount of damage the death blow inflicted.
-Mob Arena mode

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
[/list]

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


Features of the randomizer
-Randomizes all Chests in a world.
-Items can have random enchantments and randomized names.
-Chests that are on Wool Blocks will NOT have their contents randomized.


Randomization data is stored in the "BCRandomizer" folder of your plugin directory, inside a file named "survivalchests.cfg". This file is not created by default, instead it defaults to using the file inside the plugin .jar. For editing, extract that file to the correct location to get a "default" template upon which to make your changes.

The data for Randomized Names can be changed by creating a "namegen.ini" file in the BCRandomizer folder. Each line of this file can be a comment, starting with //, or an entry. An entry is in the form:

nametype=name

where nametype can be one of Noun,Verb,Adjective,Sword,Helmet,Chestplate,Leggings,Boots,Bow,Pickaxe,Shovel,Hoe,Axe,MobName,MobTitle. The value can also be a comma-delimited list, for example the lines:

Sword=Cutter
Sword=Greatsword

are the equivalent of this one line:

Sword=Cutter,Greatsword

Items are added to the existing list of words. If you want to clear the list and only use your own name, use "!clear!" as a entry; for example:

Axe=!clear!

will clear that entire list.


For the actual Item Randomizer, There are a few "Types" of lines in the file: these come in the form of "Standard" lines, as well as lines for creating Potions and Player Heads.

The following is an example line (standard) from this file (A default survivalchests.cfg file is provided in the zip alongside the jar and this file):

Stone Sword,50,272,0,0,0,1,1,,NONE,700,SHARPNESS,100,FIREASPECT,50


The format here is:
Name,Weight,TypeID,Data,MinDamage,MaxDamage,MinCount,MaxCount,Lore, EnchantmentName,EnchantmentWeight...



The first element is the Name. This is normally ignored (as in, it is not applied to the Item as it's name). If you want to give the item a special name, precede that name with !. There are also special variables you can use in conjunction with !, Currently implemented name randomizers include:
%CLEVERHATNAME%
%CLEVERCHESTPLATENAME%
%CLEVERLEGGINGSNAME%
%CLEVERBOOTSNAME%
%CLEVERSWORDNAME%
%CLEVERBOWNAME%
%CLEVERSHEARSNAME%
Which create random names suitable (or, well, mostly suitable) for each specified item type. 

Weight assigns the "weight" of this item. The higher this value is compared to the other values in the file, the more likely this item is to appear. A item with a weight of 5, for example, is half as likely to appear as one with a weight of 10.

The next item is the TypeID. This is the typeID of the item in question. In the above, we use 272 because that is the ID of the stone Sword.

MinDamage and MaxDamage: When the item is created, a random value is chosen between these values, and set as the items Damage value.

MinCount and MaxCount: When the item is created, a random value is chosen between these values, and the size of the stack of items is set to the resulting value. For armours and most items and tools, it makes sense to have both values set to 1. If you want the possibility of "disjoint" values- for example, if you want it to be only possible to have 1, ot 64 of an item, you will need to use two Lines which differ only in their min and max count.

Lore: This sets the lore text for the item. Normally, this will be empty, but it can be changed. Note that all the items generated from the line will have this lore text. Some item types (helmet, shears, etc.) will be given random lore (about a 10% chance) if this item is empty.

Enchantments are listed at the end of the line. All remaining entries on the line are parsed as Enchantment Names followed by their weight. If the enchantment name starts with a exclamation mark, than that is parsed as being a enchantment that is always given to the resulting item, at the level of the following item. For example:


STATIC:SUPERENCHANT:!Femur of Deliverance,700,352,0,0,0,1,1,§r§cDelivers Justice. Also a good fertilizer,!SHARPNESS,6,!FIREASPECT,3,SHARPNESS,40,!SMITE,10

This has the "STATIC:" modifier, meaning the plugin will only add one of this item to the game. SUPERENCHANT: means that enchantments will be "boosted" and more powerful, and since the given name starts with an Exclamation mark, it will be used to name the item. the probability elements here are unused, ID is 352, and the stack sizes will always be 1 (range from 1 to 1). The Lore data is "Delivers Justice, Also a good fertilizer". And items will start with the enchantments of Sharpness VI, Fire Aspect III, and Smite X. the additional Sharpness entry is used with the default enchantment code, and means there is a probability that the item will have a higher sharpness enchantment than VI.


For creation potions, there is a similar but slightly different syntax to the line:

POTION:SLOWNESS,10,373,8194,0,0,0

the format is:
POTION:NAME,Weight,ID,Damage,Extended,Leveled,Splash

NAME is the more significant value here. Supported items include:

FIRERESIST,DAMAGE,HEAL,POISON,REGENERATION,SLOWNESS,SPEED,STRENGTH,WATER,WEAKNESS,INVISIBILITY

ID and Damage are both unused, and will be 373 and the appropriate damage value for the resulting potion in the created item. Extended indicates when the potion has a extended length. Leveled indicates whether it is a Level II potion, and Splash is whether it is a splash potion.

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

Another idea I'm toying with is the plugin showing the recent viewers of that chest, as a way to track player movement depending on the map in question. 



GITHUB: (It's messy, I apologize in advance for anybody that looks at it...)

https://github.com/BCProgramming/ChestRandomizer
