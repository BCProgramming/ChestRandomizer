package com.BASeCamp.SurvivalChests;

public class KeyValuePair<K extends Comparable<K>,V> implements Comparable<KeyValuePair<K,V>> {
	
	private K _Key;
	private V _Value;
	
	public K getKey() { return _Key;}
	public V getValue() { return _Value;}
	
	public KeyValuePair(K Key,V Value){
		_Key = Key;
		_Value=Value;
		
		
	}
	@Override
	public int compareTo(KeyValuePair<K, V> o) {
		// TODO Auto-generated method stub
		return _Key.compareTo(o.getKey());
	}
	
	
	
	

}
