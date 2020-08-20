package game;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import component.PlayerComponent;
import entity.EntityType;
import factory.BombercatFactory;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static entity.EntityType.BRICK;
import static entity.EntityType.WALL;


public class BombercatApp extends GameApplication{

    private static final int WIDTH = 600; //600
    private static final int HEIGHT = 600; //440
    private static final int BRICK_SIZE = 40;

    private AStarGrid grid;
    private PlayerComponent playerComponent;
    private Entity player;

    public AStarGrid getGrid() {
        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Bombercat");
        settings.setVersion("0.1");
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                playerComponent.left();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
               playerComponent.right();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                playerComponent.up();
            }
        }, KeyCode.W);

        getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                playerComponent.down();
            }
        }, KeyCode.S);

    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new BombercatFactory());

        Level level = getAssetLoader().loadLevel("0.txt", new TextLevelLoader(40, 40, '0'));
        getGameWorld().setLevel(level);

        spawn("BG");

        grid = AStarGrid.fromWorld(getGameWorld(), 600, 600, 40, 40, type -> {
            if (type.equals(WALL) || type.equals(BRICK))
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });

        player = spawn("cat");
        playerComponent = player.getComponent(PlayerComponent.class);
    }

    @Override
    protected void initPhysics() {
        FXGL.onCollision(EntityType.CAT, EntityType.MOUSE, (cat, mouse) -> {
            cat.removeFromWorld();
            mouse.removeFromWorld();
            FXGL.showMessage("YOU DIED!", () -> {
                getGameController().startNewGame();
            });
        });
        FXGL.onCollision(EntityType.CAT, EntityType.FIRE, (cat, bomb) -> {
            cat.removeFromWorld();
            bomb.removeFromWorld();
            FXGL.showMessage("YOU DIED!", () -> {
                getGameController().startNewGame();
            });
        });
        FXGL.onCollision(EntityType.CAT, BRICK, (cat, brick) -> {

        });
    }
}
