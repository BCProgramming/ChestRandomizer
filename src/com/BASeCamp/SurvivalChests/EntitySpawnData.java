package com.BASeCamp.SurvivalChests;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class EntitySpawnData {
	public enum EquipmentSlot
	{
		EQUIPMENT_WEAPON,
		EQUIPMENT_BOOTS,
		EQUIPMENT_LEGGINGS,
		EQUIPMENT_CHESTPLATE,
		EQUIPMENT_HELMET
		
		
	}
	
	private EntityType spawnEntity = EntityType.ZOMBIE;
	private float probabilityWeight = 1f;
	private HashMap<RandomData,Float> WeaponSlot = new HashMap<RandomData,Float>();
	private HashMap<RandomData,Float> BootsSlot = new HashMap<RandomData,Float>();
	private HashMap<RandomData,Float> LeggingsSlot = new HashMap<RandomData,Float>();
	private HashMap<RandomData,Float> ChestplateSlot = new HashMap<RandomData,Float>();
	private HashMap<RandomData,Float> HelmetSlot = new HashMap<RandomData,Float>();
	
	public EntityType getSpawnEntity() { return spawnEntity;}
	public void setSpawnEntity(EntityType value) { spawnEntity=value;}
	
	public float getProbabilityWeight() { return probabilityWeight;}
	
	public Map<RandomData,Float> getWeaponSlot() { return Collections.unmodifiableMap(WeaponSlot);}
	public Map<RandomData,Float> getBootsSlot() { return Collections.unmodifiableMap(BootsSlot);}
	public Map<RandomData,Float> getLeggingsSlot() { return Collections.unmodifiableMap(LeggingsSlot);}
	public Map<RandomData,Float> getChestplateSlot() { return Collections.unmodifiableMap(ChestplateSlot);}
	public Map<RandomData,Float> getHelmetSlot() { return Collections.unmodifiableMap(HelmetSlot);}
	
	private EntityType entityFromString(String struse){
		
		//only hostile mobs for the moment.
		if(struse.equalsIgnoreCase("Zombie")){
			
			return EntityType.ZOMBIE;
			
		}
		else if(struse.equalsIgnoreCase("PigZombie")){
			
			return EntityType.PIG_ZOMBIE;
			
		}
		else if(struse.equalsIgnoreCase("Skeleton")){
			
			return EntityType.SKELETON;
			
		}
		else if (struse.equalsIgnoreCase("Blaze")){
			
			return EntityType.BLAZE;
			
		}
		
		else if(struse.equalsIgnoreCase("Cave Spider")){
			return EntityType.CAVE_SPIDER;
		}
		else if(struse.equalsIgnoreCase("Spider")){
			return EntityType.SPIDER;
		}
		else if(struse.equalsIgnoreCase("Ghast")){
			
			return EntityType.GHAST;
		}
		else if(struse.equalsIgnoreCase("Witch")){
			
			return EntityType.WITCH;
			
		}
		
			//EntityType.CREEPER
			//EntityType.ENDERMAN
			//EntityType.MAGMA_CUBE
			//EntityType.SLIME
			//EntityType.WITCH
			
			
		return null;
		
	}
	public void Apply(LivingEntity le){
		
		
		
		
		
		
	}
	private HashMap<RandomData,Float> readRandomData(Element sourceelement){
		
		//format is a element containing child "randomdata" elements.
		//empty string is a sentinel to indicate to choose from the list of items as normal (those provided for random item selection).
		HashMap<RandomData,Float> createlist = new HashMap<RandomData,Float>();
		NodeList Randomdatas = sourceelement.getElementsByTagName("randomdata");
		for (int n=0;n<Randomdatas.getLength();n++){
			
			Element currelement = (Element) Randomdatas.item(n);
			String initializer = currelement.getAttribute("initializer");
			float useWeight=0; 
			try {
			useWeight =Float.parseFloat(currelement.getAttribute("weight"));
			}
			catch(Exception ex) { useWeight=0;}
			if(!initializer.equalsIgnoreCase("")){
				RandomData createitem = new RandomData(initializer);
				if(createitem!=null){
					createlist.put(createitem,useWeight);
						
					
				}
			} else {
				createlist.put(null,useWeight);
			}
			
			
			
		}
		return createlist;
		
	}
	public EntitySpawnData(Element sourceelement){
		//takes the sourceelement, this is the Entity XML element.
		/*
		 <entity name="Zombie" weight="Number">
		 <weapon>
		 <randomdata initializer="same as cfg line" weight="number"><randomdata>
		 ... 
		 </weapon>
		[!--same story for <helmet>,<chestplate>, <leggings> and <boots>
		 </entity>
*/
		//get name and weight.
		String usename = sourceelement.getAttribute("name");
		
		//convert name to a Entity.
		spawnEntity = entityFromString(usename);
		
		probabilityWeight = Float.parseFloat(sourceelement.getAttribute("weight"));
		
		//now we need to parse each equipment tag.
		NodeList childnodes = sourceelement.getChildNodes();
		
		for(int i=0;i<childnodes.getLength();i++){
			if(childnodes.item(i) instanceof Element){
			Element currnode = (Element)childnodes.item(i);
			String tagname = currnode.getNodeName();
			
			if(tagname.equalsIgnoreCase("weapon")){
			WeaponSlot = readRandomData(currnode);
			}
			else if(tagname.equalsIgnoreCase("boots")){
				BootsSlot = readRandomData(currnode);
			}
			else if(tagname.equalsIgnoreCase("leggings")){
				LeggingsSlot = readRandomData(currnode);
			}
			else if(tagname.equalsIgnoreCase("helmet")){
				HelmetSlot = readRandomData(currnode);
			}
			}
		}
		
		
		
		
		
	}
	
	
	
}
