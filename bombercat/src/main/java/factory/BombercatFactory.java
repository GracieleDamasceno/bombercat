package factory;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.texture;
import static entity.EntityType.*;

public class BombercatFactory implements EntityFactory {
    private static final int WIDGTH = 40;
    private static final int HEIGHT = 40;

    @Spawns("cat")
    public Entity newPlayer(SpawnData data) {
        return entityBuilder(data)
                .type(CAT)
                .collidable()
                .viewWithBBox(texture("cutecat2.png", WIDGTH, HEIGHT))
                .build();
    }

    @Spawns("mouse")
    public Entity newAdversary(SpawnData data) {
        return entityBuilder(data)
                .type(MOUSE)
                .collidable()
                .viewWithBBox(texture("mouse.png", WIDGTH, HEIGHT))
                .build();
    }

    @Spawns("bomb")
    public Entity newWeapon(SpawnData data) {
        return entityBuilder(data)
                .type(BOMB)
                .collidable()
                .viewWithBBox(texture("bomb.png", WIDGTH, HEIGHT))
                .build();
    }

    @Spawns("wall")
    public Entity newWall(SpawnData data) {
        return entityBuilder(data)
                .type(WALL)
                .collidable()
                .viewWithBBox(texture("brick.png", WIDGTH, HEIGHT))
                .build();
    }


}
