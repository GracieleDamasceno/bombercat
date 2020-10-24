package component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.pathfinding.astar.AStarMoveComponent;
import entity.EntityType;

@Required(AStarMoveComponent.class)
public class AIComponent  extends Component {
    private AStarMoveComponent astar;

    private boolean isDelayed = false;

    @Override
    public void onUpdate(double tpf) {
        if (!isDelayed) {
            move();
        } else {
            if (astar.isAtDestination()) {
                move();
            }
        }
    }

    private void move() {
        var player = FXGL.getGameWorld().getSingleton(EntityType.CAT);

        int x = player.call("getCellX");
        int y = player.call("getCellY");

        astar.moveToCell(x, y);
    }

    public AIComponent withDelay() {
        isDelayed = true;
        return this;
    }
}
