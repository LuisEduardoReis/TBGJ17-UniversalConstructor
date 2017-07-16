package com.tbgj17.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tbgj17.Main;
import com.tbgj17.Util;

public class KeyBoardMouseController implements GameController {


	boolean lmbD, rmbD;
	boolean lmbP, rmbP;
	
	@Override
	public void update() {
		boolean lmb = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
		boolean rmb = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
		
		lmbP = lmb && !lmbD;
		lmbD = lmb;
		
		rmbP = rmb && !rmbD;
		rmbD = rmb;
	}

	@Override
	public float getMoveAxisX() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) return -1;
		if (Gdx.input.isKeyPressed(Input.Keys.D)) return 1;
		return 0;
	}


	@Override
	public float getMoveAxisY() {
		if (Gdx.input.isKeyPressed(Input.Keys.S)) return 1;
		if (Gdx.input.isKeyPressed(Input.Keys.W)) return -1;
		return 0;
	}
	

	@Override
	public float getLookDir(float x, float y) {
		float mouse_x = ((float) Gdx.input.getX() / Gdx.graphics.getWidth()) * Main.WIDTH;
		float mouse_y = (1 - (float) Gdx.input.getY() / Gdx.graphics.getHeight()) * Main.HEIGHT;
		
		return Util.pointDirection(x, y, mouse_x, mouse_y);
	}

	@Override
	public float getLookNormal() {
		return 1;
	}

	@Override
	public boolean getShootingDown() { return lmbD; }
	@Override
	public boolean getShootingPressed() { return lmbP; }

	@Override
	public boolean getUseButtonDown() { return rmbD; }
	@Override
	public boolean getUseButtonPressed() { return rmbP; }

	@Override
	public boolean getRestartButtonDown() {
		return Gdx.input.isKeyPressed(Input.Keys.R);
	}

	@Override
	public boolean getRestartButtonPressed() {
		return Gdx.input.isKeyJustPressed(Input.Keys.R);
	}

	@Override
	public boolean getStartButtonDown() {
		return Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER);
	}

	@Override
	public boolean getStartButtonPressed() {
		return Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER);
	}
	
	@Override
	public String toString() {
		return "Keyboard and Mouse Controller";
	}

}
