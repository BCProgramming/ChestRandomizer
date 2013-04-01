package com.BASeCamp.SurvivalChests;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class OutsideChestFeature implements ArenaGenerationFeature {

	@Override
	public void GenerateFeature(Location atLocation) {
		// TODO Auto-generated method stub
		//choose a random location.
		final int XPos = atLocation.getBlockX();
		final int YPos = atLocation.getBlockY();
		final int ZPos = atLocation.getBlockZ();
		final World inWorld = atLocation.getWorld();
		
		//create a pedestal, make sure we aren't over water.
		if(inWorld.getBlockAt(XPos,YPos-1,ZPos).getType().equals(Material.WATER)){
			return;
		}
			for(int xp = XPos-3;xp<XPos+3;xp++){
				for(int zp = ZPos-3;zp<ZPos+3;zp++){
					for(int yp=YPos+1;yp < YPos+5;yp++){
						inWorld.getBlockAt(xp,yp,zp).setType(Material.AIR);
					}
				}
			}
			
			//at that position, create a small checkerboard of obsidian and glowstone.
			for (int xp = XPos-3;xp<XPos+4;xp++){
				for(int zp = ZPos-3;zp<ZPos+4;zp++){
					//if the block at this location is not bedrock...
					Block atpos = inWorld.getBlockAt(xp,YPos,zp);
						if(!atpos.getType().equals(Material.BEDROCK)){
							
							
							Material usetype = ((xp+zp)%2==0)?Material.OBSIDIAN:Material.GLOWSTONE;
							
							atpos.setType(usetype);
						}
					
					
					
				}
				
				
				
			}
			int[] cornerX = new int[]{XPos-3,XPos+3};
			int[] cornerZ = new int[]{ZPos-3,ZPos+3};
			for(int cx:cornerX){
				for(int cz:cornerZ){
					
					for(int usey=YPos+1;usey<YPos+5;usey++){
						inWorld.getBlockAt(cx,usey,cz).setType(Material.NETHER_FENCE);
					}
					
					
				}
			}
			
			
			//
			
			
			//set Torches
			
			
			
		
		inWorld.getBlockAt(XPos,YPos+1,ZPos).setType(Material.COBBLE_WALL);
		Block setchest = inWorld.getBlockAt(XPos,YPos+2,ZPos);
		setchest.getChunk().load();
		setchest.setType(Material.CHEST);
	}

	
	
	
}
