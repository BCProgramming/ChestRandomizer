package com.BASeCamp.SurvivalChests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import BASeCamp.Configuration.INIFile;
import BASeCamp.Configuration.INISection;


import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.masks.RegionMask;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class SchematicImporter {

	private CuboidClipboard cc=null;
	//we use the WorldEdit API.
	//http://dev.bukkit.org/server-mods/worldedit/
	  //final EditSession es = new EditSession(new BukkitWorld((org.bukkit.World) world), 500000);
      //final CuboidClipboard cc = CuboidCli
	 //final CuboidClipboard cc = CuboidClipboard.loadSchematic(file);
     //cc.place(es, origin, false);
	public static HashMap<String,SchematicImporter> Schematics = null;
	public static INIFile SchematicOptions = null;
	private BCRandomizer _Owner = null;
	private String _Source;
	
	//method reads out the default schematics from the jar.
	//the default schematics are stored in a schematics folder in the jar.
	static boolean initcalled=false;
	public static void InitSchematics(BCRandomizer Owner){
		//first, jar files, as we know, are just ZIP files, so first get the file we are from:
		//ensure schematics folder exists.
		if(initcalled) return;
		initcalled=true;
		new File(BCRandomizer.SchematicsFolder).mkdirs();
		
		File jarName=null;
		try {
		jarName = new File (Owner.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
		}catch(Exception exx){exx.printStackTrace();}
		try {
		ZipFile zf=new ZipFile(jarName.getAbsoluteFile());
		
		Enumeration e=zf.entries();
	      while (e.hasMoreElements()) 
	      {
	          ZipEntry ze=(ZipEntry)e.nextElement();
	          //System.out.println(ze.getName());
	          //if it starts with Schematics, write it to the schematics folder.
	          if(!ze.isDirectory() && ze.getName().toLowerCase().startsWith("schematics/")){
	        	  //found a schematic!
	        	  System.out.println("Schematic found in JAR:" + ze.getName());
	        	  String basename = ze.getName().substring(ze.getName().indexOf("/")+1);
	        	  String createfilename = BCRandomizer.SchematicsFolder + "/" + basename;
	        	  FileWriter fw = new FileWriter(createfilename);
	        	 InputStream is = zf.getInputStream(ze);
	        	 OutputStream os = new FileOutputStream(createfilename);
	        	 
	        	 
	        	 byte[] buffer = new byte[20480];
	        	 int len;
	        	 while ((len = is.read(buffer)) != -1) {
	        	     os.write(buffer, 0, len);
	        	 }
	        	 is.close();
	        	 os.close();
	        	 fw.close(); 
	        	  
	          }
	          		
	      }
	      zf.close();
		  Init(Owner);
		
		
		}
		catch(IOException exi){
			exi.printStackTrace();
		}
		
	}
	
	public static void Init(BCRandomizer Owner){
		Schematics  = new HashMap<String,SchematicImporter>();
		
		String datfolder = Owner.SchematicsFolder;
		
		String optionsfile = datfolder + File.separatorChar + "options.ini";
		if(new File(optionsfile).exists()){
		try {SchematicOptions = new INIFile(new FileReader(optionsfile));} catch(IOException exr){}
		}
		else
		{
			SchematicOptions = new INIFile();
		}
		
		//the options.ini file has sections for each schematic.
		File checkfile = new File(datfolder);
		System.out.println(checkfile.getPath());
		if(checkfile.exists()){
			
			
			//schematics folder found. iterate all files.
			int schematiccount=0;
			for(File iterate:checkfile.listFiles()){
				//System.out.println("file:" + iterate.getPath());
				if(iterate.getName().toLowerCase().endsWith(".schematic")){
					schematiccount++;
							
					String name = iterate.getName();
					int dot = name.lastIndexOf('.');
					String base = (dot == -1) ? name : name.substring(0, dot);
					String extension = (dot == -1) ? "" : name.substring(dot+1);
							
					SchematicImporter makescheme = new SchematicImporter(Owner,iterate);
					Schematics.put(base, makescheme);
					System.out.println("loaded schematic:" + base);
					
				}
				
				
			}
			System.out.println("schematiccount=" + schematiccount);
			if(schematiccount==0){
				
				InitSchematics(Owner);
				
			}
			
		} else {InitSchematics(Owner);}
		
		
	}
	public SchematicImporter(BCRandomizer sourceplugin,File source)
	{
		_Owner = sourceplugin;
		
		if(Schematics==null){
			Init(_Owner);
		}
		
		try {
			_Source = source.getName();
			_Source = _Source.substring(0, _Source.indexOf('.'));
			cc = SchematicFormat.MCEDIT.load(source);
			
			//cc = CuboidClipboard.loadSchematic(source);
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
	WorldEditPlugin wep=null;
    public WorldEditPlugin getWorldEdit() {
    	if(wep!=null) return wep;
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (plugin == null || !(plugin instanceof WorldEditPlugin)) { return null; }
        return (wep=(WorldEditPlugin)plugin);
    }
    public void doFillDown(Location min, Location max, Material[] replacethese, Material with) {
    	ArrayList<Material> replaceem = new ArrayList<Material>(Arrays.asList(replacethese));
    	
    	int Xsize = max.getBlockX() - min.getBlockX();
    	int Zsize = max.getBlockZ() - min.getBlockZ();
    	
    	
    	
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
        	int Xindex = x-min.getBlockX();
        	for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
        		for (int y = max.getBlockY(); y <= min.getBlockY(); y--) {
        		
                	
                    Block blk = min.getWorld().getBlockAt(new Location(min.getWorld(), x, y, z));
                    
                    if (replaceem.contains(blk.getType())) {
                        blk.setType(with);
                    }else { break;} 
                }
            }
        }
    }
    public CuboidClipboard getClip(){ return cc;}
	public boolean Place(World target,Vector origin,int rotation){
		System.out.println("Placing schematic at " + origin.toString());
		BukkitWorld bw = new BukkitWorld((org.bukkit.World)target);
		//find matching config.
		INISection usesection = SchematicOptions.getSection(_Source);
		
		//acquire the heightoffset, and add this onto the origin Y value.
		String heightoffset = usesection.getValue("heightoffset","0").getValue();
		String wateronly = usesection.getValue("waterfeature","false").getValue();
		
		Location PointA = new Location(target,origin.getBlockX(),origin.getBlockY(),origin.getBlockZ());
		Location PointB = new Location(target,origin.getBlockX()+cc.getLength(),origin.getBlockY(),origin.getBlockZ()+cc.getWidth());
		
		int useHeight = BCRandomizer.getAverageY(PointA, PointB);
		origin = new Vector(origin.getX(),useHeight,origin.getZ());
		
		
		String[] includebiomes = usesection.getValue("includebiomes").getValues();
		String[] excludebiomes = usesection.getValue("excludebiomes").getValues();
		String sforceair = usesection.getValue("forceair","true").getValue();
		
		int offsetheight = Integer.parseInt(heightoffset);
		boolean iswater = Boolean.parseBoolean(wateronly);
		boolean forceair = Boolean.parseBoolean(sforceair);
		origin  = new Vector(origin.getBlockX(),origin.getBlockY()+offsetheight,origin.getBlockZ());
		Block grabblock =target.getBlockAt(origin.getBlockX(),origin.getBlockY()-1,origin.getBlockZ());
		String currbiome = grabblock.getBiome().toString().replace("_", " ");
		boolean allowedbiome=false;
		if(includebiomes.length>0){
			for(String findinclude:includebiomes){
				if(findinclude.equalsIgnoreCase(currbiome) || findinclude.length()==0)
				{
					allowedbiome=true;
					break;
				}
				
			}
		} else {allowedbiome=true;}
		
		if(allowedbiome && excludebiomes.length>0){
			for(String findexclude : excludebiomes){
				if(findexclude.equalsIgnoreCase(currbiome)){
					allowedbiome=false;
					break;
				}
			}
		}
		
		
		boolean onwater = grabblock.getType().equals(Material.STATIONARY_WATER);
		
		
		
		final EditSession es =  new EditSession(bw,500000);
		
		if(allowedbiome && iswater==onwater){
		try {
			//cc.rotate2D(90*rotation);
			cc.setOrigin(origin);
			es.enableQueue();
		//cc.place(es, origin,false);
			cc.paste(es, origin,!forceair,true);
		//cc.pasteEntities(origin);
		
		
		Vector pA = cc.getOrigin();
		Vector pB =new Vector(pA.getX()+ cc.getSize().getX(),
				pA.getY()+cc.getSize().getY(),
				pA.getZ()+cc.getSize().getZ());
		
		Location lA = new Location(target,pA.getBlockX(),pA.getBlockY(),pA.getBlockZ());
		Location lB = new Location(target,pB.getBlockX(),pB.getBlockY(),pB.getBlockZ());
		int MinX = Math.min(lA.getBlockX(), lB.getBlockX());
		int MinY = Math.min(lA.getBlockY(), lB.getBlockY());
		int MinZ = Math.min(lA.getBlockZ(), lB.getBlockZ());
		int MaxX = Math.max(lA.getBlockX(), lB.getBlockX());
		int MaxY = Math.max(lA.getBlockY(), lB.getBlockY());
		int MaxZ = Math.max(lA.getBlockZ(), lB.getBlockZ());
		
		lA = new Location(target,MinX,5,MinZ);
		lB = new Location(target,MaxX-1,MinY-1,MaxZ-1);
		//we replace starting from the Minimum Y (the lowest) to 0.
		//only for land features.
		if(!onwater)
			doFillDown(lA,lB,new Material[]{Material.AIR,Material.WATER,Material.LAVA,Material.STATIONARY_WATER,Material.STATIONARY_LAVA},Material.BRICK);
		
		
		}
		catch(MaxChangedBlocksException mcb){
			mcb.printStackTrace();
		}
		//fill up all the air beneath the schematic to bedrock with cobblestone.
		}
		//es.replaceBlocks(, arg1, arg2)
		
		
		
		es.flushQueue();
		return true;
		
	}
	public boolean Place(World inWorld, Location location, int Rotation) {
		// TODO Auto-generated method stub
		return Place(inWorld,new Vector(location.getBlockX(),location.getBlockY(),location.getBlockZ()),Rotation);
	}
	
}
