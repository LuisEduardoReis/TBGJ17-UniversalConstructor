package com.tbgj17.controllers;

import com.badlogic.gdx.controllers.Controller;
import com.tbgj17.Util;

public class Xbox360Controller implements GameController {

	Controller c;
	boolean useButtonD, restartButtonD, shootButtonD, startButtonD;
	boolean useButtonP, restartButtonP, shootButtonP, startButtonP;
	
	public Xbox360Controller(Controller c) {
		this.c = c;
	}
	
	public void update() {
		boolean useButton = c.getButton(XBox360Pad.BUTTON_A);
		boolean restartButton = c.getButton(XBox360Pad.BUTTON_Y);
		boolean shootButton = Math.abs(c.getAxis(XBox360Pad.AXIS_RIGHT_TRIGGER)) > 0.5;
		boolean startButton = c.getButton(XBox360Pad.BUTTON_START);
		
		useButtonP = useButton && !useButtonD;
		useButtonD = useButton;
		
		restartButtonP = restartButton && !restartButtonD;
		restartButtonD = restartButton;
		
		shootButtonP = shootButton && !shootButtonD;
		shootButtonD = shootButton;
		
		startButtonP = startButton && !startButtonD;
		startButtonD = startButton;
	};

	@Override
	public float getMoveAxisX() { return c.getAxis(XBox360Pad.AXIS_LEFT_X); }

	@Override
	public float getMoveAxisY() { return c.getAxis(XBox360Pad.AXIS_LEFT_Y); }

	@Override
	public float getLookDir() {
		float rax = c.getAxis(XBox360Pad.AXIS_RIGHT_X);
		float ray = c.getAxis(XBox360Pad.AXIS_RIGHT_Y);
		return - (float) Math.atan2(ray, rax);
	}

	@Override
	public float getLookNormal() {
		float rax = c.getAxis(XBox360Pad.AXIS_RIGHT_X);
		float ray = c.getAxis(XBox360Pad.AXIS_RIGHT_Y);
		return Util.pointDistance(0, 0, rax, ray);
	}


	@Override
	public boolean getUseButtonDown() { return useButtonD; }
	@Override
	public boolean getUseButtonPressed() { return useButtonP; }

	@Override
	public boolean getRestartButtonDown() { return restartButtonD; }
	@Override
	public boolean getRestartButtonPressed() { return restartButtonP; }

	@Override
	public boolean getShootingDown() { 	return shootButtonD; }
	@Override
	public boolean getShootingPressed() { return shootButtonP; }
	
	@Override
	public boolean getStartButtonDown() { return startButtonD; }
	@Override
	public boolean getStartButtonPressed() { return startButtonP; }

}
