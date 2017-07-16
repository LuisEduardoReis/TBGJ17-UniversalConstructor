package com.tbgj17;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	public static Texture testImage;
	public static Texture testBackground;
	
	public static Texture background, background2, controlsBackground;
	public static TextureRegion xbox_controller, keyboardmouse_controller;
	
	public static Texture spritesheet;
	public static TextureRegion[][] spritesheet96;
	public static BitmapFont font;
	public static Texture fillTexture;
	
	
	public static Sound shoot;
	public static Sound[] hurt,coin,powerups;
	
	public static Music music;
	
	
	public static void createAssets() {
		testImage = new Texture(Gdx.files.internal("badlogic.jpg"));
		testBackground = new Texture(Gdx.files.internal("testbackground.jpg"));
		background = new Texture(Gdx.files.internal("map2.png"));
		background2 = new Texture(Gdx.files.internal("map3.png"));
		controlsBackground = new Texture(Gdx.files.internal("controls.png"));
		
		xbox_controller = new TextureRegion(new Texture(Gdx.files.internal("xbox_controller.png")));
		keyboardmouse_controller = new TextureRegion(new Texture(Gdx.files.internal("keyboardmouse_controller.png")));
		
		int s = Main.SIZE;	
		
		spritesheet = new Texture(Gdx.files.internal("sprite_sheet.png"));
		spritesheet96 = new TextureRegion[spritesheet.getWidth()/s][spritesheet.getHeight()/s];
		for(int i = 0; i < spritesheet96.length; i++) {
		for(int j = 0; j < spritesheet96[i].length; j++) {
			spritesheet96[i][j] = new TextureRegion(spritesheet, j*s, i*s, s,s);
		}}
		
		
		font = new BitmapFont(Gdx.files.internal("font.fnt"));
		
		Pixmap p = new Pixmap(Main.WIDTH, Main.HEIGHT, Format.RGBA8888);
		p.setColor(1, 1, 1, 1);
		p.fill();
		fillTexture = new Texture(p);
		p.dispose();
		
		
		shoot = Gdx.audio.newSound(Gdx.files.internal("sound/shoot1.wav"));
		
		hurt = new Sound[3];
		hurt[0] = Gdx.audio.newSound(Gdx.files.internal("sound/hurt1.wav"));
		hurt[1] = Gdx.audio.newSound(Gdx.files.internal("sound/hurt2.wav"));
		hurt[2] = Gdx.audio.newSound(Gdx.files.internal("sound/hurt3.wav"));
		
		coin = new Sound[3];
		coin[0] = Gdx.audio.newSound(Gdx.files.internal("sound/coin1.wav"));
		coin[1] = Gdx.audio.newSound(Gdx.files.internal("sound/coin2.wav"));
		coin[2] = Gdx.audio.newSound(Gdx.files.internal("sound/coin3.wav"));
		
		powerups = new Sound[5];
		powerups[0] = Gdx.audio.newSound(Gdx.files.internal("sound/powerup4.wav"));
		powerups[1] = Gdx.audio.newSound(Gdx.files.internal("sound/powerup2.wav"));
		powerups[2] = Gdx.audio.newSound(Gdx.files.internal("sound/powerup1.wav"));
		powerups[3] = Gdx.audio.newSound(Gdx.files.internal("sound/explosion.wav"));
		powerups[4] = Gdx.audio.newSound(Gdx.files.internal("sound/powerup3.wav"));
		
		
		music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.mp3"));
		music.setLooping(true);
		music.setVolume(0.7f);
	}
	
}
