/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.laser;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.TimerPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.commonlaser.IShootLaser;
import dk.sdu.mmmi.cbse.commonlaser.Laser;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    ,
    @ServiceProvider(service = IShootLaser.class)}
)

/**
 *
 * @author chris
 */
public class LaserSystem implements IShootLaser, IEntityProcessingService {

    @Override
    public Entity createLaser(Entity shooter, GameData gameData) {
        PositionPart shooterPos = shooter.getPart(PositionPart.class);

        float x = shooterPos.getX();
        float y = shooterPos.getY();
        float radians = shooterPos.getRadians();
        float dt = gameData.getDelta();
        float speed = 350;

        Entity bullet = new Laser();
        bullet.setRadius(2);

        float bx = (float) cos(radians) * shooter.getRadius() * bullet.getRadius();
        float by = (float) sin(radians) * shooter.getRadius() * bullet.getRadius();
        
        bullet.add(new PositionPart(bx + x, by + y, radians));
        bullet.add(new MovingPart(0, 5000000, speed, 5));
        bullet.add(new TimerPart(1));
        bullet.add(new LifePart(1));

        bullet.setShapeX(new float[2]);
        bullet.setShapeY(new float[2]);

        return bullet;
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity laser : world.getEntities(Laser.class)) {

            PositionPart positionPart = laser.getPart(PositionPart.class);
            MovingPart movingPart = laser.getPart(MovingPart.class);
            TimerPart timerPart = laser.getPart(TimerPart.class);
            movingPart.setUp(true);
            
            if (timerPart.getExpiration() < 0) {
                world.removeEntity(laser);
            }
            
            movingPart.process(gameData, laser);
            positionPart.process(gameData, laser);
            timerPart.process(gameData, laser);

            setShape(laser);
        }
    }

    private void setShape(Entity laser) {
        float[] shapex = laser.getShapeX();
        float[] shapey = laser.getShapeY();
        PositionPart positionPart = laser.getPart(PositionPart.class);
        float x = positionPart.getX();
        float y = positionPart.getY();
        float radians = positionPart.getRadians();

        shapex[0] = x;
        shapey[0] = y;

        shapex[1] = (float) (x + Math.cos(radians - 4 * 3.1415f / 5));
        shapey[1] = (float) (y + Math.sin(radians - 4 * 3.1145f / 5));

        laser.setShapeX(shapex);
        laser.setShapeY(shapey);
    }
    
}
