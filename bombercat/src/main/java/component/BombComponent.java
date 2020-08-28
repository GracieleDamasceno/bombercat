package component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import entity.EntityType;
import app.BombercatApp;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

public class BombComponent extends Component {
    private int radius;
    private boolean hasWall;
    private boolean brokeBrick;

    public BombComponent(int radius) {
        this.radius = radius;
    }

    public void explode(double x, double y, Entity bomb) {
        FXGL.play("explosion.wav");
        FXGL.getGameTimer().runOnceAfter(() -> {
            bomb.removeFromWorld();
        }, Duration.seconds(0.5));

        removeBrickXLeft(x,y);
        removeBrickXRight(x,y);
        removeBrickYDown(x,y);
        removeBrickYUp(x,y);
    }

    private void removeBrick(double x, double y) {
        getGameWorld()
            .getEntitiesAt(new Point2D(x, y))
            .stream()
            .forEach(e -> {
                if (e.isType(EntityType.WALL)) {
                    this.hasWall = true;
                    return;
                }
                if (e.isType(EntityType.BRICK)) {
                    FXGL.<BombercatApp>getAppCast().onBrickDestroyed(e);
                    e.removeFromWorld();
                    this.brokeBrick = true;
                    return;
                }
            });

        if (!this.hasWall) {
            Entity fire = FXGL.spawn("fire", x, y);
            FXGL.getGameTimer().runOnceAfter(() -> {
                fire.removeFromWorld();
            }, Duration.seconds(0.5));
        }
    }

    private void removeBrickYDown(double x, double y) {
        this.hasWall = false;
        this.brokeBrick = false;
        for (double i = y; i <= y + this.radius; i = i + 40) {
            if (i > (BombercatApp.WIDTH - 80) || this.hasWall || this.brokeBrick) {
                break;
            }

            removeBrick(x, i);
        }
    }

    private void removeBrickXLeft(double x, double y) {
        this.hasWall = false;
        this.brokeBrick = false;
        for (double i = x; i <= x + this.radius; i = i + 40) {
            if (i > (BombercatApp.WIDTH - 80) || this.hasWall || this.brokeBrick) {
                break;
            }

            removeBrick(i, y);
        }
    }

    private void removeBrickXRight(double x, double y) {
        this.hasWall = false;
        this.brokeBrick = false;
        for (double i = x; i >= x - this.radius; i = i - 40) {
            if (i < 40 || this.hasWall || this.brokeBrick) {
                break;
            }

            removeBrick(i, y);
        }
    }

    private void removeBrickYUp(double x, double y) {
        this.hasWall = false;
        this.brokeBrick = false;
        for (double i = y; i >= y - this.radius; i = i - 40) {
            if (i < 40 || this.hasWall || this.brokeBrick) {
                break;
            }

            removeBrick(x, i);
        }
    }

}
