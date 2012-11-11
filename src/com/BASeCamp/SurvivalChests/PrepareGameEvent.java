package com.BASeCamp.SurvivalChests;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrepareGameEvent extends Event {
	 private static final HandlerList handlers = new HandlerList();
	 @Override
		public HandlerList getHandlers() {
			// TODO Auto-generated method stub
			return handlers;
		}
		public static HandlerList getHandlerList() {
		        return handlers;
		}
	
		private boolean _cancelled;
	
		public boolean getCancelled() { return _cancelled;}
		public void setCancelled(boolean value) { _cancelled=value;}
		public PrepareGameEvent(){
		
			
			
		}
		
		
		
}
