package processing.swarm.ant;

public class Direction {
    int directionx;
    int directiony;
    
    protected Direction(int directionx, int directiony){
        this.directionx = directionx;
        this.directiony = directiony;
    }
    
    protected boolean equals (Direction direction){
        return ((directionx == direction.directionx) && (directiony == direction.directiony));
    }
}
