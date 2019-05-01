package processing.swarm.ant;

import java.util.ArrayList;
import processing.core.PApplet;

public class Anthill {
    PApplet sketch;
    Pheromone pheromone; 
        
    //protected int antCount = 0;
    protected ArrayList antsArray = new ArrayList();
    
    protected int anthillx, anthilly;
    public int foodAccumulated = 0;
    
    protected int maxAntLife; 

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
    
    public void releaseAnts(int antReleaseRate, int maxAntLife, int maxAnts){
        for (int ant = 0; ant < antReleaseRate; ant++){
            if(antsArray.size() < maxAnts){
                antsArray.add(new Ant(sketch,this,maxAntLife));
                //antCount++;
            }
        }
    }

    public void moveAntsForFood(int timeGearing, Food food) {
        for (int i = 0; i < antsArray.size() * timeGearing; i++) {
            int antRandom = (int) (Math.random()* antsArray.size());
            Ant ant = (Ant) antsArray.get(antRandom);
            ACO(ant,food);
        }
    }

    protected void ACO(Ant ant,Food food) {
        if (ant.hasFood) {
            if (ant.atAnthill()) { //Ant has food and it's at home
                ant.hasFood = false;
                pheromone.positions[ant.antx][ant.anty].pheromoneHome = pheromone.maxPheromone;
                spin180(ant);
                findFood(ant);
                foodAccumulated++;
                // Did we collect 95% of the food?
                if (foodAccumulated > 0.95 * food.foodSources * food.foodPerSources) {
                    sketch.exit();
                }
            } else {
                pheromone.positions[ant.antx][ant.anty].pheromoneFood += (pheromone.maxPheromone * ant.life / ant.maxAntLife);
                pheromone.positions[ant.antx][ant.anty].pheromoneFood = Math.min(pheromone.positions[ant.antx][ant.anty].pheromoneFood, pheromone.maxPheromone);
                findAnthill(ant);
            }
        } else {
            if (ant.atFood(pheromone.positions)) {
                pheromone.positions[ant.antx][ant.anty].foodCount--;
                ant.hasFood = true;
                spin180(ant);
                pheromone.positions[ant.antx][ant.anty].pheromoneFood = pheromone.maxPheromone;
                findAnthill(ant);
            } else if (ant.atAnthill()) {
                pheromone.positions[ant.antx][ant.anty].pheromoneHome = pheromone.maxPheromone;
                findFood(ant);
            } else {
                pheromone.positions[ant.antx][ant.anty].pheromoneHome += (pheromone.maxPheromone * ant.life / ant.maxAntLife);
                pheromone.positions[ant.antx][ant.anty].pheromoneHome = Math.min(pheromone.positions[ant.antx][ant.anty].pheromoneHome, pheromone.maxPheromone);
                findFood(ant);
            }
        }
        
        ant.life--;
        if (ant.life == 0) {
            /*for (int a = 0; a < antsArray.size(); a++) {  // NOTE: Very slow way to remove this ant from the ants ArrayList...
                Ant ant_tocheck = (Ant) antsArray.get(a);
                if (ant_tocheck.id == ant.id) {
                    if ((ant.hasFood) && (!ant.atAnthill())) {
                        pheromone.positions[ant.antx][ant.anty].foodCount ++;
                    }
                    antsArray.remove(a);
                    antCount--;
                    break;
                }
            }*/
            //v2
            if ((ant.hasFood) && (!ant.atAnthill())) {
                pheromone.positions[ant.antx][ant.anty].foodCount ++;
            }
            antsArray.remove(ant);
            //antCount--;
            
        }
    }

    private void findAnthill(Ant ant) {
        findObject("anthill", ant);
    }

    private void findFood(Ant ant) {
        findObject("food", ant);
    }

    private void spin180(Ant ant) {
        ant.direction.directionx = -1 * ant.direction.directionx;
        ant.direction.directiony = -1 * ant.direction.directiony;
    }
    
    public void setMaxAntLife(int maxAntLife){
        this.maxAntLife = maxAntLife;
    }

    private void findObject(String object, Ant ant) { //Using Roulette Wheel Selection
        Direction[] directions = ant.orientation.forwardDirections(ant);
        int directionsCount = directions.length;
        // Weights
        float[] weights = new float[directionsCount];
        for (int i = 0; i < directionsCount; i++) {
            if (pheromone.positions[ant.antx + directions[i].directionx][ant.anty + directions[i].directiony].inaccessible == false) {
                if (object == "anthill") {
                    weights[i] = (float) Math.pow(pheromone.alpha + pheromone.positions[ant.antx + directions[i].directionx][ant.anty + directions[i].directiony].pheromoneHome / pheromone.maxPheromone, pheromone.beta);
                } else if (object == "food") {
                    weights[i] = (float) Math.pow(pheromone.alpha + pheromone.positions[ant.antx + directions[i].directionx][ant.anty + directions[i].directiony].pheromoneFood / pheromone.maxPheromone, pheromone.beta);
                }
            }
        }
        // Weights Sum
        float[] weights_sum = new float[directionsCount];
        weights_sum[0] = weights[0];
        for (int i = 1; i < directionsCount; i++) {
            weights_sum[i] = weights_sum[i - 1] + weights[i];
        }

        //In weight_sum vector we have the probabilities of choosen a path, now we have to calculate the next direction and move there
        //So we have to find the r number (the random number to choose a path)
        //Calculate the direction and move there
        float r = (float) (Math.random()*weights_sum[directionsCount - 1]);
        for (int j = 0; j < directionsCount; j++) {
            if (r < weights_sum[j]) {
                int x = ant.antx + directions[j].directionx;
                int y = ant.anty + directions[j].directiony;
                if (pheromone.positions[ant.antx][ant.anty].inaccessible == false) {
                    ant.antx = x;
                    ant.anty = y;
                    ant.direction.directionx = directions[j].directionx;
                    ant.direction.directiony = directions[j].directiony;
                }
                break;
            }
            spin180(ant);
        }
    }
    
    public void drawAnts(int antColorR, int antColorG, int antColorB, int antColorWithFoodR, int antColorWithFoodG, int antColorWithFoodB) {
        for (int a = 0; a < antsArray.size(); a++) {
            Ant ant = (Ant) antsArray.get(a);
            if (ant.hasFood) {
                sketch.fill(antColorWithFoodR, antColorWithFoodG, antColorWithFoodB);
                sketch.stroke(antColorWithFoodR, antColorWithFoodG, antColorWithFoodB);
                sketch.rect(ant.antx, ant.anty, 1, 1);
                sketch.fill(255);
                sketch.stroke(255);
            } else {
                sketch.fill(antColorR, antColorG, antColorB);
                sketch.stroke(antColorR, antColorG, antColorB);
                sketch.rect(ant.antx, ant.anty, 1, 1);
                sketch.fill(255);
                sketch.stroke(255);
            }
        }
    }
}
