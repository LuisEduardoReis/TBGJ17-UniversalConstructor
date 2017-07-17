package com.tbgj17;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tbgj17.entities.Entity;
import com.tbgj17.entities.enemies.Blaze;
import com.tbgj17.entities.enemies.Blizz;
import com.tbgj17.entities.enemies.Creeper;
import com.tbgj17.entities.enemies.Enemy;

public class EnemySpawner {
	Level level;
	
	float t;
	int wave;
	float wave_timer, wave_delay;
	int message_code;
	float message_alpha, slow_message_alpha, message_timer, message_delay;
	
	public EnemySpawner(Level level) {
		this.level = level;
		
		this.wave = 0;
		this.wave_timer = -1;
		
		this.message_code = 0;
		this.message_alpha = 0;
		this.slow_message_alpha = 0;
		this.message_timer = -1;
		this.message_delay = 3.5f;
	}
	
	public String getMessage(int code) {
		switch(code) {
		case 0:
			return "Wave "+wave+"!";
		case 1:
			return "Wave "+wave+" in "+(int)Math.ceil(wave_timer)+"...";			
		default:
			return "Wave "+wave+"!";
		}
	}
	
	public void update(float delta) {
		t += delta;
		
		if (wave_timer > 0) {
			wave_timer = Util.stepTo(wave_timer, 0, delta);
		}
		
		if (level.countAliveEnemies() == 0 && wave_timer == -1) startWaveTimer();		
	
		
		if (wave_timer == 0) {
			wave_timer = -1;			
			spawnWave(wave);
			for(Entity e : level.entities) if (e instanceof Enemy && e.dead && !e.beingDragged) e.fade_anim_timer = Math.max(e.fade_anim_timer, e.fade_anim_delay);
			showMessage(0);
 
		}
		
		// Messages
		slow_message_alpha = Util.stepTo(slow_message_alpha, message_alpha, 1.0f*delta);
		
		if (message_timer > 0 || (wave_timer > 0 && wave_timer < 3)) message_alpha = 1; else message_alpha = 0;
		if (message_timer > 0) 	message_timer = Util.stepTo(message_timer, 0, delta);
		if (message_timer == 0) message_timer = -1;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.K)) spawnWave(1000);
	}

	private void showMessage(int code) {
		message_code = code;
		System.out.println(getMessage(code));
		message_alpha = slow_message_alpha = 0;
		message_timer = message_delay;		
	}

	void startWaveTimer() {
		wave_timer = Math.max(wave_timer,getWaveDelay());
		wave++;
		showMessage(1);
	}	
	
	
	
	public void render(SpriteBatch batch) {
		BitmapFont font = Assets.font;
		font.getData().setScale(2f);
		font.setColor(1, 1, 1, 0.75f*slow_message_alpha);
		Util.drawTextCentered(batch, font, getMessage(message_code), Main.WIDTH/2,Main.HEIGHT*3/4);
		font.setColor(Color.WHITE);
	}

	
	
	public int getWaveCount() {
		int starting_count = 10;
		int increment = 8;
		return starting_count + wave*increment;
	}
	
	public float getWaveDelay() {
		return Util.clamp(0.5f*level.countEnemies(),10,20);
	}
	
	public void spawnWave(int wave) {
		for(int i = 0; i < getWaveCount(); i++) {
			Enemy e;
			double v = Math.random();
			if (v < 0.5) 
				e = new Blizz(level);
			else if (v < 0.85)
				e = new Blaze(level);
			else
				e = new Creeper(level);
							
			float dir = (float) (Math.random()*Math.PI*2);
			float dist = 0.75f*Main.WIDTH;
			e.x = Main.WIDTH/2 + (float) (dist * Math.cos(dir));
			e.y = Main.HEIGHT/2 + (float) (dist * Math.sin(dir));
			e.direction = (float) (Math.PI - dir);
		}
	}
	
}
