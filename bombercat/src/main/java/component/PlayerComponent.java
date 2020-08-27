package component;

import app.BombercatApp;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getGameTimer;
import static com.almasb.fxgl.dsl.FXGL.spawn;

public class PlayerComponent extends Component {
    private AStarMoveComponent astar;
    private CellMoveComponent cell;
    private int bombsPlaced = 0 ;
    private int bombsMaximum = 1;
    private int explosionRadius = 40;

    public void left() {
        getEntity().setScaleX(-1);
        astar.moveToLeftCell();
    }

    public void right() {
        getEntity().setScaleX(1);
        astar.moveToRightCell();
    }

    public void up() {
        astar.moveToUpCell();
    }

    public void down() {
        astar.moveToDownCell();
    }

    public void placeBomb() {
        int radius = 40;
        if (this.bombsPlaced >= this.bombsMaximum){
            return;
        }
        increaseBombsPlaced();
        double x = cell.getCellX() * radius;
        double y = cell.getCellY() * radius;
        Entity bomb = spawn("bomb", new SpawnData(x, y).put("radius", this.explosionRadius));
        getGameTimer().runOnceAfter(() -> {
            bomb.getComponent(BombComponent.class).explode(x, y, bomb);
            decreaseBombsPlaced();
        }, Duration.seconds(2));

    }

    public void increaseBombsPlaced(){this.bombsPlaced++;}
    public void decreaseBombsPlaced(){this.bombsPlaced--;}
    public void increaseBombsMaximum(){this.bombsMaximum++;}
    public void increaseRadiusMaximum(){this.explosionRadius = this.explosionRadius + 40;}
}
