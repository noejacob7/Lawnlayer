package lawnlayer;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WormTest {

    @Test
    public void constructor(){
        assertNotNull(new Worm(100, 100));
    }
    
    // @Test
    // public void movementTest(){
    //     Worm worm = new Worm(300, 300);
    //     int x = worm.getX();
    //     int y = worm.getY();
    //     worm.tick();
    //     assertEquals(x + worm.xVel, worm.getX());
    //     assertEquals(y + worm.yVel, worm.getY());
    // }
}
