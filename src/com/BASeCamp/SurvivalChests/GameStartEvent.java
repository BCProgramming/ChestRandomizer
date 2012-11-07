package com.BASeCamp.SurvivalChests;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {
	  private static final HandlerList handlers = new HandlerList();
	    private String message;
	    
	    private List<Player> _Participants=null;
	    private List<Player> _Spectators = null;
	    
	    
	    public List<Player> getParticipants() { return _Participants;}
	    public List<Player> getSpectators() { return _Spectators;}
	    
	    public GameStartEvent(List<Player> Participants,List<Player> Spectators) {
	       
	    	
	    	_Participants = Participants;
	    	_Spectators = Spectators;
	    	
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
