package com.BASeCamp.SurvivalChests;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
 
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagDouble;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;
 
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
 
public class ItemNamer {
   
    private static CraftItemStack                    craftStack;
    private static net.minecraft.server.ItemStack    itemStack;
   
    public static void load(ItemStack item) {
        if (item instanceof CraftItemStack) {
            craftStack = (CraftItemStack) item;
            ItemNamer.itemStack = craftStack.getHandle();
        }
        else if (item instanceof ItemStack) {
            craftStack = new CraftItemStack(item);
            ItemNamer.itemStack = craftStack.getHandle();
        }
        NBTTagCompound tag = itemStack.tag;
        if (tag == null) {
            tag = new NBTTagCompound();
            tag.setCompound("display", new NBTTagCompound());
            itemStack.tag = tag;
        }
    }
   
    public static void setName(String name) {
    	if(name=="") return;
        NBTTagCompound tag = itemStack.tag.getCompound("display");
        tag.setString("Name", name);
        itemStack.tag.setCompound("display", tag);
    }
   
    public static String getName() {
        NBTTagCompound tag = itemStack.tag.getCompound("display");
        return tag.getString("Name");
    }
   
    public static void addLore(String lore) {
        NBTTagCompound tag = itemStack.tag.getCompound("display");
        NBTTagList list = tag.getList("Lore");
        if (list == null) list = new NBTTagList();
        list.add(new NBTTagString("", lore));
        tag.set("Lore", list);
        itemStack.tag.setCompound("display", tag);
    }
    
    public static void setLore(String lore) {
        NBTTagCompound tag = itemStack.tag.getCompound("display");
        tag.set("Lore", new NBTTagList());
        
        String[] splitresult = lore.split("\\r?\\n");
        
        for(int i=0;i<splitresult.length;i++)
        {
        addLore(splitresult[i]);
        }
        
        //list.add(new NBTTagString("","§r§5" + lore));
        //tag.set("Lore", list);
        
        //itemStack.tag.setCompound("display", tag);
    }
    public static String NBTtoString(NBTTagCompound sourcenbt){
    	//sourceNBT must be an NBT tag of some sort.
    	if(sourcenbt instanceof NBTTagCompound){
    		
    		ByteArrayOutputStream os = new ByteArrayOutputStream();
     		XMLEncoder xml = new XMLEncoder(os);
     		xml.writeObject(sourcenbt);
     		xml.close();
     		String serialized = javax.xml.bind.DatatypeConverter.printBase64Binary(os.toByteArray());
     		return serialized;
    	}
		return "";
    	
    	
    	
    }
    public static NBTTagCompound StringtoNBT(String source){
    	
    	
    	InputStream is = new java.io.ByteArrayInputStream(source.getBytes());
    	
    	
    	XMLDecoder xml = new XMLDecoder(is);
    	Object readobject = xml.readObject();
    	xml.close();
    	if(readobject instanceof NBTTagCompound){ return (NBTTagCompound) readobject;}
    	
    	return null;
    }
   public static String SaveNBT(ItemStack istack){
    	
	   if(istack instanceof CraftItemStack){
		      
		   CraftItemStack crafty = (CraftItemStack)istack;
		   net.minecraft.server.ItemStack usestack=crafty.getHandle();
		   
		   
		   
		   
		   
	   }
	   
	   return ""; //empty string indicates no NBT tag data.
	   
   }
    public static String[] getLore() {
        NBTTagCompound tag = itemStack.tag;
        NBTTagList list = tag.getCompound("display").getList("Lore");
        ArrayList<String> strings = new ArrayList<String>();
        String[] lores = new String[] {};
        for (int i = 0; i < strings.size(); i++)
            strings.add(((NBTTagString) list.get(i)).data);
        strings.toArray(lores);
        return lores;
    }
   
    public static org.bukkit.inventory.ItemStack getItemStack() {
        return craftStack;
    }
}