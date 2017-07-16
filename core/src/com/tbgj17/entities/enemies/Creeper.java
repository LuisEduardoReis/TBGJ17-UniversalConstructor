package com.tbgj17.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.tbgj17.Level;
import com.tbgj17.Main;
import com.tbgj17.Sprites;
import com.tbgj17.Util;
import com.tbgj17.entities.Player;
import com.tbgj17.entities.particles.Explosion;

public class Creeper extends Enemy {

	public Creeper(Level level) {
		super(level);
		
		this.sprite = Sprites.creeperSprite;
		
		this.movespeed = 0.75f*Main.SIZE;
		this.radius = Main.SIZE/2;
		this.attack_range = radius * 1.5f;
		this.health = this.full_health = 350;
		this.mass = 10;
		
		this.aux_color = Color.LIME;		
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		direction = 0;
		
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
				x += delta*movespeed*(player.x-x)/dist;
				y += delta*movespeed*(player.y-y)/dist;
				
				// Attack player
				if (attack_timer == 0 && dist <= attack_range + player.radius) {					
					Explosion.enemyExplosion(x, y, level);
					remove = true;
				}
			}
		}
	}	
}
