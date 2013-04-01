package com.BASeCamp.SurvivalChests;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;

public class SchematicImporter {

	private CuboidClipboard cc=null;
	//we use the WorldEdit API.
	//http://dev.bukkit.org/server-mods/worldedit/
	  //final EditSession es = new EditSession(new BukkitWorld((org.bukkit.World) world), 500000);
      //final CuboidClipboard cc = CuboidCli
	 //final CuboidClipboard cc = CuboidClipboard.loadSchematic(file);
     //cc.place(es, origin, false);
	public static HashMap<String,SchematicImporter> Schematics = null;
	private BCRandomizer _Owner = null;
	public static void Init(BCRandomizer Owner){
		Schematics  = new HashMap<String,SchematicImporter>();
		
		String datfolder = Owner.pluginMainDir;
		if(!datfolder.endsWith(String.valueOf(File.separatorChar))) datfolder = datfolder +File.separatorChar;
		datfolder = datfolder + "Schematics";
		
		File checkfile = new File(datfolder);
		System.out.println(checkfile.getPath());
		if(checkfile.exists()){
			
			
			//schematics folder found. iterate all files.
			for(File iterate:checkfile.listFiles()){
				System.out.println("file:" + iterate.getPath());
				if(iterate.getName().toLowerCase().endsWith(".schematic")){
					
							
					String name = iterate.getName();
					int dot = name.lastIndexOf('.');
					String base = (dot == -1) ? name : name.substring(0, dot);
					String extension = (dot == -1) ? "" : name.substring(dot+1);
							
					SchematicImporter makescheme = new SchematicImporter(Owner,iterate);
					Schematics.put(base, makescheme);
					System.out.println("loaded schematic:" + base);
					
				}
				
				
			}
			
			
		}
		
		
	}
	public SchematicImporter(BCRandomizer sourceplugin,File source)
	{
		_Owner = sourceplugin;
		
		if(Schematics==null){
			Init(_Owner);
		}
		
		try {
			cc = CuboidClipboard.loadSchematic(source);
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void Place(World target,int X,int Y, int Z){
		Place(target,new Vector(X,Y,Z),RandomData.rgen.nextInt(4));
	}
	public void Place(World target,Vector origin,int rotation){
		System.out.println("Placing schematic at " + origin.toString());
		final EditSession es = new EditSession(new BukkitWorld((org.bukkit.World)target),500000);
		
		try {
			cc.rotate2D(rotation);
			
		cc.place(es, origin,false);
		}
		catch(MaxChangedBlocksException mcb){
			
		}
		es.flushQueue();
		
	}
	public void Place(World inWorld, Location location, int Rotation) {
		// TODO Auto-generated method stub
		Place(inWorld,new Vector(location.getBlockX(),location.getBlockY(),location.getBlockZ()),Rotation);
	}
	
}
