package com.BASeCamp.SurvivalChests;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;


import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
 

 


public final class ItemNamer {
   
	
   
    public static ItemStack renameItem(ItemStack renameit,String Name){
    	
    	return renameItem(renameit,Name,(String)null);
    	
    	
    }
    public static ItemStack renameItem(ItemStack renameit,String Name,String Lore){
    	
    	String[] useLore = null;
    	if(Lore!=null)
    		useLore = Lore.split("\n");
    	
    	return renameItem(renameit,Name,useLore);
    	
    	
    }
   public static ItemStack renameItem(ItemStack renameit,String Name,String[] Lore){
	   
	   ArrayList<String> createlist = new ArrayList<String>(); 
	   if(Lore!=null){
	   for(String iterate : Lore)
	   {
		   if(iterate!=null && iterate.length() > 0)
			   createlist.add(iterate);
		   
	   
	   }
	   if(createlist.size() > 0) createlist.toArray(Lore);
	   }
	   
	   ItemMeta im = renameit.getItemMeta();
	   if(Name!=null) im.setDisplayName(Name);
	    
	   if(Lore!=null && Lore.length>=0 && Lore[0].length() > 0) {
		   
			   im.setLore(Arrays.asList(Lore));
			   System.out.println("Set Lore to " + Lore[0]);
	   }
	   else 
	   {
		   
		//im.setLore(null);   
	   
	   }
	   renameit.setItemMeta(im);
	   
	   return renameit;
	   
   }
  
}