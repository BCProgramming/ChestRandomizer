package com.BASeCamp.SurvivalChests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ParticipantJoinEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	
	private final Player _JoinedPlayer;
	private boolean _Cancelled=false;
	 
	public void setCancelled(boolean value) { _Cancelled=value;}
	public boolean getCancelled() { return _Cancelled;}
	public Player getPlayer() { return _JoinedPlayer;}
	
	public ParticipantJoinEvent(Player Joined){
		_JoinedPlayer = Joined;
	}
	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return handlers;
	}
	public static HandlerList getHandlerList() {
	        return handlers;
	}
}
