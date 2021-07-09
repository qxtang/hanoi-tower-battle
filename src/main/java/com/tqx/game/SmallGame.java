package com.tqx.game;

import com.tqx.entity.Stick;

public class SmallGame extends BaseGame{
	
	public SmallGame() {
		super(DEFAULT_GAME_WIDTH, DEFAULT_GAME_HEIGHT, null);
	}

	public SmallGame(int gameWidth, int gameHeight, GamePanel panel){
		super(gameWidth, gameHeight, panel);
	}
	
	
	public Stick getStickByName(String stickName){
		
		if (stickName.equals(Stick.STICK1_NAME)) {
			return stick1;
		}
		
		if (stickName.equals(Stick.STICK2_NAME)) {
			return stick2;
		}
		
		if (stickName.equals(Stick.STICK3_NAME)) {
			return stick3;
		}
		return null;
	}
	
	public void showTopClearByName(String stickName){
		
		Stick s = getStickByName(stickName);
		if (s!=null) {
			s.showTopClearly();
			panelInstance.repaint();
		}
	}
	
	public void moveToStickByName(String fromStickName, String targetStickName){
		
		Stick fs = getStickByName(fromStickName);
		Stick ts = getStickByName(targetStickName);
		
		if (!(fs == null || ts == null)) {
			moveToStick(fs, ts);
		}
		
	}
	
}
