package com.tbgj17.entities.enemies;

import com.tbgj17.Assets;
import com.tbgj17.Level;
import com.tbgj17.Main;
import com.tbgj17.Sprites;
import com.tbgj17.Util;
import com.tbgj17.entities.Entity;

public class Enemy extends Entity {

	public float attack_damage;
	public float attack_range;
	public float attack_timer;
	public float attack_delay;
	
	public float movespeed;


	public Enemy(Level level) {
		super(level);
		
		this.entityCollisions = true;
		
		this.sprite = Sprites.blizzSprite;
		
		this.anim_timer = (float) (Math.random()*sprite.anim_delay);
		
		this.attack_damage = 10;
		this.attack_range = radius * 1.5f;
		this.attack_delay = 1f;
		this.attack_timer = (float) (Math.random()*attack_delay);
		
		this.movespeed = Main.SIZE;
		
		this.matter = 10;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if  (!dead) {
			attack_timer = Util.stepTo(attack_timer, 0, delta);
			anim_speed = speed != 0 ? 1 : 0;
		} else {
			speed = 0;
			anim_speed = 0;
			
			x = Util.clamp(x, radius, Main.WIDTH-radius);
			y = Util.clamp(y, radius, Main.HEIGHT-radius);
		}
	}
	
	@Override
	public void damage(float damage) {
		super.damage(damage);
		
		Util.randomSound(Assets.hurt);
	}
		
	
	@Override
	public void die() {
		super.die();
		
		dragable = true;
		anim_speed = 0;
		speed = 0;
	}

}
