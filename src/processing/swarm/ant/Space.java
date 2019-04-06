package processing.swarm.ant;

import processing.core.PApplet;

public class Space {
    PApplet sketch; 
    
    protected int spaceWidth;
    protected int spaceHeight;
    protected Position[][] positions;
    
    public Space(PApplet sketch) {
        this.sketch = sketch;         
    }
    
    public void initSpace(Position[][] positions, int spaceWidht, int spaceHeight){
        this.positions = positions;
        this.spaceWidth = spaceWidht;
        this.spaceHeight = spaceHeight;
        for (int spacex = 0; spacex < spaceWidth + 2; spacex++){
            for (int spacey = 0; spacey < spaceHeight + 2; spacey++){
                positions[spacex][spacey] = new Position(); 
                if ((spacex == 0) || (spacex == spaceWidth + 1) || (spacey == 0) || (spacey == spaceHeight + 1))
                    positions[spacex][spacey].inaccessible = true; 
                else 
                    positions[spacex][spacey].inaccessible = false;
            }
        }
    }
}
