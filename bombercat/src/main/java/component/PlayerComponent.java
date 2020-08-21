package component;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.CellMoveComponent;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import app.BombercatApp;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.getGameTimer;
import static com.almasb.fxgl.dsl.FXGL.spawn;

public class PlayerComponent extends Component {
    private AStarMoveComponent astar;
    private CellMoveComponent cell;
    private int bombsPlaced = 0 ;
    private int bombsMaximum = 1;
    private int radius = 40;
    private int explosionRadius = BombercatApp.BRICK_SIZE;

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
        if(bombsPlaced >= bombsMaximum){
            return;
        }
        increaseBombsPlaced();
        Entity bomb = spawn("bomb", new SpawnData(cell.getCellX() * radius, cell.getCellY() * radius).put("radius", explosionRadius / 2));
        getGameTimer().runOnceAfter(() -> {
            bomb.getComponent(BombComponent.class).explode();
            bomb.getComponent(BombComponent.class).explosionEffect();
            decreaseBombsPlaced();
            System.out.println("Bombs max: "+bombsMaximum+" bombs now: "+bombsPlaced);
            System.out.println("Radius: "+explosionRadius);
        }, Duration.seconds(2));

    }

    public void increaseBombsPlaced(){bombsPlaced++;}
    public void decreaseBombsPlaced(){bombsPlaced--;}
    public void increaseBombsMaximum(){bombsMaximum++;}
    public void increaseRadiusMaximum(){explosionRadius = explosionRadius + radius;}
}
