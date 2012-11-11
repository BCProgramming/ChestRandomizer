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
		Spot = iframe.getLocation();
		innards = iframe.getItem();
		Original=iframe;
		
		
	}
	//Attempts to regenerate the Item frame in the provided world.
	public void Regenerate(World genworld){
		ItemFrame iframe = (ItemFrame)(genworld.spawnEntity(Spot, EntityType.ITEM_FRAME));
		iframe.setItem(innards);
		iframe.setFacingDirection(attachedTo);
		
	}
	
}
