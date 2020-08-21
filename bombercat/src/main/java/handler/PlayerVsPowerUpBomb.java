package handler;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import component.PlayerComponent;
import entity.EntityType;

public class PlayerVsPowerUpBomb extends CollisionHandler {

    public PlayerVsPowerUpBomb() {
        super(EntityType.CAT, EntityType.POWER_UP_BOMB);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity powerUpBomb){
        player.removeFromWorld();
        powerUpBomb.removeFromWorld();
        player.getComponent(PlayerComponent.class).increaseBombsMaximum();
    }
}
