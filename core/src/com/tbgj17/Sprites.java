package com.tbgj17;

public class Sprites {
	
	public static Sprite playerSprite;
	public static Sprite bulletSprite;
	public static Sprite blizzSprite;
	public static Sprite blazeSprite;
	public static Sprite creeperSprite;
	public static Sprite[] powerUpSprites;
	public static Sprite enemyBulletSprite;
	public static Sprite waveSprite;
	public static Sprite sparkSprite;
	
	
	public static void createSprites() {
		playerSprite = new Sprite();
		playerSprite.i_rotation = 90;
		playerSprite.addFrame(Assets.spritesheet96[1][8]);
		
		bulletSprite = new Sprite();
		bulletSprite.i_rotation = 90;
		bulletSprite.addFrame(Assets.spritesheet96[0][1]);
		
		enemyBulletSprite = new Sprite();
		enemyBulletSprite.i_rotation = 90;
		enemyBulletSprite.addFrame(Assets.spritesheet96[1][9]);
		
		blizzSprite = new Sprite();
		blizzSprite.i_rotation = 90;
		blizzSprite.anim_delay = 1/6f;
		blizzSprite.addFrame(Assets.spritesheet96[0][13]);
		blizzSprite.addFrame(Assets.spritesheet96[0][14]);
		blizzSprite.addFrame(Assets.spritesheet96[0][15]);
		
		blazeSprite = new Sprite();
		blazeSprite.i_rotation = 90;
		blazeSprite.anim_delay = 1/6f;
		blazeSprite.addFrame(Assets.spritesheet96[0][7]);
		blazeSprite.addFrame(Assets.spritesheet96[0][8]);
		blazeSprite.addFrame(Assets.spritesheet96[0][9]);
		
		creeperSprite = new Sprite();
		creeperSprite.anim_delay = 1/6f;
		creeperSprite.addFrame(Assets.spritesheet96[0][11]);
		creeperSprite.addFrame(Assets.spritesheet96[0][12]);
		
		powerUpSprites = new Sprite[5];
		powerUpSprites[0] = new Sprite();
		powerUpSprites[0].addFrame(Assets.spritesheet96[0][10]);
		
		powerUpSprites[1] = new Sprite();
		powerUpSprites[1].addFrame(Assets.spritesheet96[1][3]);
		
		powerUpSprites[2] = new Sprite();
		powerUpSprites[2].addFrame(Assets.spritesheet96[1][5]);
		
		powerUpSprites[3] = new Sprite();
		powerUpSprites[3].addFrame(Assets.spritesheet96[1][6]);
		
		powerUpSprites[4] = new Sprite();
		powerUpSprites[4].addFrame(Assets.spritesheet96[1][11]);
		
		waveSprite = new Sprite();
		waveSprite.addFrame(Assets.spritesheet96[1][4]);
		
		sparkSprite = new Sprite();
		sparkSprite.i_rotation = 90;
		sparkSprite.addFrame(Assets.spritesheet96[1][10]);		
	}
}
