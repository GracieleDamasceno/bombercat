package handler;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import entity.EntityType;

public class PlayerVsFireHandler extends CollisionHandler {

    public PlayerVsFireHandler() {
        super(EntityType.CAT, EntityType.FIRE);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity fire){
            player.removeFromWorld();
            fire.removeFromWorld();
            FXGL.showMessage("YOU DIED!", () -> {
                FXGL.getGameController().startNewGame();
            });
    }
}
