package component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import entity.EntityType;
import app.BombercatApp;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

public class BombComponent extends Component {
    private int radius;

    public BombComponent(int radius) {
        this.radius = radius;
    }

    public void explode() {
        BoundingBoxComponent boundingBoxComponent = entity.getBoundingBoxComponent();
        getGameWorld()
                .getEntitiesInRange(boundingBoxComponent.range(radius, radius))
                .stream()
                .filter(e -> e.isType(EntityType.BRICK))
                .forEach(e -> {
                    FXGL.<BombercatApp>getAppCast().onBrickDestroyed(e);
                    e.removeFromWorld();
                });
        entity.removeFromWorld();
    }

    public void explosionEffect() {
        FXGL.play("explosion.wav");
        BoundingBoxComponent boundingBoxComponent = entity.getBoundingBoxComponent();
        getGameWorld()
                .getEntitiesInRange(boundingBoxComponent.range(radius, radius))
                .stream()
                .forEach(e -> {
                    Entity fire = FXGL.spawn("fire", e.getX(), e.getY());
                    FXGL.getGameTimer().runOnceAfter(() -> {
                        fire.removeFromWorld();
                    }, Duration.seconds(0.5));
                });
        entity.removeFromWorld();
    }

}
