package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

public abstract class Sprite {

    protected int x;
    protected int y;

    protected int xVel = 2;
    protected int yVel = 2;

    protected final int VELOCITY = 2;

    public PImage sprite;

    public Sprite(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setSprite(PImage sprite) {
        this.sprite = sprite;
    }

    /**
     * All the logic is written in this function
    */
    public abstract void tick();


    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
