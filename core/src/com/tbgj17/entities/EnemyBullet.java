package com.tbgj17.entities;

import com.tbgj17.Level;
import com.tbgj17.Main;
import com.tbgj17.Sprites;

public class EnemyBullet extends Bullet {
	
	public EnemyBullet(Level level) {
		super(level);
		
		this.damage = 5;
		this.punch = 0;
		this.enemy = true;
		
		this.speed = 2*Main.SIZE;
		this.sprite = Sprites.enemyBulletSprite;
		this.scale = 0.5f;
	}
}
