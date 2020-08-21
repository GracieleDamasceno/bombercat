package handler;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import component.PlayerComponent;
import entity.EntityType;

public class PlayerVsPowerUpRadius extends CollisionHandler {

    public PlayerVsPowerUpRadius() {
        super(EntityType.CAT, EntityType.POWER_UP_RADIUS);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity powerUpRadius){
        player.removeFromWorld();
        powerUpRadius.removeFromWorld();
        player.getComponent(PlayerComponent.class).increaseRadiusMaximum();
    }
}
