package processing.swarm.ant;

public class Position {
    protected int foodCount;
    protected float pheromoneHome;
    protected float pheromoneFood; 
    protected boolean inaccessible; 
    
    public Position() {
        this.foodCount = 0; 
        this.pheromoneHome = 0; 
        this.pheromoneFood = 0;
    }
}
