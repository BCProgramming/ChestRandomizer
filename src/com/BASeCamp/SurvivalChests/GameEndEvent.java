package com.BASeCamp.SurvivalChests;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
 
public class GameEndEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String message;
    private Player _winner = null;
    
    public Player getPlayer() { return _winner;}
    public void setPlayer(Player value) { _winner = value;}
    public GameEndEvent(Player winner) {
        _winner=winner;
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