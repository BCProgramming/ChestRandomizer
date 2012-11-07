package com.BASeCamp.SurvivalChests;

public class StringUtil {

	
	public static String Join(String[] Source,String Delimiter){
		
		StringBuffer sb = new StringBuffer();
		
		for(String iterate:Source){
			sb.append(iterate);
			sb.append(Delimiter);
			
			
		}
		sb.setLength(sb.length()-Delimiter.length());
		
		
		return sb.toString();
	}
	
	
}
