package com.BASeCamp.SurvivalChests;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.TileEntityMobSpawner;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftSkeleton;
import org.bukkit.craftbukkit.entity.CraftZombie;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

//this class needs a LOT of work. (different weights, give mobs special abilities, etc.)
public class SpawnerRandomizer {
	
	
	LinkedList<SpawnerRandomData> randomdata = null;
	
	private BCRandomizer _owner;
	public BCRandomizer getOwner() { return _owner;}
	public SpawnerRandomizer(BCRandomizer owner){ _owner = owner;}
	
	private void reload(){
		
		randomdata = new LinkedList<SpawnerRandomData>();
		Scanner readfrom = _owner.acquireStream("spawners.cfg");
		while(readfrom.hasNext()){
			try {
				String gotline=readfrom.nextLine();
				SpawnerRandomData addelement=null;
				if(gotline.trim().length()>0 && !gotline.startsWith("//")){
					{
					System.out.println("Spawner init string:'" + gotline + "'");
					addelement = new SpawnerRandomData(this,gotline);
					}
			if(addelement!=null){
				randomdata.add(addelement);
			}
				}}
			catch(Exception ex){
				ex.printStackTrace();
			}
			}
			
			
			
		
		
	}
	
	
	private static final EntityType[] ValidMobs = new EntityType[] 
     	{ EntityType.BAT,EntityType.BLAZE,EntityType.CAVE_SPIDER,EntityType.ENDERMAN,EntityType.CREEPER,
     		EntityType.GHAST,EntityType.PIG_ZOMBIE,EntityType.WITCH,EntityType.SKELETON,EntityType.MAGMA_CUBE,EntityType.ZOMBIE
     	};
	
		public void RandomizeEntity(LivingEntity le){
			if(le instanceof CraftLivingEntity){
				CraftLivingEntity cle = (CraftLivingEntity)le;
				EntityLiving el = cle.getHandle();
				//Order is Weapon,Boots,Leggings,Chestplate,Helmet
				
				//Armor/weapons are only equipable by Zombies, Skeletons, and Pigmen.
				int baseXP = 0;
				
				if(le instanceof Creeper || le instanceof Blaze || 
						le instanceof Enderman || le instanceof Spider 
						|| le instanceof CaveSpider || le instanceof Zombie){
					baseXP =5;
				}
				
				
				if(le instanceof Creeper){
					
					baseXP*=2;
					((Creeper)le).setPowered(RandomData.rgen.nextBoolean());
				}
				if(le instanceof Skeleton){
					
					baseXP*=2.5;
					//((Skeleton)le).
					CraftSkeleton cs = (CraftSkeleton)le;
					cs.getHandle().setSkeletonType(RandomData.rgen.nextBoolean()?1:0);
					
				}
				if(le instanceof Zombie){
					
					CraftZombie cz = (CraftZombie)le;
					cz.getHandle().setBaby(RandomData.rgen.nextBoolean());
					
					
				}
				
				
				if(le instanceof Zombie || le instanceof Skeleton || le instanceof PigZombie){
					
					//choose random Weapon, Boots, Leggings, Chestplate, and Helmet.
					//each item has a 20 percent chance of being blank.
					CraftLivingEntity craftliving = (CraftLivingEntity) le;
					
					
					//weapon.
					if(RandomData.rgen.nextFloat() > 0.2f){
						//choose a random weapon.
						List<RandomData> Weapons = ChestRandomizer.getWeaponsData(_owner);
						//choose one element.
						RandomData chosenweapon = RandomData.ChooseRandomData(Weapons);
						ItemStack acquiredweapon = chosenweapon.Generate();
						if(acquiredweapon!=null)
							el.setEquipment(0, new CraftItemStack(acquiredweapon).getHandle());
						
						
					}
					//boots (index 1)
					if(RandomData.rgen.nextFloat() > 0.2f){
						List<RandomData> Boots = ChestRandomizer.getBootsData(_owner);
						//choose one element...
						RandomData chosenboots = RandomData.ChooseRandomData(Boots);
						ItemStack acquiredboots = chosenboots.Generate();
						if(acquiredboots!=null) 
							el.setEquipment(1,new CraftItemStack(acquiredboots).getHandle());
						
						
					}
					//leggings.
					if(RandomData.rgen.nextFloat() > 0.2f){
						List<RandomData> Leggings = ChestRandomizer.getBootsData(_owner);
						RandomData chosenLeggings = RandomData.ChooseRandomData(Leggings);
						ItemStack acquiredleggings = chosenLeggings.Generate();
						if(acquiredleggings!=null) 
							el.setEquipment(2,new CraftItemStack(acquiredleggings).getHandle());
						
						
						
						
					}
					//chestplate.
					if(RandomData.rgen.nextFloat() > 0.2f){
						List<RandomData> chestplates = ChestRandomizer.getChestplateData(_owner);
						RandomData chosenchestplate = RandomData.ChooseRandomData(chestplates);
						ItemStack acquiredchestplate = chosenchestplate.Generate();
						if(acquiredchestplate!=null) el.setEquipment(3,new CraftItemStack(acquiredchestplate).getHandle());
						
						
					}
					
					
					
					//lastly, helmet.
					if(RandomData.rgen.nextFloat() > 0.2f) {
						List<RandomData> Helmets = ChestRandomizer.getHelmetData(_owner);
						RandomData chosenHelmet = RandomData.ChooseRandomData(Helmets);
						ItemStack acquiredHelmet = chosenHelmet.Generate();
						if(acquiredHelmet!=null) el.setEquipment(4,new CraftItemStack(acquiredHelmet).getHandle());
						
						
						
					}
					
					
					
				}
				
				
				
				
				
				
			}
			
			
			
			
		}
     	public void RandomizeSpawner(CreatureSpawner modify){
     		
     		if(randomdata==null) reload();
     		SpawnerRandomData[] choosefrom = new SpawnerRandomData[randomdata.size()];
     		choosefrom = randomdata.toArray(choosefrom);
     		//create the corresponding weight array.
     		float[] weights = new float[choosefrom.length];
     		for(int i=0;i<weights.length;i++){
     			
     			weights[i] = choosefrom[i].getProbabilityWeight();
     			System.out.println("weight for " + choosefrom[i].getEntityType().getName() + " is " + weights[i]);
     			
     		}
     		//now, choose one.
     		SpawnerRandomData chosen = RandomData.Choose(choosefrom,weights);
     		chosen.Apply(modify);
     	}
     	
     	public void RandomizeSpawners(World w){
     		
     		for(Chunk iteratechunk :w.getLoadedChunks()){
     			
     			for(BlockState te :iteratechunk.getTileEntities()){
     				
     				if(te instanceof CreatureSpawner){
     					CreatureSpawner spawner = (CreatureSpawner )te;
     					//
     					
     					RandomizeSpawner(spawner);
     					
     					
     				}
     				
     				
     				
     				
     			}
     			
     			
     		}
     		
     	}

	
	

}
