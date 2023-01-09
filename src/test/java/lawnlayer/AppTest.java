package lawnlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.data.JSONArray;
import processing.core.PFont;

import java.io.FileNotFoundException;

public class AppTest extends App {

    @Test
    public void constructor() {
        App app = new App();
        assertNotNull(app);
    }

    @Test
    public void settingsTest(){
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        assertEquals(1280, app.width);
        assertEquals(720, app.height);
    }  

    @Test
    public void imagesTest(){
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        assertNotNull(app.grass);
        assertNotNull(app.concrete);
        assertNotNull(app.pathGreen);
        assertNotNull(app.pathRed);
        assertNotNull(app.powerup1);
        assertNotNull(app.powerup2);
    }

    @Test
    public void floodFillTest(){
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.settings();
        app.setup();
        app.delay(1000);
        app.changingMapforFill();
        app.floodFill(app.map, 16, 30);
        for (int y = 0; y<map.length; y++){
            for (int x = 0; x<map[y].length; x++){
                if (app.map[y][x] != "X")
                assertEquals("IN",app.map[y][x]);
            }
        }
    }

    @Test
    public void drawtest(){
        App app = new App();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
    }

    @Test
    public void powerup1Test(){
        App app = new App();
        app.timer = 600;
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        app.powerup1();
        assertTrue(app.powerup1Enabled);
        for (int y = 0; y<map.length; y++){
            for (int x = 0; x<map[y].length; x++){
                if (app.map[y][x] == "P1"){
                    app.ball.x = x*app.SPRITESIZE;
                    app.ball.y = (y*app.SPRITESIZE) +app.TOPBAR;
                    break;
                }
            }
        }
        app.powerup1();
        assertTrue(app.powerup1Actived);
    }

    @Test
    public void powerup2Test(){
        App app = new App();
        app.timer = 900;
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        app.powerup2();
        assertTrue(app.powerup2Enabled);
        for (int y = 0; y<map.length; y++){
            for (int x = 0; x<map[y].length; x++){
                if (app.map[y][x] == "P2"){
                    app.ball.x = x*app.SPRITESIZE;
                    app.ball.y = (y*app.SPRITESIZE) +app.TOPBAR;
                    break;
                }
            }
        }
        app.powerup2();
        assertTrue(app.powerup2Actived);
    }

    @Test
    public void changeTileTest(){
        App app = new App();
        app.timer = 900;
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        app.changeTile = true;
        // PApplet.runSketch(new String[] {"App"}, app);
    }

    @Test
    public void gameresultTest(){
        App app = new App();
        app.timer = 900;
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        app.noofLevels = 0;
        app.levelIndex = 0;
        assertTrue(app.gameResult());
    }

    @Test
    public void informationbarTest(){
        App app = new App();
        app.timer = 900;
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        assertTrue(app.informationBar());
    }

    @Test
    public void deadTest(){
        App app = new App();
        app.timer = 900;
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        app.dead();
        assertEquals(2, app.lives);
    }

    @Test
    public void mapTest(){
        App app = new App();
        app.timer = 900;
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        app.map[0][1] = "G";
        app.map[0][2] = "R";

    }
    
    @Test
    public void nextlevelcheck(){
        App app = new App();
        app.timer = 900;
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        this.completed = 0.85;
        app.changelevel();
        assertEquals(1, app.levelIndex);
    }

    @Test
    public void changetilefromgreemtoredtest(){
        App app = new App();
        app.timer = 900;
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        app.changeTile = true;
        int[] xy = {20,20};
        app.greenTrailList.add(xy);
        app.map[20][20] = "R";
        app.timer = 0;
        app.changetilefromgreentored();
    }

    @Test
    public void drawingtest(){
        App app = new App();
        app.timer = 900;
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        app.map[1][0] = "G";
        app.map[1][1] = "X";
        app.map[1][2] = "O";
        app.map[1][3] = "R";
        app.map[1][4] = "P1";
        app.map[1][5] = "P2";
        assertNotNull(app.mapDraw());
    }
    
    @Test
    public void greentrailtest(){
        App app = new App();
        app.timer = 900;
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.ball.x = 100;
        app.ball.y = 100;
        app.delay(1000);
        assertEquals(1, app.greenTrailList.size());
    }

    // @Test
    // public void keypressTest(){
    //     App app = new App();
    //     PApplet.runSketch(new String[] {"App"}, app);
    //     app.setup();
    //     app.delay(1000);
    //     app.keyCode = 37;
    //     app.keyPressed(new KeyEvent(, 10, 1, 1, 37, "37"));
    //     assertTrue(app.ball.moveLeft);
    // }

    // @Test
    // public void jsonFileTest(){
    //     App app = new App();
    //     app.noLoop();
    //     PApplet.runSketch(new String[] {"App"}, app);
    //     app.configPath = "nothing.json";
    // }

    // Collide
    // Array Check all borders.
    // 
}
