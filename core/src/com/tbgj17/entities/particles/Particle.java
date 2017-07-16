package com.tbgj17.entities.particles;

import com.tbgj17.Level;
import com.tbgj17.Sprites;
import com.tbgj17.Util;
import com.tbgj17.entities.Entity;

public class Particle extends Entity {

	public float lifetime;
	public float dscale;
	
	public Particle(Level level) {
		super(level);
		
		this.sprite = Sprites.waveSprite;
		
		this.lifetime = 1f;
		
		this.dscale = 0f;
	}
	
	public Particle setDscale(float dscale) {
		this.dscale = dscale; return this;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (t > lifetime) remove = true;
		
		scale += dscale*delta;
		
		alpha = Util.clamp(1f - (t / lifetime),0,1);
	}

}
