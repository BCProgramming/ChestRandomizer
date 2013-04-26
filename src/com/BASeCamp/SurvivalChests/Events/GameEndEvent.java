package com.BASeCamp.SurvivalChests.Events;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.BASeCamp.SurvivalChests.GameTracker;
 
public class GameEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String message;
    private String _winner = null;
    private GameTracker _Tracker = null;
    
    
    private HashMap<Integer,String> Placers;
    
    
    public GameTracker getTracker(){ return _Tracker;}
    /**
     * @Deprecated use getWinner() method instead.
     * @return
     */
    public Player getPlayer() { return Bukkit.getPlayer(_winner);}
    
    private void setPlayer(Player value) { _winner = value.getName();}
    
    public Player getWinner() { return Bukkit.getPlayer(_winner);}
    
    /**
     * retrieves all Participants for the game that just ended.
     * @return Participants of the ending game.
     */
    public List<Player> getAllParticipants(){
    	LinkedList<Player> buildlisting = new LinkedList<Player>();
    	for(String p:Placers.values()){
    		
    		buildlisting.add(Bukkit.getPlayer(p));
    		
    	}
    	return buildlisting;
    	
    }
    
    /**
     * returns player that made the given place.
     * @param placement place to retrieve. 1 for 1st place, 2 for second place, etc.
     * @return the Player that made the given place, or null if there is no player at that place.
     */
    public Player getPlacer(int placement){
    	
    	
    	if(!Placers.containsKey(placement)){
    		return null;
    	}
    	
    	return Bukkit.getPlayer(Placers.get(placement));
    	
    }
    
    public GameEndEvent(Player winner,HashMap<Integer,String> PlaceOrder,GameTracker trackerobject) {
        _winner=winner.getName();
        Placers = PlaceOrder;
        _Tracker = trackerobject;
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