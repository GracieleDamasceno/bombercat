package app;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.text.TextLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.pathfinding.CellState;
import com.almasb.fxgl.pathfinding.astar.AStarGrid;
import component.PlayerComponent;
import entity.EntityType;
import factory.BombercatFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static entity.EntityType.*;


public class BombercatApp extends GameApplication{

    public static final int WIDTH = 600; //600
    public static final int HEIGHT = 640; //440
    public static final int BRICK_SIZE = 40;
    public static final int TIMER = 200;

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
        settings.setFullScreenAllowed(true);
        settings.setPreserveResizeRatio(true);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("lives", 7);
        vars.put("time", TIMER);
    }

    @Override
    protected void initUI() {
        Text scoreText = getUIFactoryService().newText("", Color.WHITE, 20);
        scoreText.setTranslateX(10);
        scoreText.setTranslateY(30);
        scoreText.textProperty().bind(getip("score").asString("Score: [%d]"));

        addUINode(scoreText);

        Text timerText = getUIFactoryService().newText("", Color.WHITE, 25);
        timerText.setTranslateX(275);
        timerText.setTranslateY(30);
        timerText.textProperty().bind(getip("time").asString("%d"));

        addUINode(timerText);

        Text livesText = getUIFactoryService().newText("", Color.WHITE, 20);
        livesText.setTranslateX(510);
        livesText.setTranslateY(30);
        livesText.textProperty().bind(getip("lives").asString("Lives: [%d]"));

        addUINode(livesText);
    }

    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                playerComponent.left();
            }
            @Override
            protected void onActionBegin() {
                FXGL.play("cat_left.wav");
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
               playerComponent.right();// loopBGM("cat_right.wav");
            }
            @Override
            protected void onActionBegin() {
                FXGL.play("cat_right.wav");
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                playerComponent.up();
            }
            @Override
            protected void onActionBegin() {
                FXGL.play("cat_left.wav");
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                playerComponent.down();
            }
            @Override
            protected void onActionBegin() {
                FXGL.play("cat_right.wav");
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

        grid  = AStarGrid.fromWorld(FXGL.getGameWorld(), 600, 600, 40, 40, type -> {
            if (type.equals(WALL) || type.equals(BRICK))
                return CellState.NOT_WALKABLE;

            return CellState.WALKABLE;
        });

        player = FXGL.spawn("cat");
        playerComponent = player.getComponent(PlayerComponent.class);
        FXGL.spawn("dog", new SpawnData(520, 560));
        FXGL.spawn("mouse", new SpawnData(520, 240));
        FXGL.spawn("mouse", new SpawnData(420, 400));
        FXGL.spawn("mouse", new SpawnData(320, 80));

        run(() -> inc("time", -1), Duration.seconds(1));
        getWorldProperties().<Integer>addListener("time", (old, now) -> {
            if (now == 0) {
                gameOver();
            }
        });
        getWorldProperties().<Integer>addListener("lives", (prev, now) -> {
            if (now <= 0) {
                gameOver();
            }
        });
    }

    private void gameOver() {
        FXGL.showMessage("YOU DIED!", () -> {
            FXGL.getGameController().startNewGame();
        });
    }

    @Override
    protected void initPhysics() {
        FXGL.onCollision(CAT, EntityType.MOUSE, (cat, mouse) -> {
            hitTaken(cat, 1);
        });

        FXGL.onCollision(CAT, EntityType.FIRE, (cat, bomb) -> {
            if (Math.abs(bomb.getPosition().getX() - cat.getPosition().getX()) < 20 &&
                    Math.abs(bomb.getPosition().getY() - cat.getPosition().getY()) < 20) {
                hitTaken(cat, 1);
            }
        });

        FXGL.onCollision(CAT, EntityType.DOG, (cat, dog) -> {
            if (Math.abs(dog.getPosition().getX() - cat.getPosition().getX()) < 20 &&
                    Math.abs(dog.getPosition().getY() - cat.getPosition().getY()) < 20) {
                hitTaken(cat, 3);
            }
        });
        FXGL.onCollision(DOG, FIRE, (dog, fire) -> {
            dog.removeFromWorld();
            FXGL.spawn("dog", new SpawnData(320, 360));
            inc("score", +5000);
        });

        FXGL.onCollision(MOUSE, FIRE, (mouse, fire) -> {
            if (Math.abs(fire.getPosition().getX() - mouse.getPosition().getX()) < 20 &&
                    Math.abs(fire.getPosition().getY() - mouse.getPosition().getY()) < 20) {
                inc("score", +1000);
                mouse.removeFromWorld();
            }

        });
        FXGL.onCollision(CAT, POWER_UP_BOMB, (cat, powerUp) -> {
            if (Math.abs(powerUp.getPosition().getX() - cat.getPosition().getX()) < 20 &&
                    Math.abs(powerUp.getPosition().getY() - cat.getPosition().getY()) < 20) {
                playerComponent.increaseBombsMaximum();
                FXGL.play("powerup1.wav");
                powerUp.removeFromWorld();
            }
        });
        FXGL.onCollision(CAT, POWER_UP_RADIUS, (cat, powerUpRadius) -> {
            if (Math.abs(powerUpRadius.getPosition().getX() - cat.getPosition().getX()) < 20 &&
                    Math.abs(powerUpRadius.getPosition().getY() - cat.getPosition().getY()) < 20) {
                playerComponent.increaseRadiusMaximum();
                FXGL.play("powerup2.wav");
                powerUpRadius.removeFromWorld();
            }
        });
    }

    private void hitTaken(Entity cat, int livesLost) {
        cat.removeFromWorld();
        this.player = FXGL.spawn("cat");
        this.playerComponent = this.player.getComponent(PlayerComponent.class);
        inc("lives", -livesLost);
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
