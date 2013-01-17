package com.BASeCamp.SurvivalChests;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
 
public class GameEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String message;
    private Player _winner = null;
    private GameTracker _Tracker = null;
    
    
    private HashMap<Integer,Player> Placers;
    
    
    public GameTracker getTracker(){ return _Tracker;}
    /**
     * @Deprecated use getWinner() method instead.
     * @return
     */
    public Player getPlayer() { return _winner;}
    
    private void setPlayer(Player value) { _winner = value;}
    
    public Player getWinner() { return _winner;}
    
    /**
     * retrieves all Participants for the game that just ended.
     * @return Participants of the ending game.
     */
    public List<Player> getAllParticipants(){
    	LinkedList<Player> buildlisting = new LinkedList<Player>();
    	for(Player p:Placers.values()){
    		
    		buildlisting.add(p);
    		
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
    	
    	return Placers.get(placement);
    	
    }
    
    public GameEndEvent(Player winner,HashMap<Integer,Player> PlaceOrder,GameTracker trackerobject) {
        _winner=winner;
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