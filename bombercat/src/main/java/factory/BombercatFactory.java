package factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import component.PlayerComponent;
import game.BombercatApp;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;
import static entity.EntityType.*;

public class BombercatFactory implements EntityFactory {
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;

    @Spawns("BG")
    public Entity newBackground(SpawnData data) {
        return entityBuilder()
                .at(0, 0)
                .view(new Rectangle(600, 600, Color.LIGHTGREEN))
                .zIndex(-1)
                .build();
    }

    @Spawns("cat")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
                .atAnchored(new Point2D(20, 20), new Point2D(20, 20))
                .at(new Point2D(40, 40))
                .type(CAT)
                .viewWithBBox(texture("cutecat2.png", WIDTH, HEIGHT))
                .with(new CollidableComponent(true))
                .with(new CellMoveComponent(40, 40, 480))
                .with(new AStarMoveComponent(FXGL.<BombercatApp>getAppCast().getGrid()))
                .with(new PlayerComponent())
                .build();
    }

    @Spawns("mouse")
    public Entity newAdversary(SpawnData data) {
        return entityBuilder(data)
                .type(MOUSE)
                .collidable()
                .viewWithBBox(texture("mouse.png", WIDTH, HEIGHT))
                .build();
    }

    @Spawns("bomb")
    public Entity newWeapon(SpawnData data) {
        return entityBuilder(data)
                .type(BOMB)
                .collidable()
                .viewWithBBox(texture("bomb.png", WIDTH, HEIGHT))
                .build();
    }

    @Spawns("fire")
    public Entity newExplosion(SpawnData data) {
        return entityBuilder(data)
                .type(FIRE)
                .collidable()
                .viewWithBBox(texture("fire.png", WIDTH, HEIGHT))
                .build();
    }

    @Spawns("w")
    public Entity newWall(SpawnData data) {
        return entityBuilder(data)
                .type(WALL)
                .viewWithBBox(new Rectangle(WIDTH, HEIGHT, Color.GRAY.saturate()))
                .build();
    }

    @Spawns("b")
    public Entity newBrick(SpawnData data) {
        return entityBuilder(data)
                .type(BRICK)
                .viewWithBBox(new Rectangle(WIDTH, HEIGHT, Color.GRAY.saturate()))
                .build();
    }

}
