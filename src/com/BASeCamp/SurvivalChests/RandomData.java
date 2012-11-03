package com.BASeCamp.SurvivalChests;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import net.minecraft.server.Item;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;
import net.minecraft.server.PotionBrewer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

//RandomData class. Holds random probability data for a single item.
//This item can optionally have an additional list of possible enchantments.

public class RandomData {

	
	private int _SpawnType=0; //0 == normal, 1==potion,2==Head
	private float _Weighting = 1f;
	private String _Name;
	private int _ItemID;
	private int _DamageMin;
	private int _DamageMax;
	private int _MinCount;
	private int _MaxCount;
	private int _Data;
	private String _Lore = "";
	private EnchantmentProbability _enchantmentprob = null;
	private boolean _SuperEnchantment;
	public float getWeighting(){return _Weighting;}
	public void setWeighting(float pweight){_Weighting=pweight;}
	public int getData(){return _Data;}
	public void setData(int value){_Data=value;}
	public String getName(){return _Name;}
	public void setName(String value){ _Name=value;}
	public int getItemID(){ return _ItemID;}
	public void setItemID(int value)	{_ItemID=value;	}
	public int getDamageMin(){return _DamageMin;}
	public void setDamageMin(int value){_DamageMin=value;}
	public int getDamageMax(){return _DamageMax;}
	public void setDamageMax(int value){_DamageMax=value;}
	public int getMinCount(){return _MinCount;}
	public void setMinCount(int value){_MinCount=value;}
	public int getMaxCount(){return _MaxCount;}
	public void setMaxCount(int value){_MaxCount=value;}
	public String getLore(){return _Lore;}
	public void setLore(String value){ _Lore=value;}
	
	
	
