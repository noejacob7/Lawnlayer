package lawnlayer;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BallTest {

    @Test
    public void constructor(){
        assertNotNull(new Ball());
    }

    @Test
    public void onConcreteTest(){
        Ball ball = new Ball();
        int x = ball.getX();
        int y = ball.getY();
        ball.onConcrete = true;
        ball.tick();
        assertEquals(x, ball.getX());
        assertEquals(y, ball.getY());
    }

    @Test
    public void onConcretefunctionTest(){
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        int x = app.ball.getX();
        int y = app.ball.getY();
        app.map[0][0] = "X";
        app.ball.isConcrete();
        app.ball.tick();
        assertEquals(x, app.ball.getX());
        assertEquals(y, app.ball.getY());
    }

    @Test
    public void startingPositionOfBall(){
        Ball ball = new Ball();
        assertEquals(0, ball.getX());
        assertEquals(80, ball.getY());
    }

    @Test
    public void spriteTest(){
        App app = new App();
        app.noLoop();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.delay(1000);
        assertNotNull(app.ball.sprite);
    }

    @Test
    public void pressRightTest(){
        Ball ball = new Ball();
        ball.onConcrete = true;
        ball.pressRight();
        for (int i = 0; i < 10; i++){
            ball.tick();
        }
        assertEquals(20, ball.getX());
        assertEquals(80, ball.getY());
    }

    @Test
    public void pressLeftTest(){
        Ball ball = new Ball();
        ball.x = 1260;
        ball.y = 620;
        ball.onConcrete = true;
        ball.pressLeft();
        for (int i = 0; i < 10; i++){
            ball.tick();
        }
        assertEquals(1240, ball.getX());
        assertEquals(620, ball.getY());
    }

    @Test
    public void pressUpTest(){
        Ball ball = new Ball();
        ball.x = 1260;
        ball.y = 620;
        ball.onConcrete = true;
        ball.pressUp();
        for (int i = 0; i < 10; i++){
            ball.tick();
        }
        assertEquals(1260, ball.getX());
        assertEquals(600, ball.getY());
    }

    @Test
    public void pressDownTest(){
        Ball ball = new Ball();
        ball.onConcrete = true;
        ball.pressDown();
        for (int i = 0; i < 10; i++){
            ball.tick();
        }
        assertEquals(0, ball.getX());
        assertEquals(100, ball.getY());
    }

    @Test
    public void restartBallTest(){
        Ball ball = new Ball();
        ball.x = 1260;
        ball.y = 620;
        ball.onConcrete = true;
        ball.pressUp();
        for (int i = 0; i < 10; i++){
            ball.tick();
        }
        ball.restartBall();
        assertEquals(0, ball.getX());
        assertEquals(80, ball.getY());
    }
    
    @Test
    public void BallStstaysonGridTest(){
        Ball ball = new Ball();
        ball.x = 1260;
        ball.y = 700;
        ball.pressDown();
        for (int i = 0; i < 10; i++){
            ball.tick();
        }
        assertEquals(1260, ball.getX());
        assertEquals(700, ball.getY());
        ball.pressRight();
        for (int i = 0; i < 10; i++){
            ball.tick();
        }
        assertEquals(1260, ball.getX());
        assertEquals(700, ball.getY());
    }
}
