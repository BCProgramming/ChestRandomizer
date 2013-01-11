package com.BASeCamp.SurvivalChests;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.enchantments.*;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
public class EnchantmentProbability implements Cloneable {

	public class EnchantmentAssignmentData implements Cloneable{
		
		private String Name;
		private int Level;
		private float Weight;
		public String getName() { return Name;}
		public int getLevel() { return Level;}
		public float getWeight() { return Weight;}
		public EnchantmentAssignmentData clone(){
			
			return new EnchantmentAssignmentData(Name,Level,Weight);
			
			
		}
		public EnchantmentAssignmentData(String pName,int pLevel,float pWeight){
			
			Name=pName.toUpperCase();
			Level=pLevel;
			Weight=pWeight;
			
			
		}
		
	}
	public Object clone(){
		
		
		EnchantmentProbability ep = new EnchantmentProbability();
		ep.Enchantprobabilities = (LinkedList<EnchantmentAssignmentData>) Enchantprobabilities.clone();
	
		ep.preCache();
		return ep;
		
	}
	public static HashMap<String,Enchantment> EnchantmentMapping = null;
	protected LinkedList<EnchantmentAssignmentData> Enchantprobabilities = new LinkedList<EnchantmentAssignmentData>();
	protected HashMap<Integer,Float> EnchantLevelProbabilities = new HashMap<Integer,Float>();
	
	
	
	public void setProbability(String EnchantmentName,float Probability)
	{
		//no level was specified, so we'll assume we are to set a bunch of levels...
		float calcsum=0;
		for(Float getsum:EnchantLevelProbabilities.values()){
			calcsum+=getsum;
			
		}
		
		
		for(Integer iterate:EnchantLevelProbabilities.keySet()){
			
			
			float useprobability = Probability*(EnchantLevelProbabilities.get(iterate)/calcsum);
			
			
			
		   Enchantprobabilities.add(new EnchantmentAssignmentData(EnchantmentName,
				   iterate,useprobability));	
			
		}
		
		
	
	}
	public void setProbability(String EnchantmentName,int Level,float Probability){
		
		Enchantprobabilities.add(new EnchantmentAssignmentData(EnchantmentName,Level,Probability));
		
		
	}
	float[] cacheprobabilities = null;
	EnchantmentAssignmentData[] enchantdata = null;
	public void Apply(ItemStack applyitem){
		
		if(Enchantprobabilities.size()==0) return; //no enchants to choose from.
		//otherwise, simplez. create an array of floats and a corresponding array of EnchantmentAssignmentData's...
		
		//System.out.println("Applying enchantment. Choosing from " + Enchantprobabilities.size());
		preCache();
		//now, use the infamous Choose routine...
		
		Enchantment useenchant = null;
		EnchantmentAssignmentData chosen =null;

        chosen = RandomData.Choose(enchantdata,cacheprobabilities);
        if(chosen==null) return; //no enchant selected.
		useenchant = EnchantmentMapping.get(chosen.getName());
		//System.out.println("chosen enchant:" + useenchant.getName());

		if(useenchant==null) {
			//System.out.println("Null enchantment....");
			return;
			
		}
		if(!applyitem.containsEnchantment(useenchant)){
		applyitem.addUnsafeEnchantment(useenchant, chosen.getLevel());
		}
		
		
	}
	public void preCache() {
		
		/*if(cacheprobabilities ==null){
			System.out.println("Precache enchantmentprobabilities->" + Enchantprobabilities.size());*/
		cacheprobabilities = new float[Enchantprobabilities.size()];
		enchantdata = new EnchantmentAssignmentData[Enchantprobabilities.size()];
		int index=0;
		for(EnchantmentAssignmentData ead:Enchantprobabilities){
			
			cacheprobabilities[index] = ead.getWeight();
			enchantdata[index] = ead;
			index++;
			
			
			
		}
		}
		
	}
	//applies a random enchantment to the given item.
	/*
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
		
		
	}*/
	public EnchantmentProbability()
	{
		EnchantLevelProbabilities.put(1,100f);
		EnchantLevelProbabilities.put(2,50f);
		EnchantLevelProbabilities.put(3,45f);
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
