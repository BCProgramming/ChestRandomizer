package com.BASeCamp.SurvivalChests;

//inner class representing the items used for death and kill elements.
//it gives the assailant as well as the victim.
//they are stored as String names.
public class TallyData
{
	private String Assailant="";
	private String Victim = "";
	private int AttackCount=0;
	public String getAssailant() { return Assailant;}
	public void setAssailant(String value) { Assailant=value;}
	public String Victim() { return Victim;}
	public void setVictim(String value) { Victim=value;}
	public int getAttackCount(){ return AttackCount;}
	public void setAttackCount(int value) { AttackCount=value;}
	public TallyData(String Assailant,String Victim,int pAttackCount)
	{
		setAssailant(Assailant);
		setVictim(Victim);
		setAttackCount(pAttackCount);
	}
	
}