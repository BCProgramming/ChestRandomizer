package com.BASeCamp.SurvivalChests;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSpectateEvent extends Event {
	 private static final HandlerList handlers = new HandlerList();
	 private Player SpectatingPlayer = null;
	 private GamePreparer PrepareObject = null;
	 private boolean _Cancelled = false;
	 public PlayerSpectateEvent(GamePreparer gp,Player p){
		 
		 PrepareObject = gp;
		 SpectatingPlayer = p;
		 
	 }
	 
	 
	 public Player getSpectator() { return SpectatingPlayer;}
	 public GamePreparer getPrepareObject() { return PrepareObject;}
	 public void setCancelled(boolean value) { _Cancelled=value;}
	 public boolean getCancelled() { return _Cancelled;}
	 public HandlerList getHandlers() {
	        return handlers;
	    }
	 
	    public static HandlerList getHandlerList() {
	        return handlers;
	    }
	    
	    
	    
}
