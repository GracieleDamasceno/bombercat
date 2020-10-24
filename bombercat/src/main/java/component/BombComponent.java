package component;

import app.BombercatApp;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellState;
import entity.EntityType;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;

//@Required(AStarMoveComponent.class)
public class BombComponent extends Component {
    private int radius;
    private boolean hasWall;
    private boolean brokeBrick;
//    private ArrayList<Double> placedBombX = new ArrayList();
//    private ArrayList<Double> placedBombY = new ArrayList();

    public BombComponent(int radius) {
        this.radius = radius;
    }

    @Override
    public void onAdded() {
        //this.getEntity().getComponent(AStarMoveComponent.class).getGrid()
        FXGL.<BombercatApp>getAppCast().getGrid()
                .get((int)this.getEntity().getX(), (int) this.getEntity().getY())
                .setState(CellState.NOT_WALKABLE);
    }

    public void explode(double x, double y, Entity bomb) {
        FXGL.play("explosion.wav");
        FXGL.getGameTimer().runOnceAfter(() -> {
            bomb.removeFromWorld();
        }, Duration.seconds(0.5));

//        this.placedBombX.add(x);
//        this.placedBombY.add(y);

        removeBrickXLeft(x,y);
        removeBrickXRight(x,y);
        removeBrickYDown(x,y);
        removeBrickYUp(x,y);
    }

//    public void explodeCollide(double x, double y, Entity bomb) {
//
//        System.out.println("Posicao bomba collide " + x + "  " + y);
//        bomb.removeFromWorld();
//        this.placedBombX.add(x);
//        this.placedBombY.add(y);
//
//        removeBrickXLeft(x,y);
//        removeBrickXRight(x,y);
//        removeBrickYDown(x,y);
//        removeBrickYUp(x,y);
//    }

//    private boolean verifyBomb(double x, double y, Entity bomb) {
//        boolean isSameBomb = false;
//        for (int i = 0; i < this.placedBombY.size(); i++) {
//            if (this.placedBombX.get(i) != x || this.placedBombY.get(i) != y) {
//                System.out.println("Valor verificado "+ this.placedBombX.get(i) + "   " + this.placedBombY.get(i));
//                System.out.println("Valor verificado 2 "+ x + "   " + y);
//                for (int j = 0; j < this.placedBombY.size(); j++) {
//                    if (i != j &&
//                            this.placedBombX.get(j) == this.placedBombX.get(i) &&
//                            this.placedBombY.get(j) == this.placedBombY.get(i)) {
//                        isSameBomb = true;
//                        break;
//                    }
//                }
//
//                if(!isSameBomb) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

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
//                if (e.isType(EntityType.BOMB)) {
//                    System.out.println("Tamanho: x " + this.placedBombX.size());
//                    if (verifyBomb(e.getX(), e.getY(), e)) {
//                        System.out.println("Haa uma bomba aqui " + e.getX() + "  " + e.getY());
//                        this.placedBombX.add(e.getX());
//                        this.placedBombY.add(e.getY());
//                        explodeCollide(e.getX(), e.getY(), e);
//                    }
//                }
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
