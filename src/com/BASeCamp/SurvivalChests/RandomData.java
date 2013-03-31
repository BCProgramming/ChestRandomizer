package com.BASeCamp.SurvivalChests;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;




import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Builder;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Dye;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.BASeCamp.SurvivalChests.EnchantmentProbability.EnchantmentAssignmentData;

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
	private boolean _SingleEnchantment=false;
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
	
	public static ItemStack getHead(String headname) {

	    ItemStack skull = new ItemStack(Material.SKULL_ITEM);
	    SkullMeta meta = (SkullMeta) skull.getItemMeta();
	    meta.setOwner(headname);
	    skull.setItemMeta(meta);
	    return skull;
		
		
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
		
		
		String gotname=source.getItemMeta().getDisplayName();
		
		if(gotname!=null){
			String ownerName = gotname;
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
	public static ItemStack createRandomFireworkItem(int StackSize)
	{
		ArrayList<FireworkEffect> totaleffects = new ArrayList<FireworkEffect>();
		float currenthead = 5.0f;
		
		while(currenthead> 2)
		{
		
		
		Builder fb = FireworkEffect.builder();
		fb.flicker(RandomData.rgen.nextBoolean());
		fb.trail(RandomData.rgen.nextBoolean());
		FireworkEffect.Type[] selectabletypes = new FireworkEffect.Type[] {
				FireworkEffect.Type.BALL,
				FireworkEffect.Type.BALL_LARGE,
				FireworkEffect.Type.BURST,
				FireworkEffect.Type.CREEPER,
				FireworkEffect.Type.STAR};
		
		fb.with(RandomData.Choose(selectabletypes));
		//choose a number of initial colours, from 1 to 5.
		int initialcolours = RandomData.rgen.nextInt(4)+1;
		
		Color[] createdcolours = new Color[initialcolours];
		for(int i=0;i<createdcolours.length;i++){
		createdcolours[i] = Color.fromRGB(RandomData.rgen.nextInt(255), 
				RandomData.rgen.nextInt(255), RandomData.rgen.nextInt(255));
		
			
		}
		fb.withColor(createdcolours);
		
		//1/8 chance of having a fade colour.
		if(RandomData.rgen.nextFloat() < 0.1f){
			int fadecolourcount = RandomData.rgen.nextInt(4)+1;
			Color[] fadecolours = new Color[fadecolourcount];
			for(int i=0;i<fadecolours.length;i++){
			fadecolours[i] =Color.fromRGB(RandomData.rgen.nextInt(255), 
					RandomData.rgen.nextInt(255), RandomData.rgen.nextInt(255));
				
			}
			
			fb.withFade(fadecolours);
			
		}
		
			FireworkEffect result = fb.build();
			totaleffects.add(result);
			currenthead-=RandomData.rgen.nextFloat();
		}	
			
			
			ItemStack fireworkstack = new ItemStack(Material.FIREWORK,StackSize);
		
			FireworkMeta fm = (FireworkMeta)fireworkstack.getItemMeta();
			fm.addEffects(totaleffects);
			fm.setPower(RandomData.rgen.nextInt(3));
			fireworkstack.setItemMeta(fm);
			return fireworkstack;
			
			
		}
		
		
		
		
		
	private String getRoman(int value){
		
		//brute force...
		return value==0?"":
			value==1?"I":
				value==2?"II":
					value==3?"III":
						value==4?"IV":
							value==5?"V":
								value==6?"VI":
									value==7?"VII":
										value==8?"VIII":
											value==9?"IX":
												value==10?"X":String.valueOf(value);
		
		
		
	}
		
		
	private String getPotionEffectDescription(PotionEffect pe){
		
		
		
		
		String basetype = "";
		if(pe.getType()==PotionEffectType.BLINDNESS)
			basetype="Blindness";
		else if(pe.getType()==PotionEffectType.CONFUSION)
			basetype="Confusion";
		else if(pe.getType()==PotionEffectType.DAMAGE_RESISTANCE)
			basetype="Resistance";
		else if(pe.getType()==PotionEffectType.FAST_DIGGING)
			basetype="Haste";
		else if(pe.getType()==PotionEffectType.FIRE_RESISTANCE)
			basetype="Fire Resistance";
		else if(pe.getType()==PotionEffectType.HARM)
			basetype="Instant Damage";
		else if(pe.getType()==PotionEffectType.HEAL)
			basetype="Instant Health";
		else if(pe.getType()==PotionEffectType.HUNGER)
			basetype="Hunger";
		else if(pe.getType()==PotionEffectType.INCREASE_DAMAGE)
			basetype="Strength";
		else if(pe.getType()==PotionEffectType.INVISIBILITY)
			basetype="Invisibility";
		else if(pe.getType()==PotionEffectType.JUMP)
			basetype="Jump";
		else if(pe.getType()==PotionEffectType.NIGHT_VISION)
			basetype = "Night Vision";
		else if(pe.getType()==PotionEffectType.POISON)
			basetype = "Poison";
		else if(pe.getType()==PotionEffectType.REGENERATION)
			basetype="Regeneration";
		else if(pe.getType()==PotionEffectType.SLOW)
			basetype="Slowness";
		else if(pe.getType()==PotionEffectType.SLOW_DIGGING)
			basetype="Slow Digging";
		else if(pe.getType()==PotionEffectType.SPEED)
			basetype="Swiftness";
		else if(pe.getType()==PotionEffectType.WATER_BREATHING)
			basetype="Gills";
		else if(pe.getType()==PotionEffectType.WEAKNESS)
			basetype="Weakness";
		else if(pe.getType()==PotionEffectType.WITHER)
			basetype="Wither";
		
		//now that we have a basetype, add on the level...
		
		basetype = basetype + " " + getRoman(pe.getAmplifier()+1);
		
		if(pe.getDuration() > 0)
			basetype = basetype + " (" + 
		(pe.getDuration()/20)/60 + ":" +  
		String.format("00",(pe.getDuration()/20)%60) + ")";
		
		
		return basetype;
		
		
	}
		
		/**
		 * retrieves the Name of this Potion. This includes any level modifier, and whether it is a splash potion.
		 * sets the name and lore of this potion to reflect it's effects. 
		 */
	public void setPotionData(ItemStack source){
		if(source.getType()==Material.POTION){
			
			//we retrieve and set the name and lore for this potion.
			String basepotiontype="";
			
			//get the base potion type. retrieve metadata first.
			Potion p = Potion.fromItemStack(source);
			PotionMeta pm = (PotionMeta)source.getItemMeta();
			if(p.isSplash())
				basepotiontype = "Splash Potion";
			else
				basepotiontype = "Potion";
			
			//set the name...
			pm.setDisplayName(basepotiontype);
			
			List<PotionEffect> getnamesfor = pm.getCustomEffects();
			List<String> makelore = new LinkedList<String>();
			for(PotionEffect iterateeffect:getnamesfor){
				makelore.add(getPotionEffectDescription(iterateeffect));
			}
			
			
			pm.setLore(makelore);
			
			
			source.setItemMeta(pm);
			
			
			
			
		}
		else
		{
			throw new IllegalArgumentException("Cannot retrieve potion name for non-potion item:" + source.getType().name());
		}
		
		
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
		
		String initial = BCRandomizer.NameGen.GenerateLore();
		return initial;
		
		
	}
	public Material getItemMaterial(){
		
		ItemStack generated = Generate();
		if(generated!=null) return generated.getType();
		return null;
	}
	
	public void AddPotionEffect(ItemStack targetpotion,PotionEffectType potioneffect,int Level,int Duration){
		
		if(targetpotion.getType()==Material.POTION){
			
			PotionMeta pm = (PotionMeta) targetpotion.getItemMeta();
			
			PotionEffect useeffect = Potion.getBrewer().createEffect(potioneffect, Duration,Level);
			
			pm.addCustomEffect(useeffect,true);
			
			targetpotion.setItemMeta(pm);
			
			
			
		}
		
		
		
		
	}
	private ItemStack MakePotion(String Name,int Duration,int Level,int Splash){
		Duration*=20; //duration is # of seconds...
		BCRandomizer.emitmessage("Creating Potion- Name:" + Name + " Duration:" + Duration + " Level:" + Level + " Splash:" + Splash);
		PotionEffectType pet = MapPotionType(Name);
		PotionType pt = PotionType.getByEffect(pet);
	
		Potion makepotion = new Potion(pt);
		makepotion.setSplash(Splash>0);
		ItemStack getpotionstack = makepotion.toItemStack(1);
		AddPotionEffect(getpotionstack,MapPotionType(Name),Level,Duration);
		//setPotionData(getpotionstack);
		
		return getpotionstack;
		
		
	}
	/*private ItemStack MakePotion(String Name,int Duration,int Level,int Splash){
		Duration*=20;
		BCRandomizer.emitmessage("Creating Potion- Name:" + Name + " Duration:" + Duration + " Level:" + Level + " Splash:" + Splash);
		PotionEffectType pet = MapPotionType(Name);
		
		
		PotionType pt = PotionType.getByEffect(pet);
		System.out.println("Splash?=" + (Splash!=0));
		Potion makepotion = new Potion(pt);
		makepotion.setSplash(Splash!=0);
		ItemStack createpotion =null;
		try {
		makepotion.setHasExtendedDuration(Duration!=0);
		}catch(IllegalArgumentException iae){
		//do nothing...
		}
		createpotion = makepotion.toItemStack(1);
		PotionMeta pm = (PotionMeta)(createpotion.getItemMeta());
		pm.addCustomEffect(new PotionEffect(pet,Duration,Level), Splash!=0);
		createpotion.setItemMeta(pm);
		return createpotion;
		
		/*
		 *     ItemStack stack = new Potion(PotionType.STRENGTH).toItemStack(1);
    PotionMeta meta = (PotionMeta) stack.getItemMeta();
     
    meta.addCustomEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 680, 1), true);
    stack.setItemMeta(meta);
     
    target.getInventory().addItem(stack);}
		 */
		
		
		
	
	public ItemStack Generate()
	{
		//Potion.getBrewer().createEffect(PotionEffectType., arg1, arg2)
		try {
		ItemStack createitem=null;
		if(_ItemID==403){
			
			System.out.println("403...");
			
		}
		if(_SpawnType==1){
			
			
			//System.out.println("Making potion:" + _Name + " " + _DamageMin + " " + _DamageMax + " " + _MinCount);
			createitem = MakePotion(_Name,(int)_DamageMin,(int)_DamageMax+1,_MinCount);
			//
			if(rgen.nextBoolean()){
				int extraeffects = rgen.nextInt(4);
				for(int i=0;i<extraeffects;i++){
					
					PotionEffectType[] chooseme = PotionEffectType.values();
					
					AddPotionEffect(createitem, RandomData.Choose(chooseme), rgen.nextInt(5), 20*(rgen.nextInt(120)+30));
					
					
					
				}
				
				
				
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
		if(!_SingleEnchantment){
			int numints = RandomData.Choose(numenchants,probabilities);
			try {
			for(int i=0;i<numints;i++){
				
			_enchantmentprob.Apply(createitem);
			}
			
			}
			
			
			catch(Exception exx){
				
				exx.printStackTrace();
				System.out.println(exx);
				
			} //ignore errors...
		}
		else {
			try {
			//singleenchant: choose a single enchantment and crank it.
			EnchantmentAssignmentData[] copiedarray = new EnchantmentAssignmentData[_enchantmentprob.Enchantprobabilities.size()];
			_enchantmentprob.Enchantprobabilities.toArray(copiedarray);
			EnchantmentAssignmentData ead = RandomData.Choose(copiedarray);
			Enchantment selected = EnchantmentProbability.EnchantmentMapping.get(ead.getName());
			
			float[] chooselevels = new float[]{100,100,100,50,50,50,20,20,20,10};
			int setlevel = RandomData.Choose(new Integer[]{1,2,3,4,5,6,7,8,9,10},chooselevels);
			
			createitem.addUnsafeEnchantment(selected,setlevel);
			}
			catch(Exception erx){
				//System.out.println("Exception:" + erx.getClass().getName());
				erx.printStackTrace();
			}
		}
		String usename=_Name;
		String uselore = _Lore;
		
		
		if(usename.startsWith("!")){
		usename=usename.substring(1);
		//BCRandomizer.emitmessage("usename=" + usename);
		if(usename.contains("%CLEVERAXENAME%")){
			
			usename = usename.replace("%CLEVERAXENAME%",
					BCRandomizer.NameGen.GenerateName(BCRandomizer.NameGen.Axe, BCRandomizer.NameGen.Adjectives,BCRandomizer.NameGen.Verbs));
			
		}
		if(usename.contains("%CLEVERBOOKNAME%")){
			usename = usename.replace("%CLEVERBOOKNAME%",RandomData.ChooseString(BCRandomizer.NameGen.Adjectives) + " " + RandomData.ChooseString(BCRandomizer.NameGen.Books)); 
			
			
			
		}
		if(usename.contains("%CLEVERPICKAXENAME%")) {
			
			usename = usename.replace("%CLEVERPICKAXENAME%",
					BCRandomizer.NameGen.GenerateName(BCRandomizer.NameGen.Pickaxe,BCRandomizer.NameGen.Adjectives,BCRandomizer.NameGen.Verbs));
			
			
		}
		if(usename.contains("%CLEVERHOENAME%")) {
			
			usename = usename.replace("%CLEVERHOENAME%",
					BCRandomizer.NameGen.GenerateName(BCRandomizer.NameGen.Hoe, BCRandomizer.NameGen.Adjectives,BCRandomizer.NameGen.Verbs));
			
			
		}
		if(usename.contains("%CLEVERSHOVELNAME%")){
			usename = usename.replace("%CLEVERSHOVELNAME%",
					BCRandomizer.NameGen.GenerateName(BCRandomizer.NameGen.Shovel,BCRandomizer.NameGen.Adjectives,BCRandomizer.NameGen.Verbs));
			
			
		}
		if(usename.contains("%CLEVERSHEARSNAME%")){
			usename = usename.replace("%CLEVERSHEARSNAME%",
					BCRandomizer.NameGen.
					GenerateName(new LinkedList<String>(Arrays.asList("Shears","Scissors","Cutters","Safety Scissors")),
							BCRandomizer.NameGen.Adjectives,BCRandomizer.NameGen.Verbs));
			
			
		}
		if(usename.contains("%CLEVERSIGNNAME%"))
		{
		usename = usename.replace("%CLEVERSIGNNAME%", 
				BCRandomizer.NameGen.GenerateName(
						new LinkedList<String>(Arrays.asList("Reader","Sign","BattleSign","Signage","Hinter")),
						BCRandomizer.NameGen.Adjectives, BCRandomizer.NameGen.Verbs));	
			
		
		}
		
		if(usename.contains("%CLEVERHATNAME%")){
			usename = usename.replace("%CLEVERHATNAME%",
					BCRandomizer.NameGen.GenerateName(BCRandomizer.NameGen.Hats, 
							BCRandomizer.NameGen.Adjectives, BCRandomizer.NameGen.Verbs));
			}
			
			
		
		if(usename.contains("%CLEVERCHESTPLATENAME%"))	{
			usename = usename.replace("%CLEVERCHESTPLATENAME%",
					BCRandomizer.NameGen.GenerateName(BCRandomizer.NameGen.Chestplates,
							BCRandomizer.NameGen.Adjectives,BCRandomizer.NameGen.Verbs));
			
			
		}
		if(usename.contains("%CLEVERLEGGINGSNAME%")) {
			usename = usename.replace("%CLEVERLEGGINGSNAME%",
					BCRandomizer.NameGen.GenerateName(BCRandomizer.NameGen.Pants,
							BCRandomizer.NameGen.Adjectives,BCRandomizer.NameGen.Verbs));
			
		}
		if(usename.contains("%CLEVERBOOTSNAME%")) {
			usename = usename.replace("%CLEVERBOOTSNAME%",
					BCRandomizer.NameGen.GenerateName(BCRandomizer.NameGen.Boots,
							BCRandomizer.NameGen.Adjectives,BCRandomizer.NameGen.Verbs));
			
		}
		if(usename.contains("%CLEVERSWORDNAME%")){
			usename = usename.replace("%CLEVERSWORDNAME%",
					BCRandomizer.NameGen.GenerateName(BCRandomizer.NameGen.Sword
			,BCRandomizer.NameGen.Adjectives,BCRandomizer.NameGen.Verbs));
		}
		if(usename.contains("%CLEVERBOWNAME%")){
			usename = usename.replace("%CLEVERBOWNAME%",
					BCRandomizer.NameGen.GenerateName(BCRandomizer.NameGen.Bow,
							BCRandomizer.NameGen.Adjectives,BCRandomizer.NameGen.Verbs));
			
			
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
		 
		 LeatherArmorMeta lam = (LeatherArmorMeta)item.getItemMeta();
		 lam.setColor(Color.fromRGB(color));
		 item.setItemMeta(lam);
		 return item;
		 
		 }
	public static ItemStack DyeLeather(ItemStack item){
		//Dye's a leather Item to a random color.
		if(item.getType()==Material.LEATHER_HELMET ||
		item.getType()==Material.LEATHER_CHESTPLATE ||
		item.getType()==Material.LEATHER_LEGGINGS ||
		item.getType()==Material.LEATHER_BOOTS){
			
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
		boolean foundprefix=true;
		while(foundprefix){
			foundprefix=false;
			System.out.println(Initializer);
			if(Initializer.startsWith("POTION:"))
			{
				_SpawnType=1;
				Initializer = Initializer.substring(7);
				//System.out.println("Initializer:" + Initializer);
				foundprefix=true;
			}
			else if(Initializer.startsWith("HEAD:"))
			{
				_SpawnType=2;
				Initializer = Initializer.substring(5);
				foundprefix=true;
				
			}
			else if(Initializer.startsWith("SUPERENCHANT:")){
				_SuperEnchantment = true;
				Initializer = Initializer.substring(13);
				foundprefix=true;
				
			}
			else if(Initializer.startsWith("SINGLEENCHANT:")){
				System.out.println("Single Enchant");
				_SingleEnchantment=true;
				Initializer = Initializer.substring(14);
				foundprefix=true;
			}
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
		
		//createitem = MakePotion(_Name0,(int)_DamageMin4,(int)_DamageMax5+1,_MinCount6);
		//
		//
		_Name = splitresult[0]; //if Name doesn't start with "!", then no name will be set in the NBT Data.
		_Weighting = Float.parseFloat(splitresult[1]);
		_ItemID = Integer.parseInt(splitresult[2]);
		_Data = Byte.parseByte(splitresult[3]);
		BCRandomizer.emitmessage("_Data set from " + splitresult[3] + " to " + _Data);
		_DamageMin = Float.parseFloat(splitresult[4]);
		_DamageMax = Float.parseFloat(splitresult[5]);
		BCRandomizer.emitmessage("Damage Min set to " + _DamageMin + " from " + splitresult[3]);
		BCRandomizer.emitmessage("Damage Max set to " + _DamageMax + " from " + splitresult[4]);
		//System.out.println(_Name + " read in ");
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
			//System.out.println("Error in RandomData class..." + Initializer);
			//exx.printStackTrace();
			
		}
		
		
		
	}
	
	
	
	
	
	
	
	public static Random rgen = new Random();
	
	public static String ChooseString(List<String> rdata){
		
		String[] getlist= new String[rdata.size()]; 
		rdata.toArray(getlist);
		return Choose(getlist);
		
		
	}
	
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
