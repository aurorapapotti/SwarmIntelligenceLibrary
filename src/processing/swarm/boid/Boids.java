package processing.swarm.boid;

import java.util.ArrayList;
import processing.core.PApplet;

public class Boids {
    
    protected PApplet sketch; 

    protected ArrayList<Bird> birds;
    protected ArrayList<Obstacle> obstacles;

    protected float globalScale = (float).91;
    protected float eraseRadius = 20;
    protected String tool = "boids";

    protected float maxSpeed;
    protected float friendRadius;
    protected float crowdRadius;
    protected float avoidRadius;
    protected float coheseRadius;

    protected boolean option_friend;
    protected boolean option_crowd;
    protected boolean option_avoid;
    protected boolean option_noise;
    protected boolean option_cohese;
    
    public Boids(PApplet sketch){
        this.sketch = sketch; 
        birds = new ArrayList<Bird>();
        obstacles = new ArrayList<Obstacle>();
        option_friend = true;
        option_crowd = true;
        option_avoid = true;
        option_noise = true;
        option_cohese = true;
    }
    
    public void recalculateConstants() {
        maxSpeed = (float) (2.1 * globalScale);
        friendRadius = 60 * globalScale;
        crowdRadius = (float) (friendRadius / 1.3);
        avoidRadius = 90 * globalScale;
        coheseRadius = friendRadius;
    }

    public void setupWalls() {
        obstacles = new ArrayList<Obstacle>();
        for (int x = 0; x < sketch.width; x += 20) {
            obstacles.add(new Obstacle(sketch, x, 10));
            obstacles.add(new Obstacle(sketch, x, sketch.height - 10));
        }
    }

    public void setupCircle() {
        obstacles = new ArrayList<Obstacle>();
        for (int x = 0; x < 50; x += 1) {
            float dir = (float) ((x / 50.0) * Math.PI * 2);
            obstacles.add(new Obstacle(sketch, (float)(sketch.width * 0.5 + Math.cos(dir) * sketch.height * .4), (float)(sketch.height * 0.5 + Math.sin(dir) * sketch.height * .4)));
        }
    }

    public void drawBoids() {
        sketch.noStroke();
        sketch.colorMode(sketch.HSB);
        sketch.fill(0, 100);
        sketch.rect(0, 0, sketch.width, sketch.height);

        if (tool == "erase") {
            sketch.noFill();
            sketch.stroke(0, 100, 260);
            sketch.rect(sketch.mouseX - eraseRadius, sketch.mouseY - eraseRadius, eraseRadius * 2, eraseRadius * 2);
            if (sketch.mousePressed) {
                erase();
            }
        } else if (tool == "avoids") {
            sketch.noStroke();
            sketch.fill(0, 200, 200);
            sketch.ellipse(sketch.mouseX, sketch.mouseY, 15, 15);
        }
        for (int i = 0; i < birds.size(); i++) {
            Bird current = birds.get(i);
            current.moveBird();
            current.drawBird();
        }

        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle current = obstacles.get(i);
            current.drawObstacle();
        }

        /*if (messageTimer > 0) {
            messageTimer -= 1;
        }
        drawGUI();*/
    }
    
    /*
    void drawGUI() {
        if (messageTimer > 0) {
            fill((min(30, messageTimer) / 30.0) * 255.0);

            text(messageText, 10, height - 20);
        }
    }

    String s(int count) {
        return (count != 1) ? "s" : "";
    }*/
    
    /*
    protected String on(boolean in) {
        return in ? "on" : "off";
    }*/


    protected void erase() {
        for (int i = birds.size() - 1; i > -1; i--) {
            Bird bird = birds.get(i);
            if (Math.abs(bird.position.x - sketch.mouseX) < eraseRadius && Math.abs(bird.position.y - sketch.mouseY) < eraseRadius) {
                birds.remove(i);
            }
        }

        for (int i = obstacles.size() - 1; i > -1; i--) {
            Obstacle obstacle = obstacles.get(i);
            if (Math.abs(obstacle.position.x - sketch.mouseX) < eraseRadius && Math.abs(obstacle.position.y - sketch.mouseY) < eraseRadius) {
                obstacles.remove(i);
            }
        }
    }
    
    /*
    void drawText(String s, float x, float y) {
        fill(0);
        text(s, x, y);
        fill(200);
        text(s, x - 1, y - 1);
    }

    void message(String in) {
        messageText = in;
        messageTimer = (int) frameRate * 3;
    }
    */
    
    public String getTool(){
        return tool; 
    }
    
    public void setTool(String tool){
        this.tool = tool; 
    }
    
    public void decreaseScale(float globalScale){
        this.globalScale *= globalScale; 
    }
    
    public void increaseScale(float globalScale){
        this.globalScale /= globalScale; 
    } 
    
    public void addBoid(){
        birds.add(new Bird(sketch, this, sketch.mouseX, sketch.mouseY));
    }
    
    public void addObstacle(){
        obstacles.add(new Obstacle(sketch, sketch.mouseX, sketch.mouseY));
    }

    public void setOption_friend() {
        option_friend = option_friend ? false : true;
    }

    public void setOption_crowd() {
        option_crowd = option_crowd ? false : true;
    }

    public void setOption_avoid() {
        option_avoid = option_avoid ? false : true;
    }

    public void setOption_noise() {
        option_noise =  option_noise ? false : true;
    }

    public void setOption_cohese() {
        option_cohese = option_cohese ? false : true; 
    }
     
}
