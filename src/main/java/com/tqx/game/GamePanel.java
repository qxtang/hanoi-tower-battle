package com.tqx.game;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel{
	
	BaseGame gameInstance;
	
	public GamePanel() {
	}
	public GamePanel(BaseGame gameInstance) {
		this.setGameInstance(gameInstance);
	}
	
	public void setGameInstance(BaseGame gameInstance) {
		this.gameInstance = gameInstance;
	}
	public BaseGame getGameInstance() {
		return gameInstance;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		gameInstance.paintGame(g);
	}
}
