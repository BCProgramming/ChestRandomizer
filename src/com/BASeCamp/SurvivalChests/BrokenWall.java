package com.BASeCamp.SurvivalChests;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class BrokenWall implements ArenaGenerationFeature {

	@Override
	public void GenerateFeature(Location atLocation) {
		// TODO Auto-generated method stub
		//System.out.println("BrokenWall...");
		World inworld = atLocation.getWorld();
		if(!inworld.getBlockAt(atLocation.getBlockX(),atLocation.getBlockY()-1,atLocation.getBlockZ()).getType().equals(Material.STATIONARY_WATER)){
			
			int[][] usearrays = new int[][] { 
			new int[] {0,1},		
			new int[] {0,-1},
			new int[] {-1,0},
			new int[] {1,0}
			
			};
			int[] chooseindices = usearrays[RandomData.rgen.nextInt(usearrays.length)];
			
			int iterationcount =0;
			int currX = atLocation.getBlockX();
			int currZ = atLocation.getBlockZ();
			
			for(iterationcount=0;iterationcount<10;iterationcount++){
				float currentprobability = 0.90f;
				for (int wallpos=atLocation.getBlockY();wallpos<atLocation.getBlockY()+10;wallpos++){
				
					Material[] choosematerials = new Material[]{Material.STONE,Material.SMOOTH_BRICK,Material.MOSSY_COBBLESTONE,Material.BRICK,Material.COBBLE_WALL,Material.STEP};
					Material chosenMaterial = RandomData.Choose(choosematerials);
					if(RandomData.rgen.nextFloat() <currentprobability){
					inworld.getBlockAt(currX,wallpos,currZ).setType(chosenMaterial);
					currentprobability*=currentprobability;
					}
					
				}
				currX+=chooseindices[0];
				currZ+=chooseindices[1];
				
				
			}
			
			
			
			
		}
		
		
	}
	
	
	

}
