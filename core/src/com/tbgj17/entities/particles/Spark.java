package com.tbgj17.entities.particles;

import com.badlogic.gdx.graphics.Color;
import com.tbgj17.Level;
import com.tbgj17.Sprites;
import com.tbgj17.Util;

public class Spark extends Particle {

	public Spark(Level level) {
		super(level);
		
		this.sprite = Sprites.sparkSprite;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (edx != 0 || edy != 0) {
			this.direction = Util.pointDirection(0, 0, edx, edy);
		}
	}

	public static void sparks(float x, float y, Level level, Color color) {
		
		for(int i = 0; i < 25; i++) {
			Spark s = (Spark) new Spark(level).setPosition(x, y);
			float f = Util.randomRangef(250, 500);
			float d = Util.randomRangef(0, (float) (2*Math.PI));
			s.alpha = 0.5f;
			s.lifetime = 0.5f;
			s.scale = 0.5f;
			s.blend = color;
			s.addEVel((float)(f * Math.cos(d)),(float)(f * Math.sin(d)));
			s.edf = Util.randomRangef(0.95f, 0.975f);
			s.direction = d;
		}
		
	}

}
