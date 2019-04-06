package processing.swarm.ant;

public class Orientation {

    protected Direction north = new Direction(0, 1);
    protected Direction north_east = new Direction(1, 1);
    protected Direction east = new Direction(1, 0);
    protected Direction south_east = new Direction(1, -1);
    protected Direction south = new Direction(0, -1);
    protected Direction south_west = new Direction(-1, -1);
    protected Direction west = new Direction(-1, 0);
    protected Direction north_west = new Direction(-1, 1);
    protected Direction[] allDirections = {north, north_east, east, south_east, south, south_west, west, north_west};

    Direction[] forwardDirections(Ant ant) {
        if (ant.direction.equals(north)) {
            Direction[] possibleDirections = {north_west, north, north_east};
            return possibleDirections;
        } else if (ant.direction.equals(north_east)) {
            Direction[] possibleDirections = {north, north_east, east};
            return possibleDirections;
        } else if (ant.direction.equals(east)) {
            Direction[] possibleDirections = {north_east, east, south_east};
            return possibleDirections;
        } else if (ant.direction.equals(south_east)) {
            Direction[] possibleDirections = {east, south_east, south};
            return possibleDirections;
        } else if (ant.direction.equals(south)) {
            Direction[] possibleDirections = {south_east, south, south_west};
            return possibleDirections;
        } else if (ant.direction.equals(south_west)) {
            Direction[] possibleDirections = {south, south_west, west};
            return possibleDirections;
        } else if (ant.direction.equals(west)) {
            Direction[] possibleDirections = {south_west, west, north_west};
            return possibleDirections;
        } else if (ant.direction.equals(north_west)) {
            Direction[] possibleDirections = {west, north_west, north};
            return possibleDirections;
        } else {
            return allDirections;
        }
    }
}
