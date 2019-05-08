package processing.swarm.boid;

import processing.core.PApplet;
import processing.core.PVector;

public class Obstacle {
    PVector position; 
    PApplet sketch; 
    
    public Obstacle (PApplet sketch, float x, float y) {
        this.sketch = sketch; 
        position = new PVector(x,y); 
    }
    
    void go(){ //TODO:something in movement
    }
    
    void drawObstacle() {
        sketch.fill(0, 255, 200);
        sketch.ellipse(position.x, position.y, 15, 15); 
    }
}
