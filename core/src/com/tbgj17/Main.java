package com.tbgj17;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;

public class Main extends Game {

	public static int WIDTH = 1920;
	public static int HEIGHT = 1080;
	public static int SIZE = 96;
	
	public static boolean DEBUG = false;
	public static boolean MUSIC = true;
	public static boolean SOUND = true;
	
	
	@Override
	public void create() {
		Assets.createAssets();
		
		start();		
	}

	public void start() {		
		Sprites.createSprites();
		
		setScreen(new GameScreen(this));
		
		if (MUSIC) Assets.music.play();
	}
	
	@Override
	public void render() {
		super.render();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) start();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
			if (Gdx.graphics.isFullscreen()) { 
				Gdx.graphics.setWindowedMode(Main.WIDTH/2, Main.HEIGHT/2);
				start();
			} else {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				start();
			}
		}
	
		if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) Main.DEBUG = !Main.DEBUG;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			Main.MUSIC ^= true;
			if (!Main.MUSIC && Assets.music.isPlaying()) Assets.music.pause();
			if (Main.MUSIC && !Assets.music.isPlaying()) Assets.music.play();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
			Main.SOUND ^= true;
		}
	}

	
	public static void playSound(Sound sound) { if (SOUND) sound.play(); }
	
	public static void randomSound(Sound[] sounds) {playSound(sounds[Util.randomRangei(sounds.length)]);}

}
