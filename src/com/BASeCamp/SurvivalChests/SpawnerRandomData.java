package com.BASeCamp.SurvivalChests;



import java.lang.reflect.Field;
import java.util.List;



import net.minecraft.server.v1_4_6.NBTTagCompound;
import net.minecraft.server.v1_4_6.NBTTagFloat;
import net.minecraft.server.v1_4_6.NBTTagList;
import net.minecraft.server.v1_4_6.TileEntity;
import net.minecraft.server.v1_4_6.TileEntityMobSpawner;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_4_6.block.CraftCreatureSpawner;
import org.bukkit.craftbukkit.v1_4_6.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.craftbukkit.v1_4_6.CraftWorld;
public class SpawnerRandomData {
//analogous to the RandomData class, but for spawners.
	
	//basic information.
	private float ProbabilityWeight = 1.0f;
	private int MinSpawnDelay = 200;
	private int MaxSpawnDelay = 800;
	private short MinPlayerRange = 10;
	private short MaxPlayerRange = 50;
	private short MinSpawnRange = 15;
	private short MaxSpawnRange = 100;
	
	private short MinSpawnCount = 1;
	private short MaxSpawnCount = 10;
	
	
	private short minEntityCap = 15;
	private short maxEntityCap = 75;
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
	SpawnerRandomizer _Owner;
	public SpawnerRandomData(SpawnerRandomizer owner){
		//empty boring initializer.
		_Owner=owner;
		
	}
	
	
	
	
	public SpawnerRandomData(SpawnerRandomizer owner,String initializer){
		
		//accepts a line with initialization data. Usually, read from a file.
		_Owner = owner;
		//initializer format:
		//MobName,Weight,MinDelay,MaxDelay
		System.out.println("Spawner String:" + initializer);
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
		try {
			 //TileEntity te = ((CraftWorld)block.getWorld()).getHandle().getTileEntity(block.getX(), block.getY(), block.getZ());
			Block block = applyTo.getBlock();
			TileEntity te = (((CraftWorld) ((applyTo.getWorld()))).getHandle()).getTileEntity(block.getX(),block.getY(),block.getZ());
			  if(te instanceof TileEntityMobSpawner) {
			    TileEntityMobSpawner tems = (TileEntityMobSpawner)te;
			    
			    NBTTagCompound c = new NBTTagCompound();
			    tems.b(c);
			    short playerrange = (short)(RandomData.rgen.nextInt(MaxPlayerRange-MinPlayerRange)+MinPlayerRange);
			    short spawnerrange = (short)(RandomData.rgen.nextInt(MaxSpawnRange-MinSpawnRange) + MinSpawnRange);
			    short usespawncount = (short)(RandomData.rgen.nextInt(MaxSpawnCount-MinSpawnCount) + MinSpawnCount);
			    short useEntityCap = (short)(RandomData.rgen.nextInt(maxEntityCap-minEntityCap) + minEntityCap);
			   // System.out.println("Set spawner playerrange to " + playerrange);
			    
			    
			    c.setShort("RequiredPlayerRange", playerrange);
			    c.setShort("SpawnRange",spawnerrange);
			    c.setShort("SpawnCount",usespawncount);
			    c.setBoolean("CanPickUpLoot", true);
			    
			    
			    NBTTagList DropChances = new NBTTagList("DropChances");
			    for(int i=0;i<4;i++)
			    	DropChances.add(new NBTTagFloat("",1.0f));
			    
			    
			    c.set("DropChances", DropChances);
			    //for adding Armour:
			    //spawner has "SpawnData" compound. This is the same as the Mob Data:
			    NBTTagCompound SData = new NBTTagCompound("SpawnData");
			    NBTTagList equipment = new NBTTagList("Equipment");
			    SData.set("Equipment",equipment);
			    
			    //CraftItemStack Weapon = new CraftItemStack(org.bukkit.Material.IRON_SWORD,1);
			    
			    //choose random data for the weapon.
			    RandomData chosenweapon = RandomData.ChooseRandomData(ChestRandomizer.getWeaponsData(_Owner.getOwner() ));
			    
			    ItemStack gotweapon = chosenweapon==null?null:chosenweapon.Generate();
			    
			    
			    
			    RandomData chosenBoots = RandomData.ChooseRandomData(ChestRandomizer.getBootsData(_Owner.getOwner()));
			    
			    ItemStack gotboots = chosenBoots==null?null:chosenBoots.Generate();
			    
			    
			    RandomData chosenLeggings = RandomData.ChooseRandomData(ChestRandomizer.getLeggingsData(_Owner.getOwner()));
			    ItemStack gotLeggings =chosenLeggings==null?null: chosenLeggings.Generate();
			    
			    RandomData chosenChestPlate = RandomData.ChooseRandomData(ChestRandomizer.getChestplateData(_Owner.getOwner()));
			    ItemStack gotChestplate = chosenChestPlate==null?null:chosenChestPlate.Generate();
			    
			    RandomData ChosenHelmet = RandomData.ChooseRandomData(ChestRandomizer.getHelmetData(_Owner.getOwner()));
			    ItemStack gotHelmet = ChosenHelmet==null?null:ChosenHelmet.Generate();
			    
			    CraftItemStack Weapon = null;
			    if(gotweapon!=null) Weapon = CraftItemStack.asCraftCopy(gotweapon);
			    
			    CraftItemStack Boots = null;
			    if(gotboots!=null) Boots = CraftItemStack.asCraftCopy(gotboots);
			    
			    CraftItemStack ChestPlate = null;
			    if(gotChestplate !=null) ChestPlate = CraftItemStack.asCraftCopy(gotChestplate);
			    
			    
			    CraftItemStack Leggings = null;
			    if(gotLeggings!=null) Leggings = CraftItemStack.asCraftCopy(gotLeggings);
			    
			    
			    CraftItemStack Helmet = null;
			    if(gotHelmet!=null) Helmet = CraftItemStack.asCraftCopy(gotHelmet);
			    
			    //CraftItemStack Boots = new CraftItemStack(org.bukkit.Material.IRON_BOOTS,1);
			    //CraftItemStack ChestPlate = new CraftItemStack(org.bukkit.Material.IRON_CHESTPLATE,1);
			    //CraftItemStack Leggings = new CraftItemStack(org.bukkit.Material.IRON_LEGGINGS,1);
			    //CraftItemStack Helmet = new CraftItemStack(org.bukkit.Material.LEATHER_HELMET,1);
			  //first is weapon
			    
			    
			    if(Weapon==null) 
			    	equipment.add(new NBTTagCompound());
			    else{
			    	NBTTagCompound NBTSave = new NBTTagCompound("Equipment");
			    	
			    	//Weapon.getHandle().save(NBTSave);
			    	equipment.add(NBTSave);
			    }
			   
			    if(Boots==null)
			    	equipment.add(new NBTTagCompound());
			    else
			    {
			    	NBTTagCompound NBTSave = new NBTTagCompound("Equipment");
			    	//Boots.getHandle().save(NBTSave);
			    	//equipment.add(NBTSave);
			    }
			    
			    if(Leggings==null)
			    	equipment.add(new NBTTagCompound());
			    else{
			    	NBTTagCompound NBTSave = new NBTTagCompound("Equipment");
			    	//Leggings.getHandle().save(NBTSave);
			    	//equipment.add(NBTSave);
			    }
			    
			    if(ChestPlate == null)
			    	equipment.add(new NBTTagCompound());
			    else
			    {
			    	NBTTagCompound NBTSave = new NBTTagCompound("Equipment");
			    	//ChestPlate.getHandle().save(NBTSave);
			    	//equipment.add(NBTSave);
			    	
			    	
			    }
			    
			    if(Helmet==null)
			    	equipment.add(new NBTTagCompound());
			    else
			    {
			    	NBTTagCompound NBTSave = new NBTTagCompound("Equipment");
			    	//Helmet.getHandle().save(NBTSave);
			    	//equipment.add(NBTSave);
			    	
			    }
			    
			    c.set("SpawnData", SData);
			    
			    
			    
			    
			    
			    
			     
			    
			    
			    /*
			     * Health
AttackTime
HurtTime
DeathTime
ActiveEffects (ignore for now?)
Equipment Compoud tag, five items...
 0 heldd item (weapon)
 1 Boots
 2 Leggings
 3.Chestplate
 4. Head

dropChanges list of 5 floats indicating chances to drop the corresponding equipment item

CanPickUpLoot 1/0 t/f
PersistenceRequired 0/1
Invulnerable 1/0

			     * */
			    tems.a(c);
			
			  }
		 
		 
		}
		catch(Exception exx){
			exx.printStackTrace();
		}
	}
	
	
	
	
	
	
	
}
