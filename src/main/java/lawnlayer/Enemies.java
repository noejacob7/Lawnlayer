package lawnlayer;

import processing.core.PApplet;

public class Enemies extends Sprite {

    public int powerup1Timer;

    public Enemies(int x, int y){
        super(x,y);
        // Randomly choose the direction to move.
        this.xVel = Math.random() >= 0.5 ? this.xVel*-1 : this.xVel;
        this.yVel = Math.random() >= 0.5 ? this.yVel*-1 : this.yVel;
    }


    /**
     * The movement and all logic for the enemies is written in this function
     * The function is called every frame
     * The powerup effect is also implemented here
    */
    public void tick(){

        // Resetting the power Up timer and the powerup changes
        if (this.powerup1Timer >= 10*App.FPS){
            this.powerup1Timer = 0;
            App.powerup1Actived = false;
        }

        // Powerup effect implemented
        if (App.powerup1Actived){
            this.x += xVel/2;
            this.y += yVel/2;
            this.powerup1Timer++;
        }
        // Normal Movement of enemies
        else{
            this.x += xVel;
            this.y += yVel;
        }

        // Make the enemies stay in the grid
        if (this.x <= App.SPRITESIZE){
            this.xVel = VELOCITY;
        }
        if (this.x >= App.WIDTH - (2*App.SPRITESIZE)){
            this.xVel = -VELOCITY;
        }
        if (this.y <= App.TOPBAR + App.SPRITESIZE){
            this.yVel = VELOCITY;
            
        }
        if (this.y >= App.HEIGHT - (2*App.SPRITESIZE)){
            this.yVel = -VELOCITY;
        }
    }

    /**
     * It checks if the enemy is colliding with any components of the map
     * It returns the coordinates of the object with which it is colliding
     * @param map is the map of the game
     * @return the coordinates of the object it touches
    */
    public int[] Collide(String[][] map){
        int newX = (this.x + (App.SPRITESIZE/2)) /20;
        int newY = ((this.y - App.TOPBAR) + (App.SPRITESIZE/2)) / 20;
        int[] xy = {newX, newY};
        if (this.x%20 <= 10 && this.y%20 <= 10){
            if (map[newY][newX +1] != null && map[newY][newX +1] != "P1" && map[newY][newX +1] != "P2"){
                this.xVel = -VELOCITY;
                xy[0] = newX +1;
                xy[1] = newY;
            }
            else if (map[newY][newX-1] != null && map[newY][newX -1] != "P1" && map[newY][newX -1] != "P2"){
                this.xVel = VELOCITY;
                xy[0] = newX -1;
                xy[1] = newY;
            }
            if (map[newY+1][newX] != null && map[newY+1][newX] != "P1" && map[newY+1][newX] != "P2"){
                this.yVel = -VELOCITY;
                xy[0] = newX;
                xy[1] = newY+1;
            }
            else if (map[newY-1][newX] != null && map[newY-1][newX] != "P1" && map[newY-1][newX] != "P2"){
                this.yVel = VELOCITY;
                xy[0] = newX;
                xy[1] = newY-1;
            }
            return xy;
        }

        return null;
    }

}
