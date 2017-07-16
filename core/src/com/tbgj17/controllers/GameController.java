package com.tbgj17.controllers;

public interface GameController {
	
	void update();
	
	float getMoveAxisX();
	float getMoveAxisY();

	float getLookDir(float x, float y);
	float getLookNormal();

	boolean getShootingDown();
	boolean getShootingPressed();

	boolean getUseButtonDown();
	boolean getUseButtonPressed();

	boolean getRestartButtonDown();
	boolean getRestartButtonPressed();

	boolean getStartButtonDown();
	boolean getStartButtonPressed();

}
