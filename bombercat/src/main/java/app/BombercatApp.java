package app;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
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

import static entity.EntityType.*;


public class BombercatApp extends GameApplication{

    public static final int WIDTH = 600; //600
    public static final int HEIGHT = 600; //440
    public static final int BRICK_SIZE = 40;

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
        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                playerComponent.left();
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
               playerComponent.right();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                playerComponent.up();
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                playerComponent.down();
            }
        }, KeyCode.S);
        FXGL.getInput().addAction(new UserAction("Bomb") {
            @Override
            protected void onActionBegin() {
                playerComponent.placeBomb();
            }
        }, KeyCode.SPACE);

    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new BombercatFactory());

        Level level = FXGL.getAssetLoader().loadLevel("0.txt", new TextLevelLoader(40, 40, '0'));
        FXGL.getGameWorld().setLevel(level);

        FXGL.spawn("background");

        grid = AStarGrid.fromWorld(FXGL.getGameWorld(), 600, 600, 40, 40, type -> {
            if (type.equals(WALL) || type.equals(BRICK))
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });

        player = FXGL.spawn("cat");
        playerComponent = player.getComponent(PlayerComponent.class);
    }

    @Override
    protected void initPhysics() {
        FXGL.onCollision(CAT, EntityType.MOUSE, (cat, mouse) -> {
            cat.removeFromWorld();
            mouse.removeFromWorld();
            FXGL.showMessage("YOU DIED!", () -> {
                FXGL.getGameController().startNewGame();
            });
        });
        FXGL.onCollision(CAT, EntityType.FIRE, (cat, bomb) -> {
            cat.removeFromWorld();
            bomb.removeFromWorld();
            FXGL.showMessage("YOU DIED!", () -> {
                FXGL.getGameController().startNewGame();
            });
        });
        FXGL. onCollisionCollectible(CAT, POWER_UP_BOMB, powerup -> {
            playerComponent.increaseBombsMaximum();
        });
        FXGL.onCollisionCollectible(CAT, POWER_UP_RADIUS, powerup -> {
            playerComponent.increaseRadiusMaximum();
        });
    }

    public void onBrickDestroyed(Entity brick) {
        int cellX = (int)((brick.getX() + 20) / BRICK_SIZE);
        int cellY = (int)((brick.getY() + 20) / BRICK_SIZE);
        grid.get(cellX, cellY).setState(CellState.WALKABLE);

        if(FXGLMath.randomBoolean()){
            if(FXGLMath.randomBoolean()){
                FXGL.spawn("powerUpBomb", cellX * 40, cellY * 40);
                return;
            }
            FXGL.spawn("powerUpRadius", cellX * 40, cellY * 40);
        }
    }
}