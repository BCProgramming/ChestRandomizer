package com.BASeCamp.SurvivalChests;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialHelper {
	//equipment types...
	private static List<Material> Helmets = null;
	private static List<Material> Chestplates = null;
	private static List<Material> Leggings = null;
	private static List<Material> Boots = null;
	private static List<Material> Weapons = null;
	
	
	private static List<Material> Wooden = null;
	private static List<Material> Leather = null;
	private static List<Material> Chainmail = null;
	private static List<Material> Iron = null;
	private static List<Material> Gold = null;
	private static List<Material> Diamond = null;
	
public  static List<Material> getWooden() {
		
		if(Wooden==null)
			Wooden = new LinkedList<Material>();
			for(Material iterate : new Material[] {
					Material.WOOD_AXE,
					Material.WOOD_HOE,
					Material.WOOD_PICKAXE,
					Material.WOOD_SPADE,
					Material.WOOD_SWORD
			}) Wooden.add(iterate);
			
			return Wooden;
			
		
		
	}
public static List<Material> getLeather() {
		
		if(Leather==null)
			Leather = new LinkedList<Material>();
			for(Material iterate : new Material[] {
					Material.LEATHER_HELMET,
					Material.LEATHER_CHESTPLATE,
					Material.LEATHER_LEGGINGS,
					Material.LEATHER_BOOTS,
					
			}) Leather.add(iterate);
			
			return Leather;
			
		
		
	}
public static List<Material> getIron() {
	
	if(Iron==null)
		Iron = new LinkedList<Material>();
		for(Material iterate : new Material[] {
				Material.IRON_HELMET,
				Material.IRON_CHESTPLATE,
				Material.IRON_LEGGINGS,
				Material.IRON_BOOTS,
				Material.IRON_AXE,
				Material.IRON_HOE,
				Material.IRON_SWORD,
				Material.IRON_PICKAXE,
				Material.IRON_SPADE
			
		}) Iron.add(iterate);
		return Iron;

}
public static List<Material> getGold() {
	
	if(Gold==null)
		Gold = new LinkedList<Material>();
		for(Material iterate : new Material[] {
				Material.GOLD_HELMET,
				Material.GOLD_CHESTPLATE,
				Material.GOLD_LEGGINGS,
				Material.GOLD_BOOTS,
				Material.GOLD_AXE,
				Material.GOLD_HOE,
				Material.GOLD_SWORD,
				Material.GOLD_PICKAXE,
				Material.GOLD_SPADE
			
		}) Gold.add(iterate);
		return Gold;

}
public static List<Material> getDiamond() {
	
	if(Diamond==null)
		Diamond = new LinkedList<Material>();
		for(Material iterate : new Material[] {
				Material.DIAMOND_HELMET,
				Material.DIAMOND_CHESTPLATE,
				Material.DIAMOND_LEGGINGS,
				Material.DIAMOND_BOOTS,
				Material.DIAMOND_AXE,
				Material.DIAMOND_HOE,
				Material.DIAMOND_SWORD,
				Material.DIAMOND_PICKAXE,
				Material.DIAMOND_SPADE
			
		}) Diamond.add(iterate);
		return Diamond;

}

public static List<Material> getChainmail() {
	
	if(Chainmail==null)
		Chainmail = new LinkedList<Material>();
		for(Material iterate : new Material[] {
				Material.CHAINMAIL_HELMET,
				Material.CHAINMAIL_CHESTPLATE,
				Material.CHAINMAIL_LEGGINGS,
				Material.CHAINMAIL_BOOTS,
		}) Chainmail.add(iterate);
		
		return Chainmail;
	
	
	
}

	
	public static List<Material> getHelmets() {
		
		if(Helmets==null) {
		
		Helmets = new LinkedList<Material>();
		for(Material iterate :new Material[] {
				Material.LEATHER_HELMET,
				Material.CHAINMAIL_HELMET,
				Material.IRON_HELMET,
				Material.GOLD_HELMET,
				Material.DIAMOND_HELMET
		}) Helmets.add(iterate);
		}
		return Helmets;
	}
	
	public static List<Material> getChestplates() {
		if(Chestplates==null){
		Chestplates = new LinkedList<Material>();
		for(Material iterate : new Material[] {
				Material.LEATHER_CHESTPLATE,
				Material.CHAINMAIL_CHESTPLATE,
				Material.IRON_CHESTPLATE,
				Material.GOLD_CHESTPLATE,
				Material.DIAMOND_CHESTPLATE
		}) Chestplates.add(iterate);
		}
		return Chestplates;
	}
	
	public static List<Material> getLeggings() {
		if(Leggings==null) {
			
			Leggings = new LinkedList<Material>();
			for(Material iterate : new Material[] {
					Material.LEATHER_LEGGINGS,
					Material.CHAINMAIL_LEGGINGS,
					Material.IRON_LEGGINGS,
					Material.GOLD_LEGGINGS,
					Material.DIAMOND_LEGGINGS
			}) Leggings.add(iterate);
			
		}
		return Leggings;
	}
	
	public static List<Material> getBoots() {
		if(Boots==null){
			
			
			
			Boots = new LinkedList<Material>();
			for(Material iterate : new Material[] {
					Material.LEATHER_BOOTS,
					Material.CHAINMAIL_BOOTS,
					Material.IRON_BOOTS,
					Material.GOLD_BOOTS,
					Material.DIAMOND_BOOTS
			});
			
			
			
		}
		return Boots;
	}
	
	
	public static boolean isHelmet(Material testmaterial) {
		
		return getHelmets().contains(testmaterial);
		
		
	}
	public static boolean isChestplate(Material testmaterial) {
		
		return getChestplates().contains(testmaterial);
		
		
	}
	public static boolean isLeggings(Material testmaterial) {
		return getLeggings().contains(testmaterial);
	}
	public static boolean isBoots(Material testmaterial) {
		return getBoots().contains(testmaterial);
	}
	
	
	public static boolean isLeather(Material testmaterial) {
		return getLeather().contains(testmaterial);
	}
	public static boolean isWooden(Material testmaterial) {
		
		return getWooden().contains(testmaterial);
		
	}
	public static boolean isIron(Material testmaterial) {
		return getIron().contains(testmaterial);
		
		
	}
	public static boolean isGold(Material testmaterial) {
		
		return getGold().contains(testmaterial);
		
		
	}
	public static boolean isChainmail(Material testmaterial) {
		
		return getChainmail().contains(testmaterial);
		
		
	}
	public static boolean isDiamond(Material testmaterial) {
		
		return getDiamond().contains(testmaterial);
		
	}
}
