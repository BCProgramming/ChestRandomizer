package com.BASeCamp.SurvivalChests;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.server.Item;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;
import net.minecraft.server.PotionBrewer;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionBrewer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//RandomData class. Holds random probability data for a single item.
//This item can optionally have an additional list of possible enchantments.

public class RandomData {

	
		
		
		
		
	
	
	private int _SpawnType=0; //0 == normal, 1==potion,2==Head
	private float _Weighting = 1f;
	private String _Name;
	private int _ItemID;
	private float _DamageMin;
	private float _DamageMax;
	private int _MinCount;
	private int _MaxCount;
	private byte _Data;
	private String _Lore = "";
	private EnchantmentProbability _enchantmentprob = null;
	private boolean _SuperEnchantment;
	public float getWeighting(){return _Weighting;}
	public void setWeighting(float pweight){_Weighting=pweight;}
	public byte getData(){return _Data;}
	public void setData(byte value){_Data=value;}
	public String getName(){return _Name;}
	public void setName(String value){ _Name=value;}
	public int getItemID(){ return _ItemID;}
	public void setItemID(int value)	{_ItemID=value;	}
	public float getDamageMin(){return _DamageMin;}
	public void setDamageMin(float value){_DamageMin=value;}
	public float getDamageMax(){return _DamageMax;}
	public void setDamageMax(float value){_DamageMax=value;}
	public int getMinCount(){return _MinCount;}
	public void setMinCount(int value){_MinCount=value;}
	public int getMaxCount(){return _MaxCount;}
	public void setMaxCount(int value){_MaxCount=value;}
	public String getLore(){return _Lore;}
	public void setLore(String value){ _Lore=value;}
	public EnchantmentProbability getEnchantmentInformation() { return _enchantmentprob;}
	public HashMap<String,Integer> staticenchants = new HashMap<String,Integer>();
	
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
	
