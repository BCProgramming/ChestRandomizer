package com.BASeCamp.SurvivalChests;
import java.util.*;
public class Filters {

	
	public static <T> List<T> FilterList(List<T> FilterList,IFilterPredicate<T> predicate){
		
		
		LinkedList<T> createlist = new LinkedList<T>();
		
		//System.out.println("Filtering " + FilterList.size());
		
		for(T iterate:FilterList){
			if(predicate.Predicate(iterate)){
				createlist.add(iterate);
			}
			
		}
		return createlist;
	}
	
}
