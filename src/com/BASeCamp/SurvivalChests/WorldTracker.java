package com.BASeCamp.SurvivalChests;

import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.Vector;
public class WorldTracker implements Listener  {

	private BCRandomizer _owner;
	
	private static Vector[] offsets = new Vector[] {
			
			new Vector(0,-1,0),
			new Vector(0,0,1),
			new Vector(0,0,-1),
			new Vector(-1,0,0),
			new Vector(1,0,0)
	};
	
	private static Hopper grabhopper(Block Source){
		if(Source.getState()==null) return null;
		return (Hopper)(Source.getState());
	}
	/**
	 * retrieves the Hopper source for this block, if any.
	 * @param blocksource
	 * @return Hopper Block that is unloading into this block, or null of no dispenser is found.
	 */
	
	
	public static Block getHopperSource(Block blocksource){
		//we want to look in each cardinal direction and upwards.
		int XPos = blocksource.getLocation().getBlockX();
		int YPos = blocksource.getLocation().getBlockY();
		int ZPos = blocksource.getLocation().getBlockZ();
		World w = blocksource.getWorld();
		Hopper AcquiredH;
		Block AboveBlock = w.getBlockAt(XPos,YPos+1,ZPos);
		
		Block NorthBlock = w.getBlockAt(XPos,YPos,ZPos+1);
		Block SouthBlock = w.getBlockAt(XPos,YPos,ZPos-1);
		Block WestBlock = w.getBlockAt(XPos-1,YPos,ZPos);
		Block EastBlock = w.getBlockAt(XPos+1,YPos,ZPos);
				Block[] testblocks = new Block[] {AboveBlock,NorthBlock,SouthBlock,WestBlock,EastBlock};
				
				
		for(Block testit:testblocks){
			AcquiredH=grabhopper(testit);
			if(getHopperOutput(AcquiredH).getLocation().equals(blocksource.getLocation()))
				return (Block)AcquiredH;			
		}
		return null;
		
		
		
	}
	
	public static Block getHopperInput(Hopper source){
		
		Location sourcespot = source.getBlock().getLocation();
		Location newspot = new Location(sourcespot.getWorld(),sourcespot.getBlockX(),sourcespot.getBlockY()+1,sourcespot.getBlockZ());
		return sourcespot.getWorld().getBlockAt(newspot);
		
		
	}
	//returns the block this hopper is pointing at.
	public static Block getHopperOutput(Hopper source){
		Location sourcespot = source.getBlock().getLocation();
		byte rawdata = source.getRawData();
		//0 is straight down
		//1 is SOUTH (Z+)
		//2 is NORTH (Z-)
		//4 is WEST (X-)
		//5 is EAST (X+)
		
		Vector useoffset = offsets[rawdata];
		
		Location outputblock = new Location(sourcespot.getWorld(),
				sourcespot.getBlockX()+useoffset.getBlockX(),
				sourcespot.getBlockY()+useoffset.getBlockY(),
				sourcespot.getBlockZ()+useoffset.getBlockZ());
		
		return sourcespot.getWorld().getBlockAt(outputblock);
	}
	
	public WorldTracker(BCRandomizer owner){
		_owner = owner;
		_owner.getServer().getPluginManager().registerEvents(this, _owner);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent bpe){
		Player p = bpe.getPlayer();
		if(bpe.getBlockPlaced().getType().equals(Material.HOPPER)){
			p.sendMessage("Hopper Placed.");
			Hopper h = (Hopper)bpe.getBlockPlaced().getState();
			p.sendMessage("Raw Data:" + h.getRawData());
			
			
		}
	}
	
	@EventHandler
	public void onWorldInit(WorldInitEvent wie){
		String testname = wie.getWorld().getName();
		System.out.println("onWorldInit:" + testname);
		boolean isarena=false;
		if(RandomizerCommand.ArenaNames!=null){
			for(String iterate:RandomizerCommand.ArenaNames){
				if(iterate.equalsIgnoreCase(testname)){
					isarena=true;
					break;
				}
			}
			
			
		}
		if(isarena){
			System.out.println("Arena Detected...");
			System.out.println("Populators:" + wie.getWorld().getPopulators().size());
			
			for(BlockPopulator bp:wie.getWorld().getPopulators()){
				System.out.println("BP:Class:" + bp.getClass().getName());
			}
			
			
		}
	}
	
	
}
