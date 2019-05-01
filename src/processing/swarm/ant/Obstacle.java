package processing.swarm.ant;

import processing.core.PApplet;

public class Obstacle {
    
    protected int obstacleWidth;
    protected int obstacleHeight;
    protected int obstaclex; 
    protected int obstacley; 
    
    PApplet sketch; 
    
    protected Position[][] positions; 

    public Obstacle(PApplet sketch, Position[][] positions) {
        this.sketch = sketch; 
        this.positions = this.positions; 
    }
    
    public void initObstacle(int obstacleWidth, int obstacleHeight, int obstaclex, int obstacley){
        this.obstacleWidth = obstacleWidth;
        this.obstacleHeight = obstacleHeight;
        this.obstaclex = obstaclex;
        this.obstacley = obstacley; 
        for (int i = obstaclex; i < obstacleWidth; i ++){
            for (int j = obstacley; j < obstacleWidth; j++){
                positions[i][j].inaccessible = true; 
            }
        }
    }
    
    public void drawObstacle(int obstacleColorR, int obstacleColorG, int obstacleColorB){
        sketch.fill(obstacleColorR, obstacleColorG, obstacleColorB);
        sketch.stroke(obstacleColorR, obstacleColorG, obstacleColorB);
        sketch.rect(obstaclex, obstacley, obstacleWidth, obstacleHeight);
        sketch.fill(255);
        sketch.stroke(255);
        
    }
      
}
