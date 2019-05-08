package processing.swarm.boid;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;

public class Bird {
    protected PApplet sketch; 
    
    protected Boids boids; 
    
    protected PVector position;
    protected PVector velocity;
    protected float shade;
    protected ArrayList<Bird> friends;

    // timers
    int thinkTimer = 0;

    public Bird(PApplet sketch, Boids boids,float x, float y) {
        this.sketch = sketch;
        this.boids = boids; 
        velocity = new PVector(0, 0);
        position = new PVector(0, 0);
        position.x = x;
        position.y = y; 
        thinkTimer = (int)(Math.random()*10); 
        shade = (float)(Math.random()*255); 
        friends = new ArrayList<Bird>();
    }

    protected void moveBird() {
        increment();
        wrap();

        if (thinkTimer == 0) {
            // update our friend array (lots of square roots)
            getFriends();
        }
        flock();
        position.add(velocity);
    }

    private void flock() {
        PVector allign = getAverageDir();
        PVector avoidDir = getAvoidDir();
        PVector avoidObjects = getAvoidAvoids();
        PVector noise = new PVector((float)((Math.random()*2)-1), (float)((Math.random()*2)-1));
        PVector cohese = getCohesion();

        allign.mult(1);
        if (!boids.option_friend) {
            allign.mult(0);
        }

        avoidDir.mult(1);
        if (!boids.option_crowd) {
            avoidDir.mult(0);
        }

        avoidObjects.mult(3);
        if (!boids.option_avoid) {
            avoidObjects.mult(0);
        }

        noise.mult((float)0.1);
        if (!boids.option_noise) {
            noise.mult(0);
        }

        cohese.mult(1);
        if (!boids.option_cohese) {
            cohese.mult(0);
        }

        sketch.stroke(0, 255, 160);

        velocity.add(allign);
        velocity.add(avoidDir);
        velocity.add(avoidObjects);
        velocity.add(noise);
        velocity.add(cohese);

        velocity.limit(boids.maxSpeed);

        shade += getAverageColor() * 0.03;
        shade += ((Math.random()*2)-1);
        shade = (shade + 255) % 255; //max(0, min(255, shade));
    }

    private void getFriends() {
        ArrayList<Bird> nearby = new ArrayList<Bird>();
        for (int i = 0; i < boids.birds.size(); i++) {
            Bird test = boids.birds.get(i);
            if (test == this) {
                continue;
            }
            if (Math.abs(test.position.x - this.position.x) < boids.friendRadius && Math.abs(test.position.y - this.position.y) < boids.friendRadius) {
                nearby.add(test);
            }
        }
        friends = nearby;
    }

    private float getAverageColor() {
        float total = 0;
        float count = 0;
        for (Bird other : friends) {
            if (other.shade - shade < -128) {
                total += other.shade + 255 - shade;
            } else if (other.shade - shade > 128) {
                total += other.shade - 255 - shade;
            } else {
                total += other.shade - shade;
            }
            count++;
        }
        if (count == 0) {
            return 0;
        }
        return total / (float) count;
    }

    private PVector getAverageDir() {
        PVector sum = new PVector(0, 0);
        int count = 0;

        for (Bird other : friends) {
            float d = PVector.dist(position, other.position);
            // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
            if ((d > 0) && (d < boids.friendRadius)) {
                PVector copy = other.velocity.copy();
                copy.normalize();
                copy.div(d);
                sum.add(copy);
                count++;
            }
            if (count > 0) {
                //sum.div((float)count);
            }
        }
        return sum;
    }

    private PVector getAvoidDir() {
        PVector steer = new PVector(0, 0);
        int count = 0;

        for (Bird other : friends) {
            float d = PVector.dist(position, other.position);
            // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
            if ((d > 0) && (d < boids.crowdRadius)) {
                // Calculate vector pointing away from neighbor
                PVector diff = PVector.sub(position, other.position);
                diff.normalize();
                diff.div(d);        // Weight by distance
                steer.add(diff);
                count++;            // Keep track of how many
            }
        }
        if (count > 0) {
            //steer.div((float) count);
        }
        return steer;
    }

    private PVector getAvoidAvoids() {
        PVector steer = new PVector(0, 0);
        int count = 0;

        for (Obstacle other : boids.obstacles) {
            float d = PVector.dist(position, other.position);
            // If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
            if ((d > 0) && (d < boids.avoidRadius)) {
                // Calculate vector pointing away from neighbor
                PVector diff = PVector.sub(position, other.position);
                diff.normalize();
                diff.div(d);        // Weight by distance
                steer.add(diff);
                count++;            // Keep track of how many
            }
        }
        return steer;
    }

    private PVector getCohesion() {
        PVector sum = new PVector(0, 0);   // Start with empty vector to accumulate all locations
        int count = 0;
        for (Bird other : friends) {
            float d = PVector.dist(position, other.position);
            if ((d > 0) && (d < boids.coheseRadius)) {
                sum.add(other.position); // Add location
                count++;
            }
        }
        if (count > 0) {
            sum.div(count);

            PVector desired = PVector.sub(sum, position);
            return desired.setMag((float)0.05);
        } else {
            return new PVector(0, 0);
        }
    }

    protected void drawBird() {
        for (int i = 0; i < friends.size(); i++) {
            Bird f = friends.get(i);
            sketch.stroke(90);
            //line(this.pos.x, this.pos.y, f.pos.x, f.pos.y);
        }
        sketch.noStroke();
        sketch.fill(shade, 90, 200);
        sketch.pushMatrix();
        sketch.translate(position.x, position.y);
        sketch.rotate(velocity.heading());
        sketch.beginShape();
        sketch.vertex(15 * boids.globalScale, 0);
        sketch.vertex(-7 * boids.globalScale, 7 * boids.globalScale);
        sketch.vertex(-7 * boids.globalScale, -7 * boids.globalScale);
        sketch.endShape(sketch.CLOSE);
        sketch.popMatrix();
    }

    // update all those timers!
    private void increment() {
        thinkTimer = (thinkTimer + 1) % 5;
    }

    private void wrap() {
        position.x = (position.x + sketch.width) % sketch.width;
        position.y = (position.y + sketch.height) % sketch.height;
    }
}
