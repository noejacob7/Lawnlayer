package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;

public class Ball extends Sprite {

    public boolean moveLeft;
    private boolean moveRight;
    private boolean moveUp;
    private boolean moveDown;
    public int timer;

    /**
     * True if the ball is on the concrete
    */
    public boolean onConcrete;

    // public char[][] pathGreen;

    public Ball(){
        super(0, App.TOPBAR);
        this.moveLeft = false;
        this.moveRight = false;
        this.moveUp = false;
        this.moveDown = false;
        this.timer = 0;
        this.onConcrete = false;
        // this.pathGreen = new char[31][63];
    }


    /**
     * The ball movement is calculated in this function.
     * Make sure that the ball stays on the map and the ball does not move continuously on the concrete.
    */
    public void tick(){

        if (this.timer == 0) {

            // if (this.y<=App.TOPBAR || this.y>=App.HEIGHT - App.SPRITESIZE || this.x<= 0 || this.x >= App.WIDTH - App.SPRITESIZE){
            //     this.xVel = 0;
            //     this.yVel = 0;
            // }

            if (this.onConcrete){
                this.xVel = 0;
                this.yVel = 0;
            }

            if (moveDown){
                this.moveDown = false;
                if (this.yVel == 0){
                    this.yVel = VELOCITY;
                    this.xVel = 0;
                }
            }
            if (moveUp){
                this.moveUp = false;
                if (this.yVel == 0){
                    this.yVel = -VELOCITY;
                    this.xVel = 0;
                }
            }
            if (moveRight){
                this.moveRight = false;
                if (this.xVel == 0){
                    this.xVel = VELOCITY;
                    this.yVel = 0;
                }
            }
            if (moveLeft){
                this.moveLeft = false;
                if (this.xVel == 0){
                    this.xVel = -VELOCITY;
                    this.yVel = 0;
                }
            }
            this.timer = 10;
        }

        this.x += xVel;
        this.y += yVel;

        if (this.timer <=10){
            this.timer--;
        }

        // Make sure the ball stays in the grid
        if (this.x <= 0){
            this.x = 0;
        }
        if (this.x >= App.WIDTH - App.SPRITESIZE){
            this.x = App.WIDTH - App.SPRITESIZE;
        }
        if (this.y <= App.TOPBAR){
            this.y = App.TOPBAR;
            
        }
        if (this.y >= App.HEIGHT - App.SPRITESIZE){
            this.y = App.HEIGHT - App.SPRITESIZE;
        }

    }

    public void pressLeft(){
        if (this.moveRight != true)
        this.moveLeft = true;
    }

    public void pressRight(){
        if (this.moveLeft != true)
        this.moveRight = true;
    }

    public void pressUp(){
        if (this.moveDown != true)
        this.moveUp = true;
    }

    public void pressDown(){
        if (this.moveUp != true)
        this.moveDown = true;
    }

    /**
     * Checks whether the ball is on the concrete
    */
    public void isConcrete(){
        this.onConcrete = false;
        if (App.map[(this.getY()-App.TOPBAR)/20 ][this.getX()/20] == "X"){
            this.onConcrete = true;
        }
    }

    /**
     * Sets the ball into its starting position.
    */
    public void restartBall(){
        this.x = 0;
        this.y = App.TOPBAR;
    }
}
