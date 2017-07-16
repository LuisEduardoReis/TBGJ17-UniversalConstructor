package com.tbgj17;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
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
import com.tbgj17.controllers.GameController;
import com.tbgj17.controllers.KeyBoardMouseController;
import com.tbgj17.controllers.Xbox360Controller;
import com.tbgj17.entities.Entity;
import com.tbgj17.entities.Player;

public class GameScreen extends ScreenAdapter {
	public Main main;
	
	SpriteBatch batch;
	ShapeRenderer renderer;
	OrthographicCamera camera;
	Viewport viewport;
	
	ArrayList<GameController> controllers;
	boolean[] active_controllers;
	
	enum State {
		MENU, INSTRUCTIONS, SELECT_CONTROLS, PLAY, PAUSE
	}
	
	State state;
	float state_anim_timer;

	
	float lose_rtimer, t;
	
	Level level;
	
	public ArrayList<String> messages;
	public ArrayList<Color> message_colors;
	
	
	public GameScreen(Main main) {
		this.main = main;
	}

	@Override
	public void show() {
		super.show();
		
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(Main.WIDTH/2, Main.HEIGHT/2);
		viewport = new FitViewport(Main.WIDTH, Main.HEIGHT, camera);
		
		controllers = new ArrayList<GameController>();
		
		controllers.add(new KeyBoardMouseController());
		for(Controller c : Controllers.getControllers()) controllers.add(new Xbox360Controller(c));
		
		active_controllers = new boolean[controllers.size()];
		Arrays.fill(active_controllers, true);
		if (controllers.size() > 1) active_controllers[0] = false;
		
		
		messages = new ArrayList<String>();
		message_colors = new ArrayList<Color>();
		
		level = null;		
		
		lose_rtimer = -1;
		t = 0;
		
		state_anim_timer = 0;
		
		state = State.MENU;
		
		System.out.println(controllers);
	}
	
	private void start() {
		state = State.PLAY;
		state_anim_timer = 0;
		t = 0;
		
		messages.clear();
		message_colors.clear();
		
		level = new Level(this);
		
		for(int i = 0; i < 5; i++)
			//if (active_controllers[i])
				level.createPlayer(controllers.get(Math.min(i+1,controllers.size()-1)));
		
		lose_rtimer = -1;
	}
	
	
	public static final Color color = new Color();
	@Override
	public void render(float delta) {
		super.render(delta);
		
		// Update
		t += delta;
		
		for(GameController c : controllers) c.update();
		
		if (state == State.PLAY) {
			level.update(Math.min(delta, 1/60f));		
		}
		
		// Lose logic
		if (lose_rtimer >= 0) {
			lose_rtimer += delta;
			for(Player p : level.players) {
				if (p.controller.getRestartButtonDown()) start();
			}
		}
		
		// State machine
		state_anim_timer = Util.stepTo(state_anim_timer, 1, 0.5f*delta);
		
		for(GameController c : controllers) {
			if (c.getStartButtonPressed()) {
				switch (state) {
				case MENU: 
					state = State.INSTRUCTIONS;
					state_anim_timer = 0;
					t = 0; 
					break; 
				case INSTRUCTIONS: 
					if (controllers.size() > 1)
						state = State.SELECT_CONTROLS;
					else
						start();
					
					state_anim_timer = 0;
					t = 0;
					break;
				case SELECT_CONTROLS:
					int active = 0;
					for(int i = 0; i < active_controllers.length; i++) if (active_controllers[i]) active++;
					if (active == 0) break;
					
					start();			
					break;
				case PLAY:
					state = State.PAUSE; 
					break;
				case PAUSE: 
					state = State.PLAY; 
					break;
				
				default:
					break;
				}				
			}
		}
		
		if (state == State.SELECT_CONTROLS) {
			for(int i = 0; i < controllers.size(); i++) {
				if (controllers.get(i).getShootingPressed()) {
					active_controllers[i] ^= true; 
				}
			}
		}
		
		// Render
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		viewport.apply();
		batch.setProjectionMatrix(camera.combined);
		renderer.setProjectionMatrix(camera.combined);
		
		// Background and Level
		batch.begin();
			batch.draw(Assets.background,0,0);
			
			if (level != null) level.render(batch);
		batch.end();
		
		// Health Bars
		if (level != null) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.begin(ShapeType.Filled);
			
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
		}
		
		batch.begin();
			BitmapFont font = Assets.font;
		
			// Generator Glow
			if (level != null) {
				batch.setColor(1, 1, 1, level.generator.getAnim());
				batch.draw(Assets.background2,0,0);
				batch.setColor(Color.WHITE);
			}
		
			
			
			// HUD
			if (Main.DEBUG) {
				font.getData().setScale(0.5f);
				if (level != null) font.draw(batch,level.entities.size()+"",50,50);
				
				font.draw(batch,Gdx.input.getX()+" "+Gdx.input.getY(),50,100);
			}
				
