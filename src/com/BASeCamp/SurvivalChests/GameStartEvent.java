package com.BASeCamp.SurvivalChests;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {
	  private static final HandlerList handlers = new HandlerList();
	    private String message;
	    private boolean _MobArenaMode = false;
	    private List<Player> _Participants=null;
	    private List<Player> _Spectators = null;
	    
	    
	    public List<Player> getParticipants() { return _Participants;}
	    public List<Player> getSpectators() { return _Spectators;}
	    
	    public boolean getMobArenaMode() { return _MobArenaMode;}
	    
	    public GameStartEvent(List<Player> Participants,List<Player> Spectators,boolean MobArena) {
	       
	    	
	    	_Participants = Participants;
	    	_Spectators = Spectators;
	    	_MobArenaMode = MobArena;
	    }
	 
	    public String getMessage() {
	        return message;
	    }
	 
	    public HandlerList getHandlers() {
	        return handlers;
	    }
	 
	    public static HandlerList getHandlerList() {
	        return handlers;
	    }
}
//TODO: hook out own events, on start, cache the location of all frame entities and pictures and whatnot. On game end, re-add those frames
//and remove all buttons that are attached to gold blocks.