	public static CraftItemStack getHead(String headname) {
		CraftItemStack head;
		try {
		head = new CraftItemStack(Material.SKULL_ITEM,1,(short)3);
		} catch (NullPointerException e) {
			head = new CraftItemStack(Material.LEATHER_HELMET,1,(short)55);
		}	
		NBTTagCompound headNBT = new NBTTagCompound();
		headNBT.setString("SkullOwner", headname);
		head.getHandle().tag = headNBT;
		head.setAmount(1);
		return head;
		}
	public static boolean isHead(ItemStack testitem){
		return testitem.getType().equals(Material.SKULL_ITEM) ||
		testitem.getType().equals(Material.SKULL);
			
			
			
		
		
	}
	public static boolean isDye(ItemStack source){
	return source.getType().equals(Material.INK_SACK);
	
	}
	public static String getDyeName(ItemStack source){
		
		if(isDye(source)){
		Dye sourcedye = new Dye(source.getData().getData());
		
		if(sourcedye.getColor()==DyeColor.BLACK)
			return "Ink Sac";
		else if(sourcedye.getColor()==DyeColor.BLUE)
			return "Lapis Lazuli";
		else if(sourcedye.getColor()==DyeColor.BROWN)
			return "Coca Beans";
		else if(sourcedye.getColor()==DyeColor.CYAN)
			return "Cyan Dye";
		else if(sourcedye.getColor()==DyeColor.GRAY)
			return "Gray Dye";
		else if(sourcedye.getColor()==DyeColor.GREEN)
			return "Cactus Green";
		else if(sourcedye.getColor()==DyeColor.LIGHT_BLUE)
			return "Light Blue Dye";
		else if(sourcedye.getColor()==DyeColor.LIME)
			return "Lime Dye";
		else if(sourcedye.getColor()==DyeColor.MAGENTA)
			return "Magenta Dye";
		else if(sourcedye.getColor()==DyeColor.ORANGE)
			return "Orange Dye";
		else if(sourcedye.getColor() == DyeColor.PINK)
			return "Pink Dye";
		else if(sourcedye.getColor() == DyeColor.PURPLE)
			return "Purple Dye";
		else if(sourcedye.getColor()==DyeColor.RED)
			return "Rose Red";
		else if(sourcedye.getColor() == DyeColor.SILVER)
			return "Light Gray Dye";
		else if(sourcedye.getColor()==DyeColor.WHITE)
			return "Bone Meal";
		else if(sourcedye.getColor()==DyeColor.YELLOW)
			return "Dandelion Yellow";
		return "Unknown Dye";
		
		
		}
		return null;
		
	}
	private static CraftItemStack toCraftStack(ItemStack source){
		if(source instanceof CraftItemStack)
			return (CraftItemStack)source;
		else
			return new CraftItemStack(source);
		
	}
	public static String getHeadName(ItemStack source){
		//retrieves the name of the users head being represented.
		//only applicable for Heads.
		if(!isHead(source))
			return null;
		if(source.getDurability()==0){
		//skellie
			return "Skeleton Head";
		}
		else if(source.getDurability() == 1){
			//wither skellie
			return "Wither Skull";
		}
		else if(source.getDurability()==2){
			//zombie
			return "Zombie Head";
		}
		else if(source.getDurability()==4){
			//creeper
			return "Creeper Head";
		}
		else if(source.getDurability()==3 || source.getDurability() > 3){
		
			CraftItemStack cstack = toCraftStack(source);
		
		NBTTagCompound headNBT = cstack.getHandle().getTag();
		if(headNBT!=null){
			String ownerName =headNBT.getString("SkullOwner");
			if(ownerName==null || ownerName.length()==0) ownerName="Steve?";
			return  ownerName + "'s Head";
			
		}
		else
		{
			BCRandomizer.emitmessage("Head's tag was Null...");
			return "Human Head";
		}
		}
		
		return "Unknown Head";
		
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
	public Material getItemMaterial(){
		
		ItemStack generated = Generate();
		if(generated!=null) return generated.getType();
		return null;
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
			
			//BCRandomizer.emitmessage("Potion Data Value:" + _Data);
			//BCRandomizer.emitmessage("Potion Extended:" + _DamageMin);
			//BCRandomizer.emitmessage("Potion Level:" + _DamageMax);
			//BCRandomizer.emitmessage("Potion Splash:" + _MinCount);
			//PotionType pt = PotionType.getByDamageValue(_Data);
			if(pt!=null)
			{
			BCRandomizer.emitmessage("Potion Type:" + pt.name());
			}
			Potion makepotion = new Potion(pt);
			// Potions: Type is DataValue, Extended is DamageMin, Level is DamageMax, and Splash is MinCount.
			try {
				if(_DamageMin==1) _DamageMin=(int)(20f*(1.5f*60f)); //default 1 minute 3 seconds.
				CraftPotionBrewer cp = new CraftPotionBrewer();
				PotionEffect effect = cp.createEffect(pet, (int)_DamageMin, (int)_DamageMax);
				
				makepotion.getEffects().clear();
				makepotion.getEffects().add(effect);
				
				makepotion.setSplash(_MinCount > 0);
				
			}catch(IllegalArgumentException ex){}
			try {makepotion.apply(createitem);}catch(IllegalArgumentException ex){}
			}
		}
		if(_SpawnType==2){
			createitem = new ItemStack(397,1,(short) 3);
			createitem = getHead(_Name);
			
			
			
			
		}
		else if(_SpawnType==0){
			if(_ItemID<=0){
				Bukkit.broadcastMessage("SurvivalChests: Error: ItemID in config file is 0!");
				BCRandomizer.emitmessage("itemID has value of 0...");
				
				
			}
			//type,amount,damage,data
			int amountset = 1;
			if(_MaxCount==_MinCount) {
				if(_MaxCount==0) _MaxCount=1;
				amountset=_MaxCount;}
			else
				amountset = rgen.nextInt(_MaxCount-_MinCount) + _MinCount;
			
				int durabilityset = 0;
			if(_DamageMin==_DamageMax)
				durabilityset =  (short)_DamageMax;
			else{
				
				ItemStack temp = new ItemStack(_ItemID,amountset);
				short maxdir = temp.getType().getMaxDurability();
				short mindir = 0;
				float randval = (rgen.nextFloat()*(_DamageMax-_DamageMin))+_DamageMin;
				//BCRandomizer.emitmessage("randval=" + randval);
				durabilityset = (short)(((float)temp.getType().getMaxDurability())*
						randval)
						;
				
				
			//	durabilityset = (short)(rgen.nextInt(_DamageMax-_DamageMin)+_DamageMin);
				
			}
			BCRandomizer.emitmessage("durability set to " + durabilityset + "Min/max " + _DamageMin + " " + _DamageMax);
		createitem = new ItemStack(_ItemID,amountset,(short)durabilityset,_Data);
		//createitem.getData().setData(_Data);
		if(durabilityset > 0)
			createitem.setDurability((short) durabilityset);
		
		
		
		for(String statics:staticenchants.keySet()){
			
			createitem.addUnsafeEnchantment(EnchantmentProbability.EnchantmentMapping.get(statics), staticenchants.get(statics).intValue());
			
		}
		
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
			
		_enchantmentprob.Apply(createitem);
		}
		
		}
		
		
		catch(Exception exx){
			
			exx.printStackTrace();
			
			
		} //ignore errors...
		String usename=_Name;
		String uselore = _Lore;
		
		
		if(usename.startsWith("!")){
		usename=usename.substring(1);
		//BCRandomizer.emitmessage("usename=" + usename);
		if(usename.contains("%CLEVERAXENAME%")){
			
			usename = usename.replace("%CLEVERAXENAME%",
					NameGenerator.GenerateName(NameGenerator.Axe, NameGenerator.Adjectives,NameGenerator.Verbs));
			
		}
		if(usename.contains("%CLEVERPICKAXENAME")) {
			
			usename = usename.replace("%CLEVERPICKAXENAME%",
					NameGenerator.GenerateName(NameGenerator.Pickaxe,NameGenerator.Adjectives,NameGenerator.Verbs));
			
			
		}
		if(usename.contains("%CLEVERHOENAME%")) {
			
			usename = usename.replace("%CLEVERHOENAME%",
					NameGenerator.GenerateName(NameGenerator.Hoe, NameGenerator.Adjectives,NameGenerator.Verbs));
			
			
		}
		if(usename.contains("%CLEVERSHOVELNAME%")){
			usename = usename.replace("%CLEVERSHOVELNAME%",
					NameGenerator.GenerateName(NameGenerator.Shovel,NameGenerator.Adjectives,NameGenerator.Verbs));
			
			
		}
		if(usename.contains("%CLEVERSHEARSNAME%")){
			usename = usename.replace("%CLEVERSHEARSNAME%",
					NameGenerator.GenerateName(new String[]{"Shears","Scissors","Cutters","Safety Scissors"},NameGenerator.Adjectives,NameGenerator.Verbs));
			
			
		}
		if(usename.contains("%CLEVERSIGNNAME%"))
		{
		usename = usename.replace("%CLEVERSIGNNAME%", 
				NameGenerator.GenerateName(new String[]{"Reader","Sign","BattleSign","Signage","Hinter"}, NameGenerator.Adjectives, NameGenerator.Verbs));	
			
		
		}
		
		if(usename.contains("%CLEVERHATNAME%")){
			usename = usename.replace("%CLEVERHATNAME%",
					NameGenerator.GenerateName(NameGenerator.Hats, 
					NameGenerator.Adjectives, NameGenerator.Verbs));
			}
			
			
		
		if(usename.contains("%CLEVERCHESTPLATENAME%"))	{
			usename = usename.replace("%CLEVERCHESTPLATENAME%",NameGenerator.GenerateName(NameGenerator.Chestplates,
					NameGenerator.Adjectives,NameGenerator.Verbs));
			
			
		}
		if(usename.contains("%CLEVERLEGGINGSNAME%")) {
			usename = usename.replace("%CLEVERLEGGINGSNAME%",
					NameGenerator.GenerateName(NameGenerator.Pants,
					NameGenerator.Adjectives,NameGenerator.Verbs));
			
		}
		if(usename.contains("%CLEVERBOOTSNAME%")) {
			usename = usename.replace("%CLEVERBOOTSNAME%",
					NameGenerator.GenerateName(NameGenerator.Boots,
					NameGenerator.Adjectives,NameGenerator.Verbs));
			
		}
		if(usename.contains("%CLEVERSWORDNAME%")){
			usename = usename.replace("%CLEVERSWORDNAME%",NameGenerator.GenerateName(NameGenerator.Sword
			,NameGenerator.Adjectives,NameGenerator.Verbs));
		}
		if(usename.contains("%CLEVERBOWNAME%")){
			usename = usename.replace("%CLEVERBOWNAME%",NameGenerator.GenerateName(NameGenerator.Bow,
					NameGenerator.Adjectives,NameGenerator.Verbs));
			
			
		}
		//BCRandomizer.emitmessage("usename result=" + usename);
		
		if(!usename.trim().equals("") && uselore.trim().equals("")){
			
			if(RandomData.rgen.nextFloat()>0.9f){
				
				uselore = createSpecialLore();
				//Bukkit.getLogger().info("Created random Lore, " + uselore);
				
			}
			
		}
		
	
		
		
		createitem = setItemNameAndLore(createitem,usename,uselore);
			
		
		}
		
		
		
	
				
		
		
		}//spawntype ==0
		
		createitem = DyeLeather(createitem);
		
		return createitem;
		}
		catch(Exception exx)
		{
			BCRandomizer.emitmessage("Exception with " + _Name  + exx.toString()) ;
		
			return null;
		}
		
	}
	public ItemStack setItemNameAndLore(ItemStack item,String name,String Lore){
		
		
		return ItemNamer.renameItem(item,name,Lore);
		
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
		
	public RandomData(Element xmlelement){
		
		
		//the "core" element type tagname isn't particularly important, but we do need
		//to inspect the "type" attribute.
		//<RandomData type="item" Name="Wooden Sword" ID=
		String typeattr = "item";
		if(xmlelement.hasAttribute("type"))
			typeattr = xmlelement.getAttribute("type");
		
		if(xmlelement.hasAttribute("Name")){
			
			_Name = xmlelement.getAttribute("Name");
			
		}
		if(xmlelement.hasAttribute("Lore")){
			_Lore = xmlelement.getAttribute("Lore");
			
		}
		if(xmlelement.hasAttribute("Weight")){
			_Weighting = Float.parseFloat(xmlelement.getAttribute("Weight")); 
			
		}

		//type can be item, potion or head.
		if(typeattr.equalsIgnoreCase("item")){
			//item Random Data type.
			
			//Attributes:
			//name, ID, Data,MinDamage,MaxDamage,MinCount,MaxCount,Weight
			
			
			if(xmlelement.hasAttribute("id")){
				
				_ItemID = Integer.parseInt(xmlelement.getAttribute("id"));
				
			}if(xmlelement.hasAttribute("data")){
				
				_Data = Byte.parseByte(xmlelement.getAttribute("data"));
				
			}
			if(xmlelement.hasAttribute("mindamage")){
				
				_DamageMin = Integer.parseInt(xmlelement.getAttribute("minfamage"));
			}
			if(xmlelement.hasAttribute("maxdamage")){
				_DamageMax = Integer.parseInt(xmlelement.getAttribute("maxdamage"));
				
			}
			if(xmlelement.hasAttribute("mincount")){
				_MinCount = Integer.parseInt(xmlelement.getAttribute("mincount"));
				
			}
			if(xmlelement.hasAttribute("maxcount")){
				_MaxCount = Integer.parseInt(xmlelement.getAttribute("maxcount"));
			}
		
			
			
		}
		else if(typeattr.equalsIgnoreCase("potion")){
			//potion random data type.
			
			//potion type is _Data, extendedduration is DamageMin, Level is DamageMax, Splash is MinCount.
			//
			//effect
			_ItemID = Material.POTION.getId();
			if(xmlelement.hasAttribute("effect")){
				_Data = Byte.parseByte(xmlelement.getAttribute("effect"));
			}
			if(xmlelement.hasAttribute("duration")){
				_DamageMin = Integer.parseInt(xmlelement.getAttribute("duration"));
				
			}
			if(xmlelement.hasAttribute("level")){
				
				_DamageMax = Integer.parseInt(xmlelement.getAttribute("level"));
				
			}
			if(xmlelement.hasAttribute("splash")){
				
				_MinCount = Boolean.parseBoolean(xmlelement.getAttribute("splash"))?1:0;
				
				
			}
		}
		else if(typeattr.equalsIgnoreCase("head")){
			
			//head item type.
			//Head Person name is Name, Weight is, well, weight.
			_ItemID = Material.SKULL_ITEM.getId();

			
			
			
		}
		
		//enchantments are listed as a "<enchant>" subkey.
		NodeList enchantments = xmlelement.getElementsByTagName("enchant");
		
		for(int i=0;i<enchantments.getLength();i++){
			
			
			Node gotnode = enchantments.item(i);
			if(gotnode instanceof Element){
				//example: example
				Element enchantelement = (Element)gotnode;
				//<enchant name="SHARPNESS" Level="2" Weight="100" /> 
				
				String enchantname = "NONE";
					enchantname = enchantelement.getAttribute("name");
				int useLevel=1;
				if(enchantelement.hasAttribute("level"))
					useLevel = Integer.parseInt(enchantelement.getAttribute("level"));
				
				float useWeight=100; ;
				if(enchantelement.hasAttribute("weight"))
					useWeight = Float.parseFloat(enchantelement.getAttribute("weight"));
				
				
				boolean isstatic = false;
				if(enchantelement.hasAttribute("static")){
					
					isstatic = Boolean.parseBoolean(enchantelement.getAttribute("static"));
					
					
				}
				
				if(isstatic){
					staticenchants.put(enchantname, useLevel);	
				
				}
				else {
					if(_enchantmentprob==null) _enchantmentprob = new EnchantmentProbability();
				_enchantmentprob.setProbability(enchantname, useLevel,useWeight);
				}
				//_enchantmentprob.setProbability(EnchantmentName, Probability)
				
			}
			
			
			
		}
		
		
		
		
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
		String lastelement = splitresult[splitresult.length-1];
		//if lastelement starts with !D, format is:
		//!D(10-50) which indicates the percentage durability range that the item can be generated with.
		
		
		_Name = splitresult[0]; //if Name doesn't start with "!", then no name will be set in the NBT Data.
		_Weighting = Float.parseFloat(splitresult[1]);
		_ItemID = Integer.parseInt(splitresult[2]);
		_Data = Byte.parseByte(splitresult[3]);
		BCRandomizer.emitmessage("_Data set from " + splitresult[3] + " to " + _Data);
		_DamageMin = Float.parseFloat(splitresult[4]);
		_DamageMax = Float.parseFloat(splitresult[5]);
		BCRandomizer.emitmessage("Damage Min set to " + _DamageMin + " from " + splitresult[3]);
		BCRandomizer.emitmessage("Damage Max set to " + _DamageMax + " from " + splitresult[4]);
		_MinCount = Integer.parseInt(splitresult[6]);
		_MaxCount = Integer.parseInt(splitresult[7]);
		if(splitresult.length > 8)
			_Lore = splitresult[8];
		if(splitresult.length>9){
			//if we have more than 6, than we have enchantments and probabilities.
			
			for(int i=9;i<splitresult.length-1;i+=2){
			//current element is enchant name,
				String enchname = splitresult[i];
				//BCRandomizer.emitmessage("Enchantmentname=" + enchname);
				//BCRandomizer.emitmessage("weight:" + splitresult[i+1]);
			//next item is the probability weight.
				if(enchname.startsWith("!")){
				//if it starts with an exclamation mark, this is static and we want to always add it.	
					int enchantlevel = Integer.parseInt(splitresult[i+1]);
					staticenchants.put(enchname.substring(1), enchantlevel);
				
				} else {
				float probabilityweight = Float.parseFloat(splitresult[i+1]);
				//and add this one in!
				_enchantmentprob.setProbability(enchname, probabilityweight);
				}
				
			}
			_enchantmentprob.preCache();
		}
			
			
		
		}
		catch(Exception exx){
			//BCRandomizer.emitmessage("Error in RandomData class...");
			
		}
		
		
		
	}
	
	
	
	
	
	
	
	public static Random rgen = new Random();
	public static RandomData Choose(List<RandomData> rdata){
		
		
		//create arrays...
		RandomData[] choosefrom = new RandomData[rdata.size()];
		float[] weights = new float[rdata.size()];
		
		for(int i=0;i<choosefrom.length;i++){
			choosefrom[i] = rdata.get(i);
			weights[i] = choosefrom[i].getWeighting();
			
			
		}
		
		return Choose(choosefrom,weights);
		
		
		
		
	}
	public static <T> T Choose(T[] selectfrom){
		float[] probabilities = new float[selectfrom.length];
		for(int i=0;i<probabilities.length;i++)
			probabilities[i] = 1f;
		
		return Choose(selectfrom,probabilities);
		
		
		
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
		
		
		//BCRandomizer.emitmessage("totalsum=" + totalsum + " randomvalue=" + randomvalue);
		return null;
		
	}
		
	
}