	public static RandomData ChooseRandomData(List<RandomData> FromList){
		
		//iterate through all the items, and create an array of probabilities and the corresponding RandomData.
		float[] probabilities;
		RandomData[] rdata=new RandomData[FromList.size()];
		
		FromList.toArray(rdata);
		probabilities = new float[rdata.length];
		for(int i=0;i<rdata.length;i++)
			probabilities[i] = rdata[i].getWeighting();
		
		
		
		return RandomData.Choose(rdata, probabilities);
		
	}
	private String GenerateCleverName(String ItemType){
		
		return ItemType + "Powerful " + ItemType;	
	}
	private String GenerateCleverLore(String ItemType){
		
	return ItemType + "has extra power :D";
	}
	public CraftItemStack getHead(String headname) {
		CraftItemStack head;
		try {
		head = new CraftItemStack(Material.SKULL_ITEM,1,(short)3);
		} catch (NullPointerException e) {
			head = new CraftItemStack(Material.LEATHER_HELMET,1,(short)55);
		}	
		NBTTagCompound headNBT = new NBTTagCompound();
		headNBT.setString("SkullOwner", headname);
		head.getHandle().tag = headNBT;
		return head;
		}
	private PotionEffectType MapPotionType(String TypeName)
	{
		TypeName=TypeName.toUpperCase();
		if(TypeName.equalsIgnoreCase("BLINDNESS")) return PotionEffectType.BLINDNESS;
		if(TypeName.equalsIgnoreCase("CONFUSION")) return PotionEffectType.CONFUSION;
		if(TypeName.equalsIgnoreCase("RESISTANCE")) return PotionEffectType.DAMAGE_RESISTANCE;
		if(TypeName.equalsIgnoreCase("HASTE")) return PotionEffectType.FAST_DIGGING;
		if(TypeName.equalsIgnoreCase("FIRERESIST")) return PotionEffectType.FIRE_RESISTANCE;
		if(TypeName.equalsIgnoreCase("DAMAGE")) return PotionEffectType.HARM;
		if(TypeName.equalsIgnoreCase("HEAL")) return PotionEffectType.HEAL;
		if(TypeName.equalsIgnoreCase("HUNGER")) return PotionEffectType.HUNGER;
		if(TypeName.equalsIgnoreCase("STRENGTH")) return PotionEffectType.INCREASE_DAMAGE;
		if(TypeName.equalsIgnoreCase("INVISIBILITY")) return PotionEffectType.INVISIBILITY;
		if(TypeName.equalsIgnoreCase("JUMP")) return PotionEffectType.JUMP;
		if(TypeName.equalsIgnoreCase("NIGHTVISION")) return PotionEffectType.NIGHT_VISION;
		if(TypeName.equalsIgnoreCase("POISON")) return PotionEffectType.POISON;
		if(TypeName.equalsIgnoreCase("REGENERATION")) return PotionEffectType.REGENERATION;
		if(TypeName.equalsIgnoreCase("SLOWNESS")) return PotionEffectType.SLOW;
		if(TypeName.equalsIgnoreCase("SPEED")) return PotionEffectType.SPEED;
		if(TypeName.equalsIgnoreCase("RESPIRATION")) return PotionEffectType.WATER_BREATHING;
		if(TypeName.equalsIgnoreCase("WITHER")) return PotionEffectType.WITHER;
		
		return null;
		
	}
	private PotionType MapPotion(String TypeName)
	{
		
		TypeName=TypeName.toUpperCase();
		if(TypeName.equalsIgnoreCase("FIRERESIST")){return PotionType.FIRE_RESISTANCE;}
		if(TypeName.equalsIgnoreCase("DAMAGE")) {return PotionType.INSTANT_DAMAGE;}
		if(TypeName.equalsIgnoreCase("HEAL")) return PotionType.INSTANT_HEAL;
		if(TypeName.equalsIgnoreCase("POISON")) return PotionType.POISON;
		if(TypeName.equalsIgnoreCase("REGENERATION")) return PotionType.REGEN;
		if(TypeName.equalsIgnoreCase("SLOWNESS")) return PotionType.SLOWNESS;
		if(TypeName.equalsIgnoreCase("SPEED")) return PotionType.SPEED;
		if(TypeName.equalsIgnoreCase("STRENGTH")) return PotionType.STRENGTH;
		if(TypeName.equalsIgnoreCase("WATER")) return PotionType.WATER;
		if(TypeName.equalsIgnoreCase("WEAKNESS")) return PotionType.WEAKNESS;
		return null;
		
	}
	private String createSpecialLore(){
		
		String initial = NameGenerator.GenerateLore();
		return initial;
		
		
	}
	public ItemStack Generate()
	{
		//Potion.getBrewer().createEffect(PotionEffectType., arg1, arg2)
		try {
		ItemStack createitem=null;
		
		if(_SpawnType==1){
			createitem = new ItemStack(373,1); //373 is potion
			if(_Name=="INVISIBILITY")
			{
				
				createitem.setDurability((short) 14);
				return createitem;
				
			
			}
			else {
			PotionEffectType pet = MapPotionType(_Name);
			PotionType pt = PotionType.getByEffect(pet);
			
			System.out.println("Potion Data Value:" + _Data);
			System.out.println("Potion Extended:" + _DamageMin);
			System.out.println("Potion Level:" + _DamageMax);
			System.out.println("Potion Splash:" + _MinCount);
			//PotionType pt = PotionType.getByDamageValue(_Data);
			if(pt!=null)
			{
			System.out.println("Potion Type:" + pt.name());
			}
			Potion makepotion = new Potion(pt);
			// Potions: Type is DataValue, Extended is DamageMin, Level is DamageMax, and Splash is MinCount.
			try {
			makepotion.setHasExtendedDuration(_DamageMin>0);
			}catch(IllegalArgumentException ex){}
			try {makepotion.setLevel(_DamageMax);}catch(IllegalArgumentException ex){}
			try {makepotion.setSplash(_MinCount>0);}catch(IllegalArgumentException ex){}
			try {makepotion.apply(createitem);}catch(IllegalArgumentException ex){}
			}
		}
		if(_SpawnType==2){
			createitem = new ItemStack(397,1,(short) 3);
			createitem = getHead(_Name);
			
			
			
			
		}
		else if(_SpawnType==0){
		createitem = new ItemStack(_ItemID);
		//we want to have multiple enchants possibly- we choose up to four.
		//of our _Name contains the string "of" though, we will up the probability of more enchantments, too.
		float[] probabilities = new float[]{60,20,10,5};
		Integer[] numenchants = new Integer[] {1,2,3,4};
		if(_Name.indexOf("of")>0){
			probabilities = new float[]{1,1,1,1}; 
			
		}
		else if(_SuperEnchantment)
		{
			
			probabilities = new float[]{0,0,20,60};
			
		}
		
		int numints = RandomData.Choose(numenchants,probabilities);
		try {
		for(int i=0;i<numints;i++){
			
		_enchantmentprob.Apply(createitem,_SuperEnchantment);
		}
		}
		catch(Exception exx){} //ignore errors...
		String usename=_Name;
		String uselore = _Lore;
		
		
		if(usename.startsWith("!")){
		usename=usename.substring(1);
		System.out.println("usename=" + usename);
		
		if(usename.equalsIgnoreCase("%CLEVERSIGNNAME%"))
		{
		usename = NameGenerator.GenerateName(new String[]{"Reader","Sign","BattleSign","Signage"}, NameGenerator.Adjectives, NameGenerator.Verbs);	
			
		
		}
		
		if(usename.equalsIgnoreCase("%CLEVERHATNAME%")){
			usename = NameGenerator.GenerateName(NameGenerator.Hats, 
					NameGenerator.Adjectives, NameGenerator.Verbs);
			}
			
			
		
		else if(usename.equalsIgnoreCase("%CLEVERCHESTPLATENAME%"))	{
			usename = NameGenerator.GenerateName(NameGenerator.Chestplates,
					NameGenerator.Adjectives,NameGenerator.Verbs);
			
			
		}
		else if(usename.equalsIgnoreCase("%CLEVERLEGGINGSNAME%")) {
			usename = NameGenerator.GenerateName(NameGenerator.Pants,
					NameGenerator.Adjectives,NameGenerator.Verbs);
			
		}
		else if(usename.equalsIgnoreCase("%CLEVERBOOTSNAME%")) {
			usename = NameGenerator.GenerateName(NameGenerator.Boots,
					NameGenerator.Adjectives,NameGenerator.Verbs);
			
		}
		else if(usename.equalsIgnoreCase("%CLEVERSWORDNAME%")){
			usename = NameGenerator.GenerateName(NameGenerator.Sword
			,NameGenerator.Adjectives,NameGenerator.Verbs);
		}
		else if(usename.equalsIgnoreCase("%CLEVERBOWNAME%")){
			usename = NameGenerator.GenerateName(NameGenerator.Bow,
					NameGenerator.Adjectives,NameGenerator.Verbs);
			
			
		}
		System.out.println("usename result=" + usename);
		
		if(!usename.trim().equals("") && uselore.trim().equals("")){
			
			if(RandomData.rgen.nextFloat()>0.6f){
				
				uselore = createSpecialLore();
				Bukkit.getLogger().info("Created random Lore, " + uselore);
				
			}
			
		}
		
		createitem = setItemNameAndLore(createitem,usename,uselore);
			
		
		}
		
		
		if(_DamageMin==_DamageMax)
			createitem.setDurability((short) _DamageMax);
		else
			createitem.setDurability((short)(rgen.nextInt(_DamageMax-_DamageMin)+_DamageMin));
				
		
		if(_MaxCount==_MinCount) {
			if(_MaxCount==0) _MaxCount=1;
			createitem.setAmount(_MaxCount);}
		else
			createitem.setAmount(rgen.nextInt(_MaxCount-_MinCount) + _MinCount);
		
		}//spawntype ==0
		
		createitem = DyeLeather(createitem);
		
		return createitem;
		}
		catch(Exception exx)
		{
			System.out.println("Exception with " + _Name  + exx.toString()) ;
		
			return null;
		}
		
	}
	public ItemStack setItemNameAndLore(ItemStack item,String name,String Lore){
		
		
		
		if(!name.trim().equalsIgnoreCase("")){
			ItemNamer.load(item);
			System.out.println("name of item set to \"" + name + "\"");
		ItemNamer.setName(name);
		}
		if(!Lore.trim().equalsIgnoreCase("")){
			//ItemNamer.load(item);
			System.out.println("Lore of item set to \"" + Lore + "\"");
		    ItemNamer.setLore(Lore); 
		}
		return ItemNamer.getItemStack();
	}
	 public static ItemStack setColor(ItemStack item, int color){
		 CraftItemStack craftStack = null;
		 color=Math.abs(color);
		 net.minecraft.server.ItemStack itemStack = null;
		 if (item instanceof CraftItemStack) {
		 craftStack = (CraftItemStack) item;
		 itemStack = craftStack.getHandle();
		 }
		 else if (item instanceof ItemStack) {
		 craftStack = new CraftItemStack(item);
		 itemStack = craftStack.getHandle();
		 }
		 NBTTagCompound tag = itemStack.tag;
		 if (tag == null) {
		 tag = new NBTTagCompound();
		 tag.setCompound("display", new NBTTagCompound());
		 itemStack.tag = tag;
		 }
		  
		 tag = itemStack.tag.getCompound("display");
		 System.out.println("dyed item to color " + color);
		 tag.setInt("color", color);
		 itemStack.tag.setCompound("display", tag);
		 return craftStack;
		 }
	public static ItemStack DyeLeather(ItemStack item){
		//Dye's a leather Item to a random color.
		if(item.getTypeId()==Item.LEATHER_HELMET.id ||
		item.getTypeId()==Item.LEATHER_CHESTPLATE.id ||
		item.getTypeId()==Item.LEATHER_LEGGINGS.id ||
		item.getTypeId()==Item.LEATHER_BOOTS.id){
			
			//int usecolor = Color.HSBtoRGB(RandomData.rgen.nextFloat(), RandomData.rgen.nextFloat()*0.5f+0.5f, 0.5f);
			int usecolor = RandomColor();
			return setColor(item,usecolor);
			
			
		}
			return item;
		
		
	}
	public static String toHex(int r, int g, int b) {
		return "" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b);
		}
		 
		private static String toBrowserHexValue(int number) {
		StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
		while (builder.length() < 2) {
		builder.append("0");
		}
		return builder.toString().toUpperCase();
	    }
		
		
	public static int RandomColor(){
		
		int r = RandomData.rgen.nextInt(256);
		int g = RandomData.rgen.nextInt(256);
		int b = RandomData.rgen.nextInt(256);
		return Integer.parseInt(toHex(r,g,b),16);
		
		
	}
		
	
	
	public RandomData(String Initializer){
		//initialize a RandomData instance. Initializer String is provided from the configuration file, and applies to all non-
		if(Initializer.startsWith("POTION:"))
		{
			_SpawnType=1;
			Initializer = Initializer.substring(7);
			
		}
		else if(Initializer.startsWith("HEAD:"))
		{
			_SpawnType=2;
			Initializer = Initializer.substring(5);
			
			
		}
		else if(Initializer.startsWith("SUPERENCHANT:")){
			_SuperEnchantment = true;
			Initializer = Initializer.substring(13);
		}
		if(Initializer.trim().length()==0) return;
		//initializer is the initialization line.
		//format:
		//Name,Weighting,ItemID,DataValue,DamageMin,DamageMax,MinCount,MaxCount, Enchantmentname,Enchantment probability, Enchantmentname, Enchantment Probability...
		//POTION:Name,Weighting,ItemID,Strength,SecondsLength,Splash
		//HEAD:Name,Weighting
		//potion type is _Data, extendedduration is DamageMin, Level is DamageMax, Splash is MinCount.
		//Head Person name is Name, Weight is, well, weight.
		
		//split the string, and parse/read each element to the appropriate field.
		//some fields will not reflect what they indicate. (For example, Potion or Head fields).
		//ideally this could be "fixed" by having a single base class with three derived classes, one for each "type" of line, but this works for the moment 
		//and that is a rather major refactoring.
		try {
		_enchantmentprob = new EnchantmentProbability();
		String[] splitresult = Initializer.split(",");
		_Name = splitresult[0]; //if Name doesn't start with "!", then no name will be set in the NBT Data.
		_Weighting = Float.parseFloat(splitresult[1]);
		_ItemID = Integer.parseInt(splitresult[2]);
		_Data = Integer.parseInt(splitresult[3]);
		_DamageMin = Integer.parseInt(splitresult[4]);
		_DamageMax = Integer.parseInt(splitresult[5]);
		_MinCount = Integer.parseInt(splitresult[6]);
		_MaxCount = Integer.parseInt(splitresult[7]);
		if(splitresult.length > 8)
			_Lore = splitresult[8];
		if(splitresult.length>9){
			//if we have more than 6, than we have enchantments and probabilities.
			
			for(int i=9;i<splitresult.length-1;i+=2){
			//current element is enchant name,
				String enchname = splitresult[i];
				System.out.println("Enchantmentname=" + enchname);
				System.out.println("weight:" + splitresult[i+1]);
			//next item is the probability weight.
				float probabilityweight = Float.parseFloat(splitresult[i+1]);
				//and add this one in!
				_enchantmentprob.setProbability(enchname, probabilityweight);
				
			}
		
		}
			
			
		
		}
		catch(Exception exx){
			//System.out.println("Error in RandomData class...");
			
		}
		
		
		
	}
	
	
	
	
	
	
	
	public static Random rgen = new Random();
	
	public static <T> T Choose(T[] selectfrom){
		
		float[] createarray = new float[selectfrom.length];
		for(int i=0;i<createarray.length;i++)
			createarray[i] = 1;
		
		return Choose(selectfrom,createarray);
		
		
	}
	
	public static <T> T Choose(T[] selectfrom,float[] probabilities){
		
		//First: get the full sum of "probabilities"...
		float[] accumsums = new float[probabilities.length];
		float totalsum = 0;
		for(int i=0;i<probabilities.length;i++)
		{
			accumsums[i] = totalsum;
			totalsum+=probabilities[i];
			
			
		}
		//we now have the sum, so choose a random value in that range (0 to totalsum).
		float randomvalue = rgen.nextFloat()*totalsum;
		//find the latest item larger than randomvalue.
		int y=accumsums.length;
		for(y=accumsums.length-1;y>=0;y--)
		{
		if(accumsums[y] < randomvalue)
			return selectfrom[y];
			
		
		}
		
		
		System.out.println("totalsum=" + totalsum + " randomvalue=" + randomvalue);
		return null;
		
	}
	
	
}
