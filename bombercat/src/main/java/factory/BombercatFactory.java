package factory;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;
import static entity.EntityType.*;

public class BombercatFactory implements EntityFactory {
    private static final int WIDTH = 35;
    private static final int HEIGHT = 35;

    @Spawns("cat")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
                .type(CAT)
                .collidable()
                .viewWithBBox(texture("cutecat2.png", WIDTH, HEIGHT))
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

    @Spawns("wall")
    public Entity newWall(SpawnData data) {
        return entityBuilder(data)
                .type(WALL)
                .collidable()
                .viewWithBBox(texture("brick.png", WIDTH, HEIGHT))
                .build();
    }

    @Spawns("brick")
    public Entity newBrick(SpawnData data) {
        return entityBuilder(data)
                .type(WALL)
                .viewWithBBox(new Rectangle(40, 40, Color.GRAY.saturate()))
                .build();
    }


}
