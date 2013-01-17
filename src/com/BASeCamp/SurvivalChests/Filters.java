package com.BASeCamp.SurvivalChests;
import java.lang.Iterable;
import java.util.LinkedList;
import java.util.List;
public class Filters {

	
	public static <T> List<T> filterList(Iterable<T> filterList,IFilterPredicate<T> predicate){
		
		
		LinkedList<T> createlist = new LinkedList<T>();
		
		//System.out.println("Filtering " + FilterList.size());
		
		for(T iterate:filterList){
			if(predicate.predicate(iterate)){
				createlist.add(iterate);
			}
			
		}
		return createlist;
	}
	
}
