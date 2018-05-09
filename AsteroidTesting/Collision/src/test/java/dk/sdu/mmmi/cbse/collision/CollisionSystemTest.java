/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.collision;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.commonenemy.Enemy;
import dk.sdu.mmmi.cbse.commonplayer.Player;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chris
 */
public class CollisionSystemTest {
    private World world;
    private GameData gd;
    private Player player;
    private Enemy enemy;
    
    public CollisionSystemTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.gd = new GameData();
        this.world = new World();
        this.player = new Player();
        this.player.add(new PositionPart(100,100,2));
        this.player.add(new LifePart(1));
        this.player.setRadius(4);
        this.enemy = new Enemy();
        this.enemy.add(new PositionPart(100,100,2));
        this.enemy.add(new LifePart(1));
        this.enemy.setRadius(4);
    }
    
    @After
    public void tearDown() {
        this.enemy = null;
        this.gd = null;
        this.player = null;
        this.world = null;
    }

    /**
     * Test of process method, of class CollisionSystem.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        
        this.world.addEntity(this.player);
        this.world.addEntity(this.enemy);
        
        CollisionSystem instance = new CollisionSystem();
        instance.process(this.gd, this.world);
        
        System.out.println("Check player & enemy isHit");
        LifePart player_lp = this.player.getPart(LifePart.class);
        LifePart enemy_lp = this.enemy.getPart(LifePart.class);
        
        assertTrue(player_lp.isHit());
        assertTrue(enemy_lp.isHit());
    }
    
}
