package com.tbgj17;

import java.util.ArrayList;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tbgj17.controllers.Xbox360Controller;
import com.tbgj17.entities.Entity;
import com.tbgj17.entities.Generator;
import com.tbgj17.entities.Player;

public class Level {
	public GameScreen game;
	
	public static Color player_colors[] = {Color.MAGENTA,Color.CYAN,Color.YELLOW,Color.GREEN};
	public static String player_names[] = {"Anna","Rachel","Susie","Fiona"};
	
	public ArrayList<Entity> entities;
	ArrayList<Entity> newEntities;
	
	public ArrayList<Player> players;
	public Generator generator;	
	public EnemySpawner spawner;	
		
	public Level(GameScreen game) {
		this.game = game;
		
		entities = new ArrayList<Entity>();
		newEntities = new ArrayList<Entity>();
		players = new ArrayList<Player>();
		
		int ci = 0;
		for(Controller c : Controllers.getControllers()) {
			Player player = (Player) new Player(this).setPosition(Main.WIDTH/2, Main.HEIGHT/2);
			player.blend = player_colors[ci];
			player.name = player_names[ci];
			player.controller = new Xbox360Controller(c);
			players.add(player);
			
			game.addMessage(player.name + " is ready to fight!", player.blend);
			game.message_times.set(game.message_times.size()-1, game.t-(4-ci)*0.5f);
			
			ci++; ci %= 4;
		}
				
		generator = (Generator) new Generator(this).setPosition(Main.WIDTH/2, Main.HEIGHT/2);
		spawner = new EnemySpawner(this);
	}

	public void addEntity(Entity e) {newEntities.add(e);}
	
	public void update(float delta) {
		
		// Spawn Enemies	
		spawner.update(delta);
		
		// Update
		for(Entity e : entities) e.update(delta);
		
		// New Entities
		entities.addAll(newEntities);
		newEntities.clear();
		
		// Entity Collisions
		for(Entity e : entities) {
		for(Entity o : entities) {
			if (e == o) continue;
			float sqrDist = Util.pointDistanceSqr(e.x, e.y, o.x, o.y);
			float radii = (e.radius + o.radius);
			// Dragging
			if (e instanceof Player && o.dragable && sqrDist <= 2*radii*radii)  ((Player) e).drag(o);
			// Collisions
			if (sqrDist <= radii*radii) e.collide(o);
			
		}}
		
		// Remove old Entities
		for(int i = 0; i < entities.size(); i++) 
			if (entities.get(i).remove)
				entities.remove(i).destroy();	
		
		// Restart game
		if(countAlive(Player.class) == 0 && game.lose_rtimer < 0) game.lose_rtimer = 0; 
		
		// Sound
		commonSounds(delta);
	}
	
	

	public void render(SpriteBatch batch) {
		for(Entity e : entities) e.render(batch);
	}
	
	public void renderDebug(ShapeRenderer renderer) {
		for(Entity e : entities) e.renderDebug(renderer);
	}
	
	public int count(Class<? extends Entity> clazz) {
		int r = 0;
		for(Entity e : entities) if (clazz.isInstance(e)) r++; 
		return r;
	}
	public int countAlive(Class<? extends Entity> clazz) {
		int r = 0;
		for(Entity e : entities) if (clazz.isInstance(e) && !e.dead) r++; 
		return r;
	}
	
	
	public void commonSounds(float delta) {
		Player.shooting_timer = Util.stepTo(Player.shooting_timer, 0, delta);
		if(Player.shooting && Player.shooting_timer == 0) {
			Main.playSound(Assets.shoot);
			Player.shooting_timer = Player.SHOOT_DELAY;
		}
			
		Player.shooting = false;
	}
}
