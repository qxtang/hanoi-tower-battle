package com.tqx.entity;

import java.awt.*;
import java.util.Stack;

public class Stick {

	private Stack<Plate> plateStack = new Stack<Plate>();

	private int stickWidth;
	private int stickHeight;
	private String name = "";

	public static int DEFAULT_STICK_WIDTH = 10;
	public static int DEFAULT_STICK_HEIGHT = 300;
	public static String STICK1_NAME = "stick1";
	public static String STICK2_NAME = "stick2";
	public static String STICK3_NAME = "stick3";

	public int getStickWidth() {
		return stickWidth;
	}

	public void setStickWidth(int stickWidth) {
		this.stickWidth = stickWidth;
	}

	public int getStickHeight() {
		return stickHeight;
	}

	public void setStickHeight(int stickHeight) {
		this.stickHeight = stickHeight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Stick() {
		this("", DEFAULT_STICK_WIDTH, DEFAULT_STICK_HEIGHT);
	}

	public Stick(String name, int width, int height) {
		this.setName(name);
		this.setStickHeight(height);
		this.setStickWidth(width);
	}

	public boolean canMoveTo(Plate movePlate) {
		if (movePlate!=null) {
			if (plateStack.isEmpty()) {
				return true;
			}
			Plate topPlate = plateStack.peek();
			if ((topPlate != null) && (movePlate.getNumber() > topPlate.getNumber())) {
				return true;
			}
		}
		
		return false;
	}

	public Plate popAPlate() {

		if (!plateStack.isEmpty()) {
			return plateStack.pop();
		}
		return null;
	}

	public Plate pushAPlate(Plate pushPlate) {
		if (this.canMoveTo(pushPlate)) {
			return plateStack.push(pushPlate);
		}
		return null;
	}

	public Plate getTopPlate() {
		if (plateStack.size() != 0) {
			return plateStack.peek();
		}
		return null;
	}

	public Stack<Plate> getPlateStack() {
		return plateStack;
	}

	public void showTopClearly() {
		if (!plateStack.isEmpty()) {
			Plate tp = plateStack.pop();
			tp.setColor(Color.RED);
			pushAPlate(tp);
		}
	}

	public void hideTopClearly() {
		if (!plateStack.isEmpty()) {
			Plate tp = plateStack.pop();
			tp.setColor(Color.BLACK);
			pushAPlate(tp);
		}
	}

	public static void drawStick(Graphics graphics, Stick stick, int x, int y) {

		if (stick != null) {
			graphics.setColor(Color.BLACK);
			graphics.fillRect(x - (stick.getStickWidth() >> 1), y, stick.getStickWidth(), stick.getStickHeight());

			int i = 9;
			for (Plate plate : stick.plateStack) {
				Plate.drawPlate(graphics, plate, x - (plate.getWidth() >> 1), y + plate.getHeight() * i);
				i--;
			}
		}
	}

}
