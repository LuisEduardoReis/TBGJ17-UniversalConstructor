package com.tbgj17.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.tbgj17.Level;
import com.tbgj17.Main;
import com.tbgj17.Sprites;
import com.tbgj17.Util;
import com.tbgj17.entities.Player;

public class Blizz extends Enemy {
	
	public Blizz(Level level) {
		super(level);
		
		this.sprite = Sprites.blizzSprite;
		
		this.scale = Util.randomRangef(0.75f, 1.25f);
		this.movespeed = 2f*Main.SIZE / this.scale;
		this.radius = this.scale*Main.SIZE/2;
		this.attack_range = radius * 1.5f;
		this.health = this.full_health = 100 * scale;
		
		this.aux_color = Color.CYAN;
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
				speed = movespeed;
				float turnspeed = (float) (Math.PI/2);
				float targetDirection = Util.pointDirection(x, y, player.x, player.y);
				direction = Util.stepToDirection(direction, targetDirection, turnspeed*delta);
				
				// Attack player
				if (attack_timer == 0 && dist <= attack_range + player.radius && Math.abs(Util.angleDifference(direction, targetDirection)) < Math.PI/4) {					
					player.damage(attack_damage);
					attack_timer = attack_delay;
				}
			} else {
				speed = 0;
				anim_speed = 1;
				direction = Util.stepToDirection(direction, (float) (-Math.PI/2), delta);
			}
		}	
	}

	
}