			if (level != null) level.spawner.render(batch);
			
			
			// Menu
			switch(state) {
			case MENU:
				font.getData().setScale(3.5f);
				Util.drawTitle(batch, font, "Universal", Main.WIDTH/2, Main.HEIGHT*5.75f/6, state_anim_timer);
				Util.drawTitle(batch, font, "Constructor", Main.WIDTH/2, Main.HEIGHT*4.75f/6, state_anim_timer);
				
				if(t % 1f > 0.5f) { 
					font.getData().setScale(1.5f);
					font.setColor(1,1,1,state_anim_timer);
					Util.drawTextCentered(batch, font, "Press Start/Space to continue", Main.WIDTH/2,Main.HEIGHT*1.5f/6);
				}
				break;
			case INSTRUCTIONS:
				font.getData().setScale(3f);
				Util.drawTitle(batch, font, "Controls", Main.WIDTH/2,Main.HEIGHT*5.5f/6,state_anim_timer);
				
				batch.setColor(Color.WHITE);
				batch.draw(Assets.controlsBackground,0,Main.HEIGHT/7);			
				
				font.getData().setScale(1.5f);
				font.setColor(1,1,1,state_anim_timer);
				Util.drawTextCentered(batch, font, "Press Start/Space to continue", Main.WIDTH/2,Main.HEIGHT*1/6);
				break;
			case SELECT_CONTROLS:
				font.getData().setScale(1.5f);
				Util.drawTitle(batch, font, "Select Controllers", Main.WIDTH/2,Main.HEIGHT*5.75f/6,state_anim_timer);
				
				
				for(int i = 0; i < controllers.size(); i++) {
					GameController c = controllers.get(i);
					float pos = Main.WIDTH/5.5f * (i -(controllers.size()-1)/2f);				
					
					if (active_controllers[i]) 
						batch.setColor(Color.WHITE);
					else 
						batch.setColor(Color.DARK_GRAY);
					
					if (c instanceof Xbox360Controller) {
						Util.drawCentered(batch, Assets.xbox_controller,Main.WIDTH/2+pos,Main.HEIGHT/2, 0.5f);
					}
					if (c instanceof KeyBoardMouseController) {
						Util.drawCentered(batch, Assets.keyboardmouse_controller,Main.WIDTH/2+pos,Main.HEIGHT/2, 0.75f);
					}
				}
				batch.setColor(Color.WHITE);
				
				font.getData().setScale(1f);
				font.setColor(1,1,1,state_anim_timer);
				Util.drawTextCentered(batch, font, "Press Fire to activate/deactivate controller", Main.WIDTH/2,Main.HEIGHT*1.75f/6);
				
				int active = 0;
				for(int i = 0; i < active_controllers.length; i++) if (active_controllers[i]) active++;
				if (active > 0) {
					font.getData().setScale(1.5f);
					font.setColor(1,1,1,state_anim_timer);
					Util.drawTextCentered(batch, font, "Press Start/Space to continue", Main.WIDTH/2,Main.HEIGHT*1/6);
				}
				break;
			case PLAY:
				// Messages
				if (t < 4) { 
					for(int i = 0; i < messages.size(); i++) {
						
						font.getData().setScale(1f);
						font.setColor(color.set(message_colors.get(i)).mul(1, 1, 1, Util.clamp((4-t),0,1)));
						
						Util.drawTextCentered(batch, font, messages.get(i), Main.WIDTH/2, Main.HEIGHT*(6-i)/12);
					}
				}
				
				// Lose Screen
				if (lose_rtimer > 0) {
					float lf = 0.5f*Util.clamp((lose_rtimer - 2)/3, 0, 1);
					batch.setColor(0.5f,0,0,lf);
					batch.draw(Assets.fillTexture,0,0);
					
					font.getData().setScale(2f);
					Util.drawTitle(batch, font, "You made it to Wave " + level.spawner.wave, Main.WIDTH/2,Main.HEIGHT*5/6,2*lf);					
					
					if (lose_rtimer > 6 && t % 2 < 1) {
						font.getData().setScale(1.5f);
						font.setColor(1,1,1,2*lf);
						Util.drawTextCentered(batch, font, "Press Y/R to play again", Main.WIDTH/2,Main.HEIGHT*1/4);
					} 
				}
				break;
			case PAUSE:
				batch.setColor(0,0,0,0.5f);
				batch.draw(Assets.fillTexture,0,0);
				
				font.getData().setScale(1.5f);
				font.setColor(Color.WHITE);
				Util.drawTextCentered(batch, font, t % 1 > 0.5 ? "<Paused>" : " Paused ", Main.WIDTH/2,Main.HEIGHT*4/5);
				
				batch.setColor(Color.WHITE);
				batch.draw(Assets.controlsBackground,0,0);
				break;
			default:
				break;			
			}
			
			font.setColor(Color.WHITE);
			batch.setColor(Color.WHITE);
		batch.end();
		
		if (Main.DEBUG) {
			if (level != null) {
				renderer.begin(ShapeType.Line);
					level.renderDebug(renderer);
				renderer.end();
			}
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
	}

}
