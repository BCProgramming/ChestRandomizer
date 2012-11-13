package com.BASeCamp.SurvivalChests;

import java.util.LinkedList;
import java.util.Scanner;

import net.minecraft.server.TileEntityMobSpawner;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

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
