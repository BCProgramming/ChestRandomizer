package com.BASeCamp.SurvivalChests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ParticipantDeathEvent extends Event {
	 private static final HandlerList handlers = new HandlerList();
	 @Override
		public HandlerList getHandlers() {
			// TODO Auto-generated method stub
			return handlers;
		}
		public static HandlerList getHandlerList() {
		        return handlers;
		}
	 private Player _DeadPlayer;
	 private Player _Assailant;
	 private String _WeaponUsed;
	 
	 public Player getDeadPlayer() { return _DeadPlayer;}
	 public Player getAssailant() { return _Assailant;}
	 public String getWeaponUsed() { return _WeaponUsed;}
	 public ParticipantDeathEvent(Player p,Player Assailant,String WeaponUsed){
		 
		 _DeadPlayer = p;
		 _Assailant = Assailant;
		 _WeaponUsed = WeaponUsed;
		 
	 }
	 
	

}
