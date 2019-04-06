package processing.swarm.ant;

import java.util.ArrayList;
import processing.core.PApplet;

public class Anthill {
    PApplet sketch;
    Pheromone pheromone; 
        
    protected int antCount = 0;
    protected ArrayList antsArray = new ArrayList();
    
    protected int anthillx, anthilly;
    public int foodAccumulated = 0;

    protected int anthillColorR, anthillColorG, anthillColorB;

    public Anthill(PApplet sketch, int anthillx, int anthilly, Pheromone pheromone){
        this.sketch = sketch;
        this.anthillx = anthillx;
        this.anthilly = anthilly;
        this.pheromone = pheromone; 
    }
    
    public void drawAnthill(int anthillColorR, int anthillColorG, int anthillColorB){
        this.anthillColorR = anthillColorR;
        this.anthillColorG = anthillColorG;
        this.anthillColorB = anthillColorB;
        sketch.fill(anthillColorR, anthillColorG, anthillColorB);
        sketch.stroke(anthillColorR, anthillColorG, anthillColorB); 
        sketch.ellipse(anthillx, anthilly, 5, 5);
        sketch.fill(255);
        sketch.stroke(255);
    }
    
    public void releaseAnts(int antReleaseRate, int maxAnts){
        for (int ant = 0; ant < antReleaseRate; ant++){
            if(antCount < maxAnts){
                antsArray.add(new Ant(sketch, this, pheromone));
                antCount++;
            }
        }
    }    
}
