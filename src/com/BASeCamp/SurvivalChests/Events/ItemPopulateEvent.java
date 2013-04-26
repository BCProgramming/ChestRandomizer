package com.BASeCamp.SurvivalChests.Events;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.BASeCamp.SurvivalChests.GameTracker;
 
public class ItemPopulateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String message;
    private ItemStack ItemAdded;
    private Inventory TargetInventory;
    private GameTracker _Tracker = null;
    public GameTracker getTracker(){ return _Tracker;}
  public ItemStack getItemAdded(){return ItemAdded;}
  public void setItemAdded(ItemStack value){ItemAdded=value;}
  public Inventory getTargetInventory(){return TargetInventory;}
  
    public ItemPopulateEvent(ItemStack pItemAdded,Inventory pTarget){
    	ItemAdded=pItemAdded;
    	TargetInventory = pTarget;
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