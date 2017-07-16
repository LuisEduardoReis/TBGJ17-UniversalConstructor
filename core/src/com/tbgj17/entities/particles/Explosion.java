package com.tbgj17.entities.particles;

import com.badlogic.gdx.graphics.Color;
import com.tbgj17.Level;
import com.tbgj17.Main;
import com.tbgj17.Sprites;
import com.tbgj17.Util;
import com.tbgj17.entities.Bullet;
import com.tbgj17.entities.EnemyBullet;
import com.tbgj17.entities.Entity;
import com.tbgj17.entities.Player;
import com.tbgj17.entities.enemies.Enemy;

public class Explosion extends Particle {

	public Explosion(Level level) {
		super(level);
		
		this.sprite = Sprites.waveSprite;
		this.blend = Color.ORANGE;
		
		this.dscale = 25f;
	}

	public static void explosion(float x, float y, Level level) {

		new Explosion(level).setDscale(10).setPosition(x, y).setBlend(Color.ORANGE);
		new Explosion(level).setDscale(15).setPosition(x, y).setBlend(Color.GRAY);
		new Explosion(level).setDscale(20).setPosition(x, y).setBlend(Color.DARK_GRAY);
		
		for(int i = 0; i < 100; i++) {
			Spark e = (Spark) new Spark(level).setDscale(0).setPosition(x, y);
			
			e.scale = 0.5f;
			float dark = Util.randomRangef(0.5f, 1);
			e.blend = new Color(1*dark,0.5f*dark,0,1);
			e.dscale = Util.randomRangef(0, 0.5f);
			e.lifetime = Util.randomRangef(0.5f, 1f);
			float f = Util.randomRangef(1000, 2500);
			float d = Util.randomRangef(0, (float) (2*Math.PI));
			e.addEVel((float)(f * Math.cos(d)),(float)(f * Math.sin(d)));
			e.edf = Util.randomRangef(0.9f, 0.95f);
		}
		
		// Effect
		float range = Main.WIDTH/3;
		for(Entity e : level.entities) {
			float dist = Math.max(e.radius,Util.pointDistance(x, y, e.x, e.y));
			
			if (dist > range) continue;
			if (e instanceof EnemyBullet) {
				e.remove = true;
				continue;
			} 
			
			if (!(e instanceof Enemy)) continue;								
			
			float ratio = 1f - (dist/range);
			float force = 2500 * ratio;
			float damage = 250 * ratio;
			
			e.addEVel(force*(e.x-x)/dist, force*(e.y-y)/dist);
			e.damage(damage);
		}
		
	}

	public static void enemyExplosion(float x, float y, Level level) {

		new Explosion(level).setDscale(10).setPosition(x, y).setBlend(Color.LIME);
		new Explosion(level).setDscale(15).setPosition(x, y).setBlend(Color.GRAY);
		new Explosion(level).setDscale(20).setPosition(x, y).setBlend(Color.DARK_GRAY);
		
		for(int i = 0; i < 100; i++) {
			Spark e = (Spark) new Spark(level).setDscale(0).setPosition(x, y);
			
			e.scale = 0.5f;
			float dark = Util.randomRangef(0.5f, 1);
			e.blend = new Color(0.5f*dark,1f*dark,0.25f*dark,1);
			e.dscale = Util.randomRangef(0, 0.5f);
			e.lifetime = Util.randomRangef(0.5f, 1f);
			float f = Util.randomRangef(1000, 2500);
			float d = Util.randomRangef(0, (float) (2*Math.PI));
			e.addEVel((float)(f * Math.cos(d)),(float)(f * Math.sin(d)));
			e.edf = Util.randomRangef(0.9f, 0.95f);
		}
		
		// Effect
		float range = Main.WIDTH/3;
		for(Entity e : level.entities) {
			float dist = Math.max(e.radius,Util.pointDistance(x, y, e.x, e.y));
			
			if (dist > range) continue;
			if (e instanceof Bullet && !((Bullet) e).enemy) {
				e.remove = true;
				continue;
			}
			
			if (!(e instanceof Player)) continue;								
			
			float ratio = 1f - (dist/range);
			float force = 2500;
			float damage = 25 * ratio;
			
			e.addEVel(force*(e.x-x)/dist, force*(e.y-y)/dist);
			e.damage(damage);
		}
		
	}

}
