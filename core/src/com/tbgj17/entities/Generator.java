package com.tbgj17.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tbgj17.Assets;
import com.tbgj17.Level;
import com.tbgj17.Main;
import com.tbgj17.Util;

public class Generator extends Entity {

	public float anim_timer, anim_delay;
	public float bar_anim_timer, bar_anim_delay;
	
	public float matter;
	public float slow_matter;
	
	public Generator(Level level) {
		super(level);
		
		this.entityCollisions = true;
		this.imovable = true;
		this.radius = Main.SIZE * 1.5f;
		
		this.anim_timer = 0;
		this.anim_delay = 1;
		
		this.bar_anim_timer = 0;
		this.bar_anim_delay = 4f;
		
		this.matter = 0;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		anim_timer = Util.stepTo(anim_timer, 0, delta);
		bar_anim_timer = Util.stepTo(bar_anim_timer, 0, delta);
		
		
		slow_matter = Util.clamp(Util.stepTo(slow_matter, matter, 25*delta), 0, 100);
		while(matter > 100) {
			// Create Powerup
			matter -= 100;
			slow_matter = 0;
			anim_timer = 3*anim_delay;
			
			Assets.coin[1].play();
			
			new PowerUp(level).setPosition(x, y).addEVel(Util.randomRangef(-500,500),-1000);;			
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) new PowerUp(level).setPosition(x, y).addEVel(Util.randomRangef(-500,500),-1000);
		
		for(Entity e : level.entities) {
			if (!e.dragable) continue;
			float dist = Util.pointDistance(x, y, e.x, e.y);
			if (dist > radius + e.radius + Main.SIZE) continue;
			float force = 10;
			
			e.x = Util.stepTo(e.x, x, force);
			e.y = Util.stepTo(e.y, y, force);
			
			if (dist < e.radius) {
				e.entityCollisions = false;
				if (e.fade_anim_timer < 0) {
					// Add matter
					anim_timer = Math.max(anim_delay, anim_timer);
					bar_anim_timer = Math.max(bar_anim_delay, bar_anim_timer);
					
					e.fade_anim_timer = Math.max(e.fade_anim_timer,e.fade_anim_delay);
					matter += e.matter;
					
					if(matter<100) Assets.coin[2].play();
				}
			}
		}
	}
	
	@Override
	public boolean collidesWith(Entity o) {
		if (o.dragable) return false;
		
		return super.collidesWith(o);
	}

	public float getAnim() {return Util.clamp(anim_timer / anim_delay, 0,1);}
	public float getBarAnim() {return Util.clamp(bar_anim_timer,0,1) / 1.0f;}

}
