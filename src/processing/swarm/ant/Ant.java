package processing.swarm.ant;

import java.util.UUID;
import processing.core.PApplet;

public class Ant {
    PApplet sketch;
    
    Anthill anthill;

    protected int maxAntLife = 1000;
    protected int timeGearing;

    protected int life = maxAntLife;
    protected boolean hasFood;

    protected UUID id;

    protected int antx, anty;
    protected Direction direction;

    protected int antColorR, antColorG, antColorB;
    protected int antColorWithFoodR, antColorWithFoodG, antColorWithFoodB;

    Food food;
    
    Orientation orientation = new Orientation();

    public Ant(PApplet sketch, Anthill anthill, int maxAntLife) {
        this.sketch = sketch;
        this.anthill = anthill; 
        this.maxAntLife = maxAntLife;
        antx = anthill.anthillx;
        anty = anthill.anthilly;
        hasFood = false;

        direction = new Direction(0, 0);
        direction.directionx = (int) (Math.random()*2 -1);
        direction.directiony = (int) (Math.random()*2 -1);
    }
    
    protected boolean atAnthill() { 
        return ((antx == anthill.anthillx) && (anty == anthill.anthilly));
    }

    protected boolean atFood(Position[][] positions) {
        return (positions[antx][anty].foodCount > 0);
    }
    /*
    public void moveAntsForFood(int timeGearing, Food food) {
        for (int i = 0; i < anthill.antsArray.size() * timeGearing; i++) {
            int antRandom = (int) (Math.random()* anthill.antsArray.size());
            Ant ant = (Ant) anthill.antsArray.get(antRandom);
            ant.ACO(food);
        }
    }

    protected void ACO(Food food) {
        if (hasFood) {
            if (atAnthill()) { //Ant has food and it's at home
                hasFood = false;
                anthill.pheromone.positions[antx][anty].pheromoneHome = anthill.pheromone.maxPheromone;
                spin180();
                findFood(this);
                anthill.foodAccumulated++;
                // Did we collect 95% of the food?
                if (anthill.foodAccumulated > 0.95 * food.foodSources * food.foodPerSources) {
                    sketch.exit();
                }
            } else {
                anthill.pheromone.positions[antx][anty].pheromoneFood += (anthill.pheromone.maxPheromone * life / maxAntLife);
                anthill.pheromone.positions[antx][anty].pheromoneFood = Math.min(anthill.pheromone.positions[antx][anty].pheromoneFood, anthill.pheromone.maxPheromone);
                findAnthill(this);
            }
        } else {
            if (atFood(anthill.pheromone.positions)) {
                anthill.pheromone.positions[antx][anty].foodCount--;
                hasFood = true;
                spin180();
                anthill.pheromone.positions[antx][anty].pheromoneFood = anthill.pheromone.maxPheromone;
                findAnthill(this);
            } else if (atAnthill()) {
                anthill.pheromone.positions[antx][anty].pheromoneHome = anthill.pheromone.maxPheromone;
                findFood(this);
            } else {
                anthill.pheromone.positions[antx][anty].pheromoneHome += (anthill.pheromone.maxPheromone * life / maxAntLife);
                anthill.pheromone.positions[antx][anty].pheromoneHome = Math.min(anthill.pheromone.positions[antx][anty].pheromoneHome, anthill.pheromone.maxPheromone);
                findFood(this);
            }
        }
        
        life--;
        if (life == 0) {
            for (int a = 0; a < anthill.antsArray.size(); a++) {  // NOTE: Very slow way to remove this ant from the ants ArrayList...
                Ant ant = (Ant) anthill.antsArray.get(a);
                if (ant.id == id) {
                    if ((ant.hasFood) && (!atAnthill())) {
                        anthill.pheromone.positions[antx][anty].foodCount ++;
                    }
                    anthill.antsArray.remove(a);
                    anthill.antCount--;
                    break;
                }
            }
        }
    }

    private void findAnthill(Ant ant) {
        findObject("anthill", ant);
    }

    private void findFood(Ant ant) {
        findObject("food", ant);
    }

    private void spin180() {
        direction.directionx = -1 * direction.directionx;
        direction.directiony = -1 * direction.directiony;
    }
    
    public void setMaxAntLife(int maxAntLife){
        this.maxAntLife = maxAntLife;
    }

    private void findObject(String object, Ant ant) { //Using Roulette Wheel Selection
        Direction[] directions = orientation.forwardDirections(ant);
        int directionsCount = directions.length;
        // Weights
        float[] weights = new float[directionsCount];
        for (int i = 0; i < directionsCount; i++) {
            if (anthill.pheromone.positions[ant.antx + directions[i].directionx][ant.anty + directions[i].directiony].inaccessible == false) {
                if (object == "anthill") {
                    weights[i] = (float) Math.pow(anthill.pheromone.alpha + anthill.pheromone.positions[ant.antx + directions[i].directionx][ant.anty + directions[i].directiony].pheromoneHome / anthill.pheromone.maxPheromone, anthill.pheromone.beta);
                } else if (object == "food") {
                    weights[i] = (float) Math.pow(anthill.pheromone.alpha + anthill.pheromone.positions[ant.antx + directions[i].directionx][ant.anty + directions[i].directiony].pheromoneFood / anthill.pheromone.maxPheromone, anthill.pheromone.beta);
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
                if (anthill.pheromone.positions[ant.antx][ant.anty].inaccessible == false) {
                    ant.antx = x;
                    ant.anty = y;
                    direction.directionx = directions[j].directionx;
                    direction.directiony = directions[j].directiony;
                }
                break;
            }
            spin180();
        }
    }

    public void drawAnts(int antColorR, int antColorG, int antColorB, int antColorWithFoodR, int antColorWithFoodG, int antColorWithFoodB) {
        for (int a = 0; a < anthill.antsArray.size(); a++) {
            Ant ant = (Ant) anthill.antsArray.get(a);
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
    }*/

    @Override
    public boolean equals(Object o) {
        return (((Ant)o).id == this.id);
    }
}
