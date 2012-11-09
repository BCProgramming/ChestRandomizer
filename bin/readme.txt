BASeCamp 'Survival Chests' Chest Randomizer plugin
Version 1.0 (initial release)


Permissions nodes:

chestrandomizer.worldname.command

available commands are repopchests, preparegame, startgame,joingame and spectategame

Game Tracking:

Although outside the original scope of the plugin, I added the capability to track games to the plugin. This is done by somebody with permission to use the preparegame command. the preparegame command prepares a game to start; the command accepts a name of the world in which to start the event or if not provided it will use the world of the issuing player.

At this point,it is now announced and players that wish to participate (on the entire server) to use /joingame. Those who wish to spectate use /spectategame.

If the player is outside the world in which the event occurs, in both cases they will be teleported to the spawn location of the world in which the event occurs.

when /startgame is issued (again, by somebody with the proper permissions), it can either be provided a time interval to delay (default is 30 seconds) in which all PvP combat will be disabled. All players will be put into adventure mode and their health and hunger bars will be refilled. After the time has expired, players will be incurred with 5 seconds of blindness (this is to help prevent kills immediately when PvP is re-enabled).


Features:

Randomizes all Chests in a world.
Items can have random enchantments and randomized names (though the latter is limited to the built-in name randomizer).

Chests that are on Wool Blocks will NOT have their contents randomized.

Randomization data is stored in the "BCRandomizer" folder of your plugin directory, inside a file named "hungergames.cfg"


There are a few "Types" of lines in the file: these come in the form of "Standard" lines, as well as lines for creating Potions and Player Heads.

The following is an example line (standard) from this file (A default hungergames.cfg file is provided in the zip alongside the jar and this file):

Stone Sword,50,272,0,0,0,1,1,,NONE,700,SHARPNESS,100,FIREASPECT,50


The format here is:
Name,Weight,TypeID,Data,MinDamage,MaxDamage,MinCount,MaxCount,Lore, EnchantmentName,EnchantmentWeight...


The first element is the Name. This is normally ignored (as in, it is not applied to the Item through NBT Tags). If you want to give the item a special name, precede that name with !. There are also special variables you can use in conjunction with !, Currently implemented name randomizers include:

%CLEVERHATNAME%,%CLEVERCHESTPLATENAME%,%CLEVERLEGGINGSNAME%,%CLEVERBOOTSNAME%,%CLEVERSWORDNAME%,%CLEVERBOWNAME%

Which create random names suitable (or, well, mostly suitable) for each item type in the name.

Weight assigns the "weight" of this item. The higher this value is compared to the other values in the file, the more likely this item is to appear. A item with a weight of 5, for example, is half as likely to appear as one with a weight of 10.

The next item is the TypeID. This is the typeID of the item in question. In the above, we use 272 because tgat us the ID of the stone Sword.

MinDamage and MaxDamage: When the item is created, a random value is chosen between these values, and set as the items Damage value.

MinCount and MaxCount: When the item is created, a random value is chosen between these values, and the size of the stack of items is set to the resulting value. For armours and most items and tools, it makes sense to have both values set to 1.

Lore: This sets the lore text for the item. Normally, this will be empty, but it can be changed. Note that all the items generated from the line will have this lore text. In the current implementation there is no generator for random lore.


Enchantments are listed at the end. All remaining entries on the line are parsed as Enchantment Names followed by their weight. If the enchantment name starts with a exclamation mark, than that is parsed as being a enchantment that is always given to the resulting item, at the level of the following item.


For creation potions, there is a similar but slightly different syntax to the line:

POTION:SLOWNESS,10,373,8194,0,0,0

the format is:
POTION:NAME,Weight,ID,Damage,Extended,Leveled,Splash

NAME is the more significant value here. Supported items include:

FIRERESIST,DAMAGE,HEAL,POISON,REGENERATION,SLOWNESS,SPEED,STRENGTH,WATER,WEAKNESS

Invisibility is unfortunately not available yet.

ID and Damage are both unused, and will be 373 and the appropriate damage value for the resulting potion in the created item. Extended indicates when the potion has a extended length. Leveled indicates whether it is a Level II potion, and Splash is whether it is a splash potion.

The Head Randomizing line is nice and simple:

HEAD:BC_Programming,1200

Format is: 

HEAD:PlayerName,Weight

Which is self-explanatory.
