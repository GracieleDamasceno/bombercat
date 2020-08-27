package factory;

import app.BombercatApp;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import component.AIComponent;
import component.BombComponent;
import component.PlayerComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;
import static entity.EntityType.*;

public class BombercatFactory implements EntityFactory {
    private static final int POINT_SIZE = 20;
    private static final int CAT_SPEED = 180;

    @Spawns("background")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(0, 0)
                .view(new Rectangle(600, 600, Color.YELLOWGREEN.desaturate()))
                .zIndex(-1)
                .build();
    }

    @Spawns("cat")
    public Entity newPlayer(SpawnData data) {
        Entity player =  entityBuilder(data)
                .at(new Point2D( BombercatApp.BRICK_SIZE,  BombercatApp.BRICK_SIZE*2))
                .type(CAT)
                .viewWithBBox(texture("cutecat2.png",  BombercatApp.BRICK_SIZE,  BombercatApp.BRICK_SIZE))
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent( BombercatApp.BRICK_SIZE,  BombercatApp.BRICK_SIZE, CAT_SPEED))
                .with(new AStarMoveComponent(FXGL.<BombercatApp>getAppCast().getGrid()))
                .with(new PlayerComponent())
                .build();
        player.setLocalAnchorFromCenter();
        return player;
    }

    @Spawns("dog")
    public Entity newBoss(SpawnData data) {
        Entity enemy =  entityBuilder(data)
                .type(DOG)
                .at(new Point2D(520,  560))
                .viewWithBBox(texture("dog.png",  BombercatApp.BRICK_SIZE,  BombercatApp.BRICK_SIZE))
                .with(new CellMoveComponent(BombercatApp.BRICK_SIZE, BombercatApp.BRICK_SIZE, 90))
                .with(new AStarMoveComponent((FXGL.<BombercatApp>getAppCast().getGrid())))
                .with(new AIComponent())
                .with(new CollidableComponent(true))
                .build();
        enemy.setLocalAnchorFromCenter();
        return enemy;
    }

    @Spawns("mouse")
    public Entity newAdversary(SpawnData data) {
        Entity enemy = entityBuilder(data)
                .type(MOUSE)
                .at(new Point2D(520,  400))
                .viewWithBBox(texture("mouse.png",  BombercatApp.BRICK_SIZE,  BombercatApp.BRICK_SIZE))
                .with(new CellMoveComponent(BombercatApp.BRICK_SIZE, BombercatApp.BRICK_SIZE, 50))
                .with(new AStarMoveComponent((FXGL.<BombercatApp>getAppCast().getGrid())))
                .with(new AIComponent().withDelay())
                .with(new CollidableComponent(true))
                .build();
        enemy.setLocalAnchorFromCenter();
        return enemy;
    }

    @Spawns("bomb")
    public Entity newWeapon(SpawnData data) {
        return entityBuilder(data)
                .type(BOMB)
                .viewWithBBox(texture("bomb.png",  BombercatApp.BRICK_SIZE,  BombercatApp.BRICK_SIZE))
                .with(new BombComponent(data.get("radius")))
                .atAnchored(new Point2D(POINT_SIZE, POINT_SIZE), new Point2D(data.getX() + BombercatApp.BRICK_SIZE / 2, data.getY() +  BombercatApp.BRICK_SIZE / 2))
                .with(new CollidableComponent(true))
                .build();

    }

    @Spawns("fire")
    public Entity newExplosion(SpawnData data) {
        return entityBuilder(data)
                .type(FIRE)
                .collidable()
                .viewWithBBox(texture("fire.png",  BombercatApp.BRICK_SIZE,  BombercatApp.BRICK_SIZE))
                .build();
    }

    @Spawns("w")
    public Entity newWall(SpawnData data) {
        return entityBuilder(data)
                .type(WALL)
                .viewWithBBox(new Rectangle(BombercatApp.BRICK_SIZE,  BombercatApp.BRICK_SIZE, Color.GRAY.saturate()))
                .build();
    }

    @Spawns("b")
    public Entity newBrick(SpawnData data) {
        return entityBuilder(data)
                .type(BRICK)
                .viewWithBBox(texture("brick.png",  BombercatApp.BRICK_SIZE,  BombercatApp.BRICK_SIZE))
                .build();
    }

    @Spawns("1")
    public Entity newBlock(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(WALL)
                .viewWithBBox(new Rectangle(40, 40, Color.BLACK))
                .build();
    }

    @Spawns("powerUpBomb")
    public Entity newPowerupBomb(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(POWER_UP_BOMB)
                .viewWithBBox(texture("powerup2.png",  BombercatApp.BRICK_SIZE,  BombercatApp.BRICK_SIZE))
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("powerUpRadius")
    public Entity powerUpRadius(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(POWER_UP_RADIUS)
                .viewWithBBox(texture("powerup1.png",  BombercatApp.BRICK_SIZE,  BombercatApp.BRICK_SIZE))
                .with(new CollidableComponent(true))
                .build();
    }

}
