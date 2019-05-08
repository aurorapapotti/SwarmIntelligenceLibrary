package processing.swarm.ant;

import processing.core.PApplet;



public class Food {
        
    PApplet sketch;
    
    protected int foodSources;
    protected int foodPerSources;
    protected int foodMaxDistance; 
    protected int foodMinDistance;
    
    protected int anthillx;
    protected int anthilly;
    
    protected Position[][] positions;
    protected int spaceWidth;
    protected int spaceHeight;
     
            
    public Food(PApplet sketch, int anthillx, int anthilly) {
        this.sketch = sketch;
        this.anthillx = anthillx;
        this.anthilly = anthilly;
    }
    
    public void setFoodParameter(int foodSources, int foodPerSources, int foodMaxDistance, int foodMinDistance){
        this.foodSources = foodSources;
        this.foodPerSources = foodPerSources;
        this.foodMaxDistance = foodMaxDistance;
        this.foodMinDistance = foodMinDistance;
    }

    public void initFood(Position[][] positions, int spaceWidth, int spaceHeight) {
        this.positions = positions;
        this.spaceWidth = spaceWidth;
        this.spaceHeight = spaceHeight;
        for(int food = 0; food < foodSources; food++){
            int foodx = 0;
            int foody = 0; 
            boolean checkAllocation = false; 
            while(!checkAllocation){
                foodx = (int) (Math.random()*spaceWidth);
                foody = (int) (Math.random()*spaceHeight);
                float foodDistance = (float) Math.sqrt(Math.pow((foodx - anthillx), 2) + Math.pow((foody - anthilly), 2));
                if ((foodDistance < foodMaxDistance) && (foodDistance > foodMinDistance)) {
                    positions[foodx][foody].foodCount = foodPerSources;
                    checkAllocation = true;
                }
            }
        }
    }
    

    public Position[][] getPositions() {
        return positions;
    }
    
    public void drawFood(int foodColorR, int foodColorG, int foodColorB){
        positions = getPositions();
        for (int x = 0; x < spaceHeight + 2; x++) {
            for (int y = 0; y < spaceWidth + 2; y++) {
                if (positions[x][y].foodCount > 0) {
                    sketch.fill(foodColorR, foodColorG, foodColorB);
                    sketch.stroke(foodColorR, foodColorG, foodColorB);
                    if (positions[x][y].foodCount > 0.75 * foodPerSources) {
                        sketch.rect(x, y, 3, 3);
                    } else if (positions[x][y].foodCount > 0.5 * foodPerSources) {
                        sketch.rect(x, y, 2, 2);
                    } else {
                        sketch.rect(x, y, 1, 1);
                    }
                    sketch.fill(255);
                    sketch.stroke(255);
                }
            }
        }
    }
}
