package com.BASeCamp.SurvivalChests;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

import com.BASeCamp.SurvivalChests.BCRandomizer;
import com.BASeCamp.SurvivalChests.RandomData;


public class ChestRandomizer {

	private BCRandomizer _owner = null;
	private Chest mchest;
	private Inventory mInventory;
	private int _MinItems = 1;
	private int _MaxItems = 10;
	public int getMinItems() { return _MinItems;}
	public void setMinItems(int value){_MinItems = value;}
	public int getMaxItems() { return _MaxItems;}
	public void setMaxItems(int value) { _MaxItems=value;}
	private static LinkedList<RandomData> randData = null;
	public static LinkedList<RandomData> addall = null; //items that will always be added once.
	private String _SourceFile = "";
	public ChestRandomizer(BCRandomizer owner,Chest pChest,String pURL){
		_owner = owner;
		mchest=pChest;
		mInventory = mchest.getBlockInventory();
		if(randData==null|| _SourceFile!=pURL)
		{
			_SourceFile=pURL;
			reload();
			
			
		}
	}
	public ChestRandomizer(BCRandomizer owner,Inventory sourceinventory,String pURL){
		
		_owner = owner;
		mchest = null;
		mInventory = sourceinventory;
		if(randData==null || _SourceFile!=pURL)
		{
			_SourceFile=pURL;
			reload();
			
			
		}
		
	}
	private void reload()
	{
		randData = new LinkedList<RandomData>();
		addall= new LinkedList<RandomData>();
		
			Scanner sc=null;
			
			try {
				
				if(_SourceFile!=""){
					
					sc = new Scanner(new URL(_SourceFile).openStream());
				
				}
				else if(new File(BCRandomizer.pluginMainDir).exists() && new File(BCRandomizer.pluginConfigLocation).exists()){
			//read each line.
			sc = new Scanner(new File(BCRandomizer.pluginConfigLocation));
		}
			}
			catch(Exception exx){sc=null;}
		if(sc==null)
		{
			Bukkit.getLogger().log(Level.WARNING, "Warning: Config/List file not found at " + BCRandomizer.pluginConfigLocation + " using built in.");
			sc = new Scanner(getClass().getClassLoader().getResourceAsStream("hungergames.cfg"));
		}
			
			while(sc.hasNextLine()){
				String lineread = sc.nextLine();
				if(!(lineread.startsWith("//")||lineread.trim().length()==0)){
					if(lineread.startsWith("STATIC:")){
					    lineread = lineread.substring(7);	
					    RandomData staticdata = new RandomData(lineread);
					    addall.add(staticdata);
					}
					else {
					
				RandomData gendata = new RandomData(lineread);
				randData.add(gendata);
					}
				}
				
			}
			
		
		System.out.println("Read in " + randData.size() + " elements");
		
		
		
		
		
	}
	public void clear()
	{
		
		Inventory gotinventory = mchest.getBlockInventory();
		//OK, loop the appropriate number of times, choose a randomData and generate it.
		gotinventory.clear();	
	
	}
	private Integer[] emptyslots(Inventory inv){
		Integer[] createit = new Integer[inv.getSize()];
		int currempty = 0;
		for(int i=0;i<inv.getSize();i++){
			if(inv.getItem(i)==null || inv.getItem(i).getTypeId()==0)
				createit[currempty]=new Integer(i);
			currempty++;
			
		}
		return createit;
		
		
		
	}
	public int Shuffle()
	{
		
		//if the block beneath the chest is wool, break out. We don't randomize chests with wool underneath.
		if(mchest!=null){
			Location chestspot = mchest.getLocation();
			Location spotbelow = new Location(mchest.getWorld(), chestspot.getX(), chestspot.getY()-1, chestspot.getZ());
			if(mchest.getWorld().getBlockAt(spotbelow).getType()==Material.WOOL){
			return 0;	
			}
		}
		
		
		//select a random number of items.
		
		if(_owner!=null){
			try {
			FileConfiguration fc = _owner.getConfig();
			_MaxItems = Integer.parseInt(fc.getString("maxgen"));
			_MinItems = Integer.parseInt(fc.getString("mingen"));
			}
			catch(NumberFormatException nfe){
				
				
			}
			}
	
		
		
		int _numgenerate=0;
		if(_MaxItems==_MinItems) _numgenerate = _MaxItems; else
		_numgenerate = RandomData.rgen.nextInt(_MaxItems-_MinItems)+_MinItems;
		
		
		Inventory gotinventory = mInventory;
		//OK, loop the appropriate number of times, choose a randomData and generate it.
		gotinventory.clear();
		//System.out.println("Minitems:" + _MinItems + "MaxItems " + _MaxItems + "Gen:" + _numgenerate);
		
		for(int i=1;i<_numgenerate;i++){
			RandomData rdata = RandomData.ChooseRandomData(randData);
			if(rdata!=null){
				
				ItemStack created = rdata.Generate();
				if(created!=null)
				{
				//get all empty slots.
					Integer[] empties = emptyslots(gotinventory);
					Integer selectedslot = RandomData.Choose(empties);
					if(selectedslot!=null){
					gotinventory.setItem(selectedslot,created);
					}
					else {
				    gotinventory.addItem(created);
					}
					
				}
			}
		
		}
		return gotinventory.getContents().length;
		
	}
}
