package factory;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

public class BombercatFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer(SpawnData data) {

        return null;
    }
}
