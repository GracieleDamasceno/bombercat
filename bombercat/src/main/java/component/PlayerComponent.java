package component;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;

public class PlayerComponent extends Component {
    private AStarMoveComponent astar;

    public void left() {
        getEntity().setScaleX(-1);
        astar.moveToLeftCell();
    }

    public void right() {
        getEntity().setScaleX(1);
        astar.moveToRightCell();
    }

    public void up() {
        /*getEntity().setScaleY(-1);*/
        astar.moveToUpCell();
    }

    public void down() {
        /*getEntity().setScaleY(1);*/
        astar.moveToDownCell();
    }

}
