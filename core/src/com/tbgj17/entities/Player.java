package com.tbgj17.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tbgj17.Assets;
import com.tbgj17.Level;
import com.tbgj17.Main;
import com.tbgj17.Sprites;
import com.tbgj17.Util;
import com.tbgj17.controllers.GameController;
import com.tbgj17.entities.enemies.Enemy;
import com.tbgj17.entities.particles.Explosion;
import com.tbgj17.entities.particles.Particle;

public class Player extends Entity {
	
	public static final float SHOOT_DELAY = 1.0f / 10;

	public String name;
	public GameController controller;
	
	public float shoot_timer, shoot_delay;
	public float shield_timer, shield_delay;
	public float power_timer, power_delay;
	
	public static boolean shooting = false;
	public static float shooting_timer = 0;
	
	public Player(Level level) {
		super(level);
		
		this.entityCollisions = true;
		
		this.sprite = Sprites.playerSprite;
		this.scale = 1.5f;
		
		this.shoot_timer = 0;
		this.shoot_delay = SHOOT_DELAY;
		
		this.shield_timer = 0;
		this.shield_delay = 15f;
		
		this.power_timer = 0;
		this.power_delay = 15f;
		
		this.health = this.full_health = 100f;
		this.aux_color = Color.RED;
	}
	
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		x = Util.clamp(x, radius, Main.WIDTH-radius);
		y = Util.clamp(y, radius, Main.HEIGHT-radius);
		
		shield_timer = Util.stepTo(shield_timer, 0, delta);
		power_timer = Util.stepTo(power_timer, 0, delta);
		
		if(!dead & controller != null) {		
			// Movement
			float lax = controller.getMoveAxisX(),
				lay = controller.getMoveAxisY();
			float deadzone = 0.25f;
			float speed = 4*Main.SIZE;
			
			if(Math.abs(lay) > deadzone) this.y -= speed*delta * lay;
			if(Math.abs(lax) > deadzone) this.x += speed*delta * lax;		
			
			if (controller.getLookNormal() > deadzone) {
				this.direction = controller.getLookDir(x,y);
			} else if (Util.pointDistance(0, 0, lax,lay) > deadzone) {
				this.direction = -((float) Math.atan2(lay, lax));
			}		
			
			// Shooting
			shoot_timer = Util.stepTo(shoot_timer, 0, delta);
			if (shoot_timer == 0 && controller.getShootingDown()) {
				Bullet b = new Bullet(level);
				b.blend = blend;
				b.x = (float) (x + radius*Math.cos(direction-Math.PI/2) + 2*radius*Math.cos(direction));
				b.y = (float) (y + radius*Math.sin(direction-Math.PI/2) + 2*radius*Math.sin(direction));			
				b.direction = (float) (direction);
				if (power_timer > 0) {
					b.damage *= 2;
					b.punch *= 2;
				}
				
				shooting = true;
				shoot_timer = shoot_delay;
			}			

			// A pressed
			if (controller.getUseButtonPressed()) {
				bar_anim_timer = Math.max(bar_anim_timer,bar_anim_delay);
									
				// Get closest in range powerup
				float pdist = Float.MAX_VALUE; PowerUp pu = null;
				for(Entity e : level.entities) {
					if (!(e instanceof PowerUp)) continue;
					float d = Util.pointDistance(x, y, e.x, e.y);
					if (d < pdist) {
						pdist = d;
						pu = (PowerUp) e;
					}
				}
				if (pu != null && pdist < 1.5f*pu.radius + radius) {
					// PowerUps
					switch(pu.type) {
					case 0:
						health = full_health;
						break;
					case 1:
						shield_timer = Math.max(shield_timer, shield_delay);
						break;
					case 2:
						power_timer = Math.max(power_timer, power_delay);
						break;
					case 3:
						Explosion.explosion(x,y,level);
						break;
					case 4:
						for(Player player : level.players) {
							if (player.dead) {
								player.revive();
								new Particle(level).setDscale(10).setPosition(player.x, player.y).blend = Color.YELLOW;
								break;
							}
						}
						break;
					}
					pu.remove = true;
					Main.playSound(Assets.powerups[pu.type]);
				}
			}
			
			// Y pressed
			if (controller.getRestartButtonPressed()) {
				// Skip enemy collect
				if (level.countAliveEnemies() == 0) {
					for(Entity e : level.entities) if (e instanceof Enemy) e.fade_anim_timer = Math.max(e.fade_anim_delay,e.fade_anim_timer);
				}
			}
			
		} else {
			// Dead
			shield_timer = 0;
		}
	}


	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		
		if (power_timer > 0) {
			batch.setColor(0.75f,0,0,0.75f*Util.clamp(power_timer*1.0f, 0, 1));
			float s = 1.5f;
			batch.draw(Assets.spritesheet96[1][4],this.x -s*Main.SIZE/2,this.y -s*Main.SIZE/2, Main.SIZE, -Main.SIZE,s*Main.SIZE,s*Main.SIZE,1,1,0,true);
		}
		if (shield_timer > 0) {
			batch.setColor(1,1,1,Util.clamp(shield_timer*1.0f, 0, 1));
			float s = 1f;
			batch.draw(Assets.spritesheet96[1][4],this.x -s*Main.SIZE/2,this.y -s*Main.SIZE/2, Main.SIZE, -Main.SIZE,s*Main.SIZE,s*Main.SIZE,1,1,0,true);
		}
		
		
		batch.setColor(Color.WHITE);
	}
	
	@Override
	public void damage(float damage) {
		if (shield_timer > 0) return;
		super.damage(damage);		
	}


	public void drag(Entity o) {
		if (controller.getUseButtonDown()) {
			float dist = (Util.pointDistance(x, y, o.x, o.y));
			o.x += (x - o.x)/dist * (dist  - radius - o.radius);
			o.y += (y - o.y)/dist * (dist  - radius - o.radius);
			o.beingDragged = true;
			o.fade_anim_timer = -1;
		}		
	}	
}
