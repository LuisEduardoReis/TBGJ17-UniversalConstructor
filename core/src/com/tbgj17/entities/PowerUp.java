package com.tbgj17.entities;

import com.tbgj17.Level;
import com.tbgj17.Main;
import com.tbgj17.Sprites;
import com.tbgj17.Util;

public class PowerUp extends Entity {

	public int type;
	
	public PowerUp(Level level) {
		super(level);
		
		if (level.countAlive(Player.class) < level.players.size())
			this.type = (int) (Math.random()*5);
		else
			this.type = (int) (Math.random()*4);
		
		this.sprite = Sprites.powerUpSprites[type];
		
		this.entityCollisions = false;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (Util.pointDistance(x, y, level.generator.x, level.generator.y) < level.generator.radius)
			this.entityCollisions = false;
		else
			this.entityCollisions = true;
		
		x = Util.clamp(x, radius, Main.WIDTH-radius);
		y = Util.clamp(y, radius, Main.HEIGHT-radius);
	}
}
