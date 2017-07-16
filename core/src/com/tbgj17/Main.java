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
	public static boolean SOUND = false;
	
	
	@Override
	public void create() {
		Assets.createAssets();
		
		start(GameScreen.MENU);		
	}

	public void start(int state) {		
		Sprites.createSprites();
		
		setScreen(new GameScreen(this,state));
		
		if (SOUND) Assets.music.play();
	}
	
	@Override
	public void render() {
		super.render();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) start(GameScreen.MENU);
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
			if (Gdx.graphics.isFullscreen()) { 
				Gdx.graphics.setWindowedMode(Main.WIDTH/2, Main.HEIGHT/2);
				start(GameScreen.PLAY);
			} else {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				start(GameScreen.PLAY);
			}
		}
	
		if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) Main.DEBUG = !Main.DEBUG; 
		if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
			Main.SOUND = !Main.SOUND;
			if (!Main.SOUND && Assets.music.isPlaying()) Assets.music.pause();
			if (Main.SOUND && !Assets.music.isPlaying()) Assets.music.play();
		}
	}

	public static void playSound(Sound sound) {
		if (SOUND) sound.play();
	}
	

}
