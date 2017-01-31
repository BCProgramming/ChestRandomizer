package com.BASeCamp.SurvivalChests;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;



import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mule;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Spider;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;

//this class needs a LOT of work. (different weights, give mobs special abilities, etc.)
/**
 * class for Randomizing Spawners as well as Randomizing Monsters in various
 * ways.
 */
public class SpawnerRandomizer {

	LinkedList<SpawnerRandomData> randomdata = null;

	private BCRandomizer _owner;

	public BCRandomizer getOwner() {
		return _owner;
	}

	public SpawnerRandomizer(BCRandomizer owner) {
		_owner = owner;
	}

	private void reload() {

		randomdata = new LinkedList<SpawnerRandomData>();
		Scanner readfrom = _owner.acquireStream("spawners.cfg");
		while (readfrom.hasNext()) {
			try {
				String gotline = readfrom.nextLine();
				SpawnerRandomData addelement = null;
				if (gotline.trim().length() > 0 && !gotline.startsWith("//")) {
					{
						System.out.println("Spawner init string:'" + gotline
								+ "'");
						addelement = new SpawnerRandomData(this, gotline);
					}
					if (addelement != null) {
						randomdata.add(addelement);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	private static final EntityType[] ValidMobs = new EntityType[] {
			EntityType.BAT, EntityType.BLAZE, EntityType.SPIDER,
			EntityType.ENDERMAN, EntityType.CREEPER, EntityType.GHAST,
			EntityType.PIG_ZOMBIE, EntityType.WITCH, EntityType.SKELETON,
			EntityType.MAGMA_CUBE, EntityType.ZOMBIE,EntityType.HORSE };

	public void RandomizeEntity(LivingEntity le) {

		
		//give them a name. First get the name of the Mob.
		String mobdescription = CoreEventHandler.getEntityDescription(le,false);
		String usemobname = _owner.NameGen.RandomMobName(mobdescription);
		//Random mob names are shown rarely. It seems to affect the fps...
		if(RandomData.rgen.nextFloat() > 0.95f)
		{
			//make sure we are 1.5
			try {
			le.setCustomName(usemobname);
			le.setCustomNameVisible(true);
			}
			catch(NoSuchMethodError ex){
			
			}
		}
		
		
		
		// Order is Weapon,Boots,Leggings,Chestplate,Helmet

		// Armor/weapons are only equipable by Zombies, Skeletons, and Pigmen.
		int baseXP = 0;
		le.setRemoveWhenFarAway(false);
		if (le instanceof Creeper || le instanceof Blaze
				|| le instanceof Enderman || le instanceof Spider
				|| le instanceof CaveSpider || le instanceof Zombie) {
			baseXP = 5;
		}

		if(le instanceof AbstractHorse){
			baseXP *=3;
			AbstractHorse h = (AbstractHorse)le;
			
			
			
			if(h instanceof Horse){
			Material selectedItem = RandomData.Choose(new Material[]{Material.IRON_BARDING,Material.GOLD_BARDING,Material.DIAMOND_BARDING});
			((Horse)h).getInventory().setArmor(new ItemStack(selectedItem,1));
			}
			else if(h instanceof Donkey){
				
				//randomize!
				ChestRandomizer cr = new ChestRandomizer(this._owner,((Donkey)h).getInventory(),"");
				cr.Shuffle();
				//now reset Armour and Saddle.
				((Donkey)h).getInventory().setItem(0,new ItemStack(Material.SADDLE,1) );
			}
			else if(h instanceof Mule)
			{
				ChestRandomizer cr = new ChestRandomizer(this._owner,((Mule)h).getInventory(),"");
				cr.Shuffle();
				//now reset Armour and Saddle.
				((Mule)h).getInventory().setItem(0,new ItemStack(Material.SADDLE,1) );
			}
		}
		if (le instanceof Creeper) {

			baseXP *= 2;
			((Creeper) le).setPowered(RandomData.rgen.nextBoolean());
		}
		if (le instanceof Skeleton) {

			baseXP *= 2.5;
			// ((Skeleton)le).
			Skeleton cs = (Skeleton) le;
			
			if(RandomData.rgen.nextBoolean())
			{
				//replace with Wither Skeleton
				le.getWorld().spawnEntity(le.getLocation(), EntityType.WITHER_SKELETON); //should be randomized automatically- no need to do it here, assuming the event is fired.
			}
			/*cs.setSkeletonType(
					RandomData.rgen.nextBoolean() ? SkeletonType.WITHER : SkeletonType.NORMAL);*/

		}
		if (le instanceof Zombie) {

			Zombie cz = (Zombie) le;
			cz.setBaby(RandomData.rgen.nextBoolean());

		}
		if (le instanceof Enderman) {
			Enderman eman = (Enderman) le;
			eman.setTarget(BCRandomizer.getNearestPlayer(eman.getLocation()));

		}

		if (le instanceof Zombie || le instanceof Skeleton
				|| le instanceof PigZombie) {

			// choose random Weapon, Boots, Leggings, Chestplate, and Helmet.
			// each item has a 20 percent chance of being blank.
			
            if(RandomData.rgen.nextBoolean())
            {
            	le.getEquipment().setItemInOffHand(RandomData.GenerateShield());
            }
			// weapon.
			if (RandomData.rgen.nextFloat() > 0.2f) {
				// choose a random weapon.
				List<RandomData> Weapons = ChestRandomizer
						.getWeaponsData(_owner);
				List<RandomData> Bows = ChestRandomizer.getBowData(_owner);
				// choose one element.
				RandomData chosenweapon =null;
				if(le instanceof Skeleton && !(le instanceof WitherSkeleton))
				{
					
				chosenweapon = RandomData.ChooseRandomData(Bows);	
				}
				else {
				chosenweapon = RandomData.ChooseRandomData(Weapons);
				}
				ItemStack acquiredweapon = chosenweapon.Generate();
				if (acquiredweapon != null)
					// el.setEquipment(0,CraftItemStack.asCraftCopy(acquiredweapon));
				    le.getEquipment().setItemInMainHand(acquiredweapon);
					//el.setEquipment(0, CraftItemStack.asNMSCopy(acquiredweapon));

			}
			// boots (index 1)
			if (RandomData.rgen.nextFloat() > 0.2f) {
				List<RandomData> Boots = ChestRandomizer.getBootsData(_owner);
				// choose one element...
				RandomData chosenboots = RandomData.ChooseRandomData(Boots);
				ItemStack acquiredboots = chosenboots.Generate();
				if (acquiredboots != null)
					le.getEquipment().setBoots(acquiredboots);
					//el.setEquipment(1, CraftItemStack.asNMSCopy(acquiredboots));

			}
			// leggings.
			if (RandomData.rgen.nextFloat() > 0.2f) {
				List<RandomData> Leggings = ChestRandomizer
						.getBootsData(_owner);
				RandomData chosenLeggings = RandomData
						.ChooseRandomData(Leggings);
				ItemStack acquiredleggings = chosenLeggings.Generate();
				if (acquiredleggings != null)
					le.getEquipment().setLeggings(acquiredleggings);
					

			}
			// chestplate.
			if (RandomData.rgen.nextFloat() > 0.2f) {
				List<RandomData> chestplates = ChestRandomizer
						.getChestplateData(_owner);
				RandomData chosenchestplate = RandomData
						.ChooseRandomData(chestplates);
				ItemStack acquiredchestplate = chosenchestplate.Generate();
				if (acquiredchestplate != null)
					le.getEquipment().setChestplate(acquiredchestplate);
					

			}

			// lastly, helmet.
			if (RandomData.rgen.nextFloat() > 0.2f) {
				List<RandomData> Helmets = ChestRandomizer
						.getHelmetData(_owner);
				RandomData chosenHelmet = RandomData.ChooseRandomData(Helmets);
				ItemStack acquiredHelmet = chosenHelmet.Generate();
				if (acquiredHelmet != null)
					le.getEquipment().setHelmet(acquiredHelmet);
					

			}

			//also: 20 percent chance of them riding a horse.
			if(RandomData.rgen.nextFloat() < 0.2f){
				
				//spawn a Horse in their location...
				AbstractHorse EnemyHorse;
				EntityType ChosenHorse = RandomData.Choose(new EntityType[]{EntityType.SKELETON_HORSE,EntityType.ZOMBIE_HORSE});
				EnemyHorse = (AbstractHorse)le.getWorld().spawnEntity(le.getLocation(),ChosenHorse);
				
				
				
				((AbstractHorse)EnemyHorse).addPassenger(le);
				
				
				
				//give it the entity as a rider.
				
				
				
			}
			
			
		}

	}

	public void RandomizeSpawner(CreatureSpawner modify) {

		if (randomdata == null)
			reload();
		SpawnerRandomData[] choosefrom = new SpawnerRandomData[randomdata
				.size()];
		choosefrom = randomdata.toArray(choosefrom);
		// create the corresponding weight array.
		float[] weights = new float[choosefrom.length];
		for (int i = 0; i < weights.length; i++) {

			weights[i] = choosefrom[i].getProbabilityWeight();
			System.out.println("weight for "
					+ choosefrom[i].getEntityType().getName() + " is "
					+ weights[i]);

		}
		// now, choose one.
		SpawnerRandomData chosen = RandomData.Choose(choosefrom, weights);
		//chosen.Apply(modify);
	}

	public void RandomizeSpawners(World w) {

		for (Chunk iteratechunk : w.getLoadedChunks()) {

			for (BlockState te : iteratechunk.getTileEntities()) {

				if (te instanceof CreatureSpawner) {
					CreatureSpawner spawner = (CreatureSpawner) te;
					//

					RandomizeSpawner(spawner);

				}

			}

		}

	}

}
