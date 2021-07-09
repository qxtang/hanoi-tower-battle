package com.tqx.entity;

import java.awt.*;

public class Plate {
    private int number;
    private int width;
    private int height;
    private Color color;

    public static int DEFAULT_PLATE_WIDTH = 200;
    public static int DEFAULT_PLATE_HEIGHT = 30;

    public Plate(int number) {
        this.setNumber(number);
        this.setWidth(DEFAULT_PLATE_WIDTH - (number - 1) * 15);

        this.setHeight(DEFAULT_PLATE_HEIGHT);
        this.setColor(Color.BLACK);

    }

    public Plate(int number, int width, int height) {
        this.setNumber(number);
        this.setHeight(height);
        this.setWidth(width - (number - 1) * 15);
        this.setColor(Color.BLACK);
    }

    public Plate() {
        this(1);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static void drawPlate(Graphics graphics, Plate plate, int x, int y) {

        if (plate != null) {
            int width = plate.getWidth();
            int height = plate.getHeight();

            graphics.setColor(plate.getColor());
            graphics.fillRect(x, y, width, height);
            graphics.setColor(Color.WHITE);
            graphics.drawRect(x, y, width, height);

            graphics.setFont(new Font("", Font.BOLD, 15));
            graphics.drawString("" + plate.getNumber(), x + (width >> 1), y + (height >> 1));
        }
    }

}
