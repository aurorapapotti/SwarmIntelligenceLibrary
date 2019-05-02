package processing.swarm.ant;

import java.util.Objects;
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
        id = UUID.randomUUID();
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

    @Override
    public boolean equals(Object o) {
        return (((Ant)o).id == this.id);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
