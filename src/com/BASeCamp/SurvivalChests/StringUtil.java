package com.BASeCamp.SurvivalChests;

import java.util.List;

public class StringUtil {

	
	public static String Join(List<String> Source,String Delimiter){
		
		String[] makearray = new String[Source.size()];
		Source.toArray(makearray);
		return Join(makearray,Delimiter);
		
		
	}
	
	public static String Join(String[] Source,String Delimiter){
		if(Source.length==0) return "";
		StringBuffer sb = new StringBuffer();
		
		for(String iterate:Source){
			sb.append(iterate);
			sb.append(Delimiter);
			
			
		}
		sb.setLength(sb.length()-Delimiter.length());
		
		
		return sb.toString();
	}
	
	
}
