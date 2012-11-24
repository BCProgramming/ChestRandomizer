package com.BASeCamp.SurvivalChests;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Projectile;

public class ProjectileMapper {

	private Material ItemShooter; //eg. Wooden Hoe
	private Class<? extends Projectile> ProjectileClass;
	private Material ItemRequire; //eg. Arrows
	private Sound shootsound;
	
	public Material getItemShooter() { return ItemShooter;}
	public Class<? extends Projectile> getProjectileClass() { return ProjectileClass;}
	public Material getItemRequire() { return ItemRequire;}
	public Sound getSound() { return shootsound;}
	public ProjectileMapper(Material Shooter, Material RequiredItem,Class<? extends Projectile> pProjectileClass,Sound shootysound){
		
		
		ItemShooter = Shooter;
		ItemRequire = RequiredItem;
		ProjectileClass = pProjectileClass;
		shootsound = shootysound;
		
	}
	
	
	
	
	
	
	
}
