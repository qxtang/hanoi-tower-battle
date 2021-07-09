package com.tqx.game;

import com.tqx.entity.Plate;
import com.tqx.entity.Stick;

import java.awt.*;

/*
 * Created by TQX 2017/3/23, Base Game
 * 
 * */
public class BaseGame {
	
	public static int DEFAULT_GAME_WIDTH = 800;
	public static int DEFAULT_GAME_HEIGHT = 500;
	public static int MAX_LEVEL = 10;

	protected int gameWidth;
	protected int gameHeight;

	protected Stick stick1;
	protected Stick stick2;
	protected Stick stick3;

	protected int plateCount = 3;
	protected GamePanel panelInstance;

	public int getPlateCount() {
		return plateCount;
	}
	public void setPlateCount(int plateCount) {
		this.plateCount = plateCount;
	}
	public void setGameWidth(int gameWidth) {
		this.gameWidth = gameWidth;
	}
	public int getGameWidth() {
		return gameWidth;
	}
	public void setGameHeight(int gameHeight) {
		this.gameHeight = gameHeight;
	}
	public int getGameHeight() {
		return gameHeight;
	}
	public void setPanelInstance(GamePanel panelInstance) {
		if (panelInstance == getPanelInstance()) {
			return;
		}
		if (panelInstance!=null) {
			if (getPanelInstance()!=null) {
				getPanelInstance().setGameInstance(null);
			}
			this.panelInstance = panelInstance;
			panelInstance.setGameInstance(this);
		}
	}
	public GamePanel getPanelInstance() {
		return panelInstance;
	}
	
	public BaseGame(){
		this(DEFAULT_GAME_WIDTH, DEFAULT_GAME_HEIGHT, null);
	}
	
	public BaseGame(int gameWidth, int gameHeight, GamePanel panel){
		this.setGameWidth(gameWidth);
		this.setGameHeight(gameHeight);
		this.setPanelInstance(panel);
	}
	
	public void init(){
		int sw = Stick.DEFAULT_STICK_WIDTH * gameWidth / DEFAULT_GAME_WIDTH;
		int sh = Stick.DEFAULT_STICK_HEIGHT * gameHeight / DEFAULT_GAME_HEIGHT;
		
		stick1 = new Stick(Stick.STICK1_NAME, sw, sh);
		stick2 = new Stick(Stick.STICK2_NAME, sw, sh);
		stick3 = new Stick(Stick.STICK3_NAME, sw, sh);
		
		reset();
	}
	
	public void reset(){
		if (stick2!=null) {
			stick2.getPlateStack().clear();
		}
		if (stick3!=null) {
			stick3.getPlateStack().clear();
		}
		if (stick1!=null) {
			stick1.getPlateStack().clear();
			for (int i = 1; i <= plateCount; i++) {
				stick1.pushAPlate(
						new Plate(i, (gameWidth >> 2), Plate.DEFAULT_PLATE_HEIGHT * gameHeight / DEFAULT_GAME_HEIGHT));
			}
		}
		
		panelInstance.repaint();
	}
	
	public void moveToStick(Stick fromStick, Stick targetStick) {
		Plate movePlate = fromStick.getTopPlate();
		if (targetStick.canMoveTo(movePlate) && !(fromStick == targetStick)) {
			movePlate = fromStick.popAPlate();
			targetStick.pushAPlate(movePlate);
		}
		if (movePlate!=null) {
			movePlate.setColor(Color.BLACK);
		}
		panelInstance.repaint();
	}
	
	public void clear(){
		stick1 = null;
		stick2 = null;
		stick3 = null;
		panelInstance.repaint();
	}
	
	protected Image bufferImage;
	protected void drawBuffer() {
		if (bufferImage == null) {
			bufferImage = panelInstance.createImage(gameWidth, gameHeight);
		}
		Graphics graphics = bufferImage.getGraphics();
		graphics.clearRect(0, 0, gameWidth, gameHeight);
		
		if (stick1!=null) {
			int offsetH = (gameHeight>>1) - (stick1.getStickHeight()>>1);
			Stick.drawStick(graphics, stick1, (gameWidth >> 2), offsetH);
			Stick.drawStick(graphics, stick2, (gameWidth >> 1), offsetH);
			Stick.drawStick(graphics, stick3, (gameWidth >> 2) * 3, offsetH);
		}
		
	}
	public void paintGame(Graphics graphics) {
		drawBuffer();
		graphics.drawImage(bufferImage, 0, 0, panelInstance);
	}
	
}
