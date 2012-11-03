package com.BASeCamp.SurvivalChests;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.fusesource.jansi.Ansi.Color;



public class PlayerDeathWatcher implements Listener{
	private BCRandomizer _owner=null;
	public PlayerDeathWatcher(BCRandomizer bcRandomizer) {
			_owner=bcRandomizer;
	}
	private String FriendlizeName(String source)
	{
		
		source = source.replace('_', ' ');
		return NameGenerator.toTitleCase(source);
		
	}
   @EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		
		//Bukkit.broadcastMessage(event.getEntity().getName() + " has been slain!");
		ChatColor usecolor = ChatColor.YELLOW;
		String usemessage = event.getDeathMessage();
		Player dyingPlayer = event.getEntity();
		
	
		
		
		String DyingName = dyingPlayer.getName();
		String KillerName="";
		Player Killer = dyingPlayer.getKiller();
		if(Killer!=null) KillerName = Killer.getName();
		if(dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.FALL))
		{
		
		usemessage=dyingPlayer.getName()+ " Fell " + Math.round(dyingPlayer.getFallDistance()) + " Blocks to their Death." ;
			
		}
		else if(dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.BLOCK_EXPLOSION)){
			String[] explosionstrings = new String[] {
					dyingPlayer + " blew up.",
					dyingPlayer + "'s body parts became shrapnel",
					dyingPlayer + " exploded."
			};
			usemessage = RandomData.Choose(explosionstrings);
		
		}
		else if((dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.DROWNING))){
		String[] possiblemessages = new String[] {
				DyingName + " Drowned.",
				DyingName + " Forgot to come up for air",
				DyingName + " Sleeps with the fishes"};
		usemessage = RandomData.Choose(possiblemessages);
		
				
				
		}
		else if((dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.SUICIDE))){
			String[] possiblemessages = new String[]{
					DyingName + " ended it all",
					DyingName + " didn't even leave a suicide note",
					DyingName + " was a casualty of their own genius",
					DyingName + " killed their own dumb self",
					DyingName + " committed suicide.",
					DyingName + " answered Luna's call."
					
			};
			usemessage = RandomData.Choose(possiblemessages);
			
		}
		else if((dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.POISON))){
		String[] possiblemessages = new String[] {
			DyingName + " threw up and then died.",
			DyingName + " should have seen a doctor.",
			DyingName + " couldn't find the antidote.",
			DyingName + " died from poison."};
			usemessage = RandomData.Choose(possiblemessages);
		
		}
		else if((dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.SUFFOCATION))){
			String[] possiblemessages = new String[] {
			DyingName + " tried to share space with a wall. It didn't work out.",
			DyingName + " suffocated in a wall.",
			DyingName + " choked on their own stupidity."
					
			};
			usemessage = RandomData.Choose(possiblemessages);
		}
		else if((dyingPlayer.getLastDamageCause().getCause().equals(DamageCause.LIGHTNING))){
			
			String[] possiblemessages = new String[] {
			 DyingName + " was struck down by Celestia",
			 DyingName + " was struck down by Luna",
			 DyingName + " was electrocuted.",
			 DyingName + " acted as a lightning rod",
			 DyingName + " was struck by lightning"
					
			};
			
			
		}
		else if(Killer!=null){
			//ok, get the item the Killer has.
			
			
			
			String weapon = FriendlizeName(Killer.getItemInHand().getType().name());
			
			ItemNamer.load(Killer.getItemInHand());
			String gotname= ItemNamer.getName();
			if(gotname!=null && gotname!="")
				weapon = gotname;
			
			String[] possiblemessages = new String[] {
					DyingName + " met there end from " + 
					KillerName + "'s " + weapon + ".",
					KillerName + " and their " + weapon + " slaughtered " + DyingName,
					DyingName + " was slain by " + KillerName + " using " + weapon,
					KillerName + " killed " + DyingName + " with a " + weapon
			};
			
			
			usemessage = RandomData.Choose(possiblemessages);
			
			}
			
		usemessage = usemessage.replace(DyingName, ChatColor.RED + DyingName + usecolor);
		if(KillerName.length() > 0)
			usemessage = usemessage.replace(KillerName,ChatColor.BLUE + KillerName + ChatColor.YELLOW);
		usemessage=usemessage + "(" + dyingPlayer.getLastDamage() + " damage)";
				System.out.println(usemessage + dyingPlayer.getLastDamageCause().toString());
		event.setDeathMessage(usecolor + usemessage);
		event.getDrops().add(RandomData.getHead(DyingName));
		if(_owner._Tracker!=null){
			//if there is a Tracker, notify it of the player death. do this after. The tracker
			//tracks the game itself.
			_owner._Tracker.PlayerDeath(dyingPlayer,Killer);
		}
		
	}

}
