package com.BASeCamp.SurvivalChests;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
public class CachedFrameData {

	BlockFace attachedTo;
	Location Spot = null;
	ItemStack innards;
	ItemFrame Original;
	public CachedFrameData(ItemFrame iframe){
		
		attachedTo = iframe.getAttachedFace();
		
		Location lgrab = new Location(iframe.getWorld(), iframe.getLocation().getBlockX(),iframe.getLocation().getBlockY(),iframe.getLocation().getBlockZ());
		BlockFace gfacing = iframe.getFacing();
		int Xoffset=0;
		int Zoffset=0;
		//The z coordinate decreases as you move north and 
		//increases when you move south, 
		//while the x coordinate decreases as 
		//you move west and increases as you move east. 
		
		if(gfacing.equals(BlockFace.EAST)){
			
			Xoffset=1;
			
		}
	else if(gfacing.equals(BlockFace.WEST)){
		Xoffset=-1;
		
	}
	else if(gfacing.equals(BlockFace.NORTH)){
		Zoffset=-1;
		
	}
	else if(gfacing.equals(BlockFace.SOUTH)){
		Zoffset=1;
		
	}
		
		Spot = new Location(lgrab.getWorld(),lgrab.getX()+Xoffset,lgrab.getY(),lgrab.getZ()+Zoffset);
		innards = iframe.getItem();
		Original=iframe;
		
		
	}
	//Attempts to regenerate the Item frame in the provided world.
	public void Regenerate(World genworld){
		try {
		ItemFrame iframe = (ItemFrame)(genworld.spawnEntity(Spot, EntityType.ITEM_FRAME));
		iframe.setItem(innards);
		iframe.setFacingDirection(attachedTo);
		}
		catch(Exception exx)
		{
			System.out.println("Exception regenerating ItemFrame:");
			exx.printStackTrace();
			
			
		}
		
	}
	
}
