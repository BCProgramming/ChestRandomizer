package com.BASeCamp.SurvivalChests;

import java.util.LinkedList;
import java.util.Timer;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import com.BASeCamp.SurvivalChests.*;
public class RandomizerCommand implements CommandExecutor{

	private BCRandomizer _Owner = null;
	
	public BCRandomizer getOwner(){ return _Owner;}
	public void setOwner(BCRandomizer value){ _Owner=value;}
	
	public RandomizerCommand(BCRandomizer Owner)
	{
		_Owner = Owner;
		
	}
	
	private String TeamList(Player[] listing){
		
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<listing.length;i++){
			
			sb.append(listing[i]);
			if(i < listing.length-1)
				sb.append(",");
			
			
		}
		return sb.toString();
		
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
		
		
		
		
		if (sender instanceof Player) {
			
			
			//make sure they have permission.
			
			
			Player p = (Player)sender;
			
			String usecmd = arg2.toLowerCase();
			String WorldName = p.getWorld().getName().toLowerCase();
			String permnode = "chestrandomizer." + WorldName + "." + usecmd;
			
			if(!p.hasPermission(permnode)){
				if(!p.isOp()){
				if(p.getGameMode()==GameMode.CREATIVE) p.setGameMode(GameMode.ADVENTURE);
				p.getWorld().strikeLightning(p.getLocation());
				p.getWorld().strikeLightningEffect(p.getLocation());
				p.damage(50);
				Bukkit.broadcastMessage(ChatColor.RED + p.getName() + " tried to use Chest Randomizer, but isn't an op. punishment applied.");
				}
				else
				{
				p.sendMessage("You do not have permission to use that command.");
				p.sendMessage(ChatColor.GREEN + "NODE:" + permnode);
				
				}
				return true;
			}
			
			if(arg2.equalsIgnoreCase("teamsplit")){
			//get all online Players.
				
			LinkedList<Player> onlineplayers = new LinkedList<Player>();
			for(Player checkonline : p.getWorld().getPlayers()){
				if(checkonline.isOnline()){
				onlineplayers.add(checkonline);
				}
				
			
			}
			
			//now split contents of onlineplayers in half.
			LinkedList<Player> TeamA = new LinkedList<Player>();
			LinkedList<Player> TeamB = new LinkedList<Player>();
			for(int i=0;i<onlineplayers.size()/2;i++){
				
				Player[] theplayers = new Player[onlineplayers.size()];
				onlineplayers.toArray(theplayers);
				Player addteamA = RandomData.Choose(theplayers);
				TeamA.add(addteamA);
				onlineplayers.remove(addteamA);
				
				
				
			}
			TeamB = onlineplayers;
			
			
			Player[] TeamAArr = new Player[TeamA.size()];
			Player[] TeamBArr = new Player[TeamB.size()];
			TeamA.toArray(TeamAArr);
			TeamB.toArray(TeamBArr);
			
			Bukkit.broadcastMessage("Team A is " + TeamList(TeamAArr));
			Bukkit.broadcastMessage("Team B is " + TeamList(TeamBArr));
			
			
			for(Player APlayer : TeamAArr){
				APlayer.setDisplayName("[TEAM A]" + APlayer.getDisplayName());
				APlayer.sendMessage(ChatColor.YELLOW +  "You are on Team A!");
				
			}
			
			for(Player BPlayer : TeamBArr){
				BPlayer.setDisplayName("[TEAM B]" + BPlayer.getDisplayName());
				BPlayer.sendMessage(ChatColor.YELLOW + "You are on Team B!");
				
			}
			
			
				
				
				
			
			}
			if(arg2.equalsIgnoreCase("stopPvP"))
			{
				
				
				int numseconds = 30;
			
				try {numseconds= Integer.parseInt(arg3[0]);}
				catch(Exception exx) {numseconds=30;}
			    ResumePvP rp = new ResumePvP(p.getWorld(), numseconds);
			    
				p.getWorld().setPVP(false);
				
				Bukkit.broadcastMessage(ChatColor.GREEN + "PvP has been suspended for " + ChatColor.RED + numseconds + ChatColor.GREEN + " Seconds! get ready.");
				Thread thr = new Thread(rp);
				thr.start();
				
				
			}
			
			if(arg2.equalsIgnoreCase("repopchests")){
				
				
				int populatedamount =0;
				LinkedList<Chest> allchests = new LinkedList<Chest>();
				World gotworld = p.getWorld();
				Bukkit.broadcastMessage("BASeCamp Chest Randomizer- Running...");
				String sourcefile="";
				if(arg3.length > 0){
				sourcefile = arg3[0];}
				//randomize the enderchest contents, too :D
				for(Player popplayer : gotworld.getPlayers()){
					Inventory grabinv = popplayer.getEnderChest();
					ChestRandomizer cr = new ChestRandomizer(grabinv,sourcefile);
					cr.setMinItems(grabinv.getSize());
					cr.setMaxItems(grabinv.getSize());
					cr.Shuffle();
					
					
				}
				
				
				Chunk[] iteratechunks = gotworld.getLoadedChunks();
				//iterate through all the chunks...
				for(Chunk iteratechunk : iteratechunks){
					//go through all tileentities and look for Chests.
					BlockState[] entities = iteratechunk.getTileEntities();
					for(BlockState iteratestate:entities){
					if(iteratestate instanceof Chest){
						Chest casted = (Chest)iteratestate;
						//randomize!
						allchests.add(casted);
						ChestRandomizer cr = new ChestRandomizer(casted,sourcefile);
						populatedamount+= cr.Shuffle();
						
					}
						
					}
					
					
					
				}
				
				
				
				
				
				//turn chest LinkedList into an array.
				Chest[] chestchoose =new Chest[allchests.size()];
				allchests.toArray(chestchoose);
				
				int StaticAdded = 0;
				Bukkit.broadcastMessage(ChatColor.AQUA.toString() + allchests.size() + ChatColor.YELLOW + " Chests Populated.");
				Bukkit.broadcastMessage(ChatColor.YELLOW + "Populated " + ChatColor.AQUA.toString() +
						populatedamount + ChatColor.YELLOW + " slots.");
				for(RandomData iterate:ChestRandomizer.addall){
					
					ItemStack result = iterate.Generate();
					if(result!=null)
					{
					//choose a random chest.
					Chest chosen = RandomData.Choose(chestchoose);
					Inventory iv = chosen.getBlockInventory();
					//System.out.println("Added Static Item:" + result.toString());
					iv.addItem(result);
					StaticAdded++;
					}
				}
				Bukkit.broadcastMessage(ChatColor.YELLOW + "Added " + ChatColor.AQUA.toString() + StaticAdded + ChatColor.YELLOW + " Static items.");
				
				
				}
				
				
				
				
			}
		
		
	

	return false;}}
	
	

