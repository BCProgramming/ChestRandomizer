package com.BASeCamp.SurvivalChests;



import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityMobSpawner;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class SpawnerRandomData {
//analogous to the RandomData class, but for spawners.
	
	//basic information.
	private float ProbabilityWeight = 1.0f;
	private int MinSpawnDelay = 200;
	private int MaxSpawnDelay = 800;
	private EntityType SpawnType = EntityType.PIG; //default to pig, because, hey, why not.
	
	//accessors. also, special accessors to set Entity by name as well.
	
	public float getProbabilityWeight() { return ProbabilityWeight;}
	public void setProbabilityWeight(float value) { ProbabilityWeight = value;}
	
	
	public int getMinSpawnDelay(){ return MinSpawnDelay;}
	public void setMinSpawnDelay(int value) { MinSpawnDelay=value;}
	
	public int getMaxSpawnDelay() { return MaxSpawnDelay;}
	public void setMaxSpawnDelay(int value) { MaxSpawnDelay = value;}
	
	public EntityType getEntityType() { return SpawnType;}
	public void setEntityType(EntityType value) { SpawnType=value;}
	public void setEntityTypebyName(String EntityName){
	for(EntityType iterate:EntityType.values()){
		if(iterate!=null && iterate.getName()!=null){
		if(iterate.getName().equalsIgnoreCase(EntityName)){
			SpawnType = iterate;
			return;
		}
		}
	}
		
	}
	public SpawnerRandomData(){
		//empty boring initializer.
		
	}
	public SpawnerRandomData(String initializer){
		
		//accepts a line with initialization data. Usually, read from a file.
		
		//initializer format:
		//MobName,Weight,MinDelay,MaxDelay
		
		String[] separated = initializer.split(",");
		
		setEntityTypebyName(separated[0]);
		ProbabilityWeight = Float.parseFloat(separated[1]);
		MinSpawnDelay = Integer.parseInt(separated[2]);
		MaxSpawnDelay = Integer.parseInt(separated[3]);
		
		
		//done...
		
		
		
		
		
	}
	public void Apply(CreatureSpawner applyTo){
		
		int useSpawnDelay = RandomData.rgen.nextInt(MaxSpawnDelay-MinSpawnDelay) + MinSpawnDelay;
		applyTo.setDelay(useSpawnDelay);
		
		applyTo.setSpawnedType(SpawnType);

		
		
		
	}
	
	
	
	
	
	
	
}
