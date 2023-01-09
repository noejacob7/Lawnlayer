package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;


public class EnemiesTest {
    
    @Test
    public void movementTest(){
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        Beetle beetle = new Beetle(300,300);
        int x = beetle.getX();
        int y = beetle.getY();
        int xDirection = beetle.xVel/2;
        int yDirection = beetle.yVel/2;
        beetle.tick();
        x+= (2*xDirection);
        y+= (2*yDirection);
        assertEquals(x, beetle.getX());
        assertEquals(y, beetle.getY());
    }

    @Test
    public void powerupTest(){
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        Beetle beetle = new Beetle(40,200);
        beetle.xVel = beetle.VELOCITY;
        beetle.yVel = beetle.VELOCITY;
        int x = beetle.getX();
        int y = beetle.getY();
        int xDirection = 1;
        int yDirection = 1;
        app.powerup1Actived = true;
        for (int i = 0; i < app.FPS*15; i++) {
            beetle.tick();
        }
        for (int i = 0; i < app.FPS*10; i++) {
            x += xDirection;
            y += yDirection;
            if (x <= app.SPRITESIZE){
                xDirection *=-1;
            }
            if (x >= app.WIDTH - (2*app.SPRITESIZE)){
                xDirection *=-1;
            }
            if (y <= app.TOPBAR + app.SPRITESIZE){
                yDirection *=-1;
                
            }
            if (y >= app.HEIGHT - (2*app.SPRITESIZE)){
                yDirection *=-1;
            }
        }
        xDirection*=2;
        yDirection*=2;
        for (int i = 0; i < app.FPS*5; i++) {
            x += xDirection;
            y += yDirection;
            if (x <= app.SPRITESIZE){
                xDirection *=-1;
            }
            if (x >= app.WIDTH - (2*app.SPRITESIZE)){
                xDirection *=-1;
            }
            if (y <= app.TOPBAR + app.SPRITESIZE){
                yDirection *=-1;
                
            }
            if (y >= app.HEIGHT - (2*app.SPRITESIZE)){
                yDirection *=-1;
            }
        }
        assertEquals(x,beetle.getX());
        assertEquals(y, beetle.getY());
    }

    @Test
    public void collisionTest1(){
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        app.map[30][50] = "X";
        app.map[29][51] = "X";
        Worm worm = new Worm(50*app.SPRITESIZE, (29*app.SPRITESIZE) + app.TOPBAR);
        int x = worm.getX();
        int y = worm.getY();
        worm.xVel = worm.VELOCITY;
        worm.yVel = worm.VELOCITY;

        worm.Collide(app.map);
        worm.tick();
        assertEquals(x - 2, worm.getX());
        assertEquals(y - 2, worm.getY());
    }

    @Test
    public void collisionTest2(){
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        app.map[24][30] = "X";
        app.map[25][29] = "X";
        Worm worm = new Worm(30*app.SPRITESIZE, (25*app.SPRITESIZE) + app.TOPBAR);
        int x = worm.getX();
        int y = worm.getY();
        worm.xVel = -worm.VELOCITY;
        worm.yVel = -worm.VELOCITY;

        worm.Collide(app.map);
        worm.tick();
        assertEquals(y + 2, worm.getY());
        assertEquals(x + 2, worm.getX());
    }
}
