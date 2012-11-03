package com.BASeCamp.SurvivalChests;
import java.util.HashMap;

import org.bukkit.enchantments.*;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
public class EnchantmentProbability {

	private static HashMap<String,Enchantment> EnchantmentMapping = null;
	private HashMap<String,Float> Enchantprobabilities = new HashMap<String,Float>();
	private HashMap<Integer,Float> EnchantLevelProbabilities = new HashMap<Integer,Float>();
	public void setProbability(String EnchantmentName,float Probability)
	{
		Enchantprobabilities.put(EnchantmentName, new Float(Probability));
		
		
	
	}
	public float getProbability(String EnchantmentName){
		if(Enchantprobabilities.containsKey(EnchantmentName)){
			
		return Enchantprobabilities.get(EnchantmentName);
		}
		else
		{
		return 0;	
		
		}
	}
	//applies a random enchantment to the given item.
	public void Apply(ItemStack applyitem, boolean superEnchantment)
	{
		//first, select a random enchantment.
		if(Enchantprobabilities.size()==0) return; //no enchantments to choose from
		Float[] boxedchantprobabilities = new Float[Enchantprobabilities.size()];
		String[] chantnames = new String[boxedchantprobabilities.length];
		
		float[] chantprobabilities= new float[boxedchantprobabilities.length];
		Enchantprobabilities.keySet().toArray(chantnames);
		Enchantprobabilities.values().toArray(boxedchantprobabilities);
		chantprobabilities = new float[boxedchantprobabilities.length];
		for(int g=0;g<boxedchantprobabilities.length;g++){
			chantprobabilities[g] = boxedchantprobabilities[g];
		}
		
		Enchantment gotenchantment = EnchantmentMapping.get(RandomData.Choose(chantnames, chantprobabilities));
		//if gotenchantment is null, then we are applying NO enchantment.
		if(gotenchantment==null) return;
		//otherwise we need to choose a random level.
		
		float[] levelprobabilities = new float[EnchantLevelProbabilities.size()];
		Float[] boxedlevelprobabilities = new Float[levelprobabilities.length];
		
		EnchantLevelProbabilities.values().toArray(boxedlevelprobabilities);
		for(int i=0;i<boxedlevelprobabilities.length;i++)
		{
			levelprobabilities[i] = boxedlevelprobabilities[i];
			
		
		}
		
		Integer[] chooselevels = new Integer[levelprobabilities.length]; 
			EnchantLevelProbabilities.keySet().toArray(chooselevels);
			int chosenlevel=0;
			if(superEnchantment){
				chosenlevel = RandomData.rgen.nextInt(6)+1;
				
			}                                
			else {chosenlevel = RandomData.Choose(chooselevels, levelprobabilities);}
		if(!applyitem.containsEnchantment(gotenchantment))
		{
		//applyitem.addEnchantment(gotenchantment, chosenlevel);
			applyitem.addUnsafeEnchantment(gotenchantment, chosenlevel);
		}
		else
		{
			//if it does contain the enchantment, add the chosen number of levels.
			int currentlevel = applyitem.getEnchantmentLevel(gotenchantment);
			currentlevel+=1;
			if(currentlevel > 10) currentlevel = 10;
			applyitem.removeEnchantment(gotenchantment);
			applyitem.addEnchantment(gotenchantment, currentlevel);
			
			
		}
		
		
	}
	public EnchantmentProbability()
	{
		EnchantLevelProbabilities.put(1,500f);
		EnchantLevelProbabilities.put(2,75f);
		EnchantLevelProbabilities.put(3,50f);
		EnchantLevelProbabilities.put(4,25f);
		EnchantLevelProbabilities.put(5,13f);
		EnchantLevelProbabilities.put(6,7f);
		EnchantLevelProbabilities.put(7,6f);
		EnchantLevelProbabilities.put(8,5f);
		EnchantLevelProbabilities.put(9,4f);
		EnchantLevelProbabilities.put(10,3f);
		if(EnchantmentMapping==null)
		{
			EnchantmentMapping = new HashMap<String,Enchantment>();
	EnchantmentMapping.put("NONE", null);
	EnchantmentMapping.put("FLAME", Enchantment.ARROW_FIRE);
	EnchantmentMapping.put("POWER",Enchantment.ARROW_DAMAGE);
	EnchantmentMapping.put("INFINITY", Enchantment.ARROW_INFINITE);
	EnchantmentMapping.put("PUNCH",Enchantment.ARROW_KNOCKBACK);
	EnchantmentMapping.put("SHARPNESS", Enchantment.DAMAGE_ALL);
	EnchantmentMapping.put("ARTHROPODS", Enchantment.DAMAGE_ARTHROPODS);
	EnchantmentMapping.put("SMITE", Enchantment.DAMAGE_UNDEAD);
	EnchantmentMapping.put("EFFICIENCY", Enchantment.DIG_SPEED);
	EnchantmentMapping.put("UNBREAKING", Enchantment.DURABILITY);
	EnchantmentMapping.put("FIREASPECT", Enchantment.FIRE_ASPECT);
	EnchantmentMapping.put("KNOCKBACK",Enchantment.KNOCKBACK);
	EnchantmentMapping.put("FORTUNE", Enchantment.LOOT_BONUS_BLOCKS);
	EnchantmentMapping.put("LOOTING", Enchantment.LOOT_BONUS_MOBS);
	EnchantmentMapping.put("RESPIRATION", Enchantment.OXYGEN);
	EnchantmentMapping.put("PROTECTION", Enchantment.PROTECTION_ENVIRONMENTAL);
	EnchantmentMapping.put("BLASTPROTECTION", Enchantment.PROTECTION_EXPLOSIONS);
	EnchantmentMapping.put("FEATHERFALLING",Enchantment.PROTECTION_FALL);
	EnchantmentMapping.put("FIREPROTECTION",Enchantment.PROTECTION_FIRE);
	EnchantmentMapping.put("PROJECTILEPROTECTION", Enchantment.PROTECTION_PROJECTILE);
	EnchantmentMapping.put("SILKTOUCH", Enchantment.SILK_TOUCH);
	EnchantmentMapping.put("AQUAAFFINITY",Enchantment.WATER_WORKER);
	
	
	
		}
		
	}
	
	
}
