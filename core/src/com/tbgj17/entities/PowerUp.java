package com.tbgj17.entities;

import com.tbgj17.Level;
import com.tbgj17.Main;
import com.tbgj17.Sprites;
import com.tbgj17.Util;

public class PowerUp extends Entity {

	public int type;
	
	public PowerUp(Level level) {
		super(level);
		
		int deadPlayers = level.players.size() - level.countAlivePlayers();
		int revivePowerUps = 0;
		for(Entity e : level.entities) if (e instanceof PowerUp && ((PowerUp) e).type == 4) revivePowerUps++;
		
		if (revivePowerUps < deadPlayers)
			this.type = 4;
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
