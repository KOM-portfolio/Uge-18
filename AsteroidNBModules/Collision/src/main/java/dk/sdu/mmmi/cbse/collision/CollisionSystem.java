/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.collision;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.commonasteroid.Asteroid;
import dk.sdu.mmmi.cbse.commonenemy.Enemy;
import dk.sdu.mmmi.cbse.commonlaser.Laser;
import dk.sdu.mmmi.cbse.commonplayer.Player;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IPostEntityProcessingService.class)}
)

/**
 *
 * @author chris
 */
public class CollisionSystem implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> asteroids = new ArrayList<>(world.getEntities(Asteroid.class));
        List<Entity> player = new ArrayList<>(world.getEntities(Player.class));
        List<Entity> enemies = new ArrayList<>(world.getEntities(Enemy.class));
        List<Entity> bullets = new ArrayList<>(world.getEntities(Laser.class));

        // Asteroid as main obj
        for (Entity e : asteroids) {
            for (Entity pl : player) {
                if (this.checkCollision(world, e, pl)) {
                    world.removeEntity(e);
                }
            }
            for (Entity en : enemies) {
                if (this.checkCollision(world, e, en)) {
                    world.removeEntity(e);
                }
            }
            for (Entity bl : bullets) {
                if(this.checkCollision(world, e, bl)){
                    world.removeEntity(bl);
                }
            }
        }
        // Player as main obj
        for (Entity pl : player) {
            for (Entity en : enemies) {
                this.checkCollision(world, pl, en);
            }
            for (Entity bl : bullets) {
                if(this.checkCollision(world, pl, bl)){
                    world.removeEntity(bl);
                }
            }
        }
        // Enemy as main obj
        for (Entity en : enemies) {
            for (Entity bl : bullets) {
                if(this.checkCollision(world, en, bl)){
                    world.removeEntity(bl);
                }
            }
        }
    }

    /**
     * @param world
     * @param mainObj The Entity for which we currently check collision for
     * @param foreignObj The Entity which we check has collided with mainObj
     */
    private boolean checkCollision(World world, Entity mainObj, Entity foreignObj) {
        PositionPart mainObjPos = mainObj.getPart(PositionPart.class);
        PositionPart foreignObjPos = foreignObj.getPart(PositionPart.class);

        // I cast to int to have more precise collision detection
        double pythagoras = Math.pow((int) mainObjPos.getX() - (int) foreignObjPos.getX(), 2) + Math.pow((int) mainObjPos.getY() - (int) foreignObjPos.getY(), 2);

        if ((int) pythagoras == 0 || ((int) foreignObjPos.getX() == (int) mainObjPos.getY() && (int) foreignObjPos.getY() == (int) mainObjPos.getY())) {
            System.out.println("Faulty collision detected.");
            return false;
        }
        // We then make use of the distance formula
        if (Math.sqrt((int) pythagoras) < foreignObj.getRadius() + mainObj.getRadius()) {
            LifePart mainObjLP = mainObj.getPart(LifePart.class);
            LifePart foreignObjLP = foreignObj.getPart(LifePart.class);

            mainObjLP.setIsHit(true);
            foreignObjLP.setIsHit(true);
            return true;
        }
        return false;
    }
    
}
