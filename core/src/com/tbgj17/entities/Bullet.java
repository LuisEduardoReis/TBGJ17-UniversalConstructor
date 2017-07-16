package com.tbgj17.entities;

import com.tbgj17.Level;
import com.tbgj17.Main;
import com.tbgj17.Sprites;
import com.tbgj17.Util;
import com.tbgj17.entities.enemies.Enemy;
import com.tbgj17.entities.particles.Spark;

public class Bullet extends Entity {

	public float damage;
	public float punch;
	public boolean enemy;
	
	public Bullet(Level level) {
		super(level);
		
		this.damage = 25;
		this.punch = 500;
		this.enemy = false;
		
		this.speed = 10*Main.SIZE;
		this.sprite = Sprites.bulletSprite;
		this.scale = 0.5f;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (x < -2*Main.SIZE || y < -2*Main.SIZE ||	x > Main.WIDTH + 2*Main.SIZE || y > Main.HEIGHT + 2*Main.SIZE) 
			remove = true;	
	}
	
	
	@Override
	public void collide(Entity o) {
		super.collide(o);
		
		if (!o.dead && ((!enemy && o instanceof Enemy) || (enemy && o instanceof Player))) {
			this.remove = true;
			o.damage(damage);
			float dist = Util.pointDistance(x, y, o.x, o.y);
			o.addEVel(-(x-o.x)/dist * punch / o.mass,-(y-o.y)/dist * punch / o.mass);
			
			if (!enemy) Spark.sparks(x,y,level, o.aux_color);
		}
		if (o instanceof Bullet && enemy != ((Bullet) o).enemy) {
			remove = true;
			o.remove = true;
		}
	}
}
