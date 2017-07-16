package com.tbgj17.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.tbgj17.Level;
import com.tbgj17.Main;
import com.tbgj17.Sprites;
import com.tbgj17.Util;
import com.tbgj17.entities.EnemyBullet;
import com.tbgj17.entities.Player;

public class Blaze extends Enemy {
	
	public Blaze(Level level) {
		super(level);
		
		this.sprite = Sprites.blazeSprite;
		
		this.movespeed = Main.SIZE;
		this.health = this.full_health = 100;
		
		this.attack_delay = 2f;
		this.attack_range = Main.SIZE * 8;
		
		this.aux_color = Color.ORANGE;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		if  (!dead) {			
			// Find closest player		
			float dist = Float.MAX_VALUE; Player player = null;
			for(Player p : level.players) {
				if (p.dead) continue;
				float d = Util.pointDistance(x, y, p.x, p.y);
				if (d < dist) {
					dist = d;
					player = p;
				}
			}
		
			if (player != null)  {
				// Follow player
				float turnspeed = (float) (Math.PI);
				float targetDirection = Util.pointDirection(x, y, player.x, player.y);
				direction = Util.stepToDirection(direction, targetDirection, turnspeed*delta);
				
				if (dist < attack_range) {					
					// Attack player
					if (attack_timer == 0 && Math.abs(Util.angleDifference(direction, targetDirection)) < Math.PI/4) {
						EnemyBullet b = (EnemyBullet) new EnemyBullet(level).setPosition(x,y);
						b.direction = direction;
						attack_timer = attack_delay;
					}
				} 
				if (dist < attack_range/2)
					speed = 0;
				else
					speed = movespeed;
				
				
				
			} else {
				speed = 0;
				anim_speed = 1;
				direction = Util.stepToDirection(direction, (float) (-Math.PI/2), delta);
			}
		}
	}

	
	@Override
	public void damage(float damage) {
		// TODO Auto-generated method stub
		super.damage(damage);
		
		
	}
	
}
