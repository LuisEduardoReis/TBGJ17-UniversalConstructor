package com.tbgj17;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tbgj17.entities.Entity;
import com.tbgj17.entities.Player;

public class GameScreen extends ScreenAdapter {
	public Main main;
	
	SpriteBatch batch;
	ShapeRenderer renderer;
	OrthographicCamera camera;
	Viewport viewport;
	
	int state;
	float state_anim_timer;
	static final int MENU = 0;
	static final int INSTRUCTIONS = 1;
	static final int PLAY = 2;
	static final int PAUSE = 3;
	
	float lose_rtimer, t;
	
	Level level;
	
	public ArrayList<String> messages;
	public ArrayList<Color> message_colors;
	public ArrayList<Float> message_times;
	
	
	public GameScreen(Main main, int state) {
		this.main = main;
		
		this.state = state;
	}

	@Override
	public void show() {
		super.show();
		
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(Main.WIDTH/2, Main.HEIGHT/2);
		viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, camera);
		
		messages = new ArrayList<String>();
		message_colors = new ArrayList<Color>();
		message_times = new ArrayList<Float>();
		
		level = new Level(this);		
		
		lose_rtimer = -1;
		t = 0;
		
		state_anim_timer = 0;
		
		System.out.println(Controllers.getControllers());
	}
	
	
	public static final Color color = new Color();
	@Override
	public void render(float delta) {
		super.render(delta);
		
		// Update
		t += delta;
		
		// Update Controllers
		for(Player p : level.players) p.controller.update();
		
		if (state == PLAY) {
			level.update(Math.min(delta, 1/60f));		
		}
		
		if (lose_rtimer >= 0) lose_rtimer += delta;
		state_anim_timer = Util.stepTo(state_anim_timer, 1, 0.5f*delta);
		
		for(Player p : level.players) {
			if (p.controller != null && p.controller.getStartButtonDown()) {
				if (!p.Start_Down) {
					if (state != PLAY) state = (int) Util.stepTo(state, PLAY, 1);
					else state = PAUSE;
					
					state_anim_timer = 0;
					t = 0;
				}
				p.Start_Down = true;
				break;
			} else
				p.Start_Down = false; 
		}
		
		// Render
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		viewport.apply();
		batch.setProjectionMatrix(camera.combined);
		renderer.setProjectionMatrix(camera.combined);
		
		batch.begin();
			// Background
			batch.draw(Assets.background,0,0);
			
			level.render(batch);
		batch.end();
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.begin(ShapeType.Filled);
			// Bars
			float bw = Main.SIZE*3;
			renderer.setColor(color.set(1f, 1f, 0.5f, 0.75f * level.generator.getBarAnim()));
			renderer.rect(Main.WIDTH/2 - bw/2, Main.HEIGHT/2 + 1.75f*Main.SIZE, bw* Util.clamp(level.generator.slow_matter/100,0,100), 30);
			
			for(Entity e : level.entities) {
				float hr = e.health/e.full_health;
				if (e.bar_anim_timer == 0 && hr > 0.25f) continue;
				bw = Main.SIZE*1.5f;
				
				boolean jumping = (e instanceof Player) &&  hr < 0.25f && t % 0.5f < 0.25;
				color.set(e.aux_color == null ? Color.WHITE : e.aux_color);
				if (!(e instanceof Player && hr < 0.25f)) 
					color.mul(1f, 1f, 1f, 0.75f * Util.clamp(e.bar_anim_timer*1.0f,0,1));
				renderer.setColor(color);
				renderer.rect(e.x - bw/2, e.y + e.radius + (jumping ? 10 : 0), bw*hr, 10);
			}
			renderer.setColor(Color.WHITE);
		renderer.end();
		
		batch.begin();
			batch.setColor(1, 1, 1, level.generator.getAnim());
			batch.draw(Assets.background2,0,0);
			batch.setColor(Color.WHITE);
		
			// HUD
			BitmapFont font = Assets.font;
			if (Main.DEBUG) {
				font.getData().setScale(0.5f);
				font.draw(batch,level.entities.size()+"",50,50);
			}
			
			level.spawner.render(batch);
			
			// Menu
			if (state == MENU) {
				font.getData().setScale(3.5f);
				Util.drawTitle(batch, font, "Universal", Main.WIDTH/2, Main.HEIGHT*5.75f/6, state_anim_timer);
				Util.drawTitle(batch, font, "Constructor", Main.WIDTH/2, Main.HEIGHT*4.75f/6, state_anim_timer);
				
				if(t % 1f > 0.5f) { 
					font.getData().setScale(1.5f);
					font.setColor(1,1,1,state_anim_timer);
					Util.drawTextCentered(batch, font, "Press Start to continue", Main.WIDTH/2,Main.HEIGHT*1.5f/6);
				}
				
			} else if (state == INSTRUCTIONS) {
				
				font.getData().setScale(3f);
				Util.drawTitle(batch, font, "Controls", Main.WIDTH/2,Main.HEIGHT*5.5f/6,state_anim_timer);
				
				batch.setColor(Color.WHITE);
				batch.draw(Assets.controlsBackground,0,Main.HEIGHT/7);			
				
				font.getData().setScale(1.5f);
				font.setColor(1,1,1,state_anim_timer);
				Util.drawTextCentered(batch, font, "Press Start to continue", Main.WIDTH/2,Main.HEIGHT*1/6);
				
			} else if (state == PLAY) {
				// Messages
				int mc = 0; float mdt = 5f;
				for(Float mt : message_times) {
					if (t-mt > mdt) break;
					mc++;					
				} 
				for(int i = 0; i < mc; i++) {
					float mt = message_times.get(i);
					font.getData().setScale(1.5f);
					font.setColor(color.set(message_colors.get(i)).mul(1, 1, 1, Util.clamp((mdt-1.0f)-(t-mt),0,1)));
					
					Util.drawTextCentered(batch, font, messages.get(i), Main.WIDTH/2, Main.HEIGHT*(4-i)/8);
				}
				
				// Lose Screen
				if (lose_rtimer > 0) {
					float lf = 0.5f*Util.clamp((lose_rtimer - 2)/3, 0, 1);
					batch.setColor(0.5f,0,0,lf);
					batch.draw(Assets.fillTexture,0,0);
					
					font.getData().setScale(2f);
					Util.drawTitle(batch, font, "You made it to Wave " + level.spawner.wave, Main.WIDTH/2,Main.HEIGHT*5/6,lf);					
					
					if (lose_rtimer > 6 && t % 2 < 1) {
						font.getData().setScale(1.5f);
						font.setColor(1,1,1,lf);
						Util.drawTextCentered(batch, font, "Press Y to play again", Main.WIDTH/2,Main.HEIGHT*1/4);
					} 
					
					for(Player p : level.players) {
						if (p.controller.getRestartButtonDown()) main.start(GameScreen.PLAY);
					}
				}
			} else  if (state == PAUSE) {
				batch.setColor(0,0,0,0.5f);
				batch.draw(Assets.fillTexture,0,0);
				
				font.getData().setScale(1.5f);
				font.setColor(Color.WHITE);
				Util.drawTextCentered(batch, font, t % 1 > 0.5 ? "<Paused>" : " Paused ", Main.WIDTH/2,Main.HEIGHT*4/5);
				
				batch.setColor(Color.WHITE);
				batch.draw(Assets.controlsBackground,0,0);
			}
			
			font.setColor(Color.WHITE);
			batch.setColor(Color.WHITE);
		batch.end();
		
		if (Main.DEBUG) {
		renderer.begin(ShapeType.Line);
			level.renderDebug(renderer);
		renderer.end();
		}
		
	}
	
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		viewport.update(width, height);
	}
	
	public void addMessage(String message, Color color) {
		messages.add(message);
		message_colors.add(color);
		message_times.add(t);
	}

}
