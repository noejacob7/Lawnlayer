package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BeetleTest {
    
    @Test
    public void constructor(){
        assertNotNull(new Beetle(100, 100));
    }
}
