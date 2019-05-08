package processing.swarm.ant;

import processing.core.PApplet;

public class Pheromone {
    PApplet sketch;
    
    protected int maxPheromone;
    protected float evapRatePheromone;
    protected float diffRatePheromone;
    protected float alpha;
    protected float beta;
    
    Position[][] positions;
    protected Position[][] trials; 
    int spaceWidth;
    int spaceHeight;
    
    public Pheromone(PApplet sketch){
        this.sketch = sketch;       
    }
    
    public void setPheromoneParameter(int maxPheromone, float evapRatePheromone, float diffRatePheromone, float alpha, float beta){
        this.maxPheromone = maxPheromone;
        this.evapRatePheromone = evapRatePheromone;
        this.diffRatePheromone = diffRatePheromone;
        this.alpha = alpha;
        this.beta = beta;
    }

    public void initPheromone(Position[][] positions, int spaceWidth, int spaceHeight){
        this.positions = positions; 
        this.spaceWidth = spaceWidth;
        this.spaceHeight = spaceHeight;
        diffusePheromone();
        evaporatePheromone();       
    }

    public Position[][] getPositions() {
        return positions;
    }
    
    
    
    protected void diffusePheromone(){
        int[][] pheromoneFoodMatrix = new int[spaceWidth + 2][spaceHeight + 2];
        int[][] pheromoneHomeMatrix = new int[spaceWidth + 2][spaceHeight + 2];
        // Calculate the Pheromone Matrix
        for (int x = 2; x < spaceWidth; x++) {
            for (int y = 2; y < spaceHeight; y++) {
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        pheromoneFoodMatrix[x + k][y + l] += (int) ((positions[x][y].pheromoneFood - positions[x + k][y + l].pheromoneFood) * diffRatePheromone);
                        pheromoneHomeMatrix[x + k][y + l] += (int) ((positions[x][y].pheromoneHome - positions[x + k][y + l].pheromoneHome) * diffRatePheromone);
                    }
                }
            }
        }
        // Apply the pheromone matrix
        for (int x = 1; x < spaceWidth + 1; x++) {
            for (int y = 1; y < spaceHeight + 1; y++) {
                positions[x][y].pheromoneFood = positions[x][y].pheromoneFood + pheromoneFoodMatrix[x][y];
                positions[x][y].pheromoneHome = positions[x][y].pheromoneHome + pheromoneHomeMatrix[x][y];
            }
        }
    }
    
    protected void evaporatePheromone(){
        for (int x = 0; x < spaceWidth + 2; x++) {
            for (int y = 0; y < spaceHeight + 2; y++) {
                positions[x][y].pheromoneHome = (int) (positions[x][y].pheromoneHome * (1 - evapRatePheromone));
                positions[x][y].pheromoneFood = (int) (positions[x][y].pheromoneFood * (1 - evapRatePheromone));
            }
        }
    }
    
    public void drawPheromone(){
        for (int x = 0; x < spaceWidth + 2; x++) {
            for (int y = 0; y < spaceHeight + 2; y++) {
                if (positions[x][y].pheromoneFood + positions[x][y].pheromoneHome > 0) {
                    int b = (int) (255 * positions[x][y].pheromoneFood / maxPheromone);
                    int g = (int) (255 * positions[x][y].pheromoneHome / maxPheromone);
                    sketch.set(x, y, sketch.color(0, b, g));
                }
            }
        }
    }
}
